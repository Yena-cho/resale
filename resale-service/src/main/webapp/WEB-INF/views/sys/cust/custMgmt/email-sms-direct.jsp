<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/html/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-3";
    var twoDepth = "adm-sub-05-1";
</script>

<script>
    $(document).ready(function () {
        //바이트 제한
        $("#eContent").on("keyup", function () {
            var str = $("#eContent").val();
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
                cutstr = $("#eContent").val().substr(str, strlength);
                $("#eContent").val(cutstr);
                $("#eByte").html(maxlength + "/2000 byte");
                return;
            } else {
                $("#eByte").html(tbyte + "/2000 byte");
            }
        });

        $("#sContent").on("keyup", function () {
            var str = $("#sContent").val();
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
                cutstr = $("#sContent").val().substr(str, strlength);
                $("#sContent").val(cutstr);
                $("#sByte").html(maxlength + "/1000 byte");
                return;
            } else {
                $("#sByte").html(tbyte + "/1000 byte");
            }
        });

    }); //doc ready

    function sendEmail() {
        var emailAdr = $("#emailAddress").val();
        var etitle = $('#etitle').val();
        var econtent = $('#econtent').val();
        if (emailAdr == '' || emailAdr == null) {
            swal({
                type: 'info',
                text: '발송대상을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if (etitle == '' || etitle == null) {
            swal({
                type: 'info',
                text: '제목을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if (econtent == '' || econtent == null) {
            swal({
                type: 'info',
                text: '내용을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        var param = {
            emailAdr: emailAdr,
            etitle: etitle,
            econtent: econtent
        };
        $.ajax({
            type: "post",
            async: true,
            url: "/sys/sysEmailDirectSend",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                if(data.retCode == "0001"){
                    swal({
                        type: 'error',
                        text: "메시지 전송 실패하였습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else {
                    emptyVars("E");
                    swal({
                        type: 'success',
                        text: "메시지를 전송하였습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
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

    };

    function sendSms() {
        var phoneNo = $('#phoneNumber').val();
        var stitle = $('#stitle').val();
        var scontent = $('#scontent').val();
        if (phoneNo == '' || phoneNo == null) {
            swal({
                type: 'info',
                text: '발송대상을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if (stitle == '' || stitle == null) {
            swal({
                type: 'info',
                text: '제목을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if (scontent == '' || scontent == null) {
            swal({
                type: 'info',
                text: '내용을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        var param = {
            phoneNo: phoneNo,
            stitle: stitle,
            scontent: scontent
        };
        $.ajax({
            type: "post",
            async: true,
            url: "/sys/sysSmsDirectSend",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                if(data.retCode == "0001"){
                    swal({
                        type: 'error',
                        text: "메시지 전송 실패하였습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else{
                    emptyVars("S");
                    swal({
                        type: 'success',
                        text: "메시지를 전송하였습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
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
    };

    function emptyVars(vars){
        if(vars == "E"){
            $("#emailAddress").val("");
            $('#etitle').val("");
            $('#econtent').val("");
        }

        if(vars == "S"){
            $('#phoneNumber').val("");
            $('#stitle').val("");
            $('#scontent').val("");
        }
    };
</script>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>공지발송관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>개별공지발송</strong>
            </li>
        </ol>
        <p class="page-description">서비스 이용 안내 전달을 위한 공지 발송/관리 화면입니다.</p>
    </div>
</div>

<div class="wrapper wrapper-content">
    <div class="animated fadeInUp">
        <div class="row">
            <div class="col-lg-12 m-b-3">
                <div class="tabs-container">
                    <ul class="nav nav-tabs" role="tablist">
<%--                        <li role="presentation" class="active"><a href="#email-notification-list" aria-controls="email-notification-list" role="tab" data-toggle="tab">E-MAIL 발송</a></li>--%>
                        <li role="presentation" class="active"><a href="#sms-notification-list" aria-controls="sms-notification-list" role="tab" data-toggle="tab">문자 발송</a></li>
                    </ul>

                    <div class="tab-content">
                        <%--<div id="email-notification-list" role="tabpanel" class="tab-pane active">
                            <div class="panel-body row m-b-xs">
                                <div class="col-lg-12">
                                    <div class="ibox">

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive" style="overflow-x: hidden">
                                                <div class="row m-b-sm">
                                                    <label class="col-sm-12">발송대상 이메일 주소</label>
                                                    <div class="col-sm-12"><input class="form-control" id="emailAddress" placeholder=", 로 구분"></div>
                                                </div>

                                                <div class="row m-b-sm">
                                                    <label class="col-sm-12">제목</label>
                                                    <div class="col-sm-12"><input class="form-control" id="etitle"></div>
                                                </div>
                                                <div class="row">
                                                    <label class="col-sm-12">내용</label>
                                                    <div class="col-sm-12">
                                                        <textarea class="form-control" rows="15" id="econtent" style="resize:none;"></textarea>
                                                        <p id="eByte">0/2000 byte</p>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-lg-12 text-right">
                                                        <button class="btn btn-w-m btn-primary btn-open-send-email-without-list" onclick="sendEmail()">
                                                            <i class="fa fa-fw fa-envelope-o"></i> E-MAIL 공지 발송</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>--%>

                        <div id="sms-notification-list" role="tabpanel" class="tab-pane active">
                            <div class="panel-body row m-b-xs">
                                <div class="col-lg-12">
                                    <div class="ibox">

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive" style="overflow-x: hidden">
                                                <div class="row m-b-sm">
                                                    <label class="col-sm-12">발송대상 휴대폰 번호</label>
                                                    <div class="col-sm-12"><input class="form-control" id="phoneNumber" placeholder=", 로 구분"></div>
                                                </div>

                                                <div class="row m-b-sm">
                                                    <label class="col-sm-12">제목</label>
                                                    <div class="col-sm-12"><input class="form-control" id="stitle" value=""></div>
                                                </div>
                                                <div class="row">
                                                    <label class="col-sm-12">내용</label>
                                                    <div class="col-sm-12">
                                                        <textarea class="form-control " rows="15" id="scontent" style="resize:none;"></textarea>
                                                        <p id="sByte">0/1000 byte</p>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-lg-12 text-right">
                                                        <button class="btn btn-w-m btn-primary btn-open-send-sms-without-list" onclick="sendSms()">
                                                            <i class="fa fa-fw fa-mobile"></i> 문자 공지 발송</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>


<script>



</script>
