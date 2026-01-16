<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">
    $(function () {
        $('#certBtn').click(function () {
            var rowCnt = $('input[name=sorting-order-type]:checked').length;
            if (rowCnt <= 0) {
                swal({
                    text: "증명서 종류를 선택해 주세요.",
                    type: 'info',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            } else {
                if ($('#sBrowserType').val() == 'etc') {
                    $('#pdfMakeForm').attr("target", "formInfo");
                    window.open('', 'formInfo', 'height=800, width:1030, menubar=no');
                }
                $("#popup-adm-choose-certificate").modal("hide");
                $('#billSect').val($('input[name=sorting-order-type]:checked').val());
                $('#pdfMakeForm').submit();
            }
        });
    });
</script>

<div class="modal fade" id="popup-adm-choose-certificate" tabindex="-1" role="dialog" aria-labelledby="regPayer"
     aria-hidden="true">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">증명서 인쇄</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3 mb-2">
                        <div class="col-12">
                            <h5>인쇄하실 증명서 종류를 선택해주세요.</h5>
                        </div>
                    </div>
                </div>

                <form>
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-12 mt-3 mb-2">
                                <div class="form-group form-group-sm">
                                    <input type="radio" id="tableCheckbox1" name="sorting-order-type" value="cert1">
                                    <label for="tableCheckbox1"><span class="mr-2"></span>교육비 납입증명서</label>
                                </div>
                            </div>
                            <div class="col-12 mb-2">
                                <div class="form-group form-group-sm">
                                    <input type="radio" id="tableCheckbox2" name="sorting-order-type" value="cert2">
                                    <label for="tableCheckbox2"><span class="mr-2"></span>장기요양급여 납부확인서</label>
                                </div>
                            </div>
                            <div class="col-12 mb-5">
                                <div class="form-group form-group-sm">
                                    <input type="radio" id="tableCheckbox3" name="sorting-order-type" value="cert3">
                                    <label for="tableCheckbox3"><span class="mr-2"></span>기부금 영수증</label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%--<div class="container list-button-group-bottom text-center">--%>
                        <%--<div class="row">--%>
                            <%--<div class="col">--%>
                                <%--<button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>--%>
                                <%--<button type="button" class="btn btn-primary" id="certBtn">확인</button>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                </form>
            </div>
            <div class="modal-footer">
                    <div>
                        <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                        <button type="button" class="btn btn-primary" id="certBtn">확인</button>
                    </div>
            </div>
        </div>
    </div>
</div>
