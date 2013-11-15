package com.zy17.controller;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import com.zy17.model.CardListModel;

import java.util.ArrayList;
import java.util.List;

import static com.zy17.controller.ControllerProtocol.*;

public class CardController {

    private static final String TAG = CardController.class.getName();

    private final CardListModel cardListModel;

    private final HandlerThread inboxHandlerThread;
    private final Handler inboxHandler;
    private final List<Handler> outboxHandlers = new ArrayList<Handler>();

    private ControllerState state;

    public CardController(CardListModel cardListModel) {
        this.cardListModel = cardListModel;

        inboxHandlerThread = new HandlerThread("Controller Inbox"); // note you can also set a priority here
        inboxHandlerThread.start();

        this.state = new ReadyState(this);

        inboxHandler = new Handler(inboxHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                CardController.this.handleMessage(msg);
            }
        };
    }

    public final void dispose() {
        // ask the inbox thread to exit gracefully
        inboxHandlerThread.getLooper().quit();
    }

    public final Handler getInboxHandler() {
        return inboxHandler;
    }

    public final void addOutboxHandler(Handler handler) {
        outboxHandlers.add(handler);
    }

    public final void removeOutboxHandler(Handler handler) {
        outboxHandlers.remove(handler);
    }

    final void notifyOutboxHandlers(int what, int arg1, int arg2, Object obj) {
        if (outboxHandlers.isEmpty()) {
            Log.w(TAG, String.format("No outbox handler to handle outgoing message (%d)", what));
        } else {
            for (Handler handler : outboxHandlers) {
                Message msg = Message.obtain(handler, what, arg1, arg2, obj);
                msg.sendToTarget();
            }
        }
    }

    private void handleMessage(Message msg) {
        Log.d(TAG, "Received message: " + msg);

        if (!state.handleMessage(msg)) {
            Log.w(TAG, "Unknown message: " + msg);
        }
    }

    final CardListModel getCardListModel() {
        return cardListModel;
    }

    final void quit() {
        notifyOutboxHandlers(C_QUIT, 0, 0, null);
    }

    final void changeState(ControllerState newState) {
        Log.d(TAG, String.format("Changing state from %s to %s", state, newState));
        state = newState;
    }
}
