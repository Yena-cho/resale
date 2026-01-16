package kr.co.finger.damoa.commons.io;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkHttpInvoker {
    private int readTimeout;
    private int connectTimeout;
    private int maxIdleConnections;
    private long keepAliveDuration;

    private OkHttpClient okHttpClient;


    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public void setKeepAliveDuration(long keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }

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
