<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/html/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-3";
    var twoDepth = "adm-sub-05";
</script>

<script>
    //페이지 변경
    function list(page) {
        var param = {
            pageScale: "10",
            curPage: page
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxSysSmsList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    sysajaxPaging(result, 'PageArea');
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

    function fnGrid(result, obj) {
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr id=' + v.smsSeq + '>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.reqDate + '</td>';
                str += '<td>' + v.userStatus + '</td>';
                str += '<td>' + v.sendCnt + '</td>';
                str += '<td class="text-danger">' + v.failCnt + '</td>';
                str += '<td>' + v.telNo + '</td>';
                str += '<td class="title">' + v.title + '</td>';
                str += '<td class="context modalw">' + v.smsMsg + '</td>';
            });
        }
        $("#" + obj).html(str);
    }

    //이메일 페이지 변경
    function list2(page) {
        var param = {
            pageScale: "10",
            curPage: page
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxSysEmailList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid2(result, 'reSearchbodye');
                    sysajaxPagingE(result, 'ePageArea');
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

    function fnGrid2(result, obj) {
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="10" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.elist, function (i, v) {
                str += '<tr id=' + v.emailSeq + '>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.emReqDate + '</td>';
                str += '<td>' + v.emailChast + '</td>';
                str += '<td>' + v.emSendCnt + '</td>';
                str += '<td class="text-danger">' + v.emFailCnt + '</td>';
                str += '<td class="title">' + v.emailTitle + '</td>';
                str += '<td class="context emodalw">' + v.emailMsg + '</td>';
            });
        }
        $("#" + obj).html(str);
    }
</script>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>공지발송관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>공지발송관리</strong>
            </li>
        </ol>
        <p class="page-description">서비스 이용 안내 전달을 위한 공지 발송/관리 화면입니다.</p>
    </div>
</div>

<div class="wrapper wrapper-content">
    <div class="animated fadeInUp">
        <div class="row">
            <div class="col-lg-12 m-b-3">
                <div class="tabs-container">
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="active"><a href="#email-notification-list" aria-controls="email-notification-list" role="tab" data-toggle="tab">E-MAIL 공지관리</a></li>
                        <li role="presentation"><a href="#sms-notification-list" aria-controls="sms-notification-list" role="tab" data-toggle="tab">문자메시지 공지관리</a></li>
                    </ul>

                    <div class="tab-content">
                        <div id="email-notification-list" role="tabpanel" class="tab-pane active">
                            <div class="panel-body row m-b-xs">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="m-b-md">
                                            <div class="col-lg-6">
                                                전체 건수 : <strong class="text-success">${map.emCount}</strong> 건
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="table table-stripped table-align-center has-ellipsis">
                                                    <!-- 2018.05.11 클래스 수정 -->
                                                    <colgroup>
                                                        <col width="50">
                                                        <col width="200">
                                                        <col width="120">
                                                        <col width="100">
                                                        <col width="100">
                                                        <col width="350">
                                                        <col width="650">
                                                    </colgroup>

                                                    <thead>
                                                        <tr>
                                                            <th>NO</th>
                                                            <th>발송일시</th>
                                                            <th>대상 구분</th>
                                                            <th>발송건수</th>
                                                            <th>실패건수</th>
                                                            <th>제목</th>
                                                            <th>내용</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbodye">
                                                        <c:choose>
                                                            <c:when test="${map.emlist.size() > 0}">
                                                                <c:forEach var="erow" items="${map.emlist}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td>${erow.rn}</td>
                                                                        <td>${erow.emReqDate}</td>
                                                                        <td>${erow.emailChast}</td>
                                                                        <td>${erow.emSendCnt}</td>
                                                                        <td class="text-danger">${erow.emFailCnt}</td>
                                                                        <td class="title">${erow.emailTitle}</td>
                                                                        <td class="context emodalw">${erow.emailMsg}</td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <tr>
                                                                    <td colspan="10" style="text-align: center;">[조회된
                                                                        내역이없습니다.]
                                                                    </td>
                                                                </tr>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tbody>
                                                </table>
                                            </div>

                                            <jsp:include page="/WEB-INF/views/include/sysPagingE.jsp" flush="false"/>

                                            <div class="row">
                                                <div class="col-lg-12 text-right">
                                                    <button class="btn btn-w-m btn-primary btn-open-send-email-without-list">
                                                        <i class="fa fa-fw fa-envelope-o"></i> E-MAIL 공지 발송
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="sms-notification-list" role="tabpanel" class="tab-pane">
                            <div class="panel-body row m-b-xs">
                                <div class="col-lg-12">
                                    <div class="ibox">
                                        <div class="m-b-md">
                                            <div class="col-lg-6">
                                                전체 건수 : <strong class="text-success">${map.count}</strong> 건
                                            </div>
                                        </div>

                                        <div class="ibox-content" style="border-top: 0">
                                            <div class="table-responsive">
                                                <table class="footable table table-stripped table-align-center has-ellipsis">
                                                    <colgroup>
                                                        <col width="50">
                                                        <col width="200">
                                                        <col width="120">
                                                        <col width="100">
                                                        <col width="100">
                                                        <col width="150">
                                                        <col width="350">
                                                        <col width="500">
                                                    </colgroup>

                                                    <thead>
                                                        <tr>
                                                            <th>NO</th>
                                                            <th>발송일시</th>
                                                            <th>대상구분</th>
                                                            <th>발송건수</th>
                                                            <th>실패건수</th>
                                                            <th>발신번호</th>
                                                            <th>제목</th>
                                                            <th>내용</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="reSearchbody">
                                                        <c:choose>
                                                            <c:when test="${map.list.size() > 0}">
                                                                <c:forEach var="row" items="${map.list}" varStatus="status">
                                                                    <tr>
                                                                        <td>${row.rn }</td>
                                                                        <td>${row.reqDate }</td>
                                                                        <td>${row.userStatus }</td>
                                                                        <td>${row.sendCnt }</td>
                                                                        <td class="text-danger">${row.failCnt }</td>
                                                                        <td>${row.telNo}</td>
                                                                        <td class="title">${row.title}</td>
                                                                        <td class="context modalw">${row.smsMsg }</td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <tr>
                                                                    <td colspan="10" style="text-align: center;">[조회된
                                                                        내역이없습니다.]
                                                                    </td>
                                                                </tr>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <jsp:include page="/WEB-INF/views/include/sysPaging.jsp" flush="false"/>

                                            <div class="row">
                                                <div class="col-lg-12 text-right">
                                                    <button class="btn btn-w-m btn-primary btn-open-send-sms-without-list"><i class="fa fa-fw fa-mobile"></i> 문자 공지 발송</button>
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
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- 문자 보내기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/send-sms-without-list.jsp" flush="false"/>
<jsp:include page="/WEB-INF/views/include/modal/admin/sys-sms-detail.jsp" flush="false"/>

<!-- 이메일 보내기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/send-email-without-list.jsp" flush="false"/>
<jsp:include page="/WEB-INF/views/include/modal/admin/sys-email-detail.jsp" flush="false"/>

<script>
    $(document).on("click", '.modalw', function () {
        var txt = $(this).text();
        $("#smsMsgMd").html(txt);
        $("#popup-sys-sms-detail").modal({
            backdrop: 'static',
            keyboard: false
        });
    });

    //이메일 내용 상세보기
    $(document).on("click", '.emodalw', function () {
        var txt = $(this).text();
        $("#EmailMsgMd").html(txt);
        $("#popup-sys-email-detail").modal({
            backdrop: 'static',
            keyboard: false
        });
    });

    $('.btn-open-send-email-without-list').click(function () {
        $("input[type=checkbox]").prop("checked", false);
        $('#etitle').val("");
        $('#emailMsg').val("");
        $("#mByte").html("0/2000 byte");
        $("#emailCounting").html("수신대상(총 0개 기관)");
        $("#popup-send-email-without-list").modal({
            backdrop: 'static',
            keyboard: false
        });
    });


    $('.btn-open-send-sms-without-list').click(function () {
        $("input[type=checkbox]").prop("checked", false);
        $('#title').val("");
        $('#smsMsg').val("");
        $("#messagebyte").html("0/1000 byte");
        $("#smsCounting").html("수신대상(총 0개 기관)");
        $("#popup-send-sms-without-list").modal({
            backdrop: 'static',
            keyboard: false
        });
    });
</script>
