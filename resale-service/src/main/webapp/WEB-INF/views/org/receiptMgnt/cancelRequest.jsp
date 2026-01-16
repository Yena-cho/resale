<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>NICEPAY CANCEL REQUEST(UTF-8)</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes, target-densitydpi=medium-dpi" />
<link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap.css">
<link rel="stylesheet" type="text/css" href="/assets/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css?ver=0.1">
<link rel="stylesheet" type="text/css" href="/assets/css/import.css"/>

<script src="/assets/js/jquery.min.js"></script>
<script src="/assets/js/jquery-ui.min.js"></script>
<script src="/assets/js/common.js?version=${project.version}"></script>
<script type="text/javascript">

function goCancel() {
	if($("#CancelPwd").val() == ""){
		swal({
		       type: 'info',
		       text: "비밀번호를 입력해주세요.",
		       confirmButtonColor: '#3085d6',
		       confirmButtonText: '확인'
			});
		return;
	}
	document.tranMgr.submit();
}

</script>
</head>
<body>
<form name="tranMgr" method="post" action="/org/receiptMgnt/goCancel">
	<input type="hidden" name="payDay" id="payDay" value="${map.result.payDay}">
	<input type="hidden" name="bnkMsgNo" id="bnkMsgNo" value="${map.result.bnkMsgNo}">
    <div class="payfin_area">
      <div class="top">결제취소정보 확인</div>
      <div class="conwrap">
        <div class="con">
          <div class="tabletypea">
            <table>
              <colgroup><col width="30%" /><col width="*" /></colgroup>
              <tr>
                <th><span>MID</span></th>
                <td><input class="form-control" type="text" name="MID" value="${map.result.pgServiceId}" readonly="readonly"></td>
              </tr>	
              <tr>
                <th><span>거래번호</span></th>
                <td><input class="form-control" type="text" name="TID" value="${map.result.packetNo}" readonly="readonly"></td>
              </tr>
              <tr>
                <th><span>고객명</span></th>
                <td><input class="form-control" type="text" name="CusName" value="${map.result.cusName}" readonly="readonly"></td>
              </tr>
              <tr>
                <th><span>청구월</span></th>
                <td><input class="form-control" type="text" name="MasMonth" value="${fn:substring(map.result.masMonth, 0, 4)}.${fn:substring(map.result.masMonth, 4, 6)}" readonly="readonly"></td>
              </tr>	              
              <tr>
                <th><span>취소 금액</span></th>
                <td><input class="form-control" type="text" name="CancelAmt" value="${map.result.rcpAmt}" readonly="readonly"></td>
              </tr>
              <tr>
                <th><span>취소 사유</span></th>
                <td><input class="form-control" type="text" name="CancelMsg" value="고객 요청" readonly="readonly"></td>
              </tr>           
              <tr>
                <th><span>취소 비밀번호</span></th>
                <td><input class="form-control" type="password" id="CancelPwd" name="CancelPwd" value=""></td>
              </tr> 
            </table>
          </div>
        </div>
        <p>* 취소 요청시 상단의 모든 값을 입력 하세요.</p>
        <p>* 신용카드결제, 계좌이체, 가상계좌만 부분취소/부분환불이 가능합니다.</p>
        <div class="btngroup">
          <a href="#" class="btn_blue" onClick="goCancel();">요 청</a>
        </div>
      </div>
    </div>
</form>
</body>
</html>
