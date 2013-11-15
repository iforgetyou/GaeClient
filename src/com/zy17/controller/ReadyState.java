package com.zy17.controller;

import android.os.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zy17.GaeClient.http.GaeClient;
import com.zy17.GaeClient.http.HttpRequestAsyncTask;
import com.zy17.protobuf.domain.Eng;

import static com.zy17.controller.ControllerProtocol.*;

final class ReadyState implements ControllerState {

    private final CardController cardController;

    public ReadyState(CardController cardController) {
        this.cardController = cardController;
    }

    @Override
    public final boolean handleMessage(Message msg) {
        switch (msg.what) {
            case C_QUIT:
                onRequestQuit();
                return true;
            case V_REQUEST_UPDATE:
                onRequestUpdate();
                return true;
            case V_REQUEST_DATA:
                onRequestData();
                return true;
            case V_INIT_DATA:
                return true;
        }
        return false;
    }

    private void onRequestData() {
        HttpRequestAsyncTask asyncTask = new HttpRequestAsyncTask();
        asyncTask.setOnCompleteListener(new HttpRequestAsyncTask.OnCompleteListener() {
            @Override
            public void onComplete(byte[] bytes) {
                if (bytes != null) {
                    Eng.CardList cardList = null;
                    try {
                        cardList = Eng.CardList.parseFrom(bytes);
                        cardController.notifyOutboxHandlers(C_DATA, 0, 0, cardList);
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        });
        asyncTask.execute(GaeClient.BASE_URL + "cards");
        // send the data to the outbox handlers (view)
//        cardController.notifyOutboxHandlers(C_DATA, 0, 0, cardController.getCardListModel().getData());
    }

    private void onRequestUpdate() {
        // we can't just call model.updateState() here because it will block
        // the inbox thread where this processing is happening.
        // thus we change the state to UpdatingState that will launch and manage
        // a background thread that will do that operation

//        controller.changeState(new UpdatingState(controller));
    }

    private void onRequestQuit() {
        cardController.quit();
    }
}
