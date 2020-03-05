/**
 * User: 이준수
 * Date: 2018.01.09
 * Time: 오후 12:48
 */
define('local', ['common', 'formcheck', 'jqGrid.setting', 'openlayers', 'proj4', 'jquery','select2.lang', 'jquery-ui', 'jqGrid'], function (common, formcheck, jqFn, openLayers, proj4, jQuery) {

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

    // GIS CCTV 마커 표출
    function getCctvGeoData (mapInfo) {

        var tmp;
        var tmpPoint = [];
        var wgsPoint = [];
        var tmPoint = [];
        var features = [];

        jQuery.when(
            jQuery.ajax({
                url : './getCctvGeoData',
                type : 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                async:false
            })
        ).then(function (data) {
                tmp = data;
            }).fail(common.ajaxError)
            .always(function() {
                return false;
            });

        for (var i = 0; i < tmp.length; i++) {
            tmpPoint.push(+tmp[i].point_x);
            tmpPoint.push(+tmp[i].point_y);
            wgsPoint.push(tmpPoint);
            tmPoint[i] = openLayers.proj.fromLonLat(wgsPoint[i]);
            tmpPoint = [];
        }

        var lineString = new openLayers.geom.LineString([]);

        lineString.setCoordinates(tmPoint);

        var routeCoords = lineString.getCoordinates();
        var routeLength = routeCoords.length;

        for(var i = 0; i < routeLength; i++ ) {
            var cctvMarker = new openLayers.Feature({
                geometry: new openLayers.geom.Point(routeCoords[i]),
                id: tmp[i].cctv_id,
                name: tmp[i].cctv_type + '_' + tmp[i].cctv_rotate,
                type: 'cctvMarker'
            });

            features.push(cctvMarker);
        }

        var source = new openLayers.source.Vector({
           features: features
        });

        var clusterSource = new openLayers.source.Cluster({
            distance: 50,
            source: source
        });

        var styleCache = {};
        cctvMarkerLayer = new openLayers.layer.Vector({
            source: clusterSource,
            style: function(feature) {
                var size = feature.get('features').length;
                var style = styleCache[size];
                    style = new openLayers.style.Style({
                        image: new openLayers.style.Icon({
                            scale: .7 ,
                            opacity: 1,
                            rotateWithView: false,
                            anchor: [0.5, 1],
                            src: jQuery.fn.sysUrl + '/res/assets/img/cctvicon/' + feature.get('features')[size-1].O.name + '.png'
                        })
                    });
                    styleCache[size] = style;
                return style;
            }
        });

        cctvMarkerLayer.setZIndex(1);

        mapInfo.map.addLayer(cctvMarkerLayer);

    }

    // GIS 프리셋 클릭
    function cctvPresetClick(feature, featureType) {

        var pSelectFeatures = [];
        var pFeatures = [];
        var psArr = [];
        var pArr = [];

        if(presetSelectMarkerLayer != undefined) {
            var pSelectSource = presetSelectMarkerLayer.getSource();
            pSelectFeatures = pSelectSource.getFeatures();
        }

        if(presetMarkerLayer != undefined) {
            var pSource = presetMarkerLayer.getSource();
            pFeatures = pSource.getFeatures();
        }

        // 프리셋 마커 생성
        if(featureType == 'presetMarker') {
            var presetMarker = new openLayers.Feature({
                geometry: new openLayers.geom.Point(feature.get('geometry').A),
                id: feature.get('id'),
                name: feature.get('name'),
                type: 'presetSelectMarker'
            });

            var netSetNumMarker = new openLayers.Feature({
                geometry: new openLayers.geom.Point(feature.get('geometry').A),
                type: 'netSetNum'
            });

            // 프리셋 마커 스타일 적용
            presetMarker.setStyle(
                new openLayers.style.Style({
                    image: new openLayers.style.Icon({
                        scale: .5,
                        opacity: 1,
                        rotateWithView: false,
                        anchor: [0.5, 1],
                        src: jQuery.fn.sysUrl + '/res/assets/img/example/preset_select.png'
                    }),
                    text: new openLayers.style.Text({
                        text: feature.get('name'),
                        offsetX: 0,
                        offsetY: -10,
                        fill: new openLayers.style.Fill({color: '#FFFFFF'}),
                        stroke: new openLayers.style.Stroke({color: '#FFFFFF'})
                    })
                })
            );
        }

        for(var i = 0; i < pFeatures.length; i++) {
            pArr.push(pFeatures[i].O.id);
        }

        for(var i = 0; i < pSelectFeatures.length; i++) {
            psArr.push(pSelectFeatures[i].O.id);
        }

        if(psArr.indexOf(feature.get('id')) != -1 && pArr.indexOf(feature.get('id')) == -1 ) {

            if(presetSelectMarkerLayer != undefined) {
                mapInfo.map.removeLayer(presetSelectMarkerLayer);
                presetSelectMarkerLayer = undefined;
            }

            clkCnt--;

            jQuery('#cctvId' + parseInt(cctvIdArr.indexOf(feature.get('id')) + 1)).val('');
            jQuery('#presetNum' + parseInt(presetNumArr.indexOf(feature.get('name')) + 1)).val('');

            pSelectFeatures.splice(psArr.indexOf(feature.get('id')), 1);
            pSelectFeatures.splice(psArr.indexOf(feature.get('num')) < psArr.indexOf(feature.get('id')) ? psArr.indexOf(feature.get('num')) : psArr.indexOf(feature.get('num'))-1 , 1);

            cctvIdArr[cctvIdArr.indexOf(feature.get('id'))] = '';
            presetNumArr[parseInt(presetNumArr.indexOf(feature.get('name')))] = '';

            if(clkCnt > 0 ) {

                presetSelectMarkerLayer = new openLayers.layer.Vector({
                    source: new openLayers.source.Vector({
                        features: pSelectFeatures
                    })
                });

                presetSelectMarkerLayer.setZIndex(1);

                mapInfo.map.addLayer(presetSelectMarkerLayer);
            }
        }

        if(psArr.indexOf(feature.get('id')) == -1 && pArr.indexOf(feature.get('id')) == -1) {

            clkCnt++;

            var idx = 0;

            for (var i = 5; i >= 1; i--) {
                if (jQuery('#cctvId' + i).val() == '') {
                    idx = i;
                }
            }

            presetMarker.set('num', idx);
            netSetNumMarker.set('id', idx);

            // 투망감시 설정 번호
            netSetNumMarker.setStyle(
                new openLayers.style.Style({
                    text: new openLayers.style.Text({
                        text: idx + '번',
                        offsetX: 0,
                        offsetY: -25,
                        fill: new openLayers.style.Fill({color: '#FF0000'}),
                        stroke: new openLayers.style.Stroke({color: '#FFFFFF'})
                    })
                })
            );

            if (idx <= 5 && idx > 0) {

                if(presetSelectMarkerLayer != undefined) {
                    mapInfo.map.removeLayer(presetSelectMarkerLayer);
                    presetSelectMarkerLayer = undefined;
                }

                cctvIdArr[idx - 1] = feature.get('id');
                presetNumArr[idx - 1] = feature.get('name');

                pSelectFeatures.push(presetMarker);
                pSelectFeatures.push(netSetNumMarker);

                jQuery('#cctvId' + idx).val(feature.get('id'));
                jQuery('#presetNum' + idx).val(feature.get('name'));

                presetSelectMarkerLayer = new openLayers.layer.Vector({
                    source: new openLayers.source.Vector({
                    features: pSelectFeatures
                    })
                });

                presetSelectMarkerLayer.setZIndex(1);

                mapInfo.map.addLayer(presetSelectMarkerLayer);
            }
        }

        if(pArr.indexOf(feature.get('id')) != -1) {
            common.setOSXModal('이미 설정한 CCTV입니다.');
        }
    }

    // 모든 프리셋 GIS 표출
    function getAllPresetGeoData(cctv_id,point_x, point_y) {

        if(presetAllMarkerLayer != undefined) {

            mapInfo.map.removeLayer(presetAllMarkerLayer);
            presetAllMarkerLayer = undefined;
        }

        var tmp;
        var tmpPoint = [];
        var wgsPoint = [];
        var tmPoint = [];
        var features = [];
        var tmpArr = [];

        tmpArr.push(+point_x);
        tmpArr.push(+point_y);
        mapInfo.map.getView().setCenter(openLayers.proj.fromLonLat(tmpArr));
        mapInfo.map.getView().setResolution(1.1943285667419434);

        if(jQuery("#cctvList select").length == 0) {
            jQuery.when(
                jQuery.ajax({
                    url: './getAllPresetGeoData',
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify({
                        cctv_id: cctv_id,
                        point_x: point_x,
                        point_y: point_y,
                        gisBoundsLeft: gisBoundsLeft,
                        gisBoundsTop: gisBoundsTop,
                        gisBoundsRight: gisBoundsRight,
                        gisBoundsBottom: gisBoundsBottom
                    }),
                    contentType: 'application/json; charset=utf-8',
                    async: false
                })
            ).then(function (data) {
                    tmp = data;
                }).fail(common.ajaxError)
                .always(function () {
                    return false;
                });


            for (var i = 0; i < tmp.length; i++) {
                tmpPoint.push(+tmp[i].point_x);
                tmpPoint.push(+tmp[i].point_y);
                wgsPoint.push(tmpPoint);
                tmPoint[i] = openLayers.proj.fromLonLat(wgsPoint[i]);
                tmpPoint = [];
            }

            var lineString = new openLayers.geom.LineString([]);

            lineString.setCoordinates(tmPoint);

            var routeCoords = lineString.getCoordinates();
            var routeLength = routeCoords.length;

            for (var i = 0; i < routeLength; i++) {
                var presetMarker = new openLayers.Feature({
                    geometry: new openLayers.geom.Point(routeCoords[i]),
                    id: tmp[i].cctv_id,
                    name: tmp[i].preset_num,
                    type: 'presetMarker'
                });

                presetMarker.setStyle(
                    new openLayers.style.Style({
                        image: new openLayers.style.Icon({
                            scale: .5,
                            opacity: 1,
                            rotateWithView: false,
                            anchor: [0.5, 1],
                            src: jQuery.fn.sysUrl+'/res/assets/img/example/preset_non_select.png'
                        }),
                        text: new openLayers.style.Text({
                            text: tmp[i].preset_num,
                            offsetX: 0,
                            offsetY: -10,
                            fill: new openLayers.style.Fill({color: '#FFFFFF'}),
                            stroke: new openLayers.style.Stroke({color: '#FFFFFF'})
                        })
                    })
                );

                features.push(presetMarker);
            }

            presetAllMarkerLayer = new openLayers.layer.Vector({
               source: new openLayers.source.Vector({
                   features: features
               })
            });

            presetAllMarkerLayer.setZIndex(1);

            mapInfo.map.addLayer(presetAllMarkerLayer);
        }
    }

    // 설정된 프리셋 GIS 표출
    function getPresetGeoData(cctv_id) {

        if(presetMarkerLayer != undefined) {

            mapInfo.map.removeLayer(presetMarkerLayer);
            presetMarkerLayer = undefined;
        }

        var tmp;
        var tmpPoint = [];
        var wgsPoint = [];
        var tmPoint = [];
        var features = [];

        if(jQuery("#cctvList select").length == 0) {
            jQuery.when(
                jQuery.ajax({
                    url: './getPresetGeoData',
                    type: 'POST',
                    dataType: 'json',
                    data: cctv_id,
                    contentType: 'application/json; charset=utf-8',
                    async: false
                })
            ).then(function (data) {
                    tmp = data;
                }).fail(common.ajaxError)
                .always(function () {
                    return false;
                });


            for (var i = 0; i < tmp.length; i++) {
                tmpPoint.push(+tmp[i].point_x);
                tmpPoint.push(+tmp[i].point_y);
                wgsPoint.push(tmpPoint);
                tmPoint[i] = openLayers.proj.fromLonLat(wgsPoint[i]);
                tmpPoint = [];
            }

            var lineString = new openLayers.geom.LineString([]);

            lineString.setCoordinates(tmPoint);

            var routeCoords = lineString.getCoordinates();
            var routeLength = routeCoords.length;

            for (var i = 0; i < routeLength; i++) {
                var presetMarker = new openLayers.Feature({
                    geometry: new openLayers.geom.Point(routeCoords[i]),
                    id: tmp[i].cctv_id,
                    name: tmp[i].preset_num,
                    type: 'presetSelectedMarker'
                });

                var netSetNumMarker = new openLayers.Feature({
                    geometry: new openLayers.geom.Point(routeCoords[i]),
                    type: 'presetSelectedNum',
                    id: tmp[i].sort_seq
                });

                presetMarker.setStyle(
                    new openLayers.style.Style({
                        image: new openLayers.style.Icon({
                            scale: .5,
                            opacity: 1,
                            rotateWithView: false,
                            anchor: [0.5, 1],
                            src: jQuery.fn.sysUrl+'/res/assets/img/example/preset_select.png'
                        }),
                        text: new openLayers.style.Text({
                            text: tmp[i].preset_num,
                            offsetX: 0,
                            offsetY: -10,
                            fill: new openLayers.style.Fill({color: '#FFFFFF'}),
                            stroke: new openLayers.style.Stroke({color: '#FFFFFF'})
                        })
                    })
                );

                netSetNumMarker.setStyle(
                    new openLayers.style.Style({
                        text: new openLayers.style.Text({
                            text: tmp[i].sort_seq + '번',
                            offsetX: 0,
                            offsetY: -25,
                            fill: new openLayers.style.Fill({color: '#FF0000'}),
                            stroke: new openLayers.style.Stroke({color: '#FFFFFF'})
                        })
                    })
                );

                features.push(presetMarker);
                features.push(netSetNumMarker);
            }

            presetMarkerLayer = new openLayers.layer.Vector({
                source: new openLayers.source.Vector({
                    features: features
                })
            });

            presetMarkerLayer.setZIndex(1);

            mapInfo.map.addLayer(presetMarkerLayer);
        }
    }

    // 설정된 투망감시 가져오기
    function netSettingList (cctv_id) {
        var tmp;
        jQuery.when(
            jQuery.ajax({
                url : './getNetSettingList',
                type : 'POST',
                dataType: 'json',
                data: cctv_id,
                contentType: 'application/json; charset=utf-8',
                async:false
            })
        ).then(function (data) {
                tmp = data;
            }).fail(common.ajaxError)
            .always(function() {
                return false;
            });

        if(tmp.length > 0 ) {
            for (var i = 0; i < tmp.length; i++) {
                jQuery('#cctvId' + tmp[i].sort_seq).val(tmp[i].cctv_id);
                jQuery('#presetNum' + tmp[i].sort_seq).val(tmp[i].preset_num);
                jQuery('#btnDel' + tmp[i].sort_seq).val(tmp[i].sort_seq);
                jQuery('#btnDel' + tmp[i].sort_seq).attr('disabled', false);
            }
            presetCnt = tmp.length;
        }
    }

    // 리턴 스크립트 체크
    function inputCheckScript(tarID) {

        switch (tarID) {
            default:

                return formcheck.checkForm(tarID);

                break;
        }
    }

    // 투망감시 설정 적용 이벤트
    function dataSend() {
        // 로딩 시작
        jQuery.fn.loadingStart();

        // 적용 모드
        if (authCrud.MOD_FL === 'N') return false;

        var rowid = jQuery('#cctvList').jqGrid('getGridParam' ,'selrow');
        var formData = jQuery('#netSetting :input');
        var reqData = formData.serializeObject();

        // 기본 입력 폼의 값(key 변경 : vo 변수명에 맞춰서)
        reqData = common.changeKeys(reqData,  [
            { k: 'cctvId1', v:'lnk_cctv_id1' },
            { k: 'presetNum1', v:'lnk_preset_num1' },
            { k: 'cctvId2', v:'lnk_cctv_id2' },
            { k: 'presetNum2', v:'lnk_preset_num2' },
            { k: 'cctvId3', v:'lnk_cctv_id3' },
            { k: 'presetNum3', v:'lnk_preset_num3' },
            { k: 'cctvId4', v:'lnk_cctv_id4' },
            { k: 'presetNum4', v:'lnk_preset_num4' },
            { k: 'cctvId5', v:'lnk_cctv_id5' },
            { k: 'presetNum5', v:'lnk_preset_num5' }
        ]);

        rowid === null ? reqData.prev_cctv_id = '' : reqData.prev_cctv_id = jQuery('#cctvList').jqGrid('getRowData', rowid).prev_cctv_id; // 모니티링 CCTV 목록 cctv_id

        // 데이터 전송
        jQuery.when(

            jQuery.ajax({
                url: './setNetSettingAct',
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
                    dataReload('cctv');
                    jQuery('#netSetting input').val('');

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

    // jqGrid data 리프레쉬
    function dataReload(tarID) {

        var listID = tarID + "List";
        var dataGrid = jQuery("#" + listID);
        var filterData = {};

        var jqOpt = {};

        switch (listID) {

            case 'cctvList' :
                // 모니터링 CCTV 목록
                filterData = jQuery('#srcPanel :input').serializeObject();

                jqOpt = {
                    url: './getCctvList'
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
            panelClear(false, 'cctvPanel', false);
            panelClear(false, 'netSettingPanel', false);

            var rowid = jQuery("#cctvList").jqGrid('getGridParam', 'selrow');

            if (rowid == null && isListReset) {

                jQuery("#cctvList").jqGrid("resetSelection"); // Grid Select Reset 처리
            }

            return false;
        }

        switch (objID) {

            case 'cctvPanel':
                // 관심차량 목록 그리드
                cctvSel = undefined;

                jQuery('#btnAdd').attr('disabled', (authCrud.REG_FL === 'Y') ? false : true); // 추가버튼 Style 변경
                //jQuery('#cctvList').jqGrid('clearGridData'); // 그리드 데이터 초기화

                break;

            case 'netSettingPanel':
                // 투망감시 설정
                common.clearElement('#' + objID); // form element

                var rowid = jQuery('#cctvList').jqGrid('getGridParam', 'selrow');

                if(presetAllMarkerLayer != undefined) {
                    mapInfo.map.removeLayer(presetAllMarkerLayer);
                    presetAllMarkerLayer = undefined;
                }

                if(presetMarkerLayer != undefined) {
                    mapInfo.map.removeLayer(presetMarkerLayer);
                    presetMarkerLayer = undefined;
                }

                if(presetSelectMarkerLayer != undefined) {
                    mapInfo.map.removeLayer(presetSelectMarkerLayer);
                    presetSelectMarkerLayer = undefined;
                }

                if(crossHairLayer != undefined) {
                    mapInfo.map.removeLayer(crossHairLayer);
                    crossHairLayer = undefined;
                    jQuery('#selectMarker').remove();
                }

                jQuery('#btnSave').attr('disabled', true);
                jQuery('#netSetting .btnDel').attr('disabled', true);

                if (rowid !== null && isListReset) {

                    jQuery('#cctvList').jqGrid('resetSelection'); // Grid Select Reset 처리

                }

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
            case 'cctvList' :
                // 모니터링 CCTV 목록
                jqOpt = {
                    url: '',
                    editurl: './setCctvAct',
                    scroll: 1,
                    scrollrows: true,
                    rowList: [10, 30, 50, 100],
                    rowNum: 10,
                    height: 320,
                    colNames: ['', '', '', '','CCTV ID', '주소', '위경도 좌표', '관리'],
                    colModel: [
                        {name: 'tmc_seq', index: 'tmc_seq', editable: true, hidden: true},
                        {name: 'prev_cctv_id', index: 'prev_cctv_id', editable: true, hidden: true},
                        {name: 'point_x', index: 'point_x', editable: true, hidden: true},
                        {name: 'point_y', index: 'point_y', editable: true, hidden: true},
                        {
                            name: 'cctv_id',
                            index: 'cctv_id',
                            width: 1,
                            align: 'center',
                            editable: true,
                            edittype: 'select',
                            sortable: false,
                            editrules: {required: true},
                            editoptions: {
                                value:{},
                                dataInit: function (el) {
                                    var obj = jQuery(el);
                                    var rowid = obj.attr('rowid');
                                    var kObj = jQuery('#' + rowid + '_cctv_id');

                                    // DB 값 세팅하기 위한 옵션
                                    obj
                                        .append(new Option(obj.parent('td').attr('title'), kObj.val(), true, true))
                                        .val(kObj.val())
                                        .trigger('change');

                                    obj.select2({
                                        minimumInputLength: 0, // 최소 검색어 갯수
                                        ajax: cctvIdSelect2()
                                    });

                                    kObj.change(function(){

                                        var cctvInfo = setCctvInfo(jQuery('#' + rowid + '_cctv_id option:selected').val());

                                        jQuery('#' + rowid + '_cctv_adres').val(cctvInfo.cctv_adres);
                                        jQuery('#' + rowid + '_point_xy').val(cctvInfo.point_xy);
                                        jQuery('#' + rowid + '_point_x').val(cctvInfo.point_x);
                                        jQuery('#' + rowid + '_point_y').val(cctvInfo.point_y);

                                        getAllPresetGeoData(jQuery('#' + cctvSel + '_cctv_id option:selected').val(), jQuery('#' + cctvSel + '_point_x').val(), jQuery('#' + cctvSel + '_point_y').val());
                                    });
                                }

                            }
                        },
                        {
                            name: 'cctv_adres',
                            index: 'cctv_adres',
                            width: 1,
                            align: 'center',
                            editable: true,
                            sortable: false,
                            editoptions: {readonly: 'readonly'}
                        },
                        {
                            name: 'point_xy',
                            index: 'point_xy',
                            width: 2,
                            align: 'center',
                            editable: true,
                            sortable: false,
                            editrules: {required: true},
                            editoptions: {readonly: 'readonly'}
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
                                    jQuery('#btnSave').attr('disabled', true);
                                    jQuery('#netSetting .btnDel').attr('disabled', true);

                                    formcheck.setEvents();

                                    if (cctvSel !== rowid) {

                                        jQuery(this).jqGrid('restoreRow', cctvSel);
                                        jqFn.jqGridListIcon(this.id, cctvSel);
                                    }

                                    cctvSel = jQuery.jgrid.jqID(rowid);

                                    panelClear(false, 'netSettingPanel', false);
                                },
                                afterRestore: function (rowid) {
                                    // 취소 버튼 클릭 시 Event
                                },
                                onSuccess: function (res) {
                                    // 저장 후 리턴 결과
                                    gridResAction(jQuery.parseJSON(res.responseText), 'cctv');
                                },
                                restoreAfterError: true, // 저장 후 입력 폼 restore 자동/수동 설정
                                delOptions: {
                                    url: './setCctvDel',
                                    mtype: 'POST',
                                    ajaxDelOptions: {contentType: "application/json", mtype: 'POST'},
                                    serializeDelData: function () {

                                        var reqData = dataGrid.jqGrid('getRowData', cctvSel);
                                        return JSON.stringify({
                                            tmc_seq: reqData.tmc_seq
                                        });
                                    },
                                    reloadAfterSubmit: false,
                                    afterComplete: function (res) {
                                        panelClear(true);
                                        dataReload('cctv');
                                        gridResAction(jQuery.parseJSON(res.responseText), 'cctv');

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
                        if (id && id !== cctvSel) {

                            if (cctvSel !== undefined) {

                                jQuery(this).jqGrid('restoreRow', cctvSel);
                                jqFn.jqGridListIcon(this.id, cctvSel);

                                // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                var tmpObj = jQuery(this).find('#' + cctvSel);

                                if (tmpObj.hasClass('success')) {

                                    tmpObj.removeClass('success').removeAttr('aria-selected');
                                }
                            }

                            cctvSel = id;

                            jQuery('#netSetting input').val('');

                            presetCnt = 0;
                            cctvIdArr = [];
                            presetNumArr = [];

                            if(jQuery('#cctvList select').length == 0 && jQuery('#cctvList').jqGrid('getRowData', cctvSel).cctv_id != '') {

                                jQuery('#netSetting .btnDel').attr('disabled', true);

                                var point = [];

                                point.push(+jQuery('#cctvList').jqGrid('getRowData',cctvSel).point_x);
                                point.push(+jQuery('#cctvList').jqGrid('getRowData',cctvSel).point_y);

                                drawCrossHair(openLayers.proj.fromLonLat(point), 630);
                                getAllPresetGeoData(jQuery('#cctvList').jqGrid('getRowData', cctvSel).cctv_id ,jQuery('#cctvList').jqGrid('getRowData', cctvSel).point_x, jQuery('#cctvList').jqGrid('getRowData', cctvSel).point_y);
                                getPresetGeoData(jQuery('#cctvList').jqGrid('getRowData', cctvSel).cctv_id);
                                netSettingList(jQuery('#cctvList').jqGrid('getRowData', cctvSel).cctv_id);


                                if(presetCnt == 5 )
                                    jQuery('#btnSave').attr('disabled', true);
                                if(presetCnt < 5 )
                                    jQuery('#btnSave').attr('disabled', false);

                            }
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

    // 투망감시설정 테이블 생성
    function netSettingTable() {

        jQuery('#netSetting').append('<tbody>');

        for(var i = 1; i < 6; i++) {

            jQuery('#netSetting').append('<tr>');

            jQuery('#netSetting tr:eq(' + parseInt(i-1) + ')').append('<th class="col-md-2">' + i + '번</th>' +
                                                      '<td class="col-md-8">' +
                                                      '<input type="text" id="cctvId' + i +'" name="cctvId' + i + '" class="form-control" readonly/>' +
                                                      '<input type="hidden" id="presetNum' + i +'" name="presetNum' + i +'">'+
                                                      '</td>' +
                                                      '<td class="col-md-2">' +
                                                      '<button type="button" id="btnDel' + i + '" class="btn btn-primary btn-sm btnDel">삭제</button>' +
                                                      '</td>');

            jQuery('#netSetting').append('</tr>');
        }

        jQuery('#netSetting').append('</tbody>');
    }

    // CCTV ID select2(ajax)
    function cctvIdSelect2(pVal) {
        if (pVal === undefined) {
            pVal = '';
        }

        return {
            url: './getCctvIdList',
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            delay: 250,
            data: JSON.stringify(function (params) {
                    return {
                        q: params.term,
                        sv: pVal
                    };
                }
            ),
            processResults: function (data) {

                return {
                    results: data
                }

            }
        }
    }

    // 모니터링 CCTV 정보 가져오기
    function setCctvInfo(cctv_id){

        var tmp;

        jQuery.when(
            jQuery.ajax({
                url : './getCctvInfo',
                type : 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data:JSON.stringify({cctv_id: cctv_id}),
                async:false
            })
        ).then(function (data) {
                tmp = data;
            }).fail(common.ajaxError)
            .always(function() {
                return false;
            });

        return tmp;
    }

    // 반경 500m 표시
    function drawCrossHair(point, radius) {

        if(jQuery('#selectMarker').length > 0) {
            jQuery('#selectMarker').remove();
        }

        if(crossHairLayer != undefined) {

            mapInfo.map.removeLayer(crossHairLayer);
            crossHairLayer = undefined;
        }

        jQuery('#overlayTmp').append('<div id="selectMarker"></div>');

        jQuery('#selectMarker').css({
            width: '64px',
            height: '64px',
            background: 'url("' + jQuery.fn.sysUrl +'/res/assets/img/example/point_select.gif") no-repeat 0% 0% transparent'
        });

        var selectCctvMarker = new ol.Overlay({
            position: point,
            element: jQuery('#selectMarker')[0],
            offset: [0, -19],
            positioning: 'center-center',
            stopEvent: false,
            insertFirst: false
        });

        mapInfo.map.addOverlay(selectCctvMarker);

        var features = [];
        var circle = new openLayers.geom.Circle(point, radius);
        var crossHairFeature = new openLayers.Feature(circle);
        crossHairFeature.setStyle(
            new openLayers.style.Style({
                fill: new openLayers.style.Fill({
                    color: 'rgba(241, 196, 15, 0.25)'
                }),
                stroke: new openLayers.style.Stroke({
                    color: 'rgba(255, 0, 0, 1)',
                    lineCap: 'round',
                    width: 3,
                    lineDash: [5, 5]
                }),
                text: new openLayers.style.Text({
                    font: '13px sans-serif',
                    text: '반경 500m',
                    stroke: new openLayers.style.Stroke({color: '#FF0000'}),
                    fill: new openLayers.style.Fill({color: '#190707'}),
                    offsetY: -60,
                    offsetX: 0
                })
            })
        );

        features.push(crossHairFeature);

        crossHairLayer = new openLayers.layer.Vector({
            source: new openLayers.source.Vector({
                features: features
            })
        });

        crossHairLayer.setZIndex(0);

        mapInfo.map.addLayer(crossHairLayer);
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
        getCctvGeoData: getCctvGeoData,
        cctvPresetClick: cctvPresetClick,
        getAllPresetGeoData: getAllPresetGeoData,
        getPresetGeoData: getPresetGeoData,
        inputCheckScript: inputCheckScript,
        setEvents: formcheck.setEvents,
        dataSend: dataSend,
        dataReload: dataReload,
        panelClear: panelClear,
        resizePanel: resizePanel,
        gridResAction: gridResAction,
        gridSetting: gridSetting,
        netSettingTable: netSettingTable,
        cctvIdSelect2: cctvIdSelect2,
        displayPlacement: displayPlacement,
        addInteraction: addInteraction
    }
});

require(['common', 'darkhand', 'local', 'openlayers', 'proj4', 'jquery', 'select2.lang'], function (common, darkhand, lc, openLayers, proj4, jQuery) {
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
                        lc.dataReload('cctv'); // 모니터링 CCTV 목록

                        jQuery.fn.loadingComplete();

                    }
                });

                tw.push({
                    chk: jQuery('#netSettingPanel'),
                    script: function () {

                        var lc = require('local');
                        return lc.inputCheckScript('netSettingPanel');
                    },
                    ret: "btnSave",
                    state: function () {
                        // 적용 Event 처리
                        var lc = require('local');
                        lc.dataSend();

                        lc.panelClear(true);

                        jQuery.fn.loadingComplete();
                    }
                })
                break;
        }

        common.enterSend(tw);
    }

    // 그리드 엔터 키 누를 경우 validation 및 저장 함수
    function gridEnterSave(listID) {

        var obj = jQuery('#' + listID);
        var id, key;

        switch (listID) {
            case 'cctv' :

                id = cctvSel;
                key = 'cctv';
                break;
        }

        if (lc.inputCheckScript(listID) === true) {

            var opers = ( jQuery("#" + id).hasClass('jqgrid-new-row') ? "add" : "edit" );

            obj.jqGrid('saveRow', id, {
                extraparam: {oper: opers},
                successfunc: function (res) {
                    // 리턴 결과
                    lc.gridResAction(jQuery.parseJSON(res.responseText), key);
                },
                restoreAfterError: false //저장 실패 시 restore기능 사용 유무(true : 입력 상태 grid 복원, false : 입력 상태 계속 유지)
            });
        }
    }

    // 페이지 로딩 완료 후 이벤트
    jQuery(function () {

        lc.netSettingTable();

        // 권한에 따른 버튼 비활성화
        if (authCrud.READ_FL === 'N') {

            jQuery('#btnSrch').attr('disabled', true);
        }
        if (authCrud.REG_FL === 'N') {

            jQuery('#btnAdd').attr('disabled', true);
            jQuery('#btnSave').attr('disabled', true);
        }
        if (authCrud.MOD_FL === 'N') {

            jQuery('#netSetting .btnDel').attr('disabled', true);
        }

        jQuery('#btnSave').attr('disabled', true);
        jQuery('#netSetting .btnDel').attr('disabled', true);

        // 엔터키 이벤트 체크
        lc.setEvents();
        enterCheck(); // 엔터 적용

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

        lc.getCctvGeoData(mapInfo); // 지도 CCTV 표출

        // GIS의 feature에 마우스 커서 놓을 시 커서 모양 변경
        mapInfo.map.on('pointermove', function(evt) {

            var feature = mapInfo.map.forEachFeatureAtPixel(evt.pixel,
                function(feature) {
                   return feature;
                });

            if(feature) {
                if (feature.get('type') == 'presetSelectMarker' || feature.get('type') == 'presetMarker' || feature.get('type') == 'presetSelectedMarker') {

                    mapInfo.map.getTargetElement().style.cursor = 'pointer';
                }
                else {

                    mapInfo.map.getTargetElement().style.cursor = '';
                }
            }
        });

        // GIS의 feature 클릭 시
        mapInfo.map.on('click', function(evt) {

            var feature = mapInfo.map.forEachFeatureAtPixel(evt.pixel,
                function(feature) {
                    return feature;
                });
            if(feature) {
                if (feature.get('type') == 'presetSelectMarker' || feature.get('type') == 'presetMarker' || feature.get('type') == 'presetSelectedMarker') {

                    lc.cctvPresetClick(feature, feature.get('type'));
                }
            }
            else {
                jQuery("#cctvList").jqGrid("resetSelection"); // Grid Select Reset 처리
                lc.panelClear(true);
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

        // 모니터링 CCTV 목록 추가 버튼 클릭 이벤트
        jQuery('#btnAdd').on('click', function () {

            lc.panelClear(true);

            jQuery.when(
                jQuery('#cctvList').jqGrid('setGridParam', {page: 1})
            ).always(function () {
                    common.addRow('cctvList', {}, function () {

                        var lc = require('local');
                        var rowid = jQuery('#cctvList').jqGrid('getGridParam', 'selrow');

                        jQuery('#' + rowid + '_cctv_id').select2({
                           minimumInputLength: 0,
                            ajax: lc.cctvIdSelect2()
                        });

                        lc.setEvents();
                    });
                });
        });

        // 투망감시 설정 삭제 버튼 클릭
        jQuery('#netSetting .btnDel').on('click', function () {

            var idx = jQuery('#netSetting .btnDel').index(this) + 1;

            jQuery.when(
                jQuery.ajax({
                    url: './setNetSettingDel',
                    type: "POST",
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify({
                        prev_cctv_id: jQuery('#cctvList').jqGrid('getRowData', cctvSel).prev_cctv_id,
                        sort_seq: jQuery('#btnDel' + idx).val()
                    })
                })
            ).then(function(data) {
                    // 결과에 따라 다음 이벤트 처리
                    if (data !== null) {

                        presetCnt--;
                        //cctvIdArr[idx-1] = '';
                        //presetNumArr[idx-1] = '';


                        jQuery('#cctvId' + idx).val('');
                        jQuery('#presetNum' + idx).val('');
                        jQuery('#btnDel' + idx).val('');

                        if(presetCnt < 5) {

                            jQuery('#btnSave').attr('disabled', false);
                            jQuery('#btnDel' + idx).attr('disabled', true);
                            lc.getPresetGeoData(jQuery('#cctvList').jqGrid('getRowData', cctvSel).cctv_id);
                        }
                        if(presetCnt == 5) {
                            jQuery('#netSetting .btnDel').attr('disabled', false);
                            jQuery('#btnSave').attr('disabled', true);
                            mapInfo.map.removeLayer(presetMarkerLayer);
                        }

                        common.setOSXModal('변경사항을 성공적으로 저장하였습니다.');

                    } else {

                        common.setOSXModal('저장이 실패하였습니다.');
                    }
                })
                .fail(common.ajaxError)
                .always(function() {

                    jQuery.fn.loadingComplete();
                    return false;
                });
        });

        // jqGrid의 입력/수정 모드 시 엔터 값 적용 하기 위한 key Event Catch
        jQuery("#cctvList").on("keydown", ':input', function (e) {

            if (e.keyCode === 13) {

                gridEnterSave('cctvList');
                return false;
            }
        });

        // 그리드 초기화
        lc.gridSetting('cctv'); // 모니터링 CCTV

        lc.displayPlacement(); // 화면 설정

        jQuery.fn.loadingComplete();
    });

    // 윈도우 화면 리사이즈 시 이벤트
    jQuery(window).bind('resize', function () {

        lc.resizePanel('cctv');
    }).trigger('resize');

});