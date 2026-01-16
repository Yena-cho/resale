<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    $(function () {
        $(":input[name=chargeAmt]").change(function (e) {
            var num01;
            var num02;
            num01 = $(this).val();
            num02 = num01.replace(/\D/g, ""); //숫자가 아닌것을 제거, //즉 [0-9]를 제외한 문자 제거; /[^0-9]/g 와 같은 표현
            num01 = addThousandSeparatorCommas(num02); //콤마 찍기
            $(this).val(num01);
        });

        // 청구금액 유효성체크
        $(":input[name=chargeAmt]").attr("onkeyup", "inputNumberFormat(this)");


        $("#chargeRemark").on("keyup", function () {
            var id = $(this).attr('id');
            var cutstr = '';
            var limit = 360;
            var maxLine = $(this).attr("rows");
            var text = $(this).val();
            var textlength = text.length;
            var line = text.split("\n").length;
            var _byte = 0;
            var lines = text.split(/(\r\n|\n|\r)/gm);
            var lineMaxLength = 60;

            for (var idx = 0; idx < text.length; idx++) {
                var oneChar = escape(text.charAt(idx));
                if (oneChar.length == 1) {
                    _byte++;
                } else if (oneChar.indexOf("%u") != -1) { // 한글~
                    _byte += 2;
                } else if (oneChar.indexOf("%") != -1) {
                    _byte++;
                }

                //길면 자르기
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
                        text: '한줄에 ' + lineMaxLength + '자 이하 최대 3줄, 최대 360자 까지만 작성할 수 있습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    $(this).val(lines.join(''));
                    $(this).blur();
                }
            }

            if (line > maxLine) {
                if ($("#lineAlert").val() == "Y") {
                    swal({
                        type: 'warning',
                        text: '추가안내 사항은 ' + maxLine + '줄까지만 입력가능합니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    $("#lineAlert").val("N");
                }
            } else {
                $("#lineAlert").val("Y");
            }

            if (_byte > limit) {
                cutstr = $("#" + id + "").val().substr(text, textlength);
                $("#" + id + "").val(cutstr);
                $("#" + id + "").parent().children().children().html(limit);
                limitCharactersmc(id);
            } else {
                $("#" + id + "").parent().children().children().html(_byte);
            }

            if (event.keyCode == 13 || event.keyCode == 176) {
                limitCharactersmc(id);
            }

            if (event.keyCode == 17 || event.keyCode == 86) {
                limitCharactersmc(id);
            }
        });
    });

    // 청구대상등록수정
    function fn_modifyCharge(masCd, str, curPage) {
        fn_itemComboRefresh();
        fn_reset_scroll();

        $("#curPage").val(curPage);
        var url = "/org/claimMgmt/detailClaim";
        var param = {
            notiMasCd: masCd,
            viewType: str
        };
        $.ajax({
            type: "post",
            url: url,
            data: param,
            success: function (data) {
                $('#chargeMasCd').val(data.det.notiMasCd);
                $('#chargeCusName').text(data.det.cusName);					//고객명
                $('#chargeCusKey').text(data.det.cusKey);					//고객번호 > 확인필요
                $('#chargeCusHp').text(data.det.cusHp);
                $('#chargeVano').text(data.det.vano);						//가상계좌
                $('#chargeMasDay').text(data.det.masDay);					//청구일자
                $('#chargeStartDate').val(data.det.startDate);				//입금시작일
                $('#chargeEndDate').val(data.det.endDate);					//입금마감일
                $('#chargePrintDate').val(data.det.printDate);			 	//고지서용출력일
                $('#chargeRemark').text(data.det.remark);					//비고
                countBytes($("#chargeRemark").val(), 'chargeRemark');

                var loop = ((str == "M" || str == "N") ? 10 : data.list.length);

                // 청구항목 combo 생성
                chargeItemCdCombo(loop, data, 'chargeItem');

                // 청구항목 필드|데이터 생성
                $(data.list).each(function (k, v) {
                    setChargeItemsData((k + 1), v);
                });

                $('#chargeYear').val(data.det.masMonth.substring(0, 4));
                $('#chargeMonth').val(data.det.masMonth.substring(4, 6));
                $("#str").val(str);

                if (str == 'N') {
                    $("#chargeStartDate").attr("disabled", true);
                    $("#chargeStartDate").datepicker('option', 'disabled', true);
                }

                $('#modify-charge-popup').modal({
                    backdrop: 'static',
                    keyboard: false
                }); // 청구항목수정 및 등록 modal
            }
        });
    }

    // 청구항목 combo 박스 생성
    function chargeItemCdCombo(loop, data, objId) {
        for (var i = 1; i <= loop; i++) {
            var str = '';
            str += '<option value="">선택</option>';
            $.each(data.claimItemCd, function (k, v) {
                str += '<option value="' + v.code + '">' + v.codeName + '</option>';
            });
            $('#' + objId + i).html(str);
        }
    }

    // 청구항목 데이터 바인드
    function setChargeItemsData(index, data) {
        var detSt = data.notiDetSt;

        if (detSt == 'PA02' || detSt == 'PA00') {
            // 미납
            $('#chargeItem' + index).attr('disabled', false);
            $('#chargeAmt' + index).attr('disabled', false);
            $('#chargeAmt' + index).removeClass('input-readonly text-right');
            $('#del' + index).html('<button class="btn btn-img" onclick="fn_chargeDelItem(' + index + ');"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button>');
        } else {
            // 수납완료
            $('#chargeItem' + index).attr('disabled', true);
            $('#chargeAmt' + index).attr('disabled', true);
            $('#chargeAmt' + index).addClass('input-readonly text-right');
            $('#del' + index).html('');
        }

        $('#chargeItem' + index).val(data.payItemCd);
        $('#chargeAmt' + index).val(addThousandSeparatorCommas(data.payItemAmt)); // 천단위 콤마추가
        $('#chargeRcp' + index).val(data.payItemRcpAmt == null ? '0' : addThousandSeparatorCommas(data.payItemRcpAmt)); // 천단위 콤마추가
        $('#chargeEtc' + index).val(data.ptrItemRemark);
        $('#chargeDetCd' + index).val(data.notiDetCd);
        $('#rcpSt' + index).val(detSt);
    }

    // 청구항목 초기화
    function fn_itemComboRefresh() {
        for (var i = 1; i < 10; i++) {
            $('#chargeItem' + i).val('');
            $('#chargeAmt' + i).val('');
            $('#chargeRcp' + i).val('');
            $('#chargeEtc' + i).val('');
            $('#chargeDetCd' + i).val('');
            $('#chargeAmt' + i).removeClass('input-readonly text-right');
            $('#chargeAmt' + i).attr('disabled', false);
            $('#chargeItem' + i).attr('disabled', false);
            $('#del' + i).html('<button class="btn btn-img" onclick="fn_chargeDelItem(' + i + ');"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button>');
            $('#rcpSt' + i).val('');
        }
    }

    // 청구항목 삭제
    function fn_chargeDelItem(idx) {
        var cnt = 0;
        for (var i = 1; i < 10; i++) {
            if ($('#chargeItem' + i).val()) {
                cnt += 1;
            }
        }

        if (!$('#chargeItem' + idx).val()) {
            swal({
                type: 'info',
                text: '삭제할 항목이 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (cnt < 2) {
            swal({
                type: 'info',
                text: '청구항목은 최소 1개 이상 필요합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if ($('#chargeItem' + idx).attr('disabled') == 'disabled') { // db에 저장되어있는 청구항목
            swal({
                text: "삭제하시겠습니까?",
                type: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                $('#chargeItem' + idx).val('');
                $('#chargeAmt' + idx).val('');
                $('#chargeRcp' + idx).val('');
                $('#chargeEtc' + idx).val('');
                $('#rcpSt' + idx).val('');
                $('#chargeItem' + idx).attr('disabled', false);
            });
        } else {
            $('#chargeItem' + idx).val('');
            $('#chargeAmt' + idx).val('');
            $('#chargeRcp' + idx).val('');
            $('#chargeEtc' + idx).val('');
            $('#rcpSt' + idx).val('');
        }
    }

    // 청구대상 수정
    function fn_chargeUpdate() {
        var curPage = $("#curPage").val();
        var stDt = $('#chargeStartDate').val().replace(/\./gi, "");
        var edDt = $('#chargeEndDate').val().replace(/\./gi, "");
        var masSt;
        var detSt;
        if ($("#str").val() == 'N') {
            detSt = "PA02";
        } else {
            detSt = "PA00";
        }

        if ((!stDt || !edDt) || (stDt.length < 8 || edDt.length < 8)) {
            swal({
                type: 'info',
                text: '납부기한을 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if ($("#str").val() != 'N') {
            var nowDt = getCurrentDate();
            if (stDt < nowDt) {
                swal({
                    type: 'info',
                    text: '입금가능시작일은 금일부터 가능합니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        if ($("#str").val() == 'N') {
            var nowDt = getCurrentDate();
            if (edDt < nowDt) {
                swal({
                    type: 'info',
                    text: '입금가능마감일은 금일 이후로 선택해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        var prDt = $('#chargePrintDate').val().replace(/\./gi, "");
        if (!prDt || prDt.length < 8) {
            swal({
                type: 'info',
                text: '고지서용표시마감일을 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (stDt > prDt || edDt < prDt) {
            swal({
                type: 'info',
                text: '고지서용표시마감일은 납부기한보다 작거나 클 수 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var rowCnt = 0;
        var rowCk = 0;
        var rowAmt = 0;
        var rowRcpAmt = 0;
        var rcpPA00 = 0;
        var rcpPA02 = 0;
        var rcpPA03 = 0;
        var rcpPA04 = 0;
        var cdVals = [];
        var amtVals = [];
        var rcpAmtVals = [];
        var mVals = [];
        var selectBox = $('select[name=chargeItem]');

        var valRow = 0;

        selectBox.each(function (i) {
            var tr = selectBox.parent().parent().eq(i);
            var td = tr.children();
            var rcpSt = tr.children().find('input[name="rcpSt"]').val();
            var selectVal = tr.children().find('select[name="chargeItem"]').val();
            var amt = tr.children().find('input[name="chargeAmt"]').val();
            var rcpAmt = tr.children().find('input[name="chargeRcp"]').val();
            var remark = basicEscape(tr.children().find('input[name="chargeEtc"]').val());

            if (!(amt == '' || amt == null)) {
                if ($(this).val() == '' || $(this).val() == null || $(this).val() == "선택") {
                    valRow++;
                }
            }

            if ($(this).val() != '' && $(this).val() != null && $(this).val() != "선택") {
                if (rowCnt > 0 && cdVals.indexOf($(this).val()) != -1) { // 중복체크
                    rowCk++;
                }
                cdVals.push($(this).val());

                amt = removeCommas(amt);
                if (amt == '' || amt == null) {
                    rowAmt++;
                }
                amtVals.push(amt);

                rcpAmt = removeCommas(rcpAmt);
                if (rcpAmt == '' || rcpAmt == null) {
                    rowRcpAmt++;
                }
                rcpAmtVals.push(rcpAmt);

                mVals.push(remark);
                rowCnt++;

                if (rcpSt == 'PA00') {
                    // 임시
                    rcpPA00++;
                } else if (rcpSt == 'PA02') {
                    // 미납
                    rcpPA02++;
                } else if (rcpSt == 'PA03') {
                    // 완납
                    rcpPA03++;
                } else if (rcpSt == 'PA04') {
                    // 일부납
                    rcpPA04++;
                }
            }
        });

        if (rcpPA00 > 0) {
            // 임시가 하나라도 있으면 임시데이터기 때문에 청구서는 임시
            masSt = "PA00";
        } else if (rcpPA03 > 0 || rcpPA04 > 0) {
            if (rowCnt == rcpPA03) {
                masSt = "PA03";
            } else {
                // 일부납이나, 완납이 하나라도 있으면 일부납
                masSt = "PA04";
            }
        } else if (rowCnt == rcpPA03) {
            // 미납건 삭제 후 남은 데이터가 모두 완납일 경우 완납
            masSt = "PA03";
        } else if (rcpPA00 == 0 && rcpPA03 == 0 && rcpPA04 == 0) {
            // 임시 데이터가 하나도 없고, 완납 데이터가 하나도 없고, 일부납 데이터가 하나도 없을 때 미납 ( 초과납이 있거나, 환불이 있을 경우 모달창이 아예 열리지 않으니 고려X)
            masSt = "PA02";
        }

        if (rowCnt == 0) {
            swal({
                type: 'info',
                text: '청구항목은 최소 1개 이상 선택하셔야 합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (rowCk != 0) {
            swal({
                type: 'info',
                text: '중복된 청구항목이 있습니다. 확인해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (rowAmt > 0) {
            swal({
                type: 'info',
                text: '청구항목 금액을 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (valRow > 0) {
            swal({
                type: 'info',
                text: '청구항목명을 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        // 개인별 비고항목
        if (limitCharactersmc('chargeRemark') == false) {
            return false;
        }

        swal({
            title: "수정 하시겠습니까? ",
            html: "청구금액 수정 시 수납상태에도 수정 반영되오니<p>참고하여 주시기 바랍니다.",
            type: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var url = "/org/claimMgmt/chargeItemSave";
                var param = {
                    vano: $('#chargeVano').text(),
                    notiMasCd: $('#chargeMasCd').val(),
                    masMonth: $('#chargeYear').val() + $('#chargeMonth').val(),
                    startDate: $('#chargeStartDate').val().replace(/\./gi, ""),
                    endDate: $('#chargeEndDate').val().replace(/\./gi, ""),
                    printDate: $('#chargePrintDate').val().replace(/\./gi, ""),
                    idxList: cdVals,
                    itemList: amtVals,
                    strList: mVals,
                    notiDetSt: detSt,
                    remark: basicEscape($('#chargeRemark').val()),
                    notiMasSt: masSt
                };
                $.ajax({
                    type: "post",
                    url: url,
                    data: JSON.stringify(param),
                    contentType: "application/json; charset=utf-8",
                    success: function (data) {
                        swal({
                            type: 'success',
                            text: '수정하였습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function (result) {
                            if (result.value) {
                                $('#modify-charge-popup').modal('hide');
                                fn_ClaimListCall(curPage);
                            }
                        });
                    },
                    error: function (data) {
                        swal({
                            type: 'error',
                            text: '수정 실패하였습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        })
                    }
                });
            }
        });
    }

    // 글자수 제한 (총바이트, 총 줄수, 줄당 최대길이, 출당 최대바이트)
    function limitCharactersmc(id) {
        var limit = 360;
        var maxLine = 3;
        var lineMaxLength = 60;
        var lineMaxByte = 120;

        var text = $("#" + id + "").val();
        var textlength = text.length;
        var line = text.split("\n").length;
        var lines = text.split("\n");

        var valueCheck = true;
        var _byte = 0;
        var billcon = '메모';

        for (var idx = 0; idx < text.length; idx++) {
            var oneChar = escape(text.charAt(idx));
            if (oneChar.length == 1) {
                _byte++;
            } else if (oneChar.indexOf("%u") != -1) { // 한글~
                _byte += 2;
            } else if (oneChar.indexOf("%") != -1) {
                _byte++;
            }
        }

        // 3줄 이상 제한
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

        // byte 제한
        if (_byte > limit) {
            $('#' + id + '').focusout();
            swal({
                type: 'warning',
                text: '입력가능 용량  초과하였습니다. (' + _byte + '/' + limit + ')' + '\n' + billcon + ' 을(를) 수정해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })

            valueCheck = false;
        }

        // 1열당 글자수 제한
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
                text: '한줄에 ' + lineMaxLength + '자 미만, 최대 180자 까지만 작성할 수 있습니다. ' + '\n' + billcon + ' 의 ' + k + ' 열을 수정해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })

            valueCheck = false;
        }

        $("#" + id + "").parent().children().children().html(_byte);
        return valueCheck;
    }
</script>

<jsp:include page="/WEB-INF/views/include/popHeader.jsp" />

<input type="hidden" id="chargeMasCd"  name="chargeMasCd">
<input type="hidden" id="chargeDetCd1" name="chargeDetCd">
<input type="hidden" id="chargeDetCd2" name="chargeDetCd">
<input type="hidden" id="chargeDetCd3" name="chargeDetCd">
<input type="hidden" id="chargeDetCd4" name="chargeDetCd">
<input type="hidden" id="chargeDetCd5" name="chargeDetCd">
<input type="hidden" id="chargeDetCd6" name="chargeDetCd">
<input type="hidden" id="chargeDetCd7" name="chargeDetCd">
<input type="hidden" id="chargeDetCd8" name="chargeDetCd">
<input type="hidden" id="chargeDetCd9" name="chargeDetCd">
<input type="hidden" id="curPage" name="curPage">

<div class="modal fade" id="modify-charge-popup" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title claim_modal_title">청구등록수정</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">

                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-6">
                            <h5 class="claim_modal_title">청구등록수정</h5>
                        </div>
                        <div class="col-6 text-right">
                            <span class="text-danger">* 필수입력</span>
                        </div>
                    </div>
                    <table class="table table-form">
                        <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>고객명</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <span id="chargeCusName"></span>
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">고객번호</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <span id="chargeCusKey"></span>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">연락처</th>
                            <td class="col-md-4 col-sm-8 col-8 form-inline contact-number">
                                <span id="chargeCusHp"></span>
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">가상계좌</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <span id="chargeVano"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-6">
                            <h5>청구정보</h5>
                        </div>
                        <div class="col-6 text-right">
                            <span class="text-danger">* 필수입력</span>
                        </div>
                    </div>

                    <table class="table table-form">
                        <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>청구월</th>
                            <td class="col-md-4 col-sm-8 col-8 font-inline" id="chargeCal">
                                <select class="form-control col" id="chargeYear"></select>
                                <span class="ml-1 mr-1">년</span>

                                <select class="form-control col" id="chargeMonth"></select>
                                <span class="ml-1 mr-auto">월</span>
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">청구일자</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <span id="chargeMasDay"></span>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>납부기한</th>
                            <td class="col-md-4 col-sm-8 col-8 form-inline" id="chargeDate">
                                <div class="date-input">
                                    <div class="input-group">
                                        <input type="hidden" id="str" value="">
                                        <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="chargeStartDate" aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                    </div>
                                </div>
                                <span class="range-mark"> ~ </span>
                                <div class="date-input">
                                    <div class="input-group">
                                        <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="chargeEndDate" aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                    </div>
                                </div>
                            </td>
                            <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>고지서용<br/>표시마감일</th>
                            <td class="col-md-4 col-sm-8 col-8 form-inline">
                                <div class="date-input">
                                    <div class="input-group">
                                        <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="chargePrintDate" aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                    </div>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                    <table class="table tabl-sm table-primary mt-3">
                        <colgroup id="colgroup-col">
                            <col width="60">
                            <col width="170">
                            <col width="170">
                            <col width="170">
                            <col width="190">
                            <col width="50">
                        </colgroup>

                        <thead>
                        <tr>
                            <th>NO</th>
                            <th><span class="text-danger">*</span>청구항목명</th>
                            <th><span class="text-danger">*</span>청구금액(원)</th>
                            <th>수납금액(원)</th>
                            <th>비고</th>
                            <th>삭제</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr>
                            <td>1</td>
                            <td><select class="form-control" id="chargeItem1" name="chargeItem"><option>선택</option></select></td>
                            <td class="form-td-inline"><input type="text" class="form-control" id="chargeAmt1" name="chargeAmt" style="text-align: right;"><span class="ml-1"></span></td>
                            <td><input type="text" class="form-control input-readonly text-right" id="chargeRcp1" name="chargeRcp" disabled="disabled"><input id="rcpSt1" type="hidden" name="rcpSt"></td>
                            <td><input type="text" class="form-control" id="chargeEtc1" name="chargeEtc"></td>
                            <td id="del1"><button class="btn btn-img" onclick="fn_chargeDelItem(1);"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button></td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td><select class="form-control" id="chargeItem2" name="chargeItem"><option>선택</option></select></td>
                            <td class="form-td-inline"><input type="text" class="form-control" id="chargeAmt2" name="chargeAmt" style="text-align: right;"><span class="ml-1"></span></td>
                            <td><input type="text" class="form-control input-readonly text-right" id="chargeRcp2" name="chargeRcp" disabled="disabled"><input id="rcpSt2" type="hidden" name="rcpSt"></td>
                            <td><input type="text" class="form-control" id="chargeEtc2" name="chargeEtc"></td>
                            <td id="del2"><button class="btn btn-img" onclick="fn_chargeDelItem(2);"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button></td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td><select class="form-control" id="chargeItem3" name="chargeItem"><option>선택</option></select></td>
                            <td class="form-td-inline"><input type="text" class="form-control" id="chargeAmt3" name="chargeAmt" style="text-align: right;"><span class="ml-1"></span></td>
                            <td><input type="text" class="form-control input-readonly text-right" id="chargeRcp3" name="chargeRcp" disabled="disabled"><input id="rcpSt3" type="hidden" name="rcpSt"></td>
                            <td><input type="text" class="form-control" id="chargeEtc3" name="chargeEtc"></td>
                            <td id="del3"><button class="btn btn-img" onclick="fn_chargeDelItem(3);"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button></td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td><select class="form-control" id="chargeItem4" name="chargeItem"><option>선택</option></select></td>
                            <td class="form-td-inline"><input type="text" class="form-control" id="chargeAmt4" name="chargeAmt" style="text-align: right;"><span class="ml-1"></span></td>
                            <td><input type="text" class="form-control input-readonly text-right" id="chargeRcp4" name="chargeRcp" disabled="disabled"><input id="rcpSt4" type="hidden" name="rcpSt"></td>
                            <td><input type="text" class="form-control" id="chargeEtc4" name="chargeEtc"></td>
                            <td id="del4"><button class="btn btn-img" onclick="fn_chargeDelItem(4);"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button></td>
                        </tr>
                        <tr>
                            <td>5</td>
                            <td><select class="form-control" id="chargeItem5" name="chargeItem"><option>선택</option></select></td>
                            <td class="form-td-inline"><input type="text" class="form-control" id="chargeAmt5" name="chargeAmt" style="text-align: right;"><span class="ml-1"></span>
                            <td><input type="text" class="form-control input-readonly text-right" id="chargeRcp5" name="chargeRcp" disabled="disabled"><input id="rcpSt5" type="hidden" name="rcpSt"></td>
                            <td><input type="text" class="form-control" id="chargeEtc5" name="chargeEtc"></td>
                            <td id="del5"><button class="btn btn-img" onclick="fn_chargeDelItem(5);"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button></td>
                        </tr>
                        <tr>
                            <td>6</td>
                            <td><select class="form-control" id="chargeItem6" name="chargeItem"><option>선택</option></select></td>
                            <td class="form-td-inline"><input type="text" class="form-control" id="chargeAmt6" name="chargeAmt" style="text-align: right;"><span class="ml-1"></span></td>
                            <td><input type="text" class="form-control input-readonly text-right" id="chargeRcp6" name="chargeRcp" disabled="disabled"><input id="rcpSt6" type="hidden" name="rcpSt"></td>
                            <td><input type="text" class="form-control" id="chargeEtc6" name="chargeEtc"></td>
                            <td id="del6"><button class="btn btn-img" onclick="fn_chargeDelItem(6);"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button></td>
                        </tr>
                        <tr>
                            <td>7</td>
                            <td><select class="form-control" id="chargeItem7" name="chargeItem"><option>선택</option></select></td>
                            <td class="form-td-inline"><input type="text" class="form-control" id="chargeAmt7" name="chargeAmt" style="text-align: right;"><span class="ml-1"></span></td>
                            <td><input type="text" class="form-control input-readonly text-right" id="chargeRcp7" name="chargeRcp" disabled="disabled"><input id="rcpSt7" type="hidden" name="rcpSt"></td>
                            <td><input type="text" class="form-control" id="chargeEtc7" name="chargeEtc"></td>
                            <td id="del7"><button class="btn btn-img" onclick="fn_chargeDelItem(7);"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button></td>
                        </tr>
                        <tr>
                            <td>8</td>
                            <td><select class="form-control" id="chargeItem8" name="chargeItem"><option>선택</option></select></td>
                            <td class="form-td-inline"><input type="text" class="form-control" id="chargeAmt8" name="chargeAmt" style="text-align: right;"><span class="ml-1"></span></td>
                            <td><input type="text" class="form-control input-readonly text-right" id="chargeRcp8" name="chargeRcp" disabled="disabled"><input id="rcpSt8" type="hidden" name="rcpSt"></td>
                            <td><input type="text" class="form-control" id="chargeEtc8" name="chargeEtc"></td>
                            <td id="del8"><button class="btn btn-img" onclick="fn_chargeDelItem(8);"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button></td>
                        </tr>
                        <tr>
                            <td>9</td>
                            <td><select class="form-control" id="chargeItem9" name="chargeItem"><option>선택</option></select></td>
                            <td class="form-td-inline"><input type="text" class="form-control" id="chargeAmt9" name="chargeAmt" style="text-align: right;"><span class="ml-1"></span></td>
                            <td><input type="text" class="form-control input-readonly text-right" id="chargeRcp9" name="chargeRcp" disabled="disabled"><input id="rcpSt9" type="hidden" name="rcpSt"></td>
                            <td><input type="text" class="form-control" id="chargeEtc9" name="chargeEtc"></td>
                            <td id="del9"><button class="btn btn-img" onclick="fn_chargeDelItem(9);"><img src="/assets/imgs/common/btn-lubbish-bin.png"></button></td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="guide-mention" style="color:#71adfd;">
                        * '청구관리 > 항목관리'에서 청구 항목을 미리 설정해주세요.
                    </div>

                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-6">
                            <h5>추가안내 사항</h5>
                        </div>
                        <div class="col-6 text-right">
                        </div>
                    </div>
                    <table class="table table-form">
                        <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <td class="col-12">
                                <textarea class="form-control" id="chargeRemark" name="chargeRemark" rows="3" ></textarea>
                                <span class="textarea-byte-counter"><em>0</em>/360byte</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="guide-mention" style="color:#71adfd;">
                        * 각 고지서의 추가 안내사항이 있을 경우 이용하세요. 해당 고지서 하단에 표기됩니다.
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <input type="hidden" value="Y" id="lineAlert"/>
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary" onclick="fn_chargeUpdate();">수정</button>
            </div>
        </div>
    </div>
</div>
