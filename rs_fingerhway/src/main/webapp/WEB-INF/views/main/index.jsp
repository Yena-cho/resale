<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
  /**
  * @Class Name : index.jsp
  * @Description : 메인(로그인) 화면
  * @Modification Information
  *
  *   수정일		수정자		수정내용
  *  -------	--------	---------------------------
  *  2018.02.07	이동한		최초 생성
  *
  */
%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta charset="utf-8" />
<title>경기고속도로 가상계좌 자금관리 서비스</title>
<link type="text/css" rel="stylesheet" href="/resources/css/default.css" /> <!-- 공통 -->
<link type="text/css" rel="stylesheet" href="/resources/css/main.css" /> <!-- 메인 -->
<!--[if lt iE 8]><link rel="stylesheet" type="text/css" href="/resources/css/ie.css" /><![endif]--> <!-- IE 위한 CSS -->
<link type="text/css" rel="stylesheet" href="/resources/css/jquery-ui.css" />

<script type="text/javascript" src="/resources/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery-ui.js"></script>
<script type="text/javascript" src="/resources/js/common.js"></script>
<script type="text/javascript" src="/resources/js/appCommon.js?20180306003"></script>
<!--[if lt IE 9]><script type="text/javascript" src="/resources/js/html5.js"></script><![endif]--> <!-- IE9이하 html5 태그적용 -->
<!--[if lt iE 7]><script type="text/javascript" src="/resources/js/IE8.js"></script><![endif]--> <!-- IE구버전 웹표준향상 -->

</head>
<body>
<form:form commandName="paramVO" name="pageNav" id="pageNav" method="post" >
	<input type="hidden" id="mainMenuId" name="mainMenuId"  value="<c:out value='${paramVO.mainMenuId}'/>" />
	<input type="hidden" id="subMenuId"  name="subMenuId"   value="<c:out value='${paramVO.subMenuId}'/>" />
</form:form>
<div id="body">
<div class="wrap_body">

    <!-- 헤더 영역 -->    
    <div id="header"><header>   	       
        <h1><span>경기고속도로</span></h1> 
        <h2><span>가상계좌 자금관리 서비스 로그인</span></h2>              
	</header></div>     
    <!-- //헤더 영역 -->
    <hr />   
     
    <!-- 컨테이너 영역 -->
    <div id="container">    
    <div class="contents">
    
        <!-- 로그인영역 -->
        <div class="wrap_login">
            <ul class="input_login">
            <li class="input_id"><label>아이디</label><input id="userId" type="text" value=""  tabindex="1" /></li>
            <li class="input_pw"><label>비밀번호</label><input id="userPswd" type="password" value="" tabindex="2" /></li>
            </ul>
            <span class="btn_login"><a id="loginButton" style="cursor:pointer">LOGIN</a></span>
        </div>
        <!-- //로그인영역 -->        
        
        <!-- 하단배너영역 -->
        <div class="wrap_bnnr">            
            <strong>핑거 고객센터</strong><span>02-786-8201</span><em>평일 10:00~17:00</em>
        </div>
        <!-- //하단배너영역 -->
                    
    </div>
    </div>
    <!-- //컨테이너 영역 -->    
    <hr />    
    <!-- 푸터 영역 -->
    <div id="footer"><footer>    
        <em>(07327) 서울특별시 영등포구 국제금융로 20 11층 (여의도동, 율촌빌딩)</em> 
        <span>Copyright 2019 Finger Inc. All rights reserved.</span> 
    </footer></div>    
    <!-- //푸터 영역 -->
    
</div>    
</div> 

</body>

<script>
function loginCallBack(data){
	//alert(JSON.stringify(data));
	var menuInfo = JSON.stringify(data.menuInfo);
	var mainMenuId;
	var subMenuId;
	var mainMenuUrl;
	
	//권한별 기본 메뉴를 찾는다.
	for(var i=0;i<data.menuInfo.length;i++){
		if(data.menuInfo[i].defaultYn=="Y"){
			//alert("야호");
			//상위메뉴 세팅
			mainMenuId = data.menuInfo[i].menuId;
			//하위메뉴 세팅
			subMenuId = data.menuInfo[i].subMenu[0].menuId;
			//하위메뉴의 URL 세팅
			subMenuUrl = data.menuInfo[i].subMenu[0].menuUrl;
			
			break;
		}
	}
	//alert(data.menuInfo.length);
	//location.href="/userInfoMgmt.do";

	goMenu(subMenuUrl, mainMenuId, subMenuId);
}
$(document).ready(function(){
	$("#userId").focus();
	
	$("#userPswd").keydown(function (key){
		if (key.keyCode == 13) {
			var param = {userId : $("#userId").val(), userPswd : $("#userPswd").val()};
			var _url = "/login.do";
			
			ajaxCall(param, _url, 'loginCallBack' );
		}
	});
	
	$("#loginButton").click(function (e) {
		var param = {userId : $("#userId").val(), userPswd : $("#userPswd").val()};
		var _url = "/login.do";
		
		ajaxCall(param, _url, 'loginCallBack' );
	});
});
</script>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>