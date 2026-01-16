<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script>
    function onCancel() {
        $('#pwd').val("");
        $('#chgPwd1').val("");
        $('#chgPwd2').val("");
        $("#password-reset-popup").modal('hide');
    }

    function chgPwd() {
        if (!$('#pwd').val()) {
            swal({
                type: 'info',
                text: '현재 비밀번호를 입력해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (!$('#chgPwd1').val()) {
            swal({
                type: 'info',
                text: '새 비밀번호를 입력해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (!$('#chgPwd2').val()) {
            swal({
                type: 'info',
                text: '새 비밀번호 확인을 입력해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if ($('#chgPwd1').val() != $('#chgPwd2').val()) {
            swal({
                type: 'info',
                text: "신규 비밀번호와 재입력 내용이 다릅니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#chgPwd2").focus();
            });
            return;
        }

        var newPw = $('#chgPwd1').val();
        var num = newPw.search(/[0-9]/g);
        var eng = newPw.search(/[a-zA-Z]/gi);
        var spe = newPw.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi);

        if (newPw.length < 8 || newPw.length > 20) {
            swal({
                type: 'info',
                text: "비밀번호는 영문, 숫자, 특수문자를 사용하여 8~20자까지, 영문은 대소문자를 구분합니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if (newPw.search(/\s/) != -1) {
            swal({
                type: 'info',
                text: "비밀번호는 공백없이 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });

            return;
        }
        if (newPw.search(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/) != -1) {
            swal({
                type: 'info',
                text: "비밀번호는 영문, 숫자, 특수문자를 사용하여 8~20자까지, 영문은 대소문자를 구분합니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if ((num < 0 && eng < 0) || (eng < 0 && spe < 0) || (spe < 0 && num < 0)) {
            swal({
                type: 'info',
                text: "비밀번호는 영문, 숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var SamePass_0 = 0; //동일문자 카운트
        var SamePass_1 = 0; //연속성(+) 카운드
        var SamePass_2 = 0; //연속성(-) 카운드

        for (var i = 0; i < newPw.length; i++) {
            var chr_pass_0 = newPw.charAt(i);
            var chr_pass_1 = newPw.charAt(i + 1);

            //동일문자 카운트
            if (chr_pass_0 == chr_pass_1) {
                SamePass_0 = SamePass_0 + 1
            }

            var chr_pass_2 = newPw.charAt(i + 2);

            //연속성(+) 카운드
            if (chr_pass_0.charCodeAt(0) - chr_pass_1.charCodeAt(0) == 1 && chr_pass_1.charCodeAt(0) - chr_pass_2.charCodeAt(0) == 1) {
                SamePass_1 = SamePass_1 + 1
            }

            //연속성(-) 카운드
            if (chr_pass_0.charCodeAt(0) - chr_pass_1.charCodeAt(0) == -1 && chr_pass_1.charCodeAt(0) - chr_pass_2.charCodeAt(0) == -1) {
                SamePass_2 = SamePass_2 + 1
            }
        }

        if (SamePass_0 > 2) {
            swal({
                type: 'info',
                text: "동일문자를 3번 이상 사용할 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (SamePass_1 > 2 || SamePass_2 > 2) {
            swal({
                type: 'info',
                text: "연속된 문자열(123 또는 321, abc, cba 등)을\n 3자 이상 사용 할 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var url = "";
        if ($('#authGb').val() == "GROUP") { // 그룹관리자 정보변경
            url = "/group/updatePwd";
        } else {							// 기관관리자 정보변경
            url = "/org/myPage/updatePwd";
        }
        var param = {
            chaCd: $('#chaCd').val(),
            pwd: $('#pwd').val(),
            chgPwd1: $('#chgPwd1').val(),
            chgPwd2: $('#chgPwd2').val(),
        };

        swal({
            type: 'question',
            html: "비밀번호를 변경 하시겠습니까?",
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
                    data: JSON.stringify(param),
                    contentType: "application/json; charset=utf-8",
                    success: function (data) {
                        if (data.retCode == '0000') {
                            swal({
                                type: 'success',
                                text: '비밀번호가 정상적으로 변경되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    $('#pwd').val("");
                                    $('#chgPwd1').val("");
                                    $('#chgPwd2').val("");
                                    $('#password-reset-popup').modal('hide');
                                }
                            });
                        } else if (data.retCode == '8888') {
                            swal({
                                type: 'info',
                                html: '현재 비밀번호가 올바르지 않습니다. <p>다시 입력해 주세요.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    $("#pwd").focus();
                                }
                            });
                        } else if (data.retCode == '9999') {
                            swal({
                                type: 'info',
                                html: '현재 비밀번호와 수정한 비밀번호가  같습니다. <p>다시 입력해 주세요.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    $("#pwd").focus();
                                }
                            });
                        } else if (data.retCode == '5555') {
                            swal({
                                type: 'info',
                                html: '비밀번호 재설정에 5회 실패하여 로그아웃 됩니다, 서비스센터에 문의해주세요.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                var url = "/logout";
                                var param = {};
                                $.ajax({
                                    type: "post",
                                    async: true,
                                    url: url,
                                    contentType: "application/json; charset=UTF-8",
                                    data: JSON.stringify(param),
                                    success: function (data) {
                                        swal({
                                            type: 'success',
                                            text: '로그아웃 되었습니다.',
                                            confirmButtonColor: '#3085d6',
                                            confirmButtonText: '확인'
                                        }).then(function (result) {
                                            if (!result.value) {
                                                return;
                                            }
                                            location.href = "/";
                                        });
                                    }
                                });
                            });
                        }
                    },
                    error: function (data) {
                        swal({
                            type: 'error',
                            text: '비밀번호 변경을 실패하였습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        })
                    }
                });
            }
        });
    }
</script>

<div class="modal fade" id="password-reset-popup" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">비밀번호 재설정</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="onCancel();">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <table class="table table-primary">
                                <tbody>
                                    <tr>
                                        <th>현재 비밀번호</th>
                                        <td>
                                            <input type="password" class="form-control" id="pwd" name="pwd">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>새 비밀번호</th>
                                        <td>
                                            <input type="password" class="form-control" id="chgPwd1" name="chgPwd1">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>비밀번호 확인</th>
                                        <td>
                                            <input type="password" class="form-control" id="chgPwd2" name="chgPwd2">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col text-center">
                            <button type="button" class="btn btn-primary btn-outlined" onclick="onCancel();">취소</button>
                            <button type="button" class="btn btn-primary" onclick="chgPwd();">변경</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
