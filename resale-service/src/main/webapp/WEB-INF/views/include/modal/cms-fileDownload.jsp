<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
    // 동의서 다운로드
    function fn_downloadFile() {
        fncClearTime();

        $('#fileName').val("autoWithdrawal.pdf");

        document.fileForm.action = "/common/login/download";
        document.fileForm.submit();
    }
</script>

<form id="fileForm" name="fileForm" method="post" style="display: none;">
    <input type="hidden" id="fileName" name="fileName">
</form>

<div class="modal fade" id="cms-file-agree" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="poptitle">서면 동의</span></h5>
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
                        <li>상기 내용은 가상계좌 수납관리 서비스 이용신청서의 기재사항과 동일합니다.</li>
                        <li>동의서 작성 후 가상계좌 수납관리 서비스 이메일 또는 팩스로 전달 부탁드립니다.</li>
                        - 이메일 : cs@finger.co.kr<br/>
                        - 팩스 : 0303-0959-8201
                        <li>동의 완료에 약 1~2일 정도 소요되며, 처리 완료 전 재로그인 시 자동출금 동의 화면이 지속적으로 노출될 수 있습니다.</li>
                    </ul>
                </div>
            </div>

            <div class="modal-footer mb-3">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary save-payer-information" onclick="fn_downloadFile();">동의서 다운로드</button>
                <button type="button" class="btn btn-primary save-payer-information" onclick="fn_goMain();">메인으로 이동</button>
            </div>
        </div>
    </div>
</div>
