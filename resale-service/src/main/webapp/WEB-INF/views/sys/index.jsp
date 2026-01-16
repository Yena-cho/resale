<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
	String error = request.getParameter("error");
	String auth  = request.getParameter("auth");
%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>가상계좌 수납관리 서비스 관리자 | Login</title>

    <link href="/assets/bootstrap/bootstrap-for-admin.min.css" rel="stylesheet">
    <link href="/assets/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/assets/css/animate.css" rel="stylesheet">
    <link href="/assets/css/style.min.css" rel="stylesheet">
    <script src="/assets/js/sweetalert.js"></script>
    <script src="/assets/js/promise.min.js"></script>

<script type="text/javascript">

function setCookie(name, value, expiredays) //쿠키 저장함수
{
    var todayDate = new Date();
    todayDate.setDate(todayDate.getDate() + expiredays);
    document.cookie = name + "=" + escape(value) + "; path=/; expires="
            + todayDate.toGMTString() + ";"
}

function getCookie() { // 쿠키 불러오는 함수
	var cookies = document.cookie.split("; ");
	for(var  i = 0; i < cookies.length; i++) {
		if(cookies[i].split("=")[0] == "sysId") {
			$('#sysId').val(unescape(cookies[i].split("=")[1]));
		    $('#rememberId').attr("checked", true); 
		} 
	}
}

function sendit() {
	if (!$('#sysId').val()) { //아이디를 입력하지 않으면.
		swal({
	        type: 'info',
	        text: "아이디를 입력해주세요!",
	        confirmButtonColor: '#3085d6',
	        confirmButtonText: '확인'
		}).then(function() {
			$("#sysId").focus();
        });
        return;
    }
    if (!$('#sysPw').val()) { //비밀번호를 입력하지 않으면.
    	swal({
	        type: 'info',
	        text: "비밀번호를 입력해주세요!",
	        confirmButtonColor: '#3085d6',
	        confirmButtonText: '확인'
		}).then(function() {
			$("#sysPw").focus();
        });
        return;
    }
    if (!$('#otpCode').val()) { //비밀번호를 입력하지 않으면.
        swal({
            type: 'info',
            text: "OTP코드를 입력해주세요!",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        }).then(function() {
            $("#otpCode").focus();
        });
        return;
    }

    
    if ($('#rememberId').is(":checked")) { // 아이디 저장을 체크 하였을때
        setCookie("sysId", $('#sysId').val(), 7); //쿠키이름을 id로 아이디입력필드값을 7일동안 저장
    } else { // 아이디 저장을 체크 하지 않았을때
        setCookie("sysId", $('#sysId').val(), 0); //날짜를 0으로 저장하여 쿠키삭제
    }
	
    $('#userId').val("SYS" + ":///:" + $('#sysId').val() + ":///:" + "DA"); 
    $('#userPw').val($('#sysPw').val());
    $('#userOtp').val($('#otpCode').val());
	
    document.loginForm.action = "/login";
    document.loginForm.submit(); 
}

function fn_errorMsg() {

    var errorMsg = "<c:out value="<%=error%>" escapeXml="true" />";
    var authMsg = "<c:out value="<%=auth%>" escapeXml="true" />";
	var title = "비밀번호가 올바르지 않습니다.";
	var msg = "";
	
	if(errorMsg != "null" && errorMsg != "") {
		if(errorMsg == "badCredential") {
			if(authMsg == "admin") {
				title = "아이디가 올바르지 않습니다.";
			}
			msg = "아이디나 비밀번호가 기억나지 않을 때는 고객센터로 문의하시기 바랍니다.";
		} else if(errorMsg == "locked") {  // 계정 잠김
			title = "비밀번호 오류 횟수 5회 초과되었습니다.";
			msg = "자세한 사항은 고객센터(02-786-8201)를 통해  문의해 주세요.";
		} else if(errorMsg == "accountExpired") {  // 정지
			title = "서비스 이용 정지상태입니다.";
			msg = "자세한 사항은 고객센터(02-786-8201)를 통해  문의해 주세요.";
		} else if (errorMsg === 'otp') {
            title = "OTP가 틀렸습니다.";
            msg = "정상적인 코드를 입력해주세요";
        }
		swal({
			type: 'error',
			title: title,
			text: msg,    
			confirmButtonColor: '#3085d6',
			confirmButtonText: '확인'
		}).then(function(result) { 
			if (result.value) {  
				setCookie("sysId", $('#sysId').val(), 0);
				location.href = "/sys";
			} 
		});
	}
}

</script>

</head>

<form name="loginForm" id="loginForm" method="post">
	<input type="hidden" name="userId"  id="userId" />
	<input type="hidden" name="userPw"  id="userPw" />
    <input type="hidden" name="userOtp"  id="userOtp" />
</form>

<body class="gray-bg">

    <div class="middle-box text-center loginscreen animated fadeInDown">
        <div>
            <div>
                <h1 class="logo-name">RS</h1>
            </div>
            <h3>재판매 핑거 관리자</h3>
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="아이디" id="sysId" maxlength="16">
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" placeholder="비밀번호" id="sysPw" maxlength="15" onkeypress="if(event.keyCode == 13){sendit();}">
                </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="otp" id="otpCode" maxlength="16">
            </div>
                <div class="form-group">
                    <input class="form-check-input table-check-child" type="checkbox" id="rememberId" value="option2">
                    <label for="rememberId"><span></span> 아이디 저장</label>
                </div>
                <button type="submit" class="btn btn-primary block full-width m-b" onclick="sendit();">로그인</button>

				<p class="m-t"> <small>관리자만 로그인 할 수 있습니다.</small> </p>
        </div>
    </div>

    <!-- Mainly scripts -->
    <script src="/assets/js/jquery.min.js"></script>
	<script src="/assets/js/jquery-ui.min.js"></script>
    <script src="/assets/bootstrap/bootstrap.min.js"></script>

</body>

</html>

<script>
$(document).ready(function() {
	getCookie();
	fn_errorMsg();
});
</script>
