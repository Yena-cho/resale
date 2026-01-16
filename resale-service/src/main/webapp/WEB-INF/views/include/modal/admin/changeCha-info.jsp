<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>

<div class="inmodal modal fade" id="changeCha-info" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">상세보기</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h3>기관 기본 정보</h3>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <table class="table table-bordered white-bg">
                                <colgroup>
                                    <col width="147">
                                    <col width="180">
                                    <col width="180">
                                </colgroup>

                                <tbody>
                                    <tr>
                                        <th>항목</th>
                                        <th>변경 전</th>
                                        <th>변경 후</th>
                                    </tr>

                                    <tr>
                                        <td>기관코드</td>
                                        <td><span id="asisClientId"></span></td>
                                        <td><span id="clientId"></span></td>
                                    </tr>

                                    <tr>
                                        <td>사업자번호</td>
                                        <td><span id="asisClientIdNo"></span></td>
                                        <td><span id="clientIdNo"></span></td>
                                    </tr>

                                    <tr>
                                        <td>기관명</td>
                                        <td><span id="asisClientName"></span></td>
                                        <td><span id="clientName"></span></td>
                                    </tr>

                                    <tr>
                                        <td>기관상태</td>
                                        <td><span id="asisStatusName"></span></td>
                                        <td><span id="clientStatusName"></span></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h3>이용 요금 정보</h3>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <table class="table table-bordered white-bg">
                                <colgroup>
                                    <col width="147">
                                    <col width="180">
                                    <col width="180">
                                </colgroup>

                                <tbody>
                                    <tr>
                                        <th>항목</th>
                                        <th>변경 전</th>
                                        <th>변경 후</th>
                                    </tr>

                                    <tr>
                                        <td>배분율(%)</td>
                                        <td><span id="asisFingerFeeRate"></span></td>
                                        <td><span id="fingerFeeRate"></span></td>
                                    </tr>

                                    <tr>
                                        <td>입금수수료(원)</td>
                                        <td><span id="asisPaymentFeeAmt"></span></td>
                                        <td><span id="paymentFeeAmt"></span></td>
                                    </tr>

                                    <tr>
                                        <td>핑거 입금수수료(원)</td>
                                        <td><span id="asisPaymentFinerFeeAmt"></span></td>
                                        <td><span id="paymentFinerFeeAmt"></span></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div id="accountUseYn">
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <h3>다계좌 정보</h3>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered white-bg">
                                    <colgroup>
                                        <col width="107">
                                        <col width="80">
                                        <col width="120">
                                        <col width="80">
                                        <col width="120">
                                    </colgroup>

                                    <tbody>
                                        <tr>
                                            <th>코드</th>
                                            <th>변경 전 사용 여부</th>
                                            <th>변경 전 계좌명</th>
                                            <th>변경 후 사용 여부</th>
                                            <th>변경 후 계좌명</th>
                                        </tr>

                                        <tr>
                                            <td>00000</td>
                                            <td><span id="asisCode1"></span></td>
                                            <td><span id="asisName1"></span></td>
                                            <td><span id="tobeCode1"></span></td>
                                            <td><span id="tobeName1"></span></td>
                                        </tr>

                                        <tr>
                                            <td>00001</td>
                                            <td><span id="asisCode2"></span></td>
                                            <td><span id="asisName2"></span></td>
                                            <td><span id="tobeCode2"></span></td>
                                            <td><span id="tobeName2"></span></td>
                                        </tr>

                                        <tr>
                                            <td>00002</td>
                                            <td><span id="asisCode3"></span></td>
                                            <td><span id="asisName3"></span></td>
                                            <td><span id="tobeCode3"></span></td>
                                            <td><span id="tobeName3"></span></td>
                                        </tr>

                                        <tr>
                                            <td>00003</td>
                                            <td><span id="asisCode4"></span></td>
                                            <td><span id="asisName4"></span></td>
                                            <td><span id="tobeCode4"></span></td>
                                            <td><span id="tobeName4"></span></td>
                                        </tr>

                                        <tr>
                                            <td>00004</td>
                                            <td><span id="asisCode5"></span></td>
                                            <td><span id="asisName5"></span></td>
                                            <td><span id="tobeCode5"></span></td>
                                            <td><span id="tobeName5"></span></td>
                                        </tr>

                                        <tr>
                                            <td>00005</td>
                                            <td><span id="asisCode6"></span></td>
                                            <td><span id="asisName6"></span></td>
                                            <td><span id="tobeCode6"></span></td>
                                            <td><span id="tobeName6"></span></td>
                                        </tr>

                                        <tr>
                                            <td>00006</td>
                                            <td><span id="asisCode7"></span></td>
                                            <td><span id="asisName7"></span></td>
                                            <td><span id="tobeCode7"></span></td>
                                            <td><span id="tobeName7"></span></td>
                                        </tr>

                                        <tr>
                                            <td>00007</td>
                                            <td><span id="asisCode8"></span></td>
                                            <td><span id="asisName8"></span></td>
                                            <td><span id="tobeCode8"></span></td>
                                            <td><span id="tobeName8"></span></td>
                                        </tr>

                                        <tr>
                                            <td>00008</td>
                                            <td><span id="asisCode9"></span></td>
                                            <td><span id="asisName9"></span></td>
                                            <td><span id="tobeCode9"></span></td>
                                            <td><span id="tobeName9"></span></td>
                                        </tr>

                                        <tr>
                                            <td>00009</td>
                                            <td><span id="asisCode10"></span></td>
                                            <td><span id="asisName10"></span></td>
                                            <td><span id="tobeCode10"></span></td>
                                            <td><span id="tobeName10"></span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h3>지점정보</h3>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <table class="table table-bordered white-bg">
                                <colgroup>
                                    <col width="147">
                                    <col width="180">
                                    <col width="180">
                                </colgroup>

                                <tbody>
                                    <tr>
                                        <th>항목</th>
                                        <th>변경 전</th>
                                        <th>변경 후</th>
                                    </tr>

                                    <tr>
                                        <td>지점 코드</td>
                                        <td><span id="asisBrchCode"></span></td>
                                        <td><span id="brchCode"></span></td>
                                    </tr>

                                    <tr>
                                        <td>지점 이름</td>
                                        <td><span id="asisAgtName"></span></td>
                                        <td><span id="agtName"></span></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>