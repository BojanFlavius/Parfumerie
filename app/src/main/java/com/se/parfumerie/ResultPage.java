package com.se.parfumerie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ResultPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.result);
    }
}
