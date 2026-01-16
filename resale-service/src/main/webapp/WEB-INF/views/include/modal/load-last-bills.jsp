<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    // 항목 초기화
    function fn_clear() {
        $('#beMonth').val('');
        $('#afYear').val('');
        $('#useCnt').hide();
        $('#beYear').attr('disabled', 'true');
        $('#beMonth').attr('disabled', 'true');
        $('#afYear').attr('disabled', 'true');
        $('#afMonth').attr('disabled', 'true');
    }

    // 이전청구년도 불러오기
    function fn_beforeYearSearch() {
        var url = "/org/claimMgmt/beforeMasYear";
        var param = {};
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                fn_clear();
                fn_masYear(data, 'beYear');
                fn_afYear();

				$('#afYear').val($('#beYear').val());
            }
        });
    }

    //이전청구월 불러오기
    function fn_beforeMonthSearch() {
        var url = "/org/claimMgmt/beforeMasMonth";
        var param = {
            year: $('#beYear option:selected').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                fn_masMonth(data, 'beMonth');
                fn_useClaimCnt();
            }
        });
    }

    // 이전청구년도
    function fn_masYear(data, obj) {
        var str;
        $.each(data.list, function (k, v) {
            str += '<option value="' + v.year + '">' + v.year + '</option>';
        });
        $('#' + obj).html(str);

        fn_beforeMonthSearch();
    }

    //이전청구월
    function fn_masMonth(data, obj) {
        var str;
        $.each(data.list, function (k, v) {
            str += '<option value="' + v.month + '">' + v.month + '</option>';
        });
        $('#' + obj).html(str);

        $("#beMonth option:last").prop("selected", "selected");

        var beYYYY = $('#beYear').val();
        var beMMMM = $('#beMonth').val();
        var beDate = moment(beYYYY + "-" + beMMMM).format('YYYYMM');
        var afYYYYMM = moment(beDate, 'YYYYMM').add(1, 'month').format('YYYYMM');
        var afYYYY = moment(afYYYYMM, 'YYYYMM').format('YYYY');
        var afMMMM = moment(afYYYYMM, 'YYYYMM').format('MM');

        $('#afMonth').val(afMMMM);
        $('#afYear').val(afYYYY);
    }

    // 이전 년도 option 삭제
    function fn_afYear() {
        $("#afYear").find("option").each(function () {
            if ($(this).val() < getYear()) {
                $(this).remove();
            }
        });
    }

    // 청구대상 추가
    function fn_copyClaimReg() {
        swal({
            type: 'question',
            html: "이전청구정보를 적용하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var url = "/org/claimMgmt/copyClaimInsert";
                var param = {
                    month: $('#beYear option:selected').val() + $('#beMonth option:selected').val(),
                    masMonth: $('#afYear option:selected').val() + $('#afMonth option:selected').val()
                };
                $.ajax({
                    type: "post",
                    async: true,
                    url: url,
                    contentType: "application/json; charset=UTF-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        swal({
                            type: 'success',
                            text: '취소 청구건을 제외하고 청구대상에 추가되었습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function (result) {
                            if (result.value) {
                                //location.href = "/org/claimMgmt/claimReg";
                                $('#load-last-bills').modal('hide');
                                fn_ClaimListCall();
                            }
                        });
                    },
                    error: function (result) {
                        swal({
                            type: 'error',
                            text: '등록 실패하였습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        })
                    }
                });
            }
        });
    }

    // 선택한 청구월의 데이터 건수 확인
    function fn_useClaimCnt() {
        var url = "/org/claimMgmt/useClaimCnt";
        var param = {
            month: $('#beYear').val() + $('#beMonth').val(),
            masMonth: $('#afYear').val() + $('#afMonth').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                $('#useCnt').show();
                $('#useCnt').text('(대상건수 : ' + data.cnt + '건)');
            }
        });
    }
</script>

<jsp:include page="/WEB-INF/views/include/popHeader.jsp" />

<div class="modal fade" id="load-last-bills" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">이전 청구정보 불러오기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div id="page-description">
                        <div class="row">
                            <div class="col-12">
                                <p>
									- 이전 청구내역을 복사하여 새로운 청구목록을 생성합니다.<br/>
									- 복사할 이전 청구월에 취소된 청구 및 삭제된 고객 등의 청구정보는 불러오지 않습니다.<br/>
									- 납부기한 및 고지서용 표시마감일 변경은 청구대상 추가 이후 청구정보 일괄변경에서 일괄변경 가능합니다.<br/>
									- 변경청구월은 이전청구 월 다음월로 변경이 가능합니다.<br/>
									<strong class="text-danger"> - 전월 청구건 중 한 고객에게 여러 개의 청구서가 있는경우, 변경 청구 월에 여러 개의 청구서가 생성되니 참고바랍니다. </strong>
								</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container">
                    <div class="search-box">
	                    <div class="row mb-1 year-month-dropdown">
							<div class="col col-md-2">
								<span class="text-danger">*</span> 이전 청구월
							</div>
	                        <div class="col col-md-10 col-12 form-inline">
	                            <select class="form-control" id="beYear" onchange="fn_beforeMonthSearch();"></select>
	                            <div class="float-left ml-1 mr-3">년</div>
									<select class="form-control" id="beMonth" onchange="fn_useClaimCnt()">
	                                <option value="">선택</option>
	                            </select>
								<div class="float-left ml-1">월</div>

								<span class="mr-auto">의 청구정보를</span>
							</div>
	                    </div>
	                     <div class="row year-month-dropdown">
							 <div class="col col-md-2">
								 <span class="text-danger">*</span> 변경 청구월
							 </div>
							 <div class="col col-md-10 col-12 form-inline">
	                            <select class="form-control" id="afYear" name="afYear"></select>
								 <div class="float-left ml-1 mr-3">년</div>
	                            <select class="form-control" id="afMonth" onchange="fn_useClaimCnt()"></select>
								 <div class="float-left ml-1">월</div>

								 <span class="mr-auto">의 청구정보로 변경하겠습니다.</span>
								 <span class="font-red" id="useCnt" style="display: none;"></span>
							</div>
	                    </div>
                    </div>

					<div class="modal-footer">
						<button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal" onclick="fn_ClaimListCall();">취소</button>
						<button class="btn btn-primary" onclick="fn_copyClaimReg();">청구대상 추가</button>
					</div>
                </div>
            </div>
        </div>
    </div>
</div>
