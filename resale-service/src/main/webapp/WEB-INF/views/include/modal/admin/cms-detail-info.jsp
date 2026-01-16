<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
    var pgBankRate = 2.6;
    var nowChaSt = "";
</script>

<%-- <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script> --%>
<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>

<div class="inmodal modal fade" id="popup-cms-detail-info" tabindex="-1" role="dialog" aria-labelledby="regPayer"
     aria-hidden="true">
    <div class="modal-dialog modal-big" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">기관정보상세보기</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h3>기관기본정보</h3>
                        </div>
                    </div>
                </div>

                <form>

                    <div class="container-fluid" id="table_for_U">
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered white-bg">
                                    <tbody>
                                        <tr>
                                            <th width="150">기관분류</th>
                                            <td width="352">
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="RdoChatrty1" value="01" name="RdoChatrty" disabled>
                                                    <label for="RdoChatrty1">WEB</label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="RdoChatrty2" value="03" name="RdoChatrty" disabled>
                                                    <label for="RdoChatrty2">API</label>
                                                </div>
                                            </td>
                                            <th width="150">아이디</th>
                                            <td width="354"><span id="mDid"></span></td>
                                        </tr>
                                        <tr>
                                            <th>기관명(업체명)</th>
                                            <td><input type="text" class="form-control" id="mDchaname" maxlength="50" readonly></td>
                                            <th>대표자명</th>
                                            <td><input type="text" class="form-control" id="mDowner" maxlength="50" readonly></td>
                                        </tr>
                                        <tr>
                                            <th>대표전화번호</th>
                                            <td>
                                                <input type="text" class="form-control tel-input" id="mDownertel1" maxlength="4" readonly><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDownertel2" maxlength="4" readonly><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDownertel3" maxlength="4" readonly>
                                            </td>
                                            <th>기관코드</th>
                                            <td><input type="text" class="form-control" id="mDchacd" readonly></td>
                                        </tr>
                                        <tr>
                                            <th>사업자번호</th>
                                            <td><input type="text" class="form-control" id="mDchaoffno" maxlength="20" readonly></td>
                                            <th>담당자명</th>
                                            <td><input type="text" class="form-control" id="mDchrname" maxlength="50" readonly></td>
                                        </tr>
                                        <tr>
                                            <th>담당자 전화번호</th>
                                            <td>
                                                <input type="text" class="form-control tel-input" id="mDchrtelno1" maxlength="3" readonly><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDchrtelno2" maxlength="4" readonly><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDchrtelno3" maxlength="4" readonly>
                                            </td>
                                            <th>기관상태</th>
                                            <td>
                                                <div id="mDchaSt02">
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="RdoChast2" value="ST06" name="RdoChast" disabled>
                                                        <label for="RdoChast2">정상</label>
                                                    </div>
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="RdoChast3" value="ST08" name="RdoChast" disabled>
                                                        <label for="RdoChast3">정지</label>
                                                    </div>
                                                </div>
                                                <div id="mDchaSt03"></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>담당자 핸드폰번호</th>
                                            <td>
                                                <input type="text" class="form-control tel-input" id="mDchrhp1" maxlength="3" readonly><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDchrhp2" maxlength="4" readonly><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDchrhp3" maxlength="4" readonly>
                                            </td>
                                            <th>E-MAIL</th>
                                            <td colspan="3">
                                                <div class="form-group form-group-sm">
                                                    <input type="text" class="form-control email-input" id="chrmail1" readonly>
                                                    <span class="ml-1 mr-1 tel-bar">@</span>
                                                    <input type="text" class="form-control email-input" id="chrmail2" readonly>
                                                </div>
                                                <div class="form-group form-group-sm">
                                                    <select class="form-control" id="chrmail3" disabled>
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
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>업종</th>
                                            <td><input type="text" class="form-control" id="mDchaStatus" maxlength="50" readonly></td>
                                            <th>업태</th>
                                            <td><input type="text" class="form-control" id="mDchaType" maxlength="50" readonly></td>
                                        </tr>
                                        <tr>
                                            <th>주소</th>
                                            <td colspan="3">
                                                <div class="form-group form-group-sm">
                                                    <input type="text" class="form-control postNo-input" readonly id="mDchazipcode">
                                                    <button type="button" class="btn btn-sm btn-d-gray" readonly>우편번호검색</button>
                                                </div>
                                                <div class="form-group form-group-sm">
                                                    <input type="text" class="form-control postTxt-input" id="mDchaaddress1" maxlength="100" readonly>
                                                    <input type="text" class="form-control postTxt-input" id="mDchaaddress2" maxlength="100" readonly>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>납부자 번호</th>
                                            <td><input type="text" id="nmFinOwnNo" class="form-control" maxlength="25"/></td>
                                            <th>납부방법</th>
                                            <td>
                                                <select class="form-control" id="nmPayTy">
                                                <option value="CUR">직접납부</option>
                                                <option value="CMS">CMS</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>예금주(기관)명</th>
                                            <td><input type="text" id="nmFinsPayer" class="form-control" maxlength="25"/></td>
                                            <th>사업자 번호</th>
                                            <td><input type="text" id="nmDchaoffno" class="form-control" maxlength="20"/></td>
                                        </tr>
                                        <tr>
                                            <th>출금계좌 은행정보</th>
                                            <td>
                                            <select class="form-control" id="bnkVal">
                                            </select>
                                            </td>
                                            <th>출금 계좌번호</th>
                                            <td><input type="text" class="form-control" id="mDfeeaccno" maxlength="25"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </form>
                <td><input type="hidden" class="form-control" id="hDfeeaccno"></td>
                <td><input type="hidden" class="form-control" id="hbnkVal"></td>
                <td><input type="hidden" class="form-control" id="hFinsPayer"></td>
                <td><input type="hidden" class="form-control" id="hDchaoffno"></td>
                <td><input type="hidden" class="form-control" id="favail"></td>
                <td><input type="hidden" class="form-control" id="hFinOwnNo"></td>
                <td><input type="hidden" class="form-control" id="hPayTy"></td>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary save-cha-info" onclick="saveChaIfoSYS();"><i class="fa fa-fw fa-floppy-o"></i>정보수정</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    function collector_info_init(thisId, favail) {
        for (var i = 0; i <= 9; i++) {
            var j = i + 1;
            $("#tDadjfiregkey" + j).text('');
            $("#tDgrpadjname" + j).text('');
        }
        if(favail == "null" || favail == " "){
            $("#favail").val('N');
        }else{
            $("#favail").val('Y');
        }

            $(".save-cha-info").show();

            $("#modaltitle").text("기관정보상세");
            var _chacd = thisId;

            var url = "/sys/chaMgmt/selectChaListInfoAjax";
            var param = {
                chaCd: _chacd
            };

            $.ajax({
                type: "post",
                url: url,
                async: true,
                data: JSON.stringify(param),
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    fn_bnkList(data, 'bnkVal');
                    var map = data.info;
                    // 기관기본정보

                    if (map.chatrty == "01") {
                        $('#RdoChatrty1').prop('checked', true);
                        $('#RdoChatrty2').prop('checked', false);
                    } else {
                        $('#RdoChatrty1').prop('checked', false);
                        $('#RdoChatrty2').prop('checked', true);
                    }
                    $("#mDid").text(map.loginId);
                    $("#mDchaname").val(map.chaName);
                    $("#mDowner").val(map.owner);

                    if (map.ownerTel != null && map.ownerTel != '') {
                        var ownertel = map.ownerTel;
                        ownertel = ownertel.split('-');
                        $("#mDownertel1").val(ownertel[0]);
                        $("#mDownertel2").val(ownertel[1]);
                        $("#mDownertel3").val(ownertel[2]);
                    } else {
                        $("#mDownertel1").val("");
                        $("#mDownertel2").val("");
                        $("#mDownertel3").val("");
                    }
                    $("#mDchacd").val(map.chaCd);
                    $("#mDchaoffno").val(map.chaOffNo);
                    $("#mDchrname").val(map.chrName);
                    if (map.chrTelNo != null && map.chrTelNo != '') {
                        var chrtelno = map.chrTelNo;

                        chrtelno = chrtelno.split('-');
                        $("#mDchrtelno1").val(chrtelno[0]);
                        $("#mDchrtelno2").val(chrtelno[1]);
                        $("#mDchrtelno3").val(chrtelno[2]);
                    } else {
                        $("#mDchrtelno1").val("");
                        $("#mDchrtelno2").val("");
                        $("#mDchrtelno3").val("");
                    }
                    if (map.chast != "ST06" && map.chast != "ST08") {
                        $("#mDchaSt02").hide();
                        $("#mDchaSt03").show();

                    } else {
                        //정상에서 정지로 변경되는 경우를 체크하기 위하여 값설정
                        nowChaSt = map.chast;
                        $("#mDchaSt03").hide();
                        $("#mDchaSt02").show();
                        if (map.chast == 'ST08') {
                            $('#RdoChast2').prop('checked', false);
                            $('#RdoChast3').prop('checked', true);
                        } else {
                            $('#RdoChast2').prop('checked', true);
                            $('#RdoChast3').prop('checked', false);
                        }
                    }
                    if (map.chrHp != null && map.chrHp != '') {
                        var chrhp = map.chrHp;
                        if (chrhp.indexOf('-') < 0 && chrhp.length >= 8) {
                            chrhp = getHpNoFormat(Number(chrhp));
                        }
                        chrhp = chrhp.split('-');
                        $("#mDchrhp1").val(chrhp[0]);
                        $("#mDchrhp2").val(chrhp[1]);
                        $("#mDchrhp3").val(chrhp[2]);
                    } else {
                        $("#mDchrhp1").val("");
                        $("#mDchrhp2").val("");
                        $("#mDchrhp3").val("");
                    }
                    if (map.chrMail != null && map.chrMail != '') {
                        var _cusMail = map.chrMail;
                        _cusMail = _cusMail.split('@');
                        $("#chrmail1").val(_cusMail[0]);
                        $("#chrmail2").val(_cusMail[1]);
                        $("#chrmail3").val(_cusMail[1]).prop("selected", true);
                        if ($("#chrmail3").val() == null || $("#chrmail3").val() == '') {
                            $("#chrmail3").val("");
                        }
                        if ($("#chrmail3").val() != '') {
                            $("#chrmail2").prop("disabled", "disabled");
                        }
                    } else {
                        $("#chrmail1").val("");
                        $("#chrmail2").val("");
                        $("#chrmail3").val("").prop("selected", true);
                        $('#chrmail2').attr('disabled', false);
                    }
                    $("#mDchaStatus").val(map.chaStatus);
                    $("#mDchaType").val(map.chaType);
                    $("#mDchaaddress1").val(map.chaAddress1);
                    $("#mDchaaddress2").val(map.chaAddress2);
                    $("#mDchazipcode").val(map.chaZipCode);

                    $("#mDfeeaccno").val(nullValueChange(map.finFeeAccNo));
                    $("#bnkVal").val(nullValueChange(map.bnkCd)).prop("selected", true);
                    $("#nmFinsPayer").val(nullValueChange(map.finFeeAccOwner));
                    $("#nmDchaoffno").val(nullValueChange(map.finFeeAccIdent));
                    $("#nmFinOwnNo").val(nullValueChange(map.finFeeOwnNo));
                    $("#nmPayTy").val(nullValueChange(map.finFeePayTy)).prop("selected", true);

                    $("#hDfeeaccno").val(nullValueChange(map.finFeeAccNo));
                    $("#hbnkVal").val(nullValueChange(map.bnkCd)).prop("selected", true);
                    $("#hFinsPayer").val(nullValueChange(map.finFeeAccOwner));
                    $("#hDchaoffno").val(nullValueChange(map.finFeeAccIdent));
                    $("#hFinOwnNo").val(nullValueChange(map.finFeeOwnNo));
                    $("#hPayTy").val(nullValueChange(map.finFeePayTy)).prop("selected", true);

                }
            });
    }

    function fn_bnkList(result, obj) {
        var str = '';
            str += '<option value="">정보없음</option>';
        $.each(result.bnkList, function(k, v){
            str += '<option value="' + v.code + '">' + v.value + '</option>';
        });
        $('#' + obj).html(str);
    }

    //저장(save)
    function saveChaIfoSYS() {
        var chngd = "f";
        if ($("#mDfeeaccno").val() != $("#hDfeeaccno").val() || $("#bnkVal").val() != $("#hbnkVal").val()
            || $("#nmFinsPayer").val() != $("#hFinsPayer").val() || $("#nmDchaoffno").val() != $("#hDchaoffno").val()
            || $("#nmFinOwnNo").val() != $("#hFinOwnNo").val() || $("#nmPayTy").val() != $("#hPayTy").val()) {
            chngd = "t";
        }

        var url = "";
        var param = {};
        var chaCd = $("#mDchacd").val();
        var jobType = "ACMS";
            url = "/sys/chaMgmt/updateChaInfo";
            param = {
                 chaCd: chaCd
                , finFeeAccNo : $("#mDfeeaccno").val()
                , bnkCd : $("#bnkVal").val()
                , finFeeAccIdent : $("#nmDchaoffno").val()
                , finFeeAccOwner : $("#nmFinsPayer").val()
                , jobType: jobType
                , finFeeOwnNo : $("#nmFinOwnNo").val()
                , finFeePayTy : $("#nmPayTy").val()
            };
        swal({
            type: 'question',
            html: "저장 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (!result.value) {
                return;
            }
            
            $.ajax({
                type: "post",
                url: url,
                async: true,
                data: JSON.stringify(param),
                contentType: "application/json; charset=utf-8",
                success: function (result) {
                    if (result.retCode != "0000") {
                        swal({
                            type: 'error',
                            text: result.retMsg,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });
                        
                        return;
                    }
                    
                    swal({
                        type: 'success',
                        text: '기관정보수정이 완료되었습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#popup-cms-detail-info").modal("hide");
                        if(chngd == "t" && $("#favail").val() == "Y"){
                            doDeleteCmsFile(chaCd);
                        }else{
                            fn_search($("#pageNo").val());
                        }
                    });
                }
            });
        });
    }
</script>
