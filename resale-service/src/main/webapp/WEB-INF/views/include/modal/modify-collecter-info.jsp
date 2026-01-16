<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="modify-collecter-info" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">기관정보수정</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-12">
                            <h5>기관기본정보</h5>
                        </div>
                    </div>
                </div>

                <form>
                <div class="container-fluid">
                    <table class="table table-form">
                        <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">기관명(업체명)</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">아이디</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">대표 전화번호</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">기관코드</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">사업자등록번호</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">담당자명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">담당자 전화번호</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">기관상태</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">주소</th>
                                <td class="col-md-10 col-sm-8 col-8 address-form-in-table">
                                    <div class="zipcode">
                                        <input type="text" class="form-control zipcode-input mr-1" readonly>
                                        <button class="btn btn-sm btn-d-gray">우편번호검색</button>
                                    </div>
                                    <div class="address-lines w-100">
                                        <input type="text" class="form-control first-line mt-1">
                                        <input type="text" class="form-control second-line mt-1">
                                    </div>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">수수료 계좌번호</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control">
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">다계좌 사용여부</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="option1" value="option1" name="account">
                                        <label for="option1"><span class="mr-2"></span>사용</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="option2" value="option2" name="account">
                                        <label for="option2"><span class="mr-2"></span>미사용</label>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12">
                            <h5>대리점정보</h5>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="row no-gutters">
                        <div class="col col-md-6 col-sm-12">
                            <table class="table table-primary">
                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>계좌 순번</th>
                                        <th>계좌명</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>1</td>
                                        <td>0000</td>
                                        <td>미래로 유치원</td>
                                    </tr>
                                    <tr>
                                        <td>2</td>
                                        <td>0001</td>
                                        <td>미래로 미술학원</td>
                                    </tr>
                                    <tr>
                                        <td>3</td>
                                        <td>0002</td>
                                        <td>미래로 피아노</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="col col-md-6 col-sm-12">
                            <table class="table table-primary">
                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>계좌 순번</th>
                                        <th>계좌명</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>4</td>
                                        <td>0003</td>
                                        <td>미래로 어린이집</td>
                                    </tr>
                                    <tr>
                                        <td>-</td>
                                        <td>-</td>
                                        <td>-</td>
                                    </tr>
                                    <tr>
                                        <td>-</td>
                                        <td>-</td>
                                        <td>-</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12">
                            <h5>이용요금 정보</h5>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <table class="table table-primary">
                                <thead>
                                    <tr>
                                        <th colspan="2">구분</th>
                                        <th>범위</th>
                                        <th>총 금액</th>
                                        <th>은행</th>
                                        <th>핑거</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td rowspan="2">관리수수료</td>
                                        <td>정액</td>
                                        <td class="text-right">100건 이하</td>
                                        <td class="text-right">15,000원</td>
                                        <td class="text-right">0원</td>
                                        <td class="text-right">15,000원</td>
                                    </tr>
                                    <tr>
                                        <td>건당</td>
                                        <td class="text-right">100건 초과</td>
                                        <td class="text-right">150원</td>
                                        <td class="text-right">0원</td>
                                        <td class="text-right">150원</td>
                                    </tr>
                                    <tr>
                                        <td>입금수수료</td>
                                        <td>건당</td>
                                        <td class="text-right">1건 이상</td>
                                        <td class="form-td-inline">
                                            <input type="text" class="form-control">
                                            <span class="ml-1">원</span>
                                        </td>
                                        <td class="form-td-inline">
                                            <input type="text" class="form-control">
                                            <span class="ml-1">원</span>
                                        </td>
                                        <td class="form-td-inline">
                                            <input type="text" class="form-control">
                                            <span class="ml-1">원</span>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12">
                            <h5>지점정보</h5>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <table class="table table-form">
                        <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">지점이름</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    서여의도점
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">지점코드</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    123132312412
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">메모</th>
                                <td class="col-md-10 col-sm-8 col-8" colspan="3">
                                    메모메모메모메모메ㅗ메모모
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
