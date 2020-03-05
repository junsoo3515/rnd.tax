<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<!-- begin breadcrumb -->
<ol class="breadcrumb pull-right">
    <li><a class="h_icon" href="javascript:;"><img src="${p}/res/assets/img/home_btn.png" alt="홈버튼"></a></li>
    <li><a href="javascript:;">이벤트관리</a></li>
    <li class="active">상황발생 이력조회</li>
</ol>
<!-- end breadcrumb -->
<!-- begin page-header -->
<h1 class="page-header">상황발생 이력조회<small>식별된 차량 정보에 대한 상황 발생 이력 표시</small></h1>
<!-- end page-header -->

<!-- begin 검색 폼 panel -->
<div id="srcPanel" class="panel">
    <div class="panel-body">
        <div class="row">
            <div class="col-md-3 form-inline">
                <div class="form-group m-r-25">
                    <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> 차량번호</label>
                    <input type="text" id="srcCarNo" name="srcCarNo" class="form-control">
                </div>
            </div>
            <div class="col-md-3 form-inline">
                <div class="form-group m-r-25">
                    <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> CCTV</label>
                    <input type="text" id="srcCctvInfo" name="srcCctvInfo" class="form-control">
                </div>
            </div>
            <div class="col-md-5 form-inline">
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
            <div class="col-md-1">
                <div class="pull-right">
                    <button id="btnSrch" class="btn btn-sm btn-primary"><i class="fa fa-search icon-white"></i> 검색
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- end 검색 폼 panel -->

<div class="row">
    <div class="col-md-12">
        <!-- begin 체납차량 발생현황 목록 panel -->
        <div id="taxOcrPanel" class="panel panel-inverse">
            <div class="panel-heading">
                <h4 class="panel-title">체납차량 발생현황 목록</h4>
            </div>
            <table id="taxOcrList"></table>
            <div id="taxOcrPager"></div>
        </div>
        <!-- end 체납차량 발생현황 목록 panel -->
    </div>
    <div class="col-md-8">
        <div id="timeTaxOcrChartPanel" class="panel panel-inverse">
            <div id="timeTaxOcrCharts"></div>
        </div>
    </div>
    <div class="col-md-4">
        <div id="timeTaxOcrPanel" class="panel panel-inverse">
            <div class="panel-heading">
                <h4 class="panel-title">시간별 발생현황 목록</h4>
            </div>
            <table id="timeTaxOcrList"></table>
            <div id="timeTaxOcrPager"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div id="areaOcrTopPanel" class="panel panel-inverse">
            <div class="panel-heading">
                <h4 class="panel-title">지역별 발생현황 Top 5</h4>
            </div>
            <table id="areaOcrTopList"></table>
            <div id="areaOcrTopPager"></div>
        </div>
    </div>
    <div class="col-md-6">
        <div id="cctvOcrTopPanel" class="panel panel-inverse">
            <div class="panel-heading">
                <h4 class="panel-title">CCTV별 발생현황 Top 5</h4>
            </div>
            <table id="cctvOcrTopList"></table>
            <div id="cctvOcrTopPager"></div>
        </div>
    </div>
</div>

<script type="text/javascript" charset="utf-8">
    <!--
    var authCrud = ${authCrud};
    var sitOcrSel; // 상황처리 목록 그리드에서 선택된 rowID
    //-->
</script>