package damoa.comm.util;

import java.util.ArrayList;

public class DataCheck {

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList getCustInfo(String strCustID) {
        ArrayList list = new ArrayList();

        list.add("1");
        list.add("2");

        return list;
    }

    @SuppressWarnings("rawtypes")
	public static ArrayList getArrAdmExpMem(String custNo, String procKind) {
        ArrayList al = null;
        return al;
    }

    @SuppressWarnings("rawtypes")
	public static ArrayList getArrAbleCD(String custNo, String procKind) {
        return null;
    }

    @SuppressWarnings("rawtypes")
	public static ArrayList getMinMaxAmount(String procKind, String custNo) {
        return null;
    }

    public static long getMonPayAmount(String procKind, String procDate, String custNo) {
        return 0;
    }

}
