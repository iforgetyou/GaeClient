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
public  class ProtobufParser<T extends GeneratedMessage>{
    private static final String TAG = ProtobufParser.class.getName();
    private Class<T> persistentClass;

    public ProtobufParser() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public  T parse(byte[] bytes){
        T message=null;
        Method parseMethod;
        try {
            //利用反射机制获得parseFrom方法
            parseMethod = getPersistentClass().getDeclaredMethod("parseFrom", byte[].class);
             message = (T) parseMethod.invoke(getPersistentClass(), bytes);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "获取数据解析protobuf失败", e);
        }
        return message;
    }


    // -- Getter && Setter
    protected Class<T> getPersistentClass() {
        return persistentClass;
    }

}
