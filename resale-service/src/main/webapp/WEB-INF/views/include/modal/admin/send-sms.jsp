<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="modal fade" id="popup-send-sms" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">문자 발송</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-8 col-md-12">
                            <div class="row m-b-sm">
                                <label class="col-sm-12">제목</label>
                                <div class="col-sm-12"><input class="form-control" id="title"></div>
                            </div>
                            <div class="row">
                                <label class="col-sm-12">내용</label>
                                <div class="col-sm-12"><textarea class="form-control" rows="15" id="smsMsg"></textarea></div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-12">
                            <div class="row">
                                <label class="col-sm-12">전화번호목록</label>
                                <div class="col-sm-12">
                                    <div class="gray-bg sms-list">
                                        <ul id="chaRecvList">
                                            <li class="sms-unit"></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outline" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" onclick="fn_msgSend()">발송</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var strList = [];
    var idxList = [];

    //초기화
    function fn_smsInit(stList) {

        var url = "/sys/chaMgmt/selChaSmsNo";
        var param = {
            stList: stList,
            flag: 10
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fn_smsGrid(result, 'chaRecvList');
                }
            }
        });

    }

    function fn_smsGrid(result, obj) {
        var str = '';
        $.each(result.list, function (i, v) {
            strList.push(v.chrHp);
            idxList.push(v.chaCd);
            str += '<li class="sms-unit">';
            str += '<div class="sms-name">' + v.chaName + '</div>';
            str += '<div class="sms-address">' + v.chrHp + '</div>';
            str += '</li>';
        });
        $("#" + obj).html(str);
    }

    // 메시지 발송
    function fn_msgSend() {

        if (!$("#title").val()) {
            swal({
                type: 'info',
                text: '제목을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#title').focus();
            });
            return;
        }
        if (!$("#smsMsg").val()) {
            swal({
                type: 'info',
                text: '내용을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#smsMsg').focus();
            });
            return;
        }

        var url = "/sys/chaMgmt/smsMsgSend";
        var param = {
            title: $("#title").val(),
            msg: $('#smsMsg').val(),
            stList: strList,		//폰번호
            valList: idxList		//기관코드
        };
        swal({
            type: 'question',
            html: "메시지를 발송하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                $('.spinner-area').css('display', 'block');
                $.ajax({
                    type: "post",
                    async: true,
                    url: url,
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (data) {
                        $('.spinner-area').css('display', 'none');
                        swal({
                            type: 'success',
                            text: "메시지를 전송하였습니다.",
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function () {
                            $('#popup-send-sms').modal('hide');
                        });
                    },
                    error: function (data) {
                        $('.spinner-area').css('display', 'none');
                        swal({
                            type: 'error',
                            text: "메시지 전송 실패하였습니다.",
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });
                    }
                });
            }
        });
    }

</script>