package kr.co.finger.damoa.commons;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Damoas {
    private Damoas(){}

    private static Map<String, String> CMS_ERROR = new HashMap<>();
    static {
        CMS_ERROR.put("0012","계좌번호 오류 또는 계좌번호 없음");
        CMS_ERROR.put("0014","사업자등록번호 또는 생년월일 오류");
        CMS_ERROR.put("0015","계정과목 오류");
        CMS_ERROR.put("0017","출금이체 미신청계좌");
        CMS_ERROR.put("0018","출금이체신청 임의해지");
        CMS_ERROR.put("0019","출금이체신청 참가기관 해지");
        CMS_ERROR.put("0020","출금동의 증빙자료 부재로 인한 임의해지");
        CMS_ERROR.put("0021","잔액 또는 지불가능 잔액 부족");
        CMS_ERROR.put("0022","입금한도 초과");
        CMS_ERROR.put("0024","계좌변경으로 인한 출금 이체신청 해지");
        CMS_ERROR.put("0031","해약계좌");
        CMS_ERROR.put("0032","가명계좌 또는 실명미확인");
        CMS_ERROR.put("0033","잡 좌");
        CMS_ERROR.put("0034","법적제한계좌, 지급정지 또는 사고신고계좌");
        CMS_ERROR.put("0035","압류․가압류 계좌");
        CMS_ERROR.put("0036","잔액증명발급 계좌");
        CMS_ERROR.put("0037","연체계좌 또는 지점통제계좌");
        CMS_ERROR.put("0041","참가기관시스템 오류");
        CMS_ERROR.put("0051","기타 오류");
        CMS_ERROR.put("0065","법인계좌 사용불가");
        CMS_ERROR.put("0066","투자자예탁금이 아님");
        CMS_ERROR.put("A011","신청일자 오류");
        CMS_ERROR.put("A018","납부자번호체계 오류");
        CMS_ERROR.put("A012","신청구분 오류  (출금이체신청내역(EB13)에서 신청구분이 신규(1), 해지(3), 임의해지(7)이 아닌 경우)");
        CMS_ERROR.put("0011","참가기관(은행)점코드 오류");
        CMS_ERROR.put("0061","의뢰금액이 0원인 경우");
        CMS_ERROR.put("0062","건당 이체금액한도 초과");
        CMS_ERROR.put("0068","통장기재내용 Field에 HEX 20(Space)보다 작은 값이 왔을 때");
        CMS_ERROR.put("0075","①출금의뢰내역(EB21, EC21)의 출금형태항목에 전액출금(1) 또는 부분출금가능(0, 2, 3, 4, 5, 6)이 아닌 값이 왔을 경우 ②출금의뢰내역에 의뢰금액이 140원 미만(EB21)이거나 300원 미만(EC21)인 경우");
        CMS_ERROR.put("0077","계좌변경 내역을 상대 참가기관(변경 전 또는 변경 후)에서 전송하지 않은 경우");
        CMS_ERROR.put("0078","이용기관이 EI13을 전송하지 않아 EB13 신규 신청내역을 불능처리 하는 경우");
        CMS_ERROR.put("0079","이용기관이 계좌변경 내역을 SET로 처리하지 않아 결제원이 참가기관으로 대행응답 하는 경우");
        CMS_ERROR.put("0081","Record 구분 항목이 H, R 또는 T가 아니거나, 일련번호가 오류인 경우");
        CMS_ERROR.put("0087","한글 오류 (한글코드 Field에 한글코드이외의 값이 올 경우)");
        CMS_ERROR.put("0088","영문자․숫자 오류 (영․숫자코드 Field에 영․숫자코드 이외의 값이 올 경우)");
        CMS_ERROR.put("0089","Space 오류 (Space Field에 Space가 아닌 값이 올 경우)");
        CMS_ERROR.put("0090","All Zero 오류 (All Zero Field에 All Zero가 아닌 값이 올 경우)");
        CMS_ERROR.put("0091","생년월일 대신 주민등록번호로 신청하는 경우");
        CMS_ERROR.put("0096","CMS 미참가 참가기관(은행)");
        CMS_ERROR.put("0098","Alpha-Numeric + Space 오류");
        CMS_ERROR.put("9998","기타 오류 (표준불능코드에 정의되지 않은 사유로 발생한 오류)");
        CMS_ERROR.put("9999","참가기관(은행)시스템 장애");
        CMS_ERROR.put("A013","납부자번호 상이 또는 없음");
        CMS_ERROR.put("A016","이중신청");
        CMS_ERROR.put("A017","기타오류");
        CMS_ERROR.put("A019","출금요청 중 또는 출금일 도래");
    }
    public static String toString(int length) {
        return StringUtils.leftPad(length+"", 4,"0");
    }

    public static String appendLength(String msg, Charset charset) {
        int length = msg.getBytes(charset).length;
        return Damoas.toString(length) + msg;
    }
    public static String checkAdminInfo(String adminInfo) {
        if ("001".equalsIgnoreCase(adminInfo)) {
            return "개시";
        } else if ("002".equalsIgnoreCase(adminInfo)) {
            return "재개시";
        } else if ("003".equalsIgnoreCase(adminInfo)) {
            return "종료예고";
        } else if ("004".equalsIgnoreCase(adminInfo)) {
            return "종료";
        }else if ("005".equalsIgnoreCase(adminInfo)) {
            return "장애";
        }else if ("006".equalsIgnoreCase(adminInfo)) {
            return "장애회복";
        } else if ("007".equalsIgnoreCase(adminInfo)) {
            return "TEST-CALL";
        } else {
            return "잘못된항목 " + adminInfo;
        }
    }


    public static String findCmsError(String errorCode) {
        if (CMS_ERROR.containsKey(errorCode)) {
            return CMS_ERROR.get(errorCode);
        } else {
            return "";
        }
    }
    public static String toFirstCapital(String id) {
        char[] chars = id.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static Long toLong(String value) {
        if (StringUtils.isEmpty(value)) {
            return Long.valueOf(0);
        } else {
            return Long.valueOf(value.trim());
        }
    }

    public static boolean isEmpty(String value) {
        return StringUtils.isEmpty(value);
    }

    public static String sum(String amount1, String amount2) {
        return (toLong(amount1) + toLong(amount2))+"";
    }

    public static String makeKey(String notimas, String notidet) {
        return notimas + "_" + notidet;
    }
}
