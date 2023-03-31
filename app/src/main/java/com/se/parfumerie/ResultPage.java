package com.se.parfumerie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ResultPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.result);

        Intent intent = getIntent();
        List<String> fragrances = intent.getStringArrayListExtra("fragrances");
        TextView textView = (TextView)findViewById(R.id.text2);
        textView.setText(fragrances.toString());
    }
}
