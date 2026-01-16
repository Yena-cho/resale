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
	var secondDepth = "sub-05";

	// 입금수단코드
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
		if ($('#searchGb option:selected, ').val() == "vano") {
			$('#searchvalue').val("");
		}
	}

	/**
	 * 고객구분 검색 초기화
	 */
	function fnCusGubn(obj) {
		cusGubn = $(obj).val();
		if(cusGubn == 'all'){
			$('#cusGubnValue').attr('placeholder','검색어를 입력하세요.');
		}else{
			$('#cusGubnValue').attr('placeholder','콤마(,) 구분자로 다중검색이 가능합니다.');
		}
	}

	/**
	 * 파라메터 생성
	 */
	function buildParams() {
		var params = {};
		params.pageTab      =  $("#currentTab").val();
        params.curPage      =  $("#pageNo").val();
        params.pageScale    =  $('#pageScale option:selected').val();
        params.masmonth     =  $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val();
		params.startDate    =  replaceDot($('#fDate').val());;
		params.endDate      =  replaceDot($('#tDate').val());
        params.cusGubn      =  $('#cusGubn option:selected').val();
        params.cusGubnValue =  $('#cusGubnValue').val();
        params.searchGb     =  $('#searchGb option:selected').val();
        params.searchValue  =  $('#searchValue').val();

        if ($("#currentTab").val() == "refund") {
	        params.orderBy      =  $('#orderByRefund option:selected').val();
        } else {
	       	params.orderBy      =  $('#orderByRefundCancel option:selected').val();
        }

        var sves = new Array();
        $(':checkbox[name=deposItem]:checked').each(function(i, check){
	        sves.push($(this).val());
	    });
        params.svecds       =  sves;
        
        var status = new Array();
        $(':checkbox[name=payItem]:checked').each(function(i, check){
        	status.push($(this).val());
	    });
        params.status       =  status;

		return params;
	}

	/**
	 * 화면목록 표시 건수 이벤트
	 */
	function pageChange(type) {
		list(1);
	}

	/**
	 * 페이징 버튼
	 */
	function list(page) {
		$('#pageNo').val(page);
		fnSearch(page);
	}

	/**
	 * 환불등록/환불완료 목록 조회
	 */
	function fnSearch(page) {
		
		if(page == null || page == 'undefined' || page == ''){
			cuPage = "";
			cuPage = 1;
		}else{
			cuPage = page;
		}
		$('#pageNo').val(cuPage);

		// 환불등록/완료 조건생성
		var param = buildParams();

		if (param.pageTab == "refund_cancel") {
			// 환불완료 조회 조건

			if (param.startDate != "" && param.endDate != "") {
				var vdm = "ok";
				if (param.startDate == "" || param.startDate == null || param.startDate == undefined) vdm = "환불일자 시작일을 입력하세요.";
				if (param.endDate == "" || param.endDate == null || param.endDate == undefined) vdm = "환불일자 종료일을 입력하세요.";
				if (param.startDate > param.endDate) vdm = "환불일자 시작일은 종료일보다 클 수 없습니다.";
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
		
		// 환불등록 화면 목록 조회
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

		var url = "/org/receiptMgnt/refundReceiptListAjax";
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
		str+='<th>입금일시</th>';
		if ($("#currentTab").val() == "refund_cancel") {
			str+='<th>환불일자</th>';
		} else {
			str+='<th>수납상태</th>';
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
		if ($("#currentTab").val() == "refund") {
			str+='<th>환불대상금액(원)</th>';
		} else {
			str+='<th>환불금액(원)</th>';
			str+='<th>입금금액(원)</th>';
		}

		str+='<th>청구금액(원)</th>';
		str+='<th id="thType">입금수단</th>';
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
			$('#listCount').text("0");
			$('#sumRefundAmt').text("0");
			$('#sumRcpAmt').text("0");
			$('#sumAmt').text("0");
			str = '<tr><td colspan="'+$("#resultHead > tr > th").length+'" class="text-center" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
			$('#resultBody').html(str);
			return;
		}

		if (result.listCount > 0) {
			$('#listCount').text(numberToCommas(result.listCount));
			$('#sumRefundAmt').text(numberToCommas(result.sumRefundAmt));
			$('#sumRcpAmt').text(numberToCommas(result.sumRcpPayitemAmt));
			$('#sumAmt').text(numberToCommas(result.sumAmt));

			var str = '';
			$.each(result.list, function(i, v){
				str += '<tr>';
				str += '	<td>';
				str += '		<input class="form-check-input table-check-child" type="checkbox" name="checkList" id="row-'+i+'" value="'+v.vano+'" num="'+i+'" nmcd="'+v.notimascd+'" ndcd="'+v.notidetcd+'" rmcd="'+v.rcpmascd+'" rdcd="'+v.rcpdetcd+'" rfcd="'+v.repaymascd+'" onchange="changeBGColor(this)" >';
				str += '		<label for="row-'+i+'"><span></span></label>';
				str += '	</td>';
				str += '	<td class="text-center">'+v.rn+'</td>';
				str += '	<td class="text-center">'+formatYearMonth(v.masmonth)+'</td>';
				str += '	<td class="text-center">'+changeDateFormat(v.payday)+' ' + getTimeFmtBasic(v.paytime) +'</td>';
				if ($("#currentTab").val() == "refund_cancel") {
					str += '	<td class="text-center">'+changeDateFormat(v.repayday)+'</td>';
				} else {
					str += '	<td class="text-center text-danger">'+rcpStatusMap[v.notimasst]+'</td>';
				}
				str += '	<td class="text-center"><button type="button" class="btn btn-xs btn-link" onclick="cusName(this.value)" value="'+v.vano+'">'+basicEscape(v.cusname)+'</button></td>';
				str += '	<td class="text-center">'+v.vano+'</td>';
				if($('#cusGubn1').val() != null && $('#cusGubn1').val() != ''){
					str += '	<td class="text-center">'+nullValueChange( basicEscape(v.cusgubn1) )+'</td>';
				}
				if($('#cusGubn2').val() != null && $('#cusGubn2').val() != ''){
					str += '	<td class="text-center">'+nullValueChange( basicEscape(v.cusgubn2) )+'</td>';
				}
				if($('#cusGubn3').val() != null && $('#cusGubn3').val() != ''){
					str += '	<td class="text-center">'+nullValueChange( basicEscape(v.cusgubn3) )+'</td>';
				}
				if($('#cusGubn4').val() != null && $('#cusGubn4').val() != ''){
					str += '	<td class="text-center">'+nullValueChange( basicEscape(v.cusgubn4) )+'</td>';
				}

				str += '	<td class="text-center" code="'+ v.payitemcd +'">'+ v.payitemname +'</td>';
				
				//str += '	<input type="text" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)" class="form-control text-right text-danger" id="refundAmt-'+i+'" name="payItemAmt" value="'+v.rcppayitemamt+'" style="width: 120px !important;">';
				if ($("#currentTab").val() == "refund") {
					str += '	<td class="text-center font-blue">';
					str += numberToCommas(v.rcppayitemamt);
					str += '	</td>';
				} else {
					str += '	<td class="text-center text-danger">';
					str += numberToCommas(v.refundamt);
					str += '	</td>';
					str += '	<td class="text-right font-blue">'+numberToCommas(v.rcppayitemamt)+'</td>';
				}
				str += '	<td class="text-right font-blue">'+numberToCommas(v.payitemamt)+'</td>';
				str += '	<td class="text-center" code="'+v.svecd+'">';
				str += sveMap[v.svecd];
				str += '	</td>';
				str += '</tr>';
			}); // end $.each
		} else {
			$('#listCount').text("0");
			$('#sumRefundAmt').text("0");
			$('#sumRcpAmt').text("0");
			$('#sumAmt').text("0");
			
			str = '<tr><td colspan="'+$("#resultHead > tr > th").length+'" class="text-center" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
		}
		
		$('#resultBody').html(str);
	}

	/**
	 * 엑셀 파일저장
	 */
	function fn_fileSave() {
		// 환불등록/완료 조건생성
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
	        var url = '/org/receiptMgnt/refundReceiptExcelDown?' + $.param(params);
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
	 * 환불등록 저장/수정 처리
	 */
	function directRefund() {

		var checkedCnt = $(':checkbox[name="checkList"]:checked').length;

		if ( checkedCnt <= 0) {
			swal({
				type: 'info',
				text: '환불할 입금내역을 선택하세요.',
				confirmButtonColor: '#3085d6',
				confirmButtonText: '확인'
			});
			return;
		}

		if (checkedCnt > 0) {

			swal({
				type: 'question',
				text: '선택하신 ['+checkedCnt+']건을 환불처리 하시겠습니까?',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: '확인',
				cancelButtonText: '취소'
			}).then(function(confirm){
				if (confirm.value) {
					// 환불처리
					var params = {};
					params.repayday = replaceDot($('#regRefundDate').val());
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
					params.rcpList = rcpList;

					var url = "/org/receiptMgnt/refundReceiptSaveAjax";
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
                                    type: 'success',
                                    html: retMsg,
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                });
							}

							// 목록 새로고침
							fnSearch($("#pageNo").val());
						}
					});
				} else {
					// cancel
					return;
				}
			});
		}
	}

	/**
	 * 환불완료 취소 처리여부 확인
	 */
	function cancelRefund(flag) {
		if (flag == 'all') {
			// 전부 취소
			swal({
				type: 'question',
				text: '조회된 ['+$("#listCount").text()+']건의 환불을 일괄 취소하시겠습니까?',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: '확인',
				cancelButtonText: '취소'
			}).then(function(confirm){
				if (confirm.value) {
					// 일괄취소
					cancelRefundProcced(null);
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
					text: '취소할 환불건을 선택해 주세요.',
					confirmButtonColor: '#3085d6',
					confirmButtonText: '확인'
				});
				return;
		    } else {
				swal({
					type: 'question',
					text: '선택하신 ['+checkedCnt+']건의 환불을 취소하시겠습니까?',
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
							var rcpMasCd    = $(row).attr("rmcd");
							var rcpDetCd    = $(row).attr("rdcd");
							var rePayMasCd  = $(row).attr("rfcd");
							var map = {};
							map.notiMasCd   = notiMasCd;
							map.rcpMasCd    = rcpMasCd;
							map.rcpDetCd    = rcpDetCd;
							map.rePayMasCd  = rePayMasCd;
							rcpList.push(map);
						});
						// 선택취소
						cancelRefundProcced(rcpList);
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
	 * 환불완료 항목 취소 처리
	 */
	function cancelRefundProcced(rcpList) {

		// 환불등록/완료 조건생성
		var params = buildParams();
		params.rcpList = rcpList;

		var url = "/org/receiptMgnt/refundReceiptCancelAjax";
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
                    swal({
                        type: 'info',
                        html: result.retMsg,
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
<input type="hidden" id="pageCnt"  name="pageCnt"  value="${map.pageScale}" />
<input type="hidden" id="pageNo"   name="pageNo"   value="1" />
<input type="hidden" id="currentTab"  name="currentTab"  value="refund" />

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link" href="/org/receiptMgnt/receiptList">수납내역 조회</a>
			<a class="nav-link" href="/org/receiptMgnt/paymentList">입금내역 조회</a>
			<a class="nav-link" href="/org/receiptMgnt/payItemList">항목별 입금내역</a>
			<a class="nav-link" href="/org/receiptMgnt/directReceiptReg">수기수납내역</a>
			<a class="nav-link active" href="#">수기환불내역</a>
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
					<span class="depth-2 active">수기환불내역</span>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2>수기환불내역</h2>
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
					<p class="cashDct tabRefundItems">수납된 내역을 수기로 환불 등록할 수 있는 화면입니다.</p>
					<p class="refundDct tabRefundCancelItems" style="display: none;">수기 등록으로 환불이 완료된 내역을 확인할 수 있으며, 등록된 환불을 취소할 수 있는 화면입니다.</p>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="tab-selecter type-3">
					<ul class="nav nav-tabs" role="tablist">
						<li class="nav-item"><a id="showRefundList" class="nav-link active" data-toggle="tab" href="#direct-refund">환불등록</a></li>
						<li class="nav-item"><a id="showRefundCancel" class="nav-link" data-toggle="tab" href="#refund-cancel">환불완료</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div class="tab-content">
		<div id="direct-refund" class="tab-pane fade show active"></div>
		<div id="refund-cancel" class="tab-pane fade show"></div>
	</div>
	<div class="container">
		<div>
			<div class="search-box">
				<form>
					<c:set var="minYear" value="${fn:substring(masMonth.minmonth, 0, 4)}" />
					<c:set var="maxYear" value="${fn:substring(masMonth.maxmonth, 0, 4)}" />
					<c:set var="maxMonth" value="${fn:substring(masMonth.maxmonth, 4, 6)}" />
					<div class="row">
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							청구월
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							${maxMasMonth}
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
							<span class="ml-1">월</span>
						</div>
					</div>
					<div class="row">
						<label class="col col-md-1 col-sm-2 col-2 col-form-label tabRefundItems">
							입금일자
						</label>
						<label class="col col-md-1 col-sm-2 col-2 col-form-label tabRefundCancelItems" style="display:none;">
							환불일자
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
							<select class="form-control" id="searchGb" onchange="onSearchGb();">
								<option value="cusname">고객명</option>
								<option value="vano">계좌번호</option>
							</select>
							<input class="form-control col-auto" type="text" id="searchValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch(1);}"/>
						</div>
						<label class="col col-md-1 col-sm-2 col-2 col-form-label tabRefundItems">수납상태 </label>
						<div class="col col-md-6 col-sm-11 col-10 form-inline tabRefundItems">
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="payAll" id="payRadio1" value="ALL">
								<label class="form-check-label" for="payRadio1"><span class="mr-1"></span>전체</label>
							</div>
							<%-- <div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="payItem" id="payRadio2" value="PA02">
								<label class="form-check-label" for="payRadio2"><span class="mr-1"></span>미납</label>
							</div> --%>
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
							<%-- <div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="payItem" id="payRadio6" value="PA06">
								<label class="form-check-label" for="payRadio6"><span class="mr-1"></span>환불</label>
							</div> --%>
						</div>
					</div>
					<div class="row">
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
						<label class="col col-md-1 col-sm-2 col-2 col-form-label">
							입금수단
						</label>
						<div class="col col-md-6 col-sm-11 col-10 form-inline">
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposAll" id="deposRadio1" value="ALL">
								<label class="form-check-label" for="deposRadio1"><span class="mr-1"></span>전체</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio2" value="VAS">
								<label class="form-check-label" for="deposRadio2"><span class="mr-1"></span>가상계좌</label>
							</div>
							<%--<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio3" value="OCD">
								<label class="form-check-label" for="deposRadio3"><span class="mr-1"></span>온라인카드</label>
							</div>--%>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio4" value="DCD">
								<label class="form-check-label" for="deposRadio4"><span class="mr-1"></span>오프라인카드</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio5" value="DVA">
								<label class="form-check-label" for="deposRadio5"><span class="mr-1"></span>계좌입금</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio6" value="DCS">
								<label class="form-check-label" for="deposRadio6"><span class="mr-1"></span>현금</label>
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
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="listCount"><fmt:formatNumber pattern="#,###" value="${listCount}"/></em>건]</span>
					<%-- <span class="amount mr-2 tabRefundCancelItems"><span id="sumRefundAmtTitle">환불금액</span> [총 <em class="text-danger" id="sumRefundAmt"><fmt:formatNumber pattern="#,###" value="${sumRefundAmt}"/></em>원]</span> --%>
					<span class="amount mr-2"><span id="sumRcpAmtTitle">입금금액</span> [총 <em class="font-blue" id="sumRcpAmt"><fmt:formatNumber pattern="#,###" value="${sumRcpAmt}"/></em>원]</span>
					<span class="amount mr-2 tabRefundItems"><span id="sumAmtTitle">청구금액</span> [총 <em class="font-blue" id="sumAmt"><fmt:formatNumber pattern="#,###" value="${sumAmt}"/></em>원]</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<select class="form-control tabRefundItems" id="orderByRefund" onchange="pageChange();">
						<option value="payday">입금일순 정렬</option>
						<option value="month">청구월순 정렬</option>
						<option value="cusname">고객명순 정렬</option>
						<option value="cusgubun">고객구분순 정렬</option>
					</select>
					<select class="form-control tabRefundCancelItems" style="display:none;" id="orderByRefundCancel" onchange="pageChange();">
						<option value="payday">환불일자순 정렬</option>
						<option value="payday">입금일자순 정렬</option>
						<option value="month">청구월순 정렬</option>
						<option value="cusname">고객명순 정렬</option>
						<option value="cusgubun">고객구분순 정렬</option>
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
								<col width="120">
								<col width="180">
								<col width="200">
								<c:if test="${not empty orgSess.cusGubn1}"><col width="140"></c:if>
								<c:if test="${not empty orgSess.cusGubn2}"><col width="140"></c:if>
								<c:if test="${not empty orgSess.cusGubn3}"><col width="140"></c:if>
								<c:if test="${not empty orgSess.cusGubn4}"><col width="140"></c:if>
								<col width="140">
								<col width="140">
								<col width="140">
								<col width="140">
								<col width="140">
							</colgroup>
	
							<thead id="resultHead">
								<tr>
									<th>
										<input class="form-check-input table-check-parents" type="checkbox" id="row-th" value="checkList" onclick="checkButtonTrigger(this)" onchange="changeBGColor(this)">
										<label for="row-th"><span></span></label>
									</th>
									<th>NO</th>
									<th>청구월</th>
									<th>입금일시</th>
									<th>고객명</th>
									<th>가상계좌번호</th>
									<c:if test="${not empty orgSess.cusGubn1}"><th>${orgSess.cusGubn1}</th></c:if>
									<c:if test="${not empty orgSess.cusGubn2}"><th>${orgSess.cusGubn2}</th></c:if>
									<c:if test="${not empty orgSess.cusGubn3}"><th>${orgSess.cusGubn3}</th></c:if>
									<c:if test="${not empty orgSess.cusGubn4}"><th>${orgSess.cusGubn4}</th></c:if>
									<th>청구항목</th>
									<th>환불금액</th>
									<th>입금금액(원)</th>
									<th>청구금액(원)</th>
									<th id="thType">입금수단</th>
								</tr>
							</thead>
							<tbody id="resultBody"></tbody>
						</table>
					</div>
				</div>
				
				<div class="row mb-3 hidden-on-mobile">
					<div class="col-6">
						<div class="tabRefundCancelItems" style="display:none;">
							<button type="button" class="btn btn-sm btn-gray-outlined btn-cancel-payments" onclick="cancelRefund(null)">선택취소</button>
							<button type="button" class="btn btn-sm btn-gray-outlined btn-cancel-paymentsAll" onclick="cancelRefund('all')">일괄취소</button>
		
							<a tabindex="0" class="popover-dismiss" role="button" data-toggle="popover" data-trigger="focus" title="" data-content="조회된 내역을 대량으로 전체 취소 가능. 취소 시, 납부 상태 변경" data-original-title="일괄취소란?">
								<img src="/assets/imgs/common/icon-info.png">
							</a>
						</div>
						<div class="tabRefundItems">
							<div class="date-input">
								<label class="col col-form-label" style="min-width:85px;" for="regRefundDate">환불일자</label>
								<div class="input-group">
									<input type="text" id="regRefundDate" style="height: 28px; min-width:70px; max-width:95px;" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-label="환불일자" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
								</div>
							</div>
						</div>
					</div>
					<div class="col-6 text-right">
						<div class="tabRefundItems">
							<button type="button" id="directRefundProcced" class="btn btn-sm btn-d-gray btn-open-modify-payer-info">선택저장</button>
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
						<h6>■ 환불 상태</h6>
						<ul>
							<li>환불 등록 : 입금내역이 존재하여 환불 가능한 상태의 목록을 표시</li> 
							<li>환불 완료 : 환불내역을 등록한 목록을 조회, 환불등록 취소 가능 </li>
						</ul>
						<h6>■ 환불내역 등록 방법</h6>
						<ul>
							<li>환불금액을 입력</li>
							<li>환불일자를 지정한 후 선택저장 버튼 클릭</li>
							<li>환불 등록을 완료한 내역은 환불완료 탭에서 확인 가능</li>
							<li>환불 등록 시 고객 가상계좌로 환불처리 불가하며 고객의 계좌로 직접 환불처리 필요</li>
							<li>현금영수증이 발행된 건에 대해서는 별도로 발행 취소 처리 필요</li>
						</ul>
						<h6>■ 환불내역 등록 취소 방법</h6>
						<ul>
							<li>환불완료 탭에서 취소 건의 체크박스를 선택하여 좌측 하단의 선택취소 버튼을 클릭</li>  
							<li>조회된 내역 전체를 취소하고자 할 경우 좌측 하단의 일괄취소 클릭</li>
							<li>환불등록을 취소한 항목은 다시 환불등록 탭에서 확인 가능</li>
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
	    $("#directRefundProcced").click(function (){
	    	directRefund();
	    });

	    $('.popover-dismiss').popover({ trigger: 'focus' });

		getMonthBox('monthBox');
		setInputDateField($("#pMonth1"), 0, 'd');

		$("#monthBox").val("${maxMonth}");
		
		/*
		 * tab 버튼 처리
		 */
		$('#showRefundList').click(function(){
			$(".tabRefundCancelItems").hide();
			$(".tabRefundItems").show();
			$("#currentTab").val("refund");
			fnSearch($("#pageNo").val());
		});
		$('#showRefundCancel').click(function(){
			$(".tabRefundCancelItems").show();
			$(".tabRefundItems").hide();
			$("#currentTab").val("refund_cancel");
			fnSearch($("#pageNo").val());
		});

        //수납상태 전체 체크, 해제
		$('#payRadio1, :checkbox[name=payItem]').prop('checked', true);
        $(':checkbox[name=payItem]').click(function(e){
        	var length = $(':checkbox[name=payItem]').length;
        	var checkedLen = $(':checkbox[name=payItem]:checked').length;
        	var checked = (length == checkedLen);
        	$('#payRadio1').prop('checked', checked);
        });
        $('#payRadio1').click(function () {
            var checked = $(this).prop('checked');
            $(':checkbox[name=payItem]').prop('checked', checked);
        });
        
        //입금수단 전체 체크, 해제
		$('#deposRadio1, :checkbox[name=deposItem]').prop('checked', true);
        $(':checkbox[name=deposItem]').click(function(e){
        	var length = $(':checkbox[name=deposItem]').length;
        	var checkedLen = $(':checkbox[name=deposItem]:checked').length;
        	var checked = (length == checkedLen);
        	$('#deposRadio1').prop('checked', checked);
        });
        $('#deposRadio1').click(function () {
            var checked = $(this).prop('checked');
            $(':checkbox[name=deposItem]').prop('checked', checked);
        });
        
		$("#regRefundDate").val( $.datepicker.formatDate("yy.mm.dd", new Date() ) );

		fnSearch($("#pageNo").val());
	});
</script>
