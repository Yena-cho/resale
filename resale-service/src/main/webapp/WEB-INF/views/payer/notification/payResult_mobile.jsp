<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:authentication property="principal.username" var="chaCd"/>
<s:authentication property="principal.name" 	var="cusName"/>
<s:authentication property="principal.vano" 	var="vaNo"/>
<s:authentication property="principal.email" 	var="email"/>
<s:authentication property="principal.loginId" 	var="loginId"/>

<!DOCTYPE html>
<html>
<head>
<title>NICEPAY PAY RESULT(UTF-8)</title>
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
</script>
</head>
<body> 
  <div class="payfin_area">
    <div class="top">결제결과확인</div>
    <div class="conwrap">
      <div class="con">
        <div class="tabletypea">
          <table>
            <colgroup><col width="30%"/><col width="*"/></colgroup>
              <tr>
                <th><span>결과 내용</span></th>
                <td>[${map.resultCode}]${map.resultMsg}</td>
              </tr>
              <tr>
                <th><span>이용기관명</span></th>
                <td>${map.chaName}</td>
              </tr>
               <tr>
                 <th><span>승인일시</span></th>
                 <td>${map.authDate}</td>
               </tr>
               <tr>
                 <th><span>납부자명</span></th>
                 <td>${map.buyerName}</td>
               </tr>
               <tr>
                 <th><span>결제금액</span></th>
                 <td>${map.amt}원</td>
               </tr>
               <c:choose>               
               	<c:when test="${map.payMethod eq 'CARD'}">
              		<tr>
                    	<th><span>카드종류</span></th>
                    	<td>${map.cardName}</td>
                  	</tr>
               		<tr>
                 		<th><span>할부정보</span></th>
                 		<td>${map.cardQuota}개월</td>
               		</tr>
               	</c:when>
            	<c:when test="${map.payMethod eq 'BANK'}">
               		<tr>
                 		<th><span>은행</span></th>
                 		<td>${map.bankName}</td>
               		</tr>
               		<tr>
                   		<th><span>현금영수증 타입</span></th>
                   		<td>${rcptType}(0:발행안함,1:소득공제,2:지출증빙)</td>
                  	</tr>
                </c:when>
				<c:when test="${map.payMethod eq 'CELLPHONE'}">
               		<tr>
                 		<th><span>이통사 구분</span></th>
                 		<td>${map.carrier}</td>
               		</tr>
               		<tr>
                 		<th><span>휴대폰 번호</span></th>
                 		<td>${map.dstAddr}</td>
               		</tr>
               	</c:when>
            	<c:when test="${map.payMethod eq 'VBANK'}">
					<tr>
                    	<th><span>입금 은행</span></th>
                    	<td>${map.vbankBankName}</td>
                  	</tr>
               		<tr>
                 		<th><span>입금 계좌</span></th>
                 		<td>${map.vbankNum}</td>
               		</tr>
               		<tr>
                 		<th><span>입금 기한</span></th>
                 		<td>${map.vbankExpDate}</td>
               		</tr>
               	</c:when>
            	<c:when test="${map.payMethod eq 'SSG_BANK'}">
			  		<tr>
						<th><span>은행</span></th>
						<td>${map.bankName}</td>
			  		</tr>
			  		<tr>
						<th><span>현금영수증 타입</span></th>
						<td>${map.rcptType}(0:발행안함,1:소득공제,2:지출증빙)</td>
			  		</tr>
			  	</c:when>					  
               </c:choose>		  
          </table>
        </div>
      </div>
      <div class="btngroup">
          <a href="#" class="btn_blue" onClick="sendit();">확인</a>	
      </div>
    </div>
  </div>
</body>
</html>
