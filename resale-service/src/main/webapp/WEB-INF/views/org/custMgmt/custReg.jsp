<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/>

<script type="text/javascript">
    var firstDepth = "nav-link-31";
    var secondDepth = "sub-01";
    var cuPage = 1;
    var delMessage = '선택내역을 삭제하시겠습니까?';
    var noSelectMsg = "삭제할 고객을 선택하세요.";
    var noSelectMsg2 = "등록할 고객을 선택하세요.";
    var saveMessage = "임시고객이 정상등록되었습니다.";

    $(document).ready(function () {
        $("#row-th").click(function () {
            if ($("#row-th").prop("checked")) {
                $("input[name=checkOne]").prop("checked", true);
            } else {
                $("input[name=checkOne]").prop("checked", false);
            }
        });

        if ("${map.count}" > 0) {
            swal({
                title: "이전에 작성중인 내역이 임시저장 상태로 존재합니다.",
                html: "이어서 작업하시겠습니까?",
                type: 'info',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: '삭제',
                cancelButtonText: '이어서 작업',
                reverseButtons: false
            }).then(function (result) {
                if (result.value) {
                    delCusmoter();
                } else {
                    fnSearch(cuPage);
                }
            });
        } else {
            fnSearch(cuPage);
        }

        $(".btn-open-reg-payer-information").click(function () {
            modify_payer_information_init("I");
            fn_reset_scroll();

            $('#modify-payer-information').modal({
                backdrop: 'static',
                keyboard: false
            });
        });

        $(".btn-open-modify-payer-info").click(function () {
            fn_reset_scroll();

            var rowCnt = 0;
            var checkbox = $("input[name='checkOne']:checked");
            checkbox.map(function (i) {
                rowCnt++;
            });

            if (rowCnt == 0) {
                swal({
                    type: 'question',
                    text: '선택하신 정보수정 건이 없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            } else if (rowCnt > 1) {
                swal({
                    type: 'question',
                    text: '수정할 고객을 하나만 선택하세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            } else if (rowCnt == 1) {
                modify_payer_information_init("U");
                $('#modify-payer-information').modal({
                    backdrop: 'static',
                    keyboard: false
                });
            }

        });

        $(".btn-reg-payer-information").click(function () {
            if ($("#totCnt").html() == 0 || $("#totCnt").html() == null) {
                swal({
                    type: 'error',
                    text: '등록할 고객정보가 없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
                return false;
            }
            swal({
                type: 'question',
                html: "고객대상을 등록하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    var url = "/org/custMgmt/updateCustList";
                    var param = {};
                    $.ajax({
                        type: "post",
                        url: url,
                        contentType: "application/json; charset=UTF-8",
                        data: JSON.stringify(param),
                        success: function (result) {
                            swal({
                                type: 'success',
                                text: '고객정보를 등록하였습니다.',
                                showCancelButton: true,
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '청구등록',
                                cancelButtonColor: '#3085d6',
                                cancelButtonText: '고객조회'
                            }).then(function (result) {
                                if (result.value) {
                                    // fnSearch();
                                    window.location.href = '/org/claimMgmt/claimReg';
                                } else {
                                    window.location.href = '/org/custMgmt/custList';
                                }
                            });
                        },
                        error: function (data) {
                            swal({
                                type: 'error',
                                text: '고객정보를 등록 실패하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            })
                        }
                    });
                }
            });
        });

        $(".btn-delete-payer-information").click(function () {
            var rowCnt = 0;
            var itemDelVals = [];
            var checkbox = $("input[name='checkOne']:checked");
            checkbox.each(function (i) {
                var tr = checkbox.parent().parent().eq(i);
                var td = tr.children();
                itemDelVals.push($(this).val());
                rowCnt++;
            });

            if (rowCnt == 0) {
                swal({
                    type: 'info',
                    text: noSelectMsg,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            } else {
                swal({
                    type: 'question',
                    html: "선택하신 " + rowCnt + "건을 삭제하시겠습니까?",
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '확인',
                    cancelButtonText: '취소',
                    reverseButtons: true
                }).then(function (result) {
                    if (result.value) {
                        var url = "/org/custMgmt/deleteCustInfo";
                        var param = {
                            itemList: itemDelVals
                        };
                        $.ajax({
                            type: "post",
                            url: url,
                            data: param,
                            success: function (result) {
                                swal({
                                    type: 'success',
                                    text: '고객 대상 삭제하였습니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                }).then(function (result) {
                                    if (result.value) {
                                        $('#row-th').prop('checked', false);
                                        fnSearch();
                                    }
                                });
                            },
                            error: function (data) {
                                swal({
                                    type: 'error',
                                    text: '고객 대상 삭제 실패하였습니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })
                            }
                        });
                    }
                });
            }
        });

        fnSearch();
    });

    function delCusmoter() {
        var url = "/org/custMgmt/deleteCustomer"
        var param = {};
        $.ajax({
            type: "GET",
            url: url,
            data: param,
            success: function (result) {
                swal({
                    type: 'success',
                    text: '삭제 하였습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function (result) {
                    if (result.value) {
                        fnSearch();
                    }
                });
            },
            error: function (data) {
                swal({
                    type: 'error',
                    text: '삭제 실패하였습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
            }
        });
    }

    function fnSearch(page) {
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        var url = "/org/custMgmt/getCustRegList";
        var param = {
            searchOrderBy: $('#vSearchOrderBy option:selected').val(),
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                $('#row-th').prop('checked', false);
                if (result.retCode == "0000") {
                    fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
                    ajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });
    }

    function fnGrid(result, obj) {
        var str = '';
        $('#totCnt').text(result.count);
        if (result.count <= 0) {
            str += '<tr><td colspan="16" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>';
                str += '<input class="form-check-input table-check-child" type="checkbox" name="checkOne" id="row-' + v.rn + '" value="' + v.vano + '" onclick="fn_listCheck();" onchange="changeBGColor(this)">';
                str += '<label for="row-' + v.rn + '"><span></span></label>';
                str += '<input type="hidden" id="chaCd"    value="' + v.chaCd + '"/>'
                str += '<input type="hidden" id="vano"     value="' + v.vano + '"/>'
                str += '<input type="hidden" id="masKey"   value="' + v.masKey + '"/>'
                str += '<input type="hidden" id="cusMail"  value="' + v.cusMail + '"/>'
                str += '<input type="hidden" id="rcpGubn"  value="' + v.rcpGubn + '"/>'
                str += '<input type="hidden" id="disabled" value="' + v.disabled + '"/>'
                str += '<input type="hidden" id="rcpReqTy" value="' + v.rcpReqTy + '"/>'
                str += '</td>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.regDt + '</td>';
                str += '<td><a href="#" onclick="fnCusInfoUpdate(\'' + v.vano + '\');">' + basicEscape(v.cusName) + '</a></td>';
                str += '<td>' + NVL(v.vano) + '</td>';
                if (NVL(v.disabled) == 'N') {
                    str += '<td>정상</td>';
                } else if (NVL(v.disabled) == 'Y') {
                    str += '<td class="text-danger">삭제</td>';
                } else {
                    str += '<td class="text-danger">임시</td>';
                }
                str += '<td>' + v.cusKey + '</td>';
                if ('${orgSess.cusGubn1}' != null && '${orgSess.cusGubn1}' != '') {
                    str += '<td>' + basicEscape(NVL(v.cusGubn1)) + '</td>';
                }
                if ('${orgSess.cusGubn2}' != null && '${orgSess.cusGubn2}' != '') {
                    str += '<td>' + basicEscape(NVL(v.cusGubn2)) + '</td>';
                }
                if ('${orgSess.cusGubn3}' != null && '${orgSess.cusGubn3}' != '') {
                    str += '<td>' + basicEscape(NVL(v.cusGubn3)) + '</td>';
                }
                if ('${orgSess.cusGubn4}' != null && '${orgSess.cusGubn4}' != '') {
                    str += '<td>' + basicEscape(NVL(v.cusGubn4)) + '</td>';
                }
                if (NVL(v.rcpGubn) == 'N') {
                    str += '<td class="text-danger">제외</td>';
                } else {
                    str += '<td>대상</td>';
                }
                str += '<td>' + NVL(v.cusHp) + '</td>';
                str += '<td>' + NVL(v.cusMail) + '</td>';
                if (v.cusType == '1') {
                    str += '<td class="hidden hidden-01">소득공제</td>';
                } else if (v.cusType == '2') {
                    str += '<td class="hidden hidden-01">지출증빙</td>';
                } else {
                    str += '<td class="hidden hidden-01"></td>';
                }
                if (v.confirm == '11') {
                    str += '<td class="hidden hidden-01">휴대폰번호</td>';
                } else if (v.confirm == '12') {
                    str += '<td class="hidden hidden-01">현금영수증카드번호</td>';
                } else if (v.confirm == '13') {
                    str += '<td class="hidden hidden-01">주민번호</td>';
                } else if (v.confirm == '21') {
                    str += '<td class="hidden hidden-01">사업자번호</td>';
                } else {
                    str += '<td class="hidden hidden-01"></td>';
                }
                str += '<td class="hidden hidden-01">' + NVL(v.cusOffNo) + '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);

        if ($('#tableCheckbox1').is(":checked")) {
            $("#tableCheckbox1").click();
            $("#tableCheckbox1").click();
        }
    }

    function fnCusInfoUpdate(vano) {
        modify_payer_information_init("U", vano);
        fn_reset_scroll();
        $('#modify-payer-information').modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    function list(page) {
        fnSearch(page);
    }

    function pageChange() {
        cuPage = 1;
        fnSearch(cuPage);
    }

    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }

    function NVL(value) {
        if (value == "" || value == null || value == undefined
            || ( value != null && typeof value == "object" && !Object.keys(value).length )) {
            return "";
        } else {
            return value;
        }
    }

    function fn_fileSave() {
        fncClearTime();
        var rowCnt = 0;
        var checkbox = $("input[name='checkOne']");
        checkbox.map(function (i) {
            rowCnt++;
        });
        if (rowCnt == 0) {
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
            if (result.value) {
                $('#searchOrderBy').val($('#vSearchOrderBy option:selected').val());
                if ($('#tableCheckbox1').is(":checked")) {
                    $('#cashShow').val("Y");
                } else {
                    $('#cashShow').val("N");
                }
                document.fileForm.action = "/org/custMgmt/excelSaveCustReg";
                document.fileForm.submit();
            }
        });
    }

    function modalList(num, val) {
        if (val == '1') {
            fn_regCusNameSearch(num);
        } else if (val == '2') {
            fn_custFailList(num);
        }
    }

    function fn_listCheck() {
        var stList = [];
        var idx = 0;
        var num = 0;

        var check = $("input[name='checkOne']");
        check.map(function (i) {
            if ($(this).is(':checked') == true) {
                idx++;
            }
            num++;
        });

        if (num == idx) {
            $('#row-th').prop('checked', true);
        } else {
            $('#row-th').prop('checked', false);
        }
    }

    function fn_regBulkPayer() {
        $('#reg-bulk-payer').modal({backdrop: 'static', keyboard: false});
    }
</script>

<input type="hidden" id="curPage" name="curPage"/>

<form id="fileForm" name="fileForm" method="post">
    <input type="hidden" name="searchOrderBy" id="searchOrderBy"/>
</form>

<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link active" href="#">고객등록</a>
            <a class="nav-link" href="#">고객조회</a>
        </nav>
    </div>

    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                    <span> > </span> <span class="depth-1">고객관리</span>
                    <span> > </span> <span class="depth-2 active">고객등록</span>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <h2>고객등록</h2>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>
                        새로운 신한 다모아는 [고객등록]과 [청구등록]의 메뉴와 기능을 분리하여 제공합니다.
                        <br/>고객등록은 새로운 고객의 정보를 입력하고 가상계좌를 부여하기 위해 사용하며, 청구등록을 위해서는 반드시 고객등록이 먼저 진행되어야 합니다.
                    </p>
                </div>
            </div>
        </div>
    </div>

    <div class="container mb-5">
        <div id="payer-registration-option">
            <div class="row">
                <div class="col col-md-6 col-6 text-center btn-open-reg-payer-information">
                    <a href="#">
                        <img src="/assets/imgs/common/icon-registration-payer.png" class="mb-2">
                        <span>개별고객<br>등록</span>
                    </a>
                </div>
                <div class="col col-md-6 col-6 text-center" onclick="fn_regBulkPayer();">
                    <a href="" data-toggle="modal" data-target="#reg-bulk-form-selecter">
                        <img src="/assets/imgs/common/icon-bulk-registration.png" class="mb-2">
                        <span>엑셀파일<br>대량등록</span>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="search-result" class="list-id">
        	<div class="row no-gutters mt-3">
				<div class="col-6">
					<h5>고객대상 목록</h5>
				</div>
				<div class="col-6 text-right"></div>
			</div>
            <div class="table-option row mb-2">
                <div class="col-md-6 col-sm-12 col-12 form-inline">
                    <span class="amount mr-2">조회결과 [총 <em class="font-blue" id="totCnt">${map.count}</em>건]</span>
                    <div class="form-check form-check-inline ml-2">
                        <input class="form-check-input" type="checkbox" id="tableCheckbox1" value="option1">
                        <label for="tableCheckbox1"> <span class="mr-2"></span>현금영수증 발급정보 표시</label>
                    </div>
                </div>

                <div class="col-md-6 col-sm-12 col-12 text-right mt-1">
                    <select class="form-control" name="vSearchOrderBy" id="vSearchOrderBy" onchange="fnSearch();">
                        <option value="regDt">등록일자순 정렬</option>
                        <option value="cusName">고객명순 정렬</option>
                    </select>
                    <select class="form-control" name="pageScale" id="pageScale" onchange="fnSearch();">
                        <option value="10">10개씩 조회</option>
                        <option value="20">20개씩 조회</option>
                        <option value="50">50개씩 조회</option>
                        <option value="100">100개씩 조회</option>
                        <option value="200">200개씩 조회</option>
                    </select>
                    <button class="btn btn-sm btn-d-gray hidden-on-mobile" type="button" onclick="fn_fileSave();">파일저장</button>
                </div>
            </div>

            <div class="row">
                <div class="table-responsive col mb-3 pd-n-mg-o">
                    <table class="table table-sm table-hover table-primary">
                        <thead>
                            <tr>
                                <th><input class="form-check-input table-check-parents" type="checkbox" name="row-th" id="row-th"> <label for="row-th"><span></span></label></th>
                                <th>NO</th>
                                <th>등록일자</th>
                                <th>고객명</th>
                                <th>가상계좌</th>
                                <th>고객상태</th>
                                <th>고객번호</th>
                                <c:if test="${orgSess.cusGubn1 != null && orgSess.cusGubn1 != '' }"><th>${orgSess.cusGubn1}</th></c:if>
                                <c:if test="${orgSess.cusGubn2 != null && orgSess.cusGubn2 != '' }"><th>${orgSess.cusGubn2}</th></c:if>
                                <c:if test="${orgSess.cusGubn3 != null && orgSess.cusGubn3 != '' }"><th>${orgSess.cusGubn3}</th></c:if>
                                <c:if test="${orgSess.cusGubn4 != null && orgSess.cusGubn4 != '' }"><th>${orgSess.cusGubn4}</th></c:if>
                                <th>납부대상</th>
                                <th>연락처</th>
                                <th class="border-r-e6">이메일</th>
                                <th class="hidden hidden-01" width="110">발급용도</th>
                                <th class="hidden hidden-01" width="110">발급방법</th>
                                <th class="hidden hidden-01" width="120">발급번호</th>
                            </tr>
                        </thead>

                        <tbody id="resultBody">
                            <c:choose>
                                <c:when test="${map.count > 0}">
                                    <c:forEach var="row" items="${map.list}" varStatus="status">
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" name="checkOne" type="checkbox" id="row-${row.rn}" value="${row.vano}" onclick="fn_listCheck();" onchange="changeBGColor(this)"/>
                                                <label for="row-${row.rn}"><span></span></label>
                                                <input type="hidden" id="chaCd" value="${row.chaCd}"/>
                                                <input type="hidden" id="vano" value="${row.vano}"/>
                                                <input type="hidden" id="masKey" value="${row.masKey}"/>
                                                <input type="hidden" id="cusMail" value="${row.cusMail}"/>
                                                <input type="hidden" id="rcpGubn" value="${row.rcpGubn}"/>
                                                <input type="hidden" id="disabled" value="${row.disabled}"/>
                                                <input type="hidden" id="rcpReqTy" value="${row.rcpReqTy}"/>
                                            </td>
                                            <td>${row.rn}</td>
                                            <td>${row.regDt}</td>
                                            <td><c:out value="${row.cusName}" escapeXml="true"/> </td>
                                            <td>${row.vano}</td>
                                            <c:if test="${row.disabled == 'N' }"><td>정상</td></c:if>
                                            <c:if test="${row.disabled == 'Y' }"><td class="text-danger">삭제</td></c:if>
                                            <c:if test="${row.disabled != 'N' && row.disabled != 'Y'}"><td class="text-danger">임시</td></c:if>
                                            <td>${row.cusKey}</td>
                                            <c:if test="${orgSess.cusGubn1 != null && orgSess.cusGubn1 != '' }"><td>${row.cusGubn1}</td></c:if>
                                            <c:if test="${orgSess.cusGubn2 != null && orgSess.cusGubn2 != '' }"><td>${row.cusGubn2}</td></c:if>
                                            <c:if test="${orgSess.cusGubn3 != null && orgSess.cusGubn3 != '' }"><td>${row.cusGubn3}</td></c:if>
                                            <c:if test="${orgSess.cusGubn4 != null && orgSess.cusGubn4 != '' }"><td>${row.cusGubn4}</td></c:if>
                                            <c:if test="${row.rcpGubn != 'N' }"><td>대상</td></c:if>
                                            <c:if test="${row.rcpGubn == 'N' }"><td class="text-danger">제외</td></c:if>
                                            <td>${row.cusHp}</td>
                                            <td>${row.cusMail}</td>
                                            <td class="hidden hidden-01">${row.cusType}</td>
                                            <td class="hidden hidden-01">${row.cusMtd}</td>
                                            <td class="hidden hidden-01">${row.cusOffNo}</td>
                                        </tr>
                                    </c:forEach>
                                </c:when>

                                <c:otherwise>
                                    <tr>
                                        <td colspan="16" class="text-center">[조회된 내역이 없습니다.]</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="row mb-3 hidden-on-mobile">
                <div class="col-6">
                    <button type="button" class="btn btn-sm btn-gray-outlined btn-delete-payer-information">선택삭제</button>
                </div>
                <div class="col-6 text-right">
                    <button type="button" class="btn btn-sm btn-d-gray btn-open-modify-payer-info">정보수정</button>
                </div>
            </div>

            <jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false"/>

            <div class="row mb-4 hidden-on-mobile">
                <div class="col-12 text-center">
                    <button type="button" class="btn btn-primary btn-reg-payer-information">고객등록</button>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="quick-instruction" class="foldable-box">
            <div class="row foldable-box-header">
                <div class="col-8">
                    <img src="/assets/imgs/common/icon-notice.png"> 알려드립니다.
                </div>
                <div class="col-4 text-right">
                    <img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
                </div>
            </div>

            <div class="row foldable-box-body">
                <div class="col">
                    <h6>■ 고객 등록 방법</h6>
                    <ul>
                        <li>개별고객 등록 : 신규 고객을 한 명씩 입력할 경우 버튼 클릭</li>
                        <li>엑셀파일 대량등록 : 여러 고객을 대량으로 손쉽게 등록할 수 있는 기능</li>
                        <li class="depth-2">- '엑셀파일 양식 다운로드' 받은 후, 해당 파일에 고객 정보 입력 후 '파일찾기' 버튼을 클릭하여 파일 등록</li>
                        <li class="depth-2">- 대량등록 실패내역 : 엑셀자료 등록 중 실패된 내역을 노출, '등록 실패건 엑셀 다운로드' 버튼을 클릭하여 양식 수정 후 업로드 후 재적용</li>
                    </ul>

                    <h6>■ 고객 기본 정보</h6>
                    <ul>
                        <li>필수 입력 정보 : 고객명, 납부대상 여부</li>
                        <li>고객번호 : 개별 고객의 고유 식별 정보로 사용여부 선택 가능</li>
                        <li class="depth-2">[tip] 학번, 고객번호 등 기존에 사용하던 고객별 구분 체계가 있다면 고객번호란에 입력, 동명이인 구분 등을 위해 사용</li>
                        <li>고객구분 : [마이페이지 > 서비스설정] 메뉴에서 설정한 고객별 그룹 (ex : 학년, 반, 대리점, 부서)의 명칭으로 최대 4개까지 설정 가능</li>
                        <li>납부대상 : 청구등록 대상에 포함할 지에 따라 [납부대상/납부제외] 중 선택</li>
                        <li>납부제외 시 해당 가상계좌로 입금 불가하며 청구수수료 대상에서 제외</li>
                    </ul>

                    <h6>■ 현금영수증 정보(선택)</h6>
                    <ul>
                        <li>발급용도 : 소득공제 또는 지출증빙 선택</li>
                        <li>발급방법 : 휴대폰번호, 현금영수증 카드번호, 주민등록번호, 사업자번호 중 선택</li>
                        <li>발급번호 : 핸드폰번호, 카드번호, 사업자번호의 "-"없이, "숫자"만 입력</li>
                        <li>현금영수증 자동발급 여부 : [자동발급/수동발급] 중 선택, 자동발급 선택 시 가상계좌 납부완료 시 저장된 정보로 자동발행</li>
                    </ul>

                    <h6>■ 메모</h6>
                    <ul>
                        <li>고객별 참고사항 및 특이사항을 기입하여 관리</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>
<jsp:include page="/WEB-INF/views/include/modal/modify-payer-information.jsp" flush="false"/>
<jsp:include page="/WEB-INF/views/include/modal/reg-bulk-payer.jsp" flush="false"/>
<jsp:include page="/WEB-INF/views/include/modal/payer-classification.jsp" flush="false"/>
<jsp:include page="/WEB-INF/views/include/modal/detail-payer-information.jsp" flush="false"/>
<jsp:include page="/WEB-INF/views/include/modal/reg-bulk-asis-payer.jsp" flush="false"/>
