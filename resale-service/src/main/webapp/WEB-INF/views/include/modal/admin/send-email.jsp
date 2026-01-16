<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="modal fade" id="popup-send-email" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">E-MAIL 공지발송</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-8 col-md-12">
                            <div class="row m-b-sm" id="div200">
                                <label class="col-sm-12">수신자</label>
                                <div class="col-sm-12"><input class="form-control" id="receiver200" disabled="disabled"></div>
                            </div>
                            <div class="row m-b-sm">
                                <label class="col-sm-12">제목</label>
                                <div class="col-sm-12"><input class="form-control" id="mailtitle"></div>
                            </div>
                            <div class="row">
                                <label class="col-sm-12">내용</label>
                                <div class="col-sm-12"><textarea class="form-control" rows="15" id="mailMsg"></textarea></div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-12">
                            <div class="row" id="div300">
                                <label class="col-sm-12"><span id="titl">전화번호목록</span></label>
                                <div class="col-sm-12">
                                    <div class="gray-bg sms-list">
                                        <ul id="emailChaRecvList">
                                            <li class="sms-unit">
                                                <div class="sms-name"></div>
                                                <div class="sms-address"></div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <input type="hidden" value="" id="lgpw">
                <button type="button" class="btn btn-primary btn-outline" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" onclick="fm_emailSend()">발송</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var strList = [];
    var idxList = [];
    var nmList = [];
    var stList = [];
    var title200 = "가입승인";
    var title300 = "메일공지";
    var ecareNo = "";
    var msg200 = "";

    var msg300 = "메일공지내용입니다";


    //초기화
    function fn_emailInit(stList, inecareNo, lgpw) {
        strList = [];
        idxList = [];
        nmList = [];
        stList = stList;  // 기관코드
        ecareNo = inecareNo;
        var chacd = stList[0];
        $("#lgpw").val(lgpw);

        var str = '';
        if (ecareNo == "200") {
            msg200 += "담당자님, 안녕하세요? ";
            msg200 += "\n가상계좌 수납관리 서비스 가입을 축하드립니다. ";
            msg200 += "\n보다 빠른 서비스 이용을 위해 서비스 이용정보를 보내드리니";
            msg200 += "\n가상계좌 수납관리 서비스 웹사이트 회원가입 및 회원확인 절차를 완료해주세요.";
            msg200 += "\n(* 회원확인 절차 미완료 시, 서비스 이용이 불가능합니다.)";
// 		msg200 += "\n회원님의 초기 아이디는 '"+stList[0]+"'\n 초기 비밀번호는 '"+lgpw+"' 입니다." ;

            $('#mailtitle').val(title200);
            $('#mailMsg').val(msg200);
            $('#div200').show();
            $('#div300').hide();
            $('#titl').text('전화번호목록');
            msg200 = '';
            chacd = stList[0];
        } else {
            $('#mailtitle').val(title300);
            $('#mailMsg').val(msg300);
            $('#div200').hide();
            $('#div300').show();
            $('#titl').text('E-MAIL목록');
            chacd = "";
        }

        var url = "/sys/chaMgmt/selChaSmsNo";
        var param = {
            chaCd: chacd,
            ecareNo: ecareNo,
            stList: stList,
            flag: 11
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    if (ecareNo == "200") {
                        fn_mailInfo(result, 'receiver200');
                    } else {
                        fn_mailGrid(result, 'emailChaRecvList');
                    }
                }
            }
        });
    }


    function fn_mailGrid(data, obj) {
        var str = '';
        $.each(data.list, function (i, v) {
            strList.push(v.chrMail);
            idxList.push(v.chaCd);
            nmList.push(v.chaName);
            str += '<li class="sms-unit">';
            str += '<div class="sms-name">' + v.chaName + '</div>';
            str += '<div class="sms-address">' + v.chrMail + '</div>';
            str += '</li>';
        });
        $("#" + obj).html(str);
    }

    function fn_mailInfo(data) {
        $.each(data.list, function (i, v) {
            idxList.push(v.chaCd);
            nmList.push(v.chaName);
            if (v.chrMail == '' || v.chrMail == null) {
                $('#receiver200').val('');
                $('#receiver200').attr('disabled', false);
            } else {
                strList.push(v.chrMail);
                $('#receiver200').val(v.chrMail);
                $('#receiver200').attr('disabled', true);
            }
        });
    }

    // 이메일 발송
    function fm_emailSend() {

        var idx = 0;
        if (ecareNo == "200") {
            if (!$('#receiver200').val()) {
                swal({
                    type: 'info',
                    text: "E-MAIL을 입력해 주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                strList = [];
                return;
            }
        }
        if ((ecareNo == "200") && (strList == null || strList == '')) {
            strList.push($('#receiver200').val());
        }

        strList.forEach(function (entry) {
            if (!fn_emailCheck(entry)) {
                idx++;
            }
        }, this);

        if (idx > 0) {
            swal({
                type: 'info',
                text: "E-MAIL 형식이 올바르지 않습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            strList = [];
            return;
        }

        var resultVal = $('#mailMsg').val();
        swal({
            type: 'question',
            html: "E-MAIL을 전송 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                $('.spinner-area').css('display', 'block');
                var url = "/sys/chaMgmt/emailMsgSend";
                var param = {
                    ecareNo: ecareNo, 	// 200: 승인, 300:공지
                    valList: idxList,	// 기관코드
                    stList: strList,	// 메일주소
                    nmList: nmList,   // 기관명
                    chaOffNo: $("#lgpw").val(), //초기비번
                    billGubn: '',
                    title: $('#mailtitle').val(),
                    jonmun: resultVal
                };
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
                            text: "E-MAIL을 전송하였습니다.",
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function (result) {
                            $("#row-th").prop("checked", false); // 전체선택을 했을경우  체크박스 해제
                            $('#popup-send-email').modal('hide');
                            fnSearch();
                            if (ecareNo == "200") {
                            }
                        });
                    },
                    error: function (data) {
                        $('.spinner-area').css('display', 'none');
                        swal({
                            type: 'error',
                            text: "전송 실패하였습니다.",
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });
                    }
                });
            }
        });
    }

    //이메일 정규식
    function fn_emailCheck(email) {
        var regex = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
        return (email != '' && email != 'undefined' && regex.test(email));
    }
</script>
