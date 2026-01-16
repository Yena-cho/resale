<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<s:authentication property="principal.username" var="chaCd"/>
<s:authentication property="principal.name" var="chaName"/>
<s:authentication property="principal.loginId" var="loginId"/>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no">
    <title>가상계좌 수납관리 서비스 | Admin</title>

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

    <link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap-for-admin.min.css">
    <link rel="stylesheet" type="text/css" href="/assets/font-awesome/css/font-awesome.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/animate.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/style.min.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/admin.min.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/resale-admin.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/plugins/chosen/chosen.css" />
    <link rel="stylesheet" type="text/css" href="/assets/css/jquery-ui.css" />

    <script src="/assets/js/common.js"></script>
    <script src="/assets/js/jquery.min.js"></script>
    <script src="/assets/js/jquery-ui.min.js"></script>
    <script src="/assets/js/promise.min.js"></script>
    <script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>
    <script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
    <script src="/assets/js/moment.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $(".spinner-area").show();
            $(".spinner-area").delay(500).hide(0);
        });

        function fn_logout() {

            swal({
                text: "로그아웃 하시겠습니까?",
                type: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {

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
                                if (result.value) {
                                    location.href = "/";
                                }
                            });
                        }
                    });

                }
            });

        }

    </script>


    <%-- Global site tag (gtag.js) - Google Analytics --%>
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-122322587-1"></script>
    <script>
        window.dataLayer = window.dataLayer || [];

        function gtag() {
            dataLayer.push(arguments);
        }

        gtag('js', new Date());

        gtag('config', 'UA-122322587-1');
    </script>

    <style>
        .ui-autocomplete {
            z-index: 9900;
        }
    </style>
</head>

<body>
<div id="wrapper">

    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element">
                    </div>
                    <h1 class="logo"><strong>RESALE</strong><br>${chaName}</h1>
                    <div class="logo-element">
                        <a href="/">RESALE</a>
                    </div>
                </li>
                <li id="adm-nav-1">
                    <a href="/sys/index"><i class="fa fa-dashboard"></i> <span class="nav-label">대시보드</span></a>
                </li>
                <li id="adm-nav-2">
                    <a href="#"><i class="fa fa-th-large"></i> <span class="nav-label">이용기관관리</span> <span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse">
                        <%--<li id="adm-sub-01"><a href="/sys/chaMgmt/newChaList">신규기관목록</a></li>
                        <li id="adm-sub-34"><a href="/sys/chaMgmt/changeChaList">변경대기목록</a></li>--%>
                        <li id="adm-sub-01"><a href="/sys/chaMgmt/chaList">이용기관조회</a></li>
                        <li id="adm-sub-02"><a href="/sys/chaMgmt/registCha">이용기관등록</a></li>
                        <%--<li id="adm-sub-02-1"><a href="/sys/chaMgmt/chaPartnerList">기관그룹관리</a></li>
                        <li id="adm-sub-03"><a href="/sys/chaMgmt/chaConfirm">기관검증관리</a></li>--%>
                    </ul>
                </li>
                <li id="adm-nav-3">
                    <a href="#"><i class="fa fa-bar-chart-o"></i> <span class="nav-label">고객상담관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse">
                        <li id="adm-sub-04"><a href="/sys/counselSetting">상담내역관리</a></li>
                        <%--<li id="adm-sub-05"><a href="/sys/email-sms-list">공지발송관리</a></li>--%>
                        <li id="adm-sub-05-1"><a href="/sys/email-sms-direct">개별공지발송</a></li>
                    </ul>
                </li>
                <li id="adm-nav-4">
                    <a href="#"><i class="fa fa-envelope"></i> <span class="nav-label">게시판관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse">
                        <li id="adm-sub-06"><a href="/sys/noticeSetting">공지사항관리</a></li>
                        <li id="adm-sub-07"><a href="/sys/faqSetting">자주하는질문</a></li>
                        <li id="adm-sub-08"><a href="/sys/qnaSetting">서비스문의</a></li>
                        <li id="adm-sub-09"><a href="/sys/popupSetting">팝업관리</a></li>
                        <li id="adm-sub-09-2"><a href="/sys/bannerSetting">배너관리</a></li>
                    </ul>
                </li>
                <li id="adm-nav-5">
                    <a href="#"><i class="fa fa-edit"></i> <span class="nav-label">정산관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse">
                        <li id="adm-sub-10"><a href="/sys/rcpMgmt/monthly-close">월 마감</a></li>
                        <li id="adm-sub-35"><a href="/sys/rcpMgmt/va-calculate">가상계좌 정산</a></li>
                        <li id="adm-sub-31"><a href="/sys/rcpMgmt/daily-close">일 마감</a></li>
                        <%--<li id="adm-sub-11"><a href="/sys/rcpMgmt/rcpStatisticList">수납통계</a></li>
                        <li id="adm-sub-12"><a href="/sys/rcpMgmt/commission-status">수수료통계</a></li>
                        <li id="adm-sub-13"><a href="/sys/rcpMgmt/autoTrans">자동이체출금내역</a></li>--%>
                        <%--<li id="adm-sub-15"><a href="/sys/rcpMgmt/invoice-issue-list">세금계산서발행내역</a></li>--%>
                    </ul>
                </li>
                <li id="adm-nav-6">
                    <a href="#"><i class="fa fa-desktop"></i> <span class="nav-label">부가서비스관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse">
                        <li id="adm-sub-16"><a href="/sys/addServiceMgmt/smsRegManage">문자서비스신청관리</a></li>
                        <%--<li id="adm-sub-35-1"><a href="/sys/addServiceMgmtA/atRegManage">알림톡서비스신청관리</a></li>
                        <li id="adm-sub-35-2"><a href="/sys/addServiceMgmtA/atUseList">알림톡서비스이용내역</a></li>--%>
                        <li id="adm-sub-18"><a href="/sys/addServiceMgmt/cardPayHistory">온라인카드결제이용내역</a></li>
                        <%--<li id="adm-sub-19"><a href="/sys/addServiceMgmt/notiPrintHistory">고지출력의뢰이용내역</a></li>--%>
                    </ul>
                </li>
                <li id="adm-nav-8">
                    <a href="#"><i class="fa fa-money"></i> <span class="nav-label">현금영수증 관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse">
                        <li id="adm-sub-17"><a href="/sys/cash-receipt">발행 내역</a></li>
                        <li id="adm-sub-28"><a href="/sys/cash-receipt/status-by-client">기관별 발행 현황</a></li>
                        <li id="adm-sub-26-1"><a href="/sys/cash-receipt/status-by-receipt">수납별 현황</a></li>
                        <li id="adm-sub-28-1"><a href="/sys/cash-receipt/cash-receipt-history">현금영수증 이용내역조회</a></li>
                        <li id="adm-sub-26-2"><a href="/sys/cash-receipt/status-by-issue">발행 현황</a></li>
                    </ul>
                </li>
                <%--<li id="adm-nav-9">
                    <a href="#"><i class="fa fa-files-o"></i> <span class="nav-label">자동이체관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse">
                        <li id="adm-sub-25"><a href="/sys/auto/autoTran">자동이체신청관리</a></li>
                        <li id="adm-sub-26"><a href="/sys/auto/autoTranReg">자동출금동의관리</a></li>
                        <li id="adm-sub-27"><a href="/sys/auto/autoTranTargetCha">자동이체대상관리</a></li>
                        <li id="adm-sub-29"><a href="/sys/auto/autoTranTargetFee">자동이체출금관리</a></li>
                    </ul>
                </li>--%>
                <li id="adm-nav-9">
                    <a href="#"><i class="fa fa-files-o"></i> <span class="nav-label">가상계좌관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse">
                        <li id="adm-sub-32"><a href="/sys/vanoMgmt/vanoTranCheckList">가상계좌 거래입금전문내역</a></li>
                        <li id="adm-sub-33"><a href="/sys/vanoMgmt/vanoTranInitCheck">가상계좌 거래개시전문내역</a></li>
                        <%--<li id="adm-sub-20"><a href="/sys/vanoMgmt/vanoOwnsList">가상계좌보유현황</a></li>--%>
                        <li id="adm-sub-34"><a href="/sys/vanoMgmt/chaVanoOwnsList">기관별 가상계좌 보유현황</a></li>
                        <li id="adm-sub-35"><a href="/sys/vanoMgmt/vanoIssuedList">가상계좌 발급요청관리</a></li>
                        <li id="adm-sub-40"><a href="/sys/highway/vanoSendListView">가상계좌전송</a></li>
                    </ul>
                </li>
                <li id="adm-nav-10">
                <a href="#"><i class="fa fa-files-o"></i> <span class="nav-label">고속도로관리</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                        <li id="adm-sub-41"><a href="/sys/highway/highwayRcpListView">수납누락 정보확인 및 재전송</a></li>
                        <li id="adm-sub-42"><a href="/sys/highway/vanoUseListView">가상계좌 분기별 사용량 보고</a></li>
                    </ul>
                </li>
                <li id="adm-nav-11">
                    <a href="#"><i class="fa fa-files-o"></i> <span class="nav-label">PG관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level collapse">
                        <li id="adm-sub-43"><a href="/sys/chaMgmt/newChaList">PG 신규기관목록</a></li>
                        <li id="adm-sub-44"><a href="/sys/chaMgmt/pgChaUpdate">PG 변경대기목록</a></li>
                        <li id="adm-sub-45"><a href="/sys/vanoMgmt/pgVanoSend">PG 가상계좌요청목록</a></li>

                    </ul>
                </li>


            </ul>
        </div>
    </nav>

    <div id="page-wrapper" class="gray-bg">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top white-bg" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                    <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i>
                    </a>
                </div>
                <ul class="nav navbar-top-links navbar-right">
                    <li>
                        <a href="#" onclick="fn_logout();">
                            <i class="fa fa-sign-out"></i> 로그아웃
                        </a>
                    </li>
                </ul>
            </nav>
