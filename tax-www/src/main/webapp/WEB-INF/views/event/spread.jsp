<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<!-- begin breadcrumb -->
<ol class="breadcrumb pull-right">
  <li><a class="h_icon" href="javascript:;"><img src="${p}/res/assets/img/home_btn.png" alt="홈버튼"></a></li>
  <li><a href="javascript:;">이벤트관리</a></li>
  <li class="active">상황전파 이력조회</li>
</ol>
<!-- end breadcrumb -->
<!-- begin page-header -->
<h1 class="page-header">상황전파 이력조회 <small>식별된 차량에 대한 SMS 전송 이력 표시</small></h1>
<!-- end page-header -->

<!-- begin 검색 폼 panel -->
<div id="srcPanel" class="panel">
  <div class="panel-body">
    <div class="row">
      <div class="col-md-3 form-inline">
        <div class="form-group m-r-25">
          <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> 전파그룹</label>
          <select id="srcTcgJong" name="srcTcgJong" class="form-control"></select>
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
  <div class="col-md-12">
    <!-- begin 상황전파 이력관리 목록 리스트 panel -->
    <div id="sitPpgPanel" class="panel panel-inverse">
      <div class="panel-heading">
        <h4 class="panel-title">상황전파 이력관리 목록</h4>
      </div>
      <table id="sitPpgList"></table>
      <div id="sitPpgPager"></div>
    </div>
    <!-- end 상황전파 이력관리 목록 리스트 panel -->
  </div>
</div>
<script type="text/javascript" charset="utf-8">
  <!--
  var authCrud = ${authCrud};

  var sitPpgSel; // 상황전파 이력관리 목록 그리드에서 선택된 rowID

  var tcgJongList = ${tcgJongList}; // 전파그룹 종류

  //-->
</script>
