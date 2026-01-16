<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:authentication property="principal.username" var="userId" />
<s:authentication property="principal.name" var="userName" />

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<script type="text/javascript">
	var firstDepth = "nav-link-34";
	var secondDepth = "sub-01";
	var cuPage = 1;
	var toDay = getFormatCurrentDate();
	var cusGubn;
	$(document).ready(function() {
		//getYearsFullBox('yearsFullBox');
		getYearsBox2('yearsBox');
		getMonthBox('monthBox');
		getYearsBox('fYearsBox');
		getMonthBox('fMonthBox');
		getYearsBox('tYearsBox');
		getMonthBox('tMonthBox');
		//$('#fDate').val(getPrevDate(toDay,1));
		//$('#tDate').val(toDay);
		cusGubnBox('cusGubn'); // 고객구분 selectBox

		$('#payRadio1').prop('checked', true);
		$('input:checkbox[name=payItem]').prop('checked', true);
		$('#deposRadio1').prop('checked', true);
		$('input:checkbox[name=deposItem]').prop('checked', true);

		$('#fDate').val(getPrevDate(toDay,1));
		$('#tDate').val(toDay);

		// list 전체 체크 후 항목 클릭해지시 전제 선택 값 false
		$('input:checkbox[name=checkList]').click(function() {
			if($('#row-th').is(":checked")){
				$('#row-th').prop("checked",false);
			}
		});

		//수납상태 전체 체크, 해제
		$('#payRadio1').click(function() {
			if ($(this).prop('checked')) {
				$('input:checkbox[name=payItem]').prop('checked', true);
			} else {
				$('input:checkbox[name=payItem]').prop('checked', false);
			}
		});
		//입금수단 전체 체크, 해제
		$('#deposRadio1').click(function(){
			if ($(this).prop('checked')) {
				$('input:checkbox[name=deposItem]').prop('checked', true);
			} else {
				$('input:checkbox[name=deposItem]').prop('checked', false);
			}
		});

		$('input:checkbox[name=payItem]').click(function() {
			if($('#payRadio1').is(":checked")){
				$('#payRadio1').prop("checked",false);
				$(this).prop("checked",false);
			}
			if($('input:checkbox[name=payItem]').length == $('input:checkbox[name=payItem]:checked').length){
				$('#payRadio1').prop("checked",true);
			}
		});

		$('input:checkbox[name=deposItem]').click(function() {
			if($('#deposRadio1').is(":checked")){
				$('#deposRadio1').prop("checked",false);
			}
			if($('input:checkbox[name=deposItem]').length == $('input:checkbox[name=deposItem]:checked').length){
				$('#deposRadio1').prop("checked",true);
			}
		});

		$('#row-th').click(function(){
			if ($(this).prop('checked')) {
				$('input:checkbox[name=checkList]').prop('checked', true);
			} else {
				$('input:checkbox[name=checkList]').prop('checked', false);
			}
		});
		if('${map.gubn}' == 'main'){
			$("#lookup-by-month").css('display','none');
			$("#lookup-by-range").css('display','');
			$('#inlineRadio2').attr('checked','checked');

			prevDate(3);
		}
		fnSearch();
	});

	function cusName(vano){
		fnDetail(vano);
	}

	// 카드결제 취소
	function fnRefund(notiMasCd) {

		swal({
			type: 'question',
			text: '신용카드 결제내역을 취소하시겠습니까?',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			cancelButtonColor: '#d33',
			confirmButtonText: '확인',
			cancelButtonText: '취소'
		}).then(function(result) {
			if(result.value){
				var strFeature = "width=700, height=500, scrollbars=no, resizable=yes";

				document.cancelCardForm.target = "formInfo";
				//document.cancelCardForm.cancelRcpMasCd.value = rcpMasCd;
				document.cancelCardForm.cancelNotiMasCd.value = notiMasCd;
				window.open("", "formInfo", strFeature);
				document.cancelCardForm.submit();

			}else{

			}

		});
	}

	// 검색
	function fnSearch(page) {
		var checkArr = [];
		var svecdArr = [];
		var masmonth = null;
		var startDate = null;
		var endDate = null;
		if (page == null || page == 'undefined') {
			cuPage = "";
			cuPage = 1;
		} else {
			cuPage = page;
		}

		$('input:checkbox[name=payItem]:checked').each(function (i) {
			checkArr.push($(this).val());
		});
		$('input:checkbox[name=deposItem]:checked').each(function (i) {
			if ($(this).val() == 'DCS') {
				svecdArr.push($(this).val());
				svecdArr.push('DVA');
			} else {
				svecdArr.push($(this).val());
			}
		});

		if ($('input:radio[name=inlineRadioOptions]:checked').val() == 'month') {
			startDate = null;
			endDate = null;
			masmonth = $('#yearsBox option:selected').val() + ""
					+ $('#monthBox option:selected').val();
		} else {
			masmonth = null;

			startDate = replaceDot($('#fDate').val());
			endDate = replaceDot($('#tDate').val());
		}
		if ($('#searchGb option:selected').val() == "vano" && $("#searchValue").val()) {
			var str = $("#searchValue").val();
			var s = str.split(",");

			for (var i = 0; i < s.length; i++) {
				if (($.isNumeric(s[i]) && (s[i].length < 2 || s[i].length > 4)) && s[i].length != 14) {
					swal({
						type: 'info',
						html: "가상계좌번호 14자리 모두 입력 또는 <p>가상계좌 끝 4자리 중 최소 2자리 이상, 4자리 이하로 입력해 주세요.",
						confirmButtonColor: '#3085d6',
						confirmButtonText: '확인'
					});
					return;
				}
				if (!$.isNumeric(s[i])) {
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

		if (fnValidation()) {
			var url = "/org/receiptMgnt/receiptListAjax";
			var param = {
				masMonth: masmonth,
				startDate: startDate,
				endDate: endDate,
				dateGb: $('input:radio[name=inlineRadioOptions]:checked').val() == 'month' ? 'M' : 'D',
				searchGb: $('#searchGb option:selected').val(), //검색구분(cusname:고객명, vano:계좌번호)
				searchValue: $('#searchValue').val(), //검색구분 텍스트값
				payList: checkArr,
				payGb1: $('#payRadio1').is(":checked") ? "ALL" : "NOTALL", //납부구분 (전체)
				payNoGb: $('#payRadio2').is(":checked") ? "CHK" : null,
				chaCd: $('#chaCd').val(),
				svecdList: svecdArr,
				svecdGb1: $('#deposRadio1').is(":checked") ? "ALL" : "NOTALL",
				cusGubn: $('#cusGubn option:selected').val(), //고객구분
				cusGubnValue: $('#cusGubnValue').val(),
				curPage: cuPage,
				pageScale: $('#pageScale option:selected').val(),
				sortGb: $('#sortGb option:selected').val()	// 정렬
			};

			$.ajax({
				type: "post",
				async: true,
				url: url,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(param),
				success: function (result) {
					if (result.retCode == "0000") {

						fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
						ajaxPaging(result, 'PageArea');   // 페이징
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

	// 데이터 새로고침
	function fnGrid(result, obj) {
		var nowDays = new Date();
		var str = '';
		$('#pageCnt').val(result.PAGE_SCALE);
		$('#totCnt').text(numberToCommas(result.count));
		$('#totAmt').text(numberToCommas(result.totAmt));
		$('#realTotAmt').text(numberToCommas(result.realTotAmt));
		$('#totUnAmt').text(numberToCommas(result.totUnAmt));
		$('#totRefundAmt').text(numberToCommas(result.rePayAmt));

		if(result.count <= 0){
			str+='<tr><td colspan="20" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
		}else{
			$.each(result.list, function(i, v){

				str+='<tr>';
				str+='<input type="hidden" id="notiMasCd" name="notiMasCd" value="' + v.notiMasCd + '">';
				str+='<td class="hidden-on-mobile">';
				str+='<input class="form-check-input table-check-child" name="checkList" type="checkbox" id="row-'+v.rn+'" value="'+v.notiMasCd+'" sveCd="'+v.notiMasSt+'" onchange="changeBGColor(this)">';
				str+='<label for="row-'+v.rn+'"><span></span></label>';
				str+='</td>';
				str+='<td>'+v.rn+'</td>';
				str+='<td>'+v.masMonth+'</td>';
				str+='<td>'+changeDateFormat(v.payDay)+'</td>';
				str+='<td><button type="button" class="btn btn-xs btn-link" onclick="cusName(\''+v.vano+'\')" value="'+v.vano+'">'+basicEscape(v.cusName)+'</button></td>';
				str+='<td>'+v.vano+'</td>';
				str+='<td class="text-danger">'+nullValueChange(v.notiMasStNm)+'</td>';
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
				str+='<td>';
				if(v.payItemName != '0'){
					str+='<button type="button" class="btn btn-xs btn-link" id="btnPop-'+v.rn+'" notiMasCd="'+v.notiMasCd+'" vaNo="'+v.vano+'" cusName="'+v.cusName+'" masMonth="'+v.masMonth+'" onclick="payPop('+v.rn+');">'+v.itemCnt+'건</button>';
				}
				str+='</td>';
				str+='<td class="text-right">'+numberToCommas(v.amt)+'</td>';
				str+='<td class="text-right">'+numberToCommas(v.rcpAmt)+'</td>';
				str+='<td class="text-right text-danger">'+numberToCommas(v.unAmt)+'</td>';
				str+='<td class="text-right">'+numberToCommas(v.vasAmt)+'</td>';
				/*str+='<td class="text-right">'+numberToCommas(v.ocdAmt)+'';
				if(v.ocdAmt > 0 && v.notiMasSt == 'PA03'){
					var payDays = new Date(v.payDay.substring(0,4),v.payDay.substring(4,6)-1,v.payDay.substring(6,8));
					var gapDays = Math.floor((nowDays.getTime()-payDays.getTime())/(1000*60*60*24));
					if(gapDays <= 60){
						str+='<button type="button" class="btn btn-xs btn-outline-secondary" onclick="fnRefund(\''+v.notiMasCd+'\')">결제취소</button>';
					}
				}*/
				str+='</td>';
				str+='<td class="text-right">'+numberToCommas(v.dcsAmt)+'</td>';
				str+='<td class="text-right">'+numberToCommas(v.dcdAmt)+'</td>';
				str+='<td class="text-right">'+numberToCommas(v.rePayAmt)+'</td>';
				str+='</tr>';
			});
		}
		$('#' + obj).html(str);
	}

	// 페이징 버튼
	function list(page) {
		$('#pageNo').val(page);
		fnSearch(page);
	}

	// validation
	function fnValidation() {
		if($('input:radio[name=inlineRadioOptions]:checked').val() == 'month'){
			return true;
		}else {
			var startDate = replaceDot($('#fDate').val());
			var endDate = replaceDot($('#tDate').val());
			var calcDate = CalcMonth(startDate, endDate);

			if(CalcDay(startDate, endDate) > 365 ){
				swal({
					type: 'info',
					text: "최대 조회기간은 1년 입니다.",
					confirmButtonColor: '#3085d6',
					confirmButtonText: '확인'
				});
				return false;
			}
			var vdm = dateValidity(startDate, endDate);
			if (vdm != 'ok'){
				swal({
					type: 'info',
					text: vdm,
					confirmButtonColor: '#3085d6',
					confirmButtonText: '확인'
				});
				return false;
			}else{
				return true;
			}
		}
	}//fn

	function onSearchGb() {
		if ($('#searchGb option:selected').val() == "vano") {
			$('#searchvalue').val("");
		}
	}

	function prevDate(num){
		var toDay = $.datepicker.formatDate("yy.mm.dd", new Date() );
		var vdm = dateValidity($('#fDate').val(), toDay);
		$('#tDate').val(toDay);
		$('#fDate').val(monthAgo(toDay,num));
		$('.btn-preset-month').removeClass('active');
		$('#pMonth'+num+'').addClass('active');
	}

	function pageChange() {
		cuPage = 1;
		fnSearch(cuPage);
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
		$('#' + obj).html(str);
	}

	function fnCusGubn(str) {
		cusGubn = str.value;
		if(str.value == 'all'){
		}else{
			$('#cusGubnValue').attr('placeholder','콤마(,) 구분자로 다중검색이 가능합니다.');
		}
	}

	// 파일저장
	function fn_fileSave() {
		fncClearTime();
		var alertResult = false;
		if($('#totCnt').text() <= 0){
			swal({
				type: 'info',
				text: '다운로드할 데이터가 없습니다.',
				confirmButtonColor: '#3085d6',
				confirmButtonText: '확인'
			});
			return;
		} else {
			swal({
				type: 'question',
				html: "다운로드 하시겠습니까?",
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: '확인',
				cancelButtonText: '취소',
				onAfterClose: function() {
					if(alertResult) {
						fnReceipFile();
					}
				}
			}).then(function(result) {
				if (result.value) {
					alertResult = true;
				}
			});
		}
	}

	// 수납내역 엑셀파일생성
	function fnReceipFile() {
		var checkArr = [];
		var svecdArr = [];
		$('input:checkbox[name=payItem]:checked').each(function(i) {
			checkArr.push($(this).val());
		});
		$('input:checkbox[name=deposItem]:checked').each(function(i) {
			svecdArr.push($(this).val());
		});
		if($('input:radio[name=inlineRadioOptions]:checked').val() == 'month'){
			$('#startDate').val('');
			$('#endDate').val('');
			$('#masMonth').val($('#yearsBox option:selected').val() + ""+ $('#monthBox option:selected').val());
		}else{
			$('#masMonth').val('');
			startDate = replaceDot($('#fDate').val());
			endDate = replaceDot($('#tDate').val());
			$('#startDate').val(replaceDot($('#fDate').val()));
			$('#endDate').val(replaceDot($('#tDate').val()));
		}
		var checkArr2 = [];
		$('input:checkbox[name=checkList]:checked').each(function(i) {
			checkArr2.push($(this).val());
		});

		$('#fSearchGb').val($('#searchGb option:selected').val());
		$('#fSearchValue').val($('#searchValue').val());
		$('#fCusGubn').val($('#cusGubn option:selected').val());
		$('#fCusGubnValue').val($('#cusGubnValue').val());
		$('#fSortGb').val($('#sortGb option:selected').val());
		$('#payList').val(checkArr);
		$('#svecdList').val(svecdArr);
		$('#payGb1').val($('#payRadio1').is(":checked") ? "ALL" : "NOTALL");
		$('#svecdGb1').val($('#deposRadio1').is(":checked") ? "ALL" : "NOTALL");
		$('#fDateGb').val($('input:radio[name=inlineRadioOptions]:checked').val() == 'month' ? 'M' : 'D');
		$('#payNoGb').val($('#payRadio2').is(":checked") ? "CHK" : null);
		// 다운로드
		var url = '/org/receiptMgnt/rcpHistoryExcelDown?' + $('#fileForm').serialize();
		window.open(url, '_parent');
	}

	// 영수증 인쇄
	function fnPrint(billSect) {
		var count = $('input:checkbox[name=checkList]:checked').length;
		var checkArr = [];
		var printMsg = "";
		var billSectPrintMsg = "";
		var flag = true;
		var alertResult = false;

		if(count == 0) {
			swal({
				type: 'info',
				text: "선택된 인쇄건이 없습니다.",
				confirmButtonColor: '#3085d6',
				confirmButtonText: '확인'
			});
		}else{
			if(billSect == "cut"){
				billSectPrintMsg = "영수증";
				printMsg = "건의 "+billSectPrintMsg+"를 인쇄하시겠습니까?";
			}else{
				billSectPrintMsg = "증명서";
				printMsg = "건의 "+billSectPrintMsg+"를 인쇄하시겠습니까?";
			}

			swal({
				type: 'question',
				html: count + "" + printMsg,
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: '확인',
				cancelButtonText: '취소',
				reverseButtons: true,
				onAfterClose: function() {
					if(alertResult) {
						receiptVillFile(checkArr, billSect);
					}
				}
			}).then(function(result) {
				if(result.value == true) {
					$('input:checkbox[name=checkList]:checked').each(function(i) {
						if(nullValueChange($(this).val()) != 'null'){
							checkArr.push($(this).val());
						}

						if($(this).attr('sveCd') == 'PA03' || $(this).attr('sveCd') == 'PA04' || $(this).attr('sveCd') == 'PA05'){
						}else{
							swal({
								type: 'info',
								text: "납부된 수납 건에 대해서만 "+billSectPrintMsg+" 발급이 가능합니다.",
								confirmButtonColor: '#3085d6',
								confirmButtonText: '확인'
							});
							flag = false;
							return false;
						}
					});

					if(flag == true){
						alertResult = true;
					}
				}
			});

		}
	}

	// true 일 경우 영수증파일 생성
	function receiptVillFile(checkArr, billSect) {
		var agent = navigator.userAgent.toLowerCase();

		$('#checkListValue').val(checkArr);
		$('#sFileName').val($('#chaCd').val()+getCurrentDate());
		$('#billSect').val(billSect);
		$('#sBrowserType').val('ie');

		if(billSect == "cut"){ // 영수증
			$('#pdfMakeForm').submit();
		}else{ // 증명서
			$("#popup-choose-certificate").modal({
				backdrop:'static',
				keyboard : false
			});
		}
	}

	function payPop(idx) {
		var notiMasCd = $('#btnPop-' + idx + '').attr('notiMasCd');
		var cusName = $('#btnPop-' + idx + '').attr('cusName');
		var masMonth = replaceDot($('#btnPop-' + idx + '').attr('masMonth'));
		var vaNo = $('#btnPop-' + idx + '').attr('vaNo');

		fn_reset_scroll();

		var url = "/org/receiptMgnt/getReceiptDetailAjax";
		var param = {
			notiMasCd: notiMasCd,
			masMonth: masMonth
		};

		$.ajax({
			type: "post",
			url: url,
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(param),
			success: function (result) {
				if (result.retCode == "0000") {
					$('#popCusName').text(cusName);
					$('#popMasMonth').text(formatYearMonth(masMonth));
					$('#popMasVano').text(vaNo);

					var str = '';
					var popTotalAmt = 0;
					var popTotalRcpAmt = 0;
					if (result.count <= 0) {
						str += '<tr><td colspan="3" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
					} else {
						$.each(result.list, function (i, v) {
							str += '<tr>';
							str += '<td>' + v.payItemName + '</td>';
							str += '<td style="text-align: right;">' + numberToCommas(v.payItemAmt) + '</td>';
							str += '<td style="text-align: right;">' + numberToCommas(v.rcpAmt) + '</td>';
							str += '<td>' + (v.ptrItemRemark == null ? "" : v.ptrItemRemark) + '</td>';
							str += '</tr>';
						});

						var foot = "";
						foot += '<tr>';
						foot += '<td class="summary-bg">총액</td>';
						foot += '<td class="summary-bg" style="text-align: right;">' + numberToCommas(result.totalPayAmt) + '</td>';
						foot += '<td class="summary-bg" style="text-align: right;">' + numberToCommas(result.totalRcpAmt) + '</td>';
						foot += '<td class="summary-bg"></td>';
						foot += '</tr>';
						$('#popResultFoot').html(foot);

					}

					$('#popResultBody').html(str);
					$("#popup-payment-unit-list").modal({
						backdrop: 'static',
						keyboard: false
					});
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

	// sms 고지
	function fn_smsNoti() {
		fn_reset_scroll();

		var url = "/org/notiMgmt/selSmsUseYn";
		var param = {
		};

		$.ajax({
			type : "post",
			async : true,
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success: function(data){
				if(data.map == 'Y') {			// sms 고지
					fn_smsNotification('rec');
				} else if(data.map == 'N') {	// 문자메시지 서비스 이용등록
					fn_smsServiceInsPage();
				} else if(data.map == 'W') {	// 문자메시지 서비스 이용등록 신청중
					swal({
						type: 'info',
						text: '문자메시지 서비스 이용등록 신청중입니다.',
						confirmButtonColor: '#3085d6',
						confirmButtonText: '확인'
					});
				} else if(data.map == 'C') {
					swal({
						type: 'info',
						text: '문자메시지 서비스 이용 취소상태입니다. 재사용은 관리자에게 문의바랍니다.',
						confirmButtonColor: '#3085d6',
						confirmButtonText: '확인'
					});
				}
			}
		});
	}

	// 이메일 고지
	function fn_emailNoti() {
		fn_reset_scroll();

		var url = "/org/notiMgmt/selEmailUseYn";
		var param = {
		};

		$('#billGubn').val("01").prop("selected", true);

		$.ajax({
			type : "post",
			async : true,
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success: function(data){
				if(data.cnt > 0) {
					fn_emailNotiPage('rec');
				} else {
					swal({
						type: 'info',
						text: '고지서 설정 후 사용가능합니다.',
						confirmButtonColor: '#3085d6',
						confirmButtonText: '확인'
					});
				}
			}
		});
	}

	//modal 페이징 버튼
	function modalList(num, val) {
		if (val == '1') {		        // SMS 고지 modal 페이징
			fn_selSms(num);
		} else if (val == '2') {        // EMAIL 고지 modal 페이징
			fn_selEmail(num);
		} else {                    	// 알림톡 고지 modal 페이징
			fn_selAtList(num);
		}
	}

	function fn_atNoti() {
		fn_reset_scroll();

		var url = "/org/notiMgmt/selAtUseYn";
		var param = {};

		$.ajax({
			type: "post",
			async: true,
			url: url,
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(param),
			success: function (data) {
				if (data.map == 'Y') {
					$("#atRow-th").prop("checked", false);
					fn_atNotification();
				} else if (data.map == 'N') {
					fn_atModalOpen();
				} else if (data.map == 'W') {
					swal({
						type: 'info',
						html: '알림톡 서비스 이용등록 신청중입니다.<p>승인 완료 후 담당자 연락처로 연락드리겠습니다.',
						confirmButtonColor: '#3085d6',
						confirmButtonText: '확인'
					});
				} else if (data.map == 'C') {
					swal({
						type: 'info',
						text: '알림톡 서비스 이용 취소상태입니다. 재사용은 관리자에게 문의바랍니다.',
						confirmButtonColor: '#3085d6',
						confirmButtonText: '확인'
					});
				} else if (data.map == 'D') {
					fn_atModalOpen();
				}
			}
		});
	}
</script>

<form id="pdfMakeForm" name="pdfMakeForm" method="post" action="/org/receiptMgnt/pdfMakeRcpBill">
	<input type="hidden" id="billSect" name="billSect"/>
	<input type="hidden" id="checkListValue" name="checkListValue"/>
	<input type="hidden" name="sFileName" id="sFileName" />
	<input type="hidden" id="sBrowserType" name="sBrowserType" />
</form>

<form id="fileForm" name="fileForm" method="post" action="/org/receiptMgnt/excelDown">
	<input type="hidden" id="masMonth" name="masMonth"/>
	<input type="hidden" id="startDate" name="startDate"/>
	<input type="hidden" id="endDate" name="endDate"/>
	<input type="hidden" id="payList" name="payList"/>
	<input type="hidden" id="svecdList" name="svecdList"/>
	<input type="hidden" id="payItemCd" name="payItemCd"/>
	<input type="hidden" id="fSearchGb" name="fSearchGb"/>
	<input type="hidden" id="fSearchValue" name="fSearchValue"/>
	<input type="hidden" id="fCusGubn" name="fCusGubn"/>
	<input type="hidden" id="fCusGubnValue" name="fCusGubnValue"/>
	<input type="hidden" id="fSortGb" name="fSortGb"/>
	<input type="hidden" id="payGb1" name="payGb1"/>
	<input type="hidden" id="SvecdGb1" name="SvecdGb1"/>
	<input type="hidden" id="fDateGb" name="fDateGb"/>
	<input type="hidden" id="payNoGb" name="payNoGb"/>
</form>
<form name="cancelCardForm" id="cancelCardForm" method="post" action="/org/receiptMgnt/selectRcpMas">
	<input type="hidden" id="cancelRcpMasCd" name="cancelRcpMasCd"/>
	<input type="hidden" id="cancelNotiMasCd" name="cancelNotiMasCd"/>
</form>
<input type="hidden" id="vano" name="vano"/>
<input type="hidden" id="userName" name="userName" value="${userName}" />
<input type="hidden" id="chaCd" name="chaCd" value="${orgSess.chacd}" />
<input type="hidden" id="cusGubn1" name="cusGubn1" value="${orgSess.cusGubn1}" />
<input type="hidden" id="cusGubn2" name="cusGubn2" value="${orgSess.cusGubn2}" />
<input type="hidden" id="cusGubn3" name="cusGubn3" value="${orgSess.cusGubn3}" />
<input type="hidden" id="cusGubn4" name="cusGubn4" value="${orgSess.cusGubn4}" />
<input type="hidden" id="pageCnt" name="pageCnt" value="${map.PAGE_SCALE}">
<input type="hidden" id="pageNo"  name="pageNo"  value="1">

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link active" href="#">수납내역 조회</a>
			<a class="nav-link" href="/org/receiptMgnt/paymentList">입금내역 조회</a>
			<a class="nav-link" href="/org/receiptMgnt/payItemList">항목별 입금내역</a>
			<a class="nav-link" href="/org/receiptMgnt/directReceiptReg">수기수납내역</a>
			<a class="nav-link" href="/org/receiptMgnt/refundReceipt">수기환불내역</a>
			<a class="nav-link" href="/org/receiptMgnt/cashReceipt">현금영수증</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div id="breadcrumb-in-title-area" class="row align-items-center">
				<div class="col-12 text-right">
					<img src="/assets/imgs/common/icon-home.png" class="mr-2">
					<span> > </span> <span class="depth-1">수납관리</span>
					<span> > </span> <span class="depth-2 active">수납내역 조회</span>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2>수납내역 조회</h2>
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
					<p>
						청구 내용에 따라 수납상태를 볼 수 있는 화면입니다. 항목 건 수별 수납 금액 및 미납금액, 납부 수단을 확인할 수 있습니다.<br/>
						청구등록을 이용하지 않는 경우 확인되지 않는 정보가 있을 수 있습니다.
					</p>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="search-box">
			<form>
				<div id="monthly-or-duration" class="row radio-seㅇlecter">
					<legend class="col-form-label col col-md-1 col-sm-2 col-2 pt-0">조회방식</legend>
					<div class="col col-md-10 col-sm-10 col-9">
						<div class="form-check form-check-inline">
							<input class="form-check-input lookup-by-month" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="month" checked="checked">
							<label class="form-check-label" for="inlineRadio1"><span></span>청구월별</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input lookup-by-range" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="period">
							<label class="form-check-label" for="inlineRadio2"><span></span>수납일자별</label>
						</div>
					</div>
				</div>

				<div id="lookup-by-month" class="row">
					<div class="col-md-11 offset-md-1 offset-sm-2 offset-2 col-10 form-inline year-month-dropdown">
						<%-- <select class="form-control" id="yearsFullBox">
						<option selected="selected" value="ALL">전체월</option> --%>
						<select class="form-control" id="yearsBox">
							<option>선택</option>
						</select>
						<span class="ml-1 mr-1">년</span>
						<select class="form-control" id="monthBox">
							<option>선택</option>
						</select>
						<span class="ml-1">월</span>
					</div>
				</div>

				<div id="lookup-by-range" class="row" style="display: none;">
					<div class="col col-md-5 col-sm-10 offset-md-1 offset-sm-2 offset-2 form-inline">
						<div class="date-input">
							<div class="input-group">
								<input type="text" id="fDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD"
									   aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
							</div>
						</div>
						<span class="range-mark"> ~ </span>
						<div class="date-input">
							<div class="input-group">
								<input type="text" id="tDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD"
									   aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
							</div>
						</div>
					</div>
					<div class="col-md-6 col-sm-10 offset-md-0 offset-sm-2 offset-2">
						<button id="pMonth1" type="button" class="btn btn-sm btn-preset-month active" onclick="prevDate(1)">1개월</button>
						<button id="pMonth2" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(2)">2개월</button>
						<button id="pMonth3" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(3)">3개월</button>
					</div>
				</div>

				<div class="row mb-3 mt-3">
					<div class="col" style="border-top: 1px solid #d6d6d6;"></div>
				</div>

				<div class="row">
					<label class="col col-md-1 col-sm-2 col-2 col-form-label">
						검색구분 </label>
					<div class="col col-md-4 col-sm-10 col-9 form-inline">
						<select class="form-control" id="searchGb" onchange="onSearchGb();">
							<option value="cusname">고객명</option>
							<option value="vano">계좌번호</option>
						</select>
						<input class="form-control col-auto" type="text" name="searchValue" id="searchValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch();}"/>
					</div>
					<label class="col col-md-1 col-sm-2 col-2 col-form-label">수납상태 </label>
					<div class="col col-md-6 col-sm-11 col-10 form-inline">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payAll" id="payRadio1" value="ALL">
							<label class="form-check-label" for="payRadio1"><span class="mr-1"></span>전체</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio2" value="PA02">
							<label class="form-check-label" for="payRadio2"><span class="mr-1"></span>미납</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio3" value="PA04">
							<label class="form-check-label" for="payRadio3"><span class="mr-1"></span>일부납</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio4" value="PA05">
							<label class="form-check-label" for="payRadio4"><span class="mr-1"></span>초과납</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio5" value="PA03">
							<label class="form-check-label" for="payRadio5"><span class="mr-1"></span>완납</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio6" value="PA06">
							<label class="form-check-label" for="payRadio6"><span class="mr-1"></span>환불</label>
						</div>
					</div>
				</div>

				<div class="row">
					<label class="col col-md-1 col-sm-2 col-2 col-form-label">
						고객구분 </label>
					<div class="col col-md-4 col-sm-10 col-9 form-inline">
						<select class="form-control" id="cusGubn" onchange="fnCusGubn(this);">
						</select>
						<div class="input-with-magnet">
							<input class="form-control" type="text" id="cusGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch();}"/>
						</div>
					</div>
					<label class="col col-md-1 col-sm-2 col-2 col-form-label">
						입금수단 </label>
					<div class="col col-md-6 col-sm-11 col-10 form-inline">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposAll" id="deposRadio1" value="ALL">
							<label class="form-check-label" for="deposRadio1"><span class="mr-1"></span>전체</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio2" value="VAS">
							<label class="form-check-label" for="deposRadio2"><span class="mr-1"></span>가상계좌</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio3" value="DCS">
							<label class="form-check-label" for="deposRadio3"><span class="mr-1"></span>현금</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio4" value="DVA">
							<label class="form-check-label" for="deposRadio4"><span class="mr-1"></span>계좌입금</label>
						</div>
						<%--<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio5" value="OCD">
							<label class="form-check-label" for="deposRadio5"><span class="mr-1"></span>온라인카드</label>
						</div>--%>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio6" value="DCD">
							<label class="form-check-label" for="deposRadio6"><span class="mr-1"></span>오프라인카드</label>
						</div>
					</div>
				</div>
				<div class="row form-inline mt-3">
					<div class="col-12 text-center">
						<input type="button" class="btn btn-primary btn-wide" onclick="fnSearch()" onkeypress="if(event.keyCode == 13){fnSearch();}" value="조회" />
					</div>
				</div>
			</form>
		</div>
	</div>


	<div class="container">
		<div id="book-keeping-list" class="list-id">
			<div class="table-option row mb-2">
				<div class="col-md-7 col-sm-12 form-inline" id="paAll">
					<span class="amount mr-2">조회결과 [<em class="font-blue" id="totCnt"><fmt:formatNumber pattern="#,###" value="${map.count}"/></em>건]</span>
					<span class="amount mr-2">총 수납금액 [<em class="font-blue" id="totAmt"><fmt:formatNumber pattern="#,###" value="${map.totAmt}"/></em>원]</span>
					<span class="amount mr-2">실 수납금액 [<em class="font-blue" id="realTotAmt"><fmt:formatNumber pattern="#,###" value="${map.realTotAmt}"/></em>원]</span>
					<span class="amount mr-2">미납금액 [<em class="font-blue" id="totUnAmt"><fmt:formatNumber pattern="#,###" value="${map.totUnAmt}"/></em>원]</span>
					<span class="amount">환불금액 [<em class="font-blue" id="totRefundAmt"><fmt:formatNumber pattern="#,###" value="${map.rePayAmt}"/></em>원]</span>
				</div>
				<div class="col-md-5 col-sm-12 text-right mt-1">
					<select class="form-control" id="sortGb" onchange="pageChange();">
						<option value="RG">수납일자순 정렬</option>
						<option value="NM">고객명순 정렬</option>
					</select>
					<select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
						<option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
						<option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
						<option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
						<option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
						<option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
					</select>
					<button class="btn btn-sm btn-d-gray hidden-on-mobile" type="button" onclick="fn_fileSave()">파일저장</button>
				</div>
			</div>

			<div class="row">
				<div class="table-responsive pd-n-mg-o col mb-3">
					<table class="table table-sm table-hover table-primary">
						<colgroup>
							<col class="hidden-on-mobile" width="52">
							<col width="68">
							<col width="100">
							<col width="120">
							<col width="120">
							<col width="120">

							<c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}"><col width="130"></c:if>
							<c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}"><col width="130"></c:if>
							<c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}"><col width="130"></c:if>
							<c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}"><col width="130"></c:if>

							<col width="100">
							<col width="130">
							<col width="130">
							<col width="130">
							<col width="130">
							<col width="130">
							<col width="130">
							<col width="140">
							<col width="130">
						</colgroup>

						<thead>
						<tr>
							<th class="hidden-on-mobile" rowspan="2">
								<input class="form-check-input table-check-parents" type="checkbox" id="row-th" value="option2">
								<label for="row-th"><span></span></label>
							</th>
							<th rowspan="2">NO</th>
							<th rowspan="2">청구월</th>
							<th rowspan="2">수납일자</th>
							<th rowspan="2">고객명</th>
							<th rowspan="2">가상계좌번호</th>
							<th rowspan="2">수납상태</th>

							<c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}"><th rowspan="2">${orgSess.cusGubn1}</th></c:if>
							<c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}"><th rowspan="2">${orgSess.cusGubn2}</th></c:if>
							<c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}"><th rowspan="2">${orgSess.cusGubn3}</th></c:if>
							<c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}"><th rowspan="2">${orgSess.cusGubn4}</th></c:if>

							<th rowspan="2">항목건수</th>
							<th rowspan="2">청구금액(원)</th>
							<th rowspan="2">수납금액(원)</th>
							<th rowspan="2">미납금액(원)</th>
							<th colspan="4">납부수단 상세</th>
							<th rowspan="2">환불액(원)</th>
						</tr>
						<tr>
							<th>가상계좌(원)</th>
							<%--<th>온라인카드(원)</th>--%>
							<th>현금(원)</th>
							<th>오프라인카드(원)</th>
						</tr>
						</thead>
						<tbody id="resultBody">

						</tbody>
					</table>
				</div>
			</div>

			<div class="row mb-2 hidden-on-mobile">
				<div class="col-6"></div>
				<div class="col-6 text-right">
					<button class="btn btn-sm btn-d-gray" id="btn-sms-notification-popup" onclick="fn_smsNoti();">문자메시지고지</button>
					<button class="btn btn-sm btn-d-gray" id="btn-email-notification-popup" onclick="fn_emailNoti();">E-MAIL고지</button>
					<button class="btn btn-sm btn-d-gray" id="btn-at-notification-popup" onclick="fn_atNoti();">알림톡고지</button>
					<button class="btn btn-sm btn-d-gray" onclick="fnPrint()">증명서인쇄</button>
					<button class="btn btn-sm btn-d-gray" onclick="fnPrint('cut')">영수증인쇄</button>
				</div>
			</div>

			<jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />
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
					<h6>■ 수납상태</h6>
					<ul>
						<li>미납 : 청구 금액의 전부를 납부하지 않은 상태</li>
						<li>일부납 : 청구 금액의 일부를 납부한 상태</li>
						<li>초과납 : 청구 금액을 초과하여 납부한 상태</li>
						<li>완납 : 청구금액과 일치한 금액을 납부한 상태</li>
						<li>환불 : 직접 수납관리 메뉴를 통해 직접 입력한 환불내역</li>
						<li class="depth-2 text-danger">※ 환불 등록의 경우 수납 내역에 기록될 뿐, 실제 환불 처리는 실행되지 않습니다.</li>
					</ul>

					<h6>■ 증명서 인쇄</h6>
					<ul>
						<li>인쇄할 수납 건 선택 후 '증명서인쇄' 버튼 클릭</li>
						<li>발행할 증명서 종류 선택 후 '증명서 인쇄' 버튼 클릭</li>
						<li class="depth-2 text-danger">※ 증명서 인쇄는 완납,일부납 및 초과납의 경우만 가능합니다.</li>
					</ul>

					<h6>■ 영수증 인쇄</h6>
					<ul>
						<li>인쇄할 수납건 선택 후 '영수증인쇄' 버튼 클릭</li>
						<li class="depth-2 text-danger">※ 영수증 인쇄는 완납 및 초과납의 경우만 가능합니다.</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<%--고객구분 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/payer-classification.jsp" flush="false" />

<%-- 문자메시지 서비스 이용등록 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-sms-service.jsp" flush="false" />

<%-- sms 고지팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/sms-notification.jsp" flush="false" />

<%-- E-mail 고지 등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/email-notification.jsp" flush="false" />

<%-- 2018.05.11 증명서인쇄 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/choose-certificate.jsp" flush="false"/>

<%-- 청구상세 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/payment-unit-list.jsp" flush="false" />

<%-- 고객정보수정 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/detail-payer-information.jsp" flush="false"/>

<%-- 알림톡 서비스 이용등록 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-at-service.jsp" flush="false"/>

<%-- 알림톡 고지 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/at-notification.jsp" flush="false"/>
