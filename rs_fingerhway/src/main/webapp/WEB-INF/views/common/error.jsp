<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
  /**
  * @Class Name : error.jsp
  * @Description : 오류페이지
  * @Modification Information
  *
  *   수정일		수정자		수정내용
  *  -------	--------	---------------------------
  *  2018.02.09	이동한		최초 생성
  *
  */
%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta charset="utf-8" />
<title>경기고속도로 가상계좌 자금관리 서비스</title>
<link type="text/css" rel="stylesheet" href="/resources/css/default.css" /> <!-- 공통 -->
<link type="text/css" rel="stylesheet" href="/resources/css/error.css" /> <!-- error -->
<!--[if lt iE 8]><link rel="stylesheet" type="text/css" href="/resources/css/ie.css" /><![endif]--> <!-- IE 위한 CSS -->

<script type="text/javascript" src="/resources/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/resources/js/common.js"></script>
<!--[if lt IE 9]><script type="text/javascript" src="/resources/js/html5.js"></script><![endif]--> <!-- IE9이하 html5 태그적용 -->
<!--[if lt iE 7]><script type="text/javascript" src="/resources/js/IE8.js"></script><![endif]--> <!-- IE구버전 웹표준향상 -->

</head>
<body>

<div id="body">


    <!-- 컨테이너 영역 -->
    <div id="container">
    
    <div class="wrap_error">
        <img src="/resources/images/ico_error.png" alt="오류" />
        <h3>페이지 오류 안내</h3>
        <p>사이트 이용에 불편을 드려 죄송합니다.  페이지에 오류가 있거나 일시적으로 사용할 수 없습니다.<br />이전 화면으로 이동합니다.</p>
        <span class="btn2"><a href="javascript:history.back();">확인</a></span>
    </div>
        
    <span class="float_clear"></span>    
    </div>
    <!-- //컨테이너 영역 -->  
    <hr /> 
    
	 
</div>  

</body>
</html>