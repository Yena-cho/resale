<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/>

<script type="text/javascript">
    var cuPage = 1;
    var firstDepth = "nav-link-35";
    var secondDepth = "sub-04";
</script>

<script type="text/javascript">
    $(document).ready(function () {
        getYearsBox('yearsBox');
        getMonthBox2('monthBox', '3');
        getYearsBox('yearsBox2');
        getMonthBox2('monthBox2', '1');

        var sYear = moment().add(-1, 'month').format('YYYY');
        var sMonth = moment().add(-1, 'month').format('MM');
        var eYear = moment().add(-12, 'month').format('YYYY');
        var eMonth = moment().add(-12, 'month').format('MM');

        $('#yearsBox2').val(sYear).prop("selected", true);
        $('#monthBox2').val(sMonth).prop("selected", true);
        $('#yearsBox').val(eYear).prop("selected", true);
        $('#monthBox').val(eMonth).prop("selected", true);

        var nowDateTime = moment(new Date()).format('YYYY년 MM월 DD일 H:mm');

        $('#nowDateTime').html(nowDateTime);

        fnSearch();
    });

    // validation
    function fnValidation() {
        var sYYYY = $('#yearsBox option:selected').val();
        var sMM = $('#monthBox option:selected').val();
        var eYYYY = $('#yearsBox2 option:selected').val();
        var eMM = $('#monthBox2 option:selected').val();
        var startDate = $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val();
        var endDate = $('#yearsBox2 option:selected').val() + "" + $('#monthBox2 option:selected').val();
        var nowDate = moment(new Date).format("YYYYMM");

        if (sYYYY == "선택" || sMM == "선택" || eYYYY == "선택" || eMM == "선택") {
            swal({
                type: 'info',
                text: "이용기간을 선택해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (endDate < startDate) {
            swal({
                type: 'info',
                text: "시작 기간이 종료 기간보다 클 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (endDate - startDate > 101) {
            swal({
                type: 'info',
                text: "최대 조회기간은 1년 입니다. 다시 선택해 주세요",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (endDate >= nowDate) {
            swal({
                type: 'info',
                text: "현재달보다 이후를 조회할 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        return true;
    }

    // 검색
    function fnSearch(page) {
        if (page == null || page == 'undefined') {
            cuPage = "";
            cuPage = 1;
        } else {
            cuPage = page;
        }

        if (fnValidation()) {
            var url = "/org/myPage/monthSumListAjax";
            var param = {
                startDate: $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val(),
                endDate: $('#yearsBox2 option:selected').val() + "" + $('#monthBox2 option:selected').val(),
                curPage: cuPage,
                pageScale: $('#pageScale option:selected').val()
            };

            $.ajax({
                type: "post",
                async: true,
                url: url,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (result) {
                    if (result.retCode == "0000") {
                        fnGrid(result, 'resultBody');
                    } else {
                        swal({
                            type: 'error',
                            text: result.retMsg,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });
                    }
                }
            });
        }
    }

    // 데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';

        if (result.count <= 0) {
            str += '<tr><td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td><a href="javascript:;" onclick="fnDaySum(' + v.month + ');" >' + formatYearMonth(v.month) + '</a></td>';
                str += '<td class="text-right summary-bg">' + numberToCommas(v.sumFee) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.notiCnt) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.notiFee) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.rcpCnt) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.rcpFee) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.smsCnt) + '</td>';
                // str += '<td class="text-right">' + numberToCommas(v.smsFee) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.lmsCnt) + '</td>';
                // str += '<td class="text-right">' + numberToCommas(v.lmsFee) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.atCnt) + '</td>';
                // str += '<td class="text-right">' + numberToCommas(v.atFee) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.smsFee+ v.lmsFee + v.atFee) + '</td>';
                /*str += '<td class="text-right">' + numberToCommas(v.prnCnt) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.prnFee) + '</td>';*/
                // str += '<td class="hidden-on-mobile"><input type="button" onclick="fnPrint(' + v.month + ');" class="btn btn-sm btn-gray-outlined btn-damoa-commission-receipt ml-2" value="인쇄"/></td>'
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    // 페이징 버튼
    function list(page) {
        $('#pageNo').val(page);
        fnSearch(page);
    }

    function pageChange() {
        fnSearch(cuPage);
    }

    function fnPrint(month) {
        var strUrl = 'pdfMakeBill';
        var alertResult = false;

        $('#sFileName').val($('#chaCd').val() + getCurrentDate());

        swal({
            type: 'question',
            html: "다운로드 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true,
            onAfterClose: function () {
                if (alertResult) {
                    villfile();
                }
            }
        }).then(function (result) {
            if (result.value == true) {
                $('#month').val(month);
                alertResult = true;
            }
        });

    }

    // 영수증 파일생성
    function villfile() {
        fncClearTime();
        $('#sBrowserType').val('ie');
        $('#pdfMakeForm').submit();
    }

    // 일별 통계 페이지 이동
    function fnDaySum(month) {
        $('#setMonth').val(month);
        $('#daySum').submit();
    }
</script>

<form id="daySum" name="daySum" method="post" action="/org/myPage/daySum">
    <input type="hidden" id="setMonth" name="setMonth"/>
</form>

<form id="pdfMakeForm" name="pdfMakeForm" method="post" action="pdfMakeSumRcpBill">
    <input type="hidden" id="chaTrTy" name="chaTrTy" value="${orgSess.chaTrTy}"/>
    <input type="hidden" id="finishYn" name="finishYn"/>
    <input type="hidden" id="month" name="month"/>
    <input type="hidden" id="sBrowserType" name="sBrowserType"/>
</form>

<input type="hidden" id="pageCnt" name="pageCnt" value="${map.PAGE_SCALE}">
<input type="hidden" id="pageNo" name="pageNo" value="1">

<div id="contents">
    <div class="container">
        <div id="page-title">
            <div class="row">
                <div class="col-8">
                    <h2>이용요금조회</h2>
                </div>
                <div class="col-4 text-right">
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>가상계좌 수납관리 서비스 이용요금을 확인할 수 있는 화면입니다.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-7">
                <h5>이번달 이용 현황</h5>
            </div>
            <div class="col-5 text-right">
                <span class="guide-mention font-blue">* VAT 포함</span>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col">
                <div class="table-responsive mb-2">
                    <table id="bill-reference-list" class="table table-sm table-hover table-primary">
                        <colgroup>
                            <col width="100">
                            <col width="130">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                            <col width="110">
                        </colgroup>

                        <thead>
                        <tr>
                            <th rowspan="2">이용월</th>
                            <th rowspan="2"><strong>이용요금(원)</strong></th>
                            <th colspan="4">수수료</th>
                            <th colspan="4">부가서비스</th>
                        </tr>
                        <tr>
                            <th class="border-l">청구건수</th>
                            <th>금액(원)</th>
                            <th>입금건수</th>
                            <th>금액(원)</th>
                            <th style="border-left: 2px solid #737373;">SMS건수<br>(20원/건)</th>
                            <th>LMS건수<br>(40원/건)</th>
                            <th>알림톡<br>(20원/건)</th>
                            <th>금액(원)</th>
                            <%--<th style="border-left: 2px solid #737373;">출력건수</th>
                            <th>금액(원)</th>--%>

                        </tr>
                        </thead>

                        <tbody>
                        <tr>
                            <c:choose>
                                <c:when test="${map.size() > 0}">
                                    <td><fmt:parseDate pattern="yyyyMM" var="masMonth" value="${map.nowMonth}"/><fmt:formatDate pattern="yyyy.MM" value="${masMonth}"/></td>
                                    <td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.sumFee == null || map.sumFee == '' ? 0 : map.sumFee}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.notiCnt == null || map.notiCnt == '' ? 0 : map.notiCnt}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.notiFee == null || map.notiFee == '' ? 0 : map.notiFee}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.rcpCnt == null || map.rcpCnt == '' ? 0 : map.rcpCnt}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.rcpFee == null || map.rcpFee == '' ? 0 : map.rcpFee}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.smsCnt == null || map.smsCnt == '' ? 0 : map.smsCnt}"/></td>
                                    <%--                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.smsCnt == null || map.smsCnt == '' ? 0 : map.smsCnt * 20}"/></td>--%>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.lmsCnt == null || map.lmsCnt == '' ? 0 : map.lmsCnt}"/></td>
                                    <%--                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.lmsCnt == null || map.lmsCnt == '' ? 0 : map.lmsCnt * 40}"/></td>--%>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.atCnt == null || map.atCnt == '' ? 0 : map.atCnt}"/></td>
                                    <%--                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.atFee == null || map.atFee == '' ? 0 : map.atFee}"/></td>--%>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${(map.smsCnt == null || map.smsCnt == '' ? 0 : map.smsCnt * 20) + (map.lmsCnt == null || map.lmsCnt == '' ? 0 : map.lmsCnt * 40) + (map.atFee == null || map.atFee == '' ? 0 : map.atFee)}"/></td>
                                    <%--<td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.prnCnt == null || map.prnCnt == '' ? 0 : map.prnCnt}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${map.prnFee == null || map.prnFee == '' ? 0 : map.prnFee}"/></td>--%>
                                </c:when>

                                <c:otherwise>sp
                                    <td colspan="10">[조회된 내역이 없습니다.]</td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-12">
                <div class="guide-mention">* 상기 건수 및 금액은 <strong id="nowDateTime"></strong> 기준입니다.</div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-12">
                <h5>월별 이용내역 조회</h5>
            </div>
        </div>
    </div>

    <div class="container">
        <form class="search-box">
            <div class="row mt-3">
                <label class="col-form-label col col-md-2 col-sm-3 col-3">이용기간</label>
                <div class="col col-md-10 col-sm-9 col-9 form-inline">
                    <select class="form-control" id="yearsBox">
                        <option>선택</option>
                    </select>
                    <span class="ml-1 mr-1">년</span>
                    <select class="form-control" id="monthBox">
                        <option>선택</option>
                    </select>
                    <span class="ml-1 mr-1">월</span>
                    <span class="ml-1 mr-1">~</span>
                    <select class="form-control" id="yearsBox2">
                        <option>선택</option>
                    </select>
                    <span class="ml-1 mr-1">년</span>
                    <select class="form-control" id="monthBox2">
                        <option>선택</option>
                    </select>
                    <span class="ml-1 mr-auto">월</span>
                </div>
            </div>
            <div class="row mt-3">
                <div class="col-12 text-center">
                    <input type="button" class="btn btn-primary btn-wide" onclick="fnSearch();" value="조회"/>
                </div>
            </div>
        </form>
    </div>

    <div class="container">
        <div class="table-option row mb-2">
            <div class="col-md-6 col-sm-12 form-inline"></div>
            <div class="col-md-6 col-sm-12 text-right mt-1">
                <span class="guide-mention font-blue">* VAT 포함</span>
            </div>
        </div>

        <div class="row">
            <div class="table-responsive pd-n-mg-o col mb-2">
                <table id="useage-commission-list" class="table table-sm table-hover table-primary table-btn-td">
                    <colgroup>
                        <col width="100">
                        <col width="140">
                        <col width="110">
                        <col width="110">
                        <col width="110">
                        <col width="110">
                        <col width="110">
                        <col width="110">
                        <col width="110">
                        <col width="110">
                        <col width="110">
                        <col width="110">
                        <col width="90">
                    </colgroup>

                    <thead>
                    <tr>
                        <th rowspan="2">이용월</th>
                        <th rowspan="2"><strong>이용요금(원)</strong></th>
                        <th colspan="4">수수료</th>
                        <th colspan="4">부가서비스</th>
                        <%--<th rowspan="2" class="hidden-on-mobile">영수증</th>--%>
                    </tr>
                    <tr>
                        <th class="border-l">청구건수</th>
                        <th>금액(원)</th>
                        <th>입금건수</th>
                        <th>금액(원)</th>
                        <th style="border-left: 2px solid #737373;">SMS건수<br>(20원/건)</th>
                        <th>LMS건수<br>(40원/건)</th>
                        <th>알림톡<br>(20원/건)</th>
                        <th>금액(원)</th>
                        <%--<th style="border-left: 2px solid #737373;">출력건수</th>
                        <th>금액(원)</th>--%>
                        <%--<th>입금건수</th>
                        <th>금액(원)</th>--%>
                    </tr>
                    </thead>

                    <tbody id="resultBody">
                    <c:choose>
                        <c:when test="${map.count > 0}">
                            <c:forEach var="row" items="${map.list}" varStatus="status">
                                <tr>
                                    <fmt:parseDate pattern="yyyyMM" var="masMonth2" value="${row.month}"/>
                                    <td><a href="javascript:;" onclick="fnDaySum('${row.month}');"><fmt:formatDate pattern="yyyy.MM" value="${masMonth2}"/></a></td>
                                    <td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${row.sumFee}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.notiCnt}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.notiFee}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.rcpCnt}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.rcpFee}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.smsCnt}"/></td>
                                        <%--                                        <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.smsFee}"/></td>--%>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.lmsCnt}"/></td>
                                        <%--                                        <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.lmsFee}"/></td>--%>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.atCnt}"/></td>
                                        <%--                                        <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.atFee}"/></td>--%>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.smsFee + row.lmsFee + row.atFee}"/></td>
                                    <%--<td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.prnCnt}"/></td>
                                    <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.prnFee}"/></td>
                                    <td class="hidden-on-mobile">
                                        <input type="button" onclick="fnPrint('${row.month}');" class="btn btn-sm btn-gray-outlined btn-damoa-commission-receipt ml-2" value="인쇄"/>
                                    </td>--%>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="guide-mention">* 이용월을 클릭하시면 일별 내역을 보실 수 있습니다.</div>
            </div>
        </div>
    </div>

    <div class="container mt-5">
        <div id="quick-instruction" class="foldable-box">
            <div class="row foldable-box-header">
                <div class="col-8"><img src="/assets/imgs/common/icon-notice.png">알려드립니다.</div>
                <div class="col-4 text-right"><img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
                </div>
            </div>
            <div class="row foldable-box-body">
                <div class="col">
                    <h6>■ 이용요금</h6>
                    <ul>
                        <li>수수료와 부가서비스 이용료를 합산하여 청구</li>
                        <li>당월사용분은 익월 초, 사용월 말일자로 세금계산서 발행</li>
                    </ul>

                    <h6>■ 이번 달 이용현황</h6>
                    <ul>
                        <li>당월의 이용현황을 현재 기준으로 건수 및 금액 확인</li>
                    </ul>

                    <h6>■ 월별 이용내역 조회</h6>
                    <ul>
                        <li>지난 이용내역을 기간별로 설정하여 조회</li>
                        <li>이용월을 클릭하면 일별 상세내역 조회 가능</li>
                    </ul>

                    <%--<h6>■ 입금수수료 영수증 인쇄</h6>
                    <ul>
                        <li>가상계좌 입금건에 대한 수수료 부과에 대한 영수증 인쇄 가능</li>
                        <li>신한은행에서 출금하는 부분으로 실출금여부와 상관없이 출력은 가능</li>
                        <li>가상계좌 수납관리 서비스의 운영사 (주)핑거가 자동출금하는 관리수수료는 세금계산서로 매달 첫 영업일 발행</li>
                    </ul>--%>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>
