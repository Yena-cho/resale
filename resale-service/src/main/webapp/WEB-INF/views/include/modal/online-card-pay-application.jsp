<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">

$(document).ready(function(){
	
$('#btn-upload').click(function(){
	$('#inputFile').click();
});	
	
});

function fnGrid(){
	var str = '';
			str += '<tr>';
			str += '</tr>';
/*             <td>
            <input class="form-check-input table-check-child" type="checkbox" id="row-90" value="option2">
            <label for="row-90"><span></span></label>
        </td>
        <td>온라인 카드결제 서비스 신청서.jpg</td>
        <td>2MB</td> */
	$('#' + obj).html(str);
}


</script>>

</sciprt>

<div class="modal fade" id="online-card-pay-application" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">온라인 카드결제 서비스 신청 등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid mt-3 mb-3">
                    <div class="row">
                        <div class="col">
                            <button type="button" class="btn btn-primary btn-outlined">삭제</button>
                            <input type="file" id="inputFile" name="inputFile" class="hidden" style="display:none;" onchange="fnGrid();">
                            <button type="button" class="btn btn-primary" id="btn-upload" onfocus="this.blur();">서류등록</button>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="table-responsive mb-3">
                        <table class="table table-sm table-hover table-primary">
                            <thead>
                                <tr>
                                    <th>
                                        <input class="form-check-input table-check-parents" type="checkbox" id="row-th" value="option2">
                                        <label class="label-table-check-parents" for="row-th"><span></span></label>
                                    </th>
                                    <th>등록서류</th>
                                    <th>파일크기</th>
                                </tr>
                            </thead>
                            <tbody id="fileBody">
                                <tr>
                                    <td>
                                        <input class="form-check-input table-check-child" type="checkbox" id="row-90" value="option2">
                                        <label for="row-90"><span></span></label>
                                    </td>
                                    <td>온라인 카드결제 서비스 신청서.jpg</td>
                                    <td>2MB</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="row no-gutters mt-4 mb-5">
                        <div class="col-12 text-center">
                            <button type="button" class="btn btn-primary btn-outlined">취소</button>
                            <button type="button" class="btn btn-primary">서류제출</button>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <div class="info-box">
                                <h6>
                                    <img src="/assets/imgs/common/icon-exclamation-mark.png" class="mr-1">
                                    안내문
                                </h6>
                                <ul>
                                    <li>파일용량 5MB 이내</li>
                                    <li>파일형식 : pdf, jpg, gif</li>
                                    <li>이용을 원하시는 부가서비스의 필수제출 서류를 확인하시고 등록 해주시기 바랍니다.</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
