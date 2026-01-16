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
    var twoDepth = "adm-sub-30-1";
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

        if (endday == null || startday == null) {
            swal({
                type: 'info',
                text: "검색일자를 선택해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
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

        var url = "/sys/addServiceMgmt/pastRcpHistListAjax";

        var param = {
            tMonth: endday, //조회 시작년월
            fMonth: startday, //조회 종료년월
            chaCd: $("#chaCd").val(),
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val(),
            searchOrderBy: $('#searchOrderBy option:selected').val(),
            statusCheck: $("input[name=statusCheck]:checked").val()
        };
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    if($("#chaCd").val() == "20000000"){
                        $("#chaCd").val("");
                    }
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
        if($("input[name=statusCheck]:checked").val() == 'rcpDet') {
            $("#notiItemNm").show();
        }else{
            $("#notiItemNm").hide();
        }

        if (result.count <= 0) {
            str += '<tr><td colspan="15" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str+='<input type="hidden" id="notiMasCd" name="notiMasCd" value="' + v.notiMasCd + '">';
                str+='<td>';
                str += '<div class="checkbox checkbox-primary checkbox-inline">';
                str+='<input name="checkList" type="checkbox" id="row-'+v.rn+'" value="'+v.notiMasCd+'" notimasst="'+v.notimasst+'"">';
                str+='<label for="row-'+v.rn+'"></label>';
                str+='</td>';
                str += '<td>' + defaultString(v.chaCd) + '</td>';
                str += '<td>' + defaultString(v.chaName) + '</td>';
                str += '<td>' + defaultString(v.masMonth) + '</td>';
                str += '<td>' + defaultString(v.payDay2) + '</td>';
                str += '<td>' + defaultString(v.cusName) + '</td>';
                str += '<td>' + defaultString(v.vaNo) + '</td>';
                str += '<td>' + defaultString(v.cusGubn1) + '</td>';
                str += '<td>' + defaultString(v.cusGubn2) + '</td>';
                str += '<td>' + defaultString(v.cusGubn3) + '</td>';
                str += '<td>' + defaultString(v.cusGubn4) + '</td>';
                if($("input[name=statusCheck]:checked").val() == 'rcpDet') {
                    str += '<td>' + defaultString(v.payItemNm) + '</td>';
                }
                str += '<td class="text-right">' + defaultString(numberToCommas(v.payItemAmt)) + '</td>';
                str += '<td class="text-right">' + defaultString(numberToCommas(v.rcpAmt)) + '</td>';
                str += '<td>' + defaultString(v.sveCd) + '</td>';
                str += '<td>' + defaultString(v.bnkNm) + '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
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
                $('#fSearchOrderBy').val($('#searchOrderBy option:selected').val());
                $('#fStatusCheck').val($("input[name=statusCheck]:checked").val());
                // 다운로드
                $('#fileForm').submit();
            }
        });
    }

    // 영수증 인쇄
    function fnPrint(billSect) {
        var count = $('input:checkbox[name=checkList]:checked').length;
        var checkArr = [];
        var printMsg = "";
        var billSectPrintMsg = "";
        var flag = true;
        var alertResult = false;

        if(count == 0) {
            swal({
                type: 'info',
                text: "선택된 인쇄건이 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
        }else{
            if(billSect == "cut"){
                billSectPrintMsg = "영수증";
                printMsg = "건의 "+billSectPrintMsg+"를 인쇄하시겠습니까?";
            }else{
                billSectPrintMsg = "증명서";
                printMsg = "건의 "+billSectPrintMsg+"를 인쇄하시겠습니까?";
            }

            swal({
                type: 'question',
                html: count + "" + printMsg,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true,
                onAfterClose: function() {
                    if(alertResult) {
                        receiptVillFile(checkArr, billSect);
                    }
                }
            }).then(function(result) {
                if(result.value == true) {
                    $('input:checkbox[name=checkList]:checked').each(function(i) {
                        if(nullValueChange($(this).val()) != 'null'){
                            checkArr.push($(this).val());
                        }

                        if($(this).attr('notimasst') == '완납' || $(this).attr('notimasst') == '일부납' || $(this).attr('notimasst') == '초과납'){
                        }else{
                            swal({
                                type: 'info',
                                text: "납부된 수납 건에 대해서만 "+billSectPrintMsg+" 발급이 가능합니다.",
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                            flag = false;
                            return false;
                        }
                    });

                    if(flag == true){
                        alertResult = true;
                    }
                }
            });

        }
    }

    // true 일 경우 영수증파일 생성
    function receiptVillFile(checkArr, billSect) {
        var agent = navigator.userAgent.toLowerCase();

        $('#checkListValue').val(checkArr);
        $('#billSect').val(billSect);
        $('#sBrowserType').val('ie');
        $('#sChaCd').val($('#chaCd').val());

        if(billSect == "cut"){ // 영수증
            $('#pdfMakeForm').submit();
        }else{ // 증명서
            $("#popup-adm-choose-certificate").modal({
                backdrop:'static',
                keyboard : false
            });
        }
    }

</script>

<form id="pdfMakeForm" name="pdfMakeForm" method="post" action="/org/receiptMgnt/admPastPayPdfMakeRcpBill">
    <input type="hidden" id="billSect" name="billSect"/>
    <input type="hidden" id="checkListValue" name="checkListValue"/>
    <input type="hidden" id="sBrowserType" name="sBrowserType" />
    <input type="hidden" id="sChaCd" name="sChaCd" />
    <input type="hidden" id="amtChkTy" name="amtChkTy" value="Y"/>
</form>

<form id="fileForm" name="fileForm" method="post" action="/sys/addServiceMgmt/pastRcpHistExcelDown">
    <input type="hidden" id="fStartDate" name="fStartDate" value=""/>
    <input type="hidden" id="fEndDate" name="fEndDate" value=""/>
    <input type="hidden" id="fChaCd" name="fChaCd" value=""/>
    <input type="hidden" id="fSearchOrderBy" name="fSearchOrderBy" value=""/>
    <input type="hidden" id="fStatusCheck" name="fStatusCheck" value=""/>
</form>

<input type="hidden" id="pageNo" name="pageNo" value="1">
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>과거수납내역조회</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>부가서비스관리</a>
            </li>
            <li class="active">
                <strong>과거수납내역조회</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 과거수납내역을 조회하는 화면입니다.</p>
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
                                    <label class="form-label block">입금일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" name="startday" id="startday" value="" maxlength="8"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" name="endday" id="endday" value="" maxlength="8"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth"  id="btnSetMonth1"  onclick="setMonthTerm(6);">6개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth"  id="btnSetMonth6"  onclick="setMonthTerm(12);">1년</button>
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
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaCd" id="chaCd" maxlength="50" style="height: 34px;">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">조회구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="radio" id="rcpMas" name="statusCheck" value="rcpMas">
                                                <label for="rcpMas"> 기본 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="radio" id="rcpDet" name="statusCheck" value="rcpDet">
                                                <label for="rcpDet"> 항목별 </label>
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
                            전체 건수 : <strong class="text-success" id="totalCount"></strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="arrayChange();">
                                <option value="payDt">결제일순</option>
                                <option value="masMonth">청구월순</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" onclick="fn_fileSave()">파일저장</button>
                            <button class="btn btn-sm btn-d-gray" onclick="fnPrint()">증명서인쇄</button>
                            <button class="btn btn-sm btn-d-gray" onclick="fnPrint('cut')">영수증인쇄</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <thead>
                                <tr>
                                    <th>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="row-th" value="option1">
                                            <label for="row-th"></label>
                                        </div>
                                    </th>
                                    <th>기관코드</th>
                                    <th>기관명</th>
                                    <th>청구월</th>
                                    <th>입금일시</th>
                                    <th>고객명</th>
                                    <th>가상계좌번호</th>
                                    <th>고객구분1</th>
                                    <th>고객구분2</th>
                                    <th>고객구분3</th>
                                    <th>고객구분4</th>
                                    <th id="notiItemNm">청구항목명</th>
                                    <th>청구금액</th>
                                    <th>입금금액</th>
                                    <th>입금수단</th>
                                    <th>입금은행</th>
                                </tr>
                                </thead>
                                <tbody id="resultBody">
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

<jsp:include page="/WEB-INF/views/include/modal/admin/adm-choose-certificate.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<script>
    $(document).ready(function () {
        setMonthTerm(1);
        $("#rcpMas").prop("checked", true);
        $("#chaCd").val("20000000");

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

        $("#row-th").click(function(){
            if($("#row-th").prop("checked")) {
                $("input[name=checkList]").prop("checked",true);
            } else {
                $("input[name=checkList]").prop("checked",false);
            }
        });
        fnSearch(1);
    });

    function fn_listCheck() {
        var stList = [];
        var idx = 0;
        var num = 0;

        var check = $("input[name='checkList']");
        check.map(function(i) {
            if($(this).is(':checked') == true) {
                idx++;
            }
            num++;
        });

        if(num == idx) {
            $('#row-th').prop('checked', true);
        } else{
            $('#row-th').prop('checked', false);
        }
    }
</script>
