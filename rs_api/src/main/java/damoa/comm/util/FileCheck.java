package damoa.comm.util;

import damoa.Constants;
import damoa.comm.data.ServerInfo;
import damoa.comm.log.GeneLog;
import damoa.conf.Property;

import java.io.*;


public class FileCheck {

    /*
     * 회원등록 접수 파일
     */
    public static FileOutputStream writeRecvFile(String custId, String procDate, String procSeq,
                                                 String dataKind, GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;
        FileOutputStream fos = null;

        String strOutFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strOutFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strOutFile = ServerInfo.PAY_DATA_PATH + fileName;
        }

        File file = new File(strOutFile);

        if (file.isFile()) {
            gLog.println("해당 파일["+strOutFile+"]이 존재합니다.");
//            file.delete();
//            GeneLog.println("파일을 삭제했습니다.");
        } else {
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return fos;
    }

    /*
     * 파일 존재유무
     */
    public static boolean isRecvFile(String custId, String procDate, String procSeq, String dataKind,
                                     GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;

        String strInFile = "";
        if ("PAY".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.PAY_DATA_PATH + fileName;
        }

        File file = new File(strInFile);

        if (file.isFile()) {
            gLog.println("기존 파일["+strInFile+"]이 존재합니다.");
            return true;
        }
        return false;
    }

    public static boolean isDoneFile(String custId, String procDate, String procSeq, String dataKind,
                                     GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;

        String strInFile = "";
        if ("PAY".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.PAY_DATA_PATH + fileName;
        } else if ("RCP".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.RCP_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.RCP_DATA_PATH + fileName;
        }

        File file = new File(strInFile);

        if (file.isFile()) {
            gLog.println("기존 파일["+strInFile+"]이 존재합니다.");
            return true;
        }
        return false;
    }

    /*
     * 수신 파일 열기
     */
    public static FileInputStream readRecvFile(String custId, String procDate, String procSeq,
                                               String dataKind, GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;
        FileInputStream fis = null;

        String strInFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.PAY_DATA_PATH + fileName;
        }

        File file = new File(strInFile);

        if (!file.isFile()) {
            gLog.println("해당 파일["+strInFile+"]이 존재하지 않습니다.");
        } else {
            try {
                fis = new FileInputStream(strInFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return fis;
    }

    /*
     * 수신 파일 열기
     */
    public static FileInputStream readDoneFile(String custId, String procDate, String procSeq,
                                               String dataKind, GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;
        FileInputStream fis = null;

        String strInFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.PAY_DATA_PATH + fileName;
        } else if ("RCP".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.RCP_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.RCP_DATA_PATH + fileName;
        }

        File file = new File(strInFile);

        if (!file.isFile()) {
            gLog.println("해당 파일["+strInFile+"]이 존재하지 않습니다.");
        } else {
            try {
                fis = new FileInputStream(strInFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return fis;

    }

    /*
     * 등록 임시 파일 쓰기
     */
    public static FileOutputStream writeReturnFile(String custId, String procDate, String procSeq,
                                                   String dataKind, GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;
        FileOutputStream fos   = null;

        String strOutFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strOutFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strOutFile = ServerInfo.PAY_DATA_PATH + fileName;
        }

        File file = new File(strOutFile);

        if (file.isFile()) {
            gLog.println("해당 파일["+strOutFile+"]이 존재합니다.");
        } else {
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return fos;
    }

    /*
     * 전송완료 파일 쓰기
     */
    public static FileOutputStream writeDoneFile(String custId, String procDate, String procSeq,
                                                 String dataKind, GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;
        FileOutputStream fos   = null;

        String strOutFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strOutFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strOutFile = ServerInfo.PAY_DATA_PATH + fileName;
        } else if ("RCP".equals(dataKind)) {
            if (testFlag) strOutFile = ServerInfo.RCP_DATA_TESTPATH + fileName;
            else strOutFile = ServerInfo.RCP_DATA_PATH + fileName;
        }

        File file = new File(strOutFile);

        if (file.isFile()) {
            gLog.println("해당 파일["+strOutFile+"]이 존재합니다.");
        } else {
            try {
                fos = new FileOutputStream(file);
                gLog.println("["+fileName+"] 파일 생성...");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return fos;
    }

    public static String readOneLine(FileInputStream datFile, String dataKind) {

        byte readBytes[] = new byte[4096];
        boolean boolCheckFile = true;
        int intCnt = 0;
        int readLength = Property.MSGSIZE_LENGTH;
        try{
            while (boolCheckFile) {
                int intRead = datFile.read();
                if (intRead == -1) {
                    boolCheckFile = false;
                }

                Integer iRead = new Integer(intRead);
                readBytes[intCnt] = iRead.byteValue();
                intCnt++;
                if (intCnt == readLength) {
                    boolCheckFile = false;
                }
            }
        } catch (IOException e1){
        }
        
        return new String(readBytes, 0, intCnt, Constants.MY_CHARSET);
    }

    public static byte[] readOneLineByte(FileInputStream datFile, String dataKind) {

        byte readBytes[] = new byte[4096];
        boolean boolCheckFile = true;
        int intCnt = 0;
        int readLength = 0;
        if ("CUS".equals(dataKind)) {
            readLength = Property.MSGSIZE_LENGTH;
        } else if ("PAY".equals(dataKind)) {
            readLength = Property.MSGSIZE_LENGTH;
        }
        try{
            while (boolCheckFile) {
                int intRead = datFile.read();
                if (intRead == -1) {
                    boolCheckFile = false;
                }

                Integer iRead = new Integer(intRead);
                readBytes[intCnt] = iRead.byteValue();
                intCnt++;
                if (intCnt == readLength) {
                    boolCheckFile = false;
                }
            }
        } catch (IOException e1){
        }

        return readBytes;
    }

    /*
     * 이체 등록 수신 파일 열기
     */
    public static boolean moveFailFile(String custId, String procDate, String procSeq, String dataKind,
                                       GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;
        boolean isSuccess = false;

        String strInFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.PAY_DATA_PATH + fileName;
        }

        String strOutFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strOutFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strOutFile = ServerInfo.PAY_DATA_PATH + fileName;
        }

        File file = new File(strInFile);
        File outFile = new File(strOutFile);

        gLog.println("["+strInFile+"]파일옮기기시작");
        if (!file.isFile()) {
            gLog.println("["+strInFile+"]해당 파일이 존재하지 않습니다.");
            return false;
        }

        isSuccess = file.renameTo(outFile);

        if (!isSuccess) {
            gLog.println("["+strInFile+"]["+strOutFile+"]해당 파일을 옮기는중 실패하였습니다.");
        }
        if (isSuccess) {
            if(!file.delete()) {
                gLog.println("["+strInFile+"]해당 파일을 삭제중 실패하였습니다.");
            }
        }

        return isSuccess;
    }

    /*
     * 이체 등록 수신 파일 열기
     */
    public static boolean deleteReturnFile(String custId, String procDate, String procSeq, String dataKind,
                                           GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;

        String strInFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.PAY_DATA_PATH + fileName;
        }

        File file = new File(strInFile);

        gLog.println("["+strInFile+"]파일삭제시작");
        if (!file.isFile()) {
            gLog.println("["+strInFile+"]해당 파일이 존재하지 않습니다.");
            return false;
        }

        if(!file.delete()) {
            gLog.println("["+strInFile+"]해당 파일을 삭제중 실패하였습니다.");
            return false;
        }
        return true;
    }

    public static boolean deleteDoneFile(String custId, String procDate, String procSeq, String dataKind,
                                         GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;

        String strInFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.PAY_DATA_PATH + fileName;
        } else if ("RCP".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.RCP_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.RCP_DATA_PATH + fileName;
        }

        File file = new File(strInFile);

        gLog.println("["+strInFile+"]파일삭제시작");
        if (!file.isFile()) {
            gLog.println("["+strInFile+"]해당 파일이 존재하지 않습니다.");
            return false;
        }

        if(!file.delete()) {
            gLog.println("["+strInFile+"]해당 파일을 삭제중 실패하였습니다.");
            return false;
        }
        return true;
    }

    /*
     * 이체 등록 수신 파일 열기
     */
    public static boolean deleteReceiveFile(String custId, String procDate, String procSeq, String dataKind,
                                            GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;

        String strInFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.PAY_DATA_PATH + fileName;
        }

        File file = new File(strInFile);

        gLog.println("["+strInFile+"]파일삭제시작");
        if (!file.isFile()) {
            gLog.println("["+strInFile+"]해당 파일이 존재하지 않습니다.");
            return false;
        }

        if(!file.delete()) {
            gLog.println("["+strInFile+"]해당 파일을 삭제중 실패하였습니다.");
            return false;
        }
        return true;
    }

    /*
     * 회원등록 전송 결과 리턴 파일
     */
    public static FileInputStream readReturnFile(String custId, String procDate, String procSeq,
                                                 String dataKind, GeneLog gLog, boolean testFlag) {

        String fileName = procDate+"-"+procSeq+"."+custId;
        FileInputStream fis = null;

        String strInFile = "";

        if ("PAY".equals(dataKind)) {
            if (testFlag) strInFile = ServerInfo.PAY_DATA_TESTPATH + fileName;
            else strInFile = ServerInfo.PAY_DATA_PATH + fileName;
        }

        File file = new File(strInFile);

        if (!file.isFile()) {
            gLog.println("["+strInFile+"]해당 파일이 존재하지 않습니다.");
            return null;
        }

        try {
            fis = new FileInputStream(strInFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fis;
    }
}
