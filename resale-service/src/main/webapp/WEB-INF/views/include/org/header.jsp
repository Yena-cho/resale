<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<s:authentication property="principal.username" var="chaCd"/>
<s:authentication property="principal.name" var="chaName"/>
<s:authentication property="principal.vano" var="vano"/>
<s:authentication property="principal.email" var="email"/>
<s:authentication property="principal.tel" var="tel"/>
<s:authentication property="principal.loginId" var="loginId"/>

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

    <script src="/assets/js/jquery.min.js"></script>
    <script src="/assets/js/jquery-ui.min.js"></script>
    <script src="/assets/js/common.js?version=${project.version}"></script>
    <script src="/assets/js/promise.min.js"></script>
    <script src="/assets/js/moment.js"></script>

    <link rel="stylesheet" type="text/css" href="/assets/css/swiper.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css?ver=0.1">
    <link rel="stylesheet" type="text/css" href="/assets/css/animate.css">

    <link rel="apple-touch-icon" sizes="57x57" href="/assets/imgs/favicon.ico/apple-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="60x60" href="/assets/imgs/favicon.ico/apple-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/assets/imgs/favicon.ico/apple-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="/assets/imgs/favicon.ico/apple-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/assets/imgs/favicon.ico/apple-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="/assets/imgs/favicon.ico/apple-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/assets/imgs/favicon.ico/apple-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="/assets/imgs/favicon.ico/apple-icon-152x152.png">
    <link rel="apple-touch-icon" sizes="180x180" href="/assets/imgs/favicon.ico/apple-icon-180x180.png">
    <link rel="icon" type="image/png" sizes="192x192" href="/assets/imgs/favicon.ico/android-icon-192x192.png">
    <link rel="icon" type="image/png" sizes="32x32" href="/assets/imgs/favicon.ico/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="96x96" href="/assets/imgs/favicon.ico/favicon-96x96.png">
    <link rel="icon" type="image/png" sizes="16x16" href="/assets/imgs/favicon.ico/favicon-16x16.png">
    <link rel="manifest" href="/assets/imgs/favicon.ico/manifest.json">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="/assets/imgs/favicon.ico/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">
    <script type="text/javascript">
        $(document).ready(function () {
            $(".ajax-loader-area").show();
            $(".ajax-loader-area").delay(200).hide(0);

            $(".logout").click(function () {
                swal({
                    text: "로그아웃 하시겠습니까?",
                    type: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '확인',
                    cancelButtonText: '취소'
                }).then(function (result) {
                    if (!result.value) {
                        return;
                    }

                    var url = "/logout";
                    var param = {};
                    $.ajax({
                        type: "post",
                        async: true,
                        url: url,
                        contentType: "application/json; charset=UTF-8",
                        data: JSON.stringify(param),
                        success: function (data) {
                            swal({
                                type: 'success',
                                text: '로그아웃 되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (!result.value) {
                                    return;
                                }
                                
                                location.href = "/";
                            });
                        }
                    });
                });
            });

            $('#exit-run-as').click(function() {
                swal({
                    text: "관리자 계정으로 돌아가시겠습니까?",
                    type: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '확인',
                    cancelButtonText: '취소'
                }).then(function (result) {
                    if (!result.value) {
                        return;
                    }

                    window.open('/exit-run-as');
                });
            });
        });

        function fn_orgInfoPage() {
            location.href = "/org/myPage/orgInfoMgmt?chaCd=${chaCd}";
        }

    </script>

    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-122322587-1"></script>
    <script>

        window.dataLayer = window.dataLayer || [];

        function gtag() {
            dataLayer.push(arguments);
        }

        gtag('js', new Date());

        gtag('config', 'UA-122322587-1');
    </script>
</head>

<body>
<c:set var="URI" value="${pageContext.request.requestURI}"/>

<div id="page-wrapper" class="page-wrapper-pb">
    <div id="page">
        <div id="top-bar">
            <div class="container">
                <div class="row align-items-center">
                    <c:choose>
                        <c:when test="${URI == '/WEB-INF/views/common/login/memberAuth.jsp'}">
                            <div class="col">
                                <a class="navbar-brand" href="#">
                                    <img src="/assets/imgs/common/finger.png">
                                </a>
                            </div>
                        </c:when>
                        <c:when test="${URI == '/WEB-INF/views/common/login/chaAgreeCms.jsp'}">
                            <div class="col">
                                <a class="navbar-brand" href="#">
                                    <img src="/assets/imgs/common/finger.png">
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="col-md-8 col">
                                <a class="navbar-brand" href="/">
                                    <img src="/assets/imgs/common/finger.png">
                                </a>
                                <div id="login-set-in-top-bar" class="login">
                                    <img src="/assets/imgs/common/icon-user.png">
                                    <span class="mr-auto"><strong>${chaName}</strong>님 반갑습니다.</span>
                                    <button type="button" class="btn btn-xs btn-topbar ml-2" onclick="fn_orgInfoPage();">정보변경</button>
                                    <button type="button" class="btn btn-xs btn-topbar mr-auto logout">로그아웃</button>
                                    <s:authorize access="hasRole('ROLE_RUN_AS')">
                                        <button type="button" class="btn btn-xs btn-topbar mr-auto" id="exit-run-as">돌아가기</button>
                                    </s:authorize>
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
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <c:choose>
            <c:when test="${URI == '/WEB-INF/views/common/login/memberAuth.jsp'}"></c:when>
            <c:when test="${URI == '/WEB-INF/views/common/login/chaAgreeCms.jsp'}"></c:when>

            <c:otherwise>
                <nav id="nav-gnb-damoa" class="bg-primary">
                    <div class="container">
                        <div id="navbar-menus">
                            <ul class="first-depth">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="nav-link-31" aria-haspopup="true" aria-expanded="false">고객관리</a>
                                    <div class="second-depth-menu for-desktop nav-link-31">
                                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-31">
                                            <li class="second-depth-item sub-01"><a href="/org/custMgmt/custReg">고객등록</a></li>
                                            <li class="second-depth-item sub-02"><a href="/org/custMgmt/custList">고객조회</a></li>
                                        </ul>
                                    </div>
                                </li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="nav-link-32" aria-haspopup="true" aria-expanded="false">청구관리</a>
                                    <div class="second-depth-menu for-desktop nav-link-32">
                                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-32">
                                            <li class="second-depth-item sub-01"><a href="/org/claimMgmt/claimReg">청구등록</a></li>
                                            <li class="second-depth-item sub-02"><a href="/org/claimMgmt/claimSel">청구조회</a></li>
                                            <li class="second-depth-item sub-03">
                                                <a href="/org/claimMgmt/claimItemMgmt">청구항목관리</a></li>
                                        </ul>
                                    </div>
                                </li>
                                <li class="nav-item dropdown hidden-on-mobile">
                                    <a class="nav-link dropdown-toggle" href="#" id="nav-link-33" aria-haspopup="true" aria-expanded="false">고지관리</a>
                                    <div class="second-depth-menu for-desktop nav-link-33">
                                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-33">
                                            <li class="second-depth-item sub-01"><a href="/org/notiMgmt/notiInq">고지서 조회/출력</a>
                                            </li>
                                            <li class="second-depth-item sub-02"><a href="/org/notiMgmt/notiConfig">고지서설정</a>
                                            </li>
<%--                                            <li class="second-depth-item sub-03"><a href="/org/notiMgmt/notiPrintReq">고지서 출력의뢰</a></li>--%>
                                        </ul>
                                    </div>
                                </li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="nav-link-34" aria-haspopup="true" aria-expanded="false">수납관리</a>
                                    <div class="second-depth-menu for-desktop nav-link-34">
                                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-34">
                                            <li class="second-depth-item sub-01"><a href="/org/receiptMgnt/receiptList">수납내역 조회</a></li>
                                            <li class="second-depth-item sub-02"><a href="/org/receiptMgnt/paymentList">입금내역 조회</a></li>
                                            <li class="second-depth-item sub-03"><a href="/org/receiptMgnt/payItemList">항목별 입금내역</a></li>
                                            <li class="second-depth-item sub-04"><a href="/org/receiptMgnt/directReceiptReg">수기수납내역</a></li>
                                            <li class="second-depth-item sub-05"><a href="/org/receiptMgnt/refundReceipt">수기환불내역</a></li>
                                            <li class="second-depth-item sub-06"><a href="/org/receiptMgnt/cashReceipt">현금영수증</a></li>
                                        </ul>
                                    </div>
                                </li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="nav-link-35" aria-haspopup="true" aria-expanded="false">마이페이지</a>
                                    <div class="second-depth-menu for-desktop nav-link-35">
                                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-35">
                                            <li class="second-depth-item sub-01">
                                                <a href="/org/myPage/orgInfoMgmt">기본정보관리</a></li>
                                            <li class="second-depth-item sub-02">
                                                <a href="/org/myPage/serviceConfig">서비스설정</a></li>
                                            <li class="second-depth-item sub-03">
                                                <a href="/org/myPage/notiConfig">고지상세설정</a></li>
                                            <li class="second-depth-item sub-04"><a href="/org/myPage/monthSum">이용요금조회</a></li>
                                                <%--<li class="second-depth-item sub-05"><a href="/org/myPage/rcpState">수납현황분석</a></li>--%>
                                        </ul>
                                    </div>
                                </li>
                                <li class="nav-item dropdown hidden-on-mobile">
                                    <a class="nav-link dropdown-toggle" href="#" id="nav-link-06" aria-haspopup="true" aria-expanded="false">서비스안내</a>
                                    <div class="second-depth-menu for-desktop nav-link-06">
                                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                                            <li class="second-depth-item sub-01">
                                                <a href="/common/serviceGuide/infoDamoa">가상계좌 수납관리 서비스란?</a></li>
                                            <li class="second-depth-item sub-02">
                                                <a href="/common/serviceGuide/userGuide">이용안내</a></li>
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
                                <li class="nav-item dropdown mr-5">
                                    <a class="nav-link dropdown-toggle" href="#" id="nav-link-08" aria-haspopup="true" aria-expanded="false">고객지원</a>
                                    <div class="second-depth-menu for-desktop nav-link-08">
                                        <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-8">
                                            <li class="second-depth-item sub-01">
                                                <a href="/common/customerAsist/noticeList">공지사항</a></li>
                                            <li class="second-depth-item sub-02">
                                                <a href="/common/customerAsist/faqList">자주하는 질문</a></li>
                                            <li class="second-depth-item sub-04">
                                                <a href="/common/customerAsist/qnaList">서비스문의</a></li>
                                            <li class="second-depth-item sub-03">
                                                <a href="/common/customerAsist/customerCenter">고객센터</a></li>
                                        </ul>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div id="mobile-sub">
                        <div class="second-depth-menu for-mobile nav-link-31">
                            <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                                    <%-- <li class="second-depth-item"><a href="/org/custMgmt/custReg">고객등록</a></li> --%>
                                <li class="second-depth-item"><a href="/org/custMgmt/custList">고객조회</a></li>
                            </ul>
                        </div>

                        <div class="second-depth-menu for-mobile nav-link-32">
                            <ul class="second-depth-group " aria-labelledby="navbarDropdownMenuLink-1">
                                    <%-- <li class="second-depth-item"><a href="/org/claimMgmt/claimReg">청구등록</a></li> --%>
                                <li class="second-depth-item"><a href="/org/claimMgmt/claimSel">청구조회</a></li>
                                    <%-- <li class="second-depth-item"><a href="/org/claimMgmt/claimItemMgmt">청구항목관리</a></li> --%>
                            </ul>
                        </div>

                        <div class="second-depth-menu for-mobile nav-link-33">
                            <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                                <li class="second-depth-item"><a href="/org/notiMgmt/notiInq">고지서 조회/출력</a></li>
                                <li class="second-depth-item"><a href="/org/notiMgmt/notiConfig">고지서설정</a></li>
<%--                                <li class="second-depth-item"><a href="/org/notiMgmt/notiPrintReq">고지서 출력의뢰</a></li>--%>
                            </ul>
                        </div>

                        <div class="second-depth-menu for-mobile nav-link-34">
                            <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                                <li class="second-depth-item"><a href="/org/receiptMgnt/receiptList">수납내역 조회</a></li>
                                <li class="second-depth-item"><a href="/org/receiptMgnt/paymentList">입금내역 조회</a></li>
                                <%-- <li class="second-depth-item"><a href="/org/receiptMgnt/directReceiptReg">수기수납내역</a></li> --%>
                                <%-- <li class="second-depth-item"><a href="/org/receiptMgnt/refundReceipt">수기환불내역</a></li> --%>
                                <%-- <li class="second-depth-item"><a href="/org/receiptMgnt/cashReceipt">현금영수증</a></li> --%>
                            </ul>
                        </div>

                        <div class="second-depth-menu for-mobile nav-link-35">
                            <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                                <li class="second-depth-item"><a href="/org/myPage/orgInfoMgmt?chaCd=${chaCd}">기본정보관리</a></li>
                                <li class="second-depth-item"><a href="/org/myPage/serviceConfig?chaCd=${chaCd}">서비스설정</a></li>
                                    <%-- <li class="second-depth-item"><a href="/org/myPage/notiConfig?chaCd=${chaCd}">고지문구설정</a></li> --%>
                                <li class="second-depth-item"><a href="/org/myPage/monthSum">이용요금조회</a></li>
                                    <%--<li class="second-depth-item"><a href="/org/myPage/rcpState">수납현황분석</a></li>--%>
                            </ul>
                        </div>

                            <%-- <div class="second-depth-menu for-mobile nav-link-06">
                                <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                                    <li class="second-depth-item"><a href="/common/serviceGuide/infoDamoa">신한 DAMOA란?</a></li>
                                    <li class="second-depth-item"><a href="/common/serviceGuide/userGuide">이용안내</a></li>
                                </ul>
                            </div> --%>

                        <div class="second-depth-menu for-mobile nav-link-08">
                            <ul class="second-depth-group" aria-labelledby="navbarDropdownMenuLink-1">
                                <li class="second-depth-item"><a href="/common/customerAsist/noticeList">공지사항</a></li>
                                <li class="second-depth-item">
                                    <a href="/common/customerAsist/faqList" style="width: 100px;">자주하는 질문</a></li>
                                <li class="second-depth-item"><a href="/common/customerAsist/qnaList">서비스문의</a></li>
                                <li class="second-depth-item"><a href="/common/customerAsist/customerCenter">고객센터</a></li>
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
                                        <span class="mr-auto"><strong>${principal.name}</strong>님 반갑습니다.</span><br>
                                        <button type="button" class="btn btn-xs btn-topbar ml-2" onclick="fn_orgInfoPage();">정보변경</button>
                                        <button type="button" class="btn btn-xs btn-topbar logout">로그아웃</button>
                                    </div>
                                </div>
                            </div>
                            <div class="row all-links">
                                <div class="col">
                                    <ul>
                                        <li><h5>고객관리</h5></li>
                                        <li class="hidden-on-mobile"><a href="/org/custMgmt/custReg">고객등록</a></li>
                                        <li><a href="/org/custMgmt/custList">고객조회</a></li>
                                    </ul>
                                </div>
                                <div class="col">
                                    <ul>
                                        <li><h5>청구관리</h5></li>
                                        <li class="hidden-on-mobile"><a href="/org/claimMgmt/claimReg">청구등록</a></li>
                                        <li><a href="/org/claimMgmt/claimSel">청구조회</a></li>
                                        <li class="hidden-on-mobile"><a href="/org/claimMgmt/claimItemMgmt">청구항목관리</a></li>
                                    </ul>
                                </div>
                                <div class="col hidden-on-mobile">
                                    <ul>
                                        <li><h5>고지관리</h5></li>
                                        <li><a href="/org/notiMgmt/notiInq">고지서 조회/출력</a></li>
                                        <li><a href="/org/notiMgmt/notiConfig">고지서설정</a></li>
<%--                                        <li><a href="/org/notiMgmt/notiPrintReq">고지서 출력의뢰</a></li>--%>
                                    </ul>
                                </div>
                                <div class="col">
                                    <ul>
                                        <li><h5>수납관리</h5></li>
                                        <li><a href="/org/receiptMgnt/receiptList">수납내역 조회</a></li>
                                        <li><a href="/org/receiptMgnt/paymentList">입금내역 조회</a></li>
                                        <li class="hidden-on-mobile"><a href="/org/receiptMgnt/payItemList">항목별 입금내역</a></li>
                                        <li class="hidden-on-mobile"><a href="/org/receiptMgnt/directReceiptReg">수기수납내역</a></li>
                                        <li class="hidden-on-mobile"><a href="/org/receiptMgnt/refundReceipt">수기환불내역</a></li>
                                        <li class="hidden-on-mobile"><a href="/org/receiptMgnt/cashReceipt">현금영수증</a></li>
                                    </ul>
                                </div>
                                <div class="col">
                                    <ul>
                                        <li><h5>마이페이지</h5></li>
                                        <li><a href="/org/myPage/orgInfoMgmt?chaCd=${chaCd}">기본정보관리</a></li>
                                        <li><a href="/org/myPage/serviceConfig?chaCd=${chaCd}">서비스설정</a></li>
                                        <li class="hidden-on-mobile"><a href="/org/myPage/notiConfig?chaCd=${chaCd}">고지상세설정</a>
                                        </li>
                                        <li><a href="/org/myPage/monthSum">이용요금조회</a></li>
                                            <%--<li><a href="/org/myPage/rcpState">수납현황분석</a></li>--%>
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
                                <div class="col mr-auto">
                                    <ul>
                                        <li><h5>고객지원</h5></li>
                                        <li><a href="/common/customerAsist/noticeList">공지사항</a></li>
                                        <li><a href="/common/customerAsist/faqList">자주하는 질문</a></li>
                                        <li><a href="/common/customerAsist/qnaList">서비스문의</a></li>
                                        <li><a href="/common/customerAsist/customerCenter">고객센터</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </nav>

                <div class="sub-menu-area"></div>
            </c:otherwise>
        </c:choose>
            
