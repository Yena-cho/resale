<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" 	   uri="http://www.springframework.org/security/tags" %>

<s:authorize access="hasRole('ROLE_USER')">

<s:authentication property="principal.username" var="chaCd"/>
<s:authentication property="principal.name" 	var="cusName"/>
<s:authentication property="principal.vano" 	var="vaNo"/>
<s:authentication property="principal.email" 	var="email"/>
<s:authentication property="principal.tel" 		var="tel"/>
<s:authentication property="principal.unCus" 	var="unCus"/>

</s:authorize>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<title>가상계좌 수납관리 서비스</title>

<link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap.css">
<link rel="stylesheet" type="text/css" href="/assets/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css?ver=0.1">
<link rel="stylesheet" type="text/css" href="/assets/css/swiper.css">

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
<script src="/assets/js/common.js"></script>
<script src="/assets/js/promise.min.js"></script>

    <%-- Global site tag (gtag.js) - Google Analytics --%>
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-122322587-1"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'UA-122322587-1');
    </script>

</head>

<script type="text/javascript">

$(document).ready(function() {
	$(".ajax-loader-area").show();
	$(".ajax-loader-area").delay(200).hide(0);
	if($("#unCus").val() != "cus" && $("#unCus").val() != null){
        $(".uncus").attr("hidden", true);
    }

	$(".logout").click(function(){

		swal({
		  text: "로그아웃 하시겠습니까?",
		  type: 'question',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: '확인',
		  cancelButtonText: '취소',
            reverseButtons: true
		}).then(function(result) {
		  if (result.value) {

			  var url = "/logout";
				var param = {
				};
				$.ajax({
					type : "post",
					async : true,
					url : url,
					contentType : "application/json; charset=UTF-8",
					data : JSON.stringify(param),
					success: function(data){
						swal({
							type: 'success',
							text: '로그아웃 되었습니다.',
							confirmButtonColor: '#3085d6',
							confirmButtonText: '확인'
						}).then(function(result) {
							if (result.value) {
								location.href = "/";
							}
						});
					}
				});
		  }
		});
    });
});

function clickMenu(val){
	if('noti' == val){
		document.payerSess.action = '/payer/notification/notificationList';
	}else if('pay' == val){
		document.payerSess.action = '/payer/payment/payList';
	}else if('cash' == val){
		document.payerSess.action = '/payer/cashReceipt/cashReceiptReqList';
	}else if('mobile' == val){
		document.payerSess.action = '/payer/notification/mobileReciptList';
	}
	document.payerSess.submit();
}

</script>

<body>
<form name="payerSess" method="post">
<input type="hidden" name="chaCd" value="${chaCd}">
<input type="hidden" name="cusName" value="${cusName}">
<input type="hidden" name="vaNo" id="vaNo" value="${vaNo}">
<input type="hidden" name="tel" value="${tel}">
<input type="hidden" name="unCus" id="unCus" value="${unCus}">
</form>
    <div id="page-wrapper" class="page-wrapper-pb">
        <div id="page">
            <div id="top-bar">
                <div class="container">
                    <div class="row align-items-center">
                        <div class="col">
                            <a class="navbar-brand" href="/payer/main/index?second=true">
                                <img src="/assets/imgs/common/finger.png">
                            </a>
                            <div id="login-set-in-top-bar" class="login">
                                <img src="/assets/imgs/common/icon-user.png">
                                <span class="mr-auto"><strong>${cusName}</strong>님 반갑습니다.${hp}</span>
                                <button type="button" class="btn btn-xs btn-topbar mr-auto logout">로그아웃</button>
                            </div>
                        </div>
                        <div class="col text-right">
                            <span class="customer-center">고객센터 : 02-786-8201</span>
                            <button id="btn-sitemap" class="btn btn-sitemap" type="button" aria-label="Toggle navigation">
								<span> </span>
								<span> </span>
								<span> </span>
                                <span class="sr-only">Sitemap</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <nav id="nav-gnb-damoa" class="bg-primary">
                <div class="container">
                    <div id="navbar-menus">
                        <ul class="first-depth">
                            <li class="nav-item dropdown uncus">
                                <a class="nav-link dropdown-toggle" href="#" id="nav-link-21" aria-haspopup="true" aria-expanded="false">
                                    고지관리
                                </a>
                                <div class="second-depth-menu for-desktop nav-link-21">
                                    <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-21">
                                        <li class="second-depth-item sub-01"><a href="javascript:clickMenu('noti');">고지내역조회</a></li>
                                    </ul>
                                </div>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="nav-link-22" aria-haspopup="true" aria-expanded="false">
                                    납부관리
                                </a>
                                <div class="second-depth-menu for-desktop nav-link-22">
                                    <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-22">
                                        <li class="second-depth-item sub-01"><a href="javascript:clickMenu('pay');">납부내역조회</a></li>
                                    </ul>
                                </div>
                            </li>
                            <li class="nav-item dropdown uncus">
                                <a class="nav-link dropdown-toggle" href="#" id="nav-link-23" aria-haspopup="true" aria-expanded="false">
                                    현금영수증
                                </a>
                                <div class="second-depth-menu for-desktop nav-link-23">
                                    <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-23">
                                        <li class="second-depth-item sub-01"><a href="javascript:clickMenu('cash');">현금영수증조회</a></li>
                                    </ul>
                                </div>
                            </li>
                            <li class="nav-item dropdown hidden-on-mobile">
                                <a class="nav-link dropdown-toggle" href="#" id="nav-link-06" aria-haspopup="true" aria-expanded="false">
                                    서비스안내
                                </a>
                                <div class="second-depth-menu for-desktop nav-link-06">
                                    <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-6">
                                        <li class="second-depth-item sub-01"><a href="/common/serviceGuide/infoDamoa">가상계좌 수납관리 서비스란?</a></li>
                                        <li class="second-depth-item sub-02"><a href="/common/serviceGuide/userGuide">이용안내</a></li>
                                    </ul>
                                </div>
                            </li>
							<li class="nav-item hidden-on-mobile dropdown">
	                            <a class="nav-link dropdown-toggle" href="#" id="nav-link-07" aria-haspopup="true" aria-expanded="false">
	                                자료실
	                            </a>
                                <div class="second-depth-menu for-desktop nav-link-07">
                                    <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-7">
                                        <li class="second-depth-item sub-01"><a href="/common/library">자료실</a></li>
                                    </ul>
                                </div>
                            </li>
                            <li class="nav-item dropdown mr-5 hidden-on-mobile">
                                <a class="nav-link dropdown-toggle" href="#" id="nav-link-08" aria-haspopup="true" aria-expanded="false">
                                    고객지원
                                </a>
                                <div class="second-depth-menu for-desktop nav-link-08">
                                    <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-8">
                                        <li class="second-depth-item sub-01"><a href="/common/customerAsist/noticeList">공지사항</a></li>
                                        <li class="second-depth-item sub-02"><a href="/common/customerAsist/faqList">자주하는 질문</a></li>
                                        <li class="second-depth-item sub-03"><a href="/common/customerAsist/customerCenter">고객센터</a></li>
                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div id="mobile-sub" class="container">
                    <div class="second-depth-menu for-mobile nav-link-21 uncus">
                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                            <li class="second-depth-item sub-01"><a href="javascript:clickMenu('noti');">고지내역</a></li>
                            <li class="second-depth-item sub-02"><a href="javascript:clickMenu('mobile');">모바일청구서</a></li>
                        </ul>
                    </div>

                    <div class="second-depth-menu for-mobile nav-link-22">
                        <ul class="second-depth-group " aria-labelledby="navbarDropdownMenuLink-1">
                            <li class="second-depth-item sub-01"><a href="javascript:clickMenu('pay');">납부내역조회</a></li>
                        </ul>
                    </div>

                    <div class="second-depth-menu for-mobile nav-link-23 uncus">
                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                            <li class="second-depth-item sub-01"><a href="javascript:clickMenu('cash');">현금영수증조회</a></li>
                        </ul>
                    </div>

                    <div class="second-depth-menu for-mobile nav-link-06 hidden-on-mobile">
                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                            <li class="second-depth-item sub-01"><a href="/common/serviceGuide/infoDamoa">가상계좌 수납관리 서비스란?</a></li>
                            <li class="second-depth-item sub-02"><a href="/common/serviceGuide/userGuide">이용안내</a></li>
                        </ul>
                    </div>

                    <div class="second-depth-menu for-mobile nav-link-08 hidden-on-mobile">
                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                            <li class="second-depth-item sub-01"><a href="/common/customerAsist/noticeList">공지사항</a></li>
                            <li class="second-depth-item sub-02"><a href="/common/customerAsist/faqList">자주하는 질문</a></li>
                            <li class="second-depth-item sub-03"><a href="/common/customerAsist/customerCenter">고객센터</a></li>
                        </ul>
                    </div>
                </div>

                <div id="navbarTogglerSitemap">
                    <div class="container">
                        <div class="row title-area">
                            <div class="col-12">
                                <h2>전체메뉴</h2>
                            </div>
							<div class="col-12 text-center">
								<div id="login-set-in-sitemap" class="login mt-3">
	                                <img src="/assets/imgs/common/icon-user.png">
	                                <span class="mr-auto"><strong>${cusName}</strong>님 반갑습니다.${hp}</span>
	                                <button type="button" class="btn btn-xs btn-topbar logout">로그아웃</button>
	                            </div>
							</div>
                        </div>
                        <div class="row all-links">
                            <div class="col uncus">
                                <ul>
                                    <li><h5>고지관리</h5></li>
                                    <li><a href="javascript:clickMenu('noti');">고지내역조회</a></li>
									<li class="hidden-on-web show-on-mobile"><a href="javascript:clickMenu('mobile');">모바일청구서</a></li>
                                </ul>
                            </div>
                            <div class="col">
                                <ul>
                                    <li><h5>납부관리</h5></li>
                                    <li><a href="javascript:clickMenu('pay');">납부내역조회</a></li>
                                </ul>
                            </div>
                            <div class="col uncus">
                                <ul>
                                    <li><h5>현금영수증</h5></li>
                                    <li><a href="/payer/cashReceipt/cashReceiptReqList">현금영수증조회</a></li>
                                </ul>
                            </div>
                            <div class="col hidden-on-mobile">
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
                            <div class="col hidden-on-mobile">
                                <ul>
                                    <li><h5>고객지원</h5></li>
                                    <li><a href="/common/customerAsist/noticeList">공지사항</a></li>
                                    <li><a href="/common/customerAsist/faqList">자주하는 질문</a></li>
                                    <li><a href="/common/customerAsist/customerCenter">고객센터</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </nav>
            <div class="sub-menu-area">
            </div>
