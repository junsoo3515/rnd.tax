/**
 * User: 이준수
 * Date: 2017.10.27
 * Time: 오후 05:29
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

            case 'sitPpgList' :
                // 상황전파 이력관리 목록

                filterData = jQuery('#srcPanel :input').serializeObject();

                jqOpt = {
                    url : './getSitPpgList'
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
            panelClear(false, 'sitPpgPanel', false);

            var rowid = jQuery("#sitPpgList").jqGrid('getGridParam', 'selrow');

            if (rowid !== null && isListReset) {

                jQuery("#sitPpgList").jqGrid("resetSelection"); // Grid Select Reset 처리
            }

            return false;
        }

        switch(objID) {

            case 'sitPpgPanel':
                // 파일수신현황 그리드
                sitPpgSel = undefined;

                jQuery('#sitPpgList').jqGrid('clearGridData'); // 상황전파 이력관리 목록 그리드 데이터 초기화
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
            case 'sitPpgList' :
                // 상황전파 이력관리 목록
                jqOpt = {
                    url : '',
                    editurl: '',
                    scroll: 1,
                    scrollrows: true,
                    rowList: [10, 30, 50, 100],
                    colNames: ['', '차량번호', '발견일시', 'SMS전송그룹', 'SMS전송일시', '전파내용', '단말건수', '처리자','송신여부'],
                    colModel: [
                        {name: 'tsp_seq', index: 'tsp_seq', key: true, hidden: true, align: "center"},
                        {name: 'car_no', index: 'car_no', width: 2, editable: false, align: "center"},
                        {name: 'reg_dts', index: 'reg_dts', width: 5, editable: false, align: "center"},
                        {name: 'grp_nm', index: 'grp_nm', width: 5, editable: false, align: "center"},
                        {name: 'send_dt', index: 'send_dt', width: 3, editable: false, align: "center"},
                        {name: 'sms_conts', index: 'sms_conts', width: 3, editable: false, align: "center"},
                        {name: 'con_ct', index: 'con_ct', width: 3, editable: false, align: "center"},
                        {name: 'prc_nm', index: 'prc_nm', width: 4, editable: false, align: "center"},
                        {name: 'send_fl', index: 'send_fl', width: 4, editable: false, align: "center"},
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
                        lc.dataReload('sitPpg'); // 파일수신현황 목록
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
        common.setSelectOpt(jQuery('#srcTcgJong'), null, tcgJongList); // 전파그룹종류

        var nowTemp = new Date();

        jQuery('.input-daterange').datepicker({
            language: 'kr',
            format: 'yyyy-mm-dd',
            todayHighlight: true,
            endDate: new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0),
            todayBnt: "linked"
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
        lc.gridSetting('sitPpg'); // 파일수신현황 목록
    });

    // 윈도우 화면 리사이즈 시 이벤트
    jQuery(window).bind('resize',function () {

        lc.resizePanel('sitPpg');

    }).trigger('resize');
});