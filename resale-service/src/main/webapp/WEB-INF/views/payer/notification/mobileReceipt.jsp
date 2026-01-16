<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/>

<script src="https://cdn.jsdelivr.net/clipboard.js/1.5.3/clipboard.min.js"></script>

<script>
    window.onload = function () {
        $(document).ready(function () {
            var clipboard = new Clipboard('.clipboard');
        });
    };

    // GNB의 Depth 1을 활성화 시켜주는 변수
    var firstDepth = "nav-link-21";
    var secondDepth = "sub-02";

    //카드결제
    function payByCard(notimasCd) {
        var strFeature = "width=700, height=500, scrollbars=no, resizable=yes";

        document.payCardForm.target = "formInfo";
        document.payCardForm.notimasCd.value = notimasCd;
        window.open("", "formInfo", strFeature);
        document.payCardForm.submit();
    }

    //SOL 간편송금 버튼 클릭 시 결제확인 팝업창 노출
    function payBySol(count) {
        $("#popUnSubTot").text($("#unSubTot-" + count).text());
        $("#popEndDate").text($("#endDate-" + count).text());

        $("#popup-transfer-by-sol").modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    function vaNoCopy(cnt) {
        $("#btnCopy-" + cnt).attr("data-clipboard-target", "#vaNo-" + cnt);
        swal({
            type: 'info',
            text: "클립보드에 복사되었습니다.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
    }
</script>

<form name="payCardForm" id="payCardForm" method="post" action="/payer/notification/selectNotiMas">
    <input type="hidden" id="notimasCd" name="notimasCd"/>
    <input type="hidden" id="accessGubn" name="accessGubn" value="mobile"/>
</form>

<div id="invisible_div" style="display:none;"></div>
<div id="contents">
    <div class="container">
        <div id="page-title">
            <div class="row">
                <div class="col-12 form-inline">
                    <h2>모바일 청구서</h2> <span class="badge badge-pill badge-danger">${map.count}</span>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="col col-12">
                <c:if test="${map.count == 0 }">
                    <div class="container mt-3">
                        <table class="table table-sm table-primary mb-5">
                            <tbody>
                                <tr>
                                    <td style="padding: 4rem 0;">
                                        <img src="/assets/imgs/common/mobile-icon-exclamation-mark.png" class="icon mb-3" alt="느낌표 마크" style="width: 100px;">
                                        <br/>
                                        청구내역이 없습니다.
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </c:if>

                <c:if test="${map.count != 0 }">
                    <div id="bill-slider" class="swiper-container">
                        <div class="swiper-wrapper">
                            <c:forEach var="mas" items="${map.masList}" varStatus="status">
                                <div class="swiper-slide">
                                    <div class="container payment-history-slider">
                                        <div class="row">
                                            <div class="col-12">
                                                <div class="selected-payment-amount">
                                                    <span class="short-description">${fn:substring(mas.masMonth, 0, 4)}년 ${fn:substring(mas.masMonth, 4, 6)}월 납부 금액</span>
                                                    <div class="amount" id="unSubTot-${status.count}">
                                                        <fmt:formatNumber pattern="#,###" value="${mas.unSubTot}"/>
                                                    </div>
                                                    <div class="btn-group">
                                                        <c:if test="${mas.endDate ge mas.toDay}">
                                                            <button type="button" class="btn btn-transfer-by-sol" onclick="payBySol('${status.count}');">SOL 간편송금</button>
                                                            <c:if test="${map.chaInfo.usePgYn eq 'Y' && mas.stCd eq 'PA02'}">
                                                                <button type="button" class="btn" onclick="payByCard('${mas.notimasCd}');">카드결제</button>
                                                            </c:if>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="container mt-4">
                                        <div class="row">
                                            <div class="col-12">
                                                <h5>납부자 정보</h5>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-12">
                                                <table class="table table-primary table-mobile-receipt mb-3">
                                                    <tbody>
                                                        <tr>
                                                            <td>납부자명</td>
                                                            <td class="table-title-ellipsis">${map.cusName}</td>
                                                        </tr>
                                                        <tr>
                                                            <td>연락처</td>
                                                            <td>${map.tel}</td>
                                                        </tr>
                                                        <tr>
                                                            <td>납부계좌</td>
                                                            <td>신한<span id="vaNo-${status.count}">${map.vaNo}</span><button type="button" class="btn btn-sm btn-gray-outlined clipboard ml-2" id="btnCopy-${status.count}" onclick="vaNoCopy(${status.count});">복사</button></td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="container mt-4 mb-5">
                                        <div class="row">
                                            <div class="col-12">
                                                <h5>청구서 상세 내역</h5>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-12">
                                                <table class="table table-primary table-mobile-receipt">
                                                    <tbody>
                                                        <tr>
                                                            <td colspan="2" class="text-center"><h4 class="collecter">${map.chaInfo.chaName}</h4></td>
                                                        </tr>
                                                        <tr class="small-row">
                                                            <td>주소</td>
                                                            <td class="table-title-ellipsis">${map.chaInfo.chaAddress1} ${map.chaInfo.chaAddress2}</td>
                                                        </tr>
                                                        <tr class="small-row">
                                                            <td>전화번호</td>
                                                            <td>${map.chaInfo.ownerTel}</td>
                                                        </tr>
                                                        <tr class="small-row">
                                                            <td>납부 마감일</td>
                                                            <td id="endDate-${status.count}">${fn:substring(mas.endDate, 0, 4)}.${fn:substring(mas.endDate, 4, 6)}.${fn:substring(mas.endDate, 6, 8)}</td>
                                                        </tr>
                                                        <c:set var="cnt" value="1"/>
                                                        <c:forEach var="det" items="${map.detList}" varStatus="status">
                                                            <c:choose>
                                                                <c:when test="${mas.notimasCd eq det.notimasCd}">
                                                                    <c:choose>
                                                                        <c:when test="${cnt == 1}">
                                                                            <tr class="total">
                                                                                <td>총 <span class="font-blue">${det.detCnt}</span>건</td>
                                                                                <td><fmt:formatNumber pattern="#,###" value="${det.subTot}"/></td>
                                                                            </tr>
                                                                        </c:when>
                                                                    </c:choose>
                                                                    <tr>
                                                                        <td>${det.payItemName}</td>
                                                                        <td><fmt:formatNumber pattern="#,###" value="${det.payItemAmt}"/></td>
                                                                    </tr>
                                                                    <c:set var="cnt" value="${cnt+1}"/>
                                                                </c:when>
                                                            </c:choose>
                                                        </c:forEach>
                                                        <tr>
                                                            <td>납부한 금액</td>
                                                            <td><fmt:formatNumber pattern="#,###" value="${mas.rcpAmt}"/></td>
                                                        </tr>
                                                        <tr class="total">
                                                            <td>미납 금액</td>
                                                            <td><fmt:formatNumber pattern="#,###" value="${mas.unSubTot}"/></td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="swiper-pagination"></div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false"/>

<%-- sms고지 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/transfer-by-sol.jsp" flush="false"/>

<script src="/assets/js/swiper.js"></script>

<script>
    $(document).ready(function () {
        var varUA = navigator.userAgent.toLowerCase(); //userAgent 값 얻기

        if (varUA.match('android') != null) {
            //안드로이드 일때 처리

        } else if (varUA.indexOf("iphone") > -1 || varUA.indexOf("ipad") > -1 || varUA.indexOf("ipod") > -1) {
            //IOS 일때 처리
            $("#btnCopy").attr("hidden", "true");
        } else {
            //아이폰, 안드로이드 외 처리
        }
    });

    $(".see-more").click(function () {
        $(".hidden-list").slideToggle();
    });

    var mySwiper = new Swiper('.swiper-container', {
        direction: 'horizontal',
        loop: false,
        autoplay: false,
        autoHeight: true,
        pagination: {
            el: '.swiper-pagination',
        },

        navigation: {
            prevEl: '.swiper-button-next',
            nextEl: '.swiper-button-prev',
        },
    })
</script>
