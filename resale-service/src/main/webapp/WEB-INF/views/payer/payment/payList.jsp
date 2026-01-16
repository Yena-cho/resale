<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/>

<script>
    var cuPage = 1;
    // GNB의 Depth 1을 활성화 시켜주는 변수
    var firstDepth = "nav-link-22";
    var secondDepth = "sub-01";
    var toDay = getFormatCurrentDate();
    var message = '납부내역을 선택해주세요.';

    $(document).ready(function () {
        $('#row-th').click(function () {
            if ($(this).prop('checked')) {
                $('input:checkbox[name=checkList]').prop('checked', true);
            } else {
                $('input:checkbox[name=checkList]').prop('checked', false);
            }
        });

        $(document).on('click', 'input:checkbox[name=checkList]', function () {
            var _this = $(this).closest("table").attr("id");
            var numCheckbox = $("#" + _this + " .table-check-child").length;

            if ($(this).is(":checked")) {
                if ($("#" + _this + " .table-check-child:checked").length == numCheckbox) {
                    $("#" + _this + " .table-check-parents").prop("checked", true);
                }
            } else if ($(this).not(":checked")) {
                $("#" + _this + " .table-check-parents").prop("checked", false);
            }
        });

        prevDate(1);
        fnSearch(1);
    });

    function fnSearch(page) {

        var tmasMonth = replaceDot($('#tDate').val());
        var fmasMonth = replaceDot($('#fDate').val());

        var vdm = dateValidity(fmasMonth, tmasMonth);
        if (vdm != 'ok') {
            swal({
                type: 'info',
                text: vdm,
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (page == null || page == 'undefined') {
            cuPage = "";
            cuPage = 1;
        } else {
            cuPage = page;
        }

        if (tmasMonth < fmasMonth) {
            swal({
                type: 'info',
                text: "조회시작년월이 더 클 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        var url = "/payer/payment/payListAjax";

        var param = {
            vaNo: $("#vaNo").val(),
            tmasMonth: tmasMonth, //조회 시작년월 
            fmasMonth: fmasMonth, //조회 종료년월
            cusName: $("#cusName").val(),
            chaName: $("#chaName").val(),
            chaCd: $("#chaCd").val(),
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val(),
            searchOrderBy: $('#searchOrderBy option:selected').val()
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {

                if (result.retCode == "0000") {
                    fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
                    ajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'error',
                        text: result.retCode,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });

    }

    //데이터 새로고침
    function fnGrid(result, obj) {
        $("#listCount").text(result.count);
        var str = '';

        if (result.payCount <= 0) {
            str += '<tr><td colspan="8" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.payList, function (i, v) {
                str += '<tr>';
                str += '<td class="hidden-on-mobile">';
                str += '<input class="form-check-input table-check-child" type="checkbox" name="checkList" id="row-' + v.rn + '" value="' + v.rcpmasCd + '" onchange="changeBGColor(this)">';
                str += '<label for="row-' + v.rn + '"><span></span></label>';
                str += '</td>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + dotDate(v.payDay2) + '</td>';
                str += '<td>' + dotDate(v.masMonth) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.payItemAmt) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.rcpAmt) + '</td>';

                if (v.rcpMasSt != 'PA09') {
                    if(v.notiMasSt != null){
                        str += '<td>' + v.notiMasSt + '</td>';
                    }else{
                        str += '<td>수납</td>';
                    }
                } else {
                    str += '<td>카드취소</td>';
                }

                if ("VAS" == v.sveCd) {
                    str += '<td>가상계좌</td>';
                }
                if ("DCS" == v.sveCd) {
                    str += '<td>현금</td>';
                }
                if ("DCD" == v.sveCd) {
                    str += '<td>오프라인카드</td>';
                }
                if ("DVA" == v.sveCd) {
                    str += '<td>무통장입금</td>';
                }
                if ("OCD" == v.sveCd) {
                    str += '<td>온라인카드';
                    if (v.packetNo.length > 17) {
                        str += '<button class="btn btn-xs btn-outline-secondary" onclick=printReceipt("' + v.packetNo + '")>카드영수증</button>';
                    }
                    str += '</td>'
                }
                ;
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    //화면보여주는 개수 변경
    function pageChange() {
        fnSearch(cuPage);
    }

    //정렬 변경
    function arrayChange() {
        fnSearch(cuPage);
    }

    //페이징 조회
    function list(page) {
        fnSearch(page);
    }

    //날짜 포맷추가
    function dotDate(val) {
        if (val.length < 7) {
            val = val.substring(0, 4) + "." + val.substring(4, 6);
        } else {
            val = val.substring(0, 4) + "." + val.substring(4, 6) + "."
                + val.substring(6, 8);
        }

        return val;
    }

    //납부확인증 조회
    function paymentCheck() {
        var count = $('input:checkbox[name=checkList]:checked').length;
        var checkArr = [];

        var tmasMonth = replaceDot($('#tDate').val());
        var fmasMonth = replaceDot($('#fDate').val());

        if (count == 0) {
            swal({
                type: 'question',
                text: '납부내역을 선택해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
        } else {
            swal({
                type: 'question',
                text: count + '건의 납부확인증을 확인하시겠습니까?',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {
                    $('input:checkbox[name=checkList]:checked').each(function (i) {
                        checkArr.push($(this).val());
                    });
                    $('#sSearchOrderBy').val($('#searchOrderBy option:selected').val());
                    $('#checkListValue').val(checkArr);
                    $('#pdfTmasMonth').val(tmasMonth);
                    $('#pdfFmasMonth').val(fmasMonth);
                    $('#sFileName').val($('#chaCd').val() + getCurrentDate());
                    var agent = navigator.userAgent.toLowerCase();
                    if ((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1)) {
                        $('#sBrowserType').val('ie');
                    } else {
                        $('#sBrowserType').val('etc');
                        $('#pdfMakeForm').attr("target", "formInfo");
                        window.open('', 'formInfo', 'height=800, width:1030, menubar=no');
                    }
                    $('#pdfMakeForm').submit();
                } else {

                }

            });
        }
    }

    //카드영수증 출력
    function printReceipt(tid) {

        var status = "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=420,height=540";
        var url = "https://npg.nicepay.co.kr/issue/IssueLoader.do?TID=" + tid + "&type=0";
        window.open(url, "popupIssue", status);
    }

    function prevDate(num) {
        var toDay = $.datepicker.formatDate("yy.mm.dd", new Date());
        var vdm = dateValidity($('#fDate').val(), toDay);

        $('#tDate').val(toDay);
        $('#fDate').val(monthAgo(toDay, num));
        $('.btn-preset-month').removeClass('active');
        $('#pMonth' + num + '').addClass('active');
    }
</script>
<input type="hidden" id="curPage" name="curPage"/>
<input type="hidden" id="usePgYn" name="usePgYn" value="${map.usePgYn }"/>

<form id="pdfMakeForm" name="pdfMakeForm" method="post" action="/payer/payment/chkPayList">
    <input type="hidden" id="cusName" name="cusName" value="${map.cusName}"/>
    <input type="hidden" id="vaNo" name="vaNo" value="${map.vaNo}">
    <input type="hidden" id="chaCd" name="chaCd" value="${map.chaCd}">
    <input type="hidden" id="chaName" name="chaName" value="${map.chaName}"/>
    <input type="hidden" id="pdfTmasMonth" name="pdfTmasMonth">
    <input type="hidden" id="pdfFmasMonth" name="pdfFmasMonth">
    <input type="hidden" id="checkListValue" name="checkListValue"/>
    <input type="hidden" name="sFileName" id="sFileName"/>
    <input type="hidden" id="sBrowserType" name="sBrowserType"/>
    <input type="hidden" id="sSearchOrderBy" name="sSearchOrderBy"/>
</form>

<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link active" href="#">납부내역조회</a>
        </nav>
    </div>
    <div class="container">
        <div id="page-title">
            <div class="row">
                <div class="col-6">
                    <h2>납부내역조회</h2>
                </div>
                <div class="col-6 text-right">
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>
                        납부한 내역을 확인하고 납부확인증을 인쇄할 수 있는 화면입니다.<br/>
                        납부 관련 사항은 해당 이용기관에 문의하시기 바랍니다.
                    </p>
                </div>
            </div>
        </div>
    </div>

    <input type="hidden" id="tmasMonth" value="${map.tmasMonth}">
    <div class="container">
        <div id="payable-summary">
            <div class="row">
                <div class="col-12">
                    <img src="/assets/imgs/payer/icon-bill-in-letter.png">
                    <p>
                        <strong>${map.cusName}</strong>님께서 <strong id="thisMonth">${map.maxmonth }월</strong> 납부하신 금액은
                        <strong id=""><fmt:formatNumber pattern="#,###" value="${map.totAmt}"/>원</strong> 입니다.
                    </p>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h5>납부자정보</h5>
            </div>
        </div>

        <div class="row">
            <div class="table-responsive pd-n-mg-o col mb-5">
                <table class="table table-sm table-primary">
                    <colgroup>
                        <col width="300">
                        <col width="400">
                        <col width="400">
                    </colgroup>

                    <thead>
                    <tr>
                        <th>납부자명</th>
                        <th>이용기관명</th>
                        <th>납부계좌</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${map.cusName}</td>
                        <td>${map.chaName}</td>
                        <td>${map.vaNo}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="container">
        <form class="search-box">
            <div class="row">
                <label class="col-form-label col col-md-1 col-sm-3 col-3">납부일자</label>
                <div class="col col-md-4 col-sm-8 col-8 form-inline">
                    <div class="date-input">
                        <div class="input-group">
                            <input type="text" id="fDate" class="form-control date-picker-input">
                        </div>
                    </div>
                    <span class="range-mark"> ~ </span>
                    <div class="date-input">
                        <div class="input-group">
                            <input type="text" id="tDate" class="form-control date-picker-input">
                        </div>
                    </div>
                </div>

                <div class="col col-md-6 col-sm-9 offset-md-0 offset-sm-2 offset-3">
                    <button id="pMonth1" type="button" class="btn btn-sm btn-preset-month active" onclick="prevDate(1)">1개월</button>
                    <button id="pMonth2" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(2)">2개월</button>
                    <button id="pMonth3" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(3)">3개월</button>
                </div>
            </div>
            <div class="row mt-3">
                <div class="col-12 text-center">
                    <input type="button" class="btn btn-primary btn-wide" onclick="fnSearch();" value="조회"/>
                </div>
            </div>
        </form>
    </div>

    <div class="container mb-5">
        <div id="paid-reference-list" class="list-id">
            <div class="row">
                <div class="col-12">
                    <h5>납부내역</h5>
                </div>
            </div>
            <div class="table-option row mb-2">
                <div class="col-md-6 col-sm-12 form-inline">
                    <span class="amount mr-2">조회결과 [총 <em class="font-blue" id="listCount">${map.count}</em>건]</span>
                </div>
                <div class="col-md-6 col-sm-12 text-right mt-1">
                    <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="arrayChange();">
                        <option value="rcpDt" <c:if test="${map.search_orderBy == 'rcpDt'}">selected</c:if>>납부일자순 정렬
                        </option>
                        <option value="rcpGbn" <c:if test="${map.search_orderBy == 'rcpGbn'}">selected</c:if>>납부구분별 정렬
                        </option>
                    </select>
                    <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                        <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                        <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                        <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                        <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
                        <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <div class="table-responsive pd-n-mg-o col mb-3">
                    <table class="table table-sm table-hover table-primary" id="payTable">
                        <colgroup>
                            <col class="hidden-on-mobile" width="52">
                            <col width="68">
                            <col width="140">
                            <col width="140">
                            <col width="200">
                            <col width="200">
                            <col width="150">
                            <col width="150">
                        </colgroup>

                        <thead>
                        <tr>
                            <th class="hidden-on-mobile">
                                <input class="form-check-input table-check-parents" type="checkbox" id="row-th" value="option2">
                                <label for="row-th"><span></span></label>
                            </th>
                            <th>NO</th>
                            <th>납부일자</th>
                            <th>청구월</th>
                            <th>청구금액(원)</th>
                            <th>납부금액(원)</th>
                            <th>납부구분</th>
                            <th>납부방법</th>
                        </tr>
                        </thead>
                        <tbody id="resultBody">
                        <c:choose>
                            <c:when test="${map.payCount > 0}">
                                <c:forEach var="row" items="${map.payList}" varStatus="status">
                                    <tr>
                                        <td class="hidden-on-mobile">
                                            <input class="form-check-input table-check-child" type="checkbox" name="checkList" id="row-${status.count}" value="${row.rcpmasCd}" onchange="changeBGColor(this)">
                                            <label for="row-${status.count}"><span></span></label>
                                        </td>
                                        <td>${status.count}</td>
                                        <td>
                                            <fmt:parseDate pattern="yyyyMMdd" var="fmtPayDay2" value="${row.payDay2}"/><fmt:formatDate pattern="yyyy.MM.dd" value="${fmtPayDay2}"/></td>
                                        <td>
                                            <fmt:parseDate pattern="yyyyMM" var="fmtMasMonth" value="${row.masMonth}"/><fmt:formatDate pattern="yyyy.MM" value="${fmtMasMonth}"/></td>
                                        <td class="text-right">
                                            <fmt:formatNumber pattern="#,###" value="${row.payItemAmt}"/></td>
                                        <td class="text-right">
                                            <fmt:formatNumber pattern="#,###" value="${row.rcpAmt}"/></td>
                                        <td>
                                            <c:if test="${row.rcpMasSt != 'PA09'}">${row.notiMasSt}</c:if>
                                            <c:if test="${row.rcpMasSt == 'PA09'}">카드취소</c:if>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${row.sveCd eq 'VAS'}">가상계좌</c:when>
                                                <c:when test="${row.sveCd eq 'DCS'}">현금</c:when>
                                                <c:when test="${row.sveCd eq 'DCD'}">오프라인카드</c:when>
                                                <c:when test="${row.sveCd eq 'DVA'}">무통장입금</c:when>
                                                <c:when test="${row.sveCd eq 'OCD'}">온라인카드<c:if test="${fn:length(row.packetNo)>17}">
                                                    <button class="btn btn-xs btn-outline-secondary" onclick="printReceipt('${row.packetNo}')">카드영수증</button>
                                                </c:if></c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" style="text-align: center;">
                                        [조회된 내역이 없습니다.]
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row hidden-on-mobile">
                <div class="col text-right">
                    <button type="button" class="btn btn-sm btn-d-gray" onclick="paymentCheck();">납부확인증 인쇄</button>
                </div>
            </div>

            <jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false"/>

        </div>
    </div>


    <div class="container">
        <div id="quick-instruction" class="foldable-box">
            <div class="row foldable-box-header">
                <div class="col-8">
                    <img src="/assets/imgs/common/icon-notice.png">
                    알려드립니다.
                </div>
                <div class="col-4 text-right">
                    <img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
                </div>
            </div>
            <div class="row foldable-box-body">
                <div class="col">
                    <h6>■ 납부확인증 발급 방법</h6>
                    <ul>
                        <li>납부년월 기간 선택 후 조회 버튼 클릭</li>
                        <li>납부 확인증 발급 대상을 그리드 좌측 체크박스에 선택</li>
                        <li>납부 확인증 보기 버튼 클릭</li>
                        <li>납부 확인증 팝업에 호출된 정보 확인 후 인쇄</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false"/>
