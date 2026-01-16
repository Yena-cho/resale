<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
<!-- 사용 여부 확인할 것 -->

<link href="/assets/css/plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet">

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.js"></script>

<script>
    var oneDepth = "adm-nav-5";
    var twoDepth = "adm-sub-11";
</script>

<script type="text/javascript">
    //테이블 캡션 현재시간구하기 (ex: 2018010131200)
    var getCurTime = function () {
        var today = new Date();
        var year = today.getFullYear().toString();
        var month = today.getMonth().toString();
        var date = (today.getDate() + 1).toString();
        var time = today.getTime().toString();
        return year + (month[1] ? month : '0' + month[0]) + (date[1] ? date : '0' + date[0]) + (time[1] ? time : '0' + time[0]);
    };
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
    $(document).on("change", "input[id='inlineCheckbox2-1']", function () {
        if ($("#inlineCheckbox2-1").prop("checked")) {
            $("input[name='inlineCheckbox2']").prop("checked", true);
        } else {
            $("input[name='inlineCheckbox2']").prop("checked", false);
        }
    });
    $(document).on("change", "input[name='inlineCheckbox2']", function () {
        $("#inlineCheckbox2-1").prop("checked", false);
    });

    //조회기간 제한 체크
    var checkLimitDate = function (sdate, edate) {
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
                text: "조회 시작일이 종료일보다 큽니다. 날짜를 다시 선택해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }
        return true;
    };
    //월별 수남통계 조회
    var search = function (comSt) {
        var startday = $('#syearsBox option:selected').val() + $('#smonthBox option:selected').val();
        var endday = $('#eyearsBox option:selected').val() + $('#emonthBox option:selected').val();
        if (!checkLimitDate(startday, endday)) {
            return;
        }

        var param = {
            startday: startday,
            endday: endday,
            comSt: comSt,
            chaCd: $('#chacd').val(),
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/rcpMgmt/monthlyRcpList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    fnChart(result);
                    var scmove = $('#focus').offset();
                    $('html, body').animate({scrollTop: scmove}, 300);
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg, //'시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });
    };
    //ajax 값  Table로
    var fnGrid = function (result, obj) {
        var data = result.list;
        var adata = result.alist;
        var tdata = result.tlist;
        var obj = obj;
        $('#monthTotRcpCnt').html("&lt; 총수납개월 :" + (adata.length < 2 ? 0 : adata.length / 2) + "개월 &gt;");
        drawRcpTotTable(data, obj); //월별 전체 수납 리스트
        drawRcpDetailTable(adata, obj); //월별수납 상세 리스트
    };
    //전체 수납 리스트
    var drawRcpTotTable = function (data, obj) {
        if (true) {
            var $tbody = $('#reSearchbody').empty();
        } else {
            var $tbody = $('#reSearchbody3').empty();
        }
        if (data.length == 0) {
            $('<tr>').append($('<td colspan="7">').addClass('text-center').text('[조회된 내역이 없습니다.]')).appendTo($tbody);
            return $tbody;
        } else {
            for (let i = 0; i < data.length; i++) {
                var item = data[i];
                var $tr = $('<tr>').appendTo($tbody);
                $('<td>').text(item.custSt).appendTo($tr);
                $('<td>').text(item.totCusCount ? item.totCusCount + '개' : 0).appendTo($tr);
                $('<td>').text(item.totRcpAmt ? numberToCommas(item.totRcpAmt) + '원' : 0).appendTo($tr);
                $('<td>').text(item.totRcpCount ? item.totRcpCount + '건' : 0).appendTo($tr);
            }
        }
    }
    //수납상세내역 리스트   TODO: 쿼리  구현후 수정
    var drawRcpDetailTable = function (adata, obj) {
        if (adata.length < 8) {
            var $tbody = $('#reSearchbody2').empty();
        } else {
            var $tbody = $('#reSearchbody4').empty();
        }

        if (adata.length == 0) {
            $('<tr>').append($('<td colspan="5">').addClass('text-center').text('[조회된 내역이 없습니다.]')).appendTo($tbody);
            return $tbody;
        } else {
            for (let i = 0; i < adata.length; i++) {
                var item = adata[i];
                if (item.payMethod == '1') {
                    var $tr = $('<tr>').appendTo($tbody);
                    $('<td>').attr('rowspan', 2).text(item.masMonth).appendTo($tr);
                    $('<td>').text('가상계좌').appendTo($tr);
                    $('<td>').text(item.custCount ? item.custCount : 0).appendTo($tr);
                    $('<td>').text(Number(item.rcpAmt) ? Number(item.rcpAmt) : 0).appendTo($tr);
                    $('<td>').text(item.rcpCount ? item.rcpCount : 0).appendTo($tr);
                }
                if (item.payMethod == '2') {
                    var $tr = $('<tr>').appendTo($tbody);
                    $('<td>').text('온라인신용카드').appendTo($tr);
                    $('<td>').text(item.custCount ? item.custCount : 0).appendTo($tr);
                    $('<td>').text(Number(item.rcpAmt) ? Number(item.rcpAmt) : 0).appendTo($tr);
                    $('<td>').text(item.rcpCount ? item.rcpCount : 0).appendTo($tr);
                }
                if (item.payMethod == 'total') {
                    $('<td>').text('합계').appendTo($tr);
                    $('<td>').text(ocdCustCount ? ocdCustCount : 0).appendTo($tr);
                    $('<td>').text(Number(ocdRcpAmt) ? Number(ocdRcpAmt) : 0).appendTo($tr);
                    $('<td>').text(ocdRcpCount ? ocdRcpCount : 0).appendTo($tr);
                }
            }
        }
    }
    //차트
    var fnChart = function (result) {
        // 파이 차트
        var pmet = new Array();
        $.each(result.tlist, function (i, t) {
            if (t.payMethod != 'total') {
                pmet.push(t.ccSum);
            }
        });
        var barChartData1 = {
            labels: ['가상계좌', '카드'],
            datasets: [{
                label: '수납 매체 활용률',
                backgroundColor: ['skyblue', 'blue'],
                data: pmet
            }]
        };
        var ctx1 = document.getElementById("monthGraphpie");
        var myBarChart = new Chart(ctx1, {
            type: 'pie',
            data: barChartData1
        });
        // 바 차트
        var arrLabel = new Array();
        var arrRcp = new Array();
        var rcpAmt = 0;
        $.each((result.alist.reverse()), function (i, v) {
            //홀수면 합치고 짝수면 푸쉬 //쿼리 2개로 불러오기
            if (i != 0 && i % 2 != 0) {
                arrLabel.push(v.masMonth);
                rcpAmt += Number(v.rcpAmt);
                arrRcp.push(rcpAmt);
                rcpAmt = 0;
            } else {
                rcpAmt += Number(v.rcpAmt);
            }
        });
        var barChartData2 = {
            labels: arrLabel,
            datasets: [{
                label: '월 총수납액',
                backgroundColor: 'skyblue',
                data: arrRcp
            }]
        };
        var ctx2 = document.getElementById("monthGraphbar");
        var myBarChart = new Chart(ctx2, {
            type: 'bar',
            data: barChartData2
        });
    }

    //파일저장
    var fn_fileSave = function (i, t) {
        //TODO : 구현해야함
        if ($('').t() == 0) {
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
                $('#chaCd').val($('#chacd').val());
                $('#calDateFrom').val($('#nCalDateFrom').val().replace(/\./gi, ""));
                $('#calDateTo').val($('#nCalDateTo').val().replace(/\./gi, ""));
                $('#chaStItem').val($("input[name=chaSt]:checked").val());
                $('#chaCloseChk').val($("input[name=chaClose]:checked").val());
                $('#searchOrderBy').val($('#vSearchOrderBy option:selected').val());

                // 다운로드
                document.downForm.action = "/sys/chaMgmt/newChaListExcel";
                document.downForm.submit();
            }
        });
    }

    //일별 수남통계 조회    TODO: 서비스 구현 필요
    var search2 = function () {
        var startday = $('#startday2').val().toString().replace(/\./gi, "");
        var endday = $("#endday2").val().toString().replace(/\./gi, "");
        checkLimitDate(startday, endday); //조회날짜 입력 오류 체크

        if (!checkLimitDate(startday, endday)) {
            return;
        }
        var param = {
            startday: startday,
            endday: endday,
            //comSt : comSt,
            chaCd: $('#chacd').val(),
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/rcpMgmt/daylyRcpList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid2(result, 'reSearchbody3');
                    var scmove = $('#focus').offset();
                    $('html, body').animate({scrollTop: scmove}, 300);
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
    //ajax 값 Table로
    var fnGrid2 = function (result, obj) {
        var data = result.list;
        var adata = result.alist;
        var tdata = result.tlist;
        var obj = obj;
        drawRcpTotTable(data, obj); //일별 전체 수납 리스트
        $('#dayTotRcpCnt').html("&lt; 총수납개월 :" + (adata.length < 2 ? 0 : adata.length / 2) + "개월 &gt;");
        drawRcpDetailTable(adata, obj); //일별 수납상세 리스트
    }
    //차트   TODO : 서비스 구현 필요
    var fnChart2 = function (result) {
        // 파이 차트
        var pmet = new Array();
        $.each(result.tlist, function (i, t) {
            if (t.payMethod != 'total') {
                pmet.push(t.ccSum);
            }
        });
        var barChartData1 = {
            labels: ['가상계좌', '카드'],
            datasets: [{
                label: '수납 매체 활용률',
                backgroundColor: ['skyblue', 'blue'],
                data: pmet
            }]
        }
        var ctx1 = document.getElementById("dayGraphpie");
        var myBarChart = new Chart(ctx1, {
            type: 'pie',
            data: barChartData1
        });
        // 바 차트
        var arrLabel = new Array();
        var arrRcp = new Array();
        var rcpAmt = 0;

        $.each((result.alist.reverse()), function (i, v) {
            //홀수면 합치고 짝수면 푸쉬 //쿼리 2개로 불러오기
            if (i != 0 && i % 2 != 0) {
                arrLabel.push(v.masMonth);
                rcpAmt += Number(v.rcpAmt);
                arrRcp.push(rcpAmt);
                rcpAmt = 0;
            } else {
                rcpAmt += Number(v.rcpAmt);
            }
        });
        var barChartData2 = {
            labels: arrLabel,
            datasets: [{
                label: '월 총수납액',
                backgroundColor: 'skyblue',
                data: arrRcp
            }]
        }
        var ctx2 = document.getElementById("dayGraphbar");
        var myBarChart = new Chart(ctx2, {
            type: 'bar',
            data: barChartData2
        });
    }
    //파일저장   TODO: 서비스 구현후 수정 필요
    /*function fn_fileSave2() {
        if($('#acount').text() <= 0){
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
        }).then(function(result){
            var startday = $("#startday2").val().replace(/\./gi,"");
            var endday = $("#endday2").val().replace(/\./gi,"");
            if(startday>endday){
                var temp = startday;
                startday = endday;
                endday = temp;
            }
            var comSt = "";
            if($("#inlineCheckbox2-1").prop("checked")){
                comSt = "all";
            }else{
                comSt = $("input[name='inlineCheckbox2']:checked").val();
            }
                $('#fstartday').val(startday);
                $('#fendday').val(endday);
                $('#fcomSt').val(comSt);
                // 다운로드
                 $('#fileForm').submit();
      });
    }*/
</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/rcpMgmt/rcpstatexcelDown">
    <input type="hidden" id="fstartday" name="startday" value=""/>
    <input type="hidden" id="fendday" name="endday" value=""/>
    <input type="hidden" id="fcomSt" name="comSt" value=""/>
</form>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>수납통계</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>정산관리</a>
            </li>
            <li class="active">
                <strong>수납통계</strong>
            </li>
        </ol>
        <p class="page-description">가상계좌 및 카드 수납현황을 기간별로 조회하여 볼 수 있는 화면입니다.</p>
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
                        <li role="presentation" class="active"><a href="#monthly-book-keeping" aria-controls="monthly-book-keeping" role="tab" data-toggle="tab" id="mothlyTab">월별 수납현황</a></li>
                        <li role="presentation"><a href="#daily-book-kepping" aria-controls="daily-book-kepping" role="tab" data-toggle="tab" id="daylyTab">일별 수납현황</a></li>
                    </ul>

                    <div class="tab-content">
                        <div id="monthly-book-keeping" role="tabpanel" class="tab-pane fade in active">
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
                                                                <div class="col-md-12 form-inline year-month-dropdown">
                                                                    <select class="form-control" id="syearsBox">
                                                                        <option>선택</option>
                                                                    </select>
                                                                    <span class="ml-1 mr-2">년</span>
                                                                    <select class="form-control" id="smonthBox">
                                                                        <option>선택</option>
                                                                    </select>
                                                                    <span class="ml-1">월</span>
                                                                    <span class="ml-1"> &nbsp; ~ &nbsp; </span>
                                                                    <select class="form-control" id="eyearsBox">
                                                                        <option>선택</option>
                                                                    </select>
                                                                    <span class="ml-1 mr-2">년</span>
                                                                    <select class="form-control" id="emonthBox">
                                                                        <option>선택</option>
                                                                    </select>
                                                                    <span class="ml-1">월</span>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="col-md-6">
                                                            <label class="form-label block">기관분류</label>
                                                            <div class="form-group form-group-sm">
                                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                                    <input type="checkbox" id="inlineCheckbox1-1" value="all" checked>
                                                                    <label for="inlineCheckbox1-1"> 전체 </label>
                                                                </div>
                                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                                    <input type="checkbox" id="inlineCheckbox1-2" name="inlineCheckbox1" value="01" checked>
                                                                    <label for="inlineCheckbox1-2"> WEB </label>
                                                                </div>
                                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                                    <input type="checkbox" id="inlineCheckbox1-3" name="inlineCheckbox1" value="03" checked>
                                                                    <label for="inlineCheckbox1-3"> API </label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label class="form-label block">기관코드</label>
                                                            <div class="form-group form-group-sm">
                                                                <div class="input-group col-md-12">
                                                                    <div class="input-group">
                                                                        <input type="text" class="form-control" name="chacd" id="chacd" maxlength="50">
                                                                        <span class="input-group-btn">
					                                                    <button type="button" class="btn btn-primary btn-lookup-collecter">기관검색</button>
					                                                </span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <hr>

                                                    <div class="text-center">
                                                        <button class="btn btn-primary" type="button" onclick="search();">조회</button>
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
                                            <div class="col-lg-12" style="text-align:right">
                                                <strong class="text-success">&lt;2018.07. 13시 기준&gt;</strong>
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                    <colgroup>
                                                        <col width="300">
                                                        <col width="250">
                                                        <col width="*">
                                                        <col width="300">
                                                    </colgroup>

                                                    <thead>
                                                        <tr>
                                                            <th>기관분류</th>
                                                            <th>이용기관 수</th>
                                                            <th>총 수납금액</th>
                                                            <th>총 수납건수</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody"></tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-6">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <h3>수납매체활용율</h3>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <canvas id="monthGraphpie" width="280" height="280" class="chartjs-render-monitor"></canvas>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-lg-6">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <h3>월별수납현황</h3>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <canvas id="monthGraphbar" width="280" height="280" class="chartjs-render-monitor"></canvas>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <div class="col-lg-6">
                                                <strong class="text-primary" id="monthTotRcpCnt">&lt; 검색결과 : 총 0 개월 &gt;</strong>
                                            </div>
                                            <div class="col-lg-6 form-inline form-searchOrderBy">
                                                <button class="btn btn-primary" onclick="fn_fileSave()">파일저장</button>
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                    <colgroup>
                                                        <col width="200">
                                                        <col width="*">
                                                        <col width="300">
                                                        <col width="300">
                                                        <col width="300">
                                                    </colgroup>

                                                    <thead>
                                                        <tr>
                                                            <th>청구월</th>
                                                            <th>납부방법</th>
                                                            <th>이용기관수</th>
                                                            <th>수납금액</th>
                                                            <th>입금건수</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody2">
                                                        <tr>
                                                            <td rowspan="2">
                                                                <button type="button" class="btn btn-xs btn-link">0000.00
                                                                </button>
                                                            </td>
                                                            <td>가상계좌</td>
                                                            <td>0</td>
                                                            <td>0</td>
                                                            <td>0</td>
                                                        </tr>
                                                        <tr>
                                                            <td>온라인신용카드</td>
                                                            <td>0</td>
                                                            <td>0</td>
                                                            <td>0</td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="daily-book-kepping" role="tabpanel" class="tab-pane fade">
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
                                                                        <button class="btn btn-sm btn-primary btn-outline" id="btn_yesterDay">어제</button>
                                                                        <button class="btn btn-sm btn-primary btn-outline" id="btn_agoWeek">일주일</button>
                                                                        <button class="btn btn-sm btn-primary btn-outline" id="btn_agoMonth">1개월</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label class="form-label block">기관분류</label>
                                                            <div class="form-group form-group-sm">
                                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                                    <input type="checkbox" id="inlineCheckbox2-1" onclick="allClick()" value="all" checked>
                                                                    <label for="inlineCheckbox2-1"> 전체 </label>
                                                                </div>
                                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                                    <input type="checkbox" id="inlineCheckbox2-2" name="inlineCheckbox2" onclick="notAll()" value="01" checked>
                                                                    <label for="inlineCheckbox2-2"> WEB </label>
                                                                </div>
                                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                                    <input type="checkbox" id="inlineCheckbox2-3" name="inlineCheckbox2" onclick="notAll()" value="03" checked>
                                                                    <label for="inlineCheckbox2-3"> API </label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label class="form-label block">기관코드</label>
                                                            <div class="form-group form-group-sm">
                                                                <div class="input-group col-md-12">
                                                                    <div class="input-group">
                                                                        <input type="text" class="form-control" name="chacd" id="chacd2" maxlength="50">
                                                                        <span class="input-group-btn">
                                                                            <button type="button" class="btn btn-primary btn-lookup-collecter">기관검색</button>
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <hr>

                                                    <div class="text-center">
                                                        <!-- TODO: 임시 처리
                                                            <button class="btn btn-primary" type="button" onclick="search2();" >조회</button>
                                                        -->
                                                        <button class="btn btn-primary" type="button">조회</button>
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
                                            <div class="col-lg-12" style="text-align:right">
                                                <strong class="text-success">&lt;2018.07. 13시 기준&gt;</strong>
                                            </div>
                                        </div>
                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                    <colgroup>
                                                        <col width="200">
                                                        <col width="*">
                                                        <col width="300">
                                                        <col width="300">
                                                    </colgroup>

                                                    <thead>
                                                        <tr>
                                                            <th>기관분류</th>
                                                            <th>이용기관 수</th>
                                                            <th>총 수납금액</th>
                                                            <th>총 수납건수</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody3">
                                                        <tr>
                                                            <td>전체</td>
                                                            <td>1050개</td>
                                                            <td>5,500,000원</td>
                                                            <td>10,500건</td>
                                                        </tr>
                                                        <tr>
                                                            <td>WEB</td>
                                                            <td>350개</td>
                                                            <td>1,500,000원</td>
                                                            <td>3,500건</td>
                                                        </tr>
                                                        <tr>
                                                            <td>API</td>
                                                            <td>600개</td>
                                                            <td>4,000,000원</td>
                                                            <td>7,777건</td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-6">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <h3>수납매체활용율</h3>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <canvas id="dayGraphpie" width="280" height="280" class="chartjs-render-monitor"></canvas>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-lg-6">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <h3>월별수납현황</h3>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <canvas id="dayGraphBar" width="280" height="280" class="chartjs-render-monitor"></canvas>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="ibox-title">
                                            <div class="col-lg-6">
                                                <strong class="text-primary" id="dayTotRcpCnt">&lt; 검색결과 : 총 0 개월 &gt;</strong>
                                            </div>
                                            <div class="col-lg-6 form-inline form-searchOrderBy">
                                                <button class="btn btn-primary" onclick="">파일저장</button>
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                                                    <colgroup>
                                                        <col width="200">
                                                        <col width="*">
                                                        <col width="300">
                                                        <col width="300">
                                                        <col width="300">
                                                    </colgroup>

                                                    <thead>
                                                        <tr>
                                                            <th>청구일자</th>
                                                            <th>납부방법</th>
                                                            <th>이용기관수</th>
                                                            <th>수납금액</th>
                                                            <th>입금건수</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody4">
                                                        <tr>
                                                            <td rowspan="2">
                                                                <button type="button" class="btn btn-xs btn-link">
                                                                    2018.05.25
                                                                </button>
                                                            </td>
                                                            <td>가상계좌</td>
                                                            <td>100</td>
                                                            <td>800,000</td>
                                                            <td>1000</td>
                                                        </tr>
                                                        <tr>
                                                            <td>온라인신용카드</td>
                                                            <td>50</td>
                                                            <td>800,000</td>
                                                            <td>1000</td>
                                                        </tr>
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

<script type="text/javascript">
    $(document).ready(function () {
        //월별 수납조회 달력 기본값
        getYearsBox('syearsBox');
        getMonthBox2('smonthBox', '1');
        getYearsBox('eyearsBox');
        getMonthBox('emonthBox');
        //일별 수납조회 달력 기본값
        $("#startday2").val(getDateFmtDot(getCurrentDate()));
        $("#endday2").val(getDateFmtDot(getCurrentDate()));
        //날짜 달력
        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            forceParse: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            autoclose: true
        });

        // 1~3달 세팅
        function whatday(no) {
            var settingDate = new Date();
            var month = settingDate.getMonth() + 1;
            var day = settingDate.getDate();
            var day_tran = settingDate.getDate() - no;
            var year = settingDate.getFullYear();

            if (month < 10) {
                month = '0' + month;
            }
            if (day_tran < 10) {
                day_tran = '0' + day_tran;
            }
            $("#startday2").val(year + '.' + month + '.' + day_tran);
            $("#endday2").val(year + '.' + month + '.' + day);
        }

        //일별 수납현황 몇일전 조회버튼 세팅
        $('#btn_yesterDay').on('click', function (e) {
            e.preventDefault();
            whatday(1);
        });
        $('#btn_agoWeek').on('click', function (e) {
            e.preventDefault();
            whatday(7);
        });
        $('#btn_agoMonth').on('click', function (e) {
            e.preventDefault();
            $("#startday2").val(monthAgo(getCurrentDate(), 1));	//1개월
        });
        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($('#chacd').val());
            $("#popChaname").val('');
            fn_ListCollector();
        });
        //기관분류 체크박스 선택 값 구하기
        var getComSt = function () {
            if ($("#inlineCheckbox1-1").prop("checked")) {
                comSt = "00";
            } else {
                if ($("#inlineCheckbox1-2").prop("checked") == true && $("#inlineCheckbox1-3").prop("checked") == true) {
                    comSt = "00";
                } else {
                    if ($("#inlineCheckbox1-2").prop("checked") == true) {
                        comSt = "01";
                    } else {
                        if ($("#inlineCheckbox1-3").prop("checked") == true) {
                            comSt = "03";
                        }
                        if ($("#inlineCheckbox1-2").prop("checked") == true && $("#inlineCheckbox1-3").prop("checked") == true) {
                            comSt = "";
                            swal({
                                type: 'info',
                                text: "기관분류를 선택해 주세요 !!",
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                }
            }
            return comSt;
        };
        // 총수납현황 table lable (ex:<201711 14:00기준>
        var nowTime = getCurTime();
        $('.text-success').html("&lt;" + nowTime.substr(0, 8) + "  " + nowTime.substr(9, 2) + ":" + nowTime.substr(11, 2) + "기준" + "&gt;");
        var comSt = getComSt(); //조회조건 기관분류 chkbox
        search(comSt);	//월별 수납현황 조회
        //search2(comSt);	//일별 수납현황 조회
        var pmet = new Array();
        $.each('${map.tlist}', function (i, t) {
            if (t.payMethod != 'total') {
                pmet.push(t.ccSum);
            }
        });
        var barChartData1 = {
            labels: ['가상계좌', '카드'],
            datasets: [{
                label: '수납 매체 활용률',
                backgroundColor: ['skyblue', 'blue'],
                data: pmet
            }]
        }
        var ctx1 = document.getElementById("monthGraphpie");
        var myBarChart = new Chart(ctx1, {
            type: 'pie',
            data: barChartData1
        });

        //기관검색 버튼 클릭시 모달창 팝업
        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($('#chacd').val());
            fn_ListCollector();
        });

        //기관분류 체크박스
        var allClick = function () {
            if ($("#inlineCheckbox1-1").is(":checked")) {
                $("#inlineCheckbox1-2").prop("checked", true);
                $("#inlineCheckbox1-3").prop("checked", true);
            } else {
                $("#inlineCheckbox1-2").prop("checked", false);
                $("#inlineCheckbox1-3").prop("checked", false);
            }
        };
        var notAll = function () {
            $("inlineCheckbox1-1").prop("checked", false);
        };

        $("#inlineCheckbox1-1").on('click', function (e) {
            allClick();
        });
        $("#inlineCheckbox1-2").on('click', function (e) {
            notAll();
        });
        $("#inlineCheckbox1-3").on('click', function (e) {
            notAll();
        });
    });
</script>