<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script>
    function updateCust() {
        var mday1 = replaceDot($("#modal-sys-counselling-dialog #mday1").val());
        var mdata3 = '';

        if ($("#modal-sys-counselling-dialog #mdata2 option:selected").val() == 01) {
            mdata3 = "사이트이용";
        } else if ($("#modal-sys-counselling-dialog #mdata2 option:selected").val() == 02) {
            mdata3 = "증명서 발급";
        } else if ($("#modal-sys-counselling-dialog #mdata2 option:selected").val() == 03) {
            mdata3 = "결제";
        } else if ($("#modal-sys-counselling-dialog #mdata2 option:selected").val() == 04) {
            mdata3 = "오류 및 불편사항";
        } else if ($("#modal-sys-counselling-dialog #mdata2 option:selected").val() == 05) {
            mdata3 = "기타";
        } else {
            mdata3 = "고지/수납";
        }

        var mdata1 = $("#modal-sys-counselling-dialog #mdata1").val();
        if (mdata1.length == 11) {
            var kw = mdata1.substring(0, 3) + "-" + mdata1.substring(3, 7) + "-" + mdata1.substring(7, 11);
            mdata1 = kw;
        }

        var emplo = $("#modal-sys-counselling-dialog #mdata5").val();
        var mtitle = $("#modal-sys-counselling-dialog #mtitle").val();
        var mcontents = $("#modal-sys-counselling-dialog #mcontents").val();
        if (emplo == null || emplo == "") {
            swal({
                type: 'info',
                text: '상담직원명을 입력해 주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if ($("#modal-sys-counselling-dialog #mwho").val() == "1" &&
            ($("#modal-sys-counselling-dialog #mCode").val() == null || $("#modal-sys-counselling-dialog #mCode").val() == "")) {
            swal({
                type: 'info',
                text: '기관코드를 입력해 주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if (mtitle == null || mtitle == "") {
            swal({
                type: 'info',
                text: '문의내용을 입력해 주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if (mcontents == null || mcontents == "") {
            swal({
                type: 'info',
                text: '상담내용을 입력해 주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if (mday1 == null || mday1 == "") {
            swal({
                type: 'info',
                text: '상담일시를 입력해 주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if ($("#modal-sys-counselling-dialog #mwriter").val() == null || $("#modal-sys-counselling-dialog #mwriter").val() == "") {
            swal({
                type: 'info',
                text: '신청자명을 입력해 주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else if ($("#modal-sys-counselling-dialog #mdata1").val() == null || $("#modal-sys-counselling-dialog #mdata1").val() == "") {
            swal({
                type: 'info',
                text: '신청자 연락처를 입력해 주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else {

            var param = {
                no: $("#modal-sys-counselling-dialog #mno").val(),
                //userClass : , //고객분류
                data2: $("#modal-sys-counselling-dialog #mdata2 option:selected").val(), //예약사유코드
                data3: mdata3, //예약사유명
                data5: emplo, //상담직원
                day1: mday1, //상담일시
                id: $("#modal-sys-counselling-dialog #mCode").val(), //기관코드
                data4: $("#modal-sys-counselling-dialog #mstatus option:selected").val(), //상태
                writer: $("#modal-sys-counselling-dialog #mwriter").val(), //신청자명
                data1: mdata1, //신청자 연락처
                title: mtitle, //문의내용
                contents: mcontents //답변내용
            };

            $.ajax({
                type: "POST",
                async: true,
                url: "/sys/updateCust",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (result) {
                    if (result.retCode == "0000") {
                        $("#modal-sys-counselling-dialog").modal("hide");
                        search();
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
    }
</script>

<div class="inmodal modal fade" id="modal-sys-counselling-dialog" tabindex="-1" role="dialog" aria-labelledby="regPayer"
     aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="mdtitle">전화상담 진행상황 수정</h4>
            </div>
            <div class="modal-body">
                <form>
                    <div class="row form-group">
                        <div class="col col-md-6 col-12">
                            <label class="control-label">고객분류</label>
                            <select class="form-control" id="mwho" >
                                <option value="1">이용기관</option>
                                <option value="2">납부자/신규</option>
                            </select>
                        </div>
                        <div class="col col-md-6 col-12">
                            <label class="control-label">예약사유</label>
                            <select class="form-control" id="mdata2" >
                                <option value="01">사이트이용</option>
                                <option value="02">증명서 발급</option>
                                <option value="03">결제</option>
                                <option value="04">오류 및 불편사항</option>
                                <option value="05">기타</option>
                                <option value="06">고지/수납</option>
                            </select>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col col-md-6 col-12">
                            <label class="control-label">상담직원</label>
                            <input type="text" class="form-control" placeholder="" value="" id="mdata5" maxlength="10">
                        </div>
                        <div class="col col-md-6 col-12">
                            <label class="control-label">상담일시</label>
                            <div class="date">
                                <input type="text" class="form-control" value="" id="mday1" disabled="disabled">
                            </div>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col col-md-6 col-12">
                            <label class="control-label">기관명/기관코드</label>
                            <div class="form-inline">
                                <input type="text" class="form-control" placeholder="" value="" id="mid" disabled="disabled" style="width: 65%; margin-right: 6px;"/>
                                <input type="text" class="form-control" placeholder="" value="" id="mCode" style="width: calc( 35% - 10px );"/>
                            </div>
                        </div>
                        <div class="col col-md-6 col-12">
                            <label class="control-label">진행상태</label>
                            <select class="form-control" id="mstatus">
                                <option value="1">대기</option>
                                <option value="2">진행중</option>
                                <option value="3">완료</option>
                            </select>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col col-md-6 col-12">
                            <label class="control-label">신청자명(수신자명)</label>
                            <input type="text" class="form-control" placeholder="" value="" id="mwriter" maxlength="10">
                        </div>
                        <div class="col col-md-6 col-12">
                            <label class="control-label">신청자 연락처</label>
                            <input type="text" class="form-control" placeholder="" value="" id="mdata1" max="15">
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col col-xs-12">
                            <label class="control-label">상담 문의내용</label>
                            <textarea class="form-control" rows="8" id="mtitle" maxlength="500"></textarea>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col col-xs-12">
                            <label class="control-label">상담 답변내용</label>
                            <textarea class="form-control" rows="8" id="mcontents" maxlength="500"></textarea>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" onclick="updateCust()" id="msavebtn">저장</button>
                <input type="hidden" value="" name="" id="mno">
            </div>
        </div>
    </div>
</div>