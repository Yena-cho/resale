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
    var twoDepth = "adm-sub-28";
</script>

<style>
#resultBody td {
	vertical-align: middle;
}
</style>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>기관별 발행 현황</h2>
        <ol class="breadcrumb">
            <li>
                <a>현금영수증 관리</a>
            </li>
            <li class="active">
                <strong>기관별 발행 현황</strong>
            </li>
        </ol>
        <p class="page-description">등록된 기관 정보를 조회하는 화면입니다.</p>
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
                                    <label class="form-label block">수납일</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control"  id="startDate" name="startDate" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="endDate"   name="endDate" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
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
                                    <label class="form-label block">기관상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="chaStAll" name="status" value="" checked="checked" />
                                                <label for="chaStAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="ST06" name="status" value="ST06">
                                                <label for="ST06"> 정상 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="ST04" name="status" value="ST04">
                                                <label for="ST04"> 정지 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="ST02" name="status" value="ST02">
                                                <label for="ST02"> 해지 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">현금영수증 발행여부</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input id="enableCashReceipt_0" type="radio" name="enableCashReceipt" value="" checked="checked" />
                                                <label for="enableCashReceipt_0"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="enableCashReceipt_1" type="radio" name="enableCashReceipt" value="Y">
                                                <label for="enableCashReceipt_1"> 사용 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="enableCashReceipt_2" type="radio" name="enableCashReceipt" value="N">
                                                <label for="enableCashReceipt_2"> 사용안함 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">현금영수증 자동발행여부</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input id="enableAutomaticCashReceipt_0" type="radio" name="enableAutomaticCashReceipt" value="" checked="checked" />
                                                <label for="enableAutomaticCashReceipt_0"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="enableAutomaticCashReceipt_1" type="radio" name="enableAutomaticCashReceipt" value="Y">
                                                <label for="enableAutomaticCashReceipt_1"> 사용 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="enableAutomaticCashReceipt_2" type="radio" name="enableAutomaticCashReceipt" value="N">
                                                <label for="enableAutomaticCashReceipt_2"> 사용안함 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">현금영수증 의무발행업체여부</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input id="mandRcpYn_0" type="radio" name="mandRcpYn" value="" checked="checked" />
                                                <label for="mandRcpYn_0"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="mandRcpYn_1" type="radio" name="mandRcpYn" value="Y">
                                                <label for="mandRcpYn_1"> 사용 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input id="mandRcpYn_2" type="radio" name="mandRcpYn" value="N">
                                                <label for="mandRcpYn_2"> 사용안함 </label>
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
                                <option value="clientId">기관코드 오름차순</option>
                                <option value="clientName">기관명 오름차순</option>
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
                            <table class="table table-stripped table-align-center">
                                <colgroup>
                                    <col width="100" />
                                    <col width="200" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />                                    
                                    <col width="100" />
                                    <col width="50" />
                                    <col width="50" />
                                    <col width="50" />                                    
                                    <col width="50" />
                                    <col width="50" />                                    
                                    <col width="50" />
                                    <col width="50" />                                    
                                    <col width="50" />
                                    <col width="50" />                                    
                                    <col width="50" />
                                    <col width="50" />                                    
                                    <col width="50" />
                                    <col width="100" />
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th rowspan="2">기관코드</th>
                                        <th rowspan="2">기관명</th>
                                        <th rowspan="2">상태</th>
                                        <th rowspan="2">현금영수증<br />발헹여부</th>
                                        <th rowspan="2">현금영수증<br />자동발행여부</th>
                                        <th rowspan="2">현금영수증<br />의무발행업체여부</th>
                                        <th colspan="4">가상계좌</th>
                                        <th colspan="4">창구현금</th>
                                        <th colspan="4">온라인현금</th>
                                        <th rowspan="2"></th>
									</tr>
                                    <tr>
                                        <th>미생성</th>
                                        <th>미발행</th>
                                        <th>발행중</th>
                                        <th>발행</th>
                                        
                                        <th>미생성</th>
                                        <th>미발행</th>
                                        <th>발행중</th>
                                        <th>발행</th>
                                        
                                        <th>미생성</th>
                                        <th>미발행</th>
                                        <th>발행중</th>
                                        <th>발행</th>
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

                        <div class="row">
                            <div class="col-lg-12 text-right">
                                <button type="button" class="btn btn-primary" onclick="fn_smsNoti();"><i class="fa fa-fw fa-mobile"></i>문자발송</button>
                                <button type="button" class="btn btn-primary" onclick="fn_emailNoti();"><i class="fa fa-fw fa-envelope-o"></i>E-MAIL발송</button>
                            </div>
                        </div>
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
	$('#SRCHsearchGb').on('change', function (e) {
	    var optionSelected = $("option:selected", this);
	    var valueSelected = this.value;
	    if(valueSelected =='chaOffNo') {
	    	$('#SRCHsearchValue').attr('placeholder', '사업자등록번호의 "-"를 빼로 입력해 주세요');
		} else 
			$('#SRCHsearchValue').attr('placeholder', '');
	});

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

	$("#row-th").click(function(){ 
		if($("#row-th").prop("checked")) {
			$("input[name=checkOne]").prop("checked",true); 
		} else { 
			$("input[name=checkOne]").prop("checked",false); 
		} 
	});
	
	setMonthTerm(1);
	fnSearch();
	
});

function fn_listCheck() {
	var stList = [];
	var idx = 0;
	var num = 0;
	
	var check = $("input[name='checkOne']");
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

    var param = {
        clientId: $('#clientId').val(),
        clientName: $('#clientName').val(),
        startDate: startday,
        endDate: endday,
        status: $("input[name=status]:checked").val(),
        enableCashReceipt: $("input[name=enableCashReceipt]:checked").val(),
        enableAutomaticCashReceipt: $("input[name=enableAutomaticCashReceipt]:checked").val(),
        mandRcpYn: $("input[name=mandRcpYn]:checked").val(),
        orderBy: $('#orderBy option:selected').val(),
        pageNo: pageNo,
        pageSize: $('#pageScale option:selected').val()
    };
	
	if(history.pushState) {
        history.pushState(param, '기관별 현금영수증 현황', '/sys/cash-receipt/status-by-client?' + $.param(param));
    }

	$.ajax({
		type : "get",
		async : true,
		url : '/sys/rest/cash-receipt/status-by-client',
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
    var startDate = $('#startDate').val().replace(/\./gi, "");
    var endDate   = $("#endDate").val().replace(/\./gi, "");
    var $tbody = $('#' + obj).empty();
    
    $('#totCnt').text(result.totalItemCount);
    if (result == null || result.totalItemCount <= 0) {
        $('<tr><td colspan="15" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>').appendTo($tbody);
        return;
    }
    
    $.each(result.itemList, function (i, v) {
        var $tr = $('<tr></tr>').appendTo($tbody);

        $('<td>' + NVL(v.id) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.name) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.statusName) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.enableCashReceipt) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.enableAutomaticCashReceipt) + '</td>').appendTo($tr);
        $('<td>' + NVL(v.mandRcpYn) + '</td>').appendTo($tr);
        $('<td>' + v.vasNoCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.vasReadyCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.vasRequestCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.vasIssueCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.dcsNoCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.dcsReadyCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.dcsRequestCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.dcsIssueCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.dvaNoCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.dvaReadyCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.dvaRequestCashReceiptCount + '</td>').appendTo($tr);
        $('<td>' + v.dvaIssueCashReceiptCount + '</td>').appendTo($tr);

        $('<td></td>')
            .append('<button type="button" class="btn btn-xs btn-info " style="float: left; margin-right: 3px;" onclick="popupCreateCashReceipt(\''+v.id+'\', \''+startDate+'\', \''+endDate+'\')" >생성</button>')
            .append('<button type="button" class="btn btn-xs btn-info " style="float: left;" onclick="popupRequestCashReceipt(\''+v.id+'\', \''+startDate+'\', \''+endDate+'\')" >발행</button>')
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

// 기관 상세보기
function viewChaInfoPop(chacd) {

	$("#popup-collecter-info").modal({
        backdrop: 'static',
        keyboard: false
    });
    MODE = "Q";
    collector_info_init(MODE, chacd);
}

function updateChaInfoPop(chacd) {

    $("#popup-collecter-info").modal({
        backdrop: 'static',
        keyboard: false
    });
    MODE = "U";
    collector_info_init(MODE, chacd);
}

function fn_smsNoti() {
	var stList = [];
	var idx = 0;
	var checkbox = $("input[name=checkOne]:checked");
	checkbox.each(function(i) {
		stList.push($(this).val());
		idx++;
	});
	if(idx == 0) {
		swal({
           type: 'info',
           text: '문자 발송할 기관을 선택해 주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
    $("#popup-send-sms").modal({
        backdrop: 'static',
        keyboard: false
    });	
	fn_smsInit(stList);
}

function fn_emailNoti() {
	var ecareNo = "300";
	var idx = 0;
	var stList = [];
	var checkbox = $("input[name=checkOne]:checked");
	checkbox.each(function(i) {
		stList.push($(this).val());
		idx++;
	});
	if(idx == 0) {
		swal({
           type: 'info',
           text: 'E-MAIL 발송할 기관을 선택해 주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
    $("#popup-send-email").modal({
        backdrop: 'static',
        keyboard: false
    });	
	fn_emailInit(stList, ecareNo);
}

function getChastNameDamoa(chast) {
	var retVal = "";
	if(chast == 'ST01') {
		retVal = "승인대기";
	} else if(chast == 'ST06') {
		retVal = "정상";
	} else if(chast == 'ST07') {
		retVal= "이용거부";
	} else if(chast == 'ST08') {
		retVal= "정지";
	} else if(chast == 'ST02') {
		retVal= "해지";
	} else if(chast == 'ST03') {
		retVal= "승인거부";
	} else if(chast == 'ST04') {
		retVal= "정지";
	} else if(chast == 'ST05') {
		retVal= "승인대기";
	} else {
		retVal= chast;
	}
	
	return retVal;
}

function getChaCloseStNm(code) {

	var retVal = "";
	if(code == '00') {
		retVal = "미확인/오류";
	} else if(code == '11') {
		retVal = "[정상]일반";
	} else if(code == '12') {
		retVal= "[정상]간이";
	} else if(code == '21') {
		retVal= "[정상]면세";
	} else if(code == '22') {
		retVal= "[정상]비영리";
	} else if(code == '31') {
		retVal= "휴업";
	} else if(code == '32') {
		retVal= "폐업";
	} else if(code == '99')  {
		retVal= "기타";
	} else {
		retVal= code;
	}
	
	return retVal;
}

function formatBizNo(str){
	var str = str;
    var temp = str.substring(0, 3) + "-" + str.substring(3,5) + "-" + str.substring(5,10); 
	return temp;
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
            text: '등록일자를 확인해 주세요.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }

    var param = {
        clientId: $('#clientId').val(),
        clientName: $('#clientName').val(),
        startDate: startday,
        endDate: endday,
        status: $("input[name=status]:checked").val(),
        enableCashReceipt: $("input[name=enableCashReceipt]:checked").val(),
        enableAutomaticCashReceipt: $("input[name=enableAutomaticCashReceipt]:checked").val(),
        mandRcpYn: $("input[name=mandRcpYn]:checked").val(),
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
        
        var url = '/sys/rest/cash-receipt/status-by-client/download?' + $.param(param);
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

function popupCreateCashReceipt(clientId, startDate, endDate) {
    var $popup = $('#create-cash-receipt-popup');
    $popup.find('.client-id').val(clientId);
    $popup.find('.start-date').val(startDate);
    $popup.find('.end-date').val(endDate);

    $popup.modal();
}

function popupRequestCashReceipt(clientId, startDate, endDate) {
    var $popup = $('#request-cash-receipt-popup');
    $popup.find('.client-id').val(clientId);
    $popup.find('.start-date').val(startDate);
    $popup.find('.end-date').val(endDate);

    $popup.modal();
}
</script>
