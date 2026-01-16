<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@	page import="com.ghvirtualaccount.vo.UserInfoVO" %>
<%
  /**
  * @Class Name : popHeader.jsp
  * @Description : 팝업 해더 공통
  * @Modification Information
  *
  *   수정일		수정자		수정내용
  *  -------	--------	---------------------------
  *  2018.02.09	이동한		최초 생성
  *
  */
%>
<%
	UserInfoVO userInfo = (UserInfoVO)session.getAttribute("userInfo");
	
	//세션변수에서 사용자 정보를 읽는다.
	if(userInfo==null){
		%>
		<script>
			alert("장시간 사용하지 않아 자동으로 로그아웃 되었습니다.\n화면을 닫습니다.");
			window.close();
		</script>
		<%
		return;
	}
%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta charset="utf-8" />

<link type="text/css" rel="stylesheet" href="/resources/css/default.css" /> <!-- 공통 -->
<link type="text/css" rel="stylesheet" href="/resources/css/popup.css" /> <!-- 팝업 추가 180207 -->
<!--[if lt iE 8]><link rel="stylesheet" type="text/css" href="/resources/css/ie.css" /><![endif]--> <!-- IE 위한 CSS -->
<link type="text/css" rel="stylesheet" href="/resources/css/jquery-ui.css" />

<script type="text/javascript" src="/resources/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery-ui.js"></script>
<script type="text/javascript" src="/resources/js/common.js"></script>
<script type="text/javascript" src="/resources/js/appCommon.js?20180313001"></script>
<script type="text/javascript" src="/resources/js/util.js?20180228001"></script>
<!--[if lt IE 9]><script type="text/javascript" src="/resources/js/html5.js"></script><![endif]--> <!-- IE9이하 html5 태그적용 -->
<!--[if lt iE 7]><script type="text/javascript" src="/resources/js/IE8.js"></script><![endif]--> <!-- IE구버전 웹표준향상 -->
