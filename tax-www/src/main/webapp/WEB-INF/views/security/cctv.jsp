<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<!-- begin breadcrumb -->
<ol class="breadcrumb pull-right" style="padding: 20px 0px;">
  <li><a class="h_icon" href="javascript:;"><img src="${p}/res/assets/img/home_btn.png" alt="홈버튼"></a></li>
  <li><a href="javascript:;">시스템 관리</a></li>
  <li class="active">모니터링 CCTV 관리</li>
</ol>
<!-- end breadcrumb -->
<!-- begin page-header -->
<h1 class="page-header">모니터링 CCTV 관리 <small>지방세 체납관리 서비스에서 활용하는 CCTV 정보 관리</small></h1>
<!-- end page-header -->
<div class="vertical-box">
  <div class="vertical-box-column">
    <div class="vertical-box">
      <div id="mapPanel" class="panel panel-inverse">
        <div class="panel-heading">
          <div class="btn-group pull-right">
            <button type="button" id="btnMove" class="btn btn-primary btn-sm"><i class="fa fa-arrows icon-white"></i> 이동</button>
            <button type="button" id="btnZoomIn" class="btn btn-primary btn-sm"><i class="fa fa-search-plus icon-white"></i> 확대</button>
            <button type="button" id="btnZoomOut" class="btn btn-primary btn-sm"><i class="fa fa-search-minus icon-white"></i> 축소</button>
              <button type="button" id="btnInit" class="btn btn-primary btn-sm"><i class="fa fa-retweet icon-white"></i> 초기화</button>
            <button type="button" id="btnDistance" class="btn btn-primary btn-sm"><i class="fa fa-bars icon-white"></i> 거리</button>
            <button type="button" id="btnArea" class="btn btn-primary btn-sm"><i class="fa fa-cube icon-white"></i> 면적</button>
          </div>
          <h4 class="panel-title">
            <i class="fa fa-map-marker text-danger m-r-5"></i> 모니터링 서비스
            <span class="m-r-15"></span>
            <label class="radio-inline text-white">
              <input type="radio" name="mapSwicher" value="normal" checked="1"> 일반
            </label>
            <label class="radio-inline text-white">
              <input type="radio" name="mapSwicher" value="satellite"> 항공
            </label>
          </h4>
        </div>
      </div>
      <div class="vertical-box-row">
        <div class="vertical-box-cell">
          <div class="vertical-box-inner-cell">
            <div id="map"></div>
            <div id="overlayTmp"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="vertical-box-column width-400">
    <div class="vertical-box">
      <div class="vertical-box-row bg-white">
        <div class="vertical-box-cell">
          <div class="vertical-box-inner-cell">
            <div id="managePanel" class="panel panel-inverse">
              <!-- begin 검색 폼 panel -->
              <div id="srcPanel" class="panel">
                <div class="panel-body">
                  <div class="row">
                    <div class="col-md-10 form-inline">
                      <div class="form-group m-r-25">
                        <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i>검색어</label>
                        <input type="text" id="srcWord" name="srcWord" class="form-control">
                      </div>
                    </div>
                    <div class="col-md-2">
                      <div class="pull-right">
                        <button id="btnSrch" class="btn btn-sm btn-primary"><i class="fa fa-search icon-white"></i> 검색</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <!-- end 검색 폼 panel -->

              <!-- begin 모니터링 CCTV 리스트 panel -->
              <div id="cctvPanel" class="panel panel-inverse">
                <div class="panel-heading">
                  <div class="btn-group pull-right">
                    <button type="button" id="btnAdd" class="btn btn-primary btn-sm"><i class="fa fa-plus icon-white"></i> 추가
                    </button>
                  </div>
                  <h4 class="panel-title">모니터링 CCTV 목록</h4>
                </div>
                <table id="cctvList"></table>
                <div id="cctvPager"></div>
              </div>
              <!-- end 모니터링 CCTV 리스트 panel -->

              <!-- begin 투망감시 설정 panel -->
              <div id="netSettingPanel" class="panel panel-inverse">
                <div class="panel-heading">
                  <div class="btn-group pull-right">
                    <button type="button" id="btnSave" class="btn btn-primary btn-sm"> 설정
                    </button>
                  </div>
                  <h4 class="panel-title">투망감시 설정</h4>
                </div>
                <div class="panel-body">
                <table class="table table-bordered" id="netSetting">
                </table>
                </div>
                </div>
              </div>
              <!-- end 투망감시 설정 panel -->
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript" charset="utf-8">
  <!--
  var authCrud = ${authCrud};

  var cctvSel; // 관심차량 목록 그리드에서 선택된 rowID

  // GIS 정보
  var gisApiKey = '${gisApiKey}'; // GIS API KEY
  var gisProjection = '${gisProjection}'; // GIS 좌표계
  var gisWebserviceIp = '${gisWebserviceIp}'; // GIS Webservice IP
  var gisWebservicePort = '${gisWebservicePort}'; // GIS Webservice PORT

  // 지도범위
  var gisBoundsLeft = '${gisBoundsLeft}';
  var gisBoundsTop = '${gisBoundsTop}';
  var gisBoundsRight = '${gisBoundsRight}';
  var gisBoundsBottom = '${gisBoundsBottom}';

  // 지도 레이어 설정 및 지도
  var mapInfo;

  var cctvMarkerLayer;
  var presetAllMarkerLayer;
  var presetMarkerLayer;
  var presetSelectMarkerLayer;
  var crossHairLayer;

  // 지도 거리/면적 제어
  var sketch;
  var helpTooltipElement;
  var helpTooltip;
  var measureTooltipElement;
  var measureTooltip;
  var draw;
  var measureLayer;

  var presetCnt = 0;
  var cctvIdArr = [];
  var presetNumArr = [];
  var clkCnt = 0;
  //-->
</script>
