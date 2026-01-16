<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet">
<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-5";
    var twoDepth = "adm-sub-10";
</script>

<script>
    var cuPage = 1;
    var currPage = 1;
    var toDay = getFormatCurrentDate();
    
    //금액 콤마
    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }

    function NVL(value) {
        if (value == "" || value == null || value == undefined
            || ( value != null && typeof value == "object" && !Object.keys(value).length )
        ) {
            return "";
        }
        else {
            return value;
        }
    }
  	//조회기간 제한 체크
    var monthValidation = function (sdate, edate) {
        if (edate < sdate) {
            swal({
                type: 'info',
                text: "조회 시작일이 종료일보다 큽니다. 날짜를 다시 선택해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }
        if (edate - sdate > 99) {
            swal({
                type: 'info',
                text: "최대 조회기간은 1년 입니다. 다시 선택해 주세요",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }
        return true;
    };
    // 일마감 조회
    function dayCloseSearch(page) {
    	var startday = $('#closeStartDt').val().toString().replace(/\./gi, "");
        var endday = $("#closeEndDt").val().toString().replace(/\./gi, "");
        var chacd = $('#chacd').val().toString().replace(/\./gi, "");
        
		//마감일 유형성 체크
        var vdm = dateValidity(startday, endday);
        if (vdm != 'ok'){
            swal({
                type: 'info',
                text: vdm,
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }
        
        if (page == null || page == 'undefined') {
            cuPage = 1;
        } else {
            cuPage = page;
        }

        var searchOrderBy = $("#dayClosesearchOrderBy").val();
        if ($("#dayClosesearchOrderBy").val() == null || $("#dayClosesearchOrderBy").val() == "") {
            searchOrderBy = "closeDt";
        } else {
            searchOrderBy = $("#dayClosesearchOrderBy").val();
        }
        
        param = {
            startDt: startday,
            endDt: endday,
            curPage: cuPage,
            pageScale: $('#dayClosePageScale option:selected').val(),
            chaCd: chacd,
            orderBy: searchOrderBy            
        };


        $.ajax({
            type: "post",
            async: true,
            url: "/sys/rcpMgmt/dayCloseListAjax",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {            	
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    sysajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'info',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });
        return false;
    }

    //ajax 값 grid로
    function fnGrid(result, obj) {
        var str = '';
        $('#pageCnt').val(result.PAGE_SCALE);
        $('#totCnt').text(result.count);

        if (result.rn <= 0) {
            str += '<tr><td colspan="16" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.closeDt.substring(0, 4) + '.' + v.closeDt.substring(4, 6) + '.' + v.closeDt.substring(6, 8) + '</td>';
                str += '<td class="title" title="' + v.chaName + '">' + v.chaName + '</td>';
                str += '<td class="title" title="' + v.chaCd + '">' + v.chaCd + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.notiAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.notiCnt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.rcpAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.rcpCnt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.ocdAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.ocdCnt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.cmsAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.cmsCnt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.dvaAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.dvaCnt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.smsCnt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.lmsCnt) + '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    function dayClosePageChange() {
        dayCloseSearch(cuPage);
    }

    function monthClosePageChange() {
        monthCloseSearch(cuPage);
    }

    function monthCloseArrayChange() {
        monthCloseSearch(currPage);
    }

    //페이징 버튼
    function list(page) {
        $('#pageNo').val(page);
        dayCloseSearch(page);
    }

    //페이징 버튼
    function list2(page) {
        monthCloseSearch(page);
    }

    // 일마감 실행
    function fnDayCloseGo() {
        var startday = $("#closeStartDt").val().replace(/\./gi, "");
        var endday = $("#closeEndDt").val().replace(/\./gi, "");
        if (startday > endday) {
            var temp = startday;
            startday = endday;
            endday = temp;
        }

        param = {
            startDt: startday,
            endDt: endday
        };


        $.ajax({
            type: "post",
            async: true,
            url: "/sys/rcpMgmt/dayCloseGo",
            data: param,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (result) {            	
                if (result.retCode == "0000") {
                    swal({
                        type: 'success',
                        text: '일마감 실행을 성공하였습니다...',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        if (result.value) {
                            dayCloseSearch();
                        }
                    });
                } else {
                    swal({
                        type: 'error',
                        text: '일마감 실행을 실패하였습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });
    }

    // 일마감 파일 저장
    function dayCloseFileSave() {
    	
        if ($('#totCnt').text() <= 0) {
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
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (!result.value) {
            	return;
            }
            
            $('#fStartDate').val(replaceDot($('#closeStartDt').val()));
            $('#fEndDate').val(replaceDot($('#closeEndDt').val()));
            $('#fOrderBy').val($('#dayClosesearchOrderBy option:selected').val());
            $('#fChacd').val($('#chacd').val());
            //$('#chaCd').val(); 고객번호
            
            // 다운로드
            $('#dayCloseFileForm').submit();
            
        });
    }

    //월마감 파일 저장
    function monthCloseFileSave() {
        var startMonth = $('#closeStartYear option:selected').val() + "" + $('#closeStrartMonth option:selected').val();
        var endMonth = $('#closeEndYear option:selected').val() + "" + $('#closeEndMonth option:selected').val();
        var chaCd = $('#chaCd').val().toString().replace(/\./gi, "");
        
        if ($('#monthCloseTotCnt').text() <= 0) {
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
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                $('#fMonthClosefDate').val(startMonth);
                $('#fMonthClosetDate').val(endMonth);
                $('#fChaCd').val($('#chaCd').val());
                $('#fMonthCloseOrderBy').val($('#monthClosesearchOrderBy option:selected').val());
                // 다운로드
                $('#monthCloseFileForm').submit();
            }
        });
    }

    // 월마감 조회
    function monthCloseSearch(page) {
        var startMonth = $('#closeStartYear option:selected').val() + "" + $('#closeStrartMonth option:selected').val();
        var endMonth = $('#closeEndYear option:selected').val() + "" + $('#closeEndMonth option:selected').val();
        var chaCd = $('#chaCd').val().toString().replace(/\./gi, "");
        
		if (!monthValidation(startMonth, endMonth)) {
            return;
        }

        if (CalcDay(startMonth + "00", endMonth + "00") > 365) {
            swal({
                type: 'info',
                text: "최대 조회기간은 1년 입니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (page == null || page == 'undefined') {
            currPage = 1;
        } else {
            currPage = page;
        }

        var searchOrderBy2 = $("#monthClosesearchOrderBy").val();
        if ($("#monthClosesearchOrderBy").val() == null || $("#monthClosesearchOrderBy").val() == "") {
            searchOrderBy2 = "closeMonth";
        } else {
            searchOrderBy2 = $("#monthClosesearchOrderBy").val();
        }

        var param = {
            fmasMonth: startMonth, //조회 시작년월
            tmasMonth: endMonth,     //조회 종료년월
            curPage: currPage,
            chaCd: chaCd,
            pageScale: $('#monthClosePageScale option:selected').val(),
            orderBy: searchOrderBy2
        };
        $.ajax({
            type: "post",
            async: true,
            url: "/sys/rcpMgmt/monthCloseListAjax",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    monthCloseFnGrid(result, 'monthCloseResultBody'); // 현재 데이터로 셋팅
                    sysajaxPagingE(result, 'ePageArea');
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
        
        return false;
    }

    //월마감 데이터 새로고침
    function monthCloseFnGrid(result, obj) {
        var str = '';
        $('#monthCloseTotCnt').text(result.monthCloseCount);
        if (result.rn <= 0) {
            str += '<tr><td colspan="16" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.monthCloseList, function (i, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.closeMonth.substring(0, 4) + '.' + v.closeMonth.substring(4, 6) + '</td>';
                str += '<td class="title" title="'+ v.chaName + '">' + v.chaName + '</td>';
                str += '<td class="title" title="' + v.chaCd + '">' + v.chaCd + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.notiAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.notiCnt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.notiFee) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.rcpAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.rcpCnt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.rcpFee) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.rcpBnkFee) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.smsAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.smsCnt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.lmsAmt) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(v.lmsCnt) + '</td>';
                str += '<td>' + v.finishDt + '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    //월마감 실행
    function fnMonthCloseGo() {
        var startMonth = $('#closeStartYear option:selected').val() + "" + $('#closeStrartMonth option:selected').val();
        var endMonth = $('#closeEndYear option:selected').val() + "" + $('#closeEndMonth option:selected').val();

        if (startMonth > endMonth) {
            var temp = startMonth;
            startMonth = endMonth;
            endMonth = temp;
        }

        var param = {
            fmasMonth: startMonth,  //조회 시작년월
            tmasMonth: endMonth,    //조회 종료년월
            chaCd: chaCd			//조회 기관코드
        };

        $.ajax({
            type: "post",
            url: "/sys/rcpMgmt/monthCloseGo",
            data: param,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (result) {
                swal({
                    type: 'success',
                    text: '월마감 실행을 성공하였습니다..',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function (result) {
                    if (result.value) {
                        monthCloseSearch();
                    }
                });
            },
            error: function (result) {
                swal({
                    type: 'error',
                    text: '월마감 실행을 실패하였습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
            }
        });
    }
</script>

<input type="hidden" id="totCnt" name="totCnt"/>
<input type="hidden" id="monthCloseTotCnt" name="monthCloseTotCnt"/>

<form id="dayCloseFileForm" name="dayCloseFileForm" method="post" action="/sys/rcpMgmt/dayCloseExcelDown">
    <input type="hidden" id="fStartDate" name="fStartDate"/>
    <input type="hidden" id="fEndDate" name="fEndDate"/>
    <input type="hidden" id="fOrderBy" name="fOrderBy"/>
    <input type="hidden" id="fChacd" name="fChacd"/>
</form>

<form id="monthCloseFileForm" name="monthCloseFileForm" method="post" action="/sys/rcpMgmt/monthCloseExcelDown">
    <input type="hidden" id="fMonthClosefDate" name="fMonthClosefDate"/>
    <input type="hidden" id="fMonthClosetDate" name="fMonthClosetDate"/>
    <input type="hidden" id="fMonthCloseOrderBy" name="fMonthCloseOrderBy"/>
    <input type="hidden" id="fChaCd" name="fChaCd"/>
</form>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>마감</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>정산관리</a>
            </li>
            <li class="active">
                <strong>마감</strong>
            </li>
        </ol>
    </div>
</div>

<input type="hidden" id="curPage" name="curPage"/>

<div class="wrapper wrapper-content">
    <div class="animated fadeInUp">
        <div class="row">
            <div class="col-lg-12 m-b-3">
                <div class="tabs-container">
                    <ul id="receiptClosing" class="nav nav-tabs" role="tablist">
                        <li role="tab" class="active"><a href="#closing-by-daily" aria-controls="closing-by-daily" role="tab" data-toggle="tab">일마감</a></li>
                        <li role="tab"><a href="#closing-by-monthly" aria-controls="closing-by-monthly" role="tab" data-toggle="tab">월마감</a></li>
                    </ul>

                    <div class="tab-content">
                        <div id="closing-by-daily" role="tabpanel" class="tab-pane fade in active">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-content">
                                            <p class="page-description">
                                                - 일별 마감을 실행하는 화면입니다.<br>
                                                - 일별 자동마감이 안되었거나, 재마감이 필요할 경우 사용합니다.
                                            </p>
                                        </div>
                                    </div>
                                </div>

                                <div class="animated fadeInRight article">
                                    <div class="col-lg-12">
                                        <div class="ibox float-e-margins">
                                            <div class="ibox-title">
                                                <h5>검색</h5>
                                            </div>

                                            <div class="ibox-content">
                                                <form onsubmit="return false;">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label class="form-label block">마감일</label>
                                                            <div class="form-group form-group-sm">
                                                                <div class="input-group col-md-12">
                                                                    <div class="input-daterange input-group float-left" id="datepicker">
                                                                        <input type="text" class="input-sm form-control" id="closeStartDt" name="closeStartDt" readonly="readonly"/>
                                                                        <span class="input-group-addon">to</span>
                                                                        <input type="text" class="input-sm form-control" id="closeEndDt" name="closeEndDt" readonly="readonly"/>
                                                                    </div>

                                                                    <div class="daterange-setMonth">
                                                                        <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth1" onclick="setMonthTerm(1);">1개월</button>
                                                                        <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth2" onclick="setMonthTerm(2);">2개월</button>
                                                                        <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth3" onclick="setMonthTerm(3);">3개월</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label class="form-label block">기관코드</label>
					                                        <div class="form-group form-group-sm">
					                                            <div class="input-group">
				                                                <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chacd" maxlength="50">
				                                                <span class="input-group-btn">
				                                                	<button class="btn btn-primary btn-lookup-collecter no-margins" type="button" onclick="searchChacd('day');">기관검색</button>
				                                            	</span>
				                                            </div>
					                                        </div>
                                                        </div>
                                                    </div>

                                                    <hr>

                                                    <div class="text-center">
                                                        <button class="btn btn-primary" onclick="dayCloseSearch('1')">조회</button>
                                                        <!-- 임시 버튼 비활성화 20180816
                                                        <button class="btn btn-primary" onclick="fnDayCloseGo()">마감실행</button>
                                                        -->
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="animated fadeInRight">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="ibox">
                                            <div class="ibox-title">
                                                <div class="col-lg-6"></div>

                                                <div class="col-lg-6 form-inline form-searchOrderBy">
                                                    <select class="form-control" id="dayClosesearchOrderBy" name="dayClosesearchOrderBy" onchange="dayCloseSearch(1)">
                                                        <option value="closeDt">마감일순 정렬</option>
                                                        <option value="chaName">기관명순 정렬</option>
                                                    </select>
                                                    <select class="form-control" name="dayClosePageScale" id="dayClosePageScale" onchange="dayClosePageChange();">
                                                        <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                                                        <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                                                        <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                                                        <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
                                                        <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
                                                    </select>
                                                    <button class="btn btn-md btn-primary" type="button" onclick="dayCloseFileSave();">파일저장</button>
                                                </div>
                                            </div>

                                            <div class="ibox-content">
                                                <div class="table-responsive">
                                                    <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                        <colgroup>
                                                            <col width="50">
                                                            <col width="100">
                                                            <col width="122">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                        </colgroup>

                                                        <thead>
                                                            <tr>
                                                                <th rowspan="2">NO</th>
                                                                <th rowspan="2">마감일</th>
                                                                <th rowspan="2">기관명</th>
                                                                <th rowspan="2">기관코드</th>
                                                                <th colspan="2">청구</th>
                                                                <th colspan="2">가상계좌수납</th>
                                                                <th colspan="2">온라인카드</th>
                                                                <th colspan="2">현금</th>
                                                                <th colspan="2">오프라인카드</th>
                                                                <th colspan="2">문자메시지</th>
                                                            </tr>
                                                            <tr>
                                                                <th>금액</th>
                                                                <th>건수</th>
                                                                <th>금액</th>
                                                                <th>건수</th>
                                                                <th>금액</th>
                                                                <th>건수</th>
                                                                <th>금액</th>
                                                                <th>건수</th>
                                                                <th>금액</th>
                                                                <th>건수</th>
                                                                <th>SMS 건수</th>
                                                                <th>LMS 건수</th>
                                                            </tr>
                                                        </thead>

                                                        <tbody id="reSearchbody">
                                                            <tr>
                                                                <td colspan=16>[조회된 내역이 없습니다.]</td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>

                                                <jsp:include page="/WEB-INF/views/include/sysPaging.jsp" flush="false"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="closing-by-monthly" role="tabpanel" class="tab-pane fade">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-content">
                                            <p class="page-description">
                                                - 월별 마감을 실행하는 화면입니다.<br>
                                                - 월별 자동마감이 안되었거나, 재마감이 필요할 경우 사용합니다.
                                            </p>
                                        </div>
                                    </div>
                                </div>

                                <div class="animated fadeInRight article">
                                    <div class="col-lg-12">
                                        <div class="ibox float-e-margins">
                                            <div class="ibox-title">
                                                <h5>검색</h5>
                                            </div>

                                            <div class="ibox-content">
                                                <form onsubmit="return false;">
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label class="form-label block">마감월</label>
                                                            <div class="form-group form-group-sm">
                                                                <div class="col-md-12 form-inline year-month-dropdown">
                                                                    <select class="form-control" id="closeStartYear"></select>
                                                                    <span class="ml-1 mr-2">년</span>
                                                                    <select class="form-control" id="closeStrartMonth"></select>
                                                                    <span class="ml-1">월</span>
                                                                    <span class="ml-1"> &nbsp; ~ &nbsp; </span>
                                                                    <select class="form-control" id="closeEndYear"></select>
                                                                    <span class="ml-1 mr-2">년</span>
                                                                    <select class="form-control" id="closeEndMonth"></select>
                                                                    <span class="ml-1">월</span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label class="form-label block">기관코드</label>
					                                        <div class="form-group form-group-sm">
					                                            <div class="input-group">
				                                                <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chaCd" maxlength="50">
				                                                <span class="input-group-btn">
				                                                	<button class="btn btn-primary btn-lookup-collecter no-margins" type="button" onclick="searchChacd('month')">기관검색</button>
				                                            	</span>
				                                            </div>
					                                        </div>
                                                        </div>
                                                    </div>

                                                    <hr>

                                                    <div class="text-center">
                                                        <button class="btn btn-primary" onclick="monthCloseSearch('1');">조회</button>
                                                        <!-- 임시 버튼 비활성화 20180816 
                                                        <button class="btn btn-primary" onclick="fnMonthCloseGo();">마감실행</button>
                                                         -->
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="animated fadeInRight">
                                    <div class="col-lg-12">
                                        <div class="ibox">
                                            <div class="ibox-title">
                                                <div class="col-lg-6"></div>

                                                <div class="col-lg-6 form-inline form-searchOrderBy">
                                                    <select class="form-control" id="monthClosesearchOrderBy" name="monthClosesearchOrderBy" onchange="monthCloseSearch()">
                                                        <option value="closeMonth">마감월순 정렬</option>
                                                        <option value="chaName">기관명순 정렬</option>
                                                    </select>
                                                    <select class="form-control" name="monthClosePageScale"
                                                            id="monthClosePageScale" onchange="monthClosePageChange();">
                                                        <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                                                        <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                                                        <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                                                        <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
                                                        <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
                                                    </select>
                                                    <button class="btn btn-md btn-primary" type="button" onclick="monthCloseFileSave();">파일저장</button>
                                                </div>
                                            </div>

                                            <div class="ibox-content">
                                                <div class="table-responsive">
                                                    <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                        <colgroup>
                                                            <col width="50">
                                                            <col width="105">
                                                            <col width="122">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="110">
                                                            <col width="105">
                                                        </colgroup>

                                                        <thead>
                                                            <tr>
                                                                <th rowspan="2">NO</th>
                                                                <th rowspan="2">마감일</th>
                                                                <th rowspan="2">기관명</th>
                                                                <th rowspan="2">기관코드</th>
                                                                <th colspan="3">청구</th>
                                                                <th colspan="4">수납</th>
                                                                <th colspan="4">문자메시지</th>
                                                                <th rowspan="2">마감일시</th>
                                                            </tr>
                                                            <tr>
                                                                <th>금액</th>
                                                                <th>건수</th>
                                                                <th>수수료</th>
                                                                <th>금액</th>
                                                                <th>건수</th>
                                                                <th>수수료</th>
                                                                <th>은행수수료</th>
                                                                <th>SMS금액</th>
                                                                <th>SMS건수</th>
                                                                <th>LMS금액</th>
                                                                <th>LMS건수</th>
                                                            </tr>
                                                        </thead>

                                                        <tbody id="monthCloseResultBody">
                                                            <tr>
                                                                <td colspan=16>[조회된 내역이 없습니다.]</td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>

                                                <jsp:include page="/WEB-INF/views/include/sysPagingE.jsp" flush="false"/>
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

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<script>
$(document).ready(function(){
	var showType = 'day';
	var toDay = getFormatCurrentDate();
	
    $('.input-daterange').datepicker({
        keyboardNavigation: false,
        format: 'yyyy.mm.dd',
        maxDate: "+0d",
        forceParse: false,
        autoclose: true
    });
    
    $('#closeStartDt').val(getPrevDate(toDay, 1));
    $('#closeEndDt').val(toDay);

    //월마감
    getYearsBox('closeStartYear');
    getMonthBox2('closeStrartMonth', 1);
    getYearsBox('closeEndYear');
    getMonthBox('closeEndMonth');
    
    $("#showDay").click(function() {
    	showType = 'day';
		$(this).tab("show");
		dayCloseSearch('1');
	});
    $("#showMonth").click(function() {
    	showType = 'month';
		$(this).tab("show");
		monthCloseSearch('1');
	});    
    
    //마감화면 첫 진입시 일마감 리스트 보여주기
	dayCloseSearch('1');
	//마감일 개월버튼
	$('#btnSetMonth1').on('click', function(){
		$('#closeStartDt').val(getPrevDate(toDay, 1));
	    $('#closeEndDt').val(toDay);
	});
	$('#btnSetMonth2').on('click', function(){		
		$('#closeStartDt').val(getPrevDate(toDay, 2));
	    $('#closeEndDt').val(toDay);
	});
	$('#btnSetMonth3').on('click', function(){
		$('#closeStartDt').val(getPrevDate(toDay, 3));
	    $('#closeEndDt').val(toDay);
	});
});

//기관코드검색  modal
function searchChacd(showType) {
	$("#lookup-collecter-popup").modal({
        backdrop: 'static',
        keyboard: false
    });
	switch(showType) {
	case 'day':
		$("#popChacd").val($('#chacd').val());
		break;
	case 'month':
		$("#popChacd").val($('#chaCd').val());
		break;
	default:
		alert("### unknown show type(" + showType + ")");
		return;
	}
	$("#popChaname").val('');
	
	//fn_ListCollector(); 
	//기관코드검색 탭별로 다르게 조회하도록 처리
	fn_ListCollector(1, fnModalResult);
	
};
//기관코드검색 modal
function fnModalResult(chacd, chaname) {
	switch($('#receiptClosing > li.active').text()) {
	case '일마감':
		$('#chacd').val(chacd);
		break;
	case '월마감':
		$('#chaCd').val(chacd);
		break;
	}
}
</script>
