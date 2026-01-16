package kr.co.finger.damoa.commons;

public class FullHalfWidthUtils {
    /**
     * 전문에서 추출하여 사용할 때 사용.
     * 전문중에 전각문자로 처리해야 하는 항목인 경우 이 메소드로 전처리한 후에 비즈니스로직에 사용해야 함.
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char)(source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 영문 특수기호를 전각문자로 변경함.
     * 전문으로 처리할 때에 사용
     * 전문중에 전각문자로 처리해야 하는 항목 같은 경우 이 메소드로 전처리해야 함.
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    private static boolean isEmpty(CharSequence  str) {
        return (str == null || str.length() == 0);
    }

    public static void main(String[] args) {
//        System.out.println(FullHalfWidthUtils.fullWidthToHalfWidth("！＼＂＃＄％＆"));
//        System.out.println(FullHalfWidthUtils.halfWidthToFullWidth("!\\\"#$%&12345"));
//        System.out.println(FullHalfWidthUtils.halfWidthToFullWidth("12345"));
//        System.out.println(FullHalfWidthUtils.halfWidthToFullWidth("가나다라마"));

        char[] chars = new char[1];
        chars[0] = (char)12288;

        String k = "핑거";
        String kk = new String(chars);
        String result = FullHalfWidthUtils.fullWidthToHalfWidth(k);
        String _result = FullHalfWidthUtils.halfWidthToFullWidth(result);

        if (k.equals(_result)) {
            System.out.println("OK");
        } else {
            System.out.println("NO");
        }

    }
}
