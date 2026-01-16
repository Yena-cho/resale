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
    var twoDepth = "adm-sub-26-1";
</script>

<style>
#resultBody td {
	vertical-align: middle;
}
</style>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>수납별 현황</h2>
        <ol class="breadcrumb">
            <li>
                <a>현금영수증 관리</a>
            </li>
            <li class="active">
                <strong>수납별 현황</strong>
            </li>
        </ol>
        <p class="page-description">수납별 현금영수증 현황 정보를 조회하는 화면입니다.</p>
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
                                    <label class="form-label block">수납일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" id="startDate" name="startDate" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="endDate" name="endDate" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth" id="btnSetMonth0" onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth"  id="btnSetMonth1"  onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth"  id="btnSetMonth6"  onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">거래일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker2">
                                                <input type="text" class="input-sm form-control"  id="startDate2" name="startDate2" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="endDate2" name="endDate2" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth2" id="btnSetMonth0_2" onclick="setMonthTerm2(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth2"  id="btnSetMonth1_2"  onclick="setMonthTerm2(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth2"  id="btnSetMonth6_2"  onclick="setMonthTerm2(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaCd" id="chaCd" value="${param.chaCd}" />
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaName" id="chaName" value="${param.chaName}" />
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">수납방법</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="svecdAll" name="sveCd" value="" checked="checked" />
                                                <label for="svecdAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="VAS" name="sveCd" value="VAS">
                                                <label for="VAS"> 가상계좌 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="DCS_DVA" name="sveCd" value="DCS_DVA">
                                                <label for="DCS_DVA"> 현금 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="OCD" name="sveCd" value="OCD">
                                                <label for="OCD"> 온라인카드 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="DCD" name="sveCd" value="DCD">
                                                <label for="DCD"> 오프라인카드 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">현금영수증 데이터 생성 여부</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input id="createCashReceipt_0" type="radio" name="createCashReceipt" value="" checked="checked" />
                                                <label for="createCashReceipt_0"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="createCashReceipt_1" type="radio" name="createCashReceipt" value="Y">
                                                <label for="createCashReceipt_1"> 생성 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="createCashReceipt_2" type="radio" name="createCashReceipt" value="N">
                                                <label for="createCashReceipt_2"> 미생성 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">현금영수증 미발급 오류 여부</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input id="notIssuedErrorCashReceipt_0" type="radio" name="notIssuedErrorCashReceipt" value="" checked="checked" />
                                                <label for="notIssuedErrorCashReceipt_0"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="notIssuedErrorCashReceipt_1" type="radio" name="notIssuedErrorCashReceipt" value="Y">
                                                <label for="notIssuedErrorCashReceipt_1"> 정상 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="notIssuedErrorCashReceipt_2" type="radio" name="notIssuedErrorCashReceipt" value="N">
                                                <label for="notIssuedErrorCashReceipt_2"> 오류 </label>
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
                            전체 기관 : <strong class="text-success" id="totCnt"></strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="nSearchOrderBy" id="orderBy" onchange="pageChange();">
                                <option value="clientId">거래일시 내림차순</option>
                                <option value="clientName">기관코드 오름차순</option>
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
                            <table class="table table-stripped table-align-center" style="table-layout: fixed">
                                <colgroup>
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="200" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="200" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="200" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="200" />
                                    <col width="100" />
                                    <col width="100" />
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>거래번호</th>
                                        <th>거래일시</th>
                                        <th>수납일자</th>
                                        <th>수납상태</th>
                                        <th>발행여부</th>
                                        <th>가상계좌<br/>자동발헹여부</th>
                                        <th>수기수납<br/>발행여부</th>
                                        <th>수기수납<br/>자동발행여부</th>
                                        <th>면세여부</th>
                                        <th>현금영수증번호</th>
                                        <th>상태</th>
                                        <th>승인일자</th>
                                        <th>승인번호</th>
                                        <th>매출금액</th>
                                        <th>세금</th>
                                        <th>서비스</th>
                                        <th>발급번호</th>
                                        <th>발급번호구분</th>
                                        <th>발급자구분</th>
                                        <th>마지막<br/>조작자</th>
                                        <th>마지막<br/>조작일시</th>
                                        <th></th>
									</tr>
                                </thead>

                                <tbody id="resultBody">
                                    <tr>
                                        <td colspan=15>[조회된 내역이 없습니다.]</td>
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

<!-- 생성 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/cash-receipt/create-cash-receipt.jsp" flush="false"/>

<!-- 발행 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/cash-receipt/request-cash-receipt.jsp" flush="false"/>

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

	$("#row-th").click(function(){ 
		if($("#row-th").prop("checked")) {
			$("input[name=checkOne]").prop("checked",true); 
		} else { 
			$("input[name=checkOne]").prop("checked",false); 
		} 
	});
	
	setMonthTerm(1);
    setMonthTerm2(0);
	fnSearch();
});

function list(page, val) {
	if(val == '55') {
		fn_ListCollector(page); // 기관검색
	} else {
		fnSearch(page);
	}
}

function fnSearch(page) {
    var pageNo = page || 1;
    $('#pageNo').val(pageNo);
	var startday = $('#startDate').val().replace(/\./gi, "");
    var endday   = $("#endDate").val().replace(/\./gi, "");

    if((startday > endday)) {
        swal({
            type: 'info',
            text: '수납일자를 확인해 주세요.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }

    var startday2 = $('#startDate2').val().replace(/\./gi, "");
    var endday2   = $("#endDate2").val().replace(/\./gi, "");

    if((startday2 > endday2)) {
        swal({
            type: 'info',
            text: '거래일자를 확인해 주세요.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }

    var param = {
        chaCd: $('#chaCd').val(),
        chaName: $('#chaName').val(),
        startDate: startday,
        endDate: endday,
        startDate2: startday2,
        endDate2: endday2,
        sveCd: $("input[name=sveCd]:checked").val(),
        createCashReceipt: $("input[name=createCashReceipt]:checked").val(),
        notIssuedErrorCashReceipt: $("input[name=notIssuedErrorCashReceipt]:checked").val(),
        orderBy: $('#orderBy option:selected').val(),
        pageNo: pageNo,
        pageSize: $('#pageScale option:selected').val()
    };

    if(history.pushState) {
        history.pushState(param, '수납별 현황', '/sys/cash-receipt/status-by-receipt?' + $.param(param));
    }

	$.ajax({
		type : "get",
		async : true,
		url : '/sys/rest/cash-receipt/status-by-receipt',
		contentType : "application/json; charset=utf-8",
		data : param,
		success : function(result) {			
			$("#row-th").prop("checked", false);  
            fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
            fnPaginate(result, pageNo, 'PageArea');					
		},
		error:function(result){
			$("#row-th").prop("checked", false);  
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
    
    $('#totCnt').text(result.totalItemCount);
    if (result == null || result.totalItemCount <= 0) {
        $('<tr><td colspan="15" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>').appendTo($tbody);
        return;
    }
    
    $.each(result.itemList, function (i, v) {
        var $tr = $('<tr></tr>').appendTo($tbody);

        $('<td>' + NVL(v.chaCd) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.chaName) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.rcpmasCd) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.regDt) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.payDay) + '</td>').appendTo($tr);

        if(NVL(v.rcpmasSt) == 'PA03'){
            $('<td>수납</td>').appendTo($tr);
        }else if(NVL(v.rcpmasSt) == 'PA09'){
            $('<td>취소</td>').appendTo($tr);
        }else{
            $('<td></td>').appendTo($tr);
        }

        if(NVL(v.rcpreqYn) == 'Y'){
            $('<td>발행</td>').appendTo($tr);
        }else{
            $('<td>발행안함</td>').appendTo($tr);
        }

        if(NVL(v.rcpReqty) == 'A'){
            $('<td>자동</td>').appendTo($tr);
        }else{
            $('<td>수동</td>').appendTo($tr);
        }

        if(NVL(v.rcpReqsvety) == '00'){
            $('<td>발행안함</td>').appendTo($tr);
        }else if(NVL(v.rcpReqsvety) == '01'){
            $('<td>발행</td>').appendTo($tr);
        }else{
            $('<td></td>').appendTo($tr);
        }

        if(NVL(v.mnlrcpReqty) == 'A'){
            $('<td>자동</td>').appendTo($tr);
        }else{
            $('<td>수동</td>').appendTo($tr);
        }

        if(NVL(v.notaxYn) == 'Y'){
            $('<td>과세</td>').appendTo($tr);
        }else{
            $('<td>면세</td>').appendTo($tr);
        }

        $('<td>' + NVL(v.cashmasCd) + '</td>').appendTo($tr);

        if(NVL(v.cashMasst) == 'ST02'){
            $('<td>미발행</td>').appendTo($tr);
        }else if(NVL(v.cashMasst) == 'ST03'){
            $('<td>발행</td>').appendTo($tr);
        }else if(NVL(v.cashMasst) == 'ST04'){
            $('<td>발행중</td>').appendTo($tr);
        }else if(NVL(v.cashMasst) == 'ST05'){
            $('<td>발행요청</td>').appendTo($tr);
        }else{
            $('<td></td>').appendTo($tr);
        }

        $('<td>' + NVL(v.appDay) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.appNo) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.rcpAmt) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.tax) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.tip) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.cusoffNo) + '</td>').appendTo($tr);

        if(NVL(v.confirm) == '11'){
            $('<td>휴대폰번호</td>').appendTo($tr);
        }else if(NVL(v.confirm) == '12'){
            $('<td>현금영수증카드번호</td>').appendTo($tr);
        }else if(NVL(v.confirm) == '21'){
            $('<td>사업자등록번호</td>').appendTo($tr);
        }else if(NVL(v.confirm) == '13'){
            $('<td>주민번호</td>').appendTo($tr);
        }else{
            $('<td></td>').appendTo($tr);
        }

        if(NVL(v.cusType) == '1'){
            $('<td>소득공제</td>').appendTo($tr);
        }else if(NVL(v.cusType) == '2'){
            $('<td>지출증빙</td>').appendTo($tr);
        }else{
            $('<td></td>').appendTo($tr);
        }

        $('<td>' + NVL(v.maker) + '</td>').appendTo($tr);

        var makeDate = new Date(v.makeDt);
        $('<td>' + makeDate.toLocaleString()+ '</td>').appendTo($tr);

        $('<td></td>')
            //.append('<button type="button" class="btn btn-xs btn-info " style="float: left; margin-right: 3px;" onclick="popupCreateCashReceipt(\''+v.id+'\', \''+startDate+'\', \''+endDate+'\')" >생성</button>')
            //.append('<button type="button" class="btn btn-xs btn-info " style="float: left;" onclick="popupRequestCashReceipt(\''+v.id+'\', \''+startDate+'\', \''+endDate+'\')" >발행</button>')
            .appendTo($tr);
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

function setMonthTerm2 (val) {
    $('#searchDate2').val(val);
    var toDate = new Date();

    if(val != 0) {
        $('#startDate2').attr('disabled', false);
        $('#endDate2').attr('disabled', false);

        $('#startDate2').val(monthAgo($('#endDate2').val(), val));
        $('#endDate2').val(getDateFormatDot(toDate));

        $("button[name=btnSetMonth2]").removeClass('active');
        $('#btnSetMonth2_1' + val).addClass('active');
    } else {
        $('#startDate2').attr('disabled', true);
        $('#endDate2').attr('disabled', true);

        $('#startDate2').val('');
        $('#endDate2').val('');

        $("button[name=btnSetMonth2]").removeClass('active');
        $('#btnSetMonthAll2_1').addClass('active');
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
            text: '수납일자를 확인해 주세요.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }

    var startday2 = $('#startDate2').val().replace(/\./gi, "");
    var endday2   = $("#endDate2").val().replace(/\./gi, "");

    if((startday2 > endday2)) {
        swal({
            type: 'info',
            text: '거래일자를 확인해 주세요.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }

    var param = {
        chaCd: $('#chaCd').val(),
        chaName: $('#chaName').val(),
        startDate: startday,
        endDate: endday,
        startDate2: startday2,
        endDate2: endday2,
        sveCd: $("input[name=sveCd]:checked").val(),
        createCashReceipt: $("input[name=createCashReceipt]:checked").val(),
        notIssuedErrorCashReceipt: $("input[name=notIssuedErrorCashReceipt]:checked").val(),
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
        
        var url = '/sys/rest/cash-receipt/status-by-receipt/download?' + $.param(param);
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
    var totalItemCount = result.totalItemCount;

    if(totalItemCount <= pageSize) {
        return;
    }

    var paginate = window.paginate(pageNo*1, totalItemCount, pageSize);

    var val = result.modalNo;
    var str = '';
    str += '<button type="button" class="btn btn-white" onclick="list(' + paginate.previousPage + ', ' + val + ')"><i class="fa fa-chevron-left"></i></button>';
    for (var i = paginate.startPage; i <= paginate.endPage; i++) {
        if (i == result.pageNo) {
            str += '<button class="btn btn-white  active">' + i + '</button>'; // 현재 페이지인 경우 하이퍼링크 제거
        } else {
            str += '<button class="btn btn-white" onclick="list(' + i + ', ' + val + ')">' + i + '</button>';
        }
    }
    str += '<button type="button" class="btn btn-white" onclick="list(' + paginate.nextPage + ', ' + val + ')"><i class="fa fa-chevron-right"></i> </button>';
    $('#' + obj).html(str);
}
</script>
