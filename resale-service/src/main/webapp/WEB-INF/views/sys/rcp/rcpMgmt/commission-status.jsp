<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">
<link href="/assets/css/plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-5";
    var twoDepth = "adm-sub-12";
</script>

<script>
    //숫자만 가능하도록
    function removeChar(event) {
        event = event || window.event;
        var keyID = (event.which) ? event.which : event.keyCode;
        if (keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39)
            return;
        else
            event.target.value = event.target.value.replace(/[^0-9]/g, "");
    }

    //1~3달 세팅
    function whatday(no) {
        var settingDate = new Date();

        var month = settingDate.getMonth() + 1;
        var month_tran = settingDate.getMonth() + 1 - no;
        var day = settingDate.getDate();
        var year = settingDate.getFullYear();

        if (month < 10) {
            month = '0' + month;
        }
        if (month_tran < 10) {
            month_tran = '0' + month_tran;
        }
        if (day < 10) {
            day = '0' + day;
        }
        $("#startday").val(year + '.' + month_tran + '.' + day);
        $("#endday").val(year + '.' + month + '.' + day);
    }

    function whatday2(no) {
        var settingDate = new Date();

        var month = settingDate.getMonth() + 1;
        var month_tran = settingDate.getMonth() + 1 - no;
        var day = settingDate.getDate();
        var year = settingDate.getFullYear();

        if (month < 10) {
            month = '0' + month;
        }
        if (month_tran < 10) {
            month_tran = '0' + month_tran;
        }
        if (day < 10) {
            day = '0' + day;
        }
        $("#startday2").val(year + '.' + month_tran + '.' + day);
        $("#endday2").val(year + '.' + month + '.' + day);
    }

    //조회
    function search() { // 조회버튼에 value or id 줘서 탭 구분하기
        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");
        if (startday > endday) {
            var temp = startday;
            startday = endday;
            endday = temp;
        }

        if (CalcDay(startday, endday) > 365) {
            swal({
                type: 'info',
                text: "최대 조회기간은 1년 입니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        var comSt = "";
        if ($("#inlineCheckbox1-1").prop("checked")) {
            comSt = "all";
        } else {
            comSt = $("input[name='inlineCheckbox1']:checked").val();
        }
        var pageScale = $("#pageScale").val();

        var param = {
            startday: startday,
            endday: endday,
            comSt: comSt,
            curPage: "1",
            pageScale: pageScale,
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/rcpMgmt/commStatList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    //sysajaxPaging(result, 'PageArea');
                    /* 					var scmove = $('#focus').offset().top;
                                        $('html, body').animate( { scrollTop : scmove }, 300 ); */
                } else {
                    swal({
                        type: 'success',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });
    }

    //ajax 값 grid로
    function fnGrid(result, obj) {
        var str = '';
        if (result.list.size <= 0) {
            str += '<tr><td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + v.custSt + '</td>';
                str += '<td>' + v.totCusCount + '</td>';
                str += '<td>' + v.totRcp + '</td>';
                str += '<td>' + v.totFGRcp + '</td>';
                str += '</tr>';
            });
        }
        var str1 = '';
        if (result.alist.size <= 0) {
            str1 += '<tr><td colspan="11" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.alist, function (i, a) {
                str1 += '<tr>';
                str1 += '<td>' + a.day + '</td>';
                str1 += '<td>' + a.custCount + '</td>';
                str1 += '<td>' + a.mgmtFee + '</td>';
                str1 += '<td>' + a.mgmtFeeCount + '</td>';
                str1 += '<td>' + a.smsFee + '</td>';
                str1 += '<td>' + a.smsFeeCount + '</td>';
                str1 += '<td>' + a.bankFee + '</td>';
                str1 += '<td>' + a.bankFeeCount + '</td>';
                str1 += '<td>' + a.cardFee + '</td>';
                str1 += '<td>' + a.cardFeeCount + '</td>';
                str1 += '</tr>';
            });
            $.each(result.alisttot, function (i, a) {
                str1 += '<tr>';
                str1 += '<td colspan="2">계</td>';
                str1 += '<td>' + a.mfSum + '</td>';
                str1 += '<td>' + a.mfCSum + '</td>';
                str1 += '<td>' + a.sfSum + '</td>';
                str1 += '<td>' + a.sfCSum + '</td>';
                str1 += '<td>' + a.bfSum + '</td>';
                str1 += '<td>' + a.bfCSum + '</td>';
                str1 += '<td>' + a.cfSum + '</td>';
                str1 += '<td>' + a.cfCSum + '</td>';
                str1 += '<td>' + '' + '</td>';
                str1 += '</tr>';
            });
        }
        var str2 = '';
        if (result.fglist.size <= 0) {
            str2 += '<tr><td colspan="9" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.fglist, function (i, fg) {
                str2 += '<tr>';
                str2 += '<td>' + fg.fgday + '</td>';
                str2 += '<td>' + fg.fgcustCount + '</td>';
                str2 += '<td colspan="2">' + fg.fingerIC + '</td>';
                /* str2 +=	'<td>'+fg.fingerICCount+'</td>'; */
                str2 += '<td>' + fg.bankIC + '</td>';
                str2 += '<td>' + fg.bankICCount + '</td>';
                str2 += '<td colspan="2">' + fg.cardIC + '</td>';
                /* str2 +=	'<td>'+fg.cardICCount+'</td>'; */
                str2 += '</tr>';
            });
            $.each(result.fglisttot, function (i, fg) {
                str2 += '<tr>';
                str2 += '<td colspan="2">계</td>';
                str2 += '<td colspan="2">' + fg.fingerSum + '</td>';
                /* str2 +=	'<td>'+fg.fingerCSum+'</td>'; */
                str2 += '<td>' + fg.bankSum + '</td>';
                str2 += '<td>' + fg.bankCSum + '</td>';
                str2 += '<td colspan="2">' + fg.cardSum + '</td>';
                /* str2 +=	'<td>'+fg.cardCSum+'</td>'; */
                str2 += '<td>' + '' + '</td>';
                str2 += '</tr>';
            });
        }
        $("#reSearchbody").html(str);
        $("#reSearchbody1").html(str1);
        $("#reSearchbody2").html(str2);

    }

    //조회 두번째탭
    function search2() { // 조회버튼에 value or id 줘서 탭 구분하기
        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");
        if (startday > endday) {
            var temp = startday;
            startday = endday;
            endday = temp;
        }

        if (CalcDay(startday, endday) > 365) {
            swal({
                type: 'info',
                text: "최대 조회기간은 1년 입니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        var comSt = "";
        if ($("#inlineCheckbox1-1").prop("checked")) {
            comSt = "all";
        } else {
            comSt = $("input[name='inlineCheckbox1']:checked").val();
        }
        var pageScale = $("#pageScale").val();

        var param = {
            startday: startday,
            endday: endday,
            comCd: $("#comCd").val(),
            comNm: $("#comNm").val(),
            curPage: "1",
            pageScale: pageScale,
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/rcpMgmt/commStatList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid2(result, 'reSearchbody3');
                    /* sysajaxPaging(result, 'PageArea');
                    var scmove = $('#focus').offset().top;
                    $('html, body').animate( { scrollTop : scmove }, 300 ); */
                } else {
                    swal({
                        type: 'success',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });
    }

    //ajax 값 grid로
    function fnGrid2(result, obj) {
        var str3 = '';
        if (result.alist.size <= 0) {
            str3 += '<tr><td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.alist, function (i, a) {
                str3 += '<tr>';
                str3 += '<td>' + a.day + '</td>';
                str3 += '<td>' + a.custCount + '</td>';
                str3 += '<td>' + a.mgmtFee + '</td>';
                str3 += '<td>' + a.mgmtFeeCount + '</td>';
                str3 += '<td>' + a.smsFee + '</td>';
                str3 += '<td>' + a.smsFeeCount + '</td>';
                str3 += '<td>' + a.bankFee + '</td>';
                str3 += '<td>' + a.bankFeeCount + '</td>';
                str3 += '<td>' + a.cardFee + '</td>';
                str3 += '<td>' + a.cardFeeCount + '</td>';
                str3 += '</tr>';
            });
            $.each(result.alisttot, function (i, a) {
                str3 += '<tr>';
                str3 += '<td colspan="2">계</td>';
                str3 += '<td>' + a.mfSum + '</td>';
                str3 += '<td>' + a.mfCSum + '</td>';
                str3 += '<td>' + a.sfSum + '</td>';
                str3 += '<td>' + a.sfCSum + '</td>';
                str3 += '<td>' + a.bfSum + '</td>';
                str3 += '<td>' + a.bfCSum + '</td>';
                str3 += '<td>' + a.cfSum + '</td>';
                str3 += '<td>' + a.cfCSum + '</td>';
                str3 += '<td>' + '' + '</td>';
                str3 += '</tr>';
            });
        }
        var str4 = '';
        if (result.fglist.size <= 0) {
            str4 += '<tr><td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.fglist, function (i, fg) {
                str4 += '<tr>';
                str4 += '<td>' + fg.fgday + '</td>';
                str4 += '<td>' + fg.fgcustCount + '</td>';
                str4 += '<td colspan="2">' + fg.fingerIC + '</td>';
                /* str4 +=	'<td>'+fg.fingerICCount+'</td>'; */
                str4 += '<td>' + fg.bankIC + '</td>';
                str4 += '<td>' + fg.bankICCount + '</td>';
                str4 += '<td colspan="2">' + fg.cardIC + '</td>';
                /* str4 +=	'<td>'+fg.cardICCount+'</td>'; */
                str4 += '</tr>';
            });
            $.each(result.fglisttot, function (i, fg) {
                str4 += '<tr>';
                str4 += '<td colspan="2">계</td>';
                str4 += '<td colspan="2">' + fg.fingerSum + '</td>';
                /* str4 +=	'<td>'+fg.fingerCSum+'</td>'; */
                str4 += '<td>' + fg.bankSum + '</td>';
                str4 += '<td>' + fg.bankCSum + '</td>';
                str4 += '<td colspan = "2">' + fg.cardSum + '</td>';
                /* str4 +=	'<td>'+fg.cardCSum+'</td>'; */
                str4 += '<td>' + '' + '</td>';
                str4 += '</tr>';
            });
        }
        $("#reSearchbody3").html(str3);
        $("#reSearchbody4").html(str4);
    }

    //파일저장
    function fn_fileSave(no) {
        /* 	if($('#acount').text() <= 0){
                swal({
                   type: 'info',
                   text: '다운로드할 데이터가 없습니다.',
                   confirmButtonColor: '#3085d6',
                   confirmButtonText: '확인'
                });
                return;
            } */
        swal({
            type: 'question',
            html: "다운로드 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            var startday = $("#startday").val().replace(/\./gi, "");
            var endday = $("#endday").val().replace(/\./gi, "");
            if (startday > endday) {
                var temp = startday;
                startday = endday;
                endday = temp;
            }

            var comSt = "";
            if ($("#inlineCheckbox1-1").prop("checked")) {
                comSt = "all";
            } else {
                comSt = $("input[name='inlineCheckbox1']:checked").val();
            }
            var searchOrderBy = $("#searchOrderBy").val();
            var comCd = $("#comCd").val();
            var comNm = $("#comNm").val();

            $('#fstartday').val(startday);
            $('#fendday').val(endday);
            $('#fcomSt').val(comSt);
            $('#fNo').val(no);
            // 다운로드
            $('#fileForm').submit();
        });
    }

    function fn_fileSave2(no) {
        if ($('#acount').text() <= 0) {
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
            var startday = $("#startday2").val().replace(/\./gi, "");
            var endday = $("#endday2").val().replace(/\./gi, "");
            if (startday > endday) {
                var temp = startday;
                startday = endday;
                endday = temp;
            }
            var comCd = $("#comCd").val();
            var comNm = $("#comNm").val();

            $('#fstartday').val(startday);
            $('#fendday').val(endday);
            $('#fcomCd').val(comCd);
            $('#fcomNm').val(comNm);
            $('#fNo').val(no);
            // 다운로드
            $('#fileForm').submit();
        });
    }
</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/rcpMgmt/commstatexcelDown">
    <input type="hidden" id="fstartday" name="startday" value=""/>
    <input type="hidden" id="fendday" name="endday" value=""/>
    <input type="hidden" id="fcomSt" name="comSt" value=""/>
    <input type="hidden" id="fcomCd" name="comCd" value=""/>
    <input type="hidden" id="fcomNm" name="comNm" value=""/>
    <input type="hidden" id="fNo" name="no" value=""/>
</form>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>수수료통계</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>정산관리</a>
            </li>
            <li class="active">
                <strong>수수료통계</strong>
            </li>
        </ol>
        <p class="page-description">이용기관이 납부하는 전체 월 수수료와 핑거의 월 수수료를 조회하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<div class="wrapper wrapper-content">
    <div class="animated fadeInUp">
        <div class="row">
            <div class="col-lg-12 m-b-3">
                <div class="tabs-container">
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="active"><a href="#monthly-commission-status" aria-controls="monthly-book-keeping" role="tab" data-toggle="tab">전체 수납현황</a></li>
                        <li role="presentation"><a href="#daily-commission-status" aria-controls="daily-book-kepping" role="tab" data-toggle="tab">기관별 수납현황</a></li>
                    </ul>

                    <div class="tab-content">
                        <div id="monthly-commission-status" role="tabpanel" class="tab-pane fade in active">
                            <div class="animated fadeInRight article">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="ibox float-e-margins">
                                            <div class="ibox-content">
                                                <form>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label class="form-label block">승인일자</label>
                                                            <div class="form-group form-group-sm">
                                                                <div class="input-group col-md-12">
                                                                    <div class="input-daterange input-group float-left" id="datepicker">
                                                                        <input type="text" class="input-sm form-control" name="start" id="startday" readonly="readonly"/>
                                                                        <span class="input-group-addon">to</span>
                                                                        <input type="text" class="input-sm form-control" name="end" id="endday" readonly="readonly"/>
                                                                    </div>

                                                                    <div class="daterange-setMonth">
                                                                        <button class="btn btn-sm btn-primary btn-outline" type="button" onclick="whatday(1)">1개월</button>
                                                                        <button class="btn btn-sm btn-primary btn-outline" type="button" onclick="whatday(2)">2개월</button>
                                                                        <button class="btn btn-sm btn-primary btn-outline" type="button" onclick="whatday(3)">3개월</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="col-md-6">
                                                            <label class="form-label block">기관분류</label>
                                                            <div class="form-group form-group-sm">
                                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                                    <input type="checkbox" id="inlineCheckbox1-1" value="all">
                                                                    <label for="inlineCheckbox1-1"> 전체 </label>
                                                                </div>
                                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                                    <input type="checkbox" id="inlineCheckbox1-2" name="inlineCheckbox1" value="1">
                                                                    <label for="inlineCheckbox1-2"> WEB </label>
                                                                </div>
                                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                                    <input type="checkbox" id="inlineCheckbox1-3" name="inlineCheckbox1" value="2">
                                                                    <label for="inlineCheckbox1-3"> API </label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <hr>

                                                    <div class="text-center">
                                                        <button class="btn btn-primary" type="button" onclick="fnSearch('1');">조회</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <div class="col-lg-6">
                                                전체 건수 : <strong class="text-success">${map.emCount}</strong> 건
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                    <colgroup>
                                                        <col width="350">
                                                        <col width="400">
                                                        <col width="*">
                                                        <col width="350">
                                                    </colgroup>

                                                    <thead>
                                                        <tr>
                                                            <th>기관분류</th>
                                                            <th>총 이용기관 수</th>
                                                            <th>이용기관 납부 수수료</th>
                                                            <th>핑거 수수료</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody">
                                                        <c:choose>
                                                            <c:when test="${map.list.size() > 0}">
                                                                <c:forEach var="row" items="${map.list}" varStatus="status">
                                                                    <tr>
                                                                        <td>${row.custSt }</td>
                                                                        <td>${row.totCusCount }</td>
                                                                        <td>${row.totRcp}</td>
                                                                        <td>${row.totFGRcp }</td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:when>

                                                            <c:otherwise>
                                                                <tr>
                                                                    <td colspan="7" style="text-align: center;">[조회된
                                                                        내역이없습니다.]
                                                                    </td>
                                                                </tr>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <div class="col-lg-6"><h3>전체 이용기관 납부 수수료</h3></div>
                                            <div class="col-lg-6 form-inline form-searchOrderBy">
                                                <button class="btn btn-sm btn-primary" onclick="fn_fileSave(1)">파일저장</button>
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis" id="alisttable1">
                                                    <colgroup>
                                                        <col width="130">
                                                        <col width="150">
                                                        <col width="130">
                                                        <col width="130">
                                                        <col width="130">
                                                        <col width="130">
                                                        <col width="130">
                                                        <col width="130">
                                                        <col width="130">
                                                        <col width="130">
                                                        <col width="*">
                                                    </colgroup>

                                                    <thead>
                                                        <tr>
                                                            <th rowspan="2">이용월</th>
                                                            <th rowspan="2">이용기관수</th>
                                                            <th colspan="2">관리비(원/건)</th>
                                                            <th colspan="2">문자(원/건)</th>
                                                            <th colspan="2">은행수수료(원/건)</th>
                                                            <th colspan="2">카드수수료(원/건)</th>
                                                            <th rowspan="2">합계(원)</th>
                                                        </tr>
                                                        <tr>
                                                            <th colspan="2">핑거 자동출금</th>
                                                            <th colspan="2">핑거 자동출금</th>
                                                            <th colspan="2">신한은행 자동출금</th>
                                                            <th colspan="2">나이스 페이먼츠 차감</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody1">
                                                        <c:choose>
                                                            <c:when test="${map.alist.size() > 0}">
                                                                <c:forEach var="row" items="${map.alist}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td>${row.day }</td>
                                                                        <td>${row.custCount}</td>
                                                                        <td>${row.mgmtFee}</td>
                                                                        <td>${row.mgmtFeeCount}</td>
                                                                        <td>${row.smsFee}</td>
                                                                        <td>${row.smsFeeCount }</td>
                                                                        <td>${row.bankFee}</td>
                                                                        <td>${row.bankFeeCount}</td>
                                                                        <td>${row.cardFee}</td>
                                                                        <td>${row.cardFeeCount}</td>
                                                                        <td></td>
                                                                    </tr>
                                                                </c:forEach>

                                                                <c:forEach var="row" items="${map.alisttot}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td colspan="2">계</td>
                                                                        <td>${row.mfSum}</td>
                                                                        <td>${row.mfCSum }</td>
                                                                        <td>${row.sfSum }</td>
                                                                        <td>${row.sfCSum }</td>
                                                                        <td>${row.bfSum }</td>
                                                                        <td>${row.bfCSum }</td>
                                                                        <td>${row.cfSum }</td>
                                                                        <td>${row.cfCSum }</td>
                                                                        <td></td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:when>

                                                            <c:otherwise>
                                                                <tr>
                                                                    <td colspan="7" style="text-align: center;">[조회된
                                                                        내역이없습니다.]
                                                                    </td>
                                                                </tr>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <div class="col-lg-6"><h3>핑거 수수료</h3></div>
                                            <div class="col-lg-6 form-inline form-searchOrderBy">
                                                <button class="btn btn-sm btn-primary" onclick="fn_fileSave(2)">파일저장</button>
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                    <thead>
                                                        <tr>
                                                            <th rowspan="2">이용월</th>
                                                            <th rowspan="2">이용기관수</th>
                                                            <th colspan="2">핑거(원)</th>
                                                            <th colspan="2">은행(원/핑거비율)</th>
                                                            <th colspan="2">신용카드(원)</th>
                                                            <th rowspan="2">합계(원)</th>
                                                        </tr>
                                                        <tr>
                                                            <th colspan="2">핑거 정산</th>
                                                            <th colspan="2">신한은행 정산</th>
                                                            <th colspan="2">나이스페이먼츠 정산</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody2">
                                                        <c:choose>
                                                            <c:when test="${map.fglist.size() > 0}">
                                                                <c:forEach var="row" items="${map.fglist}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td>${row.fgday }</td>
                                                                        <td>${row.fgcustCount}</td>
                                                                        <td colspan="2">${row.fingerIC}</td>
                                                                            <%-- <td>${row.fingerICCount}</td> --%>
                                                                        <td>${row.bankIC}</td>
                                                                        <td>${row.bankICCount }</td>
                                                                        <td colspan="2">${row.cardIC}</td>
                                                                            <%-- <td>${row.cardICCount}</td> --%>
                                                                        <td></td>
                                                                    </tr>
                                                                </c:forEach>
                                                                <c:forEach var="row" items="${map.fglisttot}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td colspan="2">계</td>
                                                                        <td colspan="2">${row.fingerSum}</td>
                                                                            <%-- <td>${row.fingerCSum}</td> --%>
                                                                        <td>${row.bankSum}</td>
                                                                        <td>${row.bankCSum }</td>
                                                                        <td colspan="2">${row.cardSum}</td>
                                                                            <%-- <td>${row.cardCSum}</td> --%>
                                                                        <td></td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <tr>
                                                                    <td colspan="7" style="text-align: center;">[조회된
                                                                        내역이없습니다.]
                                                                    </td>
                                                                </tr>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="daily-commission-status" role="tabpanel" class="tab-pane fade">
                            <div class="animated fadeInRight article">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="ibox float-e-margins">
                                            <div class="ibox-content">
                                                <form>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label class="form-label block">기간</label>
                                                            <div class="form-group form-group-sm">
                                                                <div class="input-group col-md-12">
                                                                    <div class="input-daterange input-group float-left" id="datepicker">
                                                                        <input type="text" class="input-sm form-control" name="start" id="startday2" readonly="readonly"/>
                                                                        <span class="input-group-addon">to</span>
                                                                        <input type="text" class="input-sm form-control" name="end" id="endday2" readonly="readonly"/>
                                                                    </div>

                                                                    <div class="daterange-setMonth">
                                                                        <button class="btn btn-sm btn-primary btn-outline" type="button" onclick="whatday(1)">1개월</button>
                                                                        <button class="btn btn-sm btn-primary btn-outline" type="button" onclick="whatday(2)">2개월</button>
                                                                        <button class="btn btn-sm btn-primary btn-outline" type="button" onclick="whatday(3)">3개월</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="col-md-6"></div>
                                                    </div>

                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label class="form-label block">기관코드</label>
                                                            <div class="form-group form-group-sm">
                                                                <input type="text" class="form-control" onkeyup="removeChar(event)">
                                                            </div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label class="form-label block">이용기관명</label>
                                                            <div class="form-group form-group-sm">
                                                                <input type="text" class="form-control">
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <hr>

                                                    <div class="text-center">
                                                        <button class="btn btn-primary" type="button" onclick="search2();">조회</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <div class="col-lg-6"><h3>이용기관 납부 수수료</h3></div>
                                            <div class="col-lg-6 form-inline form-searchOrderBy">
                                                <button class="btn btn-primary" onclick="fn_fileSave2(3)">파일저장</button>
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                    <thead>
                                                        <tr>
                                                            <th rowspan="2">이용월</th>
                                                            <th rowspan="2">이용기관수</th>
                                                            <th colspan="2">관리비(원/건)</th>
                                                            <th colspan="2">문자(원/건)</th>
                                                            <th colspan="2">은행수수료(원/건)</th>
                                                            <th colspan="2">카드수수료(원/건)</th>
                                                            <th rowspan="2">합계(원)</th>
                                                        </tr>
                                                        <tr>
                                                            <th colspan="2">핑거 자동출금</th>
                                                            <th colspan="2">핑거 자동출금</th>
                                                            <th colspan="2">신한은행 자동출금</th>
                                                            <th colspan="2">나이스 페이먼츠 차감</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody3">
                                                        <c:choose>
                                                            <c:when test="${map.alist.size() > 0}">
                                                                <c:forEach var="row" items="${map.alist}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td>${row.day }</td>
                                                                        <td>${row.custCount}</td>
                                                                        <td>${row.mgmtFee}</td>
                                                                        <td>${row.mgmtFeeCount}</td>
                                                                        <td>${row.smsFee}</td>
                                                                        <td>${row.smsFeeCount }</td>
                                                                        <td>${row.bankFee}</td>
                                                                        <td>${row.bankFeeCount}</td>
                                                                        <td>${row.cardFee}</td>
                                                                        <td>${row.cardFeeCount}</td>
                                                                        <td></td>
                                                                    </tr>
                                                                </c:forEach>
                                                                <c:forEach var="row" items="${map.alisttot}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td colspan="2">계</td>
                                                                        <td>${row.mfSum}</td>
                                                                        <td>${row.mfCSum }</td>
                                                                        <td>${row.sfSum }</td>
                                                                        <td>${row.sfCSum }</td>
                                                                        <td>${row.bfSum }</td>
                                                                        <td>${row.bfCSum }</td>
                                                                        <td>${row.cfSum}</td>
                                                                        <td>${row.cfCSum }</td>
                                                                        <td></td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:when>

                                                            <c:otherwise>
                                                                <tr>
                                                                    <td colspan="7" style="text-align: center;">[조회된
                                                                        내역이없습니다.]
                                                                    </td>
                                                                </tr>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <div class="col-lg-6"><h3>핑거 수수료</h3></div>
                                            <div class="col-lg-6 form-inline form-searchOrderBy">
                                                <button class="btn btn-primary" onclick="fn_fileSave2(4)">파일저장</button>
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                    <thead>
                                                        <tr>
                                                            <th rowspan="2">이용월</th>
                                                            <th rowspan="2">기관명</th>
                                                            <th colspan="2">핑거</th>
                                                            <th colspan="2">은행</th>
                                                            <th colspan="2">신용카드</th>
                                                            <th rowspan="2">합계(원)</th>
                                                        </tr>
                                                        <tr>
                                                            <th colspan="2">핑거 정산</th>
                                                            <th colspan="2">신한은행 정산</th>
                                                            <th colspan="2">나이스페이먼츠 정산</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody4">
                                                        <c:choose>
                                                            <c:when test="${map.fglist.size() > 0}">
                                                                <c:forEach var="row" items="${map.fglist}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td>${row.fgday }</td>
                                                                        <td>${row.fgcustCount}</td>
                                                                        <td>${row.fingerIC}</td>
                                                                        <td>${row.fingerICCount}</td>
                                                                        <td>${row.bankIC}</td>
                                                                        <td>${row.bankICCount }</td>
                                                                        <td>${row.cardIC}</td>
                                                                        <td>${row.cardICCount}</td>
                                                                        <td></td>
                                                                    </tr>
                                                                </c:forEach>

                                                                <c:forEach var="row" items="${map.fglisttot}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td colspan="2">계</td>
                                                                        <td>${row.fingerSum}</td>
                                                                        <td>${row.fingerCSum}</td>
                                                                        <td>${row.bankSum}</td>
                                                                        <td>${row.bankCSum }</td>
                                                                        <td>${row.cardSum}</td>
                                                                        <td>${row.cardCSum}</td>
                                                                        <td></td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:when>

                                                            <c:otherwise>
                                                                <tr>
                                                                    <td colspan="7" style="text-align: center;">[조회된
                                                                        내역이없습니다.]
                                                                    </td>
                                                                </tr>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tbody>
                                                </table>
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

<script>
    $(document).ready(function () {
        var now = new Date();
        var endDay = now.getFullYear() + "." + addZero(now.getMonth() + 1) + "." + addZero(now.getDate());
        $("#endday").val(endDay);
        $("#endday2").val(endDay);
        $("#inlineCheckbox1-1").prop("checked", true);
        search();
        search2();
        // 합계컬럼은 쿼리로 처리 힘들경우 js로 처리
    });

    $('.input-daterange').datepicker({
        keyboardNavigation: false,
        forceParse: false,
        format: 'yyyy.mm.dd',
        maxDate: "+0d",
        autoclose: true
    });

    //checkbox 전체 선택 fn
    $(document).on("change", "input[id='inlineCheckbox1-1']", function () {
        if ($("#inlineCheckbox1-1").prop("checked")) {
            $("input[name='inlineCheckbox1']").prop("checked", true);
        } else {
            $("input[name='inlineCheckbox1']").prop("checked", false);
        }
    });
    $(document).on("change", "input[name='inlineCheckbox1']", function () {
        $("#inlineCheckbox1-1").prop("checked", false);
    });
</script>
