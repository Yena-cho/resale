<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet">

<input type="hidden" id="page-no" name="page-no"/>

<form id="monthCloseFileForm" name="monthCloseFileForm" method="post" action="/sys/rcpMgmt/monthCloseExcelDown">
    <input type="hidden" id="fMonthClosefDate" name="fMonthClosefDate"/>
    <input type="hidden" id="fMonthClosetDate" name="fMonthClosetDate"/>
    <input type="hidden" id="fMonthCloseOrderBy" name="fMonthCloseOrderBy"/>
    <input type="hidden" id="fChaCd" name="fChaCd"/>
</form>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>월 마감</h2>
        <ol class="breadcrumb">
            <li><a href="/sys/index">대시보드</a></li>
            <li><a>정산관리</a></li>
            <li class="active"><strong>월 마감</strong></li>
        </ol>

        <p class="page-description" style="display: none;">
            - 월별 마감을 실행하는 화면입니다.<br>
            - 월별 자동마감이 안되었거나, 재마감이 필요할 경우 사용합니다.
        </p>
    </div>
</div>

<div class="wrapper wrapper-content">
    <div class="animated fadeInUp">
        <div class="row">
            <div class="col-lg-12 m-b-3">
                <div class="tabs-container">
                    <div id="closing-by-monthly" role="tabpanel" class="tab-pane fade in active">
                        <div class="row">
                            <div class="animated fadeInRight article">
                                <div class="col-lg-12">
                                    <div class="ibox float-e-margins">
                                        <div class="ibox-title">
                                            <h5>검색</h5>
                                        </div>

                                        <div class="ibox-content">
                                            <form onsubmit="return false;">
                                                <div class="row">
                                                    <div class="col-md-4">
                                                        <label class="form-label block">마감월</label>
                                                        <div class="form-group form-group-sm">
                                                            <div class="input-group date">
                                                                <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                                                <input type="text" class="form-control" value="" id="month" autocomplete="off"/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label class="form-label block">기관코드</label>
                                                        <div class="form-group form-group-sm">
                                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="clientId" id="client-id"/>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label class="form-label block">기관명</label>
                                                        <div class="form-group form-group-sm">
                                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="clientName" id="client-name"/>
                                                        </div>
                                                    </div>
                                                </div>

                                                <hr>

                                                <div class="text-center">
                                                    <button class="btn btn-primary" id="search-button">조회</button>
                                                    <!-- 임시 버튼 비활성화 20180816 
                                                    <button class="btn btn-primary" onclick="fnMonthCloseGo();">마감실행</button>
                                                     -->
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="animated fadeInRight">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <div class="col-lg-6"></div>

                                            <div class="col-lg-6 form-inline form-searchOrderBy">
                                                <select class="form-control" id="order-by" name="orderBy">
                                                    <option value="month_desc">마감월순 정렬</option>
                                                    <option value="clientName_asc">기관명순 정렬</option>
                                                    <option value="clientId_asc">기관코드순 정렬</option>
                                                </select>

                                                <select class="form-control" name="pageSize" id="page-size">
                                                    <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                                                    <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                                                    <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                                                    <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
                                                    <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
                                                </select>
                                                <button class="btn btn-md btn-primary" type="button" id="download-button">파일저장</button>
                                                <button class="btn btn-md btn-primary" type="button" id="download-invoice-client-button">거래처 등록 파일 다운로드</button>
                                                <button class="btn btn-md btn-primary" type="button" id="download-invoice-button">세금계산서 파일 다운로드</button>
                                            </div>
                                        </div>

                                        <div class="ibox-content">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis" id="result-table">
                                                    <colgroup>
                                                        <col width="82">
                                                        <col width="105">
                                                        <col width="105">
                                                        <col width="130">
                                                        <col width="95">
                                                        <col width="80">
                                                        <col width="95">
                                                        <col width="95">
                                                        <col width="80">
                                                        <col width="95">
                                                        <col width="95">
                                                        <col width="95">
                                                        <col width="80">
                                                        <col width="95">
                                                        <col width="80">
                                                        <col width="95">
                                                        <%--<col width="80">
                                                        <col width="95">
                                                        <col width="80">
                                                        <col width="95">--%>
                                                    </colgroup>

                                                    <thead>
                                                        <tr>
                                                            <th rowspan="2">마감월</th>
                                                            <th rowspan="2">은행코드</th>
                                                            <th rowspan="2">기관코드</th>
                                                            <th rowspan="2">기관명</th>
                                                            <th colspan="3">청구</th>
                                                            <th colspan="5">수납</th>
                                                            <th colspan="4">문자메시지</th>
                                                            <%--<th colspan="2">알림톡</th>
                                                            <th colspan="2">고지서출력의뢰</th>--%>
                                                        </tr>
                                                        <tr>
                                                            <th>금액</th>
                                                            <th>건수</th>
                                                            <th>수수료</th>
                                                            <th>금액</th>
                                                            <th>건수</th>
                                                            <th>수수료</th>
                                                            <th>은행수수료</th>
                                                            <th>핑거수수료</th>
                                                            <th>SMS건수</th>
                                                            <th>SMS금액</th>
                                                            <th>LMS건수</th>
                                                            <th>LMS금액</th>
                                                            <%--<th>건수</th>
                                                            <th>금액</th>
                                                            <th>출력건수</th>
                                                            <th>금액</th>--%>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="monthCloseResultBody">
                                                        <tr>
                                                            <td colspan=17>[조회된 내역이 없습니다.]</td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>

                                            <div class="row m-b-lg">
                                                <div class="col-lg-12 text-center">
                                                    <div class="btn-group paginate" id="paginate"></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<script src="/assets/js/common.js"></script>

<script>
    var oneDepth = "adm-nav-5";
    var twoDepth = "adm-sub-10";

    var cuPage = 1;
    var currPage = 1;
    var toDay = getFormatCurrentDate();

    //금액 콤마
    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }

    function NVL(value) {
        if (value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length )) {
            return "";
        }
        else {
            return value;
        }
    }

    //조회기간 제한 체크
    var monthValidation = function (sdate, edate) {
        if (edate < sdate) {
            swal({
                type: 'info',
                text: "조회 시작일이 종료일보다 큽니다. 날짜를 다시 선택해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }
        if (edate - sdate > 99) {
            swal({
                type: 'info',
                text: "최대 조회기간은 1년 입니다. 다시 선택해 주세요",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }
        return true;
    };

    function monthClosePageChange() {
        monthCloseSearch(cuPage);
    }

    //페이징 버튼
    function list2(page) {
        monthCloseSearch(page);
    }

    $(function () {
        var toDay = getFormatCurrentDate();

        $('#month').datepicker({
            keyboardNavigation: false,
            format: 'yyyymm',
            forceParse: false,
            autoclose: true,
            minViewMode: 1
        });

        $('#closeStartDt').val(getPrevDate(toDay, 1));
        $('#closeEndDt').val(toDay);

        //월마감
        getYearsBox('closeStartYear');
        getMonthBox2('closeStrartMonth', 1);
        getYearsBox('closeEndYear');
        getMonthBox('closeEndMonth');

        $("#showDay").click(function () {
            showType = 'day';
            $(this).tab("show");
            dayCloseSearch('1');
        });
        $("#showMonth").click(function () {
            showType = 'month';
            $(this).tab("show");
            monthCloseSearch('1');
        });

        $('#search-button').click(search);

        $('#page-size, #order-by').change(search);

        $('#download-button').click(downloadData);
        $('#download-invoice-button').click(downloadInvoice);
        $('#download-invoice-client-button').click(downloadInvoiceClient);

        search();

        function search() {
            $('#page-no').val(1);
            searchInternal();
        }

        function goPage(pageNo) {
            $('#page-no').val(pageNo);
            searchInternal();
        }

        function downloadData() {
            var month = $('#month').val();
            var clientId = $('#client-id').val();
            var clientName = $('#client-name').val();
            var orderBy = $('#order-by').val();

            var param = {
                month: month,
                clientId: clientId,
                clientName: clientName,
                orderBy: orderBy
            };

            var url = '/sys/rest/rcpMgmt/monthly-close/download';
            var queryString = $.param(param);

            window.open(url + '?' + queryString);
        }

        function downloadInvoice() {
            var month = $('#month').val();
            var clientId = $('#client-id').val();
            var clientName = $('#client-name').val();
            var orderBy = $('#order-by').val();

            var param = {
                month: month,
                clientId: clientId,
                clientName: clientName,
                orderBy: orderBy
            };

            var url = '/sys/rest/rcpMgmt/invoice/download';
            var queryString = $.param(param);

            window.open(url + '?' + queryString);
        }

        function downloadInvoiceClient() {
            var url = '/sys/rest/client/download/invoice';

            window.open(url);
        }

        function searchInternal() {
            var pageNo = $('#page-no').val();
            var pageSize = $('#page-size').val();
            var month = $('#month').val();
            var clientId = $('#client-id').val();
            var clientName = $('#client-name').val();
            var orderBy = $('#order-by').val();

            var param = {
                pageNo: pageNo,
                pageSize: pageSize,
                month: month,
                clientId: clientId,
                clientName: clientName,
                orderBy: orderBy
            };

            var url = '/sys/rest/rcpMgmt/monthly-close';
            $.getJSON(url, param, function (result) {
                if (result.retCode != '0000') {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    return;
                }

                printTable(result, 'monthCloseResultBody'); // 현재 데이터로 셋팅
                printPaginate(result);
            });
        }

        function printPaginate(result) {
            var totalItemCount = result.totalItemCount;
            var pageNo = $('#page-no').val();
            var pageSize = $('#page-size').val();
            var pageGroupSize = 10;
            var firstPage = 1;
            var lastPage = Math.ceil(totalItemCount / pageSize);
            var lastPageGroup = Math.ceil(lastPage / pageGroupSize);
            var currentPageGroup = Math.ceil(pageNo / pageGroupSize);
            var startPage = Math.max((currentPageGroup - 1) * pageGroupSize + 1, 1);
            var endPage = Math.min(currentPageGroup * pageGroupSize, lastPage);
            var previousPage = Math.max(startPage - 1, firstPage);
            var nextPage = Math.min(endPage - 1, lastPage);

            var $paginate = $('#paginate');
            $paginate.empty();

            var $previousPage = $('<button type="button" class="btn btn-white paginate-previous-page"><i class="fa fa-chevron-left"></i></button>');
            $previousPage.data('page', previousPage);
            $previousPage.appendTo($paginate);

            for (var i = startPage; i <= endPage; i++) {
                var $page = $('<button type="button" class="btn btn-white paginate-page">' + i + '</button>');
                $page.data('page', i);
                if (i === pageNo) {
                    $page.addClass('active');
                } else {
                    $page.click(function () {
                        goPage($(this).data('page'));
                    });
                }

                $page.appendTo($paginate);
            }

            var $nextPage = $('<button type="button" class="btn btn-white paginate-next-page"><i class="fa fa-chevron-right"></i> </button>');
            $nextPage.data('page', nextPage);
            $nextPage.appendTo($paginate);
        }

        //월마감 데이터 새로고침
        function printTable(result) {
            var str = '';
            $('#total-item-count').text(result.totalItemCount);
            if (result.itemCount <= 0) {
                str += '<tr><td colspan="17" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
            } else {
                $.each(result.itemList, function (i, v) {
                    str += '<tr>';
                    str += '<td>' + v.month + '</td>';
                    str += '<td class="title" title="' + v.fgcd + '">' + v.fgcd + '</td>';
                    str += '<td class="title" title="' + v.chacd + '">' + v.chacd + '</td>';
                    str += '<td class="title" title="' + v.chaname + '">' + v.chaname + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.notiamt) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.noticnt) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.notifee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.rcpamt) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.rcpcnt) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.rcpfee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.rcpbnkfee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.rcpfingerfee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.smscnt) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.smsfee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.lmscnt) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.lmsfee) + '</td>';
                    /*str += '<td style="text-align: right;">' + numberToCommas(v.ATCNT) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.ATFEE) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.PRNCNT) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.PRNFEE) + '</td>';*/
                    str += '</tr>';
                });
            }
            $('#result-table tbody').html(str);
        }
    });

    // 월마감 조회
    function monthCloseSearch(page) {
        var startMonth = $('#closeStartYear option:selected').val() + "" + $('#closeStrartMonth option:selected').val();
        var endMonth = $('#closeEndYear option:selected').val() + "" + $('#closeEndMonth option:selected').val();
        var chaCd = $('#chaCd').val().toString().replace(/\./gi, "");

        if (!monthValidation(startMonth, endMonth)) {
            return;
        }

        if (CalcDay(startMonth + "00", endMonth + "00") > 365) {
            swal({
                type: 'info',
                text: "최대 조회기간은 1년 입니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (page == null || page == 'undefined') {
            currPage = 1;
        } else {
            currPage = page;
        }

        var searchOrderBy2 = $("#monthClosesearchOrderBy").val();
        if ($("#monthClosesearchOrderBy").val() == null || $("#monthClosesearchOrderBy").val() == "") {
            searchOrderBy2 = "closeMonth";
        } else {
            searchOrderBy2 = $("#monthClosesearchOrderBy").val();
        }

        var param = {
            fmasMonth: startMonth, //조회 시작년월
            tmasMonth: endMonth,     //조회 종료년월
            curPage: currPage,
            chaCd: chaCd,
            pageScale: $('#monthClosePageScale option:selected').val(),
            orderBy: searchOrderBy2
        };

        $.ajax({
            type: "post",
            async: true,
            url: "/sys/rcpMgmt/monthCloseListAjax",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    printTable(result); // 현재 데이터로 셋팅
                    printPaginate(result);
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

        return false;
    }
</script>
