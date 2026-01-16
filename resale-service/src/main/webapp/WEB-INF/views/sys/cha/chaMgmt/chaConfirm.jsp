<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false" />
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">
<%-- <link href="/html/assets/css/plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet"> --%>

<script src="/assets/js/common.js?version=${project.version}"></script>
<input type="hidden" id="curPage" name="curPage"/>

<script>
    var oneDepth = "adm-nav-2";
    var twoDepth = "adm-sub-03";
</script>

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-6">
        <h2>기관검증관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>기관관리</a>
            </li>
            <li class="active">
                <strong>기관검증관리</strong>
            </li>
        </ol>
        <p class="page-description">등록된 기관 이용상태를 검증하는 화면입니다.</p>
    </div>

    <div class="col-lg-6 text-right m-t-lg">
        <button class="btn btn-lg btn-w-m btn-danger" id="btnupdateStatusJobHistory" onclick="updateStatusJobHistory();" style="display:none;">
            <i class="fa fa-fw fa-refresh"></i>강제완료처리
        </button>
        <button class="btn btn-lg btn-w-m btn-primary" id="btnchkChaConfirmManual" onclick="chkChaConfirmManual();">
            <i class="fa fa-fw fa-refresh"></i>수동검증
        </button>
        <h4 class="text-danger"><span id="lastStartDateTime"></span></h4>
    </div>
</div>

<div class="wrapper-content">
    <div class="animated fadeInRight">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">
                            <span>전체 건수 : <strong class="text-success" id="totCnt">0</strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
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
                            <table class="table table-stripped table-align-center"><!-- 2018.05.11 클래스 수정 -->
                                <colgroup>
                                    <col width="50">
                                    <col width="50">
                                    <col width="120">
                                    <col width="120">
                                    <col width="230">
                                    <col width="100">
                                    <col width="130">
                                    <col width="140">
                                    <col width="150">
                                    <col width="120">
                                    <col width="400">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="row-th" value="option1">
                                                <label for="row-th"></label>
                                            </div>
                                        </th>
                                        <th>NO</th>
                                        <th>검증일시</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>업종</th>
                                        <th>담당자명</th>
                                        <th>담당자핸드폰번호</th>
                                        <th>기관상태</th>
                                        <th>기관검증결과</th>
                                        <th>부적합 사유</th>
                                        <!-- <th>제한기관등록</th> -->
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                <tr>
                                    <td colspan="11">[조회된 내역이 없습니다.]</td>
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

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false" />

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<!-- SMS 보내기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/send-sms.jsp" flush="false"/>

<!-- 이메일 보내기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/send-email.jsp" flush="false"/>


<script>
var noSelectMsg = "선택하신 기관이 없습니다.";
$(document).ready(function(){
	$("#btnupdateStatusJobHistory").hide();
    
	// 그리드 전체선택, 전체해제
	$("#row-th").click(function(){ //만약 전체 선택 체크박스가 체크된상태일경우 
		if($("#row-th").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
			$("input[name=checkOne]").prop("checked",true); // 전체선택 체크박스가 해제된 경우 
		} else { //해당화면에 모든 checkbox들의 체크를해제시킨다. 
			$("input[name=checkOne]").prop("checked",false); 
		} 
	});	
	
	getStatusJobHistory();
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

function getStatusJobHistory() {
	var url = "/sys/chaMgmt/getStatusJobHistory";
	var jobid = "chkChaConfirmManual";
	var status = "";	
	var param = {
		jobid   : jobid,
		status  : status  
	};
	$.ajax({
		type : "post",
		async : true,
		url: url,
		data: param,
		success: function(result){
			var retCode = result.retCode;
			var retMsg  = result.retMsg;
			if(retCode == "0000") {
				var map = result.jobhistory;
				var headerEnd     = "최근 작업종료 일시 : ";
				var headerStart   = "최근 작업시작 일시 : ";
				var headerErrCnt  = " /부적합건수 : ";
				var lastStatus    = map.status;
				var totcnt        = map.totCnt;
				var _startdate    = map.startDate;
				if(_startdate != null && _startdate != '' && _startdate != 'null') {
					_startdate = getDateTimeFmtDot(_startdate);
				}
				
				var _enddate = map.endDate;
				if(_enddate != null && _enddate != '' && _enddate != 'null') {
					_enddate = getDateTimeFmtDot(_enddate);
				}
				if(lastStatus == "1") {
					$('#lastStartDateTime').text(headerStart + _startdate);						
					$('#btnchkChaConfirmManual').text("작업중");
					$('#btnupdateStatusJobHistory').show();
				} else {
					$('#lastStartDateTime').text(headerEnd + _enddate + headerErrCnt + totcnt);						
				}
			} else {
				swal({
		           type: 'error',
		           text: '[' + retCode + '] ' + retMsg,
		           confirmButtonColor: '#3085d6',
		           confirmButtonText: '확인'
		       });				
			}										
		},
		error:function(data){
			swal({
	           type: 'error',
	           text: '작업이력상태조회를 실패하였습니다.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       });
		}
	});
}

//검색
function fnSearch(page) {
	var checkArr = [];
	if(page == null || page == 'undefined'){
		cuPage = '1';
	}else{
		cuPage = page;
	}

	var url = "/sys/chaMgmt/selCloseChaList";
	var param = {
		curPage   : cuPage,
		pageScale : $('#pageScale option:selected').val()
	};
	
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(param),
		success : function(result) {
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
		},
		error:function(result){
			swal({
		           type: 'error',
		           text: result,
		           confirmButtonColor: '#3085d6',
		           confirmButtonText: '확인'
		       });
		}			
	});
}

//데이터 새로고침
function fnGrid(result, obj) {
	var str = '';
	$('#totCnt').text(result.count);
	if(result == null || result.count <= 0){
		str+='<tr><td colspan="12" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
	}else{
		$.each(result.list, function(i, v){
			str += '<tr>';
			str += '<td>';
			str += '<div class="checkbox checkbox-primary checkbox-inline">';
			str += '<input type="checkbox" id="row-' + v.rn + '" name="checkOne" value="' + v.chaCd + '" onclick="fn_listCheck();">';
			str += '<label for="row-' + v.rn + '"><span></span></label>';
			str += '</div>';
			str += '</td>';
			str += '<td>'+NVL(v.rn     )+'</td>';
			str += '<td>' + NVL(v.chaCloseVarDt  ) + '</td>';
			str += '<td>' + NVL(v.chaCd  ) + '</td>';			
			str += '<td>' + NVL(v.chaName) + '</td>';
			str += '<td>' + NVL(v.chaStatus) + '</td>';
			str += '<td>' + NVL(v.chrName) + '</td>';
			str += '<td>' + NVL(v.chrHp) + '</td>';
			if(NVL(v.chast) == 'ST06') {
				str += '<td class="text-success">' + getChastNameDamoa(NVL(v.chast)) + '</td>';																	
			} else {
				str += '<td class="text-warning">' + getChastNameDamoa(NVL(v.chast)) + '</td>';															
			}	
			if(NVL(v.chaCloseChk) == 'N') {
				str += '<td class="text-success">적합</td>';					
			} else {
				str += '<td class="text-warning">부적합</td>';
			}
			str += '<td class="text-warning">' + NVL(v.chaCloseVarReson) + '</td>';
			//str += '<td><button type="button" class="btn btn-xs btn-info " onclick="updateChaAssign(\'' + v.chaCd + '\',\'' + v.chaName + '\', \'ST08\' )"  ><i class="fa fa-pencil-square-o"></i>이용정지</button></td>'
			str += '</tr>';
		});
	}
	$('#' + obj).html(str);
}

function fn_smsNoti() {	

	var rowData = new Array();
	var checkbox = $("input[name=checkOne]:checked");
	checkbox.each(function(i) {
		rowData.push($(this).val());
	})
    $("#popup-send-sms").modal({
        backdrop: 'static',
        keyboard: false
    });	
	fn_smsInit(rowData);
}

function fn_emailNoti() {
	var ecareNo = "300";	
	var rowData = new Array();
	var checkbox = $("input[name=checkOne]:checked");
	checkbox.each(function(i) {
		rowData.push($(this).val());
	})
    $("#popup-send-email").modal({
        backdrop: 'static',
        keyboard: false
    });	
	fn_emailInit(rowData, ecareNo);
}

// 파일저장
function fn_fileSave() {
	
	document.fileDown.action = "/sys/chaMgmt/selCloseChaListExcel";
	document.fileDown.submit();
}

//승인 및 거부 처리
function updateChaAssign(chacd, chaname, cd) {

	var msgYes= "[기관코드 : "+ chacd +"][기관명 : " + chaname +"]<br/> 기관을 이용정지 하시겠습니까?";
	var msg = "";
	
	cd = cd.replace(/(\s*)/g,"");
	if(cd == 'ST08') {
		msg = msgYes;
	} else {  
		swal({
		     type: 'error',
		     text: '이용정지코드가 누락되었습니다.',
		     confirmButtonColor: '#3085d6',
		     confirmButtonText: '확인'
		  });
	   	return;
	}
	
	var param = {
		chaCd   : chacd,
		chast   : cd,
		flag	: 6 
	};   
	
	swal({
		type: 'question',
		html: msg, 
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '확인',
		cancelButtonText: '취소'
	}).then(function(result) {
		if (result.value) { 
			updateChaListConfig(param);
		}
	});
}

function updateChaListConfig(inParam) {

	var param = inParam;
	var url = "/sys/chaMgmt/updateChaInfoConfig";

	$.ajax({
		type: "post",
		url: url,
		async : true,
		data: JSON.stringify(param),
		contentType : "application/json; charset=utf-8",
		success: function(result){
			if (result.retCode == "0000") {
			
		        swal({
		            type: 'success',
		            text: '기관정보수정 완료되었습니다.',
		            confirmButtonColor: '#3085d6',
		            confirmButtonText: '확인'
		        }).then(function() {
		        	pageChange();
		        });
			} else {

	        	swal({
		            type: 'warning',
		            text: result.retMsg,
		            confirmButtonColor: '#3085d6',
		            confirmButtonText: '확인'
	        	});					
			}				
		}
	});	
}

// 강제완료처리
function updateStatusJobHistory() {

	var url = "/sys/chaMgmt/updateStatusJobHistory";
	var jobid = "chkChaConfirmManual";
	var status = "";	
	var param = {
		jobid   : jobid,
		status  : status  
	};
	$.ajax({
		type : "post",
		async : true,
		url: url,
		data: param,
		success: function(result){
			var retCode = result.retCode;
			var retMsg  = result.retMsg;
			if(retCode == "0000") {
				 location.reload();
			} else {
				swal({
		        	type: 'error',
		            text: '[' + retCode + '] ' + retMsg,
		            confirmButtonColor: '#3085d6',
		            confirmButtonText: '확인'
		        });				
			}										
		},
		error:function(data){
			swal({
	           type: 'error',
	           text: '작업이력상태조회를 실패하였습니다.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       });
		}
	});
}

// 수동검증
function chkChaConfirmManual(){
	swal({
		type: 'question',
		html: "수동기관검증하시겠습니까?",  
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '확인',
		cancelButtonText: '취소'
	}).then(function(result) {
		if (result.value) {
			var url = "/sys/chaMgmt/chkChaConfirmManual";
			var param = {
				targetstatus : "1"
			};
			$.ajax({
				type : "post",
				async: true,
				url  : url,
				data : param,
				success: function(result){
					var retCode = result.retCode;
					var retMsg  = result.retMsg;
					if(retCode == "0000") {
						swal({
				            type: 'success',
				            text: '기관정보 수동검증을 정상 기동하였습니다.',
				            confirmButtonColor: '#3085d6',
				            confirmButtonText: '확인'
			        	});
						$('#btnchkChaConfirmManual').text("작업중");
					} else {
						swal({
				            type: 'error',
				            text: '[' + retCode + '] ' + retMsg,
				            confirmButtonColor: '#3085d6',
				            confirmButtonText: '확인'
				        });
							
						if(retCode == "7777") {
							$('#btnchkChaConfirmManual').text("작업중");
							var map = result.jobhistory;
							var header = "최근 작업시작 일시 : ";
							var _startdate = map.startdate;
							_startdate = getDateTimeFmtDot(_startdate);
							$('#lastStartDateTime').text(header + _startdate);
						}
					}
				},
				error:function(data){
					swal({
			            type: 'error',
			            text: '기관정보를 기관검증 실패하였습니다.',
			            confirmButtonColor: '#3085d6',
			            confirmButtonText: '확인'
			        });
				}
			});
		}
	});

}	

function getChastNameDamoa(chast) {
	var retVal = "";
	if(chast == 'ST01') {
		retVal = "승인대기";
	} else if(chast == 'ST06') {
		retVal = "승인";
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

function list(page, val) {
	fnSearch(page);
}

</script>
