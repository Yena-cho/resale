package com.finger.shinhandamoa.sys.setting.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finger.shinhandamoa.common.AbstractXlsxStreamingView;
import com.finger.shinhandamoa.common.XlsxBuilder;
import com.finger.shinhandamoa.sys.setting.dto.VanoMgmt2DTO;


public class ExcelSaveChaVanoOwns extends AbstractXlsxStreamingView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      XlsxBuilder builder,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {


        List<VanoMgmt2DTO> listExcel = (List<VanoMgmt2DTO>) model.get("list");

        if (listExcel == null || listExcel.isEmpty()) {
            logger.warn("엑셀 출력 대상 데이터가 없습니다");
            return;
        }

        builder.newSheet("sheet1");

        String[] headers = {
                "번호  ",
                "발급일자  ",
                "기관코드    ",
                "기관명  ",
                "고객명",
                "가상계좌번호  ",
                "가상계좌상태",
                "최근입금일자"
        };

        for (int i = 0; i < headers.length; i++) {
            builder.addHeader(0, i, headers[i]);
        }

        int rowIndex = 1;
        for(VanoMgmt2DTO dto : listExcel) {
            builder.newDataRow();
            builder.addData(0,rowIndex);
            builder.addData(1,dto.getRegdt());
            builder.addData(2,dto.getChacd());
            builder.addData(3,dto.getChaname());
            builder.addData(4,dto.getCusname());
            builder.addData(5,dto.getVano());
            builder.addData(6,resolveStatus(String.valueOf(dto.getUseyn())));
            builder.addData(7,dto.getLastrcpdate());

            rowIndex++;
        }

    }


    private String resolveStatus(String code) {
        String chastName = "";
        switch (code) {
            case "Y":
                chastName = "사용";
                break;
            case "N":
                chastName = "미사용";
                break;
            case "D":
                chastName = "삭제";
                break;
            default:
                chastName = code;
                break;
        }
        return chastName;
    }

}
