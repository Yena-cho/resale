<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" 	   uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<title>가상계좌 수납관리 서비스</title>

<link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap.css">
<link rel="stylesheet" type="text/css" href="/assets/css/swiper.css">
<link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css">

<link rel="apple-touch-icon" sizes="57x57" href="/assets/imgs/favicon.ico/apple-icon-57x57.png">
<link rel="apple-touch-icon" sizes="60x60" href="/assets/imgs/favicon.ico/apple-icon-60x60.png">
<link rel="apple-touch-icon" sizes="72x72" href="/assets/imgs/favicon.ico/apple-icon-72x72.png">
<link rel="apple-touch-icon" sizes="76x76" href="/assets/imgs/favicon.ico/apple-icon-76x76.png">
<link rel="apple-touch-icon" sizes="114x114" href="/assets/imgs/favicon.ico/apple-icon-114x114.png">
<link rel="apple-touch-icon" sizes="120x120" href="/assets/imgs/favicon.ico/apple-icon-120x120.png">
<link rel="apple-touch-icon" sizes="144x144" href="/assets/imgs/favicon.ico/apple-icon-144x144.png">
<link rel="apple-touch-icon" sizes="152x152" href="/assets/imgs/favicon.ico/apple-icon-152x152.png">
<link rel="apple-touch-icon" sizes="180x180" href="/assets/imgs/favicon.ico/apple-icon-180x180.png">
<link rel="icon" type="image/png" sizes="192x192"  href="/assets/imgs/favicon.ico/android-icon-192x192.png">
<link rel="icon" type="image/png" sizes="32x32" href="/assets/imgs/favicon.ico/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="96x96" href="/assets/imgs/favicon.ico/favicon-96x96.png">
<link rel="icon" type="image/png" sizes="16x16" href="/assets/imgs/favicon.ico/favicon-16x16.png">
<link rel="manifest" href="/assets/imgs/favicon.ico/manifest.json">
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage" content="/assets/imgs/favicon.ico/ms-icon-144x144.png">
<meta name="theme-color" content="#ffffff">

<script src="/assets/js/jquery.min.js"></script>
<script src="/assets/js/jquery-ui.min.js"></script>

<script src="/assets/js/jquery.autocomplete.js"></script>
<script src="/assets/js/jquery.autocomplete.min.js"></script>
<script src="/assets/js/jquery.autocomplete.pack.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$(".ajax-loader-area").show();
	$(".ajax-loader-area").delay(200).hide(0);
	});
	function fn_loginPageMove() {
		location.href = "/common/login";
	}

</script>

	<%-- Global site tag (gtag.js) - Google Analytics --%>
	<script async src="https://www.googletagmanager.com/gtag/js?id=UA-122322587-1"></script>
	<script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'UA-122322587-1');
	</script>

</head>
<body>
    <div id="page-wrapper" class="page-wrapper-pb">
        <div id="page">
            <nav id="mobile-nav" class="navbar navbar-light">
				<div class="container">
					<div class="row no-gutters" style="width:100%;">
						<div class="col col-md-6 col-10 col-sm-12">
							<div class="align-items-center">
		                        <a class="navbar-brand" href="/">
		                            <img src="/assets/imgs/common/finger.png">
		                        </a>
		                        <c:set var="URI" value="${pageContext.request.requestURI}" />
		                        <c:if test="${URI != '/WEB-INF/views/common/login/login.jsp'}">
		                       		<button type="button" class="btn btn-xs btn-topbar" onclick="fn_loginPageMove();">로그인</button>
		                        </c:if>
		                    </div>
						</div>
						<div class="col col-md-6 col-2 text-right">
							<div id="navbar-menus" class="main-page">
		                        <ul class="first-depth navbar-nav">
		                            <li class="nav-item hidden-on-mobile dropdown">
			                            <a class="nav-link dropdown-toggle" href="#" id="nav-link-06" aria-haspopup="true" aria-expanded="false">서비스안내</a>
		                                <div class="second-depth-menu for-desktop nav-link-06">
		                                    <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-6">
		                                        <li class="second-depth-item sub-01"><a href="/common/serviceGuide/infoDamoa">가상계좌 수납관리 서비스란?</a></li>
		                                        <li class="second-depth-item sub-02"><a href="/common/serviceGuide/userGuide">이용안내</a></li>
		                                    </ul>
		                                </div>
		                            </li>

									<li class="nav-item hidden-on-mobile dropdown">
			                            <a class="nav-link dropdown-toggle" href="#" id="nav-link-07" aria-haspopup="true" aria-expanded="false">자료실</a>
		                                <div class="second-depth-menu for-desktop nav-link-07">
		                                    <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-7">
		                                        <li class="second-depth-item sub-01"><a href="/common/library">자료실</a></li>
		                                    </ul>
		                                </div>
		                            </li>

		                            <li class="nav-item hidden-on-mobile dropdown mr-5">
		                                <a class="nav-link dropdown-toggle" href="#" id="nav-link-08" aria-haspopup="true" aria-expanded="false">고객지원</a>
		                                <div class="second-depth-menu for-desktop nav-link-08">
		                                    <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-8">
		                                        <li class="second-depth-item sub-01"><a href="/common/customerAsist/noticeList">공지사항</a></li>
		                                        <li class="second-depth-item sub-02"><a href="/common/customerAsist/faqList">자주하는 질문</a></li>
		                                        <li class="second-depth-item sub-03"><a href="/common/customerAsist/customerCenter">고객센터</a></li>
		                                    </ul>
		                                </div>
		                            </li>
		                            <button id="btn-sitemap" class="btn btn-sitemap for-main ml-auto" type="button" aria-label="Toggle navigation">
										<span> </span>
										<span> </span>
										<span> </span>
		                                <span class="sr-only">Sitemap</span>
		                            </button>
		                        </ul>
		                    </div>
						</div>
					</div>


                </div>
                <div class="container">
                    <div id="mobile-sub">
                    </div>
                </div>
            </nav>

            <div id="navbarTogglerSitemap" class="main-before-login">
                <div class="container">
                    <div class="row title-area">
                        <div class="col">
                            <h2>전체메뉴</h2>
                        </div>
                    </div>
                    <div class="row all-links">
                        <div class="col">
                            <ul>
                                <li><h5>서비스안내</h5></li>
                                <li><a href="/common/serviceGuide/infoDamoa">가상계좌 수납관리 서비스란?</a></li>
                                <li><a href="/common/serviceGuide/userGuide">이용안내</a></li>
                            </ul>
                        </div>
                        <div class="col hidden-on-mobile">
                            <ul>
                                <li><h5>자료실</h5></li>
                                <li><a href="/common/library">자료실</a></li>
                            </ul>
                        </div>
                        <div class="col">
                            <ul>
                                <li><h5>고객지원</h5></li>
                                <li><a href="/common/customerAsist/noticeList">공지사항</a></li>
                                <li><a href="/common/customerAsist/faqList" >자주하는 질문</a></li>
                                <li><a href="/common/customerAsist/customerCenter" >고객센터</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

        </nav>
        <div class="sub-menu-area" style="border-bottom:0;">
        </div>
