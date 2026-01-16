<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:authentication property="principal.username" var="userId" />
<s:authentication property="principal.name" var="userName" />

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
    var cuPage = 1;
    var toDay = getFormatCurrentDate();
    var firstDepth = "nav-link-34";
    var secondDepth = "sub-04";
    var cusGubn;
    var cusGubn2;
    var showType='cash';
    $(document).ready(function() {


		getYearsBox('yearsBox');
		getMonthBox('monthBox');
		getYearsBox('yearsBox2');
		getMonthBox('monthBox2');
		$('#fDate').val(getPrevDate(toDay,1));
		$('#tDate').val(toDay);
		cusGubnBox('cusGubn'); // 고객구분 selectBox

		$('#row-th').click(function(){
			if ($(this).prop('checked')) {
				$('input:checkbox[name=checkList]').prop('checked', true);
			} else {
				$('input:checkbox[name=checkList]').prop('checked', false);
			}
		});
		$('#row2-th').click(function(){
			if ($(this).prop('checked')) {
				$('input:checkbox[name=checkList2]').prop('checked', true);
			} else {
				$('input:checkbox[name=checkList2]').prop('checked', false);
			}
		});
		$('input:radio[name=notiMasSt]').click(function(){
			if($(this).val() != 'PA02'){
				$('#dp1').css('display', 'none');
			}else{
				$('#dp1').css('display', 'block');
			}
		});

		$('input:radio[name=rePayYn]').click(function(){
			if($(this).val() != 'N'){
				$('#dp2').css('display', 'none');
			}else{
				$('#dp2').css('display', 'block');
			}
		});

		// list 전체 체크 후 항목 클릭해지시 전제 선택 값 false
		$('input:checkbox[name=checkList]').click(function() {
			if($('#row-th').is(":checked")){
				$('#row-th').prop("checked",false);
			}
		});

		// list2 전체 체크 후 항목 클릭해지시 전제 선택 값 false
		/* $('input:checkbox[name=checkList2]').click(function() {
			if($('#row2-th').is(":checked")){
				$('#row-th2').prop("checked",false);
			}else{
			}
		}); */


		$('#showCash').click(function(){
			showType = 'cash';
			$('.cashDct').css('display','');
			$('.refundDct').css('display','none');
		});
		$('#showRefund').click(function(){
			showType = 'refund';
			$('.cashDct').css('display','none');
			$('.refundDct').css('display','');
			fnSearch('','refund');
		});

        fnSearch('',showType);
	});

	//취소
	function fnCancle(type){
		if(type == 'cash'){
			$('input:checkbox[name=checkList]:checked').each(function(i) {
				var idx = $(this).attr('num');
				$('#svecd-'+idx+'').val('');
				$('#dcsAmt-'+idx+'').val($('#dcsAmt-'+idx+'').attr('ognvalue'));
				$('#dcdAmt-'+idx+'').val($('#dcdAmt-'+idx+'').attr('ognvalue'));
				$(this).prop('checked', false);
			});

		}else{
			$('input:checkbox[name=checkList]:checked').each(function(i) {
				var idx = $(this).attr('num');
				$(this).prop('checked', false);
			});

		}
	}

	function prevDate(num){
		toDay = $.datepicker.formatDate("yy.mm.dd", new Date() );
		var vdm = dateValidity($('#fDate').val(), toDay);
		/*
	 	if (vdm != 'ok'){
	 		swal({
	 	           type: 'info',
	 	           text: vdm,
	 	           confirmButtonColor: '#3085d6',
	 	           confirmButtonText: '확인'
	 	       });
	 		$('#tDate').val(toDay);
	 		$('#fDate').val(getPrevDate(toDay,num));
	 		return false;
	 	}
		*/
	 	$('#tDate').val(toDay);
		$('#fDate').val(getPrevDate(toDay,num));
		$('.btn-preset-month').removeClass('active');
		$('#pMonth'+num+'').addClass('active');
	}

	function fnSave(type) {
		if(type == 'cash'){ //현장수납등록
			var count = $('input:checkbox[name=checkList]:checked').length;
			var jsonRECS  = [];
			var jsonREC   = null;
			var flag = true;
			var afterCd = '';
			var state = '';
			if(count == 0) {
				swal({
				       type: 'info',
				       text: '수납 등록할 건을 1건 이상 선택해주세요.',
				       confirmButtonColor: '#3085d6',
				       confirmButtonText: '확인'
					});
				return false;
			}else{
				$('input:checkbox[name=checkList]:checked').each(function(i) {
					/* var amt = parseInt($('#dcsAmt-'+$(this).attr('num')+'').val()) + parseInt($('#dcdAmt-'+$(this).attr('num')+'').val())
						+ parseInt($(this).attr('rcpAmt')); */
					if(isNaN(parseInt(removeCommas($('#dcsAmt-'+$(this).attr('num')+'').val()))) == true){
						$('#dcsAmt-'+$(this).attr('num')+'').val('0')
					}
					if(isNaN(parseInt(removeCommas($('#dcdAmt-'+$(this).attr('num')+'').val()))) == true){
						$('#dcdAmt-'+$(this).attr('num')+'').val('0')
					}
					var amt = parseInt($(this).attr('vasAmt')) + parseInt(removeCommas($('#dcsAmt-'+$(this).attr('num')+'').val())) + parseInt(removeCommas($('#dcdAmt-'+$(this).attr('num')+'').val()));
					var dAmt = parseInt(removeCommas($('#dcsAmt-'+$(this).attr('num')+'').val())) + parseInt(removeCommas($('#dcdAmt-'+$(this).attr('num')+'').val()));
					var originalAmt = parseInt($('#row-'+$(this).attr('num')+'').attr('dcsamt')) + parseInt($('#row-'+$(this).attr('num')+'').attr('dcdamt'));

					if($('#row-'+$(this).attr('num')+'').attr('stCode') == 'PA02' && dAmt == 0){
						swal({
						       type: 'info',
						       text: '금액이 입력되지 않았습니다.',
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
						flag = false;
						return false;
					}
					if($('#row-'+$(this).attr('num')+'').attr('stCode') == 'PA02' && (removeCommas($('#dcsAmt-'+$(this).attr('num')+'').val()) == "0" && removeCommas($('#dcdAmt-'+$(this).attr('num')+'').val()) == "0")){
						swal({
						       type: 'info',
						       text: '금액이 입력되지 않았습니다.',
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
						flag = false;
						return false;
					}
					if(originalAmt == 0 && dAmt == 0){
						swal({
						       type: 'info',
						       text: '금액이 입력되지 않았습니다.',
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
						flag = false;
						return false;
					}
					if( ($('#row-'+$(this).attr('num')+'').attr('dcsamt') == removeCommas($('#dcsAmt-'+$(this).attr('num')+'').val())) &&
							($('#row-'+$(this).attr('num')+'').attr('dcdamt') == removeCommas($('#dcdAmt-'+$(this).attr('num')+'').val()))){
						swal({
						       type: 'info',
						       text: '기존 금액과 동일합니다.',
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
						flag = false;
						return false;
					}
					if(amt > parseInt(removeCommas($('#dcsAmt-'+$(this).attr('num')+'').attr('notiAmt')))){
						swal({
						       type: 'info',
						       text: '수납 가능한 금액을 초과하였습니다.',
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
						flag = false;
						return false;
					}
					if($('#row-'+$(this).attr('num')+'').attr('stCode') != 'PA02' && $('#row-'+$(this).attr('num')+'').attr('stCode') != 'PA04'){
						swal({
						       type: 'info',
						       text: '미납,일부납 데이터만 수정가능합니다.',
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
						flag = false;
						return false;
					}
					if(amt == 0){
						//일부납
						afterCd = 'PA02';
					}else if(amt >= removeCommas($('#dcdAmt-'+$(this).attr('num')+'').attr('notiAmt'))){
						afterCd = 'PA03';
					}else{
						afterCd = 'PA04';
					}

					jsonREC = {};
					jsonREC["sveCd"] = $('#svecd-'+$(this).attr('num')+'').val(); //서비스 구분코드
					if($('#row-'+$(this).attr('num')+'').attr('stCode') == 'PA04'){ //일부납인경우 기존금액과 합산해서 저장
						jsonREC["rcpAmt"] = amt;
					}else{
						jsonREC["rcpAmt"] = amt; // 수납금액
					}
					jsonREC["vano"] = $(this).attr('vano');
					jsonREC["dcsAmt"] = removeCommas($('#dcsAmt-'+$(this).attr('num')+'').val());
					jsonREC["dcdAmt"] = removeCommas($('#dcdAmt-'+$(this).attr('num')+'').val());
					jsonREC["notiMasCd"] = $(this).val();
					jsonREC["beforeCd"] = $('#row-'+$(this).attr('num')+'').attr('stCode');
					jsonREC["afterCd"] = afterCd;
					//jsonREC["rcpMasCd"] = $(this).attr('rcpMasCd');
					jsonRECS[i] = jsonREC;
				});
				if(flag == true){
					var url = "/org/receiptMgnt/actualReceiptSaveAjax";
					param = {
							rec: jsonRECS
					};
				$.ajax({
						type : "post",
						async : true,
						url : url,
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(param),
						success : function(result) {
							if (result.retCode == "0000") {
								swal({
								       type: 'success',
								       text: '현장수납등록 내역이 저장되었습니다',
								       confirmButtonColor: '#3085d6',
								       confirmButtonText: '확인'
									});
								$('#row-th').prop('checked', false);
								fnSearch(cuPage,'cash');
							} else {
								swal({
								       type: 'error',
								       text: result.retMsg,
								       confirmButtonColor: '#3085d6',
								       confirmButtonText: '확인'
									});
							}
						}
					});
				}
			}
		}else{ //환불내역등록
			var count = $('input:checkbox[name=checkList2]:checked').length;
			var jsonRECS  = [];
			var jsonREC   = null;
			var flag = true;
			var afterCd = '';
			var state = '';
			
			swal({
		        type: 'question',
		        html: "환불 등록이 완료되면, 해당 청구 건은 추가 수납이 불가능합니다. \n 환불 등록을 하시겠습니까?",  
		        content: "input",
		        showCancelButton: true,
		        confirmButtonColor: '#3085d6',
		        cancelButtonColor: '#d33',
		        confirmButtonText: '확인',
		        cancelButtonText: '취소'
			}).then(function(result) {
				if (result.value) { 

			if(count == 0) {
				swal({
				       type: 'info',
				       text: '환불 내역을 등록할 건을 1건 이상 선택해주세요.',
				       confirmButtonColor: '#3085d6',
				       confirmButtonText: '확인'
					});
				return false;
			}else{
				$('input:checkbox[name=checkList2]:checked').each(function(i) {
					if($('#rePayAmt-'+$(this).attr('num')+'').val() == "" || $('#rePayAmt-'+$(this).attr('num')+'').val() == 0){
						swal({
						       type: 'info',
						       text: '환불 금액이 입력되지 않았습니다.',
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
						flag = false;
					}

					if(parseInt($('#rePayAmt-'+$(this).attr('num')+'').val()) > parseInt($('#rePayAmt-'+$(this).attr('num')+'').attr('ognValue'))){
						swal({
						       type: 'info',
						       text: '환불액은 입금액을 초과할 수 없습니다.',
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
						flag = false;
					}
					jsonREC = {};
					jsonREC["rePayAmt"] = removeCommas($('#rePayAmt-'+$(this).attr('num')+'').val()); // 수납금액
					jsonREC["notiMasCd"] = $(this).val();
					jsonRECS[i] = jsonREC;
				});
				if(flag == true){
					var url = "/org/receiptMgnt/actualRepaySaveAjax";
					param = {
							rec: jsonRECS
					};
					$.ajax({
						type : "post",
						async : true,
						url : url,
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(param),
						success : function(result) {
							if (result.retCode == "0000") {
								swal({
								       type: 'success',
								       text: '환불 내역이 저장되었습니다.',
								       confirmButtonColor: '#3085d6',
								       confirmButtonText: '확인'
									});
								$('#row2-th').prop('checked', false);
								fnSearch(cuPage,'refund');
							} else {
								swal({
								       type: 'error',
								       text: result.retMsg,
								       confirmButtonColor: '#3085d6',
								       confirmButtonText: '확인'
									});
							}
						}
					});
				}
			}
		}
		});

	}
	}

	// 고객구분selectBox
	function cusGubnBox(obj) {
		var str = "<option value='all'>전체</option>";
		if ('${orgSess.cusGubn1}' != null && '${orgSess.cusGubn1}' != '') {
			str += "<option value='cusGubn1'>${orgSess.cusGubn1}</option>";
		}
		if ('${orgSess.cusGubn2}' != null && '${orgSess.cusGubn2}' != '') {
			str += "<option value='cusGubn2'>${orgSess.cusGubn2}</option>";
		}
		if ('${orgSess.cusGubn3}' != null && '${orgSess.cusGubn3}' != '') {
			str += "<option value='cusGubn3'>${orgSess.cusGubn3}</option>";
		}
		if ('${orgSess.cusGubn4}' != null && '${orgSess.cusGubn4}' != '') {
			str += "<option value='cusGubn4'>${orgSess.cusGubn4}</option>";
		}
		$('#cusGubn').html(str);
		$('#cusGubn_2').html(str);
	}

	function onSearchGb() {
		if ($('#searchGb option:selected').val() == "vano") {
			$('#searchvalue').val("");
		}
		if ($('#searchGb2 option:selected').val() == "vano") {
			$('#searchvalue2').val("");
		}
	}

	function fnCusGubn(str) {
		cusGubn = str.value;
		if(str.value == 'all'){
//			$('#cusGubnValue').attr('placeholder','검색어를 입력하세요.');
		}else{
			$('#cusGubnValue').attr('placeholder','콤마(,) 구분자로 다중검색이 가능합니다.');
		}
	}

	function fnCusGubn2(str) {
		cusGubn2 = str.value;
		if(str.value == 'all'){
//			$('#cusGubnValue2').attr('placeholder','검색어를 입력하세요.');
		}else{
			$('#cusGubnValue2').attr('placeholder','콤마(,) 구분자로 다중검색이 가능합니다.');
		}
	}

	function pageChange(type) {
		cuPage = 1;
		fnSearch(cuPage, type);
	}

	// 페이징 버튼
	function list(page) {
		$('#pageNo').val(page);
		fnSearch(page, showType);
	}

	// 페이징 버튼
	function list2(page) {
		fnSearch(page, showType);
	}
	
	// type :a  현장수납조회 / b:환불내역조회
	function fnSearch(page, type) {
		var startDate = replaceDot($('#fDate').val());
		var endDate = replaceDot($('#tDate').val());
		
		var vdm = dateValidity(startDate, endDate);
        if (vdm != 'ok'){
            swal({
                type: 'info',
                text: vdm,
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }
		
		if(page == null || page == 'undefined' || page == ''){
			cuPage = "";
			cuPage = 1;
		}else{
			cuPage = page;
		}
		if(type == 'cash'){ //현장수납조회
			var gubn;
			if($('input:radio[name=notiMasSt]:checked').val() == 'PA02'){
				gubn = 0;
			}else{
				gubn = 1;
			}
			if($('#searchGb option:selected').val() == "vano" && $("#searchValue").val()) {
				var str = $("#searchValue").val();
				var s = str.split(",");

				for(var i = 0; i < s.length; i++) {
					if(($.isNumeric(s[i]) && (s[i].length < 2 || s[i].length > 4)) && s[i].length != 14) {
						swal({
						   type: 'info',
						   html: "가상계좌번호 14자리 모두 입력 또는 <p>가상계좌 끝 4자리 중 최소 2자리 이상, 4자리 이하로 입력해 주세요.",
						   confirmButtonColor: '#3085d6',
						   confirmButtonText: '확인'
						});
						return;
					}
					if(!$.isNumeric(s[i])) {
	                    swal({
	                       type: 'info',
	                       text: "가상계좌는 숫자형식으로만 입력가능합니다.",
	                       confirmButtonColor: '#3085d6',
	                       confirmButtonText: '확인'
	                    }).then(function () {
	                        $("#searchValue").val('');
	                    });
	                    return;
	                }
				}
			}
			
			var url = "/org/receiptMgnt/actualReceiptRegAjax";
			var param = {
				masMonth : $('#yearsBox option:selected').val() + ""
							+ $('#monthBox option:selected').val(),
				searchGb : $('#searchGb option:selected').val(),
				searchValue : $('#searchValue').val() != "" ? $('#searchValue').val() : null,
				cusGubn : $('#cusGubn option:selected').val(),
				cusGubnValue : $('#cusGubnValue').val() != "" ? $('#cusGubnValue').val() : null,
				curPage : cuPage,
				pageScale : $('#pageScale option:selected').val(),
				orderBy : $('#orderBy option:selected').val(),
				gubn : gubn
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
						ajaxPaging(result, 'PageArea');
					} else {
						swal({
						       type: 'error',
						       text: result.retMsg,
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
					}
				}
			});
		}else { // 환불내역조회
			var masmonth = null;
			var startDate = null;
			var endDate = null;
			if($('input:radio[name=inlineRadioOptions]:checked').val() == 'month'){
				masmonth = $('#yearsBox2 option:selected').val() + ""
							+ $('#monthBox2 option:selected').val();
			}else{
				startDate = replaceDot($('#fDate').val());
				endDate = replaceDot($('#tDate').val());
			}

			if($('#searchGb2 option:selected').val() == "vano" && $("#searchValue2").val()) {
				var str = $("#searchValue2").val();
				var s = str.split(",");

				for(var i = 0; i < s.length; i++) {
					if(($.isNumeric(s[i]) && (s[i].length < 2 || s[i].length > 4)) && s[i].length != 14) {
						swal({
						   type: 'info',
						   html: "가상계좌번호 14자리 모두 입력 또는 <p>가상계좌 끝 4자리 중 최소 2자리 이상, 4자리 이하로 입력해 주세요.",
						   confirmButtonColor: '#3085d6',
						   confirmButtonText: '확인'
						});
						return;
					}
					if(!$.isNumeric(s[i])) {
	                    swal({
	                       type: 'info',
	                       text: "가상계좌는 숫자형식으로만 입력가능합니다.",
	                       confirmButtonColor: '#3085d6',
	                       confirmButtonText: '확인'
	                    }).then(function () {
	                        $("#searchValue2").val('');
	                    });
	                    return;
	                }
				}
			}
			var url = "/org/receiptMgnt/refundReceiptRegAjax";
			var param = {
				masMonth : masmonth,
				startDate : startDate,
				endDate : endDate,
				amtChkTy : $('#amtChkTy').val(),
				searchGb : $('#searchGb2 option:selected').val(),
				searchValue : $('#searchValue2').val() != "" ? $('#searchValue2').val() : null,
				curPage : cuPage,
				pageScale : $('#pageScale2 option:selected').val(),
				orderBy : $('#orderBy2 option:selected').val(),
				cusGubn : $('#cusGubn_2 option:selected').val(),
				cusGubnValue : $('#cusGubnValue2').val() != "" ? $('#cusGubnValue2').val() : null,
				rePayYN : $('input:radio[name=rePayYn]:checked').val(), //N 환불가능 Y 환불완료
				amtChkTy : $('#amtChkTy').val()	//금액체크여부[IR방식:Y, SET방식:N]
			};

			$.ajax({
				type : "post",
				async : true,
				url : url,
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(param),
				success : function(result) {
					if (result.retCode == "0000") {
						fnGrid(result, 'resultBody2');		// 현재 데이터로 셋팅
						ajaxPaging2(result, 'PageArea2');
					} else {
						swal({
						       type: 'error',
						       text: result.retMsg,
						       confirmButtonColor: '#3085d6',
						       confirmButtonText: '확인'
							});
					}
				}
			});
		}

	}

	function tHead(flag){
		var str = '';
		str+='<tr>';
		if(flag == '0'){
			str+='<th>';
			str+='<input class="form-check-input table-check-parents" type="checkbox" id="row-th" value="option2">';
			str+='<label for="row-th"><span></span></label>';
			str+='</th>';
		}else{
		}
		
		str+='<th>NO</th>';
		str+='<th>청구월</th>';
		str+='<th>고객명</th>';
		if($('#cusGubn1').val() != null && $('#cusGubn1').val()!= ''){
			str+='<th>${orgSess.cusGubn1}</th>';
		}
		if($('#cusGubn2').val() != null && $('#cusGubn2').val()!= ''){
			str+='<th>${orgSess.cusGubn2}</th>';
		}
		if($('#cusGubn3').val() != null && $('#cusGubn3').val()!= ''){
			str+='<th>${orgSess.cusGubn3}</th>';
		}
		if($('#cusGubn4').val() != null && $('#cusGubn4').val()!= ''){
			str+='<th>${orgSess.cusGubn4}</th>';
		}
		str+='<th id="thType">납부기한</th>';
		str+='<th>청구금액(원)</th>';
		str+='<th>입금액(원)</th>';
		str+='<th>미납액(원)</th>';
		str+='<th>현금(원)</th>';
		str+='<th>오프라인카드(원)</th>';
		str+='<th>수납상태</th>';
		str+='</tr>';
		$('#resultHead').html(str);
	}
	
	// 데이터 새로고침
	function fnGrid(result, obj) {
		var str = '';
		if(obj == 'resultBody'){ //현장수납내역
			$('#pageCnt').val(result.PAGE_SCALE);
			var flag = 0;
			if($('input:radio[name=notiMasSt]:checked').val() == 'PA03'){ // 수납완료
				//$('#thType').text('입금가능일시');
				$('#totTitle').text('수납금액');
				$('#totAmt').text(numberToCommas(result.totAmt));
				flag = 1;
				//tHead(flag);
			}else{
				//$('#thType').text('입금일시');
				flag = 0;
				//$('#totCnt').text(numberToCommas(result.count));
				$('#totTitle').text('미납금액');
				$('#totAmt').text(numberToCommas(result.unAmt));
				//tHead(flag);
			}
			$('#totCnt').text(numberToCommas(result.count));
			if(result.count <= 0){
				str+='<tr><td colspan="15" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
			}else{
				$.each(result.list, function(i, v){
					if(v.notiMasSt == 'PA06'){
						str+='<tr class="bg-secondary">';
					}else{
						str+='<tr>';
					}
					if(flag == 0){
						str+='<td>';
						if(v.notiMasSt == 'PA06'){
							str+='<input class="form-check-input table-check-child checkbox-none-check-disabled" type="checkbox" name="checkList" id="row-'+v.rn+'" value="'+v.notiMasCd+'" num="'+v.rn+'" stCode="'+v.notiMasSt+'" vaNo="'+v.vano+'" disabled="disabled">';
						}else{
							str+='<input class="form-check-input table-check-child" type="checkbox" name="checkList" id="row-'+v.rn+'" value="'+v.notiMasCd+'" rcpMasCd="'+v.rcpMasCd+'" num="'+v.rn+'" stCode="'+v.notiMasSt+'" vaNo="'+v.vano+'" rcpAmt="'+v.rcpAmt+'" vasAmt="'+v.vasAmt+'" dcsAmt="'+v.dcsAmt+'" dcdAmt="'+v.dcdAmt+'" onchange="changeBGColor(this)">';
						}
						str+='<label for="row-'+v.rn+'"><span></span></label>';
						str+='</td>';
					}else{
						str+='<td>';
						str+='<input class="form-check-input table-check-child" type="checkbox" name="checkList" id="row-'+v.rn+'" value="'+v.notiMasCd+'" num="'+v.rn+'" stCode="'+v.notiMasSt+'" vaNo="'+v.vano+'" dcsAmt="'+v.dcsAmt+'" dcdAmt="'+v.dcdAmt+'" onchange="changeBGColor(this)">';
						str+='<label for="row-'+v.rn+'"><span></span></label>';
						str+='</td>';
					}
					str+='<td>'+v.rn+'</td>';
					str+='<td>'+v.masMonth+'</td>';
					str+='<td><button type="button" class="btn btn-xs btn-link" onclick="cusName('+v.vano+')" value="'+v.vano+'">'+basicEscape(v.cusName)+'</button></td>';
					if($('#cusGubn1').val() != null && $('#cusGubn1').val() != ''){
						str+='<td>'+basicEscape(nullValueChange(v.cusGubn1))+'</td>';
					}
					if($('#cusGubn2').val() != null && $('#cusGubn2').val() != ''){
						str+='<td>'+basicEscape(nullValueChange(v.cusGubn2))+'</td>';
					}
					if($('#cusGubn3').val() != null && $('#cusGubn3').val() != ''){
						str+='<td>'+basicEscape(nullValueChange(v.cusGubn3))+'</td>';
					}
					if($('#cusGubn4').val() != null && $('#cusGubn4').val() != ''){
						str+='<td>'+basicEscape(nullValueChange(v.cusGubn4))+'</td>';
					}
					str+='<td>'+v.rcpDate+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.sumAmt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.rcpAmt)+'</td>';
					if(flag == 0){
						str+='<td class="text-right">'+numberToCommas(v.unAmt)+'</td>';
						if(v.notiMasSt == 'PA06'){
							str+='<td class="text-right"><input type="text" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)" class="form-control text-right" id="dcsAmt-'+v.rn+'" name="sumAmt" value="'+numberToCommas(v.dcsAmt)+'" rcpAmt="'+v.rcpAmt+'" notiAmt="'+v.sumAmt+'" ognValue="'+v.dcsAmt+'" disabled="disabled" style="width: 120px"></td>';
							str+='<td class="text-right"><input type="text" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)" class="form-control text-right" id="dcdAmt-'+v.rn+'" name="sumAmt" value="'+numberToCommas(v.dcdAmt)+'" rcpAmt="'+v.rcpAmt+'" notiAmt="'+v.sumAmt+'" ognValue="'+v.dcdAmt+'" disabled="disabled" style="width: 120px"></td>';
						}else{
							str+='<td class="text-right"><input type="text" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)" class="form-control text-right" id="dcsAmt-'+v.rn+'" name="sumAmt" value="'+numberToCommas(v.dcsAmt)+'" rcpAmt="'+v.rcpAmt+'" notiAmt="'+v.sumAmt+'" ognValue="'+v.dcsAmt+'" style="width: 120px"></td>';
							str+='<td class="text-right"><input type="text" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)" class="form-control text-right" id="dcdAmt-'+v.rn+'" name="sumAmt" value="'+numberToCommas(v.dcdAmt)+'" rcpAmt="'+v.rcpAmt+'" notiAmt="'+v.sumAmt+'" ognValue="'+v.dcdAmt+'" style="width: 120px"></td>';
						}
					}else{
						str+='<td class="text-danger">'+numberToCommas(v.unAmt)+'</td>';
						str+='<td class="text-right">'+numberToCommas(v.dcsAmt)+'</td>';
						str+='<td class="text-right">'+numberToCommas(v.dcdAmt)+'</td>';
					}
					str+='<td class="form-td-inline">';
					str+=v.notiMasStNm;
					str+='</td>';
					str+='</tr>';
				});
			}
		}else{ // 환불내역
			$('#pageCnt2').val(result.PAGE_SCALE);
			$('#totCnt2').text(numberToCommas(result.count));
			var flag = 0;
			if($('input:radio[name=rePayYn]:checked').val() == 'N'){ // 환불가능
				$('#totTitle2').text('입금금액');
				$('#totAmt2').text(numberToCommas(result.totAmt));
				
			}else{	// 환불완료
				$('#totTitle2').text('환불금액');
				$('#totAmt2').text(numberToCommas(result.totAmt));
			}
			
			if(result.count <= 0){
				str+='<tr><td colspan="14" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
			}else{
				$.each(result.list, function(i, v){
					str+='<tr>';
					str+='<td>';
					if((v.cashMasSt == 'ST02' && (v.job == 'I' || v.job == 'U')) || (v.cashMasSt == 'ST03' || v.cashMasSt == 'ST04')){
						str+='<input class="form-check-input table-check-child checkbox-none-check-disabled" type="checkbox" name="checkList2" disabled="disabled">';
					}else{
						str+='<input class="form-check-input table-check-child" type="checkbox" name="checkList2" id="row2-'+v.rn+'" value="'+v.notiMasCd+'" num="'+v.rn+'" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)" >';
					}
					str+='<label for="row2-'+v.rn+'"><span></span></label>';
					str+='</td>';
					str+='<td>'+v.rn+'</td>';
					str+='<td>'+v.masMonth+'</td>';
					str+='<td><button type="button" class="btn btn-xs btn-link" onclick="cusName('+v.vano+')" value="'+v.vano+'">'+basicEscape(v.cusName)+'</button></td>';
					if($('#cusGubn1').val() != null && $('#cusGubn1').val() != ''){
						str+='<td>'+basicEscape(nullValueChange(v.cusGubn1))+'</td>';
					}
					if($('#cusGubn2').val() != null && $('#cusGubn2').val() != ''){
						str+='<td>'+basicEscape(nullValueChange(v.cusGubn2))+'</td>';
					}
					if($('#cusGubn3').val() != null && $('#cusGubn3').val() != ''){
						str+='<td>'+basicEscape(nullValueChange(v.cusGubn3))+'</td>';
					}
					if($('#cusGubn4').val() != null && $('#cusGubn4').val() != ''){
						str+='<td>'+basicEscape(nullValueChange(v.cusGubn4))+'</td>';
					}
					str+='<td><button type="button" class="btn btn-xs btn-link" id="btnPop-'+v.rn+'" notiMasCd="'+v.notiMasCd+'" cusName="'+v.cusName+'" masMonth="'+v.masMonth+'" onclick="payPop('+v.rn+');">'+nullValueChange(v.itemCnt)+'건</buttom></td>';
					str+='<td class="text-right">'+numberToCommas(nullValueChange(v.sumAmt))+'</td>';
					str+='<td class="text-right">'+numberToCommas(nullValueChange(v.rcpAmt))+'</td>';
					if($('input:radio[name=rePayYn]:checked').val() == 'N'){
						if((v.cashMasSt == 'ST02' && (v.job == 'I' || v.job == 'U')) || (v.cashMasSt == 'ST03' || v.cashMasSt == 'ST04')){
							str+='<td class="text-right"><input type="text" id="rePayAmt-'+v.rn+'" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)" class="form-control text-right" value="'+numberToCommas(nullValueChange(v.rcpAmt))+'" ognValue="'+v.rcpAmt+'" style="width: 120px" disabled="disabled"></td>';
						}else{
							str+='<td class="text-right"><input type="text" id="rePayAmt-'+v.rn+'" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)" class="form-control text-right" value="'+numberToCommas(nullValueChange(v.rcpAmt))+'" ognValue="'+v.rcpAmt+'" style="width: 120px"></td>';
						}
					}else{
						str+='<td class="text-right">'+numberToCommas(nullValueChange(v.rePayAmt))+'</td>';
					}
					if($('input:radio[name=rePayYn]:checked').val() == 'N'){
						str+='<td></td>';
						//str+='<td></td>';
					}else{
						str+='<td>'+v.rePayDay+'</td>'; //환불일시
						//str+='<td>'+v.rePayDay+' 환불액 '+numberToCommas(v.rePayAmt)+'원 처리</td>'; // 환불금액, 환불일시
					}
					str+='</tr>';
				});
			}
		}

		$('#' + obj).html(str);
	}

	// 파일저장
	function fn_fileSave(type) {
		var alertResult = false;
		if(type == "cash"){
			// 현장수납등록
			if($('#totCnt').text() <= 0) {
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
                reverseButtons: true,
                onAfterClose: function() {
		            if(alertResult) {
		            	actualReceipFile(type);
			        } 
		        }
			}).then(function(result) {
				if (result.value) {
					alertResult = true;
	    		}
		    });
		}else{
			// 환불내역등록
			if($('#totCnt2').text() <= 0){
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
                reverseButtons: true,
                onAfterClose: function() {
		            if(alertResult) {
		            	refundFile(type);
			        } 
		        }
			}).then(function(result) {
				if (result.value) {
					alertResult = true;
	    		}
		    });
		}
	}
    //현장수납 파일생성
	function actualReceipFile(type){
		if($('input:radio[name=notiMasSt]:checked').val() == 'PA02'){
			$('#fGubn').val('0');
		}else{
			$('#fGubn').val('1');
		}
		$('#fType').val(type);
		$('#masMonth').val($('#yearsBox option:selected').val() + ""+ $('#monthBox option:selected').val());
		$('#fSearchGb').val($('#searchGb option:selected').val());
		$('#fSearchValue').val($('#searchValue').val());
		$('#fCusGubn').val($('#cusGubn option:selected').val());
		$('#fCusGubnValue').val($('#cusGubnValue').val());
		$('#fOrderBy').val($('#orderBy option:selected').val());
		
		// 다운로드
	 	$('#fileForm').submit();
	}
	//환불내역 파일생성
	function refundFile(type){
		if($('input:radio[name=inlineRadioOptions]:checked').val() == 'month'){
			$('#startDate').val('');
			$('#endDate').val('');
			$('#masMonth').val($('#yearsBox2 option:selected').val() + ""+ $('#monthBox2 option:selected').val());
		}else{
			$('#masMonth').val('');
			$('#startDate').val(replaceDot($('#fDate').val()));
			$('#endDate').val(replaceDot($('#tDate').val()));
		}
	
		$('#fSearchGb').val($('#searchGb2 option:selected').val());
		$('#fSearchValue').val($('#searchValue2').val());
		$('#fCusGubn').val($('#cusGubn_2 option:selected').val());
		$('#fCusGubnValue').val($('#cusGubnValue2').val());
		$('#fOrderBy').val($('#orderBy2 option:selected').val());
		$('#fType').val(type);
		$('#fGubn').val($('input:radio[name=rePayYn]:checked').val());
		$('#fAmtChkTy').val($('#amtChkTy').val());	
		// 다운로드
	 	$('#fileForm').submit();
	}
	function cusName(vano){
		fnDetail(vano);
	}
	
	function payPop(idx){
		var notiMasCd = $('#btnPop-'+idx+'').attr('notiMasCd');
		var cusName = $('#btnPop-'+idx+'').attr('cusName');
		var masMonth = replaceDot($('#btnPop-'+idx+'').attr('masMonth'));

		fn_reset_scroll();

		var url = "/org/receiptMgnt/getReceiptDetailAjax";
		var param = {
				notiMasCd : notiMasCd,
				masMonth : masMonth
		};
		$.ajax({
			type: "post",
			url: url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success: function(result){
				if (result.retCode == "0000") {
					$('#popCusName').text(basicEscape(cusName));
					$('#popMasMonth').text(formatYearMonth(masMonth));
					var str = '';
					var popTotalAmt = 0;
					var popTotalRcpAmt = 0;
					if(result.count <= 0){
						str+='<tr><td colspan="3" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
					}else{
						$.each(result.list, function(i, v){
							popTotalAmt += Number(v.payItemAmt);
							popTotalRcpAmt += Number(v.rcpAmt);
							str+='<tr>';
							str+='<td>'+numberToCommas(basicEscape(v.payItemName))+'</td>';
							str+='<td>'+numberToCommas(v.payItemAmt)+'</td>';
							str+='<td>'+numberToCommas(v.rcpAmt)+'</td>';
                            str += '<td>' + (v.ptrItemRemark == null ? "" : basicEscape(v.ptrItemRemark)) + '</td>';
							str+='</tr>';
						});
					}
					$('#popResultBody').html(str);
					$('#popTotalAmt').text(numberToCommas(popTotalAmt));
					$('#popTotalRcpAmt').text(numberToCommas(popTotalRcpAmt));
					$("#popup-payment-unit-list").modal({
				        backdrop:'static',
				        keyboard : false
				    });
				}else{
					swal({
					       type: 'error',
					       text: result.retMsg,
					       confirmButtonColor: '#3085d6',
					       confirmButtonText: '확인'
						});
				}
			}
		});

	}
</script>
<form id="fileForm" name="fileForm" method="post" action="/org/receiptMgnt/actlExcelDown">
	<input type="hidden" id="masMonth" name="masMonth"/>
	<input type="hidden" id="startDate" name="startDate"/>
	<input type="hidden" id="endDate" name="endDate"/>
	<input type="hidden" id="fSearchGb" name="fSearchGb"/>
	<input type="hidden" id="fSearchValue" name="fSearchValue"/>
	<input type="hidden" id="fCusGubn" name="fCusGubn"/>
	<input type="hidden" id="fCusGubnValue" name="fCusGubnValue"/>
	<input type="hidden" id="fOrderBy" name="fOrderBy"/>
	<input type="hidden" id="fGubn" name="fGubn"/>
	<input type="hidden" id="fType" name="fType"/>
	<input type="hidden" id="fAmtChkTy" name="fAmtChkTy" />
</form>
<input type="hidden" id="cusGubn1" name="cusGubn1" value="${orgSess.cusGubn1}" />
<input type="hidden" id="cusGubn2" name="cusGubn2" value="${orgSess.cusGubn2}" />
<input type="hidden" id="cusGubn3" name="cusGubn3" value="${orgSess.cusGubn3}" />
<input type="hidden" id="cusGubn4" name="cusGubn4" value="${orgSess.cusGubn4}" />
<input type="hidden" id="amtChkTy" name="amtChkTy" value="${orgSess.amtChkTy}" />
<input type="hidden" id="pageCnt" name="pageCnt" value="${map.PAGE_SCALE}">
<input type="hidden" id="pageCnt2" name="pageCnt2" value="${map.PAGE_SCALE}">
<input type="hidden" id="pageNo"  name="pageNo"  value="1">

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link" href="/org/receiptMgnt/receiptList">수납내역 조회</a>
			<a class="nav-link" href="/org/receiptMgnt/paymentList">입금내역 조회</a>
			<a class="nav-link active" href="#">직접수납관리</a>
			<a class="nav-link" href="/org/receiptMgnt/cashReceipt">현금영수증</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div id="breadcrumb-in-title-area" class="row align-items-center">
				<div class="col-12 text-right">
					<img src="/assets/imgs/common/icon-home.png" class="mr-2">
					<span> > </span>
					<span class="depth-1">수납관리</span>
					<span> > </span>
					<span class="depth-2 active">직접수납관리</span>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2>직접수납관리</h2>
				</div>
				<div class="col-6 text-right">
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div id="page-description">
			<div class="row">
				<div class="col-12">
					<p class="cashDct">현장에서 직접 수납한 건을 수기로 등록하기 위한 화면으로, 온라인 카드결제가 완료된 건은 조회 대상에서 제외됩니다.</p>
					<p class="refundDct" style="display: none;">환불 처리 건을 수기로 등록하기 위한 화면입니다.</p>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="tab-selecter type-3">
					<ul class="nav nav-tabs" role="tablist">
						<li class="nav-item"><a id="showCash" class="nav-link active" data-toggle="tab" href="#cash-on-desk">현장수납등록</a></li>
						<li class="nav-item"><a id="showRefund" class="nav-link" data-toggle="tab" href="#direct-refund">환불내역등록</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div class="container tab-content">
		<div id="cash-on-desk" class="tab-pane fade show active">
			<div class="search-box">
				<form>
					<div class="row">
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							청구월
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control" id="yearsBox">
								<option>선택</option>
							</select>
							<span class="ml-1 mr-1">년</span>
							<select class="form-control" id="monthBox">
								<option>선택</option>
							</select>
							<span class="ml-1 mr-auto">월</span>
						</div>
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							수납상태
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" id="tableCheckbox1" name="notiMasSt" checked="checked" value="PA02">
								<label for="tableCheckbox1"><span class="mr-2"></span>미납/일부납</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" id="tableCheckbox2" name="notiMasSt" value="PA03">
								<label for="tableCheckbox2"><span class="mr-2"></span>완납</label>
							</div>
						</div>
					</div>
					<div class="row">
						<label class="ccol col-md-1 col-sm-2 col-2 col-form-label">
							검색구분
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control" id="searchGb" onchange="onSearchGb();">
								<option value="cusname">고객명</option>
								<option value="vano">계좌번호</option>
							</select>
							<input class="form-control" type="text" name="searchValue" id="searchValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch('','cash');}"/>
						</div>
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							고객구분
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control" id="cusGubn" onchange="fnCusGubn(this);">
							</select>
							<div class="input-with-magnet">
								<input class="form-control" type="text" id="cusGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch('','cash');}"/>
							</div>
						</div>
					</div>

					<div class="row form-inline mt-3">
						<div class="col-12 text-center">
							<button type="button" class="btn btn-primary btn-wide" onclick="fnSearch('','cash');">조회</button>
						</div>
					</div>
				</form>
			</div>


			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="totCnt"><fmt:formatNumber pattern="#,###" value="${map.count}"/></em>건]</span>
					<span class="amount"><span id="totTitle">미납금액</span> [총 <em class="font-blue" id="totAmt"><fmt:formatNumber pattern="#,###" value="${map.unAmt}"/></em>원]</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<select class="form-control" id="orderBy" onchange="pageChange('cash');">
						<option value="month">청구월순 정렬</option>
						<option value="cusname">고객명순 정렬</option>
					</select>
					<select class="form-control" name="pageScale" id="pageScale" onchange="pageChange('cash');">
						<option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
	                    <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
	                    <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
	                    <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
	                    <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
					</select>
					<button class="btn btn-sm btn-d-gray" type="button" onclick="fn_fileSave('cash')">파일저장</button>
				</div>
			</div>

			<div id="search-result-cod">
				<div class="row">
					<div class="table-responsive pd-n-mg-o col mb-3">
						<table class="table table-sm table-hover table-primary">
							<colgroup>
								<col width="52">
								<col width="68">
								<col width="120">
								<col width="100">
								<c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}"><col width="130"></c:if>
								<c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}"><col width="130"></c:if>
								<c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}"><col width="130"></c:if>
								<c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}"><col width="130"></c:if>
								<col width="180">
								<col width="140">
								<col width="140">
								<col width="140">
								<col width="140">
								<col width="140">
								<col width="100">
							</colgroup>

							<thead id="resultHead">
								<tr>
									<th>
										<input class="form-check-input table-check-parents" type="checkbox" id="row-th" value="option2">
										<label for="row-th"><span></span></label>
									</th>
									<th>NO</th>
									<th>청구월</th>
									<th>고객명</th>
									<c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}"><th>${orgSess.cusGubn1}</th></c:if>
									<c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}"><th>${orgSess.cusGubn2}</th></c:if>
									<c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}"><th>${orgSess.cusGubn3}</th></c:if>
									<c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}"><th>${orgSess.cusGubn4}</th></c:if>
									<th id="thType">납부기한</th>
									<th>청구금액(원)</th>
									<th>입금액(원)</th>
									<th>미납액(원)</th>
									<th>현금(원)</th>
									<th>오프라인카드(원)</th>
									<th>수납상태</th>
								</tr>
							</thead>
							<tbody id="resultBody">

							</tbody>
						</table>
					</div>
				</div>

				<jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />

				<div class="row mb-4" id="dp1">
					<div class="col text-center">
						<button type="button" class="btn btn-primary" onclick="fnSave('cash');">저장</button>
					</div>
				</div>
			</div>
		</div>

		<div id="direct-refund" class="tab-pane fade show">
			<div class="search-box">
				<form>
					<div id="monthly-or-duration" class="row radio-selecter">
						<legend class="col-form-label col col-md-1 col-sm-2 col-2 pt-0">조회방식</legend>
						<div class="col col-md-10 col-sm-10 col-9">
							<div class="form-check form-check-inline">
								<input class="form-check-input lookup-by-month" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="month" checked="checked">
								<label class="form-check-label" for="inlineRadio1"><span></span>청구월별</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input lookup-by-range" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="period">
								<label class="form-check-label" for="inlineRadio2"><span></span>입금일자별</label>
							</div>
						</div>
					</div>
					<div id="lookup-by-month" class="row">
						<div class="col-md-11 offset-md-1 offset-sm-2 offset-2 col-10 form-inline year-month-dropdown">
							<select class="form-control" id="yearsBox2">
								<option>선택</option>
							</select>
							<span class="ml-1 mr-1">년</span>
							<select class="form-control" id="monthBox2">
								<option>선택</option>
							</select>
							<span class="ml-1">월</span>
						</div>
					</div>
					<div id="lookup-by-range" class="row" style="display:none;">
						<div class="col-md-5 col-sm-10 offset-md-1 offset-sm-2 offset-2 form-inline">
							<div class="date-input">
								<label class="sr-only" for="inlineFormInputGroupUsername">From</label>
								<div class="input-group">
									<input type="text" id="fDate" class="form-control date-picker-input" placeholder="YYYY/MM/DD" aria-label="From" aria-describedby="basic-addon2">
								</div>
							</div>
							<span class="range-mark"> ~ </span>
							<div class="date-input">
								<label class="sr-only" for="inlineFormInputGroupUsername">To</label>
								<div class="input-group">
									<input type="text" id="tDate" class="form-control date-picker-input" placeholder="YYYY/MM/DD" aria-label="To" aria-describedby="basic-addon2">
								</div>
							</div>
						</div>
						<div class="col-md-6 col-sm-9 offset-md-0 offset-sm-2 offset-3">
							<button id="pMonth1" type="button" class="btn btn-sm btn-preset-month active" onclick="prevDate(1)">1개월</button>
							<button id="pMonth2" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(2)">2개월</button>
							<button id="pMonth3" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(3)">3개월</button>
						</div>
					</div>

					<div class="row mb-3 mt-3">
						<div class="col" style="border-top:1px solid #d6d6d6;"></div>
					</div>

					<div class="row">
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							고객구분
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control" id="cusGubn_2" onchange="fnCusGubn2(this);">
							</select>
							<div class="input-with-magnet">
								<input class="form-control" type="text" id="cusGubnValue2" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch('','refund');}"/>
							</div>
						</div>
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							환불상태
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<div class="form-inline">
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="rePayYn" id="refundable" value="N" checked="checked">
									<label class="form-check-label" for="refundable"><span class="mr-1"></span>가능</label>
								</div>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="rePayYn" id="refunded" value="Y">
									<label class="form-check-label" for="refunded"><span class="mr-1"></span>완료</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							검색구분
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control" id="searchGb2" onchange="onSearchGb();">
								<option value="cusname">고객명</option>
								<option value="vano">계좌번호</option>
							</select>
							<input class="form-control col-auto" type="text" id="searchValue2" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch('','refund');}"/>
						</div>
					</div>
					<div class="row form-inline mt-3">
						<div class="col-12 text-center">
							<button type="button" class="btn btn-primary btn-wide" onclick="fnSearch('','refund');">조회</button>
						</div>
					</div>
				</form>
			</div>

			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="totCnt2">0</em>건]</span>
					<span class="amount"><span id="totTitle2">입금금액</span> [총 <em class="font-blue" id="totAmt2">0</em>원]</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<select class="form-control" id="orderBy2" onchange="pageChange('refund');">
						<option value="month">청구월순 정렬</option>
						<option value="cusname">고객명순 정렬</option>
					</select>
					<select class="form-control" name="pageScale" id="pageScale2" onchange="pageChange('refund');">
						<option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
	                    <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
	                    <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
	                    <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
	                     <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
					</select>
					<button class="btn btn-sm btn-d-gray" type="button" onclick="fn_fileSave('refund')">파일저장</button>
				</div>
			</div>

			<div id="search-result-direct-refund">
				<div class="row">
					<div class="table-responsive pd-n-mg-o col mb-3">
						<table class="table table-sm table-hover table-primary">
							<colgroup>
								<col width="52">
								<col width="68">
								<col width="120">
								<col width="100">

								<c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}"><col width="130"></c:if>
								<c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}"><col width="130"></c:if>
								<c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}"><col width="130"></c:if>
								<c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}"><col width="130"></c:if>

								<col width="100">
								<col width="140">
								<col width="140">
								<col width="140">
								<col width="140">
							</colgroup>

							<thead>
								<tr>
									<th>
										<input class="form-check-input table-check-parents" type="checkbox" id="row2-th" value="option2">
										<label for="row2-th"><span></span></label>
									</th>
									<th>NO</th>
									<th>청구월</th>
									<th>고객명</th>
									<c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}"><th>${orgSess.cusGubn1}</th></c:if>
									<c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}"><th>${orgSess.cusGubn2}</th></c:if>
									<c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}"><th>${orgSess.cusGubn3}</th></c:if>
									<c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}"><th>${orgSess.cusGubn4}</th></c:if>
									<th>청구항목</th>
									<th>청구금액(원)</th>
									<th>입금액(원)</th>
									<th>환불액(원)</th>
									<th class="border-r-e6">환불 처리일</th>
								</tr>
							</thead>
							<tbody id="resultBody2">
								<tr>
									<td colspan="13" style="text-align: center;">
										[조회된 내역이 없습니다.]
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<jsp:include page="/WEB-INF/views/include/paging2.jsp" flush="false" />
			
				<div class="row mb-4" id="dp2">
					<div class="col text-center">
						<button type="button" class="btn btn-primary" onclick="fnSave('refund');">저장</button>
					</div>
				</div>
			</div>
		</div>

	</div>

	<div class="container">
        <div id="quick-instruction" class="foldable-box">
            <div class="row foldable-box-header">
                <div class="col-8">
                    <img src="/assets/imgs/common/icon-notice.png">
                    알려드립니다.
                </div>
                <div class="col-4 text-right">
                    <img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
                </div>
            </div>
            <div class="row foldable-box-body">
                <div class="col">
					<span class="cashDct">
						<h6>■ 현장수납 등록</h6>
						<ul>
							<li>수납처리할 고객의 청구월 및 고객구분, 수납상태를 선택 후 조회 버튼 클릭</li>
							<li>입금수단 선택 후 금액 입력 후 하단 저장 버튼 클릭</li>
							<li>참고사항 : 2018.8.24. 다모아 업그레이드 이전에 '무통장입금' 으로 등록된 항목은 '현금(창구)'건으로 변경</li>
						</ul>
					</span>

					<span class="refundDct" style="display: none;">
						<h6>■ 환불내역 등록</h6>
						<ul>
							<li>환불 처리할 고객의 청구월 및 입금일, 입금상태, 고객구분을 선택 후 조회 버튼 클릭</li>
							<li>환불액 등록 및  화면 하단 저장 버튼 클릭</li>
							<li>유의사항 : 현금영수증 기 발행 건에 대한 환불 진행시, 별도로 현금영수증 발행 취소 처리 필요</li>
						</ul>
					</span>
                </div>
            </div>
        </div>
    </div>
</div>
<%-- 청구상세 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/payment-unit-list.jsp" flush="false" />

<%-- 고객정보수정 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/detail-payer-information.jsp" flush="false"/>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />
<script>
$(document).ready(function() {
    $("#keep-it-permenantly-refund").click(function (){
        keepItPermenantly();
    });
});

</script>
