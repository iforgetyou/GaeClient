package com.zy17.ui;

import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zy17.R;
import com.zy17.ResponsiveUIActivity;
import com.zy17.controller.CardController;
import com.zy17.model.CardListModel;
import com.zy17.protobuf.domain.Eng;

import static com.zy17.controller.ControllerProtocol.*;

public class CardListFragment extends Fragment implements Handler.Callback {

    private static final String TAG = CardListFragment.class.getName();
    private CardController cardController;
    private int mPos = -1;
    private int mImgRes;
    private GridView gv;
    private Eng.CardList cardList;

    public CardListFragment(int pos) {
        mPos = pos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        初始化controller
        cardController = new CardController(new CardListModel());
        cardController.addOutboxHandler(new Handler(this)); // messages will go to .handleMessage()
        cardController.getInboxHandler().sendEmptyMessage(V_REQUEST_DATA); // request initial data

        if (mPos == -1 && savedInstanceState != null)
            mPos = savedInstanceState.getInt("mPos");
        TypedArray imgs = getResources().obtainTypedArray(R.array.birds_img);
        mImgRes = imgs.getResourceId(mPos, -1);

        gv = (GridView) inflater.inflate(R.layout.list_grid, null);
        gv.setBackgroundResource(android.R.color.black);
//        GridAdapter adapter = new GridAdapter();
        gv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (getActivity() == null)
                    return;
                ResponsiveUIActivity activity = (ResponsiveUIActivity) getActivity();
                activity.onBirdPressed(position, cardList);
            }
        });
//        loadCardTask = new LoadCardsTask().execute(GaeClient.BASE_URL + "/cards");
        return gv;
    }

    @Override
    public void onDestroyView() {
        // I think it is a good idea to not fail in onDestroy()
        try {
            cardController.dispose();
        } catch (Throwable t) {
            Log.e(TAG, "Failed to destroy the controller", t);
        }
        super.onDestroyView();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mPos", mPos);
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(TAG, "Received message: " + msg);

        switch (msg.what) {
            case C_QUIT:
                return true;
            case C_DATA:
                this.cardList = (Eng.CardList) msg.obj;
                if (cardList == null) {

                } else {
                    gv.setAdapter(new GridAdapter(this.cardList));
                }
                return true;
            case C_UPDATE_STARTED:
                return true;
            case C_UPDATE_FINISHED:
                return true;
        }
        return false;
    }


    public class GridAdapter extends BaseAdapter {
        private Eng.CardList mCardList;

        public GridAdapter(Eng.CardList mCardList) {
            this.mCardList = mCardList;
        }

        @Override
        public int getCount() {
            if (mCardList == null || mCardList.getCardCount() == 0) {
                return 0;
            }
            return mCardList.getCardCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.grid_item, null);
            }

            if (mCardList != null && mCardList.getCard(position).getImage() != null) {
                ImageView img = (ImageView) convertView.findViewById(R.id.grid_item_img);
                String imageUri = mCardList.getCard(position).getImage().getMediaInfo().getServUrl();
                ImageLoader.getInstance().displayImage(imageUri, img);
            }
            return convertView;
        }

    }
}

