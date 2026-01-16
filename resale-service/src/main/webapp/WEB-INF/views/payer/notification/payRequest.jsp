<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.commons.codec.binary.Hex" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	
    /*
     *******************************************************
     * <결제요청 파라미터>
     * 결제시 Form 에 보내는 결제요청 파라미터입니다.
     * 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며,
     * 추가 가능한 옵션 파라미터는 연동매뉴얼을 참고하세요.
     *******************************************************
     */
    String price = (String) request.getAttribute("amt");
    /*String price = "104";*/
    String merchantKey = (String) request.getAttribute("merchantKey");   // 상점키
    String merchantID = (String) request.getAttribute("merchantID");                      // 상점아이디
    String goodsCnt = "1";                               // 결제상품개수
    String goodsName = "신한다모아";                      // 결제상품명
    String buyerName = "나이스";                          // 구매자명
    String buyerTel = "01000000000";                     // 구매자연락처
    String buyerEmail = "happy@day.co.kr";                 // 구매자메일주소
	//String moid             = "mnoid1234567890";                 // 상품주문번호	
    String encodeParameters = "CardNo,CardExpire,CardPwd";       // 암호화대상항목 (변경불가) 
    String charset = "utf-8";
    String notidetCd = (String) request.getAttribute("notidetCd");

    /*
     *******************************************************
     * <해쉬암호화> (수정하지 마세요)
     * SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
     *******************************************************
     */
    DataEncrypt sha256Enc = new DataEncrypt();
    String ediDate = getyyyyMMddHHmmss();
    String hashString = sha256Enc.encrypt(ediDate + merchantID + price + merchantKey);

    /*
     *******************************************************
     * <서버 IP값>
     *******************************************************
     */
    InetAddress inet = InetAddress.getLocalHost();
%>
<!DOCTYPE html>
<html>
<head>
    <title>NICEPAY PAY REQUEST(UTF-8)</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes, target-densitydpi=medium-dpi"/>
    <link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/jquery-ui.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css?ver=0.1">
    <link rel="stylesheet" type="text/css" href="/assets/css/import.css"/>
    <style type="text/css">
        html {
            overflow: hidden;
        }
    </style>

    <script src="/assets/js/jquery.min.js"></script>
    <script src="/assets/js/jquery-ui.min.js"></script>
    <script src="/assets/js/common.js?version=${project.version}"></script>
    <script src="https://web.nicepay.co.kr/flex/js/nicepay_tr_utf.js" type="text/javascript"></script>
    <script type="text/javascript">
        var firstDepth = false;
        var secondDepth = false;

        $(document).ready(function () {
            $("#price").val(numberToCommas($("#price").val()));
            $("#endDate").val(changeDateFormat($("#endDate").val()));

            if ($("#Amt").val() < 50000) {
                document.payForm.SelectQuota.value = '00';
            } else {
                document.payForm.SelectQuota.value = '01,02,03,04,05,06';
            }
        });

        //결제창 최초 요청시 실행됩니다.
        function nicepayStart() {
            var buyerTel = $('#buyer-tel').val();
            buyerTel = buyerTel || '';
            buyerTel = buyerTel.replace(/[^0-9]/g, '');
            buyerTel = buyerTel.trim();
            if (!buyerTel.match(/^[0-9]{8,11}$/)) {
                alert('연락처를 입력해주세요.');
                return;
            }

            $("#vExp").val(getTomorrow());
            goPay(document.payForm);
        }

        //결제 최종 요청시 실행됩니다. <<'nicepaySubmit()' 이름 수정 불가능>>
        function nicepaySubmit() {
            document.payForm.submit();
        }

        //결제창 종료 함수 <<'nicepayClose()' 이름 수정 불가능>>
        function nicepayClose() {
            swal({
                type: 'info',
                text: "결제가 취소 되었습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            self.close();
        }

        //가상계좌입금만료일 설정 (today +1)
        function getTomorrow() {
            var today = new Date();
            today.setDate(today.getDate() + 1);
            var yyyy = today.getFullYear().toString();
            var mm = (today.getMonth() + 1).toString();
            var dd = (today.getDate() + 1).toString();
            if (mm.length < 2) {
                mm = '0' + mm;
            }
            if (dd.length < 2) {
                dd = '0' + dd;
            }
            return (yyyy + mm + dd);
        }
    </script>
</head>
<body style=“overflow-X:hidden”>
<form name="payForm" method="post" action="/payer/notification/goPay">

    <input type="hidden" name="chaCd" id="chaCd" value="${map.result.chaCd}">
    <input type="hidden" name="notiMasCd" id="notiMasCd" value="${map.notimasCd}">
    <input type="hidden" name="notiDetCd" id="notiDetCd" value="<c:out value="<%=notidetCd%>" escapeXml="true" />">
    <input type="hidden" name="SelectQuota" id="SelectQuota">
    <input type="hidden" name="PayMethod" id="PayMethod" value="CARD">
    <input type="hidden" name="Amt" id="Amt" value="<c:out value="<%=price%>" escapeXml="true" />">
    <input type="hidden" name="MID" value="<c:out value="<%=merchantID%>" escapeXml="true" />">
    <input type="hidden" name="GoodsName" value="<c:out value="<%=goodsName%>" escapeXml="true" />">
    <input type="hidden" name="GoodsCnt" value="<c:out value="<%=goodsCnt%>" escapeXml="true" />">
    <input type="hidden" name="UserIP" value="<c:out value="<%=request.getRemoteAddr()%>" escapeXml="true" />"/>
    <input type="hidden" name="MallIP" value="<c:out value="<%=inet.getHostAddress()%>" escapeXml="true" />"/>
    <input type="hidden" name="VbankExpDate" id="vExp"/>
    <input type="hidden" name="CharSet" value="<c:out value="<%=charset%>" escapeXml="true" />"/>
    <input type="hidden" name="BuyerEmail" value="${map.result.cusMail}"/>
    <input type="hidden" name="GoodsCl" value="1"/>
    <input type="hidden" name="TransType" value="0"/>
    <input type="hidden" name="EncodeParameters" value="<c:out value="<%=encodeParameters%>" escapeXml="true" />"/>
    <input type="hidden" name="EdiDate" value="<c:out value="<%=ediDate%>" escapeXml="true" />"/>
    <input type="hidden" name="EncryptData" value="<c:out value="<%=hashString%>" escapeXml="true" />"/>
    <input type="hidden" name="TrKey" value=""/>
    <input type="hidden" name="SocketYN" value="Y"/>

    <div class="payfin_area">
        <div class="top">결제정보확인</div>
        <div class="conwrap">
            <div class="con">
                <div class="tabletypea">
                    <table>
                        <colgroup>
                            <col width="30%"/>
                            <col width="*"/>
                        </colgroup>
                        <tr>
                            <th><span>이용기관명</span></th>
                            <td>
                                <input class="form-control" type="text" name="chaName" id="chaName" value="${map.result.chaName}" readonly="readonly">
                            </td>
                        </tr>
                        <tr>
                            <th><span>청구항목명</span></th>
                            <td>
                                <input class="form-control" type="text" name="payItemName" id="payItemName" value="${map.result.itemName}" readonly="readonly">
                            </td>
                        </tr>
                        <tr>
                            <th><span>납부자명</span></th>
                            <td>
                                <input class="form-control" type="text" name="BuyerName" id="BuyerName" value="${map.result.cusName}" readonly="readonly">
                            </td>
                        </tr>
                        <tr>
                            <th><span>결제금액</span></th>
                            <td>
                                <input class="form-control" type="text" name="price" id="price" value="<c:out value="<%=price%>" escapeXml="true" />" readonly="readonly">
                            </td>
                        </tr>
                        <tr>
                            <th><span>납부마감일</span></th>
                            <td>
                                <input class="form-control" type="text" name="endDate" id="endDate" value="${map.result.endDate}" readonly="readonly">
                            </td>
                        </tr>
                        <tr>
                            <th><span>납부자 연락처</span></th>
                            <td><input class="form-control" type="text" name="BuyerTel" id="buyer-tel" value="${map.result.cusHp}" />
                        </tr>
                    </table>
                </div>
            </div>
            <div class="btngroup">
                <a href="#" class="btn_blue" onClick="self.close();">취소</a>
                <a href="#" class="btn_blue" onClick="nicepayStart();">결제하기</a>
            </div>
        </div>
    </div>
</form>
<script src="/assets/bootstrap/bootstrap.min.js"></script>
<script src="/assets/js/sweetalert.js"></script>
<script src="/assets/js/damoa.js"></script>
<script src="/assets/js/payer.js"></script>
</body>
</html>
<%!
    public final synchronized String getyyyyMMddHHmmss() {
        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        return yyyyMMddHHmmss.format(new Date());
    }

    // SHA-256 형식으로 암호화
    public class DataEncrypt {
        MessageDigest md;
        String strSRCData = "";
        String strENCData = "";
        String strOUTData = "";

        public DataEncrypt() {
        }

        public String encrypt(String strData) {
            String passACL = null;
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
                md.reset();
                md.update(strData.getBytes());
                byte[] raw = md.digest();
                passACL = encodeHex(raw);
            } catch (Exception e) {
                System.out.print("암호화 에러" + e.toString());
            }
            return passACL;
        }

        public String encodeHex(byte[] b) {
            char[] c = Hex.encodeHex(b);
            return new String(c);
        }
    }
%>
