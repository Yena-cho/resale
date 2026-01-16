<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<script>
    var firstDepth = "nav-link-32";
    var secondDepth = "sub-02";
    var delMessage = '선택된 청구건을 취소하시겠습니까?';
    var noSelectMsg = '청구건을 선택해주세요.';
    var modMessage = '청구건을 선택해주세요.';
    var selGb;
    var allCancelYN = false;

    $(document).ready(function(){
        $('.popover-dismiss').popover({
            trigger: 'focus'
        });

        var cuPage = 1; // 청구목록 현재페이지

        //고객구분 select 초기 설정
        if($('#selCusGubn option').index(0)){
            $(this).val('all');
        };

        // 그리드 전체선택, 전체해제
        $("#row-th").click(function(){ //만약 전체 선택 체크박스가 체크된상태일경우
            if($("#row-th").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("#mainBody input[type=checkbox]").prop("checked",true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("#mainBody input[type=checkbox]").prop("checked",false);
            }
        });

        //청구상태 전체 체크, 해제
        $('#inqNotiMasStAll').click(function() {
            if ($('#inqNotiMasStAll').prop('checked')) {
                $('input:checkbox[name=inqNotiMasSt]').prop('checked', true);
            } else {
                $('input:checkbox[name=inqNotiMasSt]').prop('checked', false);
            }
        });

        var month = '${map.claimMonth}';

        // 조회조건 - 월별
        getYearsBox2('claimYear');
        getMonthBox('claimMonth');
        $('#claimYear').val(month.substring(0, 4));
        $('#claimMonth').val(month.substring(4, 6));
        // 조회조건 - 기간별
        $('#inqStartDt').val(getFormatCurrentDate());
        $('#inqEndDt').val(getFormatCurrentDate());

        $('#inqAmtSum').text(numberToCommas('${map.payAmtSum}'));

        // 청구상태
        $('#inqNotiMasStN').prop('checked', true);

        $('#payRadio1').prop('checked', true);
        $('input:checkbox[name=payItem]').prop('checked', true);

        $('#payRadio1').click(function () {
            if ($('#payRadio1').prop('checked')) {
                $('input:checkbox[name=payItem]').prop('checked', true);
            } else {
                $('input:checkbox[name=payItem]').prop('checked', false);
            }
        });

        $('input:checkbox[name=payItem]').click(function () {
            if ($('#payRadio1').is(":checked")) {
                $('#payRadio1').prop("checked", false);
                $(this).prop("checked", false);
            }
            if ($('input:checkbox[name=payItem]').length == $('input:checkbox[name=payItem]:checked').length) {
                $('#payRadio1').prop("checked", true);
            }
        });

        fn_selMonth(1);
        fn_ClaimListCall();
    });

    // 기간별 조회 - 기간선택
    function fn_selMonth(val) {

        if((!$('#inlineRadio1').is(':checked') && !$('#inlineRadio2').is(':checked'))) {
            $('#inlineRadio1').attr('checked', true);
        }
        var vdm = dateValidity($('#inqStartDt').val(), $('#inqEndDt').val());
        var endDate = $.datepicker.formatDate("yy.mm.dd", new Date() );

        $('#inqEndDt').val(endDate);
        $('#inqStartDt').val(monthAgo(endDate, val));

        $("button[name=btnMonth]").removeClass('active');
        $('#pMonth' + val).addClass('active');
    }

    //고객구분 selectBox
    function cusGubnBox(obj) {
        var str = "<option value='all'>전체</option>";

        if ('${orgSess.cusGubn1}' != null && '${orgSess.cusGubn1}' != '') {
            str += "<option value='cusGubn1'>${orgSess.cusGubn1}</option>";
        }
        if ('${orgSess.cusGubn2}' != null && '${orgSess.cusGubn2}' != '') {
            str += "<option value='cusGubn2'>${orgSess.cusGubn2}</option>";
        }
        if ('${orgSess.cusGubn3}' != null && '${orgSess.cusGubn3}' != '') {
            str += "<option value='cusGubn3'>${orgSess.cusGubn3}</option>";
        }
        if ('${orgSess.cusGubn4}' != null && '${orgSess.cusGubn4}' != '') {
            str += "<option value='cusGubn4'>${orgSess.cusGubn4}</option>";
        }
        $('#' + obj).html(str);
    }

    function fn_ClaimListCall(page) {
        allCancelYN = false;

        fn_ClaimListCall_internal(page, allCancelYN, function(data){
            $('#row-th').prop('checked', false);
            if (data.retCode == "0000") {
                fnGrid(data, 'mainBody');		// 현재 데이터로 셋팅
                ajaxPaging(data, 'PageArea');
            } else {
                swal({
                    type: 'error',
                    text: data.retMsg,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            }
        });
    }

    // 조회
    function fn_ClaimListCall_internal(page, allCancelYN, callback) {
        if(page == null || page == 'undefined'){
            cuPage = '1';
        } else {
            cuPage = page;
        }

        var selGb;
        if($("#inlineRadio2").is(":checked")) {  //기간별
            selGb = "D";
        } else {
            selGb = "M";
        }

        /**
         *  수납상태
         **/
        var checkArr = [];
        $('input:checkbox[name=payItem]:checked').each(function (i) {
            checkArr.push($(this).val());
        });

        if($("#inlineRadio1").is(":checked")) { // 청구월 조회
            if(!$('#claimYear').val() || !$('#claimMonth').val()) {
                swal({
                    type: 'info',
                    text: '조회할 청구월을 확인해주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        var stDt = $('#inqStartDt').val().replace(/\./gi,"");
        var edDt = $('#inqEndDt').val().replace(/\./gi,"");
        if($("#inlineRadio2").is(":checked")) { // 청구월 기간별 조회

            var vdm = dateValidity(stDt, edDt);
            if (vdm != 'ok'){
                swal({
                    type: 'info',
                    text: vdm,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        var strList = [];
        var check = $("input[name='inqNotiMasSt']:checked");
        check.map(function(i) {
            if($(this).val() != null && $(this).val() != '') {
                strList.push($(this).val());
            }
        });

        if($('#inqCusGubn option:selected').val() == "vano" && $("#inqKeyWord").val()) {
            var str = $("#inqKeyWord").val();
            var s = str.split(",");
            for (var i = 0; i < s.length; i++) {
                if (($.isNumeric(s[i]) && (s[i].length < 2 || s[i].length > 4)) && s[i].length != 14) {
                    swal({
                        type: 'info',
                        html: "가상계좌번호 14자리 모두 입력 또는 <p>가상계좌 끝 4자리 중 최소 2자리 이상, 4자리 이하로 입력해 주세요.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    return;
                }
                if (!$.isNumeric(s[i])) {
                    swal({
                        type: 'info',
                        text: "가상계좌는 숫자형식으로만 입력가능합니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#inqKeyWord").val('');
                    });
                    return;
                }
            }
        }

        var url = "/org/claimMgmt/claimSelAjax";
        if(allCancelYN){
            var strList = [];
            var check = $('#ac_strList').val();
            if(check == null || check == ""){
                strList = ["N", "Y"];
            }else{
                strList = check.split(",");
            }

            var checkArr = [];
            var check2 = $('#ac_payList').val();
            if(check2 == null || check2 == ""){
                checkArr = ["PA02","PA04","PA05","PA03","PA06"];
            }else{
                checkArr = check2.split(",");
            }

			$('#ac_selGb').val(selGb);
			$('#ac_masMonth').val($('#claimYear').val() + $('#claimMonth').val());
			$('#ac_curPage').val(cuPage);
			$('#ac_notiMasSt').val('PA02');
			$('#ac_masStDt').val($('#inqStartDt').val().replace(/\./gi, ""));
			$('#ac_masEdDt').val($('#inqEndDt').val().replace(/\./gi, ""));
			$('#ac_cusGubn').val($('#inqCusGubn option:selected').val());
			$('#ac_searchValue').val($('#inqKeyWord').val());
			$('#ac_seachCusGubn').val($('#selCusGubn option:selected').val());
			$('#ac_cusGubnValue').val($('#SRCHcusGubnValue').val());
			$('#ac_search_orderBy').val($('#searchOrderBy option:selected').val());
			$('#ac_pageScale').val($('#PAGE_SCALE option:selected').val());

            var param = {
                selGb:  $('#ac_selGb').val(),
                masMonth: $('#ac_masMonth').val(),
                curPage: $('#ac_curPage').val(),
                notiMasSt: $('#ac_notiMasSt').val(),
                masStDt: $('#ac_masStDt').val(),
                masEdDt: $('#ac_masEdDt').val(),
                cusGubn: $('#ac_cusGubn').val(),
                searchValue: $('#ac_searchValue').val(),
                seachCusGubn: $('#ac_seachCusGubn').val(),
                cusGubnValue: $('#ac_cusGubnValue').val(),
                strList: strList,
                search_orderBy: $('#ac_search_orderBy').val(),
                pageScale: $('#ac_pageScale').val(),
                payList: checkArr
            };
        }else{
            $('#ac_selGb').val(selGb);
            $('#ac_masMonth').val($('#claimYear').val() + $('#claimMonth').val());
            $('#ac_curPage').val(cuPage);
            $('#ac_notiMasSt').val('PA02');
            $('#ac_masStDt').val($('#inqStartDt').val().replace(/\./gi, ""));
            $('#ac_masEdDt').val($('#inqEndDt').val().replace(/\./gi, ""));
            $('#ac_cusGubn').val($('#inqCusGubn option:selected').val());
            $('#ac_searchValue').val($('#inqKeyWord').val());
            $('#ac_seachCusGubn').val($('#selCusGubn option:selected').val());
            $('#ac_cusGubnValue').val($('#SRCHcusGubnValue').val());
            $('#ac_strList').val(strList);
            $('#ac_search_orderBy').val($('#searchOrderBy option:selected').val());
            $('#ac_pageScale').val($('#PAGE_SCALE option:selected').val());
            $('#ac_payList').val(checkArr);

            var param = {
                selGb: selGb,	// 조회구분
                masMonth: $('#claimYear').val() + $('#claimMonth').val(), // 청구월
                curPage: cuPage,
                notiMasSt: 'PA02',
                masStDt: $('#inqStartDt').val().replace(/\./gi, ""),
                masEdDt: $('#inqEndDt').val().replace(/\./gi, ""),
                cusGubn: $('#inqCusGubn option:selected').val(), 	// 검색 selectbox
                searchValue: $('#inqKeyWord').val(),
                seachCusGubn: $('#selCusGubn option:selected').val(), 	// 고객구분 selectbox
                cusGubnValue: $('#SRCHcusGubnValue').val(),
                strList: strList,
                search_orderBy: $('#searchOrderBy option:selected').val(),
                pageScale: $('#PAGE_SCALE option:selected').val(),
                payList: checkArr
            };
        }
        $.ajax({
            type : "post",
            async : true,
            url : url,
            contentType : "application/json; charset=UTF-8",
            data : JSON.stringify(param),
            success: callback
        });
    }

    // 조회조건체크
    function fn_vilidationCk() {
        if($("#inlineRadio1").is(":checked")) { // 청구월 조회
            if(!$('#claimYear').val() || $('#claimMonth').val()) {
                swal({
                    type: 'info',
                    text: '조회할 청구월을 확인해주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }
        var stDt = $('#inqStartDt').val().replace(/\./gi,"");
        var edDt = $('#inqEndDt').val().replace(/\./gi,"");
        if($("#inlineRadio2").is(":checked")) { // 청구월 기간별 조회
            if((!stDt || !edDt) || (stDt.length < 8 || edDt.length < 8)) {
                swal({
                    type: 'info',
                    text: '조회할 기간별 일자를 확인해주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            if(stDt > edDt) {
                swal({
                    type: 'info',
                    text: '조회할 기간별 일자를 확인해주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }
    }

    //청구 조회
    function fnGrid(result, obj) {
        var str = '';
        var col = 12 + result.cusGbCnt;
        $('#inqCnt').text(numberToCommas(result.count));
        $('#inqAmtSum').text(numberToCommas(result.payAmtSum));

        if(result.count <= 0){
            str += '<tr><td colspan="'+ col +'" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        }else{
            $.each(result.list, function(k, v){
                str += '<tr>';
                str += '<input type="hidden" id="masCd" name="masCd" value="' + v.notiMasCd + '">';
                str += '<td class="hidden-on-mobile"><input class="form-check-input table-check-child" type="checkbox" name="checkOne" id="row-' + v.rn + '" value="' + v.notiMasCd + '" onclick="fn_listCheck();" onchange="changeBGColor(this)">';
                str += '<label for="row-' + v.rn + '"><span></span></label></td>';
                str += '<td>'+ v.rn +'</td>';
                str += '<td>'+ nullValueChange(v.masMonth) +'</td>';
                str += '<td>'+ nullValueChange(v.masDay) +'</td>';
                str += '<td><a href="#" onclick="fnDetail(\'' + v.vano + '\');">' + basicEscape(v.cusName) + '</a></td>';
                if(result.cusGbCnt == 1 || result.cusGbCnt > 1) {
                    str += '<td>'+ basicEscape(nullValueChange(v.cusGubn1)) +'</td>';
                }
                if(result.cusGbCnt == 2 || result.cusGbCnt > 2) {
                    str += '<td>'+ basicEscape(nullValueChange(v.cusGubn2)) +'</td>';
                }
                if(result.cusGbCnt == 3 || result.cusGbCnt > 3) {
                    str += '<td>'+ basicEscape(nullValueChange(v.cusGubn3)) +'</td>';
                }
                if(result.cusGbCnt == 4 || result.cusGbCnt > 4) {
                    str += '<td>'+ basicEscape(nullValueChange(v.cusGubn4)) +'</td>';
                }
                str += '<td>'+ nullValueChange(v.vano) +'</td>';
                str += '<td><a href="#" onclick="fn_detailView(\'' + v.notiMasCd + '\', \'' + v.masMonth + '\', \'' + v.cusName + '\', \'' + v.vano + '\');">'+ v.cnt +'건</a></td>';
                str += '<td class="form-td-inline" style="text-align: right;">';
                str += '<span class="ml-1">' + numberToCommas(v.payItemAmt) + '</span>';
                str += '</td>';
                str += '<input type="hidden" id="masSt" name="masSt" value="' + v.notiMasSt + '">';
                str += '<input type="hidden" id="month" name="month" value="' + nullValueChange(v.masMonth) + '">';
                str += '<input type="hidden" id="notiCanYn" name="notiCanYn" value="' + v.notiCanYn + '">';
                str += '<input type="hidden" id="cusDisabled" name="cusDisabled" value="' + v.disabled + '">';
                if(v.notiMasSt == 'PA00') {
                    str += '<td>임시</td>';
                } else if(v.notiMasSt == 'PA02') {
                    str += '<td>미납</td>';
                } else if(v.notiMasSt == 'PA03') {
                    str += '<td>완납</td>';
                } else if(v.notiMasSt == 'PA04') {
                    str += '<td>일부납</td>';
                } else if(v.notiMasSt == 'PA05') {
                    str += '<td>초과납</td>';
                } else if(v.notiMasSt == 'PA06') {
                    str += '<td>환불</td>';
                }
                str += '<td>'+ nullValueChange(v.startDate) + '~' + nullValueChange(v.endDate) +'</td>';
                str += '<td>'+ nullValueChange(v.printDate)+'</td>';
                if(v.notiCanYn == 'Y') {
                    str += '<td>취소</td>';
                } else {
                    str += '<td>정상</td>';
                }
                if(v.disabled == 'Y') {
                    str += '<td>삭제</td>';
                } else {
                    str += '<td>정상</td>';
                }
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    function list(page) {
        $("#pageNo").val(page);
        fn_ClaimListCall(page);
    }

    // 청구 취소
    function fn_cancel() {
        var rowCnt = 0;
        var cancelCnt  = 0;
        var cancelYCnt = 0;
        var claimDelVals = [];
        var status = [];	// 납부상태
        var cancel = [];
        var checkbox = $("input[name='checkOne']:checked");
        checkbox.each(function(i) {
            var tr = checkbox.parent().parent().eq(i);
            var td = tr.children();

            status.push(td.eq(td.length-5).text().replace(/\s/gi, ""));
            cancel.push(td.eq(td.length-4).text().replace(/\s/gi, ""));
            claimDelVals.push($(this).val());
            rowCnt++;

            if(td.eq(td.length-4).text() == '정상'){
                cancelYCnt++;
            }
            if(td.eq(td.length-4).text() == '취소') {
                cancelCnt++;
            }
        });

        if(status.indexOf("납부") != -1 || status.indexOf("일부납") != -1 || status.indexOf("초과납") != -1 || status.indexOf("완납") != -1) {
            swal({
                type: 'info',
                text: '납부한 청구는 취소할 수 없습니다. 다시 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (status.indexOf("환불") != -1) {
			swal({
				type: 'info',
				text: '환불등록한 청구는 취소할 수 없습니다. 다시 확인해 주세요.',
				confirmButtonColor: '#3085d6',
				confirmButtonText: '확인'
			});
        	return;
		}

        if(cancelYCnt > 0 && cancelCnt > 0) {
            swal({
                type: 'info',
                text: '이미 취소된 청구항목이 있습니다. 다시 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if(rowCnt == cancelCnt && (rowCnt > 0)) {
            swal({
                type: 'info',
                text: '이미 취소된 청구입니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if(rowCnt == 0) {
            swal({
                type: 'info',
                text: '취소할 청구건을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
        } else {
            swal({
                type: 'question',
                html: "취소하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function(result) {
                if (result.value) {
                    // 삭제
                    var url = "/org/claimMgmt/claimCancel";
                    var param = {
                        itemList : claimDelVals
                    };
                    $.ajax({
                        type: "post",
                        url: url,
                        data: param,
                        success: function(result){
                            if(result.retCode == '0000') {
                                swal({
                                    type: 'success',
                                    text: '취소하였습니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                }).then(function(result) {
                                    if (result.value) {
                                        fn_ClaimListCall(1);
                                    }
                                });
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
            });
        }
    }

    // 일괄취소
    function fn_cancelAll() {
        page = null;
        allCancelYN = true;

        fn_ClaimListCall_internal(page, allCancelYN, function(data){
            if(data.cancelCount == 0){
                swal({
                    type: 'question',
                    text: "일괄 취소 가능한 미납 청구 건이 없습니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인',
                });
                return;
            }

            swal({
                type: 'question',
                html: "조회된 미납 ["+data.cancelCount+"]건의 청구를 일괄 취소하시겠습니까?",
                content: "input",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function(result) {
                if (!result.value) {
                    return;
                }

                var url = "/org/claimMgmt/claimAllCancel";

                if($("#inlineRadio2").is(":checked")) {  //기간별
                    selGb = "D";
                } else {
                    selGb = "M";
                }
				var strList = [];
				var check = $('#ac_strList').val();
				if(check == null || check == ""){
					strList = ["N", "Y"];
				}else{
					strList = check.split(",");
				}

				var checkArr = [];
				var check2 = $('#ac_payList').val();
				if(check2 == null || check2 == ""){
					checkArr = ["PA02","PA04","PA05","PA03","PA06"];
				}else{
					checkArr = check2.split(",");
				}

                if(selGb == "D"){
                    var param = {
                        masStDt : $('#ac_masStDt').val(),
                        masEdDt : $('#ac_masEdDt').val(),
                        masMonth : null,
						cusGubn: $('#ac_cusGubn').val(),
						searchValue: $('#ac_searchValue').val(),
						seachCusGubn: $('#ac_seachCusGubn').val(),
						cusGubnValue: $('#ac_cusGubnValue').val(),
						strList: strList,
						payList: checkArr
                    };
                }else{
                    var param = {
                        masStDt : null,
                        masEdDt : null,
                        masMonth : $('#ac_masMonth').val(),
						cusGubn: $('#ac_cusGubn').val(),
						searchValue: $('#ac_searchValue').val(),
						seachCusGubn: $('#ac_seachCusGubn').val(),
						cusGubnValue: $('#ac_cusGubnValue').val(),
						strList: strList,
						payList: checkArr
                    };
                }

                $.ajax({
                    type : "post",
                    async : true,
                    url : url,
                    contentType : "application/json; charset=UTF-8",
                    data : JSON.stringify(param),
                    success: function(result){
                        swal({
                            type: 'success',
                            html: '미납 ['+data.cancelCount+']건의 청구가 일괄 취소되었습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function(result) {
                            if (result.value) {
                                fn_ClaimListCall();
                            }
                        });
                    },
                    error:function(result){
                        swal({
                            type: 'error',
                            text: '실패하였습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        })
                    }
                });
            });
        });
    }

    // 파일저장
    function fn_fileSave() {
        fncClearTime();
        var rowCnt = 0;
        var checkbox = $("input[name='checkOne']");
        checkbox.map(function(i) {
            rowCnt++;
        });
        if(rowCnt == 0) {
            swal({
                type: 'info',
                text: '다운로드할 데이터가 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            type: 'question',
            html: "다운로드 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function(result) {
            if (result.value) {
                if($("#inlineRadio2").is(":checked")) {  //기간별
                    $('#selGb').val('D');
                } else {
                    $('#selGb').val('M');
                }

                $('#masMonth').val($('#claimYear').val() + $('#claimMonth').val());
                $('#notiMasSt').val('PA02');
                $('#masStDt').val($('#inqStartDt').val().replace(/\./gi,""));
                $('#masEdDt').val($('#inqEndDt').val().replace(/\./gi,""));
                $('#cusGubn').val($('#inqCusGubn option:selected').val());
                $('#searchValue').val($('#inqKeyWord').val());
                $('#seachCusGubn').val($('#selCusGubn option:selected').val());
                $('#cusGubnValue').val($('#SRCHcusGubnValue').val());

                var checkArr = [];
                $('input:checkbox[name=payItem]:checked').each(function (i) {
                    checkArr.push($(this).val());
                });
                $('#payList').val(checkArr);

                var strList = [];
                var check = $("input[name='inqNotiMasSt']:checked");
                check.map(function(i) {
                    if($(this).val() != null && $(this).val() != '') {
                        strList.push($(this).val());
                    }
                });
                $('#strList').val(strList);
                $('#search_orderBy').val($('#searchOrderBy option:selected').val());

                // 다운로드
                document.fileForm.action = "/org/claimMgmt/excelSave";
                document.fileForm.submit();
            }
        });
    }

    function fn_listCheck() {
        var stList = [];
        var idx = 0;
        var num = 0;

        var check = $("input[name='checkOne']");
        check.map(function(i) {
            if($(this).is(':checked') == true) {
                idx++;
            }
            num++;
        });

        if(num == idx) {
            $('#row-th').prop('checked', true);
        } else{
            $('#row-th').prop('checked', false);
        }
    }

    function fn_masStClick() {
        if($('#inqNotiMasStY').is(':checked') == true && $('#inqNotiMasStN').is(':checked') == true) {
            $('#inqNotiMasStAll').prop('checked', true);
        } else {
            $('#inqNotiMasStAll').prop('checked', false);
        }
    }

    function fn_doModalDetailView(result) {
        var str = '';
        var totalPayAmt = 0;
        var totalRcpAmt = 0;

        if (result.count <= 0) {
            str += '<tr><td colspan="4" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + basicEscape(v.payItemName) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.payItemAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.rcpAmt) + '</td>';
                str += '<td>' + (v.ptrItemRemark == null ? "" : basicEscape(v.ptrItemRemark)) + '</td>';
                str += '</tr>';
            });

            var foot = "";
            foot += '<tr>';
            foot += '<td class="summary-bg">총액</td>';
            foot += '<td class="summary-bg" style="text-align: right;">' + numberToCommas(result.totalPayAmt) + '</td>';
            foot += '<td class="summary-bg" style="text-align: right;">' + numberToCommas(result.totalRcpAmt) + '</td>';
            foot += '<td class="summary-bg"></td>';
            foot += '</tr>';
            $('#popResultFoot').html(foot);
        }
        $('#popResultBody').html(str);

        $("#popup-payment-unit-list").modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    // 항목건수 선택시, 청구항목 상세보기 이벤트
    function fn_detailView(notiMasCd, masMonth, cusName, vano) {
        fn_reset_scroll();

        var url = "/org/claimMgmt/payItemDetailView";
        var param = {
            notiMasCd : notiMasCd
        };
        $.ajax({
            type: "post",
            url: url,
            contentType : "application/json; charset=utf-8",
            data : JSON.stringify(param),
            success: function(result){
                if(result.retCode == '0000'){
                    $('#popCusName').text(basicUnEscape(cusName));
                    $('#popMasMonth').text(masMonth);
                    $('#popMasVano').text(vano);

                    fn_doModalDetailView(result);
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

</script>

<form id="fileForm" name="fileForm" method="post">
	<input type="hidden" id="notiMasSt" name="notiMasSt" >
	<input type="hidden" id="selGb" 	name="selGb" >
	<input type="hidden" id="masMonth" 	name="masMonth">
	<input type="hidden" id="masStDt" 	name="masStDt" >
	<input type="hidden" id="masEdDt" 	name="masEdDt" >
	<input type="hidden" id="cusGubn" 	name="cusGubn" >
	<input type="hidden" id="searchValue" 	name="searchValue" >
	<input type="hidden" id="seachCusGubn"	name="seachCusGubn" >
	<input type="hidden" id="cusGubnValue" name="cusGubnValue">
	<input type="hidden" id="strList"   name="strList" >
	<input type="hidden" id="payList"   name="payList" >
	<input type="hidden" id="search_orderBy" name="search_orderBy" >
</form>

<form id="allCancel" name="allCancel" method="post">
	<input type="hidden" id="ac_selGb" 	name="ac_selGb" >
	<input type="hidden" id="ac_masMonth" 	name="ac_masMonth">
	<input type="hidden" id="ac_curPage" 	name="ac_curPage">
	<input type="hidden" id="ac_notiMasSt" name="ac_notiMasSt" >
	<input type="hidden" id="ac_masStDt" 	name="ac_masStDt" >
	<input type="hidden" id="ac_masEdDt" 	name="ac_masEdDt" >
	<input type="hidden" id="ac_cusGubn" 	name="ac_cusGubn" >
	<input type="hidden" id="ac_searchValue" 	name="ac_searchValue" >
	<input type="hidden" id="ac_seachCusGubn"	name="ac_seachCusGubn" >
	<input type="hidden" id="ac_cusGubnValue" name="ac_cusGubnValue">
	<input type="hidden" id="ac_strList"   name="ac_strList" >
	<input type="hidden" id="ac_search_orderBy" name="ac_search_orderBy" >
	<input type="hidden" id="ac_pageScale" name="ac_pageScale" >
	<input type="hidden" id="ac_payList" name="ac_payList" >
</form>

<input type="hidden" id="pageNo"  name="pageNo"  value="1">

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link active" href="#">청구조회</a>
			<a class="nav-link" href="#">청구등록</a>
			<a class="nav-link" href="#">청구항목관리</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div id="breadcrumb-in-title-area" class="row align-items-center">
				<div class="col-12 text-right">
					<img src="/assets/imgs/common/icon-home.png" class="mr-2">
					<span> > </span> <span class="depth-1">청구관리</span>
					<span> > </span> <span class="depth-2 active">청구조회</span>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2>청구조회</h2>
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
					<p>등록된 청구내역 조회 및 청구취소를 할 수 있는 화면입니다.</p>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="search-box">
			<form>
				<div id="monthly-or-duration" class="row radio-selecter">
					<legend class="col-form-label col col-md-1 col-sm-2 col-2 pt-0">조회일자</legend>
					<div class="col col-md-11 col-sm-10 col-10">
						<div class="form-check form-check-inline">
							<input class="form-check-input lookup-by-month" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="M">
							<label class="form-check-label" for="inlineRadio1"><span></span>청구월별</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input lookup-by-range" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="D">
							<label class="form-check-label" for="inlineRadio2"><span></span>청구일자별</label>
						</div>
					</div>
				</div>
				<div id="lookup-by-month" class="row">
					<div class="col-md-11 offset-md-1 offset-sm-2 offset-2 col-10 form-inline year-month-dropdown">
						<select class="form-control" name="claimYear" id="claimYear">
							<option value="">선택</option>
						</select>
						<span class="ml-1 mr-1">년</span>
						<select class="form-control"  name="claimMonth" id="claimMonth">
							<option value="">선택</option>
						</select>
						<span class="ml-1">월</span>
					</div>
				</div>
				<div id="lookup-by-range" class="row" style="display: none;">
					<div class="col col-md-5 col-sm-10 offset-md-1 offset-sm-2 offset-2 form-inline">
						<div class="date-input">
							<div class="input-group">
								<input type="text" id="inqStartDt" class="form-control date-picker-input" placeholder="YYYY.MM.DD"
									   aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
							</div>
						</div>
						<span class="range-mark"> ~ </span>
						<div class="date-input">
							<div class="input-group">
								<input type="text" id="inqEndDt" class="form-control date-picker-input" placeholder="YYYY.MM.DD"
									   aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
							</div>
						</div>
					</div>
					<div class="col-md-6 col-sm-10 offset-md-0 offset-sm-2 offset-2">
						<button type="button" class="btn btn-sm btn-preset-month active" id="pMonth1" name="btnMonth" onclick="fn_selMonth(1)">1개월</button>
						<button type="button" class="btn btn-sm btn-preset-month" id="pMonth2" name="btnMonth" onclick="fn_selMonth(2)">2개월</button>
						<button type="button" class="btn btn-sm btn-preset-month" id="pMonth3" name="btnMonth" onclick="fn_selMonth(3)">3개월</button>
					</div>
				</div>

				<div class="row mb-3 mt-3">
					<div class="col" style="border-top: 1px solid #d6d6d6;"></div>
				</div>

				<div class="row">
					<label class="col col-md-1 col-sm-2 col-2 col-form-label">
						검색구분
					</label>
					<div class="col col-md-5 col-sm-10 col-10 form-inline">
						<select class="form-control col-auto" id="inqCusGubn">
							<option value="cusName">고객명</option>
							<option value="vano">가상계좌</option>
						</select>
						<input class="form-control col-auto" type="text" id="inqKeyWord" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fn_ClaimListCall();}">
					</div>
					<label class="col col-md-1 col-sm-2 col-2 col-form-label">청구상태</label>
					<div class="col col-md-5 col-sm-10 col-10 form-inline">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="inqNotiMasSt" id="inqNotiMasStAll" value="">
							<label class="form-check-label" for="inqNotiMasStAll"><span class="mr-1"></span>전체</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="inqNotiMasSt" id="inqNotiMasStN" value="N" onclick="fn_masStClick();">
							<label class="form-check-label" for="inqNotiMasStN"><span class="mr-1"></span>정상</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="inqNotiMasSt" id="inqNotiMasStY" value="Y" onclick="fn_masStClick();">
							<label class="form-check-label" for="inqNotiMasStY"><span class="mr-1"></span>취소</label>
						</div>
					</div>
				</div>
				<div class="row">
					<label class="col col-md-1 col-sm-2 col-2 col-form-label">고객구분</label>
					<div class="col col-md-5 col-sm-10 col-10 form-inline">
						<select class="form-control" id="selCusGubn" name="selCusGubn" >
							<option value="all">전체</option>
							<c:forEach var="cusGubn" items="${map.cusGbList}">
								<option value="${cusGubn.code}">${cusGubn.codeName}</option>
							</c:forEach>
						</select>
						<input class="form-control" type="text" id="SRCHcusGubnValue"  name="SRCHcusGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." maxlength="30" onkeypress="if(event.keyCode == 13){fn_ClaimListCall();}"/>
					</div>

					<label class="col col-md-1 col-sm-2 col-2 col-form-label">수납상태</label>
					<div class="col col-md-5 col-sm-10 col-10 form-inline">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payAll" id="payRadio1" value="ALL">
							<label class="form-check-label" for="payRadio1"><span class="mr-1"></span>전체</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio2" value="PA02">
							<label class="form-check-label" for="payRadio2"><span class="mr-1"></span>미납</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio3" value="PA04">
							<label class="form-check-label" for="payRadio3"><span class="mr-1"></span>일부납</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio4" value="PA05">
							<label class="form-check-label" for="payRadio4"><span class="mr-1"></span>초과납</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio5" value="PA03">
							<label class="form-check-label" for="payRadio5"><span class="mr-1"></span>완납</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="payItem" id="payRadio6" value="PA06">
							<label class="form-check-label" for="payRadio6"><span class="mr-1"></span>환불</label>
						</div>
					</div>
				</div>
				<div class="row form-inline mt-3">
					<div class="col-12 text-center">
						<button type="button" class="btn btn-primary btn-wide" onclick="fn_ClaimListCall();">조회</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<div class="container mb-3">
		<div id="list-of-result" class="list-id">
			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="inqCnt"><fmt:formatNumber pattern="#,###" value="${map.count}" /></em>건]</span>
					<span class="amount mr-2">등록금액 [총 <em class="font-blue" id="inqAmtSum"></em>원]</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="fn_ClaimListCall();">
						<option value="masDay"   >청구일자순 정렬</option>
						<option value="masMonth" >청구월순 정렬</option>
						<option value="cusName"  >고객명순 정렬</option>
						<option value="cusGubn"  >고객구분순 정렬</option>
						<option value="date"     >납부기한순 정렬</option>
					</select>
					<select class="form-control" name="PAGE_SCALE" id="PAGE_SCALE" onchange="fn_ClaimListCall();">
						<option value="10"  >10개씩 조회</option>
						<option value="20"  >20개씩 조회</option>
						<option value="50"  >50개씩 조회</option>
						<option value="100" >100개씩 조회</option>
						<option value="200">200개씩 조회</option>
					</select>
					<button class="btn btn-sm btn-d-gray hidden-on-mobile" type="button" onclick="fn_fileSave();">파일저장</button>
				</div>
			</div>
			<div class="row">
				<div class="table-responsive pd-n-mg-o col mb-3">
					<table class="table table-sm table-hover table-primary">
						<colgroup>
							<col class="hidden-on-mobile" width="52">
							<col width="68">
							<col width="100">
							<col width="100">
							<col width="100">
							<c:forEach var="cusGbList" items="${map.cusGbList}">
								<col width="130">
							</c:forEach>
							<col width="100">
							<col width="130">
							<col width="120">
							<col width="100">
							<col width="100">
							<col width="140">
							<col width="180">
						</colgroup>

						<thead>
						<tr>
							<th class="hidden-on-mobile">
								<input class="form-check-input table-check-parents" type="checkbox" id="row-th" >
								<label for="row-th">
									<span></span>
								</label>
							</th>
							<th>NO</th>
							<th>청구월</th>
							<th>청구일자</th>
							<th>고객명</th>
							<c:forEach var="cusGbList" items="${map.cusGbList}">
								<th>${cusGbList.codeName}</th>
							</c:forEach>
							<th>가상계좌</th>
							<th>항목건수</th>
							<th>청구금액(원)</th>
							<th>수납상태</th>
							<th>납부기한</th>
							<th>고지서용 표시마감일</th>
							<th>청구상태</th>
							<th>고객상태</th>
						</tr>
						</thead>
						<tbody id="mainBody">
						<c:set var="colCnt" value="${map.cusGbCnt + 11}"/>
						<tr>
							<td colspan="${colCnt}" style="text-align: center;">
								[조회된 내역이 없습니다.]
							</td>
						</tr>
						</tbody>
					</table>
				</div>
			</div>

			<div class="row list-button-group-bottom hidden-on-mobile">
				<div class="col-6">
					<button class="btn btn-sm btn-gray-outlined" onclick="fn_cancel();">선택취소</button>
					<button class="btn btn-sm btn-gray-outlined" onclick="fn_cancelAll();">일괄취소</button>

					<a tabindex="0" class="popover-dismiss" role="button" data-toggle="popover" data-trigger="focus"
					   title="일괄취소란?" data-content="대량 취소건 발생 시, 청구 월별 취소를 위한 기능">
						<img src="/assets/imgs/common/icon-info.png">
					</a>
				</div>
				<div class="col-6 text-right">
					<button class="btn btn-sm btn-gray-outlined" onclick="fn_update();">청구수정</button>
					<button class="btn btn-sm btn-gray-outlined" onclick="fn_updateDay();">납부기한수정</button>
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
					<img src="/assets/imgs/common/btn-arrow-notice.png"
						 class="fold-status">
				</div>
			</div>
			<div class="row foldable-box-body">
				<div class="col">
					<h6>■ 선택취소</h6>
					<ul>
						<li>기존 청구한 내역을 조회하여 삭제할 건을 선택취소</li>
					</ul>

					<h6>■ 일괄취소</h6>
					<ul>
						<li>조회된 청구내역 중 수납상태가 미납인 청구에 대해 일괄취소</li>
					</ul>

					<h6>■ 청구수정</h6>
					<ul>
						<li>기존 청구한 내역을 조회 하여 청구항목 및 금액에 대해 수정</li>
					</ul>

					<h6>■ 납부기한 일괄수정</h6>
					<ul>
						<li>기존 청구한 내역 중 선택한 건에 대해 수납기한 일괄수정</li>
					</ul>

					<ul>
						<li class="depth-2 text-danger">* 이미 입금완료 된 건 , 청구취소 건에 대해서는 취소 또는 수정불가 (위의 모든경우 동일)</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<%-- 일괄취소 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/block-cancel.jsp" flush="false" />

<%-- 고객상세보기 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/detail-payer-information.jsp" flush="false"/>

<%-- 개별청구등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/modify-charge.jsp" flush="false" />

<%-- 청구상세 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/payment-unit-list.jsp" flush="false" />

<%-- 개별청구수정 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/modify-charge.jsp" flush="false" />

<%-- 납부기한수정 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/modify-chargeDay.jsp" flush="false" />

<script type="text/javascript">
    function fn_update() {
        fn_reset_scroll();

        var curPage = $("#pageNo").val();
        var masCd;
        var masSt;
        var notiCanYn;
        var cusDisabled;
        var month;
        var rowSt = true;
        var rowCnt = 0;
        var checkbox = $("input[name='checkOne']:checked");
        checkbox.each(function (i) {
            var tr = checkbox.parent().parent().eq(i);
            rowCnt++;

            masCd = tr.find("input[name='masCd']").val();
            masSt = tr.find("input[name='masSt']").val();
            month = tr.find("input[name='month']").val();
            notiCanYn = tr.find("input[name='notiCanYn']").val();
            cusDisabled = tr.find("input[name='cusDisabled']").val();
        });

        if (!(masSt == 'PA02' || masSt == 'PA04')) {
            rowSt = 'masSt';
        } else if (notiCanYn == 'Y') {
            rowSt = 'notiCanYn';
        } else if (cusDisabled == 'Y') {
            rowSt = 'cusDisabled';
        }

        if (rowCnt == 0) {
            swal({
                type: 'info',
                text: '수정할 대상을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (rowSt == 'masSt') {
            swal({
                type: 'info',
                text: '미납/일부납건만 수정가능합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (rowSt == 'notiCanYn') {
            swal({
                type: 'info',
                text: '취소한 청구건에 대해서는 수정이 불가합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (rowSt == 'cusDisabled') {
            swal({
                type: 'info',
                text: '삭제된 고객에 대해서는 수정이 불가합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (rowCnt > 1) {
            swal({
                type: 'info',
                text: '수정할 대상을 하나만 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        // 청구등록 수정
        getYearsBox2('chargeYear');
        getMonthBox('chargeMonth');
        $('#chargeYear').val(month.substring(0, 4));
        $('#chargeMonth').val(month.substring(4, 6));

        fn_modifyCharge(masCd, 'N', curPage);
    }

    function fn_updateDay() {
        fn_reset_scroll();

        var curPage = $("#pageNo").val();
        var masCd;
        var rowCnt = 0;
        var rowSt = true;
        var checkbox = $("input[name='checkOne']:checked");
        var updateData = [];

        checkbox.each(function (i) {
            var tr = checkbox.parent().parent().eq(i);
            rowCnt++;

            var masSt = tr.find("input[name='masSt']").val();
            var notiCanYn = tr.find("input[name='notiCanYn']").val();
            var cusDisabled = tr.find("input[name='cusDisabled']").val();


            if ((masSt == 'PA02' || masSt == 'PA04') && notiCanYn != 'Y' && cusDisabled != 'Y') {
                masCd = tr.find("input[name='masCd']").val();
                updateData.push(masCd);
            } else {
                if (!(masSt == 'PA02' || masSt == 'PA04')) {
                    return rowSt = 'masSt';
                } else if (notiCanYn == 'Y') {
                    return rowSt = 'notiCanYn';
                } else if (cusDisabled == 'Y') {
                    return rowSt = 'cusDisabled';
                }
            }
        });

        if (rowCnt == 0) {
            swal({
                type: 'info',
                text: '수정할 대상을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (rowSt == 'masSt') {
            swal({
                type: 'info',
                text: '미납/일부납건만 수정가능합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (rowSt == 'notiCanYn') {
            swal({
                type: 'info',
                text: '취소한 청구건에 대해서는 수정이 불가합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (rowSt == 'cusDisabled') {
            swal({
                type: 'info',
                text: '삭제된 고객에 대해서는 수정이 불가합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else {
            fn_modifyDayCharge(updateData, curPage);
        }
    }
</script>
