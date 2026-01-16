<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">


</script>

<div class="modal fade" id="download-receipt-history" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
                <h5 class="modal-title"><span id="poptitle">발행이력다운로드</span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            
            <div class="modal-body">
            	 <div class="container">
			        <div id="page-description">
			            <div class="row">
			                <div class="col-12">
			                    <p>가상계좌 수납관리 서비스를 통해 발급 된 현금영수증 내역을 국세청에서 제공하는 양식과 동일하게 다운로드 하실 수 있습니다.</p>
			                </div>
			            </div>
			        </div>
			    </div>
			    <div class="container">
			    	<div class="search-box" style="margin-bottom: 0;">
		    			<div class="row">
							<div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
								<div class="form-check form-check-inline">
									<label class="form-check-label" for="chargeCalChk"><span class="mr-1"></span>승인일자</label>
								</div>
							</div>
							<div class="col col-md-8 col-sm-12 col-12 form-inline year-month-dropdown mb-2">
                                <div class="date-input">
		                            <div class="input-group">
		                                <input type="text" id="ntsStartDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
		                            </div>
		                        </div>
		                        <span class="range-mark"> ~ </span>
		                        <div class="date-input">
		                            <div class="input-group">
		                                <input type="text" id="ntsEndDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
		                            </div>
		                        </div>
							</div>
						</div>
			    		<div class="row form-inline mt-3">
		                    <div class="col-12 text-center">
		                        <button type="button" class="btn btn-primary btn-wide" onclick="fn_downHistory();">다운로드</button>
		                    </div>
		                </div>
			    	</div>
			    </div>
			    <div class="container">
			        <div id="page-description">
			            <div class="row">
			                <div class="col-12">
			                    <h6>■ 주의</h6>
			                    <ul>
			                        <li>다운받은 자료는 국세청 승인기준이므로 발행중 또는 미발행 건은 포함되어 있지 않습니다.</li>
			                        <li>재발행 건의 경우 취소/승인 두건의 내역이 생성됩니다.</li>
			                        <li>발행/재발행 등의 내역은 발행요청일 기준 D+2일 오전 10시 이후 다운로드 하셔야 포함된 내역을 다운로드 할 수 있습니다.<br/>
     									(예 - 2019년5월1일 발급 시 5월 3일 자료다운로드 가능)</li>
			                        <li>가상계좌 수납관리 서비스를 통해 발행된 내역만 다운로드 됩니다.</li>
			                    </ul>
			                </div>
			            </div>
			        </div>
			    </div>
            </div>
 		</div>
	</div>
</div>

<script type="text/javascript">
$(document).ready(function () {
	$('#ntsStartDate').val(getPrevDate(toDay, 1));
	$('#ntsEndDate').val(toDay);
});



</script>
