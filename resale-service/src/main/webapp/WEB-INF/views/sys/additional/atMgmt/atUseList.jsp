<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-6";
    var twoDepth = "adm-sub-35-2";
</script>

<script type="text/javascript">
    var cuPage = 1;

    function fnSearch(page) {
        if (page == null || typeof page == 'undefined') {
            cuPage = 1;
        } else {
            cuPage = page;
        }

        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");

        if (getCurrentDate() < endday || getCurrentDate() < startday) {
            swal({
                type: 'info',
                text: "발송일자를 정확히 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (endday < startday) {
            swal({
                type: 'info',
                text: "발송일자를 정확히 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        var param = {
            fDate: startday,                                           //조회 시작일자
            tDate: endday,                                             //조회 종료일자
            chaCd: $("#chaCd").val(),
            chaName: $("#chaName").val(),
            searchGb: $('#searchGb option:selected').val(),             //검색구분 옵션
            searchValue: $('#searchValue').val(),                       //검색구분 입력값
            msgType: $("input[name=msgType]:checked").val(),            //유형
            statusCheck: $("input[name=statusCheck]:checked").val(),    //발송결과
            searchOrderBy: $('#searchOrderBy option:selected').val(),   //정렬
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: "/sys/addServiceMgmtA/atUseListAjax",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'resultBody'); // 현재 데이터로 셋팅
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

    // 데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#totalCount').text(result.totalCount);
        $('#successCount').text(result.successCount);
        $('#failCount').text(result.failCount);

        if (result.totalCount < 1) {
            str += '<tr><td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + defaultString(v.rn) + '</td>';
                str += '<td>' + moment(v.sendDate, 'YYYY-MM-DD HH:mm:ss').format('YYYY.MM.DD HH:mm') + '</td>';
                str += '<td>' + defaultString(v.chaCd) + '</td>';
                str += '<td>' + defaultString(v.loginId) + '</td>';
                str += '<td>' + defaultString(v.chaName) + '</td>';
                str += '<td>' + defaultString(v.msgType) + '</td>';
                str += '<td>' + defaultString(v.cusName) + '</td>';
                str += '<td>' + defaultString(v.cusTelNo) + '</td>';
                str += '<td>' + defaultString(v.sendStatusCd) + '</td>';

                if(v.sendResultCd == '7000') {  // 전달
                    str += '<td></td>';
                } else {
                    str += '<td style="width: ">' + defaultString(v.description) + '</td>';
                }
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    // 페이징 버튼
    function list(page) {
        fnSearch(page);
    }

    // 파일 저장
    function fn_fileSave() {
        var rowCnt = $('#totalCount').text();
        if(rowCnt == 0) {
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
            cancelButtonText: '취소'
        }).then(function(result) {
            if (result.value) {
                $('#fDate').val($('#startday').val().replace(/\./g,''));
                $('#tDate').val($('#endday').val().replace(/\./g,''));
                $('#fChaCd').val($('#chaCd').val());
                $('#fChaName').val($('#chaName').val());
                $('#fSearchGb').val($('#searchGb option:selected').val());
                $('#fMsgType').val($("input[name=msgType]:checked").val());
                $('#fSearchValue').val($('#searchValue').val());
                $('#fStatusCheck').val($("input[name=statusCheck]:checked").val());
                $('#fSearchOrderBy').val($('#searchOrderBy option:selected').val());

                document.fileForm.action = "/sys/addServiceMgmtA/atUseExcelDown";
                document.fileForm.submit();
            }
        });
    }

</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/addServiceMgmtA/atUseExcelDown">
    <input type="hidden" id="fDate" name="fDate" value=""/>
    <input type="hidden" id="tDate" name="tDate" value=""/>
    <input type="hidden" id="fChaCd" name="fChaCd" value=""/>
    <input type="hidden" id="fChaName" name="fChaName" value=""/>
    <input type="hidden" id="fMsgType" name="fMsgType" value=""/>
    <input type="hidden" id="fSearchGb" name="fSearchGb" value=""/>
    <input type="hidden" id="fSearchValue" name="fSearchValue" value=""/>
    <input type="hidden" id="fStatusCheck" name="fStatusCheck" value=""/>
    <input type="hidden" id="fSearchOrderBy" name="fSearchOrderBy" value=""/>
</form>

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>알림톡서비스 이용내역</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>부가서비스관리</a>
            </li>
            <li class="active">
                <strong>알림톡서비스 이용내역</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 알림톡 이용내역을 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2"></div>
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
                                <div class="col-md-12">
                                    <label class="form-label block">발송일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" onkeyup="onlyNumber(this)" class="input-sm form-control" name="startday" id="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" onkeyup="onlyNumber(this)" class="input-sm form-control" name="endday" id="endday" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth" id="btnSetMonth0" onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth1" onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth6" onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaCd" id="chaCd" maxlength="50">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaName" id="chaName" maxlength="50">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
											<span class="input-group-select">
												<select class="form-control" id="searchGb">
													<option value="cusName">고객명</option>
													<option value="chaTelNo">발신번호</option>
													<option value="cusTelNo">수신번호</option>
												</select>
											</span>
                                            <input type="text" class="form-control" id="searchValue" name="searchValue" maxlength="50">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">유형</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-inline">
                                                <input type="radio" id="msgTypeAll" name="msgType" value="all" checked>
                                                <label for="msgTypeAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="msgType0" name="msgType" value="0">
                                                <label for="msgType0"> 청구 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="msgType1" name="msgType" value="1">
                                                <label for="msgType1"> 미납 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="msgType2" name="msgType" value="2">
                                                <label for="msgType2"> 입금 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">발송결과</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-inline">
                                                <input type="radio" id="statusAll" name="statusCheck" value="all" checked>
                                                <label for="statusAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="status1" name="statusCheck" value="1">
                                                <label for="status1"> 발송성공 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="status2" name="statusCheck" value="2">
                                                <label for="status2"> 발송실패 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
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
                            <span class="m-r-sm">전체발송건수 : <strong class="text-primary" id="totalCount">0</strong> 건</span>
                            <span> | </span>
                            <span class="m-l-sm">발송성공건수 : <strong class="text-success" id="successCount">0</strong> 건</span>
                            <span> | </span>
                            <span class="m-l-sm">발송실패건수 : <strong class="text-danger" id="failCount">0</strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="fnSearch();">
                                <option value="sendDate">발송일시순 정렬</option>
                                <option value="chaName">기관명순 정렬</option>
                                <option value="msgType">발송유형순 정렬</option>
                                <option value="sendStatusCd">발송결과순 정렬</option>
                            </select>
                           <select class="form-control" name="pageScale" id="pageScale" onchange="fnSearch();">
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
                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>발송일시</th>
                                        <th>기관코드</th>
                                        <th>로그인아이디</th>
                                        <th>기관명</th>
                                        <th>유형</th>
                                        <th>고객명</th>
                                        <th>수신번호</th>
                                        <th>발송결과</th>
                                        <th>실패사유</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <tr>
                                        <td colspan="13" style="text-align: center;">
                                            [조회된 내역이 없습니다.]
                                        </td>
                                    </tr>
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

<!-- FooTer -->
<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
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
        setMonthTerm(1);
        fnSearch();
    });
</script>
