package com.se.parfumerie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : fragrances) {
            stringBuilder.append(string).append('\n');
        }
        TextView textView = findViewById(R.id.text2);
        textView.setText(stringBuilder.toString());

        Button retryButton = findViewById(R.id.buttonRetry);

        retryButton.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        });
    }
}
