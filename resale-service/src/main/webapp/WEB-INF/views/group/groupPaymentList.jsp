<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/group/header.jsp" flush="false"/>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript">
    var firstDepth = "nav-link-38";
    var secondDepth = false;

    var cuPage = 1;
    var moPage = 1;

    var masMonth = '';

    $(document).ready(function () {
        getYearsBox('yearsBox');
        getMonthBox('monthBox');
        cusGubnBox('PcusGubn');

        fn_selMonth(1);
        fn_Search();

        $("#monthBox").val("").prop("selected", true);
    });

    function fnValidation() {
        var stDt = replaceDot($('#PstartDate').val());
        var edDt = replaceDot($('#PendDate').val());

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

    function fn_selMonth(val) {
        $('#searchDate').val(val);
        var toDate = $.datepicker.formatDate("yy.mm.dd", new Date());

        if (val != 0) {	// 전체아닐떄
            $('#PendDate').val(toDate);
            $('#PstartDate').val(monthAgo(toDate, val));

            var vdm = dateValidity($('#PstartDate').val(), $('#PendDate').val());
            if (vdm != 'ok') {
                swal({
                    type: 'info',
                    text: vdm,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return false;
            } else {
                $('#PstartDate').attr('disabled', false);
                $('#PendDate').attr('disabled', false);

                $("button[name=btnMonth]").removeClass('active');
                $('#pMonth' + val).addClass('active');
            }
        } else {
            $('#PstartDate').attr('disabled', true);
            $('#PendDate').attr('disabled', true);

            $('#PstartDate').val('');
            $('#PendDate').val('');

            $("button[name=btnMonth]").removeClass('active');
            $('#pMonthAll').addClass('active');
        }
    }

    function onCusGubn(obj) {
        if (obj.value == 'all') {
            $('#cusGubnValue').attr('placeholder', '콤마(,) 구분자로 다중검색이 가능합니다.');
        } else {
            $('#cusGubnValue').attr('placeholder', '콤마(,) 구분자로 다중검색이 가능합니다.');
        }
    }

    function cusGubnBox(obj) {
        var str = "<option value='all'>전체</option>";
        str += "<option value='cusGubn1'>고객구분1</option>";
        str += "<option value='cusGubn2'>고객구분2</option>";
        str += "<option value='cusGubn3'>고객구분3</option>";
        str += "<option value='cusGubn4'>고객구분4</option>";
        $('#' + obj).html(str);
    }

    /**
     * 조회
     */
    function fn_Search(page) {
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        if ($('#yearsBox option:selected').val() != "" && $('#monthBox option:selected').val() != "") {
            masMonth = $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val();
        } else {
            masMonth = '';
        }

        if (fnValidation()) {
            var url = "/group/ajaxGroupPaymentList";
            var param = {
                startDate: $('#PstartDate').val().replace(/\./gi, ""),                                                   // 입금일자 시작일
                endDate: $('#PendDate').val().replace(/\./gi, ""),                                                       // 입금일자 마지막일
                masMonth: masMonth,                                                                                      // 청구월
                chaCd: $("#PchaCode").val(),                                                                             // 가맹점코드
                searchGb: $('#SRCHsearchGb option:selected').val(),                                                     // 검색구분
                searchValue: $('#SRCHsearchValue').val(),                                                               // 검색구분 검색값
                cusGubn: $('#PcusGubn option:selected').val(),                                                           // 고객구분
                cusGubnValue: $('#SRCHcusGubnValue').val(),                                                             // 고객구분 검색값
                searchSveCd: $('#PsearchSveCd option:selected').val(),                                                   // 입금구분
                searchOrderBy : $('#PsearchOrderBy option:selected').val(),                                              // order By
                curPage: cuPage
            }

            $.ajax({
                type: "post",
                async: true,
                url: url,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (result) {
                    fnGrid(result);
                    ajaxPaging(result, 'PageArea');
                }
            })
        }
    }

    function fnGrid(data) {
        $('#count').text(numberToCommas(data.count));
        $('#totalAmt').text(numberToCommas(data.totalCnt));

        var str = '';
        if (data.count <= 0) {
            str += '<tr><td colspan="16" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(data.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + moment(v.payDay, 'YYYYMMDD').format('YYYY.MM.DD') + '</td>';
                str += '<td>' + moment(v.masMonth, 'YYYYMM').format('YYYY.MM') + '</td>';
                str += '<td>' + v.chaCd + '</td>';
                str += '<td>' + v.chaName + '</td>';
                str += '<td>' + v.cusname + '</td>';
                str += '<td>' + v.cuskey + '</td>';
                str += '<td>' + v.cusgubn1 + '</td>';
                str += '<td>' + v.cusgubn2 + '</td>';
                str += '<td>' + v.cusgubn3 + '</td>';
                str += '<td>' + v.cusgubn4 + '</td>';
                str += '<td>' + v.vano + '</td>';

                if (v.sveCd == '온라인카드') {
                    if (v.rcpMasSt == 'PA03') {
                        var nowDays = new Date();
                        var payDays = moment(v.payDay, 'YYYYMMDD').subtract(1, 'month').format('YYYYMMDD');
                        var gapDays = moment(nowDays, 'YYYYMMDD').diff(payDays, 'day');

                        str += '<td>' + v.sveCd + '</td>';
                        if (gapDays <= 60) {
                            str += '<button type="button" class="btn btn-xs btn-outline-secondary ml-2 mr-2" onclick=fnRefund("' + v.rcpMasCd + '")>결제취소</button>';
                        }
                        str += '</td>';
                    } else if (v.rcpMasSt == 'PA09') {
                        str += '<td>' + v.sveCd + '&nbsp;취소완료</td>';
                    }
                } else {
                    str += '<td>' + v.sveCd + '</td>';
                }

                str += '<td>' + v.rcpusrname + '</td>';
                str += '<td>' + numberToCommas(v.rcpamt || 0) + '</td>';
                str += '</tr>';
            });
        }
        $('#resultBody').html(str);
    }

    function list(page) {
        fn_Search(page);
    }

    /**
     * 기관검색
     */
    function fn_orgSearch() {
        $("#popChacd").val($('#cusCd').val());
        $("#popChaname").val($('#chaname').val());
        $("#lookup-collecter-popup").modal({
            backdrop: 'static',
            keyboard: false
        });
        fn_ListCollector();
    }

    /**
     * 카드 결제 취소
     */
    function fnRefund(rcpMasCd) {
        swal({
            type: 'question',
            text: '신용카드 결제내역을 취소하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var strFeature = "width=700, height=500, scrollbars=no, resizable=yes";
                document.cancelCardForm.target = "formInfo";
                document.cancelCardForm.cancelRcpMasCd.value = rcpMasCd;
                window.open("", "formInfo", strFeature);
                document.cancelCardForm.submit();
            }
        });
    }

    /**
     * 엑셀 파일 저장
     */
    function fn_fileSave() {
        if ($('#yearsBox option:selected').val() != "" && $('#monthBox option:selected').val() != "") {
            masMonth = $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val();
        } else {
            masMonth = '';
        }

        var rowCnt = $("#count").text();
        if (rowCnt == 0) {
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
        }).then(function (result) {
            if (result.value) {

                $("#startDate").val($('#PstartDate').val().replace(/\./gi, ""));
                $("#endDate").val($('#PendDate').val().replace(/\./gi, ""));
                $("#masMonth").val(masMonth);
                $("#chaCd").val($("#PchaCode").val());
                $("#searchGb").val($('#SRCHsearchGb option:selected').val());
                $("#searchValue").val($('#SRCHsearchValue').val());
                $("#cusGubn").val($('#PcusGubn option:selected').val());
                $("#cusGubnValue").val($('#SRCHcusGubnValue').val());
                $("#searchSveCd").val($('#PsearchSveCd option:selected').val());
                $("#searchOrderBy").val($('#PsearchOrderBy option:selected').val());

                document.fileForm.action = "/group/excelSave";
                document.fileForm.submit();
            }
        });
    }

    // validation
    function fnValidation() {
        var fromDate = moment($("#PstartDate").val(), 'YYYYMMDD').format('YYYYMMDD');
        var toDate = moment($("#PendDate").val(), 'YYYYMMDD').format('YYYYMMDD');

        if (fromDate > toDate) {
            swal({
                type: 'info',
                text: '정확한 날짜를 선택해주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }
        return true;
    }
</script>

</div>

<form name="cancelCardForm" id="cancelCardForm" method="post" action="/org/receiptMgnt/selectRcpMas">
    <input type="hidden" id="cancelRcpMasCd" name="cancelRcpMasCd"/>
</form>

<input type="hidden" name="searchDate" id="searchDate"/>

<form id="fileForm" name="fileForm" method="post">
    <input type="hidden" id="startDate" name="startDate"/>
    <input type="hidden" id="endDate" name="endDate"/>
    <input type="hidden" id="masMonth" name="masMonth"/>
    <input type="hidden" id="chaCd" name="chaCd"/>
    <input type="hidden" id="searchGb" name="searchGb"/>
    <input type="hidden" id="searchValue" name="searchValue"/>
    <input type="hidden" id="cusGubn" name="cusGubn"/>
    <input type="hidden" id="cusGubnValue" name="cusGubnValue"/>
    <input type="hidden" id="searchSveCd" name="searchSveCd"/>
    <input type="hidden" id="searchOrderBy" name="searchOrderBy"/>
</form>

<div id="contents" class="group-contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link active" href="#">입금내역조회</a>
        </nav>
    </div>

    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                    <span> > </span> <span class="depth-1">입금내역조회</span>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <h2>입금내역조회</h2>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>기관 그룹 입금내역조회 화면입니다.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <form class="search-box">
            <div class="row">
                <label class="col-form-label col col-md-1 col-sm-3 col-3">입금일자</label>
                <div class="col col-md-4 col-sm-8 col-8 form-inline">
                    <div class="date-input">
                        <div class="input-group">
                            <input type="text" class="form-control date-picker-input" id="PstartDate" aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this);">
                        </div>
                    </div>
                    <span class="range-mark"> ~ </span>
                    <div class="date-input">
                        <div class="input-group">
                            <input type="text" class="form-control date-picker-input" id="PendDate" aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this);">
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
                <label class="col-form-label col col-md-1 col-sm-3 col-3">청구월 </label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline year-month-dropdown">
                    <select class="form-control" id="yearsBox">
                        <option value="">선택</option>
                    </select>
                    <span class="ml-1 mr-1">년</span>
                    <select class="form-control" id="monthBox">
                        <option value="">선택</option>
                    </select>
                    <span class="ml-1 mr-auto">월</span>
                </div>

                <label class="col-form-label col col-md-1 col-sm-3 col-3">기관코드</label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline">
                    <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="PchaCode" id="PchaCode" maxlength="50">
                    <span class="input-group-btn">
                        <button class="btn btn-sm btn-primary" type="button" onclick="fn_orgSearch();">기관검색</button>
                    </span>
                </div>
            </div>

            <div class="row">
                <label class="col-form-label col col-md-1 col-sm-3 col-3">검색구분 </label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline">
                    <select class="form-control" name="SRCHsearchGb" id="SRCHsearchGb">
                        <option value="cusname">고객명</option>
                        <option value="regGb">고객번호</option>
                        <option value="vano">가상계좌</option>
                    </select>
                    <input class="form-control" type="text" name="SRCHsearchValue" id="SRCHsearchValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." maxlength="30" onkeypress="if(event.keyCode == 13){fn_Search();}"/>
                </div>
                <label class="col-form-label col col-md-1 col-sm-3 col-3">고객구분 </label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline">
                    <select class="form-control" id="PcusGubn"></select>
                    <div class="input-with-magnet">
                        <input class="form-control" type="text" id="SRCHcusGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fn_Search();}"/>
                    </div>
                </div>
            </div>

            <div class="row">
                <label class="col-form-label col col-md-1 col-sm-3 col-3">입금구분 </label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline">
                    <select class="form-control col" name="PsearchSveCd" id="PsearchSveCd">
                        <option value="">전체</option>
                        <option value="VAS">가상계좌</option>
                        <option value="DCS">현금</option>
                        <option value="DCD">오프라인카드</option>
                        <option value="OCD">온라인카드</option>
                    </select>
                </div>
                <label class="col-form-label col col-md-1 col-sm-3 col-3"></label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline">
                </div>
            </div>

            <div class="row mt-3">
                <div class="col-12 text-center">
                    <input type="button" class="btn btn-primary btn-wide" onclick="fn_Search();" value="조회"/>
                </div>
            </div>
        </form>
    </div>

    <div class="container">
        <div id="search-result" class="list-id">
            <div class="table-option row mb-2">
                <div class="col-md-6 col-sm-12 form-inline">
                    <span class="amount mr-2">입금건수 [총 <em class="font-blue" id="count">0</em>건]</span>
                    <span class="amount mr-2">입금금액 [총 <em class="font-blue" id="totalAmt">0</em>원]</span>
                </div>
                <div class="col-md-6 col-sm-12 text-right mt-1">
                    <select class="form-control" name="PsearchOrderBy" id="PsearchOrderBy" onchange="fn_Search();">
                        <option value="cusName">고객명순 정렬</option>
                        <option value="chaCd">기관코드순 정렬</option>
                        <option value="pay">입금일자순 정렬</option>
                    </select>

                    <button class="hidden-on-mobile btn btn-sm btn-d-gray" type="button" onclick="fn_fileSave()">파일저장</button>
                </div>
            </div>

            <div class="row">
                <div class="table-responsive col mb-3 pd-n-mg-o">
                    <table class="table table-sm table-hover table-primary">
                        <thead>
                            <tr>
                                <th>NO</th>
                                <th>입금일자</th>
                                <th>청구월</th>
                                <th>기관코드</th>
                                <th>기관명</th>
                                <th>고객명</th>
                                <th>고객번호</th>
                                <th>고객구분1</th>
                                <th>고객구분2</th>
                                <th>고객구분3</th>
                                <th>고객구분4</th>
                                <th>가상계좌번호</th>
                                <th>입금구분</th>
                                <th>입금자명</th>
                                <th>입금액(원)</th>
                            </tr>
                        </thead>

                        <tbody id="resultBody">
                        </tbody>
                    </table>
                </div>
            </div>

            <jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false"/>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>

<%-- 가맹점검색 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-group.jsp" flush="false"/>
