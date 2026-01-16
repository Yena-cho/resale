package com.finger.shinhandamoa.common;

import java.util.regex.Pattern;

/**
 * XSS(Cross-Site Scripting) 공격 방어를 위한 Sanitizer
 *
 * @author Security Team
 * @version 3.0 (기존 함수 유지 + 강화 버전 추가)
 * @since 2025-10-24
 */
public class SimpleXSSSanitizer {

    // ========================================
    // 기존 함수 (하위 호환성 유지)
    // ========================================

    /**
     * 기존 XSS 필터링 함수 (하위 호환성 유지)
     *
     * @param input 사용자 입력값
     * @return XSS 방어 처리된 문자열
     * @deprecated 새로운 프로젝트에서는 sanitizeEnhanced() 사용 권장
     */
    public static String sanitize(String input) {
        if (input == null) return null;

        // XSS 관련 태그 및 속성 제거
        input = input.replaceAll("(?i)<script.*?>.*?</script>", "");
        input = input.replaceAll("(?i)javascript:", "");
        input = input.replaceAll("(?i)<[^>]*on\\w+\\s*['\\\"].*?['\\\"]", "");
        input = input.replaceAll("(?i)<[^>]+>", "");

        // 특수문자 이스케이프 (< > & " ')
        input = input.replace("&", "&amp;");
        input = input.replace("<", "&lt;");
        input = input.replace(">", "&gt;");
        input = input.replace("\"", "&quot;");
        input = input.replace("'", "&#39;");

        return input;
    }

    // ========================================
    // 새로운 강화된 XSS 필터링 함수
    // ========================================

    // XSS 공격 패턴 정의
    private static final Pattern[] XSS_PATTERNS = new Pattern[]{
            // 스크립트 태그
            Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("<script[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),

            // iframe 태그
            Pattern.compile("<iframe[^>]*>.*?</iframe>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("<iframe[^>]*>", Pattern.CASE_INSENSITIVE),

            // javascript: 프로토콜
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),

            // 이벤트 핸들러 (onclick, onerror, onload 등)
            Pattern.compile("on\\w+\\s*=", Pattern.CASE_INSENSITIVE),

            // eval 함수
            Pattern.compile("eval\\s*\\(", Pattern.CASE_INSENSITIVE),

            // expression (CSS expression)
            Pattern.compile("expression\\s*\\(", Pattern.CASE_INSENSITIVE)
    };

    /**
     * 강화된 XSS 필터링 함수 (신규 프로젝트 권장)
     * - 다양한 XSS 공격 패턴 차단
     * - 위험한 태그 명시적 제거
     * - 완벽한 HTML 특수문자 이스케이프
     *
     * @param input 사용자 입력값
     * @return XSS 방어 처리된 문자열
     */
    public static String sanitizeEnhanced(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String result = input;

        // 1단계: XSS 공격 패턴 제거
        for (Pattern pattern : XSS_PATTERNS) {
            result = pattern.matcher(result).replaceAll("");
        }

        // 2단계: 위험한 태그 제거 (화이트리스트 방식)
        // <script>, <iframe>, <object>, <embed> 등 위험한 태그 제거
        result = result.replaceAll("(?i)<(script|iframe|object|embed|applet|meta|link|style)[^>]*?>.*?</\\1>", "");
        result = result.replaceAll("(?i)<(script|iframe|object|embed|applet|meta|link|style)[^>]*?>", "");

        // 3단계: HTML 특수문자 이스케이프
        result = escapeHtmlEnhanced(result);

        return result;
    }

    /**
     * HTML 특수문자를 엔티티로 변환 (강화 버전)
     *
     * @param input 입력 문자열
     * @return 이스케이프된 문자열
     */
    private static String escapeHtmlEnhanced(String input) {
        if (input == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&#39;");
                    break;
                case '/':
                    sb.append("&#x2F;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 특정 HTML 태그만 허용하는 화이트리스트 방식 Sanitizer
     * 허용 태그: <br>, <p>, <strong>, <em>, <b>, <i>, <u>
     *
     * @param input 입력 문자열
     * @return 안전한 HTML 문자열
     */
    public static String sanitizeWithWhitelist(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 1단계: 기본 XSS 방어 적용
        String result = sanitizeEnhanced(input);

        // 2단계: 허용된 태그만 복원 (필요시)
        // 예: <br/> 태그는 허용하려면 아래 코드 활성화
        // result = result.replace("&lt;br/&gt;", "<br/>");
        // result = result.replace("&lt;br&gt;", "<br>");

        return result;
    }

    /**
     * 파일명에 대한 XSS 방어
     * 파일명은 영문, 숫자, 점, 하이픈, 언더스코어만 허용
     *
     * @param fileName 파일명
     * @return 안전한 파일명
     */
    public static String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return fileName;
        }

        // 안전한 문자만 허용 (영문, 숫자, 점, 하이픈, 언더스코어)
        String safeName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");

        // 연속된 점(..) 제거 (디렉토리 순회 공격 방어)
        safeName = safeName.replaceAll("\\.{2,}", ".");

        return safeName;
    }

    /**
     * URL에 대한 XSS 방어
     * javascript:, data:, vbscript: 등 위험한 프로토콜 제거
     *
     * @param url URL 문자열
     * @return 안전한 URL
     */
    public static String sanitizeUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }

        String lowerUrl = url.toLowerCase().trim();

        // 위험한 프로토콜 차단
        if (lowerUrl.startsWith("javascript:") ||
                lowerUrl.startsWith("data:") ||
                lowerUrl.startsWith("vbscript:") ||
                lowerUrl.startsWith("file:")) {
            return "";
        }

        return url;
    }
}