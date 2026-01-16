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

<script>
    var oneDepth = "adm-nav-2";
    var twoDepth = "adm-sub-01";
</script>

<style>
#resultBody td {
	vertical-align: middle;
}
</style>

</div>
<form id="frm" name="frm" method="post">
	<input type="hidden" id="loginId" name="loginId">
</form>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>이용기관조회</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>기관관리</a>
            </li>
            <li class="active">
                <strong>이용기관조회</strong>
            </li>
        </ol>
        <p class="page-description">등록된 기관 정보를 조회하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<form id="MainfileForm" name="MainfileForm" method="post">
    <input type="hidden" name="chaCd"         	id="mChaCd" />
    <input type="hidden" name="chaName"         id="mChaName" />
    <input type="hidden" name="calDateFrom"     id="calDateFrom" />
    <input type="hidden" name="calDateTo"       id="calDateTo" />
    <input type="hidden" name="searchGb"        id="searchGb" />
    <input type="hidden" name="searchValue"     id="searchValue" />
    <input type="hidden" name="chast"         	id="chast" />
    <input type="hidden" name="chatrty"         id="chatrty" />
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
                                    <label class="form-label block">등록일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control"  id="nCalDateFrom" name="nCalDateFrom" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="nCalDateTo"   name="nCalDateTo" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                            	<button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth"  id="btnSetMonthAll"  onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth1"  onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth6"  onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>

                            <div class="row">
                                <div class="col-md-3">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chacd" maxlength="50">
                                    </div>
                                </div>

                                <div class="col-md-3">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaname" id="chaname" maxlength="50">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                             <span class="input-group-select">
                                                <select class="form-control" id="SRCHsearchGb">                                     
                                                    <option value="chaOffNo" selected>사업자번호</option>
                                                    <option value="owner">대표자명</option>
                                                	<option value="chrTelNo">담당자전화번호</option>
                                                </select>
                                            </span>
                                            <input type="text" class="form-control"  id="SRCHsearchValue" maxlength="100">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="chaStAll" name="chaStItem" value="All" checked>
                                                <label for="chaStAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="ST06" name="chaStItem" value="ST06">
                                                <label for="ST06"> 정상 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="ST08" name="chaStItem" value="ST08">
                                                <label for="ST08"> 정지 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="ST02" name="chaStItem" value="ST02">
                                                <label for="ST02"> 해지 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관분류</label>
                                    <div class="input-group col-md-12">
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="chatrtyAll" name="cChatrty" value="All" checked>
                                            <label for="chatrtyAll"> 전체 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="01" name="cChatrty" value="01">
                                            <label for="01"> WEB </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="03" name="cChatrty" value="03">
                                            <label for="03"> API </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="05" name="cChatrty" value="05">
                                            <label for="05"> SET </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="07" name="cChatrty" value="07">
                                            <label for="07"> RELAY </label>
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
                            <select class="form-control" name="nSearchOrderBy" id="nSearchOrderBy" onchange="pageChange();">
                                <option value="regDt">등록일자순 정렬</option>
                                <option value="chaCd">기관코드순 정렬</option>
                                <option value="chaName">기관명순 정렬</option>
                                <option value="rCnt">가상계좌잔여순 정렬</option>
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
<%--                                    <col width="30">--%>
                                    <col width="30">
                                    <col width="100">
                                    <col width="90">
                                    <col width="100">
                                    <col width="100">
                                    <col width="300">
                                    <col width="150">
                                    <col width="100">
                                    <col width="100">
                                    <col width="180">
                                    <col width="100">
                                    <col width="100">
                                    <col width="130">
                                </colgroup>

                                <thead>
                                    <tr>
<%--                                        <th>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="row-th" value="option1">
                                                <label for="row-th"></label>
                                            </div>
                                        </th>--%>
                                        <th>NO</th>
                                        <th>등록일자</th>
                                        <th>분류</th>
                                        <th>은행코드</th>
                                        <th>기관코드</th>
                                        <th>가맹점코드</th>
                                        <th>기관명</th>
                                        <th>사업자번호</th>
                                        <th>대표자명</th>
                                        <th>담당자명</th>
                                        <th>담당자전화번호</th>
                                        <th>상태</th>
                                        <th>잔여가상계좌</th>
										<th>Action</th>
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
<%--
                        <div class="row">
                            <div class="col-lg-12 text-right">
                                <button type="button" class="btn btn-primary" onclick="fn_smsNoti();"><i class="fa fa-fw fa-mobile"></i>문자발송</button>
                            </div>
                        </div>--%>
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

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<!-- 기관정보 상세보기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/collecter-info.jsp" flush="false"/>

<!-- SMS 보내기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/send-sms.jsp" flush="false"/>

<!-- 출금 동의서 업로드 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/withdrawal-agreement-upload.jsp" flush="false"/>

<script>

$(document).ready(function(){
	
	$('#SRCHsearchGb').on('change', function (e) {
	    var optionSelected = $("option:selected", this);
	    var valueSelected = this.value;
	    if(valueSelected =='chaOffNo') {
	    	$('#SRCHsearchValue').attr('placeholder', '사업자등록번호의 "-"를 빼고 입력해 주세요');
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

/*    $(".btn-lookup-collecter").click(function(){
        $("#lookup-collecter-popup").modal({
            backdrop: 'static',
            keyboard: false
        });
    	$("#popChacd").val($('#chacd').val());
    	$("#popChaname").val('');
    	fn_ListCollector();
    });*/

	$("#row-th").click(function(){ 
		if($("#row-th").prop("checked")) {
			$("input[name=checkOne]").prop("checked",true); 
		} else { 
			$("input[name=checkOne]").prop("checked",false); 
		} 
	});
	
	setMonthTerm(0);
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
	if(page == null || page == 'undefined'){
		cuPage = 1;
	}else{
		cuPage = page;
	}
	
	var startday = $('#nCalDateFrom').val().replace(/\./gi, "");
	var endday   = $("#nCalDateTo").val().replace(/\./gi, "");
	if((startday > endday)) {
		swal({
           type: 'info',
           text: '등록일자를 확인해 주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
       });
		return;
	}
	var url = "/sys/chaMgmt/getChaList";
	var param = {
		calDateFrom   : startday,
		calDateTo     : endday,
		chaCd 		  : $('#chacd').val(),
        chaName       : $('#chaname').val(),
		searchGb      : $('#SRCHsearchGb option:selected').val(),
		searchValue   : $('#SRCHsearchValue').val(),
		chast		  : $("input[name=chaStItem]:checked").val(),
		chatrty		  : $("input[name=cChatrty]:checked").val(),
		searchOrderBy : $('#nSearchOrderBy option:selected').val(),
		curPage       : cuPage,
		pageScale 	  : $('#pageScale option:selected').val()
	};

	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(param),
		success : function(result) {			
			// $("#row-th").prop("checked", false);
			if (result.retCode == "0000") {
				fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
				sysajaxPaging(result, 'PageArea');					
			} else {
				swal({
					type: 'info',
					text: result.retMsg,
					confirmButtonColor: '#3085d6',
					confirmButtonText: '확인'
				});
			}
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
	var str = '';
	$('#totCnt').text(result.count);
	if(result == null || result.count <= 0){
		str+='<tr><td colspan="15" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
	} else {		
		$.each(result.list, function(i, v){
			str += '<tr>';
        /*	문자발송 시 필요
            str += '<td>';
			str += '<div class="checkbox checkbox-primary checkbox-inline">';
			str += '<input type="checkbox" id="row-' + v.rn + '" name="checkOne" value="' + v.chaCd + '" onclick="fn_listCheck();">';
			str += '<label for="row-' + v.rn + '"></label>';
			str += '</div>';
			str += '</td>';
        */
			str += '<td>' + NVL(v.rn     ) + '</td>';
            str += '<td>' + NVL(v.regDt) + '</td>'
			if(NVL(v.chatrty) == '01') {
				str+='<td class="text-success">WEB</td>';	
			} else {
				str+='<td class="text-success">API</td>';	
			}	
			str += '<td>' + NVL(v.fgCd  ) + '</td>';
			str += '<td>' + NVL(v.chaCd  ) + '</td>';
            str += '<td>' + NVL(v.mchtId) + '</td>';
            str += '<td>' + NVL(v.chaName) + '</td>';
			str += '<td>' + formatBizNo(NVL(v.chaOffNo)) + '</td>';
			str += '<td>' + NVL(v.owner) + '</td>';
			str += '<td>' + NVL(v.chrName) + '</td>';
			str += '<td>' + NVL(v.chrTelNo) + '</td>';
			if(NVL(v.chast) == 'ST06') {
				str += '<td class="text-success">'+ getChastNameDamoa(NVL(v.chast)) +'</td>';																	
			} else {
				str += '<td class="text-warning">'+ getChastNameDamoa(NVL(v.chast)) +'</td>';															
			}
			str += '<td class="text-success">' + v.rcnt + '</td>';
			str += '<td>'
				str += '<button type="button" class="btn btn-xs btn-info m-r-xs m-t-xxs" onclick="updateChaInfoPop(\''+v.chaCd +'\')" ><i class="fa fa-pencil-square-o"></i>상세정보</button>'
				str += '<button type="button" class="btn btn-xs btn-info m-r-xs m-t-xxs" onclick="fn_orgMove(\''+v.loginId +'\')" ><i class="fa fa-pencil-square-o"></i>화면이동</button>'
				str += '<button type="button" class="btn btn-xs btn-warning m-r-xs m-t-xxs"  onclick="fn_resetFailCnt(\''+v.chaCd +'\')" ><i class="fa fa-pencil-square-o"></i>잠금해제</button>'
				str += '<button type="button" class="btn btn-xs btn-warning m-r-xs m-t-xxs" onclick="fn_resetPw(\''+v.chaCd +'\')" ><i class="fa fa-pencil-square-o"></i>비번초기화</button>'
			str += '</td>'
			str += '</tr>';
		});
	}
	$('#' + obj).html(str);
}

function fn_resetFailCnt(val) {

	var url = "/sys/chaMgmt/resetFailCntAdm";
	var param = {
		chaCd : val
	};
	swal({
		type: 'question',
		html: "해당기관을 잠금해제 하시겠습니까?",  
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '확인',
		cancelButtonText: '취소'
	}).then(function(result) { 
		if (result.value) { 
			$.ajax({
				type : "post",
				async : true,
				url : url,
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(param),
				success : function(result) {			
					$("#row-th").prop("checked", false);  
					if (result.retCode == "0000") {
						swal({
							type: 'info',
							text: result.retMsg,
							confirmButtonColor: '#3085d6',
							confirmButtonText: '확인'
						});
					} else {
						swal({
							type: 'info',
							text: result.retMsg,
							confirmButtonColor: '#3085d6',
							confirmButtonText: '확인'
						});
					}
				}
			});
		}
	});
}

function fn_resetPw(val) {

	var url = "/sys/chaMgmt/resetPwAdm";
	var param = {
		chaCd : val
	};
	swal({
		type: 'question',
		html: "해당기관의 비밀번호를 초기화 하시겠습니까?",  
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '확인',
		cancelButtonText: '취소'
	}).then(function(result) { 
		if (result.value) { 
			$.ajax({
				type : "post",
				async : true,
				url : url,
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(param),
				success : function(result) {			
					$("#row-th").prop("checked", false);  
					if (result.retCode == "0000") {
						swal({
							type: 'info',
							text: result.retMsg,
							confirmButtonColor: '#3085d6',
							confirmButtonText: '확인'
						});
					} else {
						swal({
							type: 'info',
							text: result.retMsg,
							confirmButtonColor: '#3085d6',
							confirmButtonText: '확인'
						});
					}
				}
			});
		}
	});
}
 
function setMonthTerm (val) {
	$('#searchDate').val(val);
	var toDate = new Date();
	if(val != 0) {
		$('#nCalDateFrom').attr('disabled', false);
		$('#nCalDateTo').attr('disabled', false);
		
	    $('#nCalDateFrom').val(monthAgo($('#nCalDateTo').val(), val));
	    $('#nCalDateTo').val(getDateFormatDot(toDate));

	    $("button[name=btnSetMonth]").removeClass('active');
	    $('#btnSetMonth' + val).addClass('active');
	} else {
		$('#nCalDateFrom').attr('disabled', true);
		$('#nCalDateTo').attr('disabled', true);
		
		$('#nCalDateFrom').val('');
	    $('#nCalDateTo').val('');
	    
		$("button[name=btnSetMonth]").removeClass('active');
	    $('#btnSetMonthAll').addClass('active');
	}
}

// 기관 상세보기
function updateChaInfoPop(chacd) {

    $("#popup-collecter-info").modal({
        backdrop: 'static',
        keyboard: false
    });
    MODE = "U";
    collector_info_init(chacd);
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
	var startday = $('#nCalDateFrom').val().replace(/\./gi, "");
	var endday   = $("#nCalDateTo").val().replace(/\./gi, "");
	
	if( !(!startday && !endday) && ((!startday || !endday) || (startday > endday))) {
		swal({
           type: 'info',
           text: '등록일자를 확인해 주세요.',
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
			$('#mChaCd').val($('#chacd').val());
            $('#mChaName').val($('#chaname').val());
			$('#calDateFrom').val(startday);
			$('#calDateTo').val(endday);
			$('#searchGb').val($('#SRCHsearchGb option:selected').val());
			$('#searchValue').val($('#SRCHsearchValue').val());
			$('#chast').val($("input[name=chaStItem]:checked").val());
			$('#chatrty').val($("input[name=cChatrty]:checked").val());
			$('#searchOrderBy').val($('#nSearchOrderBy option:selected').val());
			
			document.MainfileForm.action = "/sys/chaMgmt/getSysChaListExcel";
			document.MainfileForm.submit();
		} 
	});	
}

function fn_orgMove(loginId) {
	$("#loginId").val(loginId);
	document.frm.action = "/sys/orgMoveAuth";
	document.frm.target = '_blank';
	document.frm.submit();
}
</script>
