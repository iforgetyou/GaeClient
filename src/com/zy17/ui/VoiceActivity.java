package com.zy17.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.GaeClient.http.SendMessageUtil;
import com.zy17.R;
import com.zy17.util.AudioMeter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class VoiceActivity extends Activity {

    private volatile boolean isRecording;
    private static final int frequency = 8000;
    private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    int bufferSize;
    AudioMeter recordInstance;

    private static final int REQUEST_CODE = 1234;
    private static final String TAG = VoiceActivity.class.getName();
    private static String mFileName = "";
    private ListView resultList;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    SpeechRecognizer speechRecognizer;
    Button speakButton;
    Button recordButton;
    TextView textView;
    boolean mStartRecording = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createcard);

        speakButton = (Button) findViewById(R.id.speakButton);
        recordButton = (Button) findViewById(R.id.recordButton);

        resultList = (ListView) findViewById(R.id.list);

        textView = (TextView) findViewById(R.id.Answer);
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        speechRecognizer.setRecognitionListener(new myRecognitionListener());

        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String o = (String) resultList.getItemAtPosition(i);
                textView.append(o);
            }
        });


        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            speakButton.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Recognizer Not Found", 1000).show();
        }
    }

    /**
     * 开始停止录音
     *
     * @param v
     */
    public void onRecordButtonClick(View v) {
        Button b = (Button) v;
        onRecord(mStartRecording);
        if (!mStartRecording) {
            Log.d(TAG, "Stop recording");
            b.setText("Stop recording");

        } else {
            b.setText("Start recording");
        }
        mStartRecording = !mStartRecording;
    }

    /**
     * 启动语音识别
     *
     * @param v
     */
    public void startVoiceRecognitionActivity(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.example.GaeClient");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice Recognition...Result");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

//        speechRecognizer.startListening(intent);
        startActivityForResult(intent, REQUEST_CODE);
    }


    private void onRecord(boolean start) {
        if (start) {
            startRecording();
//            startAudioRecord();
        } else {
            stopRecording();
//            stopAudioRecord();
        }
    }

    public void startAudioRecord() {
        android.os.Process
                .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        bufferSize = AudioRecord.getMinBufferSize(frequency,
                AudioFormat.CHANNEL_IN_MONO, audioEncoding);
        recordInstance =
                AudioMeter.getInstance();
//                 new AudioRecord(
//                MediaRecorder.AudioSource.MIC, frequency,
//                AudioFormat.CHANNEL_IN_MONO, audioEncoding, bufferSize);
        recordInstance.startRecording();
    }

    public void stopAudioRecord() {
        int bufferRead = 0;
        recordInstance.stop();

        short[] tempBuffer = new short[bufferSize];
        //bufferRead = recordInstance.read(tempBuffer, 0, bufferSize);
        bufferRead = recordInstance.read(tempBuffer, 0, 640);
        if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
            throw new IllegalStateException(
                    "read() returned AudioRecord.ERROR_INVALID_OPERATION");
        } else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
            throw new IllegalStateException(
                    "read() returned AudioRecord.ERROR_BAD_VALUE");
        } else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
            throw new IllegalStateException(
                    "read() returned AudioRecord.ERROR_INVALID_OPERATION");
        }
        Log.d(TAG, "read Data " + bufferRead);
    }

    private void startRecording() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            Log.d(TAG, "停止播放");
        }
        Log.d(TAG, "开始播放");
        SimpleDateFormat dateformat = new SimpleDateFormat("MMddHHmmss");
        String date = dateformat.format(new Date());
        mFileName = getApplicationContext().getFilesDir().getAbsolutePath() + "/audio" + date + ".3gp";
        Log.d(TAG, "mFileName path:" + mFileName);
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed", e);
        }

        mRecorder.start();

//        startVoiceRecognitionActivity(this.recordButton);

    }

    private void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        //停止后播放录音
        mPlayer = new MediaPlayer();
        try {
            Log.d(TAG, "停止录音，开始播放");
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
        } catch (IOException e) {
            Log.e(TAG, "mPlayer prepare failed", e);
        }
        mPlayer.start();
    }

    public void sendMessageToServer(View v) {
        if (textView.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "English anwser is blank", 1000).show();
            return;
        }
        File file = new File(mFileName);
        SendMessageUtil.sendCard(textView.getText().toString(), "", file, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            resultList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    private static int[] mSampleRates = new int[]{8000, 11025, 22050, 44100};
//
//    public AudioRecord findAudioRecord() {
//        for (int rate : mSampleRates) {
//            for (short audioFormat : new short[]{AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT}) {
//                for (short channelConfig : new short[]{AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO}) {
//                    try {
//                        Log.d(TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
//                                + channelConfig);
//                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);
//
//                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
//                            // check if we can instantiate and have a success
//                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);
//
//                            Log.d(TAG, "AudioRecord state:" + recorder.getState());
//                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
//                                return recorder;
//                        }
//                    } catch (Exception e) {
//                        Log.e(TAG, rate + "Exception, keep trying.", e);
//                    }
//                }
//            }
//        }
//        return null;
//    }
}