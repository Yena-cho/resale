<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/>

<%
	String error = request.getParameter("error");
	String auth  = request.getParameter("auth");
%>

<script src="/assets/js/jquery.min.js"></script>
<script src="/assets/js/jquery-ui.min.js"></script>

<style>
.ui-autocomplete {
    position: absolute;
    cursor: default;
    height: 200px;
    overflow-y: scroll;
    overflow-x: hidden;
    }
</style>

<script type="text/javascript">

var firstDepth = null;
var secondDepth = null;
</script>

<script type="text/javascript">
    $(function () {
        if (getCookie("id")) { // getCookie함수로 id라는 이름의 쿠키를 불러와서 있을경우
            $('#orgId').val(getCookie("id")); //input 이름이 id인곳에 getCookie("id")값을 넣어줌
            $('#remember_id').attr("checked", true); //document.frm.remember_id.checked = true; // 체크는 체크됨으로
        }

        fn_errorMsg();

        $("#bankNum").autocomplete({
            source: function (request, response) {
                if ($("#info").val() == "cus") {
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


    });

    function fn_errorMsg() {

        var errorMsg = "<c:out value="<%=error%>" escapeXml="true" />";
        var authMsg = "<c:out value="<%=auth%>" escapeXml="true" />";
        var title = "";
        var msg = "";

        if (errorMsg != "null" && errorMsg != "") {
            if (errorMsg == "internal") { // 내부시스템 오류
                title = "계정정보가 올바르지 않습니다.";
                msg = "";
            } else if (errorMsg == "disabled") { // disabled
                title = "서비스 이용이 제한된 사용자 입니다.";
                msg = "해당 계정은 이미 탈퇴 처리된 계정입니다. 서비스 재가입 문의는 고객센터(02-786-8201)를 통해 문의해 주세요";
            } else if (errorMsg == "accountExpired") {  // 서비스이용제한(수수료미납)
                title = "서비스 이용이 제한된 사용자 입니다.";
                msg = "당사 서비스 정책에 따라 사용이 제한되었습니다. 자세한 사항은 고객센터(02-786-8201)를 통해  문의해 주세요.";
            } else if (errorMsg == "credentialsExpired") {    // 계정 권한 만료
                title = "서비스 이용이 제한된 사용자 입니다.";
                msg = "계정 또는 권한이 없어 로그인할 수 없습니다. 관리자에게 문의바랍니다.";
            } else if (errorMsg == "locked") {  // 계정 잠김
                title = "비밀번호 오류 횟수 5회 초과되었습니다.";
                msg = "비밀번호 찾기를 통해 본인 확인 후 재발급 받으시길 바랍니다.";
            } else if (errorMsg == "badCredential") {	// 계정 정보가 올바르지 않음
                if (authMsg == "id") {
                    title = "아이디 정보가 올바르지 않습니다.";
                } else if (authMsg == "acc") {
                    title = "납부계좌번호 정보가 올바르지 않습니다.";
                } else if (authMsg == "cus") {
                    title = "고객명 정보가 올바르지 않습니다.";
                } else if (authMsg == "pwd") {
                    title = "비밀번호가 올바르지 않습니다.";
                } else {
                    title = "연락처가 올바르지 않습니다.";
                }

// 			title = "계정 정보가 올바르지 않습니다.";
                msg = "확인 후 다시 로그인해 주세요.";
            }

            swal({
                type: 'error',
                title: title,
                text: msg,
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function (result) {
                if (result.value) {
                    setCookie("id", $("#userName").val(), 0);
                    location.href = "/common/login/";
                }
            });
        }
    }

    function setCookie(name, value, expiredays) //쿠키 저장함수
    {
        var todayDate = new Date();
        todayDate.setDate(todayDate.getDate() + expiredays);
        document.cookie = name + "=" + escape(value) + "; path=/; expires="
            + todayDate.toGMTString() + ";"
    }

    function getCookie(Name) { // 쿠키 불러오는 함수
        var cookies = document.cookie.split("; ");
        for (var i = 0; i < cookies.length; i++) {
            if (cookies[i].split("=")[0] == "id") {
                return unescape(cookies[i].split("=")[1]);

            }
        }
    }

    function removeChar(event) {
        event = event || window.event;
        var keyID = (event.which) ? event.which : event.keyCode;
        if (keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39) {
            return;
        } else {
            event.target.value = event.target.value.replace(/[^0-9a-zA-Z/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/g, "");
        }
    }

    function sendit() {
        var pw = '';
        var num = '';
        var eng = '';
        var spe = '';

        if (!$('#orgId').val()) { //아이디를 입력하지 않으면.
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

        //아이디 유효성 체크
        pw = $('#orgId').val();
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

        if (!$('#orgPw').val()) { //비밀번호를 입력하지 않으면.
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

        if ($('#remember_id').is(":checked")) { // 아이디 저장을 체크 하였을때
            setCookie("id", $('#orgId').val(), 7); //쿠키이름을 id로 아이디입력필드값을 7일동안 저장
        } else { // 아이디 저장을 체크 하지 않았을때
            setCookie("id", $('#orgId').val(), 0); //날짜를 0으로 저장하여 쿠키삭제
        }

        $('#userId').val("ORG" + ":///:" + $('#orgId').val());
        $('#userPw').val($('#orgPw').val());

        document.loginForm.action = "/login";
        document.loginForm.submit();
    }

    // 납부자 로그인
    function sendPay() {
        if ($("#info").val() == "num") { // 납부자 : 납부계좌번호
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

            $('#userId').val("NUM" + ":///:" + $("#bankNum").val());
            $('#userPw').val($("#bankNum").val());

        } else if ($("#info").val() == "cus") { // 납부자 : 고객정보

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

            $('#userId').val("CUS" + ":///:" + $("#userName").val() + ":///:" + $("#bankNum").val() + ":///:" + $("#phoneNum").val());
            $('#userPw').val($("#phoneNum").val());
        }
        document.loginForm.action = "/login";
        document.loginForm.submit();
    }

    function loginGb(str) {
        $('#loginGb').val(str);
    }

    function fn_isChecked(str) {
        $('#info').val(str);
        $("#bankNum").val('');
        if ($('#info').val() == "num") {
        	$("#bankNum").attr("type", "tel");
            $("#bankNum").attr("placeholder", "납부계좌번호 '-'제외");
            $("#contact").css("display", "none");
        } else {
        	$("#bankNum").attr("type", "text");
            $("#bankNum").attr("placeholder", "이용기관명");
            $("#contact").css("display", "block");
        }
    }

    function onlyNumberCheck(obj) {
        $(obj).keyup(function () {
            if ($("#info").val() == "num") {
                $(this).val($(this).val().replace(/[^0-9]/g, ""));
            }
        });
    }

</script>

<form name="loginForm" id="loginForm" method="post">
	<input type="hidden" name="loginGb" id="loginGb" value="org">
	<input type="hidden" name="info" 	id="info" 	 value="num">
	<input type="hidden" name="userId"  id="userId">
	<input type="hidden" name="userPw"  id="userPw">
</form>

            <div id="contents">
                <div id="damoa-breadcrumb">
                    <nav class="nav container">
                        <a class="nav-link" href="#">공지사항</a>
                        <a class="nav-link active" href="#">자주하는 질문</a>
                        <a class="nav-link" href="#">고객센터</a>
                    </nav>
                </div>
                <div class="container">
                    <div id="page-title">
                        <div id="breadcrumb-in-title-area" class="row align-items-center">
                            <div class="col-12 text-right">
                                <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                                <span> > </span>
                                <span class="depth-1">고객지원</span>
                                <span> > </span>
                                <span class="depth-2 active">자주하는 질문</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-12">
                            	<h2>로그인</h2>
                            </div>
                        </div>
                    </div>
                </div>

                <form name="frm" id="frm">
                <div class="container">
                    <div class="row">
                        <div class="col">
                            <div class="tab-selecter type-4">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#collecter" onclick="loginGb('org');">기관담당자</a></li>
<%--                                    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#payer" onclick="loginGb('pay');">납부자</a></li>--%>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col col-lg-5 col-md-7 col-sm-12 col-12">
                            <div class="tab-content login-box">
                                <div id="collecter" class="container-fluid tab-pane fade show active">
                                    <div class="row no-gutters mb-4">
                                        <div class="col">
                                            <div class="row mb-2">
                                                <div class="col">
                                                    <input type="text" class="form-control" placeholder="아이디" maxlength="20" name="orgId" id="orgId" onkeyup="removeChar(event)">
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col">
                                                    <input type="password" class="form-control" placeholder="비밀번호" maxlength="20" name="orgPw" id="orgPw" onkeyup="removeChar(event)" onkeypress="if(event.keyCode == 13){sendit();}">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <button type="button" class="btn btn-primary btn-flexible-height btn-sub-login ml-2" onclick="sendit();">로그인</button>
                                        </div>
                                    </div>
                                    <div class="row no-gutters login-option align-items-center">
                                        <div class="col">
                                            <input class="form-check-input" type="checkbox" value="" id="remember_id" name="remember_id">
                                            <label class="form-check-label mr-1" for="remember_id"><span></span>
                                                아이디 저장
                                            </label>
                                            <a href="/common/idReset" id="find-id-pw" class="ml-auto">아이디 찾기/비밀번호 찾기(재설정)</a>
                                        </div>
                                    </div>
                                </div>
                                <div id="payer" class="container-fluid tab-pane fade show">
                                    <div class="row mb-2">
                                        <div id="payer-login-method" class="col form-inline">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="by-account" name="method-of-login" onclick="fn_isChecked('num');" checked>
                                                <label for="by-account"><span class="mr-2"></span>납부계좌</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="by-personal-info" name="method-of-login" onclick="fn_isChecked('cus');">
                                                <label for="by-personal-info"><span class="mr-2"></span>고객정보</label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row no-gutters">
                                        <div class="col">
                                            <div class="row">
                                                <div class="col">
                                                    <input type="tel" class="form-control variable-row-input" placeholder="납부계좌번호 '-'제외" onkeydown="onlyNumberCheck(this)" name="bankNum" id="bankNum" maxlength="15" onkeypress="if(event.keyCode == 13){sendPay();}">
                                                </div>
                                            </div>

                                            <div id="contact" class="row mt-2">
                                                <div class="col-12">
                                                    <input type="text" class="form-control" placeholder="고객명" name="userName" id="userName" maxlength="30">
                                                </div>
                                                <div class="col-12 mt-2">
                                                    <input type="tel" class="form-control" placeholder="연락처 '-'제외" name="phoneNum" id="phoneNum" onkeydown="onlyNumber(this)" maxlength="15" onkeypress="if(event.keyCode == 13){sendPay();}">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <button type="button" class="btn btn-primary btn-flexible-height btn-sub-login ml-2" onclick="sendPay();">로그인</button>
                                        </div>
                                    </div>
                                    <div class="row mt-4 mb-2">
                                        <div class="col">
                                            <div class="guide-mention">
                                                * 납부계좌번호를 잊으신 고객께서는 고객정보 입력 후 이용해주시기 바랍니다.
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </form>
                <div id="login-instruction">
                    <div class="container">
                        <div class="row">
							<div class="col col-lg-4 col-md-4 col-12 has-border">
                                <div class="row align-items-center">
                                    <div class="col col-md-12 col-4">
                                        <img src="/assets/imgs/common/img-pw-change.png">
                                    </div>
                                    <div class="col col-md-12 col-8">
                                        <h4>비밀번호 변경관리</h4>
                                        <p>
                                            사용자 비밀번호는 주기적으로 변경관리를 통해 타인에게 노출되지 않도록 주의해주시기 바랍니다.
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col col-lg-4 col-md-4 col-12 has-border">
                                <div class="row align-items-center">
                                    <div class="col col-md-12 col-4">
                                        <img src="/assets/imgs/common/img-monitor-with-unlock.png">
                                    </div>
                                    <div class="col col-md-12 col-8">
                                        <h4>로그아웃</h4>
                                        <p>
                                            서비스 이용 중 자리를 비우게 될 경우에는 개인정보보호를 위해 반드시 로그아웃을 하시기 바랍니다.
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col col-lg-4 col-md-4 col-12 has-border">
                                <div class="row align-items-center">
                                    <div class="col col-md-12 col-4">
                                        <img src="/assets/imgs/common/img-monitor-in-lock.png">
                                    </div>
                                    <div class="col col-md-12 col-8">
                                        <h4>로그인 제한</h4>
                                        <p>
                                            비밀번호를 연속으로 5회이상 입력 오류 시 로그인이 제한되며, 본인인증 후 비밀번호를 다시 등록해야 합니다.
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

<jsp:include page="/WEB-INF/views/include/footer.jsp" flush="false"/>

<script>

$('.nav-tabs a[href="' + window.location.hash + '"]').tab('show');

</script>
