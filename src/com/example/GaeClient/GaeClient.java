package com.example.GaeClient;

import com.loopj.android.http.*;

/**
 * Created with IntelliJ IDEA.
 * User: yan.zhang
 * Date: 13-10-14
 * Time: 下午5:25
 */
public class GaeClient  {
    private static final String BASE_URL ="http://iforgetyou529.appsp0t.com/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
