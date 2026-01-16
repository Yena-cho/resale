<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="reg-single-payer" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">개별고객등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-6">
                            <h5>고객기본정보</h5>
                        </div>
                        <div class="col-6 text-right">
                            <span class="text-danger">* 필수입력</span>
                        </div>
                    </div>
                </div>

                <form>
                <div class="container-fluid">
                    <table class="table table-form">
                        <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>고객명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">고객번호</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>납부대상</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="checkbox" id="payable" value="option1">
                                        <label for="payable"><span class="mr-2"></span>대상</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="checkbox" id="excepted" value="option2">
                                        <label for="excepted"><span class="mr-2"></span>제외</label>
                                    </div>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">가상계좌</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    562-004-91318099
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>연락처</th>
                                <td class="col-md-10 col-sm-8 col-8 form-inline contact-number" colspan="3">
                                    <input type="tel" class="form-control" maxlength="3">
                                    <span class="hypen"> - </span>
                                    <input type="tel" class="form-control" maxlength="4">
                                    <span class="hypen"> - </span>
                                    <input type="tel" class="form-control" maxlength="4">
                                    <div class="guide-mention">
                                        * 휴대폰 번호로 입력하지 않을 시, 문자메시지 서비스 이용에 제한이 있을 수 있습니다.
                                    </div>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4"><span class="text-danger">*</span>메일주소</th>
                                <td class="col-md-10 col-sm-8 col-8 form-inline email-contact" colspan="3">
                                    <input type="text" class="form-control">
                                    <span class="ml-1 mr-1">@</span>
                                    <input type="text" class="form-control mr-1 email-provider">
                                    <select class="form-control provider-preset">
                                        <option value="">직접입력</option>
                                        <option value="naver.com">naver.com</option>
                                        <option value="nate.com">nate.com</option>
                                        <option value="yahoo.com">yahoo.com</option>
                                        <option value="empal.com">empal.com</option>
                                        <option value="gmail.com">gmail.com</option>
                                        <option value="hanmail.net">hanmail.net</option>
                                        <option value="daum.net">daum.net</option>
                                    </select>
                                    <div class="guide-mention">
                                        * 메일주소 미 입력 시, E-MAIL 고지 서비스 이용에 제한이 있을 수 있습니다.
                                    </div>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">고객구분1</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <img src="/assets/imgs/common/icon-x-in-circle.png" class="remove-text">
                                    <img src="/assets/imgs/common/input-mag.png" class="search">
                                    <input type="text" class="form-control">
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">고객구분2</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <img src="/assets/imgs/common/icon-x-in-circle.png" class="remove-text">
                                    <img src="/assets/imgs/common/input-mag.png" class="search">
                                    <input type="text" class="form-control">
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">고객구분3</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <img src="/assets/imgs/common/icon-x-in-circle.png" class="remove-text">
                                    <img src="/assets/imgs/common/input-mag.png" class="search">
                                    <input type="text" class="form-control">
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">고객구분4</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <img src="/assets/imgs/common/icon-x-in-circle.png" class="remove-text">
                                    <img src="/assets/imgs/common/input-mag.png" class="search">
                                    <input type="text" class="form-control">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12">
                            <h5>현금영수증 정보(선택)</h5>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <table class="table table-form">
                        <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">발급용도</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <select class="form-control parents">
                                        <option value="">선택</option>
                                        <option value="option1">소득공제</option>
                                        <option value="option2">지출증빙</option>
                                    </select>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">발급방법</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <select class="form-control first-depth-child" disabled>
                                        <option>선택</option>
                                        <option>소득공제</option>
                                        <option>지출증빙</option>
                                    </select>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">발급번호</th>
                                <td class="col-md-10 col-sm-8 col-8" colspan="3">
                                    <input type="text" class="form-control first-depth-child" placeholder="숫자만 입력" style="max-width:260px;" disabled>
                                    <span class="guide-mention ml-2">
                                        '-' 없이 숫자만 입력
                                    </span>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">현금영수증<br/>자동발급 여부</th>
                                <td class="col-md-10 col-sm-8 col-8 form-inline" colspan="3">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="auto" name="receipt-issuing-option">
                                        <label for="auto"><span class="mr-2"></span>자동발급</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="manual" name="receipt-issuing-option">
                                        <label for="manual"><span class="mr-2"></span>수동발급</label>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-6">
                            <h5>메모</h5>
                        </div>
                        <div class="col-6 text-right">
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <table class="table table-form">
                        <tbody>
                            <tr class="row no-gutters">
                                <td class="col-12">
                                    <input type="text" class="form-control">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="guide-mention" style="color:#71adfd;">
                        * 고객별 특이사항을 기입할 때 이용하세요
                    </div>
                </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary">대상 추가</button>
            </div>
        </div>
    </div>
</div>
