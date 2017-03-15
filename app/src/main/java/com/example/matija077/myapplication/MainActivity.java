package com.example.matija077.myapplication;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    MenuItem first, second, third;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.man_menu, menu);
        first = menu.findItem(R.id.action_favorite);
        second = menu.findItem(R.id.action_settings1);
        third = menu.findItem(R.id.action_settings2);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Toast.makeText(this, "pritisnuo na ikonu...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings1:
                return true;
            case R.id.action_settings2:
                Intent newActivity = new Intent(this, secondActivity.class);
                startActivity(newActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

