/**
 * User: 이준수
 * Date: 2017.10.27
 * Time: 오전 10:30
 */
define('local', ['common', 'formcheck', 'jqGrid.setting', 'jquery', 'bootstrap-datepicker.lang', 'jqGrid'], function (common, formcheck, jqFn, jQuery) {

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

        switch(listID) {

            case 'fileCltResList' :
                // 파일수신현황 목록

                filterData = jQuery('#srcPanel :input').serializeObject();

                jqOpt = {
                    url : './getFileCltResList'
                };

                break;

            case 'fileCltErrList' :
                // 오류세부 목록
                var rowid = jQuery('#fileCltResList').jqGrid('getGridParam','selrow');

                rowid != null ? filterData.tfcr_seq = jQuery('#fileCltResList').jqGrid('getRowData',rowid).tfcr_seq : filterData.tfcr_seq = '';

                jqOpt = {
                    url : './getFileCltErrList'
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
            .trigger("reloadGrid", [{page:1}]);
    }

    // 패널 초기화
    function panelClear(isAll, objID, isListReset) {

        if (isAll === undefined) isAll = false; // 전체 reset 여부
        if (objID === undefined) objID = ''; // panelID
        if (isListReset === undefined) isListReset = true; // Master List reset 여부

        if(isAll) {
            // 모든 패널 초기화
            panelClear(false, 'fileCltResPanel', false);
            panelClear(false, 'fileCltErrPanel', false);

            var rowid = jQuery("#fileCltResList").jqGrid('getGridParam', 'selrow');

            if (rowid !== null && isListReset) {

                jQuery("#fileCltResList").jqGrid("resetSelection"); // Grid Select Reset 처리
            }

            return false;
        }

        switch(objID) {

            case 'fileCltResPanel':
                // 파일수신현황 그리드
                fileCltSel = undefined;

                jQuery('#fileCltResList').jqGrid('clearGridData'); // 파일수신현황 그리드 데이터 초기화
                break;

            case 'fileCltErrPanel':
                // 파일수신현황 그리드
                fileCltSel = undefined;

                jQuery('#fileCltErrList').jqGrid('clearGridData'); // 오류세부 목록 그리드 데이터 초기화
                break;
        }
    }

    // 레이아웃 변경 시 사이즈 조절 리턴 함수
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
            height: 500,                // 세로 크기
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
            case 'fileCltResList' :
                // 파일수신현황 목록
                jqOpt = {
                    url : '',
                    editurl: '',
                    scroll: 1,
                    scrollrows: true,
                    rowList: [10, 30, 50, 100],
                    colNames: ['', '발생일자', '수신건수', '처리건수', '영숫자오류', '하이픈오류', '기타오류', '처리일자'],
                    colModel: [
                        {name: 'tfcr_seq', index: 'tfcr_seq', key: true, hidden: true, align: "center"},
                        {name: 'make_dt', index: 'make_dt', width: 4, editable: false, align: "center"},
                        {name: 'clt_cnt', index: 'clt_cnt', width: 5, editable: false, align: "center"},
                        {name: 'prc_cnt', index: 'prc_cnt', width: 5, editable: false, align: "center"},
                        {name: 'err_var_cnt', index: 'err_var_cnt', width: 3, editable: false, align: "center"},
                        {name: 'err_hpn_cnt', index: 'err_hpn_cnt', width: 3, editable: false, align: "center"},
                        {name: 'err_etc_cnt', index: 'err_etc_cnt', width: 3, editable: false, align: "center"},
                        {name: 'prc_dt', index: 'prc_dt', width: 4, editable: false, align: "center"}
                    ],
                    onInitGrid: function() {

                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {
                        // 행 선택 시
                        if (id && id !== fileCltSel) {

                            if (fileCltSel !== undefined) {

                                jQuery(this).jqGrid('restoreRow', fileCltSel);
                                jqFn.jqGridListIcon(this.id, fileCltSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + fileCltSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            fileCltSel = id;
                        }
                        dataReload('fileCltErr');
                    },
                    loadComplete: function () {5
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

            case 'fileCltErrList' :
                // 오루세부 목록
                jqOpt = {
                    url : '',
                    editurl: '',
                    scroll: 1,
                    scrollrows: true,
                    rowList: [10, 30, 50, 100],
                    colNames: ['', '', '수집데이터','에러유형','에러유형코드'],
                    colModel: [
                        {name: 'tfcr_seq', index: 'tfcr_seq', key: true, hidden: true, align: "center"},
                        {name: 'tfce_seq', index: 'tfce_seq', key: true, hidden: true, align: "center"},
                        {name: 'clt_data', index: 'clt_data', width: 4, editable: false, align: "center"},
                        {name: 'err_type', index: 'err_type', width: 5, editable: false, align: "center"},
                        {name: 'err_type_cd', index: 'err_type_cd', width: 5, editable: false, align: "center"},
                    ],
                    onInitGrid: function() {

                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {

                    },
                    loadComplete: function () {
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(
                            // grid 크기 자동 적용
                            resizePanel(tarID)
                        ).done(function () {
                                // 위에 dataInit 부분에서 isDigitDotOnly attr추가 하면서 이벤트 처리하기 위해서 다시 호출
                                //formcheck.setEvents();
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

    return {
        inputCheckScript: inputCheckScript,
        setEvents: formcheck.setEvents,
        dataReload: dataReload,
        panelClear : panelClear,
        resizePanel: resizePanel,
        gridSetting: gridSetting
    }
});

require(['common', 'darkhand', 'local', 'jquery', 'bootstrap-datetimepicker'], function (common, darkhand, lc, jQuery, datetimepicker) {
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

                        lc.panelClear(true); // 전체 폼 초기화
                        lc.dataReload('fileCltRes'); // 파일수신현황 목록
                    }
                });
                break;
        }

        common.enterSend(tw);
    }

    // 페이지 로딩 완료 후 이벤트
    jQuery(function () {
        // 권한에 따른 버튼 비활성화
        if(authCrud.READ_FL === 'N') {

            jQuery('#btnSrch').attr('disabled', true);
        }

        // 엔터키 이벤트 체크
        lc.setEvents();
        enterCheck(); // 엔터 적용

        // 검색 폼
        common.setSelectOpt(jQuery('#srcFileCltJong'), null, fileCltJongList); // 파일수신현황종류

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


        // 그리드 초기화
        lc.gridSetting('fileCltRes'); // 파일수신현황 목록
        lc.gridSetting('fileCltErr'); // 오류세부 목록
    });

    // 윈도우 화면 리사이즈 시 이벤트
    jQuery(window).bind('resize',function () {

        lc.resizePanel('fileCltRes');
        lc.resizePanel('fileCltErr');

    }).trigger('resize');
});