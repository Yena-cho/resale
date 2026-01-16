package damoa.server.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.charset.Charset;

public class AegisRMsg {
    public String DrecordType = "";
    public String serviceId = "";
    public String workField = "";
    public String seccessYN = "";
    public String resultCd = "";
    public String resultMsg = "";
    public String trNo = "";
    public String vaNo = "";
    public String payMasMonth = "";
    public String payMasDt = "";
    public String rcpDt = "";
    public String rcpUsrName = "";
    public String bnkCd = "";
    public String bchCd = "";
    public String mdGubn = "";
    public String trnDay = "";
    public String rcpAmt = "";
    public String vaNoBnkCd = "";
    public String content1 = "";
    public String content2 = "";
    public String content3 = "";
    public String content4 = "";
    public String svecd = "";
    public String chaTrNo = "";
    public String adjfiGrpCd = "";
    public String payItemNm = "";
    public String payItemAmt = "";

    public String rcvMsg = "";

    public AegisRMsg() {
    }

    public void setMsg(byte[] rcvByte) {

        this.rcvMsg = new String(rcvByte, Charset.forName("EUC-KR"));

        DrecordType = newString(rcvByte, 0, 1);  //
        serviceId = newString(rcvByte, 1, 8);  //      가맹점코드
        workField = newString(rcvByte, 9, 1);  //      처리내역
        seccessYN = newString(rcvByte, 10, 1);  //     처리결과
        resultCd = newString(rcvByte, 11, 4);  //     처리결과
        resultMsg = newString(rcvByte, 15, 30);  //     처리결과
        trNo = newString(rcvByte, 45, 30).toUpperCase();//     청구거래번호
        vaNo = newString(rcvByte, 75, 20);  //    가상계좌번호
        payMasMonth = newString(rcvByte, 95, 6);  //     청구월
        payMasDt = newString(rcvByte, 101, 8);  //    청구일자
        rcpDt = newString(rcvByte, 109, 14);  //   수납일시
        rcpUsrName = newString(rcvByte, 123, 20);  //   수납계좌명
        bnkCd = newString(rcvByte, 143, 3);  //    처리일자
        bchCd = newString(rcvByte, 146, 4);  //    처리시간
        mdGubn = newString(rcvByte, 150, 1);  //    매체구분
        trnDay = newString(rcvByte, 151, 14);  //   전송일시
        rcpAmt = newString(rcvByte, 165, 10);  //   수납금액
        content1 = newString(rcvByte, 175, 30);  //   참조1
        content2 = newString(rcvByte, 205, 30);  //   참조2
        content3 = newString(rcvByte, 235, 30);  //   참조3
        content4 = newString(rcvByte, 265, 30);  //   참조4
        svecd = newString(rcvByte, 295, 3);  //    수납구분
        chaTrNo = newString(rcvByte, 420, 30);  //   항목거래번호
        adjfiGrpCd = newString(rcvByte, 450, 5);  //    신한은행발급배분키
        payItemNm = newString(rcvByte, 455, 50);  //   항목명
        payItemAmt = newString(rcvByte, 505, 10);  //   항목금액
//			DrecordType  = new String(rcvByte,0,1).trim();
//			serviceId    = new String(rcvByte,1,8).trim();     // 가맹점코드
//			workField    = new String(rcvByte,9,1).trim();     // 처리내역
//			seccessYN    = new String(rcvByte,10,1).trim();    // 처리결과
//			resultCd     = new String(rcvByte,11,4).trim();    // 처리결과
//			resultMsg    = new String(rcvByte,15,30).trim();    // 처리결과
//			trNo         = new String(rcvByte,45,30).trim().toUpperCase();    // 청구거래번호
//			vaNo         = new String(rcvByte,75,20).trim();   // 가상계좌번호
//			payMasMonth  = new String(rcvByte,95,6).trim();    // 청구월
//			payMasDt     = new String(rcvByte,101,8).trim();   // 청구일자
//			rcpDt		 = new String(rcvByte,109,14).trim();  // 수납일시
//			rcpUsrName   = new String(rcvByte,123,20).trim();  // 수납계좌명
//			bnkCd     	 = new String(rcvByte,143,3).trim();   // 처리일자
//			bchCd     	 = new String(rcvByte,146,4).trim();   // 처리시간
//			mdGubn     	 = new String(rcvByte,150,1).trim();   // 매체구분
//			trnDay	     = new String(rcvByte,151,14).trim();  // 전송일시
//			rcpAmt     	 = new String(rcvByte,165,10).trim();  // 수납금액
//			content1 	 = new String(rcvByte,175,30).trim();  // 참조1
//			content2 	 = new String(rcvByte,205,30).trim();  // 참조2
//			content3 	 = new String(rcvByte,235,30).trim();  // 참조3
//			content4 	 = new String(rcvByte,265,30).trim();  // 참조4
//			svecd 		 = new String(rcvByte,295,3).trim();   // 수납구분
//			chaTrNo      = new String(rcvByte,420,30).trim();  // 항목거래번호
//			adjfiGrpCd   = new String(rcvByte,450,5).trim();   // 신한은행발급배분키
//			payItemNm    = new String(rcvByte,455,50).trim();  // 항목명
//			payItemAmt   = new String(rcvByte,505,10).trim();  // 항목금액

    }

    private String newString(byte[] rcvByte,int offset,int length) {
        return new String(rcvByte,offset,length,Charset.forName("EUC-KR")).trim();
    }
    public void clear() {
        DrecordType = "";
        serviceId = "";
        workField = "";
        seccessYN = "";
        resultCd = "";
        resultMsg = "";
        trNo = "";
        vaNo = "";
        payMasMonth = "";
        payMasDt = "";
        rcpDt = "";
        rcpUsrName = "";
        bnkCd = "";
        bchCd = "";
        mdGubn = "";
        trnDay = "";
        rcpAmt = "";
        vaNoBnkCd = "";
        content1 = "";
        content2 = "";
        content3 = "";
        content4 = "";
        svecd = "";
        chaTrNo = "";
        adjfiGrpCd = "";
        payItemNm = "";
        payItemAmt = "";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("DrecordType", DrecordType)
                .append("serviceId", serviceId)
                .append("workField", workField)
                .append("seccessYN", seccessYN)
                .append("resultCd", resultCd)
                .append("resultMsg", resultMsg)
                .append("trNo", trNo)
                .append("vaNo", vaNo)
                .append("payMasMonth", payMasMonth)
                .append("payMasDt", payMasDt)
                .append("rcpDt", rcpDt)
                .append("rcpUsrName", rcpUsrName)
                .append("bnkCd", bnkCd)
                .append("bchCd", bchCd)
                .append("mdGubn", mdGubn)
                .append("trnDay", trnDay)
                .append("rcpAmt", rcpAmt)
                .append("vaNoBnkCd", vaNoBnkCd)
                .append("content1", content1)
                .append("content2", content2)
                .append("content3", content3)
                .append("content4", content4)
                .append("svecd", svecd)
                .append("chaTrNo", chaTrNo)
                .append("adjfiGrpCd", adjfiGrpCd)
                .append("payItemNm", payItemNm)
                .append("payItemAmt", payItemAmt)
                .append("rcvMsg", rcvMsg)
                .toString();
    }
}
