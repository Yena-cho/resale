<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false" />
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>
<script src="/assets/js/paginate.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-8";
    var twoDepth = "adm-sub-28-1";
</script>

<style>
#resultBody td {
	vertical-align: middle;
}
</style>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>현금영수증 관리</h2>
        <ol class="breadcrumb">
            <li>
                <a>현금영수증 관리</a>
            </li>
            <li class="active">
                <strong>현금영수증 이용내역조회</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 현금영수증 이용내역을 확인하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<form id="MainfileForm" name="MainfileForm" method="post">
    <input type="hidden" name="pageNo" id="pageNo" value="${param.pageNo}" />
    <input type="hidden" name="searchOrderBy"	id="searchOrderBy" />
</form>

<input type="hidden" id="curPage" name="curPage"/>

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
                                    <label class="form-label block">조회방식</label>
                                    <div class="form-group form-group-sm">
                                        <div>
                                            <div class="input-group col-md-12">
                                                <div class="radio radio-primary radio-inline">
                                                    <input id="payDay" type="radio" name="dateTy" value="payDay" checked="checked" />
                                                    <label for="payDay"> 입금일자 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input id="appDay" type="radio" name="dateTy" value="appDay">
                                                    <label for="appDay"> 승인일자 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input id="reqDay" type="radio" name="dateTy" value="reqDay">
                                                    <label for="reqDay"> 조작일시 </label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control"  id="startDate" name="startDate" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="endDate"   name="endDate" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth1"  onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth2"  onclick="setMonthTerm(2);">2개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth3"  onclick="setMonthTerm(3);">3개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="clientId" id="clientId" value="${param.clientId}" />
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="clientName" id="clientName" value="${param.clientName}" />
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
                                                    <option value="rcpUsrName">입금자명</option>
                                                    <option value="cusOffNo">현금영수증발행번호</option>
                                                    <option value="maker">조작자</option>
                                                </select>
                                            </span>
                                            <input type="text" class="form-control" id="searchValue" name="searchValue" maxlength="50">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">현금영수증 발행현황</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input id="resultTy" type="radio" name="resultTy" value="" checked="checked" />
                                                <label for="resultTy"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="issued" type="radio" name="resultTy" value="issued">
                                                <label for="issued"> 발행 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="notissued" type="radio" name="resultTy" value="notissued">
                                                <label for="notissued"> 미발행 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="request" type="radio" name="resultTy" value="request">
                                                <label for="request"> 요청접수 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fnSearch('1');" >조회</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="animated fadeInRight">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">
                            <span class="m-r-sm m-l-sm">전체 건수 : <strong class="text-success" id="totCnt"></strong> 건</span>
                            <span> | </span>
                            <span class="m-r-sm m-l-sm">거래금액 : <strong class="text-success" id="totAmt"></strong> 원</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="nSearchOrderBy" id="orderBy" onchange="pageChange();">
                                <option value="payDay">입금일자순</option>
                                <option value="appDay">승인일자순</option>
                                <option value="reqDay">조작일시순</option>
                                <option value="chaCd">기관코드순</option>
                            </select>
                            <select class="form-control"  name="pageScale" id="pageScale" onchange="pageChange();">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" type="button" onclick="fn_fileSave();">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-striped table-align-center" style="table-layout:fixed;">
                                <colgroup>
                                    <col width="80">
                                    <col width="80">
                                    <col width="70">
                                    <col width="80">
                                    <col width="80">
                                    <col width="100">
                                    <col width="70">
                                    <col width="100">
                                    <col width="100">
                                    <col width="110">
                                    <col width="110">
                                    <col width="80">
                                    <col width="80">
                                    <col width="80">
                                    <col width="80">
                                    <col width="80">
                                    <col width="80">
                                    <col width="100">
                                    <col width="80">
                                    <col width="80">
                                    <col width="100">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>과세여부</th>
                                        <th>입금일자</th>
                                        <th>승인일자</th>
                                        <th>승인번호</th>
                                        <th>승인여부</th>
                                        <th>취소 승인일자</th>
                                        <th>취소 승인번호</th>
                                        <th>사업자등록번호</th>
                                        <th>현금영수증 발행번호</th>
                                        <th>고객명</th>
                                        <th>입금자명</th>
                                        <th>거래금액</th>
                                        <th>공급가액</th>
                                        <th>부가세액</th>
                                        <th>입금구분</th>
                                        <th>발행상태</th>
                                        <th>조작일시</th>
                                        <th>조작자</th>
                                        <th>실패사유</th>
									</tr>
                                </thead>

                                <tbody id="resultBody">
                                    <tr>
                                        <td colspan=20>[조회된 내역이 없습니다.]</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <jsp:include page="/WEB-INF/views/include/sysPaging.jsp" flush="false" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 어드민용 스피너 추가 -->
<div class="spinner-area" style="display:none;">
    <div class="sk-spinner sk-spinner-cube-grid">
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
    </div>
    <div class="modal-backdrop-back-spinner"></div>
</div>
<!-- 어드민용 스피너 추가 -->
<!-- ##################################################################### -->


<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false" />

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<script>

$(document).ready(function(){
	$('.input-daterange').datepicker({
	    format: 'yyyy.mm.dd',			
	    maxDate: "+0d",
		keyboardNavigation : false,	
		forceParse : false,
		autoclose : true
	});

	$('.input-group.date').datepicker({
	    format: 'yyyy.mm.dd',
	    maxDate: "+0d",
	    todayBtn: "linked",
	    keyboardNavigation: false,
	    forceParse: false,
	    calendarWeeks: true,
	    autoclose: true
	});

    $(".btn-lookup-collecter").click(function(){
        $("#lookup-collecter-popup").modal({
            backdrop: 'static',
            keyboard: false
        });
    	$("#popChacd").val($('#chacd').val());
    	$("#popChaname").val('');
    	fn_ListCollector();
    });

	setMonthTerm(1);
	fnSearch(1);
	
});

function list(page) {
		fnSearch(page);
}

function fnSearch(page) {
    var pageNo = page || 1;
    $('#pageNo').val(pageNo);
	var startday = $('#startDate').val().replace(/\./gi, "");
	var endday   = $("#endDate").val().replace(/\./gi, "");
	if((startday > endday)) {
		swal({
           type: 'info',
           text: '조회일자를 확인해 주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
       });
		return;
	}

    var param = {
        chaCd: $('#clientId').val(),
        chaName: $('#clientName').val(),
        startDate: startday,
        endDate: endday,
        dateTy: $("input[name=dateTy]:checked").val(),
        searchGb: $('#searchGb option:selected').val(),
        searchValue: $("#searchValue").val(),
        resultTy: $("input[name=resultTy]:checked").val(),

        orderBy: $('#orderBy option:selected').val(),
        pageNo: pageNo,
        pageSize: $('#pageScale option:selected').val()
    };

    if(history.pushState) {
        history.pushState(param, '현금영수증 이용내역조회', '/sys/cash-receipt/cash-receipt-history?' + $.param(param));
    }

	$.ajax({
		type : "get",
		async : true,
		url : '/sys/rest/cash-receipt/cash-receipt-history',
		contentType : "application/json; charset=utf-8",
		data : param,
		success : function(result) {
            fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
            fnPaginate(result, pageNo, 'PageArea');					
		},
		error:function(result){
			swal({
				type: 'info',
				text: JSON.stringify(result),
				confirmButtonColor: '#3085d6',
				confirmButtonText: '확인'
			});
		}			
	});
}

function fnGrid(result, obj) {
    var $tbody = $('#' + obj).empty();
    
    $('#totCnt').text(result.list.totalItemCount);
    $('#totAmt').text(numberToCommas(result.list.totalItemAmount));
    if (result.list == null || result.list.totalItemCount <= 0) {
        $('<tr><td colspan="21" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>').appendTo($tbody);
        return;
    }
    
    $.each(result.list.itemList, function (i, v) {
        var $tr = $('<tr></tr>').appendTo($tbody);

        $('<td>' + NVL(v.chaCd) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.chaName) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.noTaxYn) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.payDay) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.txDate) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.txNo) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.txTypeCode) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.cancelTxDate) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.cancelTxNo) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.hostIdentityNo) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.clientIdentityNo) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.cusName) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.rcpusrName) + '</td>').appendTo($tr);
        $('<td>' + numberToCommas(v.txAmount) + '</td>').appendTo($tr);
        $('<td>' + numberToCommas(v.taxFreeAmount) + '</td>').appendTo($tr);
        $('<td>' + numberToCommas(v.tax) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.svecdNm) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.reqStatus) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.requestDt) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.requestUser) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.responseMessage) + '</td>').appendTo($tr);

    });
}

function setMonthTerm (val) {
	$('#searchDate').val(val);
	var toDate = new Date();
	if(val != 0) {
		$('#startDate').attr('disabled', false);
		$('#endDate').attr('disabled', false);
		
	    $('#endDate').val(getDateFormatDot(toDate));
        $('#startDate').val(monthAgo($('#endDate').val(), val));

	    $("button[name=btnSetMonth]").removeClass('active');
	    $('#btnSetMonth' + val).addClass('active');
	} else {
		$('#startDate').attr('disabled', true);
		$('#endDate').attr('disabled', true);
		
		$('#startDate').val('');
	    $('#endDate').val('');
	    
		$("button[name=btnSetMonth]").removeClass('active');
	    $('#btnSetMonthAll').addClass('active');
	}
}

function pageChange() {
	cuPage = 1;
	fnSearch(cuPage);
}

function NVL(value) { 

	if( value == "" || value == null || value == undefined 
	||( value != null && typeof value == "object" && !Object.keys(value).length )) {
		return ""; 
	} else { 
		return value; 
	} 
}

function fn_fileSave() {
    var startday = $('#startDate').val().replace(/\./gi, "");
    var endday   = $("#endDate").val().replace(/\./gi, "");
    if((startday > endday)) {
        swal({
            type: 'info',
            text: '조회일자를 확인해 주세요.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }

    var param = {
        chaCd: $('#clientId').val(),
        chaName: $('#clientName').val(),
        startDate: startday,
        endDate: endday,
        dateTy: $("input[name=dateTy]:checked").val(),
        searchGb: $('#searchGb option:selected').val(),
        searchValue: $("#searchValue").val(),
        resultTy: $("input[name=resultTy]:checked").val(),
        orderBy: $('#orderBy option:selected').val(),
    };
	
	swal({
		type: 'question',
		html: "다운로드 하시겠습니까?",  
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '확인',
		cancelButtonText: '취소'
	}).then(function(result) { 
		if (!result.value) {
		    return;
        }
        
        var url = '/sys/rest/cash-receipt/cash-receipt-history/download?' + $.param(param);
        window.open(url, '_blank');
	});	
}

function fnPaginate(result, pageNo, obj) {
    var $paginate = $('#' + obj);
    $paginate.empty();

    if(!result) {
        return;
    }

    var pageSize = $('#pageScale').val();
    var totalItemCount = result.list.totalItemCount;

    if(totalItemCount <= pageSize) {
        return;
    }

    var paginate = window.paginate(pageNo*1, totalItemCount, pageSize);

    var str = '';
    str += '<button type="button" class="btn btn-white" onclick="list(' + paginate.previousPage + ')"><i class="fa fa-chevron-left"></i></button>';
    for (var i = paginate.startPage; i <= paginate.endPage; i++) {
        if (i == result.list.pageNo) {
            str += '<button class="btn btn-white  active">' + i + '</button>'; // 현재 페이지인 경우 하이퍼링크 제거
        } else {
            str += '<button class="btn btn-white" onclick="list(' + i + ')">' + i + '</button>';
        }
    }
    str += '<button type="button" class="btn btn-white" onclick="list(' + paginate.nextPage + ')"><i class="fa fa-chevron-right"></i> </button>';
    $('#' + obj).html(str);
}
</script>
