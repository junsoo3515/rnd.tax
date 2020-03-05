/**
 * User: 현재호
 * Date: 2017.11.13
 * Time: 오후 5:37
 */

// 지도 초기화
function mapInit(targetDiv, ol, opt) {

    if (targetDiv === undefined) targetDiv = 'map';
    if (targetDiv === null) targetDiv = 'map';

    var optLoad = opt;

    var extent = ol.proj.transformExtent([Number(optLoad.gisBoundsLeft), Number(optLoad.gisBoundsBottom), Number(optLoad.gisBoundsRight), Number(optLoad.gisBoundsTop)], 'WGS84', optLoad.gisProjection);

    var defOptions = {
        target: targetDiv,
        controls: [],
        layers: [],
        view : new ol.View({
            // center: ol.proj.transform([getLongi, getLati ], 'EPSG:4326', 'EPSG:3857'),
            //center: new ol.proj.fromLonLat([127.5, 36]),        // center 좌표
            extent: extent,
            zoom: 8,                                        // 초기화면 zoom level
            minZoom: 0,                                     // 최소 zoom level
            maxZoom: 28,                                    // 최대 zoom level
            maxResolution : 76.43702827148438,
            resolutions: [
                76.43702827148438
                , 38.21851413574219
                , 19.109257067871095
                , 9.554628533935547
                , 4.777314266967774
                , 2.388657133483887
                , 1.1943285667419434
                , 0.5971642833709717
            ]
            //maxExtent : new OpenLayers.Bounds(-20037508.34, -20037508.34, 20037508.34, 20037508.34)
            //, units : "m"
        })
    };

    // 지도 Layer 설정
    var layers = {
        normal: new ol.layer.Tile({
            title : '배경지도',
            visible : true,
            type : 'base',
            source : new ol.source.XYZ({
                //url : 'http://api.vworld.kr/req/wmts/1.0.0/' + optLoad.gisApiKey + '/Base/{z}/{y}/{x}.png'
                url : 'http://' + gisWebserviceIp + ':' + gisWebservicePort + '/tax/vworldMap?apikey=' + optLoad.gisApiKey + '&type=Base&z={z}&y={y}&x={x}'
            })
        }),
        satellite: new ol.layer.Tile({
            title: '영상지도',
            visible: true,
            type: 'base',
            source: new ol.source.XYZ({
                //url: 'http://api.vworld.kr/req/wmts/1.0.0/' + optLoad.gisApiKey + '/Satellite/{z}/{y}/{x}.jpeg'
                url : 'http://' + gisWebserviceIp + ':' + gisWebservicePort + '/tax/vworldMap?apikey=' + optLoad.gisApiKey + '&type=Satellite&z={z}&y={y}&x={x}'
            })
        }),
        hybrid: new ol.layer.Tile({
            title: '하이브리드지도',
            visible: true,
            type: 'base',
            source: new ol.source.XYZ({
                //url: 'http://api.vworld.kr/req/wmts/1.0.0/' + optLoad.gisApiKey + '/Hybrid/{z}/{y}/{x}.png'
                url : 'http://' + gisWebserviceIp + ':' + gisWebservicePort + '/tax/vworldMap?apikey=' + optLoad.gisApiKey + '&type=Hybrid&z={z}&y={y}&x={x}'
            })
        }),
        gray: new ol.layer.Tile({
            title: 'Gray지도',
            visible: true,
            type: 'base',
            source: new ol.source.XYZ({
                //url: 'http://api.vworld.kr/req/wmts/1.0.0/' + optLoad.gisApiKey + '/Gray/{z}/{y}/{x}.png'
                url : 'http://' + gisWebserviceIp + ':' + gisWebservicePort + '/tax/vworldMap?apikey=' + optLoad.gisApiKey + '&type=Gray&z={z}&y={y}&x={x}'
            })
        }),
        midnight: new ol.layer.Tile({
            title: 'Midnight지도',
            visible: true,
            type: 'base',
            source: new ol.source.XYZ({
                //url: 'http://api.vworld.kr/req/wmts/1.0.0/' + optLoad.gisApiKey + '/Midnight/{z}/{y}/{x}.png'
                url : 'http://' + gisWebserviceIp + ':' + gisWebservicePort + '/tax/vworldMap?apikey=' + optLoad.gisApiKey + '&type=Midnight&z={z}&y={y}&x={x}'
            })
        })
    };

    // vectorLayer 선언
    var vectorLayer = new ol.layer.Vector({
        source: new ol.source.Vector({
            projection: optLoad.gisProjection
        })
    });

    defOptions.layers = [layers.normal, vectorLayer];

    // 지도 생성
    var map = new ol.Map(defOptions);

    map.getView().fit( extent, map.getSize() ); // 최대 Bound에 맞춰 크기 조절

    // Zoom 버튼 추가
    var zoomctr = new ol.control.Zoom();
    map.addControl(zoomctr);

    // Zoom 슬라이드 추가
    var zoomslider = new ol.control.ZoomSlider();
    map.addControl(zoomslider);

    //map.on('movestart', moveStartAction);
    //map.on('moveend', moveEndAction);
    //map.on('zoomend', zoomAction);

    return {
        map: map,
        layers: layers
    }
}