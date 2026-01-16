package damoa.comm.tele;

import damoa.Constants;
import damoa.comm.util.ComCheck;

public class EndTele {

    public static String makeEndTele(byte[] rcvSByte, int rcvSLen) {
        String msg = "";
        msg = "E" + new String(rcvSByte,1,9,Constants.MY_CHARSET) + "YE000" + ComCheck.makeSpaceData("정상",30) +
                new String(rcvSByte,45,18,Constants.MY_CHARSET) + ComCheck.makeSpaceData("",635) + "\r\n";
        return msg;
    }

}
