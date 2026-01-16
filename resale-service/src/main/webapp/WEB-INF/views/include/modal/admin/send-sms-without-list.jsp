<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<script>
    function allClick() {
        if ($("#inlineCheckbox10-1").is(":checked")) {
            $("#inlineCheckbox10-2").prop("checked", false)
            $("#inlineCheckbox10-3").prop("checked", false)
            $("#inlineCheckbox10-4").prop("checked", false)
        }
    }

    function notAll() {
        $("#inlineCheckbox10-1").prop("checked", false)
    }

    function onlyOne() {
        if ($("#inlineCheckbox11-1").is(":checked")) {
            $("#inlineCheckbox11-2").prop("checked", false)
        }
    }

    function onlyOne2() {
        if ($("#inlineCheckbox11-2").is(":checked")) {
            $("#inlineCheckbox11-1").prop("checked", false)
        }
    }

    function allClick2() {
        if ($("#inlineCheckbox12-1").is(":checked")) {
            $("#inlineCheckbox12-2").prop("checked", false)
            $("#inlineCheckbox12-3").prop("checked", false)
        }
    }

    function notAll2() {
        $("#inlineCheckbox12-1").prop("checked", false)
    }

    function sendSms() {

        var option1 = "0";
        var option2 = "0";
        var option3 = "0";

        if ($("#inlineCheckbox10-1").is(":checked")) {
            option1 = "0"; //전체
        }
        if ($("#inlineCheckbox10-2").is(":checked") && $("#inlineCheckbox10-3").is(":checked") == false && $("#inlineCheckbox10-4").is(":checked") == false) {
            option1 = "1"; // 정상이용기관
        }
        if ($("#inlineCheckbox10-3").is(":checked") && $("#inlineCheckbox10-2").is(":checked") == false && $("#inlineCheckbox10-4").is(":checked") == false) {
            option1 = "2"; // 해지기관
        }
        if ($("#inlineCheckbox10-4").is(":checked") && $("#inlineCheckbox10-2").is(":checked") == false && $("#inlineCheckbox10-3").is(":checked") == false) {
            option1 = "3"; // 정지기관
        }
        if ($("#inlineCheckbox10-2").is(":checked") && $("#inlineCheckbox10-3").is(":checked") && $("#inlineCheckbox10-4").is(":checked") == false) {
            option1 = "4"; // 정상+해지
        }
        if ($("#inlineCheckbox10-2").is(":checked") && $("#inlineCheckbox10-4").is(":checked") && $("#inlineCheckbox10-3").is(":checked") == false) {
            option1 = "5"; // 정상+정지
        }
        if ($("#inlineCheckbox10-3").is(":checked") && $("#inlineCheckbox10-4").is(":checked") && $("#inlineCheckbox10-2").is(":checked") == false) {
            option1 = "6"; // 해지+정지
        }

        if ($("#inlineCheckbox11-1").is(":checked")) {
            option2 = "1"; // 3개월이내
        }
        if ($("#inlineCheckbox11-2").is(":checked")) {
            option2 = "2"; // 6개월이내
        }

        if ($("#inlineCheckbox12-1").is(":checked")) {
            option3 = "0"; // 전체
        }
        if ($("#inlineCheckbox12-2").is(":checked") && $("#inlineCheckbox11-3").is(":checked") == false) {
            option3 = "1"; // web
        }
        if ($("#inlineCheckbox12-3").is(":checked") && $("#inlineCheckbox11-2").is(":checked") == false) {
            option3 = "2"; // api
        }

        var stitle = $('#title').val();
        var smsg = $('#smsMsg').val();
        if (stitle == '' || stitle == null) {
            swal({
                type: 'info',
                text: '제목을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if (smsg == '' || smsg == null) {
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
                title: stitle,
                smsMsg: smsg,
                userStatus: option1,
                userDate: option2,
                userClass: option3,
                id: $('#loginUId').val()
            };
            $.ajax({
                type: "post",
                async: true,
                url: "/sys/sysSmsMsgSend",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (data) {
                    swal({
                        type: 'success',
                        text: "메시지를 전송하였습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    $("#popup-send-sms-without-list").modal("hide");
                    list(1);
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

<div class="inmodal modal fade" id="popup-send-sms-without-list" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">문자 공지 발송</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="row m-b-sm">
                                <label class="col-sm-12" id="smsCounting">수신대상(총 0개 기관)</label>
                            </div>
                            <div class="row m-b-sm">
                                <label class="col-sm-12">기관상태별</label>
                                <div class="col-sm-12 form-inline">
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox10-1" value="all" onclick="allClick()" name="smsCb">
                                        <label for="inlineCheckbox10-1"> 전체기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox10-2" value="activecust" onclick="notAll()" name="smsCb">
                                        <label for="inlineCheckbox10-2"> 정상이용기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox10-3" value="canceledcust" onclick="notAll()" name="smsCb">
                                        <label for="inlineCheckbox10-3"> 해지기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox10-4" value="stoppedcust" onclick="notAll()" name="smsCb">
                                        <label for="inlineCheckbox10-4"> 정지기관 </label>
                                    </div>
                                </div>
                            </div>
                            <div class="row m-b-sm">
                                <label class="col-sm-12">이용상태별</label>
                                <div class="col-sm-12 form-inline">
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox11-1" value="withinthree" onclick="onlyOne()" name="smsCb">
                                        <label for="inlineCheckbox11-1"> 최근 3개월 내 사용기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox11-2" value="withinsix" onclick="onlyOne2()" name="smsCb">
                                        <label for="inlineCheckbox11-2"> 최근 6개월 내 사용기관 </label>
                                    </div>
                                </div>
                            </div>
                            <div class="row m-b-sm">
                                <label class="col-sm-12">고객분류별</label>
                                <div class="col-sm-12 form-inline">
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox12-1" value="allcust" onclick="allClick2()" name="smsCb">
                                        <label for="inlineCheckbox12-1"> 전체기관 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox12-2" value="webcust" onclick="notAll2()" name="smsCb">
                                        <label for="inlineCheckbox12-2"> WEB </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox12-3" value="apicust" onclick="notAll2()" name="smsCb">
                                        <label for="inlineCheckbox12-3"> API </label>
                                    </div>
                                </div>
                            </div>
                            <div class="row m-b-sm">
                                <label class="col-sm-12">제목</label>
                                <div class="col-sm-12"><input class="form-control" id="title" value=""></div>
                            </div>
                            <div class="row">
                                <label class="col-sm-12">내용</label>
                                <div class="col-sm-12">
                                    <textarea class="form-control " rows="15" id="smsMsg" style="resize:none;"></textarea>
                                    <p id="messagebyte">0/1000 byte</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outline" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" onclick="sendSms()">발송</button>
                <input type="hidden" value="${loginId }" id="loginUId">
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        //sms용 1000바이트 제한
        $("#smsMsg").on("keyup", function () {
            var str = $("#smsMsg").val();
            var tbyte = 0;
            var strlength = 0;
            var charStr = '';
            var maxlength = 1000;
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
                cutstr = $("#smsMsg").val().substr(str, strlength);
                $("#smsMsg").val(cutstr);
                $("#messagebyte").html(maxlength + "/1000 byte");
                return;
            } else {
                $("#messagebyte").html(tbyte + "/1000 byte");
            }
        });


    }); //doc ready

    //체크박스로 건수 조회
    $(document).on("change", "input[name=smsCb]", function () {
        var option1 = "0";
        var option2 = "0";
        var option3 = "0";

        if ($("#inlineCheckbox10-1").is(":checked")) {
            option1 = "0"; //전체
        }
        if ($("#inlineCheckbox10-2").is(":checked")
            && $("#inlineCheckbox10-3").is(":checked") == false
            && $("#inlineCheckbox10-4").is(":checked") == false) {
            option1 = "1"; // 정상이용기관
        }
        if ($("#inlineCheckbox10-3").is(":checked")
            && $("#inlineCheckbox10-2").is(":checked") == false
            && $("#inlineCheckbox10-4").is(":checked") == false) {
            option1 = "2"; // 해지기관

        }
        if ($("#inlineCheckbox10-4").is(":checked")
            && $("#inlineCheckbox10-2").is(":checked") == false
            && $("#inlineCheckbox10-3").is(":checked") == false) {
            option1 = "3"; // 정지기관
        }
        if ($("#inlineCheckbox10-2").is(":checked")
            && $("#inlineCheckbox10-3").is(":checked")
            && $("#inlineCheckbox10-4").is(":checked") == false) {
            option1 = "4"; // 정상+해지
        }
        if ($("#inlineCheckbox10-2").is(":checked")
            && $("#inlineCheckbox10-4").is(":checked")
            && $("#inlineCheckbox10-3").is(":checked") == false) {
            option1 = "5"; // 정상+정지
        }
        if ($("#inlineCheckbox10-3").is(":checked")
            && $("#inlineCheckbox10-4").is(":checked")
            && $("#inlineCheckbox10-2").is(":checked") == false) {
            option1 = "6"; // 해지+정지
        }

        if ($("#inlineCheckbox11-1").is(":checked")) {
            option2 = "1"; // 3개월이내
        }
        if ($("#inlineCheckbox11-2").is(":checked")) {
            option2 = "2"; // 6개월이내
        }

        if ($("#inlineCheckbox12-1").is(":checked")) {
            option3 = "0"; // 전체
        }
        if ($("#inlineCheckbox12-2").is(":checked")
            && $("#inlineCheckbox12-3").is(":checked") == false) {
            option3 = "1"; // web
        }
        if ($("#inlineCheckbox12-3").is(":checked")
            && $("#inlineCheckbox12-2").is(":checked") == false) {
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
                    $("#smsCounting").html(
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