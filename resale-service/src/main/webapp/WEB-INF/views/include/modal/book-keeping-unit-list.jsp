<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
$(function() {
	
});
</script>

<div class="modal fade" id="book-keeping-unit-list" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">수납내역 상세보기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3 mb-2">
                        <div class="col-12">
                            <h5 id="rcpTitle"></h5>
                        </div>
                    </div>
                </div>

                <form>
					<div class="container table-container">
						<div id="" class="list-id">
							<div class="row">
								<div class="col">
									<div class="table-responsive mb-3">
										<table class="table table-sm table-hover table-primary">
											<thead id="pResultHead">
												<tr>
													<th>고객명</th>
													<c:if test="${orgSess.cusGubn1 != null && orgSess.cusGubn1 != '' }"><th>${orgSess.cusGubn1}</th></c:if>								
													<c:if test="${orgSess.cusGubn2 != null && orgSess.cusGubn2 != '' }"><th>${orgSess.cusGubn2}</th></c:if>								
													<c:if test="${orgSess.cusGubn3 != null && orgSess.cusGubn3 != '' }"><th>${orgSess.cusGubn3}</th></c:if>								
													<c:if test="${orgSess.cusGubn4 != null && orgSess.cusGubn4 != '' }"><th>${orgSess.cusGubn4}</th></c:if>
													<th>청구금액(원)</th>
													<th>수납상태</th>
													<th>수납액(원)</th>
													<th>미납액(원)</th>
												</tr>
											</thead>
											<tbody id="pResultBody">
												<tr>
													<td></td>
													<c:if test="${orgSess.cusGubn1 != null && orgSess.cusGubn1 != '' }"><td>${orgSess.cusGubn1}</td></c:if>								
													<c:if test="${orgSess.cusGubn2 != null && orgSess.cusGubn2 != '' }"><td>${orgSess.cusGubn2}</td></c:if>								
													<c:if test="${orgSess.cusGubn3 != null && orgSess.cusGubn3 != '' }"><td>${orgSess.cusGubn3}</td></c:if>								
													<c:if test="${orgSess.cusGubn4 != null && orgSess.cusGubn4 != '' }"><td>${orgSess.cusGubn4}</td></c:if>
													<td class="text-right"></td>
													<td></td>
													<td class="text-right"></td>
													<td class="text-right"></td>
												</tr>
											</tbody>
										</table>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
									</div>
								</div>
							</div>
						</div>
					</div>
                </form>
            </div>
        </div>
    </div>
</div>