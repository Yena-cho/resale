<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<script type="text/javascript">
    var firstDepth = "nav-link-31";
    var secondDepth = "sub-02";
    var cuPage = 1;
    var delMessage = '선택내역을 삭제하시겠습니까?';
    var noSelectMsg = "삭제할 고객을 선택하세요.";
    var noSelectMsg3 = "납부제외할 고객을 선택하세요.";

    $(document).ready(function () {
        $('.popover-dismiss').popover({
            trigger: 'focus'
        });

        //개별고객 대상등록 팝업 정상고객등록
        $(".btn-open-reg-payer-information").click(function () {
            modify_payer_information_init("I");
            $('#modify-payer-information').modal({
                backdrop: 'static',
                keyboard: false
            });
        });

        // 정보수정
        $(".btn-open-modify-payer-info").click(function () {
            fn_reset_scroll();

            var rowCnt = 0;
            var disabledYCnt = 0;
            var vano;
            var checkbox = $("input[name='checkOne']:checked");
            checkbox.map(function (i) {
                vano = $(this).val();
                rowCnt++;
                var tr = checkbox.parent().parent().eq(i);
                var td = tr.children();
                if (td.eq(td.length - 5).text().indexOf('삭제') != -1) {
                    disabledYCnt++;
                }
            });

            if (rowCnt == 0) {
                swal({
                    type: 'info',
                    text: '수정할 고객을 선택해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            if (rowCnt > 1) {
                swal({
                    type: 'info',
                    text: '수정할 고객을 한건만 선택해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            if (disabledYCnt > 0) {
                swal({
                    type: 'info',
                    text: '삭제된 고객은 수정할 수  없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            modify_payer_information_init("U", vano);
            $('#modify-payer-information').modal({
                backdrop: 'static',
                keyboard: false
            });
        });

        // 그리드 전체선택, 전체해제
        $("#row-th").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#row-th").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=checkOne]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=checkOne]").prop("checked", false);
            }
        });

        //납부대상 전체 체크, 해제
        $('#payAll').click(function () {
            if ($('#payAll').prop('checked')) {
                $('input:checkbox[name=payItem]').prop('checked', true);
            } else {
                $('input:checkbox[name=payItem]').prop('checked', false);
            }
        });

        //고객상태 전체 체크, 해제
        $('#statAll').click(function () {
            if ($('#statAll').prop('checked')) {
                $('input:checkbox[name=statItem]').prop('checked', true);
            } else {
                $('input:checkbox[name=statItem]').prop('checked', false);
            }
        });

        // 검색구분이 계좌번호일때는 숫자만 입력하고 아닐땐 허용
        $('#SRCHsearchValue').keyup(function () {
            if ($('#SRCHsearchValue option:selected').val() == "vano") {
                $('#SRCHsearchValue').val($('#SRCHsearchValue').val().replace(/[^0-9]/g, ""));
            }
        });

        $('#payAll').click();
        $('#statN').click();
        $('#rcpReqAll').click();

        // 선택삭제
        $(".btn-delete-payer-information").click(function () {
            var rowCnt = 0;
            var disabledCnt = 0;
            var disabledYCnt = 0; // 삭제여부
            var itemDelVals = [];
            var checkbox = $("input[name='checkOne']:checked");
            checkbox.each(function (i) {
                var tr = checkbox.parent().parent().eq(i);
                var td = tr.children();
                itemDelVals.push($(this).val());
                rowCnt++;

                if (td.eq(td.length - 5).text() == '정상') {
                    disabledYCnt++;
                }
                if (td.eq(td.length - 5).text() == '삭제') {
                    disabledCnt++;
                }
            });

            if (disabledYCnt > 0 && disabledCnt > 0) {
                swal({
                    type: 'info',
                    text: '이미 삭제된 고객이 있습니다. 다시 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            if (rowCnt == disabledCnt && (rowCnt > 0)) {
                swal({
                    type: 'info',
                    text: '이미 삭제된 고객입니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (rowCnt == 0) {
                swal({
                    type: 'info',
                    text: noSelectMsg,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            } else {
                swal({
                    type: 'question',
                    html: "선택하신 " + rowCnt + "건을 삭제하시겠습니까?",
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '확인',
                    cancelButtonText: '취소',
                    reverseButtons: true
                }).then(function (result) {
                    if (result.value) {
                        var url = "/org/custMgmt/deleteUseCustInfo";
                        var param = {
                            itemList: itemDelVals
                        };
                        $.ajax({
                            type: "post",
                            url: url,
                            data: param,
                            success: function (result) {
                                swal({
                                    type: 'success',
                                    text: '고객 대상 삭제하였습니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                }).then(function (result) {
                                    if (result.value) {
                                        $('#row-th').prop('checked', false);
                                        pageChange();
                                    }
                                });
                            },
                            error: function (data) {
                                swal({
                                    type: 'error',
                                    text: '고객 대상 삭제 실패하였습니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })
                            }
                        });
                    }
                });
            }
        });

        // 납부제외
        $(".btn-except-payer-information").click(function () {
            var rowCnt = 0;
            var rcpgubnNCnt = 0; // 납부제외여부
            var rcpgubnCnt = 0;
            var itemDelVals = [];
            var checkbox = $("input[name='checkOne']:checked");
            checkbox.each(function (i) {
                var tr = checkbox.parent().parent().eq(i);
                var td = tr.children();
                itemDelVals.push($(this).val());
                rowCnt++;

                if (td.eq(td.length - 8).text() == '대상') {
                    rcpgubnNCnt++;
                }
                if (td.eq(td.length - 8).text() == '제외') {
                    rcpgubnCnt++;
                }
            });

            if (rcpgubnCnt > 0 && rcpgubnNCnt > 0) {
                swal({
                    type: 'info',
                    text: '이미 납부제외된 고객이 있습니다. 다시 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (rowCnt == rcpgubnCnt && (rowCnt > 0)) {
                swal({
                    type: 'info',
                    text: '이미 납부제외된 고객입니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (rowCnt == 0) {
                swal({
                    type: 'info',
                    text: noSelectMsg3,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            } else {
                swal({
                    type: 'question',
                    html: "선택하신 " + rowCnt + "건을 납부제외 하시겠습니까?",
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '확인',
                    cancelButtonText: '취소',
                    reverseButtons: true
                }).then(function (result) {
                    if (result.value) {
                        var url = "/org/custMgmt/exceptCustInfo";
                        var param = {
                            itemList: itemDelVals
                        };
                        $.ajax({
                            type: "post",
                            url: url,
                            data: param,
                            success: function (result) {
                                swal({
                                    type: 'success',
                                    text: '선택한 고객정보를 납부제외하였습니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                }).then(function (result) {
                                    if (result.value) {
                                        $('#row-th').prop('checked', false);
                                        pageChange();
                                    }
                                });
                            },
                            error: function (data) {
                                swal({
                                    type: 'error',
                                    text: '선택한 고객정보를 납부제외 실패하였습니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })
                            }
                        });
                    }
                });
            }
        });

        fn_selMonth(0);
        fnSearch();

    });


    // 조회
    function fnSearch(page) {
        var checkArr = [];
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        if (fnValidation()) {

            var url = "/org/custMgmt/getCustListList";
            //납부대상
            var payList = [];
            var check = $("input[name='payItem']:checked");
            check.map(function (i) {
                if ($(this).val() != null && $(this).val() != '') {
                    payList.push($(this).val());
                }
            });
            // 고객상태
            var cusList = [];
            var check = $("input[name='statItem']:checked");
            check.map(function (i) {
                if ($(this).val() != null && $(this).val() != '') {
                    cusList.push($(this).val());
                }
            });

            if ($('#SRCHsearchGb option:selected').val() == "vano" && $("#SRCHsearchValue").val()) {
                var str = $("#SRCHsearchValue").val();
                var str2 = str.split(',');
                for (var i = 0; i < str2.length; i++) {
                    if (($.isNumeric(str2[i]) && (str2[i].length < 2 || str2[i].length > 4)) && str2[i].length != 14) {
                        swal({
                            type: 'info',
                            html: "가상계좌번호 14자리 모두 입력 또는 <p>가상계좌 끝 4자리 중 최소 2자리 이상, 4자리 이하로 입력해 주세요.",
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });
                        return;
                    }
                    if (!$.isNumeric(str2[i])) {
                        swal({
                            type: 'info',
                            text: "가상계좌는 숫자형식으로만 입력가능합니다.",
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function () {
                            $("#SRCHsearchValue").val('');
                        });
                        return;
                    }
                }
            }

            var param = {
                startDate: $('#SRCHcalDateFrom').val().replace(/\./gi, ""),
                endDate: $('#SRCHcalDateTo').val().replace(/\./gi, ""),
                searchGb: $('#SRCHsearchGb option:selected').val(),
                searchValue: $('#SRCHsearchValue').val(),
                payList: payList,
                cusList: cusList,
                // monList: monList,
                cusGubn: $('#selCusGubn option:selected').val(),
                cusGubnValue: $('#SRCHcusGubnValue').val(),
                searchOrderBy: $('#cSearchOrderBy option:selected').val(),
                curPage: cuPage,
                pageScale: $('#pageScale option:selected').val()
            };

            $.ajax({
                type: "post",
                async: true,
                url: url,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (result) {
                    $('#row-th').prop('checked', false);
                    if (result.retCode == "0000") {
                        fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
                        ajaxPaging(result, 'PageArea');
                    } else {
                        swal({
                            type: 'error',
                            text: result.retMsg,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });
                    }
                }
            });
        }
    }

    //데이터 새로고침
    function fnGrid(result, obj) {
        $('#totCnt').text(result.count);
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="16" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td class="hidden-on-mobile">';
                if (NVL(v.disabled) == 'N') {
                    str += '<input class="form-check-input table-check-child" type="checkbox" name="checkOne" id="row-' + v.rn + '" value="' + v.vano + '" onclick="fn_listCheck();" onchange="changeBGColor(this)">';
                } else {
                    str += '<input class="form-check-input checkbox-none-check-disabled" type="checkbox" >';
                }
                str += '<label for="row-' + v.rn + '"><span></span></label>';
                str += '<input type="hidden" id="chaCd"    value="' + v.chaCd + '"    />'
                str += '<input type="hidden" id="vano"     value="' + v.vano + '"     />'
                str += '<input type="hidden" id="masKey"   value="' + v.masKey + '"   />'
                str += '<input type="hidden" id="cusMail"  value="' + v.cusMail + '"  />'
                str += '<input type="hidden" id="rcpGubn"  value="' + v.rcpGubn + '"  />'
                str += '<input type="hidden" id="disabled" value="' + v.disabled + '" />'
                str += '<input type="hidden" id="rcpReqTy" value="' + v.rcpReqTy + '" />'
                str += '</td>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.regDt + '</td>';
                if (NVL(v.disabled) == 'N') {
                    if ($(window).width() >= "970") {
                        str += '<td><a href="#" onclick="fnCusInfoUpdate(\'' + v.vano + '\');">' + basicEscape(v.cusName) + '</a></td>';
                    } else {
                        str += '<td><a href="#" onclick="fnDetail(\'' + v.vano + '\');">' + basicEscape(v.cusName) + '</a></td>';
                    }
                } else {
                    if ($(window).width() >= "970") {
                        str += '<td>' + basicEscape(v.cusName) + '</td>';
                    } else {
                        str += '<td>' + basicEscape(v.cusName) + '</td>';
                    }
                }
                str += '<td>' + NVL(v.vano) + '</td>';
                if (NVL(v.disabled) == 'N') {
                    str += '<td>정상</td>';
                } else {
                    str += '<td class="text-danger">삭제</td>';
                }
                str += '<td>' + NVL(v.cusKey) + '</td>';
                if ('${orgSess.cusGubn1}' != null && '${orgSess.cusGubn1}' != '') {
                    str += '<td>' + basicEscape(NVL(v.cusGubn1)) + '</td>';
                }
                if ('${orgSess.cusGubn2}' != null && '${orgSess.cusGubn2}' != '') {
                    str += '<td>' + basicEscape(NVL(v.cusGubn2)) + '</td>';
                }
                if ('${orgSess.cusGubn3}' != null && '${orgSess.cusGubn3}' != '') {
                    str += '<td>' + basicEscape(NVL(v.cusGubn3)) + '</td>';
                }
                if ('${orgSess.cusGubn4}' != null && '${orgSess.cusGubn4}' != '') {
                    str += '<td>' + basicEscape(NVL(v.cusGubn4)) + '</td>';
                }
                if (NVL(v.rcpGubn) == 'N') {
                    str += '<td class="text-danger">제외</td>';
                } else {
                    str += '<td>대상</td>';
                }
                str += '<td>' + NVL(v.cusHp) + '</td>';
                str += '<td>' + NVL(v.cusMail) + '</td>';
                if (v.cusType == '1') {
                    str += '<td class="hidden hidden-01">소득공제</td>';
                } else if (v.cusType == '2') {
                    str += '<td class="hidden hidden-01">지출증빙</td>';
                } else {
                    str += '<td class="hidden hidden-01"></td>';
                }
                if (v.confirm == '11') {
                    str += '<td class="hidden hidden-01">휴대폰번호</td>';
                } else if (v.confirm == '12') {
                    str += '<td class="hidden hidden-01">현금영수증카드번호</td>';
                } else if (v.confirm == '13') {
                    str += '<td class="hidden hidden-01">주민번호</td>';
                } else if (v.confirm == '21') {
                    str += '<td class="hidden hidden-01">사업자번호</td>';
                } else {
                    str += '<td class="hidden hidden-01"></td>';
                }
                str += '<td class="hidden hidden-01">' + NVL(v.cusOffNo) + '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);

        if ($('#tableCheckbox1').is(":checked")) {
            $("#tableCheckbox1").click();
            $("#tableCheckbox1").click();
        }
    }

    //고객정보수정
    function fnCusInfoUpdate(vano) {
        fn_reset_scroll();
        modify_payer_information_init("U", vano);
        $('#modify-payer-information').modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    // validation
    function fnValidation() {

        var stDt = replaceDot($('#SRCHcalDateFrom').val());
        var edDt = replaceDot($('#SRCHcalDateTo').val());

        if ($('#searchDate').val() != 0) {
            var vdm = dateValidity(stDt, edDt);
            if (vdm != 'ok') {
                swal({
                    type: 'info',
                    text: vdm,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    // 납부 대상 check
    function fn_payClick() {
        if ($('#payY').is(':checked') == true && $('#payN').is(':checked') == true) {
            $('#payAll').prop('checked', true);
        } else {
            $('#payAll').prop('checked', false);
        }
    }

    // 고객상태 check
    function fn_statClick() {
        if ($('#statY').is(':checked') == true && $('#statN').is(':checked') == true) {
            $('#statAll').prop('checked', true);
        } else {
            $('#statAll').prop('checked', false);
        }
    }

    //파일저장
    function fn_fileSave() {
        fncClearTime();
        var rowCnt = $('#totCnt').text();

        if (rowCnt == 0) {
            swal({
                type: 'info',
                text: '다운로드할 데이터가 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (fnValidation()) {
            swal({
                type: 'question',
                html: "다운로드 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    //납부대상
                    var payList = [];
                    var check = $("input[name='payItem']:checked");
                    check.map(function (i) {
                        if ($(this).val() != null && $(this).val() != '') {
                            payList.push($(this).val());
                        }
                    });
                    // 고객상태
                    var cusList = [];
                    var check = $("input[name='statItem']:checked");
                    check.map(function (i) {
                        if ($(this).val() != null && $(this).val() != '') {
                            cusList.push($(this).val());
                        }
                    });

                    $('#startDate').val($('#SRCHcalDateFrom').val().replace(/\./gi, ""));
                    $('#endDate').val($('#SRCHcalDateTo').val().replace(/\./gi, ""));
                    $('#searchGb').val($('#SRCHsearchGb option:selected').val());
                    $('#searchValue').val($('#SRCHsearchValue').val());
                    $('#payList').val(payList);
                    $('#cusList').val(cusList);
                    // $('#monList').val(monList);
                    $('#cusGubn').val($('#selCusGubn option:selected').val());
                    $('#cashShow').val($("input[id=tableCheckbox1]:checked").val());
                    $('#cusGubnValue').val($('#SRCHcusGubnValue').val());
                    $('#searchOrderBy').val($('#cSearchOrderBy option:selected').val());

                    document.fileForm.action = "/org/custMgmt/excelSaveCustList";
                    document.fileForm.submit();
                }
            });
        }
    }

    // 페이징 버튼
    function list(page) {
        fnSearch(page);
    }

    function pageChange() {
        cuPage = 1;
        fnSearch(cuPage);
    }

    function NVL(value) {

        if (value == "" || value == null || value == undefined
            || ( value != null && typeof value == "object" && !Object.keys(value).length )) {
            return "";
        } else {
            return value;
        }
    }

    //기간별 조회 - 기간선택
    function fn_selMonth(val) {

        $('#searchDate').val(val);
        var toDate = $.datepicker.formatDate("yy.mm.dd", new Date());
        if (val != 0) {	// 전체아닐떄
            $('#SRCHcalDateTo').val(toDate);
            $('#SRCHcalDateFrom').val(monthAgo(toDate, val));
            var vdm = dateValidity($('#SRCHcalDateFrom').val(), $('#SRCHcalDateTo').val());
            if (vdm != 'ok') {
                swal({
                    type: 'info',
                    text: vdm,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return false;
            } else {
                $('#SRCHcalDateFrom').attr('disabled', false);
                $('#SRCHcalDateTo').attr('disabled', false);


                $("button[name=btnMonth]").removeClass('active');
                $('#pMonth' + val).addClass('active');
            }

        } else {
            $('#SRCHcalDateFrom').attr('disabled', true);
            $('#SRCHcalDateTo').attr('disabled', true);

            $('#SRCHcalDateFrom').val('');
            $('#SRCHcalDateTo').val('');

            $("button[name=btnMonth]").removeClass('active');
            $('#pMonthAll').addClass('active');
        }
    }

    function fn_listCheck() {
        var stList = [];
        var idx = 0;
        var num = 0;

        var check = $("input[name='checkOne']");
        check.map(function (i) {
            if ($(this).is(':checked') == true) {
                idx++;
            }
            num++;
        });

        if (num == idx) {
            $('#row-th').prop('checked', true);
        } else {
            $('#row-th').prop('checked', false);
        }
    }

    // 검색조건 > 숫자형 자릿수 체크
    function fn_selStr() {
        if ($('#SRCHsearchGb option:selected').val() == "vano") {
            var str = $("#SRCHsearchValue").val();
            var str2 = str.split(',');
            for (var i = 0; i < str2.length; i++) {
                if ($.isNumeric(str2[i]) && (str2[i].length < 2 || str2[i].length > 4)) {
                    swal({
                        type: 'info',
                        text: "가상계좌는 최소 2자리이상 입력하셔야 합니다.()",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    return;
                }
            }
        }
    }
</script>

<input type="hidden" id="curPage" name="curPage"/>

<form id="fileForm" name="fileForm" method="post">
	<input type="hidden"  name ="startDate"     id="startDate" 		/>
	<input type="hidden"  name ="endDate"    	id="endDate" 		/>
	<input type="hidden"  name ="searchGb"      id="searchGb"    	/>
	<input type="hidden"  name ="searchValue"   id="searchValue"    />
	<input type="hidden"  name ="payList"    	id="payList" 		/>
	<input type="hidden"  name ="cusList"    	id="cusList" 		/>
	<%--<input type="hidden"  name ="monList"    	id="monList" 		/>--%>
	<input type="hidden"  name ="cusGubn"    	id="cusGubn" 	    />
	<input type="hidden"  name ="cashShow"    	id="cashShow" 	    />
	<input type="hidden"  name ="cusGubnValue"  id="cusGubnValue"   />
	<input type="hidden"  name ="searchOrderBy" id="searchOrderBy"  />
</form>

<input type="hidden"  name ="searchDate"     id="searchDate" 		/>

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link" href="#">고객등록</a> <a class="nav-link active"
				href="#">고객조회</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div id="breadcrumb-in-title-area" class="row align-items-center">
				<div class="col-12 text-right">
					<img src="/assets/imgs/common/icon-home.png" class="mr-2">
					<span> > </span> <span class="depth-1">고객관리</span> <span> >
					</span> <span class="depth-2 active">고객조회</span>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2>고객조회</h2>
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
					<p>고객등록 메뉴에서 입력한 고객정보를 조회 및 수정, 삭제, 납부대상 여부를 변경 할 수 있는 화면입니다.</p>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<form class="search-box" >
			<div class="row">
				<label class="col-form-label col col-md-1 col-sm-3 col-3">등록일자</label>
				<div class="col col-md-4 col-sm-8 col-8 form-inline">
					<div class="date-input">
						<div class="input-group">
							<input type="text" class="form-control date-picker-input" id="SRCHcalDateFrom" aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this);">
						</div>
					</div>
					<span class="range-mark"> ~ </span>
					<div class="date-input">
						<div class="input-group">
							<input type="text" class="form-control date-picker-input" id="SRCHcalDateTo" aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this);">
						</div>
					</div>
				</div>
				<div class="col col-md-6 col-sm-9 offset-md-0 offset-sm-2 offset-3">
					<button type="button" class="btn btn-sm btn-preset-month active" id="pMonthAll" name="btnMonth" onclick="fn_selMonth(0)">전체</button>
					<button type="button" class="btn btn-sm btn-preset-month" id="pMonth1" name="btnMonth" onclick="fn_selMonth(1)">1개월</button>
					<button type="button" class="btn btn-sm btn-preset-month" id="pMonth6" name="btnMonth" onclick="fn_selMonth(6)">6개월</button>
				</div>
			</div>

			<div class="row">
				<label class="col-form-label col col-md-1 col-sm-3 col-3">검색구분 </label>
				<div class="col col-md-5 col-sm-9 col-9 form-inline">
					<select class="form-control" name="SRCHsearchGb" id="SRCHsearchGb">
						<option value="cusname">고객명</option>
						<option value="regGb">고객번호</option>
						<option value="vano">가상계좌</option>
						<option value="hpno">연락처</option>
					</select>
					<input class="form-control" type="text" name="SRCHsearchValue"  id="SRCHsearchValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." maxlength="30" onkeypress="if(event.keyCode == 13){fnSearch();}"/>
				</div>
				<label class="col-form-label col col-md-1 col-sm-3 col-3 hidden-on-mobile">납부대상 </label>
				<div class="col col-md-5 col-sm-9 col-9 form-inline hidden-on-mobile">
					<div class="form-inline">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payAll" value="">
							<label class="form-check-label" for="payAll"><span class="mr-1"></span>전체</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payY" value="Y" onclick="fn_payClick();">
							<label class="form-check-label" for="payY"><span class="mr-1"></span>대상</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payN" value="N" onclick="fn_payClick();">
							<label class="form-check-label" for="payN"><span class="mr-1"></span>제외</label>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<label class="col-form-label col col-md-1 col-sm-3 col-3">고객구분 </label>
				<div class="col col-md-5 col-sm-9 col-9 form-inline">
					<select class="form-control" id="selCusGubn" name="selCusGubn" >
						<option value="all">전체</option>
						<c:forEach var="cusGubn" items="${map.cusGbList}">
							<option value="${cusGubn.code}">${cusGubn.codeName}</option>
						</c:forEach>
					</select>
					<input class="form-control" type="text" id="SRCHcusGubnValue"  name="SRCHcusGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." maxlength="30" onkeypress="if(event.keyCode == 13){fnSearch();}"/>
				</div>
				<label class="col-form-label col col-md-1 col-sm-3 col-3">고객상태 </label>
				<div class="col col-md-5 col-sm-9 col-9 form-inline">
					<div class="form-inline">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="statItem"	id="statAll" value="">
								<label class="form-check-label" for="statAll"><span class="mr-1"></span>전체</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="statItem"	id="statN" value="N" onclick="fn_statClick();">
							<label class="form-check-label" for="statN"><span	class="mr-1"></span>정상</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="statItem"	id="statY" value="Y" onclick="fn_statClick();">
							<label class="form-check-label" for="statY"><span class="mr-1"></span>삭제</label>
						</div>
					</div>
				</div>
			</div>

			<div class="row mt-3">
				<div class="col-12 text-center">
					<input type="button" class="btn btn-primary btn-wide" onclick="fnSearch('1');" value="조회" />
				</div>
			</div>
	    </form>
	</div>

	<div class="container">
		<div id="search-result" class="list-id">
			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2" >조회결과 [총 <em class="font-blue" id="totCnt">${map.count}</em>건]</span>
					<div class="form-check form-check-inline ml-2">
						<input class="form-check-input" type="checkbox" id="tableCheckbox1" value="Y"> 
						<label for="tableCheckbox1"><span class="mr-2"></span>현금영수증 발급정보 표시</label>
					</div>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<select class="form-control" name="cSearchOrderBy" id="cSearchOrderBy" onchange="pageChange();">
						<option value="regDt">등록일자순 정렬</option>
						<option value="cusName">고객명순 정렬</option>
						<option value="state">고객상태별 정렬</option>
					</select>
					<select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
						<option value="10">10개씩 조회</option>
						<option value="20">20개씩 조회</option>
						<option value="50">50개씩 조회</option>
						<option value="100">100개씩 조회</option>
						<option value="200">200개씩 조회</option>
					</select>
					<button class="btn btn-sm btn-d-gray hidden-on-mobile" type="button" onclick="fn_fileSave();">파일저장</button>
				</div>
			</div>

			<div class="row">
				<div class="table-responsive col mb-3 pd-n-mg-o">
					<table class="table table-sm table-hover table-primary">
						<thead>
							<tr>
								<th class="hidden-on-mobile"><input class="form-check-input table-check-parents" type="checkbox" name="row-th"  id="row-th" > <label for="row-th"><span></span></label></th>
								<th>NO</th>
								<th>등록일자</th>
								<th>고객명</th>
								<th>가상계좌</th>
								<th>고객상태</th>
								<th>고객번호</th>
								<c:if test="${orgSess.cusGubn1 != null && orgSess.cusGubn1 != '' }"><th>${orgSess.cusGubn1}</th></c:if>
								<c:if test="${orgSess.cusGubn2 != null && orgSess.cusGubn2 != '' }"><th>${orgSess.cusGubn2}</th></c:if>
								<c:if test="${orgSess.cusGubn3 != null && orgSess.cusGubn3 != '' }"><th>${orgSess.cusGubn3}</th></c:if>
								<c:if test="${orgSess.cusGubn4 != null && orgSess.cusGubn4 != '' }"><th>${orgSess.cusGubn4}</th></c:if>
								<th>납부대상</th>
								<th>연락처</th>
								<th class="border-r-e6">이메일</th>
								<th class="hidden hidden-01" width="110">발급용도</th>
								<th class="hidden hidden-01" width="110">발급방법</th>
								<th class="hidden hidden-01" width="120">발급번호</th>
							</tr>
						</thead>

						<tbody id="resultBody">
							<c:choose>
								<c:when test="${map.count > 0}">
									<c:forEach var="row" items="${map.list}" varStatus="status">
										<tr>
											<td class="hidden-on-mobile"><input class="form-check-input table-check-child" name="checkOne" type="checkbox" id="row-${row.rn}" value="${row.vano}" onclick="fn_listCheck();" onchange="changeBGColor(this)"/>
												<label for="row-${row.rn}"><span></span></label>
												<input type="hidden" id="chaCd"    value="${row.chaCd}"    />
												<input type="hidden" id="vano"     value="${row.vano}"     />
												<input type="hidden" id="masKey"   value="${row.masKey}"   />
												<input type="hidden" id="cusMail"  value="${row.cusMail}"  />
												<input type="hidden" id="rcpGubn"  value="${row.rcpGubn}"  />
												<input type="hidden" id="disabled" value="${row.disabled}" />
												<input type="hidden" id="rcpReqTy" value="${row.rcpReqTy}" />
											</td>
											<td>${row.rn}</td>
											<td>${row.regDt}</td>
											<td><c:out value="${row.cusName}" escapeXml="true" /></td>
											<td>${row.vano}</td>
											<c:if test="${row.disabled == 'N' }"><td>정상</td></c:if>
											<c:if test="${row.disabled == 'Y' }"><td class="text-danger">삭제</td></c:if>
											<c:if test="${row.disabled != 'N' && row.disabled != 'Y'}"><td class="text-danger">임시</td></c:if>
											<td>${row.cusKey}</td>
											<c:if test="${orgSess.cusGubn1 != null && orgSess.cusGubn1 != '' }"><td>${row.cusGubn1}</td></c:if>
											<c:if test="${orgSess.cusGubn2 != null && orgSess.cusGubn2 != '' }"><td>${row.cusGubn2}</td></c:if>
											<c:if test="${orgSess.cusGubn3 != null && orgSess.cusGubn3 != '' }"><td>${row.cusGubn3}</td></c:if>
											<c:if test="${orgSess.cusGubn4 != null && orgSess.cusGubn4 != '' }"><td>${row.cusGubn4}</td></c:if>
											<c:if test="${row.rcpGubn != 'N' }"><td>대상</td></c:if>
											<c:if test="${row.rcpGubn == 'N' }"><td class="text-danger">제외</td></c:if>
											<td>${row.cusHp}</td>
											<td>${row.cusMail}</td>
											<td class="hidden hidden-01">${row.cusType}</td>
											<td class="hidden hidden-01">${row.cusMtd}</td>
											<td class="hidden hidden-01">${row.cusOffNo}</td>
										</tr>
									</c:forEach>
								</c:when>

								<c:otherwise>
									<tr>
										<td colspan="16" style="text-align: center;">[조회된 내역이 없습니다.]</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>

			<div class="row mb-3 hidden-on-mobile">
				<div class="col-6">
					<button type="button" class="btn btn-sm btn-gray-outlined btn-delete-payer-information">선택삭제</button>
					<button type="button" class="btn btn-sm btn-gray-outlined btn-except-payer-information">납부제외</button>

					<a tabindex="0" class="popover-dismiss" role="button" data-toggle="popover" data-trigger="focus"
					   title="납부제외란?" data-content="개별 고객 정보는 유지하면서 신규 청구를 제외하여 납부 일시정지 처리하는 기능">
						<img src="/assets/imgs/common/icon-info.png">
					</a>
				</div>
				<div class="col-6 text-right">
					<button type="button" class="btn btn-sm btn-d-gray btn-open-modify-payer-info">정보수정</button>
				</div>
			</div>

			<jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />
		</div>
	</div>
	<div class="container">
		<div id="quick-instruction" class="foldable-box">
			<div class="row foldable-box-header">
				<div class="col-8">
					<img src="/assets/imgs/common/icon-notice.png"> 알려드립니다.
				</div>
				<div class="col-4 text-right">
					<img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
				</div>
			</div>
			<div class="row foldable-box-body">
				<div class="col">
					<h6>■ 고객조회 및 정보수정</h6>
					<ul>
						<li>고객명을 클릭하여 상세 화면 팝업 창에서 고객 정보 조회</li>
						<li>수정하고자 하는 정보 입력 후 저장버튼 클릭</li>
					</ul>

					<h6>■ 고객삭제</h6>
					<ul>
						<li>더 이상 거래를 하지 않는 고객을 삭제 처리</li>
						<li>고객상태는 '정상'과 '삭제' 두 가지가 있음</li>
						<li>고객삭제 시 더 이상 청구 및 가상계좌 입금이 불가하나 기존 발생된 이력은 조회 가능</li>
						<li>고객삭제 방법 : 고객 선택 후 표 하단 좌측의 '선택삭제' 버튼 클릭</li>
					</ul>

					<h6>■ 납부제외</h6>
					<ul>
						<li>일시적으로 거래를 하지 않을 경우 납부제외 처리하여 이용요금 과금 대상에서 제외</li>
						<li>납부대상 여부에 따라 '납부대상'과 '납부제외'의 두 가지 구분이 있음</li>
						<li>납부제외 시 해당 가상계좌로 입금 불가하며 청구 수수료 대상에서 제외</li>
						<li>납부제외 방법 : 고객 선택 후 하단 좌측의 '납부제외' 버튼 클릭</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<%-- 고객정보수정 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/modify-payer-information.jsp" flush="false"/>

<%-- 고객상세보기 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/detail-payer-information.jsp" flush="false"/>
