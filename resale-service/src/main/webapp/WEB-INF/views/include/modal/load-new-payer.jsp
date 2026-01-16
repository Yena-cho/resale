<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="load-new-payer" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">신규등록고객 불러오기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div id="page-description">
                        <div class="row">
                            <div class="col-12">
                                <p>최근 30일 내 신규등록된 고객의 청구정보를 등록하는 화면입니다.</p>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="container">
                    <div class="search-box">
                            <div class="row">
                                <label class="col-form-label col col-md-2 col-sm-4 col-4">
                                    청구월
                                </label>
                                <div class="col col-md-4 col-sm-8 col-8 form-inline">
                                    <select class="form-control">
                                        <option>선택</option>
                                        <option>2018</option>
                                        <option>2017</option>
                                    </select>
                                    <span class="ml-1 mr-1">년</span>

                                    <select class="form-control">
                                        <option>선택</option>
                                        <option>2018</option>
                                        <option>2017</option>
                                    </select>
                                    <span class="ml-1 mr-auto">월</span>
                                </div>
                                <label class="col-form-label col col-md-2 col-sm-4 col-4">
                                    청구항목
                                </label>
                                <div class="col col-md-4 col-sm-8 col-8 form-inline">
                                    <select class="form-control parents">
                                        <option value="">선택</option>
                                        <option value="option1">청구항목</option>
                                        <option value="option2">청구금액</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <label class="col-form-label col col-md-2 col-sm-4 col-4">
                                    납부기한 수정
                                </label>
                                <div class="col col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="date-input">
                                        <label class="sr-only" for="inlineFormInputGroupUsername">From</label>
                                        <div class="input-group">
                                            <input type="date" class="form-control date-picker-input" placeholder="YYYY/MM/DD" aria-label="From" aria-describedby="basic-addon2">
                                        </div>
                                    </div>
                                    <span class="range-mark"> ~ </span>
                                    <div class="date-input">
                                        <label class="sr-only" for="inlineFormInputGroupUsername">To</label>
                                        <div class="input-group">
                                            <input type="date" class="form-control date-picker-input" placeholder="YYYY/MM/DD" aria-label="To" aria-describedby="basic-addon2">
                                        </div>
                                    </div>
                                </div>
                                <label class="col-form-label col col-md-2 col-sm-4 col-4">
                                    청구금액 일괄변경
                                </label>
                                <div class="col col-md-4 col-sm-8 col-8 form-inline">
                                    <input type="text" class="form-control first-depth-child" disabled>
                                    <span class="ml-1">원</span>
                                </div>
                            </div>
                            <div class="row">
                                <label class="col-form-label col col-md-2 col-sm-4 col-4">
                                    고지서용 표시마감일
                                </label>
                                <div class="col col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="date-input">
                                        <label class="sr-only" for="inlineFormInputGroupUsername">From</label>
                                        <div class="input-group">
                                            <input type="date" class="form-control date-picker-input" placeholder="YYYY/MM/DD" aria-label="From" aria-describedby="basic-addon2">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row form-inline mt-3">
                                <div class="col-12 text-center">
                                    <button class="btn btn-primary btn-wide">
                                        적용
                                    </button>
                                </div>
                            </div>
                    </div>
                </div>


                <div class="container table-container">
                    <div id="loaded-list" class="list-id">
                        <div class="table-option row mb-2">
                            <div class="col-md-6 col-sm-12 form-inline">
                                <span class="amount mr-2">조회결과 [총 <em class="font-blue">90</em>건]</span>
                            </div>
                            <div class="col-md-6 col-sm-12 text-right mt-1">
                                <select class="form-control">
                                    <option>등록일자순</option>
                                    <option>상태별 정렬</option>
                                </select>
                                <select class="form-control">
                                    <option>최대10개</option>
                                    <option>최대20개</option>
                                    <option>최대50개</option>
                                    <option>최대100개</option>
                                </select>
                                <button class="btn btn-sm btn-d-gray" type="button">파일저장</button>
                            </div>
                        </div>
                        <div class="row">
                            <div class="table-responsive mb-3 col">
                                <table class="table table-sm table-hover table-primary">
                                    <thead>
                                        <tr>
                                            <th>
                                                <input class="form-check-input table-check-parents" type="checkbox" id="row-2-th" value="option2">
                                                <label for="row-2-th"><span></span></label>
                                            </th>
                                            <th>NO</th>
                                            <th>
                                                고객명
                                                <img src="/assets/imgs/common/icon-info.png">
                                            </th>
                                            <th>고객번호</th>
                                            <th>구분1</th>
                                            <th>구분2</th>
                                            <th>가상계좌</th>
                                            <th>연락처</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-90" value="option2">
                                                <label for="row-2-90"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-89" value="option2">
                                                <label for="row-2-89"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-88" value="option2">
                                                <label for="row-2-88"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-87" value="option2">
                                                <label for="row-2-87"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-86" value="option2">
                                                <label for="row-2-86"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-85" value="option2">
                                                <label for="row-2-85"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-84" value="option2">
                                                <label for="row-2-84"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-83" value="option2">
                                                <label for="row-2-83"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-82" value="option2">
                                                <label for="row-2-82"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input class="form-check-input table-check-child" type="checkbox" id="row-2-81" value="option2">
                                                <label for="row-2-81"><span></span></label>
                                            </td>
                                            <td>90</td>
                                            <td>김나라</td>
                                            <td></td>
                                            <td>피아노</td>
                                            <td>국영수</td>
                                            <td>738-384-28472901</td>
                                            <td>01098765432</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="row mb-4">
                            <div class="col-5">
                                <button class="btn btn-sm btn-gray-outlined btn-delete-selected-row">삭제</button>
                            </div>

                            <div class="col-2 text-center">
                                <button type="button" class="btn btn-primary btn-add-selected-charge" data-dismiss="modal">청구대상 추가</button>
                            </div>

                            <div class="col-5 text-right">
                            </div>
                        </div>

                        <nav aria-label="Page navigation example">
                            <ul class="pagination justify-content-center">
                                <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Move to first">
                                        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png"></span>
                                        <span class="sr-only">Move to first page</span>
                                    </a>
                                </li>
                                <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Previous">
                                        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png"></span>
                                        <span class="sr-only">Previous</span>
                                    </a>
                                </li>
                                <li class="page-item active"><a class="page-link" href="#">1</a></li>
                                <li class="page-item"><a class="page-link" href="#">2</a></li>
                                <li class="page-item"><a class="page-link" href="#">3</a></li>
                                <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Next">
                                        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png" class="rotate-180-deg"></span>
                                        <span class="sr-only">Next</span>
                                    </a>
                                </li>
                                <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Next">
                                        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png" class="rotate-180-deg"></span>
                                        <span class="sr-only">Move to last page</span>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
