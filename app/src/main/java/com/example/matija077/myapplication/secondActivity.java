package com.example.matija077.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class secondActivity extends AppCompatActivity {

    Button saveButton;
    EditText saveEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveEditText = (EditText) findViewById(R.id.saveEditText);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_in_SP(saveEditText.getText().toString());
                saveEditText.setText("");
            }
        });

        String from_SP = readSP();
        saveEditText.setText(from_SP);
    }


    private void save_in_SP(String s) {
        SharedPreferences sharedPref = getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("KLJUC", s);
        editor.commit();

        Toast.makeText(this, "ovo je otislo u Sp", Toast.LENGTH_SHORT).show();
    }

    private String readSP() {
        SharedPreferences sharePref = getPreferences(this.MODE_PRIVATE);
        String output = sharePref.getString("KLJUC", "");
        return output;
    }

}
