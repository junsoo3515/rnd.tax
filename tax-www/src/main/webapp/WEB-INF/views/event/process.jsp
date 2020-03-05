<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<!-- begin breadcrumb -->
<ol class="breadcrumb pull-right">
    <li><a class="h_icon" href="javascript:;"><img src="${p}/res/assets/img/home_btn.png" alt="홈버튼"></a></li>
    <li><a href="javascript:;">이벤트관리</a></li>
    <li class="active">상황처리 관리</li>
</ol>
<!-- end breadcrumb -->
<!-- begin page-header -->
<h1 class="page-header">상황처리 관리
    <small>상황처리하는 정보를 제공하고, 적발정보를 승인하며 영치결과를 등록</small>
</h1>
<!-- end page-header -->

<!-- begin 검색 폼 panel -->
<div id="srcPanel" class="panel">
    <div class="panel-body">
        <div class="row">
            <div class="col-md-3 form-inline">
                <div class="form-group m-r-25">
                    <label class="control-label m-r-10"><i class="fa fa-arrow-circle-right"></i> 발생유형</label>
                    <select id="srcMakeType" name="srcMakeType" class="form-control"></select>
                </div>
            </div>
            <div class="col-md-7 form-inline">
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
                    <button id="btnSrch" class="btn btn-sm btn-primary"><i class="fa fa-search icon-white"></i> 검색
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- end 검색 폼 panel -->

<!-- begin 권한 정보 tab header -->
<ul class="nav nav-tabs" id="myTabs">
    <li class="active"><a href="#wholePanel" data-toggle="tab">전체(<span id="whole"></span>)</a></li>
    <li class=""><a href="#occurPanel" data-toggle="tab">발생(<span id="occur"></span>)</a></li>
    <li class=""><a href="#acpcsPanel" data-toggle="tab">접수/처리(<span id="acpcs"></span>)</a></li>
    <li class=""><a href="#endPanel" data-toggle="tab">종료(<span id="end"></span>)</a></li>
</ul>
<!-- end 권한 정보 tab header -->

<div class="tab-content">
    <div id="wholePanel" class="tab-pane active">
        <div class="row">
            <div class="col-md-12">
                <!-- begin 상황처리(전체) 목록 리스트 panel -->
                <div id="sitPcs0Panel" class="panel panel-inverse">
                    <div class="panel-heading">
                        <h4 class="panel-title">상황처리 목록</h4>
                    </div>
                    <table id="sitPcs0List"></table>
                    <div id="sitPcs0Pager"></div>
                </div>
                <!-- end 상황처리(전체) 목록 리스트 panel -->
            </div>
        </div>
    </div>
    <div id="occurPanel" class="tab-pane">
        <div class="row">
            <div class="col-md-12">
                <!-- begin 상황처리(발생) 목록 리스트 panel -->
                <div id="sitPcs1Panel" class="panel panel-inverse">
                    <div class="panel-heading">
                        <h4 class="panel-title">상황처리 목록</h4>
                    </div>
                    <table id="sitPcs1List"></table>
                    <div id="sitPcs1Pager"></div>
                </div>
                <!-- end 상황처리(발생) 목록 리스트 panel -->
            </div>
        </div>
    </div>
    <div id="acpcsPanel" class="tab-pane">
        <div class="row">
            <div class="col-md-12">
                <!-- begin 상황처리(접수/처리) 목록 리스트 panel -->
                <div id="sitPcs2Panel" class="panel panel-inverse">
                    <div class="panel-heading">
                        <h4 class="panel-title">상황처리 목록</h4>
                    </div>
                    <table id="sitPcs2List"></table>
                    <div id="sitPcs2Pager"></div>
                </div>
                <!-- end 상황처리(접수/처리) 목록 리스트 panel -->
            </div>
        </div>
    </div>
    <div id="endPanel" class="tab-pane">
        <div class="row">
            <div class="col-md-12">
                <!-- begin 상황처리(종료) 목록 리스트 panel -->
                <div id="sitPcs3Panel" class="panel panel-inverse">
                    <div class="panel-heading">
                        <h4 class="panel-title">상황처리 목록</h4>
                    </div>
                    <table id="sitPcs3List"></table>
                    <div id="sitPcs3Pager"></div>
                </div>
                <!-- end 상황처리(종료) 목록 리스트 panel -->
            </div>
        </div>
    </div>
</div>
<div id="infoPanel" class="panel panel-inverse">
    <div class="panel-heading">
        <div class="btn-group-custom pull-right">
            <button id="btnSave" class="btn btn-sm btn-primary"><i
                    class="glyphicon glyphicon-ok icon-white"></i> 적용
            </button>
        </div>
        <h4 class="panel-title">상세정보</h4>
    </div>
    <div class="panel-body">
        <div class="col-md-6">
            <div id="photoPanel" class="nav panel-inverse">
                <div class="panel-heading">
                    <h4 class="panel-title">사진</h4>
                </div>
                <div id="photoDiv">
                    <ul id="photo">
                    </ul>
                </div>
                <div class="panel-heading">
                    <h4 class="panel-title">체납차량 발생위치 목록</h4>
                </div>
                <table id="taxCarFindLocList"></table>
            </div>
        </div>
        <div class="col-md-6">
            <div id="sitPcsInfoPanel" class="nav panel-inverse">
                <div class="panel-heading">
                    <h4 class="panel-title">체납정보</h4>
                </div>
                <table class="table table-bordered" id="taxInfoForm">
                    <th class="col-md-2">총 금액(건수)</th>
                    <td class="col-md-10">
                        <input type="text" id="tot_money_cnt" name="tot_money_cnt" class="form-control"
                               readonly/>
                    </td>
                </table>
                <table id="taxInfoList"></table>
                <div class="panel-heading">
                    <h4 class="panel-title">상황전파목록</h4>
                </div>
                <table id="sitPpgList"></table>

                <div class="panel-heading">
                    <div class="btn-group-custom pull-right">
                        <input type="checkbox" id="prc_fl" name="prc_fl"/>
                    </div>
                    <h4 class="panel-title">승인처리</h4>
                </div>
                <table class="table table-bordered" id="prcForm">
                    <tr>
                        <th class="col-md-2">담당자</th>
                        <td class="col-md-10">
                            <select id="prc_user_nm" name="prc_user_nm" class="form-control"
                                    style="width: 80%; display: inline;"></select>
                            <button id="prc_user_btn" class="btn btn-sm btn-white">직접입력</button>
                        </td>
                    </tr>
                </table>
                <div class="panel-heading">
                    <div class="btn-group-custom pull-right">
                        <input type="checkbox" id="seize_fl" name="seize_fl"/>
                    </div>
                    <h4 class="panel-title">영치결과</h4>
                </div>
                <table class="table table-bordered">
                    <tr>
                        <th class="col-md-2">담당자</th>
                        <td class="col-md-10">
                            <select id="seize_user_nm" name="seize_user_nm" class="form-control"
                                    style="width: 80%; display: inline;"></select>
                            <button id="seize_user_btn" class="btn btn-sm btn-white">직접입력</button>
                        </td>
                    </tr>
                    <tr>
                        <th class="col-md-2">영치일</th>
                        <td class="col-md-10">
                            <input type="text" id="seize_dt" name="seize_dt" class="form-control" isNumericOnly="1"
                                   maxlength="8" placeholder="영치일을 입력하세요."/>
                        </td>
                    </tr>
                    <tr>
                        <th class="col-md-2">처리내용</th>
                        <td class="col-md-10"><textarea class="form-control" rows="5" id="prc_cont" name="prc_cont"
                                                        placeholder="처리내용을 입력하세요."></textarea></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    <!--
    var authCrud = ${authCrud};
    var sitPcsSel; // 상황처리 목록 그리드에서 선택된 rowID
    var taxCarFindLocSel; // 체납차량 발견위치 목록 그리드에서 선택된 rowID
    var slideNum;

    var makeTypeList = ${makeTypeList};
    var managerList = ${managerList};
    var tabIdx = '0'; // 탭 인덱스

    var seqArr = []; // 담당자 관리 테이블 전체 seq 담는 arr
    var nmArr = []; // 담당자 관리 테이블 전체 이름 담는 arr
    var hpArr = []; // 담당자 관리 테이블 전체 전화번호 담는 arr
    var partNmArr = []; // 담당자 관리 테이블 전체 부서명 담는 arr

    var prcWsChk = 'prc_user_nm';
    var seizeWsChk = 'seize_user_nm';

    var prev_prc_user;
    var prev_seize_uesr;
    var taxCarPhoto; // 체납차량 사진 slippry 설정
    //-->
</script>