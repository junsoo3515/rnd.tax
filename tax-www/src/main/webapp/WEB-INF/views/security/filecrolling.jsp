<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<!-- begin breadcrumb -->
<ol class="breadcrumb pull-right">
  <li><a class="h_icon" href="javascript:;"><img src="${p}/res/assets/img/home_btn.png" alt="홈버튼"></a></li>
  <li><a href="javascript:;">시스템 관리</a></li>
  <li class="active">파일수신현황</li>
</ol>
<!-- end breadcrumb -->
<!-- begin page-header -->
<h1 class="page-header">파일수신현황 <small>수집하는 체납차량 정보 수집 현황을 조회</small></h1>
<!-- end page-header -->

<!-- begin 검색 폼 panel -->
<div id="srcPanel" class="panel">
  <div class="panel-body">
    <div class="row">
      <div class="col-md-3 form-inline">
        <div class="form-group m-r-25">
          <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> 파일수신현황</label>
          <select id="srcFileCltJong" name="srcFileCltJong" class="form-control"></select>
        </div>
      </div>
      <div class="col-md-7 form-inline">
        <div class="form-group">
          <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> 기간</label>
          <div class="input-group input-daterange">
            <input type="text" id="srcSDate" name="srcSDate" class="form-control" msg="기간을" placeholder="시작일을 선택하세요" readonly="readonly" />
            <span class="input-group-addon">~</span>
            <input type="text" id="srcEDate" name="srcEDate" class="form-control" msg="기간을" placeholder="종료일을 선택하세요" readonly="readonly" />
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

<div class="row">
  <div class="col-md-8">
    <!-- begin 파일수신현황 리스트 panel -->
    <div id="fileCltResPanel" class="panel panel-inverse">
      <div class="panel-heading">
        <h4 class="panel-title">파일수신현황</h4>
      </div>
      <table id="fileCltResList"></table>
      <div id="fileCltResPager"></div>
    </div>
    <!-- end 파일수신현황 리스트 panel -->
  </div>
  <div class="col-md-4">
    <!-- begin 오류세부목록 리스트 panel -->
    <div id="fileCltErrPanel" class="panel panel-inverse">
      <div class="panel-heading">
        <h4 class="panel-title">오류세부목록</h4>
      </div>
      <table id="fileCltErrList"></table>
      <div id="fileCltErrPager"></div>
    </div>
    <!-- end 오류세부목록 리스트 panel -->
  </div>
</div>
<script type="text/javascript" charset="utf-8">
  <!--
  var authCrud = ${authCrud};

  var fileCltSel; // 파일수신현황 그리드에서 선택된 rowID

  var fileCltJongList = ${fileCltJongList}; // 파일수신현황 종류

  //-->
</script>
