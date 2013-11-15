package com.zy17.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zy17.R;
import com.zy17.protobuf.domain.Eng;

import java.io.IOException;


public class CardMediaActivity extends Activity {
    private static final String TAG = CardMediaActivity.class.getName();
    private ImageView imageView;
    private MediaPlayer mediaPlayer = null;
    private Handler mHandler;
    public boolean playing = false;
    private Eng.CardList cardList;
    private int pos = 0;
    private boolean isMediaLayout = true;
    private GestureDetector gdt;
    private GestureListener listener;
    private String imageServUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            pos = getIntent().getExtras().getInt("pos");
            cardList = (Eng.CardList) getIntent().getExtras().get("cardList");
        }

        String[] birds = getResources().getStringArray(R.array.birds);

        setTitle(birds[pos]);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        ColorDrawable color = new ColorDrawable(Color.BLACK);
        color.setAlpha(128);
//        mHandler = new Handler();
        mediaPlayer = new MediaPlayer();
        listener = new GestureListener();
        gdt = new GestureDetector(this, listener);

        setContentView(R.layout.card_detail_media);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer();
            }
        });


        imageServUrl = cardList.getCard(pos).getImage().getMediaInfo().getServUrl();
        ImageLoader.getInstance().displayImage(imageServUrl, imageView, new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build());


        this.getWindow().setBackgroundDrawableResource(android.R.color.black);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
//        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }


    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                changeView();
                return true; // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                changeView();
                return true; // Left to right
            }


            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return false;
        }

    }

    private void mediaPlayer() {
        if (!playing) {
            startPlay();
        } else {
            stopPlay();
        }
    }

//    private void changeView() {
////        this.stopPlay();
//        if (isMediaLayout) {
//            this.setContentView(R.layout.card_detail_text);
//        } else {
//            this.setContentView(R.layout.card_detail_media);
//            ImageLoader.getInstance().displayImage(imageServUrl, imageView, new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build());
//        }
//        isMediaLayout = !isMediaLayout;
//    }

    /**
     * 开始播放
     */
    private void startPlay() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(cardList.getCard(pos).getSound().getMediaInfo().getServUrl());
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();
            this.playing = true;
            Toast.makeText(getApplicationContext(), "开始播放", 1000);
        } catch (IOException e) {
            mediaPlayer.stop();
            this.playing = false;
            Log.e(TAG, "播放异常", e);
            Toast.makeText(getApplicationContext(), "播放异常" + e, 1000);
        }
    }

    /**
     * 停止播放
     */
    private void stopPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.playing = false;
            mediaPlayer.release();
            mediaPlayer = null;
//            Toast.makeText(getApplicationContext(), "停止播放", 1000);
        }
    }

}
