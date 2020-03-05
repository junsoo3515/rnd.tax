/**
 * User: 현재호
 * Date: 2016.04.15
 * Time: 오후 3:42
 */
define('local', ['common', 'formcheck', 'jquery', 'jqGrid'], function (common, formcheck, jQuery) {

    // 리턴 스크립트 체크
    function inputCheckScript(tarID) {

        switch (tarID) {
            case 'infoPanel':
                // 입력 폼
                var pwd1 = jQuery('#wPwd0');
                var pwd2 = jQuery('#wPwd1');

                if (jQuery('#wUserID').val() === '') {
                    common.setOSXModal('아이디를 입력해 주세요.', jQuery('#wUserID'));
                    return false;
                }

                if (pwd1.val() !== '') {
                    
                    var pwdPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{8,15}$/;
                    if(!pwd1.val().match(pwdPattern)) {
                        common.setOSXModal('비밀번호는 문자, 대소문자, 숫자, 특수문자의 조합으로 8~15자리로 입력해 주세요.', jQuery('#wPwd0'));
                        return false;
                    }

                    if (pwd2.val() === '') {
                        common.setOSXModal('비밀번호확인을 입력해 주세요.', jQuery('#wPwd1'));
                        return false;
                    }
                    if(!pwd2.val().match(pwdPattern)) {
                        common.setOSXModal('비밀번호는 문자, 대소문자, 숫자, 특수문자의 조합으로 8~15자리로 입력해 주세요.', jQuery('#wPwd0'));
                        return false;
                    }
                }

                if (pwd1.val() !== pwd2.val()) {
                    common.setOSXModal('비밀번호 입력이 틀립니다, 다시입력 하세요', jQuery("#wPwd0"));
                    return false;
                }

                return formcheck.checkForm(tarID);
                break;
            default:

                return formcheck.checkForm(tarID);

                break;
        }
    }

    // 기본정보 폼 Setting
    function infoSetting() {

        // 리스트 폼에 있는 객체 입력 폼 Setting
        jQuery('#wUserID').attr("readonly", true); // 사용자 아이디(수정 모드에서 수정 못하게 비활성화)

        jQuery.when(

            jQuery.ajax({
                url: './getUserData',
                type: "POST",
                dataType: "json",
                contentType: "application/json; charset=utf-8"
            })
        ).done(function(data) {

            common.setValues({
                wUserID: data.mem_id,
                wUseFl: data.use_fl,
                wAuth: data.auth_cd,
                wNm: data.nm,
                wEmail: data.email,
                wTelOffice: data.tel_office,
                wTelHp: data.tel_hp,
                wEtc: data.etc
            });

            // 프로필 사진 Setting
            if (data.files_seq > 0) {

                fileSeq = data.files_seq;

                jQuery('#profile-image').css({
                    'background-image': "url('" + jQuery.fn.sysUrl + "/files/download/" + data.files_seq + "')",
                    'background-size': 'cover',
                    'background-position': 'center center'
                });
            }
        })
        .fail(common.ajaxError)
        .always(function() {

            jQuery.fn.loadingComplete();
            return false;
        });
    }

    // 패널 초기화
    function panelClear(isAll, objID, isListReset) {

        if (isAll === undefined) isAll = false; // 전체 reset 여부
        if (objID === undefined) objID = ''; // panelID
        if (isListReset === undefined) isListReset = true; // Master List reset 여부

        if(isAll) {
            // 모든 패널 초기화
            panelClear(false, 'infoPanel', false);

            return false;
        }

        switch(objID) {

            case 'infoPanel':
                // 기본정보 입력폼
                common.clearElement('#' + objID); // form element

                jQuery('#profile-image').removeAttr("style"); // 프로필 사진 초기화
                break;
        }
    }

    // 기본정보 수정 이벤트
    function dataSend() {
        // 로딩 시작
        jQuery.fn.loadingStart();

        var formData = jQuery('#infoPanel :input');
        var tmpData = formData.filter(":disabled").attr('disabled', false);
        var reqData = formData.serializeObject();

        tmpData.attr('disabled', true);

        // 기본 입력 폼의 값(key 변경 : vo 변수명에 맞춰서)
        reqData = common.changeKeys(reqData, [
            { k: 'wUserID', v: 'mem_id' },
            { k: 'wNm', v: 'nm' },
            { k: 'wPwd0', v: 'pwd' },
            { k: 'wEmail', v: 'email' },
            { k: 'wTelHp', v: 'tel_hp' },
            { k: 'wTelOffice', v: 'tel_office' },
            { k: 'wAuth', v: 'auth_cd' },
            { k: 'wEtc', v: 'etc' }
        ]);

        // 데이터 전송
        jQuery.when(

            jQuery.ajax({
                url: './setUserAct',
                type: "POST",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(reqData)
            })
        )
        .then(function(data) {
            // 결과에 따라 다음 이벤트 처리
            if (data > 0) {

                common.setOSXModal('사용자 정보가 성공적으로 저장하였습니다.');

                // 첨부파일 존재 시 이벤트
                if (jQuery('#files').val() !== '') {

                    if (fileSeq > 0) {
                        // 기존 첨부파일 삭제 Process(비동기 처리)
                        jQuery.ajax({
                            url: jQuery.fn.sysUrl + '/files/delete/' + fileSeq,
                            type: 'DELETE'
                        });
                    }

                    // 첨부파일 저장 Process(비동기 처리)
                    var formData = new FormData(); // HTML5 지원 되는 브라우저부터 지원

                    formData.append('rootPath', jQuery.fn.sysUrl);
                    formData.append('systemPath', 'tax');
                    formData.append('folPath', 'security');
                    formData.append('tableKey', 'COM_MEM_INFO');
                    formData.append('gubunKey', 'A');
                    formData.append('seq', jQuery('#wUserID').val());
                    formData.append("files", jQuery('input[name=files]')[0].files[0]);

                    jQuery.ajax({
                        url: jQuery.fn.sysUrl + '/files/singleUpload',
                        type: "POST",
                        processData: false,
                        contentType: false,
                        data: formData,
                        success: function(data) {

                            fileSeq = data;
                        },
                        error: function (jqXhr, textStatus, errorThrown) {
                            //통신 에러 발생시 처리
                            console.log("Error '" + jqXhr.status + "' (textStatus: '" + textStatus + "', errorThrown: '" + errorThrown + "')");
                            common.setOSXModal('첨부한 사진이 저장에 실패하셨습니다.');
                        }
                    });
                }
            } else {

                common.setOSXModal('저장이 실패하였습니다.');
            }
        })
        .fail(common.ajaxError)
        .always(function() {

            jQuery.fn.loadingComplete();
            return false;
        });

        return false;
    }

     return {
        inputCheckScript: inputCheckScript,
        setEvents: formcheck.setEvents,
        infoSetting: infoSetting,
        panelClear : panelClear,
        dataSend: dataSend
    }
});

require(['common', 'darkhand', 'local', 'jquery'], function (common, darkhand, lc, jQuery) {
    // 엔터 적용
    function enterCheck(idx) {

        if (idx === undefined) idx = 0;

        var tw = [];

        switch (idx) {
            case 0:
                tw.push({
                    chk: jQuery("#infoPanel :input"),
                    script: function() {

                        var lc = require('local');
                        return lc.inputCheckScript('infoPanel');
                    },
                    ret: "btnReg",
                    state: function() {
                        // 검색 Event 처리
                        var lc = require('local');
                        lc.dataSend();
                    }
                });
                break;
        }

        common.enterSend(tw);
    }

    // 페이지 로딩 완료 후 이벤트
    jQuery(function () {

        // 엔터키 이벤트 체크
        lc.setEvents();
        enterCheck(); // 엔터 적용

        lc.infoSetting();

        // 취소 버튼 클릭시
        jQuery('#btnCancel').on('click', function () {

            lc.panelClear(true);
        });

        // 이미지 파일 선택 시 이미지 미리보기
        jQuery.fn.uploadPreview({
            input_field: "#files",
            preview_box: "#profile-image",
            no_label: true
        });
    });
});