<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
$(document).ready(function() {
	$('.popover-dismiss').popover({
		trigger: 'focus'
	});
	$(":input[name=sinAmt]").change(function(e){
		var num01;
	    var num02;
	    num01 = $(this).val();
	    num02 = num01.replace(/\D/g,""); //숫자가 아닌것을 제거, //즉 [0-9]를 제외한 문자 제거; /[^0-9]/g 와 같은 표현
	    num01 = addThousandSeparatorCommas(num02); //콤마 찍기
	    $(this).val(num01);
	});

	$('#sinRemark').on("keydown", function(e) {
		var id = $(this).attr('id');
		var cutstr ='';
		var limit = 360;
		var maxLine = $(this).attr("rows");
		var text = $(this).val();
	    var textlength = text.length;
		var line = text.split("\n").length;
		var _byte = 0;
		var lines = text.split(/(\r\n|\n|\r)/gm);
		var lineMaxLength = 60;

		for(var idx = 0; idx < text.length; idx++){
			var oneChar = escape(text.charAt(idx));
			if(oneChar.length == 1){
				_byte ++;
			}else if(oneChar.indexOf("%u") != -1){ // 한글~
				_byte +=2;
			}else if(oneChar.indexOf("%") != -1) {
				_byte ++;
			}

			if (_byte <= limit) {
				textlength = idx + 1;
			}
		}

	    for(var i=0; i<lines.length; i++){
	    	if(lines[i].length > lineMaxLength){
	    	    var text = $(this).val();
	    	    var lines = text.split(/(\r\n|\n|\r)/gm);
	    	    for (var i = 0; i < lines.length; i++) {
	    	        if (lines[i].length > lineMaxLength) {
	    	            lines[i] = lines[i].substring(0, lineMaxLength);
	    	        }
	    	    }
	    	    	swal({
	    	    		type : 'warning',
	    	    		text : '한줄에 '+lineMaxLength+'자 이하 최대 3줄, 최대 360자 까지만 작성할 수 있습니다.',
	    	    		confirmButtonColor : '#3085d6',
	    	    		confirmButtonText : '확인'
	    	    	})
	    	    $(this).val(lines.join(''));
	    	    $(this).blur();
	    	}
	    }

		if(line > maxLine){
			if($("#linelimitAlert").val() == "Y"){
				swal({
					type : 'warning',
					text : '추가안내 사항은 '+ maxLine + '줄까지만 입력가능합니다.',
					confirmButtonColor : '#3085d6',
					confirmButtonText : '확인'
				})
				$("#linelimitAlert").val("N");
			}
		}else{
			$("#linelimitAlert").val("Y");
		}
		if(_byte > limit){
					cutstr = $("#"+id+"").val().substr(text, textlength);
					$("#"+id+"").val(cutstr);
					$(".textarea-byte-counter > em").html(_byte);
					limitSinRemarkCharacters(id);
		}else{
			$(".textarea-byte-counter > em").html(_byte);
		}

		if(event.keyCode == 13 || event.keyCode == 176){
			limitSinRemarkCharacters(id);
		}
		if(event.keyCode == 17 || event.keyCode == 86){
			limitSinRemarkCharacters(id);
		}
	});

});

function limitSinRemarkCharacters(id){
	var limit = 360;
	var maxLine = 3;
	var lineMaxLength = 60;
	var lineMaxByte = 120;

	var text = $("#"+id+"").val();
    var textlength = text.length;
	var line = text.split("\n").length;
	var lines = text.split("\n");

	var valueCheck = true;
	var _byte = 0;
	var billcon = '추가 안내사항';

	for(var idx = 0; idx < text.length; idx++){
		var oneChar = escape(text.charAt(idx));
		if(oneChar.length == 1){
			_byte ++;
		}else if(oneChar.indexOf("%u") != -1){ // 한글~
			_byte +=2;
		}else if(oneChar.indexOf("%") != -1) {
			_byte ++;
		}
	}
	if(line > maxLine){
		swal({
			type : 'warning',
			text : '추가 안내사항은 '+ maxLine + '줄까지만 입력가능합니다.'+'\n'+billcon+' 을 수정해주세요.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		})
		modifiedText = $("#"+id+"").val().split("\n").slice(0, maxLine);
		$("#"+id+"").val(modifiedText.join("\n"));
		valueCheck = false;
		return false;
	}
    if(_byte > limit) {
    	$('#'+id+'').focusout();
    	swal({
    		type : 'warning',
    		text : '입력가능 용량  초과하였습니다. ('+_byte+'/'+limit+')'+'\n'+billcon+' 을 수정해주세요.',
    		confirmButtonColor : '#3085d6',
    		confirmButtonText : '확인'
    	})
        valueCheck = false;
    }
    var k = 0;
    var tooLong = false;
    for(var i=0; i<lines.length; i++){
    	if(lines[i].length > lineMaxLength){
    		tooLong = true;
    		k = i + 1;
    	}
    }
    if(tooLong == true){
    	swal({
    		type : 'warning',
    		text : '한줄에 '+lineMaxLength+'자 미만, 최대 180자 까지만 작성할 수 있습니다. '+'\n'+billcon+' 의 '+k+' 열을 수정해주세요.',
    		confirmButtonColor : '#3085d6',
    		confirmButtonText : '확인'
    	})
    	valueCheck = false;
    }
    $(".textarea-byte-counter > em").html(_byte);
	return valueCheck;
}

function getNumber(obj) {
	var num01;
    var num02;
    num01 = obj.value;
    num02 = num01.replace(/\D/g,""); //숫자가 아닌것을 제거, //즉 [0-9]를 제외한 문자 제거; /[^0-9]/g 와 같은 표현
    num01 = setComma(num02); //콤마 찍기
    obj.value =  num01;
}
function setComma(n) {
	 var reg = /(^[+-]?\d+)(\d{3})/;   // 정규식
     n += '';                          // 숫자를 문자열로 변환
     while (reg.test(n)) {
        n = n.replace(reg, '$1' + ',' + '$2');
     }
     return n;
}

function fn_regCusNameSearch(num) {
	moPage = '';
	if(num == null || num == 'undefined'){
		moPage = '1';
	} else {
		moPage = num;
	}

	var gubun;
	if((!$('#inSelGubnNew').is(':checked') && !$('#inSelGubnOld').is(':checked'))) {
		$('#inSelGubnNew').attr('checked', true);
	}
	if($('#inSelGubnNew').is(':checked')) {
		gubun = "new";
	} else {
		gubun = "old";
	}

	var url = "/org/claimMgmt/searchCusName";
	var param = {
			tbgubn   : gubun,					//조회구분
			selCusCk : $('#inSelect option:selected').val(),
			cusName  : $('#inCusName').val(),
			curPage  : moPage
	};
	$.ajax({
		type: "post",
		url: url,
		data: param,
		success: function(data){
			fnCusNameGrid(data, 'cusNameBody');
			ajaxModalPaging(data, 'ModalPageArea'); //modal paging
		}
	});
}

function fnCusNameGrid(result, obj) {
	$('#cusCnt').text(result.count);
	var str = '';
	var col = 3 + result.cusGbCnt;
	if(result.count <= 0){
		str += '<tr><td colspan="'+ col +'" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
	}else{
		$.each(result.list, function(k, v){
			str += '<tr>';
			str +=  '<td><a href="#" onclick="fn_selCusName(\'' + v.vano + '\');">'+ basicEscape(nullValueChange(v.cusName)) +'</a></td>';
			if(result.cusGbCnt == 1 || result.cusGbCnt > 1) {
				str += '<td>'+ basicEscape(nullValueChange(v.cusGubn1)) +'</td>';
			}
			if(result.cusGbCnt == 2 || result.cusGbCnt > 2) {
				str += '<td>'+ basicEscape(nullValueChange(v.cusGubn2)) +'</td>';
			}
			if(result.cusGbCnt == 3 || result.cusGbCnt > 3) {
				str += '<td>'+ basicEscape(nullValueChange(v.cusGubn3)) +'</td>';
			}
			if(result.cusGbCnt == 4 || result.cusGbCnt > 4) {
				str += '<td>'+ basicEscape(nullValueChange(v.cusGubn4)) +'</td>';
			}
			str +=  '<td>'+ nullValueChange(v.cusHp) +'</td>';
			str +=  '<td>'+ nullValueChange(v.vano) +'</td>';
			str += '</tr>';
		});
	}
	$('#' + obj).html(str);
}

function  fn_selCusName(vano) {
	fn_sinComboRefresh(); // 청구항목 초기화
    $("#cusGubn1").attr("type", "hidden");
    $("#cusGubn2").attr("type", "hidden");
    $("#cusGubn3").attr("type", "hidden");
    $("#cusGubn4").attr("type", "hidden");
    $("#cusGubnRow1").hide();
    $("#cusGubnRow2").hide();
	var url = "/org/claimMgmt/cusNameInfo";
	var param = {
			vano : vano
	};
	$.ajax({
		type: "post",
		url: url,
		data: param,
		success: function(data){
			$('#sinCusName').val(basicEscape(data.dto.cusName));
			$('#sinCusKey').text(data.dto.cusKey);
			$('#sinCusHp').text(data.dto.cusHp);
			$('#sinCusVano').text(data.dto.vano);
            $.each(data.cusGbList, function(k, v){
                $("#cusGubnCd"+(k+1)+"").text(basicEscape(nullValueChange(v.codeName)));
            });
            if(data.cusGbCnt == 1 || data.cusGbCnt > 1) {
                $("#cusGubnRow1").show();
                $("#cusGubn1").attr("type", "text");
                $("#cusGubn1").val(basicEscape(nullValueChange(data.dto.cusGubn1)));
            }
            if(data.cusGbCnt == 2 || data.cusGbCnt > 2) {
                $("#cusGubn2").attr("type", "text");
                $("#cusGubn2").val(basicEscape(nullValueChange(data.dto.cusGubn2)));
            }
            if(data.cusGbCnt == 3 || data.cusGbCnt > 3) {
                $("#cusGubnRow2").show();
                $("#cusGubn3").attr("type", "text");
                $("#cusGubn3").val(basicEscape(nullValueChange(data.dto.cusGubn3)));
            }
            if(data.cusGbCnt == 4 || data.cusGbCnt > 4) {
                $("#cusGubn4").attr("type", "text");
                $("#cusGubn4").val(basicEscape(nullValueChange(data.dto.cusGubn4)));
            }
			fn_itemCd(data, 'sinItem');
		}
	});
	$("#slide-page-1").css("display", "none");
    $("#btn-footer").css("display", "none");
    $("#slide-page-2").css("display", "block");
}

function fn_itemCd(data, obj) {

	for(var i = 1; i < 10; i++) {
		var str = '';

		str += '<option value="">선택</option>';
		$.each(data.claimItemCd, function(k, v){
			str += '<option value="' + v.code + '">' + v.codeName + '</option>';
		});
		$('#' + obj + i).html(str);
	}
}

function fn_singleInsert() {
	var stDt = $('#sinStartDate').val().replace(/\./gi,"");
	var edDt = $('#sinEndDate').val().replace(/\./gi,"");
	if((!stDt || !edDt) || (stDt.length < 8 || edDt.length < 8) || (stDt > edDt)) {
		swal({
           type: 'info',
           text: '납부기한을 확인해 주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	var nowDt = getCurrentDate();
	if (stDt < nowDt) {
		swal({
			type : 'info',
			text : '입금가능시작일은 금일부터 가능합니다.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		});
		return;
	}

	var printDt = $('#sinPrintDate').val().replace(/\./gi,"");
	if(printDt && ((stDt > printDt) || (printDt > edDt))) {
		swal({
			type : 'info',
			text : '고지서용 표시마감일은 납부기한보다 작거나 클 수 없습니다.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		});
		return;
	}

	var rowCnt 	= 0;
	var rowCk 	= 0;
	var rowAmt  = 0;
	var cdVals 	= [];
	var amtVals = [];
	var mVals 	= [];
	var selectBox = $('select[name=sinItem]');
	selectBox.each(function(i) {
		var tr = selectBox.parent().parent().eq(i);
		var td = tr.children();
		if($(this).val() != '') {
			if(rowCnt > 0 && cdVals.indexOf($(this).val()) != -1) { // 중복체크
				rowCk++;
			}
			cdVals.push($(this).val()); 									// 항목코드
			var amt = tr.children().find('input[name="sinAmt"]').val();
			if(amt == '' || amt == 0) {
				rowAmt++;
			}
			amtVals.push( removeCommas(amt) ); // 항목금액
			var remark = basicEscape(tr.children().find('input[name="sinEtc"]').val());
			mVals.push(remark); 	// 비고
			rowCnt++;
		}
	});

	if(rowCnt == 0) {
		swal({
           type: 'info',
           text: '청구항목은 최소 1개 이상 선택하셔야 합니다.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	if(rowCk != 0) {
		swal({
           type: 'info',
           text: '중복된 항목이 있습니다. 확인해주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}

	if(limitSinRemarkCharacters("sinRemark") == false){
		return false;
	}

	if(rowCk == 0) {
		var url = "/org/claimMgmt/singleAdd";
		var param = {
				vano 	  : $('#sinCusVano').text(),
				masMonth  : $('#sinYear').val() + $('#sinMonth').val(),
				masDay 	  : getCurrentDate(),
				startDate : $('#sinStartDate').val().replace(/\./gi,""),
				endDate   : $('#sinEndDate').val().replace(/\./gi,""),
				printDate : $('#sinPrintDate').val().replace(/\./gi,""),
				idxList   : cdVals,
				itemList  : amtVals,
				strList   : mVals,
				remark    : basicEscape($('#sinRemark').val()),
                cusName   : $('#sinCusName').val(),
                cusGubn1  : $("#cusGubn1").val(),
                cusGubn2  : $("#cusGubn2").val(),
                cusGubn3  : $("#cusGubn3").val(),
                cusGubn4  : $("#cusGubn4").val()
		};
		swal({
	        type: 'question',
	        html: "청구 대상을 추가 하시겠습니까?",
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: '확인',
	        cancelButtonText: '취소',
            reverseButtons: true
		}).then(function(result) {
			if (result.value) {
				$.ajax({
					type: "post",
					url: url,
					data: JSON.stringify(param),
					contentType : "application/json; charset=utf-8",
					success: function(result){

						var message = '';
						var title = '';
						if(result.itemCnt > 9){
							title = '실패하였습니다.';
							message = '청구항목은 최대 9개까지 등록가능합니다. 초과된 항목은 저장 실패되었습니다.';
						} else if(result.useCnt > 0) {
							title = '실패하였습니다.';
							message = '대상고객의 기 청구된 청구 항목이 있습니다.<p>중복건을 제외한 청구 항목의 등록이 완료되었습니다.';
						} else {
							title = '청구대상 목록에 추가되었습니다.';
							message = '목록 확인 후 반드시 하단의 청구등록 버튼을 눌러야 완료됩니다.';
						}

						swal({
					           type: 'success',
					           title: title,
					           html: message,
					           confirmButtonColor: '#3085d6',
					           confirmButtonText: '확인'
					        }).then(function(result) {
								$('#reg-single-charge').modal('hide');
					        	fn_ClaimListCall(1);
						    });
					},
					error:function(result){
						swal({
				           type: 'error',
				           html: '처리중 시스템 오류가 발생 하였습니다.<p>지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.',
				           confirmButtonColor: '#3085d6',
				           confirmButtonText: '확인'
				       }).then(function() {
				    	   fn_claimFailList('1'); // 대량등록 실패내역 조회
				       });
					}
				});
			}
	    });
	}

	fn_exit();
}

function fn_sinItemDel(idx) {
	if(!$('#sinItem' + idx).val()) {
		swal({
	           type: 'info',
	           text: '삭제할 항목이 없습니다.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	        });
			return;
	}
	$('#sinItem'+idx).val('');
	$('#sinAmt'+idx).val('');
	$('#sinEtc'+idx).val('');
}

function fn_sinComboRefresh() {
	for(var i = 1; i < 10; i++) {
		$('#sinItem'+i).val('');
		$('#sinAmt'+i).val('');
		$('#sinEtc'+i).val('');
		//$('#sinRemark').val('');
// 		$('#chargeDetCd'+i).val('');
	}
	$('#sinRemark').val('');
}

function fn_reSearch() {
	$('#inCusName').val('');
	fn_regCusNameSearch(1);
    $("#slide-page-1").css("display", "block");
    $("#btn-footer").css({"display": "block", "text-align": "center"});
    $("#slide-page-2").css("display", "none");
}

function fn_exit() {
    $("#slide-page-1").css("display", "block");
    $("#btn-footer").css({"display": "block", "text-align": "center"});
    $("#slide-page-2").css("display", "none");

    $("#inSelGubnNew").prop("checked", true);
    $('#inSelGubnOld').prop('checked', false);
}


</script>

<div class="modal fade" id="reg-single-charge" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">개별청구등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true" onclick="fn_exit();">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="swiper-container charge-single-payer-slider">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide" id="slide-page-1">
                        	<div class="container-fluid">
                        		<div id="page-description">
			                        <div class="row">
			                            <div class="col-12">
			                                <p>
												신규고객 : 최근 30일 내 등록한 고객을 의미합니다.<br/>
												전체고객 : 신규 고객 외 전체 고객을 의미합니다. (단, 삭제고객, 납부제외된 고객은 포함되지 않습니다.)<br/>
												고객명을 클릭하면 해당 고객의 개별청구등록 페이지로 이동합니다.
											</p>
			                            </div>
			                        </div>
			                    </div>
                        	</div>

                            <div class="container-fluid">
                                <div class="row no-gutters mt-3">
                                    <div class="col-6">
                                        <h5>고객검색</h5>
                                    </div>
                                </div>

                                <div class="row no-gutters mt-3">
                                    <table class="table table-form">
                                        <tbody class="container-fluid">
                                            <tr class="row no-gutters">
                                                <th class="col-md-2 col-sm-4 col-4">
													<span class="text-danger">*</span>고객설정
												</th>
                                                <td class="col-md-10 col-sm-8 col-8 form-inline">
                                                    <div class="form-check form-check-inline">
                                                        <input class="form-check-input" type="radio" id="inSelGubnNew" name="payer-classification-4" value="new">
                                                        <label for="inSelGubnNew"><span class="mr-2"></span>신규고객</label>
                                                    </div>
                                                    <div class="form-check form-check-inline">
                                                        <input class="form-check-input" type="radio" id="inSelGubnOld" name="payer-classification-4" value="old">
                                                        <label for="inSelGubnOld"><span class="mr-2"></span>전체고객</label>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr class="row no-gutters">
                                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>조회조건</th>
                                                <td class="col-md-10 col-sm-8 col-8">
                                                	<select class="form-control col-md-3" id="inSelect">
                                                		<option value="cusNm">고객명</option>
                                                		<option value="cusHp">연락처</option>
                                                	</select>
                                                    <input type="text" class="form-control col-md-8" id="inCusName" name="inCusName" onkeypress="if(event.keyCode == 13){fn_regCusNameSearch();}">
                                                    <button class="btn btn-sm btn-d-gray ml-1" onclick="fn_regCusNameSearch();">검색</button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="row no-gutters mt-3">
                                	<div class="table-responsive">
	                                	<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="cusCnt"></em>건]</span>
										<span class="amount font-blue mr-2">고객명 클릭하여 청구정보 등록하세요!</span>
	                                    <table id="search-result-customer" class="table table-striped table-bordered table-hover table-primary mt-3">
											<colgroup>
												<col width="89">
												<c:forEach var="cusGbList" items="${map.cusGbList}">
													<col width="115">
												</c:forEach>
												<col width="120">
												<col width="140">
											</colgroup>
	                                        <thead>
	                                            <tr>
	                                                <th class="border-l">고객명</th>
	                                                <c:forEach var="cusGbList" items="${map.cusGbList}">
														<th>${cusGbList.codeName}</th>
													</c:forEach>
	                                                <th>연락처</th>
	                                                <th>가상계좌</th>
	                                            </tr>
	                                        </thead>
	                                        <tbody id="cusNameBody">
	                                        <c:set var="colCnt" value="#{map.cusGbCnt + 3}"/>
	                                            <tr>
	                                                <td colspan="${colCnt}" class="text-center">
														[조회된 내역이 없습니다.]
													</td>
	                                            </tr>
	                                        </tbody>
	                                    </table>
                                    </div>
                                </div>
                            </div>
							<br>
							<nav aria-label="Page navigation example">
								<ul class="pagination justify-content-center" id="ModalPageArea">
								</ul>
							</nav>

                        </div>

                        <div class="swiper-slide" id="slide-page-2" style="display: none;">
                            <div class="container-fluid">
                                <div class="row no-gutters mt-3">
                                    <div class="col-6">
                                        <h5>고객기본정보</h5>
                                    </div>
                                    <div class="col-6 text-right">
                                        <span class="text-danger">* 필수입력</span>
                                    </div>
                                </div>

                                <div class="row no-gutters mt-3 mb-5">
                                    <table class="table table-form">
                                        <tbody class="container-fluid">
                                            <tr class="row no-gutters">
                                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>고객명</th>
                                                <td class="col-md-4 col-sm-8 col-8">
                                                    <input type="text" id="sinCusName" class="form-control">
                                                </td>
                                                <th class="col-md-2 col-sm-4 col-4">고객번호</th>
                                                <td class="col-md-4 col-sm-8 col-8">
                                                    <span id="sinCusKey"></span>
                                                </td>
                                            </tr>
                                            <tr class="row no-gutters">
                                                <th class="col-md-2 col-sm-4 col-4">연락처</th>
                                                <td class="col-md-4 col-sm-8 col-8 form-inline contact-number">
                                                    <span id="sinCusHp"></span>
                                                </td>
                                                <th class="col-md-2 col-sm-4 col-4">가상계좌</th>
                                                <td class="col-md-4 col-sm-8 col-8">
                                                    <span id="sinCusVano"></span>
                                                </td>
                                            </tr>
											<tr class="row no-gutters" id="cusGubnRow1">
												<th class="col-md-2 col-sm-4 col-4" ><span id="cusGubnCd1"></span></th>
												<td class="col-md-4 col-sm-8 col-8 form-inline">
													<input type="text" id="cusGubn1" class="form-control" style="width: 100%;">
												</td>
												<th class="col-md-2 col-sm-4 col-4" ><span id="cusGubnCd2"></span></th>
												<td class="col-md-4 col-sm-8 col-8">
													<input type="text" id="cusGubn2" class="form-control" style="width: 100%;">
												</td>
											</tr>
											<tr class="row no-gutters" id="cusGubnRow2">
												<th class="col-md-2 col-sm-4 col-4" ><span id="cusGubnCd3"></span></th>
												<td class="col-md-4 col-sm-8 col-8 form-inline">
													<input type="text" id="cusGubn3" class="form-control" style="width: 100%;">
												</td>
												<th class="col-md-2 col-sm-4 col-4" ><span id="cusGubnCd4"></span></th>
												<td class="col-md-4 col-sm-8 col-8">
													<input type="text" id="cusGubn4" class="form-control" style="width: 100%;">
												</td>
											</tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="container-fluid">
                                <div class="row no-gutters mt-4">
                                    <div class="col-6">
                                        <h5>청구정보</h5>
                                    </div>
                                    <div class="col-6 text-right">
                                    </div>
                                </div>
                                <div class="row no-gutters mt-3">
                                    <table class="table table-form">
                                        <tbody class="container-fluid">
                                            <tr class="row no-gutters">
                                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>청구월</th>
                                                <td class="col-md-4 col-sm-8 col-8 font-inline">
                                                    <select class="form-control col" id="sinYear">
                                                    </select>
                                                    <span class="ml-1 mr-1">년</span>
                                                    <select class="form-control col" id="sinMonth">
                                                    </select>
                                                    <span class="ml-1 mr-auto">월</span>
                                                </td>
                                                <th class="col-md-2 col-sm-4 col-4">청구일자</th>
                                                <td class="col-md-4 col-sm-8 col-8">
                                                	<div class="date-input">
                                                        <div class="input-group">
                                                        	<span id="sinMasDay"></span>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr class="row no-gutters">
                                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>납부기한</th>
                                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                                    <div class="date-input">
                                                        <div class="input-group">
                                                            <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="sinStartDate"
                                                            	   aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                                        </div>
                                                    </div>
                                                    <span class="range-mark"> ~ </span>
                                                    <div class="date-input">
                                                        <div class="input-group">
                                                            <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="sinEndDate"
                                                             	   aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                                        </div>
                                                    </div>
                                                </td>
                                                <th class="col-md-2 col-sm-4 col-4">고지서용<br/>표시마감일</th>
                                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                                    <div class="date-input">
                                                        <div class="input-group">
                                                            <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="sinPrintDate"
                                                            	   aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="row no-gutters mt-3">
                                	<div class="table-responsive">
	                                    <table class="table tabl-sm table-primary mt-3">
											<colgroup>
												<col width="60">
												<col width="200">
												<col width="200">
												<col width="220">
												<col width="100">
											</colgroup>

	                                        <thead>
	                                            <tr>
	                                                <th>NO</th>
	                                                <th><span class="text-danger">*</span>청구항목명</th>
	                                                <th><span class="text-danger">*</span>청구금액(원)</th>
	                                                <th>비고</th>
	                                                <th>삭제</th>
	                                            </tr>
	                                        </thead>
	                                        <tbody>
	                                            <tr>
	                                                <td>1</td>
	                                                <td>
	                                                    <select class="form-control" id="sinItem1" name="sinItem">
	                                                    </select>
	                                                </td>
	                                                <td class="form-td-inline">
	                                                    <input type="text" class="form-control" id="sinAmt1" name="sinAmt" onchange="getNumber(this);" onkeyup="getNumber(this);" style="text-align: right;">
	                                                </td>
	                                                <td>
	                                                    <input type="text" class="form-control" id="sinEtc1" name="sinEtc" maxlength="25">
	                                                </td>
	                                                <td>
	                                                    <button class="btn btn-img" onclick="fn_sinItemDel(1)">
	                                                        <img src="/assets/imgs/common/btn-lubbish-bin.png">
	                                                    </button>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                                <td>2</td>
	                                                <td>
	                                                    <select class="form-control" id="sinItem2" name="sinItem">
	                                                    </select>
	                                                </td>
	                                                <td class="form-td-inline">
	                                                    <input type="text" class="form-control" id="sinAmt2" name="sinAmt" onchange="getNumber(this);" onkeyup="getNumber(this);" style="text-align: right;">
	                                                </td>
	                                                <td>
	                                                    <input type="text" class="form-control" id="sinEtc2" name="sinEtc" maxlength="25">
	                                                </td>
	                                                <td>
	                                                    <button class="btn btn-img" onclick="fn_sinItemDel(2)">
	                                                        <img src="/assets/imgs/common/btn-lubbish-bin.png">
	                                                    </button>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                                <td>3</td>
	                                                <td>
	                                                    <select class="form-control" id="sinItem3" name="sinItem">
	                                                    </select>
	                                                </td>
	                                                <td class="form-td-inline">
	                                                    <input type="text" class="form-control" id="sinAmt3" name="sinAmt" onchange="getNumber(this);" onkeyup="getNumber(this);" style="text-align: right;">
	                                                </td>
	                                                <td>
	                                                    <input type="text" class="form-control" id="sinEtc3" name="sinEtc" maxlength="25">
	                                                </td>
	                                                <td>
	                                                    <button class="btn btn-img" onclick="fn_sinItemDel(3)">
	                                                        <img src="/assets/imgs/common/btn-lubbish-bin.png">
	                                                    </button>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                                <td>4</td>
	                                                <td>
	                                                    <select class="form-control" id="sinItem4" name="sinItem">
	                                                    </select>
	                                                </td>
	                                                <td class="form-td-inline">
	                                                    <input type="text" class="form-control" id="sinAmt4" name="sinAmt" onchange="getNumber(this);" onkeyup="getNumber(this);" style="text-align: right;">
	                                                </td>
	                                                <td>
	                                                    <input type="text" class="form-control" id="sinEtc4" name="sinEtc" maxlength="25">
	                                                </td>
	                                                <td>
	                                                    <button class="btn btn-img" onclick="fn_sinItemDel(4)">
	                                                        <img src="/assets/imgs/common/btn-lubbish-bin.png">
	                                                    </button>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                                <td>5</td>
	                                                <td>
	                                                    <select class="form-control" id="sinItem5" name="sinItem">
	                                                    </select>
	                                                </td>
	                                                <td class="form-td-inline">
	                                                    <input type="text" class="form-control" id="sinAmt5" name="sinAmt" onchange="getNumber(this);" onkeyup="getNumber(this);" style="text-align: right;">
	                                                </td>
	                                                <td>
	                                                    <input type="text" class="form-control" id="sinEtc5" name="sinEtc" maxlength="25">
	                                                </td>
	                                                <td>
	                                                    <button class="btn btn-img" onclick="fn_sinItemDel(5)">
	                                                        <img src="/assets/imgs/common/btn-lubbish-bin.png">
	                                                    </button>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                                <td>6</td>
	                                                <td>
	                                                    <select class="form-control" id="sinItem6" name="sinItem">
	                                                    </select>
	                                                </td>
	                                                <td class="form-td-inline">
	                                                    <input type="text" class="form-control" id="sinAmt6" name="sinAmt" onchange="getNumber(this);" onkeyup="getNumber(this);" style="text-align: right;">
	                                                </td>
	                                                <td>
	                                                    <input type="text" class="form-control" id="sinEtc6" name="sinEtc" maxlength="25">
	                                                </td>
	                                                <td>
	                                                    <button class="btn btn-img" onclick="fn_sinItemDel(6)">
	                                                        <img src="/assets/imgs/common/btn-lubbish-bin.png">
	                                                    </button>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                                <td>7</td>
	                                                <td>
	                                                    <select class="form-control" id="sinItem7" name="sinItem">
	                                                    </select>
	                                                </td>
	                                                <td class="form-td-inline">
	                                                    <input type="text" class="form-control" id="sinAmt7" name="sinAmt" onchange="getNumber(this);" onkeyup="getNumber(this);" style="text-align: right;">
	                                                </td>
	                                                <td>
	                                                    <input type="text" class="form-control" id="sinEtc7" name="sinEtc" maxlength="25">
	                                                </td>
	                                                <td>
	                                                    <button class="btn btn-img" onclick="fn_sinItemDel(7)">
	                                                        <img src="/assets/imgs/common/btn-lubbish-bin.png">
	                                                    </button>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                                <td>8</td>
	                                                <td>
	                                                    <select class="form-control" id="sinItem8" name="sinItem">
	                                                    </select>
	                                                </td>
	                                                <td class="form-td-inline">
	                                                    <input type="text" class="form-control" id="sinAmt8" name="sinAmt" onchange="getNumber(this);" onkeyup="getNumber(this);" style="text-align: right;">
	                                                </td>
	                                                <td>
	                                                    <input type="text" class="form-control" id="sinEtc8" name="sinEtc" maxlength="25">
	                                                </td>
	                                                <td>
	                                                    <button class="btn btn-img" onclick="fn_sinItemDel(8)">
	                                                        <img src="/assets/imgs/common/btn-lubbish-bin.png">
	                                                    </button>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                                <td>9</td>
	                                                <td>
	                                                    <select class="form-control" id="sinItem9" name="sinItem">
	                                                    </select>
	                                                </td>
	                                                <td class="form-td-inline">
	                                                    <input type="text" class="form-control" id="sinAmt9" name="sinAmt" onchange="getNumber(this);" onkeyup="getNumber(this);" style="text-align: right;">
	                                                </td>
	                                                <td>
	                                                    <input type="text" class="form-control" id="sinEtc9" name="sinEtc" maxlength="25">
	                                                </td>
	                                                <td>
	                                                    <button class="btn btn-img" onclick="fn_sinItemDel(9)">
	                                                        <img src="/assets/imgs/common/btn-lubbish-bin.png">
	                                                    </button>
	                                                </td>
	                                            </tr>
	                                        </tbody>
	                                    </table>
                                    </div>
                                    <div class="guide-mention" style="color:#71adfd;">
                                        * '청구관리 > 항목관리'에서 청구 항목을 미리 설정해주세요.
                                    </div>
                                </div>
                            </div>
							 
                            <div class="container-fluid mb-3">
                              <div class="row no-gutters mt-4">
                                    <div class="col-6">
                                        <h5>추가 안내사항</h5>
                                    </div>
                                    <div class="col-6 text-right">
                                    </div>
                                </div>

                                <table class="table table-form">
                                    <tbody class="container-fluid">
                                        <tr class="row no-gutters">
                                            <td class="col-12">
                                                <textarea class="form-control" id="sinRemark" name="sinRemark" rows="3"></textarea>
                                                <span class="textarea-byte-counter"><em>0</em>/360byte</span>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                <div class="guide-mention" style="color:#71adfd;">
                                    * 각 고지서의 추가 안내사항이 있을 경우 이용하세요. 해당 고지서 하단에 표기됩니다.
                                </div>
                            </div>
 							
                            <div class="container-fluid">
                                <div class="row list-button-group-bottom">
                                    <div class="col col-6">
                                        <button class="btn btn-primary btn-outlined btn-re-search mb-1" onclick="fn_reSearch();">고객 재검색</button>
                                        <button type="button" class="btn btn-primary btn-outlined mb-1" data-dismiss="modal" onclick="fn_exit();">취소</button>
                                    </div>

                                    <div class="col col-6 text-right">
                                    	<button type="button" class="btn btn-primary add-this-charge mb-1" onclick="fn_singleInsert();">청구대상 추가</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
			<div class="modal-footer" id="btn-footer">
            	<input type="hidden" value="Y" id="linelimitAlert"/>
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal" onclick="fn_exit();">취소</button>
            </div>
        </div>
    </div>
</div>
