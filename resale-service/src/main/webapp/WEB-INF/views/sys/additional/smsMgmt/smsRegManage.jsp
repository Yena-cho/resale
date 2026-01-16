<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-6";
    var twoDepth = "adm-sub-16";
</script>

<script type="text/javascript">
    var cuPage = 1;

    function fnSearch(page) {

        var endday = $("#endday").val();
        var startday = $("#startday").val();

        endday = delDotDate(endday);
        startday = delDotDate(startday);

        if (page == null || page == 'undefined') {
            cuPage = "";
            cuPage = 1;
        } else {
            cuPage = page;
        }

        if (getCurrentDate() < endday || getCurrentDate() < startday) {
            swal({
                type: 'info',
                text: "현재일보다 이후를 조회할 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (endday < startday) {
            swal({
                type: 'info',
                text: "조회시작년월이 더 클 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        var url = "/sys/addServiceMgmt/smsRegManageAjax";
        var param = {
            tMonth: endday, //조회 종료년월
            fMonth: startday, //조회 시작년월
            chaName: $("#chaName").val(),
            chaCd: $("#chaCd").val(),
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val(),
            searchOrderBy: $('#searchOrderBy option:selected').val(),
            searchGb: $('#searchGb option:selected').val(), //검색구분
            searchValue: $('#searchValue').val(), //검색구분 텍스트값
            statusCheck: $("input[name=statusCheck]:checked").val()
        };
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
            	//console.log("list=\n" + JSON.stringify(result));
                if (result.retCode == "0000") {
                    fnGrid(result, 'resultBody'); // 현재 데이터로 셋팅
                    sysajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'error',
                        text: result.retCode,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });
    }

    //데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#waitCount').text(result.waitCount);
        $('#totalCount').text(result.count);

        if (result.count <= 0) {
            str += '<tr><td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + isNotNull(v.rn) + '</td>';
                str += '<td>' + isNotNull(v.day) + '</td>';
                str += '<td>' + isNotNull(v.chaCd) + '</td>';
                str += '<td>' + isNotNull(v.chaName) + '</td>';
                str += '<td>' + isNotNull(v.chaStatus) + '</td>';
                str += '<td>' + isNotNull(v.loginId) + '</td>';
                str += '<td>' + isNotNull(v.owner) + '</td>';
                str += '<td>' + isNotNull(v.chaOffNo) + '</td>';
                str += '<td>' + isNotNull(v.chrName) + '</td>';
                str += '<td>' + isNotNull(v.chrHp) + '</td>';
                if (isNotNull(v.fileName) != '') {
                str += '<td><input type="button" class="btn btn-xs btn-info" value="보기" onclick=openDocument("' + i + '")><input type="hidden" name="fileName" value="' + v.fileName + '"/><input type="hidden" name="filechacd" value="' + v.chaCd + '"/>'
                str += '<input type="button" class="btn btn-xs btn-warning" value="삭제" onclick=deleteSmsreg("' + i + '")><input type="hidden" name="fileno" value="' + v.no + '"/></td>';
                }else{
                str += '<td><input type="button" class="btn btn-xs btn-info" value="재등록" onclick=updateSmsreg("' + i + '")><input type="hidden" name="fileno" value="' + v.no + '"/><input type="hidden" name="filechacd" value="' + v.chaCd + '"/></td>';
                }
                str += '<td><button type="button" class="btn btn-xs btn-info" onclick=openCallerNum("' + v.no + '")>보기</button></td>';
                if (v.useSmsYn == 'W' && isNotNull(v.fileName) != '') {
                    str += '<td><button type="button" class="btn btn-xs btn-success" onclick=smsComplete(' + v.chaCd + ','+ v.no +')>완료</button></td>';
                }else if(v.useSmsYn == 'W' && isNotNull(v.fileName) == ''){
                	str += '<td><button type="button" class="btn btn-xs btn-success" onclick=smsComplete("' + v.chaCd + '") disabled="disabled">완료</button></td>';
                }else if (v.useSmsYn == 'Y') {
                    str += '<td>' + nullValueChange(v.day1) + '<button type="button" class="btn btn-xs btn-danger" onclick=smsIncomplete(' + v.chaCd + ','+ v.no +')>해지</button></td>';
                }
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    function isNotNull(v) {
        if (typeof v == "undefined" || v == null || v == "") {

            return "";

        } else {
            return v;
        }
    }

    //날짜 포맷삭제
    function delDotDate(val) {

        val = val.replace(/\./g, "");

        return val;
    }

    // 페이징 버튼
    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색
        } else {
            $('#pageNo').val(page);
            fnSearch(page);
        }
    }

    //화면보여주는 개수 변경
    function pageChange() {
        fnSearch(cuPage);
    }

    //정렬 변경
    function arrayChange() {
        fnSearch(cuPage);
    }

    //제출서류 보기
    function openDocument(idx) {
        /* var url = "/resources/upload/"+fileName; */
        var fileName = $('input[name=fileName]:eq(' + idx + ')').val();
        var chaCd = $('input[name=filechacd]:eq(' + idx + ')').val();
        var no = $('input[name=fileno]:eq(' + idx + ')').val();
       //var OpenWindow = window.open('', '_blank', 'width=800, height=1100, menubars=no, scrollbars=auto');
       var OpenWindow = window.open('', '_blank', 'width=800, height=800, menubars=no, scrollbars=auto');
  
       OpenWindow.document.write("<img src='/common/showFile?fileTy=upload&no=" + no + "&chaCd=" + chaCd + "&fileName=a' width='780px'>");
       OpenWindow.document.location = "#";
    }

    function deleteSmsreg(idx) {
        var chaCd = $('input[name=filechacd]:eq(' + idx + ')').val();
        var no = $('input[name=fileno]:eq(' + idx + ')').val();
        swal({
            type: 'question',
            html: "문자서비스 신청서를 삭제하시겠습니까? 서비스이용이 제한 됩니다.",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
            	var url = "sys/addServiceMgmt/deleteSmsCertificate";
            	var param = {
            			chaCd : chaCd,
            			no : no
            		};
                $.ajax({
                    type: "post",
                    async : true,
                    url: url,
                    contentType : "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '문자서비스 신청서가 삭제되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                	fnSearch();
                                }
                            });
                        } else {
                            swal({
                                type: 'warning',
                                text: '처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                });
            }
        });
    }
    
    function updateSmsreg(idx) {
        var fileName = $('input[name=fileName]:eq(' + idx + ')').val();
        var chaCd = $('input[name=filechacd]:eq(' + idx + ')').val();
        var no = $('input[name=fileno]:eq(' + idx + ')').val();
        fn_smsServiceInsPageAdm(chaCd, no);
    }
    
    function fn_smsServiceInsPageAdm(chaCd, no) {
    	
    	var url = "/org/notiMgmt/smsCertificate";
    	var param = {
    			chaCd : chaCd
    	};

    	$.ajax({
    		type : "post",
    		async : true,
    		url : url,
    		contentType : "application/json; charset=utf-8",
    		data : JSON.stringify(param),
    		success: function(data){
    			$('#chaName').text(data.map.chaName);
    			$('#ownerTel').text(data.map.ownerTel);
    			$('#fromNumber').val('');
    			$('#addNum').hide();
    			$("#fileNm").val('');
    			$("#file-upload").val('');
    			$("#fileno").val(no);
    			$("#regChacd").val(chaCd);
    		}
    	});
    	
    	$('#reg-sms-service').modal({backdrop:'static', keyboard:false});
    }
    
    //발신번호 보기
    function openCallerNum(no) {

        var url = "/sys/addServiceMgmt/getCallerNum";

        var param = {
            no: no
        };
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnModalGrid(result, 'callerTable'); 
                    $("#popup-caller-num-list").modal({
                        backdrop: 'static',
                        keyboard: false
                    });
                } else {
                    swal({
                        type: 'error',
                        text: result.retCode,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });

    }

    //데이터 새로고침
    function fnModalGrid(result, obj) {
        var str = '';
        $("#modalNo").val(result.no);
        $("#modalChaCd").val(result.data.chaCd);
        var contents = isNotNull(result.data.smsSendTel).split(',');

        for (var i = 0; i < 5; i++) {
            str += '<tr>';
            str += '<td>' + (i + 1) + '</td>';
            str += '<td><input type="text" class="form-control" name="fromNumber" onkeydown="onlyNumber(this)" maxlength="12" value="' + isNotNull(contents[i]) + '"></td>';
            str += '</tr>';
        }
        $('#' + obj).html(str);
    }

    //파일저장
    function fn_fileSave() {
        if ($('#totalCount').text() <= 0) {
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
                $('#fStartDate').val(delDotDate($('#startday').val()));
                $('#fEndDate').val(delDotDate($('#endday').val()));
                $('#fChaCd').val($('#chaCd').val());
                $('#fChaName').val($('#chaName').val());
                $('#fSearchOrderBy').val($('#searchOrderBy option:selected').val());
                $('#fStatusCheck').val($("input[name=statusCheck]:checked").val());
                $('#fSearchGb').val($('#searchGb option:selected').val());
                $('#fSearchValue').val($('#searchValue').val());
                // 다운로드
                $('#fileForm').submit();
            }
        });
    }

    function smsComplete(val, no) {
        swal({
            type: 'question',
            html: "해당 문자신청을 완료하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var url = "/sys/addServiceMgmt/updateUseSmsYn";

                var param = {
                    chaCd: val,
                    no : no
                };
                $.ajax({
                    type: "post",
                    url: url,
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            location.href = "/sys/addServiceMgmt/smsRegManage";
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
        });
    }
    
    function smsIncomplete(val, no) {
        swal({
            type: 'question',
            html: "해당 기관의 문자서비스를 해지 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var url = "/sys/addServiceMgmt/deleteUseSmsYn";

                var param = {
                    chaCd: val,
                    no : no
                };
                $.ajax({
                    type: "post",
                    url: url,
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            location.href = "/sys/addServiceMgmt/smsRegManage";
                        } else {
                            swal({
                                type: 'error',
                                text: result.retCode,
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                });
            }
        });
    }

    function fn_enterkey() {
    	fnSearch();
    }
</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/addServiceMgmt/smsRegExcelDown">
    <input type="hidden" id="fStartDate" name="fStartDate" value=""/>
    <input type="hidden" id="fEndDate" name="fEndDate" value=""/>
    <input type="hidden" id="fChaCd" name="fChaCd" value=""/>
    <input type="hidden" id="fChaName" name="fChaName" value=""/>
    <input type="hidden" id="fSearchOrderBy" name="fSearchOrderBy" value=""/>
    <input type="hidden" id="fStatusCheck" name="fStatusCheck" value=""/>
    <input type="hidden" id="fSearchGb" name="fSearchGb" value=""/>
    <input type="hidden" id="fSearchValue" name="fSearchValue" value=""/>
</form>

<input type="hidden" id="pageNo" name="pageNo" value="1">

</div> 
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>문자서비스신청관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>부가서비스관리</a>
            </li>
            <li class="active">
                <strong>문자서비스신청관리</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 부가서비스 신청내역을 관리하는 화면입니다.</p>
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
                        <form>
                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">신청일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" name="startday" id="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" name="endday" id="endday" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth"  id="btnSetMonth0"  onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth1"  onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth6"  onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="radio" id="smsReg1" name="statusCheck" value="ALL" <c:if test="${map.statusCheck == 'ALL'}">checked</c:if>>
                                                <label for="smsReg1"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="radio" id="smsReg2" name="statusCheck" value="W" <c:if test="${map.statusCheck == 'W'}">checked</c:if>>
                                                <label for="smsReg2"> 등록대기 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="radio" id="smsReg3" name="statusCheck" value="Y" <c:if test="${map.statusCheck == 'Y'}">checked</c:if>>
                                                <label for="smsReg3"> 등록완료 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chaCd" maxlength="50" onkeypress="if(event.keyCode==13) {fn_enterkey();}">
                                            <span class="input-group-btn">
                                                <button class="btn btn-primary btn-lookup-collecter no-margins" type="button">기관검색</button>
                                            </span>
                                        </div>

                                        <div class="col-sm-4" style="display: none;">
                                            <div class="input-group">
                                                <input type="text" class="form-control" id="chaname" onkeypress="if(event.keyCode==13) {fn_enterkey();}">
                                            </div>
                                        </div><!-- 삭제후 검색 버튼으로 변경 요청 20180619 안진호 -->
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
											<span class="input-group-select">
                                                <select class="form-control" id="searchGb">
                                                    <option value="loginId">로그인아이디</option>
                                                    <option value="owner">대표자명</option>
                                                    <option value="chaOffNo">사업자번호</option>
                                                    <option value="chrName">담당자명</option>
                                                    <option value="chrTelNo">담당자핸드폰번호</option>
                                                </select>
                                            </span>
                                            <input type="text" class="form-control" id="searchValue" name="searchValue" maxlength="50">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fnSearch();">조회</button>
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
                            <span class="m-r-sm">대기 건수 : <strong class="text-danger" id="waitCount">${map.waitCount}</strong> 건</span>
                            <span> | </span>
                            <span class="m-l-sm">전체 건수 : <strong class="text-success" id="totalCount">${map.count}</strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="arrayChange();">
                                <option value="reqDt">신청일순 정렬</option>
                                <option value="chaCd">기관코드순 정렬</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
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
                                <%--<colgroup>--%>
                                    <%--<col width="50">--%>
                                    <%--<col width="170">--%>
                                    <%--<col width="150">--%>
                                    <%--<col width="480">--%>
                                    <%--<col width="100">--%>
                                    <%--<col width="200">--%>
                                    <%--<col width="180">--%>
                                    <%--<col width="80">--%>
                                    <%--<col width="80">--%>
                                    <%--<col width="120">--%>
                                <%--</colgroup>--%>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>신청일자</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>업종</th>
                                        <th>로그인아이디</th>
                                        <th>대표자명</th>
                                        <th>사업자번호</th>
                                        <th>담당자명</th>
                                        <th>담당자핸드폰번호</th>
                                        <th>제출서류</th>
                                        <th>발신번호</th>
                                        <th>등록일</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <c:choose>
                                        <c:when test="${map.count > 0}">
                                            <c:forEach var="row" items="${map.list}" varStatus="counti">
                                                <tr>
                                                    <td>${row.rn}</td>
                                                    <td>${row.day}</td>
                                                    <td>${row.chaCd}</td>
                                                    <td>${row.chaName}</td>
                                                    <td>${row.chaStatus}</td>
                                                    <td>${row.loginId}</td>
                                                    <td>${row.owner}</td>
                                                    <td>${row.chaOffNo}</td>
                                                    <td>${row.chrName}</td>
                                                    <td>${row.chrTelNo }</td>
                                                    <td>
                                                        <button type="button" class="btn btn-xs btn-info"
                                                                onclick="openDocument('${counti.count-1}');">보기
                                                        </button>
                                                        <input type="hidden" name="fileName" value="${row.fileName}"/>
                                                        <input type="hidden" name="filechacd" value="${row.chaCd}"/></td>
                                                    <td>
                                                        <button type="button" class="btn btn-xs btn-info"
                                                                onclick="openCallerNum('${row.no}');">보기
                                                        </button>
                                                    </td>
                                                    <c:if test="${row.useSmsYn == 'W'}">
                                                        <td>
                                                            <button type="button" class="btn btn-xs btn-success"
                                                                    onclick="smsComplete('${row.chaCd}','${row.no}');">완료
                                                            </button>
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${row.useSmsYn == 'Y'}">
                                                        <td>${row.day1}</td>
                                                    </c:if>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="13" style="text-align: center;">
                                                    [조회된 내역이 없습니다.]
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>

                            <input type="hidden" id="uploadPath" value="${uploadPath }">
                        </div>
                    </div>
                </div>

                <jsp:include page="/WEB-INF/views/include/sysPaging.jsp" flush="false"/>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<!-- 등록 발신번호 상세보기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/caller-num-list.jsp" flush="false"/>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<!-- 문자서비스 신청서 재등록 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/reg-sms-service.jsp" flush="false"/>

<script type="text/javascript">
    $(document).ready(function () {
        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($('#chaCd').val());
            $("#popChaname").val('');
            $('#totCntLookupCollector').text(0);
            $("#ResultBodyCollector").html('');
            $("#ModalPageAreaCollector").html('');
        });

        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            forceParse: false,
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
        setMonthTerm(1);
        fnSearch();
    });
</script>
