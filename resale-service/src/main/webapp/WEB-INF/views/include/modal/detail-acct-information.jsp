<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="modal fade" id="detail-acct-information" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">입금내역 상세보기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-12">
                            <h5>고객기본정보</h5>
                        </div>
                    </div>
                </div>

                <form>
                    <div class="container-fluid">
                        <table class="table table-form customer"> 
                            <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">고객명</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span class="table-title-ellipsis data-name"></span></td>
                                    <th class="col-md-2 col-sm-4 col-5">고객번호</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span class="table-title-ellipsis data-register-type"></span></td>
                                </tr>

                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">납부대상</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span class="table-title-ellipsis data-notice-target"></span></td>
                                    <th class="col-md-2 col-sm-4 col-5">가상계좌</th>
                                    <td class="col-md-4 col-sm-8 col-7"><div class="table-title-ellipsis data-vano"></div></td>
                                </tr>

                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">연락처</th>
                                    <td class="col-md-10 col-sm-8 col-7 form-inline" colspan="3">
                                        <div class="contact-number form-inline w-100">
                                            <span class="table-title-ellipsis data-contact-no"></span>
                                        </div>
                                    </td>
                                </tr>

                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">메일주소</th>
                                    <td class="col-md-10 col-sm-8 col-7 form-inline" colspan="3">
                                        <div class="form-inline email-contact w-100">
                                            <span class="table-title-ellipsis data-email"></span>
                                        </div>
                                    </td>
                                </tr>

                                <c:if test="${!empty orgSess.cusGubn1}">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn1}</th>
                                        <td class="col-md-4 col-sm-8 col-7">
                                            <span class="table-title-ellipsis data-category-1"></span>
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn2}</th>
                                        <td class="col-md-4 col-sm-8 col-7">
                                            <span class="table-title-ellipsis data-category-2"></span>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${!empty orgSess.cusGubn3}">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn3}</th>
                                        <td class="col-md-4 col-sm-8 col-7">
                                            <span class="table-title-ellipsis data-category-3">
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn4}</th>
                                        <td class="col-md-4 col-sm-8 col-7">
                                            <span class="table-title-ellipsis data-category-4"></span>
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <div class="container-fluid notice">
                        <div class="row no-gutters mt-4">
                            <div class="col-12">
                                <h5>청구정보</h5>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid notice">
                        <table class="table table-form">
                            <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">청구월</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span class="table-title-ellipsis data-month"></span></td>
                                    <th class="col-md-2 col-sm-4 col-5">청구일자</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span class="table-title-ellipsis data-date"></span></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">납부기한</th>
                                    <td class="col-md-10 col-sm-8 col-7" colspan="3"><span class="table-title-ellipsis date-period"></span></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5 mobile-text-left">고지서용<br/>표시마감일</th>
                                    <td class="col-md-10 col-sm-8 col-7" colspan="3"><span class="table-title-ellipsis data-due-date"></span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="container-fluid">
                        <div class="row">
                            <div class="table-responsive pd-n-mg-o col">
                                <table class="table table-sm table-primary mt-3 notice-details">
                                    <colgroup>
                                        <col width="70">
                                        <col width="360">
                                        <col width="190">
                                        <col width="190">
                                    </colgroup>

                                    <thead>
                                        <tr>
                                            <th>NO</th>
                                            <th>청구항목</th>
                                            <th>청구금액(원)</th>
                                            <th>수납금액(원)</th>
                                        </tr>
                                    </thead>

                                    <tbody class="item-list">
                                    </tbody>

                                    <tfoot>
                                        <tr>
                                            <td colspan="2" class="summary-bg">합계</td>
                                            <td class="text-right summary-bg data-total-notice-amount"></td>
                                            <td class="text-right summary-bg data-total-payment-amount"></td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
