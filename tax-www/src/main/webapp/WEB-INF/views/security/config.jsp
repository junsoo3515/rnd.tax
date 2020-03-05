<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<!-- begin breadcrumb -->
<ol class="breadcrumb pull-right">
  <li><a class="h_icon" href="javascript:;"><img src="${p}/res/assets/img/home_btn.png" alt="홈버튼"></a></li>
  <li><a href="javascript:;">시스템 관리</a></li>
  <li class="active">체납차량 판별조건 관리</li>
</ol>
<!-- end breadcrumb -->
<!-- begin page-header -->
<h1 class="page-header">체납차량 판별조건 관리 <small>체납차량을 판별하기 위한 조건 설정(체납건수, 체납금액 등)</small></h1>
<!-- end page-header -->

<!-- begin 검색 폼 panel -->
<div id="srcPanel" class="panel">
  <div class="panel-body">
    <div class="row">
      <div class="col-md-10 form-inline">
        <div class="form-group m-r-25">
          <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> 판별조건</label>
          <select id="srcDis" name="srcDis" class="form-control"></select>
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

<!-- begin 체납차량 판별조건 리스트 panel -->
<div id="configPanel" class="panel panel-inverse">
  <div class="panel-heading">
    <div class="btn-group pull-right">
      <button type="button" id="btnAdd" class="btn btn-warning btn-sm"><i class="fa fa-plus icon-white"></i> 추가</button>
    </div>
    <h4 class="panel-title">체납차량 판별조건 목록</h4>
  </div>
  <table id="configList"></table>
  <div id="configPager"></div>
</div>
<!-- end 체납차량 판별조건 리스트 panel -->
<script type="text/javascript" charset="utf-8">
  <!--
  var authCrud = ${authCrud};

  var configSel; // 체납차량 판별조건 목록 그리드에서 선택된 rowID

  var disConList = ${disConList};

  //-->
</script>
