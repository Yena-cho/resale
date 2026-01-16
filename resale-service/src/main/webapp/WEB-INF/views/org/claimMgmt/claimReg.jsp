<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<link rel="stylesheet" type="text/css" href="/assets/css/swiper.css">

<script>
var firstDepth = "nav-link-32";
var secondDepth = "sub-01";
</script>

<script type="text/javascript">
	$(document).ready(function() {

        $("#afStartDate").val(moment(new Date(), 'YYYYMMDD').format("YYYY.MM.DD"));

        $('.popover-dismiss').popover({
            trigger: 'focus'
        });

		// 그리드 전체선택, 전체해제
		$("#row-th").click(function() { //만약 전체 선택 체크박스가 체크된상태일경우
			if($("#row-th").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
				$("#mainBody input[type=checkbox]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
			}else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
				$("#mainBody input[type=checkbox]").prop("checked", false);
			}
		});
		
		var cuPage = 1; // 청구목록 현재페이지
		var moPage = 1; // modal 현재페이지
		var month = '${map.claimMonth}';

		// 청구등록화면 - 청구년,월
		getYearsBox2('claimYear');
		getMonthBox('claimMonth');
		$('#claimYear').val(month.substring(0, 4));
		$('#claimMonth').val(month.substring(4, 6));

		// 청구등록 수정
		getYearsBox2('chargeYear');
		getMonthBox('chargeMonth');
		$('#chargeYear').val(month.substring(0, 4));
		$('#chargeMonth').val(month.substring(4, 6));

		// 개별청구등록
		getYearsBox2('sinYear');
		getMonthBox('sinMonth');
		$('#sinYear').val(getYear());
		$('#sinMonth').val(getMonth());

		// 이전청구불러오기
		getYearsBox2('afYear');
		getMonthBox('afMonth');
		$('#afYear').val(getYear());
		$('#afMonth').val(getMonth());

		// 청구대량등록 - 청구년,월
		getYearsBox2('exClaimYear'); // 엑셀파일대량등록
		getMonthBox('exClaimMonth');// 엑셀파일대량등록

		$('#exClaimYear').val(getYear());
		$('#exClaimMonth').val(getMonth());

		// 청구금액
        $('#claimAmtSum').text(numberToCommas('${map.payAmtSum}'));

		if ("${map.count}" > 0 && $("#delYN").val() == "N") { // 작성중인 청구내역이 있을 경우.
			$("#delYN").val("Y");
			swal({
                title : "이전에 작성중인 내역이 임시저장 상태로 존재합니다.",
                html  : "이어서 작업하시겠습니까?",
                type  : 'info',
                showCancelButton : true,
                confirmButtonColor : '#d33',
                cancelButtonColor : '#3085d6',
                confirmButtonText : '삭제',
                cancelButtonText : '이어서 작업',
                reverseButtons: false
			}).then(function(result) {
				if (result.value) {
					// 삭제
					fn_ClaimDelCall();
				} else {
					// 조회
					fn_ClaimListCall(cuPage);
				}
			});
		} else {
			// 조회
			fn_ClaimListCall(cuPage);
		}

		fn_ClaimListCall(cuPage);
		
		//청구항목 체크 여부에 따라 Selectbox 활성화 여부
        $('#claimItemCdCheck').click(function () {
        	if($('#claimItemCdCheck').prop('checked')) {
        		$("#claimItemCd").attr('disabled', false);
        	}else {
        		$("#claimItemCd").val("");
        		$("#claimItemCd").attr('disabled', true);
        		
        		$('#itemAmt').val('');
    			$('#itemAmt').attr('disabled', true);
        	}
        });
        
		//납부기한 수정 체크 여부에 따라 고지서용 표시마감일 활성여부
        $("#claimPrintCheck").addClass('checkbox-none-check-disabled');
        $('#claimInsPeriod').click(function () {
            if($('#claimInsPeriod').prop('checked')) {
                $("#claimPrintDate").attr('disabled', false);
                $("#claimPrintCheck").removeClass('checkbox-none-check-disabled');
                $("#claimPrintCheck").addClass('checkbox-disabled');
				$("#claimPrintCheck").prop('checked', true);
            }else {
                $('#claimPrintCheck').prop('checked', false);
                $("#claimPrintDate").attr('disabled', true);
                $("#claimPrintCheck").addClass('checkbox-none-check-disabled');
                $("#claimPrintCheck").removeClass('checkbox-disabled');
            }
        });
        
        
	});

	// 청구대상목록
	function fn_ClaimListCall(page) {
		if (page == null || page == 'undefined') {
			cuPage = '1';
		} else {
			cuPage = page;
		}

		var url = "/org/claimMgmt/claimRegAjax";
		var param = {
			delYN : 'Y',
			masMonth : '${map.claimMonth}',
			curPage : cuPage,
			notiMasSt : 'PA00',
			search_orderBy : $('#sSearch_orderBy option:selected').val(),
			pageScale : $('#PAGE_SCALE option:selected').val()
		};
		$.ajax({
			type : "post",
			async : true,
			url : url,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(param),
			success : function(data) {
				$('#row-th').prop('checked', false);
				// 청구대상목록
				fnGrid(data, 'mainBody'); // 현재 데이터로 셋팅
				ajaxPaging(data, 'PageArea'); // 페이징
			},
			error : function(data) {
				$('#row-th').prop('checked', false);
				swal({
					type : 'error',
					text : '조회 실패하였습니다.',
					confirmButtonColor : '#3085d6',
					confirmButtonText : '확인'
				})
			}
		});
	}

	//페이징 조회
	function list(page) {
		$('#pageNo').val(page);
		fn_save(page);
	}

	//청구 대상 목록
	function fnGrid(result, obj) {
		$('#claimYear').val(result.masMonth.substring(0, 4));
		$('#claimMonth').val(result.masMonth.substring(4, 6));
		$('#claimCnt').text(result.count);
        $('#claimClientCnt').text(numberToCommas(result.clientCount));
        $('#claimAmtSum').text(numberToCommas(result.payAmtSum));
		$('#order').val(result.search_orderBy);
		$('#pageCnt').val(result.PAGE_SCALE);

		var str = '';
		var col = 10 + result.cusGbCnt;
		if (result.count <= 0) {
			str += '<tr><td colspan="'+ col +'" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
		} else {
			$.each(result.list, function(k, v) {
				var payItemAmt = addThousandSeparatorCommas(v.payItemAmt); // 콤마추가
				str += '<tr>';
				str += '<input type="hidden" id="masCd" value="' + v.notiMasCd + '">';
				str += '<td><input class="form-check-input table-check-child" type="checkbox" name="checkOne" id="row-' + v.rn + '" value="' + v.notiDetCd + '" onclick="fn_listCheck();" onchange="changeBGColor(this)">';
				str += '<label for="row-' + v.rn + '"><span></span></label></td>';
				str += '<td>' + v.rn + '</td>';
                str += '<td>' + nullValueChange(v.masMonth) + '</td>';
				str += '<td><a href="#" onclick="fn_update(\'' + v.notiMasCd + '\');">' + basicEscape(v.cusName) + '</a></td>';
				if (result.cusGbCnt == 1 || result.cusGbCnt > 1) {
					str += '<td>' + basicEscape(nullValueChange(v.cusGubn1)) + '</td>';
				}
				if (result.cusGbCnt == 2 || result.cusGbCnt > 2) {
					str += '<td>' + basicEscape(nullValueChange(v.cusGubn2)) + '</td>';
				}
				if (result.cusGbCnt == 3 || result.cusGbCnt > 3) {
					str += '<td>' + basicEscape(nullValueChange(v.cusGubn3)) + '</td>';
				}
				if (result.cusGbCnt == 4 || result.cusGbCnt > 4) {
					str += '<td>' + basicEscape(nullValueChange(v.cusGubn4)) + '</td>';
				}
                str += '<td>' + nullValueChange(v.vano) + '</td>';
				str += '<td>' + nullValueChange(v.payItemName) + '</td>';
				str += '<td class="form-td-inline">';
				str += '<input type="text" id="payAmt" name="payAmt" class="form-control text-right" value="'+ payItemAmt +'" style="width: 130px !important" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)">';
				// str += '<span class="ml-1">원</span>';
				str += '</td>';
				str += '<td>' + nullValueChange(v.startDate) + '~' + nullValueChange(v.endDate) + '</td>';
				str += '<td>' + nullValueChange(v.printDate) + '</td>';
				str += '</tr>';
			});
		}
		$('#' + obj).html(str);
	}

	// 청구대상삭제
	function fn_ClaimDelCall() {

		var url = "/org/claimMgmt/claimRegDel";
		var param = {
			notiMasSt : 'PA00'
		};

		$.ajax({
			type : "post",
			url : url,
			data : param,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(param),
			success : function(result) {
				swal({
					type : 'success',
					text : '삭제되었습니다.',
					confirmButtonColor : '#3085d6',
					confirmButtonText : '확인'
				}).then(function(result) {
					if (result.value) {
						fn_ClaimListCall();
					}
				});
			},
			error : function(result) {
				swal({
					type : 'error',
					text : '삭제 실패하였습니다.',
					confirmButtonColor : '#3085d6',
					confirmButtonText : '확인'
				})
			}
		});
	}

	// 선택 삭제
	function fn_selDelete() {

		var rowCnt = 0;
		var claimDelVals = [];
		var checkbox = $("input[name='checkOne']:checked");
		checkbox.each(function(i) {
			claimDelVals.push($(this).val());
			rowCnt++;
		});
		
		if (rowCnt == 0) {
			swal({
				type : 'info',
				text : noSelectMsg,
				confirmButtonColor : '#3085d6',
				confirmButtonText : '확인'
			});
		} else {

			swal({
				type : 'question',
				html : "삭제하시겠습니까?",
				showCancelButton : true,
				confirmButtonColor : '#3085d6',
				cancelButtonColor : '#d33',
				confirmButtonText : '확인',
				cancelButtonText : '취소',
                reverseButtons: true
			}).then(function(result) {
				if (result.value) {
					// 삭제
					var url = "/org/claimMgmt/claimDel";
					var param = {
						itemList : claimDelVals
					};
					$.ajax({
						type : "post",
						async : true,
						url : url,
						contentType : "application/json; charset=UTF-8",
						data : JSON.stringify(param),
						success : function(data) {
							swal({
								type : 'success',
								text : data.retMsg,
								confirmButtonColor : '#3085d6',
								confirmButtonText : '확인'
							}).then(function(result) {
								if (result.value) {
									fn_ClaimListCall(1);
								}
							});
						},
						error : function(result) {
							swal({
								type : 'error',
								text : data.retMsg,
								confirmButtonColor : '#3085d6',
								confirmButtonText : '확인'
							})
						}
					});
				}
			});
		}
	}

	// 청구수정
	function fn_update(val) {
        fn_reset_scroll();
        var curPage = $("#pageNo").val();
		var masCd;
		if (val != null && val != '') { // 고객명 클릭 진입
			masCd = val;
		} else { // 수정 버튼 진입
			var rowCnt = 0;
			var checkbox = $("input[name='checkOne']:checked");
			checkbox.each(function(i) {
				var tr = checkbox.parent().parent().eq(i);
				masCd = tr.find(':hidden').val();
				rowCnt++;
			});

			if (rowCnt == 0) {
				swal({
					type : 'info',
					text : '수정할 대상을 선택해 주세요.',
					confirmButtonColor : '#3085d6',
					confirmButtonText : '확인'
				});
				return;
			} else if (rowCnt > 1) {
				swal({
					type : 'info',
					text : '수정할 대상을 하나만 선택해 주세요.',
					confirmButtonColor : '#3085d6',
					confirmButtonText : '확인'
				});
				return;
			}
		}

		fn_modifyCharge(masCd, 'M', curPage);
	}

	// 파일저장
	function fn_fileSave() {
	    fncClearTime();
		var rowCnt = $("#claimCnt").text();
		var alertResult = false;
		if (rowCnt == 0) {
			swal({
				type : 'info',
				text : '다운로드할 데이터가 없습니다.',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '확인'
			});
			return;
		}
		swal({
			type : 'question',
			html : "다운로드 하시겠습니까?",
			showCancelButton : true,
			confirmButtonColor : '#3085d6',
			cancelButtonColor : '#d33',
			confirmButtonText : '확인',
			cancelButtonText : '취소',
			onAfterClose: function() {
	            if(alertResult) {
	            	fnclaimfile();
		        } 
	        }
		}).then(function(result) {
			if (result.value) {
				alertResult = true;
			}
		});
	}
	// 청구등록 파일생성
	function fnclaimfile() {
		$('#search_orderBy').val($('#sSearch_orderBy option:selected').val());
		
		// 다운로드
		document.fileForm.action = "/org/claimMgmt/excelSave";
		document.fileForm.submit();
	}
	// 청구항목
	function fn_itemChanged() {
		if ($('#claimItemCd').val() != null || $('#claimItemCd').val() != '') { // 전체
			$('#itemAmt').val('');
			$('#itemAmt').attr('disabled', true);
		} else {
			$('#itemAmt').val('');
			$('#itemAmt').attr('disabled', false);
		}
	}

	// 임시저장
	function fn_pageSave(val) {
		var rowCnt = 0;
		var checkbox = $("input[name='checkOne']");
		checkbox.map(function(i) {
			rowCnt++;
		});
		if (rowCnt == 0) {
			swal({
				type : 'info',
				text : '임시저장할 데이터가 없습니다.',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '확인'
			});
			return;
		}

		swal({
			type : 'question',
			html : "임시저장하시겠습니까?",
			showCancelButton : true,
			confirmButtonColor : '#3085d6',
			cancelButtonColor : '#d33',
			confirmButtonText : '확인',
			cancelButtonText : '취소'
		}).then(function(result) {
			if (result.value) {
				// 확인
				fn_save('Y');
			}
		});
	}
	function fn_save(val) {
		var rowCnt = 0;
		var claimVals = [];
		var idxVals = [];

		var checkbox = $("input[name='checkOne']");
		checkbox.map(function(i) {
			var tr = checkbox.parent().parent().eq(i);
			var td = tr.children();

			idxVals.push(tr.children().find('input[type="text"]').val().replace(/,/gi, ""));
			claimVals.push($(this).val());

			rowCnt++;
		});

		if(rowCnt > 0) {
			var url = "/org/claimMgmt/claimTmpSave";
			var param = {
				itemList : claimVals,
				idxList : idxVals
			};
			$.ajax({
				type : "post",
				url : url,
				data : param,
				success : function(result) {
					if (val == 'Y') {
						swal({
							type : 'success',
							text : '임시저장 하였습니다.',
							confirmButtonColor : '#3085d6',
							confirmButtonText : '확인'
						}).then(function(result) {
							if (result.value) {
								fn_ClaimListCall($('#pageNo').val());
							}
						});
					} else {
						fn_ClaimListCall(val);
					}
				},
				error : function(result) {
					swal({
						type : 'error',
						text : '저장 실패하였습니다.',
						confirmButtonColor : '#3085d6',
						confirmButtonText : '확인'
					})
				}
			});
		} else {
			fn_ClaimListCall(val);
		}
	}

	// 청구정보 일괄변경 적용
	function fn_AllInsert() {
	    var masMonth = $('#claimYear').val() + $('#claimMonth').val();
		var rowCnt = $("#claimCnt").text();
        var stDt = $('#claimStartDate').val().replace(/\./gi, "");
        var edDt = $('#claimEndDate').val().replace(/\./gi, "");
        var nowDt = getCurrentDate();
        var printDt = $('#claimPrintDate').val().replace(/\./gi, "");

        if(rowCnt == 0) {
			swal({
				type : 'info',
				text : '적용할 청구대상건이 없습니다.',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '확인'
			});
			return;
		}
        
        //checkbox status(true/false)
        var isClaimMonthChecked = $('#claimMonthCheck').is(":checked");
        var isClaimItemCdChecked = $('#claimItemCdCheck').is(":checked");
        var isClaimInsPeriodChecked = $("#claimInsPeriod").is(":checked");        
        var isClaimPrintChecked = $('#claimPrintCheck').is(":checked");  
        
        //청구월 필수
        if( isClaimMonthChecked ){
        	if(!$('#claimYear').val() || !$('#claimMonth').val()){
                swal({
                    type: 'info',
                    text: '청구월을 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }
        
        //청구항목 필수
		if(isClaimItemCdChecked){
			if($('#claimItemCd option:selected').val()) {
				if (!$('#itemAmt').val()) {
					swal({
						type : 'info',
						text : '청구금액을 입력해 주세요.',
						confirmButtonColor : '#3085d6',
						confirmButtonText : '확인'
					});
					return;
				}
			} else {
				swal({
					type : 'info',
					text : '청구항목을 선택해주세요.',
					confirmButtonColor : '#3085d6',
					confirmButtonText : '확인'
				});
				return;
			}
        }
        
        //납부기한 필수
		if(isClaimInsPeriodChecked){
			if((!stDt || !edDt) || (stDt.length < 8 || edDt.length < 8) || (stDt > edDt)){
                swal({
                    type : 'info',
                    text : '납부기한을 확인해 주세요.',
                    confirmButtonColor : '#3085d6',
                    confirmButtonText : '확인'
                });
                return;
            }
			if(stDt < nowDt) {
                swal({
                    type : 'info',
                    text : '입금가능시작일은 금일부터 가능합니다.',
                    confirmButtonColor : '#3085d6',
                    confirmButtonText : '확인'
                });
                return;
            }        	
        }
        
        //체크된 항목이 없을 경우
        if(!isClaimMonthChecked && !isClaimItemCdChecked && !isClaimInsPeriodChecked){
        	swal({
                type : 'info',
                text : '수정할 항목을 체크해주세요.',
                confirmButtonColor : '#3085d6',
                confirmButtonText : '확인'
            });
            return;
        }

        if(($('#claimPrintCheck').is(":checked"))){
			if($("#claimPrintDate").val()) {
				if((stDt > printDt) || (printDt > edDt)) {
					swal({
						type : 'info',
						text : '고지서용 표시마감일은 납부기한보다 작거나 클 수 없습니다.',
						confirmButtonColor : '#3085d6',
						confirmButtonText : '확인'
					});
					return;
				}
			}else{
                $("#claimPrintDate").val(getDateFmtDot(edDt));
                printDt = edDt;
			}
		}

		var url = "/org/claimMgmt/claimRef";
		/*var param = {
			masMonth : masMonth,
			startDate : stDt,
			endDate : edDt,
			printDate : printDt,
			payItemCd : $('#claimItemCd option:selected').val(),
			payItemAmt : removeCommas($('#itemAmt').val())
		};*/
		
		var param = {};
		if( isClaimMonthChecked ){
			param.masMonth = masMonth;
		}else{
			var current_month = '${map.claimMonth}';
			param.masMonth = current_month.substring(0, 6);
		}
		
		if(isClaimItemCdChecked){
			param.payItemCd = $('#claimItemCd option:selected').val();
			param.payItemAmt = removeCommas($('#itemAmt').val());
		}
		if(isClaimInsPeriodChecked){
			param.startDate = stDt;
			param.endDate = edDt;
			param.printDate = printDt;
		}
		
		console.log(JSON.stringify(param));
		
		swal({
			type : 'question',
			html : "일괄 적용 하시겠습니까?",
			showCancelButton : true,
			confirmButtonColor : '#3085d6',
			cancelButtonColor : '#d33',
			confirmButtonText : '확인',
			cancelButtonText : '취소',
            reverseButtons: true
		}).then(function(result) {
			
			if (result.value) {
				$.ajax({
					type : "post",
					url : url,
					contentType : "application/json; charset=UTF-8",
					data : JSON.stringify(param),
					success : function(result) {
						console.log("result=\n"+JSON.stringify(result));
						
						if(result.updateCnt > 0){
							swal({
								type : 'success',
								//text : result.updateCnt + '건이 일괄 변경 적용 되었습니다.',
								text : '일괄 변경 적용 되었습니다.',
								confirmButtonColor : '#3085d6',
								confirmButtonText : '확인'
							}).then(function(result) {
								if (result.value) {
									fn_ClaimListCall();
								}
							});
						}else{
							swal({
								type : 'error',
								text : '변경 적용된 청구 대상건이 없습니다.',
								confirmButtonColor : '#3085d6',
								confirmButtonText : '확인'
							})
						}
						
					},
					error : function(result) {
						swal({
							type : 'error',
							text : '일괄적용 실패하였습니다.',
							confirmButtonColor : '#3085d6',
							confirmButtonText : '확인'
						})
					}
				});
			}
		});
	}

	// 청구등록
	function fn_claimInsert() {
		var rowCnt = 0;
		var rowAmt  = 0;
		var checkbox = $("input[name='checkOne']");
		checkbox.map(function(i) {
			var tr = checkbox.parent().parent().eq(i);
			var td = tr.children();
			rowCnt++;
		});

		if(rowCnt == 0) {
			swal({
				type : 'info',
				text : '등록할 청구대상건이 없습니다.',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '확인'
			});
			return;
		}
		
		fn_save(1);

		var url = '/org/claimMgmt/claimInsert';
		var param = {

		};
		swal({
			type : 'question',
			html : "청구 등록 하시겠습니까?",
			showCancelButton : true,
			confirmButtonColor : '#3085d6',
			cancelButtonColor : '#d33',
			confirmButtonText : '확인',
			cancelButtonText : '취소'
		}).then(function(result) {
			if (result.value) {
				// 확인
				$.ajax({
					type : "post",
					url : url,
					contentType : "application/json; charset=UTF-8",
					data : JSON.stringify(param),
					success : function(result) {
						swal({
							type : 'success',
							text : '청구 등록 완료되었습니다.',
							confirmButtonColor : '#3085d6',
							confirmButtonText : '확인'
						}).then(function(result) {
							if (result.value) {
								location.href='/org/claimMgmt/claimReg';
							}
						});
					},
					error : function(result) {
						swal({
							type : 'error',
							text : '등록 실패하였습니다.',
							confirmButtonColor : '#3085d6',
							confirmButtonText : '확인'
						})
					}
				});
			}
		});
	}

	// 개별청구 등록
	function fn_regSingleCharge() {
	    fn_reset_scroll();
		fn_regCusNameSearch(1);
		mySwiperSearchCustomer.slideTo(0); // modal창 띄울시 고객검색 화면으로 진입(현 화면에서 첫 진입이 아닐시 고객조회 화면으로 안뜸.)

		// 항목 초기화
		$('#sinMasDay').text(getFormatCurrentDate());
// 		$('#sinMasDay').val(''); // 청구일자
		$('#sinStartDate').val(''); // 납부기한
		$('#sinEndDate').val('');
		$('#sinPrintDate').val(''); // 고지서용포시마감일

		$('#reg-single-charge').modal({
			backdrop : 'static',
			keyboard : false
		});
	}

	// modal 페이징 버튼
	function modalList(num, val) {
		if (val == '1') {
			fn_regCusNameSearch(num); // 개별청구등록
		} else if (val == '2') {
			fn_claimFailList(num); // 엑셀대량등록
		}
	}

	// 이전청구불러오기
	function fn_beforeClaimSel() {
        fn_reset_scroll();
		fn_clear();

        fn_beforeYearSearch();
        fn_useClaimCnt();

		$('#load-last-bills').modal({
			backdrop : 'static',
			keyboard : false
		});
	}

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

</script>

<form id="fileForm" name="fileForm" method="post">
	<input type="hidden" id="notiMasSt" 	 name="notiMasSt" 		value="PA00">
	<input type="hidden" id="search_orderBy" name="search_orderBy">
</form>

<input type="hidden" id="order"   name="order"   value="${map.search_orderBy}">
<input type="hidden" id="pageCnt" name="pageCnt" value="${map.PAGE_SCALE}">
<input type="hidden" id="pageNo"  name="pageNo"  value="1">

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link active" href="#">청구등록</a>
			<a class="nav-link" href="#">청구조회</a>
			<a class="nav-link" href="#">청구항목관리</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div id="breadcrumb-in-title-area" class="row align-items-center">
				<div class="col-12 text-right">
					<img src="/assets/imgs/common/icon-home.png" class="mr-2">
					<span> > </span>
					<span class="depth-1">청구관리</span>
					<span> > </span>
					<span class="depth-2 active">청구등록</span>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2>청구등록</h2>
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
						청구 대상 및 내용을 생성하여 청구등록 하는 화면입니다.<br/>
						개별청구 등록 및 이전 청구정보 불러오기, 엑셀파일 대량등록의 세 가지 방식을 제공합니다.<br/>
						신규고객 청구등록 시, [고객등록] 메뉴를 통해 가상계좌를 부여한 고객(납부자)을 대상으로 청구등록 할 수 있습니다. 새로운 고객이 있다면 고객등록 메뉴에서 먼저 추가해주세요.
					</p>
				</div>
			</div>
		</div>
	</div>
	<div class="container mb-5">
		<div id="payer-registration-option">
			<div class="row">
				<div class="col col-md-4 col-4 text-center" onclick="fn_regSingleCharge();">
					<a href="#" class="btn-reg-single-charge">
					<img src="/assets/imgs/common/icon-reg-charge.png" class="icons">
						<span>개별청구<br>등록
					</span>
					</a>
				</div>
				<div class="col col-md-4 col-4 text-center" onclick="fn_beforeClaimSel();">
					<a href="" class="btn-reg-single-charge" data-toggle="modal">
						<img src="/assets/imgs/common/icon-load-last-charge-list.png" class="icons">
						<span>이전 청구정보<br>불러오기</span>
					</a>
				</div>
				<div class="col col-md-4 col-4 text-center"  onclick="fn_regBulkPayer(); overFlowHidden();">
					<a href="" class="btn-reg-single-charge" data-toggle="modal">
					<img src="/assets/imgs/common/icon-excel-upload.png" class="icons">
						<span>엑셀파일<br>대량등록
					</span>
					</a>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row no-gutters mt-3">
			<div class="col-6">
				<h5>청구정보 일괄변경
					<a tabindex="0" class="popover-dismiss" role="button" data-toggle="popover" data-trigger="focus"
					   title="청구정보 일괄변경이란?" data-content="청구대상 목록의 모든 대상 건들을 한번의 조건 선택으로 일괄 정보 변경할 수 있는 기능">
						<img src="/assets/imgs/common/icon-info.png">
					</a>
				</h5>
			</div>
			<div class="col-6 text-right">
				<span class="text-danger"></span>
			</div>
		</div>
	</div>

	<div class="container mt-2 mb-2" style="background: #fff;">
		<div id="page-description" style="background: #fff;border: none;margin-bottom: 0;padding: 0;margin-top: 0;">
			<div class="row col">
				<ul>
					<li>수정하시려는 항목을 체크박스에서 선택 하셔야 수정 반영됩니다.</li>
					<li>납부시작일은 금일날짜 이전으로 설정하실 수 없습니다.</li>
					<li>단, 고지서용 표시마감일 납부기한 변경 시 수정하실 수 있으며, 고지서용 표시마감일을 미입력 시 자동으로 납부마감일로 설정되니 참고바랍니다.</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="container mb-0">
		<div class="search-box">
			<div class="row">
				<input class="form-check-input" type="checkbox" id="claimMonthCheck" name="claimMonthCheck">
				<label class="col-form-label col col-md-2 col-sm-4 col-4" for="claimMonthCheck">
					<span class="text-danger"></span> 청구월
				</label>
				<div class="col col-md-4 col-sm-8 col-8 form-inline year-month-dropdown">
					<select class="form-control" name="claimYear" id="claimYear">
						<option>선택</option>
					</select>
					<span class="ml-1 mr-1">년</span>
					<select class="form-control" name="claimMonth" id="claimMonth">
						<option>선택</option>
					</select>
					<span class="ml-1">월</span>
				</div>
				<input class="form-check-input" type="checkbox" id="claimItemCdCheck" name="claimItemCdCheck" checked />
				<label class="col-form-label col col-md-2 col-sm-4 col-4" for="claimItemCdCheck">
					<span class="text-danger"></span> 청구항목
				</label>
				<div class="col col-md-4 col-sm-8 col-8 form-inline">
					<select class="form-control parents" name="claimItemCd" id="claimItemCd" onchange="fn_itemChanged();">
						<option value="">선택</option>
						<c:forEach var="claimItemCd" items="${map.claimItemCd}">
							<option value="${claimItemCd.code}">${claimItemCd.codeName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="row">
				<input class="form-check-input" type="checkbox" id="claimInsPeriod" name="claimInsPeriod">
				<label class="col-form-label col col-md-2 col-sm-4 col-4" for="claimInsPeriod">
					<span class="text-danger"></span> 납부기한 수정
				</label>
				<div class="col col-md-4 col-sm-8 col-8 form-inline">
					<div class="date-input">
						<label class="sr-only" for="inlineFormInputGroupUsername">From</label>
						<div class="input-group">
							<input type="text" class="form-control date-picker-input" id="claimStartDate" placeholder="YYYY.MM.DD" aria-label="From"
								   aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
						</div>
					</div>
					<span class="range-mark"> ~ </span>
					<div class="date-input">
						<label class="sr-only" for="inlineFormInputGroupUsername">To</label>
						<div class="input-group">
							<input type="text" class="form-control date-picker-input" id="claimEndDate" placeholder="YYYY.MM.DD" aria-label="To"
								   aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
						</div>
					</div>
				</div>
				<label class="col-form-label col col-md-2 col-sm-4 col-4">
					청구금액 일괄변경 </label>
				<div class="col col-md-4 col-sm-8 col-8 form-inline">
					<input type="text" id="itemAmt" class="form-control first-depth-child" style="text-align: right;"
						   onkeydown="onlyNumber(this)" maxlength="22" disabled="disabled" maxlength="8">
				    <span class="ml-1">원</span>
				</div>
			</div>
			<div class="row">
				<input class="form-check-input" type="checkbox" id="claimPrintCheck" name="claimPrintCheck" disabled="disabled">
				<label class="col-form-label col col-md-2 col-sm-4 col-4" for="claimPrintCheck">
					<span class="text-danger"></span> 고지서용 표시마감일
				</label>
				<div class="col col-md-4 col-sm-8 col-8 form-inline">
					<div class="date-input">
						<label class="sr-only">From</label>
						<div class="input-group">
							<input type="text" class="form-control date-picker-input" id="claimPrintDate" placeholder="YYYY.MM.DD" aria-label="From"
								   aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
						</div>
					</div>
				</div>
			</div>

			<div class="row form-inline mt-3">
				<div class="col-12 text-center">
					<button class="btn btn-primary btn-wide" onclick="fn_AllInsert();">적용</button>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div id="chargeable-list" class="list-id">
			<div class="row no-gutters mt-3">
				<div class="col-6">
					<h5>청구대상 목록</h5>
				</div>
				<div class="col-6 text-right"></div>
			</div>
			<input type="hidden" name="delYN" id="delYN" value="${map.delYN}">
			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="claimCnt">${map.count}</em>건]</span>
					<span class="amount mr-2">청구건수 [총 <em class="font-blue" id="claimClientCnt">${map.clientCount}</em>건]</span>
					<span class="amount mr-2">청구금액 [총 <em class="font-blue" id="claimAmtSum"></em>원]</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<select class="form-control" name="sSearch_orderBy" id="sSearch_orderBy" onchange="fn_save(1);">
						<option value="masMonth" 	>청구월순 정렬</option>
						<option value="cusName" 	>고객명순 정렬</option>
						<option value="payItemName" >청구항목순 정렬</option>
                        <option value="cusGubn"     >고객구분순 정렬</option>
						<option value="date" 	    >납부기한순 정렬</option>
					</select> <select class="form-control" name="PAGE_SCALE" id="PAGE_SCALE" onchange="fn_save(1);">
						<option value="10"  >10개씩 조회</option>
						<option value="20"  >20개씩 조회</option>
						<option value="50"  >50개씩 조회</option>
						<option value="100" >100개씩 조회</option>
						<option value="200" >200개씩 조회</option>
					</select>
					<button class="btn btn-sm btn-d-gray hidden-on-mobile" type="button"	onclick="fn_fileSave();">파일저장</button>
				</div>
			</div>

			<form id="gridFrom" name="gridFrom" method="post">
				<div class="row">
					<div class="table-responsive pd-n-mg-o col mb-3">
						<table class="table table-sm table-hover table-primary" id="gridTb">
							 <colgroup>
								 <col width="52">
								 <col width="60">
								 <col width="100">
								 <col width="100">
								 <c:forEach var="cusGbList" items="${map.cusGbList}">
									 <col width="130">
								 </c:forEach>
								 <col width="140">
								 <col width="130">
								 <col width="140">
								 <col width="180">
								 <col width="120">
							 </colgroup>

							<thead id="mainHeader">
								<tr>
									<th>
										<input class="form-check-input table-check-parents" type="checkbox" id="row-th">
										<label for="row-th"><span></span></label>
									</th>
									<th>NO</th>
									<th>청구월</th>
									<th>고객명</th>
									<c:forEach var="cusGbList" items="${map.cusGbList}">
										<th>${cusGbList.codeName}</th>
									</c:forEach>
									<th>가상계좌</th>
									<th>청구항목명</th>
									<th>청구금액(원)</th>
									<th class="border-r-e6">납부기한</th>
									<th class="border-l-n">고지서용<br>표시마감일</th>
								</tr>
							</thead>
							<tbody id="mainBody">
								<c:set var="colCnt" value="#{map.cusGbCnt + 9}" />
								<tr>
									<td colspan="${colCnt}" class="text-center">
										[조회된 내역이 없습니다.]
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</form>

			<div class="row list-button-group-bottom  hidden-on-mobile">
				<div class="col-6">
					<button class="btn btn-sm btn-gray-outlined" onclick="fn_selDelete();">선택삭제</button>
				</div>
				<div class="col-6 text-right">
					<button class="btn btn-sm btn-d-gray" onclick="fn_update();">정보수정</button>
					<button class="btn btn-sm btn-d-gray" onclick="fn_pageSave('btn');">임시저장</button>
					<a tabindex="0" class="popover-dismiss ml-2" role="button" data-toggle="popover" data-trigger="focus"
					   title="임시저장이란?" data-content="작업 중이던 청구정보를 클릭하는 시점에서 중간저장하는 기능 (청구등록 및 전체 삭제가 되면, 자동으로 초기화됩니다.)">
						<img src="/assets/imgs/common/icon-info.png">
					</a>
				</div>
			</div>

			<jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />
		</div>
	</div>

	<div class="row list-button-group-bottom hidden-on-mobile">
		<div class="col text-center">
			<button class="btn btn-primary" onclick="fn_claimInsert();">청구등록</button>
		</div>
	</div>

	<div class="container">
		<div id="quick-instruction" class="foldable-box">
			<div class="row foldable-box-header">
				<div class="col-8">
					<img src="/assets/imgs/common/icon-notice.png"> 알려드립니다.
				</div>
				<div class="col-4 text-right">
					<img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
				</div>
			</div>
			<div class="row foldable-box-body">
				<div class="col">
					<h6>■ 청구정보</h6>
					<ul>
						<li>납부기한 : 당일부터 적용 가능</li>
						<li>고지서용 표시마감일 : 입금가능 날짜인 납부기한과 관계없이 고지 출력 시 고지서에 표기되는 납부마감일</li>
						<li>청구항목 : 청구항목명 및 항목금액 입력 필수</li>
						<li class="text-danger">청구항목명은 [청구관리 > 청구항목관리] 메뉴에서 설정한 것 중 선택가능</li>
					</ul>

					<h6>■ 개별청구등록</h6>
					<ul>
						<li>고객설정의 '신규고객'은 최근 30일 이내 등록한 고객</li>
						<li>고객설정의 ＇전체고객'은 신규고객 외 고객(단, 삭제고객, 납부제외 고객은 포함되지 않음)</li>
						<li>고객조회 결과 중 청구 고객명 선택 후, 청구 정보 입력(필수정보 : 청구항목, 청구금액, 입금가능 일자)</li>
					</ul>

					<h6>■ 이전 청구정보 불러오기</h6>
					<ul>
						<li>이전 청구월 선택 후, 신규로 등록할 청구월을 선택</li>
						<li>청구대상 목록 표를 확인한 후, 필수정보(입금가능기간) 및 선택정보 확인 후 하단 ‘청구등록’버튼을 클릭하여 청구 완료</li>
					</ul>

					<h6>■ 엑셀파일 대량등록</h6>
					<ul>
						<li>엑셀양식 다운로드 : 양식 생성을 위한 조건 설정 후 우측 '엑셀양식 다운로드' 버튼 클릭<span class="text-danger">(* 조건 미 선택 시 기본 양식만 다운로드 됨)</span></li>
						<li>파일등록 : 다운로드 받은 엑셀파일에 항목값을 입력하여 '파일찾기'버튼을 통해 업로드 및 '청구대상 추가' 버튼 클릭</li>
						<li>대량등록 실패내역 : 엑셀자료 등록 중 실패된 내역을 노출, '등록 실패건 엑셀다운로드' 버튼을 클릭하여 양식 수정 후 업로드 후 재적용</li>
					</ul>

					<h6>■ 청구정보 일괄변경</h6>
					<ul>
						<li>하단의 청구대상 목록 전체에 대해 일괄 변경(청구월, 납부기한, 고지서용 표시마감일, 청구항목 및 금액) </li>
						<li>여러 청구항목의 금액을 수정하고자 하실 경우, 청구항목별로 선택한 후 적용</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<%-- 개별청구등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-single-charge.jsp" flush="false" />

<%-- 개별청구수정 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/modify-charge.jsp" flush="false" />

<%-- 이전 청구정보 불러오기 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/load-last-bills.jsp" flush="false" />

<%-- 청구대량등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-bulk-by-excel.jsp" flush="false" />

<%-- 엑셀파일 대량등록 폼 선택 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-bulk-form-selecter.jsp" flush="false"/>

<%-- 대량등록 양식 변환 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-bulk-asis-payer.jsp" flush="false"/>

<script type="text/javascript">
	var delMessage = '선택된 청구대상을 삭제하시겠습니까?';

	var modMessage = '수정할 청구건을 선택해주세요.'

	var noSelectMsg = "삭제할 청구대상건을 선택하세요.";

    // 대량등록
    function fn_regSelecter() {
        fn_reset_scroll();

        $('#reg-bulk-form-selecter').modal({backdrop:'static', keyboard:false});
    }

	$(document).ready(function() {
        $('.popover-dismiss').popover({
            trigger: 'focus'
        });

		$("label[for=load-all-payer]").click(function() {
			$("select.condition").attr("disabled", true);
			$("input.condition").attr("disabled", true).val("");
		});

		$("label[for=load-unregisted-payer]").click(function() {
			$(".condition").attr("disabled", false);
		});
	});

	var settings = {
		// Optional parameters
		direction : 'horizontal',
		// 		autoHeight: true,
		loop : false,
		autoplay : false,
		simulateTouch : false,
		observer : true,
		observeParents : true,
		allowTouchMove : false,
	}

	var mySwiperSearchCustomer = new Swiper('.charge-single-payer-slider', settings);

    function fn_regBulkPayer() {
        $("#reg-bulk-form-selecter .close-modal").click();
        $('#reg-bulk-by-excel').modal({backdrop:'static', keyboard:false});
    }

    function fn_regBulkAsisPayer() {
        $("#reg-bulk-form-selecter .close-modal").click();
        $('#reg-bulk-asis-payer').modal({backdrop:'static', keyboard:false});
    }

    function overFlowHidden() {
        $("body").css("overflow-y", "hidden");
        $("#reg-bulk-by-excel").css("overflow-y", "auto");
	}

    function overFlowAuto() {
        $("body").css("overflow-y", "");
        $("#reg-bulk-by-excel").css("overflow-y", "hidden");
    }
</script>
