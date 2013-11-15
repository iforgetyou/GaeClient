package com.zy17.GaeClient.http;

import android.util.Log;
import com.google.protobuf.GeneratedMessage;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtobufRequestClient {

    private static final String TAG = ProtobufRequestClient.class.getName();
    public static Map<String, String> map = new ConcurrentHashMap<String, String>();
    private HttpRequestBase request;
    private static HttpClient client = null;

    public ProtobufRequestClient() {
        super();

    }

    private static synchronized HttpClient getClient() {
        if (client == null) {
            final HttpParams httpParams = new BasicHttpParams();
            client = new DefaultHttpClient(httpParams);
        }

        return client;
    }

    private boolean canceled = false;

    public void cancel() {
        canceled = true;
        if (request != null)
            request.abort();
    }


    public byte[] doPost(String url, GeneratedMessage messages) {
        return null;
    }

    protected byte[] doGet(String url) throws Exception {
        Log.d(TAG, "发起get请求:" + url);
        HttpClient client = getClient();

        canceled = false;
        request = new HttpGet(url);
        addRequestParam(request);
        HttpResponse response = null;
        try {
            response = client.execute(request);
            switch (response.getStatusLine().getStatusCode()) {
                case HttpStatus.SC_OK:
                    String eTag = response.getFirstHeader("ETag").getValue();
                    map.put(url, eTag);
                    return IOUtils.toByteArray(response.getEntity().getContent());
                case HttpStatus.SC_NOT_MODIFIED:
                    Log.d(TAG,url+"  NOT_MODIFIED");
                    break;
                default:
                    throw new Exception("请求网络数据失败" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            if (!canceled) {
                Log.e(TAG, "IOException", e);
            }
            throw e;
        }
        return null;
    }

    private HttpRequestBase addRequestParam(HttpRequestBase request) {
        request.setHeader("Content-Type", "application/x-protobuf");
        request.setHeader("AppKey", "dGVzdDp0ZXN0");
        if (request instanceof HttpGet) {
            URI uri = request.getURI();
            request.setHeader("If-None-Match", map.get(uri.toString()));
        }

        return request;
    }
}