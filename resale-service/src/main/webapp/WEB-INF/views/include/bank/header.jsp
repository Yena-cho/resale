<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" 	   uri="http://www.springframework.org/security/tags" %>

<s:authentication property="principal.username" var="chaCd"/>
<s:authentication property="principal.name" 	var="chaName"/>
<s:authentication property="principal.loginId" 	var="loginId"/>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no">
<title>가상계좌 수납관리 서비스</title>

<link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap.css">
<link rel="stylesheet" type="text/css" href="/assets/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css?ver=0.1">

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
<script src="/assets/js/sweetalert.js"></script>
<script src="/assets/js/promise.min.js"></script>
<script src="/assets/js/moment.js"></script>

<script type="text/javascript">

$(document).ready(function() {
	$(".ajax-loader-area").show();
	$(".ajax-loader-area").delay(200).hide(0);
	
	$("#logout, #logout2").click(function(){

		swal({
		  text: "로그아웃 하시겠습니까?",
		  type: 'question',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: '확인',
		  cancelButtonText: '취소'
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
								location.href = "/bank";
							}
						});
					}
				});

		  }
		});

    });
});
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
            <div id="top-bar">
                <div class="container">
                    <div class="row align-items-center">
                        <div class="col">
                            <a class="navbar-brand" href="#">
                                <img src="/assets/imgs/common/finger.png">
                            </a>
                            <div id="login-set-in-top-bar" class="login">
                                <img src="/assets/imgs/common/icon-user.png">
                                <span class="mr-auto"><strong>${chaName}</strong>님 반갑습니다.</span>
                                <button type="button" id="logout" class="btn btn-xs btn-topbar mr-auto">로그아웃</button>
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
                            <li class="nav-item dropdown">
                                <a class="nav-link" href="/bank/selManage" id="nav-link-01" aria-haspopup="true" aria-expanded="false">
                                    가입승인관리
                                </a>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link" href="/bank/chaList" id="nav-link-02" aria-haspopup="true" aria-expanded="false">
                                    이용기관조회
                                </a>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link" href="/bank/feeList" id="nav-link-03" aria-haspopup="true" aria-expanded="false">
                                    이용요금조회
                                </a>
                            </li>
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
								<div id="login-set-in-top-bar" class="login mt-3">
	                                <img src="/assets/imgs/common/icon-user.png">
	                                <span class="mr-auto"><strong>${chaName}</strong>님 반갑습니다.</span>
	                                <button type="button" id="logout2" class="btn btn-xs btn-topbar mr-auto">로그아웃</button>
	                            </div>
							</div>
                        </div>
                        <div class="row all-links">
                            <div class="col">
                                <ul>
                                    <li><h5>가입승인관리</h5></li>
                                    <li><a href="/bank/selManage">가입승인관리</a></li>
                                </ul>
                            </div>
                            <div class="col">
                                <ul>
                                    <li><h5>이용기관조회</h5></li>
                                    <li><a href="/bank/chaList">이용기관조회</a></li>
                                </ul>
                            </div>
                            <div class="col">
                                <ul>
                                    <li><h5>이용요금조회</h5></li>
                                    <li><a href="/bank/feeList">이용요금조회</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </nav>
