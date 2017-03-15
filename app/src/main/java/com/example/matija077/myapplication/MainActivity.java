package com.example.matija077.myapplication;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button pressMeButton;
    TextView messageTextView;
    Boolean firstPress = false;
    LinearLayout firstLayout;
    LinearLayout secondLayout;
    LinearLayout thirdLayout;
    LinearLayout.LayoutParams firstLayoutParams;
    LinearLayout.LayoutParams secondLayoutParams;
    LinearLayout.LayoutParams thirdLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pressMeButton = (Button) findViewById(R.id.pressMeButton);
        messageTextView = (TextView) findViewById(R.id.textView);
        firstLayout = (LinearLayout) findViewById(R.id.firstLayout);
        secondLayout = (LinearLayout) findViewById(R.id.secondLayout);
        thirdLayout = (LinearLayout) findViewById(R.id.thirdLayout);
        firstLayoutParams = (LinearLayout.LayoutParams) firstLayout.getLayoutParams();
        secondLayoutParams = (LinearLayout.LayoutParams) secondLayout.getLayoutParams();
        thirdLayoutParams = (LinearLayout.LayoutParams) thirdLayout.getLayoutParams();
   /* View.OnClickListener pressMeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    }*/

        pressMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Do(v);
            }
        });
    }

    private void Do(View v) {
        if (firstPress == true) {
            messageTextView.setText(R.string.firstText);
        } else {
            messageTextView.setText(R.string.secondText);
        }
        firstPress = !firstPress;
        firstLayoutParams.weight = (float) 0.2;
        secondLayoutParams.weight = (float) 0.3;
        thirdLayoutParams.weight = (float) 0.5;
        firstLayout.setLayoutParams(firstLayoutParams);
        secondLayout.setLayoutParams(secondLayoutParams);
        thirdLayout.setLayoutParams(thirdLayoutParams);
    }


}

/*

 */
