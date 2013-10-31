package com.example.GaeClient.http;import android.util.Log;import com.google.protobuf.GeneratedMessage;import com.loopj.android.http.AsyncHttpClient;import com.loopj.android.http.AsyncHttpResponseHandler;import com.loopj.android.http.RequestParams;import org.apache.http.entity.ByteArrayEntity;import java.io.File;import java.io.FileInputStream;import java.io.FileNotFoundException;/** * Created with IntelliJ IDEA. * User: yan.zhang * Date: 13-10-14 * Time: 下午5:25 */public class GaeClient {    private static final String BASE_URL = "http://iforgetyou529.appsp0t.com/";    //    private static final String BASE_URL ="http://127.0.0.1:8080/";    private static AsyncHttpClient client = new AsyncHttpClient();    private static final String TAG = GaeClient.class.getName();    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {        client.get(getAbsoluteUrl(url), params, responseHandler);    }    public static void post(String url, AsyncHttpResponseHandler responseHandler, File... files) {        RequestParams params = new RequestParams();        for (File file : files) {            try {                params.put("profile_picture", new FileInputStream(file),file.getName(),"audio/3gp");            } catch (FileNotFoundException e) {                Log.e(TAG,"添加文件到请求失败",e);            }        }        client.post(url,params,responseHandler);    }    public static void post(String url, GeneratedMessage message, AsyncHttpResponseHandler responseHandler) {        ByteArrayEntity entity = new ByteArrayEntity(message.toByteArray());        client.post(null, getAbsoluteUrl(url), null, entity, "application/x-protobuf", responseHandler);    }    private static String getAbsoluteUrl(String relativeUrl) {        return BASE_URL + relativeUrl;    }}