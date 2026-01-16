/**
 * 기능 : 현재날짜를 가져온다(ex:20180409)
 * 작성자 : 이수현
 * @returns
 */
function getCurrentDate() {
    var today = new Date();
    var year = today.getFullYear().toString();
    var month = (today.getMonth() + 1).toString();
    var date = today.getDate().toString();

    return year + (month[1] ? month : '0' + month[0]) + (date[1] ? date : '0' + date[0]);
}

/**
 * 기능 : 현재년, 월을 가져온다(ex:201804)
 * 작성자 : 이수현
 * @returns
 */
function getCurrentMonth() {
    var today = new Date();
    var year = today.getFullYear().toString();
    var month = (today.getMonth() + 1).toString();

    return year + (month[1] ? month : '0' + month[0]);
}

/**
 * 기능 : 현재 년도를 가져온다
 * 작성자 : 이수현
 * @returns
 */
function getYear() {
    var today = new Date();
    var year = today.getFullYear().toString();

    return year;
}

/**
 * 기능 : 년도 셀렉트 박스(최근5년)
 * 작성자 : 이수현
 * @returns
 */
function getYearsBox(obj) {
    var today = new Date();
    var year = today.getFullYear();
    var nextYear = (today.getFullYear() + 1);
    var month = (today.getMonth() + 1);

    month = (month[1] ? month : '0' + month[0]);
    var str = "";
    if (month == 10 || month == 11 || month == 12) {
        for (var i = year; i >= nextYear - 5; i--) {
            str += "<option value='" + i + "'>" + i + "</option>";
        }
    } else {
        for (var i = year; i >= year - 5; i--) {
            str += "<option value='" + i + "'>" + i + "</option>";
        }
    }
    $('#' + obj).append(str);
    $('#' + obj).val(year);
}

/**
 * 기능 : 미래년도+1 셀렉트 박스(최근5년)
 * 작성자 : 이수현
 * @returns
 */
function getYearsBox2(obj) {
    var today = new Date();
    var thisyear = today.getFullYear();
    var year = (today.getFullYear() + 5).toString();
    var str = "";
    for (var i = year; i >= year - 10; i--) {
        str += "<option value='" + i + "'>" + i + "</option>";
    }
    $('#' + obj).append(str);
    $('#' + obj).val(thisyear);
}

/**
 * 기능 : 월 셀렉트 박스(12개월)
 * 작성자 : 이수현
 * @param obj
 * @returns
 */
function getMonthBox(obj) {
    var today = new Date();
    var month = (today.getMonth() + 1).toString();
    if (month < 10) {
        month = '0' + month;
    }
    var str = "";
    for (var i = 1; i <= 12; i++) {
        if (i < 10) {
            str += "<option value='0" + i + "'>0" + i + "</option>";
        } else {
            str += "<option value='" + i + "'>" + i + "</option>";
        }

    }
    $('#' + obj).append(str);
    $('#' + obj).val(month);
}

/**
 * 기능 : 월 셀렉트 박스(12개월)
 * 작성자 : 이수현
 * @param obj
 * @returns
 */
function getMonthBox2(obj, gubn) {
    var today = new Date();
    var month = (today.getMonth() + 1).toString();
    if (gubn != '') {
        month = month - gubn;
    }
    if (month < 10) {
        month = '0' + month;
    }
    var str = "";
    for (var i = 1; i <= 12; i++) {
        if (i < 10) {
            str += "<option value='0" + i + "'>0" + i + "</option>";
        } else {
            str += "<option value='" + i + "'>" + i + "</option>";
        }

    }
    $('#' + obj).append(str);
    $('#' + obj).val(month);
}

/**
 * 기능 : 년도, 월을 같이 보여준다(최근5년/12개월)
 * 작성자 : 이수현
 * @param obj
 * @returns
 */
function getYearsFullBox(obj) {
    var today = new Date();
    var year = today.getFullYear().toString();
    var month = (today.getMonth() + 1).toString();
    if (month < 10) {
        month = '0' + month;
    }

    var str = "";
    for (var i = year - 4; i <= year; i++) {
        for (var k = 1; k <= 12; k++) {
            if (k < 10) {
                str += "<option value='" + i + "0" + k + "'>" + i + "년 0" + k + "월</option>";
            } else {
                str += "<option value='" + i + "" + k + "'>" + i + "년 " + k + "월</option>";
            }
        }
    }
    $('#' + obj).append(str);
}

/**
 * 기능 : 현재페이지 링크삭제 후 다음 링크 활성화
 * 작성자 : 이수현
 * @returns
 */
function fnActPaging(page) {
    // console.log('fnActPaging');
    $('li.page-item').removeClass('active');
    $('li.page-item').each(function (i) {
        if ($(this).find('.page-link').text() == page) {
            $(this).addClass('active');
            $(this).find('.page-link').removeAttr('onclick');
            $(this).find('.page-link').removeAttr('href');
        } else {
            $(this).find('.page-link').attr('href', '#');
            $(this).find('.page-link').attr('onclick', 'list(' + (i + 1) + ')');
        }
    });
}

/**
 * 기능 : 현재페이지 링크삭제 후 다음 링크 활성화(modal)
 * 작성자 : 이수현
 * @returns
 */
function fnActModalPaging(page) {
    // console.log('fnActPaging');
    $('li.page-item').removeClass('active');
    $('li.page-item').each(function (i) {
        if ($(this).find('.page-link').text() == page) {
            $(this).addClass('active');
            $(this).find('.page-link').removeAttr('onclick');
            $(this).find('.page-link').removeAttr('href');
        } else {
            $(this).find('.page-link').attr('href', '#');
            $(this).find('.page-link').attr('onclick', 'moalList(' + (i + 1) + ')');
        }
    });
}

/**
 * 기능 : 특정일자에 대하여 날짜포맷를 가져온다(ex:2018.04.09)
 * 작성자 : 남건우
 * @returns
 */
function getDateFormatDot(obj) {
    var today = new Date(obj);
    var year = today.getFullYear().toString();
    var month = (today.getMonth() + 1).toString();
    var date = today.getDate().toString();

    return year + '.' + (month[1] ? month : '0' + month[0]) + '.' + (date[1] ? date : '0' + date[0]);
}

/**
 * 기능 : 특정일자에 대하여 날짜포맷를 가져온다(ex:2018.04.09)
 * 작성자 : 윤은정
 * @returns
 */
function getDateFmtDot(obj) {
    var today = new Date(obj.substring(0, 4), obj.substring(4, 6), obj.substring(6, 8));
    var year = today.getFullYear().toString();
    var month = (today.getMonth()).toString();
    var date = today.getDate().toString();

    return year + '.' + (month[1] ? month : '0' + month[0]) + '.' + (date[1] ? date : '0' + date[0]);
}

/**
 * 기능 : 특정일자에 대하여 날짜포맷를 가져온다(ex:2018.04)
 * 작성자 :
 * @returns
 */
function getMonthFmtDot(obj) {
    var today = new Date(obj.substring(0, 4), obj.substring(4, 6));
    var year = today.getFullYear().toString();
    var month = (today.getMonth()).toString();

    return year + '.' + (month[1] ? month : '0' + month[0]);
}

/**
 * 기능 : null 값을 "" 값으로 변경한다.
 *
 * @author 윤은정
 * @author wisehouse@finger.co.kr
 * @returns
 */
function nullValueChange(val) {
    return defaultString(val);
}

/**
 * 기능 : 문자열 value이 null 또는 undefined일 때 defaultValue의 값으로 반환한다
 *
 * @author wisehouse@finger.co.kr
 * @param value
 * @param defaultValue 초기화 기본값. 입력하지 않을 경우 빈문자열로 초기화한다.
 * @returns value 또는 defaultValue
 */
function defaultString(value, defaultValue) {
    defaultValue = defaultValue || '';

    if (value == null || typeof value == 'undefined') {
        return defaultValue;
    }

    return value;
}

/**
 * 기능 : 0을 붙여 두 자리수로 변경하여 반환 (날짜 형식에 사용)
 * 작성자 : 윤은정
 * @returns
 */
function setAddZero(val) {
    var num = parseInt(val);
    var str = num > 9 ? num : "0" + num;

    return str.toString();
}


/**
 * 기능 : ajax 페이징 처리
 * 작성자 : 윤은정
 * @returns
 */
function ajaxPaging(result, obj) {
    var str = '';
    if (result.pager.curPage > 1) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Move to first" onclick="list(1)">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png"></span>';
        str += '<span class="sr-only">Move to first page</span>';
        str += '</a>';
        str += '</li>';
    }
    if (result.pager.curBlock > 1) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Previous" onclick="list(' + result.pager.prevPage + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png"></span>';
        str += '<span class="sr-only">Previous</span>';
        str += '</a>';
        str += '</li>';
    }
    for (var i = result.pager.blockBegin; i <= result.pager.blockEnd; i++) {
        if (i == result.pager.curPage) {
            str += '<li class="page-item active"><a class="page-link">' + i + '</a></li>'; // 현재 페이지인 경우 하이퍼링크 제거
        } else {
            str += '<li class="page-item"><a class="page-link" href="#" onclick="list(' + i + ')">' + i + '</a></li>';
        }
    }
    if (result.pager.curBlock < result.pager.totBlock) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Next" onclick="list(' + result.pager.nextPage + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png" class="rotate-180-deg"></span>';
        str += '<span class="sr-only">Next</span>';
        str += '</a>';
        str += '</li>';
    }
    if (result.pager.curPage < result.pager.totPage) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Next" onclick="list(' + result.pager.totPage + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png" class="rotate-180-deg"></span>';
        str += '<span class="sr-only">Move to last page</span>';
        str += '</a>';
        str += '</li>';
    }
    $('#' + obj).html(str);
}

/**
 * 기능 : ajax 페이징 처리2 ( 한 페이지에 2개의 페이징처리 )
 * 작성자 :
 * @returns
 */
function ajaxPaging2(result, obj) {
    var str = '';
    if (result.pager2.curPage > 1) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Move to first" onclick="list2(1)">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png"></span>';
        str += '<span class="sr-only">Move to first page</span>';
        str += '</a>';
        str += '</li>';
    }
    if (result.pager2.curBlock > 1) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Previous" onclick="list2(' + result.pager2.prevPage + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png"></span>';
        str += '<span class="sr-only">Previous</span>';
        str += '</a>';
        str += '</li>';
    }
    for (var i = result.pager2.blockBegin; i <= result.pager2.blockEnd; i++) {
        if (i == result.pager2.curPage) {
            str += '<li class="page-item active"><a class="page-link">' + i + '</a></li>'; // 현재 페이지인 경우 하이퍼링크 제거
        } else {
            str += '<li class="page-item"><a class="page-link" href="#" onclick="list2(' + i + ')">' + i + '</a></li>';
        }
    }
    if (result.pager2.curBlock < result.pager2.totBlock) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Next" onclick="list2(' + result.pager2.nextPage + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png" class="rotate-180-deg"></span>';
        str += '<span class="sr-only">Next</span>';
        str += '</a>';
        str += '</li>';
    }
    if (result.pager2.curPage < result.pager2.totPage) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Next" onclick="list2(' + result.pager2.totPage + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png" class="rotate-180-deg"></span>';
        str += '<span class="sr-only">Move to last page</span>';
        str += '</a>';
        str += '</li>';
    }
    $('#' + obj).html(str);
}

/**
 * 기능 : ajax 페이징 처리(modal)
 * 작성자 : 윤은정
 * @returns
 */
function ajaxModalPaging(result, obj) {

    var val = result.modalNo;
    var str = '';
    if (result.modalPager.curPage > 1) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Move to first" onclick="modalList(1, ' + val + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png"></span>';
        str += '<span class="sr-only">Move to first page</span>';
        str += '</a>';
        str += '</li>';
    }
    if (result.modalPager.curBlock > 1) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Previous" onclick="modalList(' + result.modalPager.prevPage + ', ' + val + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png"></span>';
        str += '<span class="sr-only">Previous</span>';
        str += '</a>';
        str += '</li>';
    }
    for (var i = result.modalPager.blockBegin; i <= result.modalPager.blockEnd; i++) {
        if (i == result.modalPager.curPage) {
            str += '<li class="page-item active"><a class="page-link">' + i + '</a></li>'; // 현재 페이지인 경우 하이퍼링크 제거
        } else {
            str += '<li class="page-item"><a class="page-link" href="#" onclick="modalList(' + i + ', ' + val + ')">' + i + '</a></li>';
        }
    }
    if (result.modalPager.curBlock < result.modalPager.totBlock) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Next" onclick="modalList(' + result.modalPager.nextPage + ', ' + val + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png" class="rotate-180-deg"></span>';
        str += '<span class="sr-only">Next</span>';
        str += '</a>';
        str += '</li>';
    }
    if (result.modalPager.curPage < result.modalPager.totPage) {
        str += '<li class="page-item">';
        str += '<a class="page-link" href="#" aria-label="Next" onclick="modalList(' + result.modalPager.totPage + ', ' + val + ')">';
        str += '<span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png" class="rotate-180-deg"></span>';
        str += '<span class="sr-only">Move to last page</span>';
        str += '</a>';
        str += '</li>';
    }
    $('#' + obj).html(str);
}

/**
 * 기능 : sysajax 페이징 처리(modal)
 * 작성자 : 윤은정
 * @returns
 */
function sysajaxPaging(result, obj) {
    var val = result.modalNo;
    var str = '';
    if (result.pager.curBlock > 1) {
        str += '<button type="button" class="btn btn-white" onclick="list(' + result.pager.prevPage + ', ' + val + ')"><i class="fa fa-chevron-left"></i></button>';
    }
    for (var i = result.pager.blockBegin; i <= result.pager.blockEnd; i++) {
        if (i == result.pager.curPage) {
            str += '<button class="btn btn-white  active">' + i + '</button>'; // 현재 페이지인 경우 하이퍼링크 제거
        } else {
            str += '<button class="btn btn-white" onclick="list(' + i + ', ' + val + ')">' + i + '</button>';
        }
    }
    if (result.pager.curBlock < result.pager.totBlock) {
        str += '<button type="button" class="btn btn-white" onclick="list(' + result.pager.nextPage + ', ' + val + ')"><i class="fa fa-chevron-right"></i> </button>';
    }
    $('#' + obj).html(str);
}

/**
 * 기능 : sysajax 페이징 처리(modal)
 * 작성자 : 윤은정 (신재현 수정)
 * @returns
 */
function sysajaxPagingE(result, obj) {
    var val = result.modalNo;
    var str = '';
    if (result.epager.curBlock > 1) {
        str += '<button type="button" class="btn btn-white" onclick="list2(' + result.epager.prevPage + ', ' + val + ')"><i class="fa fa-chevron-left"></i></button>';
    }
    for (var i = result.epager.blockBegin; i <= result.epager.blockEnd; i++) {
        if (i == result.epager.curPage) {
            str += '<button class="btn btn-white  active">' + i + '</button>'; // 현재 페이지인 경우 하이퍼링크 제거
        } else {
            str += '<button class="btn btn-white" onclick="list2(' + i + ', ' + val + ')">' + i + '</button>';
        }
    }
    if (result.epager.curBlock < result.epager.totBlock) {
        str += '<button type="button" class="btn btn-white" onclick="list2(' + result.epager.nextPage + ', ' + val + ')"><i class="fa fa-chevron-right"></i> </button>';
    }
    $('#' + obj).html(str);
}

/**
 * 기능 : 현재월을 가져온다
 * 작성자 : 윤은정
 * @returns
 */
function getMonth() {
    var today = new Date();
    var month = (today.getMonth() + 1).toString();

    return (month[1] ? month : '0' + month[0]);
}

/**
 * 기능 : 숫자 정규식
 * 작성자 : 윤은정
 * @returns
 */
function onlyNumber(obj) {
    $(obj).keyup(function () {
        $(this).val($(this).val().replace(/[^0-9]/g, ""));
    });
}

/**
 * 기능 : 숫자에 ','를 표시한다.
 * 작성자 : 윤은정
 * @returns
 */
function numberToCommas(num) {
    if (num == null || num == "" || num <= 0) {
        return 0;
    } else {
        return num.toString().replace(/,/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
}


/**
 * 기능 : 휴대폰번호 포맷으로 리턴한다.(구분자: '-')
 * 작성자 : 남건우
 * @returns
 */
function getHpNoFormat(num) {
    return num.toString().replace(/(01[016789]{1}|02|0[3-9]{1}[0-9]{1})([0-9]{3,4})([0-9]{4})/, "$1-$2-$3");
}

/**
 * 기능 : 입력받은날짜 기준으로 과거 날짜 가져온다(ex:1달전, 2달전, 3달전)
 *
 * @author 이수현
 * @author wisehouse@finger.co.kr
 * @returns
 */
function getPrevDate(toDay, num) {
    toDay = toDay.split('.').join('');

    return moment(toDay, 'YYYYMMDD').subtract(num, 'months').format('YYYY.MM.DD');
}

/**
 * 기능 : . 치환
 * 작성자 : 이수현
 * @returns
 */
function replaceDot(str) {
    return str.split('.').join('');
}

/**
 * 기능 : .포멧으로 현재날짜를 가져온다(ex:20180430)
 * 작성자 : 이수현
 * @returns
 */
function getFormatCurrentDate() {
    var today = new Date();
    var year = today.getFullYear().toString();
    var month = (today.getMonth() + 1).toString();
    var date = today.getDate().toString();

    return year + '.' + (month[1] ? month : '0' + month[0]) + '.' + (date[1] ? date : '0' + date[0]);
}


/**
 * 기능 : 특정일자에 대하여 날짜포맷를 가져온다(ex:MM/DD/YYYY)
 * 작성자 : 남건우
 * @returns
 */
function getDateFormatSlash(obj) {
    var today = new Date(obj);
    var year = today.getFullYear().toString();
    var month = (today.getMonth() + 1).toString();
    var date = today.getDate().toString();

    return (month[1] ? month : '0' + month[0]) + '/' + (date[1] ? date : '0' + date[0]) + '/' + year;
}


/**
 * 기능 : 특정일자에 대하여 날짜포맷를 가져온다(YYMMDD ==> MM/DD/YYYY)
 * 작성자 : 남건우
 * @returns
 */
function getDateFromBasicStrToSlash(str) {
    var today = new Date(str.substring(0, 4), str.substring(4, 6), str.substring(6, 8));
    var year = today.getFullYear().toString();
    var month = (today.getMonth() + 1).toString();
    var date = today.getDate().toString();

    return (month[1] ? month : '0' + month[0]) + '/' + (date[1] ? date : '0' + date[0]) + '/' + year;
}


/**
 * 기능 : (MM/DD/YYYY ==> YYYYMMDD)특정일자에 대하여 날짜포맷를 가져온다
 * 작성자 : 남건우
 * @returns
 */
function getDateFromSlashToBasic(str) {
    var retVal = str.substring(6, 10) + str.substring(0, 2) + str.substring(3, 5);
    return retVal;
}

/**
 * 기능 : (MM/DD/YYYY ==> YYYY.MM.DD)특정일자에 대하여 날짜포맷를 가져온다
 * 작성자 : 남건우
 * @returns
 */
function getDateFromSlashToDot(str) {
    var retVal = str.substring(6, 10) + "." + str.substring(0, 2) + "." + str.substring(3, 5);
    return retVal;
}


/**
 * 파일명에서 확장자명 추출
 *
 *  @param filename 파일명
 * @returns _fileExt 확장자명
 */
function getExtensionOfFilename(filename) {
    var _fileLen = filename.length;
    /**
     * lastIndexOf('.')
     * 뒤에서부터 '.'의 위치를 찾기위한 함수
     * 검색 문자의 위치를 반환한다.
     * 파일 이름에 '.'이 포함되는 경우가 있기 때문에 lastIndexOf() 사용
     */
    var _lastDot = filename.lastIndexOf('.'); // 확장자 명만 추출한 후 소문자로 변경
    var _fileExt = filename.substring(_lastDot, _fileLen).toLowerCase();
    return _fileExt;
}


/**
 * 기능 : (YYYYMMDDHH24MISS ==> YYYY.MM.DD HH24:MI:SS)특정일자에 대하여 날짜포맷를 가져온다
 * 작성자 : 남건우
 * @returns
 */
function getDateTimeFmtDot(str) {
    if (str.length != 14) return "";

    var retVal = str.substring(0, 4) + "." + str.substring(4, 6) + "." + str.substring(6, 8) + " " + str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14);

    return retVal;
}


/**
 * 기능 : 분 셀렉트 박스(00~59분)
 * 작성자 : 남건우
 * @param obj
 * @returns
 */
function getMinuteBox(obj) {
    var today = new Date();
    var minute = (today.getMinutes()).toString();
    if (minute < 10) {
        minute = '0' + minute;
    }
    var str = "";
    for (var i = 0; i <= 59; i++) {
        if (i < 10) {
            str += "<option value='0" + i + "'>0" + i + "</option>";
        } else {
            str += "<option value='" + i + "'>" + i + "</option>";
        }

    }
    $('#' + obj).append(str);
}


/**
 * 기능 : (HH24MISS ==> HH24:MI:SS)특정일자에 대하여 날짜포맷를 가져온다
 * 작성자 : 남건우
 * @returns
 */
function getTimeFmtBasic(str) {
    if (str.length != 6) return "";

    var retVal = str.substring(0, 2) + ":" + str.substring(2, 4) + ":" + str.substring(4, 6);

    return retVal;
}


/**
 * 은행코드로 은행명 추출
 * @param str
 * @returns
 */
function getBnkNm(str) {
    var bnkNm = "";
    if (str == "003") {
        bnkNm = "기업은행";
    }
    else if (str == "004") {
        bnkNm = "국민은행";
    }
    else if (str == "005") {
        bnkNm = "외환은행";
    }
    else if (str == "007") {
        bnkNm = "수협";
    }
    else if (str == "011") {
        bnkNm = "농협";
    }
    else if (str == "012") {
        bnkNm = "농협";
    }
    else if (str == "013") {
        bnkNm = "농협";
    }
    else if (str == "014") {
        bnkNm = "농협";
    }
    else if (str == "015") {
        bnkNm = "농협";
    }
    else if (str == "020") {
        bnkNm = "우리은행";
    }
    else if (str == "021") {
        bnkNm = "신한은행";
    }
    else if (str == "023") {
        bnkNm = "제일은행";
    }
    else if (str == "026") {
        bnkNm = "신한은행";
    }
    else if (str == "027") {
        bnkNm = "씨티은행";
    }
    else if (str == "031") {
        bnkNm = "대구은행";
    }
    else if (str == "032") {
        bnkNm = "부산은행";
    }
    else if (str == "034") {
        bnkNm = "광주은행";
    }
    else if (str == "035") {
        bnkNm = "제주은행";
    }
    else if (str == "037") {
        bnkNm = "전북은행";
    }
    else if (str == "039") {
        bnkNm = "경남은행";
    }
    else if (str == "045") {
        bnkNm = "새마을금고";
    }
    else if (str == "048") {
        bnkNm = "신협";
    }
    else if (str == "071") {
        bnkNm = "우체국";
    }
    else if (str == "081") {
        bnkNm = "하나은행";
    }
    else if (str == "088") {
        bnkNm = "신한은행";
    }
    else if (str == "089") {
        bnkNm = "케이뱅크";
    }
    else if (str == "090") {
        bnkNm = "카카오뱅크";
    }
    else if (str == "102") {
        bnkNm = "대신저축은행";
    }
    else if (str == "103") {
        bnkNm = "에스비아저축은행";
    }
    else if (str == "104") {
        bnkNm = "에이치케이저축은행";
    }
    else if (str == "105") {
        bnkNm = "웰컴저축은행";
    }
    else if (str != "") {
        bnkNm = str;
    }
    else {
        bnkNm = "";
    }
    return bnkNm;
}

/*
 * 20180101 -> 2018.01.01 치환
 * by 이수현
 */
function changeDateFormat(str) {
    if (str == null || str == "") {
        return "";
    }
    if (str != null && str != "") {
        return str.substring(0, 4) + '.' + str.substring(4, 6) + '.' + str.substring(6, 8);
    }
}

function changeTimeFormat(str) {
    if (str == null || str == "") {
        return "";
    }
    if (str != null && str != "") {
        return str.substring(0, 2) + ':' + str.substring(2, 4) + ':' + str.substring(4);
    }
}

/**
 * 기능 : no 개월 이전 날짜를 리턴함
 * @param no : 개월, day : 기준날짜 ex)2018.06.04 || 20180604
 * @returns
 */
function monthAgo(day, no) {
    day = day.replace(/\./gi, "");
    if (day == null || day == undefined || day == "") {
        var tempDay = new Date();
        day = tempDay.getFullYear() + addZero(tempDay.getMonth() + 1) + addZero(tempDay.getDate());
    }
    var year = day.substring(0, 4);
    var month = day.substring(4, 6);
    var date = day.substring(6, 8);

    var agoDay = new Date();
    agoDay.setFullYear(year);
    agoDay.setMonth(month - 1);
    agoDay.setDate(date);

    var ago = agoDay.getMonth();
    agoDay.setMonth(ago - no);

    var agoYear = agoDay.getFullYear();
    var agoMonth = addZero(agoDay.getMonth() + 1);
    var agoDay = addZero(agoDay.getDate());

    agoDay = agoYear + "." + agoMonth + "." + agoDay;

    return agoDay;

}

function addZero(value) {
    if (value < 10) {
        value = '0' + value;
    }
    return value;
}

/*
 * 윤년여부 검사
 */
function isLeaf(year) {
    var leaf = false;

    if (year % 4 == 0) {
        leaf = true;

        if (year % 100 == 0) {
            leaf = false;
        }

        if (year % 400 == 0) {
            leaf = true;
        }
    }

    return leaf;
}

/*
 * 날짜포맷에 맞는지 검사
 * YYYYMMMDD
 */
function isDateFormat(d) {
    var df = /[0-9]{4}[0-9]{2}[0-9]{2}/;
    return d.match(df);
}

/*
 * 날짜가 유효한지 검사
 * input : YYYYMMMDD
 */
function isValidDate(d) {
    // 포맷에 안맞으면 false리턴
    if (!isDateFormat(d)) {
        return false;
    }
    d = changeDateFormat(d);
    var month_day = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

    var dateToken = d.split('.');
    var year = Number(dateToken[0]);
    var month = Number(dateToken[1]);
    var day = Number(dateToken[2]);

    // 날짜가 0이면 false
    if (day == 0) {
        return false;
    }

    var isValid = false;

    // 윤년일때
    if (isLeaf(year)) {
        if (month == 2) {
            if (day <= month_day[month - 1] + 1) {
                isValid = true;
            }
        } else {
            if (day <= month_day[month - 1]) {
                isValid = true;
            }
        }
    } else {
        if (day <= month_day[month - 1]) {
            isValid = true;
        }
    }

    return isValid;
}

//조회기간 차이 ( ex input: 20180514, 20180714)
function CalcDay(argFDay, argTDay) {
    fDay = new Date(parseInt(argFDay.substr(0, 4), 10), parseInt(argFDay.substr(4, 2), 10) - 1, parseInt(argFDay.substr(6, 2), 10));
    tDay = new Date(parseInt(argTDay.substr(0, 4), 10), parseInt(argTDay.substr(4, 2), 10) - 1, parseInt(argTDay.substr(6, 2), 10));

    gap = tDay.getTime() - fDay.getTime();
    gap = Math.floor((gap) / (60 * 60 * 24 * 1000));
    return gap;
}

// 월별 조회기간(201712~201811)
function CalcMonth(start, end) {
    start = start + '01';
    end = end + '01';
    var sDate = new Date(parseInt(start.substr(0, 4), 10), parseInt(start.substr(4, 2), 10) - 1, parseInt(start.substr(6, 2), 10));
    var eDate = new Date(parseInt(end.substr(0, 4), 10), parseInt(end.substr(4, 2), 10) - 1, parseInt(end.substr(6, 2), 10));
    var gap = (eDate.getTime() - sDate.getTime()) / 1000 / 60 / 60 / 24;
    return gap;
}

/*
 * 신재현 - 
 * 날짜 유효성 확인 공통
 */
function dateValidity(startday, endday) {
    startday = startday.replace(/\./gi, "");
    endday = endday.replace(/\./gi, "");
    var message = 'ok';
    var nYear = Number(startday.substring(0, 4));
    var nMonth = Number(startday.substring(4, 6));
    var nDay = Number(startday.substring(6, 8));
    var eYear = Number(endday.substring(0, 4));
    var eMonth = Number(endday.substring(4, 6));
    var eDay = Number(endday.substring(6, 8));

    if (nYear < 1900 || nYear > 2100 || eYear < 1900 || eYear > 2100) { // 사용가능 하지 않은 년도 체크
        message = '정확한 년도를 입력해 주세요.';
        return message;
    }

    if (nMonth < 1 || nMonth > 12 || eMonth < 1 || eMonth > 12) { // 사용가능 하지 않은 달 체크
        message = '정확한 월자를 입력해 주세요.';
        return message;
    }

    // 해당달의 마지막 일자 구하기
    var nMaxDay = new Date(new Date(nYear, nMonth, 1) - 86400000).getDate();
    var eMaxDay = new Date(new Date(eYear, eMonth, 1) - 86400000).getDate();
    if (nDay < 1 || nDay > nMaxDay || eDay < 1 || eDay > eMaxDay) { // 사용가능 하지 않은 날자 체크
        message = '정확한 일자를 입력해 주세요.';
        return message;
    }

    if ((!startday || !endday) || (startday.length < 8 || endday.length < 8)) {
        message = '조회할 기간별 일자를 확인해주세요.';
        return message;
    }
    if (getCurrentDate() < startday || getCurrentDate() < endday) {
        message = "현재일보다 이후를 조회할 수 없습니다.";
        return message;
    }
    if (startday > endday) {
        message = "시작 기간이 종료 기간보다 클 수 없습니다.";
        return message;
    } else {
        return message;
    }
};

/**
 *
 * @param obj
 */
function inputNumberFormat(obj) {
    obj.value = comma(uncomma(obj.value));
}

/**
 *
 * @param str
 * @returns
 */
function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

/**
 *
 * @param str
 * @returns
 */
function uncomma(str) {
    str = String(str);
    return str.replace(/[^\d]+/g, '');
}

/**
 * jhjeong@finger.co.kr
 *
 * 기간선택함수 각 페이지별로 산개되어 있는 펑션 하나로 정리.
 * @modified 2018. 08. 06
 * @param month
 */
function setMonthTerm(month) {
    if (month > 0) {
        var calDateFrom = "";
        var calDateTo = "";
        var toDate = new Date();
        var fromDate = new Date();
        fromDate.setMonth(fromDate.getMonth() - month);

        $('#startday').val(getDateFormatDot(fromDate));
        $('#endday').val(getDateFormatDot(toDate));
    } else {
        $('#startday').val("");
        $('#endday').val("");
    }

    $("button[name=btnSetMonth]").removeClass('active');
    $('#btnSetMonth' + month).addClass('active');

    return;
}

/**
 * 날짜에 년, 월, 일을 조정한다.
 * @author 정재훈 jhjeong@saltlux.com
 * @modified 2016. 2. 24.
 * @param _date
 * @param field {y, m, d} 년y, 월m, 일d
 * @param mm numberic 변경일(월/년)
 * @param separator 날짜구분자
 * @returns {String | Date} input과 같은 type으로 리턴
 */
function addDate(_date, field, mm, separator) {
    var typeDate = false;

    separator = (separator ? separator : "");

    var d = new Date();
    if (typeof _date === 'object' && _date instanceof Date) {
        //console.log('_date is date');
        typeDate = true;
        d.setTime(_date.getTime());
    } else {
        typeDate = false;
        var _ds = _date.split(separator);
        var _d = new Date(_ds[0], (_ds[1] - 1), _ds[2]);
        d.setTime(_d.getTime());
    }

    if (field == 'y' || field == 'Y') d.setFullYear(d.getFullYear() + mm);
    if (field == 'm' || field == 'M') d.setMonth(d.getMonth() + mm);
    if (field == 'd' || field == 'D') d.setDate(d.getDate() + mm);

    var yyyy = d.getFullYear().toString();
    var mm = (d.getMonth() + 1).toString(); // getMonth() is zero-based
    var dd = d.getDate().toString();
    return yyyy + separator + (mm[1] ? mm : "0" + mm[0]) + separator + (dd[1] ? dd : "0" + dd[0]); // padding
}


/**
 * 년월 데이터로 변경
 * @author    정미래
 * @param date        YYYYMM
 */
function formatYearMonth(date) {
    return moment(date, 'YYYYMM').format("YYYY.MM");
}

/**
 * XSS preventing
 * 기본ESCAPE
 */
function basicEscape(data) {
    if (data != null) {
        data = data.replace(/\"/g, "&quot;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\'/g, "&#x27;");
        return data;
    } else {
        return data;
    }
}

/**
 * 기본UNESCAPE
 */
function basicUnEscape(data) {
    if (data != null) {
        data = data.replace(/&quot;/g, '"').replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&#x27;/g, "'");
        return data;
    } else {
        return data;
    }
}

/**
 * ajax 통신으로 리턴받은 데이터(엑셀 파일 View)를 다운로드
 * @author    임연주
 * @param blob, status, xhr
 */
function downloadExcelAjax(blob, status, xhr) {
    var filename = "";
    var disposition = xhr.getResponseHeader('Content-Disposition');
    if (disposition && (disposition.indexOf('attachment') !== -1 || disposition.indexOf('attachement') !== -1)) {
        var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
        var matches = filenameRegex.exec(disposition);
        if (matches != null && matches[1]) filename = decodeURIComponent(matches[1].replace(/['"]/g, ''));
    }

    if (typeof window.navigator.msSaveBlob !== 'undefined') {
        window.navigator.msSaveBlob(blob, filename);
    } else {
        var URL = window.URL || window.webkitURL;
        var downloadUrl = URL.createObjectURL(blob);

        if (filename) {
            var a = document.createElement("a");
            if (typeof a.download === 'undefined') {
                window.location.href = downloadUrl;
            } else {
                a.href = downloadUrl;
                a.download = filename;
                document.body.appendChild(a);
                a.click();
            }
        } else {
            window.location.href = downloadUrl;
        }

        setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100); // cleanup
    }
}
