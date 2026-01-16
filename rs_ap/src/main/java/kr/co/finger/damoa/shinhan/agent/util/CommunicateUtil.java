package kr.co.finger.damoa.shinhan.agent.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kr.co.finger.damoa.model.msg.NoticeMessage;
import kr.co.finger.damoa.model.msg.QueryMessage;
import kr.co.finger.damoa.shinhan.agent.domain.model.ChaDO;
import kr.co.finger.damoa.shinhan.agent.model.CommunicateDTO;
import kr.co.finger.msgagent.util.JsonUtil;
import kr.co.finger.shinhandamoa.data.table.model.TransApiRelayData;
import kr.co.finger.shinhandamoa.data.table.model.xrcpmas.XrcpmasDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommunicateUtil {

    private final Gson gson;

    /**
     * 입금 통지 중계 서버와 비동기적으로 통신하여 결과를 CompletableFuture로 반환한다
     *
     * @param reqUri     요청할 URl
     * @param xrcpmasDTO 요청 본문 List<XrcpmasDTO>
     * @return CompletableFuture 객체 반환
     */
    // 입금통지 방식 기관에게 입금내역 전송
    public CompletableFuture<Boolean> communicateWithNotiServer(String reqUri, XrcpmasDTO xrcpmasDTO) {

        // CompletableFutur를 사용한 비동기 처리
        return CompletableFuture.supplyAsync(() -> {

            // 요청 바디 JSON 데이터로 변환
            String jsonBody = gson.toJson(xrcpmasDTO);
            log.info("입금 통지 중계 서버와 통신이 요청 바디 : {}", jsonBody);

            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            basicHttpEntity.setContent(new ByteArrayInputStream(jsonBody.getBytes(Charset.forName("UTF-8"))));
            basicHttpEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

            // 통신할 URL 설정
//            HttpPost httpPost = new HttpPost(baseUrl + uri);
            HttpPost httpPost = new HttpPost(reqUri);
            httpPost.setEntity(basicHttpEntity);

            // 요청 실행 후 응답 반환
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {

                // 응답 상태 코드 확인
                int statusCode = response.getStatusLine().getStatusCode();
                log.info("======================");
                log.info("======================");
                log.info("statusCode {}", statusCode);
                log.info("======================");
                log.info("======================");


                if (statusCode >= 200 && statusCode < 300) { // 응답이 정상일 때
                    InputStream inputStream = response.getEntity().getContent();
                    // 응답 파싱하여 객체로 변환
//                    CommunicateDTO.Response communicateResponse = parserResponse(inputStream);
//                    log.info("communicateResponse {}", communicateResponse.toString());

//                    Boolean result = true;

                    return true ; // communicateResponse;
                } else if (statusCode >= 400 && statusCode < 500) {
                    log.info("서버를 찾을 수 없습니다.", response.getStatusLine());
                    throw new HttpClientErrorException(HttpStatus.valueOf(statusCode));
                } else if (statusCode >= 500 && statusCode < 600) {
                    log.info("중계 서버 에러입니다.", response.getStatusLine());
                    throw new HttpServerErrorException(HttpStatus.valueOf(statusCode));
                }
            } catch (IOException e) {
                log.error("입금 통지 중계서버 통신 오류 {}", e.getMessage(), e);
                throw new RuntimeException("입금 통지 중계서버 통신 오류 {}", e);
            } catch (Exception e) {
            log.error("입금 통지 중계서버 기타 오류 {}", e.getMessage(), e);
            throw new RuntimeException("입금 통지 중계서버 기타 오류 {}", e);
        }

            return null;
        });
    }


    // 중계방식 기관에게 수취조회요청 정보 전송
    public QueryMessage sendQueryRequest(String reqUrl, QueryMessage queryMessage) {

        log.info("=== 중계방식 기관에게 수취조회요청 정보 전송하기 ===");
        String responseBody = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        QueryMessage resQueryMessage = null;

        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            ObjectMapper obMap = new ObjectMapper();

            httpclient = HttpClients.createDefault();

            StringEntity stringEntity = new StringEntity(obMap.writeValueAsString(queryMessage), "utf-8");
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            log.info("reqUrl ===> " + reqUrl);
            log.info("Executing request " + httpPost.getRequestLine());
            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            log.info("status " + status);

            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                responseBody = entity != null ? EntityUtils.toString(entity) : null;

                log.debug(" === 수취조회 응답 responseBody START===");
                log.debug(responseBody.toString());
                log.debug(" === 수취조회 응답 responseBody END ===");

                QueryMessage resQueryMsg = gson.fromJson(responseBody, QueryMessage.class);

                log.debug(" === 수취조회 응답 resQueryMsg START===");
                log.debug(resQueryMsg.toString());
                log.debug(" === 수취조회 응답 resQueryMsg END ===");

            } else {
                HttpEntity entity = (HttpEntity) response.getEntity();
                responseBody = entity != null ? EntityUtils.toString(entity) : null;
                throw new ClientProtocolException("Unexpected response: (" + responseBody+")");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (response != null) response.close();
                if (httpclient != null) httpclient.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        // JSON 형식의 응답을 객체로 변환하여 반환
        return gson.fromJson(responseBody, QueryMessage.class);
    }

    public NoticeMessage sendNoticeRequest(String reqUrl, NoticeMessage noticeMessage) {

        log.info("=== 중계방식 기관에게 입금통지정보 전송하기 ===");
        String responseBody = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        QueryMessage resQueryMessage = null;

        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            ObjectMapper obMap = new ObjectMapper();

            httpclient = HttpClients.createDefault();

            StringEntity stringEntity = new StringEntity(obMap.writeValueAsString(noticeMessage), "utf-8");
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            log.info("reqUrl ===> " + reqUrl);
            log.info("Executing request " + httpPost.getRequestLine());
            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            log.info("status " + status);

            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                responseBody = entity != null ? EntityUtils.toString(entity) : null;
                NoticeMessage resNoticeMsg = gson.fromJson(responseBody, NoticeMessage.class);

                log.debug(" === 입금통지 응답 responseBody START===");
                log.debug(resNoticeMsg.toString());
                log.debug(" === 입금통지 응답 responseBody END ===");

            } else {
                HttpEntity entity = (HttpEntity) response.getEntity();
                responseBody = entity != null ? EntityUtils.toString(entity) : null;
                throw new ClientProtocolException("Unexpected response: (" + responseBody+")");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (response != null) response.close();
                if (httpclient != null) httpclient.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // JSON 형식의 응답을 객체로 변환하여 반환
        return gson.fromJson(responseBody, NoticeMessage.class);
    }


    public NoticeMessage sendCancelRequest(String reqUrl, NoticeMessage noticeMessage) {

        log.info("=== 중계방식 기관에게 입금통지취소정보 전송하기 ===");
        String responseBody = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        QueryMessage resQueryMessage = null;

        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            ObjectMapper obMap = new ObjectMapper();

            httpclient = HttpClients.createDefault();

            StringEntity stringEntity = new StringEntity(obMap.writeValueAsString(noticeMessage), "utf-8");
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            log.info("reqUrl ===> " + reqUrl);
            log.info("Executing request " + httpPost.getRequestLine());
            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            log.info("status " + status);

            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                responseBody = entity != null ? EntityUtils.toString(entity) : null;
                NoticeMessage resNoticeMsg = gson.fromJson(responseBody, NoticeMessage.class);

                log.debug(" === 입금통지 취소 응답 responseBody START===");
                log.debug(resNoticeMsg.toString());
                log.debug(" === 입금통지 취소 응답 responseBody END ===");

            } else {
                HttpEntity entity = (HttpEntity) response.getEntity();
                responseBody = entity != null ? EntityUtils.toString(entity) : null;
                throw new ClientProtocolException("Unexpected response: (" + responseBody+")");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (response != null) response.close();
                if (httpclient != null) httpclient.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // JSON 형식의 응답을 객체로 변환하여 반환
        return gson.fromJson(responseBody, NoticeMessage.class);
    }

    // 입력 스트림에서 응답을 파싱하여 객체로 반환하는 메소드
//    private CommunicateDTO.Response parserResponse(InputStream inputStream) throws IOException {
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//            StringBuilder responseBuilder = new StringBuilder();
//            String line;
//
//            // 입력 스트림에서 읽어온 데이터를 문자열로 전환
//            while ((line = reader.readLine()) != null) {
//                responseBuilder.append(line);
//            }
//            String responseBody = responseBuilder.toString();
//            log.info("responseBody============={}", responseBody);
//
//            // JSON 형식의 응답을 객체로 변환하여 반환
//            return gson.fromJson(responseBody, CommunicateDTO.Response.class);
//        }
//    }

}
