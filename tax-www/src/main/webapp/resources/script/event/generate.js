/**
 * User: 이준수
 * Date: 2017.10.13
 * Time: 오후06:22
 */
define('local', ['common', 'formcheck', 'jqGrid.setting', 'highcharts', 'jquery', 'bootstrap-datepicker.lang', 'jqGrid'], function (common, formcheck, jqFn, Highcharts, jQuery) {

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

            case 'taxOcrList' :
                // 체납차량 발생현황 목록

                jqOpt = {
                    url: './getTaxOcrList'
                };

                filterData = jQuery('#srcPanel :input').serializeObject();
                dataGrid
                    .jqGrid("setGridParam", jQuery.extend(true, {
                        search: true,
                        postData: {
                            filters: JSON.stringify(filterData)
                        }
                    }, jqOpt))
                    .trigger("reloadGrid", [{page: 1}]);
                break;

            case 'timeTaxOcrList' :
                // 시간별 발생현황 목록

                jqOpt = {
                    url: './getTimeTaxOcrList'
                };

                filterData = jQuery('#srcPanel :input').serializeObject();
                dataGrid
                    .jqGrid("setGridParam", jQuery.extend(true, {
                        search: true,
                        postData: {
                            filters: JSON.stringify(filterData)
                        }
                    }, jqOpt))
                    .trigger("reloadGrid", [{page: 1}]);
                break;

            case 'areaOcrTopList' :
                // 지역별 발생현황 Top5 목록

                jqOpt = {
                    url: './getAreaOcrTopList'
                };

                filterData = jQuery('#srcPanel :input').serializeObject();
                dataGrid
                    .jqGrid("setGridParam", jQuery.extend(true, {
                        search: true,
                        postData: {
                            filters: JSON.stringify(filterData)
                        }
                    }, jqOpt))
                    .trigger("reloadGrid", [{page: 1}]);
                break;

            case 'cctvOcrTopList' :
                // CCTV별 발생현황 Top5 목록

                jqOpt = {
                    url: './getCctvOcrTopList'
                };

                filterData = jQuery('#srcPanel :input').serializeObject();
                dataGrid
                    .jqGrid("setGridParam", jQuery.extend(true, {
                        search: true,
                        postData: {
                            filters: JSON.stringify(filterData)
                        }
                    }, jqOpt))
                    .trigger("reloadGrid", [{page: 1}]);
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
            pager: pageID,
            rowNum: 30,                 // 처음에 로드될 때 표출되는 row 수
            rowList: [], // row 갯수 표출 세팅
            viewsortcols: [true, 'vertical', true], // 소팅 인자 세팅
            rownumbers: true,           // Grid의 RowNumber 표출
            viewrecords: true,          // 우측 View 1-4 Text 표출 부분
            gridview: true,             // Grid Alert
            autowidth: true,            // width 자동 맞춤
            shrinkToFit: true,          // width에 맞춰 Cell Width 자동 설정
            height: 285,                // 세로 크기
            caption: '',                // 캡션 명(없으면 표출 안됨)
            beforeRequest: function () {

            },
            gridComplete: function () {

                jQuery('#' + pageID + ' .ui-pg-input').attr('readonly', true);
            }
        };
        var jqOpt = {};

        switch (listID) {
            case 'taxOcrList':
                jqOpt = {
                    url: '',
                    height: 150,
                    scroll: 1,
                    rowList: [10, 30, 50, 100],
                    colNames: ['','차량번호', 'CCTV명', 'CCTV위치', '사진' , '발생시간', '처리상태', '금액(건수)'],
                    colModel: [
                        {name:'tri_seq', index: 'tri_seq', hidden:true},
                        {name: 'car_no', index: 'car_no', width: 2, align: "center", sortable:false},
                        {name: 'cctv_nm', index: 'cctv_nm', width: 3, align: "center", sortable:false},
                        {name: 'cctv_adres', index: 'cctv_adres', width: 3, align: "center", sortable:false},
                        {name: 'files', index: 'files', width: 1, align: "center", sortable:false, formatter: filesList},
                        {name: 'reg_dts', index: 'reg_dts', width: 2, align: "center"},
                        {name: 'state', index: 'state', width: 1, align: "center", sortable:false},
                        {name: 'tax_money_cnt', index: 'tax_money_cnt', width: 2, align: "center"}
                    ],
                    onInitGrid: function() {

                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {
                        var ret = dataGrid.jqGrid('getRowData', id);
                        // 행 선택 시
                        if (id && id !== sitOcrSel) {

                            if (sitOcrSel !== undefined) {
                                jQuery(this).jqGrid('restoreRow', sitOcrSel);
                                jqFn.jqGridListIcon(this.id, sitOcrSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + sitOcrSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            sitOcrSel = id;
                        }
                    },
                    loadComplete: function (data) {
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(

                            resizePanel('taxOcr') // 브라우저 창 크기 변경 시 grid 크기 자동 적용
                        ).then(function () {

                            }).always(function () {

                                jQuery.fn.loadingComplete();
                                return false;
                            });
                    }
                };
                break;

            case 'timeTaxOcrList':

                jqOpt = {
                    url: '',
                    height: 280,
                    scroll: 1,
                    scrollrows: false,
                    rownumbers: false,
                    rowNum: -1,          // 처음에 로드될 때 표출되는 row 수
                    rowList: [],         // row 갯수 표출 세팅
                    viewrecords: false, // 우측 View 1-4 Text 표출 부분
                    recordtext: '',      //
                    pgbuttons: false,   // disable page control like next, back button
                    pgtext: null,       // disable pager text like 'Page 0 of 10'
                    rowList: [10, 30, 50, 100],
                    colNames: ['시간', '발생건수', '금액(원)'],
                    colModel: [
                        {name: 'tax_time', index: 'tax_time', width: 1, align: "center"},
                        {name: 'tax_cnt', index: 'tax_cnt', width: 1, align: "center"},
                        {name: 'tax_tot_money', index: 'tax_tot_money', width: 2, align: "center", formatter: 'currency',
                            formatoptions: {thousandsSeparator:",", decimalPlaces: 0}}
                    ],
                    beforeProcessing: function(data) {

                        highChartsSetting();

                        if(data.records > 0) {

                            var moneyData = [];
                            var taxData = [];
                            var timeTaxData = data.rows;
                            var time = '';


                            for(var i = 0; i < 24; i++) {

                                moneyData.push(JSON.parse(JSON.stringify(0).replace(/"/g, '')));
                                taxData.push(JSON.parse(JSON.stringify(0).replace(/"/g, '')));

                            }

                            for(var i = 0; i<data.records; i++) {
                                if (timeTaxData[i] != undefined) {
                                    moneyData[parseInt((timeTaxData[i].tax_time).replace(/"/g, ''))] = JSON.parse(JSON.stringify(timeTaxData[i].tax_tot_money).replace(/"/g, ''));
                                    taxData[parseInt((timeTaxData[i].tax_time).replace(/"/g, ''))] = JSON.parse(JSON.stringify(timeTaxData[i].tax_cnt).replace(/"/g, ''));
                                }
                            }

                            var highCharts = jQuery('#timeTaxOcrCharts').highcharts();
                            highCharts.addSeries({
                                name: '금액(원)',
                                type: 'column',
                                yAxis: 1,
                                data: moneyData,
                                tooltip: {
                                    valueSuffix: ' 원'
                                }
                            });

                            highCharts.addSeries({
                                name: '건수',
                                type: 'spline',
                                data: taxData,
                                tooltip: {
                                    valueSuffix: ' 건'
                                }
                            });

                            highCharts.redraw();
                        }
                    },
                    onInitGrid: function() {

                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {
                        var ret = dataGrid.jqGrid('getRowData', id);
                        // 행 선택 시
                        if (id && id !== sitOcrSel) {

                            if (sitOcrSel !== undefined) {
                                jQuery(this).jqGrid('restoreRow', sitOcrSel);
                                jqFn.jqGridListIcon(this.id, sitOcrSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + sitOcrSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            sitOcrSel = id;
                        }
                    },
                    loadComplete: function (data) {
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(

                            resizePanel('timeOcr') // 브라우저 창 크기 변경 시 grid 크기 자동 적용
                        ).then(function () {

                            }).always(function () {

                                jQuery.fn.loadingComplete();
                                return false;
                            });
                    }
                };
                break;

            case 'areaOcrTopList':
                jqOpt = {
                    url: '',
                    height: 200,
                    scroll: 1,
                    scrollrows: false,
                    rowNum: 5,          // 처음에 로드될 때 표출되는 row 수
                    rowList: [],         // row 갯수 표출 세팅
                    viewrecords: false, // 우측 View 1-4 Text 표출 부분
                    recordtext: '',      //
                    pgbuttons: false,   // disable page control like next, back button
                    pgtext: null,       // disable pager text like 'Page 0 of 10'
                    rowList: [10, 30, 50, 100],
                    colNames: ['지역별', 'CCTV수량', '발생건수'],
                    colModel: [
                        {name: 'area_nm', index: 'area_nm', width: 2, sortable: false},
                        {name: 'cctv_cnt', index: 'cctv_cnt', width: 1, align: "center", sortable: false},
                        {name: 'tax_cnt', index: 'tax_cnt', width: 1, align: "center", sortable: false}
                    ],
                    onInitGrid: function() {

                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {
                        var ret = dataGrid.jqGrid('getRowData', id);
                        // 행 선택 시
                        if (id && id !== sitOcrSel) {

                            if (sitOcrSel !== undefined) {
                                jQuery(this).jqGrid('restoreRow', sitOcrSel);
                                jqFn.jqGridListIcon(this.id, sitOcrSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + sitOcrSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            sitOcrSel = id;
                        }
                    },
                    loadComplete: function (data) {
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(

                            resizePanel('areaOcrTop') // 브라우저 창 크기 변경 시 grid 크기 자동 적용
                        ).then(function () {

                            }).always(function () {

                                jQuery.fn.loadingComplete();
                                return false;
                            });
                    }
                };
                break;

            case 'cctvOcrTopList':
                jqOpt = {
                    url: '',
                    height: 200,
                    scroll: 1,
                    scrollrows: false,
                    rowNum: -1,          // 처음에 로드될 때 표출되는 row 수
                    rowList: [],         // row 갯수 표출 세팅
                    viewrecords: false, // 우측 View 1-4 Text 표출 부분
                    recordtext: '',      //
                    pgbuttons: false,   // disable page control like next, back button
                    pgtext: null,       // disable pager text like 'Page 0 of 10'
                    rowList: [10, 30, 50, 100],
                    colNames: ['CCTV명', 'CCTV위치', '발생건수'],
                    colModel: [
                        {name: 'cctv_nm', index: 'cctv_nm', width: 2, sortable: false},
                        {name: 'cctv_adres', index: 'cctv_adres', width: 1, align: "center", sortable: false},
                        {name: 'tax_cnt', index: 'tax_cnt', width: 1, align: "center", sortable: false}
                    ],
                    onInitGrid: function() {

                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {
                        var ret = dataGrid.jqGrid('getRowData', id);
                        // 행 선택 시
                        if (id && id !== sitOcrSel) {

                            if (sitOcrSel !== undefined) {
                                jQuery(this).jqGrid('restoreRow', sitOcrSel);
                                jqFn.jqGridListIcon(this.id, sitOcrSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + sitOcrSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            sitOcrSel = id;
                        }
                    },
                    loadComplete: function (data) {
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(

                            resizePanel('cctvOcrTop') // 브라우저 창 크기 변경 시 grid 크기 자동 적용
                        ).then(function () {

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

    // 패널 초기화
    function panelClear(isAll, objID, isListReset) {

        if (isAll === undefined) isAll = false; // 전체 reset 여부
        if (objID === undefined) objID = ''; // panelID
        if (isListReset === undefined) isListReset = true; // Master List reset 여부

        if(isAll) {
            // 모든 패널 초기화
            panelClear(false, 'taxOcrPanel', false);
            panelClear(false, 'timeOcrPanel', false);
            panelClear(false, 'areaOcrTopPanel', false);
            panelClear(false, 'cctvOcrTopPanel', false);

            var rowid = jQuery("#taxOcrList").jqGrid('getGridParam', 'selrow');

            if (rowid !== null && isListReset) {

                jQuery("#taxOcrList").jqGrid("resetSelection"); // Grid Select Reset 처리

            }

            return false;
        }

    }

    function highChartsSetting() {
        jQuery('#timeTaxOcrCharts').highcharts({
            chart: {
                rendTo: 'timeOcrCharts',
                type: 'xy',
                height: '400'
            },
            title: {
                text: '　'
            },
            xAxis: [{
                categories: ['00시','01시','02시','03시','04시','05시'
                            ,'06시','07시','08시','09시','10시','11시'
                            ,'12시','13시','14시','15시','16시','17시'
                            ,'18시','19시','20시','21시','22시','23시'],
                crosshair: true
            }],
            yAxis: [{
                min: 0,
                allowDecimals: false,
                labels: {
                    format: '{value} 건',
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                title: {
                    text: '건수',
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                }
            },{
                labels: {
                    format: '{value} 원',
                    style: {
                        color: Highcharts.getOptions().colors[0]
                    }
                },
                title: {
                    text: '금액(원)',
                    style: {
                        color: Highcharts.getOptions().colors[0]
                    }
                },
                opposite: true
            }],
            tooltip:{
                shared: true
            },
            legend: {
                layout: 'vertical',
                align: 'left',
                x: 120,
                verticalAlign: 'top',
                y: 100,
                floating: true,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
            },
            series: null
        });
    }

    function filesList (cellValue, rowData, options) {

        var html = '';
        for(var i = 0; i < cellValue.length; i++) {

            html += '<a id="' + cellValue[i].real_tb + cellValue[i].real_seq + i + '" href="' + jQuery.fn.sysUrl + '/res/assets/img/carno/' + cellValue[i].files_seq + '.jpg" data-lightbox=' + cellValue[i].real_tb + cellValue[i].real_seq +'>';

            (i == 0) ? html+= '<span class="glyphicon glyphicon-camera"/></a>' : html += '<span class="glyphicon glyphicon-camera" style="display: none;"/></a>';
        }

        return html;
    }

    return {
        inputCheckScript: inputCheckScript,
        setEvents: formcheck.setEvents,
        dataReload: dataReload,
        resizePanel: resizePanel,
        gridSetting: gridSetting,
        panelClear: panelClear,
        highChartsSetting: highChartsSetting
    }
});

require(['common', 'darkhand', 'local', 'bootstrap-datetimepicker', 'highcharts', 'jquery', 'lightbox'], function (common, darkhand, lc, datetimepicker, Highcharts, jQuery, lightbox) {

    lightbox.option({
        'alwaysShowNavOnTouchDevices' : false,
        'disableScrolling' : true,
        'maxWidth' : 1024,
        'maxHeight' : 768
    });

    // 엔터 적용
    function enterCheck(idx) {

        if (idx === undefined) idx = 0;

        var tw = [];

        switch (idx) {
            case 0:

                tw.push({
                    chk: jQuery("#slaPanel :input"),
                    script: function () {

                        var lc = require('local');
                        return lc.inputCheckScript('slaPanel');
                    },
                    ret: "btnSrch",
                    state: function () {

                        var lc = require('local');

                        jQuery.fn.loadingStart();


                        lc.panelClear(false, '', false);

                        lc.dataReload('taxOcr'); // 체납차량 발생현황 목록 갱신
                        lc.dataReload('timeTaxOcr'); // 시간별 발생현황 목록 갱신
                        lc.dataReload('areaOcrTop'); // 지역별 발생현황 Top5 목록 갱신
                        lc.dataReload('cctvOcrTop'); // CCTV별 발생현황 Top5 목록 갱신

                        jQuery.fn.loadingComplete();
                    }
                });
        }

        common.enterSend(tw);
    }

// 페이지 로딩 완료 후 이벤트
    jQuery(function () {

        // 권한에 따른 버튼 비활성화
        if (authCrud.READ_FL === 'N') {

            jQuery('#btnSrch').attr('disabled', true);
        }


        // 엔터키 이벤트 체크
        lc.setEvents();
        enterCheck(); // 엔터 적용

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
        lc.gridSetting('taxOcr'); // 체납차량 발생현황 목록
        lc.gridSetting('timeTaxOcr'); // 시간별 발생현황 목록
        lc.highChartsSetting(); // 시간별 발생현황 차트
        lc.gridSetting('areaOcrTop'); // 지역별 발생현황 Top5 목록
        lc.gridSetting('cctvOcrTop'); // CCTV별 발생현황 Top5 목록


    });

// 윈도우 화면 리사이즈 시 이벤트
    jQuery(window).bind('resize', function () {

        lc.resizePanel();

    }).trigger('resize');
})
;