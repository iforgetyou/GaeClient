//package com.example.GaeClient;
//
//import android.R;
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.TextView;
//import com.example.GaeClient.http.GaeClient;
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.loopj.android.http.BinaryHttpResponseHandler;
//import com.zy17.protobuf.domain.AddressBookProtos;
//
//public class MyActivityBackup extends Activity {
//    private static final String TAG = MyActivityBackup.class.getName();
//    private Button getbutton, postbutton;
//    private TextView mTextView;
//
//    /**
//     * Called when the activity is first created.
//     */
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.main);
////
////
////        if (android.os.Build.VERSION.SDK_INT > 9) {
////            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
////            StrictMode.setThreadPolicy(policy);
////        }
////        mTextView = (TextView) this.findViewById(R.id.answerText);
////
////        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg", "application/x-protobuf;charset=UTF-8"};
////        AddressBookProtos.Person john = AddressBookProtos.Person.newBuilder()
////                .setId(1234)
////                .setName("John Doe")
////                .setEmail("jdoe@example.com")
////                .addPhone(
////                        AddressBookProtos.Person.PhoneNumber.newBuilder()
////                                .setNumber("555-4321")
////                                .setType(AddressBookProtos.Person.PhoneType.HOME))
////                .build();
////        GaeClient.post("/message/person", john, new BinaryHttpResponseHandler(allowedContentTypes) {
////            @Override
////            public void onSuccess(int statusCode, byte[] binaryData) {
////                super.onSuccess(statusCode, binaryData);
////                Log.d(TAG, "post success and statusCode is " + statusCode);
////            }
////
////            @Override
////            public void onFailure(Throwable e, byte[] imageData) {
////                super.onFailure(e, imageData);
////            }
////        });
////
////        GaeClient.get("/message/person", null, new BinaryHttpResponseHandler(allowedContentTypes) {
////            @Override
////            public void onSuccess(int statusCode, byte[] binaryData) {
////                super.onSuccess(statusCode, binaryData);
////                try {
////                    AddressBookProtos.PersonList personList = AddressBookProtos.PersonList.parseFrom(binaryData);
////                    Log.d(TAG, personList.toString());
////                } catch (InvalidProtocolBufferException e) {
////                    Log.e(TAG, "解析返回数据异常", e);
////                }
////            }
////
////            @Override
////            public void onFailure(Throwable e, byte[] imageData) {
////                super.onFailure(e, imageData);
////            }
////        });
////        Log.d(TAG, "Execute finished");
////    }
//}
