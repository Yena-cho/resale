package kr.co.finger.shinhandamoa.sys.service;

import com.finger.shinhandamoa.common.XlsxBuilder;
import kr.co.finger.shinhandamoa.domain.model.ClientDO;
import kr.co.finger.shinhandamoa.domain.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 이용기관 서비스
 * 
 * @author wisehouse@finger.co.kr
 */
@Service
public class ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
    
    @Autowired
    private ClientRepository clientRepository;
    
    /**
     * 이용기관 엑셀파일 생성
     * 
     * @return
     */
    public InputStream getClientExcelInInvoiceFormat() {
        final XlsxBuilder xlsxBuilder = new XlsxBuilder();
        xlsxBuilder.newSheet("거래처등록");
        
        xlsxBuilder.addHeader(0, 0, "거래처코드(30)");
        xlsxBuilder.addHeader(0, 1, "상호(이름)(100)");
        xlsxBuilder.addHeader(0, 2, "대표자명(50)");
        xlsxBuilder.addHeader(0, 3, "업태(50)");
        xlsxBuilder.addHeader(0, 4, "종목(50)");
        xlsxBuilder.addHeader(0, 5, "전화(50)");
        xlsxBuilder.addHeader(0, 6, "Fax(50)");
        xlsxBuilder.addHeader(0, 7, "Email(100)");
        xlsxBuilder.addHeader(0, 8, "검색창내용(50)");
        xlsxBuilder.addHeader(0, 9, "주소1 우편번호(8)");
        xlsxBuilder.addHeader(0, 10, "주소1(500)");
        xlsxBuilder.addHeader(0, 11, "ecount");

        final List<ClientDO> doList = clientRepository.findActive();
        for (ClientDO each : doList) {
            xlsxBuilder.newDataRow();
            
            // 사업자등록번호
            xlsxBuilder.addData(0, each.getIdentityNo());
            // 상호
            xlsxBuilder.addData(1, each.getName());
            // 대표자
            xlsxBuilder.addData(2, each.getOwnerName());
            // 
            xlsxBuilder.addData(3, each.getBusinessType());
            xlsxBuilder.addData(4, each.getBusinessKind());
            xlsxBuilder.addData(5, each.getContactNo());
            xlsxBuilder.addData(6, "");
            xlsxBuilder.addData(7, each.getInvoiceEmail());
            xlsxBuilder.addData(8, "");
            xlsxBuilder.addData(9, "");
            xlsxBuilder.addData(10, each.getAddress());
            xlsxBuilder.addData(11, "ecount");
        }

        InputStream is;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            xlsxBuilder.writeTo(outputStream);
            is = new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            is = new ByteArrayInputStream(new byte[]{});
        }


        return is;
    }
}
