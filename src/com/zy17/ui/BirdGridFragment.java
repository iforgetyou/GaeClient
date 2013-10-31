package com.zy17.ui;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class BirdGridFragment extends Fragment {

    private int mPos = -1;
    private int mImgRes;

    public BirdGridFragment() {
    }

    public BirdGridFragment(int pos) {
        mPos = pos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPos == -1 && savedInstanceState != null)
            mPos = savedInstanceState.getInt("mPos");
        TypedArray imgs = getResources().obtainTypedArray(R.array.birds_img);
        mImgRes = imgs.getResourceId(mPos, -1);

        GridView gv = (GridView) inflater.inflate(R.layout.list_grid, null);
        gv.setBackgroundResource(android.R.color.black);
        gv.setAdapter(new GridAdapter());
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
        return gv;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mPos", mPos);
    }

    private class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 30;
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
            ImageView img = (ImageView) convertView.findViewById(R.id.grid_item_img);
//			img.setImageResource(mImgRes);
            String imageUri = "http://lh6.ggpht.com/koKfeyAjOsj2I8vUrPqddre9gtpl_2ks57Xby3D7zgUeK_o0I9lycWrKEyIH_wppOCQGzfvBGEsQkttJ6rk7Zp-8";
            ImageLoader.getInstance().displayImage(imageUri, img);
            return convertView;
        }

    }
}
