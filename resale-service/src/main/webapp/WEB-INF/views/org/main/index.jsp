<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/>

<script>
    var firstDepth = false;
    var secondDepth = false;

    var rcpRatio1 = 0;
    var rcpRatio2 = 0;

    function fn_fullView(str) {
        document.gubunFrm.viewGb.value = str;
    }

    function moveFaq(no) {
        if (no == 1) {
            location.href = "/common/customerAsist/faqList";
        } else {
            location.href = "/common/customerAsist/faqList?no=" + no;
        }
    }

    //입금현황 자세히 보기
    function fn_payDetailMove() {
        location.href = "/org/receiptMgnt/paymentList";
    }

    // 전화상담예약
    function fn_makeReservation() {
        $('#make-reservation').modal({backdrop: 'static', keyboard: false});
    }

    // 이용신청서 다운로드
    function fn_download() {
        fncClearTime();
        $('#fileName').val('fileOrgManual');

        document.fileForm.action = "/common/library/download";
        document.fileForm.submit();
    }

    function goMenu(val) {
        if ('receiptList' == val) {
            document.goMenu.action = '/org/receiptMgnt/receiptList';
            document.goMenu.submit();
        }
    }

    function fn_Amount(state) {
        if (state == "amount") {
            $(".amount").css("display", "block");
            $(".amount-money").css("display", "none");
            $("#amount-btn").removeClass('btn-outlined');
            $("#amount-money-btn").addClass('btn-outlined');
        } else {
            $(".amount").css("display", "none");
            $(".amount-money").css("display", "block");
            $("#amount-btn").addClass('btn-outlined');
            $("#amount-money-btn").removeClass('btn-outlined');
        }
    }

</script>

<form name="goMenu" id="goMenu" method="post">
    <input type="hidden" name="gubn" id="gubn" value="main">
</form>

<form name="gubunFrm" id="gubunFrm">
    <input type="hidden" name="viewGb" id="viewGb" value="NOTICE">
</form>

<form id="fileForm" name="fileForm" method="post">
    <input type="hidden" id="fileName" name="fileName">
    <input type="hidden" id="flag" name="flag" value="org">
</form>

<div id="contents">
    <div class="section-container mb-5">
        <div id="collecter-dashboard">
            <div class="dashboard-month-controller">
                <div class="container">
                    <div class="row">
                        <h3 class="mx-auto title font-weight-400">청구월별 수납현황</h3>
                    </div>
                    <div class="row text-center">
                        <div class="month-controller mx-auto">
                            <span class="mr-4" onclick="javascript:MoveSumMonth(-1);"> < </span>
                            <span id="SUMmonth" class="font-weight-400"></span>
                            <span class="ml-4" onclick="javascript:MoveSumMonth(1);"> > </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="container" style="position: relative;">
                <div class="text-center mt-3">
                    <button class="btn btn-xs btn-primary" id="amount-btn" onclick="fn_Amount('amount');">건별 보기</button>
                    <button class="btn btn-xs btn-primary btn-outlined" id="amount-money-btn" onclick="fn_Amount('amountMoney');">금액별 보기</button>
                </div>

                <div class="row summary">
                    <div class="col col-md-3 col-sm-6 col-6 text-center">
                        <div class="img-wrapper"><img src="/assets/imgs/common/icon-won-in-monitor.png" class="icon-won-in-monitor"></div>
                        <label>청구</label>
                        <div class="amount issue font-weight-400" id="SUMnoticnt" style="display: block;">0</div>
                        <div class="amount-money" id="SUMnotiamt" style="display: none;">0</div>
                    </div>
                    <div class="col col-md-3 col-sm-6 col-6 text-center">
                        <div class="img-wrapper"><img src="/assets/imgs/common/icon-coin-on-hand.png" class="icon-coin-on-hand"></div>
                        <label>입금</label>
                        <div class="amount issue font-weight-400" id="SUMrcpcnt" style="display: block;">0</div>
                        <div class="amount-money" id="SUMrcpamt" style="display: none;">0</div>
                    </div>
                    <div class="col col-md-3 col-sm-6 col-6 text-center">
                        <div class="img-wrapper"><img src="/assets/imgs/common/icon-statment-with-cog.png" class="icon-statment-with-cog"></div>
                        <label>미납</label>
                        <div class="amount issue font-weight-400" id="SUMrcpcntNot" style="display: block;">0</div>
                        <div class="amount-money" id="SUMrcpamtNot" style="display: none;">0</div>
                    </div>
                    <div class="col col-md-3 col-sm-6 col-6 text-center">
                        <div class="img-wrapper"><img src="/assets/imgs/common/icon-letter-in-envelop.png" class="icon-letter-in-envelop"></div>
                        <label>문자메시지 발송</label>
                        <div class="amount issue font-weight-400" id="SUMsmscnt" style="display: block;">0</div>
                        <div class="amount-money" id="SUMsmsfee" style="display: none;">0</div>
                    </div>
                </div>

                <div class="row hidden-on-mobile">
                    <div class="btn-group inline mx-auto">
                        <a href="#" class="btn btn-primary mr-3" onclick="go_menu(2);">현장수납등록</a>
                        <a href="#" class="btn btn-secondary" onclick="go_menu(5);">현금영수증 발급</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="payer-short-report" class="container">
        <div class="row">
            <div class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row">
                    <div class="col col-12">
                        <div class="title-sol-type purple">
                            <span>최근 입금된 내역을 안내해드립니다.</span>
                            <h4>입금내역</h4>
                        </div>
                        <div class="see-all text-right">
                            <a href="/org/receiptMgnt/paymentList">> 전체보기</a>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col col-12 text-center">
                        <table class="table table-sm table-hover table-summary">
                            <thead>
                                <tr>
                                    <th>입금일자</th>
                                    <th>입금자명</th>
                                    <th>입금액(원)</th>
                                </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${map.pList}" var="each">
                                <tr>
                                    <td class="text-center">${fn:substring(each.payDay, 0, 4)}.${fn:substring(each.payDay, 4, 6)}.${fn:substring(each.payDay, 6, 8)}</td>
                                    <td class="text-center"><c:if test="${not empty fn:trim(each.cusName)}">${fn:trim(each.cusName)}</c:if></td>
                                    <td class="text-right" style="padding-right:20px;"><fmt:formatNumber pattern="#,##0" value="${each.rcpAmt}" /></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row">
                    <div class="col col-12">
                        <div class="title-sol-type blue">
                            <span>입금금액 비율이 궁금하세요?</span>
                            <h4>입금현황</h4>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col col-sm-6 col-12 text-center">
                        <div class="doughnut-wrap" id="canvas"></div>
                    </div>
                    <div class="col col-sm-6 col-12 text-center">
                        <ul class="graph-data-text">
                            <li class="text-left">기준년월 : <span id="Limonth">데이터없음</span></li>
                            <li class="text-left">납부금액비율 : <span id="LircpRatio1">0</span> %</li>
                            <li class="text-left">미납금액비율 : <span id="LircpRatio2">0</span> %</li>
                            <li class="text-left">과/미납여부 : <span id="LioverRcpYn"></span></li>
                        </ul>
                    </div>
                </div>

                <div class="text-center mt-3">
                    <span id="payResult"></span>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row">
                    <div class="col col-12">
                        <div class="title-sol-type orange">
                            <span>가상계좌 수납관리 서비스의 최신 소식을 알려드려요!</span>
                            <h4>공지사항</h4>
                        </div>
                        <div class="see-all text-right">
                            <a href="/common/customerAsist/noticeList">> 전체보기</a>
                        </div>
                    </div>
                </div>

                <div class="row mt-2 mb-5">
                    <div class="col col-4">
                        <img src="/assets/imgs/common/fin.png" style="width:100%;">
                    </div>

                    <div class="col col-8">
                        <table id="notice" class="table table-sm table-hover table-summary">
                            <tbody>
                            <c:choose>
                                <c:when test="${empty map.nList}">
                                    <tr>
                                        <td colspan="2" style="text-align: center;">[조회된 내역이 없습니다.]</td>
                                    </tr>
                                </c:when>

                                <c:otherwise>
                                    <c:forEach var="list" items="${map.nList}">
                                        <tr>
                                            <td class="has-ellipsis has-arrow">
                                                <a class="text-secondary" href="/common/customerAsist/noticeDetail?no=${list.no}">
                                                    <c:choose>
                                                        <c:when test="${list.title.length() > 15}">
                                                            ${list.title.substring(0,15) }...
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${list.title}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </a>
                                                <c:if test="${list.filesize > 0}">
                                                    <img src="/assets/imgs/common/icon-clip.png" class="icon-clip">
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row">
                    <div class="col col-12">
                        <div class="title-sol-type navy">
                            <span>이용에 어려움이 있으신가요?</span>
                            <h4>자주하는 질문</h4>
                        </div>
                    </div>
                </div>

                <div id="faq" class="row mt-2 mb-5 no-gutters">
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(1)">전체</div>
                    </div>
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(4)">사이트 이용</div>
                    </div>
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(6)">수납</div>
                    </div>
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(7)">부가서비스</div>
                    </div>
                    <div class="col col-4">
                        <div class="category" onclick="moveFaq(5)">고지</div>
                    </div>
                    <div class="col col-4 text-right">
                        <img src="/assets/imgs/common/img-sol-character-01.png" style="max-width:107px;" class="sol-character">
                    </div>
                </div>
            </div>
        </div>

        <div class="row hidden-on-mobile">
            <div id="application-download" class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row mb-2">
                    <div class="col col-12">
                        <div class="title-sol-type green">
                            <span>가상계좌 수납관리 서비스에 대해 알고 싶으세요?</span>
                            <h4>매뉴얼 다운로드</h4>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col">
                        <div class="row application-box">
                            <div class="col col-8">
                                <label>가상계좌 수납관리 서비스 기관담당자 매뉴얼</label>
                                <button type="button" class="btn btn-block btn-cs-center btn-download-application col-8" onclick="fn_download();">다운로드</button>
                            </div>
                            <div class="col col-4 text-right">
                                <img src="/assets/imgs/common/img-download-certificate.png" style="height:93px; width:92px;">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="cs-center" class="col col-md-6 col-sm-12 col-12 mb-4">
                <div class="row mb-2">
                    <div class="col col-12">
                        <div class="title-sol-type sand">
                            <span>전문 상담원이 안내해드립니다.</span>
                            <h4>고객센터</h4>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col">
                        <div class="row cs-box">
                            <div class="col col-12">
                                <label>전화상담을 통해 전문 상담원이 안내해드립니다.</label>
                            </div>
                            <div class="col col-6">
                                <a href="tel:02-786-8201" class="btn btn-block btn-cs-center btn-cs-phone-number">02-786-8201</a>
                            </div>
                            <div class="col col-6">
                                <button type="button" class="btn btn-block btn-cs-center btn-book-counselling"data-toggle="modal" onclick="fn_makeReservation();">전화 상담 예약</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.js"></script>

<script>
    var canvas = '<canvas id="doughnutChart" width="180" height="180"></canvas>';
    $('#canvas').html(canvas);
    var ctx = document.getElementById('doughnutChart');

    $(document).ready(function () {
        var year = '${map.masMonth}'.substring(0, 4);
        var month = '${map.masMonth}'.substring(4, 6);

        $('#SUMmonth').text(year + "." + month);
        fnXmontSum(year + "" + month);
    });

    function MoveSumMonth(move) {
        var sumYearMonth = $('#SUMmonth').text();
        var _sumYearMonth = sumYearMonth.split('.');
        var _year = _sumYearMonth[0];
        var _month = _sumYearMonth[1];
        var date = new Date(_year + "/" + _month + "/" + "01");

        date.setMonth(date.getMonth() + move);
        var oYear = date.getFullYear();
        var oMonth = (date.getMonth() + 1);
        if (oMonth < 10) {
            oMonth = "0" + oMonth;
        }

        $('#SUMmonth').text(oYear + "." + oMonth);
        fnXmontSum(oYear + "" + oMonth);
    }

    // 검색
    function fnXmontSum(yyyymm) {
        var url = "/org/main/common/selXmontSum";
        if (yyyymm == '') {
            yyyymm = '201804';
        }
        var param = {
            chaCd: $('#chaCd').val(),
            month: yyyymm
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                $("#SUMnoticnt").html(numberToCommas(result.noticnt)); 		// 청구건수
                $("#SUMnotiamt").html(numberToCommas(result.notiamt));      // 청구금액
                $("#SUMrcpcnt").html(numberToCommas(result.rcpcnt));		// 입금건수
                $("#SUMrcpamt").html(numberToCommas(result.rcpamt));        // 입금금액
                $("#SUMsmscnt").html(numberToCommas(result.smscnt));		// 문자발송건수
                $("#SUMsmsfee").html(numberToCommas(result.smsfee));        // 문자발송수수료
                $("#SUMrcpcntNot").html(numberToCommas(result.rcpcntNot));  // 미납건수
                $("#SUMrcpamtNot").html(numberToCommas(result.rcpamtNot));  // 미납금액
                $("#payDay").text(result.day + " 기준");
                if (result.result > 0) {
                    $("#payResult").text("지난달 " + result.today + "일 보다 입금률이 " + result.result + "% 상승하였습니다.");
                }
                var canvas = '<canvas id="doughnutChart" width="180" height="180"></canvas>';
                $('#canvas').html(canvas);
                ctx = document.getElementById('doughnutChart');

                rcpRatio1 = result.rcpRatio1;
                rcpRatio2 = result.rcpRatio2;

                if (rcpRatio1 == 0 && rcpRatio2 == 0) {
                    var html = '';
                    html += '<div class="container">';
                    html += '<img src="/assets/imgs/common/mobile-icon-exclamation-mark.png" class="icon mt-5 mb-4" alt="느낌표 마크" style="width: 80px;">';
                    html += '<br/>';
                    html += '조회된 내역이 없습니다.';
                    html += '</div>';
                    $('#canvas').html(html);
                } else {
                    myDoughnutChart = new Chart(ctx, {
                        type: 'doughnut',
                        data: {
                            labels: ["납부", "미납"],
                            datasets: [{
                                label: '입금완료율',
                                data: [rcpRatio1, rcpRatio2],
                                backgroundColor: [
                                    'rgba(54, 162, 235, 1)',
                                    'rgba(255, 159, 64, 1)'
                                ]
                            }]
                        }
                    });
                }

                if (result.month == '') {
                    $("#Limonth").html("데이터없음");
                } else {
                    var _yyyymm = result.month;
                    _yyyymm = _yyyymm.substring(0, 4) + "." + _yyyymm.substr(4);
                    $("#Limonth").html(_yyyymm);
                }

                if (result.rcpRatio1 == '') {
                    $("#LircpRatio1").html("-");
                } else {
                    $("#LircpRatio1").html(result.rcpRatio1);
                }

                if (result.rcpRatio1 == '' && result.rcpRatio2 == '') {
                    $("#LircpRatio2").html("-");
                } else {
                    $("#LircpRatio2").html(result.rcpRatio2);
                }

                if (result.overRcpYn == 'Y') {
                    $("#LioverRcpYn").html("과수납발생");
                } else if (result.overRcpYn == 'N') {
                    if (result.rcpRatio2 == '') {
                        $("#LioverRcpYn").html("입금만발생");
                    } else {
                        $("#LioverRcpYn").html("미수납발생");
                    }
                } else {
                    $("#LioverRcpYn").html("");
                }
            }
        });
    }

    function go_menu(num) {
        if (num == 1) {
            // 청구현황조회
            location.href = "/org/claimMgmt/claimSel";
        }
        else if (num == 2) {
            // 입금수기등록
            location.href = "/org/receiptMgnt/directReceiptReg";
        }
        else if (num == 3) {
            // 미납건 SMS발송
            location.href = "/org/receiptMgnt/receiptList";
        }
        else if (num == 4) {
            // SMS발송등록
            location.href = "/org/receiptMgnt/receiptList";
        }
        else if (num == 5) {
            // 현금영수증발급
            location.href = "/org/receiptMgnt/cashReceipt";
        }
    }
</script>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>

<%-- 전화예약상담 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/counselling-on-call.jsp" flush="false"/>
