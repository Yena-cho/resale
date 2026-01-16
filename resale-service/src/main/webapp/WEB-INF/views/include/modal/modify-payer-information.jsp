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

<div class="modal fade" id="modify-payer-information" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="poptitle">고객정보수정</span></h5>
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
                            <span class="text-danger">* 필수입력</span>
                        </div>
                    </div>
                </div>

                <form>
                    <input type="hidden" id="notiMasCd">
                    <input type="hidden" id="chaCd">
                    <input type="hidden" id="vano" >
                    <input type="hidden" id="masKey">
                    <input type="hidden" id="smsYn">
                    <input type="hidden" id="mailYn">

                    <div class="container-fluid">
                        <table class="table table-form">
                            <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>고객명</th>
                                    <td class="col-md-4 col-sm-8 col-8">
                                        <input type="text" class="form-control" id="tDcustName" maxlength="20">
                                        <input type="hidden" id="BeCusName" name="BeCusName">
                                    </td>
                                    <th class="col-md-2 col-sm-4 col-4">고객번호</th>
                                    <td class="col-md-4 col-sm-8 col-8"><input type="text" id="cusKey" class="form-control" maxlength="20"></td>
                                </tr>

                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-4">
                                        <span class="text-danger">*</span>납부대상
                                        <a tabindex="0" class="popover-dismiss ml-2" role="button" data-toggle="popover"
                                           data-trigger="focus"
                                           title="납부제외란?" data-content="개별 고객 정보는 유지하면서 신규 청구를 제외하여 납부 일시정지 처리하는 기능">
                                            <img src="/assets/imgs/common/icon-info.png">
                                        </a>
                                    </th>
                                    <td class="col-md-4 col-sm-8 col-8">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="payable" name="payable-option" value="Y">
                                            <label for="payable"><span class="mr-2"></span>대상</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="excepted" name="payable-option" value="N">
                                            <label for="excepted"><span class="mr-2"></span>제외</label>
                                        </div>
                                    </td>
                                    <th class="col-md-2 col-sm-4 col-4">가상계좌</th>
                                    <td class="col-md-4 col-sm-8 col-8">
                                        <div id="tDvano"> </div>
                                        <input type="text" class="form-control" id="inputVano" maxlength="20" placeholder="미입력 시 자동으로 생성됩니다.">
                                    </td>
                                </tr>

                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-4">연락처</th>
                                    <td class="col-md-10 col-sm-8 col-8 form-inline" colspan="3">
                                        <div class="contact-number form-inline w-100">
                                            <input type="text" class="form-control" maxlength="3" id="cusHp1" onkeyup="onlyNumber(this)">
                                            <span class="hypen"> - </span>
                                            <input type="text" class="form-control" maxlength="4" id="cusHp2" onkeyup="onlyNumber(this)">
                                            <span class="hypen"> - </span>
                                            <input type="text" class="form-control" maxlength="4" id="cusHp3" onkeyup="onlyNumber(this)">
                                        </div>
                                        <div class="guide-mention w-100">* 휴대폰 번호로 입력하지 않을 시, 문자메시지 서비스 이용에 제한이 있을 수 있습니다.</div>
                                    </td>
                                </tr>

                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-4">메일주소</th>
                                    <td class="col-md-10 col-sm-8 col-8 form-inline" colspan="3">
                                        <div class="form-inline email-contact w-100">
                                            <input type="text" class="form-control email-name" id="cusMail1">
                                            <span class="ml-1 mr-1">@</span>
                                            <input type="text" class="form-control mr-1 email-provider" id="cusMail2">
                                            <select class="form-control provider-preset" id="cusMail3">
                                                <option value="">직접입력</option>
                                                <option value="naver.com">naver.com</option>
                                                <option value="nate.com">nate.com</option>
                                                <option value="yahoo.com">yahoo.com</option>
                                                <option value="empal.com">empal.com</option>
                                                <option value="gmail.com">gmail.com</option>
                                                <option value="hanmail.net">hanmail.net</option>
                                                <option value="daum.net">daum.net</option>
                                            </select>
                                        </div>
                                        <div class="guide-mention w-100">* 메일주소 미 입력 시, E-MAIL 고지 서비스 이용에 제한이 있을 수 있습니다.</div>
                                    </td>
                                </tr>

                                <c:if test="${!empty orgSess.cusGubn1}">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-4">${orgSess.cusGubn1}
                                            <a tabindex="0" class="popover-dismiss ml-2" role="button" data-toggle="popover"
                                               data-trigger="focus"
                                               title="고객구분이란?"
                                               data-content="고객별 그룹을 지정하는 명칭입니다.(ex : 학년, 반, 지역, 학번) 고객구분 그룹명 변경은 마이페이지 > 서비스설정 > 고객구분 탭에서 변경가능합니다.">
                                                <img src="/assets/imgs/common/icon-info.png">
                                            </a>
                                        </th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <img src="/assets/imgs/common/icon-x-in-circle.png" class="remove-text" style="position: absolute;right: 23px;" onclick="javascript:{$('#cusGubn1').val('');}">
                                            <input type="text" id="cusGubn1" <c:if test="${orgSess.cusGubn1 ==  null || orgSess.cusGubn1 == '' }">readonly='true'</c:if> class="form-control">
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-4">${orgSess.cusGubn2}</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <img src="/assets/imgs/common/icon-x-in-circle.png" class="remove-text" style="position: absolute;right: 23px;" onclick="javascript:{$('#cusGubn2').val('');}">
                                            <input type="text" id="cusGubn2" <c:if test="${orgSess.cusGubn2 ==  null || orgSess.cusGubn2 == '' }">readonly='true'</c:if> class="form-control">
                                        </td>
                                    </tr>
                                </c:if>

                                <c:if test="${!empty orgSess.cusGubn3}">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-4">${orgSess.cusGubn3}</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <img src="/assets/imgs/common/icon-x-in-circle.png" class="remove-text" style="position: absolute;right: 23px;" onclick="javascript:{$('#cusGubn3').val('');}">
                                            <input type="text" id="cusGubn3" <c:if test="${orgSess.cusGubn3 ==  null || orgSess.cusGubn3 == '' }">readonly='true'</c:if> class="form-control">
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-4">${orgSess.cusGubn4}</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <img src="/assets/imgs/common/icon-x-in-circle.png" class="remove-text" style="position: absolute;right: 23px;" onclick="javascript:{$('#cusGubn4').val('');}">
                                            <input type="text" id="cusGubn4" <c:if test="${orgSess.cusGubn4 ==  null || orgSess.cusGubn4 == '' }">readonly='true'</c:if> class="form-control">
                                        </td>
                                    </tr>
                                </c:if>

                                <tr class="row no-gutters hidden-on-mobile">
                                    <th class="col-md-2 col-sm-4 col-4">고객상태</th>
                                    <td class="col-md-10 col-sm-8 col-8 form-inline" colspan="3">
                                        <div class="form-inline w-100" id="divcuststat">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="rdoDisabled2" name="rdoDisabled" value="N">
                                                <label for="rdoDisabled2"><span class="mr-2"></span>정상</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="rdoDisabled3" name="rdoDisabled" value="Y">
                                                <label for="rdoDisabled3"><span class="mr-2"></span>해지</label>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="guide-mention w-100">* 고객구분 정보 수정 시, 기존 등록된 청구조회 내역의 고객구분 정보와 다르게 노출될 수 있습니다.</div>
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
                                    <th class="col-md-2 col-sm-4 col-4">발급용도</th>
                                    <td class="col-md-4 col-sm-8 col-8">
                                        <select id="selCusType" class="form-control parents" onchange="fn_selCusType(this.value)">
                                            <option value="">선택</option>
                                            <option value="1">소득공제</option>
                                            <option value="2">지출증빙</option>
                                        </select>
                                    </td>
                                    <th class="col-md-2 col-sm-4 col-4">발급방법</th>
                                    <td class="col-md-4 col-sm-8 col-8">
                                        <select id="selCusMtd" class="form-control first-depth-child" disabled="disabled">
                                            <option value="">선택</option>
                                            <option value="11">휴대폰번호</option>
                                            <option value="12">현금영수증카드번호</option>
                                            <option value="13">주민등록번호</option>
                                            <option value="21">사업자번호</option>
                                        </select>
                                    </td>
                                </tr>

                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-4">발급번호</th>
                                    <td class="col-md-10 col-sm-8 col-8" colspan="3">
                                        <input type="text" id="cusOffNo" class="form-control first-depth-child" maxlength="20" placeholder="숫자만 입력" style="max-width:260px;" onkeyup="onlyNumber(this)" disabled>
                                        <span class="guide-mention ml-2">'-' 없이 숫자만 입력</span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="container-fluid">
                        <div class="row no-gutters mt-4">
                            <div class="col-6">
                                <h5>메모</h5>
                            </div>
                            <div class="col-6 text-right">
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <table class="table table-form">
                            <tbody>
                                <tr class="row no-gutters">
                                    <td class="col-12">
                                        <textarea class="form-control smscount" id="memo" name="memo" rows="3" maxlength="500"></textarea>
                                        <span class="textarea-byte-counter"><em>0</em>byte</span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="guide-mention" style="color:#71adfd;">* 고객별 특이사항을 기입할 때 이용하세요</div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary save-payer-information" id="btn-title">고객대상 추가</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('.popover-dismiss').popover({
            trigger: 'focus'
        });

        // 저장(insert)
        $(".save-payer-information").click(function () {
            var url = "";
            var param = {};
            var qM = "";

            var msgConfirm = "";
            var msgDone = "";

            var mail = $("#cusMail1").val() + $("#cusMail2").val();
            if (mail) {
                mail = $("#cusMail1").val() + "@" + $("#cusMail2").val();
            } else {
                mail = "";
            }

            if (MODE == 'I') {
                //임시등록시 필드 검증 함수 추가
                if (!fnValidationReg()) {
                    return;
                }

                //현금영수증 관련 검증 함수
                if (!fnValidationRcpReq()) {
                    return;
                }

    		url = "/org/custMgmt/regCustInfo";
    		param = {
    				notiMasCd : $("#notiMasCd").val()
    			,	chaCd     : $("#chaCd").val()
    			,	vano      : $("#inputVano").val()
    			,	masKey    : $("#masKey").val()
    			,	cusKey    : $("#cusKey").val()
    			,	cusName   : $("#tDcustName").val()
    			,	cusGubn1  : nullValueChange($("#cusGubn1").val())
    			,	cusGubn2  : nullValueChange($("#cusGubn2").val())
    			,	cusGubn3  : nullValueChange($("#cusGubn3").val())
    			,	cusGubn4  : nullValueChange($("#cusGubn4").val())
    			,	cusHp     : $("#cusHp1").val() + $("#cusHp2").val() + $("#cusHp3").val()
    			,	cusMail   : mail
    			,	smsYn     : $("#smsYn").val()
    			,	mailYn    : $("#mailYn").val()
    			,	disabled  : 'I' // 등록시에는 무조건 임시로 등록
   				,	cusType	  : $("#selCusType option:selected").val()
       			,	confirm   : $("#selCusMtd option:selected").val()
    			,	rcpGubn	  : $("input[name='payable-option']:checked").val()
    			,	rcpReqTy  : $('input[name="receipt-issuing-option"]:checked').val()
    			,	cusOffNo  : $("#cusOffNo").val()
    			,   memo      : basicEscape($("#memo").val())
    		};
    		qM = "등록";

                msgConfirm = "고객 대상 목록에 추가하시겠습니까?";
                msgDone = "고객대상 목록에 추가되었습니다.";
            } else {
                // 수정시 필드 검증 함수 추가
                // if (!fnValidationMod()) {
                if (!fnValidationReg()) {
                    return;
                }
                //현금영수증 관련 검증 함수
                if (!fnValidationRcpReq()) {
                    return;
                }

    		url = "/org/custMgmt/updateCustInfo";
    		param = {
    				notiMasCd : $("#notiMasCd").val()
    			,	chaCd     : $("#chaCd").val()
    			,	vano      : $("#vano").val()
    			,	masKey    : $("#masKey").val()
    			,	cusKey    : $("#cusKey").val()
    			,	cusName   : $("#tDcustName").val()
    			,   beCusName : $("#BeCusName").val()
    			,	cusGubn1  : nullValueChange($("#cusGubn1").val())
    			,	cusGubn2  : nullValueChange($("#cusGubn2").val())
    			,	cusGubn3  : nullValueChange($("#cusGubn3").val())
    			,	cusGubn4  : nullValueChange($("#cusGubn4").val())
    			,	cusHp     : $("#cusHp1").val() + $("#cusHp2").val() + $("#cusHp3").val()
    			,	cusMail   : mail //$("#cusMail1").val() + "@" + $("#cusMail2").val()
    			,	smsYn     : $("#smsYn").val()
    			,	mailYn    : $("#mailYn").val()
    			,	cusType	  : $("#selCusType option:selected").val()
    			,	confirm   : $("#selCusMtd option:selected").val()
    			,	rcpGubn	  : $("input[name='payable-option']:checked").val()
    			,	rcpReqTy  : $('input[name="receipt-issuing-option"]:checked').val()
    			,	cusOffNo  : $("#cusOffNo").val()
    			,   memo      : basicEscape($("#memo").val())
    		};
    		qM = "수정";

                msgConfirm = "고객 대상 정보를 수정하시겠습니까?";
                msgDone = "고객대상 정보가 수정되었습니다.";
            }

            swal({
                type: 'question',
                html: msgConfirm,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        type: "post",
                        url: url,
                        async: true,
                        data: JSON.stringify(param),
                        contentType: "application/json; charset=utf-8",
                        success: function (result) {
                            if (result.retCode == "0000") {
                                swal({
                                    type: 'success',
                                    text: msgDone,
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                }).then(function (result) {
                                    if (result.value) {
                                        $("#modify-payer-information").modal("hide");
                                        $('#row-th').prop('checked', false);
                                        fnSearch();
                                    }
                                });
                            } else if (result.retCode == "0001") {
                                swal({
                                    type: 'error',
                                    text: "사용할 수 없는 가상계좌입니다. 다시 입력해 주세요.",
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                });
                            } else if (result.retCode == "0002") {
                                swal({
                                    type: 'error',
                                    text: "가상계좌가 부족합니다.\n가상계좌 발급을 위해 고객센터로 연락주시기 바랍니다.",
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                });
                            } else if (result.retCode == "0003") {
                                swal({
                                    type: 'info',
                                    text: "고객 정보 수정이 완료되었습니다.\n고객조회로 이동하시겠습니까?",
                                    showCancelButton: true,
                                    confirmButtonColor: '#3085d6',
                                    cancelButtonColor: '#d33',
                                    confirmButtonText: '고객조회',
                                    cancelButtonText: '취소',
                                    reverseButtons: true
                                }).then(function (result) {
                                    if (result.value) {
                                        $("#modify-payer-information").modal("hide");
                                        $('#row-th').prop('checked', false);

                                        window.location.href = '/org/custMgmt/custList';
                                    }
                                });
                            } else if (result.retCode == "0004") {
                                swal({
                                    type: 'error',
                                    text: "삭제된 가상계좌입니다. 다시 입력해 주세요.",
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                });
                            } else if (result.retCode == "0005") {
                                swal({
                                    type: 'error',
                                    text: "중복된 고객번호입니다. 다시 입력해 주세요.",
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })
                            }
                        },
                        error: function (data) {
                            swal({
                                type: 'error',
                                text: result.retMsg,
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            })
                        }
                    });
                }
            });
        });

        $(".btn-add-selected-charge").click(function () {
            swal({
                type: 'success',
                text: SAVE_MSG,
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
        });

        $("label[for=load-all-payer]").click(function () {
            $("select.condition").attr("disabled", true);
            $("input.condition").attr("disabled", true).val("");
        });

        $("label[for=load-unregisted-payer]").click(function () {
            $(".condition").attr("disabled", false);
        });

        $(".smscount").on("keyup", function () {
            var id = $(this).attr('id');
            var str = $("#" + id + "").val();
            var tbyte = 0;
            var strlength = 0;
            var charStr = '';
            var cutstr = '';
            var limit = 1800;

            for (var i = 0; i < str.length; i++) {
                //한글체크
                charStr = str.charAt(i);
                if (escape(charStr).length > 4) {
                    tbyte += 2;
                } else {
                    tbyte++;
                }

                //길면 자르기
                if (tbyte <= limit) {
                    strlength = i + 1;
                }

            }

            if (tbyte > limit) {
                cutstr = $("#" + id + "").val().substr(str, strlength);
                $("#" + id + "").val(cutstr);
                $("#" + id + "").parent().children().children().html(tbyte);
            } else {
                $("#" + id + "").parent().children().children().html(tbyte);
            }
        });

    });

    // validation
    function fnValidationReg() {

        if (MODE == 'I') {
            if (!$('#tDcustName').val()) {
                swal({
                    type: 'info',
                    text: '고객명을 입력해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function () {
                    $("#tDcustName").focus();
                });
                return;
            }
        }

        if (!$('#payable').prop('checked') && !$('#excepted').prop('checked')) {
            swal({
                type: 'info',
                text: '납부대상을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var tel = $('#cusHp1').val() + $('#cusHp2').val() + $('#cusHp3').val();
        if (tel) {
            if ($('#cusHp1').val().length < 2 || $('#cusHp2').val().length < 3 || $('#cusHp3').val().length < 4) {
                swal({
                    type: 'info',
                    text: '연락처를 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            
            //!!!!휴대폰 번호 및 유선번호 허용 하도록 regExp 변경
            //var regExp = /(01[0|1|6|9|7])(\d{3}|\d{4})(\d{4}$)/g;
            var regExp = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;            
            var result = regExp.exec($('#cusHp1').val() + $('#cusHp2').val() + $('#cusHp3').val());
            if (!result) {
                swal({
                    type: 'info',
                    text: "전화번호 형식에 맞게 다시 입력해 주세요!",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        var mail = $('#cusMail1').val() + $('#cusMail2').val();
        if (mail) {
            if ($('#cusMail1').val().length == 0 || $('#cusMail2').val().length == 0) {
                swal({
                    type: 'info',
                    text: '메일주소를 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            var regex = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
            var mail = $('#cusMail1').val() + '@' + $('#cusMail2').val();
            if (!regex.test(mail)) {
                swal({
                    type: 'info',
                    text: "E-MAIL 형식이 올바르지 않습니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        return true;        
    }

    function fnValidationRcpReq() {
        if ($("#auto").is(":checked") && !$("#selCusType option:selected").val()) {
            swal({
                type: 'info',
                text: '발급용도를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        // 발급용도
        if ($("#selCusType option:selected").val() != '') {
            if (!$("#selCusMtd option:selected").val()) {
                swal({
                    type: 'info',
                    text: '발급방법을 선택해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            if (!$("#cusOffNo").val()) {
                swal({
                    type: 'info',
                    text: '발급번호를 입력해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }
        return true;
    }

    function modify_payer_information_init(mod, thisId) {
        if (mod == "U") {
            $("#tDvano").css("display", "block");
            $("#inputVano").css("display", "none");
        } else {
            $("#tDvano").css("display", "none");
            $("#inputVano").css("display", "block");
        }

        var tDrn = "";
        var tDcustName = "";
        var tDregDt = "";
        var tDcusKey = "";
        var tDcusGubn1 = "";
        var tDcusGubn2 = "";
        var tDcusGubn3 = "";
        var tDcusGubn4 = "";
        var tDvano = "";
        var tDcusHp = "";
        var tDmasKey = "";
        var tDcusMail = "";
        var tDrcpGubn = "";
        var tDdisabled = "";
        var tDrcpReqTy = "";

        MODE = mod;

        if (mod == 'I') {
            SAVE_MSG = '고객 대상 목록에 추가되었습니다.';

            $("#poptitle").text("개별고객등록");
            $("#btn-title").text("고객대상 추가");
            $("#tDcustName").val("");
            $("#tDvano").html("");
            $("input[name=payable-option]:input[value='Y']").attr("checked", true);
            $("input[name=receipt-issuing-option]:input[value='M']").attr("checked", true);

            $("#cusHp1").val("");
            $("#cusHp2").val("");
            $("#cusHp3").val("");

            $("#cusMail1").val("");
            $("#cusMail2").val("");
            $("#cusMail3").val("");

            $("#cusGubn1").val("");
            $("#cusGubn2").val("");
            $("#cusGubn3").val("");
            $("#cusGubn4").val("");

            $("#selCusType").val("");
            $("#selCusMtd").val("");
            $("#cusOffNo").val("");

            var _target = $("#selCusType").val();

            if (_target.length == 0) {
                $(".first-depth-child").attr("disabled", true);
            } else if (_target.length = !0) {
                $(".first-depth-child").attr("disabled", false);
            }

            $("#notiMasCd").val("");
            $("#chaCd").val("");
            $("#vano").val("");
            $("#masKey").val("");
            $("#cusKey").val("");
            $("#smsYn").val("N");
            $("#mailYn").val("N");
            $("#memo").val("");
            $("#divcuststat").hide();

        } else {
            SAVE_MSG = '고객 정보가 수정되었습니다.';

            $("#poptitle").text("고객정보수정");
            $("#btn-title").text("고객대상 수정");
            $("#divcuststat").show();

            var vano = "";
            var checkbox = "";
            if (thisId != null && thisId != "" && thisId != "undefined") {
                vano = thisId;
            } else {
                checkbox = $("input[name=checkOne]:checked");
                vano = checkbox.val();
            }

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
                    $("#notiMasCd").val(map.notiMasCd);
                    $("#chaCd").val(map.chaCd);
                    $("#vano").val(map.vano);
                    $("#masKey").val(map.masKey);
                    $("#cusKey").val(map.cusKey);
                    $("#smsYn").val(map.smsYn);
                    $("#mailYn").val(map.mailYn);
                    $("#tDcustName").val(map.cusName);
                    $("#BeCusName").val(map.cusName);

                    if (map.disabled == 'N') {
                        $("#divcuststat").html("정상");
                    } else if (map.disabled == 'Y') {

                        $("#divcuststat").html("삭제");
                    } else {
                        $("#divcuststat").html("임시");
                    }
                    if (map.rcpGubn == 'Y') {
                        $('#payable').prop('checked', true);
                        $('#excepted').prop('checked', false);
                    } else {
                        $('#payable').prop('checked', false);
                        $('#excepted').prop('checked', true);
                    }

                    var cusHp1 = "";
                    var cusHp2 = "";
                    var cusHp3 = "";
                    if (map.cusHp != null && map.cusHp != '') {
                        var _cusHp = getHpNoFormat(map.cusHp);
                        _cusHp = _cusHp.split('-');
                        $("#cusHp1").val(_cusHp[0]);
                        $("#cusHp2").val(_cusHp[1]);
                        $("#cusHp3").val(_cusHp[2]);
                    }else{
                        $("#cusHp1").val("");
                        $("#cusHp2").val("");
                        $("#cusHp3").val("");
                    }

                    $("#tDvano").html(vano);
                    if (map.cusMail != null && map.cusMail != '') {
                        var _cusMail = map.cusMail;
                        _cusMail = _cusMail.split('@');
                        $("#cusMail1").val(_cusMail[0]);
                        $("#cusMail2").val(_cusMail[1]);
                        $("#cusMail3").val(_cusMail[1]).prop("selected", true);
                        if ($("#cusMail3").val() == null || $("#cusMail3").val() == '') {
                            $("#cusMail3").val("");
                        }
                        if ($("#cusMail3").val() != '') {
                            $("#cusMail2").prop("disabled", "disabled");
                        }
                    } else {
                        $("#cusMail1").val("");
                        $("#cusMail2").val("");
                        $("#cusMail3").val("");
                        $("#cusMail2").prop("disabled", false);
                    }
                    $("#cusGubn1").val(map.cusGubn1);
                    $("#cusGubn2").val(map.cusGubn2);
                    $("#cusGubn3").val(map.cusGubn3);
                    $("#cusGubn4").val(map.cusGubn4);

                    if (map.rcpReqTy == 'A') {
                        $('#auto').prop('checked', true);
                        $('#manual').prop('checked', false);
                    } else {
                        $('#auto').prop('checked', false);
                        $('#manual').prop('checked', true);
                    }
                    $("#selCusType").val(map.cusType);
                    $("#cusOffNo").val(map.cusOffNo);

                    var _target = $("#selCusType").val();

                    if (_target = !null) {
                        $(".first-depth-child").attr("disabled", false);
                    } else {
                        $(".first-depth-child").attr("disabled", true);
                    }

                    $("#memo").val(map.memo);

                    fn_selCusType(map.cusType);
                    $("#selCusMtd").val(map.confirm);
                    countBytes($("#memo").val(), 'memo');
                }
            });
        }
    }

    // 도메인 선택시 변경
    function fn_mailDomainChg(val) {
        if (val == '') { // 직접입력
            $('#cusMail2').val(val);
            $('#cusMail2').attr('disabled', false);
        } else {
            $('#cusMail2').val(val);
            $('#cusMail2').attr('disabled', true);
        }
    }

    // 발급용도변경
    function fn_selCusType(val) {
        $('#selCusMtd').html('');
        $('#selCusMtd').val('');
        var str = '';
        if (val == "2") {
            str += '<option value="21">사업자번호</option>';
        } else if (val == "1") {
            str += '<option value="11">휴대폰번호</option>';
            str += '<option value="12">현금영수증카드번호</option>';
        } else {
            str += '<option value="">선택</option>';
        }

        $('#selCusMtd').html(str);
    }
</script>
