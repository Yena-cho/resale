<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-1";
    var twoDepth = null;
</script>

</div>
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="m-b-md">
                <h3><strong>가입/상담 대기 현황</strong></h3>
            </div>
        </div>
    </div>

    <div class="row">
        <%--<div class="col-lg-3">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>가입 대기 기관</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <a href="/sys/chaMgmt/newChaList"><strong><div class="counter"><span id="thismonreadycnt">0</span></div></strong></a>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-3">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>변경 대기 목록</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <a href="/sys/chaMgmt/changeChaList"><strong><div class="counter"><span id="changeChaCnt">0</span></div></strong></a>
                    </h1>
                </div>
            </div>
        </div>--%>

        <div class="col-lg-2">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>문자서비스 신청</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <a href="/sys/addServiceMgmt/smsRegManage"><strong><div class="counter"><span id="usesmsaplcnt">0</span></div></strong></a>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-2">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>서비스 문의</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <a href="/sys/qnaSetting"><strong><div class="counter"><span id="serviceaskcnt">0</span></div></strong></a>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-2">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>전화상담예약</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <a href="/sys/counselSetting"><strong><div class="counter"><span id="phonebookingcnt">0</span></div></strong></a>
                    </h1>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="m-b-md">
                <h3 class="dashboard-title"><span id="nowYearMonth"></span> 이용기관 현황</h3>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-4">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>전체 이용기관</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <strong><div class="counter"><span id="totchacnt">0</span></div></strong>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-2">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>신규기관 (전월/당월)</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <strong><div class="counter"><span class="text-info" id="lastmonregcnt">0</span>/<span class="text-success" id="thismonregcnt">0</span></div></strong>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-2">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>해지기관 (전월/당월)</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <strong><div class="counter"><span class="text-info" id="lastmonresigncnt">0</span>/<span class="text-success" id="thismonresigncnt">0</span></div></strong>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-2">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>SMS (전월/당월)</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <strong><div class="counter"><span class="text-info" id="lastmonsmscnt">0</span>/<span class="text-success" id="thismonsmscnt">0</span></div></strong>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-2">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>LMS (전월/당월)</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <strong><div class="counter"><span class="text-info" id="lastmonlmscnt">0</span>/<span class="text-success" id="thismonlmscnt">0</span></div></strong>
                    </h1>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-6">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>미납기관 내역</h5>
                    <span class="pull-right">정산현황 바로가기 <i class="fa fa-external-link"></i></span>
                </div>
                <div class="ibox-content">
                    <div class="table-responsive">
                        <table class="table table-primary table-bordered table-hover table-striped">
                            <colgroup>
                                <col width="30%">
                                <col width="50%">
                                <col width="20%">
                            </colgroup>

                            <thead>
                                <tr>
                                    <th>최종청구월</th>
                                    <th>기관명</th>
                                    <th>관리수수료(원)</th>
                                </tr>
                            </thead>

                            <tbody id="resultBody"></tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>수수료 증감현황</h5>
                </div>
                <div class="ibox-content">
                    <div class="graph-wrap">
                        <canvas id="graph" style="width: 100%; height: 230px;"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.js"></script>

<script>
    $(document).ready(function () {
        fnXmontSum();
        fnFeeBarChart();
        fnPaymentSummary('resultBody');

        $("#nowYearMonth").html(getYear() + "년 " + getMonth() + "월 ");
    });


    // 이용기관현황 대시보드
    function fnXmontSum() {
        var url = "/sys/main/getSysMainInfo01Ajax";
        var param = {};

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                var data = result.info;
                $("#totchacnt").html(numberToCommas(data.totchacnt));
                $("#lastmonregcnt").html(numberToCommas(data.lastmonregcnt));
                $("#thismonregcnt").html(numberToCommas(data.thismonregcnt));
                $("#lastmonresigncnt").html(numberToCommas(data.lastmonresigncnt));
                $("#thismonresigncnt").html(numberToCommas(data.thismonresigncnt));
                $("#thismonreadycnt").html(numberToCommas(data.thismonreadycnt));
                $("#lastmonsmscnt").html(numberToCommas(data.lastmonsmscnt));
                $("#thismonsmscnt").html(numberToCommas(data.thismonsmscnt));
                $("#lastmonlmscnt").html(numberToCommas(data.lastmonlmscnt));
                $("#thismonlmscnt").html(numberToCommas(data.thismonlmscnt));
                $("#usesmsaplcnt").html(numberToCommas(data.usesmsaplcnt));
                $("#serviceaskcnt").html(numberToCommas(data.serviceaskcnt));
                $("#phonebookingcnt").html(numberToCommas(data.phonebookingcnt));
                $("#changeChaCnt").html(numberToCommas(data.changeChaCnt));
            }
        });
    }

    function fnFeeBarChart() {
        var url = "/sys/main/selectPaymentRatioList";
        var param = {};

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                var arrlabel = new Array();
                var arrnoti = new Array();
                var arrrcp = new Array();

                $.each(result.list, function (i, v) {
                    arrlabel.push(v.month);
                    arrnoti.push(v.notifee);
                    arrrcp.push(v.rcpfee);
                });

                var barChartData = {
                    labels: arrlabel,
                    datasets: [{
                        label: '관리수수료',
                        backgroundColor: 'skyblue',
                        data: arrnoti
                    }, {
                        label: '입금수수료',
                        backgroundColor: 'red',
                        data: arrrcp
                    }]
                }

                var ctx2 = document.getElementById("graph");
                var myBarChart = new Chart(ctx2, {
                    type: 'bar',
                    data: barChartData
                });
            }
        });
    }

    function fnPaymentSummary(obj) {
        var url = "/sys/main/selectPaymentSummary";
        var param = {};

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                var str = "";
                if (result == null || result.count <= 0) {
                    str += '<tr><td colspan="4" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
                } else {
                    $.each(result.list, function (i, v) {
                        str += '<tr>';
                        str += '<td>' + v.month.substring(0, 4) + "." + v.month.substring(4, 6) + '</td>';
                        str += '<td>' + v.chaname + '</td>';
                        str += '<td class="text-warning text-right">' + comma(v.totsumfee) + '</td>';
                        str += '</tr>';
                    });
                    $('#totCnt').text(result.count);
                }
                $('#' + obj).html(str);
            }
        });
    }

    //금액 콤마
    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }
</script>

