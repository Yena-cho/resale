<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-8";
    var twoDepth = "adm-sub-17";
</script>

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">
<script type="text/javascript">
    var cuPage = 1;
    var firstDepth = "nav-link-05";
    var toDay = getFormatCurrentDate();
    $(function () {
        setMonthTerm(1);

//        $('#fDate').val(getPrevDate(toDay, 1));
//        $('#tDate').val(toDay);

        $('#cashState1').prop("checked", true);
        $('input:checkbox[name=cashItem]').prop('checked', true);
        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($('#chacd').val());
            $("#popChaname").val('');
            $('#totCntLookupCollector').text(0);
            $("#ResultBodyCollector").html('');
            $("#ModalPageAreaCollector").html('');
        });

        $('#cashState1').click(function () {
            if ($(this).prop('checked')) {
                $('input:checkbox[name=cashItem]').prop('checked', true);
            } else {
                $('input:checkbox[name=cashItem]').prop('checked', false);
            }
        });

        $('input:checkbox[name=cashItem]').click(function () {
            if ($('#cashState1').is(":checked")) {
                $('#cashState1').prop("checked", false);
                $(this).prop("checked", false);
            }
            if ($('input:checkbox[name=cashItem]').length == $('input:checkbox[name=cashItem]:checked').length) {
                $('#cashState1').prop("checked", true);
            }
        });
    });

    function pageChange() {
        fnSearch(cuPage);
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

    function onSearchGb() {
        if ($('#searchGb option:selected').val() == "cusoffno") {
            $('#searchvalue').val("");
        }
    }

    function fnSearch(page, gubn) {
        var cashMasStList = [];
        var checkAll;
        if (page == null || page == 'undefined') {
            cuPage = "";
            cuPage = 1;
        } else {
            cuPage = page;
        }
        $('input:checkbox[name=cashItem]:checked').each(function (i) {
            cashMasStList.push($(this).val());
        });

        if ($('input:checkbox[id="cashState1"]').is(':checked') == true) {
            checkAll = $('input:checkbox[id="cashState1"]').val();
        } else {
            checkAll = null;
        }
        var url = "/sys/addServiceMgmt/cashHistoryAjax";
        param = {
            startDate: replaceDot($('#startday').val()),
            endDate: replaceDot($('#endday').val()),
            chaCd: $('#chacd').val(),
            searchGb: $('#searchGb option:selected').val(),
            searchValue: $('#searchValue').val() != "" ? $('#searchValue').val() : null, //검색구분 텍스트값
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val(),
            orderBy: $('#orderBy option:selected').val(),
            cashMasStList: cashMasStList,
            checkAll: checkAll
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
                    sysajaxPaging(result, 'PageArea');
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

//    function prevDate(num) {
//        var toDay = $.datepicker.formatDate("yy.mm.dd", new Date());
//        $('#fDate').val(getPrevDate(toDay, num));
//        $('.btn-outline').removeClass('active');
//        $('#pMonth' + num + '').addClass('active');
//    }

    function fnGrid(result, obj) {
        var str = '';
        $('#totCnt').text(result.count);

        if (result.count <= 0) {
            str += '<tr><td colspan="11" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + nullValueChange(v.regDt) + '</td>';
                str += '<td>' + nullValueChange(v.chaCd) + '</td>';
                str += '<td>' + nullValueChange(v.chaName) + '</td>';
                str += '<td>' + nullValueChange(v.cusName) + '</td>';
                str += '<td>' + nullValueChange(v.sveCdNm) + '</td>';
                str += '<td>' + nullValueChange(v.cusTypeNm) + '</td>';
                str += '<td>' + nullValueChange(v.confirmNm) + '</td>';
                str += '<td>' + nullValueChange(v.cusOffNo) + '</td>';
                str += '<td class="text-success">' + nullValueChange(v.cashMasNm) + '</td>';
                str += '<td>';
                if (v.appCd == '0000' && v.job == 'I') {
                    str += '발행 ';
                } else if (v.appCd == '0000' && v.job == 'U') {
                    str += '재발행 ';
                }
                str += nullValueChange(v.appMsg);
                str += '</td>';
                str += '</tr>';
            });
        }

        $('#' + obj).html(str);
    }

    //파일저장
    function fn_fileSave() {
        var cashMasStList = [];
        $('input:checkbox[name=cashItem]:checked').each(function (i) {
            cashMasStList.push($(this).val());
        });

        if ($('#totCnt').text() <= 0) {
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
                $('#fStartDate').val(replaceDot($('#startday').val()));
                $('#fEndDate').val(replaceDot($('#endday').val()));
                $('#fSearchGb').val($('#searchGb option:selected').val());
                $('#fSearchValue').val($('#searchValue').val());
                $('#fOrderBy').val($('#orderBy option:selected').val());
                $('#fChaCd').val($('#chacd').val());
                $('#cashMasStList').val(cashMasStList);

                // 다운로드
                $('#fileForm').submit();
            }
        });
    }
</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/addServiceMgmt/cashExcelDown">
    <input type="hidden" id="fStartDate" name="fStartDate"/>
    <input type="hidden" id="fEndDate" name="fEndDate"/>
    <input type="hidden" id="fSearchGb" name="fSearchGb"/>
    <input type="hidden" id="fSearchValue" name="fSearchValue"/>
    <input type="hidden" id="fOrderBy" name="fOrderBy"/>
    <input type="hidden" id="fChaCd" name="fChaCd"/>
    <input type="hidden" id="cashMasStList" name="cashMasStList"/>
</form>

<input type="hidden" id="curPage" name="curPage"/>
<input type="hidden" id="pageNo" name="pageNo" value="1">

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>현금영수증이용내역</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>부가서비스관리</a>
            </li>
            <li class="active">
                <strong>현금영수증이용내역</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 현금영수증 발급대행 신청현황 관리하는 화면입니다.</p>
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
                                    <label class="form-label block">신청일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" id="startday" class="input-sm form-control" name="startday" value="" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" id="endday" class="input-sm form-control" name="endday" value="" readonly="readonly"/>
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
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chacd" maxlength="50">
                                            <span class="input-group-btn">
                                                <button class="btn btn-primary btn-lookup-collecter no-margins" type="button">기관검색</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="cashState1" name="cashAll" value="ALL">
                                                <label for="cashState1"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="cashState2" name="cashItem" value="ST02">
                                                <label for="cashState2"> 미발행 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="cashState3" name="cashItem" value="ST03">
                                                <label for="cashState3"> 발행 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="cashState4" name="cashItem" value="ST04">
                                                <label for="cashState4"> 발행중 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
											<span class="input-group-select">
												<select class="form-control" id="searchGb" onchange="onSearchGb();">
													<option value="cusname">신청자명</option>
													<option value="cusoffno">발급정보</option>
												</select>
											</span>

                                            <input type="text" class="form-control" name="searchValue" id="searchValue"/>
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
                            전체 신청건수 : <strong class="text-success" id="totCnt">${map.count}</strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" id="orderBy" onchange="pageChange();">
                                <option value="date">신청일순</option>
                                <option value="code">상태순</option>
                                <option value="public">발급용도순</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                                <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회
                                </option>
                                <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회
                                </option>
                                <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회
                                </option>
                                <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회
                                </option>
                                <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회
                                </option>
                            </select>
                            <button class="btn btn-md btn-primary" type="button" onclick="fn_fileSave()">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <colgroup>
                                    <col width="50">
                                    <col width="130">
                                    <col width="120">
                                    <col width="250">
                                    <col width="180">
                                    <col width="120">
                                    <col width="120">
                                    <col width="150">
                                    <col width="150">
                                    <col width="80">
                                    <col width="240">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>신청일자</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>신청자명</th>
                                        <th>납부방법</th>
                                        <th>발급용도</th>
                                        <th>발급방법</th>
                                        <th>발급정보</th>
                                        <th>상태</th>
                                        <th>사유</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <c:choose>
                                        <c:when test="${map.count > 0}">
                                            <c:forEach var="row" items="${map.list}" varStatus="status">
                                                <tr>
                                                    <td>${row.rn}</td>
                                                    <td>${row.regDt}</td>
                                                    <td>${row.chaCd}</td>
                                                    <td>${row.chaName}</td>
                                                    <td>${row.cusName}</td>
                                                    <td>${row.sveCdNm}</td>
                                                    <td>${row.cusTypeNm}</td>
                                                    <td>${row.confirmNm}</td>
                                                    <td>${row.cusOffNo}</td>
                                                    <td class="text-success">${row.cashMasNm}</td>
                                                    <td>
                                                        <!-- 상태코드가 U이면서  -->
                                                        <c:if test="${row.appCd eq '0000' && row.job eq 'I'}">
                                                            발행
                                                        </c:if>
                                                        <c:if test="${row.appCd eq '0000' && row.job eq 'U'}">
                                                            재발행
                                                        </c:if>
                                                            ${row.appMsg}
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="11" style="text-align:center;">[조회된 내역이 없습니다.]</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <jsp:include page="/WEB-INF/views/include/sysPaging.jsp" flush="false"/>
                    </div>
                </div>
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

        //  2018.05.11 아래 삭제
        //  $('.footable').footable();
        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            forceParse: false,
            autoclose: true
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
