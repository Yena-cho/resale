<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-5";
    var twoDepth = "adm-sub-14";
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

    //조회
    function search() {
        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");
        if (startday > endday) {
            var temp = startday;
            startday = endday;
            endday = temp;
        }

        if (getCurrentDate() < endday || getCurrentDate() < endday) {
            swal({
                type: 'error',
                text: '현재일보다 이후를 조회할 수 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return false;
        }


        var sendSt = "";
        if ($("#inlineCheckbox1-1").prop("checked")) {
            sendSt = "all";
        } else {
            sendSt = $("input[name='inlineCheckbox1']:checked").val();
        }
        var pageScale = $("#pageScale").val();
        var searchOrderBy = $("#searchOrderBy").val();

        var param = {
            startday: startday,
            endday: endday,
            comCd: $("#comCd").val(),
            comNm: $("#comNm").val(),
            searchOption: $("#searchOption option:selected").val(),
            keyword: $("#keyword").val(),
            sendSt: sendSt,
            curPage: "1",
            pageScale: pageScale,
            searchOrderBy: searchOrderBy
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/rcpMgmt/smsUseList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    sysajaxPaging(result, 'PageArea');
                    /* var scmove = $('#focus').offset().top;
                    $('html, body').animate( { scrollTop : scmove }, 300 ); */
                } else {
                    swal({
                        type: 'error',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });
    }

    // 페이지변경
    function list(page) {
        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");
        if (startday > endday) {
            var temp = startday;
            startday = endday;
            endday = temp;
        }

        var sendSt = "";
        if ($("#inlineCheckbox1-1").prop("checked")) {
            sendSt = "all";
        } else {
            sendSt = $("input[name='inlineCheckbox1']:checked").val();
        }
        var pageScale = $("#pageScale").val();
        var searchOrderBy = $("#searchOrderBy").val();

        var param = {
            startday: startday,
            endday: endday,
            comCd: $("#comCd").val(),
            comNm: $("#comNm").val(),
            searchOption: $("#searchOption option:selected").val(),
            keyword: $("#keyword").val(),
            sendSt: sendSt,
            curPage: page,
            pageScale: pageScale,
            searchOrderBy: searchOrderBy
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/rcpMgmt/smsUseList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    sysajaxPaging(result, 'PageArea');
                    /* var scmove = $('#focus').offset().top;
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
        if (result.count <= 0) {
            str += '<tr><td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr id=' + v.rn + '>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.day + '</td>';
                str += '<td>' + v.comCd + '</td>';
                str += '<td>' + v.comNm + '</td>';
                str += '<td>' + v.name + '</td>';
                str += '<td>' + v.sendSt + '</td>';
                str += '<td><button type="button" class="btn btn-xs btn-link text-success btn-open-sms-sent-list">' + v.sendcount + '</button></td>';
                str += '<td><button type="button" class="btn btn-xs btn-link text-danger btn-open-sms-failed-list">' + v.failcount + '</button></td>';
                str += '<td>' + v.charge + '</td>';
                str += '</tr>';
            });

        }
        $("#" + obj).html(str);
        $("#fcount").text(result.fcount);
        $("#acount").text(result.acount);
    }

    //파일저장
    function fn_fileSave() {
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
            if (result.value) {
                var startday = $("#startday").val().replace(/\./gi, "");
                var endday = $("#endday").val().replace(/\./gi, "");
                if (startday > endday) {
                    var temp = startday;
                    startday = endday;
                    endday = temp;
                }

                var sendSt = "";
                if ($("#inlineCheckbox1-1").prop("checked")) {
                    sendSt = "all";
                } else {
                    sendSt = $("input[name='inlineCheckbox1']:checked").val();
                }

                $('#fstartday').val(startday);
                $('#fendday').val(endday);
                $('#fsendSt').val(sendSt);
                $('#fcomCd').val($("#comCd").val());
                $('#fcomNm').val($("#comNm").val());
                $('#fsearchOption').val($("#searchOption option:selected").val());
                $('#fkeyword').val($("#keyword").val());
                $('#fsearchOrderBy').val($("#searchOrderBy").val());
                // 다운로드
                $('#fileForm').submit();
            }
        });
    }
</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/rcpMgmt/smsuseexcelDown">
    <input type="hidden" id="fstartday" name="startday" value=""/>
    <input type="hidden" id="fendday" name="endday" value=""/>
    <input type="hidden" id="fcomCd" name="comCd" value=""/>
    <input type="hidden" id="fcomNm" name="comNm" value=""/>
    <input type="hidden" id="fsearchOption" name="searchOption" value=""/>
    <input type="hidden" id="fkeyword" name="keyword" value=""/>
    <input type="hidden" id="fsendSt" name="sendSt" value=""/>
    <input type="hidden" id="fsearchOrderBy" name="searchOrderBy" value=""/>
</form>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>문자서비스이용내역</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>정산관리</a>
            </li>
            <li class="active">
                <strong>문자서비스이용내역</strong>
            </li>
        </ol>
        <p class="page-description">문자서비스 이용 수수료를 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<div class="wrapper-content">
    <div class="animated fadeInRight article">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>검색</h5>
                    </div>

                    <div class="ibox-content">
                        <form>
                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">발송일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" name="start" id="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" name="end" id="endday" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button class="btn btn-sm btn-primary btn-outline" onclick="whatday(1)">1개월</button>
                                                <button class="btn btn-sm btn-primary btn-outline" onclick="whatday(2)">2개월</button>
                                                <button class="btn btn-sm btn-primary btn-outline" onclick="whatday(3)">3개월</button>
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
                                        <input type="text" class="form-control" onkeyup="removeChar(event)" id="comCd">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control" id="comNm">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                             <span class="input-group-select">
                                                <select class="form-control" id="searchOption">
                                                    <option>출금계좌</option>
                                                    <option>납부자명</option>
                                                </select>
                                            </span>
                                            <input type="text" class="form-control" id="keyword">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">발송유형</label>
                                    <div class="form-group form-group-sm">
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-1" value="all">
                                            <label for="inlineCheckbox1-1"> 전체 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-2" name="inlineCheckbox1" value="1">
                                            <label for="inlineCheckbox1-2"> SMS </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-3" name="inlineCheckbox1" value="2">
                                            <label for="inlineCheckbox1-3"> LMS </label>
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

    <div class="animated fadeInRight">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">
                            <span class="m-r-sm m-l-sm">발송 실패건수 : <strong class="text-success" id="fcount">90</strong> 건</span>
                            <span> | </span>
                            <span class="m-l-sm m-r-sm">전체 발송건수 : <strong class="text-success" id="acount">90</strong> 건</span>
                        </div>
                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" id="searchOrderBy">
                                <option value="day">발송일시 순</option>
                                <option value="sendSt">발송유형 순</option>
                            </select>
                            <select class="form-control" id="pageScale">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" type="button" onclick="fn_fileSave();">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center"><!-- 2018.05.11 클래스 수정 -->
                                <colgroup>
                                    <col width="50">
                                    <col width="150">
                                    <col width="180">
                                    <col width="*">
                                    <col width="200">
                                    <col width="100">
                                    <col width="120">
                                    <col width="120">
                                    <col width="200">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>발송일시</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>담당자명</th>
                                        <th>발송유형</th>
                                        <th>발송건수</th>
                                        <th>실패건수</th>
                                        <th>이용금액(원)</th>
                                    </tr>
                                </thead>

                                <tbody id="reSearchbody">
                                    <c:choose>
                                        <c:when test="${map.list.size() > 0}">
                                            <c:forEach var="row" items="${map.list}" varStatus="status">
                                                <tr>
                                                    <td>${row.rn }</td>
                                                    <td>${row.day }</td>
                                                    <td>${row.comCd}</td>
                                                    <td>${row.comNm }</td>
                                                    <td>${row.name }</td>
                                                    <td>${row.sendSt }</td>
                                                    <td>
                                                        <button type="button"
                                                                class="btn btn-xs btn-link text-success btn-open-sms-sent-list">${row.sendcount }</button>
                                                    </td>
                                                    <td>
                                                        <button type="button"
                                                                class="btn btn-xs btn-link text-danger btn-open-sms-failed-list">${row.failcount }</button>
                                                    </td>
                                                    <td>${row.charge }</td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>

                                        <c:otherwise>
                                            <tr>
                                                <td colspan="7" style="text-align: center;">[조회된 내역이없습니다.]</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
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

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<script>
    $(document).ready(function () {
        var now = new Date();
        var endDay = now.getFullYear() + "." + addZero(now.getMonth() + 1) + "." + addZero(now.getDate());
        $("#endday").val(endDay);
        $("#inlineCheckbox1-1").prop("checked", true);
        search();
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

    $('.input-daterange').datepicker({
        keyboardNavigation: false,
        forceParse: false,
        format: 'yyyy.mm.dd',
        maxDate: "+0d",
        autoclose: true
    });

    /* $('.btn-open-sms-sent-list').click(function(){
        $("#popup-sms-sent-list").modal({
            backdrop : 'static',
            keyboard : false
        });
    });

    $('.btn-open-sms-failed-list').click(function(){
        $("#popup-sms-failed-list").modal({
            backdrop : 'static',
            keyboard : false
        });
    }); */
</script>
