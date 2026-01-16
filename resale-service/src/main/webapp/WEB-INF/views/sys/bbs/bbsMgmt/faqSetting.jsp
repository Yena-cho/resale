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
    var twoDepth = "adm-sub-07";
</script>

<script>
    $(document).ready(function () {

        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            forceParse: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
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

        var option0 = false;
        var option1 = false;
        var option2 = false;
        var option3 = false;

        if ($("#inlineCheckbox1-0").is(":checked")) {
            option0 = true;
        }
        if ($("#inlineCheckbox1-1").is(":checked")) {
            option1 = true;
        }
        if ($("#inlineCheckbox1-2").is(":checked")) {
            option2 = true;
        }
        if ($("#inlineCheckbox1-3").is(":checked")) {
            option3 = true;
        }
        var param = {
            keyword: $("#keyword").val(),
            searchOrderBy: "num",
            pageScale: $("#page_scale option:selected").val(),
            curPage: "1",
            searchOption: $("#search_option").val(),
            category: $("#category").val(),
            option1: option1,
            option2: option2,
            option3: option3,
            startday: startday,
            endday: endday
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxFaqList",
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
        var option1 = false;
        var option2 = false;
        var option3 = false;

        if ($("#inlineCheckbox1-1").is(":checked")) {
            option1 = true;
        }
        if ($("#inlineCheckbox1-2").is(":checked")) {
            option2 = true;
        }
        if ($("#inlineCheckbox1-3").is(":checked")) {
            option3 = true;
        }

        var param = {
            keyword: $("#keyword").val(),
            searchOrderBy: "num",
            pageScale: $("#page_scale option:selected").val(),
            curPage: page,
            searchOption: $("#search_option").val(),
            category: $("#category").val(),
            option1: option1,
            option2: option2,
            option3: option3,
            startday: startday,
            endday: endday
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxFaqList",
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
    function faqUpdate(no) {
        location.href = "/sys/faqUpWr?no=" + no;
    }

    //글쓰기 버튼 클릭
    function faqWrite() {
        location.href = "/sys/faqUpWr";
    }

    //삭제 버튼 클릭
    function faqDelete(no) {
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
                    url: "/sys/ajaxDelete",
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
                            $("#" + no).hide();
                            $("#totcount").text($("#totcount").text() - 1);
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
                str += '<td>' + v.day + '</td>';
                str += '<td class="title">' + v.title + '</td>';
                str += '<td>';
                if (v.bbssubstr == 7) {
                    str += "납부자";
                } else if (v.bbssubstr == 5) {
                    str += "로그인전";
                } else if (v.bbssubstr == 6) {
                    str += "기관담당자";
                }
                str += '</td>';
                str += '<td>';
                if (v.bbs % 10 == 1) {
                    str += "신청/해지";
                } else if (v.bbs % 10 == 2) {
                    str += "납부";
                } else if (v.bbs % 10 == 3) {
                    str += "사이트이용";
                } else if (v.bbs % 10 == 4) {
                    str += "고지";
                } else if (v.bbs % 10 == 5) {
                    str += "수납";
                } else if (v.bbs % 10 == 6) {
                    str += "부가서비스";
                } else if (v.bbs % 10 == 7) {
                    str += "현금영수증";
                }
                str += '</td>';
                str += '<td>' + v.writer + '</td>';
                str += '<td><button class="btn btn-xs btn-warning btn-edit-notice" onclick="faqUpdate(' + v.no + ')">수정</button><button class="btn btn-xs btn-danger btn-delete" onclick="faqDelete(' + v.no + ')">삭제</button></td>';
            });
        }
        $("#" + obj).html(str);
        $("#totcount").text(result.count);
        sysajaxPaging(result, 'PageArea');
    }

    function allClick() {
        if ($("#inlineCheckbox1-0").is(":checked")) {
            $("#inlineCheckbox1-1").prop("checked", true);
            $("#inlineCheckbox1-2").prop("checked", true);
            $("#inlineCheckbox1-3").prop("checked", true);
        } else {
            $("#inlineCheckbox1-1").prop("checked", false);
            $("#inlineCheckbox1-2").prop("checked", false);
            $("#inlineCheckbox1-3").prop("checked", false);
        }
    }

    function notAll() {
        $("#inlineCheckbox1-0").prop("checked", false)
    }

    function fn_enterkey() {
        search();
    }

    //파일저장
    function fn_fileSave() {
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
                $('#exSearchOption').val($("#search_option option:selected").val());
                $('#exKeyword').val($("#keyword").val());
                $('#exStartday').val($('#startday').val());
                $('#exEndday').val($('#sendday').val());
                if ($("#inlineCheckbox1-0").is(":checked")) {
                    $('#exOption0').val("true");
                } else {
                    $('#exOption0').val("flase");
                }
                if ($("#inlineCheckbox1-1").is(":checked")) {
                    $('#exOption1').val("true");
                } else {
                    $('#exOption1').val("flase");
                }
                if ($("#inlineCheckbox1-2").is(":checked")) {
                    $('#exOption2').val("true");
                } else {
                    $('#exOption2').val("flase");
                }
                if ($("#inlineCheckbox1-3").is(":checked")) {
                    $('#exOption3').val("true");
                } else {
                    $('#exOption3').val("flase");
                }
                $("#exCount").val($("#totcount").text());
                $("#exCategory").val($("#category option:selected").val());

                // 다운로드
                $('#fileForm').submit();
            }
        });
    }
</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/excelSaveFaq">
    <input type="hidden" id="exSearchOption" name="exSearchOption"/>
    <input type="hidden" id="exKeyword" name="exKeyword"/>
    <input type="hidden" id="exStartday" name="exStartday"/>
    <input type="hidden" id="exEndday" name="exEndday"/>
    <input type="hidden" id="exOption0" name="exOption0"/>
    <input type="hidden" id="exOption1" name="exOption1"/>
    <input type="hidden" id="exOption2" name="exOption2"/>
    <input type="hidden" id="exOption3" name="exOption3"/>
    <input type="hidden" id="exCount" name="exCount"/>
    <input type="hidden" id="exCategory" name="exCategory"/>
</form>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>자주하는질문</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>자주하는질문</strong>
            </li>
        </ol>
        <p class="page-description">등록된 자주하는질문을 조회하고 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2 text-right m-t-xl">
        <button class="btn btn-lg btn-w-m btn-primary" onclick="faqWrite()">글쓰기</button>
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

                            <div class="col-md-6">
                                <label class="form-label block">카테고리</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group ">
                                        <span class="input-group-select">
                                            <select class="form-control" id="category">
                                                <option value="all">전체</option>
                                                <option value="req">신청/해지</option>
                                                <option value="pay">납부</option>
                                                <option value="site">사이트이용</option>
                                                <option value="noti">고지</option>
                                                <option value="receive">수납</option>
                                                <option value="additional">부가서비스</option>
                                                <option value="cash">현금영수증</option>
                                            </select>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <label class="form-label block">검색구분</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group col-md-12">
                                        <span class="input-group-select">
                                            <select class="form-control" id="search_option">
                                                <option value="title">제목</option>
                                                <option value="writer">작성자</option>
                                                <option value="contents">내용</option>
                                            </select>
                                        </span>
                                        <input type="text" class="form-control" id="keyword" maxlength="25" onkeypress="if(event.keyCode==13) {fn_enterkey();}">
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label block">읽기권한</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group col-md-12">
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-0" onclick="allClick()" checked>
                                            <label for="inlineCheckbox1-0"> 전체 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-1" onclick="notAll()" checked>
                                            <label for="inlineCheckbox1-1"> 로그인전 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-2" onclick="notAll()" checked>
                                            <label for="inlineCheckbox1-2"> 기관담당자 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-3" onclick="notAll()" checked>
                                            <label for="inlineCheckbox1-3"> 납부자 </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <hr>

                        <div class="text-center">
                            <button class="btn btn-primary" type="button" onclick="search();">조회</button>
                        </div>
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
                            전체 건수 : <strong class="text-success" id="totcount">${map.count }</strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" id="page_scale" onchange="search()">
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
                            <table class="table table-stripped table-align-center has-ellipsis">
                                <colgroup>
                                    <col width="50">
                                    <col width="160">
                                    <col width="*">
                                    <col width="150">
                                    <col width="200">
                                    <col width="180">
                                    <col width="100">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>등록일자</th>
                                        <th>제목</th>
                                        <th>읽기권한</th>
                                        <th>카테고리</th>
                                        <th>작성자</th>
                                        <th></th>
                                    </tr>
                                </thead>

                                <tbody id="reSearchbody">
                                    <c:choose>
                                        <c:when test="${map.list.size() > 0}">
                                            <c:forEach var="row" items="${map.list}">
                                                <tr id="${row.no }">
                                                    <td>${row.rn }</td>
                                                    <td>${row.day }</td>
                                                    <td class="title">${row.title }</td>
                                                    <td><c:if test="${row.bbssubstr == 7  }">납부자</c:if> <c:if
                                                            test="${row.bbssubstr == 5  }">로그인전</c:if> <c:if
                                                            test="${row.bbssubstr == 6  }">기관담당자</c:if></td>
                                                    <td>
                                                        <c:if test="${row.bbs % 10 == 1}">신청/해지</c:if>
                                                        <c:if test="${row.bbs % 10 == 2}">납부</c:if>
                                                        <c:if test="${row.bbs % 10 == 3}">사이트 이용</c:if>
                                                        <c:if test="${row.bbs % 10 == 4}">고지</c:if>
                                                        <c:if test="${row.bbs % 10 == 5}">수납</c:if>
                                                        <c:if test="${row.bbs % 10 == 6}">부가서비스</c:if>
                                                        <c:if test="${row.bbs % 10 == 7}">현금영수증</c:if>
                                                    </td>
                                                    <td>${row.writer }</td>
                                                    <td>
                                                        <button class="btn btn-xs btn-warning btn-edit-notice"
                                                                onclick="faqUpdate(${row.no })">수정
                                                        </button>
                                                        <button class="btn btn-xs btn-danger btn-delete"
                                                                onclick="faqDelete(${row.no })">삭제
                                                        </button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td>
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
