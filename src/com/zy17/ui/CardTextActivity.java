package com.zy17.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.zy17.R;
import com.zy17.protobuf.domain.Eng;


public class CardTextActivity extends Activity {
    private static final String TAG = CardTextActivity.class.getName();
    private Eng.CardList cardList;
    private int pos = 0;
    private TextView textView;
    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            pos = getIntent().getExtras().getInt("pos");
            cardList = (Eng.CardList) getIntent().getExtras().get("cardList");
        }

        this.setContentView(R.layout.card_detail_text);
        textView = (TextView) findViewById(R.id.answer);
        textView.setText(cardList.getCard(pos).getEngText());
        textView.setVisibility(View.GONE);

        editText = (EditText) findViewById(R.id.yourAnswer);
    }


    /**
     * 简单比对答案
     * @param v
     */
    public void checkAnswer(View v) {
        if (editText.getText().toString().equals(textView.getText().toString())) {
            Toast.makeText(this, "Right answer", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Wrong answer", Toast.LENGTH_LONG).show();
        }
        textView.setVisibility(View.VISIBLE);
    }


}
