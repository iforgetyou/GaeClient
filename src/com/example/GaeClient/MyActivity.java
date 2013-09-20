package com.example.GaeClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyActivity extends Activity {
    private Button getbutton, postbutton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        TextView mTextView = (TextView) this.findViewById(R.id.TextView_HTTP);
        // http地址
        String httpUrl = "http://192.168.1.107:8080/message";
        // HttpGet连接对象
        HttpGet httpRequest = new HttpGet(httpUrl);
        try {
            // 取得HttpClient对象
            HttpClient httpclient = new DefaultHttpClient();
            // 请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的字符串
                String strResult = EntityUtils.toString(httpResponse
                        .getEntity());
                mTextView.setText(strResult);
            } else {
                mTextView.setText("请求错误!");
            }
        } catch (ClientProtocolException e) {
            mTextView.setText(e.getMessage().toString());
        } catch (IOException e) {
            mTextView.setText(e.getMessage().toString());
        } catch (Exception e) {
            mTextView.setText(e.getMessage().toString());
        }

//
//        getbutton = (Button) this.findViewById(R.id.getbutton);
//        getbutton.setOnClickListener(listener);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (getbutton == v) {
                    /*
                     * 因为是GET请求，所以需要将请求参数添加到URL后，并且还需要进行URL编码
                     * URL = http://192.168.0.103:8080/Server/PrintServlet?name=%E6%88%91&age=20
                     * 此处需要进行URL编码因为浏览器提交时自动进行URL编码
                     * */
                    StringBuilder buf = new StringBuilder("http://192.168.1.107:8080/message");
                    URL url = new URL(buf.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("content-Type", "application/json");
                    conn.setRequestProperty("Accept","application/json");
//                    conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                    conn.setRequestProperty("Charset", "UTF-8");


// 建立输出流，并写入数据
//                    OutputStream outputStream =conn.getOutputStream();
//                    outputStream.write(requestStringBytes);
//                    outputStream.close();

                    if (conn.getResponseCode() == 200) {
                        Toast.makeText(MyActivity.this, "GET提交成功", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(MyActivity.this, "GET提交失败", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MyActivity.this, "GET提交Exception:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
