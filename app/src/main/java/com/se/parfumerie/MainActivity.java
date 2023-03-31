package com.se.parfumerie;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final static int MINIMUM_NUMBER_OF_QUESTIONS = 4;
    private Map<String, String> questionMap;
    private List<String> questions;
    private TextView textView;
    private KnowledgeBasedSystem kbs;
    private int currentQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.question);
        Button endButton = findViewById(R.id.endButton);
        Button yesButton = findViewById(R.id.yesButton);
        Button noButton = findViewById(R.id.noButton);
        kbs = new KnowledgeBasedSystem();
        questionMap = new HashMap<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Intrebari");

        System.out.println(Thread.currentThread() + "in main");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    questionMap.put(data.getKey(), data.getValue(String.class));
                }
                questions = new ArrayList<>(questionMap.keySet());
                Collections.shuffle(questions);
                textView.setText(questions.get(currentQuestionIndex) + "*" + currentQuestionIndex);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error retrieving questions from Firebase", error.toException());
            }
        });

        yesButton.setOnClickListener(view -> {
            if(currentQuestionIndex < questions.size() - 1) { // pus size()
                kbs.addFact(questionMap.get(questions.get(currentQuestionIndex)));
                currentQuestionIndex++;
                textView.setText(questions.get(currentQuestionIndex)+currentQuestionIndex);
            } else {
                Toast.makeText(getApplicationContext(), "Nu mai sunt intrebari disponibile", Toast.LENGTH_SHORT).show();
            }
        });

        noButton.setOnClickListener(view -> {
            if(currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                textView.setText(questions.get(currentQuestionIndex)+currentQuestionIndex);
            }
            else {
                Toast.makeText(getApplicationContext(), "Nu mai sunt intrebari disponibile", Toast.LENGTH_SHORT).show();
            }
        });

        endButton.setOnClickListener(view -> {
            if(currentQuestionIndex < MINIMUM_NUMBER_OF_QUESTIONS) {
                Toast.makeText(getApplicationContext(), "Prea putine informatii", Toast.LENGTH_SHORT).show();
            } else {
                kbs.infer();// trebuie sa returneze o lista cu parfumuri
            }
        });

    }
}