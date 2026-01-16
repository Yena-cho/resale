<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.security.MessageDigest" %>
<%
	String pgServiceId = request.getParameter("pgServiceId");
	String notimasCd = request.getParameter("notimasCd");
	String amt = request.getParameter("amt");

	StringBuffer sb = new StringBuffer();
	sb.append(pgServiceId);
	sb.append(notimasCd);
	sb.append(amt);
	
	byte[] bNoti = sb.toString().getBytes();
	MessageDigest md = MessageDigest.getInstance("MD5");
	byte[] digest = md.digest(bNoti);
	
	StringBuffer strBuf = new StringBuffer();
	for (int i=0 ; i < digest.length ; i++) {
		int c = digest[i] & 0xff;
		if (c <= 15){
			strBuf.append("0");
		}
		strBuf.append(Integer.toHexString(c));
	}

	String AGS_HASHDATA = strBuf.toString();

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no">
<title>가상계좌 수납관리 서비스</title>

<link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap.css">
<link rel="stylesheet" type="text/css" href="/assets/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css?ver=0.1">

<script src="/assets/js/jquery.min.js"></script>
<script src="/assets/js/jquery-ui.min.js"></script>
<script src="/assets/js/common.js?version=${project.version}"></script>
<script type="text/javascript" src="https://www.allthegate.com/plugin/AGSWallet.js" charset="euc-kr"></script>
<script>
var firstDepth = false;
var secondDepth = false;

StartSmartUpdate();

$(document).ready(function(){
	var retCode = '<c:out value="${map.retCode}"/>'
	if(retCode == '9999'){
		swal({
		       type: 'info',
		       text: "결제정보를 가져올 수 없습니다.",
		       confirmButtonColor: '#3085d6',
		       confirmButtonText: '확인'
			});
	}
	
	Enable_Flag(frmAGS_pay);
});

function Enable_Flag(form){
    form.Flag.value = "enable"
}

function Disable_Flag(form){
    form.Flag.value = "disable"
}

function runPay(){
	var form = document.frmAGS_pay;
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// MakePayMessage() 가 호출되면 올더게이트 플러그인이 화면에 나타나며 Hidden 필드
	// 에 리턴값들이 채워지게 됩니다.
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	if(form.Flag.value == "enable"){
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 입력된 데이타의 유효성을 검사합니다.
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//if(Check_Common(form) == true){
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 올더게이트 플러그인 설치가 올바르게 되었는지 확인합니다.
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			if(document.AGSPay == null || document.AGSPay.object == null){
				swal({
				       type: 'info',
				       text: "플러그인 설치 후 다시 시도 하십시오.",
				       confirmButtonColor: '#3085d6',
				       confirmButtonText: '확인'
					});
			}else{
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// 올더게이트 플러그인 설정값을 동적으로 적용하기 JavaScript 코드를 사용하고 있습니다.
				// 상점설정에 맞게 JavaScript 코드를 수정하여 사용하십시오.
				//
				// [1] 일반/무이자 결제여부
				// [2] 일반결제시 할부개월수
				// [3] 무이자결제시 할부개월수 설정
				// [4] 인증여부
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// [1] 일반/무이자 결제여부를 설정합니다.
				//
				// 할부판매의 경우 구매자가 이자수수료를 부담하는 것이 기본입니다. 그러나,
				// 상점과 올더게이트간의 별도 계약을 통해서 할부이자를 상점측에서 부담할 수 있습니다.
				// 이경우 구매자는 무이자 할부거래가 가능합니다.
				//
				// 예제)
				// 	(1) 일반결제로 사용할 경우
				// 	form.DeviId.value = "9000400001";
				//
				// 	(2) 무이자결제로 사용할 경우
				// 	form.DeviId.value = "9000400002";
				//
				// 	(3) 만약 결제 금액이 100,000원 미만일 경우 일반할부로 100,000원 이상일 경우 무이자할부로 사용할 경우
				// 	if(parseInt(form.Amt.value) < 100000)
				//		form.DeviId.value = "9000400001";
				// 	else
				//		form.DeviId.value = "9000400002";
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				form.DeviId.value = "9000400001";
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// [2] 일반 할부기간을 설정합니다.
				// 
				// 일반 할부기간은 2 ~ 12개월까지 가능합니다.
				// 0:일시불, 2:2개월, 3:3개월, ... , 12:12개월
				// 
				// 예제)
				// 	(1) 할부기간을 일시불만 가능하도록 사용할 경우
				// 	form.QuotaInf.value = "0";
				//
				// 	(2) 할부기간을 일시불 ~ 12개월까지 사용할 경우
				//		form.QuotaInf.value = "0:3:4:5:6:7:8:9:10:11:12";
				//
				// 	(3) 결제금액이 일정범위안에 있을 경우에만 할부가 가능하게 할 경우
				// 	if((parseInt(form.Amt.value) >= 100000) || (parseInt(form.Amt.value) <= 200000))
				// 		form.QuotaInf.value = "0:2:3:4:5:6:7:8:9:10:11:12";
				// 	else
				// 		form.QuotaInf.value = "0";
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				//결제금액이 5만원 미만건을 할부결제로 요청할경우 결제실패
				if(parseInt(form.Amt.value) < 50000)
					form.QuotaInf.value = "0";
				else
					form.QuotaInf.value = "0:2:3:4:5:6:7:8:9:10:11:12";
				
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				// [3] 무이자 할부기간을 설정합니다.
				// (일반결제인 경우에는 본 설정은 적용되지 않습니다.)
				// 
				// 무이자 할부기간은 2 ~ 12개월까지 가능하며, 
				// 올더게이트에서 제한한 할부 개월수까지만 설정해야 합니다.
				// 
				// 100:BC
				// 200:국민
				// 300:외환
				// 400:삼성
				// 500:엘지
				// 600:신한
				// 800:현대
				// 900:롯데
				// 
				// 예제)
				// 	(1) 모든 할부거래를 무이자로 하고 싶을때에는 ALL로 설정
				// 	form.NointInf.value = "ALL";
				//
				// 	(2) 국민카드 특정개월수만 무이자를 하고 싶을경우 샘플(2:3:4:5:6개월)
				// 	form.NointInf.value = "200-2:3:4:5:6";
				//
				// 	(3) 외환카드 특정개월수만 무이자를 하고 싶을경우 샘플(2:3:4:5:6개월)
				// 	form.NointInf.value = "300-2:3:4:5:6";
				//
				// 	(4) 국민,외환카드 특정개월수만 무이자를 하고 싶을경우 샘플(2:3:4:5:6개월)
				// 	form.NointInf.value = "200-2:3:4:5:6,300-2:3:4:5:6";
				//	
				//	(5) 무이자 할부기간 설정을 하지 않을 경우에는 NONE로 설정
				//	form.NointInf.value = "NONE";
				//
				//	(6) 전카드사 특정개월수만 무이자를 하고 싶은경우(2:3:6개월)
				//	form.NointInf.value = "100-2:3:6,200-2:3:6,300-2:3:6,400-2:3:6,500-2:3:6,800-2:3:6,900-2:3:6";
				//
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				form.AGS_HASHDATA.value = "<%= AGS_HASHDATA %>";
				
				if(form.DeviId.value == "9000400002")
					form.NointInf.value = "ALL";
				   
				if(MakePayMessage(form) == true){
					
					Disable_Flag(form);
					
					var openwin = window.open("AGS_progress.html","popup","width=300,height=160"); //"지불처리중"이라는 팝업창연결 부분
					
					form.submit();
				}else{
					// 취소시 이동페이지 설정부분
					swal({
					       type: 'info',
					       text: "지불에 실패하였습니다.",
					       confirmButtonColor: '#3085d6',
					       confirmButtonText: '확인'
						});
				}
			}
		//}
	}
}

</script>

</head>

<body>

<form name="frmAGS_pay" method="post">

<input type=hidden name=Job value="cardonly">
<input type=hidden name=TempJob value="">
<input type=hidden name=StoreId value="${map.result.pgServiceId}">
<input type=hidden name=OrdNo value="${map.result.notimasCd}">
<input type=hidden name=Amt value="${map.result.amt}">
<input type=hidden name=StoreNm value="${map.result.chaName}">
<input type=hidden name=ProdNm value="${map.result.itemName}">
<input type=hidden name=MallUrl value="">
<input type=hidden name=UserEmail value="${map.result.cusMail}">
<input type=hidden name=UserId value="${map.result.cusKey}">
<input type=hidden name=OrdNm value="${map.result.cusName}">
<input type=hidden name=OrdPhone value="${map.result.cusHp}">
<input type=hidden name=OrdAddr value="">
<input type=hidden name=RcpNm value="${map.result.cusName}">
<input type=hidden name=RcpPhone value="${map.result.cusHp}">
<input type=hidden name=DlvAddr value="">
<input type=hidden name=Remark value="">
    

<%-- 각 결제 공통 사용 변수 --%>
<input type=hidden name=Flag value="">				<%-- 스크립트결제사용구분플래그 --%>
<input type=hidden name=AuthTy value="">			<%-- 결제형태 --%>
<input type=hidden name=SubTy value="">				<%-- 서브결제형태 --%>
<input type=hidden name=AGS_HASHDATA value=""/>		<%-- 전역 해쉬 변수 --%>

<%-- 신용카드 결제 사용 변수 --%>
<input type=hidden name=DeviId value="">			<%-- (신용카드공통)		단말기아이디 --%>
<input type=hidden name=QuotaInf value="0">			<%-- (신용카드공통)		일반할부개월설정변수 --%>
<input type=hidden name=NointInf value="NONE">		<%-- (신용카드공통)		무이자할부개월설정변수 --%>
<input type=hidden name=AuthYn value="">			<%-- (신용카드공통)		인증여부 --%>
<input type=hidden name=Instmt value="">			<%-- (신용카드공통)		할부개월수 --%>
<input type=hidden name=partial_mm value="">		<%-- (ISP사용)			일반할부기간 --%>
<input type=hidden name=noIntMonth value="">		<%-- (ISP사용)			무이자할부기간 --%>
<input type=hidden name=KVP_RESERVED1 value="">		<%-- (ISP사용)			RESERVED1 --%>
<input type=hidden name=KVP_RESERVED2 value="">		<%-- (ISP사용)			RESERVED2 --%>
<input type=hidden name=KVP_RESERVED3 value="">		<%-- (ISP사용)			RESERVED3 --%>
<input type=hidden name=KVP_CURRENCY value="">		<%-- (ISP사용)			통화코드 --%>
<input type=hidden name=KVP_CARDCODE value="">		<%-- (ISP사용)			카드사코드 --%>
<input type=hidden name=KVP_SESSIONKEY value="">	<%-- (ISP사용)			암호화코드 --%>
<input type=hidden name=KVP_ENCDATA value="">		<%-- (ISP사용)			암호화코드 --%>
<input type=hidden name=KVP_CONAME value="">		<%-- (ISP사용)			카드명 --%>
<input type=hidden name=KVP_NOINT value="">			<%-- (ISP사용)			무이자/일반여부(무이자=1, 일반=0) --%>
<input type=hidden name=KVP_QUOTA value="">			<%-- (ISP사용)			할부개월 --%>
<input type=hidden name=CardNo value="">			<%-- (안심클릭,일반사용)	카드번호 --%>
<input type=hidden name=MPI_CAVV value="">			<%-- (안심클릭,일반사용)	암호화코드 --%>
<input type=hidden name=MPI_ECI value="">			<%-- (안심클릭,일반사용)	암호화코드 --%>
<input type=hidden name=MPI_MD64 value="">			<%-- (안심클릭,일반사용)	암호화코드 --%>
<input type=hidden name=ExpMon value="">			<%-- (일반사용)			유효기간(월) --%>
<input type=hidden name=ExpYear value="">			<%-- (일반사용)			유효기간(년) --%>
<input type=hidden name=Passwd value="">			<%-- (일반사용)			비밀번호 --%>
<input type=hidden name=SocId value="">				<%-- (일반사용)			주민등록번호/사업자등록번호 --%>

<%-- 계좌이체 결제 사용 변수 --%>
<input type=hidden name=ICHE_OUTBANKNAME value="">	<%-- 이체계좌은행명 --%>
<input type=hidden name=ICHE_OUTACCTNO value="">	<%-- 이체계좌예금주주민번호 --%>
<input type=hidden name=ICHE_OUTBANKMASTER value=""><%-- 이체계좌예금주 --%>
<input type=hidden name=ICHE_AMOUNT value="">		<%-- 이체금액 --%>

<%-- 핸드폰 결제 사용 변수 --%>
<input type=hidden name=HP_SERVERINFO value="">		<%-- 서버정보 --%>
<input type=hidden name=HP_HANDPHONE value="">		<%-- 핸드폰번호 --%>
<input type=hidden name=HP_COMPANY value="">		<%-- 통신사명(SKT,KTF,LGT) --%>
<input type=hidden name=HP_IDEN value="">			<%-- 인증시사용 --%>
<input type=hidden name=HP_IPADDR value="">			<%-- 아이피정보 --%>

<%-- 가상계좌 결제 사용 변수 --%>
<input type=hidden name=ZuminCode value="">			<%-- 가상계좌입금자주민번호 --%>
<input type=hidden name=VIRTUAL_CENTERCD value="">	<%-- 가상계좌은행코드 --%>
<input type=hidden name=VIRTUAL_DEPODT value="">	<%-- 가상계좌입금예정일 --%>
<input type=hidden name=VIRTUAL_NO value="">		<%-- 가상계좌번호 --%>

<%-- ARS 결제 사용 변수 --%>
<input type=hidden name=ARS_PHONE value="">			<%-- ARS번호 --%>
<input type=hidden name=ARS_NAME value="">			<%-- 전화가입자명 --%>

<input type=hidden name=mTId value="">				

<%-- 에스크로 결제 사용 변수 --%>
<input type=hidden name=ES_SENDNO value="">			<%-- 에스크로전문번호 --%>

<%-- 계좌이체(소켓) 결제 사용 변수 --%>
<input type=hidden name=ICHE_SOCKETYN value="">		<%-- 계좌이체(소켓) 사용 여부 --%>
<input type=hidden name=ICHE_POSMTID value="">		<%-- 계좌이체(소켓) 이용기관주문번호 --%>
<input type=hidden name=ICHE_FNBCMTID value="">		<%-- 계좌이체(소켓) FNBC거래번호 --%>
<input type=hidden name=ICHE_APTRTS value="">		<%-- 계좌이체(소켓) 이체 시각 --%>
<input type=hidden name=ICHE_REMARK1 value="">		<%-- 계좌이체(소켓) 기타사항1 --%>
<input type=hidden name=ICHE_REMARK2 value="">		<%-- 계좌이체(소켓) 기타사항2 --%>
<input type=hidden name=ICHE_ECWYN value="">		<%-- 계좌이체(소켓) 에스크로여부 --%>
<input type=hidden name=ICHE_ECWID value="">		<%-- 계좌이체(소켓) 에스크로ID --%>
<input type=hidden name=ICHE_ECWAMT1 value="">		<%-- 계좌이체(소켓) 에스크로결제금액1 --%>
<input type=hidden name=ICHE_ECWAMT2 value="">		<%-- 계좌이체(소켓) 에스크로결제금액2 --%>
<input type=hidden name=ICHE_CASHYN value="">		<%-- 계좌이체(소켓) 현금영수증발행여부 --%>
<input type=hidden name=ICHE_CASHGUBUN_CD value="">	<%-- 계좌이체(소켓) 현금영수증구분 --%>
<input type=hidden name=ICHE_CASHID_NO value="">	<%-- 계좌이체(소켓) 현금영수증신분확인번호 --%>

</form>

<div class="modal-content" style="border-radius:0;">
    <div class="modal-header" style="background: #5390e1; color:#fff;">
        <h5 class="modal-title">카드결제</h5>
    </div>
    <div class="modal-body">
        <div class="container-fluid">
            <div class="row no-gutters mt-3 mb-2">
                <div class="col-12">
                    <h5>결제정보확인</h5>
                </div>
            </div>
        </div>

        <form>
        <div class="container table-container">
            <div id="payment-info" class="list-id">
                <div class="row">
                    <div class="col">
                        <table class="table table-sm table-hover table-primary mb-3">
                            <tbody>
                                <tr>
                                    <th>이용기관명</th>
                                    <td>${map.result.chaName}</td>
                                </tr>
                                <tr>
                                    <th>청구항목명</th>
                                    <td>${map.result.itemName}</td>
                                </tr>
                                <tr>
                                    <th>납부자명</th>
                                    <td>${map.result.cusName}</td>
                                </tr>
                                <tr>
                                    <th>결제금액</th>
                                    <td>${map.result.amt}</td>
                                </tr>
                                <tr>
                                    <th>납부마감일</th>
                                    <td>${map.result.endDate}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="container list-button-group-bottom text-center">
            <div class="row">
                <div class="col">
                    <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                    <button type="button" class="btn btn-primary" onclick="runPay();">결제하기</button>
                </div>
            </div>
        </div>

        </form>
    </div>
</div>

<script src="/assets/bootstrap/bootstrap.min.js"></script>
<script src="/assets/js/sweetalert.js"></script>
<script src="/assets/js/damoa.js"></script>
<script src="/assets/js/payer.js"></script>


</body>

</html>
