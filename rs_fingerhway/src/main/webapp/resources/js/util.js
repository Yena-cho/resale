

//********************************************************
//   문자열의 길이구하는 함수 (한글 : 2,  영문 : 1)
//********************************************************

function Length(src) {
	var len = 0, i;

	for (i = 0; i < src.length; i++) {
		if (IsKorean(src.charAt(i)) == true)
			len = len + 2;
		else
			len++;
	}

	return len;
}

//********************************************************
//        문자가 숫자로 된건지 체크
//********************************************************

function IsDigit(chr) {
	if ((chr >= '0') && (chr <= '9'))
		return true;
	else
		return false;
}

//********************************************************
//        문자가 숫자로 된건지 체크
//********************************************************

function IsStrDigit(src) {
	var i, flag;

	for (i = 0; i < src.length; i++) {
		if (IsDigit(src.charAt(i)) != true)
			return false;
	}

	return true;
}

//********************************************************
//        문자열에 문자가 존재하는지 체크
//********************************************************

function ExistChar(src, chr) {
	var i;

	for (i = 0; i < src.length; i++) {
		if (src.charAt(i) == chr)
			return true;
	}

	return false;
}

//********************************************************
//        문자가 한국어인지 아닌지 체크
//********************************************************

function IsKorean(chr) {
	if (chr.charCodeAt(0) > 0x8000)
		return true;

	return false;
}

//********************************************************
//        문자가 영어인지 아닌지 체크
//********************************************************

function IsEnglish(chr) {
	if (chr >= 'A' && chr <= 'Z')
		return true;
	else if (chr >= 'a' && chr <= 'z')
		return true;
	else
		return false;
}



//********************************************************
//         문자열이 영문자와 숫자의 조합인지 체크
//********************************************************

function IsDigitLowAlpa(src) {
	var i, flag;

	for (i = 0; i < src.length; i++) {
		if (IsDigit(src.charAt(i)))
			flag = true;
		else if (IsEnglish(src.charAt(i)) == 1)
			flag = true;
		else {
			flag = false;
			break;
		}
	}

	return flag;
}



//********************************************************
//         문자열 TRIM 하는 함수 (나중에 로직 업데이트 필요)
//********************************************************
function Trim(str) {
	var index, len

	while (true) {
		index = str.indexOf(" ")
		if (index == -1) break;

		len = str.length

		str = str.substring(0, index) + str.substring((index + 1), len);
	}

	return str;
}



//********************************************************
//   생일등의 Select 박스 날짜 설정
//********************************************************
function SetDayOfMonth(Month, SetTarget) {

	// 해당 월에 최대 일수는 구해온다. 
	var Month = parseInt(Month);
	var arrMonthDay = new Array(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);


	// select 박스에 셋팅한다. 
	var oTarget = document.all(SetTarget)

	MonthDay = arrMonthDay[Month - 1];

	for (i = 1; i <= MonthDay; i++) {
		oTarget.options.length = MonthDay;
		if (i < 10)
			oTarget.options[i - 1].value = '0' + i;
		else
			oTarget.options[i - 1].value = i;

		oTarget.options[i - 1].text = i + '';

	}

}

//********************************************************
//   팝업창
//********************************************************

function OpenPopup(Url, Name, wid, hei, scr) {
	var scr;
	if (scr == 'Y') {
		var wid = parseInt(wid) + parseInt(17);
		Popup = window.open(Url, Name, 'width=' + wid + ',height=' + hei + ',resizable=no, scrollbars=yes');
	}
	else {
		Popup = window.open(Url, Name, 'width=' + wid + ',height=' + hei + ',resizable=no');
	}
	Popup.focus()
}