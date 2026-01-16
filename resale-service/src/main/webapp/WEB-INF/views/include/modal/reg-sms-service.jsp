<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">

function fn_smsServiceInsPage() {
	
	var url = "/org/notiMgmt/smsCertificate";
	var param = {
	};

	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(param),
		success: function(data){
			$('#chaName').text(data.map.chaName);
			$('#ownerTel').text(data.map.ownerTel);
			$('#fromNumber').val('');
			$("#fileNm").val('');
			$("#file-upload").val('');
			$("#fileno").val('0');
		}
	});
	
	$('#reg-sms-service').modal({backdrop:'static', keyboard:false});
}

function fn_smsServiceInsert() {
	var file = $("#fileNm").val();
	var fileext = file.substring(file.lastIndexOf(".") + 1, file.length);
	var fileSize;
	if(file != null && file != ""){
		fileSize = document.getElementById("file-upload").files[0].size;
	}
	var maxSize = 2 * 1024 * 1024 //2MB
// 	var check = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
// 	if(check.test(file)) {
// 		swal({
// 			type: 'info',
//             text: "파일명은 영문/숫자로만 입력가능합니다.",
//             confirmButtonColor: '#3085d6',
//             confirmButtonText: '확인'
// 		});
// 		return;
// 	}
	if(file == "" || file == null) {
		swal({
			type: 'info',
            text: "통신서비스 이용증명원 파일을 등록해 주세요!",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
		});
		return;
	}
	if(!fn_checkFileType(file)) {
		swal({
			type: 'info',
            text: "pdf, jpg, gif,png,tif 파일만 업로드 가능합니다!",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
		});
		return;
	} 
	if (fileSize > maxSize) {
		swal({
			type : 'info',
			text : ' 2MB 이하의 파일만 업로드 할 수 있습니다.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		});
		return;
	}
	if($("#fileno").val() == "0"){
	var regExp = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})?[0-9]{3,4}?[0-9]{4}$/;
	var idx = 0;
	var tEr = 0;	
	var tId = 0;
	var strList = [];
	var number = $("input[id='fromNumber']");
	number.each(function(i) {
		if($(this).val() != null && $(this).val() != '') {
			idx++;
			var tel = $(this).val();
			if((tel.substr(0, 2) == '15' || tel.substr(0, 2) == '16') && tel.length != 8) {
				tEr++;
			}
			if(!regExp.test(tel) && idx > 0){
				tId++;
			}
			strList.push(tel);
		}
	});
	
	if(idx == 0) {
		swal({
			type : 'info',
			text : '추가 등록 신청 전화번호를 입력해 주세요.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		});
		return;
	}
	if(tEr > 0) {
		swal({
			type: 'info',
            text: "추가 등록 신청 전화번호는 8자리까지 입력가능합니다.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
		});
		return;
	}
	if(tId > 0) {
		swal({
			type : 'info',
			text : '추가 등록 신청 전화번호 형식이 올바르지 않습니다.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		});
		return;
	}
	
	}
		swal({
			type : 'question',
			html : "통신서비스 이용증명원을 등록 하시겠습니까?",
			showCancelButton : true,
			confirmButtonColor : '#3085d6',
			cancelButtonColor : '#d33',
			confirmButtonText : '확인',
			cancelButtonText : '취소'
		}).then(function(result) {
			if (result.value) {
				var url = "/org/notiMgmt/smsCertificateIns";
				var formData = new FormData();
		 			formData.append("file", $('#file-upload')[0].files[0]);
		 			formData.append("fileSize", document.getElementById('file-upload').files[0].size);
		 			formData.append("fileno", $('#fileno').val());
		 			formData.append("regChacd", $('#regChacd').val());
		 			if($("#fileno").val() == "0"){
		 			formData.append("strList", strList);
		 			}
				if($("#fileno").val() == "0"){
	 			var param = {
					formData : formData
			 	};
				}else{
					var param = {
					formData : formData,
					strList  : strList
					};
				}
	
				$.ajax({
					type: "post",
	 				url: url,
	 				data: formData,
	 				dataType: "text",
	 				processData: false,
	 				contentType: false,
					success : function(data) {
						swal({
					        type: 'success',
					        text: "문자메시지 서비스 이용 등록 신청이 완료되었습니다.",
					        confirmButtonColor: '#3085d6',
					        confirmButtonText: '확인'
		   	 			}).then(function(result) {
		   	 			location.reload();
		   	 			});
					},
					error: function(data) {
						swal({
	    	 		        type: 'error',
	    	 		        text: "문자메시지 서비스 이용 등록 신청이 실패되었습니다.",
	    	 		        confirmButtonColor: '#3085d6',
	    	 		        confirmButtonText: '확인'
	    	 			})
					}
				});
			}
		});
}

function fn_telNoCheck(tel) {
	if((tel.substr(0, 2) == '15' || tel.substr(0, 2) == '16') && tel.length > 8) {
		swal({
			type: 'info',
            text: "추가 등록 신청 전화번호는 8자리까지 입력가능합니다.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
		});
		return;
	} 
	if((tel.substr(0, 3) == '030' || tel.substr(0, 3) == '050') && tel.length > 12) {
		swal({
			type: 'info',
            text: "추가 등록 신청 전화번호는 12자리까지 입력가능합니다.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
		});
		return;
	} 
	if((tel.substr(0, 3) != '030' && tel.substr(0, 3) != '050' && tel.substr(0, 2) != '15' && tel.substr(0, 2) != '16') && tel.length > 11) {
		swal({
			type: 'info',
            text: "추가 등록 신청 전화번호는 11자리까지 입력가능합니다.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
		});
		return;
	}
}

function fn_checkFileType(filePath) {
	var fileFormat = filePath.split(".");
	if(fileFormat.indexOf("pdf") > -1) {
		return true;
	} else if(fileFormat.indexOf("jpg") > -1) {
		return true;
	} else if(fileFormat.indexOf("gif") > -1) {
		return true;
	} else if(fileFormat.indexOf("png") > -1) {
		return true;
	} else if(fileFormat.indexOf("tif") > -1) {
		return true;
	} else {
		return false;
	}
}

</script>

<div class="modal fade" id="reg-sms-service" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">문자메시지 서비스 이용등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div id="page-description" class="container-fluid">
                    <div class="row">
                        <div class="col-12">
                            <p>발신번호 사전등록 시행에 따라 불법 스팸 문자 발송 방지를 위해 휴대폰 문자 발송 시에 발신번호를 사전에 인증하여 등록하여야 합니다.
                            통신서비스 이용증명원 제출하여 확인 후 서비스 이용이 가능합니다.</p>
                        </div>
                    </div>
                </div>
                <div class="container-fluid mb-4">
                    <div class="row">
                        <div class="col">
                        <form id="smsForm" enctype="multipart/form-data" method="post">
                        <input type="hidden" id="fileno" name="fileno" value="">
                        <input type="hidden" id="regChacd" name="regChacd" value="">
                            <table class="table table-form table-primary">
                                <tbody class="container-fluid">
                                    <tr class="row col">
                                        <th class="col col-3">가맹점</th>
                                        <td class="col col-9">
                                        	<span id="chaName"></span>
                                        </td>
                                    </tr>
                                    <tr class="row col">
                                        <th class="col col-3">대표번호</th>
                                        <td class="col col-9">
                                        	<span id="ownerTel"></span>
                                        </td>
                                    </tr>
                                    <tr class="row col" id="addNum">
                                        <th class="col col-3">추가 등록 신청 전화번호</th>
                                        <td class="col col-9 align-items-start" style="flex-direction:column">
                                            <div class="form-inline mb-1 w-100">
                                                <span class="mr-1">1.</span><input type="text" class="form-control w-75" id="fromNumber" onkeydown="onlyNumber(this)" maxlength="12">
                                            </div>
                                            <div class="form-inline mb-1 w-100">
                                                <span class="mr-1">2.</span><input type="text" class="form-control w-75" id="fromNumber" onkeydown="onlyNumber(this)" maxlength="12">
                                            </div>
                                            <div class="form-inline mb-1 w-100">
                                                <span class="mr-1">3.</span><input type="text" class="form-control w-75" id="fromNumber" onkeydown="onlyNumber(this)" maxlength="12">
                                            </div>
                                            <div class="form-inline mb-1 w-100">
                                                <span class="mr-1">4.</span><input type="text" class="form-control w-75" id="fromNumber" onkeydown="onlyNumber(this)" maxlength="12">
                                            </div>
                                            <div class="form-inline w-100">
                                                <span class="mr-1">5.</span><input type="text" class="form-control w-75" id="fromNumber" onkeydown="onlyNumber(this)" maxlength="12">
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="row col">
                                        <th class="col col-3">통신서비스 이용증명원</th>
                                        <td class="col col-9 filebox">
                                            <input type="text" class="form-control w-50" disabled="disabled" id="fileNm">
                                            <input type="file" id="file-upload" name="file-upload" class="hidden" onchange="document.getElementById('fileNm').value = this.value.replace('C:\\fakepath\\', '')">
                                            <label for="file-upload" class="btn btn-sm btn-d-gray ml-1">
                                               	파일등록
                                            </label>
                                            <span style="width: 100%" class="guide-mention ml-3">(pdf, jpg, gif,png,tif 등록 가능)</span>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            </form>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                            <h6 class="mt-2">광고성 문자표기 의무사항</h6>
                            <div class="guide-mention">
                                - 전체 발신번호수가 8~11자리인 경우 허용<br>
                                - 15XX, 16XX 등 대표번호서비스인 경우, 전체 번호수가 8자리만 허용<br>
                                - 030, 050으로 시작하는 경우, 전체 번호수가 12자리까지 허용
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container-fluid mb-4">
                    <div class="row">
                        <div class="col-12 text-center">
                            <button class="btn btn-primary btn-outlined btn-wide" data-dismiss="modal">
                               	취소
                            </button>
                            <button class="btn btn-primary btn-wide" onclick="fn_smsServiceInsert();">
                               	등록
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
