<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<script>
    function onlyOne3() {
        if ($("#inlineCheckbox14-1").is(":checked")) {
            $("#inlineCheckbox14-2").prop("checked", false)
        }
    }

    function onlyOne4() {
        if ($("#inlineCheckbox14-2").is(":checked")) {
            $("#inlineCheckbox14-1").prop("checked", false)
        }
    }

    function sendEmail() {
        var option1 = "0";
        var option2 = "0";
        var option3 = "0";

        if ($("#inlineCheckbox13-1").is(":checked")) {
            option1 = "0"; //전체
        }
        if ($("#inlineCheckbox13-2").is(":checked")
            && $("#inlineCheckbox13-3").is(":checked") == false
            && $("#inlineCheckbox13-4").is(":checked") == false) {
            option1 = "1"; // 정상이용기관
        }
        if ($("#inlineCheckbox13-3").is(":checked")
            && $("#inlineCheckbox13-2").is(":checked") == false
            && $("#inlineCheckbox13-4").is(":checked") == false) {
            option1 = "2"; // 해지기관

        }
        if ($("#inlineCheckbox13-4").is(":checked")
            && $("#inlineCheckbox13-2").is(":checked") == false
            && $("#inlineCheckbox13-3").is(":checked") == false) {
            option1 = "3"; // 정지기관
        }
        if ($("#inlineCheckbox13-2").is(":checked")
            && $("#inlineCheckbox13-3").is(":checked")
            && $("#inlineCheckbox13-4").is(":checked") == false) {
            option1 = "4"; // 정상+해지
        }
        if ($("#inlineCheckbox13-2").is(":checked")
            && $("#inlineCheckbox13-4").is(":checked")
            && $("#inlineCheckbox13-3").is(":checked") == false) {
            option1 = "5"; // 정상+정지
        }
        if ($("#inlineCheckbox13-3").is(":checked")
            && $("#inlineCheckbox13-4").is(":checked")
            && $("#inlineCheckbox13-2").is(":checked") == false) {
            option1 = "6"; // 해지+정지
        }

        if ($("#inlineCheckbox14-1").is(":checked")) {
            option2 = "1"; // 3개월이내
        }
        if ($("#inlineCheckbox14-2").is(":checked")) {
            option2 = "2"; // 6개월이내
        }

        if ($("#inlineCheckbox15-1").is(":checked")) {
            option3 = "0"; // 전체
        }
        if ($("#inlineCheckbox15-2").is(":checked")
            && $("#inlineCheckbox15-3").is(":checked") == false) {
            option3 = "1"; // web
        }
        if ($("#inlineCheckbox15-3").is(":checked")
            && $("#inlineCheckbox15-2").is(":checked") == false) {
            option3 = "2"; // api
        }

        var etitle = $('#etitle').val();
        var emsg = $('#emailMsg').val();
        if (etitle == '' || etitle == null) {
            swal({
                type: 'info',
                text: '제목을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if (emsg == '' || emsg == null) {
            swal({
                type: 'info',
                text: '내용을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if ($("input[type=checkbox]:checked").length == 0) {
            swal({
                type: 'info',
                text: '발송조건을 선택해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else {
            var param = {
                emailTitle: etitle,
                emailMsg: emsg,
                userStatus: option1,
                userDate: option2,
                userClass: option3,
                id: $('#loginUId').val()
            };
            $.ajax({
                type: "post",
                async: true,
                url: "/sys/sysEmailMsgSend",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (data) {
                    swal({
                        type: 'success',
                        text: "메시지를 전송하였습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    $("#popup-send-email-without-list").modal("hide");
                    list2(1);
                },
                error: function (data) {
                    swal({
                        type: 'error',
                        text: "메시지 전송 실패하였습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            });
        }

    };
</script>
<s:authentication property="principal.loginId" var="loginId"/>

<div class="inmodal modal fade" id="popup-send-email-without-list" tabindex="-1" role="dialog"
     aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">E-MAIL 공지 발송</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="row m-b-sm">
                                <label class="col-sm-12" id="emailCounting">수신대상(총 x개 기관)</label>
                            </div>
                            <div class="row m-b-sm">
                                <label class="col-sm-12">기관상태별</label>
                                <div class="col-sm-12 form-inline">
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox13-1" value="">
                                        <label for="inlineCheckbox13-1"> 전체기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox13-2" value="ST06" name="inlineCheckbox13">
                                        <label for="inlineCheckbox13-2"> 정상이용기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox13-3" value="ST02" name="inlineCheckbox13">
                                        <label for="inlineCheckbox13-3"> 해지기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox13-4" value="ST08" name="inlineCheckbox13">
                                        <label for="inlineCheckbox13-4"> 정지기관 </label>
                                    </div>
                                </div>
                            </div>
                            <div class="row m-b-sm">
                                <label class="col-sm-12">이용상태별</label>
                                <div class="col-sm-12 form-inline">
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox14-1" value="withinthree" onclick="onlyOne3()" name="smsCb">
                                        <label for="inlineCheckbox14-1"> 최근 3개월 내 사용기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox14-2" value="withinsix" onclick="onlyOne4()" name="smsCb">
                                        <label for="inlineCheckbox14-2"> 최근 6개월 내 사용기관 </label>
                                    </div>
                                </div>
                            </div>
                            <div class="row m-b-sm">
                                <label class="col-sm-12">고객분류별</label>
                                <div class="col-sm-12 form-inline">
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox15-1" value="">
                                        <label for="inlineCheckbox15-1"> 전체기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox15-2" value="01" name="inlineCheckbox15">
                                        <label for="inlineCheckbox15-2"> WEB </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox15-3" value="03" name="inlineCheckbox15">
                                        <label for="inlineCheckbox15-3"> API </label>
                                    </div>
                                </div>
                            </div>
                            <div class="row m-b-sm">
                                <label class="col-sm-12">제목</label>
                                <div class="col-sm-12"><input class="form-control" id="etitle"></div>
                            </div>
                            <div class="row">
                                <label class="col-sm-12">내용</label>
                                <div class="col-sm-12">
                                    <textarea class="form-control" rows="15" id="emailMsg" style="resize:none;"></textarea>
                                    <p id="mByte">0/2000 byte</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outline" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" onclick="sendEmail()">발송</button>
                <input type="hidden" value="${loginId }" id="loginUId">
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        //바이트 제한
        $("#emailMsg").on("keyup", function () {
            var str = $("#emailMsg").val();
            var tbyte = 0;
            var strlength = 0;
            var charStr = '';
            var maxlength = 2000;
            var cutstr = '';

            for (var i = 0; i < str.length; i++) {
                //한글체크
                charStr = str.charAt(i);
                if (escape(charStr).length > 4) {
                    tbyte += 2;
                } else {
                    tbyte++;
                }
                //길면 자르기
                if (tbyte <= maxlength) {
                    strlength = i + 1;
                }
            }
            if (tbyte > maxlength) {
                cutstr = $("#emailMsg").val().substr(str, strlength);
                $("#emailMsg").val(cutstr);
                $("#mByte").html(maxlength + "/2000 byte");
                return;
            } else {
                $("#mByte").html(tbyte + "/2000 byte");
            }
        });

    }); //doc ready
    //체크박스로 건수 조회
    $(document).on("change", "input[type=checkbox]", function () {
        $(document).on("change", "input[id='inlineCheckbox13-1']", function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#inlineCheckbox13-1").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name='inlineCheckbox13']").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name='inlineCheckbox13']").prop("checked", false);
            }
            counting();
        });
        $(document).on("change", "input[name='inlineCheckbox13']", function () {
            $("#inlineCheckbox13-1").prop("checked", false);
            counting();
        });
        $(document).on("change", "input[id='inlineCheckbox15-1']", function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#inlineCheckbox15-1").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name='inlineCheckbox15']").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name='inlineCheckbox15']").prop("checked", false);
            }
            counting();
        });
        $(document).on("change", "input[name='inlineCheckbox15']", function () {
            $("#inlineCheckbox15-1").prop("checked", false);
            counting();
        });
        var option1 = "0";
        var option2 = "0";
        var option3 = "0";

        if ($("#inlineCheckbox13-1").is(":checked")) {
            option1 = "0"; //전체
        }
        if ($("#inlineCheckbox13-2").is(":checked")
            && $("#inlineCheckbox13-3").is(":checked") == false
            && $("#inlineCheckbox13-4").is(":checked") == false) {
            option1 = "1"; // 정상이용기관
        }
        if ($("#inlineCheckbox13-3").is(":checked")
            && $("#inlineCheckbox13-2").is(":checked") == false
            && $("#inlineCheckbox13-4").is(":checked") == false) {
            option1 = "2"; // 해지기관

        }
        if ($("#inlineCheckbox13-4").is(":checked")
            && $("#inlineCheckbox13-2").is(":checked") == false
            && $("#inlineCheckbox13-3").is(":checked") == false) {
            option1 = "3"; // 정지기관
        }
        if ($("#inlineCheckbox13-2").is(":checked")
            && $("#inlineCheckbox13-3").is(":checked")
            && $("#inlineCheckbox13-4").is(":checked") == false) {
            option1 = "4"; // 정상+해지
        }
        if ($("#inlineCheckbox13-2").is(":checked")
            && $("#inlineCheckbox13-4").is(":checked")
            && $("#inlineCheckbox13-3").is(":checked") == false) {
            option1 = "5"; // 정상+정지
        }
        if ($("#inlineCheckbox13-3").is(":checked")
            && $("#inlineCheckbox13-4").is(":checked")
            && $("#inlineCheckbox13-2").is(":checked") == false) {
            option1 = "6"; // 해지+정지
        }

        if ($("#inlineCheckbox14-1").is(":checked")) {
            option2 = "1"; // 3개월이내
        }
        if ($("#inlineCheckbox14-2").is(":checked")) {
            option2 = "2"; // 6개월이내
        }

        if ($("#inlineCheckbox15-1").is(":checked")) {
            option3 = "0"; // 전체
        }
        if ($("#inlineCheckbox15-2").is(":checked")
            && $("#inlineCheckbox15-3").is(":checked") == false) {
            option3 = "1"; // web
        }
        if ($("#inlineCheckbox15-3").is(":checked")
            && $("#inlineCheckbox15-2").is(":checked") == false) {
            option3 = "2"; // api
        }

        var param = {
            userStatus: option1,
            userDate: option2,
            userClass: option3
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/sysSmsCustCount",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    $("#emailCounting").html(
                        "수신대상(총" + result.snDTOCount + " 개 기관)");
                } else {
                    swal({
                        type: 'success',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });

    }); //on
</script>
