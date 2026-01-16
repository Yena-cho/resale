package kr.co.finger.damoa.commons.biz;

public class VirtualAccountNoChecker {
    private static char[] WEIGHT_13 = "121212121212".toCharArray();
    private static char[] WEIGHT_14 = "8765438765876".toCharArray();
    public static String find14CrcNo(String accountNo) {
    	char[] chars = accountNo.toCharArray();
        int sum = 0;
        int value = 0;
        int remainder = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            char _w = WEIGHT_14[i];
            sum += Integer.valueOf(c+"") * Integer.valueOf(_w+"");
        }

        remainder = sum % 11;

        if (remainder == 0) {
            return "1";
        } else if (remainder == 1) {
            return "0";
        } else if(remainder==10){
            return "0";
        }else {
            return (11 - remainder)+"";
        }
    }

    public static String find13CrcNo(String accountNo) {
        char[] chars = accountNo.toCharArray();
        int sum = 0;
        int value = 0;
        int remainder = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            char _w = WEIGHT_13[i];
            value = Integer.valueOf(c+"") * Integer.valueOf(_w+"");
            sum += value / 10;
            sum += value % 10;
        }

        remainder = sum % 10;
        if (remainder == 0) {
            return "0";
        } else {
            return (10 - remainder) + "";
        }
    }

    public static void main(String[] args) {
//        System.out.println(VirtualAccountNoChecker.find13CrcNo("1111111111143"));
        System.out.println(VirtualAccountNoChecker.find14CrcNo("5621317482382"));
        long kk = 5621317482382l;
        for (int i = 0; i < 10; i++) {
            long _kk = kk+i;
            String k = VirtualAccountNoChecker.find14CrcNo((_kk)+"");
            System.out.println((_kk+"")+k);
        }
    }
}
