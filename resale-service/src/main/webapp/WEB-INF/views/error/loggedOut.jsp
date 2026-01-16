<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/>

<%
    String error = request.getParameter("error");
    String auth = request.getParameter("auth");
%>

<script type="text/javascript">
    var firstDepth = false;
    var secondDepth = false;
</script>

<script type="text/javascript">
    $(function () {
        getCookie();
        fn_errorMsg();

        $("#bankNum").autocomplete({
            source: function (request, response) {
                if ($("#payer-login-method").val() == "payer-info") {
                    $.ajax({
                        url: "/common/login/selAutoChaName",
                        type: "post",
                        dataType: "json",
                        data: {term: request.term},
                        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                        data: request,

                        success: function (data) {
                            var result = data;
                            response(result);
                        }
                    });
                }
            }
        });
    })

    function moveFaq(no) {

        if (no == 1) {
            location.href = "/common/customerAsist/faqList";
        } else {
            location.href = "/common/customerAsist/faqList?no=" + no;
        }

    }

    function fn_errorMsg() {
        var title = "";
        var msg = "";
        var type = "";
                type = "info";
                title = "자동으로 로그아웃 되었습니다.";
                msg = "약 10분 동안 서비스 이용이 없거나 이중로그인 방지로 자동 로그아웃 됩니다.";

            swal({
                type: type,
                title: title,
                text: msg,
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function (result) {
                if (result.value) {
                    location.href = "/";
                }
            });
    }

    // 쿠키 저장
    function setCookie(name, value, expiredays) {
        var todayDate = new Date();
        todayDate.setDate(todayDate.getDate() + expiredays);
        document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";"
    }

    function getCookie() { // 쿠키 불러오는 함수
        var cookies = document.cookie.split("; ");
        for (var i = 0; i < cookies.length; i++) {
            if (cookies[i].split("=")[0] == "id") {
                document.frm.orgId.value = unescape(cookies[i].split("=")[1]);
                document.frm.remember_id.checked = true;
            } else if (cookies[i].split("=")[0] == "payid") {
                document.frm.userName.value = unescape(cookies[i].split("=")[1]);
                document.frm.remember_id.checked = true;
            }
        }
    }

    function sendit() {
        if (document.loginForm.loginGb.value == "pay") { // 납부자
            if ($("#payer-login-method").val() == "bank-number") { // 납부자 : 납부계좌번호
                if (!$("#bankNum").val()) {
                    swal({
                        type: 'info',
                        text: "납부계좌번호를 입력해주세요!",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#bankNum").focus();
                    });
                    return;
                }

                document.loginForm.userId.value = "NUM" + ":///:" + $("#bankNum").val();
                document.loginForm.userPw.value = $("#bankNum").val();
            } else if ($("#payer-login-method").val() == "payer-info") { // 납부자 : 고객정보
                if (!$("#userName").val()) {
                    swal({
                        type: 'info',
                        text: "고객명을 입력해주세요!",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#userName").focus();
                    });
                    return;
                }
                if (!$("#bankNum").val()) {
                    swal({
                        type: 'info',
                        text: "이용기관명을  입력해주세요!",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#bankNum").focus();
                    });
                    return;
                }
                if (!$("#phoneNum").val()) {
                    swal({
                        type: 'info',
                        text: "연락처를 입력해주세요!",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#phoneNum").focus();
                    });
                    return;
                }

                document.loginForm.userId.value = "CUS" + ":///:" + $("#userName").val() + ":///:" + $("#bankNum").val()+ ":///:" + $("#phoneNum").val();
                document.loginForm.userPw.value = $("#phoneNum").val();
            }

            if (document.frm.remember_id.checked == true) { // 아이디 저장을 체크 하였을때
                setCookie("payid", $("#userName").val(), 7); // 쿠키이름을 id로 아이디입력필드값을 7일동안 저장
            } else { // 아이디 저장을 체크 하지 않았을때
                setCookie("payid", $("#userName").val(), 0); // 날짜를 0으로 저장하여 쿠키삭제
            }
        } else { // 기관담당자
            var pw = '';
            var num = '';
            var eng = '';
            var spe = '';
            if (!document.frm.orgId.value) { // 아이디를 입력하지 않으면.
                swal({
                    type: 'info',
                    text: "아이디를 입력해주세요!",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function () {
                    $("#orgId").focus();
                });
                return;
            }

            // 아이디 유효성 체크
            pw = document.frm.orgId.value;
            num = pw.search(/[0-9]/g);
            eng = pw.search(/[a-zA-Z]/gi);
            spe = pw.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi);
            if (pw.length < 8 || pw.length > 20) {
                swal({
                    type: 'info',
                    text: "ID는 8자리 ~ 20자리 이내로 입력해주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function () {
                    $("#orgId").focus();
                });
                return;
            }

            if (pw.search(/\s/) != -1) {
                swal({
                    type: 'info',
                    text: "ID는 공백없이 입력해주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function () {
                    $("#orgId").focus();
                });
                return;
            }

            if (pw.search(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/) != -1) {
                swal({
                    type: 'info',
                    text: "ID는 영문,숫자 조합 8~20자리 이내로 입력해주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function () {
                    $("#orgId").focus();
                });
                return;
            }

            if (!document.frm.orgPw.value) { // 비밀번호를 입력하지 않으면.
                swal({
                    type: 'info',
                    text: "비밀번호를 입력해주세요!",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function () {
                    $("#orgPw").focus();
                });
                return;
            }

            if (document.frm.remember_id.checked == true) { // 아이디 저장을 체크 하였을때
                setCookie("id", document.frm.orgId.value, 7); // 쿠키이름을 id로 아이디입력필드값을 7일동안 저장
            } else { // 아이디 저장을 체크 하지 않았을때
                setCookie("id", document.frm.orgId.value, 0); // 날짜를 0으로 저장하여 쿠키삭제
            }

            document.loginForm.userId.value = "ORG" + ":///:" + document.frm.orgId.value;
            document.loginForm.userPw.value = document.frm.orgPw.value;
        }

        document.loginForm.action = "/login";
        document.loginForm.submit();
    }

    function loginGb(str) {
        document.loginForm.loginGb.value = str;
    }

    function fn_fullView(str) {
        document.gubunFrm.viewGb.value = str;
    }

    function fn_pageMove() {
        if (document.gubunFrm.viewGb.value == "NOTICE") { // 공지사항 전체보기
            location.href = "/common/customerAsist/noticeList";
        } else { // 자주하는 질문 전체보기
            location.href = "/common/customerAsist/faqList";
        }
    }

    // 전화상담예약
    function fn_makeReservation() {
        $('#make-reservation').modal({backdrop: 'static', keyboard: false});
    }

    // 이용신청서 다운로드
    function fn_download() {
        $('#fileName').val('credit.pdf');

        document.fileForm.action = "/common/library/download";
        document.fileForm.submit();
    }

    function onlyNumberCheck(obj) {
        $(obj).keyup(function () {
            if ($("#payer-login-method").val() == "bank-number") {
                $(this).val($(this).val().replace(/[^0-9]/g, ""));
            }
        });
    }
</script>

<form id="fileForm" name="fileForm" method="post">
    <input type="hidden" id="fileName" name="fileName">
    <input type="hidden" id="flag" name="flag" value="info">
</form>

<div id="contents">
    <div id="main-carousel">
        <div class="container">
            <div class="row">
                <%--PC용 배너--%>
                <div class="col col-12 col-sm-12 col-md-9 none-padding show-on-web hidden-on-mobile" style="height: 25rem;">
                    <div id="main-slider" class="swiper-container main-slider">
                        <div class="swiper-wrapper">
                            <c:forEach var="each" items="${map.pcBannerList }">
                                <div class="swiper-slide">
                                    <h5 class="title font-orange font-weight-500">${each.title}</h5>
                                    <p>${each.content}</p>
                                    <img src="/banner/image?id=${each.fileId}" class="slider-img-1">
                                </div>
                            </c:forEach>
                        </div>
                        <div class="swiper-pagination"></div>
                    </div>
                </div>

                <%--모바일용 배너--%>
                <div class="col col-12 col-sm-12 col-md-9 none-padding hidden-on-web show-on-mobile" style="height: 25rem;">
                    <div id="main-slider" class="swiper-container main-slider">
                        <div class="swiper-wrapper">
                            <c:forEach var="each" items="${map.mobileBannerList }">
                                <div class="swiper-slide">
                                    <h5 class="title font-orange font-weight-500">${each.title}</h5>
                                    <p>${each.content}</p>
                                    <img src="/banner/image?id=${each.fileId}" class="slider-img-1">
                                </div>
                            </c:forEach>
                        </div>
                        <div class="swiper-pagination"></div>
                    </div>
                </div>

                <div class="col col-12 col-sm-12 col-md-3 d-none d-md-block none-padding" style="margin-top: 40px;">
                    <div id="login-box-in-main">
                        <form id="loginForm" name="loginForm" method="post">
                            <input type="hidden" id="userId" name="userId">
                            <input type="hidden" id="userPw" name="userPw">
                            <input type="hidden" id="loginGb" name="loginGb" value="org">
                        </form>

                        <form id="frm" name="frm">
                            <h3><img src="/assets/imgs/common/icon-login-lock.png"> 로그인</h3>
                            <div class="tab-selecter">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#collecter" onclick="loginGb('org')">기관담당자</a></li>
                                    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#payer" onclick="loginGb('pay')">납부자</a></li>
                                </ul>
                            </div>
                            <div class="tab-content">
                                <div id="collecter" class="container-fluid tab-pane fade show active">
                                    <div class="row form-group">
                                        <div class="col">
                                            <input type="text" class="form-control" placeholder="아이디" maxlength="20" id="orgId" name="orgId">
                                        </div>
                                    </div>
                                    <div class="row form-group">
                                        <div class="col">
                                            <input type="password" class="form-control" placeholder="비밀번호" maxlength="20" id="orgPw" name="orgPw" onkeypress="if(event.keyCode == 13){sendit();}">
                                        </div>
                                    </div>
                                </div>

                                <div id="payer" class="container-fluid tab-pane fade">
                                    <div class="row form-group">
                                        <div class="col">
                                            <select id="payer-login-method" class="form-control">
                                                <option value="bank-number">납부계좌번호</option>
                                                <option value="payer-info">고객정보</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="row form-group">
                                        <div class="col">
                                            <input class="second-row-input form-control" placeholder="납부계좌번호 (-)제외" name="bankNum" id="bankNum" type="tel" maxlength="20" onkeypress="if(event.keyCode == 13){sendit();}" onkeydown="onlyNumberCheck(this)">
                                        </div>
                                    </div>

                                    <div id="contact" class="row form-group">
                                        <div class="col form-group">
                                            <input type="text" class="form-control" placeholder="고객명" name="userName" id="userName" maxlength="20">
                                        </div>

                                        <div class="col">
                                            <input type="tel" class="form-control" placeholder="연락처 (-)제외" name="phoneNum" onkeydown="onlyNumber(this)" id="phoneNum" maxlength="11" onkeypress="if(event.keyCode == 13){sendit();}">
                                        </div>
                                    </div>
                                </div>

                                <div class="container-fluid">
                                    <div class="row form-group">
                                        <div class="col">
                                            <button type="button" class="btn btn-block btn-login" onclick="sendit();">로그인</button>
                                        </div>
                                    </div>

                                    <div class="row form-group id-option mt-1">
                                        <div class="col-12">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" id="remember_id" name="remember_id">
                                                <label class="form-check-label" for="remember_id" style="font-size: 11px"><span></span> 아이디저장</label>
                                                <a href="common/idReset" id="find-id-pw" class="ml-auto" style="font-size: 12px; line-height: 1.7;">아이디/비밀번호 찾기 &#62;</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="section-container">
        <div class="section">
            <div class="container-fluid" class="col-12" style="border-bottom:2px solid #ededed; margin-bottom: 2.75rem;">
                <div class="container">
                    <div class="row">
                        <div class="col-12">
                            <h3 class="section-title font-weight-500">더 편리해진 <span class="font-blue">가상계좌 수납관리 서비스</span>를 소개합니다!</h3>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container">
                <div class="row summary">
                    <div class="col col-6 col-sm-6 col-md-3 text-center mb-4">
                        <img src="/assets/imgs/icon-main-col-1.png" class="icon">
                        <div class="keyword">통합 수납관리</div>
                        <p>가상계좌 수납관리 서비스에서<br/>수납관리를 한번에!</p>
                    </div>

                    <div class="col col-6 col-sm-6 col-md-3 text-center mb-4">
                        <img src="/assets/imgs/icon-main-col-3.png" class="icon">
                        <div class="keyword">쉬운 가상계좌</div>
                        <p>다양한 설정으로<br/>수납을 편리하게!</p>
                    </div>

                    <div class="col col-6 col-sm-6 col-md-3 text-center mb-4">
                        <img src="/assets/imgs/icon-main-col-2.png" class="icon">
                        <div class="keyword">고객정보 관리</div>
                        <p>분리된 고객/청구 등록으로<br/>체계적인 관리를!</p>
                    </div>

                    <div class="col col-6 col-sm-6 col-md-3 text-center mb-4">
                        <img src="/assets/imgs/icon-main-col-4.png" class="icon">
                        <div class="keyword">모바일 가상계좌 수납관리 서비스</div>
                        <p>언제, 어디서나<br/>모바일로 조회를!</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <form name="gubunFrm" id="gubunFrm">
        <input type="hidden" name="viewGb" id="viewGb" value="NOTICE">
    </form>

    <div class="container">
        <div class="row mt-5">
            <div class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row">
                    <div class="col col-12">
                        <div class="title-sol-type orange">
                            <span>가상계좌 수납관리 서비스의 최신 소식을 알려드려요!</span>
                            <h4>공지사항</h4>
                        </div>
                        <div class="see-all text-right">
                            <a href="/common/customerAsist/noticeList">> 전체보기</a>
                        </div>
                    </div>
                </div>
                <div class="row mt-2 mb-5">
                    <div class="col col-4">
                        <img src="/assets/imgs/common/fin.png" style="width:100%;">
                    </div>
                    <div class="col col-8">
                        <table id="notice" class="table table-sm table-hover table-summary">
                            <tbody>
                            <c:choose>
                                <c:when test="${empty map.nList}">
                                    <tr>
                                        <td colspan="2" style="text-align: center;">[조회된 내역이 없습니다.]</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="list" items="${map.nList}">
                                        <tr>
                                            <td class="has-ellipsis has-arrow">
                                                <a class="text-secondary"
                                                   href="/common/customerAsist/noticeDetail?no=${list.no}">
                                                    <c:choose>
                                                        <c:when test="${list.title.length() > 15}">
                                                            ${list.title.substring(0,15) }...
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${list.title}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </a>
                                                <c:if test="${list.filesize > 0}">
                                                    <img src="/assets/imgs/common/icon-clip.png" class="icon-clip">
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row">
                    <div class="col col-12">
                        <div class="title-sol-type navy">
                            <span>이용에 어려움이 있으신가요?</span>
                            <h4>자주하는 질문</h4>
                        </div>
                    </div>
                </div>
                <div id="faq" class="row mt-2 mb-5 no-gutters">
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(1)">전체</div>
                    </div>
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(2)">신청/해지</div>
                    </div>
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(3)">납부</div>
                    </div>
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(4)">사이트 이용</div>
                    </div>
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(5)">고지</div>
                    </div>
                    <div class="col col-4 text-right">
                        <img src="/assets/imgs/common/img-sol-character-01.png" style="max-width:107px;" class="sol-character">
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt-5 hidden-on-mobile">
            <div id="application-download" class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row mb-2">
                    <div class="col col-12">
                        <div class="title-sol-type green">
                            <span>이용방법이 궁금하세요?</span>
                            <h4>이용신청서 다운로드</h4>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <div class="row application-box">
                            <div class="col col-6">
                                <label>가상계좌 수납관리 서비스 이용신청서</label>
<%--                                <button type="button" class="btn btn-block btn-cs-center btn-download-application" onclick="fn_download();">다운로드</button>--%>
                            </div>
                            <div class="col col-6 text-right">
                                <img src="/assets/imgs/common/img-download-certificate.png" style="height:93px; width:92px;">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="cs-center" class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row mb-2">
                    <div class="col col-12">
                        <div class="title-sol-type sand">
                            <span>전문 상담원이 안내해드립니다.</span>
                            <h4>고객센터</h4>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <div class="row cs-box">
                            <div class="col col-12">
                                <label>전화상담을 통해서 친절히 안내해 드립니다.</label>
                            </div>
                            <div class="col col-6">
                                <a href="tel:02-786-8201" class="btn btn-block btn-cs-center btn-cs-phone-number">02-786-8201</a>
                            </div>
                            <div class="col col-6">
                                <button type="button" class="btn btn-block btn-cs-center btn-book-counselling" data-toggle="modal" onclick="fn_makeReservation();">전화 상담 예약</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="popup-wrapper" id="popup_area" style="display: none;">
    <c:forEach var="row" items="${map.pList }">
        <div class="popup-unit" id="popup_${row.no }" style="display: none">
            <c:if test="${row.urlYn == 'O'}">
                <a href="${row.url }" target="_blank">
                    <div class="popup-img-area" onkeydown="popupImgView('${row.filename}', '${row.no}');">
                        <div id="popupImageContainer${row.no}"></div>
                    </div>
                </a>
            </c:if>

            <c:if test="${row.urlYn == 'X'}">
                <div class="popup-img-area" onkeydown="popupImgView('${row.filename}', '${row.no}');">
                    <div id="popupImageContainer${row.no}"></div>
                </div>
            </c:if>

            <div class="control-bar">
                <input class="form-check-input" type="checkbox" id="${row.no }" value="option2" onclick="dontShow1(${row.no })">
                <label for="${row.no }"><span></span></label>
                <span class="text">하루동안 보지 않겠습니다.</span>
                <button type="button" id="clolseBtn${row.no }" class="close" aria-label="Close" onclick="regCookies(${row.no})"><span aria-hidden="true">&times;</span></button>
            </div>
        </div>
    </c:forEach>
</div>

<div class="browser-recommend-area" id="exVersion_area" style="display: none;">
    <div id="browser-recommend" class="container">
        <button type="button" class="close" aria-label="Close" style="position:absolute; right:1.5rem; top:1rem;"
                onclick="closeVirsionPopup()">
            <span aria-hidden="true">&times;</span>
        </button>
        <div class="row">
            <div class="col-12 mb-2 text-center">
                <img src="/assets/imgs/common/img-browser-recommend.png" style="height:168px;">
            </div>
            <div class="col-12 text-center">
                <span>빠르고 안정적인 이용환경을 위해 익스플로러 11 또는 크롬 브라우저 이용을 권장합니다.</span>
            </div>
        </div>
        <div class="row text-center">
            <div class="col-12">
                <h3 class="display-5">“브라우저를 업그레이드해 주세요.”</h3>
                <p>
                    가상계좌 수납관리 서비스는 IE11 버전에 최적화 되어 있습니다.<br>
                    더 빠르고, 안정적인 가상계좌 수납관리 서비스 이용을 위해 업데이트를 권장합니다.<br>
                    궁금한 점이 있으시면, 언제든지 가상계좌 수납관리 서비스 고객센터(TEL. 02-786-8201)로 연락주시기 바랍니다.
                </p>
            </div>
        </div>
        <div class="row text-center mb-4">
            <div class="col-12">
                <a href="https://support.microsoft.com/ko-kr/help/17621/internet-explorer-downloads" target="_self"
                   class="btn btn-primary btn-outlined">Internet Explorer 11 설치 바로가기</a>
                <a href="https://www.google.co.kr/chrome/index.html" class="btn btn-primary btn-outlined">Chrome
                    Browser 설치 바로가기</a>
            </div>
        </div>
        <div class="row text-center">
            <div class="col-12">
                <input class="form-check-input" type="checkbox" onclick="exVirsionCheck()" value=""
                       id="never-shown">
                <label class="form-check-label mr-1" for="never-shown"><span></span>
                    하루동안 보지 않겠습니다.
                </label>
            </div>
        </div>
    </div>

    <div class="modal-backdrop fade show"></div>
</div>

<jsp:include page="/WEB-INF/views/include/footer.jsp" flush="false"/>

<%-- 전화예약상담 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/counselling-on-call.jsp" flush="false"/>

<script>
    $(".close").click(function () {
        $(".popup-wrapper").css("display", "none");
    });

    function regCookies(no) {
        if ($("#" + no).is(":checked")) {
            //alert(no + " checked");
            setCookie("popup_" + no, no, 1); //쿠키이름을 no로 아이디입력필드값을 1일동안 저장
        }
    }

    function get_browser_info() {
        var ua = navigator.userAgent, tem,
            M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
        if (/trident/i.test(M[1])) {
            tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
            return {name: 'IE ', version: (tem[1] || '')};
        }
        if (M[1] === 'Chrome') {
            tem = ua.match(/\bOPR\/(\d+)/)
            if (tem != null) {
                return {name: 'Opera', version: tem[1]};
            }
        }
        M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
        if ((tem = ua.match(/version\/(\d+)/i)) != null) {
            M.splice(1, 1, tem[1]);
        }
        return {
            name: M[0],
            version: M[1]
        };
    }

    $(document).ready(function () {
        //팝업 테그만큼 반복
        var cookies = document.cookie.split("; ");
        $(".popup-unit").each(function () {
            //로우 아이디 가져와서
            $(this).attr('id');
            //alert($(this).attr('id'));
            var showyn = 1;
            for (var i = 0; i < cookies.length; i++) {
                if ($(this).attr('id') != cookies[i].split("=")[0]) {
                    showyn = showyn * 1;
                } else {
                    showyn = showyn * 0;
                }
            }
            if (showyn == 1) {
                $("#" + $(this).attr('id')).show();
                $("#popup_area").show();
            }
        });

        var browser = get_browser_info();

        var bName = browser.name;
        var bVersion = browser.version;

        if (bName == "MSIE" && bVersion <= 10) {
            $(".browser-recommend-area").css("display", "block");
        }

        var cookieYN = 0;
        for (var i = 0; i < cookies.length; i++) {
            if (cookies[i].split("=")[0] == "exVirsion") {
                cookieYN = 1;
            }
        }
        if (cookieYN == 0 && (bName == "MSIE" && bVersion <= 10)) {
            $("#exVersion_area").show();
        } else {
            $("#exVersion_area").hide();
        }

        $(".popup-img-area").trigger("onkeydown"); // 팝업 화면 load시 태워주기..
    });

    //익스 하위버전 안내 팝업 쿠키등록
    function exVirsionCheck() {
        if ($("#never-shown").is(":checked")) {
            setCookie("exVirsion", "exVirsion", 1); //100일동안 exVirsion 쿠키저장
            $("#exVersion_area").hide();
        }
    }

    //익스 버전 팝업 닫기
    function closeVirsionPopup() {
        $("#exVersion_area").hide();
    }

    function dontShow1(no) {
        $("#clolseBtn" + no).trigger('click');
        regCookies(no);
    }

    // 팝업 이미지 보여주기
    function popupImgView(fileName, obj) {
        $('#popupImageContainer' + obj).empty();
        var url = "/popupStream?id=" + fileName;
        var img = document.createElement('img'); // 이미지 객체 생성
        img.src = url;
        document.getElementById('popupImageContainer' + obj).appendChild(img); // 이미지 동적 추가
        $(img).attr("width", "350");
        $(img).attr("height", "500");
        $(img).css({"margin": "0 auto", "cursor": "pointer"});
    }
</script>
