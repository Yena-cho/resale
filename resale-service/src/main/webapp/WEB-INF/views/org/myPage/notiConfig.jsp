<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<s:authentication property="principal.username" var="chaCd"/>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<script>
    var logoCheckSize = "ture";

    $(document).ready(function () {
        var showType = 'cash';
        $("#imgExist").val("Y");
        $('#fromsms').click(function () {
            showType = 'cash';
            $('.cashDct').css('display', '');
            $('.atDct').css('display', 'none');
            $('.refundDct').css('display', 'none');
        });
        $('#fromat').click(function () {
            showType = 'at';
            $('.cashDct').css('display', 'none');
            $('.atDct').css('display', '');
            $('.refundDct').css('display', 'none');
        });
        $('#fromnoti').click(function () {
            showType = 'refund';
            $('.cashDct').css('display', 'none');
            $('.atDct').css('display', 'none');
            $('.refundDct').css('display', '');
        });

        switch ('${map.smsConfig.useSmsYn}') {
            case 'Y':
                $('#useSmsNotiYn').css('display', '');
                $('#useSmsPayYn').css('display', '');
                $("#saveSms").css('display', '');
                $(".smsDP").css('display', '');
                break;
            case 'N':
            case 'W':
            default:
                break;
        }

        switch ('${map.atConfig.atYn}') {
            case 'Y':
                $('#useAtNotiYn').css('display', '');
                $('#useAtPayYn').css('display', '');
                $('#saveAt').css('display', '');
                $(".atDP").css('display', '');
                break;
            case 'N':
            case 'W':
            case 'D':
            default:
                break;
        }

        //초기 셋팅
        if ('${map.smsConfig.useSmsYn}' == 'A') {
            $('#notiSmsSend_1').prop("checked", true);
        } else if ('${map.smsConfig.useSmsYn}' == 'M' || '${map.smsConfig.useSmsYn}' == 'N') {
            $('#notiSmsSend_2').prop("checked", true);
        }

        if ('${map.atConfig.atYn}' == 'A') {
            $('#notiAtSend_1').prop("checked", true);
        } else if ('${map.atConfig.atYn}' == 'M' || '${map.atConfig.atYn}' == 'N' || '${map.atConfig.atYn}' == 'D') {
            $('#notiAtSend_2').prop("checked", true);
        }

        if ('${map.smsConfig.rcpSmsTy}' == 'A') {
            $('#depositSmsSend_1').prop("checked", true);
        } else if ('${map.smsConfig.rcpSmsTy}' == 'M' || '${map.smsConfig.rcpSmsTy}' == 'N') {
            $('#depositSmsSend_2').prop("checked", true);
        }

        $(".billcontcheck").on("keyup", function () {
            var id = $(this).attr('id');
            var cutstr = '';
            var limit = 1600;
            var maxLine = 10;
            var text = $("#" + id + "").val();
            var textlength = 0;
            var line = text.split("\n").length;
            var lines = text.split(/(\r\n|\n|\r)/gm);
            var _byte = 0;
            var lineMaxLength = 80;

            for (var idx = 0; idx < text.length; idx++) {
                var oneChar = escape(text.charAt(idx));
                if (oneChar.length == 1) {
                    _byte++;
                } else if (oneChar.indexOf("%u") != -1) { // 한글~
                    _byte += 2;
                } else if (oneChar.indexOf("%") != -1) {
                    _byte++;
                }

                if (_byte <= limit) {
                    textlength = idx + 1;
                }
            }

            for (var i = 0; i < lines.length; i++) {
                if (lines[i].length > lineMaxLength) {
                    var text = $(this).val();
                    var lines = text.split(/(\r\n|\n|\r)/gm);
                    for (var i = 0; i < lines.length; i++) {
                        if (lines[i].length > lineMaxLength) {
                            lines[i] = lines[i].substring(0, lineMaxLength);
                        }
                    }
                    swal({
                        type: 'warning',
                        text: '한줄에 ' + lineMaxLength + '자 이하 최대 10줄, 최대 800자 까지만 작성할 수 있습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    $(this).val(lines.join(''));
                    $(this).blur();
                }
            }

            if (_byte > limit) {
                cutstr = $("#" + id + "").val().substr(text, textlength);
                $("#" + id + "").val(cutstr);
                $("#" + id + "").parent().children().children().html(_byte);
                limitCharacters(id);
            } else {
                $("#" + id + "").parent().children().children().html(_byte);
            }

            if (event.keyCode == 13 || event.keyCode == 176) {
                limitCharacters(id);
            }
            if (event.keyCode == 17 || event.keyCode == 86) {
                limitCharacters(id);
            }
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

                charStr = str.charAt(i);
                if (escape(charStr).length > 4) {
                    tbyte += 2;
                } else {
                    tbyte++;
                }

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

        $('.billname').on("keyup", function () {
            var id = $(this).attr('id');
            var str = $("#" + id + "").val();
            var tbyte = 0;
            var strlength = 0;
            var charStr = '';
            var cutstr = '';
            var limit = 40;

            for (var i = 0; i < str.length; i++) {

                charStr = str.charAt(i);
                if (escape(charStr).length > 4) {
                    tbyte += 2;
                } else {
                    tbyte++;
                }

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

        $("#uploadLogo").change(function () {
            if ($(this).val() != "") {
                var file = $("#uploadLogo")[0].files[0];

                var img = new Image();
                var _URL = window.URL || window.webKitURL;
                img.src = _URL.createObjectURL(file);

                img.onload = function () {
                    if (img.width < 151 && img.height < 31) {
                        return logoCheckSize = "true";
                    } else {
                        return logoCheckSize = "false";
                    }
                };
            }
        });

        logoView();

        countBytes($('#notSms').val(), 'notSms');
        countBytes($('#rcpSms').val(), 'rcpSms');
        countBytes($('#notSms2').val(), 'notSms2');
        countBytes($("#smsMsgDef1").val(), 'smsMsgDef1');
        countBytes($("#smsMsgDef2").val(), 'smsMsgDef2');

        countBytes($("#billCont1").val(), 'billCont1');
        countBytes($("#billCont2").val(), 'billCont2');
        countBytes($("#billCont3").val(), 'billCont3');

        countBytes($("#billName1").val(), 'billName1');
        countBytes($("#billName2").val(), 'billName2');
        countBytes($("#billName3").val(), 'billName3');
    });
</script>

<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link" href="#">기본정보관리</a>
            <a class="nav-link" href="#">서비스설정</a>
            <a class="nav-link active" href="#">고지상세설정</a>
            <a class="nav-link" href="#">이용료조회</a>
            <a class="nav-link" href="#">수납현황분석</a>
        </nav>
    </div>
    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                    <span> > </span>
                    <span class="depth-1">마이페이지</span>
                    <span> > </span>
                    <span class="depth-2 active">고지상세설정</span>
                </div>
            </div>
            <div class="row">
                <div class="col-6">
                    <h2>고지상세설정</h2>
                </div>
                <div class="col-6 text-right">
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>고지수단별 상세 설정을 위한 화면입니다.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col">
                <div class="tab-selecter type-3">
                    <ul class="nav nav-tabs" role="tablist" id="myTab">
                        <li class="nav-item"><a class="nav-link active" data-toggle="tab" data-lastValue="smstab"  href="#sms-lms-setting" id="fromsms">문자메시지</a></li>
                        <li class="nav-item"><a class="nav-link" data-toggle="tab" data-lastValue="attab" href="#at-setting" id="fromat">알림톡</a></li>
                        <li class="nav-item"><a class="nav-link" data-toggle="tab" data-lastValue="emailtab" href="#bill-setting" id="fromnoti">출력/E-MAIL 고지서</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="container tab-content mt-3">
        <div id="sms-lms-setting" class="tab-pane fade show active">
            <div class="row">
                <div class="col">
                    <h5>문자메시지 발송 여부</h5>
                </div>
                <c:if test="${map.smsConfig.useSmsYn == 'N'}">
                    <div class="col mb-1" align="right">
                        <button id="btn-sms-notification-popup" class="btn btn-primary" onclick="fn_smsNoti();">문자메시지 사용 등록</button>
                    </div>
                </c:if>
            </div>
            <div class="row">
                <div class="col">
                    <table class="table table-primary table-form">
                        <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-3 col-sm-4 col-4">문자메시지 사용 여부</th>
                            <c:if test="${map.smsConfig.useSmsYn == 'Y'}">
                                <td class="col-md-9 col-sm-8 col-8 form-inline">
                                        <span class="col-md-3 col-sm-3 form-inline">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="hidden" id="useSmsYn_Y" name="useSmsYn" value="Y"> <label for="useSmsYn_Y">
                                                <span class="mr-2"></span>사용</label>
                                            </div>
                                        </span>

                                    <span class="col-md-9 col-sm-9">
                                            <span class="guide-mention">* 문자메시지 서비스 이용해지를 원하시는 경우 고객센터에 연락 바람.</span>
                                        </span>
                                </td>
                            </c:if>
                            <c:if test="${map.smsConfig.useSmsYn == 'W'}">
                                <td class="col-md-9 col-sm-8 col-8 form-inline">
                                        <span class="col-md-3 col-sm-3 form-inline">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="hidden" id="useSmsYn_W" name="useSmsYn" value="W"> <label for="useSmsYn_N">
                                                <span class="mr-2"></span>등록중</label>
                                            </div>
                                        </span>

                                    <span class="col-md-9 col-sm-9">
                                            <span class="guide-mention">* 문자메시지 서비스 이용등록 신청중.</span>
                                        </span>
                                </td>
                            </c:if>
                            <c:if test="${map.smsConfig.useSmsYn == 'N'}">
                                <td class="col-md-9 col-sm-8 col-8 form-inline">
                                        <span class="col-md-3 col-sm-3 form-inline">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="hidden" id="useSmsYn_N" name="useSmsYn" value="N"> <label for="useSmsYn_N">
                                                    <span class="mr-2"></span>미사용</label>
                                            </div>
                                        </span>

                                    <span class="col-md-9 col-sm-9">
                                            <span class="guide-mention">* 문자메시지 서비스 이용을 원하시는 경우 먼저 서비스 사용 등록 필요.</span>
                                        </span>
                                </td>
                            </c:if>
                        </tr>
                        <tr class="row no-gutters" id="useSmsNotiYn" style="display: none;">
                            <th class="col-md-3 col-sm-4 col-4">청구 시 문자메시지 발송</th>
                            <td class="col-md-9 col-sm-8 col-8 form-inline">
                                    <span class="col-md-3 col-sm-3 form-inline">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="notiSmsSend_1" name="notiSmsSend" value="A" <c:if test="${map.smsConfig.notSmsYn == 'A'}">checked</c:if>>
                                            <label for="notiSmsSend_1"><span class="mr-2"></span>자동</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="notiSmsSend_2" name="notiSmsSend" value="M" <c:if test="${map.smsConfig.notSmsYn == 'M' || map.smsConfig.notSmsYn == 'N'}">checked</c:if>>
                                            <label for="notiSmsSend_2"><span class="mr-2"></span>수동</label>
                                        </div>
                                    </span>
                                <span class="col-md-9 col-sm-9">
									    <span class="guide-mention">* 청구 건에 대한 문자메시지 발송 여부 체크.</span>
                                    </span>
                            </td>
                        </tr>
                        <tr class="row no-gutters" id="useSmsPayYn" style="display: none;">
                            <th class="col-md-3 col-sm-4 col-4">입금 시 문자메시지 발송</th>
                            <td class="col-md-9 col-sm-8 col-8 form-inline">
                                    <span class="col-md-3 col-sm-3 form-inline">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="depositSmsSend_1" name="depositSmsSend" value="A" <c:if test="${map.smsConfig.rcpSmsTy == 'A'}">checked</c:if>>
                                            <label for="depositSmsSend_1"><span class="mr-2"></span>자동</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" id="depositSmsSend_2" name="depositSmsSend" value="M" <c:if test="${map.smsConfig.rcpSmsTy == 'M' || map.smsConfig.rcpSmsTy == 'N'}">checked</c:if>>
                                            <label for="depositSmsSend_2"><span class="mr-2"></span>수동</label>
                                        </div>
                                    </span>
                                <span class="col-md-9 col-sm-9">
									    <span class="guide-mention">* 입금 확인 건에 대한 문자메시지 발송 여부 체크.</span>
                                    </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row mt-4">
                <div class="col smsDP" style="display: none;">
                    <h5>
                        문자메시지 문구 설정
                        <span class="guide-mention">[web발신], [이용기관명], [전송 번호]은 필수값으로 자동 지정 됩니다.</span>
                    </h5>
                </div>
            </div>
            <div class="row">
                <div class="col smsDP" style="display: none;">
                    <table class="table table-primary table-form">
                        <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-3 col-sm-4 col-4">입력값 선택</th>
                            <td class="col-md-9 col-sm-8 col-8 form-inline">
                                <button type="button" class="btn btn-sm btn-primary btn-outlined mr-1" onclick="mergeAdd(1)" id="add1" value="[#고객명#]">#고객명</button>
                                <button type="button" class="btn btn-sm btn-primary btn-outlined mr-1" onclick="mergeAdd(2)" id="add2" value="[#청구월#]">#청구월</button>
                                <button type="button" class="btn btn-sm btn-primary btn-outlined mr-1" onclick="mergeAdd(3)" id="add3" value="[#청구액#]">#청구액</button>
                                <button type="button" class="btn btn-sm btn-primary btn-outlined mr-1" onclick="mergeAdd(4)" id="add4" value="[#납부기한#]">#납부기한</button>
                                <button type="button" class="btn btn-sm btn-primary btn-outlined mr-1" onclick="mergeAdd(5)" id="add5" value="[#모바일청구서#]">#모바일청구서</button>
                                <button type="button" class="btn btn-sm btn-primary btn-outlined mr-1" onclick="mergeAdd(6)" id="add6" value="[#납부계좌#]">#납부계좌</button>
                                <button type="button" class="btn btn-sm btn-primary btn-outlined mr-1" onclick="mergeAdd(7)" id="add7" value="[#미납액#]">#미납액</button>
                                <button type="button" class="btn btn-sm btn-primary btn-outlined mr-1" onclick="mergeAdd(8)" id="add8" value="[#고지서용마감일#]">#고지서용마감일</button>
                                <button type="button" class="btn btn-sm btn-primary btn-outlined mr-1" onclick="mergeAdd(9)" id="add9" value="[#고객구분#]">#고객구분</button>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-3 col-sm-4 col-4">
                                <div>
                                    <label>
                                        청구발송
                                    </label>
                                    <div class="w-100"></div>
                                    <button type="button" class="btn btn-sm btn-gray-outlined" data-toggle="modal" data-target="#sms-preview" onclick="smsFocus(1); smsLoadingPreView();">미리보기</button>
                                </div>
                            </th>
                            <td id="textarea-01" class="col-md-9 col-sm-8 col-8">
                                <textarea class="form-control has-counter smscount" rows="4" name="notSms" id="notSms" onfocus="smsFocus(1)" style="resize:none;">${map.smsConfig.notSms}</textarea>
                                <span class="textarea-byte-counter"><em>0</em>byte</span>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-3 col-sm-4 col-4">
                                <div>
                                    <label>
                                        입금발송
                                    </label>
                                    <div class="w-100"></div>
                                    <button type="button" class="btn btn-sm btn-gray-outlined" data-toggle="modal" data-target="#sms-preview" onclick="smsFocus(2); smsLoadingPreView();">미리보기</button>
                                </div>
                            </th>
                            <td id="textarea-02" class="col-md-9 col-sm-8 col-8">
                                <textarea class="form-control has-counter smscount" rows="4" name="rcpSms" id="rcpSms" onfocus="smsFocus(2)" style="resize:none;">${map.smsConfig.rcpSms}</textarea>
                                <span class="textarea-byte-counter"><em>0</em>byte</span>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-3 col-sm-4 col-4">
                                <div>
                                    <label>
                                        미납발송
                                    </label>
                                    <div class="w-100"></div>
                                    <button type="button" class="btn btn-sm btn-gray-outlined" data-toggle="modal" data-target="#sms-preview" onclick="smsFocus(3); smsLoadingPreView();">미리보기</button>
                                </div>
                            </th>
                            <td id="textarea-03" class="col-md-9 col-sm-8 col-8">
                                <textarea class="form-control has-counter smscount" rows="4" name="notSms2" id="notSms2" onfocus="smsFocus(3)" style="resize:none;">${map.smsConfig.notSms2}</textarea>
                                <span class="textarea-byte-counter"><em>0</em>byte</span>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-3 col-sm-4 col-4">
                                <div>
                                    <label>
                                        기본설정1
                                    </label>
                                    <div class="w-100"></div>
                                    <button type="button" class="btn btn-sm btn-gray-outlined" data-toggle="modal" data-target="#sms-preview" onclick="smsFocus(4); smsLoadingPreView();">미리보기</button>
                                </div>
                            </th>
                            <td id="textarea-04" class="col-md-9 col-sm-8 col-8">
                                <textarea class="form-control has-counter smscount" rows="4" name="smsMsgDef1" id="smsMsgDef1" onfocus="smsFocus(4)" style="resize:none;">${map.smsConfig.smsMsgDef1}</textarea>
                                <span class="textarea-byte-counter"><em>0</em>byte</span>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-3 col-sm-4 col-4">
                                <div>
                                    <label>
                                        기본설정2
                                    </label>
                                    <div class="w-100"></div>
                                    <button type="button" class="btn btn-sm btn-gray-outlined" data-toggle="modal" data-target="#sms-preview" onclick="smsFocus(5); smsLoadingPreView();">미리보기</button>
                                </div>
                            </th>
                            <td id="textarea-05" class="col-md-9 col-sm-8 col-8">
                                <textarea class="form-control has-counter smscount" rows="4" name="smsMsgDef2" id="smsMsgDef2" onfocus="smsFocus(5)" style="resize:none;">${map.smsConfig.smsMsgDef2}</textarea>
                                <span class="textarea-byte-counter"><em>0</em>byte</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row mt-3 mb-5">
                <div class="col text-center">
                    <button type="button" class="btn btn-primary btn-wide" onclick="saveSms();" id="saveSms" style="display: none;">저장</button>
                </div>
            </div>
        </div>

        <div id="at-setting" class="tab-pane fade">
            <div class="row">
                <div class="col">
                    <h5>알림톡 발송 설정</h5>
                </div>

                <c:if test="${map.atConfig.atYn == 'N' || map.atConfig.atYn == 'D'}">
                    <div class="col mb-1" align="right">
                        <button id="btn-at-notification-popup" class="btn btn-primary" onclick="fn_atNoti();">알림톡 이용신청</button>
                    </div>
                </c:if>
            </div>

            <div class="row">
                <div class="col">
                    <table class="table table-primary table-form">
                        <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-3 col-sm-4 col-4">알림톡 사용 여부</th>
                            <c:if test="${map.atConfig.atYn == 'Y'}">
                                <td class="col-md-9 col-sm-8 col-8 form-inline">
                                    <span class="col-md-3 col-sm-3 form-inline">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="hidden" id="atYn_Y" name="atYn" value="Y"> <label for="atYn_Y">
                                            <span class="mr-2"></span>사용</label>
                                        </div>
                                    </span>
                                </td>
                            </c:if>

                            <c:if test="${map.atConfig.atYn == 'W'}">
                                <td class="col-md-9 col-sm-8 col-8 form-inline">
                                    <span class="col-md-3 col-sm-3 form-inline">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="hidden" id="atYn_W" name="atYn" value="W"> <label for="atYn_W">
                                            <span class="mr-2"></span>신청중</label>
                                        </div>
                                    </span>
                                </td>
                            </c:if>

                            <c:if test="${map.atConfig.atYn == 'N'}">
                                <td class="col-md-9 col-sm-8 col-8 form-inline">
                                    <span class="col-md-3 col-sm-3 form-inline">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="hidden" id="atYn_N" name="atYn" value="N"> <label for="atYn_N">
                                            <span class="mr-2"></span>미사용</label>
                                        </div>
                                    </span>
                                </td>
                            </c:if>

                            <c:if test="${map.atConfig.atYn == 'D'}">
                                <td class="col-md-9 col-sm-8 col-8 form-inline">
                                    <span class="col-md-3 col-sm-3 form-inline">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="hidden" id="atYn_W" name="atYn" value="W"> <label for="atYn_W">
                                            <span class="mr-2"></span>미사용</label>
                                        </div>
                                    </span>
                                </td>
                            </c:if>
                        </tr>

                        <tr class="row no-gutters" id="useAtNotiYn" style="display: none;">
                            <th class="col-md-3 col-sm-4 col-4">청구 시 알림톡 발송</th>
                            <td class="col-md-9 col-sm-8 col-8 form-inline">
                                <span class="col-md-3 col-sm-3 form-inline">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="notiAtSend_1" name="notiAtSend" value="A" <c:if test="${map.atConfig.notAtYn == 'A'}">checked</c:if>>
                                        <label for="notiAtSend_1"><span class="mr-2"></span>자동</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="notiAtSend_2" name="notiAtSend" value="M" <c:if test="${map.atConfig.notAtYn == 'M' || map.atConfig.notAtYn == 'N'}">checked</c:if>>
                                        <label for="notiAtSend_2"><span class="mr-2"></span>수동</label>
                                    </div>
                                </span>

                                <span class="col-md-9 col-sm-9">
                                    <span class="guide-mention">* 청구 건에 대한 알림톡 발송 여부 체크.</span>
                                </span>
                            </td>
                        </tr>
                        <tr class="row no-gutters" id="useAtPayYn" style="display: none;">
                            <th class="col-md-3 col-sm-4 col-4">입금 시 알림톡 발송</th>
                            <td class="col-md-9 col-sm-8 col-8 form-inline">
                                <span class="col-md-3 col-sm-3 form-inline">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="depositAtSend_1" name="depositAtSend" value="A" <c:if test="${map.atConfig.rcpAtYn == 'A'}">checked</c:if>>
                                        <label for="depositAtSend_1"><span class="mr-2"></span>자동</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="depositAtSend_2" name="depositAtSend" value="M" <c:if test="${map.atConfig.rcpAtYn == 'M' || map.atConfig.rcpAtYn == 'N'}">checked</c:if>>
                                        <label for="depositAtSend_2"><span class="mr-2"></span>수동</label>
                                    </div>
                                </span>

                                <span class="col-md-9 col-sm-9">
                                    <span class="guide-mention">* 입금 확인 건에 대한 알림톡 발송 여부 체크.</span>
                                </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col atDP" style="display: none;">
                    <h5>
                        알림톡 문구 설정
                        <span class="guide-mention">알림톡 발송 문구는 지정된 템플릿으로 발송되어 수정할 수 없습니다.</span>
                    </h5>
                </div>
            </div>

            <div class="row">
                <div class="col atDP" style="display: none;">
                    <table class="table table-primary table-form">
                        <tbody class="container-fluid">
                        <tr class="row no-gutters" style="height: 100px;">
                            <th class="col-md-4 col-sm-4 col-4">
                                <div>
                                    <label>청구발송</label>
                                    <div class="w-100"></div>
                                    <button type="button" class="btn btn-sm btn-gray-outlined" data-toggle="modal" data-target="#at-preview" onclick="atLoadingPreView('claim');">미리보기</button>
                                </div>
                            </th>
                            <th class="col-md-4 col-sm-4 col-4">
                                <div>
                                    <label>입금발송</label>
                                    <div class="w-100"></div>
                                    <button type="button" class="btn btn-sm btn-gray-outlined" data-toggle="modal" data-target="#at-preview" onclick="atLoadingPreView('acc');">미리보기</button>
                                </div>
                            </th>
                            <th class="col-md-4 col-sm-4 col-4">
                                <div>
                                    <label>미납발송</label>
                                    <div class="w-100"></div>
                                    <button type="button" class="btn btn-sm btn-gray-outlined" data-toggle="modal" data-target="#at-preview" onclick="atLoadingPreView('nonpay');">미리보기</button>
                                </div>
                            </th>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="row mt-3 mb-5">
                <div class="col text-center">
                    <button type="button" class="btn btn-primary btn-wide" onclick="saveAt();" id="saveAt" style="display: none;">저장</button>
                </div>
            </div>
        </div>

        <div id="bill-setting" class="tab-pane fade">
            <div class="row">
                <div class="col">
                    <h5>고지서 설정</h5>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <form id="fileForm" name="fileForm" enctype="multipart/form-data">
                        <input type="hidden" id="imgExist" value="" name="imgExist">
                        <input type="hidden" name="chaCd" id="chaCd" value="${chaCd}">
                        <table class="table table-primary table-form mb-4">
                            <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">로고이미지</th>
                                <td class="col-md-10 col-sm-8 col-8 form-inline">
                                    <div class="mr-1 w-75">
                                        <input type="text" class="form-control w-100" id="fileName" disabled="disabled">
                                    </div>
                                    <label class="btn btn-sm btn-d-gray mb-0" for="uploadLogo">파일찾기</label>
                                    <input type="file" id="uploadLogo" name="uploadLogo" class="hidden" onchange="document.getElementById('fileName').value = this.value" accept=".jpg">
                                    <button type="button" class="btn btn-sm btn-d-gray" onclick="fileReset();">삭제</button>
                                    <div class="guide-mention w-100 mt-1">* 로고 사이즈 150px*30px, jpg만 지원</div>
                                    <div id="logoImageContainer" class="mt-1 mb-2"></div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table class="table table-primary table-form mb-4">
                            <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">안내명</th>
                                <td class="col-md-10 col-sm-8 col-8">
                                    <input type="text" class="form-control billname" id="billName1" name="billName1" value="${map.billConfig1.billName1}">
                                    <span class="textarea-byte-counter"><em>0</em>/40byte</span>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">
                                    <div>
                                        <label>
                                            안내문구1
                                        </label>
                                        <div class="w-100"></div>

                                    </div>
                                </th>
                                <td id="textarea-06" class="col-md-10 col-sm-8 col-8">
                                    <textarea class="form-control has-counter billcontcheck"  id="billCont1" name="billCont1" rows="6" >${map.billConfig1.billCont1}</textarea>
                                    <span class="textarea-byte-counter"><em>0</em>/1600byte</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table class="table table-primary table-form mb-4">
                            <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">안내명</th>
                                <td class="col-md-10 col-sm-8 col-8">
                                    <input type="text" class="form-control billname" id="billName2" name="billName2" value="${map.billConfig2.billName2}" >
                                    <span class="textarea-byte-counter"><em>0</em>/40byte</span>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">
                                    <div>
                                        <label>
                                            안내문구2
                                        </label>
                                        <div class="w-100"></div>

                                    </div>
                                </th>
                                <td id="textarea-07" class="col-md-10 col-sm-8 col-8">
                                    <textarea class="form-control has-counter billcontcheck"  id="billCont2" name="billCont2" rows="6">${map.billConfig2.billCont2}</textarea>
                                    <span class="textarea-byte-counter"><em>0</em>/1600byte</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table class="table table-primary table-form mb-4">
                            <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">안내명</th>
                                <td class="col-md-10 col-sm-8 col-8">
                                    <input type="text" class="form-control billname" id="billName3" name="billName3" value="${map.billConfig3.billName3}" >
                                    <span class="textarea-byte-counter"><em>0</em>/40byte</span>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">
                                    <div>
                                        <label>
                                            안내문구3
                                        </label>
                                        <div class="w-100"></div>

                                    </div>
                                </th>
                                <td id="textarea-08" class="col-md-10 col-sm-8 col-8">
                                    <textarea class="form-control has-counter billcontcheck" id="billCont3" name="billCont3" rows="6">${map.billConfig3.billCont3}</textarea>
                                    <span class="textarea-byte-counter"><em>0</em>/1600byte</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
            <div class="row mt-3 mb-5">
                <div class="col text-center">
                    <input type="hidden" value="Y" id="lineAlert"/>
                    <button type="button" class="btn btn-primary btn-wide" onclick="saveBillForm();">저장</button>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="quick-instruction" class="foldable-box">
            <div class="row foldable-box-header">
                <div class="col-8">
                    <img src="/assets/imgs/common/icon-notice.png">
                    알려드립니다.
                </div>
                <div class="col-4 text-right">
                    <img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
                </div>
            </div>
            <div class="row foldable-box-body">
                <div class="col">
                    <span class="cashDct">
                        <h6>■ 문자메시지 사용 등록 방법</h6>
                        <ul>
                            <li>문자메시지 신청 버튼을 클릭하여 통신서비스 이용증명원 파일과 발신번호를 입력 후 등록</li>
                        </ul>
                        <h6>■ 청구/입금 시 문자메시지 발송</h6>
                        <ul>
                            <li>청구 또는 입금 건 발생 시, 자동으로 문자 메시지를 발송합니다.</li>
                            <li class="depth-2 text-danger">※ 문자메시지 발송 시,  발송 건당 이용료가 발생되니 이점 유의하시기 바랍니다.</li>
                        </ul>
                        <h6>■ 문자메시지 문구 설정</h6>
                        <ul>
                            <li>문자메시지 발송을 위한 기본 문구를 사전 등록하는 절차</li>
                            <li>최대 5개까지 설정 가능하며, 문자고지 발송시 문구 선택 가능</li>
                        </ul>

                        <h6>■ 입력값 선택</h6>
                        <ul>
                            <li>삽입을 원하는 문구 사이에 마우스 커서를 위치하여, 입력을 원하는 변수 입력값 버튼을 클릭하여 추가</li>
                            <li class="depth-2 text-danger">※ 입력값이란? : 문자메시지를 받는 개별 고객의 선택된 정보가 자동 입력되어 발송됩니다.</li>
                        </ul>
                    </span>

                    <span class="atDct" style="display: none;">
                        <h6>■ 알림톡 안내</h6>
                        <ul>
                            <li>카카오톡 알림톡은 정보성 메시지에 한해 발송이 가능합니다.</li>
                            <li>- 정보성 메시지란 ? 정보통신망법 안내서에 '영리목적 광고성 정보의 예외'에 해당하는 메시지</li>
                            <li>스팸방지 정보통신망법 안내서 확인하기 https://goo.gl/gRjeVa</li>
                            <li>스팸방지 정보통신망법 Q&A 확인하기 https://goo.gl/j54RQF</li>
                            <li>청구/미납/입금시 알림톡 발송만 가능하며, 사전 등록된 템플릿 기반으로 전고객 동일한 문구로 발송됩니다.</li>
                            <li>발송실패시 SMS, LMS등 타 발송수단으로 재전송되지 않습니다.</li>
                        </ul>

                        <h6>■ 알림톡 사용 등록 방법</h6>
                        <ul>
                            <li>알림톡 이용신청 버튼을 클릭하여 신청</li>
                        </ul>

                        <h6>■ 청구/입금 시 알림톡 발송</h6>
                        <ul>
                            <li>청구 또는 입금 건 발생 시, 자동/수동으로 알림톡을 발송합니다.</li>
                            <li class="depth-2 text-danger">※ 알림톡 발송 시, 발송 건당 이용료가 발생되니 이점 유의하시기 바랍니다.</li>
                        </ul>
                    </span>

                    <span class="refundDct" style="display: none;">
                        <h6>■ 고지서 설정</h6>
                        <ul>
                            <li>로고 : 로고 사이즈는 150px * 30px 이며  JPG 만 지원</li>
                            <li>안내명 : 안내문구의 명칭을 지정</li>
                            <li>안내문 : 출력 고지서 및 이메일 고지서 발송을 위한 안내 문구를 사전 설정.</li>
                            <li class="depth-2 text-danger">※ 각 안내문은 개당 최대 10줄까지 등록할 수 있습니다.</li>
                        </ul>
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<%-- sms 미리보기 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/sms-preview-popup.jsp" flush="false"/>

<%-- 문자메시지 서비스 이용등록 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-sms-service.jsp" flush="false" />

<%-- 알림톡 서비스 이용등록 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-at-service.jsp" flush="false"/>

<%-- 알림톡 미리보기 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/at-preview-popup.jsp" flush="false"/>

<script>
    function countBytes(s, id) {
        var b = 0, i = 0, c
        for (; c = s.charCodeAt(i++); b += c >> 11 ? 2 : c >> 7 ? 2 : 1)
            ;
        $("#" + id + "").parent().children().children().html(b);
        return b
    }

    function limitCharacters(id) {
        var cutstr = '';
        var limit = 1600;
        var maxLine = 10;
        var lineMaxLength = 80;
        var lineMaxByte = 160;

        var text = $("#" + id + "").val();
        var line = $("#" + id + "").val().split("\n").length;
        var lines = $("#" + id + "").val().split("\n");
        var valueCheck = true;
        var _byte = 0;
        var billcon = '';
        switch (id) {
            case 'billCont1':
                billcon = '안내문1';
                break;
            case 'billCont2':
                billcon = '안내문2';
                break;
            case 'billCont3':
                billcon = '안내문3';
                break;
            default :
                billcon = '';
        }

        if (line > maxLine) {
            swal({
                type: 'warning',
                text: '안내문구는 ' + maxLine + '줄까지만 입력가능합니다.' + '\n' + billcon + ' 을(를) 수정해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            modifiedText = $("#" + id + "").val().split("\n").slice(0, maxLine);
            $("#" + id + "").val(modifiedText.join("\n"));
            valueCheck = false;
            return false;
        }

        var k = 0;
        var tooLong = false;
        for (var i = 0; i < lines.length; i++) {
            if (lines[i].length > lineMaxLength) {
                tooLong = true;
                k = i + 1;
            }
        }
        if (tooLong == true) {
            swal({
                type: 'warning',
                text: '한줄에 ' + lineMaxLength + '자 이하 최대 10줄, 최대 800자 까지만 작성할 수 있습니다. ' + '\n' + billcon + ' 의 ' + k + ' 행을 수정해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            valueCheck = false;
            return false;
        }
        return valueCheck;
    }

    var firstDepth = "nav-link-35";
    var secondDepth = "sub-03";
    var focusSmsArea = "notSms";	// SMS고지설정 focus 위치 알기 위해 사용
    var focusAtArea = "notAt";
    var focusGuideArea = "billCont1";

    function smsFocus(num) {
        if (num == 1) {
            focusSmsArea = "notSms";
        } else if (num == 2) {
            focusSmsArea = "rcpSms";
        } else if (num == 3) {
            focusSmsArea = "notSms2";
        } else if (num == 4) {
            focusSmsArea = "smsMsgDef1";
        } else {
            focusSmsArea = "smsMsgDef2";
        }
    }

    function guideFocus(num) {
        if (num == 1) {
            focusGuideArea = "billCont1";
        } else if (num == 2) {
            focusGuideArea = "billCont2";
        } else if (num == 3) {
            focusGuideArea = "billCont3";
        } else if (num == 11) {
            focusGuideArea = "remark1";
        } else if (num == 22) {
            focusGuideArea = "remark2";
        } else if (num == 33) {
            focusGuideArea = "remark3";
        }
    }

    function mergeAdd(num) {

        document.all(focusSmsArea).focus();

        var element = document.getElementById(focusSmsArea);
        var strOriginal = element.value;
        var iStartPos = element.selectionStart;

        var iEndPos = element.selectionEnd;
        var strFront = "";
        var strEnd = "";

        if (iStartPos == iEndPos) {
            strFront = strOriginal.substring(0, iStartPos);
            strEnd = strOriginal.substring(iStartPos, strOriginal.length);
            element.value = strFront + document.getElementById("add" + num).value + strEnd;
        } else {
            strOriginal = strOriginal.replace(strOriginal.substring(iStartPos, iEndPos), document.getElementById("add" + num).value);
            element.value = strOriginal;
        }

    }

    function smsLoadingPreView() {

        var element = document.getElementById(focusSmsArea);
        var ls_str = document.all(focusSmsArea).value; // 이벤트가 일어난 컨트롤의 value 값

        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();

        var cusName = "[홍길동]";
        var masMonth = '[' + yyyy + '년 ' + mm + '월]'
        var payItemAmt = "[50,000]";
        var depositTime = '[' + yyyy + '년 ' + (mm + 1) + '월 ' + dd + '일]';
        var smsNoti = "[https://www.shinhandamoa.com/common/login#payer]";
        var payVano = "[신한 156-345-235535]";
        var defaultAmt = "[50,000]";
        var notiEndDate = '[' + yyyy + '년 ' + (mm + 1) + '월 ' + dd + '일]';
        var cusGbn = "[구분1 구분2 구분3 구분4]";

        ls_str = ls_str.split("[#고객명#]").join(cusName);
        ls_str = ls_str.split("[$납부자명]").join(cusName);
        ls_str = ls_str.split("[$NAME]").join(cusName);
        ls_str = ls_str.split("[#청구월#]").join(masMonth);
        ls_str = ls_str.split("[$청구월]").join(masMonth);
        ls_str = ls_str.split("[$PMONTH]").join(masMonth);
        ls_str = ls_str.split("[#청구액#]").join(payItemAmt);
        ls_str = ls_str.split("[$청구금액]").join(payItemAmt);
        ls_str = ls_str.split("[$AMT]").join(payItemAmt);
        ls_str = ls_str.split("[#납부기한#]").join(depositTime);
        ls_str = ls_str.split("[#모바일청구서#]").join(smsNoti);
        ls_str = ls_str.split("[#납부계좌#]").join(payVano);
        ls_str = ls_str.split("[$수납계좌번호]").join(payVano);
        ls_str = ls_str.split("[$ACCNO]").join(payVano);
        ls_str = ls_str.split("[#미납액#]").join(defaultAmt);
        ls_str = ls_str.split("[#고지서용마감일#]").join(notiEndDate);
        ls_str = ls_str.split("[$수납마감일]").join(notiEndDate);
        ls_str = ls_str.split("[$LDATE]").join(notiEndDate);
        ls_str = ls_str.split("[#고객구분#]").join(cusGbn);
        ls_str = basicEscape(ls_str);
        ls_str = ls_str.replace(/(?:\r\n|\r|\n)/g, '<br/>');

        $("#previewContents").html(ls_str);
        $('#sms-preview').modal("show");
    }

    function guideLoadingPreView() {
        var element = document.getElementById(focusGuideArea);
        var ls_str = document.all(focusGuideArea).value; // 이벤트가 일어난 컨트롤의 value 값

        ls_str = ls_str.replace(/(?:\r\n|\r|\n)/g, '<br/>');

        $("#previewContents").html(ls_str);
        $('#sms-preview').modal("show");
    }

    //sms저장
    function saveSms() {
        var url = "/org/myPage/updateSmsNoti";

        if ($('input[type=radio][name=notiSmsSend]:checked').val() == null) {
            swal({
                type: 'info',
                text: '청구 시 발송여부를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        } else if ($('input[type=radio][name=depositSmsSend]:checked').val() == null) {
            swal({
                type: 'info',
                text: '입금 시 발송여부를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        $('#notSms').val(basicEscape($('#notSms').val()));
        $('#rcpSms').val(basicEscape($('#rcpSms').val()));
        $('#notSms2').val(basicEscape($('#notSms2').val()));
        $('#smsMsgDef1').val(basicEscape($('#smsMsgDef1').val()));
        $('#smsMsgDef2').val(basicEscape($('#smsMsgDef2').val()));

        var param = {
            chaCd: $('#chaCd').val(),
            useSmsYn: $('input[type=hidden][name=useSmsYn]').val(),
            notSmsYn: $('input[type=radio][name=notiSmsSend]:checked').val(),
            rcpSmsTy: $('input[type=radio][name=depositSmsSend]:checked').val(),
            notSms: $('#notSms').val(),
            rcpSms: $('#rcpSms').val(),
            notSms2: $('#notSms2').val(),
            smsMsgDef1: $('#smsMsgDef1').val(),
            smsMsgDef2: $('#smsMsgDef2').val()
        };

        $.ajax({
            type: "post",
            url: url,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                swal({
                    type: 'success',
                    text: '문자메시지설정 내용이 저장되었습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function (result) {
                    if (result.value) {
                        document.location.href = "/org/myPage/notiConfig?chaCd=" +${chaCd};
                    }
                });
            },
            error: function (data) {
                swal({
                    type: 'error',
                    text: '문자메시지설정 내용 저장을 실패하였습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
            }
        });
    }

    //sms 고지
    function fn_smsNoti() {

        var url = "/org/notiMgmt/selSmsUseYn";
        var param = {};

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.map == 'Y') {			// sms 고지
                    swal({
                        type: 'info',
                        text: '이미 이용중인 서비스 입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else if (data.map == 'N') {	// 문자메시지 서비스 이용등록
                    fn_smsServiceInsPage();
                } else if (data.map == 'W') {	// 문자메시지 서비스 이용등록 신청중
                    swal({
                        type: 'info',
                        text: '문자메시지 서비스 이용등록 신청중입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else if (data.map == 'C') {
                    swal({
                        type: 'info',
                        text: '문자메시지 서비스 이용 취소상태입니다. 재사용은 관리자에게 문의바랍니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });
    }

    //파일업로드 시 파일명만 노출
    function setFileName(val) {
        var pathHeader = val.lastIndexOf("\\");
        var pathMiddle = val.lastIndexOf(".");
        var pathEnd = val.length;

        var fileName = val.substring(pathHeader + 1, pathMiddle);	// 순수 파일명만(확장자 제외)
        var extName = val.substring(pathMiddle + 1, pathEnd);	// 확장자
        var allFileName = fileName + "." + extName;	// 확장자 포함 파일명

        $("#fileName").val(allFileName);
    }

    // 고지서설정
    function saveBillForm() {
        var formData = new FormData($("#fileForm")[0]);
        var fileName = $("#fileName").val();
        $('#billName1').val(basicEscape($('#billName1').val()));
        $('#billName2').val(basicEscape($('#billName2').val()));
        $('#billName3').val(basicEscape($('#billName3').val()));
        $('#billCont1').val(basicEscape($('#billCont1').val()));
        $('#billCont2').val(basicEscape($('#billCont2').val()));
        $('#billCont3').val(basicEscape($('#billCont3').val()));

        if (limitCharacters('billCont1') == false) {
            return false;
        } else if (limitCharacters('billCont2') == false) {
            return false;
        } else if (limitCharacters('billCont3') == false) {
            return false;
        } else if (logoCheckSize == "false") {
            swal({
                type: 'error',
                text: '로고 이미지는 150px * 30px 사이즈 jpg파일만 업로드 할 수 있습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if (fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length) != 'jpg' && fileName != "") {
            swal({
                type: 'error',
                text: '로고 이미지는 jpg파일만 업로드 할 수 있습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else {
            swal({
                type: 'question',
                html: " 저장하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        type: "POST",
                        url: "/org/myPage/saveBillForm",
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (result) {
                            swal({
                                type: 'success',
                                text: '고지서 설정 내용이 저장되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    document.location.href = "/org/myPage/notiConfig?chaCd=" +${chaCd};
                                }
                            });
                        },
                        error: function (result) {
                            swal({
                                type: 'error',
                                text: '고지서 설정 내용 저장을 실패하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            })
                        }
                    });
                }
            });
        }//else
    }

    function fileReset() {
        swal({
            type: 'question',
            html: "로고 이미지를 삭제하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                if (navigator.userAgent.toLowerCase().indexOf("msie") != -1) {
                    $('#fileName').val('');
                    $("#uploadLogo").replaceWith($("#uploadLogo").clone(true));  // ie 일때 초기화
                } else {
                    $('#fileName').val('');
                    $('#uploadLogo').val('');
                }

                $("#imgExist").val("N");
            }
        });
    }

    //로고 미리보기
    function logoView() {

        $('#logoImageContainer').empty();

        $.ajax({
            dataType: "html",
            url: "/common/base64Images?fileTy=logo&fileName=" + $('#chaCd').val(),
            success: function (data) {
                var imageString = "data:image/jpeg;base64,";
                var img = document.createElement('img'); // 이미지 객체 생성
                img.src = imageString + data;
                if (data == "") {
                } else {
                    document.getElementById('logoImageContainer').appendChild(img); // 이미지 동적 추가
                    $(img).css({"margin": "0 auto", "cursor": "pointer", "max-width": "150px", "max-height": "30px"});
                }
            }
        });
    }

    // 알림톡 신청
    function fn_atNoti() {
        fn_reset_scroll();

        var url = "/org/notiMgmt/selAtUseYn";
        var param = {};

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.map == 'Y') {
                    swal({
                        type: 'info',
                        text: '이미 이용중인 서비스 입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else if (data.map == 'N') {
                    fn_atModalOpen();
                } else if (data.map == 'W') {
                    swal({
                        type: 'info',
                        html: '알림톡 서비스 이용등록 신청중입니다.<p>승인 완료 후 담당자 연락처로 연락드리겠습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else if (data.map == 'C') {
                    swal({
                        type: 'info',
                        text: '알림톡 서비스 이용 취소상태입니다. 재사용은 관리자에게 문의바랍니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else if (data.map == 'D') {
                    fn_atModalOpen();
                }
            }
        });
    }

    function atLoadingPreView(type) {
        var yyyyMmDd = moment(new Date(), 'YYYYMMDD').format('YYYY년 MM월 DD일');
        var yyyyMm = moment(new Date(), 'YYYYMMDD').format('YYYY년 MM월');
        var chaName = '${map.atConfig.chaName}';
        var conTelNo = '${map.atConfig.conTelNo}';

        $("#previewAtmsg").html('');

        switch (type) {
            case 'claim' :
                msgTxt = "[신한 다모아]<br>"
                    + chaName + " 납부고지서 입니다.<br><br>"
                    + "고객명 : 홍길동<br>"
                    + "청구월 : " + yyyyMm + "<br>"
                    + "청구금액 : 50,000<br>"
                    + "납부계좌 : 신한 156345235535<br>"
                    + "납부마감일 : " + yyyyMmDd + "<br>"
                    + "기관전화번호 : 02-786-8201<br><br>"
                    + "※ 은행에 가셔서 직접 납부는 물론 인터넷뱅킹, 텔레뱅킹, 자동화기기(CD,ATM) 등 모든 방식으로 납부가 가능하며, 별도의 의뢰인 이름을 입력할 필요없이 위의 신한은행 입금계좌로 입금하시면 됩니다.<br>"
                    + "※ 신한 다모아 홈페이지 안내(모바일가능)<br>"
                    + "http://www.shinhandamoa.com";
                $("#previewAtmsg").html(msgTxt);
                break;
            case 'acc' :
                msgTxt = "[신한 다모아]<br>"
                    + chaName + " 입금이 확인 되었습니다.<br><br>"
                    + "고객명 : 홍길동<br>"
                    + "입금금액 : 50,000<br>"
                    + "납부계좌 : 신한 156345235535<br>"
                    + "기관전화번호 : 02-786-8201<br><br>"
                    + "※ 신한 다모아 홈페이지 안내(모바일가능)<br>"
                    + "http://www.shinhandamoa.com";
                $("#previewAtmsg").html(msgTxt);
                break;
            case 'nonpay' :
                msgTxt = "[신한 다모아]<br>"
                    + chaName + " 미납 안내 드립니다.<br>"
                    + "아래의 내용을 확인하시고, 기간내 납부하여 주시기 바랍니다.<br><br>"
                    + "고객명 : 홍길동<br>"
                    + "청구월 : " + yyyyMm + "<br>"
                    + "미납금액 : 50,000<br>"
                    + "납부계좌 : 신한 156345235535<br>"
                    + "납부마감일 : " + yyyyMmDd + "<br>"
                    + "기관전화번호 : 02-786-8201<br><br>"
                    + "※ 은행에 가셔서 직접 납부는 물론 인터넷뱅킹, 텔레뱅킹, 자동화기기(CD,ATM) 등 모든 방식으로 납부가 가능하며, 별도의 의뢰인 이름을 입력할 필요없이 위의 신한은행 입금계좌로 입금하시면 됩니다.<br>"
                    + "※ 신한 다모아 홈페이지 안내(모바일가능)<br>"
                    + "http://www.shinhandamoa.com";
                $("#previewAtmsg").html(msgTxt);
                break;
        }

        $('#at-preview').modal("show");
    }

    function saveAt() {
        var url = "/org/myPage/updateAtNoti";

        if ($('input[type=radio][name=notiAtSend]:checked').val() == null) {
            swal({
                type: 'info',
                text: '청구 시 발송여부를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        } else if ($('input[type=radio][name=depositAtSend]:checked').val() == null) {
            swal({
                type: 'info',
                text: '입금 시 발송여부를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        var param = {
            chaCd: $('#chaCd').val(),
            notAtYn: $('input[type=radio][name=notiAtSend]:checked').val(),
            rcpAtYn: $('input[type=radio][name=depositAtSend]:checked').val()
        };

        $.ajax({
            type: "post",
            url: url,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                swal({
                    type: 'success',
                    text: '알림톡 설정 내용이 저장되었습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function (result) {
                    if (result.value) {
                        document.location.href = "/org/myPage/notiConfig?chaCd=" +${chaCd};
                    }
                });
            },
            error: function (data) {
                swal({
                    type: 'error',
                    text: '알림톡 설정 내용 저장에 실패하였습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
            }
        });
    }
</script>
