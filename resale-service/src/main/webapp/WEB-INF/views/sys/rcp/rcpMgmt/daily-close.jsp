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
        <h2>일 마감</h2>
        <ol class="breadcrumb">
            <li><a href="/sys/index">대시보드</a></li>
            <li><a>정산관리</a></li>
            <li class="active"><strong>일 마감</strong></li>
        </ol>
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
                                                        <label class="form-label block">마감일</label>
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
                                                    <option value="settleDate_desc">마감일순 정렬</option>
                                                    <option value="clientName_asc">기관명순 정렬</option>
                                                    <option value="clientId_asc">기관코드순 정렬</option>
                                                </select>
                                                <select class="form-control" name="pageSize" id="page-size">
                                                    <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                                                    <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                                                    <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                                                    <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
                                                    <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>> 200개씩 조회</option>
                                                </select>
                                                <button class="btn btn-md btn-primary" type="button" id="download-button">파일저장</button>
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
                                                            <th rowspan="2">마감일</th>
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
                                                            <td colspan=19>[조회된 내역이 없습니다.]</td>
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
    var twoDepth = "adm-sub-31";

    var cuPage = 1;
    var currPage = 1;
    var toDay = getFormatCurrentDate();

    $(function () {
        var toDay = getFormatCurrentDate();

        $('#month').datepicker({
            keyboardNavigation: false,
            format: 'yyyymmdd',
            forceParse: false,
            autoclose: true,
            minViewMode: 0
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
                fromDate: month,
                toDate: month,
                clientId: clientId,
                clientName: clientName,
                sort: orderBy
            };

            var url = '/sys/rest/client/settle/daily/download';
            var queryString = $.param(param);

            window.open(url + '?' + queryString);
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
                fromDate: month,
                toDate: month,
                clientId: clientId,
                clientName: clientName,
                sort: orderBy
            };

            var url = '/sys/rest/client/settle/daily';
            $.getJSON(url, param, function (result) {
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


        //일마감 데이터 새로고침
        function printTable(result) {
            var str = '';
            $('#total-item-count').text(result.totalItemCount);
            if (result.itemCount <= 0) {
                str += '<tr><td colspan="17" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
            } else {
                $.each(result.itemList, function (i, v) {
                    str += '<tr>';
                    str += '<td>' + moment(v.settleDate).format('YYYYMMDD') + '</td>';
                    str += '<td class="title" title="' + v.fgcd + '">' + v.fgcd + '</td>';
                    str += '<td class="title" title="' + v.clientId + '">' + v.clientId + '</td>';
                    str += '<td class="title" title="' + v.clientName + '">' + v.clientName + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.noticeAmount) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.noticeCount) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.noticeFee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.vaPaymentAmount) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.vaPaymentCount) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.vaPaymentFee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.vaBankFee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.vaPaymentFee - v.vaBankFee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.smsCount) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.smsFee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.lmsCount) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.lmsFee) + '</td>';
                    /*str += '<td style="text-align: right;">' + numberToCommas(v.atCount) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.atFee) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.prnCount) + '</td>';
                    str += '<td style="text-align: right;">' + numberToCommas(v.prnFee) + '</td>';*/
                    str += '</tr>';
                });
            }
            $('#result-table tbody').html(str);
        }
    });
</script>
