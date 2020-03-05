<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<!-- begin breadcrumb -->
<ol class="breadcrumb pull-right" style="padding: 20px 0px;">
  <li><a class="h_icon" href="javascript:;"><img src="${p}/res/assets/img/home_btn.png" alt="홈버튼"></a></li>
  <li><a href="javascript:;">이벤트 관리</a></li>
  <li class="active">관심차량정보현황</li>
</ol>
<!-- end breadcrumb -->
<!-- begin page-header -->
<h1 class="page-header">관심차량정보현황 <small></small></h1>
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
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="vertical-box-column width-600">
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
                      <div class="form-group">
                        <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> 기간</label>

                        <div class="input-group input-daterange">
                          <input type="text" id="srcSDate" name="srcSDate" class="form-control" msg="기간을"
                                 placeholder="시작일을 선택하세요" readonly="readonly"/>
                          <span class="input-group-addon">~</span>
                          <input type="text" id="srcEDate" name="srcEDate" class="form-control" msg="기간을"
                                 placeholder="종료일을 선택하세요" readonly="readonly"/>
                        </div>
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

              <!-- begin 관심차량 리스트 panel -->
              <div id="interestPanel" class="panel panel-inverse">
                <div class="panel-heading">
                  <h4 class="panel-title">관심차량 목록</h4>
                </div>
                <table id="interestList"></table>
                <div id="interestPager"></div>
              </div>
              <!-- end 관심차량 리스트 panel -->

              <!-- begin 관심차량 이동경로 리스트 panel -->
              <div id="interestRoutePanel" class="panel panel-inverse">
                <div class="panel-heading">
                  <h4 class="panel-title">관심차량 이동경로 목록</h4>
                </div>
                <table id="interestRouteList"></table>
              </div>
              <!-- end 관심차량 이동경로 리스트 panel -->
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

  var interestSel; // 관심차량 목록 그리드에서 선택된 rowID
  var interestRouteSel; // 관심차량 이동경로 목록 그리드에서 선택된 rowID

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

  // 이동경로 레이어
  var routeLayer;

  // 지도 거리/면적 제어
  var sketch;
  var helpTooltipElement;
  var helpTooltip;
  var measureTooltipElement;
  var measureTooltip;
  var draw;
  var measureLayer;
  //-->
</script>
