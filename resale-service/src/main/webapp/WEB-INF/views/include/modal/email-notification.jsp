<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">

function fn_emailNotiPage(str) {
	var strList = [];
	var idx = 0;
	var checkbox = $("input[name='checkList']:checked");
	checkbox.map(function(i) {
		if(str == 'rec') {
			var tr = checkbox.parent().parent().eq(i);
			masCd = tr.find(':hidden').val();
			strList.push(masCd);			// notiMasCd
		} else {
			strList.push($(this).val());	// notiMasCd
		}
		idx++;
	});
	var url = "/org/notiMgmt/emailRecNo";
	var param = {
			strList : strList,
			search_orderBy : str
	};
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(param),
		success: function(data){
			fn_mailGrid(data, 'cusMailList');
			$('#mailCnt').text(idx);
			fn_billNameCh();
		}
	});
	
	$('#mailStartDt').val(getFormatCurrentDate());
	$('#mailEndDt').val(getFormatCurrentDate());
	
	$('#email-notification').modal({backdrop:'static', keyboard:false});
}

function fn_mailGrid(data, obj) {
	var str = '';
	if(data.list != null && data.list != 'undefined' && data.list != '') {
		$.each(data.list, function(k, v){
			str += '<tr>';
			str += '<input type="hidden" id="notiMasCd" value="'+ v.notiMasCd +'">';
			str += '<td>' + basicEscape(v.cusName) + '</td>';
			str += '<td class="form-inline">';
			str += '	<input type="text" class="form-control" value="' + nullValueChange(v.cusMail) + '" id="cusMail">';
			str += '	<span class="ml-1 mr-1">@</span>';
			str += '	<input type="text" class="form-control mr-1" value="' + nullValueChange(v.mail) + '" id="mailDomain' + k + '">';
			str += '	<select class="form-control" id="mailGb' + k + '" name="mailGb" onchange="fn_mailDomainChange(this.value, ' + k + ');"></select>';
			str += '</td>';
			//str += '<td>' + v.cusName + '</td>';
			str += '</tr>';
		});
	} else {
		str += '<tr>';
		str += '<td colspan="3">[조회된 내역이 없습니다.]</td>';
		str += '</tr>';
	}
	$('#' + obj).html(str);
	
	fn_mailDomain('mailGb');
}

// 이메일 도메인 적용
function fn_mailDomain(obj) {
	var selectBox = $("select[name='mailGb']");
	selectBox.map(function(i) {
		var str = '';
		
		str += '<option value="">직접입력</option>';
		str += '<option value="naver.com">naver.com</option>';
		str += '<option value="nate.com">nate.com</option>';
		str += '<option value="yahoo.com">yahoo.com</option>';
		str += '<option value="gmail.com">gmail.com</option>';
		str += '<option value="hanmail.net">hanmail.net</option>';
		str += '<option value="daum.net">daum.net</option>';

		$('#' + obj + i).html(str);
		
		if($('#mailDomain' + i).val() != $('#mailGb' + i).val()) {
			$('#mailDomain' + i).attr('disabled', true);
			$('#mailGb' + i).val($('#mailDomain' + i).val());
			if($('#mailGb' + i).val() == null){
				$('#mailDomain' + i).attr('disabled', false);
				$('#mailGb' + i).val('');
			}
		}
	});
}

// 도메인 선택시 변경
function fn_mailDomainChange(val, idx) {
	if(val == '') { // 직접입력
		$('#mailDomain' + idx).val(val);
		$('#mailDomain' + idx).attr('disabled', false);
	} else {
		$('#mailDomain' + idx).val(val);
		$('#mailDomain' + idx).attr('disabled', true);
	}
}

// 안내문구 변경
function fn_billNameCh(val) {
	if(val == null || val == '' || val == 'undefined') {
		val = '01';
	}
	
	var url = "/org/notiMgmt/billName";
	var param = {
			billGubn : val
	};
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(param),
		success: function(data){
			if(data.map != null) {
				$('#billName').text(basicUnEscape(data.map.billName));
			} else {
				$('#billName').text('');
			}
		}
	});
}

// 이메일 발송
function fn_emailSend() {
	
	var idx = 0;
	var idxList = [];
	var strList = [];
	var selectBox = $("select[name='mailGb']");
	selectBox.map(function(i) {
		var tr = selectBox.parent().parent().eq(i);
		var mailVal = tr.find('input:text').eq(0).val() + '@' + tr.find('input:text').eq(1).val();
		if(!fn_emailCheck(mailVal)) {
			idx++;
		}
		
		idxList.push(tr.find(':hidden').val());
		strList.push(mailVal);
    });

	if(idx > 0) {
		swal({
	       type: 'info',
	       text: "E-MAIL 형식이 올바르지 않습니다.",
	       confirmButtonColor: '#3085d6',
	       confirmButtonText: '확인'
		});
		return;
	}
	
	if($('#billName').text() == '') {
		swal({
		       type: 'info',
		       text: "안내문구를 선택해주세요.",
		       confirmButtonColor: '#3085d6',
		       confirmButtonText: '확인'
			});
			return;
	}
	
	swal({
        type: 'question',
        html: "E-MAIL을 전송 하시겠습니까?",
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
	}).then(function(result) { 
		if (result.value) { 
			var url = "/org/notiMgmt/emailMsgSend";
			var param = {
					ecareNo	: '100', 	// 고지
					idxList : idxList,
					strList : strList,
					billGubn : $('#billGubn option:selected').val()
			};
			$.ajax({
				type : "post",
				async : true,
				url : url,
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(param),
				success: function(data){
					swal({
			    	   type: 'question',
			    	   html: "고지서 발송이 완료되었습니다. <p>변경된 E-MAIL 주소를 고객정보에 업데이트 하시겠습니까?",
			           showCancelButton: true,
			           confirmButtonColor: '#3085d6',
			           cancelButtonColor: '#d33',
			           confirmButtonText: '확인',
			           cancelButtonText: '취소',
			           reverseButtons: true
					}).then(function(result) { 
						if (result.value) { 
							var url = "/org/notiMgmt/emailUpdate";
							var param = {
									idxList : idxList,
									strList : strList
							};
							$.ajax({
								type : "post",
								async : true,
								url : url,
								contentType : "application/json; charset=utf-8",
								data : JSON.stringify(param),
								success: function(data){
									swal({
								       type: 'success',
								       text: "수정하였습니다.",
								       confirmButtonColor: '#3085d6',
								       confirmButtonText: '확인'
									}).then(function() {
										$('#email-notification').modal('hide');
										fnSearch();
							        });
								}, error: function(data){
									swal({
								       type: 'error',
								       text: "수정 실패하였습니다.",
								       confirmButtonColor: '#3085d6',
								       confirmButtonText: '확인'
									}).then(function() {
										$('#email-notification').modal('hide');
										fnSearch();
							        });
								}
							});
						}else{
							$('#email-notification').modal('hide');
							fnSearch();
						}
					});
				},
				error: function(data){
					swal({
				       type: 'error',
				       text: "전송 실패하였습니다.",
				       confirmButtonColor: '#3085d6',
				       confirmButtonText: '확인'
					});
				}
			});
		}
	});
}

// 이메일 정규식
function fn_emailCheck(email) {
    
    var regex=/([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    return (email != '' && email != 'undefined' && regex.test(email));
 
}

// 이메일 발송이력 조회
var ePage = 1; // 청구목록 현재페이지
function fn_selEmail(page) {
	if(page == null || page == 'undefined'){
		ePage = '1';
	} else {
		ePage = page;
	}
	
	var stDt = $('#mailStartDt').val().replace(/\./gi,"");
	var edDt = $('#mailEndDt').val().replace(/\./gi,"");
	
	if((!stDt || !edDt) || (stDt.length < 8 || edDt.length < 8) || (stDt > edDt)) {
		swal({
	       type: 'info',
	       text: "발송요청일을 확인해 주세요.",
	       confirmButtonColor: '#3085d6',
	       confirmButtonText: '확인'
		});
		return;
	}
	var url = "/org/notiMgmt/sendEmailList";
	var param = {
			curPage	 : ePage,
			stDt  	 : stDt,
			edDt 	 : edDt,
			selGubn  : $('#selMailGubn option:selected').val(),
			keyword  : $('#selKeyword').val(),
			search_orderBy : $('#searchOrderByEm option:selected').val()
	};
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(param),
		success: function(data){
			fn_selMailGrid(data, 'sendMailGrid');
			ajaxModalPaging(data, 'ModalMailPageArea'); //modal paging
		}
	});
}

function fn_selMailGrid(data, obj) {
	var str = '';
	if(data.count > 0) {
		$.each(data.list, function(k, v){
			str += '<tr>';
			str += '<td>' + v.reqDate + '</td>';
			str += '<td>' + v.cusName + '</td>';
			str += '<td>' + v.email + '</td>';
			str += '<td>' + v.billCont + '</td>';
			if(v.status == "E") {
				str += '<td>발송성공</td>';
			} else {
				str += '<td>발송대기</td>';
			}
			str += '</tr>';
		});
	} else {
		str += '<tr>';
		str += '<td colspan="5">[조회된 내역이 없습니다.]</td>';
		str += '</tr>';
	}
	$('#' + obj).html(str);
}

</script>

<div class="modal fade" id="email-notification" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">E-MAIL고지</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid  mb-4">
                    <div class="row">
                        <div class="col">
                            <div class="tab-selecter type-3">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#sending-email">발송</a></li>
                                    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#list-email-sent" onclick="fn_selEmail();">발송내역</a></li>
                                </ul>
                            </div>

                            <div class="tab-content" class="container-fluid">
                                <div id="sending-email" class="tab-pane fade show active">
	                                <div class="search-box">
		                                <div class="form-group row">
		                                    <label class="col col-md-2 col-sm-3 col-3 col-form-label">
	                                        	안내문구선택
		                                    </label>
		                                    <div class="col col-md-2 col-sm-7 col-7 form-inline">
		                                        <select class="form-control col" id="billGubn" onchange="fn_billNameCh(this.value);">
		                                            <option value="01">안내문구1</option>
		                                            <option value="02">안내문구2</option>
		                                            <option value="03">안내문구3</option>
		                                        </select>
		                                    </div>
		                                    <div class="col col-md-6 col-sm-11 col-11 form-inline" style="border: 1px solid #dedede; background: #fff;">
		                                    	<span id="billName"></span>
		                                    </div>
		                                </div>
	                                </div>
                                    <div class="row mt-3 mb-1">
                                        <div class="col-12">
                                            <span class="amount mr-2">조회결과 [총 <em class="font-blue" id="mailCnt"></em>건]</span>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col">
                                            <table class="table table-primary">
                                                <colgroup>
                                                    <col width="200">
                                                    <col width="610">
                                                </colgroup>
                                                <thead>
                                                    <tr>
                                                        <th>고객명</th>
                                                        <th class="border-b-n">이메일</th>
                                                    </tr>
                                                </thead>
                                                <tbody id="cusMailList">
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>

                                    <div class="row mt-3">
                                        <div class="col text-center">
                                            <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                                            <button type="button" class="btn btn-primary" onclick="fn_emailSend();">발송</button>
                                        </div>
                                    </div>
                                </div>

                                <div id="list-email-sent" class="tab-pane fade show">
                                    <div class="row mt-3 mb-1">
                                        <div class="col-12">
                                            <div class="search-box">
                                                    <div class="form-group row">
                                                        <label class="col col-md-2 col-sm-3 col-3 col-form-label">
                                                            	발송요청일
                                                        </label>
                                                        <div class="col col-md-4 col-sm-9 col-9 form-inline">
                                                            <div class="date-input">
                                                                <label class="sr-only" for="inlineFormInputGroupUsername">From</label>
                                                                <div class="input-group">
                                                                    <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="mailStartDt"
                                                                    	   aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                                                </div>
                                                            </div>
                                                            <span class="range-mark"> ~ </span>
                                                            <div class="date-input">
                                                                <label class="sr-only" for="inlineFormInputGroupUsername">To</label>
                                                                <div class="input-group">
                                                                    <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="mailEndDt"
                                                                    	   aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">
                                                            	검색구분
                                                        </label>
                                                        <div class="col col-md-5 col-sm-10 col-10 form-inline">
                                                            <select class="form-control col-auto" id="selMailGubn">
                                                                <option value="">전체</option>
                                                                <option value="cusName">고객명</option>
                                                                <option value="email">E-MAIL주소</option>
                                                            </select>
                                                            <input class="form-control col-auto" type="text" id="selKeyword">
                                                        </div>
                                                    </div>
                                                    <div class="row form-inline mt-3">
                                                        <div class="col-12 text-center">
                                                            <button type="button" class="btn btn-primary btn-wide" onclick="fn_selEmail();">
                                                               	조회
                                                            </button>
                                                        </div>
                                                    </div>
                                            </div>
											<div class="table-option row mb-2">
												<div class="col-md-6 col-sm-12 form-inline"></div>
												<div class="col-md-6 col-sm-12 text-right mt-1">
													<select class="form-control" name="searchOrderByEm"
														id="searchOrderByEm" onchange="fn_selEmail();">
														<option value="cusName">고객명순 정렬</option>
														<option value="reqDt">발송요청일순 정렬</option>
													</select>
												</div>
											</div>
										</div>
                                    </div>
                                    <div class="row">
                                        <div class="col table-responsive pd-n-mg-o">
                                            <table class="table table-primary">
                                                <colgroup>
                                                    <col width="120">
                                                    <col width="100">
                                                    <col width="160">
                                                    <col width="330">
                                                    <col width="100">
                                                </colgroup>
                                                <thead>
                                                    <tr>
                                                        <th>발송요청일</th>
                                                        <th>고객명</th>
                                                        <th>E-MAIL주소</th>
                                                        <th>제목</th>
                                                        <th>발송결과</th>
                                                    </tr>
                                                </thead>
                                                <tbody id="sendMailGrid">
                                                	<tr>
                                                		<td colspan="5">[조회된 내역이 없습니다.]</td>
                                                	</tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col text-center">
											<nav aria-label="Page navigation example">
												<ul class="pagination justify-content-center" id="ModalMailPageArea">
												</ul>
                                            </nav>
                                        </div>
                                    </div>
                                        <div class="col text-center">
                                            <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
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
