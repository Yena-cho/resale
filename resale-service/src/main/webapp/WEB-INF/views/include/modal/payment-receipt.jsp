<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="payment-receipt" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">납부확인증</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid receipt-title">
                    <div class="row no-gutters mt-3">
                        <div class="col-12 text-center">
                            <h4>가상계좌 수납관리 서비스 납부확인증</h4>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-6">
                            <h5>납부자 정보</h5>
                        </div>
                        <div class="col-6 text-right">
                            <button type="button" class="btn btn-img">
                                <img src="/assets/imgs/common/btn-print.png">
                            </button>
                        </div>
                    </div>
                </div>

                <form>
                <div class="container-fluid">
                    <table class="table table-form">
                        <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">납부자명</th>
                                <td class="col-md-10 col-sm-8 col-8">
                                    ${map.cusName}
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">이용기관명</th>
                                <td class="col-md-10 col-sm-8 col-8 form-inline">
                                	${map.chaName }
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">가상계좌</th>
                                <td class="col-md-10 col-sm-8 col-8 form-inline">
                                	${map.vaNo}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-6">
                            <h5>납입현황</h5>
                        </div>
                        <div class="col-6 text-right">
                            현재일자 <strong id="toDay">[]</strong>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="table-responsive mb-3">
                        <table class="table table-sm table-hover table-primary">
                            <thead>
                                <tr>
                                    <th>고지년월</th>
                                    <th>청구금액</th>
                                    <th>납부금액</th>
                                    <th>납부일자</th>
                                    <th>납부방법</th>
                                    <th>비고</th>
                                </tr>
                            </thead>
                            <tbody id="paymentGrid">
                                <tr>
                                    <td></td>
                                    <td class="text-right"></td>
                                    <td class="text-right"></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="2">납부액 합계</td>
                                    <td colspan="4" class="text-right" id="chkTotAmt"></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
                </form>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <div class="info-box">
                                <h6>
                                    <img src="/assets/imgs/common/icon-exclamation-mark.png" class="mr-1">
                                    안내문
                                </h6>
                                <ul>
                                    <li>본 확인증은 납부여부의 확인용으로 법적 효력이 없습니다.</li>
                                    <li>납부 내역의 현금영수증 및 납입증명서를 원하신다면 이용기관에 직접 문의하여 발급 받으시길 바랍니다.</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12 text-center">
                            <button type="button" class="btn btn-primary" onclick="payprint();">인쇄</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
$(document).ready(function(){
	var toDay = getCurrentDate();
	toDay = toDay.substring(0,4) + "년 " + toDay.substring(4,6) + "월 " + toDay.substring(6,8) +"일";
	$("#toDay").html(toDay);
});
</script>
