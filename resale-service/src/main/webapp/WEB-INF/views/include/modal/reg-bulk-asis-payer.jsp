<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="modal fade" id="reg-bulk-asis-payer" tabindex="-1" role="dialog" aria-labelledby="reg-bulk-asis-payer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">과거양식 변환하기</h5>
                <button type="button" class="close close-modal" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div id="page-description">
                        <div class="row">
                            <div class="col-12">
                                <p class="mb-1"><strong>변환방법 안내</strong></p>
                                <p>
                                    ① [파일찾기]를 통해 과거양식(대량등록 엑셀파일) 선택하여 업로드<br/>
                                    ② [파일변환] 버튼을 누르면 변환된 파일이 두 가지 종류로 생성<br/>
                                    ③ [파일변환 결과 – 청구 대량등록]의 파일을 다운로드 받은 후, 엑셀파일 대량등록 -> 청구 바로가기에서 업로드 후 청구등록 완료<br/>
                                    ④ [파일변환 결과 – 고객 대량등록] 파일은 기존의 고객정보를 신규고객 대량 등록할 때 사용하시는 양식과 동일하게 생성하여 고객관리에 참고용으로 사용 가능
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters">
                        <div class="col-12">
                            <h5>과거양식 파일변환</h5>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <table class="table table-form">
                                <tbody class="container-fluid">
                                    <tr class="row no-gutters">
                                        <th class="col col-md-2 col-4">과거양식</th>
                                        <td class="col col-md-10 col-8 filebox">
                                            <input class="form-control" value="파일선택" disabled="disabled" id="upload-filename-for-transform" />
                                            <label class="btn btn-sm btn-d-gray ml-1" for="upload-bulk-payer-by-excel">파일찾기</label>
                                            <input type="file" id="upload-bulk-payer-by-excel" class="hidden" />
                                            <input type="cusGbcnt" id="upload-bulk-payer-by-excel" class="hidden" />
                                            <button type="button" class="btn btn-sm btn-primary ml-1" id="transform-button">파일변환</button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col">
                            <span class="font-blue" style="font-size: 18px;">▶ 파일변환 결과</span> – 고객 대량등록 (고객등록 메뉴의 신규고객 등록 양식과 동일)
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <table class="table table-form">
                                <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <td class="col filebox">
                                        <input class="form-control" value="" disabled="disabled" id="customer-filename" />
                                        <button type="button" class="btn btn-sm btn-primary ml-1" id="customer-file-download">다운로드</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col">
                            <span class="font-blue" style="font-size: 18px;">▶ 파일변환 결과</span> – 청구 대량등록 (청구 엑셀파일 대량등록 업로드 가능)
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <table class="table table-form">
                                <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <td class="col filebox">
                                        <input class="form-control" value="" disabled="disabled" id="notice-filename" />
                                        <button type="button" class="btn btn-sm btn-primary ml-1" id="notice-file-download">다운로드</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col text-center">
                            <button type="button" class="btn btn-primary ml-1" onclick="fn_goRegBulkPayer(); overFlowHidden();">청구등록 바로가기</button>
                            <button type="button" class="btn btn-primary btn-outlined close-modal" data-dismiss="modal">닫기</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function fn_goRegBulkPayer() {
        $("#reg-bulk-asis-payer .close-modal").click();
        $('#reg-bulk-by-excel').modal({backdrop:'static', keyboard:false});
    }
    $(function() {
         $('#upload-bulk-payer-by-excel').change(function() {
            $('#upload-filename-for-transform').val(this.files[0].name);
         });
         
         $('#transform-button').click(transform);
         
         $('#customer-file-download').click(function() {
        	 var fileId = $('#customer-file-download').data('fileId');
        	 if(fileId) {
	             window.open('/org/transformer/customer/' + fileId);
        	 } else {
            	 swal({
  		           type: 'error',
  		           text: '변환된 파일이 없습니다. 파일변환을 먼저해 주세요.',
  		           confirmButtonColor: '#3085d6',
  		           confirmButtonText: '확인'
  			     });
  			     return;
             }
         });
         
         $('#notice-file-download').click(function() {
        	 var fileId = $('#notice-file-download').data('fileId');
             if(fileId) {	                          
	             window.open('/org/transformer/notice/' + fileId);
             } else {
            	 swal({
  		           type: 'error',
  		           text: '변환된 파일이 없습니다. 파일변환을 먼저해 주세요.',
  		           confirmButtonColor: '#3085d6',
  		           confirmButtonText: '확인'
  			     });
  			     return;
             }
         });
         
         function transform() {
             var file = $('#upload-bulk-payer-by-excel')[0].files[0];
             if (file == null || file == undefined) {
            	 swal({
		           type: 'error',
		           text: '선택한 파일이 없습니다. 파일을 등록해 주세요.',
		           confirmButtonColor: '#3085d6',
		           confirmButtonText: '확인'
			     });
			     return;
             }
             
             var formData = new FormData();
             formData.append('file', file);
             formData.append('cusGbcnt', ${map.cusGbCnt});             
             $.ajax({
                 url: '/org/transformer',
                 data: formData,
                 processData: false,
                 contentType: false,
                 type: 'POST',
                 success: function(data) {
                     $('#notice-filename').val(data.noticeFileName);
                     $('#customer-filename').val(data.customerFileName);
                     $('#notice-file-download').data('fileId', data.noticeId);
                     $('#customer-file-download').data('fileId', data.customerId);
                 }
             });
         }
    });
</script>
