<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" session="false" %>

<!-- begin #header -->
<div id="header" class="header navbar navbar-default navbar-fixed-top">
  <!-- begin container-fluid -->
  <div class="container-fluid">
    <!-- begin mobile sidebar expand / collapse button -->
    <div class="navbar-header">
      <a href="${p}" class="navbar-brand"><span class="navbar-logo"></span> 체납관리 서비스</a>
      <button type="button" class="navbar-toggle" data-click="sidebar-toggled">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <button type="button" class="navbar-toggle p-0 m-r-5" data-toggle="collapse" data-target="#top-navbar">
        <span class="fa-stack fa-lg text-inverse">
            <i class="fa fa-square-o fa-stack-2x m-t-2"></i>
            <i class="fa fa-cog fa-stack-1x"></i>
        </span>
      </button>
    </div>
    <!-- end mobile sidebar expand / collapse button -->

    <!-- begin navbar-collapse -->
    <div class="collapse navbar-collapse pull-left" id="top-navbar">
      <ul class="nav navbar-nav">
        ${topMenu}
      </ul>
    </div>
    <!-- end navbar-collapse -->

    <!-- begin header navigation right -->
    <ul class="nav navbar-nav navbar-right">
      <li class="dropdown navbar-user">
        <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
          <img id="header_profile_pic" src="${p}/files/download/${member_profile_seq}" alt="" onerror="this.style.display='none';" />
          <span class="hidden-xs">${member_nm}</span> <b class="caret"></b>
        </a>
        <ul class="dropdown-menu animated fadeInLeft">
          <li class="arrow"></li>
          <li><a href="${p}/onepage/myinfo">개인정보 수정</a></li>
          <li class="divider"></li>
          <li><a href="#" id="btnLogout">로그아웃</a></li>
        </ul>
      </li>
    </ul>
    <!-- end header navigation right -->
  </div>
  <!-- end container-fluid -->
</div>
<!-- end #header -->