package com.zy17.GaeClient.http;

import android.util.Log;
import android.widget.Toast;
import com.google.protobuf.InvalidProtocolBufferException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.zy17.protobuf.domain.Eng;
import com.zy17.ui.CreateCardActivity;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yan.zhang
 * Date: 13-10-25
 * Time: 下午2:32
 */
public class SendMessageUtil {
    private static final String TAG = CreateCardActivity.class.getName();

    public static void sendCard(final String eng, final String chi, final File sound, final File image) {
        String[] allowedContentTypes = new String[]{"application/x-protobuf;charset=UTF-8", "application/x-protobuf"};
//获取附件上传地址
        com.zy17.GaeClient.http.GaeClient.get("/blobs", null, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int statusCode, byte[] binaryData) {
                super.onSuccess(statusCode, binaryData);
                try {
                    Eng.BlobMessage blobMessage = Eng.BlobMessage.parseFrom(binaryData);
                    Log.d(TAG, "返回上传地址为:" + blobMessage);
                    uploadBlobs(eng, chi, blobMessage.getBlobUploadUrl(), sound, image);
                } catch (InvalidProtocolBufferException e) {
                    Log.e(TAG, "解析返回数据异常", e);
                }
            }

            @Override
            public void onFailure(Throwable e, byte[] imageData) {
                Log.e(TAG, "获取上传地址异常", e);
            }
        });
//上传card
    }

    public static void uploadBlobs(final String eng, final String chi, String uploadUrl, final File sound, final File image) {
        Log.d(TAG, "开始上传声音/图片:");
        String[] allowedContentTypes = new String[]{"application/x-protobuf;charset=UTF-8", "application/x-protobuf"};
        com.zy17.GaeClient.http.GaeClient.post(uploadUrl, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int statusCode, byte[] binaryData) {
                super.onSuccess(statusCode, binaryData);
                try {
                    Eng.MediaBlobInfoList pbBlobKeyList = Eng.MediaBlobInfoList.parseFrom(binaryData);
                    Log.d(TAG, "返回:" + pbBlobKeyList);
                    List<Eng.MediaBlobInfo> mediaBlobInfosList = pbBlobKeyList.getMediaBlobInfosList();
                    Eng.Card.Builder builder = Eng.Card.newBuilder().setEngText(eng).setChiText(chi);
                    for (Eng.MediaBlobInfo mediaBlobInfo : mediaBlobInfosList) {
                        if (sound != null && mediaBlobInfo.getFileName().equals(sound.getName())) {
                            builder.setSound(Eng.PbSound.newBuilder().setMediaInfo(mediaBlobInfo));
                        }
                        if (image != null && mediaBlobInfo.getFileName().equals(image.getName())) {
                            builder.setImage(Eng.PbImage.newBuilder().setMediaInfo(mediaBlobInfo));
                        }
                    }
                    uploadCard(builder.build());
                } catch (InvalidProtocolBufferException e) {
                    Log.e(TAG, "解析返回数据异常", e);
                }
            }

            @Override
            public void onFailure(Throwable e, byte[] imageData) {
                Log.e(TAG, "上传声音图片异常", e);
            }
        }, sound, image);
    }

    public static void uploadCard(Eng.Card card) {
        String[] allowedContentTypes = new String[]{"application/x-protobuf;charset=UTF-8", "application/x-protobuf"};
        com.zy17.GaeClient.http.GaeClient.post("/cards", card, new AsyncHttpResponseHandler() {
        });
    }
}
