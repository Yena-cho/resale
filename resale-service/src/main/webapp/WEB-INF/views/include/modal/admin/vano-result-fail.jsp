<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="inmodal modal fade" id="popup-vano-result-fail" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">가상계좌 정산 실패 등록</h5>
            </div>
            <div class="modal-body">
                <form>
                    <input type="hidden" id="failsettleseq">
                    <div class="container-fluid" id="table_for_Q">
                        <table class="table table-bordered white-bg">
                            <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-2">기관코드</th>
                                    <td class="col-md-4 col-sm-8 col-4"><span id="failchacd"></span></td>
                                    <th class="col-md-2 col-sm-4 col-2">기관명</th>
                                    <td class="col-md-4 col-sm-8 col-4"><span id="failchaname"></span></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-2">기관상태</th>
                                    <td class="col-md-4 col-sm-8 col-4" colspan="3"><span id="failchast"></span></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2">추가사유</th>
                                    <td class="col-md-10" colspan="3"><input type="text" class="form-control" id="failmemo" maxlength="500"></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-lg btn-w-m btn-primary" onclick="fnRegistFail();">등록</button>
            </div>
        </div>
    </div>
</div>
