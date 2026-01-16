/**
 * 개인정보 처리방침
 */
function fn_personalInfo(version) {
    var goVersion = $('#personalInfo option:selected').val();
    var url = "/common/personalInfo/" + goVersion;
    window.open(url, '_self');
}

/**
 * 서비스 이용약관
 */
function fn_serviceInfo(version) {
    var goVersion = $('#serviceInfo option:selected').val();
    var url = "/common/serviceInfo/" + goVersion;
    window.open(url, '_self');
}

/**
 * 전자금융거래약관
 */
function electronicFinancialInfo(version) {
    var goVersion = $('#electronicFinancialInfo option:selected').val();
    var url = "/common/electronicFinancialInfo/" + goVersion;
    window.open(url, '_self');
}
