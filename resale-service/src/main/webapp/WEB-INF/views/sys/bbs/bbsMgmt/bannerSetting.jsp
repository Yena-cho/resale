<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<style>
    .name, .title, .content {
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
    }
</style>

<script>
    var oneDepth = "adm-nav-4";
    var twoDepth = "adm-sub-09-2";
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

        setMonthTerm(0);

        search();
    })

    // 조회
    function search() {
        var startday = $("#startday").val();
        var endday = $("#endday").val();
        var startDate = "";
        var endDate = "";
        var nowDate = moment(new Date, 'YYYYMMDD').format('YYYYMMDD');

        if (startday == "" && endday != '') {
            swal({
                type: 'info',
                text: '시작일을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (startday != "" && endday == '') {
            swal({
                type: 'info',
                text: '마감일을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (startday != "" && endday != '') {
            startDate = moment(startday, 'YYYYMMDD').format('YYYYMMDD');
            endDate = moment(endday, 'YYYYMMDD').format('YYYYMMDD');

            if (startDate > endDate && endDate != '' && endDate != null) {
                swal({
                    type: 'info',
                    text: '시작일이 마감일보다 큽니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (startDate > nowDate) {
                swal({
                    type: 'info',
                    text: '시작일이 현재일보다 클 수 없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (endDate > nowDate) {
                swal({
                    type: 'info',
                    text: '마감일이 현재일보다 클 수 없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        var param = {
            startDate: startDate,
            endDate: endDate,
            searchOption: $("#search_option option:selected").val(),
            keyword: $("#keyword").val(),
            showYn: $('input[name=show]:checked').val(),
            viewType : $('input[name=viewType]:checked').val(),
            searchOrderBy: $("#searchOrderBy option:selected").val(),
            pageScale: $("#page_scale option:selected").val(),
            curPage: "1"
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxBannerSetting",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, "reSearchbody");
                    $("#totCount").text(result.count);
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

    function fnGrid(result, obj) {
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="8" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                var title;
                var content;

                if (v.title == null || v.title == "") {
                    title = "";
                } else {
                    title = v.title.replace(/<br>|<br\/>|<br \/>/g, "\r\n");
                }
                if (v.content == null || v.content == "") {
                    content = "";
                } else {
                    content = v.content.replace(/<br>|<br\/>|<br \/>/g, "\r\n");
                }

                str += '<tr id=' + v.id + '>';
                str += '<td>' + v.rn + '</td>';
                str += '<td class="createDate">' + moment(v.createDate, 'YYYY-MM-DD').format('YYYY.MM.DD') + '</td>';
                if (v.viewTypeCd == 'S10001') {
                    str += '<td class="viewTypeCd">PC</td>';
                } else if (v.viewTypeCd == 'S10002') {
                    str += '<td class="viewTypeCd">모바일</td>';
                }
                str += '<td class="name">' + v.name + '</td>';
                str += '<td class="title">' + title + '</td>';
                str += '<td class="content">' + content + '</td>';
                if (v.showYn == 'y' || v.showYn == 'Y') {
                    str += '<td class="text-primary">노출</td>';
                    str += '<td>' + v.orderNo + '</td>';
                } else if (v.showYn == 'n' || v.showYn == 'N') {
                    str += '<td class="text-danger">미노출</td>';
                    str += '<td>-</td>';
                }
                str += '<td>';
                str += '<button class="btn btn-xs btn-primary" onclick="bannerPreview(\'' + v.fileId + '\')">미리보기</button>';
                str += '<button class="btn btn-xs btn-warning" onclick="bannerUpdate(' + v.id + ')">수정</button>';
                str += '<button class="btn btn-xs btn-danger"  onclick="bannerDelete(' + v.id + ')">삭제</button>';
                str += '</td>';
                str == '</tr>';

            });
        }
        $("#" + obj).html(str);
        $("#totCount").text(result.count);
        sysajaxPaging(result, 'PageArea');
    }

    function list(page) {
        var startday = $("#startday").val();
        var endday = $("#endday").val();
        var startDate = "";
        var endDate = "";
        var nowDate = moment(new Date, 'YYYYMMDD').format('YYYYMMDD');

        if (startday == "" && endday != '') {
            swal({
                type: 'info',
                text: '시작일을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (startday != "" && endday == '') {
            swal({
                type: 'info',
                text: '마감일을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (startday != "" && endday != '') {
            startDate = moment(startday, 'YYYYMMDD').format('YYYYMMDD');
            endDate = moment(endday, 'YYYYMMDD').format('YYYYMMDD');

            if (startDate > endDate && endDate != '' && endDate != null) {
                swal({
                    type: 'info',
                    text: '시작일이 마감일보다 큽니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (startDate > nowDate) {
                swal({
                    type: 'info',
                    text: '시작일이 현재일보다 클 수 없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (endDate > nowDate) {
                swal({
                    type: 'info',
                    text: '마감일이 현재일보다 클 수 없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        var param = {
            startDate: startDate,
            endDate: endDate,
            searchOption: $("#search_option option:selected").val(),
            keyword: $("#keyword").val(),
            showYn: $('input[name=show]:checked').val(),
            viewType : $('input[name=viewType]:checked').val(),
            searchOrderBy: $("#searchOrderBy option:selected").val(),
            pageScale: $("#page_scale option:selected").val(),
            curPage: page
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxBannerSetting",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, "reSearchbody");
                    $("#totCount").text(result.count);
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

    // 배너 등록
    function bannerRegist() {
        location.href = "/sys/bannerWrite";
    }

    // 배너 수정
    function bannerUpdate(id) {
        location.href = "/sys/bannerUpdate?id=" + id;
    }

    // 배너 삭제
    function bannerDelete(id) {
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
                    id: id
                };
                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/sys/doBannerDelete",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '삭제되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });

                            list(1);
                        } else if (result.retCode == "0001") {
                            swal({
                                type: 'error',
                                text: '배너는 하나이상 존재해야 합니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            })
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

    // 배너 미리보기
    function bannerPreview(fileId) {
        getBannerPreview(fileId);

        $('#banner-preview').modal({backdrop: 'static', keyboard: false});
    }
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>배너관리</h2>
        <ol class="breadcrumb">
            <li><a href="/sys/index">대시보드</a></li>
            <li><a>게시판관리</a></li>
            <li class="active"><strong>배너관리</strong></li>
        </ol>
        <p class="page-description">메인 페이지 배너를 등록 및 수정하는 화면입니다.</p>
    </div>

    <div class="col-lg-2 text-right m-t-xl">
        <button class="btn btn-lg btn-w-m btn-primary" onclick="bannerRegist();">등록</button>
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
                                <label class="form-label block">구분</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group col-md-12">
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox1-0" name="viewType" value="" checked>
                                            <label for="inlineCheckbox1-0"> 전체 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox1-1" name="viewType" value="S10001">
                                            <label for="inlineCheckbox1-1"> PC </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox1-2" name="viewType"  value="S10002">
                                            <label for="inlineCheckbox1-2"> 모바일 </label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label block">노출여부</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group col-md-12">
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-0" name="show" value="" checked>
                                            <label for="inlineCheckbox2-0"> 전체 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-1" name="show" value="Y">
                                            <label for="inlineCheckbox2-1"> 노출 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-2" name="show"  value="N">
                                            <label for="inlineCheckbox2-2"> 미노출 </label>
                                        </div>
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
                                                <option value="name">배너명</option>
                                                <option value="content">내용</option>
                                            </select>
                                        </span>
                                        <input type="text" class="form-control" id="keyword" maxlength="25" onkeypress="if(event.keyCode == 13){search();}">
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6"></div>
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
                        <div class="col-lg-6">전체 건수 : <strong class="text-success" id="totCount">${map.count }</strong> 건</div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" id="searchOrderBy" onchange="search()">
                                <option value="createDate">등록일자순</option>
                                <option value="name">배너명순</option>
                                <option value="orderNo">노출순서순</option>
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
                            <table class="table table-stripped table-align-center has-ellipsis" style="table-layout: fixed;">
                                <colgroup>
                                    <col width="50">
                                    <col width="130">
                                    <col width="80">
                                    <col width="200">
                                    <col width="280">
                                    <col width="507">
                                    <col width="80">
                                    <col width="80">
                                    <col width="150">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>등록일</th>
                                        <th>구분</th>
                                        <th>배너명</th>
                                        <th>제목</th>
                                        <th>내용</th>
                                        <th>노출여부</th>
                                        <th>노출순서</th>
                                        <th></th>
                                    </tr>
                                </thead>

                                <tbody id="reSearchbody">
                                    <c:choose>
                                        <c:when test="${map.list.size() > 0}">
                                            <c:forEach var="row" items="${map.list}">
                                                <tr id="${row.id }">
                                                    <td>${row.rn }</td>
                                                    <td><fmt:parseDate pattern="yyyy-MM-dd" var="createDate" value="${row.createDate}"/><fmt:formatDate pattern="yyyy.MM.dd" value="${createDate}"/></td>
                                                    <c:if test="${row.viewTypeCd.equals('S10001')}">
                                                        <td class="viewTypeCd">PC</td>
                                                    </c:if>
                                                    <c:if test="${row.viewTypeCd.equals('S10002')}">
                                                        <td class="viewTypeCd">모바일</td>
                                                    </c:if>
                                                    <td class="name">${row.name }</td>
                                                    <td class="title">${row.title }</td>
                                                    <td class="content">${row.content}</td>
                                                    <c:if test="${row.showYn.equals('Y')}">
                                                        <td class="text-primary">노출</td>
                                                        <td>${row.orderNo}</td>
                                                    </c:if>
                                                    <c:if test="${row.showYn.equals('N')}">
                                                        <td class="text-danger">미노출</td>
                                                        <td>-</td>
                                                    </c:if>
                                                    <td>
                                                        <button class="btn btn-xs btn-primary" onclick="bannerPreview(${row.fileId })">미리보기</button>
                                                        <button class="btn btn-xs btn-warning" onclick="bannerUpdate(${row.id })">수정</button>
                                                        <button class="btn btn-xs btn-danger" onclick="bannerDelete(${row.id })">삭제</button>
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

<jsp:include page="/WEB-INF/views/include/modal/banner-preview.jsp" flush="false"/>
