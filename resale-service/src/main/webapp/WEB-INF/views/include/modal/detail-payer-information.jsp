<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
    var MODE = ""; //등록 작업시 "I" , 수정작업시  "M"
    var SAVE_MSG = "";
</script>

<div class="modal fade" id="detail-payer-information" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="poptitle">고객정보상세</span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-6">
                            <h5>고객기본정보</h5>
                        </div>
                        <div class="col-6 text-right">
                        </div>
                    </div>
                </div>

                <form>
                    <div class="container-fluid">
                        <table class="table table-form">
                            <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">고객명</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span id="iCusName" class="table-title-ellipsis"></span></td>
                                    <th class="col-md-2 col-sm-4 col-5">고객번호</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span id="iCusKey" class="table-title-ellipsis"></span></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">납부대상</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span id="iRcpGubn" class="table-title-ellipsis"></span></td>
                                    <th class="col-md-2 col-sm-4 col-5">가상계좌</th>
                                    <td class="col-md-4 col-sm-8 col-7"><div id="iVano" class="table-title-ellipsis"></div></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">연락처</th>
                                    <td class="col-md-10 col-sm-8 col-7 form-inline" colspan="3">
                                        <div class="contact-number form-inline w-100">
                                            <span id="iCcusHp" class="table-title-ellipsis"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">메일주소</th>
                                    <td class="col-md-10 col-sm-8 col-7 form-inline" colspan="3">
                                        <div class="form-inline email-contact w-100">
                                            <span id="iCusMail" class="table-title-ellipsis"></span>
                                        </div>
                                    </td>
                                </tr>
                                <c:if test="${!empty orgSess.cusGubn1}">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn1}</th>
                                        <td class="col-md-4 col-sm-8 col-7"><span id="iCusGubn1" class="table-title-ellipsis"></span></td>
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn2}</th>
                                        <td class="col-md-4 col-sm-8 col-7"><span id="iCusGubn2" class="table-title-ellipsis"></span></td>
                                    </tr>
                                </c:if>
                                <c:if test="${!empty orgSess.cusGubn3}">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn3}</th>
                                        <td class="col-md-4 col-sm-8 col-7"><span id="iCusGubn3" class="table-title-ellipsis"></span></td>
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn4}</th>
                                        <td class="col-md-4 col-sm-8 col-7"><span id="iCusGubn4" class="table-title-ellipsis"></span></td>
                                    </tr>
                                </c:if>
                                <tr class="row no-gutters hidden-on-mobile">
                                    <th class="col-md-2 col-sm-4 col-5">고객상태</th>
                                    <td class="col-md-10 col-sm-8 col-7 form-inline" colspan="3">
                                        <div class="form-inline" id="divcuststat">
                                            <span id="iDisabled" class="table-title-ellipsis"></span>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="container-fluid">
                        <div class="row no-gutters mt-4">
                            <div class="col-12">
                                <h5>현금영수증 정보(선택)</h5>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <table class="table table-form">
                            <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">발급용도</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span id="iSelCusType" class="table-title-ellipsis"></span></td>
                                    <th class="col-md-2 col-sm-4 col-5">발급방법</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span id="iSelCusMtd" class="table-title-ellipsis"></span></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">발급번호</th>
                                    <td class="col-md-10 col-sm-8 col-7" colspan="3"><span id="iCusOffNo" class="table-title-ellipsis"></span></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5 mobile-text-left">현금영수증<br/>자동발급 여부</th>
                                    <td class="col-md-10 col-sm-8 col-7" colspan="3"><span id="iRcpreqty" class="table-title-ellipsis"></span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="container-fluid">
                        <div class="row no-gutters mt-4">
                            <div class="col-12">
                                <h5>메모</h5>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <table class="table table-form">
                            <tbody>
                                <tr class="row no-gutters">
                                    <td class="col-12 table-title-ellipsis">
                                        <span id="iMemo"></span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>

                        <div class="guide-mention" style="color:#71adfd;"></div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $("label[for=load-all-payer]").click(function () {
            $("select.condition").attr("disabled", true);
            $("input.condition").attr("disabled", true).val("");
        });

        $("label[for=load-unregisted-payer]").click(function () {
            $(".condition").attr("disabled", false);
        });
    });

    function fnDetail(vano) {
        fn_reset_scroll();
        $("#iCusKey").text("");
        $("#iCusName").text("");
        $("#iDisabled").html("");
        $('#iRcpGubn').text('');
        $("#iCcusHp").text("");
        $("#iVano").text("");
        $("#iCusMail").text('');
        $("#iCusGubn1").text("");
        $("#iCusGubn2").text("");
        $("#iCusGubn3").text("");
        $("#iCusGubn4").text("");
        $('#iRcpreqty').text('');
        $("#iSelCusType").text('');
        $("#iSelCusMtd").text('');
        $("#iCusOffNo").text('');
        $("#iMemo").text('');

        // 고객상세
        var url = "/org/custMgmt/selectDetailCustReg";
        var param = {
            vano: vano
        };
        $.ajax({
            type: "post",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var map = data.info;

                $("#iCusKey").text(map.cusKey);
                $("#iCusName").text(basicUnEscape(map.cusName));
                if (map.disabled == 'N') {
                    $("#iDisabled").html("정상");
                } else if (map.disabled == 'Y') {
                    $("#iDisabled").html("삭제");
                } else {
                    $("#iDisabled").html("임시");
                }
                if (map.rcpGubn == 'Y') { //납부대상
                    $('#iRcpGubn').text('대상');
                } else { //납부제외
                    $('#iRcpGubn').text('제외');
                }

                var cusHp1 = "";
                var cusHp2 = "";
                var cusHp3 = "";
                if (map.cusHp != null && map.cusHp != '') {
                    var _cusHp = getHpNoFormat(map.cusHp);
                    _cusHp = _cusHp.split('-');
                    $("#iCcusHp").text(_cusHp[0] + "-" + _cusHp[1] + "-" + _cusHp[2]);
                }

                $("#iVano").text(map.vano);
                if (map.cusMail == null || map.cusMail == '@') {
                    $("#iCusMail").text('');
                } else if (map.cusMail != null && map.cusMail != '') {
                    var _cusMail = map.cusMail;
                    _cusMail = _cusMail.split('@');
                    $("#iCusMail").text(_cusMail[0] + "@" + _cusMail[1]);
                }

                $("#iCusGubn1").text(basicUnEscape(map.cusGubn1));
                $("#iCusGubn2").text(basicUnEscape(map.cusGubn2));
                $("#iCusGubn3").text(basicUnEscape(map.cusGubn3));
                $("#iCusGubn4").text(basicUnEscape(map.cusGubn4));

                if (map.rcpReqTy == 'A') {
                    $('#iRcpreqty').text('자동발급');
                } else {
                    $('#iRcpreqty').text('수동발급');
                }
                if (map.cusType == '1') {
                    $("#iSelCusType").text('소득공제');
                } else if (map.cusType == '2') {
                    $("#iSelCusType").text('지출증빙');
                }

                //COMMENT ON COLUMN XCASHMAS. IS '발급방법 [11: 휴대폰번호, 12현금영수증카드번호, 13:주민번호, 21:사업자번호]';
                var confirm = '';
                if (map.confirm == '11') {
                    confirm = '휴대폰번호';
                } else if (map.confirm == '12') {
                    confirm = '현금영수증카드번호';
                } else if (map.confirm == '13') {
                    confirm = '주민번호';
                } else if (map.confirm == '21') {
                    confirm = '사업자번호';
                } else {
                }

                $("#iSelCusMtd").text(confirm);
                $("#iCusOffNo").text(map.cusOffNo);
                $("#iMemo").text(basicUnEscape(map.memo));
            }
        });

        fn_reset_scroll();

        $('#detail-payer-information').modal({
            backdrop: 'static',
            keyboard: false
        });
    }

</script>
