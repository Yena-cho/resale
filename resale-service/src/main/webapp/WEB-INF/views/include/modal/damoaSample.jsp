<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script src="/assets/js/pdfobject.min.js"></script>

<script type="text/javascript">
function fn_image(fileName) {
	var options = {
	    pdfOpenParams: {
	        navpanes: 0,
	        toolbar: 0,
	        statusbar: 0,
	        view: "FitV",
	        pagemode: "thumbs",
	        page: 2
	    },
	    forcePDFJS: true,
	    PDFJS_URL: "/assets/pdf/web/viewer.html"
	};
	 
	var myPDF = PDFObject.embed("/resources/pdf/sample/" + fileName, "#pdf", options);
}
</script>

<div class="modal fade" id="damoaSample" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true" style="padding-right: 0 !important;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="sampleId"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>

            <div class="modal-body">
                <div id="popup1">
                    <div class="info-container tab-content mt-3 mb-3">
                        <div class="tab-pane active">
                            <div class="row sample-info">
                                <div class="col">
                                    <img src="/assets/imgs/common/print-sample2.png" class="float-left col-md-8 mb-3">
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">1. 기관 로고 등록</div>
                                            <div class="card-body">
                                                <p class="card-text">마이페이지 > 고지상세설정 > 고지서설정 메뉴<br/><strong>기관 로고 이미지를 선택하여 등록!<br/>
                                                - 로고 이미지 파일의 최소규격은 150px(가로) * 30px(세로)로 파일형식은 jpg만 지원</strong></p>
                                            </div>
                                        </div>
                                        <div class="card border-light mb-3">
                                            <div class="card-header">2. 고지서 안내문 등록</div>
                                            <div class="card-body">
                                                <p class="card-text">Step 1 마이페이지 > 고지상세설정 > 고지서설정 메뉴<br/><strong>안내명 / 안내문구를 등록!</strong></p>
                                                <p class="card-text">Step 2 고지관리 > 고지서설정 메뉴<br/><strong>등록된 안내문구를 선택!</strong></p>
                                            </div>
                                        </div>
                                        <div class="card border-light mb-3">
                                            <div class="card-header">3. 고객구분 노출여부 선택</div>
                                            <div class="card-body">
                                                <p class="card-text">마이페이지 > 서비스설정 > 고객구분 메뉴<br/><strong>고객구분 고지서출력 여부 선택하여 노출 여부 저장!</strong></p>
                                            </div>
                                        </div>
                                        <div class="card border-light mb-3">
                                            <div class="card-header">4. 출력설정 및 정보 등록</div>
                                            <div class="card-body">
                                                <p class="card-text">고지관리 > 고지서설정 메뉴<br/><strong>안내명 / 납부자호칭 / 안내문구선택 / 수납기관명 / 수납기관 연락처 선택하여 등록!</strong></p>
                                            </div>
                                        </div>
                                        <div class="card border-light mb-3">
                                            <div class="card-header">5. 추가 전달사항</div>
                                            <div class="card-body">
                                                <p class="card-text">청구관리 > 청구등록 > 개별/대량청구등록 메뉴<br/><strong>청구항목에 대한 추가 안내문 작성을 원할 경우<br/>청구 등록 시 하단 '고지서 출력용 전달사항' 입력 시 노출!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="damoaSample2" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true" style="padding-right: 0 !important;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="sampleId2"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>

            <div class="modal-body">
                <div id="popup2">
                    <div class="info-container">
                        <div class="row">
                            <div class="col">
                                <div class="tab-selecter type-3">
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li class="nav-item"><a class="nav-link mobile-link-padding active show" data-toggle="tab" href="#step1">step 1</a></li>
                                        <li class="nav-item"><a class="nav-link mobile-link-padding" data-toggle="tab" href="#step2">step 2</a></li>
                                        <li class="nav-item"><a class="nav-link mobile-link-padding" data-toggle="tab" href="#step3">step 3</a></li>
                                        <li class="nav-item"><a class="nav-link mobile-link-padding" data-toggle="tab" href="#step4">step 4</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="info-container tab-content mt-3 mb-3">
                        <div id="step1" class="tab-pane active">
                            <div class="row sample-info">
                                <div class="col">
                                    <img src="/assets/imgs/common/SMS-info1.png" class="float-left col-md-8 mb-3">
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">1. 고지발송 대상조회</div>
                                            <div class="card-body">
                                                <p class="card-text">고지관리 > 고지서 조회/출력 메뉴 선택 후<br/><strong>문자메시지 발송을 원하는 대상의 청구월을 선택하여 조회버튼 클릭!</strong></p>
                                            </div>
                                        </div>
                                        <div class="card border-light mb-3">
                                            <div class="card-header">2. 고지발송 대상선택</div>
                                            <div class="card-body">
                                                <p class="card-text">청구가 발생된 문자메시지 수신 대상을 선택 후<br/><strong>'문자메시지고지' 버튼을 클릭하여 화면 호출!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="step2" class="tab-pane">
                            <div class="row sample-info">
                                <div class="col">
                                    <img src="/assets/imgs/common/SMS-info2.png" class="float-left col-md-8 mb-3">
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">1. 안내문 유형 선택</div>
                                            <div class="card-body">
                                                <p class="card-text">발송 유형별 안내문구 버튼 클릭 후<br/><strong>선택된 안내문구 내용이 미리보기 화면에 노출!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">2. 저장된 안내문 확인</div>
                                            <div class="card-body">
                                                <p class="card-text">마이페이지 > 고지상세설정 메뉴 선택 후<br/><strong>'문자메시지'화면에서 저장된 안내문 노출!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">3. 발신/수신 번호 선택</div>
                                            <div class="card-body">
                                                <p class="card-text">문자서비스 신청 시 등록된 발신번호 선택 후<br/><strong>문자메시지 수신 대상 선택!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">4. 메시지 발송</div>
                                            <div class="card-body">
                                                <p class="card-text">선택 된 납부 고객에게<br/><strong>'메시지 발송' 버튼을 클릭하여 발송!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="step3" class="tab-pane">
                            <div class="row sample-info">
                                <div class="col">
                                    <img src="/assets/imgs/common/SMS-info3.png" class="float-left col-md-8 mb-3">
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">1. 안내문 직접 입력</div>
                                            <div class="card-body">
                                                <p class="card-text">하단 '직접입력' 버튼 선택 후<br/><strong>발송을 원하는 입력 문구를 자유롭게 입력!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">2. 안내문 과금 정책</div>
                                            <div class="card-body">
                                                <p class="card-text">입력된 안내문의 길이에 따른 발송요금 변경<br/><strong>80byte 미만일 경우 '건당 20원',<br/>80byte 이상일 경우 '건당 40원' 부과!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">3. 발송 대상 선택/삭제</div>
                                            <div class="card-body">
                                                <p class="card-text">선택된 수신 대상 확인 및 삭제<br/><strong>간편한 발송 대상을 확인, 중복제거 기능을 이용해 발송 실수를 최소화!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="step4" class="tab-pane">
                            <div class="row sample-info">
                                <div class="col">
                                    <img src="/assets/imgs/common/SMS-info4.png" class="float-left col-md-8 mb-3">
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">1. 발송내역 확인</div>
                                            <div class="card-body">
                                                <p class="card-text">문자메시지고지 > 발송내역 탭 메뉴 선택 후<br/><strong>발송 내역을 조회할 기간 및 조건을 입력 후 '조회'버튼을 클릭!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="float-left col-md-4">
                                        <div class="card border-light mb-3">
                                            <div class="card-header">2. 실패 건 확인 및 재발송</div>
                                            <div class="card-body">
                                                <p class="card-text">실패 처리된 문자메시지 발신 건을 확인,<br/><strong>문자메시지고지 > 발송 탭 메뉴 재발송!</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="damoaSample3" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true" style="padding-right: 0 !important;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="sampleId3"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>

            <div class="modal-body">
                <div id="popup3">
                    <div class="info-container">
                        <div class="row">
                            <div class="col">
                                <div class="tab-selecter type-3">
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li class="nav-item"><a class="nav-link active show" data-toggle="tab" href="#step5">step 1</a></li>
                                        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#step6">step 2</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="info-container tab-content mt-3 mb-3">
                        <div id="step5" class="tab-pane active">
                            <div class="row">
                                <div class="col">
                                    <img src="/assets/imgs/common/email-sample.png" class="col-md-12">
                                </div>
                            </div>
                        </div>

                        <div id="step6" class="tab-pane">
                            <div class="row">
                                <div class="col">
                                    <img src="/assets/imgs/common/print-sample.png" class="col-md-12">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="damoaSample4" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true" style="padding-right: 0 !important;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="sampleId4"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>

            <div class="modal-body">
                <div id="popup4">
                    <div class="info-container mt-3 mb-3">
                        <div class="row">
                            <div class="col text-center">
                                <img src="/assets/imgs/common/kakao-sample.jpg" class="col-md-12" style="width: 50%">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
