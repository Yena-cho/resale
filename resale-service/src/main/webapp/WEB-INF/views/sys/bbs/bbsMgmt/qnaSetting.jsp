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
    var twoDepth = "adm-sub-08";
</script>

<script>
    $(document).ready(function () {
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

        if ($("#inlineCheckbox1-0").is(":checked")) {
            option0 = true;
        }
        if ($("#inlineCheckbox1-1").is(":checked")) {
            option1 = true;
        }
        if ($("#inlineCheckbox1-2").is(":checked")) {
            option2 = true;
        }
        var param = {
            keyword: $("#keyword").val(),
            searchOrderBy: "num",
            pageScale: $("#page_scale option:selected").val(),
            curPage: "1",
            searchOption: $("#search_option").val(),
            category: $("#category").val(),
            option0: option0,
            option1: option1,
            option2: option2,
            startday: startday,
            endday: endday,
            code: $("#chacd").val(),
            chaname: $("#chaname").val()
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxQnaSetting",
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
    function old_list(page) {
        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");
        var option0 = false;
        var option1 = false;
        var option2 = false;

        if ($("#inlineCheckbox1-0").is(":checked")) {
            option0 = true;
        }
        if ($("#inlineCheckbox1-1").is(":checked")) {
            option1 = true;
        }
        if ($("#inlineCheckbox1-2").is(":checked")) {
            option2 = true;
        }

        var param = {
            keyword: $("#keyword").val(),
            searchOrderBy: "num",
            pageScale: $("#page_scale option:selected").val(),
            curPage: page,
            searchOption: $("#search_option").val(),
            category: $("#category").val(),
            option0: option0,
            option1: option1,
            option2: option2,
            startday: startday,
            endday: endday,
            code: $("#chacd").val(),
            chaname: $("#chaname").val()
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxQnaSetting",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    $("#totcount").text(result.count);
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
    function qnaUpdate(no) {
        location.href = "/sys/qnaUpWr?no=" + no;
    }

    function fnGrid(result, obj) {
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr id=' + v.no + '>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.code + '</td>';
                str += '<td>'
                if (v.data7 != null && v.data7 != '') {
                    str += v.data7
                }
                str += '</td>';
                str += '<td class="title">' + basicEscape(v.title) + '</td>';
                str += '<td>' + v.writer + '</td>';
                str += '<td>'
                if (v.data1 != null && v.data1 != '') {
                    str += v.data1
                }
                str += '</td>';
                str += '<td>' + v.day + '</td>';
                str += '<td>';
                if (v.datcnt == 0) {
                    str += "답변대기";
                }
                if (v.datcnt >= 1) {
                    str += "답변완료";
                }
                str += '</td>';
                str += '<td>';
                if (v.datcnt == 0) {
                    str += '<button class="btn btn-xs btn-danger btn-delete"  onclick="qnaUpdate(' + v.no + ')">답변</button>';
                }
                if (v.datcnt >= 1) {
                    str += '<button class="btn btn-xs btn-warning btn-edit-notice" onclick="qnaUpdate(' + v.no + ')">수정</button>';
                }
                str += '</td>';
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
        } else {
            $("#inlineCheckbox1-1").prop("checked", false);
            $("#inlineCheckbox1-2").prop("checked", false);
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
        if ($('#totcount').text() <= 0) {
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
            var startday = $("#startday").val().replace(/\./gi, "");
            var endday = $("#endday").val().replace(/\./gi, "");
            if (result.value) {
                $('#exChacd').val($("#chacd").val());
                $('#exChaname').val($("#chaname").val());
                $('#exCategory').val($("#category option:selected").val());
                $('#exStartday').val(startday);
                $('#exEndday').val(endday);
                $('#exSearchOption').val($("#search_option option:selected").val());
                $('#exKeyword').val($("#keyword").val());
                if ($("#inlineCheckbox1-1").is(":checked")) {
                    $('#exOption1').val("true");
                } else {
                    $('#exOption1').val("false");
                }
                if ($("#inlineCheckbox1-2").is(":checked")) {
                    $('#exOption2').val("true");
                } else {
                    $('#exOption2').val("false");
                }
                if ($("#inlineCheckbox1-3").is(":checked")) {
                    $('#exOption3').val("true");
                } else {
                    $('#exOption3').val("false");
                }
                $("#exCount").val($("#totcount").text());

                // 다운로드
                $('#fileForm').submit();
            }
        });
    }

    function findCha() {
        $("#lookup-collecter-popup").modal({
            backdrop: 'static',
            keyboard: false
        });
        $("#popChacd").val('');
        $("#popChaname").val('');
        $('#totCntLookupCollector').text(0);
        $("#ResultBodyCollector").html('');
        $("#ModalPageAreaCollector").html('');
    }

    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색
        } else {
            old_list(page);
        }
    }
</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/excelSaveQna">
    <input type="hidden" id="exChacd" name="exChacd"/>
    <input type="hidden" id="exChaname" name="exChaname"/>
    <input type="hidden" id="exCategory" name="exCategory"/>
    <input type="hidden" id="exStartday" name="exStartday"/>
    <input type="hidden" id="exEndday" name="exEndday"/>
    <input type="hidden" id="exSearchOption" name="exSearchOption"/>
    <input type="hidden" id="exKeyword" name="exKeyword"/>
    <input type="hidden" id="exOption1" name="exOption1"/>
    <input type="hidden" id="exOption2" name="exOption2"/>
    <input type="hidden" id="exOption3" name="exOption3"/>
    <input type="hidden" id="exCount" name="exCount"/>
</form>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>서비스문의</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>서비스문의</strong>
            </li>
        </ol>
        <p class="page-description">등록된 서비스문의를 수정하고 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2"></div>
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
                                <label class="form-label block">기관코드</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chacd" maxlength="50" onkeypress="if(event.keyCode==13) {fn_enterkey();}">
                                        <span class="input-group-btn">
                                            <button class="btn btn-primary btn-lookup-collecter no-margins" type="button" onclick="findCha()">기관검색</button>
                                        </span>
                                    </div>

                                    <div class="col-sm-4" style="display: none;">
                                        <div class="input-group">
                                            <input type="text" class="form-control" id="chaname" maxlength="25" onkeypress="if(event.keyCode==13) {fn_enterkey();}">
                                        </div>
                                    </div><!-- 삭제후 검색 버튼으로 변경 요청 20180619 안진호 -->
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label block">카테고리</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group ">
                                        <span class="input-group-select">
                                            <select class="form-control" id="category">
                                                <option value="all">전체</option>
                                                <option value="고객관리">고객관리</option>
                                                <option value="청구">청구</option>
                                                <option value="고지">고지</option>
                                                <option value="수납">수납</option>
                                                <option value="설정">설정</option>
                                                <option value="사이트이용">사이트이용</option>
                                                <option value="신청/해지">신청/해지</option>
                                                <option value="기타">기타</option>
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
                                <label class="form-label block">답변상태</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group col-md-12">
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-0" onclick="allClick()">
                                            <label for="inlineCheckbox1-0"> 전체 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-1" onclick="notAll()"
                                                   checked="checked">
                                            <label for="inlineCheckbox1-1"> 답변대기 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-2" onclick="notAll()">
                                            <label for="inlineCheckbox1-2"> 답변완료 </label>
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

    <div class="animated fadeInRight" id="focus">
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
                            <button class="btn btn-md btn-primary" onclick="fn_fileSave()">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center has-ellipsis">
                                <colgroup>
                                    <col width="50">
                                    <col width="140">
                                    <col width="150">
                                    <col width="*">
                                    <col width="200">
                                    <col width="170">
                                    <col width="150">
                                    <col width="130">
                                    <col width="70">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>기관코드</th>
                                        <th>카테고리</th>
                                        <th>제목</th>
                                        <th>작성자</th>
                                        <th>담당자핸드폰번호</th>
                                        <th>등록일</th>
                                        <th>답변상태</th>
                                        <th></th>
                                    </tr>
                                </thead>

                                <tbody id="reSearchbody">
                                    <c:choose>
                                        <c:when test="${map.list.size() > 0}">
                                            <c:forEach var="row" items="${map.list}">
                                                <tr id="${row.no }">
                                                    <td>${row.rn }</td>
                                                    <td>${row.code }</td>
                                                    <td>${row.data7 }</td>
                                                    <td class="title">${row.title }</td>
                                                    <td>${row.writer }</td>
                                                    <td>${row.data1 }</td>
                                                    <td>${row.day }</td>
                                                    <td>
                                                        <c:if test="${row.datcnt == 0 }">답변대기</c:if>
                                                        <c:if test="${row.datcnt > 0 }">답변완료</c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${row.datcnt == 0 }">
                                                            <button class="btn btn-xs btn-danger btn-delete"
                                                                    onclick="qnaUpdate(${row.no })">답변
                                                            </button>
                                                        </c:if>
                                                        <c:if test="${row.datcnt > 0 }">
                                                            <button class="btn btn-xs btn-warning btn-edit-notice"
                                                                    onclick="qnaUpdate(${row.no })">수정
                                                            </button>
                                                        </c:if>
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

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<script>
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
</script>
