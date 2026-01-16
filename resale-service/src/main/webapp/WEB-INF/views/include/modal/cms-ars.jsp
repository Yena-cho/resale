<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
    $(document).ready(function() {
        $('.popover-dismiss').popover({
            trigger: 'focus'
        });
    });
    /**
     * ARS 동의
     */
    function fn_telArs() {
        $('.btn-agree').prop('disabled', true);

        var url = "/common/login/telArs";
        var param = {};

        var tel = $(".chrHp").html();

        swal({
            type: 'info',
            html: "[" + tel +  "] 번호로 ARS 동의가 진행됩니다.",
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인'
        }).then(function(result) {
            $.ajax({
                type: "post",
                async: true,
                url: url,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (result) {
                    if (result.code == '0000') {
                        // 성공 시
                        // ARS 동의 확인

                        swal({
                            type: 'info',
                            html: "ARS 동의가 완료되었습니다.",
                            confirmButtonColor: '#3085d6',
                            cancelButtonColor: '#d33',
                            confirmButtonText: '확인'
                        }).then(function(result) {
                            if (result.value) {
                                $('.btn-agree').prop('disabled', false);

                                $("#btn-ars").html('');
                                $("#btn-ars").html('<button type="button" class="btn btn-primary save-payer-information btn-agree" onclick="fn_okArs();">ARS 동의 완료</button>');
                            }
                        });
                    } else if (result.code != "0000") {
                        // 실패 시
                        // ARS 다시 시도
                        $('.btn-agree').prop('disabled', false);

                        swal({
                            type: 'error',
                            html: "ARS 동의에 실패하였습니다.",
                            confirmButtonColor: '#3085d6',
                            cancelButtonColor: '#d33',
                            confirmButtonText: '확인'
                        }).then(function(result) {
                            if (result.value) {
                                $("#btn-ars").html('');
                                $("#btn-ars").html('<button type="button" class="btn btn-primary save-payer-information btn-agree" onclick="fn_reArs();">ARS 동의 재요청</button>');
                            }
                        });
                    }
                }
            });
        });
    }

    function fn_okArs() {
        window.location.href = '/org/main/index';
    }

    function fn_reArs() {
        fn_telArs();
    }
</script>

<div class="modal fade" id="cms-ars-agree" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="poptitle">ARS 동의</span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>

            <div class="modal-body">
                <div class="row">
                    <div class="col col-12 mb-1">
                        <h5><strong>이용기관 정보</strong></h5>
                    </div>

                    <div class="pd-n-mg-o mb-3">
                        <table class="table table-primary table-bordered">
                            <colgroup>
                                <col width="250">
                                <col width="150">
                                <col width="170">
                                <col width="270">
                            </colgroup>

                            <tbody>
                            <tr>
                                <th>기관(업체)명</th>
                                <th>사업자등록번호</th>
                                <th>담당자 핸드폰번호</th>
                                <th>출금계좌</th>
                            </tr>
                            <tr>
                                <td><span class="chaName"></span></td>
                                <td><span class="chaOffNo"></span></td>
                                <td><span class="chrHp"></span></td>
                                <td><span class="feeBankCd"></span> <span class="feeVano"></span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="row">
                    <ul>
                        <li>상기 내용은 가상계좌 수납관리 서비스 이용신청서의 기재사항과 동일하며, ARS는 담당자 핸드폰번호로 진행됩니다.</li>
                        <li>담당자 핸드폰번호 변경을 원하실 경우 [마이페이지>기본정보관리]에서 담당자 정보 변경 후 재로그인하여 ARS 동의를 진행해 주시기 바랍니다.</li>
                        <li>담당자 핸드폰번호 외 이용기관 정보 수정은 고객센터로 문의주시기 바랍니다.</li>
                        <li>동의 완료로 통화를 마친 이후 ARS 동의 완료 버튼을 눌러주시기 바랍니다.</li>
                    </ul>
                </div>
            </div>

            <div class="modal-footer mb-3">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
                <span id="btn-ars"></span>
                <button type="button" class="btn btn-primary save-payer-information" onclick="fn_goMain();">메인으로 이동</button>
            </div>
        </div>
    </div>
</div>

<div class="ajax-loader-area" style="display: none;">
    <div class="ajax-loader"></div>
    <div class="modal-backdrop fade show"></div>
</div>
