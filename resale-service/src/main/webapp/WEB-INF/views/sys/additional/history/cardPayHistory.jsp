<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-6";
    var twoDepth = "adm-sub-18";
</script>

<script>
    var cuPage = 1;

    function fnSearch(page) {
        var endday = $("#endday").val();
        var startday = $("#startday").val();

        endday = delDotDate(endday);
        startday = delDotDate(startday);

        if (page == null || page == 'undefined') {
            cuPage = "";
            cuPage = 1;
        } else {
            cuPage = page;
        }

        if (getCurrentDate() < endday || getCurrentDate() < startday) {
            swal({
                type: 'info',
                text: "현재일보다 이후를 조회할 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (endday < startday) {
            swal({
                type: 'info',
                text: "조회시작년월이 더 클 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        var url = "/sys/addServiceMgmt/cardPayHistoryAjax";

        var param = {
            tMonth: endday, //조회 시작년월
            fMonth: startday, //조회 종료년월
            chaName: $("#chaName").val(),
            chaCd: $("#chaCd").val(),
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val(),
            searchOrderBy: $('#searchOrderBy option:selected').val(),
            searchValue: $('#searchValue').val(), //검색구분 텍스트값
            statusCheck: $("input[name=statusCheck]:checked").val()
        };
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'resultBody'); // 현재 데이터로 셋팅
                    sysajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'info',
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
        var str = '';
        $('#totalCount').text(result.count);

        if (result.count <= 0) {
            str += '<tr><td colspan="8" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + isNotNull(v.rn) + '</td>';
                str += '<td>' + isNotNull(v.chaCd) + '</td>';
                str += '<td>' + isNotNull(v.chaName) + '</td>';
                str += '<td>' + isNotNull(v.cusName) + '</td>';
                str += '<td>' + isNotNull(v.payDay2) + '</td>';
                str += '<td class="text-right">' + isNotNull(numberToCommas(v.payItemAmt)) + '</td>';
                str += '<td class="text-right">' + isNotNull(numberToCommas(v.rcpAmt)) + '</td>';
                if (v.rcpMasSt == 'PA03') {
                    str += '<td class="text-success">승인</td>';
                } else if (v.rcpMasSt == 'PA09') {
                    str += '<td class="text-danger">취소</td>';
                }
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    function isNotNull(v) {
        if (typeof v == "undefined" || v == null || v == "") {

            return "";

        } else {
            return v;
        }
    }

    //날짜 포맷삭제
    function delDotDate(val) {

        val = val.replace(/\./g, "");

        return val;
    }

    //페이징 버튼
    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색
        } else {
            $('#pageNo').val(page);
            fnSearch(page);
        }
    }

    //화면보여주는 개수 변경
    function pageChange() {
        fnSearch(cuPage);
    }

    //정렬 변경
    function arrayChange() {
        fnSearch(cuPage);
    }

    //파일저장
    function fn_fileSave() {
        if ($('#totalCount').text() <= 0) {
            swal({
                type: 'info',
                text: '다운로드할 데이터가 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            type: 'question',
            html: "다운로드 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                $('#fStartDate').val(delDotDate($('#startday').val()));
                $('#fEndDate').val(delDotDate($('#endday').val()));
                $('#fChaCd').val($('#chaCd').val());
                $('#fChaName').val($('#chaName').val());
                $('#fSearchOrderBy').val($('#searchOrderBy option:selected').val());
                $('#fStatusCheck').val($("input[name=statusCheck]:checked").val());
                $('#fSearchValue').val($('#searchValue').val());
                // 다운로드
                $('#fileForm').submit();
            }
        });
    }

</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/addServiceMgmt/cardHistoryExcelDown">
    <input type="hidden" id="fStartDate" name="fStartDate" value=""/>
    <input type="hidden" id="fEndDate" name="fEndDate" value=""/>
    <input type="hidden" id="fChaCd" name="fChaCd" value=""/>
    <input type="hidden" id="fChaName" name="fChaName" value=""/>
    <input type="hidden" id="fSearchOrderBy" name="fSearchOrderBy" value=""/>
    <input type="hidden" id="fStatusCheck" name="fStatusCheck" value=""/>
    <input type="hidden" id="fSearchValue" name="fSearchValue" value=""/>
</form>

<input type="hidden" id="pageNo" name="pageNo" value="1">
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>온라인카드결제이용내역</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>부가서비스관리</a>
            </li>
            <li class="active">
                <strong>온라인카드결제이용내역</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 온라인 카드결제 이용현황을 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<div class="wrapper-content">
    <div class="animated fadeInRight article">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>검색</h5>
                    </div>

                    <div class="ibox-content">
                        <form>
                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">결제일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" name="startday" id="startday" value="${map.startday}" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" name="endday" id="endday" value="${map.tMonth}" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth"  id="btnSetMonth0"  onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth1"  onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth6"  onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6"></div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaCd" id="chaCd" maxlength="50" style="height: 34px;">
                                            <span class="input-group-btn">
                                                <button class="btn btn-primary btn-lookup-collecter no-margins" type="button">기관검색</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">결제상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="radio" id="cardPay1" name="statusCheck" value="ALL" checked="checked">
                                                <label for="cardPay1"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="radio" id="cardPay2" name="statusCheck" value="PA03">
                                                <label for="cardPay2"> 승인 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="radio" id="cardPay3" name="statusCheck" value="PA09">
                                                <label for="cardPay3"> 취소 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">고객명</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <input type="text" class="form-control" id="searchValue" name="searchValue">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6"></div>
                            </div>

                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fnSearch();">조회</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="animated fadeInRight" id="focus">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">
                            전체 신청건수 : <strong class="text-success" id="totalCount">${map.count}</strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="arrayChange();">
                                <option value="payDt">결제일순</option>
                                <option value="status">결제상태순</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" onclick="fn_fileSave()">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <colgroup>
                                    <col width="50">
                                    <col width="150">
                                    <col width="410">
                                    <col width="300">
                                    <col width="200">
                                    <col width="200">
                                    <col width="200">
                                    <col width="100">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>고객명</th>
                                        <th>결제일시</th>
                                        <th>청구금액</th>
                                        <th>결제금액</th>
                                        <th>결제상태</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <c:choose>
                                        <c:when test="${map.count > 0}">
                                            <c:forEach var="row" items="${map.list}">
                                                <tr>
                                                    <td>${row.rn}</td>
                                                    <td>${row.chaCd}</td>
                                                    <td>${row.chaName}</td>
                                                    <td>${row.cusName}</td>
                                                    <td>${row.payDay2}</td>
                                                    <td class="text-right"><fmt:formatNumber pattern="#,###"
                                                                                             value="${row.payItemAmt}"/></td>
                                                    <td class="text-right"><fmt:formatNumber pattern="#,###"
                                                                                             value="${row.rcpAmt}"/></td>
                                                    <c:choose>
                                                        <c:when test="${row.rcpMasSt eq 'PA03'}">
                                                            <td class="text-success">승인</td>
                                                        </c:when>
                                                        <c:when test="${row.rcpMasSt eq 'PA09'}">
                                                            <td class="text-danger">취소</td>
                                                        </c:when>
                                                    </c:choose>
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
                </div>
                <jsp:include page="/WEB-INF/views/include/sysPaging.jsp" flush="false"/>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>


<script>
    $(document).ready(function () {
        setMonthTerm(1);

        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($('#chaCd').val());
            $("#popChaname").val('');
            $('#totCntLookupCollector').text(0);
            $("#ResultBodyCollector").html('');
            $("#ModalPageAreaCollector").html('');
        });

        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            forceParse: false,
            autoclose: true
        });

        $('.btn-open-caller-num-input').click(function () {
            $("#popup-caller-num-list").modal({
                backdrop: 'static',
                keyboard: false
            });
        });

        $('.input-group.date').datepicker({
            todayBtn: "linked",
            keyboardNavigation: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            forceParse: false,
            calendarWeeks: true,
            autoclose: true
        });
    });
</script>
