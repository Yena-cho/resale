<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<s:authentication property="principal.username" var="chaCd"/>
<s:authentication property="principal.name" var="name"/>
<s:authentication property="principal.loginId" var="loginId"/>

<script type="text/javascript">
    // 비밀번호 수정 버튼
    function fn_btnPass() {
        if ($('#passGubn').val() == 'U') {
            $('.password-change-area').css('display', 'block');
            $('#passGubn').val('C');
            $('#admPw').val('');
            $('#btn_up').text('취소');
        } else {
            $('.password-change-area').css('display', 'none');
            $('#passGubn').val('U');
            $('#admPw').val('');
            $('#btn_up').text('비밀번호 수정');
        }
    }

    // 저장 및 수정
    function fn_insert() {
        if ($('#insGubn').val() == 'I') {
            if ($('#resetIdCheck').val() == 'C') {
                swal({
                    type: 'info',
                    text: "아이디 중복검사를 해주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function (result) {
                    $('#resetId').focus();
                });
                return;
            }
            if ($('#resetIdCheck').val() == 'N') {
                swal({
                    type: 'info',
                    text: "사용할 수 없는 아이디입니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function (result) {
                    $('#resetId').focus();
                });
                return;
            }
            if (!$('#admId').val()) {
                swal({
                    type: 'info',
                    text: "아이디를 입력해 주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            if (!$('#admPw').val()) {
                swal({
                    type: 'info',
                    text: "비밀번호를 입력해 주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        if ($('#passGubn').val() == 'C') {
            if (!$('#admPw').val()) {
                swal({
                    type: 'info',
                    text: "비밀번호를 입력해 주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        if (!$('#admName').val()) {
            swal({
                type: 'info',
                text: "사용자명을 입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if (!fn_hpCheck($('#admHp').val())) {
            swal({
                type: 'info',
                html: "전화번호 형식이 올바르지 않습니다. " + "<p><font size=6>-</font>" + "를 포함한 숫자만 입력하세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if (!$('#mailId').val() && !$('#mailDomain1').val()) {
            swal({
                type: 'info',
                text: "E-MAIL을  입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        var mail = $('#mailId').val() + '@' + $('#mailDomain1').val();
        if (!fn_emailCheck(mail)) {
            swal({
                type: 'info',
                text: "E-MAIL 형식이 올바르지 않습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if ($('#by-st01').is(':checked')) {
            $('#status').val('ST01');
        } else {
            $('#status').val('ST02');
        }
        swal({
            type: 'question',
            html: "저장하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var url = "sys/setting/accModify";
                var param = {
                    admId: $('#admId').val(),
                    admPw: $('#admPw').val(),
                    admName: $('#admName').val(),
                    admHp: $('#admHp').val(),
                    admEmail: $('#mailId').val() + '@' + $('#mailDomain1').val(),
                    admRank: $('#admRank').val(),
                    admDept: $('#admDept').val(),
                    admStatus: $('#status').val(),
                    admGroup: $('#admGroup').val(),
                    stat: 'success',
                    maker: '${loginId}'
                };

                $.ajax({
                    type: "post",
                    async: true,
                    url: url,
                    contentType: "application/json; charset=UTF-8",
                    data: JSON.stringify(param),
                    success: function (data) {
                        swal({
                            type: 'success',
                            text: '저장되었습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function (result) {
                            if (result.value) {
                                $('#popup-account-add-modify').modal('hide');
                                fn_selAcc(1);
                            }
                        });
                    }
                });
            }
        });
    }

    function fn_hpCheck(tel) {
        var regExp = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;
        return (tel != '' && tel != 'undefined' && regExp.test(tel));
    }

    //이메일 정규식
    function fn_emailCheck(email) {

        var regex = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
        return (email != '' && email != 'undefined' && regex.test(email));
    }

    // 메일 도메인 선택
    function fn_mailDomain(val) {
        if (val == '') { // 직접입력
            $('#mailDomain1').attr('disabled', false);
        } else {
            $('#mailDomain1').attr('disabled', true);
        }
        $('#mailDomain1').val(val);
    }

    //아이디 중복체크
    function fn_idOverlap() {

        if (!$('#admId').val()) {
            swal({
                type: 'info',
                text: "아이디를 입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        $('#resetIdCheck').val('C');
        var url = "/sys/setting/selAdmId";
        var param = {
            admId: $('#admId').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.map == 0) { // 기존 기관코드 사용가능.
                    $('#resetIdSuc').show();
                    $('#resetIdErr').hide();
                    $('#resetIdCheck').val('Y');
                } else {
                    $('#resetIdSuc').hide();
                    $('#resetIdErr').show();
                    $('#resetIdCheck').val('N');
                }
            }
        });
    }

    function fn_idOverlapCk(obj) {
        $(obj).keyup(function () {
            $(this).val($(this).val().replace(/[^0-9A-Za-z]/g, ""));
        });
        $('#resetIdCheck').val('C');
        $('#resetIdSuc').hide();
        $('#resetIdErr').hide();
    }

</script>

<input type="hidden" id="passGubn" name="passGubn" value="U">
<input type="hidden" id="status" name="status">
<input type="hidden" id="resetIdCheck" value="C">

<div class="inmodal modal fade" id="popup-account-add-modify" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">계정 추가/수정</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h3>계정 정보</h3>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <table class="table table-bordered white-bg">
                                <tbody>
                                <tr>
                                    <th width="150">* 아이디</th>
                                    <td width="352" class="form-inline">
                                        <input type="text" class="form-control" id="admId" maxlength="16" onkeyup="fn_idOverlapCk(this);">
                                        <button type="button" class="btn btn-primary" id="btnCon" onclick="fn_idOverlap();">중복확인</button>
                                        <div id="resetIdSuc" style="color: blue; display: none;"><font size="2">&nbsp;사용가능</font></div>
                                        <div id="resetIdErr" style="color: red; display: none;"><font size="2">&nbsp;사용불가</font></div>
                                    </td>
                                    <th width="150">* 비밀번호</th>
                                    <td width="354">
                                        <button type="button" class="btn btn-primary" id="btn_up" onclick="fn_btnPass();">비밀번호 수정</button>
                                        <div class="password-change-area" style="display:none;">
                                            <input type="text" class="form-control m-t-xs" placeholder="비밀번호 입력" id="admPw" maxlength="20">
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>* 사용자명</th>
                                    <td><input type="text" class="form-control" id="admName"></td>
                                    <th>* 연락처</th>
                                    <td><input type="text" class="form-control" id="admHp" maxlength="15"></td>
                                </tr>
                                <tr>
                                    <th>* E-MAIL</th>
                                    <td colspan="3" class="form-inline form-email">
                                        <input type="text" class="form-control" id="mailId"><i class="fa fa-fw fa-at"></i>
                                        <input type="text" class="form-control" id="mailDomain1">
                                        <select class="form-control" id="mailDomain2" onchange="fn_mailDomain(this.value);">
                                            <option value="">직접입력</option>
                                            <option value="naver.com">naver.com</option>
                                            <option value="nate.com">nate.com</option>
                                            <option value="yahoo.com">yahoo.com</option>
                                            <option value="gmail.com">gmail.com</option>
                                            <option value="hanmail.com">hanmail.com</option>
                                            <option value="daum.com">daum.com</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>직급</th>
                                    <td>
                                        <select class="form-control" id="admRank">
                                            <option value="">선택</option>
                                            <option value="사원">사원</option>
                                            <option value="대리">대리</option>
                                            <option value="과장">과장</option>
                                            <option value="차장">차장</option>
                                            <option value="부장">부장</option>
                                        </select>
                                    </td>
                                    <th>소속부서</th>
                                    <td><input type="text" class="form-control" id="admDept"></td>
                                </tr>
                                <tr>
                                    <th>계정상태</th>
                                    <td colspan="3">
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="by-st01" name="admStatus" checked>
                                            <label for="by-st01">정상</label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="by-st02" name="admStatus">
                                            <label for="by-st02">정지</label>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary" onclick="fn_insert();"><i class="fa fa-fw fa-floppy-o"></i>저장</button>
            </div>
        </div>
    </div>
</div>
