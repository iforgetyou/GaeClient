package com.zy17.GaeClient.http;

import android.util.Log;
import android.widget.GridView;
import com.google.protobuf.GeneratedMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;
import com.zy17.ResponsiveUIActivity;
import com.zy17.protobuf.domain.Eng;
import com.zy17.ui.BirdGridFragment;

/**
 * Created with IntelliJ IDEA.
 * User: yan.zhang
 * Date: 13-11-1
 * Time: 下午2:12
 */
public class CardController {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static SyncHttpClient syncHttpClient;
    private static final String TAG = CardController.class.getName();
    public final String[] allowedContentTypes = new String[]{"application/x-protobuf;charset=UTF-8", "application/x-protobuf"};
    BirdGridFragment.GridAdapter adapter;
    public CardController(BirdGridFragment.GridAdapter adapter) {
        this.adapter = adapter;
    }


    public void getCards() {
        GaeClient.get("/cards", null, new ProtobufHttpResponseHandler<Eng.CardList>(allowedContentTypes) {
            @Override
            public void handleMessage(GeneratedMessage message) {
                Log.d(TAG, message.toString());
                Eng.CardList cardList = (Eng.CardList) message;
            }
        });
    }
}
