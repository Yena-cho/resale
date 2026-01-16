<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<title>NICEPAY CANCEL RESULT(UTF-8)</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes, target-densitydpi=medium-dpi" />
<link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap.css">
<link rel="stylesheet" type="text/css" href="/assets/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css?ver=0.1">
<link rel="stylesheet" type="text/css" href="/assets/css/import.css"/>

<script src="/assets/js/jquery.min.js"></script>
<script src="/assets/js/jquery-ui.min.js"></script>
<script src="/assets/js/common.js?version=${project.version}"></script>
<script type="text/javascript">
function sendit(){
	opener.parent.location.reload();
	self.close();
}

$(window).bind("beforeunload", function (e){
	opener.parent.location.reload();
	self.close();
}); 
</script>
</head>
<body>
  <div class="payfin_area">
    <div class="top">결제취소결과 확인</div>
    <div class="conwrap">
      <div class="con">
        <div class="tabletypea">
          <table>
            <tr>
              <th><span>거래 아이디</span></th>
              <td>${map.tid}</td>
            </tr>
            <tr>
              <th><span>결제 수단</span></th>
              <td>${map.payMethod }</td>
            </tr>         
            <tr>
              <th><span>결과 내용</span></th>
              <td>[${map.resultCode}]${map.resultMsg}</td>
            </tr>
            <tr>
              <th><span>취소 금액</span></th>
              <td>${map.cancelAmt}</td>
            </tr>
            <tr>
              <th><span>취소일</span></th>
              <td>${map.cancelDate }</td>
            </tr>
            <tr>
              <th><span>취소시간</span></th>
              <td>${map.cancelTime }</td>
            </tr>
          </table>
        </div>
      </div>
      <div class="btngroup">
          <a href="#" class="btn_blue" onClick="sendit();">확인</a>	
      </div>
      <p>* 취소가 성공한 경우에는 다시 승인상태로 복구 할 수 없습니다.</p>
    </div>
  </div>
</body>
</html>
