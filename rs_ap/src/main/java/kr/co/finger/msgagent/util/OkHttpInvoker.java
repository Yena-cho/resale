package kr.co.finger.msgagent.util;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class OkHttpInvoker {
    @Value("${okhttp.readTimeout:1000}")
    private int readTimeout;
    @Value("${okhttp.connectTimeout:1000}")
    private int connectTimeout;
    @Value("${okhttp.maxIdleConnections:0}")
    private int maxIdleConnections;
    @Value("${okhttp.keepAliveDuration:10000}")
    private long keepAliveDuration;

    private OkHttpClient okHttpClient;

    @PostConstruct
    public void initialize() {
        if (maxIdleConnections != 0) {
            okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                    .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS))
                    .build();
        }
    }

    public String invoke(String url) throws IOException {
        if (okHttpClient != null) {
            Request request = new Request.Builder()
                    .url(url)
                    .cacheControl(new CacheControl.Builder().noCache().build())
                    .build();
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                return response.body().string();
            }
        } else {
            return "";
        }

    }

    public static void main(String[] args) throws IOException {
    }
}
