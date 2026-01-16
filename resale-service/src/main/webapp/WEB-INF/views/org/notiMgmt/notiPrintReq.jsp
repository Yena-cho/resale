<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<s:authentication property="principal.username" var="chaCd"/>
<s:authentication property="principal.username" var="userId"/>
<s:authentication property="principal.name" var="userName"/>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/>

<script type="text/javascript">
    var firstDepth = "nav-link-33";
    var secondDepth = "sub-03";
</script>

<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>

<script>
    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                document.getElementById("zipCode").value = data.zonecode; //5자리 새우편번호 사용
                document.getElementById("address1").value = data.address;
                document.getElementById("address2").value = data.buildingName;
            }
        }).open();
    }
</script>

<script type="text/javascript">
    $(function () {
        getYearsBox2('yearsBox');
        getMonthBox('monthBox');
        cusGubnBox('cusgubna');
        cusGubnBox2('cusgubnb');

        // 전체선택, 해제
        $('#rowsAll').click(function () {
            if ($('#rowsAll').prop('checked')) {
                $('input:checkbox[name=checkList]').prop('checked', true);
            } else {
                $('input:checkbox[name=checkList]').prop('checked', false);
            }
        });
        if ('${info.sortTy}' == '' && '${info.sortTy}' == null) {
            $('#tableCheckbox1').attr('checked', 'checked');
        }
        $('#billGubn').val('${info.billGubn}');
        if ('${info.billGubn}' == '' && '${info.billGubn}' == null) {
            $('#billGubn').val('01');
        }

        list("1");
    });

    /**
     * @param val       N20001 : 일반배송 | N20002 : 퀵배송
     */
    function fn_billDlvr(dlvrTypeCd) {
        var dlvrTypeCd = dlvrTypeCd;
        var html = '';
        var text = '';

        if (fnValidation('', '')) {
            if (dlvrTypeCd == 'N20001') {
                html = '택배 배송으로 출력의뢰를 요청하시겠습니까?<p>(택배비 착불)';
            } else if (dlvrTypeCd == 'N20002') {
                html = '퀵서비스 배송으로 출력의뢰를 요청하시겠습니까?<p>(요청취소불가, 택배비 착불)';
            }

            swal({
                type: 'question',
                html: html,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {
                    $('#dlvrTypeCd').val(dlvrTypeCd);

                    var url = "/org/notiMgmt/cont1LengthCheck";
                    var param = {
                        billGubn: $('#billgubn option:selected').val(),
                        chaCd: $('#chaCd').val(),
                        dlvrTypeCd : dlvrTypeCd
                    };

                    $.ajax({
                        type: "post",
                        async: true,
                        url: url,
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify(param),
                        success: function (result) {
                            if (result.retCode == "0000") {
                                var billCont1 = result.dto.billCont1;
                                if (billCont1 != null) {
                                    var line = billCont1.split("\n").length;
                                }
                                if (line > 10) {
                                    swal({
                                        type: 'info',
                                        text: "안내문구는 최대 10줄까지 등록할 수 있습니다. 고지설정 메뉴에서 재등록후 시도해 주세요.",
                                        confirmButtonColor: '#3085d6',
                                        confirmButtonText: '확인'
                                    })
                                } else {
                                    fn_print();
                                }
                            } else {
                                swal({
                                    type: 'error',
                                    text: result.retMsg,
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })
                            }
                        }
                    });
                }
            });
        }
    }

    function fn_print() {
        var checkArr = [];
        $('input:checkbox[name=checkList]:checked').each(function (i) {
            checkArr.push($(this).val());
        });

        var url = "/org/notiMgmt/saveNotiPrintReq";
        var param = {
            chaCd: $('#chaCd').val(),
            masMonth: $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val(),
            billGubn: $('#billgubn option:selected').val(),
            sortTy: "N",
            rcusGubn: $('input:radio[name=rcusgubn]:checked').val(),
            sort1Cd: $("#cusgubna").val(),
            sort2Cd: $("#cusgubnb").val(),
            reqName: $('#reqName').val(),
            reqHp: $('#reqHp').val(),
            zipCode: $('#zipCode').val(),
            address1: $('#address1').val(),
            address2: $('#address2').val(),
            cdList: checkArr,
            dlvrTypeCd: $('#dlvrTypeCd').val(),
            curpage: "1"
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    swal({
                        type: 'info',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    fnGrid(result, 'resultBody');
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });
    }

    // 출력의뢰 취소
    function fn_canclePrint() {
        var checkArr = [];
        $('input:checkbox[name=checkList]:checked').each(function (i) {
            checkArr.push($(this).val());
        });

        if (fnValidation('D', checkArr)) {
            var url = "/org/notiMgmt/cancleNotiPrintReq";
            var param = {
                chaCd: $('#chaCd').val(),
                masMonth: $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val(), //청구년도월
                billGubn: $('#billgubn option:selected').val(),
                sortTy: "N",
                rcusGubn: $('input:radio[name=rcusgubn]:checked').val(),
                sort1Cd: $("#cusgubna").val(),
                sort2Cd: $("#cusgubnb").val(),
                reqName: $('#reqName').val(),
                reqHp: $('#reqHp').val(),
                zipCode: $('#zipCode').val(),
                address1: $('#address1').val(),
                address2: $('#address2').val(),
                cdList: checkArr,
                dlvrTypeCd: $('#dlvrTypeCd').val(),
                curpage: "1"
            };

            swal({
                type: 'question',
                html: "고지서 출력요청을 취소 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        type: "post",
                        async: true,
                        url: url,
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify(param),
                        success: function (result) {
                            if (result.retCode == "0000") {
                                swal({
                                    type: 'info',
                                    text: result.retMsg,
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })
                                fnGrid(result, 'resultBody');
                            } else {
                                swal({
                                    type: 'error',
                                    text: result.retMsg,
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })
                            }
                        }
                    });
                } else {
                    return false;
                }
            });
        }
    }

    // 데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';

        if (result.count <= 0) {
            str += '<tr><td colspan="11">[신청된 내역이 없습니다.]</td></tr>';
        } else {
            var curpage;
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>';
                str += '<input class="form-check-input" type="checkbox" name="checkList" id="row-' + (i + 1) + '" value="' + v.notiMasReqCd + '" reqstCd="' + v.reqStCd + '" onchange="changeBGColor(this)"> ';
                str += '<label for="row-' + (i + 1) + '"><span></span></label>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.masMonth + '</td>';
                str += '<td>' + v.reqDate + '</td>';
                str += '<td>' + v.sendCnt + '</td>';
                str += '<td>' + v.dlvrTypeCd + '</td>';
                str += '<td>' + v.billGubn + '</td>';
                str += '<td>' + v.sort1Cd + '</td>';
                str += '<td>' + v.sort2Cd + '</td>';
                str += '<td>' + v.reqSt + '</td>';
                if (moment(v.reqDate, 'YYYYMMDD') < moment(new Date('2019-07-17 00:00:00'))) {
                    str += '<td style="color:grey">다운로드</td>';
                } else {
                    str += '<td ><button type="button" class="btn btn-xs btn-link" onclick="fn_preview(\'' + v.notiMasReqCd + '\')">다운로드</button></td>';
                }
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    function fnValidation(jobType, checkArr) {
        var reqName = $('#reqName').val().trim();
        var regExp = /^[0-9]{8,12}$/;
        var reqHp = $("#reqHp").val().trim();
        var zipCode = $('#zipCode').val().trim();
        var address1 = $('#address1').val().trim();
        var address2 = $('#address2').val().trim();

        // 고지서 출력요청 취소
        if (jobType == 'D') {
            if (checkArr.length <= 0) {
                swal({
                    type: 'warning',
                    text: '취소할 건을 1건 이상 선택하세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
                return false;
            }
            if ('${count}' == 0) {
                swal({
                    type: 'warning',
                    text: '조회된 자료가 없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
            }

            //출력의뢰 취소는 출력 의뢰가 요청된 상태에서만 가능합니다.
            var isBr01 = true;
            $('input:checkbox[name=checkList]:checked').each(function (i) {
                if ($(this).attr('reqstCd') != 'BR01') {
                    isBr01 = false;
                }
            });
            if (isBr01 == false) {
                swal({
                    type: 'info',
                    text: '출력의뢰 취소는 출력 의뢰가 요청된 상태에서만 가능합니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
                return false;
            } else {
                return true;
            }
        } else {
            // 고지서 출력 요청
            if (reqName == '' || reqName == null) {
                swal({
                    type: 'info',
                    text: "수취인 이름를 정확히 입력해주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });

                return;
            }

            if (reqHp == '') {
                swal({
                    type: 'info',
                    text: '수취인 휴대폰 번호를 입력하세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
                return false;
            }

            if (!regExp.test($("#reqHp").val())) {
                swal({
                    type: 'info',
                    text: "수취인 연락처를 정확히 입력해주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });

                return;
            }

            if (zipCode == '' || zipCode == null) {
                swal({
                    type: 'info',
                    text: "배송주소를 정확히 입력해주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });

                return;
            }

            if (address1 == '' || address1 == null) {
                swal({
                    type: 'info',
                    text: "배송주소를 정확히 입력해주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });

                return;
            }

            if (address2 == '' || address2 == null) {
                swal({
                    type: 'info',
                    text: "배송주소를 정확히 입력해주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });

                return;
            }
        }
        return true;
    }

    function cusGubnBox(obj) {
        var str = "<option value='N00001'>고객명</option>";
        if ('${orgSess.cusGubn1}' != null && '${orgSess.cusGubn1}' != '') {
            str += "<option value='N00002'>${orgSess.cusGubn1}</option>";
        }
        if ('${orgSess.cusGubn2}' != null && '${orgSess.cusGubn2}' != '') {
            str += "<option value='N00003'>${orgSess.cusGubn2}</option>";
        }
        if ('${orgSess.cusGubn3}' != null && '${orgSess.cusGubn3}' != '') {
            str += "<option value='N00004'>${orgSess.cusGubn3}</option>";
        }
        if ('${orgSess.cusGubn4}' != null && '${orgSess.cusGubn4}' != '') {
            str += "<option value='N00005'>${orgSess.cusGubn4}</option>";
        }
        $('#' + obj).html(str);
    }

    function cusGubnBox2(obj) {
        var str = "<option value='N'>선택안함</option>";
        str += "<option value='N00001'>고객명</option>";
        if ('${orgSess.cusGubn1}' != null && '${orgSess.cusGubn1}' != '') {
            str += "<option value='N00002'>${orgSess.cusGubn1}</option>";
        }
        if ('${orgSess.cusGubn2}' != null && '${orgSess.cusGubn2}' != '') {
            str += "<option value='N00003'>${orgSess.cusGubn2}</option>";
        }
        if ('${orgSess.cusGubn3}' != null && '${orgSess.cusGubn3}' != '') {
            str += "<option value='N00004'>${orgSess.cusGubn3}</option>";
        }
        if ('${orgSess.cusGubn4}' != null && '${orgSess.cusGubn4}' != '') {
            str += "<option value='N00005'>${orgSess.cusGubn4}</option>";
        }
        $('#' + obj).html(str);
    }

    function list(page) {
        var param = {
            curPage: page
        };
        
        $.ajax({
            type: "POST",
            async: true,
            url: "/org/notiMgmt/agaxGetNotiPrintReq",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'resultBody');
                    ajaxPaging(result, 'PageArea');
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

    function fnMasCnt() {
        var param = {
            masMonth: $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val() //청구년도월
        };
        
        $.ajax({
            type: "POST",
            async: true,
            url: "/org/notiMgmt/ajaxMonthPrintReq",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    $('#prtCnt').text(result.masCnt);
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

    function fn_preview(notiMasReqCd) {
        swal({
            type: 'question',
            html: "고지서 미리보기를 다운로드 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function(result) {
            if (!result.value) {
                return;
            }

            $('#sBrowserType').val('ie');
            $('#sMasMonth').val($('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val());
            $('#sNotiMasReqCd').val(notiMasReqCd);

            // ie용 pdf 출력
            $('#pdfMakeForm').submit();
        });
    }
</script>

<form id="pdfMakeForm" name="pdfMakeForm" method="post" action="/org/notiMgmt/billPdfPreView">
    <input type="hidden" id="sBrowserType" name="sBrowserType"/>
    <input type="hidden" id="sMasMonth" name="sMasMonth"/>
    <input type="hidden" id="sNotiMasReqCd" name="sNotiMasReqCd"/>
</form>

<input type="hidden" id="userName" name="userName" value="${userName}"/>
<input type="hidden" id="chaCd" name="chaCd" value="${orgSess.chacd}"/>
<input type="hidden" id="dlvrTypeCd" name="dlvrTypeCd"/>

<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link" href="/org/notiMgmt/notiInq">고지서 조회/출력</a>
            <a class="nav-link" href="/org/notiMgmt/notiConfig">고지서설정</a>
            <a class="nav-link active" href="#">고지서 출력의뢰</a>
        </nav>
    </div>
    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2"> <span> > </span>
                    <span class="depth-1">고지관리</span> <span> > </span>
                    <span class="depth-2 active">고지서 출력의뢰</span>
                </div>
            </div>
            <div class="row">
                <div class="col-6">
                    <h2>고지서 출력의뢰</h2>
                </div>
                <div class="col-6 text-right">
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>고지서 출력 시 출력업체에 출력 의뢰를 요청하는 화면입니다.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row no-gutters mt-3">
            <div class="col">
                <h5>출력 조건</h5>
                <h6>> 출력의뢰 가능 건수 : 총 <strong class="font-blue" id="prtCnt">${map.masCnt}</strong> 건</h6>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row no-gutters">
            <div class="col">
                <table class="table table-form">
                    <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">청구월</th>
                            <td class="col-md-4 col-sm-8 col-8 form-inline">
                                <select class="form-control" id="yearsBox" onchange="fnMasCnt()">
                                    <option>선택</option>
                                </select>
                                <span class="ml-1 mr-1">년</span>
                                <select class="form-control" id="monthBox" onchange="fnMasCnt()">
                                    <option>선택</option>
                                </select>
                                <span class="ml-1">월</span>
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">인쇄문구설정</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <select class="form-control w-75 mr-2" id="billgubn" name="billgubn">
                                    <option value="01">안내문구 1</option>
                                    <option value="02">안내문구 2</option>
                                    <option value="03">안내문구 3</option>
                                </select>
                                <button class="btn btn-sm btn-d-gray" onclick="window.location.href='/org/notiMgmt/notiConfig'">고지서설정</button>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">정렬순서(1차)</th>
                            <td class="col-md-4 col-sm-8 col-8 form-inline">
                                <select class="form-control w-75 mr-2" id="cusgubna" name="cusgubna">
                                    <option value="01">선택</option>
                                </select>
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">정렬순서(2차)</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <select class="form-control w-75 mr-2" id="cusgubnb" name="cusgubnb">
                                    <option value="01">선택</option>
                                </select>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row no-gutters mt-4">
            <div class="col">
                <h5>배송 정보</h5>
            </div>
        </div>
    </div>

    <div class="container" id="focus">
        <div class="row no-gutters">
            <div class="col">
                <table class="table table-form">
                    <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">수취인이름</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <input type="text" id="reqName" class="form-control" value="${info.reqName}">
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">수취인연락처</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <input type="text" id="reqHp" class="form-control" value="${info.reqHp}" onkeydown="onlyNumber(this)" maxlength="12" placeholder="'-' 제외">
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">배송주소</th>
                            <td class="col-md-10 col-sm-8 col-8 address-form-in-table">
                                <div class="zipcode mb-2">
                                    <input type="text" id="zipCode" class="form-control mr-2 w-50" value="${info.zipCode}" onkeydown="onlyNumber(this)" readonly>
                                    <button class="btn btn-sm btn-d-gray" onclick="execDaumPostcode()">우편번호검색</button>
                                </div>
                                <div class="address-lines w-100">
                                    <input type="text" id="address1" class="form-control mr-2" value="${info.address1}">
                                    <input type="text" id="address2" class="form-control" value="${info.address2}">
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row no-gutters">
            <div class="col guide-mention w-100 text-danger">※ 퀵 배송의 경우, 요청취소가 불가하며 15시 이전 접수건에 한해 당일 배송됩니다. (배송료 착불, 지역마다 상이)</div>
        </div>
    </div>


    <div class="container">
        <div class="row mt-4 mb-5">
            <div class="col text-center">
                <button class="btn btn-danger mr-lg-3" onclick="fn_billDlvr('N20002');">출력 의뢰 (퀵 배송)</button>
                <button class="btn btn-primary" onclick="fn_billDlvr('N20001');">출력 의뢰 (택배 배송)</button>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="search-result">
            <div class="row">
                <div class="table-responsive col mb-3">
                    <table class="table table-sm table-hover table-primary">
                        <colgroup>
                            <col width="52">
                            <col width="68">
                            <col width="100">
                            <col width="140">
                            <col width="100">
                            <col width="100">
                            <col width="310">
                            <col width="100">
                            <col width="100">
                            <col width="140">
                            <col width="100">
                            <col width="100">
                        </colgroup>

                        <thead>
                            <tr>
                                <th>
                                    <input class="form-check-input" type="checkbox" name="rowsAll" id="rowsAll" value="ALL">
                                    <label for="rowsAll"><span></span></label>
                                </th>
                                <th>NO</th>
                                <th>청구월</th>
                                <th>의뢰일자</th>
                                <th>출력건수</th>
                                <th>구분</th>
                                <th>인쇄문구</th>
                                <th>1차정렬</th>
                                <th>2차정렬</th>
                                <th class="border-r-e6">상태</th>
                                <th class="border-l-n">미리보기</th>
                            </tr>
                        </thead>

                        <tbody id="resultBody">
                            <tr>
                                <td colspan="11">[신청된 내역이 없습니다.]</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="row mb-2">
                <div class="col-6">
                    <button class="btn btn-sm btn-gray-outlined" onclick="fn_canclePrint();">선택취소</button>
                </div>
            </div>

            <jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false"/>
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
                    <h6>■ 출력의뢰 이용요금</h6>
                    <ul>
                        <li>장당 단가 : 50원 (VAT포함)</li>
                        <li>청구방법 : 관리수수료에 포함되어 세금계산서로 발행되며, 익월 10일 자동출금(비영업일인 경우 익영업일 출금)</li>
                    </ul>

                    <h6>■ 출력의뢰 배송료</h6>
                    <ul>
                        <li>택배배송비 : 착불</li>
                        <li>퀵배송비 : 착불, 지역마다 상이(서울 및 수도권지역 가능)</li>
                    </ul>

                    <h6>■ 출력의뢰 방법</h6>
                    <ul>
                        <li>출력조건에 해당 청구월 및 안내문구 선택</li>
                        <li>배송정보에 고지서 받는 곳 주소지와 담당자 이름, 연락처 작성 출력의뢰 버튼 클릭</li>
                        <li>(배송정보 연락처에 핸드폰번호를 입력한 경우 출력업체로 데이터가 전송되면 문자메시지로 알려드립니다.)</li>
                    </ul>

                    <h6>■ 출력의뢰 취소방법</h6>
                    <ul>
                        <li>출력취소를 원하는 대상 건 선택 후 '선택취소' 버튼 클릭</li>
                    </ul>

                    <h6>■ 처리 및 배송관련 안내</h6>
                    <ul>
                        <li>택배발송의 경우 오전 10시 이전 요청건에 한해 당일 처리완료되며, 오전 10시 이후 요청건은 익영업일 오전에 처리완료됩니다. </li>
                        <li>처리완료된 의뢰 건에 대해 택배 배송 소요 시간은 주말/공휴일을 제외한 영업일 기준으로 평균 3일정도 소요됩니다. 단, 택배사 사정에 따라 차이가 있을 수 있습니다.</li>
                        <li>퀵배송의 경우 15시 이전 요청건에 한해 당일 퀵배송되며, 15시 이후 요청건은 익영업일 오전에 배송됩니다.</li>
                        <li>처리완료는 출력업체에 데이터가 전송된 상태이며, 출력물이 발송완료된 상태는 아니오니 참고바랍니다.</li>
                    </ul>

                    <h6>■ 출력의뢰를 통한 고지서 출력가능 글자수 안내</h6>
                    <ul>
                        <li>수납기관명 : 15자 제한</li>
                        <li>고지서 제목명 : 10자 제한</li>
                        <li>납부자명 : 9.5자 제한</li>
                        <li>납부자호칭 : 5자 제한</li>
                        <li>고객구분제목명, 고객구분명 : (고객구분1 + 고객구분2 + 고객구분3 + 고객구분4)의 고객구분제목명과 고객구분명의 총길이 합이 40자 제한</li>
                        <li>청구항목명 : 15자 제한</li>
                        <li>청구항목비고란 : 25자 제한</li>
                        <li>이외 출력가능 글자수는 [고지서조회/출력 > 고지서인쇄]와 동일</li>
                    </ul>

                    <h6 class="text-danger">■ 주의사항</h6>
                    <ul>
                        <li class="text-danger">출력물은 고객 개개인에게 배송되지 않습니다.</li>
                        <li class="text-danger">'요청' 상태인 경우만 출력의뢰 취소가 가능합니다. 단, 퀵배송의 경우 요청취소가 불가하오니 주의하시기 바랍니다.</li>
                        <li class="text-danger">고지서 안내문구는 최대 10줄까지 작성이 가능합니다. 등록하신 안내문구를 수정 후 이용해 주시기 바랍니다.</li>
                        <li class="text-danger">출력업체를 통한 출력의뢰의 경우 미리보기상의 폰트, 특수기호 등 세세한 부분까지 동일하게 출력되지 않을 수 있습니다.</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>
