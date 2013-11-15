package com.zy17.ui;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import com.zy17.R;
import com.zy17.protobuf.domain.Eng;

import java.util.ArrayList;

public class CardDetailActivity extends ActivityGroup {
    private static final String TAG = CardMediaActivity.class.getName();
    private View oneView;
    private View twoView;
    private ArrayList<View> views;
    private ViewPager mViewPager;//多页面滑动切换效果
    private int pos;
    private Eng.CardList cardList;


    public static Intent newInstance(Activity activity, int pos, Eng.CardList cardList) {
        Intent intent = new Intent(activity, CardDetailActivity.class);
        intent.putExtra("pos", pos);
        intent.putExtra("cardList", cardList);
        return intent;
    }
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            pos = getIntent().getExtras().getInt("pos");
            cardList = (Eng.CardList) getIntent().getExtras().get("cardList");
        }

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.card_detail_main);
        views = new ArrayList<View>();
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mViewPager.setOnPageChangeListener(null);
        initView();

        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                Log.d(TAG,"切换CardDetail"+position);
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }
        };

        mViewPager.setAdapter(mPagerAdapter);
    }

    /**
     * 将相应的Activity转换成View对象
     */
    public void initView() {
        oneView = getViews(CardMediaActivity.class, "one");
        twoView = getViews(CardTextActivity.class, "two");
        views.add(oneView);
        views.add(twoView);
    }

    /**
     * 获取要跳转的Activity对应的View
     *
     * @return 返回一个View类型的变量
     */
    public View getViews(Class<?> cls, String pageid) {
        Intent intent = new Intent(CardDetailActivity.this, cls).addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra("pos", pos);
        intent.putExtra("cardList", cardList);
        return getLocalActivityManager().startActivity(pageid, intent).getDecorView();
    }
}