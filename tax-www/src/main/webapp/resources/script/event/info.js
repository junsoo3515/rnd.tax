/**
 * User: 이준수
 * Date: 2018.01.12
 * Time: 오전 09:27
 */
define('local', ['common', 'formcheck', 'jqGrid.setting', 'openlayers', 'proj4', 'jquery', 'bootstrap-datepicker.lang','select2.lang', 'jquery-ui', 'jqGrid'], function (common, formcheck, jqFn, openLayers, proj4, jQuery) {

    proj4.defs('EPSG:5181', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs');

    jQuery.jgrid.defaults.width = 780;
    jQuery.jgrid.defaults.responsive = true;
    jQuery.jgrid.defaults.styleUI = 'Bootstrap';

    // Base 지도 타일 변경
    function baseMapChange(id, layers, map) {

        var layer = layers[id];

        jQuery.when(
        ).then(function() {

                jQuery.fn.loadingStart();
            }).done(function() {

                if (layer) {

                    layer.setOpacity(1);
                    map.getLayers().setAt(0, layer);
                }
            })
            .fail(common.ajaxError)
            .always(function() {

                jQuery.fn.loadingComplete();
                return false;
            });
    }

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

            case 'interestList' :
                // 관심차량 목록
                filterData = jQuery('#srcPanel :input').serializeObject();

                jqOpt = {
                    url: './getInterestList'
                };
                break;

            case 'interestRouteList' :
                // 관심차량 이동경로 목록
                filterData = jQuery('#srcPanel :input').serializeObject();
                var rowid = jQuery('#interestList').jqGrid('getGridParam', 'selrow');

                rowid != null ? filterData.car_no = jQuery('#interestList').jqGrid('getRowData', rowid).car_no : filterData.car_no = '';

                jqOpt = {
                    url: './getInterestRouteList'
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
            panelClear(false, 'interestPanel', false);
            panelClear(false, 'interestRoutePanel', false);

            var rowid = jQuery("#cctvList").jqGrid('getGridParam', 'selrow');

            if (rowid == null && isListReset) {

                jQuery("#interestList").jqGrid("resetSelection"); // Grid Select Reset 처리
                jQuery("#interestRouteList").jqGrid("resetSelection"); // Grid Select Reset 처리
            }

            return false;
        }

        switch (objID) {

            case 'interestPanel':
                // 관심차량 목록 그리드
                interestSel = undefined;

                jQuery('#interestList').jqGrid('clearGridData'); // 그리드 데이터 초기화
                break;

            case 'interestRoutePanel':
                // 관심차량 이동겨로 목록 그리드
                interestRouteSel = undefined;

                jQuery('#interestRouteList').jqGrid('clearGridData'); // 그리드 데이터 초기화
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
            case 'interestList' :
                // 관심차량 목록
                jqOpt = {
                    url: '',
                    editurl: '',
                    scroll: 1,
                    scrollrows: true,
                    rowList: [10, 30, 50, 100],
                    rowNum: 3,
                    height: 190,
                    colNames: ['차량번호', 'CCTV 명', '처리상태', '구분', '금액', '건수'],
                    colModel: [
                        {
                            name: 'car_no',
                            index: 'car_no',
                            width: 2,
                            align: 'left',
                            editable: false,
                            sortable: false
                        },
                        {
                            name: 'cctv_nm',
                            index: 'cctv_nm',
                            width: 3,
                            align: 'left',
                            sortable: false,
                            editable: false

                        },
                        {
                            name: 'state',
                            index: 'state',
                            width: 1,
                            align: 'center',
                            sortable: false,
                            editable: false

                        },
                        {
                            name: 'jong_cd',
                            index: 'jong_cd',
                            width: 1,
                            align: 'center',
                            sortable: false,
                            editable: false

                        },
                        {
                            name: 'tot_money',
                            index: 'tot_money',
                            width: 2,
                            align: 'center',
                            sortable: false,
                            editable: false,
                            formatter: 'currency',
                            formatoptions: {thousandsSeparator:",", decimalPlaces: 0}

                        },
                        {
                            name: 'tot_cnt',
                            index: 'tot_cnt',
                            width: 1,
                            align: 'center',
                            sortable: false,
                            editable: false

                        }
                    ],
                    onInitGrid: function () {

                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {
                        // 행 선택 시
                        if (id && id !== interestSel) {

                            if (interestSel !== undefined) {

                                jQuery(this).jqGrid('restoreRow', interestSel);
                                jqFn.jqGridListIcon(this.id, interestSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + interestSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            interestSel = id;
                        }
                        dataReload('interestRoute');
                        getRouteGeoData(jQuery('#interestList').jqGrid('getRowData', interestSel).car_no, jQuery('#srcSDate').val(), jQuery('#srcEDate').val());
                    },
                    loadComplete: function (data) {

                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(
                            // grid 크기 자동 적용
                            resizePanel(tarID)
                        ).then(function () {
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

            case 'interestRouteList' :
                // 관심차량 이동경로 목록
                jqOpt = {
                    url: '',
                    editurl: '',
                    scroll: 1,
                    scrollrows: true,
                    rowList: [10, 30, 50, 100],
                    rowNum: 10,
                    height: 500,
                    colNames: ['', '', '','CCTV 명', '위치(주소)', '발견일시'],
                    colModel: [
                        {name: 'point_x', index: 'point_x', editable: true, hidden: true},
                        {name: 'point_y', index: 'point_y', editable: true, hidden: true},
                        {name: 'cctv_id', index: 'cctv_id', editable: true, hidden: true},
                        {
                            name: 'cctv_nm',
                            index: 'cctv_nm',
                            width: 2,
                            align: 'left',
                            sortable: false,
                            editable: false

                        },
                        {
                            name: 'cctv_adres',
                            index: 'cctv_adres',
                            width: 1,
                            align: 'left',
                            editable: false,
                            sortable: false
                        },
                        {
                            name: 'reg_dts',
                            index: 'reg_dts',
                            width: 1.5,
                            align: 'center',
                            editable: false,
                            sortable: false
                        }
                    ],
                    onInitGrid: function () {

                    },
                    onSelectRow: function (id, status, event) {
                        // 행 선택 시
                        if (id && id !== interestRouteSel) {

                            if (interestRouteSel !== undefined) {

                                jQuery(this).jqGrid('restoreRow', interestRouteSel);
                                jqFn.jqGridListIcon(this.id, interestRouteSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + interestRouteSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            interestRouteSel = id;

                            zoomRouteGeo(jQuery('#interestRouteList').jqGrid('getRowData', interestRouteSel).point_x, jQuery('#interestRouteList').jqGrid('getRowData', interestRouteSel).point_y)
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

    // 이동경로 GIS 정보 가져오기
    function getRouteGeoData(car_no, srcSDate, srcEDate) {

        var tmp;
        var point = [];

        jQuery.when(
            jQuery.ajax({
                url : './getRouteGeoData',
                type : 'POST',
                dataType : 'json',
                contentType : 'application/json; charset=utf-8',
                data : JSON.stringify({
                        car_no: car_no,
                        s_dts: srcSDate,
                        e_dts: srcEDate
                    }),
                async : false
            })
        ).then(function (data) {

                tmp = data;

                if (tmp.length > 0) {
                    for (var i = 0; i < tmp.length; i++) {

                        var tmpWgsPoint = [];

                        tmpWgsPoint.push(tmp[i].point_x);
                        tmpWgsPoint.push(tmp[i].point_y);

                        var tmpTmPoint = [];
                        tmpTmPoint = openLayers.proj.transform(tmpWgsPoint, 'EPSG:4326', 'EPSG:5181');

                        point.push(Math.round(tmpTmPoint[0] * 2.5));
                        point.push(Math.round(tmpTmPoint[1] * 2.5));
                    }
                    getRouteLine(point);
                }

                if (tmp.length == 0) {

                    common.setOSXModal('이동경로가 존재하지 않습니다.');

                    mapInfo.map.removeLayer(routeLayer);
                    routeLayer = undefined;

                }

            }).fail(common.ajaxError)
            .always(function() {
                return false;
            });
    }

    //GIS 이동경로 표출
    function getRouteLine (point) {

        var idx = 0;
        var path = [];
        var nodes = [];

        if(routeLayer != undefined) {
            mapInfo.map.removeLayer(routeLayer);
        }

        jQuery.when(
            jQuery.ajax({
                url : 'http://' + gisWebserviceIp + ':' + gisWebservicePort + '/tax/movingRoute',
                type : 'GET',
                contentType : 'text/plain;charset=UTF-8',
                data : {
                    point : JSON.stringify(point)
                },
                dataType : 'jsonp',
                async : true
            })
        ).then(function (resData) {

                path = resData.path;
                nodes = resData.nodes;

            }).fail(common.ajaxError)
            .always(function() {
                var pathLength = path.length;
                var nodesLength = nodes.length;

                var features = [];

                for (var i = 0; i < pathLength; i++) {

                    path[i] = openLayers.proj.fromLonLat(openLayers.proj.transform(path[i], 'EPSG:5181', 'EPSG:4326'));
                }

                for (var i = 0; i < nodesLength; i++) {

                    nodes[i] = openLayers.proj.fromLonLat(openLayers.proj.transform(nodes[i], 'EPSG:5181', 'EPSG:4326'));
                }

                var lineString = new openLayers.geom.LineString([]);

                lineString.setCoordinates(path);

                var routeCoords = lineString.getCoordinates();
                var routeLength = routeCoords.length;

                var routeFeature = new openLayers.Feature({
                    type: 'route',
                    geometry: lineString
                });

                routeFeature.setStyle(
                    new openLayers.style.Style({
                        stroke: new openLayers.style.Stroke({
                            width: 6, color: [237, 212, 0, 0.8]
                        }),
                        zIndex: 2
                    })
                );

                var startMarker = new openLayers.Feature({
                    geometry: new openLayers.geom.Point(routeCoords[0]),
                    name: 'start'
                });

                startMarker.setStyle(
                    [
                        new openLayers.style.Style({
                            image: new openLayers.style.Icon({
                                scale: .7,
                                opacity: 1,
                                rotateWithView: false,
                                anchor: [0.5, 1],
                                src: jQuery.fn.sysUrl+'/res/assets/img/example/number_1.png'
                            }),
                            zIndex: 5
                        }), new openLayers.style.Style({
                        image: new openLayers.style.Circle({
                            radius: 6,
                            fill: new openLayers.style.Fill({color: '#ff0000'}),
                            stroke: new openLayers.style.Stroke({color: '#ff0000'})
                        }),
                        zIndex: 4
                    })
                    ]
                );

                features.push(startMarker);

                if(point.length > 2) {
                    // 경유지
                    if (nodesLength > 0) {
                        for (var i = 1; i < pathLength - 1; i++) {

                            for (var j = 0; j < nodesLength; j++) {

                                if ((path[i][0] == nodes[j][0]) && (path[i][1] == nodes[j][1])) {

                                    var viaMarker = new openLayers.Feature({
                                        geometry: new openLayers.geom.Point(routeCoords[i])
                                    });

                                    viaMarker.setStyle(
                                        [new openLayers.style.Style({
                                            image: new openLayers.style.Icon({
                                                scale: .7,
                                                opacity: 1,
                                                rotateWithView: false,
                                                anchor: [0.5, 1],
                                                src: jQuery.fn.sysUrl + '/res/assets/img/example/number_' + parseInt(j + 2) + '.png'
                                            }),
                                            zIndex: 5
                                        }),
                                            new openLayers.style.Style({
                                                image: new openLayers.style.Circle({
                                                    radius: 6,
                                                    fill: new openLayers.style.Fill({color: '#FFFFFF'}),
                                                    stroke: new openLayers.style.Stroke({color: '#000000'})
                                                }),
                                                zIndex: 4
                                            })]
                                    );
                                    features.push(viaMarker);
                                }
                            }
                        }
                    }

                    features.push(routeFeature);

                    var endMarker = new openLayers.Feature({
                        geometry: new openLayers.geom.Point(routeCoords[routeLength - 1]),
                        name: 'end'
                    });

                    endMarker.setStyle(
                        [new openLayers.style.Style({
                            image: new openLayers.style.Icon({
                                scale: .7,
                                opacity: 1,
                                rotateWithView: false,
                                anchor: [0.5, 1],
                                src: jQuery.fn.sysUrl + '/res/assets/img/example/number_' + parseInt(nodesLength + 2) + '.png'
                            }),
                            zIndex: 5
                        }), new openLayers.style.Style({
                            image: new openLayers.style.Circle({
                                radius: 6,
                                fill: new openLayers.style.Fill({color: '#ff0000'}),
                                stroke: new openLayers.style.Stroke({color: '#ff0000'})
                            }),
                            zIndex: 4
                        })
                        ]
                    );
                    features.push(endMarker);
                }

                routeLayer = new openLayers.layer.Vector({
                    source: new openLayers.source.Vector({
                        features: features
                    })
                });

                mapInfo.map.addLayer(routeLayer);
                mapInfo.map.getView().setResolution(19.109257067871095);
                mapInfo.map.getView().setCenter(routeCoords[0]);
            });

    }

    // 선택한 경로 확대
    function zoomRouteGeo(point_x, point_y) {

        var tmpArr = [];

        tmpArr.push(+point_x);
        tmpArr.push(+point_y);

        mapInfo.map.getView().setCenter(openLayers.proj.fromLonLat(tmpArr));
        mapInfo.map.getView().setResolution(0.5971642833709717);
    }

    // 화면 설정
    function displayPlacement() {
        jQuery('.panel').css({
            marginBottom: '0px'
        });

        jQuery('.table').css({
            marginBottom: '0px'
        });

        jQuery('.vertical-box').css({
            height: '850px'
        });
    }

    // 지도 거리/면적
    function createHelpTooltip() {

        if (helpTooltipElement != undefined) {

            helpTooltipElement.remove();
        }

        helpTooltipElement = jQuery('<div id="tooltipHidden" class="tooltip hidden"></div>');

        helpTooltip = new openLayers.Overlay({
            element: helpTooltipElement[0],
            offset: [15, 0],
            positioning: 'center-left'
        });

        mapInfo.map.addOverlay(helpTooltip);
    }


    // 지도 거리/면적
    function createMeasureTooltip() {

        if (measureTooltipElement != undefined) {

            measureTooltipElement.remove();
        }

        measureTooltipElement = jQuery('<div id="tooltipMeasure" class="tooltip tooltip-measure"></div>');
        measureTooltipElement.css({
            position: 'relative',
            background: 'rgba(0, 0, 0, 0.5)',
            borderRadius: '4px',
            color: 'white',
            padding: '4px 8px',
            whiteSpace: 'nowrap',
            opacity: '1',
            fontWeight: 'bold'
        });

        measureTooltip = new openLayers.Overlay({
            element: measureTooltipElement[0],
            offset: [0, -15],
            positioning: 'bottom-center'
        });
        mapInfo.map.addOverlay(measureTooltip);
    }

    // 지도 거리/면적
    function formatArea (polygon) {
        var area = openLayers.Sphere.getArea(polygon);
        var output;
        if (area > 10000) {
            output = (Math.round(area / 1000000 * 100) / 100) +
            ' ' + 'km<sup>2</sup>';
        } else {
            output = (Math.round(area * 100) / 100) +
            ' ' + 'm<sup>2</sup>';
        }
        return output;
    };

    // 지도 거리/면적
    function formatLength (line) {
        var length = openLayers.Sphere.getLength(line);
        var output;
        if (length > 100) {
            output = (Math.round(length / 1000 * 100) / 100) +
            ' ' + 'km';
        } else {
            output = (Math.round(length * 100) / 100) +
            ' ' + 'm';
        }
        return output;
    };

    // 지도 거리/면적
    function addInteraction(btnType) {

        if(draw != undefined)
            mapInfo.map.removeInteraction(draw);

        var tooltipMeasure = jQuery('#tooltipMeasure');

        var type = (btnType == 'btnArea' ? 'Polygon' : 'LineString');

        if(measureLayer == undefined) {

            var source = new openLayers.source.Vector();
            measureLayer = new openLayers.layer.Vector({
                source: source,
                style: new openLayers.style.Style({
                    fill: new openLayers.style.Fill({
                        color: 'rgba(255, 255, 255, 0.2)'
                    }),
                    stroke: new openLayers.style.Stroke({
                        color: '#ffcc33',
                        width: 2
                    }),
                    image: new openLayers.style.Circle({
                        radius: 7,
                        fill: new openLayers.style.Fill({
                            color: '#ffcc33'
                        })
                    })
                })
            });

            mapInfo.map.addLayer(measureLayer);
        }


        draw = new openLayers.interaction.Draw({
            source: measureLayer.getSource(),
            type: type,
            style: new openLayers.style.Style({
                fill: new openLayers.style.Fill({
                    color: 'rgba(255, 255, 255, 0.2)'
                }),
                stroke: new openLayers.style.Stroke({
                    color: 'rgba(0, 0, 0, 0.5)',
                    lineDash: [10, 10],
                    width: 2
                }),
                image: new openLayers.style.Circle({
                    radius: 5,
                    stroke: new openLayers.style.Stroke({
                        color: 'rgba(0, 0, 0, 0.7)'
                    }),
                    fill: new openLayers.style.Fill({
                        color: 'rgba(255, 255, 255, 0.2)'
                    })
                })
            })
        });

        mapInfo.map.addInteraction(draw);

        createMeasureTooltip();
        createHelpTooltip();

        var listener;
        draw.on('drawstart',
            function(evt) {
                // set sketch
                sketch = evt.feature;

                var tooltipCoord = evt.coordinate;

                listener = sketch.getGeometry().on('change', function(evt) {
                    var geom = evt.target;
                    var output;

                    if (geom instanceof openLayers.geom.Polygon) {

                        output = formatArea(geom);
                        tooltipCoord = geom.getInteriorPoint().getCoordinates();
                    } else if (geom instanceof openLayers.geom.LineString) {

                        output = formatLength(geom);
                        tooltipCoord = geom.getLastCoordinate();
                    }
                    measureTooltipElement.html(output);
                    measureTooltip.setPosition(tooltipCoord);
                });
            }, this);

        draw.on('drawend',
            function() {
                measureTooltipElement.attr('class', 'tooltip tooltip-static');

                measureTooltipElement.css({
                    position: 'relative',
                    background: 'rgba(0, 0, 0, 0.5)',
                    borderRadius: '4px',
                    padding: '4px 8px',
                    opacity: '0.7',
                    whiteSpace: 'nowrap',
                    backgroundColor: '#ffcc33',
                    color: 'black',
                    border: '1px solid white'
                });

                measureTooltip.setOffset([0, -7]);
                // unset sketch
                sketch = null;
                // unset tooltip so that a new one can be created
                measureTooltipElement = null;
                createMeasureTooltip();
                openLayers.Observable.unByKey(listener);
            }, this);
    }

    return {
        baseMapChange: baseMapChange,
        inputCheckScript: inputCheckScript,
        setEvents: formcheck.setEvents,
        dataReload: dataReload,
        panelClear: panelClear,
        resizePanel: resizePanel,
        gridSetting: gridSetting,
        displayPlacement: displayPlacement,
        addInteraction: addInteraction
    }
});

require(['common', 'darkhand', 'local', 'openlayers', 'proj4', 'jquery', 'bootstrap-datetimepicker', 'select2.lang'], function (common, darkhand, lc, openLayers, proj4, jQuery, datetimepicker) {
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
                        lc.panelClear(true); // 전체 폼 초기화
                        lc.dataReload('interest'); // 관심차량 목록

                        if(interestSel != null)
                            lc.dataReload('interestRoute'); // 관심차량 이동경로 목록

                    }
                });
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

        // 엔터키 이벤트 체크
        lc.setEvents();
        enterCheck(); // 엔터 적용

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

        openLayers.proj.setProj4(proj4); // openLayers 3.36 이상부터는 proj setting 연동

        jQuery('#map').css({
            height:'100%',
            width:'100%'
        });

        // openLayers
        mapInfo = mapInit('map', openLayers, {
            gisApiKey: gisApiKey,
            gisProjection: gisProjection,
            gisBoundsLeft: gisBoundsLeft,
            gisBoundsTop: gisBoundsTop,
            gisBoundsRight: gisBoundsRight,
            gisBoundsBottom: gisBoundsBottom
        });

        mapInfo.map.on('pointermove', function(evt) {
            mapInfo.map.getTargetElement().style.cursor =
                mapInfo.map.hasFeatureAtPixel(evt.pixel) ? 'pointer' : '';

        });

        mapInfo.map.on('click', function(evt) {
            var feature = mapInfo.map.forEachFeatureAtPixel(evt.pixel,
                function(feature) {
                    return feature;
                });
            if(feature) {

            }
            else {

            }
        });

        // ToolBar Event
        // 일반/항공  지도 변경 이벤트
        jQuery('#mapPanel').find('input:radio[name="mapSwicher"]').on('change', function () {

            lc.baseMapChange(jQuery(this).val(), mapInfo.layers, mapInfo.map);
        });

        // 이동 이벤트
        jQuery('#btnMove').on('click', function () {
            mapInfo.map.removeInteraction(draw);

            return false;
        });

        // 확대/축소 이벤트
        jQuery('#btnZoomIn, #btnZoomOut').on('click', function () {

            var step = this.id === 'btnZoomIn' ? 1 : -1;
            var view = mapInfo.map.getView();

            view.animate({
                zoom: view.getZoom() + step,
                duration: 500
            });
            return false;
        });

        // 초기화 이벤트
        jQuery('#btnInit').on('click', function() {

            if(draw != undefined && measureLayer != undefined) {
                mapInfo.map.removeInteraction(draw);
                mapInfo.map.removeLayer(measureLayer);
                jQuery('.tooltip-static').remove();
                draw = undefined;
                measureLayer = undefined;
            }
        });

        // 거리 이벤트
        jQuery('#btnDistance').on('click', function() {

            lc.addInteraction('btnDistance');

        });

        // 면적 이벤트
        jQuery('#btnArea').on('click', function() {

            lc.addInteraction('btnArea');
        });

        lc.inputCheckScript();

        // 그리드 초기화
        lc.gridSetting('interest'); // 관심차량 목록
        lc.gridSetting('interestRoute'); // 관심차량 이동경로 목록

        lc.displayPlacement(); // 화면 설정

        jQuery.fn.loadingComplete();
    });

    // 윈도우 화면 리사이즈 시 이벤트
    jQuery(window).bind('resize', function () {

        lc.resizePanel('interest');
        lc.resizePanel('interestRoute');
    }).trigger('resize');

});