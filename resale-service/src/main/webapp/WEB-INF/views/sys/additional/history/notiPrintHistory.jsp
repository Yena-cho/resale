<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-6";
    var twoDepth = "adm-sub-19";
</script>

<script>
    var getCurPage = 1;

    $(document).ready(function () {
        setMonthTerm(1);

        $("#reqstAll").click(function () {
            if ($("#reqstAll").prop("checked")) {
                $("input[name=reqst]").prop("checked", true);
            } else {
                $("input[name=reqst]").prop("checked", false);
            }
        });

        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            forceParse: false,
            autoclose: true
        });
    });

    //페이지 변경
    function list(page) {
        $('#pageNo').val(page);
        search(page);
    }

    function search(page) {
        if (page == null) {
            getCurPage = 1;
        } else {
            getCurPage = page;
        }

        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");

        if ((!startday || !endday)) {
            if ((startday > endday)) {
                swal({
                    type: 'info',
                    text: '등록일자를 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        var reqstList = [];
        var reqstBox = $("input[name='reqst']:checked");
        reqstBox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                reqstList.push($(this).val());
            }
        });

        var param = {
            keyword: $("#keyword").val(),
            pagescale: $("#page_scale option:selected").val(),
            curpage: getCurPage,
            searchoption: $("#search_option option:selected").val(),
            startday: startday,
            endday: endday,
            chacd: $("#chacd").val(),
            chaname: $("#chaname").val(),
            reqstList: reqstList,
            dlvrTypeCd: $("input[name='dlvrTypeCd']:checked").val(),
            orderOption: $("#order_option option:selected").val()
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/addServiceMgmt/ajaxNotiPrintHistory",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    sysajaxPaging(result, 'PageArea');
                    var scmove = $('#focus').offset().top;
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

    function notAll() {
        if (!$('#reqst1').is(':checked') || !$('#reqst2').is(':checked')|| !$('#reqst3').is(':checked')|| !$('#reqst4').is(':checked')|| !$('#reqst5').is(':checked')) {
            $('#reqstAll').prop('checked', false);
        } else {
            $('#reqstAll').prop('checked', true);
        }
    }

    function fnGrid(result, obj) {
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="11" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                if (v.senddt == null) {
                    v.senddt = "";
                }
                str += '<tr id=' + v.notimasreqcd + '>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.regdt + '</td>';
                str += '<td>' + v.dlvrTypeCd + '</td>';
                str += '<td>' + v.chacd + '</td>';
                str += '<td>' + v.chaname + '</td>';
                str += '<td>' + v.reqname + '</td>';
                str += '<td>' + v.reqhp + '</td>';
                str += '<td>' + v.sendcnt + '</td>';
                str += '<td>' + v.senddt + '</td>';
                str += '<td>' + (v.reqst == '취소' ? v.maker + " " : "") + v.reqst + '</td>';
                str += '<td>';
                str += '<div class="btn-group">';
                if (v.reqst == '요청') {
                    str += '<button type="button" class="btn btn-xs btn-info m-r-sm" onclick="printCancle(\'' + v.notimasreqcd + '\');">취소</button>';
                    str += '<button type="button" class="btn btn-xs btn-info m-r-sm" onclick="quickPrint(\'' + v.notimasreqcd + '\');">출력긴급요청</button>';
                }
                if (v.reqst == '처리중') {
                    str += '<button type="button" class="btn btn-xs btn-info m-r-sm" onclick="printCancle(\'' + v.notimasreqcd + '\');">취소</button>';
                }
                if (v.reqst == '처리완료') {
                    str += '<button type="button" class="btn btn-xs btn-info m-r-sm" onclick="printCancle(\'' + v.notimasreqcd + '\');">취소</button>';
                    if (v.resStsCd == 'N11000' || v.resStsCd == 'N12000') {
                        str += '<button type="button" class="btn btn-xs btn-default m-r-sm">재전송</button>';
                    } else {
                        str += '<button type="button" class="btn btn-xs btn-info m-r-sm" onclick="rePrint(\'' + v.notimasreqcd + '\');">재전송</button>';
                    }
                }
                if (v.reqst == '실패') {
                    str += '<button type="button" class="btn btn-xs btn-info m-r-sm" onclick="printCancle(\'' + v.notimasreqcd + '\');">취소</button>';
                    if (v.resStsCd == 'N11000' || v.resStsCd == 'N12000') {
                        str += '<button type="button" class="btn btn-xs btn-default m-r-sm">재전송</button>';
                    } else {
                        str += '<button type="button" class="btn btn-xs btn-info m-r-sm" onclick="rePrint(\'' + v.notimasreqcd + '\');">재전송</button>';
                    }
                }
                str += '</div>';
                str += '</td>';
                str += '</tr>';
            });
        }
        $("#" + obj).html(str);
        $("#printCount").text(result.printCount);
        sysajaxPaging(result, 'PageArea');
    }

    function printCancle(no) {
        swal({
            type: 'question',
            html: "해당 고지서 출력의뢰건을 취소하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var param = {
                    notimasreqcd: no
                };

                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/sys/addServiceMgmt/notiPrintUpdate",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '고지서 출력의뢰건을 취소하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    search();
                                }
                            });
                        } else {
                            swal({
                                type: 'success',
                                text: '시스템 오류입니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                });
            }
        });
    }

    function quickPrint(notimasreqcd) {
        var sTime = 100000;
        var eTime = 110000;
        var nowTime = moment(new Date()).format("HHMMSS");

        if (sTime <= parseInt(nowTime) && parseInt(nowTime) <= eTime) {
            swal({
                type: 'question',
                html: "해당 고지서 출력의뢰건을 긴급요청하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    var param = {
                        notimasreqcd: notimasreqcd
                    };

                    $.ajax({
                        type: "POST",
                        async: true,
                        url: "/sys/addServiceMgmt/quickPrint",
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify(param),
                        success: function (result) {
                            if (result.retCode == "0000") {
                                swal({
                                    type: 'success',
                                    text: '고지서 출력의뢰건을 긴급요청하였습니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                }).then(function (result) {
                                    if (result.value) {
                                        search();
                                    }
                                });
                            } else {
                                swal({
                                    type: 'success',
                                    text: '시스템 오류입니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                });
                            }
                        }
                    });
                }
            });
        } else {
            swal({
                type: 'info',
                html: '긴급출력은 오전10:00 ~ 오전11:00 사이에만 가능합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });

            return;
        }
    }

    function rePrint(notimasreqcd) {
        swal({
            type: 'question',
            html: "해당 고지서 출력의뢰건을 재전송하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var param = {
                    notimasreqcd: notimasreqcd
                };

                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/sys/addServiceMgmt/rePrint",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '고지서 출력의뢰건을 재전송하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    search();
                                }
                            });
                        } else {
                            swal({
                                type: 'success',
                                text: '시스템 오류입니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                });
            }
        });
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
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                $('#exSearchOption').val($("#search_option option:selected").val());
                $('#exKeyword').val($("#keyword").val());
                $('#exStartday').val($('#startday').val().replace(/\./gi, ""));
                $('#exEndday').val($('#endday').val().replace(/\./gi, ""));
                var reqstList = [];
                var reqstBox = $("input[name='reqst']:checked");
                reqstBox.map(function (i) {
                    if ($(this).val() != '' && $(this).val() != null) {
                        reqstList.push($(this).val());
                    }
                });
                $('#exReqstList').val(reqstList);
                $("#exCount").val($("#printCount").text());
                $("#exChacd").val($("#chacd").val());
                $("#exChaName").val($("#chaname").val());
                $("#exOrderOption").val($("#order_option option:selected").val());
                $("#exDlvrTypeCd").val($("input[name='dlvrTypeCd']:checked").val());
                // 다운로드
                $('#fileForm').submit();
            }
        });
    }
</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/addServiceMgmt/excelSaveNotiPrint">
    <input type="hidden" id="exSearchOption" name="exSearchOption"/>
    <input type="hidden" id="exKeyword" name="exKeyword"/>
    <input type="hidden" id="exOrderOption" name="exOrderOption"/>
    <input type="hidden" id="exChacd" name="exChacd"/>
    <input type="hidden" id="exChaName" name="exChaName"/>
    <input type="hidden" id="exStartday" name="exStartday"/>
    <input type="hidden" id="exEndday" name="exEndday"/>
    <input type="hidden" id="exCount" name="exCount"/>
    <input type="hidden" id="exDlvrTypeCd" name="exDlvrTypeCd"/>
    <input type="hidden" id="exReqstList" name="exReqstList"/>
</form>

<input type="hidden" id="curPage" name="curPage"/>
<input type="hidden" id="pageNo" name="pageNo" value="1">
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>고지출력의뢰이용내역</h2>
        <ol class="breadcrumb">
            <li><a href="/sys/index">대시보드</a></li>
            <li><a>부가서비스관리</a></li>
            <li class="active"><strong>고지출력의뢰이용내역</strong></li>
        </ol>
        <p class="page-description">이용기관별 고지서 출력의뢰 현황을 관리하는 화면입니다.</p>
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
                                    <label class="form-label block">의뢰일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" maxlength="8" onkeyup="onlyNumber(this)" class="input-sm form-control" name="startday" id="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" maxlength="8" onkeyup="onlyNumber(this)" class="input-sm form-control" name="endday" id="endday" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth" id="btnSetMonth0" onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth1" onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth6" onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-inline">
                                                <input type="radio" id="dlvrTypeCdAll" name="dlvrTypeCd" value="all" checked>
                                                <label for="dlvrTypeCdAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="dlvrTypeCd1" name="dlvrTypeCd" value="N20002">
                                                <label for="dlvrTypeCd1"> 퀵 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="dlvrTypeCd2" name="dlvrTypeCd" value="N20001">
                                                <label for="dlvrTypeCd2"> 택배 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chacd" maxlength="50">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaname" id="chaname" maxlength="50">
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
													<option value="reqname">수취인명</option>
													<option value="reqhp">수취인번호</option>
												</select>
											</span>
                                            <input type="text" class="form-control" id="keyword" maxlength="25" onkeypress="if(event.keyCode==13) {search();}">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="reqstAll" name="reqst" value="all" onclick="allClick()" checked>
                                                <label for="reqstAll"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="reqst1" name="reqst" value="BR01" onclick="notAll()" checked>
                                                <label for="reqst1"> 요청 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="reqst2" name="reqst" value="BR03" onclick="notAll()" checked>
                                                <label for="reqst2"> 처리중 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="reqst3" name="reqst" value="BR04" onclick="notAll()" checked>
                                                <label for="reqst3"> 취소 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="reqst4" name="reqst" value="BR02" onclick="notAll()" checked>
                                                <label for="reqst4"> 처리완료 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="reqst5" name="reqst" value="BR09" onclick="notAll()" checked>
                                                <label for="reqst5"> 실패 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="search()">조회</button>
                            </div>
                        </form>
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
                            <span class="m-l-sm">전체의뢰건수 : <strong class="text-success" id="printCount">${map.printCount }</strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" id="order_option" onchange="search()">
                                <option value="regdt">의뢰일 순</option>
                                <option value="senddt">신청일 순</option>
                                <option value="reqst">상태 순</option>
                            </select>
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
                            <table class="table table-stripped table-align-center">
                                <colgroup>
                                    <col width="50">
                                    <col width="150">
                                    <col width="103">
                                    <col width="100">
                                    <col width="270">
                                    <col width="200">
                                    <col width="150">
                                    <col width="100">
                                    <col width="150">
                                    <col width="100">
                                    <col width="200">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>의뢰일시</th>
                                        <th>구분</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>수취인명</th>
                                        <th>수취인전화번호</th>
                                        <th>출력건수</th>
                                        <th>처리일시</th>
                                        <th>상태</th>
                                        <th>ACTION</th>
                                    </tr>
                                </thead>

                                <tbody id="reSearchbody">
                                    <c:forEach var="row" items="${map.list }">
                                        <tr id="${row.notimasreqcd }">
                                            <td>${row.rn }</td>
                                            <td>${row.regdt }</td>
                                            <td>${row.dlvrTypeCd }</td>
                                            <td>${row.chacd }</td>
                                            <td>${row.chaname }</td>
                                            <td>${row.reqname }</td>
                                            <td>${row.reqhp }</td>
                                            <td>${row.sendcnt }</td>
                                            <td>${row.senddt }</td>
                                            <td><c:if test="${row.reqst == '취소' }">${row.maker } </c:if>${row.reqst}</td>
                                            <td>
                                                <div class="btn-group">
                                                    <c:if test="${row.reqst == '요청' }">
                                                        <button type="button" class="btn btn-xs btn-info m-r-sm" onclick="printCancle('${row.notimasreqcd }')">취소</button>
                                                        <button type="button" class="btn btn-xs btn-info" onclick="quickPrint('${row.notimasreqcd }')">출력긴급요청</button>
                                                    </c:if>
                                                    <c:if test="${row.reqst == '처리중' }">
                                                        <button type="button" class="btn btn-xs btn-info" onclick="printCancle('${row.notimasreqcd }')">취소</button>
                                                    </c:if>
                                                    <c:if test="${row.reqst == '처리완료' }">
                                                        <button type="button" class="btn btn-xs btn-info m-r-sm" onclick="printCancle('${row.notimasreqcd }')">취소</button>
                                                        <c:choose>
                                                            <c:when test="${row.resStsCd == 'N11000' || row.resStsCd == 'N12000'}">
                                                                <button type="button" class="btn btn-xs btn-default">재전송</button>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <button type="button" class="btn btn-xs btn-info" onclick="rePrint('${row.notimasreqcd }')">재전송</button>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                    <c:if test="${row.reqst == '실패' }">
                                                        <button type="button" class="btn btn-xs btn-info m-r-sm" onclick="printCancle('${row.notimasreqcd }')">취소</button>
                                                        <c:choose>
                                                            <c:when test="${row.resStsCd == 'N11000' || row.resStsCd == 'N12000'}">
                                                                <button type="button" class="btn btn-xs btn-default">재전송</button>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <button type="button" class="btn btn-xs btn-info" onclick="rePrint('${row.notimasreqcd }')">재전송</button>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
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
