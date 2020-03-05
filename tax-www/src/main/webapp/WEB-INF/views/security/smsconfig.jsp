<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<!-- begin breadcrumb -->
<ol class="breadcrumb pull-right">
  <li><a class="h_icon" href="javascript:;"><img src="${p}/res/assets/img/home_btn.png" alt="홈버튼"></a></li>
  <li><a href="javascript:;">시스템 관리</a></li>
  <li class="active">SMS 연락처 관리</li>
</ol>
<!-- end breadcrumb -->
<!-- begin page-header -->
<h1 class="page-header">SMS 연락처 관리 <small>SMS를 보낼 담당자들의 그룹 및 해당 그룹에 포함되는 사용자(연락처) 관리</small></h1>
<!-- end page-header -->

<!-- begin 검색 폼 panel -->
<div id="srcPanel" class="panel">
  <div class="panel-body">
    <div class="row">
      <div class="col-md-10 form-inline">
        <div class="form-group m-r-25">
          <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> 그룹명</label>
          <input type="text" id="srcContactGrp" name="srcContactGrp" class="form-control">
        </div>
        <div class="form-group">
          <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> 연락처</label>
          <input type="text" id="srcContact" name="srcContact" class="form-control">
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
  <div class="col-md-6">
    <!-- begin 그룹 리스트 panel -->
    <div id="contactGrpPanel" class="panel panel-inverse">
      <div class="panel-heading">
        <div class="btn-group pull-right">
          <button type="button" id="btnAdd1" class="btn btn-warning btn-sm"><i class="fa fa-plus icon-white"></i> 추가</button>
        </div>
        <h4 class="panel-title">그룹목록</h4>
      </div>
      <table id="contactGrpList"></table>
      <div id="contactGrpPager"></div>
    </div>
    <!-- end 그룹 리스트 panel -->
  </div>
  <div class="col-md-6">
    <!-- begin 연락처 리스트 panel -->
    <div id="contactPanel" class="panel panel-inverse">
      <div class="panel-heading">
        <div class="btn-group pull-right">
          <button type="button" id="btnAdd2" class="btn btn-warning btn-sm"><i class="fa fa-plus icon-white"></i> 추가</button>
        </div>
        <h4 class="panel-title">연락처목록</h4>
      </div>
      <table id="contactList"></table>
      <div id="contactPager"></div>
    </div>
    <!-- end 연락처 리스트 panel -->
  </div>
</div>
<script type="text/javascript" charset="utf-8">
  <!--
  var authCrud = ${authCrud};

  var contactGrpSel; // 그룹목록 그리드에서 선택된 rowID
  var contactSel; // 연락처목록 그리드에서 선택된 rowID
  var sel; // 검색, 연락처 추가, 수정, 삭제 시 그룹목록이 선택되어 있게 하기위한 그룹목록 rowID
  var nmArr = [];
  var hpArr = [];
  var partNmArr = [];
  var nm = '';

  //-->
</script>
