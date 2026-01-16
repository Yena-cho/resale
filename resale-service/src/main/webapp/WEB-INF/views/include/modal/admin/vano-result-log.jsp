<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="inmodal modal fade" id="popup-vano-result-log" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">가상계좌 정산 이력</h5>
            </div>
            <div class="modal-body">
                <form>
                    <input type="hidden" id="logsettleseq">
                    <div class="container-fluid" id="table_for_Q">
                        <table class="table table-bordered white-bg">
                            <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-2">기관코드</th>
                                    <td class="col-md-4 col-sm-8 col-4"><span id="logchacd"></span></td>
                                    <th class="col-md-2 col-sm-4 col-2">기관명</th>
                                    <td class="col-md-4 col-sm-8 col-4"><span id="logchaname"></span></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-2">기관상태</th>
                                    <td class="col-md-4 col-sm-8 col-4" colspan="3"><span id="logchast"></span></td>
                                </tr>
                            </tbody>
                        </table>

                        <table class="table table-bordered white-bg">
                            <thead>
                            <tr>
                                <th>입금일자</th>
                                <th>은행코드</th>
                                <th>기관코드</th>
                                <th>기관명</th>
                                <th>입금계좌번호</th>
                                <th>정산금액(원)</th>
                                <th>처리상태</th>
                                <th>처리일시</th>
                                <th>비고</th>
                            </tr>
                            </thead>
                            <tbody class="container-fluid" id="resultLog">
                            <tr>
                                <td colspan=10 style="text-align: center;">[조회된 내역이 없습니다.]</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
