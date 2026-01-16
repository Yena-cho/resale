<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-3";
    var twoDepth = "adm-sub-04";
</script>

<script>
    //조회 버튼 클릭
    function search(page) {
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");

//        if ((!startday || !endday) || (startday > endday)) {
//            swal({
//                type: 'info',
//                text: '상담신청일자를 확인해 주세요.',
//                confirmButtonColor: '#3085d6',
//                confirmButtonText: '확인'
//            });
//            return;
//        }

        var option1 = "0";
        var option2 = "1";

        //옵션값으로 신청구분 및 상태 확인
        if ($("#inlineCheckbox1-1").is(":checked")) {
            option1 = "0";
        } else if ($("#inlineCheckbox1-2").is(":checked") && $("#inlineCheckbox1-3").is(":checked") == false) {
            option1 = "1";
        } else if ($("#inlineCheckbox1-3").is(":checked") && $("#inlineCheckbox1-2").is(":checked") == false) {
            option1 = "2";
        } else {
            option1 = "0";
        }

        if ($("#inlineCheckbox2-1").is(":checked")) {
            option2 = "0";
        } else if ($("#inlineCheckbox2-2").is(":checked") && $("#inlineCheckbox2-3").is(":checked") == false && $("#inlineCheckbox2-4").is(":checked") == false) {
            option2 = "1";
        } else if ($("#inlineCheckbox2-3").is(":checked") && $("#inlineCheckbox2-2").is(":checked") == false && $("#inlineCheckbox2-4").is(":checked") == false) {
            option2 = "2";
        } else if ($("#inlineCheckbox2-4").is(":checked") && $("#inlineCheckbox2-2").is(":checked") == false && $("#inlineCheckbox2-3").is(":checked") == false) {
            option2 = "3";
        } else if ($("#inlineCheckbox2-2").is(":checked") && $("#inlineCheckbox2-3").is(":checked") && $("#inlineCheckbox2-4").is(":checked") == false) {
            option2 = "4";
        } else if ($("#inlineCheckbox2-2").is(":checked") && $("#inlineCheckbox2-4").is(":checked") && $("#inlineCheckbox2-3").is(":checked") == false) {
            option2 = "5";
        } else if ($("#inlineCheckbox2-3").is(":checked") && $("#inlineCheckbox2-4").is(":checked") && $("#inlineCheckbox2-2").is(":checked") == false) {
            option2 = "6";
        } else {
            option2 = "0";
        }
        var keyword = $("#keyword").val();
        var kw = '';
        if ($("#searchOption option:selected").val() == "data1") {
            if (keyword.length == 11) {
                kw = keyword.substring(0, 3) + "-" + keyword.substring(3, 7) + "-" + keyword.substring(7, 11);
                keyword = kw;
            }
        }

        var param = {
            keyword: keyword,
            pageScale: $("#pageScale").val(),
            curPage: cuPage,
            searchOrderBy: $("#searchOrderBy").val(),
            searchOption: $("#searchOption option:selected").val(),
            id: $('#chacd').val(),
            data4: option2,
            userClass: option1,
            startday: startday,
            endday: endday
        };

        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxCustList",
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

    //페이징 버튼
    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색
        } else {
            search(page);
        }
    }

    function fnGrid(result, obj) {
        var str = '';
        if (result.count <= 0) {
            $("#keyword").val("");
            str += '<tr><td colspan="11" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr id=' + v.no + '>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + moment(v.day, 'YYYYMMDDHHmmss').format('YYYY. M. D. HH:mm')+ '</td>';
                if (v.userClass == null) {
                    str += '<td>납부자/신규</td>';
                } else {
                    str += '<td>이용기관</td>';
                }
                str += '<td>' + nullValueChange(v.chaCd) + '</td>';
                str += '<td>' + nullValueChange(v.loginName) + '</td>';
                str += '<td>' + nullValueChange(v.writer) + '</td>';
                str += '<td>' + nullValueChange(v.data1) + '</td>';
                str += '<td>' + nullValueChange(v.data3) + '</td>';
                if (v.data4 == 1) {
                    str += "<td class=" + "text-warning" + ">대기</td>";
                } else if (v.data4 == 2) {
                    str += "<td class=" + "text-primary" + ">진행중</td>";
                } else {
                    str += "<td class=" + "text-success" + ">완료</td>";
                }
                str += '<td>' + ( v.day1 ? moment(v.day1, "YYYYMMDD").format('YYYY. M. D.') : '' ) + '</td>';
                str += '<td><button class="btn btn-xs btn-primary btn-open-counselling-dialog" value="' + v.no + '" name="see" onclick="fn_dialogOpen(\'' + v.no + '\');">수정</button>'
                str += '<button class="btn btn-xs btn-danger btn-delete" onclick="fn_delete(\'' + v.no + '\');">삭제</button></td>';
            });
        }
        $("#" + obj).html(str);
        $("#totcount").text(result.count);
        $("#wcount").text(result.wcount);
    }

    //신청구분, 상태 전체선택 및 해제
    function allClick() {
        if ($("#inlineCheckbox1-1").is(":checked")) {
            $("#inlineCheckbox1-2").prop("checked", true)
            $("#inlineCheckbox1-3").prop("checked", true)
        }
        else {
            $("#inlineCheckbox1-2").prop("checked", false)
            $("#inlineCheckbox1-3").prop("checked", false)
        }
    }

    function notAll() {
        $("#inlineCheckbox1-1").prop("checked", false)
    }

    function allClick2() {
        if ($("#inlineCheckbox2-1").is(":checked")) {
            $("#inlineCheckbox2-2").prop("checked", true)
            $("#inlineCheckbox2-3").prop("checked", true)
            $("#inlineCheckbox2-4").prop("checked", true)
        }
        else {
            $("#inlineCheckbox2-2").prop("checked", false)
            $("#inlineCheckbox2-3").prop("checked", false)
            $("#inlineCheckbox2-4").prop("checked", false)
        }
    }

    function notAll2() {
        $("#inlineCheckbox2-1").prop("checked", false)
    }

    function fn_add_dialogOpen() {
        $("#modal-sys-add-counselling-dialog select#mwho").val("1").prop("selected", true);
        $("#modal-sys-add-counselling-dialog select#mdata2").val("01").prop("selected", true);
        $("#modal-sys-add-counselling-dialog select#mstatus").val("1").prop("selected", true);
        $("#modal-sys-add-counselling-dialog #mno").val('');
        $("#modal-sys-add-counselling-dialog #mdata5").val('');
        $("#modal-sys-add-counselling-dialog #mday1").val('');
        $("#modal-sys-add-counselling-dialog #mid").val('');
        $("#modal-sys-add-counselling-dialog #mCode").val('');
        $("#modal-sys-add-counselling-dialog #mwriter").val('');
        $("#modal-sys-add-counselling-dialog #mdata1").val('');
        $("#modal-sys-add-counselling-dialog #mtitle").val('');
        $("#modal-sys-add-counselling-dialog #mcontents").val('');

        $("#modal-sys-add-counselling-dialog").modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    //엑셀 등록 팝업
    function fileUploadPop() {
        $('#fileNM').val("");
        $("#popup-counseling-upload").modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    //전화상담 진행 수정
    function fn_dialogOpen(no) {

        var param = {
            no: no
        };

        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/ajaxModalCustList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    $("#modal-sys-counselling-dialog #mno").val(result.list.no);
                    $("#modal-sys-counselling-dialog #mdata5").val(result.list.data5);
                    $("#modal-sys-counselling-dialog #mday1").val(result.today);
                    $("#modal-sys-counselling-dialog #mid").val(result.list.loginName);
                    $("#modal-sys-counselling-dialog #mCode").val(result.list.chaCd);
                    $("#modal-sys-counselling-dialog #mwriter").val(result.list.writer);
                    $("#modal-sys-counselling-dialog #mdata1").val(result.list.data1);
                    $("#modal-sys-counselling-dialog #mtitle").text(result.list.title);
                    $("#modal-sys-counselling-dialog #mcontents").text(result.list.contents);
                    if (result.list.userClass == null) {
                        $("#modal-sys-counselling-dialog #mwho option:eq(1)").prop("selected", "selected");
                    } else {
                        $("#modal-sys-counselling-dialog #mwho option:eq(0)").prop("selected", "selected");
                    }
                    if (result.list.data2 == "01") {
                        $("#modal-sys-counselling-dialog #mdata2 option:eq(0)").prop("selected", "selected");
                    } else if (result.list.data2 == "02") {
                        $("#modal-sys-counskelling-dialog #mdata2 option:eq(1)").prop("selected", "selected");
                    } else if (result.list.data2 == "03") {
                        $("#modal-sys-counselling-dialog #mdata2 option:eq(2)").prop("selected", "selected");
                    } else if (result.list.data2 == "04") {
                        $("#modal-sys-counselling-dialog #mdata2 option:eq(3)").prop("selected", "selected");
                    } else if (result.list.data2 == "05") {
                        $("#modal-sys-counselling-dialog #mdata2 option:eq(4)").prop("selected", "selected");
                    } else {
                        $("#modal-sys-counselling-dialog #mdata2 option:eq(5)").prop("selected", "selected");
                    }
                    if (result.list.data4 == 1) {
                        $("#modal-sys-counselling-dialog #mstatus option:eq(0)").prop("selected", "selected");
                    } else if (result.list.data4 == 2) {
                        $("#modal-sys-counselling-dialog #mstatus option:eq(1)").prop("selected", "selected");
                    } else {
                        $("#modal-sys-counselling-dialog #mstatus option:eq(2)").prop("selected", "selected");
                    }
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

        $("#modal-sys-counselling-dialog").modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    // 전화상담건 삭제
    function fn_delete(no) {
        swal({
            type: 'question',
            html: "삭제하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var param = {
                    no: no
                };
                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/sys/deleteCust",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '삭제되었습니다.',
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
                            })
                        }
                    }
                });
            }
        });
    }

    // 파일저장
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

        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");
        if ((!startday || !endday) || (startday > endday)) {
            swal({
                type: 'info',
                text: '상담신청일자를 확인해 주세요.',
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
            if (result.value) {

                var option1 = "0";
                var option2 = "1";
                //옵션값으로 신청구분 및 상태 확인
                if ($("#inlineCheckbox1-1").is(":checked")) {
                    option1 = "0";
                }
                if ($("#inlineCheckbox1-2").is(":checked") && $("#inlineCheckbox1-3").is(":checked") == false) {
                    option1 = "1";
                }
                if ($("#inlineCheckbox1-3").is(":checked") && $("#inlineCheckbox1-2").is(":checked") == false) {
                    option1 = "2";
                }

                if ($("#inlineCheckbox2-1").is(":checked")) {
                    option2 = "0";
                } else if ($("#inlineCheckbox2-2").is(":checked") && $("#inlineCheckbox2-3").is(":checked") == false && $("#inlineCheckbox2-4").is(":checked") == false) {
                    option2 = "1";
                } else if ($("#inlineCheckbox2-3").is(":checked") && $("#inlineCheckbox2-2").is(":checked") == false && $("#inlineCheckbox2-4").is(":checked") == false) {
                    option2 = "2";
                } else if ($("#inlineCheckbox2-4").is(":checked") && $("#inlineCheckbox2-2").is(":checked") == false && $("#inlineCheckbox2-3").is(":checked") == false) {
                    option2 = "3";
                } else if ($("#inlineCheckbox2-2").is(":checked") && $("#inlineCheckbox2-3").is(":checked") && $("#inlineCheckbox2-4").is(":checked") == false) {
                    option2 = "4";
                } else if ($("#inlineCheckbox2-2").is(":checked") && $("#inlineCheckbox2-4").is(":checked") && $("#inlineCheckbox2-3").is(":checked") == false) {
                    option2 = "5";
                } else if ($("#inlineCheckbox2-3").is(":checked") && $("#inlineCheckbox2-4").is(":checked") && $("#inlineCheckbox2-2").is(":checked") == false) {
                    option2 = "6";
                } else {
                    option2 = "0";
                }
                $('#fStartDate').val(startday);
                $('#fEndDate').val(endday);
                $('#fid').val($('#chacd').val());
                $('#fuserClass').val(option1);
                $('#fsearchOption').val($("#searchOption option:selected").val());
                $('#fkeyword').val($("#keyword").val());
                $('#fdata4').val(option2);
                $('#fsearchOrderBy').val($("#searchOrderBy").val());
                // 다운로드
                document.fileForm.action = "/sys/SysCustMgmt/excelDown";
                document.fileForm.submit();
            }
        });
    }
</script>

<form id="fileForm" name="fileForm" method="post">
    <input type="hidden" id="fStartDate" name="fStartDate"/>
    <input type="hidden" id="fEndDate" name="fEndDate"/>
    <input type="hidden" id="fid" name="fid"/>
    <input type="hidden" id="fuserClass" name="fuserClass"/>
    <input type="hidden" id="fsearchOption" name="fsearchOption"/>
    <input type="hidden" id="fkeyword" name="fkeyword"/>
    <input type="hidden" id="fdata4" name="fdata4"/>
    <input type="hidden" id="fsearchOrderBy" name="fsearchOrderBy"/>
</form>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-8">
        <h2>상담내역관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>상담내역관리</strong>
            </li>
        </ol>
        <p class="page-description">등록된 전화상담 고객 목록을 확인하고 조회/관리하는 화면입니다.</p>
    </div>

    <div class="col-lg-2 text-right m-t-xl">
        <button class="btn btn-md btn-w-m btn-primary" onclick="fileUploadPop();"><i class="fa fa-fw fa-cloud-upload"></i> 엑셀등록</button>
    </div>
    <div class="col-lg-2 text-left m-t-xl">
        <button class="btn btn-md btn-w-m btn-primary" onclick="fn_add_dialogOpen()">상담등록</button>
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
                                    <label class="form-label block">상담신청일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" name="startday" id="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" name="endday" id="endday" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth" id="btnSetMonth0" onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth1" onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth6" onclick="setMonthTerm(6);">6개월</button>
                                            </div>
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

                                <div class="col-md-6">
                                    <label class="form-label block">신청구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox1-1" onclick="allClick()" checked>
                                                <label for="inlineCheckbox1-1"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox1-2" onclick="notAll()" checked>
                                                <label for="inlineCheckbox1-2"> 이용기관 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox1-3" onclick="notAll()" checked>
                                                <label for="inlineCheckbox1-3"> 납부자/신규 </label>
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
                                                <select class="form-control" id="searchOption">
                                                    <option value="writer">신청자</option>
                                                    <option value="data1">신청자 연락처</option>
                                                    <option value="contents">내용</option>
                                                    <option value="loginName">기관명</option>
                                                </select>
                                            </span>
                                            <input type="text" class="form-control" id="keyword">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox2-1" onclick="allClick2()" checked>
                                                <label for="inlineCheckbox2-1"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox2-2" onclick="notAll2()" checked>
                                                <label for="inlineCheckbox2-2"> 대기 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox2-3" onclick="notAll2()" checked>
                                                <label for="inlineCheckbox2-3"> 진행중 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox2-4" onclick="notAll2()" checked>
                                                <label for="inlineCheckbox2-4"> 완료 </label>
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

    <div class="animated fadeInRight" id="focus">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">
                            <span class="m-r-sm">
                                대기 건수 : <strong class="text-danger" id="wcount">${map.wcount }</strong> 건
                            </span>
                            <span> | </span>
                            <span class="m-l-sm">
                                전체 건수 : <strong class="text-success" id="totcount">${map.count }</strong> 건
                            </span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="search()">
                                <option value="day" <c:if test="${map.searchOrderBy == 'day'}">selected</c:if>>신청일자순 정렬</option>
                                <option value="data4" <c:if test="${map.searchOrderBy == 'data4'}">selected</c:if>>상태순 정렬</option>
                                <option value="day1" <c:if test="${map.searchOrderBy == 'day1'}">selected</c:if>>상담완료일자순 정렬</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="search()">
                                <option value="10" <c:if test="${map.pageScale == '10'}">selected</c:if>>10개씩 조회</option>
                                <option value="20" <c:if test="${map.pageScale == '20'}">selected</c:if>>20개씩 조회</option>
                                <option value="50" <c:if test="${map.pageScale == '50'}">selected</c:if>>50개씩 조회</option>
                                <option value="100" <c:if test="${map.pageScale == '100'}">selected</c:if>>100개씩 조회</option>
                                <option value="200" <c:if test="${map.pageScale == '200'}">selected</c:if>>200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" onclick="fn_fileSave()">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="footable table table-stripped table-align-center" data-page-size="10" data-filter=#filter>
                                <colgroup>
                                    <col width="50">
                                    <col width="130">
                                    <col width="150">
                                    <col width="120">
                                    <col width="340">
                                    <col width="130">
                                    <col width="150">
                                    <col width="140">
                                    <col width="100">
                                    <col width="130">
                                    <col width="133">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>신청일자</th>
                                        <th>신청구분</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>신청자</th>
                                        <th>신청자 연락처</th>
                                        <th>예약사유</th>
                                        <th>상태</th>
                                        <th>상담완료일자</th>
                                        <th></th>
                                    </tr>
                                </thead>

                                <tbody id="reSearchbody"></tbody>
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
<%-- 전화상담 파일업로드 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/counseling-upload.jsp" flush="false"/>

<%-- 전화상담 진행상황 등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/sys-add-counselling-dialog.jsp" flush="false"/>

<!-- 전화상담 진행상황 수정 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/sys-counselling-dialog.jsp" flush="false"/>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<script>
    $(document).ready(function () {
        var now = new Date();
        var endDay = now.getFullYear() + "." + addZero(now.getMonth() + 1) + "." + addZero(now.getDate());
        $("#endday").val(endDay);
        $("#inlineCheckbox2-2").prop("checked", true);

        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($('#chacd').val());
            $("#popChaname").val('');
            fn_ListCollector();
        });

        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            forceParse: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            autoclose: true
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

        $("#keyword").on("keydown", function (event) {
            if (event.keyCode == 13) {
                if ($.trim($(this).val()) == "") return false;
                else search();
            }
        });

        setMonthTerm(1);
        search();
    });
</script>
