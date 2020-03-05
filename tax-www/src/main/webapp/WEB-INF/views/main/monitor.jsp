<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<div class="vertical-box">
    <div class="vertical-box-column">
        <div class="vertical-box">
            <div id="mapPanel" class="panel panel-inverse" style="margin-bottom: 0px;">
                <div class="panel-heading">
                    <div class="btn-group pull-right">
                        <button type="button" id="btnMove" class="btn btn-primary btn-sm"><i
                                class="fa fa-arrows icon-white"></i> 이동
                        </button>
                        <button type="button" id="btnZoomIn" class="btn btn-primary btn-sm"><i
                                class="fa fa-search-plus icon-white"></i> 확대
                        </button>
                        <button type="button" id="btnZoomOut" class="btn btn-primary btn-sm"><i
                                class="fa fa-search-minus icon-white"></i> 축소
                        </button>
                        <button type="button" id="btnInit" class="btn btn-primary btn-sm"><i
                                class="fa fa-retweet icon-white"></i> 초기화
                        </button>
                        <button type="button" id="btnDistance" class="btn btn-primary btn-sm"><i
                                class="fa fa-bars icon-white"></i> 거리
                        </button>
                        <button type="button" id="btnArea" class="btn btn-primary btn-sm"><i
                                class="fa fa-cube icon-white"></i> 면적
                        </button>
                    </div>
                    <div class="pull-right" style="height: 21px;">
                        <label class="checkbox-inline text-white">
                            <input type="checkbox" id="realtimeObserve"> 실시간감시
                        </label>
                        <label class="checkbox-inline text-white">
                            <input type="checkbox" id="disStandChk"> 분포도분석
                        </label>
                        <span class="m-r-15"></span>
                        <select class="selectpicker" id="interestSelect" multiple title="관심차량이동경로"
                                data-selected-text-format="static" data-max-options="1">
                        </select>
                        <span class="m-r-15"></span>
                        <select class="selectpicker" id="cctvTypeSelect" multiple title="레이어"
                                data-selected-text-format="static" data-actions-box="true">
                        </select>
                        <span class="m-r-15"></span>
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
                        <div id="ocrCctvPanel" class="panel panel-inverse width-450 height-350 hidden-xs"
                             style="position: absolute; left: 35px; top: 20px; background: rgba(0,0,0,.6); display:none;">
                            <div class="panel-heading">
                                <div class="panel-heading-btn">
                                    <button id="ocrCctvClose" class="btn btn-xs btn-icon btn-circle btn-danger"><i
                                            class="fa fa-times"></i></button>
                                </div>
                                <h4 class="panel-title text-danger">상황발생 CCTV 영상</h4>
                            </div>
                            <div id="view-vms" class="panel-body" style="height: 300px; padding: 0px;"></div>
                        </div>
                        <div id="routeLineDiv" class="map-float-table width-sm hidden-xs p-15" style="display: none;">
                            <h4 class="m-t-0"><i class="fa fa-map-marker text-white m-r-5"></i> 이동경로 목록</h4>

                            <div data-scrollbar="true" class="height-md">
                                <table id="routeLineList" class="table table-inverse">
                                </table>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <div id="view-castNetVms" class="bg-black-darker text-black" style="height:296px;">
                <div id="view-vms0" class="bg-black-darker text-black" style="width:20%; display: inline;"></div>
                <div id="view-vms1" class="bg-black-darker text-black" style="width:20%; display: inline;"></div>
                <div id="view-vms2" class="bg-black-darker text-black" style="width:20%; display: inline;"></div>
                <div id="view-vms3" class="bg-black-darker text-black" style="width:20%; display: inline;"></div>
                <div id="view-vms4" class="bg-black-darker text-black" style="width:20%; display: inline;"></div>
            </div>
        </div>
    </div>
    <div class="vertical-box-column width-500">
        <div class="vertical-box">
            <div class="vertical-box-row bg-white">
                <div class="vertical-box-cell">
                    <div class="vertical-box-inner-cell">
                        <div id="sitPcsPanel" class="panel panel-inverse">
                            <div class="panel-heading">
                                <h4 class="panel-title">상황처리 목록</h4>
                            </div>
                            <div id="sitPcsBlock" style="pointer-events: auto;">
                                <table id="sitPcsList"></table>
                                <div id="sitPcsPager"></div>
                            </div>
                        </div>
                        <div id="infoPanel" class="panel panel-inverse">
                            <div id="generatePanel" class="nav panel-inverse">
                                <div class="panel-heading">
                                    <div class="btn-group pull-right">
                    <span id="spanBtn">
                      <button type="button" id="btnPhoto" class="btn btn-primary btn-sm"><i
                              class="glyphicon glyphicon-camera"></i> 차량번호 사진
                      </button>
                    </span>
                                    </div>
                                    <h4 class="panel-title">체납차량 발생정보</h4>
                                </div>
                                <table class="table table-bordered" id="sitInfoForm">
                                    <tr>
                                        <th class="col-md-2">발생유형</th>
                                        <td class="col-md-10">
                                            <input type="text" id="generate_type" name="generate_type"
                                                   class="form-control" readonly/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="col-md-2">차량번호</th>
                                        <td class="col-md-10">
                                            <input type="text" id="car_no" name="car_no" class="form-control" readonly/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="col-md-2">총액(건수)</th>
                                        <td class="col-md-10">
                                            <input type="text" id="tot_money_cnt" name="tot_money_cnt"
                                                   class="form-control" readonly/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="col-md-2">발생일시 / 장소</th>
                                        <td class="col-md-10">
                                            <input type="text" id="generate_info" name="generate_info"
                                                   class="form-control" readonly/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="col-md-2">CCTV ID / CCTV명</th>
                                        <td class="col-md-10">
                                            <input type="text" id="cctv_info" name="cctv_info" class="form-control"
                                                   readonly/>
                                        </td>
                                    </tr>
                                    <%--<tr>--%>
                                    <%--<th class="col-md-2">차종</th>--%>
                                    <%--<td class="col-md-10"><input type="text" id="car_type" name="car_type" class="form-control" readonly>--%>
                                    <%--</td>--%>
                                    <%--</tr>--%>
                                    <%--<tr>--%>
                                    <%--<th class="col-md-2">차량색상</th>--%>
                                    <%--<td class="col-md-10"><input type="text" id="car_color" name="car_color" class="form-control"--%>
                                    <%--readonly></td>--%>
                                    <%--</tr>--%>
                                    <%--<tr>--%>
                                    <%--<th class="col-md-2">차량연식</th>--%>
                                    <%--<td class="col-md-10"><input type="text" id="car_md_year" name="car_md_year" class="form-control"--%>
                                    <%--readonly></td>--%>
                                    <%--</tr>--%>
                                </table>
                            </div>
                            <div id="processPanel" class="nav panel-inverse">
                                <div class="panel-heading">
                                    <div class="btn-group pull-right">
                                        <button type="button" id="btnSave" class="btn btn-primary btn-sm"><i
                                                class="glyphicon glyphicon-ok icon-white"></i> 적용
                                        </button>
                                    </div>
                                    <h4 class="panel-title">상황처리</h4>
                                </div>
                                <table class="table table-bordered" id="sitPcsInfoForm">
                                    <tr>
                                        <th class="col-md-2">승인처리 / 담당자</th>
                                        <td class="col-md-10">
                      <span>
                        <input type="checkbox" id="prc_fl" name="prc_fl"/>
                      </span>
                                            <select id="prc_user_nm" name="prc_user_nm" class="form-control"></select>
                                            <button id="prc_user_btn" class="btn btn-sm btn-white">직접입력</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="col-md-2">영치결과 / 담당자</th>
                                        <td class="col-md-10">
                      <span>
                        <input type="checkbox" id="seize_fl" name="seize_fl"/>
                      </span>
                                            <select id="seize_user_nm" name="seize_user_nm"
                                                    class="form-control"></select>
                                            <button id="seize_user_btn" class="btn btn-sm btn-white">직접입력</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th class="col-md-2">영치일자</th>
                                        <td class="col-md-10"><input type="text" id="seize_dt" name="seize_dt"
                                                                     class="form-control" isNumericOnly="1"
                                                                     maxlength="8" placeholder="영치일을 입력하세요. 년월일 8자리"></td>
                                    </tr>
                                    <tr>
                                        <th class="col-md-2">처리내용</th>
                                        <td class="col-md-10"><textarea id="prc_cont" name="prc_cont" rows="5"
                                                                        class="form-control"
                                                                        placeholder="처리내용을 입력하세요."></textarea></td>
                                    </tr>
                                </table>
                            </div>
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
    var sitPcsSel; // 상황처리 목록 그리드에서 선택된 rowID

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

    // 지도 거리/면적 제어
    var sketch;
    var helpTooltipElement;
    var helpTooltip;
    var measureTooltipElement;
    var measureTooltip;
    var draw;
    var measureLayer;

    // 웹 소켓 정보(실시간 감시)
    var wsRealtimeObserveIp = '${wsRealtimeObserveIp}';
    var wsRealtimeObservePort = '${wsRealtimeObservePort}';

    var seqArr = []; // 담당자 관리 테이블 전체 seq 담는 arr
    var nmArr = []; // 담당자 관리 테이블 전체 이름 담는 arr
    var hpArr = []; // 담당자 관리 테이블 전체 전화번호 담는 arr
    var partNmArr = []; // 담당자 관리 테이블 전체 부서명 담는 arr

    var managerList = ${managerList};

    var prev_prc_user;
    var prev_seize_user;

    var routeLayer; // 관심차량 이동경로 레이어
    var distributionLayer; // 분포도 분석 레이어
    var cctvMarkerLayer; // 상황발생 CCTV 레이어
    var reCctvMarkerLayer; // 선택한 투망감시 CCTV 레이어
    var netSetLayer; // 주변(투망감시) CCTV 레이어
    var crossHairLayer; // 반경 레이어

    var socket; // 소켓

    // VMS 정보
    var vmsUseYn = '${vmsUseYn}';
    var vmsSoftware = '${vmsSoftware}';
    var vmsIp = '${vmsIp}';
    var vmsPort = '${vmsPort}';
    var vmsId = '${vmsId}';
    var vmsPwd = '${vmsPwd}';

    var triSeq; // 차량인식 일련번호
    var noGridDataPtX; // 그리드 데이터 없을 시 가장 최근 상황 발생 한 CCTV 좌표 X
    var noGridDataPtY; // 그리드 데이터 없을 시 가장 최근 상황 발생 한 CCTV 좌표 Y
    var cctvCnt; // 선택한 레이어에 해당하는 CCTV 갯수

    // 투망감시 설정 연속 알림 방지
    var GridYn = 'Y';

    //-->
</script>