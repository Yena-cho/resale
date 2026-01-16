<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<script>

var firstDepth = "nav-link-32";
var secondDepth = "sub-03";

</script>

<script type="text/javascript">
$(function(){
	$( "#resultBody" ).sortable({
		cursor: "move", 
		placeholder: "ui-state-highlight", 
		axis: "y",
		beforeStop: function( event, ui ) { 
				var idx = 1;
				$("#resultBody > tr").each(function(f, tr){ // $.each 의 인덱스 사용시 hidden 처리된 element 때문에 index 값이 흐트러짐
					if (!$(tr).hasClass("ui-sortable-placeholder")) {
						$(tr).find("td").last().html(idx + '<span class="ui-icon ui-icon-arrowthick-2-n-s"></span>');
						idx++;
					}
				});
			}
	});
	
    $( "#resultBody" ).disableSelection();
	
    $('.popover-dismiss').popover({
        trigger: 'focus'
    });

	if('${map.accList.size()}' > 0) {
		$('#accInfo').show();
		$('#accInfoList').show();
		$('#adjGroupInfo').show();
		$('#adjGroupInfoList').show();
	} else {
		$('#accInfo').hide();
		$('#accInfoList').hide();
		$('#adjGroupInfo').hide();
		$('#adjGroupInfoList').hide();
	}
	
	var cuPage = 1;

	$("#row-th").click(function(){
		$("input[type=checkbox]").prop("checked",$("#row-th").prop("checked"));
	});
	
	fn_MoneyPassbook();
});

function list(page){
	fn_ClaimItemListCall(page);
}

function fn_ClaimItemListCall(page) {
	if(page == null || page == 'undefined'){
		cuPage = '1';
	} else {
		cuPage = page;
	}
	var url = "/org/claimMgmt/claimItemMgmtAjax";
	var param = {
			curPage : cuPage
	};
	
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify(param),
		success: function(data){
			$('#row-th').prop('checked', false);
			fnGrid(data, 'resultBody');
		},
		error:function(data){
			swal({
	           type: 'error',
	           text: '조회 실패하였습니다.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       })
		}
	});
}

function fnGrid(result, obj) {
	var str = '';
	$('#count').text(result.count);
	if(result.count <= 0){
		str += '<tr><td colspan="8" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
	}else{
		$.each(result.list, function(k, v){
			str += '<tr>';
			str += '<td>';
			str += '<input class="form-check-input table-check-child" type="checkbox" name="checkOne" id="row-' + v.rn + '" value="' + v.payItemCd + '" onclick="fn_listCheck();" onchange="changeBGColor(this)">';
			str += '<label for="row-' + v.rn + '"><span></span></label>';
			str += '</td>';
			str += '<td>' + v.payItemCd + '</td>';
			str += '<td><a href="#" onclick="fn_selXadjGroup(\'U\', \''+ v.payItemCd +'\');">'+ v.payItemName +'</a></td>';
            str += '<td>'+ (nullValueChange(v.payItemSelYN) === 'Y' ? '선택' : '필수') +'</td>';
			str += '<td>'+ nullValueChange(v.grpadjName) +'</td>';
			str += '<td>'+ (nullValueChange(v.rcpItemYN) === 'Y' ? '발급' : '발급안함') +'</td>';
			str += '<td>'+ nullValueChange(v.chaOffNo) +'</td>';
			str += '<td>'+ nullValueChange(v.ptrItemName) +'</td>';
			str += '<td>'+ (k+1) +'<span class="ui-icon ui-icon-arrowthick-2-n-s"></span></td>';
			str += '</tr>';
		});
	}
	$('#' + obj).html(str);
}

function fn_deleteClaimItem() {
	var rowCnt = 0;
	var itemDelVals = [];
	var checkbox = $("input[name='checkOne']:checked");
	checkbox.each(function(i) {
		var tr = checkbox.parent().parent().eq(i);
		var td = tr.children();

    	itemDelVals.push($(this).val());
        rowCnt++;
    });

	if(rowCnt == 0) {
		swal({
	           type: 'info',
	           text: noSelectMsg,
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       });
	} else {
		swal({
	        type: 'question',
	        html: "이전청구 불러오기 기능 사용에 제한될 수 있습니다.<br> 청구항목을 삭제하시겠습니까?", //delMessage,
	        showCancelButton: true,
	        confirmButtonColor: '#3085d6',
	        cancelButtonColor: '#d33',
	        confirmButtonText: '확인',
	        cancelButtonText: '취소'
		}).then(function(result) {
			if (result.value) {
				// 삭제
				var url = "/org/claimMgmt/deleteItem";
				var param = {
						itemList : itemDelVals
				};
				$.ajax({
					type: "GET",
					url: url,
					data: param,
					success: function(result){
						swal({
				           type: 'success',
				           text: '청구항목을 삭제하였습니다.',
				           confirmButtonColor: '#3085d6',
				           confirmButtonText: '확인'
				       }).then(function(result) {
						  if (result.value) {
							  fn_ClaimItemListCall();
						  }
					   });

					},
					error:function(result){
						swal({
				           type: 'error',
				           text: '청구항목을 삭제 실패하였습니다.',
				           confirmButtonColor: '#3085d6',
				           confirmButtonText: '확인'
				       })
					}
				});
			}
		});
	}
}

function fn_orderUpdate() {
	var rowCnt = 0;
	var itemVals = [];
	var idxVals  = [];
	var checkbox = $("input[name='checkOne']:checked");
	checkbox.each(function(i) {

    	var tr = checkbox.parent().parent().eq(i);
		var td = tr.children();

		itemVals.push($(this).val());
		idxVals.push(td.eq(7).text()); // 순서변경되면 안됨.
        rowCnt++;
    });

    swal({
        type: 'question',
        html: "순서를 변경하시겠습니까?",
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
	}).then(function(result) {
		if (result.value) {
			if(rowCnt != 2) {
		    	swal({
		            type: 'info',
		            text: '순서변경할 항목을 두개 선택해 주세요.',
		            confirmButtonColor: '#3085d6',
		            confirmButtonText: '확인'
		        })
				return;
		    }

			// 변경
			var url = "/org/claimMgmt/orderByItem";
			var param = {
					itemList : itemVals,
					idxList : idxVals
			};
			$.ajax({
				type: "post",
				url: url,
				data: param,
				success: function(result){
					swal({
			           type: 'success',
			           text: '순서를 변경하였습니다.',
			           confirmButtonColor: '#3085d6',
			           confirmButtonText: '확인'
			       }).then(function(result) {
					  if (result.value) {
						  fn_ClaimItemListCall();
					  }
				   });

				},
				error:function(result){
					swal({
			           type: 'error',
			           text: '순서변경에 실패하였습니다.',
			           confirmButtonColor: '#3085d6',
			           confirmButtonText: '확인'
			       })
				}
			});
		}
	});
}

function fn_selXadjGroup(str, val) {
	var url = "/org/claimMgmt/selXadjGroup";
	$.ajax({
		type: "GET",
		url: url,
		data: {},
		success: function(data){
			if(str == 'I') {
                fn_ClaimItemIns(data.adj);
            } else {
                fn_ClaimItemModify(val, data.adj);
            }
		}
	});
}

// 청구항목수정
function fn_ClaimItemModify(val, str) {
    fn_reset_scroll();

	var itemCd;
	if(val != null && val != '') {
		itemCd = val;
	} else {
		var checkbox = $("input[name=checkOne]:checked");
		var chkCnt = 0;

		checkbox.each(function(i) {
			itemCd = $(this).val();
			chkCnt++;
		});

		if(chkCnt == 0) {
			swal({
		           type: 'info',
		           text: '수정할 항목을 선택해 주세요.',
		           confirmButtonColor: '#3085d6',
		           confirmButtonText: '확인'
		       });
			return;
		}
		
        swal({
               type: 'info',
               text: '수정할 항목을 하나만 선택해 주세요.',
               confirmButtonColor: '#3085d6',
               confirmButtonText: '확인'
        });
	}
	
	var url = "/org/claimMgmt/detailItem";
	$.ajax({
		type: "post",
		url: url,
		data: "payItemCd=" + itemCd,
		success: function(data){
			$('#itemTitleL').text('청구항목수정');
			$('#itemTitleS').text('청구항목수정');
			if(str == "Y") {
				$("#grpadjY").show();			
			} else {
				$("#grpadjY").hide();
			}
			$("#adjaccyn").val(str);
			$("#payItemCd").val(data.detail.payItemCd);
			$("#grpadjNm").val(data.detail.adjfiRegKey);
			$("#adjfiRegKey").val(data.detail.adjfiRegKey);
			$("#payItemName").val(data.detail.payItemName);
			$("#ptrItemName").val(data.detail.ptrItemName);
			
			if(data.detail.rcpItemYN == 'Y') {
				$("#receiptable-yes").prop('checked', true);
			} else if(data.detail.rcpItemYN == 'N')  {
				$("#receiptable-no").prop('checked', true);
			}
			if(data.detail.payItemSelYN == 'Y') {
				$("#optional-yes").prop('checked', true);
			} else if(data.detail.payItemSelYN == 'N')  {
                $("#optional-no").prop('checked', true);
			}
			if(data.detail.payItemSt == 'Y') {				//항목표시여부
				$("#visibility-yes").attr('checked', true);
			} else {
				$("#visibility-no").attr('checked', true);
			}
			$('#itmeName').val(data.detail.payItemName); // 청구항목명을 전역변수에 담음
			$('#payNameY').hide();
		    $('#payNameN').hide();
			$(".ajax-loader-area").attr("disabled", true);
			$("#add-charge-classification").modal({backdrop:'static', keyboard:false}); // 청구항목수정 및 등록 modal
		}
	});

}

// 등록팝업
function fn_ClaimItemIns(str) {
    fn_reset_scroll();

	if($('#count').text() >= 100) {
		swal({
	       type: 'info',
	       text: "청구항목은 최대 100개까지만 등록 가능합니다.",
	       confirmButtonColor: '#3085d6',
	       confirmButtonText: '확인'
		});
		return;
	} else {
		$('#itemTitleL').text('청구항목등록');
		$('#itemTitleS').text('청구항목등록');
		if(str == "Y") {
			$("#grpadjY").show();			
		} else {
			$("#grpadjY").hide();
		}
		$("#adjaccyn").val(str);
		$("#payItemCd").val('');
		$("#grpadjNm option:eq(0)").prop("selected", true);
		$("#adjfiRegKey").val($("#grpadjNm").val());
		$("#payItemName").val('');
		$("#ptrItemName").val('');
        $("#receiptable-yes").prop('checked', true);
        $("#optional-no").prop('checked', true);
		$("#visibility-yes").prop('checked', false);
		$("#visibility-no").prop('checked', false);
		$('#itmeName').val('');
		$('#nameUseCk').val('C');

		$("#add-charge-classification").modal({backdrop: 'static', keyboard: false});
	}
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

function fnPayItemsSorted() {
	var rowCnt   = 0;
	var itemVals = [];
	var idxVals  = [];
	var checkbox = $("input[name='checkOne']");
	checkbox.each(function(i) {
		rowCnt++;
    	var tr = checkbox.parent().parent().eq(i);
		var td = tr.children();

		itemVals.push($(this).val());
		idxVals.push(td.last().text());
    });
	
	if(rowCnt == 0) {
		swal({
			type: 'info',
            text: "순서변경할 청구항목이 없습니다.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
		});
		return;
	}
	
    swal({
        type: 'question',
        html: "순서를 변경하시겠습니까?",
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
	}).then(function(result) {
		if (!result.value) {
		    return;
        }

        var url = "/org/claimMgmt/orderByItem";
        var param = {
                itemList : itemVals,
                idxList : idxVals
        };
        
        $.ajax({
            type: "post",
            url: url,
            data: param,
            success: function(){
                swal({
                   type: 'success',
                   text: '순서를 변경하였습니다.',
                   confirmButtonColor: '#3085d6',
                   confirmButtonText: '확인'
               }).then(function(result) {
                  if (result.value) {
                      fn_ClaimItemListCall();
                  }
               });

            },
            error:function(){
                swal({
                   type: 'error',
                   text: '순서변경에 실패하였습니다.',
                   confirmButtonColor: '#3085d6',
                   confirmButtonText: '확인'
               })
            }
        });
	});
}

</script>

<input type="hidden" id="itmeName" name="itmeName">

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link" href="#">청구조회</a> <a class="nav-link" href="#">청구등록</a>
			<a class="nav-link active" href="#">청구항목관리</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div id="breadcrumb-in-title-area" class="row align-items-center">
				<div class="col-12 text-right">
					<img src="/assets/imgs/common/icon-home.png" class="mr-2"> <span>
						> </span> <span class="depth-1">청구관리</span> <span> > </span> <span
						class="depth-2 active">청구항목관리</span>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2>청구항목관리</h2>
				</div>
				<div class="col-6 text-right">
					<%--<button type="button" class="btn btn-img">--%>
						<%--<img src="/assets/imgs/common/btn-typo-control.png">--%>
					<%--</button>--%>
					<%--<button type="button" class="btn btn-img">--%>
						<%--<img src="/assets/imgs/common/btn-print.png">--%>
					<%--</button>--%>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div id="page-description">
			<div class="row">
				<div class="col-12">
					<p>입금을 요청할 청구항목 명칭 및 입금계좌를 설정하는 화면입니다. (최대 50개 등록가능)</p>
				</div>
			</div>
		</div>
	</div>
	<div class="container mb-3">
		<div id="search-result" class="list-id">
			<div class="table-option row mb-2">
				<div class="col-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="count">${map.count}</em>건]
					</span>
				</div>
			</div>
			<form name="gridForm" id="gridForm" method="post">
				<div class="row">
					<div class="table-responsive col mb-3">
						<table class="table table-sm table-hover table-primary" id="gridTb">
							<colgroup>
								<col width="52">
								<col width="130">
								<col width="170">
								<col width="150">
								<col width="150">
								<col width="120">
								<col width="180">
								<col width="200">
								<col width="108">
							</colgroup>

							<thead>
								<tr>
									<th>
										<input class="form-check-input table-check-parents" type="checkbox" name="row-th" id="row-th">
										<label for="row-th"><span></span></label>
									</th>
									<th>항목코드</th>
									<th>항목명</th>
                                    <th>필수여부</th>
									<th>입금통장명</th>
									<th>현금영수증<br/>발급가능여부</th>
									<th>현금영수증발급<br/>사업자번호</th>
									<th>고지서출력<br/>대체항목명</th>
									<th>출력순서
										<a tabindex="0" class="popover-dismiss" role="button" data-toggle="popover" data-trigger="focus" title=""
										   data-content="수정할 청구항목을 마우스로 누른 상태로 상/하 위치를 변경한 후 '순서적용'버튼 클릭!" data-original-title="순서변경기능 사용방법">
											<img src="/assets/imgs/common/icon-info.png">
										</a>
									</th>
								</tr>
							</thead>
							<tbody id="resultBody">
								<c:choose>
									<c:when test="${map.count > 0}">
										<c:forEach var="row" items="${map.list}" varStatus="status">
											<c:set var="payItem" value="${row.payItemCd}" />
											<c:set var="rowNum" value="row-${row.rn}" />
											<tr>
												<td>
													<input class="form-check-input table-check-child" type="checkbox" name="checkOne" id="row-${rowNum}" value="${payItem}" onclick="fn_listCheck();" onchange="changeBGColor(this)">
													<label for="row-${rowNum}"><span></span></label>
												</td>
												<td>${row.payItemCd}</td>
												<td><a href="#" onclick="fn_selXadjGroup('U', ${payItem});">${row.payItemName}</a></td>
												<td><c:choose><c:when test="${row.payItemSelYN eq 'Y'}">선택</c:when><c:otherwise>필수</c:otherwise></c:choose></td>
												<td>${row.grpadjName}</td>
												<td><c:choose><c:when test="${row.rcpItemYN eq 'Y'}">발급</c:when><c:otherwise>발급안함</c:otherwise></c:choose></td>
												<td>${row.chaOffNo}</td>
												<td>${row.ptrItemName}</td>
												<td>${status.count}<span class="ui-icon ui-icon-arrowthick-2-n-s"></span></td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="9" class="text-center">
												[조회된 내역이 없습니다.]
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</div>
			</form>
			
			<div class="row mb-4">
				<div class="col-6">
					<button class="btn btn-sm btn-gray-outlined" onclick="fn_deleteClaimItem();">선택삭제</button>
				</div>

				<div class="col-6 text-right">
					<button class="btn btn-sm btn-d-gray" id="modify-charge-classification" onclick="fnPayItemsSorted()">순서적용</button>
					<button class="btn btn-sm btn-d-gray" data-toggle="modal" onclick="fn_selXadjGroup('I', '');">청구항목등록</button>
				</div>
			</div>

			<h6 id="accInfo">입금계좌정보</h6>
			<div class="table-responsive mb-5" id="accInfoList">
				<table class="table table-sm table-hover table-primary">
					<colgroup>
						<col width="68">
						<col width="450">
						<col width="122">
						<col width="470">
					</colgroup>

					<thead>
						<tr>
							<th>NO</th>
							<th>입금통장명</th>
							<th>은행</th>
							<th>계좌정보</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(map.accList) > 0}">
								<c:forEach var="row" items="${map.accList}">
								<tr>
									<td>${row.rn}</td>
									<td>${row.grpadjName}</td>
									<td>신한은행</td>
									<td>${row.adjfiRegKey}</td>
								</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="4" style="text-align: center;">
										[조회된 내역이 없습니다.]
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<div class="container">
		<div id="quick-instruction" class="foldable-box">
			<div class="row foldable-box-header">
				<div class="col-8">
					<img src="/assets/imgs/common/icon-notice.png"> 알려드립니다.
				</div>
				<div class="col-4 text-right">
					<img src="/assets/imgs/common/btn-arrow-notice.png"
						class="fold-status">
				</div>
			</div>
			<div class="row foldable-box-body">
				<div class="col">
					<h6>■ 청구항목등록</h6>
					<ul>
						<li>청구항목 표 하단 우측의 '등록'버튼 클릭하여 청구항목등록 팝업 호출</li>
						<li>필수정보(입금통장명, 청구항목명) 입력 후 등록<span class="text-danger">(* 입금통장명 선택에 따라 입금될 계좌번호 변경)</span></li>
					</ul>

					<h6>■ 청구항목수정</h6>
					<ul>
						<li>수정할 청구항목의 '항목명' 클릭하여 청구항목수정 팝업 호출</li>
						<li>등록된 청구 항목 정보가 노출되면, 수정 반영 후 '저장' 버튼 클릭</li>
					</ul>

					<h6>■ 청구항목 순서변경</h6>
					<ul>
						<li>수정할 청구항목과 수정될 청구항목을 원하는 순서로 드래그&드롭, '순서적용' 버튼 클릭</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<%-- 청구항목등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/add-charge-classification.jsp"	flush="false" />

<script type="text/javascript">

var delMessage = '선택된 청구항목을 삭제하시겠습니까?';
var noSelectMsg = '선택하신 삭제건이 없습니다.';

</script>
