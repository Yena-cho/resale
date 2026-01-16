<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="inmodal modal fade" id="popup-partner-info" tabindex="-1" role="dialog" aria-labelledby="regPayer"
        aria-hidden="true">
    <div class="modal-dialog modal-big" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">기관그룹 상세</h5>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="ibox float-e-margins">
                                <div class="ibox-content">
                                    <form method="get">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="form-group form-group-sm">
                                                    <label class="form-label block">그룹ID</label>
                                                    <input type="text" class="form-control ng-untouched ng-pristine ng-valid group-id" style="height: 34px;" disabled="disabled"/>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label block">그룹명</label>
                                                <div class="form-group form-group-sm">
                                                    <div class="input-group col-md-12">
                                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid group-name">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label class="form-label block">비밀번호</label>
                                                <div class="form-group form-group-sm">
                                                    <div class="input-group col-md-12">
                                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid group-password">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="form-group form-group-sm">
                                                    <label class="form-label block">종류</label>
                                                    <div class="input-group col-md-12">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="group-transaction-type-01" name="group-transaction-type" value="01" class="group-transaction-type"/>
                                                            <label for="group-transaction-type-01"> WEB </label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="group-transaction-type-03" name="group-transaction-type" value="03" class="group-transaction-type"/>
                                                            <label for="group-transaction-type-03"> API </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="form-group form-group-sm">
                                                    <label class="form-label block">상태</label>
                                                    <div class="input-group col-md-12">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" name="group-status" id="group-status-st01" value="ST01" class="group-status"/>
                                                            <label for="group-status-st01"> 정상 </label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" name="group-status" id="group-status-st03" value="ST02" class="group-status"/>
                                                            <label for="group-status-st03"> 해지 </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-lg-12">
                                                <div class="form-group form-group-sm">
                                                    <label class="form-label block">메모</label>
                                                    <textarea name="group-remark" class="form-control group-remark" rows="4" style="height: 150px;"></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <form>
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="ibox-title">
                                    <div class="col-lg-4"><h3>기관 목록</h3></div>
                                </div>

                                <div class="ibox-content">
                                    <div class="table-responsive" style="max-height: 200px; overflow-y: auto;">
                                        <table class="table table-stripped table-align-center">
                                            <colgroup>
                                                <col width="100">
                                                <col width="150">
                                                <col width="300">
                                                <col width="150">
                                                <col width="150">
                                                <col width="150">
                                                <col width="150">
                                                <col width="150">
                                            </colgroup>

                                            <thead>
                                            <tr>
                                                <th>NO</th>
                                                <th>기관코드</th>
                                                <th>기관이름</th>
                                                <th>대표자</th>
                                                <th>사업자등록번호</th>
                                                <th>대표번호</th>
                                                <th>등록일</th>
                                                <th>&nbsp;</th>
                                            </tr>
                                            </thead>

                                            <tbody class="member-list">
                                            </tbody>
                                        </table>
                                    </div>

                                    <div class="col-lg-12 text-center" style="margin-top: 10px;">
                                        <div class="pull-right">
                                            <div class="input-group" style="width: 300px;">
                                                <input type="text" class="form-control cha-search-box" />
                                                <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-primary">추가</button>
                                                </span>
                                            </div>
                                        </div>
                                    </div>

                                    <div style="clear: both;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary save-cha-info">
                    <i class="fa fa-fw fa-floppy-o"></i>저장
                </button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

</script>
