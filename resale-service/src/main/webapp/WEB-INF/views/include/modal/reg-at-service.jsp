<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    function fn_atModalOpen() {
        var url = "/org/notiMgmt/selAtChaInfo";
        var param = {};

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                $('#atUseChaName').text(data.map.chaName);
            }
        });

        $('#reg-at-service').modal({backdrop: 'static', keyboard: false});
    }

    function fn_AtServiceInsert() {
        swal({
            type: 'question',
            html: "알림톡 서비스를 신청 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '신청',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var url = "/org/notiMgmt/atCertificateIns";
                var param = {
                    atChaName: $('#atUseChaName').text()
                };

                $.ajax({
                    type: "post",
                    async: true,
                    url: url,
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (data) {
                        swal({
                            type: 'success',
                            text: "알림톡 서비스 이용등록 신청이 완료되었습니다.",
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function(result) {
                            location.reload();
                        });
                    },
                    error: function (data) {
                        swal({
                            type: 'error',
                            text: "알림톡 서비스 이용등록 신청에 실패하였습니다.",
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });

                        return;
                    }
                });
            }
        });
    }
</script>

<div class="modal fade" id="reg-at-service" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">카카오톡 알림톡 고지 서비스 신청</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <div class="container-fluid mb-4">
                    <div class="row">
                        <div class="col">
                            <form id="atForm" enctype="multipart/form-data" method="post">
                                <table class="table table-form table-primary">
                                    <tbody class="container-fluid">
                                    <tr class="row col">
                                        <th class="col col-3">이용기관명</th>
                                        <td class="col col-9"><span id="atUseChaName"></span></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                    </div>
                </div>

                <div id="page-description" class="container-fluid mb-1">
                    <div class="row">
                        <div class="col-12">
                            <p>* 카카오톡 알림톡은 정보성 메시지에 한해 발송이 가능합니다.</p>
                            <p>- 정보성 메시지란? 정보통신망법 안내서에 '영리목적 광고성 정보의 예외'에 해당하는 메시지</p>
                            <p>- 발신자의 필요에 따라 간헐적으로 발송하는 공지사항 등은 발송할 수 없습니다.</p>
                            <p>* 알림톡 정책에 따라 청구/미납/입금시 알림톡 발송만 가능하며, 사전 등록된 템플릿 기반으로 전 고객 동일한 문구로 발송됩니다.</p>
                            <p>* 발송기관명은 사업자등록증에 등록된 상호명으로 발송되며, 변경을 원하는 경우에는 고객센터로 별도 신청바랍니다.</p>
                            <p>* 본 서비스는 유료 부가서비스이며, 1건당 20원의 수수료가 부과됩니다. 관리수수료에 포함되어 청구되오니 이 점 유의하시기 바랍니다.</p>
                        </div>
                    </div>
                </div>

                <div class="mb-4">* 위의 사항을 충분히 인지하였으며, 이에 동의하고 알림톡 서비스를 신청합니다.</div>

                <div class="container-fluid mb-4">
                    <div class="row">
                        <div class="col-12 text-center">
                            <button class="btn btn-primary btn-outlined btn-wide" data-dismiss="modal">닫기</button>
                            <button class="btn btn-primary btn-wide" onclick="fn_AtServiceInsert();">신청</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
