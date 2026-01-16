<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%--
/*
**
** edit-counselling-dialog.jsap
** Author : 이영주
** Create date : 2018.05.08
** Type : Modal
** Description : 전화상담 진행상황을 수정하기 위한 modal
**
*/
--%>

<div class="modal fade" id="modal-edit-counselling-dialog" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">전화상담 진행상황 수정</h4>
            </div>
            <div class="modal-body">
                <form>
                    <div class="row form-group">
                        <div class="col col-md-6 col-12">
                            <label class="control-label">
                                고객분류
                            </label>
                            <select class="form-control">
                                <option>이용기관</option>
                                <option>이용이관</option>
                                <option>이용이관</option>
                            </select>
                        </div>
                        <div class="col col-md-6 col-12">
                            <label class="control-label">
                                예약사유
                            </label>
                            <select class="form-control">
                                <option>사이트이용</option>
                                <option>납부자/신규</option>
                                <option>결제</option>
                            </select>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col col-md-6 col-12">
                            <label class="control-label">
                                상담직원
                            </label>
                            <input type="text" class="form-control" placeholder="" value="김핑거">
                        </div>
                        <div class="col col-md-6 col-12">
                            <label class="control-label">
                                상담일시
                            </label>
                            <div class="input-group date">
                                <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" value="03/04/2014">
                            </div>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col col-md-6 col-12">
                            <label class="control-label">
                                기관명
                            </label>
                            <input type="text" class="form-control" placeholder="" value="김핑거">
                        </div>
                        <div class="col col-md-6 col-12">
                            <label class="control-label">
                                진행상태
                            </label>
                            <select class="form-control">
                                <option>완료</option>
                                <option>진행중</option>
                                <option>결제</option>
                            </select>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col col-md-6 col-12">
                            <label class="control-label">
                                담당자명(수신자명)
                            </label>
                            <input type="text" class="form-control" placeholder="" value="김핑거">
                        </div>
                        <div class="col col-md-6 col-12">
                            <label class="control-label">
                                담당자 연락처
                            </label>
                            <input type="text" class="form-control" placeholder="" value="김핑거">
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col col-xs-12">
                            <label class="control-label">
                                상담 답변내용
                            </label>
                            <textarea class="form-control" rows="8"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary">수정</button>
            </div>
        </div>
    </div>
</div>
