package com.zy17.ui;

import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.zy17.GaeClient.http.GaeClient;
import com.zy17.R;
import com.zy17.ResponsiveUIActivity;
import com.zy17.protobuf.domain.Eng;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;


public class BirdGridFragment extends Fragment {

    private static final String TAG = BirdGridFragment.class.getName();
    private int mPos = -1;
    private int mImgRes;
    GridView gv;
    private Eng.CardList cardList;

    public BirdGridFragment(int pos) {
        mPos = pos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPos == -1 && savedInstanceState != null)
            mPos = savedInstanceState.getInt("mPos");
        TypedArray imgs = getResources().obtainTypedArray(R.array.birds_img);
        mImgRes = imgs.getResourceId(mPos, -1);

        gv = (GridView) inflater.inflate(R.layout.list_grid, null);
        gv.setBackgroundResource(android.R.color.black);
        GridAdapter adapter = new GridAdapter();
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (getActivity() == null)
                    return;
                ResponsiveUIActivity activity = (ResponsiveUIActivity) getActivity();
                activity.onBirdPressed(mPos);
            }
        });
        new LoadCardsTask().execute(GaeClient.BASE_URL + "/cards", null, null);
        return gv;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mPos", mPos);
    }


    public class GridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (cardList == null) {
                return 5;
            } else {
                return cardList.getCardCount();
            }
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

            if (cardList != null && cardList.getCard(position).getImage() != null) {
                ImageView img = (ImageView) convertView.findViewById(R.id.grid_item_img);
                String imageUri = cardList.getCard(position).getImage().getMediaInfo().getServUrl();
                ImageLoader.getInstance().displayImage(imageUri, img);
            } else {
                //show dummydata
                ImageView img = (ImageView) convertView.findViewById(R.id.grid_item_img);
                String imageUri = "http://lh6.ggpht.com/koKfeyAjOsj2I8vUrPqddre9gtpl_2ks57Xby3D7zgUeK_o0I9lycWrKEyIH_wppOCQGzfvBGEsQkttJ6rk7Zp-8";
                ImageLoader.getInstance().displayImage(imageUri, img);
            }

            return convertView;
        }

    }


    private class LoadCardsTask extends AsyncTask<String, Integer, Eng.CardList> {
        @Override
        protected Eng.CardList doInBackground(String... params) {
            Eng.CardList cardList = null;
            // 同步网络请求
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet get = new HttpGet(params[0]);
            get.setHeader("Accept", "application/x-protobuf");
            get.setHeader("Content-Type", "application/x-protobuf");
            HttpResponse response = null;
            try {
                response = httpclient.execute(get);
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                byte[] bytes = IOUtils.toByteArray(content);
                cardList = Eng.CardList.parseFrom(bytes);
            } catch (IOException e) {
                Log.e(TAG, "获取后台数据失败:/n",e);
            }
            return cardList;
        }

        protected void onProgressUpdate(Integer... progress) {
            //            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Eng.CardList result) {
            if (result != null) {
                Log.d(TAG, "加载数据完成:/n" + result.toString());
                cardList = result;
                gv.setAdapter(new GridAdapter());
            }else {
                Log.e(TAG, "加载数据失败:/n");
            }

        }
    }

}
