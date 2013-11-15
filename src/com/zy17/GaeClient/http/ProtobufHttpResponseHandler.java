package com.zy17.GaeClient.http;

import android.util.Log;
import com.google.protobuf.GeneratedMessage;
import com.loopj.android.http.BinaryHttpResponseHandler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * Created with IntelliJ IDEA.
 * User: yan.zhang
 * Date: 13-11-4
 * Time: 上午10:15
 */
public  class ProtobufHttpResponseHandler<T extends GeneratedMessage> extends BinaryHttpResponseHandler {
    private static final String TAG = ProtobufHttpResponseHandler.class.getName();
    private Class<T> persistentClass;

    /**
     * 数据处理成功后的业务逻辑
//     * @param message
     */
//    public abstract void handleMessage(GeneratedMessage message);


    public ProtobufHttpResponseHandler(String[] allowedContentTypes) {
        super(allowedContentTypes);
        persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void onSuccess(int statusCode, byte[] binaryData) {
        //利用反射机制获得parseFrom方法
        Method parseMethod;
        try {
            //利用反射机制获得parseFrom方法
            parseMethod = persistentClass.getDeclaredMethod("parseFrom", byte[].class);
            GeneratedMessage message = (GeneratedMessage) parseMethod.invoke(persistentClass, binaryData);
//            handleMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "获取数据解析protobuf失败", e);
        }
    }

    @Override
    public void onFailure(Throwable e, byte[] imageData) {
        Log.e(TAG, "数据请求异常", e);
    }
}
