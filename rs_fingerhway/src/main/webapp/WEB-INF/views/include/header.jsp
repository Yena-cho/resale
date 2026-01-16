<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@	page import="com.ghvirtualaccount.vo.UserInfoVO" %>
<%@	page import="com.ghvirtualaccount.vo.MenuInfoVO" %>
<%@	page import="com.ghvirtualaccount.vo.ParamVO" %>
<%@	page import="java.util.List" %>
<%@	page import="java.util.ArrayList" %>
<%
  /**
  * @Class Name : header.jsp
  * @Description : 해더 공통
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
			alert("장시간 사용하지 않아 자동으로 로그아웃 되었습니다.\n로그인 화면으로 이동합니다.");
			location.href="/main.do";
		</script>
		<%
		return;
	}

	//paramVO값 세팅
	ParamVO paramVO = (ParamVO)request.getAttribute("paramVO");
	
	//세션변수에서 메뉴정보 Read
	List menuInfo = new ArrayList();
	menuInfo = (List)session.getAttribute("menuInfo");
	
	//홈메뉴객체
	MenuInfoVO homeMenuInfoVO = new MenuInfoVO();
	for(int i=0;i<menuInfo.size();i++){
		if(((MenuInfoVO)menuInfo.get(i)).getDefaultYn().equals("Y")){
			homeMenuInfoVO = (MenuInfoVO)menuInfo.get(i);
			break;
		}
	}
	
	//현재  상위 메뉴
	MenuInfoVO menuInfoVO = new MenuInfoVO();
	for(int i=0;i<menuInfo.size();i++){
		if(((MenuInfoVO)menuInfo.get(i)).getMenuId().equals(paramVO.getMainMenuId())){
			menuInfoVO = (MenuInfoVO)menuInfo.get(i);
			break;
		}
	}
	
	//현재  메뉴
	MenuInfoVO subMenuInfoVO = new MenuInfoVO();
	for(int i=0;i<menuInfoVO.getSubMenu().size();i++){
		if(menuInfoVO.getSubMenu().get(i).getMenuId().equals(paramVO.getSubMenuId())){
			subMenuInfoVO = menuInfoVO.getSubMenu().get(i);
			break;
		}
	}
	
	request.setAttribute("homeMenuInfoVO", homeMenuInfoVO);
	request.setAttribute("menuInfoVO", menuInfoVO);
	request.setAttribute("subMenuInfoVO", subMenuInfoVO);
%>
<!DOCTYPE HTML>
<html lang="ko">
<head>
<meta charset="utf-8" />
<title>경기고속도로 가상계좌 자금관리 서비스</title>
<link type="text/css" rel="stylesheet" href="/resources/css/default.css?20180313001" /> <!-- 공통 -->
<link type="text/css" rel="stylesheet" href="/resources/css/sub.css" /> <!-- 서브 -->
<!--[if lt iE 8]><link rel="stylesheet" type="text/css" href="/resources/css/ie.css" /><![endif]--> <!-- IE 위한 CSS -->
<link type="text/css" rel="stylesheet" href="/resources/css/jquery-ui.css" />

<script type="text/javascript" src="/resources/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery-ui.js"></script>
<script type="text/javascript" src="/resources/js/common.js"></script>
<script type="text/javascript" src="/resources/js/appCommon.js?20180313001"></script>
<script type="text/javascript" src="/resources/js/util.js?20180228001"></script>
<!--[if lt IE 9]><script type="text/javascript" src="/resources/js/html5.js"></script><![endif]--> <!-- IE9이하 html5 태그적용 -->
<!--[if lt iE 7]><script type="text/javascript" src="/resources/js/IE8.js"></script><![endif]--> <!-- IE구버전 웹표준향상 -->

</head>