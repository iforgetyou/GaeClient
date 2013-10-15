package com.example.GaeClient;

import com.loopj.android.http.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

/**
 * Created with IntelliJ IDEA.
 * User: yan.zhang
 * Date: 13-10-14
 * Time: 下午5:25
 */
public class GaeClient  {
    private static final String BASE_URL ="http://iforgetyou529.appsp0t.com/";
//    private static final String BASE_URL ="http://127.0.0.1:8080/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(null,getAbsoluteUrl(url),null, params,"application/x-protobuf", responseHandler);
    }

    public static void post(String url, HttpEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.post(null,getAbsoluteUrl(url),null, entity,"application/x-protobuf", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
