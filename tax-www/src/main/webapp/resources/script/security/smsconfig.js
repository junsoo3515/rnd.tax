/**
 * User: 이준수
 * Date: 2017.10.27
 * Time: 오전 10:27
 */
define('local', ['common', 'formcheck', 'jqGrid.setting', 'jquery', 'jqGrid'], function (common, formcheck, jqFn, jQuery) {

    jQuery.jgrid.defaults.width = 780;
    jQuery.jgrid.defaults.responsive = true;
    jQuery.jgrid.defaults.styleUI = 'Bootstrap';


    // 리턴 스크립트 체크
    function inputCheckScript(tarID) {

        switch (tarID) {
            default:

                return formcheck.checkForm(tarID);

                break;
        }
    }

    // jqGrid data 리프레쉬
    function dataReload(tarID) {

        var listID = tarID + "List";
        var dataGrid = jQuery("#" + listID);
        var filterData = {};

        var jqOpt = {};

        switch (listID) {

            case 'contactGrpList' :
                // 그룹 목록
                filterData = jQuery('#srcPanel :input').serializeObject();

                jqOpt = {
                    url: './getContactGrpList'
                };
                break;

            case 'contactList' :
                // 연락처 목록
                filterData = jQuery('#srcPanel :input').serializeObject();
                var rowid = jQuery('#contactGrpList').jqGrid('getGridParam', 'selrow');


                (rowid != null && (jQuery("#" + rowid).hasClass('jqgrid-new-row') ? "add" : "edit") != 'add') ? filterData.tcg_seq = jQuery('#contactGrpList').jqGrid('getRowData', rowid).tcg_seq : filterData.tcg_seq = '';

                jqOpt = {
                    url: './getContactList'
                };
                break;
        }

        dataGrid
            .jqGrid("setGridParam", jQuery.extend(true, {
                search: true,
                postData: {
                    filters: JSON.stringify(filterData)
                }
            }, jqOpt))
            .trigger("reloadGrid", [{page: 1}]);
    }

    // 패널 초기화
    function panelClear(isAll, objID, isListReset) {

        if (isAll === undefined) isAll = false; // 전체 reset 여부
        if (objID === undefined) objID = ''; // panelID
        if (isListReset === undefined) isListReset = true; // Master List reset 여부

        if (isAll) {
            // 모든 패널 초기화
            panelClear(false, 'contactGrpPanel', false);
            panelClear(false, 'contactPanel', false);

            var rowid1 = jQuery("#contactGrpList").jqGrid('getGridParam', 'selrow');
            var rowid2 = jQuery("#contactList").jqGrid('getGridParam', 'selrow');

            if (rowid1 !== null || rowid2 !== null && isListReset) {

                jQuery("#contactGrpList").jqGrid("resetSelection"); // Grid Select Reset 처리
                jQuery("#contactList").jqGrid("resetSelection"); // Grid Select Reset 처리
            }

            return false;
        }

        switch (objID) {

            case 'contactGrpPanel':
                // 그룹목록 그리드
                contactGrpSel = undefined;

                jQuery('#btnAdd1').attr('disabled', (authCrud.REG_FL === 'Y') ? false : true); // 추가버튼 Style 변경
                jQuery('#contactGrpList').jqGrid('clearGridData'); // 그리드 데이터 초기화
                break;

            case 'contactPanel':
                // 연락처목록 그리드
                contactSel = undefined;

                jQuery('#btnAdd2').attr('disabled', (authCrud.REG_FL === 'Y') ? false : true); // 추가버튼 Style 변경
                jQuery('#contactList').jqGrid('clearGridData'); // 그리드 데이터 초기화
                break;
        }
    }

    // 레이아웃 변경 시 사이즈 조절 리턴 함수
    function resizePanel(id) {

        if (id === undefined) {

            id = null;
        }

        if (id !== null) {

            jQuery.each([{list: id + "List", panel: id + "Panel"}], function (sIdx, data) {

                jQuery("#" + data["list"]).jqGrid('setGridWidth', jQuery("#" + data["panel"]).width() - 2);
            });
        }
    }

    // jqGrid 결과 후 액션
    function gridResAction(res, tarID) {

        common.setOSXModal((res.isSuccess === true ? '성공적으로 적용되었습니다.' : '적용에 실패하였습니다.'));

        dataReload(tarID);
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
            pager: pageID,
            rowNum: 30,                 // 처음에 로드될 때 표출되는 row 수
            rowList: [], // row 갯수 표출 세팅
            viewsortcols: [true, 'vertical', true], // 소팅 인자 세팅
            rownumbers: true,           // Grid의 RowNumber 표출
            viewrecords: true,          // 우측 View 1-4 Text 표출 부분
            gridview: true,             // Grid Alert
            autowidth: true,            // width 자동 맞춤
            shrinkToFit: true,          // width에 맞춰 Cell Width 자동 설정
            height: 500,                // 세로 크기
            caption: '',                // 캡션 명(없으면 표출 안됨)
            beforeRequest: function () {
                // POST 보내기 전 이벤트
                if (jQuery(this).jqGrid('getGridParam', 'url') === '') return false;
            },
            gridComplete: function () {

                jQuery('#' + pageID + ' .ui-pg-input').attr('readonly', true);
            }
        };
        var jqOpt = {};

        switch (listID) {
            case 'contactGrpList' :
                // 그룹목록
                jqOpt = {
                    url: '',
                    editurl: './setContactGrpAct',
                    scroll: 1,
                    scrollrows: true,
                    rowList: [10, 30, 50, 100],
                    colNames: ['', '그룹명', '시작시간', '종료시간', '단말건수', '관리'],
                    colModel: [
                        {name: 'tcg_seq', index: 'tcg_seq', editable: true, hidden: true},
                        {
                            name: 'grp_nm', index: 'grp_nm', width: 3, editable: false, editrules: {required: true},
                            editoptions: {
                                dataInit: function (el) {

                                    jQuery(el).attr({msg: '그룹명을'});
                                },
                                placeholder:'그룹명을 입력하세요.'
                            }
                        },
                        {
                            name: 'sms_st_time', index: 'sms_st_time', width: 2, editable: true, editrules:{number:true, custom:true, custom_func:timeChk},
                            editoptions: {
                                dataInit: function (el) {

                                    jQuery(el).attr({

                                        msg: '시작시간을',
                                        isNumericOnly: 1
                                    });
                                },
                                maxlength: 4,
                                placeholder:'시, 분을 입력하세요.'
                            }
                        },
                        {
                            name: 'sms_ed_time', index: 'sms_ed_time', width: 2, editable: true, editrules:{number: true, custom:true, custom_func:timeChk},
                            editoptions: {
                                dataInit: function (el) {

                                    jQuery(el).attr({

                                        msg: '종료시간을',
                                        isNumericOnly: 1
                                    });
                                },
                                maxlength: 4,
                                placeholder:'시, 분을 입력하세요.'
                            }
                        },
                        {name: 'con_ct', index: 'con_ct', width: 2, align: 'center', editable: false},
                        {
                            name: 'myac',
                            width: 1,
                            sortable: false,
                            classes: 'text-center',
                            formatter: 'actions',
                            formatoptions: {
                                keys: true,
                                editbutton: (authCrud.MOD_FL === "N" ? false : true),
                                delbutton: (authCrud.DEL_FL === "N" ? false : true),
                                onEdit: function (rowid) {
                                    // 수정 버튼 클릭 시 Event
                                    jQuery('#btnAdd2').attr('disabled', true);

                                    formcheck.setEvents();

                                    if (contactGrpSel !== rowid) {

                                        jQuery(this).jqGrid('restoreRow', contactGrpSel);
                                        jqFn.jqGridListIcon(this.id, contactGrpSel);
                                    }

                                    contactGrpSel = jQuery.jgrid.jqID(rowid);

                                    // 수정할 수 없는 항목 disable 처리
                                    jQuery("tr#" + contactGrpSel).find("input").eq(4).attr("disabled", true);
                                },
                                afterRestore: function (rowid) {
                                    // 취소 버튼 클릭 시 Event
                                    jQuery('#btnAdd2').attr('disabled', false);

                                },
                                onSuccess: function (res) {
                                    // 저장 후 리턴 결과
                                    gridResAction(jQuery.parseJSON(res.responseText), 'contactGrp');
                                },
                                restoreAfterError: true, // 저장 후 입력 폼 restore 자동/수동 설정
                                delOptions: {
                                    url: './setContactGrpDel',
                                    mtype: 'POST',
                                    ajaxDelOptions: {contentType: "application/json", mtype: 'POST'},
                                    msg: '선택된 행을 삭제하시겠습니까? 관련된 연락처 목록들도 전부 삭제됩니다.',
                                    serializeDelData: function () {

                                        var reqData = dataGrid.jqGrid('getRowData', contactGrpSel);
                                        return JSON.stringify({
                                            tcg_seq: reqData.tcg_seq
                                        });
                                    },
                                    reloadAfterSubmit: false,
                                    afterComplete: function (res) {
                                        jQuery('#btnAdd2').attr('disabled', true);
                                        dataReload('contactGrp');
                                        dataReload('contact');
                                        gridResAction(jQuery.parseJSON(res.responseText), 'contactGrp');

                                    }
                                }
                            }
                        }
                    ],
                    onInitGrid: function () {

                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {
                        // 행 선택 시
                        if (id && id !== contactGrpSel) {

                            if (contactGrpSel !== undefined) {

                                jQuery(this).jqGrid('restoreRow', contactGrpSel);
                                jqFn.jqGridListIcon(this.id, contactGrpSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + contactGrpSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            contactGrpSel = id;

                            // 연락처목록 데이터 그리드 초기화 및 갱신
                            jQuery.when(
                            ).then(function () {

                                }).always(function () {
                                    if((jQuery("#" + id).hasClass('jqgrid-new-row') ? "add" : "edit") != 'add') {
                                        jQuery('#btnAdd2').attr('disabled', false);
                                    }

                                    dataReload('contact');
                                });
                        }
                    },
                    loadComplete: function () {

                        var obj = jQuery(this);
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(
                            // grid 크기 자동 적용
                            resizePanel(tarID)
                        ).done(function () {
                                // 위에 dataInit 부분에서 isDigitDotOnly attr추가 하면서 이벤트 처리하기 위해서 다시 호출
                                formcheck.setEvents();
                                // group Header 추가
                                // loadComplete에서 안할 시 크기가 꺠짐
                                obj.jqGrid('destroyGroupHeader').jqGrid('setGroupHeaders', {
                                    useColSpanStyle: true,
                                    groupHeaders: [{
                                        startColumnName: 'sms_st_time',
                                        numberOfColumns: 2,
                                        titleText: 'SMS수신시간'
                                    }]
                                });
                            }).always(function () {

                                jQuery.fn.loadingComplete();
                                return false;
                            });
                    }
                };

                break;

            case 'contactList' :
                // 연락처목록
                jqOpt = {
                    url: '',
                    editurl: './setContactAct',
                    scroll: 1,
                    scrollrows: true,
                    rowNum: -1,          // 처음에 로드될 때 표출되는 row 수
                    rowList: [],         // row 갯수 표출 세팅
                    viewrecords: false, // 우측 View 1-4 Text 표출 부분
                    recordtext: '',      //
                    pgbuttons: false,   // disable page control like next, back button
                    pgtext: null,       // disable pager text like 'Page 0 of 10'
                    rowList: [10, 30, 50, 100],
                    colNames: ['', '', '', '이름', '연락처', '부서', '관리'],
                    colModel: [
                        {name: 'tc_seq', index: 'tc_seq', editable: true, hidden: true},
                        {name: 'tcg_seq', index: 'tcg_seq', editable: true, hidden: true},
                        {name: 'cm_seq', index: 'cm_seq', editable: true, hidden: true},
                        {
                            name: 'nm', index: 'nm', width: 4, editable: true, editrules: {required: true},
                            editoptions: {
                                dataInit: function (el) {

                                    jQuery(el).attr({msg: '이름을'});
                                },
                                placeholder:'이름을 입력하세요.'
                            }
                        },
                        {
                            name: 'hp', index: 'hp', width: 2, editable: true, editrules: {required: true},
                            editoptions: {
                                dataInit: function (el) {

                                    jQuery(el).attr({
                                        msg: '연락처를',
                                        isTel: 'isTel'
                                    });
                                },
                                maxlength:13,
                                pattern:'[0-9]{3}-[0-9]{4}-[0-9]{4}',
                                placeholder:'연락처를 입력하세요.'
                            }
                        },
                        {
                            name: 'part_nm', index: 'part_nm', width: 2, editable: true,
                            editoptions: {
                                dataInit: function (el) {

                                    jQuery(el).attr({msg: '부서명을'});
                                },
                                placeholder:'부서명을 입력하세요.'
                            }
                        },
                        {
                            name: 'myac',
                            width: 1,
                            sortable: false,
                            classes: 'text-center',
                            formatter: 'actions',
                            formatoptions: {
                                keys: true,
                                editbutton: (authCrud.MOD_FL === "N" ? false : true),
                                delbutton: (authCrud.DEL_FL === "N" ? false : true),
                                onEdit: function (rowid) {
                                    // 수정 버튼 클릭 시 Event
                                    jQuery('#btnAdd1').attr('disabled', true);

                                    formcheck.setEvents();

                                    if (contactSel !== rowid) {

                                        jQuery(this).jqGrid('restoreRow', contactSel);
                                        jqFn.jqGridListIcon(this.id, contactSel);
                                    }

                                    contactSel = jQuery.jgrid.jqID(rowid);
                                    nm = jQuery('tr#' + contactSel).find('input').eq(3).val();
                                    chkDupContact(contactSel);

                                },
                                afterRestore: function (rowid) {
                                    // 취소 버튼 클릭 시 Event
                                    jQuery('#btnAdd1').attr('disabled', false);
                                },
                                onSuccess: function (res) {
                                    // 저장 후 리턴 결과
                                    chkDbDupContact();

                                    jQuery('#btnAdd1').attr('disabled', false);
                                    gridResAction(jQuery.parseJSON(res.responseText), 'contact');
                                    dataReload('contactGrp');
                                },
                                restoreAfterError: true, // 저장 후 입력 폼 restore 자동/수동 설정
                                delOptions: {
                                    url: './setContactDel',
                                    mtype: 'POST',
                                    ajaxDelOptions: {contentType: "application/json", mtype: 'POST'},
                                    serializeDelData: function () {

                                        var reqData = dataGrid.jqGrid('getRowData', contactSel);
                                        return JSON.stringify({
                                            tc_seq: reqData.tc_seq
                                        });
                                    },
                                    reloadAfterSubmit: false,
                                    afterComplete: function (res) {


                                        gridResAction(jQuery.parseJSON(res.responseText), 'contact');

                                        dataReload('contactGrp');
                                    }
                                }
                            }
                        }
                    ],
                    onInitGrid: function () {

                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {
                        // 행 선택 시
                        if (id && id !== contactSel) {

                            if (contactSel !== undefined) {

                                jQuery(this).jqGrid('restoreRow', contactSel);
                                jqFn.jqGridListIcon(this.id, contactSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + contactSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            contactSel = id;
                        }
                    },
                    loadComplete: function () {
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(
                            // grid 크기 자동 적용
                            resizePanel(tarID)
                        ).done(function () {
                                // 위에 dataInit 부분에서 isDigitDotOnly attr추가 하면서 이벤트 처리하기 위해서 다시 호출
                                formcheck.setEvents();
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

    function chkDupContact(sel) {

        jQuery('tr#' + sel).find('input').eq(3).focusout(function () {

            if (nmArr.indexOf(jQuery('tr#' + sel).find('input').eq(3).val()) != -1 && nm != jQuery('tr#' + sel).find('input').eq(3).val()) {

                var html = '';

                for(var i = 0; i<nmArr.length; i++){

                    if(nmArr[i] == jQuery('tr#' + sel).find('input').eq(3).val()) {

                        html += '\n( ' + nmArr[i] +' / ' + hpArr[i] + ' / ' + partNmArr[i]+ ' )';
                    }
                }

                var input = confirm('담당자 관리 데이터 베이스에' + html + '\n이 이미 존재 합니다. 계속하시겠습니까?');

                if (input) {
                }
                else {

                    jQuery('tr#' + sel).find('input').eq(3).val(nm);
                }
            }
        });
    }

        function chkDbDupContact(){
            jQuery.when(
                jQuery.ajax({
                    url : './chkDupContact',
                    type: 'POST',
                    contentType: 'application/json; charset=utf-8'
                })
            ).then(function(data) {
                    nmArr = [];
                    hpArr = [];
                    partNmArr = [];

                    if(data != null) {

                        for (var i = 0; i < data.length; i++) {

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

        function timeChk(value, colname) {

            if(parseInt(value.substr(0,2)) > 24 || parseInt(value.substr(2,2)) > 60 || parseInt(value) > 2400 || value.length < 4) {

                return [false, ' 시간을 정확히 입력하세요. '];
            }
            else {
                return [true, ''];
            }

        }

        return {
            inputCheckScript: inputCheckScript,
            setEvents: formcheck.setEvents,
            dataReload: dataReload,
            panelClear: panelClear,
            resizePanel: resizePanel,
            gridResAction: gridResAction,
            gridSetting: gridSetting,
            chkDupContact: chkDupContact,
            chkDbDupContact: chkDbDupContact
        }
    }

    )
    ;

    require(['common', 'darkhand', 'local', 'jquery'], function (common, darkhand, lc, jQuery) {
        // 엔터 적용
        function enterCheck(idx) {

            if (idx === undefined) idx = 0;

            var tw = [];

            switch (idx) {
                case 0:

                    tw.push({
                        chk: jQuery("#srcPanel :input"),
                        script: function () {

                            var lc = require('local');
                            return lc.inputCheckScript('srcPanel');
                        },
                        ret: "btnSrch",
                        state: function () {

                            var lc = require('local');

                            jQuery.fn.loadingStart();

                            if(jQuery('#srcContactGrp').val() != '' || jQuery('#srcContactGrp').val() == '') {
                                lc.panelClear(true); // 전체 폼 초기화
                                lc.dataReload('contactGrp'); // 그룹 목록
                                lc.dataReload('contact'); // 연락처 목록
                            }

                            if(jQuery('#srcContact').val() != '') {
                                lc.panelClear(false, 'contact', false);
                                lc.dataReload('contact'); // 연락처 목록
                            }



                        }
                    });
                    break;
            }

            common.enterSend(tw);
        }

        // 그리드 엔터 키 누를 경우 validation 및 저장 함수
        function gridEnterSave(listID) {

            var obj = jQuery('#' + listID);
            var id, key;

            switch (listID) {
                case 'contactGrpList' :

                    id = contactGrpSel;
                    key = 'contactGrp';
                    break;

                case 'contactList' :

                    id = contactSel;
                    key = 'contact';
                    break;
            }

            if (lc.inputCheckScript(listID) === true) {

                var opers = ( jQuery("#" + id).hasClass('jqgrid-new-row') ? "add" : "edit" );

                obj.jqGrid('saveRow', id, {
                    extraparam: {oper: opers},
                    successfunc: function (res) {
                        // 리턴 결과
                        lc.gridResAction(jQuery.parseJSON(res.responseText), key);

                        lc.dataReload('contactGrp');
                    },
                    restoreAfterError: false //저장 실패 시 restore기능 사용 유무(true : 입력 상태 grid 복원, false : 입력 상태 계속 유지)
                });
            }
        }

        // 페이지 로딩 완료 후 이벤트
        jQuery(function () {
            // 권한에 따른 버튼 비활성화
            if (authCrud.READ_FL === 'N') {

                jQuery('#btnSrch').attr('disabled', true);
            }
            if (authCrud.REG_FL === 'N') {

                jQuery('#btnAdd1').attr('disabled', true);
                jQuery('#btnAdd2').attr('disabled', true);
            }

            jQuery('#btnAdd2').attr('disabled', true);


            // 엔터키 이벤트 체크
            lc.setEvents();
            enterCheck(); // 엔터 적용

            lc.inputCheckScript();
            lc.chkDbDupContact();


            // 그룹목록 추가 버튼 클릭 이벤트
            jQuery('#btnAdd1').on('click', function () {

                jQuery('#btnAdd2').attr('disabled', true);

                jQuery.when(
                    jQuery('#contactGrpList').jqGrid('setGridParam', {page: 1})
                ).always(function () {

                        common.addRow('contactGrpList', {}, function () {

                            var lc = require('local');

                            lc.setEvents();
                        });
                    });
            });

            // 연락처목록 추가 버튼 클릭 이벤트
            jQuery('#btnAdd2').on('click', function () {

                jQuery('#btnAdd1').attr('disabled', true);

                jQuery.when(
                    jQuery('#contactList').jqGrid('setGridParam', {page: 1})
                ).always(function () {

                        common.addRow('contactList', {tcg_seq:jQuery('#contactGrpList').jqGrid('getRowData', jQuery('#contactGrpList').jqGrid('getGridParam', 'selrow')).tcg_seq}, function () {

                            var lc = require('local');

                            lc.setEvents();
                            lc.chkDupContact(jQuery('#contactList').jqGrid('getGridParam','selrow'));
                        });
                    });
            });

            // jqGrid의 입력/수정 모드 시 엔터 값 적용 하기 위한 key Event Catch
            jQuery("#contactGrpList").on("keydown", ':input', function (e) {

                if (e.keyCode === 13) {

                    gridEnterSave('contactGrpList');
                    return false;
                }
            });
            jQuery("#contactList").on("keydown", ':input', function (e) {

                if (e.keyCode === 13) {

                    gridEnterSave('contactList');
                    return false;
                }
            });

            // 그리드 초기화
            lc.gridSetting('contactGrp'); // 코드 목록
            lc.gridSetting('contact'); // 코드 목록

        });

        // 윈도우 화면 리사이즈 시 이벤트
        jQuery(window).bind('resize', function () {

            lc.resizePanel('contactGrp');
            lc.resizePanel('contact');

        }).trigger('resize');
    });