<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    
// 청구년도    
function fn_cancelMonth() {
	var url = "/org/claimMgmt/beforeMasYear";
	var param = {
	};
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify(param),
		success: function(data){
			fn_masYear(data, 'cancelYear');
			$("#cancelYear").val($("#claimYear").val()); // 청구조회의 조회값을 넣어줌
			fn_monthSearch();
		}
	});
}  

// 청구월
function fn_monthSearch() {
	var url = "/org/claimMgmt/beforeMasMonth";
	var param = {
			year : $('#cancelYear option:selected').val()
	};
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify(param),
		success: function(data){
			fn_masMonth(data, 'cancelMonth');
			$("#cancelMonth").val($("#claimMonth").val());
		}
	});
}

function fn_monthSel() {
	var url = "/org/claimMgmt/beforeMasMonth";
	var param = {
			year : $('#cancelYear option:selected').val()
	};
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify(param),
		success: function(data){
			fn_masMonth(data, 'cancelMonth');
		}
	});
}

function fn_masYear(data, obj) {
	var str = '<option value="">선택</option>';
	$.each(data.list, function(k, v){

		str += '<option value="' + v.year + '">' + v.year + '</option>';
	});
	$('#' + obj).html(str);
}

function fn_masMonth(data, obj) {
	var str = '<option value="">선택</option>';
	$.each(data.list, function(k, v){
		str += '<option value="' + v.month + '">' + v.month + '</option>';
	});
	$('#' + obj).html(str);
}

function fn_blockCancel() {
	
	if(!$('#cancelYear').val() || !$('#cancelMonth').val()) {
		swal({
	           type: 'info',
	           text: '취소할 청구월을 확인해 주세요.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       });
		return;
	}
	
	swal({
        type: 'question',
        html: "일괄 취소하시겠습니까?",  
        content: "input",
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
	}).then(function(result) {
		if (result.value) { 
			var url = "/org/claimMgmt/claimAllCancel";
			var param = {
					masMonth : $('#cancelYear').val() + $('#cancelMonth').val()
			};
			$.ajax({
				type : "post",
				async : true,
				url : url,
				contentType : "application/json; charset=UTF-8",
				data : JSON.stringify(param),
				success: function(result){
					swal({
			           type: 'success',
			           html: '납부된 청구건을 제외하고 일괄취소되었습니다.',
			           confirmButtonColor: '#3085d6',
			           confirmButtonText: '확인'
					  }).then(function(result) { 
					  if (result.value) {  
						  location.href = "org/claimMgmt/claimSel";
					  } 
				   });
				},
				error:function(result){
					swal({
			           type: 'error',
			           text: '취소 실패하였습니다.',
			           confirmButtonColor: '#3085d6',
			           confirmButtonText: '확인'
			       })
				}
			});
		}
	});
}    
    
</script>   
    
<div class="modal fade" id="block-cancel" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">일괄취소</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">

                <div class="container-fluid">
                    <div id="page-description">
                        <div class="row">
                            <div class="col-12">
                                <p><font color="red">* 한번 취소하신 청구정보는 복구하실 수 없으니 해당 기능 이용시 유의하시기 바랍니다.</font></p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container">
                    <div class="search-box">
                            <div class="row mb-1">
                                <div class="col col-12 form-inline">
                                    <select class="form-control" id="cancelYear" onchange="fn_monthSel();">
                                    	<option value="">선택</option>
                                    </select>
                                    <span class="ml-1 mr-1">년</span>

                                    <select class="form-control" id="cancelMonth">
                                        <option value="">선택</option>
                                    </select>
                                    <span class="ml-1 mr-auto">월 청구데이터를 일괄취소 하겠습니다.</span>
                                </div>
                            </div>

                            <div class="row form-inline mt-3">
                                <div class="col-12 text-center">
                                    <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                                    <button class="btn btn-primary" onclick="fn_blockCancel();">실행</button>
                                </div>
                            </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
    