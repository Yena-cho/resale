package kr.co.finger.damoa.commons.io;

import kr.co.finger.damoa.commons.JsonUtils;
import kr.co.finger.damoa.commons.model.CorpInfoBean;
import kr.co.finger.damoa.commons.model.CorpInfoBeans;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class HttpClientHelper {

    private static OkHttpClient client;
    static {
        client = new OkHttpClient()
                    .newBuilder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .build();
    }
    public static VaBean call(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String result =  response.body().string();
        return JsonUtils.toObject(result, VaBean.class);
    }

    public static String callAsString(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static CorpInfoBeans callCorpInfo(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String result =  response.body().string();
        return JsonUtils.toObject(result, CorpInfoBeans.class);
    }
    public static void downloadCorpInfo(String filePath,String url) throws IOException {
        BufferedSink sink = null;
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            File downloadedFile = new File(filePath);
            if (downloadedFile.exists()==false) {
                FileUtils.touch(downloadedFile);
            }
            sink = Okio.buffer(Okio.sink(downloadedFile));
            sink.writeAll(response.body().source());

        }finally {
            if (sink != null) {
                sink.close();
            }
        }


    }

    public static void main(String[] args) throws IOException {
        String url = "http://192.168.0.175:8081/new/virtualaccount/?corpCd=20005762&agencyCd=&branchCd=&count=10";
        System.out.println(HttpClientHelper.call(url));
        // 성공하면 result OK
        // 실패하면 result OK 가 아닌 다른 값
    }
}
