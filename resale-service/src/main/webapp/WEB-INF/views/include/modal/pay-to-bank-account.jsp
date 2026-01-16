<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="popup-pay-to-bank-account" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">계좌이체</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3 mb-2">
                        <div class="col-12">
                            <h5>인터넷뱅킹 바로가기</h5>
                            <div class="guide-mention font-blue">* 결제하시고자 하는 은행을 클릭해 주세요.</div>
                        </div>
                    </div>
                </div>

                <div class="container table-container mb-3">
                    <div id="bank-link" class="list-id">
                        <div class="row">
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.shinhan.com/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-shinhan.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.ibk.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-ibk.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://banking.nonghyup.com/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-nh.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.kbstar.com/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-kb.png" width="100%">
                                </a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.wooribank.com/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-woori.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.kebhana.com/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-hana.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.standardchartered.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-sc.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.citibank.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-citi.png" width="100%">
                                </a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.suhyup-bank.com/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-sh.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.dgb.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-dgb.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.busanbank.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-busan.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://pib.kjbank.com/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-gj.png" width="100%">
                                </a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.jbbank.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-jb.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.e-jejubank.com/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-jj.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.knbank.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-kn.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.kfcc.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-mg.png" width="100%">
                                </a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.epostbank.go.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-post.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://www.kdb.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-kdb.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                                <a href="https://openbank.cu.co.kr/" target="_blank">
                                    <img src="/assets/imgs/payer/bank-logo-sh-2.png" width="100%">
                                </a>
                            </div>
                            <div class="col col-md-3 col-6 mb-2">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container list-button-group-bottom text-center">
                    <div class="row">
                        <div class="col">
                            <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
