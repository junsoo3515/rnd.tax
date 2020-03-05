/**
 * User: 이준수
 * Date: 2017.10.30
 * Time: 오후 03:14
 */
define('local', ['common', 'formcheck', 'jqGrid.setting', 'jquery', 'bootstrap-switchery', 'slippry','bootstrap-datepicker.lang', 'jqGrid'], function (common, formcheck, jqFn, jQuery, Switchery, slippry) {

    jQuery.jgrid.defaults.width = 780;
    jQuery.jgrid.defaults.responsive = true;
    jQuery.jgrid.defaults.styleUI = 'Bootstrap';

    // 리턴 스크립트 체크
    function inputCheckScript(tarID) {

        switch (tarID) {

            case 'infoPanel':

                if(jQuery('#prc_user_input').length > 0) {

                    if (jQuery('#prc_user_input').val() != '') {

                        var userInputPattern = /[가-힣]{2,4}\/[0-9]{3}\-[0-9]{4}\-[0-9]{4}\/[가-힣|0-9]{2,10}/;

                        if (!jQuery('#prc_user_input').val().match(userInputPattern)) {


                            common.setOSXModal('이름(2~4자리, 한글 가능)/010-0000-0000/부서명(한글, 숫자 가능) 형식으로 입력해주세요.');

                            return false;
                        }
                    }
                }

                if(jQuery('#seize_user_input').length > 0) {

                    if(jQuery('#seize_user_input').val() != '') {

                        var userInputPattern = /[가-힣]{2,4}\/[0-9]{3}\-[0-9]{4}\-[0-9]{4}\/[가-힣|0-9]{2,10}/;

                        if(!jQuery('#seize_user_input').val().match(userInputPattern)) {

                            common.setOSXModal('이름(2~4자리, 한글 가능)/010-0000-0000/부서명(한글, 숫자 가능) 형식으로 입력해주세요.');

                            return false;
                        }

                    }
                }

                formcheck.checkForm(tarID);

                return processChk(prcWsChk, seizeWsChk);

                break;

            default :

                return formcheck.checkForm(tarID);

                break;
        }
    }

    // 패널 초기화
    function panelClear(isAll, objID, isListReset) {

        if (isAll === undefined) isAll = false; // 전체 reset 여부
        if (objID === undefined) objID = ''; // panelID
        if (isListReset === undefined) isListReset = true; // Master List reset 여부

        if(isAll) {
            // 모든 패널 초기화
            panelClear(false, 'wholePanel', false);
            panelClear(false, 'occurPanel', false);
            panelClear(false, 'acpcsPanel', false);
            panelClear(false, 'endPanel', false);
            panelClear(false, 'infoPanel', false);

            return false;
        }

        switch(objID) {
            case 'infoPanel':
                // 상세정보 입력폼
                common.clearElement('#' + objID); // form element

                if(jQuery('#myTabs li:eq(0)').hasClass('active') === true) {
                    // 처음 탭일 경우(하단에 shown 이벤트가 안됨)
                    jQuery('#btnSave').attr('disabled', (authCrud.MOD_FL === 'Y') ? false : true); // 적용버튼 Style 변경
                }

                changeSwitchery('#prc_fl', false);
                changeSwitchery('#seize_fl', false);

                var rowid = jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getGridParam', 'selrow');

                if (rowid !== null && isListReset) {

                    jQuery('#sitPcs' + tabIdx + 'List').jqGrid('resetSelection'); // Grid Select Reset 처리
                }

                break;

            case 'wholePanel':
                // 상황처리목록(전체) Panel
                jQuery('#sitPcs0List').jqGrid('clearGridData'); // 상황처리(전체) 목록 그리드 초기화
                break;

            case 'occurPanel':
                // 상황처리목록(발생) Panel
                jQuery('#sitPcs1List').jqGrid('clearGridData'); // 상황처리(발생) 목록 그리드 초기화
                break;

            case 'acpcsPanel':
                // 상황처리목록(접수/처리) Panel
                jQuery('#sitPcs2List').jqGrid('clearGridData'); // 상황처리(접수/처리) 목록 그리드 초기화
                break;

            case 'endPanel':
                // 상황처리목록(종료) Panel
                jQuery('#sitPcs3List').jqGrid('clearGridData'); // 상황처리(종료) 목록 그리드 초기화
                break;
        }
    }

    //jqGrid data 리프레쉬
    function dataReload(tarID) {

        if (authCrud.READ_FL === 'N') {
            // 조회 권한이 없으면 데이터 조회 실패
            return false;
        }
        if (authCrud.MOD_FL === 'N') {
            // 수정 권한이 없으면 승인여부, 영치여부 적용 실패
            return false;
        }

        var listID = tarID + "List";
        var dataGrid = jQuery("#" + listID);
        var filterData = {};

        var jqOpt = {};

        switch(listID) {



            case 'sitPcs' + tabIdx + 'List' :
                // 상황처리(전체) 목록
                jqOpt = {

                    url: './getSitPcsList'
                };

                filterData = jQuery('#srcPanel :input').serializeObject();

                filterData.sit_type = 'whole';
                if(tabIdx == '1') filterData.sit_type = 'occur';
                else if(tabIdx == '2') filterData.sit_type = 'acpcs';
                else if(tabIdx == '3') filterData.sit_type = 'end';

                break;

            case 'sitPpgList' :
                // 상황전파 목록
                jqOpt = {

                    url: './getPcsSitPpgList'
                };

                var rowid = jQuery('#taxCarFindLocList').jqGrid('getGridParam', 'selrow');

                filterData = jQuery('#srcPanel :input').serializeObject();

                rowid !== null ? filterData.tri_seq = jQuery('#taxCarFindLocList').jqGrid('getRowData',rowid).tri_seq : filterData.tri_seq = '';


                break;

            case 'taxInfoList' :
                // 체납정보 목록
                jqOpt = {

                    url: './getTaxInfoList'
                };

                var rowid = jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getGridParam','selrow');

                filterData = jQuery('#srcPanel :input').serializeObject();

                rowid !== null ? filterData.car_no = jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getRowData',rowid).car_no : filterData.car_no = '';
                rowid !== null ? filterData.make_type_cd = jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getRowData',rowid).make_type_cd : filterData.make_type_cd = '';

                break;

            case 'taxCarFindLocList' :
                // 체납차량 발견위치 목록
                jqOpt = {

                    url: './getTaxCarFindLocList'
                };

                var rowid = jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getGridParam','selrow');

                filterData = jQuery('#srcPanel :input').serializeObject();

                rowid !== null ? filterData.car_no = jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getRowData', rowid).car_no : filterData.car_no = '';

                break;
        }

        dataGrid
            .jqGrid("setGridParam", jQuery.extend(true, {
                search: true,
                postData: {
                    filters: JSON.stringify(filterData)
                }
            }, jqOpt))
            .trigger("reloadGrid", [{page:1}]);
    }

    // 상세정보 적용 이벤트
    function dataSend() {
        // 로딩 시작
        jQuery.fn.loadingStart();

        // 적용 모드
        if (authCrud.MOD_FL === 'N') return false;

        var rowid = jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getGridParam','selrow');
        var formData = jQuery('#infoPanel :input');
        var reqData = formData.serializeObject();

        // 기본 입력 폼의 값(key 변경 : vo 변수명에 맞춰서)
        reqData = common.changeKeys(reqData, [
            { k: 'seize_dt', v: 'seize_dt' },
            { k: 'prc_cont', v: 'prc_cont' }
        ]);
        (jQuery('#prc_user_input').length > 0) ? reqData.prc_user_seq = managerInsert(jQuery('#prc_user_input').val()) : reqData.prc_user_seq = jQuery('#prc_user_nm option:selected').val();
        (jQuery('#seize_user_input').length > 0) ? reqData.seize_user_seq = managerInsert(jQuery('#seize_user_input').val()) : reqData.seize_user_seq = jQuery('#seize_user_nm option:selected').val();


        reqData.prc_fl = jQuery("input:checkbox[id='prc_fl']").is(":checked") === true ? 'Y' : 'N';
        reqData.seize_fl = jQuery("input:checkbox[id='seize_fl']").is(":checked") === true ? 'Y' : 'N';
        rowid === null ? reqData.car_no = '' : reqData.car_no = jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getRowData', rowid).car_no ; // 상황처리 선택 행 차량 번호
        rowid === null ? reqData.state = '' : reqData.state = jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getRowData', rowid).state ; // 상황처리 선택 행 상태
        rowid === null ? reqData.tri_seq = '' : reqData.tri_seq = jQuery('#sitPcs' + tabIdx + 'List' ).getRowData(rowid).tri_seq; // 상황처리 고유 번호



        // 데이터 전송
        jQuery.when(

            jQuery.ajax({
                url: './setDetailInfoAct',
                type: "POST",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(reqData)
            })
        )
            .then(function(data) {
                // 결과에 따라 다음 이벤트 처리
                if (data !== null) {

                    common.setOSXModal('변경사항을 성공적으로 저장하였습니다.');
                    panelClear(true);
                    dataReload('sitPcs' + tabIdx);
                    sitPcsDataCnt();

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

    //레이아웃 변경 시 사이즈 조절 리턴 함수
    function resizePanel(id) {

        if (id === undefined) {

            id = null;
        }

        if (id !== null) {

            jQuery.each([{ list: id + "List", panel: id + "Panel" }], function (sIdx, data) {

                jQuery("#" + data["list"]).jqGrid('setGridWidth', jQuery("#" + data["panel"]).width() - 2);
            });
        }
    }

    // 상세정보 입력 폼 Setting
    function infoSetting(rowID, obj) {

        panelClear(false, 'infoPanel', false);

        jQuery('#sitPcsInfoForm, #taxInfoForm, #prcForm').css({
            marginBottom: '0px'
        });

        if (rowID) {

            jQuery.when(
                jQuery.ajax({
                    url: './getDetailInfoData',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify({
                        car_no: jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getRowData', rowID).car_no,
                        tri_seq: jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getRowData', rowID).tri_seq,
                        state: jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getRowData', rowID).state
                    })
                })
            ).then(function(data) {

                    // 리턴 결과 값을 가지고 입력 값 Setting
                    // 상세 정보
                    common.setValues({
                        car_no: data.car_no,
                        prc_fl: data.prc_fl,
                        prc_user_nm: data.prc_user_seq,
                        seize_fl: data.seize_fl,
                        seize_user_nm: data.seize_user_seq,
                        seize_dt: data.seize_dt,
                        prc_cont: data.prc_cont,
                        tot_money_cnt: data.tot_money_cnt
                    });
                    var files = data.files;

                    jQuery('.sy-box').replaceWith('<ul id="photo"></ul>');

                    taxCarPhoto = undefined;

                    if(files.length > 0) {

                        for(var i = 0; i<files.length; i++) {

                            if(files[i].real_seq != '사진없음'){

                                jQuery('#photo').append('<li><a href="#slide' + (i+1) +'"><img class="' + files[i].tri_seq + '" src="' + jQuery.fn.sysUrl +'/res/assets/img/carno/' + files[i].files_seq+'.jpg" style="height:399px;"></a></li>');
                            }
                            else {

                                jQuery('#photo').append('<li><a href="#slide' + (i+1) +'"><img class="' + files[i].tri_seq + '"><div style="height: 399px;"></div></img></a></li>');
                            }
                        }

                        taxCarPhoto = jQuery('#photo').slippry({
                            loop: false,
                            auto: false,
                            onSlideAfter: function (el, index_old, index_new) {
                                var rowid = jQuery('#photo li:eq(' + index_new + ') img').attr('class');
                                jQuery('#taxCarFindLocList').jqGrid('setSelection', rowid);
                            }
                        });


                    }

                    return data;
                }).then(function(data) {

                    changeSwitchery('#prc_fl', data.prc_fl === 'Y' ? true : false);
                    changeSwitchery('#seize_fl', data.seize_fl === 'Y' ? true : false);

                }).done(function() {
                })
                .fail(common.ajaxError)
                .always(function() {

                    return false;
                });
        }
    }

    // 사용하는 jqGrid Setting
    function gridSetting(tarID) {

        var listID = tarID + "List";
        var pageID = tarID + 'Pager';

        var dataGrid = jQuery("#" + listID);

        var jqDefault = {
            url: '',
            datatype: 'json',
            mtype: 'POST',
            sortname: '', // 소팅 인자
            sortorder: '', // 처음 소팅
            pager : pageID,
            rowNum: 30,                 // 처음에 로드될 때 표출되는 row 수
            rowList: [], // row 갯수 표출 세팅
            viewsortcols: [true, 'vertical', true], // 소팅 인자 세팅
            rownumbers: true,           // Grid의 RowNumber 표출
            viewrecords: true,          // 우측 View 1-4 Text 표출 부분
            gridview: true,             // Grid Alert
            autowidth: true,            // width 자동 맞춤
            shrinkToFit: true,          // width에 맞춰 Cell Width 자동 설정
            height: 302,                // 세로 크기
            caption: '',                // 캡션 명(없으면 표출 안됨)
            beforeRequest : function() {
                // POST 보내기 전 이벤트
                if (jQuery(this).jqGrid('getGridParam', 'url') === '') return false;
            },
            gridComplete: function () {

                jQuery('#' + pageID + ' .ui-pg-input').attr('readonly', true);
            }
        };
        var jqOpt = {};

        switch(listID) {
            case 'sitPcs' + tarID.replace(/[^0-9]/g, '') + 'List' :
                // 상황처리 목록
                jqOpt = {
                    url: '',
                    height: 200,
                    scroll: 1,
                    rowList: [10, 30, 50, 100],
                    colNames: ['','', '', '발생유형', '차량번호', '발생장소', '발생일시' , '상태'],
                    colModel: [
                        {name: 'tri_seq', index: 'tri_seq', hidden: true},
                        {name: 'tci_seq', index: 'tci_seq', hidden: true},
                        {name: 'make_type_cd', index: 'make_type_cd', hidden: true},
                        {name: 'nm_cnt', index: 'nm_cnt', width: 1, sortable: false},
                        {name: 'car_no', index: 'car_no', width: 1, align: "center", sortable: false},
                        {name: 'cctv_nm', index: 'cctv_nm', width: 2, align: "center", sortable: false},
                        {name: 'reg_dts', index: 'reg_dts', width: 1, align: "center"},
                        {name: 'state', index: 'state', width: 1, align: "center", sortable: false}
                    ],
                    onInitGrid: function() {

                        dataReload(tarID)
                    },
                    onSelectRow: function (id, status, event) {
                        var ret = dataGrid.jqGrid('getRowData', id);
                        // 행 선택 시
                        if (id && id !== sitPcsSel) {

                            if (sitPcsSel !== undefined) {
                                jQuery(this).jqGrid('restoreRow', sitPcsSel);
                                jqFn.jqGridListIcon(this.id, sitPcsSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + sitPcsSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            sitPcsSel = id;

                            // 상세정보 폼, 상황전파 목록, 체납정보 목록 갱신
                            infoSetting(id, ret);
                            if (jQuery('#prc_user_btn').text() == '선택') {
                                jQuery('#prc_user_btn').text('직접입력');
                                jQuery('#prc_user_input').replaceWith('<select id="prc_user_nm" name="prc_user_nm" class="form-control" style="width: 80%; display: inline;"/>');
                                common.setSelectOpt(jQuery('#prc_user_nm'), '-선택-', managerList);
                                prcWsChk = 'prc_user_nm';
                            }
                            if (jQuery('#seize_user_btn').text() == '선택') {
                                jQuery('#seize_user_btn').text('직접입력');
                                jQuery('#seize_user_input').replaceWith('<select id="seize_user_nm" name="seize_user_nm" class="form-control" style="width: 80%; display: inline;"/>');
                                common.setSelectOpt(jQuery('#seize_user_nm'), '-선택-', managerList);
                                seizeWsChk = 'seize_user_nm';
                            }

                            dataReload('taxInfo'); // 체납정보 목록 조회
                            dataReload('taxCarFindLoc'); // 체납차량 발견위치 목록 조회
                            jQuery('#btnSave').attr('disabled', false);
                        }
                    },
                    loadComplete: function (data) {
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(

                            resizePanel(tarID) // 브라우저 창 크기 변경 시 grid 크기 자동 적용
                        ).then(function () {
                                // 데이터가 존재 할 경우 처음 행 선택
                                if (data.rows.length > 0) {
                                    dataGrid.jqGrid('setSelection', dataGrid.find('tr[id]:eq(0)').attr('id'));
                                }

                            }).always(function () {

                                jQuery.fn.loadingComplete();
                                return false;
                            });
                    }
                };

                break;

            case 'sitPpgList' :
                // 상황전파 목록
                jqOpt = {
                    url: '',
                    height: 100,
                    scroll: 1,
                    rowList: [10, 30, 50, 100],
                    colNames: ['', 'SMS전송그룹', '일시', '전파내용', '단말건수'],
                    colModel: [
                        {name: 'tsp_seq', index: 'tsp_seq', hidden: true},
                        {name: 'grp_nm', index: 'grp_nm', width: 2, align: "center", sortable: false},
                        {name: 'reg_dts', index: 'reg_dts', width: 1, align: "center"},
                        {name: 'sms_conts', index: 'sms_conts', width: 1, align: "center"},
                        {name: 'con_ct', index: 'con_ct', width: 2, align: "center"}
                    ],
                    onInitGrid: function() {

                    },
                    onSelectRow: function (id, status, event) {
                    },
                    loadComplete: function (data) {
                        // 그리드에 모든 데이터 로딩 완료 후
                    }
                };

                break;

            case 'taxInfoList' :
                // 체납정보 목록
                jqOpt = {
                    url: '',
                    height: 130,
                    scroll: 1,
                    rowList: [10, 30, 50, 100],
                    colNames: ['', '발생유형', '일자', '금액(건수)'],
                    colModel: [
                        {name: 'tci_seq', index: 'tri_seq', hidden: true},
                        {name: 'make_type_cd_nm', index: 'make_type_cd_nm', width: 2, align: "center", sortable: false},
                        {name: 'reg_dts', index: 'reg_dts', width: 1, align: "center"},
                        {name: 'tax_money_cnt', index: 'tax_money_cnt', width: 2, align: "center"}
                    ],
                    onInitGrid: function() {

                    },
                    onSelectRow: function (id, status, event) {
                    },
                    loadComplete: function (data) {
                        // 그리드에 모든 데이터 로딩 완료 후
                    }
                };

                break;

            case 'taxCarFindLocList' :
                // 체납차량 발견위치 목록
                jqOpt = {
                    url: '',
                    height: 220,
                    scroll: 1,
                    rowList: [10, 30, 50, 100],
                    colNames: ['', '', '', 'CCTV ID', '위치', '발생일시'],
                    colModel: [
                        {name: 'photo_exists', index: 'photo_exists', hidden: true},
                        {name: 'photo_seq', index: 'photo_seq', hidden: true},
                        {name: 'tri_seq', index: 'tri_seq', hidden: true, key:true},
                        {name: 'cctv_id', index: 'make_type_cd_nm', width: 1, align: "center", sortable: false},
                        {name: 'cctv_adres', index: 'cctv_adres', width: 1, align: "center", sortable: false},
                        {name: 'reg_dts', index: 'reg_dts', width: 1, align: "center", sortable: false}
                    ],
                    onInitGrid: function() {

                    },
                    onSelectRow: function (id, status, event) {

                        if (id && id !== taxCarFindLocSel) {

                            if (taxCarFindLocSel !== undefined) {

                                jQuery(this).jqGrid('restoreRow', taxCarFindLocSel);
                                jqFn.jqGridListIcon(this.id, taxCarFindLocSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + taxCarFindLocSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            taxCarFindLocSel = id;

                            var slideNum = jQuery('#taxCarFindLocList').jqGrid('getRowData', id).photo_seq;
                            var photoExists = jQuery('#taxCarFindLocList').jqGrid('getRowData', id).photo_exists;

                            if((taxCarPhoto != undefined) && (taxCarPhoto.getSlideCount() >= slideNum)) {

                                taxCarPhoto.goToSlide(slideNum);
                            }

                            if(photoExists == 0) {

                                common.setOSXModal('선택한 체납차량 발생위치의 사진이 없습니다.');
                            }

                        }


                        dataReload('sitPpg'); // 상황전파 목록 조회
                    },
                    loadComplete: function (data) {
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(

                        ).then(function () {
                                // 데이터가 존재 할 경우 처음 행 선택
                                if (data.rows.length > 0) {
                                    dataGrid.jqGrid('setSelection', dataGrid.find('tr[id]:eq(0)').attr('id'));
                                }
                            }).always(function () {

                                jQuery.fn.loadingComplete();
                                return false;
                            });
                    }
                };
                break;

        }

        dataGrid
            .jqGrid(jQuery.extend(true, jqDefault, jqOpt))
            .jqGrid('navGrid', '#' + pageID,
            {edit: false, add: false, del: false, search: false, refresh: false}, // options
            {}, // edit options
            {}, // add options
            {}, // del options
            {width: '100%'}, // search options
            {} // view options
        );

        return false;
    }

    function sitPcsDataCnt() {

        var src = jQuery('#srcPanel :input').serializeObject();

        jQuery.when(
            jQuery.ajax({
                url: './getSitPcsDataCnt',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(src)
            })
        ).then(function(data) {
                jQuery('#whole').html(data.whole_cnt);
                jQuery('#occur').html(data.occur_cnt);
                jQuery('#acpcs').html(data.acpcs_cnt);
                jQuery('#end').html(data.end_cnt);
            }).fail(common.ajaxError)
            .always(function() {
                return false;
            });
    }

    function sitPcsDataOper() {
        if(jQuery('#prc_fl').is(':checked') == true || (jQuery('#prc_fl').is(':checked' == true && jQuery('#prc_user_nm option:selected').val() != ''))){
            jQuery('#occur').text(parseInt(jQuery('#occur').text()) - 1);
            jQuery('#acpcs').text(parseInt(jQuery('#acpcs').text()) + 1);
        }
        if((jQuery('#prc_fl').is(':checked') == true || (jQuery('#prc_fl').is(':checked' == true && jQuery('#prc_user_nm option:selected').val() != ''))) && jQuery('#seize_fl').is(':checked') == true && jQuery('#seize_user_nm option:selected').val() != ''){
            jQuery('#occur').text(parseInt(jQuery('#occur').text()) - 1);
            jQuery('#end').text(parseInt(jQuery('#end').text()) + 1);
        }
        if(jQuery('#seize_fl').is(':checked') == true && jQuery('#seize_user_nm option:selected').val() != ''){
            jQuery('#acpcs').text(parseInt(jQuery('#acpcs').text()) - 1);
            jQuery('#end').text(parseInt(jQuery('#end').text()) + 1);
        }
    }

    function changeSwitchery(element, checked) {
        var switchery;
        var defaults = {
            color             : '#64bd63'
            , secondaryColor    : '#dfdfdf'
            , jackColor         : '#fff'
            , jackSecondaryColor: null
            , className         : 'switchery'
            , disabled          : false
            , disabledOpacity   : 0.5
            , speed             : '0.1s'
            , size              : 'default'
        };

        elem = document.querySelector(element);


        elem.checked = checked;

        if(jQuery('#sitPcs' + tabIdx + 'List').jqGrid('getGridParam','selrow') == null) {

            jQuery(element).siblings().remove();

            switchery = new Switchery(elem, defaults);

            jQuery('.switchery, .switchery small').css({
                height: '23px'
            });

            return switchery;
        }

        jQuery(element).siblings().remove();

        switchery = new Switchery(elem, defaults);

        jQuery('.switchery, .switchery small').css({
            height: '23px'
        });

        return switchery;

    }

    // 직접입력시 담당자 중복 확인
    function chkDupManager(id) {

        jQuery('#' + id).focusout(function () {

            if(nmArr.indexOf(jQuery('#' + id).val()) != -1) {

                var html = '';

                for(var i = 0; i< nmArr.length; i++) {

                    if(nmArr[i] == jQuery('#' + id).val()) {

                        html += '\n( ' + nmArr[i] + ' / ' + hpArr[i] + ' / ' + partNmArr[i] +' )';
                    }
                }

                var input = confirm('담당자 이름중에' + html + '\n이 이미 존재 합니다. 계속하시겠습니까?');

                if (input) {
                } else {

                    jQuery('#' + id).val('');
                }
            }
        });
    }

    // 기존에 등록 되어있는 담당자 가져오기
    function chkDbDupManager() {
        jQuery.when(
            jQuery.ajax({
                url: './getDupChkManagerList',
                type: 'POST',
                contentType: 'application/json; charset=utf-8'
            })
        ).then(function(data) {
                seqArr = [];
                nmArr = [];
                hpArr = [];
                partNmArr = [];

                if(data != null) {

                    for (var i = 0; i < data.length; i++) {

                        seqArr.push(data[i].cm_seq);
                        nmArr.push(data[i].nm);
                        hpArr.push(data[i].hp);
                        partNmArr.push(data[i].part_nm);
                    }
                }

            }).fail(common.ajaxError)
            .always(function() {
                return false;
            });
    }

    function managerInsert(elementVal) {

        if(elementVal != '') {
            var valArr = elementVal.split('/');
            var hp = '';
            var part_nm = '';
            var obj = {};


            if (valArr[1] != undefined || valArr[2] != undefined) {

                hp = valArr[1];
                part_nm = valArr[2];
            }

            jQuery.when(
                jQuery.ajax({
                    url: './setManagerInsert',
                    type: 'POST',
                    async: false,
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',
                    data: JSON.stringify({
                        nm: valArr[0],
                        hp: hp,
                        part_nm: part_nm
                    })
                })
            ).then(function (data) {

                    if (data.tmpOk == 0) {
                        return false;
                    }

                    obj.id = data.seq;
                    obj.val = valArr[0] + ' / ' + hp + ' / ' + part_nm;
                    obj.text = null;
                    obj.text2 = null;

                    managerList.push(obj);

                }).fail(common.ajaxError)
                .always(function () {
                    return false;
                });

            return obj.id;
        }

        if(elementVal == '') {
            return '';
        }
    }

    // 상황처리 유효성 체크
    function processChk() {

        var prc = 'prc_user_nm';
        var seize = 'seize_user_nm';

        if (jQuery('#prc_user_input').length > 0) prc = 'prc_user_input'
        if (jQuery('#seize_user_input').length > 0) seize = 'seize_user_input'

        var idPrc = jQuery('#' + prc);
        var idSeize = jQuery('#' + seize);
        var prcFl = jQuery('#prc_fl');
        var prcCont = jQuery('#prc_cont');
        var seizeFl = jQuery('#seize_fl');
        var seizeDt = jQuery('#seize_dt');

        if (prcFl.is(':checked') != true && idPrc.val() != '') {

            common.setOSXModal('승인여부를 선택하세요.');

            return false;
        }

        if (!(prcFl.is(':checked') == true && idPrc.val() != '') && (seizeFl.is(':checked') == true || idSeize.val() != '' || seizeDt.val() != '')) {

            common.setOSXModal('먼저 승인처리를 확인하세요.');

            return false;
        }

        if ((prcFl.is(':checked') == true && idPrc.val() != '') && !(seizeFl.is(':checked') == true && idSeize.val() != '' && seizeDt.val() != '' && seizeDt.val().length == 10) && !(seizeFl.is(':checked') != true && idSeize.val() == '' && seizeDt.val() == '')) {

            common.setOSXModal('영치결과 여부 선택 및 영치일, 처리내용, 영치 담당자를 확인하세요.');

            return false;
        }
    }

    return {
        inputCheckScript: inputCheckScript,
        setEvents: formcheck.setEvents,
        panelClear: panelClear,
        dataReload: dataReload,
        dataSend: dataSend,
        resizePanel: resizePanel,
        infoSetting: infoSetting,
        gridSetting: gridSetting,
        sitPcsDataCnt: sitPcsDataCnt,
        sitPcsDataOper: sitPcsDataOper,
        changeSwitchery: changeSwitchery,
        chkDupManager: chkDupManager,
        chkDbDupManager: chkDbDupManager
    }
});

require(['common', 'darkhand', 'local', 'bootstrap-datetimepicker', 'jquery', 'bootstrap-switchery'], function (common, darkhand, lc, datetimepicker, jQuery, Switchery) {

    // 엔터 적용
    function enterCheck(idx) {

        if (idx === undefined) idx = 0;

        var tw = [];

        switch (idx) {
            case 0:

                tw.push({
                    chk: jQuery("#srcPanel :input"),
                    script: function() {

                        var lc = require('local');
                        return lc.inputCheckScript('srcPanel');
                    },
                    ret: "btnSrch",
                    state: function() {

                        var lc = require('local');

                        jQuery.fn.loadingStart();
                        //
                        lc.panelClear(true); // 전체 폼 초기화
                        lc.dataReload('sitPcs' + tabIdx); // 상황처리(전체) 그리드 목록 리로드
                        lc.sitPcsDataCnt();

                        jQuery.fn.loadingComplete();
                    }
                });

                if(authCrud.MOD_FL === 'Y') {

                    tw.push({
                        chk: jQuery("#infoPanel :input"),
                        script: function() {

                            var lc = require('local');
                            return lc.inputCheckScript('infoPanel');
                        },
                        ret: "btnSave",
                        state: function() {
                            // 적용 Event 처리
                            var lc = require('local');
                            lc.dataSend();
                            lc.sitPcsDataOper();
                            lc.chkDbDupManager();

                            jQuery.fn.loadingComplete();
                        }
                    });
                }
                break;
        }

        common.enterSend(tw);
    }

    // 페이지 로딩 완료 후 이벤트
    jQuery(function () {
        // 권한에 따른 버튼 비활성화
        if (authCrud.READ_FL === 'N') {

            jQuery('#btnSrch').attr('disabled', true);
        }
        if(authCrud.MOD_FL === 'N') {

            jQuery('#btnSave').attr('disabled', true);
        }

        jQuery('#photoDiv').css({

            marginBottom: '40px'
        });

        jQuery('#sitPcsInfoForm, #taxInfoForm, #prcForm').css({
            marginBottom: '0px'
        });


        // 엔터키 이벤트 체크
        lc.setEvents();
        enterCheck(); // 엔터 적용
        lc.chkDbDupManager(); // 중복확인을 위한 전체 담당자 가져오기


        // 검색 폼
        common.setSelectOpt(jQuery('#srcMakeType'), '-전체-', makeTypeList); // 발생유형

        // 상세정보 폼
        common.setSelectOpt(jQuery('#prc_user_nm'), '-선택-', managerList); // 승인처리 담당자
        common.setSelectOpt(jQuery('#seize_user_nm'), '-선택-', managerList); // 영치결과 담당자

        // 영치일자 포커스 하이픈 삭제
        jQuery('#seize_dt').click(function () {

            var seizeDtVal = jQuery('#seize_dt').val();

            jQuery('#seize_dt').val(seizeDtVal.replace(/-/gi, ''));
        });

        // 영치일자 포커스 아웃 하이픈 추가
        jQuery('#seize_dt').blur(function () {

            var seizeDtVal = jQuery('#seize_dt').val();
            var resSeizeDtVal = '';
            var seizeDtValLength = seizeDtVal.length;

            if (seizeDtValLength <= 4) {

                return jQuery('#seize_dt').val(seizeDtVal);
            }

            if (seizeDtValLength > 4 && seizeDtValLength < 7) {

                resSeizeDtVal += seizeDtVal.substr(0, 4);
                resSeizeDtVal += '-'
                resSeizeDtVal += seizeDtVal.substr(4);

                return jQuery('#seize_dt').val(resSeizeDtVal);
            }
            if (seizeDtValLength >= 7) {

                resSeizeDtVal += seizeDtVal.substr(0, 4);
                resSeizeDtVal += '-'
                resSeizeDtVal += seizeDtVal.substr(4, 2);
                resSeizeDtVal += '-'
                resSeizeDtVal += seizeDtVal.substr(6);

                return jQuery('#seize_dt').val(resSeizeDtVal);
            }
        });

        // 승인처리 직접입력 버튼 클릭 이벤트
        jQuery('#prc_user_btn').on('click', function() {
            if(jQuery('#prc_user_btn').text() == '직접입력'){
                prev_prc_user = jQuery('#prc_user_nm').val();
                jQuery('#prc_user_btn').text('선택');
                jQuery('#prc_user_nm').replaceWith('<input type="text" id="prc_user_input" placeholder="이름 / 전화번호 / 부서명 형식으로 입력해주세요." class="form-control" style="width: 80%; display: inline;">');
                lc.chkDupManager('prc_user_input');
                prcWsChk = 'prc_user_input';
            }
            else if(jQuery('#prc_user_btn').text() == '선택'){
                jQuery('#prc_user_btn').text('직접입력');
                jQuery('#prc_user_input').replaceWith('<select id="prc_user_nm" name="prc_user_nm" class="form-control" style="width: 80%; display: inline;"/>');
                common.setSelectOpt(jQuery('#prc_user_nm'), '-선택-', managerList);
                jQuery('#prc_user_nm').val(prev_prc_user);
                prcWsChk = 'prc_user_nm';
            }
        });

        // 영치결과 직접입력 버튼 클릭 이벤트
        jQuery('#seize_user_btn').on('click', function() {
            if(jQuery('#seize_user_btn').text() == '직접입력'){
                prev_seize_user = jQuery('#seize_user_nm').val();
                jQuery('#seize_user_btn').text('선택');
                jQuery('#seize_user_nm').replaceWith('<input type="text" id="seize_user_input" placeholder="이름 / 전화번호 / 부서명 형식으로 입력해주세요." class="form-control" style="width: 80%; display: inline;">');
                lc.chkDupManager('seize_user_input');
                seizeWsChk = 'seize_user_input';

            }
            else if(jQuery('#seize_user_btn').text() == '선택'){
                jQuery('#seize_user_btn').text('직접입력');
                jQuery('#seize_user_input').replaceWith('<select id="seize_user_nm" name="seize_user_nm" class="form-control" style="width: 80%; display: inline;"/>');
                common.setSelectOpt(jQuery('#seize_user_nm'), '-선택-', managerList);
                jQuery('#seize_user_nm').val(prev_seize_user);
                seizeWsChk = 'seize_user_nm';
            }
        });

        // 스위치 버튼
        lc.changeSwitchery('#prc_fl', false);
        lc.changeSwitchery('#seize_fl', false);

        // 날짜 타입 유형
        var nowTemp = new Date();

        jQuery('.input-daterange').datepicker({
            language: 'kr',
            format: 'yyyy-mm-dd',
            todayHighlight: true,
            endDate: new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0),
            todayBtn: "linked"
        }).find('input').each(function() {

            var nowVal = common.nowDate('-');

            switch (this.id) {
                case 'srcSDate':
                    nowVal = common.termDate(nowVal, 'm', -1, '-');
                    break;
            }

            jQuery(this).datepicker('update', nowVal).trigger('changeDate');
        });

        var tabObj = jQuery('#myTabs a[data-toggle="tab"]');

        jQuery('#myTabs a:first').tab('show'); // 처음 탭으로 강제 이동

        // 탭 show trigger 이벤트
        tabObj.off('shown.bs.tab').on('shown.bs.tab', function (e) {
            // 해당 계통의 펌프현황 정보 가져오기(헤더 설정을 위한)
            tabIdx = tabObj.index(e.target);

            switch(tabIdx) {
                case 0:
                    // 상황처리(전체) 탭

                    lc.dataReload('sitPcs0'); // 상황처리(전체) 목록 생성

                    break;

                case 1:
                    // 상황처리(발생) 탭

                    lc.dataReload('sitPcs1'); // 상황처리(발생) 목록 생성

                    break;

                case 2:
                    // 상황처리(접수/처리) 탭

                    lc.dataReload('sitPcs2'); // 상황처리(접수/처리) 목록 생성

                    break;

                case 3:
                    // 상황처리(종료) 탭

                    lc.dataReload('sitPcs3'); // 상황처리(종료) 목록 생성

                    break;
            }
        });

        //그리드 초기화
        lc.gridSetting('sitPpg'); // 상황전파 목록 생성
        lc.gridSetting('taxInfo'); // 체납정보 목록 생성
        lc.gridSetting('taxCarFindLoc'); // 체납차량 발견위치 목록 생성
        lc.gridSetting('sitPcs0'); // 상황처리(전체) 목록 생성
        lc.gridSetting('sitPcs1'); // 상황처리(발생) 목록 생성
        lc.gridSetting('sitPcs2'); // 상황처리(접수/처리) 목록 생성
        lc.gridSetting('sitPcs3'); // 상황처리(종료) 목록 생성
        lc.sitPcsDataCnt();
    });

    // 윈도우 화면 리사이즈 시 이벤트
    jQuery(window).bind('resize',function () {

        tabIdx = jQuery("#myTabs li").index(jQuery("#myTabs li.active"));

        // jqGrid 3개
        lc.resizePanel('sitPcs' + tabIdx);
        lc.resizePanel('sitPpg');
        lc.resizePanel('taxInfo');

    }).trigger('resize');
});