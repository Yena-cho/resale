<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
    function hiddenLoader() {
        $(".ajax-loader-area").attr("hidden", false);
    }

    function fn_MoneyPassbook() {
        $('#payNameY').hide();
        $('#payNameN').hide();
    }

    //수정 및 등록
    function fn_insClaimItem() {
        hiddenLoader();
        if (!$("#payItemName").val()) {	// 청구항목명
            swal({
                type: 'info',
                text: '청구항목명을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#payItemName").focus();
            });
            return;
        }
        if ($('#payItemCd').val() != null || $('#payItemCd').val != "") { // 코드값이 있으면 수정
            if ($('#itmeName').val() == $('#payItemName').val()) {
                $('#nameUseCk').val('Y');
            }
        }
        if ($('#nameUseCk').val() == 'C') {
            swal({
                type: 'info',
                text: '청구항목명을 중복 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#payItemName").focus();
            });
            return;
        }
        
        if ($('#nameUseCk').val() == 'N') {
            if ($("#payItemName").val().length < 2) {
                text = '청구항목명은 두 글자 이상 입력해 주세요.';
            } else {
                text = '청구항목명이 중복되어 사용할 수 없습니다. 다시 입력해 주세요.';
            }
            swal({
                type: 'info',
                text: text,
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#payNameN').hide();
                $("#payItemName").val('');
                $("#payItemName").focus();
                $('#nameUseCk').val('C');
            });
            return;
        }

        if (!$('#receiptable-yes').prop("checked") && !$('#receiptable-no').prop("checked")) {
            swal({
                type: 'info',
                text: '현금영수증 발급여부를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            text: "등록하시겠습니까?",
            type: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (!result.value) {
                return;
            }

            fn_ClaimItemInsCall(); // 수정 및 등록
        });
    }

    //수정 및 등록
    function fn_ClaimItemInsCall() {
        hiddenLoader();
        var receiptable;
        if ($('#receiptable-yes').prop("checked") && !$('#receiptable-no').prop("checked")) {
            receiptable = 'Y';
        } else {
            receiptable = 'N';
        }

        var payItemSelYN;
        if ($('#optional-yes').prop("checked")) {
            payItemSelYN = 'Y';
        } else {
            payItemSelYN = 'N';
        }

        var url = "/org/claimMgmt/modifyItem";
        var param = {
            payItemCd: $('#payItemCd').val(),
            payItemName: $('#payItemName').val(),
            adjfiRegKey: $('#grpadjNm').val(),
            rcpItemYN: receiptable,
            payItemSelYN: payItemSelYN,
            ptrItemName: $('#ptrItemName').val()
        };
        $.ajax({
            type: "post",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                swal({
                    type: 'success',
                    title: '등록되었습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function (result) {
                    if (result.value) {
                        $("#add-charge-classification").modal("hide");
                        fn_ClaimItemListCall();
                        $('#nameUseCk').val('N');
                    }
                });
            },
            error: function (data) {
                swal({
                    type: 'error',
                    text: '등록 실패하였습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
            }
        });

    }

    // 청구항목명 체크
    function fn_payItemName() {
        $(".ajax-loader-area").attr("hidden", true);
        if ($("#payItemName").val().length < 2) {
            $('#payNameY').hide();
            $('#payNameN').show();
            $('#nameUseCk').val('N');
            return;
        }
        var url = "/org/claimMgmt/payItemNameCk";
        var param = {
            adjaccyn: $("#grpadjY").val(),
            adjfiRegKey: $('#grpadjNm').val(),
            payItemName: $('#payItemName').val()
        };

        $.ajax({
            type: "post",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                // N : 사용불가
                if (data.payName == 'Y') {
                    $('#payNameY').show();
                    $('#payNameN').hide();
                    $('#nameUseCk').val('Y');
                } else {
                    $('#payNameY').hide();
                    $('#payNameN').show();
                    $('#nameUseCk').val('N');
                }
                if ($('#payItemCd').val() != null || $('#payItemCd').val != "") {
                    if ($('#itmeName').val() == $('#payItemName').val()) {
                        $('#payNameY').show();
                        $('#payNameN').hide();
                        $('#nameUseCk').val('Y');
                    }
                }
            }
        });
    }
</script>

<jsp:include page="/WEB-INF/views/include/popHeader.jsp" />

<div class="modal fade" id="add-charge-classification" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="itemTitleL">청구항목등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-6">
                            <h5 id="itemTitleS">청구항목등록</h5>
                        </div>
                        <div class="col-6 text-right">
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                
                <form id="insForm" name="insForm" method="post">
                <input type="hidden" id="nameUseCk"  name="nameUseCk" value="C">
                <input type="hidden" id="payItemCd"  name="payItemCd">
                <input type="hidden" id="grpadjName" name="grpadjName">
                <input type="hidden" id="adjaccyn"   name="adjaccyn">
                    <table class="table table-form">
                        <tbody class="container-fluid">
                            <tr class="row no-gutters" id="grpadjY" style="display: none;">
                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>입금통장명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <select class="form-control" id="grpadjNm" name="grpadjNm" onchange="fn_MoneyPassbook();">
                                    	<c:forEach var="row" items="${map.accNum}">
                                    		<option value="${row.code}">${row.codeName}</option>
                                    	</c:forEach>
                                    </select>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">통장번호</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control" id="adjfiRegKey" name="adjfiRegKey" value="" disabled>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>청구항목명</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <input type="text" class="form-control col" id="payItemName" name="payItemName" onkeyup="fn_payItemName();" maxlength="25">
                                    <div id="payNameY" style="color: blue; display: none;"><font size="2">&nbsp;사용가능</font></div>
                                    <div id="payNameN" style="color: red; display: none;"><font size="2">&nbsp;사용불가</font></div>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">고지서출력<br/>대체항목명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control" id="ptrItemName" name="ptrItemName" maxlength="25">
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">
                                    <span class="text-danger">*</span>현금영수증<br/>발급여부
                                    <a tabindex="0" class="popover-dismiss ml-2" role="button" data-toggle="popover" data-trigger="focus"
                                       title="현금영수증 발급여부란?" data-content="현금영수증 발급가능여부를 설정하여, 항목별 미발급 대상 금액을 자동으로 제외하는 기능">
                                        <img src="/assets/imgs/common/icon-info.png">
                                    </a>
                                </th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="receiptable-yes" name="receiptable">
                                        <label for="receiptable-yes"><span class="mr-2"></span>가능</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="receiptable-no" name="receiptable">
                                        <label for="receiptable-no"><span class="mr-2"></span>불가능</label>
                                    </div>
                                </td>
                                <c:choose>
                                    <c:when test="${map.cha.enabledPartialPayment}">
                                        <th class="col-md-2 col-sm-4 col-4">
                                            <span class="text-danger">*</span>필수여부
                                        </th>
                                        <td class="col-md-4 col-sm-8 col-8 form-inline">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="optional-no" name="optional" />
                                                <label for="optional-no"><span class="mr-2"></span>필수</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="optional-yes" name="optional" />
                                                <label for="optional-yes"><span class="mr-2"></span>선택</label>
                                            </div>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <th class="col-md-2 col-sm-4 col-4"></th>
                                        <td class="col-md-4 col-sm-8 col-8 form-inline"></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </tbody>
                    </table>
                    </form>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12 text-center">
                            <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal" onclick="hiddenLoader()">취소</button>
                            <button type="button" class="btn btn-primary" onclick="fn_insClaimItem();">저장</button>
                        </div>
                    </div>
                </div>

                <div class="container-fluid" id="adjGroupInfo">
                    <div class="row no-gutters mt-4">
                        <div class="col-6">
                            <h5>입금 통장 정보</h5>
                        </div>
                        <div class="col-6 text-right">
                        </div>
                    </div>
                </div>

                <div class="container-fluid" id="adjGroupInfoList">
                    <div class="table-responsive mb-3">
                        <table class="table table-sm table-hover table-primary">
                            <colgroup>
                                <col width="60">
                                <col width="300">
                                <col width="300">
                                <col width="150">
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
        </div>
    </div>
</div>
