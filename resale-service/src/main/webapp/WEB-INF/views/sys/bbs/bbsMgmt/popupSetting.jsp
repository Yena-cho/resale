<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-4";
    var twoDepth = "adm-sub-09";
</script>

<script>
    $(document).ready(function () {
        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            forceParse: false,
            autoclose: true
        });

        $('.btn-open-counselling-dialog').click(function () {
            $("#modal-edit-counselling-dialog").modal({
                backdrop: 'static',
                keyboard: false
            });
        });

        $('.input-group.date').datepicker({
            todayBtn: "linked",
            keyboardNavigation: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            forceParse: false,
            calendarWeeks: true,
            autoclose: true
        });

        setMonthTerm(0);

        search();
    })

    //조회 버튼 클릭
    function search() {
        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");
        if (startday > endday && endday != '' && endday != null) {
            swal({
                type: 'info',
                text: '시작일이 마감일보다 큽니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return
        }

        var option1 = false;
        var option2 = false;
        var option3 = false;

        if ($("#inlineCheckbox2-0").is(":checked")) {
            option1 = true;
        }
        if ($("#inlineCheckbox2-1").is(":checked")) {
            option2 = true;
        }
        if ($("#inlineCheckbox2-2").is(":checked")) {
            option3 = true;
        }
        var param = {
            keyword: $("#keyword").val(),
            pageScale: "10",
            curPage: "1",
            searchOption: $("#search_option option:selected").val(),
            option1: option1,
            option2: option2,
            option3: option3,
            startDt: startday,
            endDt: endday,
            searchOrderBy: $("#searchOrderBy option:selected").val(),
            pageScale: $("#page_scale option:selected").val()
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxPopupSetting",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    //$("#"+ obj).html(str);
                    $("#totcount").text(result.count);
                    sysajaxPaging(result, 'PageArea');
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

    //페이지 변경
    function list(page) {
        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");
        if (startday > endday && endday != '' && endday != null) {
            swal({
                type: 'info',
                text: '시작일이 마감일보다 큽니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return
        }
        ;

        var option1 = false;
        var option2 = false;
        var option3 = false;

        if ($("#inlineCheckbox2-0").is(":checked")) {
            option1 = true;
        }
        if ($("#inlineCheckbox2-1").is(":checked")) {
            option2 = true;
        }
        if ($("#inlineCheckbox2-2").is(":checked")) {
            option3 = true;
        }

        var param = {
            keyword: $("#keyword").val(),
            pageScale: "10",
            curPage: page,
            searchOption: $("#search_option option:selected").val(),
            option1: option1,
            option2: option2,
            option3: option3,
            startdt: startday,
            enddt: endday,
            searchOrderBy: $("#searchOrderBy option:selected").val(),
            pageScale: $("#page_scale option:selected").val()
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxPopupSetting",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    //$("#"+ obj).html(str);
                    $("#totcount").text(result.count);
                    sysajaxPaging(result, 'PageArea');
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

    //수정 버튼 클릭
    function update(no) {
        location.href = "/sys/popupUpdate?no=" + no;
    }

    //글쓰기 버튼 클릭
    function regist() {
        location.href = "/sys/popupWrite";
    }

    //삭제 버튼 클릭
    function popupDelete(no) {
        swal({
            type: 'question',
            html: "삭제하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var param = {
                    no: no
                };
                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/sys/ajaxPopupDelete",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '삭제되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            })
                            list(1);
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
        });
    }

    function fnGrid(result, obj) {
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr id=' + v.no + '>';
                str += '<td>' + v.rn + '</td>';
                str += '<td class="title">' + v.title + '</td>';
                str += '<td class="contents">' + v.contents + '</td>';
                if (v.url == 'O') {
                    str += '<td>' + v.url + '</td>';
                } else if (v.url == 'X') {
                    str += '<td class="text-danger">' + v.url + '</td>';
                }
                if (v.exposyn == 'y' || v.exposyn == 'Y') {
                    str += '<td>노출</td>';
                } else if (v.exposyn == 'n' || v.exposyn == 'N') {
                    str += '<td class="text-danger">미노출</td>';
                }
                if (v.dxpstype == 'C' || v.dxpstype == 'c') {
                    str += '<td>지속</td>';
                } else if (v.dxpstype == 'p' || v.dxpstype == 'P') {
                    str += '<td>' + v.startDt + '-' + v.endDt + '</td>';
                }
                str += '<td>' + v.regDt + '</td>';
                str += '<td>';
                str += '<button class="btn btn-xs btn-warning btn-edit-notice" onclick="update(' + v.no + ')">수정</button>';
                str += '<button class="btn btn-xs btn-danger btn-delete"  onclick="popupDelete(' + v.no + ')">삭제</button>';
                str += '</td>';
                str == '</tr>';

            });
        }
        $("#" + obj).html(str);
        $("#totcount").text(result.count);
        sysajaxPaging(result, 'PageArea');
    }
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>팝업관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>팝업관리</strong>
            </li>
        </ol>
        <p class="page-description">메인 페이지 팝업을 조회하고 관리하는 화면입니다.</p>
    </div>

    <div class="col-lg-2 text-right m-t-xl">
        <button class="btn btn-lg btn-w-m btn-primary" onclick="regist()">등록</button>
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
                        <div class="row">
                            <div class="col-md-6">
                                <label class="form-label block">등록일자</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group col-md-12">
                                        <div class="input-daterange input-group float-left" id="datepicker">
                                            <input type="text" maxlength="8" onkeyup="onlyNumber(this)" class="input-sm form-control" name="start" id="startday" readonly="readonly"/>
                                            <span class="input-group-addon">to</span>
                                            <input type="text" maxlength="8" onkeyup="onlyNumber(this)" class="input-sm form-control" name="end" id="endday" readonly="readonly"/>
                                        </div>

                                        <div class="daterange-setMonth">
                                            <button id="btnSetMonth0" name="btnSetMonth" class="btn btn-sm btn-primary btn-outline" onclick="setMonthTerm(0)">전체</button>
                                            <button id="btnSetMonth1" name="btnSetMonth" class="btn btn-sm btn-primary btn-outline" onclick="setMonthTerm(1)">1개월</button>
                                            <button id="btnSetMonth3" name="btnSetMonth" class="btn btn-sm btn-primary btn-outline" onclick="setMonthTerm(6)">6개월</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6"></div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <label class="form-label block">검색구분</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group col-md-12">
                                        <span class="input-group-select">
                                            <select class="form-control" id="search_option">
                                                <option value="title">팝업명</option>
                                                <option value="contents">내용</option>
                                            </select>
                                        </span>
                                        <input type="text" class="form-control" id="keyword" maxlength="25" onkeypress="if(event.keyCode == 13){search();}">
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label block">노출여부</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group col-md-12">
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-0" name="show" checked>
                                            <label for="inlineCheckbox2-0"> 전체 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-1" name="show">
                                            <label for="inlineCheckbox2-1"> 노출 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-2" name="show">
                                            <label for="inlineCheckbox2-2"> 미노출 </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <hr>

                        <div class="text-center">
                            <button class="btn btn-primary" type="button" id="search()" type="button" onclick="search()">조회</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="animated fadeInRight" id="focus">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">전체 건수 : <strong class="text-success" id="totcount">${map.count }</strong> 건</div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" id="searchOrderBy" onchange="search()">
                                <option value="regDt">등록일자순</option>
                                <option value="name">팝업명순</option>
                            </select>

                            <select class="form-control" id="page_scale" onchange="search()">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center has-ellipsis">
                                <!-- 2018.05.11 클래스 수정 -->
                                <colgroup>
                                    <col width="50">
                                    <col width="250">
                                    <col width="690">
                                    <col width="80">
                                    <col width="80">
                                    <col width="200">
                                    <col width="160">
                                    <col width="100">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>팝업명</th>
                                        <th>내용</th>
                                        <th>링크URL</th>
                                        <th>노출여부</th>
                                        <th>노출기간</th>
                                        <th>등록일</th>
                                        <th></th>
                                    </tr>
                                </thead>

                                <tbody id="reSearchbody">
                                    <c:choose>
                                        <c:when test="${map.list.size() > 0}">
                                            <c:forEach var="row" items="${map.list}">
                                                <tr id="${row.no }">
                                                    <td>${row.rn }</td>
                                                    <td class="title">${row.title }</td>
                                                    <td class="contents">${row.contents }</td>
                                                    <c:if test="${row.url.equals('O')}">
                                                        <td>${row.url}</td>
                                                    </c:if>
                                                    <c:if test="${row.url.equals('X')}">
                                                        <td class="text-danger">${row.url}</td>
                                                    </c:if>
                                                    <c:if test="${row.exposyn.equals('Y')}">
                                                        <td>노출</td>
                                                    </c:if>
                                                    <c:if test="${row.exposyn.equals('N')}">
                                                        <td class="text-danger">미노출</td>
                                                    </c:if>
                                                    <c:if test="${row.dxpstype.equals('C')}">
                                                        <td>지속</td>
                                                    </c:if>
                                                    <c:if test="${row.dxpstype.equals('P')}">
                                                        <td>${row.startDt } - ${row.endDt }</td>
                                                    </c:if>
                                                    <td>${row.regDt }</td>
                                                    <td>
                                                        <button class="btn btn-xs btn-warning btn-edit-notice" onclick="update(${row.no })">수정</button>
                                                        <button class="btn btn-xs btn-danger btn-delete" onclick="popupDelete(${row.no })">삭제</button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>

                                        <c:otherwise>
                                            <tr>
                                                <td colspan="8" style="text-align: center;">[조회된 내역이 없습니다.]</td>
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

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<!-- 전화상담 진행상황 등록 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/edit-counselling-dialog.jsp" flush="false"/>
