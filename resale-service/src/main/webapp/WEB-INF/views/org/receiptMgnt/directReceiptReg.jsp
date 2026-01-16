<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="ko_KR" />
<s:authentication property="principal.username" var="userId" />
<s:authentication property="principal.name" var="userName" />

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<script type="text/javascript">

	var cuPage = 1;
	var toDay = getFormatCurrentDate();
	var firstDepth = "nav-link-34";
	var secondDepth = "sub-04";

	// 수납방법코드
	var sveMap = {
			"VAS" : "가상계좌",
			"DCS" : "현금",
			"DCD" : "오프라인카드",
			"DVA" : "계좌입금", 
			"OCD" : "인터넷카드"
		};

	// 수납상태코드
	var rcpStatusMap = {
			"PA00" : "임시", 
			"PA02" : "미납", 
			"PA03" : "완납", 
			"PA04" : "일부납", 
			"PA05" : "초과납", 
			"PA06" : "환불"
		};

	/**
	 * datepicker 날짜 세팅
	 */
	function setInputDateField(obj, num, field){

		var date = new Date();
		toDay = $.datepicker.formatDate("yy.mm.dd", date );
		
	 	$('#tDate').val(toDay);

	 	if (field == "m" && num == 0) {
	 		date.setDate(1);
	 		$('#fDate').val( $.datepicker.formatDate("yy.mm.dd", date ) );
		} else if (field == "m" && num < 0) {
			var fd = new Date();
			fd.setMonth( date.getMonth()-1 );
			fd.setDate(1);
			date.setDate(1);
	 		$('#fDate').val( $.datepicker.formatDate("yy.mm.dd", fd ) );
	 		$('#tDate').val( addDate(date, "d", num, ".") );
		} else {
			$('#fDate').val( addDate(toDay, field, num, ".") );
		}
	 	
		$('.btn-preset-month').removeClass('active');
		$(obj).addClass('active');
	}

	/**
	 * 계좌번호 검색 초기화
	 */
	function onSearchGb() {
		if ($('#searchGb option:selected').val() == "vano") {
			$('#searchvalue').val("");
		}
		if ($('#searchGb2 option:selected').val() == "vano") {
			$('#searchvalue2').val("");
		}
	}

	/**
	 * 고객구분 검색 초기화
	 */
	function fnCusGubn(obj) {
		cusGubn = $(obj).val();
		if(cusGubn == 'all'){
			$('#cusGubnValue, #cusGubnValue2').attr('placeholder','검색어를 입력하세요.');
		}else{
			$('#cusGubnValue, #cusGubnValue2').attr('placeholder','콤마(,) 구분자로 다중검색이 가능합니다.');
		}
	}

	/**
	 * 파라메터 생성
	 */
	function buildParams() {
		var params = {};
		params.pageTab   =  $("#currentTab").val();
        params.curPage   =  $("#pageNo").val();
        params.pageScale =  $('#pageScale option:selected').val();

		if (params.pageTab == "direct") {
			// 수납등록 조회조건
			if ($('#yearsBox option:selected').val() != "" &&  $('#monthBox option:selected').val() != "") {
		        params.masmonth     =  $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val();
			}
			params.startDate = "";
			params.endDate = "";
	        params.cusGubn      =  $('#cusGubn option:selected').val();
	        params.cusGubnValue =  $('#cusGubnValue').val();
	        params.searchGb     =  $('#searchGb option:selected').val();
	        params.searchValue  =  $('#searchValue').val();
	        params.payitemcd    =  $('#searchDirectPayItems option:selected').val();
        	params.orderBy   =  $('#orderByDirectReg option:selected').val();
		} else {
			// 수납완료 조회조건
			if ($('#yearsBox2 option:selected').val() != "" &&  $('#monthBox2 option:selected').val() != "") {
		        params.masmonth     =  $('#yearsBox2 option:selected').val() + "" + $('#monthBox2 option:selected').val();
			}
        	params.startDate =  replaceDot($('#fDate').val());
        	params.endDate   =  replaceDot($('#tDate').val());
	        params.cusGubn      =  $('#cusGubn_2 option:selected').val();
	        params.cusGubnValue =  $('#cusGubnValue2').val();
	        params.searchGb     =  $('#searchGb2 option:selected').val();
	        params.searchValue  =  $('#searchValue2').val();
	        params.payitemcd    =  $('#searchCompletePayItems option:selected').val();
	        var sves = new Array();
	        $(':checkbox[name=deposItem]:checked').each(function(i, check){
		        sves.push($(this).val());
		    });
	        params.svecds       =  sves;
        	params.orderBy   =  $('#orderByCompleteReg option:selected').val();
		}

		return params;
	}

	/**
	 * 화면목록 표시 건수 이벤트
	 */
	function pageChange(type) {
		cuPage = 1;
		fnSearch(cuPage);
	}

	/**
	 * 페이징 버튼
	 */
	function list(page) {
		$('#pageNo').val(page);
		fnSearch(page);
	}

	/**
	 * 수납등록/수납완료 목록 조회
	 */
	function fnSearch(page) {
		
		if(page == null || page == 'undefined' || page == ''){
			cuPage = "";
			cuPage = 1;
		}else{
			cuPage = page;
		}
		$('#pageNo').val(cuPage);

		// 수납등록/완료 조건생성
		var param = buildParams();

		if (param.pageTab == "complete") {
			// 수납완료 조회 조건
			
			if (param.startDate != "" && param.endDate != "") {
				var vdm = "ok";
				if (param.startDate == "" || param.startDate == null || param.startDate == undefined) vdm = "입금일자 시작일을 입력하세요.";
				if (param.endDate == "" || param.endDate == null || param.endDate == undefined) vdm = "입금일자 종료일을 입력하세요.";
				if (param.startDate > param.endDate) vdm = "입금일자 시작일은 종료일보다 클 수 없습니다.";
		        if (vdm != 'ok') {
		            swal({
		                type: 'info',
		                text: vdm,
		                confirmButtonColor: '#3085d6',
		                confirmButtonText: '확인'
		            });
		            return false;
		        }
			}
		}
		
		// 수납등록 화면 목록 조회
		if(param.searchGb == "vano" && param.searchValue) {
			var str = param.searchValue;
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
                    	param.searchValue = '';
                    });
                    return;
                }
			}
		}

		var url = "/org/receiptMgnt/directReceiptListAjax";
		$.ajax({
			type : "post",
			async : true,
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success : function(result) {
				if (result.retCode == "0000") {
					fnGrid(result, 'resultBody');
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

	}

	function makeTableHead() {
		$('#resultHead').empty();
		var str = '';
		str+='<tr>';
		str+='<th>';
		str+='<input class="form-check-input table-check-parents" type="checkbox" id="row-th" value="checkList" onclick="checkButtonTrigger(this)" onchange="changeBGColor(this)">';
		str+='<label for="row-th"><span></span></label>';
		str+='</th>';
		
		str+='<th>NO</th>';
		str+='<th>청구월</th>';
		if ($("#currentTab").val() == "complete") {
			str+='<th>입금일자</th>';
		}
		str+='<th>고객명</th>';
		str+='<th>가상계좌번호</th>';
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
		str+='<th>청구항목</th>';
		str+='<th>입금수단</th>';
		str+='<th>입금금액(원)</th>';
		str+='<th>청구금액(원)</th>';
		str+='<th id="thType">납부기한</th>';
		str+='</tr>';
		$('#resultHead').html(str);
	}
	
	/**
	 * ajax 결과 display
	 */
	function fnGrid(result, obj) {

		// 테이블헤더 생성
		makeTableHead();

		if (result == null) {
			$('#totCnt').text("0");
			$('#totAmt').text("0");
			$('#sumRcpAmt').text("0");
			str = '<tr><td colspan="'+$("#resultHead > tr > th").length+'" class="text-center" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
			$('#resultBody').html(str);
			return;
		}

		if (result.listCount > 0) {
			$('#totCnt').text(numberToCommas(result.listCount));
			$('#totAmt').text(numberToCommas(result.sumAmt));
			$('#sumRcpAmt').text(numberToCommas(result.sumRcpAmt));

			var str = '';
			$.each(result.list, function(i, v){
				str += '<tr>';
				str += '	<td>';
				str += '		<input class="form-check-input table-check-child" type="checkbox" name="checkList" id="row-'+i+'" value="'+v.vano+'" num="'+i+'" nmcd="'+v.notiMasCd+'" ndcd="'+v.notiDetCd+'" rmcd="'+v.rcpMasCd+'" rdcd="'+v.rcpDetCd+'" onchange="changeBGColor(this)" >';
				str += '		<label for="row-'+i+'"><span></span></label>';
				str += '	</td>';
				str += '	<td class="text-center">'+v.rn+'</td>';
				str += '	<td>'+formatYearMonth(v.masMonth)+'</td>';
				if ($("#currentTab").val() == "complete") {
					str+='<td>'+changeDateFormat(v.payDay)+' ' + getTimeFmtBasic(v.payTime) +'</td>';
				}
				
				str += '	<td><button type="button" class="btn btn-xs btn-link" onclick="cusName(this.value)" value="'+v.vano+'">'+basicEscape(v.cusName)+'</button></td>';
				str += '	<td>'+v.vano+'</td>';
				if($('#cusGubn1').val() != null && $('#cusGubn1').val() != ''){
					str += '	<td>'+nullValueChange( basicEscape(v.cusGubn1) )+'</td>';
				}
				if($('#cusGubn2').val() != null && $('#cusGubn2').val() != ''){
					str += '	<td>'+nullValueChange( basicEscape(v.cusGubn2) )+'</td>';
				}
				if($('#cusGubn3').val() != null && $('#cusGubn3').val() != ''){
					str += '	<td>'+nullValueChange( basicEscape(v.cusGubn3) )+'</td>';
				}
				if($('#cusGubn4').val() != null && $('#cusGubn4').val() != ''){
					str += '	<td>'+nullValueChange( basicEscape(v.cusGubn4) )+'</td>';
				}
				str += '	<td class="form-td-inline" code="'+v.payItemCd+'">'+v.payItemName+'</td>';

				if ($("#currentTab").val() == "direct") {
					str += '	<td class="text-center">';
					// 수납등록
					str += '		<select class="form-control w-75 mr-2" style="min-width:110px" id="selectPaymentType'+i+'" name="selectPaymentType">';
					str += '			<option value="">선 택</option>';
					str += '			<option value="DCD" '+ (v.sveCd == 'DCD' ? 'selected' : '') +'>오프라인카드</option>';
					str += '			<option value="DVA" '+ (v.sveCd == 'DVA' ? 'selected' : '') +'>계좌입금</option>';
					str += '			<option value="DCS" '+ (v.sveCd == 'DCS' ? 'selected' : '') +'>현금</option>';
					str += '		</select>';
					str += '	</td>';
				} else {
					str += '	<td class="text-center" code="'+v.sveCd+'">';
					// 수납완료
					str += sveMap[v.sveCd];
					str += '	</td>';
				}

				if ($("#currentTab").val() == "direct") {
					str += '	<td class="text-right" code="'+v.rcpMasCd+'">';
					// 수납등록
					var inputAmt = (v.rcpPayItemAmt == 0 ? v.payItemAmt : (v.payItemAmt - v.rcpPayItemAmt) ); 
					str += '		<input type="text" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)" class="form-control text-right" id="payItemAmt-'+i+'" name="payItemAmt" value="'+numberToCommas(inputAmt)+'" style="width: 120px !important;">';
					str += '	</td>';
				} else {
					str += '	<td class="text-right text-success" code="'+v.rcpDetCd+'">';
					// 수납완료
					str += numberToCommas(v.rcpPayItemAmt);
					str += '	</td>';
				}

				str += '	<td class="text-right font-blue">'+numberToCommas(v.payItemAmt)+'</td>';
				str += '	<td>'+changeDateFormat(v.startDate)+' ~ '+changeDateFormat(v.endDate)+'</td>';
				str += '</tr>';
			}); // end $.each
		} else {
			$('#totCnt').text("0");
			$('#totAmt').text("0");
			$('#sumRcpAmt').text("0");
			str = '<tr><td colspan="'+$("#resultHead > tr > th").length+'" class="text-center" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
		}
		
		$('#resultBody').html(str);
	}

	/**
	 * 엑셀 파일저장
	 */
	function fn_fileSave() {
		// 수납등록/완료 조건생성
		var params = buildParams();

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
	        var url = '/org/receiptMgnt/directReceiptExcelDown?' + $.param(params);
	        window.open(url, '_parent');
		});
	}

	/**
	 * 고객정보 상세
	 */
	function cusName(vano){
		fnDetail(vano);
	}

	/**
	 * 수납등록 저장/수정 처리
	 */
	function directPayment() {

		var checkedCnt = $(':checkbox[name="checkList"]:checked').length;

		if (checkedCnt > 0) {

			swal({
				type: 'question',
				html: "선택된 ["+checkedCnt+"]건을 저장하시겠습니까?",  
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: '확인',
				cancelButtonText: '취소'
			}).then(function(result) { 
				if (!result.value) {
				    return;
		        }

				var successCnt = 0;
				var paramArray = new Array();
				var rcpAmtMap = {};
				var payday = replaceDot($('#regPaymentDate').val());
				$(':checkbox[name="checkList"]:checked').each(function(idx, row){
					var rowId       = $(row).attr("num");
					var vano        = $(row).val().toString().replace(/\-/gi, "");
					var notiMasCd   = $(row).attr("nmcd");
					var notiDetCd   = $(row).attr("ndcd");
					var rcpMasCd    = $(row).attr("rmcd");
					var rcpDetCd    = $(row).attr("rdcd");
					var sveCd       = $('#selectPaymentType'+rowId+' option:selected').val();
					var inputRcpAmt = $('#payItemAmt-'+rowId+'').val();

					if (sveCd != "" && inputRcpAmt != "") {
						successCnt++;
						var param = {};
						param.notiMasCd   = notiMasCd;
						param.notiDetCd   = notiDetCd;
						param.vano        = vano;
						param.sveCd       = sveCd;
						param.payday      = payday;
						param.inputRcpAmt = new Number(uncomma(inputRcpAmt));
						paramArray.push(param);

						// 입금액 계산 처리
						var amt = (rcpAmtMap[notiMasCd] == null ? 0 : rcpAmtMap[notiMasCd] );
						amt = amt + param.inputRcpAmt;
						rcpAmtMap[notiMasCd] = amt;
					}
				});

				if (successCnt == checkedCnt) {
					var params = {};
					params.rcpList = paramArray;
					params.rcpAmtMap = rcpAmtMap;
					
					var url = "/org/receiptMgnt/directReceiptSaveAjax";
					$.ajax({
						type : "post",
						async : true,
						url : url,
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(params),
						success : function(result) {
							if (result.retCode == "9999") {
								swal({
							       type: 'error',
							       text: result.retMsg,
							       confirmButtonColor: '#3085d6',
							       confirmButtonText: '확인'
								});
							} else {
								if (result.retCode == "0000") {
									updateSettings();
								} else {
									swal({
								       type: 'warning',
								       text: result.retMsg,
								       confirmButtonColor: '#3085d6',
								       confirmButtonText: '확인'
									});
								}
							}

							// 목록 새로고침
							fnSearch($("#pageNo").val());
						}
					});
					
				} else {
					swal({
						type: 'error',
						text: '입금수단을 선택해주세요.',
						confirmButtonColor: '#3085d6',
						confirmButtonText: '확인'
					});
				}
		        
			});
		} else {
			swal({
				type: 'info',
				text: '저장할 입금내역을 선택하세요.',
				confirmButtonColor: '#3085d6',
				confirmButtonText: '확인'
			});
		}
	}

	/**
	 * 수납완료 취소 처리여부 확인
	 */
	function cancelPayments(flag) {
		if (flag == 'all') {
			// 전부 취소
			swal({
				type: 'question',
				text: '조회된 ['+$("#totCnt").text()+']건의 수납을 일괄 취소하시겠습니까?',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: '확인',
				cancelButtonText: '취소'
			}).then(function(confirm){
				if (confirm.value) {
					// 일괄취소
					cancelPaymentsProcced(null);
				} else {
					// cancel
					return;
				}
			});
		} else {
			// 선택 취소
			var checkedCnt = $(':checkbox[name="checkList"]:checked').length;

			if (checkedCnt == 0){
				swal({
					type: 'warning',
					text: '취소할 수납건을 선택해 주세요.',
					confirmButtonColor: '#3085d6',
					confirmButtonText: '확인'
				});
				return;
		    } else {
				swal({
					type: 'question',
					text: '선택하신 ['+checkedCnt+']건의 수납을 취소하시겠습니까?',
					showCancelButton: true,
					confirmButtonColor: '#3085d6',
					cancelButtonColor: '#d33',
					confirmButtonText: '확인',
					cancelButtonText: '취소'
				}).then(function(confirm){
					if (confirm.value) {
						var rcpList = new Array();
						$(':checkbox[name="checkList"]:checked').each(function(idx, row){
							var notiMasCd   = $(row).attr("nmcd");
							var notiDetCd   = $(row).attr("ndcd");
							var rcpMasCd    = $(row).attr("rmcd");
							var rcpDetCd    = $(row).attr("rdcd");
							var map = {};
							map.notiMasCd   = notiMasCd;
							map.notiDetCd   = notiDetCd;
							map.rcpMasCd    = rcpMasCd;
							map.rcpDetCd    = rcpDetCd;
							rcpList.push(map);
						});
						// 선택취소
						cancelPaymentsProcced(rcpList);
					} else {
						// cancel
						return;
					}
				});
				return;
		    }
		}
	}

	/**
	 * 수납완료 항목 취소 처리
	 */
	function cancelPaymentsProcced(rcpList) {

		// 수납등록/완료 조건생성
		var params = buildParams();
		params.rcpList = rcpList;

		var url = "/org/receiptMgnt/directReceiptCancelAjax";
		$.ajax({
			type : "post",
			async : true,
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(params),
			success : function(result) {
				if (result.retCode == "9999") {
					swal({
				       type: 'error',
				       text: result.retMsg,
				       confirmButtonColor: '#3085d6',
				       confirmButtonText: '확인'
					});
				} else {
                    var getRetMsg = result.retMsg;
                    var retMsg = getRetMsg.replace(/\n/, "<p>");

                    swal({
                        type: 'info',
                        html: retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
				}

				// 목록 새로고침
				fnSearch($("#pageNo").val());
			}
		});
	}
</script>

<input type="hidden" id="cusGubn1" name="cusGubn1" value="${orgSess.cusGubn1}" />
<input type="hidden" id="cusGubn2" name="cusGubn2" value="${orgSess.cusGubn2}" />
<input type="hidden" id="cusGubn3" name="cusGubn3" value="${orgSess.cusGubn3}" />
<input type="hidden" id="cusGubn4" name="cusGubn4" value="${orgSess.cusGubn4}" />
<input type="hidden" id="amtChkTy" name="amtChkTy" value="${orgSess.amtChkTy}" />
<input type="hidden" id="pageCnt" name="pageCnt" value="${map.pageScale}">
<input type="hidden" id="pageNo"  name="pageNo"  value="1">
<input type="hidden" id="currentTab"  name="currentTab"  value="direct">

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link" href="/org/receiptMgnt/receiptList">수납내역 조회</a>
			<a class="nav-link" href="/org/receiptMgnt/paymentList">입금내역 조회</a>
			<a class="nav-link" href="/org/receiptMgnt/payItemList">항목별 입금내역</a>
			<a class="nav-link active" href="#">수기수납내역</a>
			<a class="nav-link" href="/org/receiptMgnt/refundReceipt">수기환불내역</a>
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
					<span class="depth-2 active">수기수납내역</span>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2>수기수납내역</h2>
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
					<p class="cashDct">수납한 내역을 수기로 등록할 수 있는 화면입니다.</p>
					<p class="refundDct" style="display: none;">수기 등록으로 수납이 완료된 내역을 확인할수 있으며, 등록된 수납을 취소할 수 있는 화면입니다.</p>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="tab-selecter type-3">
					<ul class="nav nav-tabs" role="tablist">
						<li class="nav-item"><a id="showCash" class="nav-link active" data-toggle="tab" href="#cash-on-desk">수납등록</a></li>
						<li class="nav-item"><a id="showRefund" class="nav-link" data-toggle="tab" href="#direct-refund">수납완료</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<c:set var="minYear" value="${fn:substring(masMonth.minmonth, 0, 4)}" />
	<c:set var="maxYear" value="${fn:substring(masMonth.maxmonth, 0, 4)}" />
	<c:set var="maxMonth" value="${fn:substring(masMonth.maxmonth, 4, 6)}" />
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
								<option value="">선택</option>
								<c:forEach var="cur" begin="${minYear}" end="${maxYear}" step="1">
									<c:set var="year" value="${maxYear - cur + minYear}" />
									<option value="${year}" <c:if test="${maxYear eq year}">selected="selected"</c:if>>${year}</option>
								</c:forEach>
							</select>
							<span class="ml-1 mr-1">년</span>
							<select class="form-control" id="monthBox">
								<option value="">선택</option>
							</select>
							<span class="ml-1 mr-auto">월</span>
						</div>
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							청구항목
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control col" id="searchDirectPayItems" style="max-width: 100%; margin-right: 0;" onchange="">
								<option value="">청구항목 선택</option>
							<c:forEach var="item" items="${payItems}" varStatus="status">
								<option value="${item.payitemcd}">${item.payitemname} - ${item.payitemcd}</option>
							</c:forEach>
							</select>
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
							<input class="form-control" type="text" name="searchValue" id="searchValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch(1);}"/>
						</div>
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							고객구분
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control" id="cusGubn" onchange="fnCusGubn(this);">
								<option value="all" selected="selected">전체</option>
								<c:if test="${not empty orgSess.cusGubn1}"><option value="cusGubn1">${orgSess.cusGubn1}</option></c:if>
								<c:if test="${not empty orgSess.cusGubn2}"><option value="cusGubn2">${orgSess.cusGubn2}</option></c:if>
								<c:if test="${not empty orgSess.cusGubn3}"><option value="cusGubn3">${orgSess.cusGubn3}</option></c:if>
								<c:if test="${not empty orgSess.cusGubn4}"><option value="cusGubn4">${orgSess.cusGubn4}</option></c:if>
							</select>
							<div class="input-with-magnet">
								<input class="form-control" type="text" id="cusGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch(1);}"/>
							</div>
						</div>
					</div>

					<div class="row form-inline mt-3">
						<div class="col-12 text-center">
							<button type="button" class="btn btn-primary btn-wide" onclick="fnSearch(1);">조회</button>
						</div>
					</div>
				</form>
			</div>
		</div>

		<div id="direct-refund" class="tab-pane fade show">
			<div class="search-box">
				<form>
					<div class="row">
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							청구월
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control" id="yearsBox2">
								<option value="">선택</option>
								<c:forEach var="cur" begin="${minYear}" end="${maxYear}" step="1">
									<c:set var="year" value="${maxYear - cur + minYear}" />
									<option value="${year}" <c:if test="${maxYear eq year}">selected="selected"</c:if>>${year}</option>
								</c:forEach>
							</select>
							<span class="ml-1 mr-1">년</span>
							<select class="form-control" id="monthBox2">
								<option value="">선택</option>
							</select>
							<span class="ml-1">월</span>
						</div>
					</div>
					<div class="row">
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							입금일자
						</label>
						<div class="col col-md-4 col-sm-8 col-8 form-inline">
							<div class="date-input">
								<label class="sr-only" for="fDate">From</label>
								<div class="input-group">
									<input type="text" id="fDate" style="min-width:70px" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
								</div>
							</div>
							<span class="range-mark"> ~ </span>
							<div class="date-input">
								<label class="sr-only" for="tDate">To</label>
								<div class="input-group">
									<input type="text" id="tDate" style="min-width:70px" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
								</div>
							</div>
						</div>
						<div class="col col-md-6 col-sm-9 offset-md-0 offset-sm-2 offset-3">
							<button id="pMonth1" type="button" class="btn btn-sm btn-preset-month active" onclick="setInputDateField(this, 0, 'd')">오늘</button>
							<button id="pMonth2" type="button" class="btn btn-sm btn-preset-month" onclick="setInputDateField(this, -1,'d')">어제</button>
							<button id="pMonth3" type="button" class="btn btn-sm btn-preset-month" onclick="setInputDateField(this, -1, 'm')">지난달</button>
							<button id="pMonth4" type="button" class="btn btn-sm btn-preset-month" onclick="setInputDateField(this, 0, 'm')">이번달</button>
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
							<input class="form-control col-auto" type="text" id="searchValue2" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch(1);}"/>
						</div>
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							청구항목
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control col" id="searchCompletePayItems" style="max-width: 100%; margin-right: 0;" onchange="">
								<option value="">청구항목 선택</option>
							<c:forEach var="item" items="${payItems}" varStatus="status">
								<option value="${item.payitemcd}">${item.payitemname} - ${item.payitemcd}</option>
							</c:forEach>
							</select>
						</div>
					</div>
					<div class="row">
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							고객구분
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<select class="form-control" id="cusGubn_2" onchange="fnCusGubn(this);">
								<option value="all" selected="selected">전체</option>
								<c:if test="${not empty orgSess.cusGubn1}"><option value="cusGubn1">${orgSess.cusGubn1}</option></c:if>
								<c:if test="${not empty orgSess.cusGubn2}"><option value="cusGubn2">${orgSess.cusGubn2}</option></c:if>
								<c:if test="${not empty orgSess.cusGubn3}"><option value="cusGubn3">${orgSess.cusGubn3}</option></c:if>
								<c:if test="${not empty orgSess.cusGubn4}"><option value="cusGubn4">${orgSess.cusGubn4}</option></c:if>
							</select>
							<div class="input-with-magnet">
								<input class="form-control" type="text" id="cusGubnValue2" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch(1);}"/>
							</div>
						</div>
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							입금수단
						</label>
						<div class="col col-md-6 col-sm-11 col-10 form-inline">
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposAll" id="deposRadio1" value="ALL">
								<label class="form-check-label" for="deposRadio1"><span class="mr-1"></span>전체</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio2" value="DCD">
								<label class="form-check-label" for="deposRadio2"><span class="mr-1"></span>오프라인카드</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio3" value="DVA">
								<label class="form-check-label" for="deposRadio3"><span class="mr-1"></span>계좌입금</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio4" value="DCS">
								<label class="form-check-label" for="deposRadio4"><span class="mr-1"></span>현금</label>
							</div>
						</div>
					</div>
					<div class="row form-inline mt-3">
						<div class="col-12 text-center">
							<button type="button" class="btn btn-primary btn-wide" onclick="fnSearch(1);">조회</button>
						</div>
					</div>
				</form>
			</div>
			
		</div>

	</div> <!--// tab end  -->

	<div class="container">
		<div class="list-id">
			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="totCnt"><fmt:formatNumber pattern="#,###" value="${listCount}"/></em>건]</span>
					<span class="amount mr-2 tabDirectRegItems"><span id="totTitle">청구금액</span> [총 <em class="font-blue" id="totAmt"><fmt:formatNumber pattern="#,###" value="${map.sumAmt}"/></em>원]</span>
					<span class="amount tabCompleteRegItems" style="display:none;"><span id="totTitle2">입금금액</span> [총 <em class="font-blue" id="sumRcpAmt"><fmt:formatNumber pattern="#,###" value="${sumRcpAmt}"/></em>원]</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<select class="form-control tabDirectRegItems" id="orderByDirectReg" onchange="pageChange();">
						<option value="month">청구월순 정렬</option>
						<option value="cusname">고객명순 정렬</option>
						<option value="cusgubun">고객구분순 정렬</option>
						<option value="items">청구항목순 정렬</option>
					</select>
					<select class="form-control tabCompleteRegItems" style="display:none;" id="orderByCompleteReg" onchange="pageChange();">
						<option value="payday">입금일자순 정렬</option>
						<option value="month">청구월순 정렬</option>
						<option value="cusname">고객명순 정렬</option>
						<option value="cusgubun">고객구분순 정렬</option>
						<option value="items">청구항목순 정렬</option>
					</select>
					<select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
						<option value="10" <c:if test="${map.pageScale == '10'}">selected</c:if>>10개씩 조회</option>
	                    <option value="20" <c:if test="${map.pageScale == '20'}">selected</c:if>>20개씩 조회</option>
	                    <option value="50" <c:if test="${map.pageScale == '50'}">selected</c:if>>50개씩 조회</option>
	                    <option value="100" <c:if test="${map.pageScale == '100'}">selected</c:if>>100개씩 조회</option>
	                    <option value="200" <c:if test="${map.pageScale == '200'}">selected</c:if>>200개씩 조회</option>
					</select>
					<button class="btn btn-sm btn-d-gray" type="button" onclick="fn_fileSave()">파일저장</button>
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
								<col width="180">
								<col width="200">
								<c:if test="${not empty orgSess.cusGubn1}"><col width="140"></c:if>
								<c:if test="${not empty orgSess.cusGubn2}"><col width="140"></c:if>
								<c:if test="${not empty orgSess.cusGubn3}"><col width="140"></c:if>
								<c:if test="${not empty orgSess.cusGubn4}"><col width="140"></c:if>
								<col width="280">
								<col width="180">
								<col width="140">
								<col width="140">
								<col width="280">
							</colgroup>
	
							<thead id="resultHead">
								<tr>
									<th>
										<input class="form-check-input table-check-parents" type="checkbox" id="row-th" value="checkList" onclick="checkButtonTrigger(this)" onchange="changeBGColor(this)">
										<label for="row-th"><span></span></label>
									</th>
									<th>NO</th>
									<th>청구월</th>
									<th>고객명</th>
									<th>가상계좌번호</th>
									<c:if test="${not empty orgSess.cusGubn1}"><th>${orgSess.cusGubn1}</th></c:if>
									<c:if test="${not empty orgSess.cusGubn2}"><th>${orgSess.cusGubn2}</th></c:if>
									<c:if test="${not empty orgSess.cusGubn3}"><th>${orgSess.cusGubn3}</th></c:if>
									<c:if test="${not empty orgSess.cusGubn4}"><th>${orgSess.cusGubn4}</th></c:if>
									<th>청구항목</th>
									<th>입금수단</th>
									<th>입금금액(원)</th>
									<th>청구금액(원)</th>
									<th id="thType">납부기한</th>
								</tr>
							</thead>
							<tbody id="resultBody"></tbody>
						</table>
					</div>
				</div>
				
				<div class="row mb-3 hidden-on-mobile">
					<div class="col-6">
						<div class="tabCompleteRegItems" style="display:none;">
							<button type="button" class="btn btn-sm btn-gray-outlined btn-cancel-payments" onclick="cancelPayments(null)">선택취소</button>
							<button type="button" class="btn btn-sm btn-gray-outlined btn-cancel-paymentsAll" onclick="cancelPayments('all')">일괄취소</button>
		
							<a tabindex="0" class="popover-dismiss" role="button" data-toggle="popover" data-trigger="focus" title="" data-content="조회된 내역을 대량으로 전체 취소 가능. 취소 시, 납부기한 내에 가상계좌 재수납 가능" data-original-title="일괄취소란?">
								<img src="/assets/imgs/common/icon-info.png">
							</a>
						</div>
						<div class="tabDirectRegItems">
							<div class="date-input">
								<label class="col col-form-label" style="min-width:85px;" for="regPaymentDate">입금일자</label>
								<div class="input-group">
									<input type="text" id="regPaymentDate" style="height: 28px; min-width:70px; max-width:95px;" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-label="입금일자" aria-describedby="basic-addon2">
								</div>
							</div>
						</div>
					</div>
					<div class="col-6 text-right">
						<div class="tabDirectRegItems">
							<button type="button" id="directPaymentProcced" class="btn btn-sm btn-d-gray btn-open-modify-payer-info">선택저장</button>
						</div>
					</div>
				</div>
	
				<jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />
	
			</div>
		</div>
		
	
	
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
						<h6>■ 수납등록 상태</h6>
						<ul>
							<li>수납등록 : 설정한 청구월의 입금 가능한 미납, 일부납 상태의 목록을 표시</li>
							<li>수납완료 : 직접수납등록을 완료한 목록을 조회하고, 등록 완료한 건을 일부 또는 전체 취소 가능</li>
						</ul>
						<h6>■ 수납등록 방법</h6>
						<ul>
							<li>입금수단을 오프라인 카드, 계좌입금, 현금 중 선택한 후, 입금금액을 입력</li>
							<li>입금일자를 지정한 후 선택저장 버튼 클릭</li>
							<li>수납 등록을 완료한 내역은 수납완료 탭에서 확인 가능</li>
							<li>청구 금액 중 일부 금액 등록 시 나머지 미납 금액은 가상계좌로 입금 가능</li>
						</ul>
						<h6>■ 수납등록 취소 방법</h6>
						<ul>
							<li>수납완료 탭에서 체크박스를 선택하여 좌측 하단의 선택취소 버튼을 클릭</li>
							<li>조회된 내역의 전체를 취소하고자 할 경우 좌측 하단의 일괄취소 버튼을 클릭</li>
							<li>수납완료를 취소한 항목은 다시 수납등록 탭에서 확인할 수 있으며, 납부기한 내 가상계좌 입금 가능</li>
						</ul>
						<h6>■ 현금영수증 발행</h6>
						<ul>
							<li>수기수납건에 대해 발행요청 및 기발행된 현금영수증은 수납을 취소 하더라도 자동취소처리 되지 않으니, 현금영수증 메뉴에서 발행내역 확인하시어 직접 발행요청취소 및 발행취소처리 부탁드립니다.</li>
							<li>소득세법 및 소득세법 시행령에 따라 현금영수증을 발행하시는 사업자 및 법인의 현금영수증 발급 의무 이행 및 모든 책임은 운영사와 무관함을 사전 고지합니다.</li>
							<li>현금영수증의 수동발행 및 재발행 시 실 입금일자(현금수령일)가 아닌 발행을 요청하신 날짜로 국세청에 등록 되오니 업무에 참고하시기 바랍니다.</li>
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


<script type="text/javascript">
	$(document).ready(function() {
	    $("#directPaymentProcced").click(function (){
	    	directPayment();
	    });

	    $('.popover-dismiss').popover({ trigger: 'focus' });

		getMonthBox('monthBox');
		getMonthBox('monthBox2');
		setInputDateField($("#pMonth1"), 0, 'd');

		$("#monthBox").val("${maxMonth}");
		$("#monthBox2").val("${maxMonth}");
		
		/*
		 * tab 버튼 처리
		 */
		$('#showCash').click(function(){
			$(".tabCompleteRegItems").hide();
			$(".tabDirectRegItems").show();
			$("#currentTab").val("direct");
			fnSearch($("#pageNo").val());
		});
		$('#showRefund').click(function(){
			$(".tabCompleteRegItems").show();
			$(".tabDirectRegItems").hide();
			$("#currentTab").val("complete");
			fnSearch($("#pageNo").val());
		});

		$('#deposRadio1, :checkbox[name=deposItem]').prop('checked', true);
		$(':checkbox[name=deposItem]').click(function(e){
			var length = $(':checkbox[name=deposItem]').length;
        	var checkedLen = $(':checkbox[name=deposItem]:checked').length;
        	var checked = (length == checkedLen);
        	$('#deposRadio1').prop('checked', checked);
        });
        //입금수단 전체 체크, 해제
        $('#deposRadio1').click(function () {
            var checked = $(this).prop('checked');
            $(':checkbox[name=deposItem]').prop('checked', checked);
        });

		$("#regPaymentDate").val( $.datepicker.formatDate("yy.mm.dd", new Date() ) );

		fnSearch($("#pageNo").val());
	});
</script>