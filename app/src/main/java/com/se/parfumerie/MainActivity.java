package com.se.parfumerie;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final static int MINIMUM_NUMBER_OF_QUESTIONS = 4;
    private Map<String, String> questionMap;
    private List<String> questions;
    private Map<String, List<String>> fragrances;
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
        fragrances = new HashMap<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Intrebari");

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
                Log.e(TAG, "Eroare la preluarea intrebarilor din baza de date", error.toException());
            }
        });

        database.getReference("Reguli").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    String key = data.getKey().toLowerCase();
                    String value = data.getValue(String.class).toLowerCase();
                    if(value.contains(",")) {
                        String[] values = value.split("\\s*,\\s*");
                        for(String v : values) {
                            kbs.addRule(new Rule(key, v));
                        }
                    } else {
                        kbs.addRule(new Rule(key, value));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Eroare la preluarea regulilor din baza de dare", error.toException());
            }
        });

        database.getReference("Parfumuri").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren()) {
                    String values = data.getValue(String.class).toLowerCase();
//                    System.out.println("////////////" + values);
                    fragrances.put(data.getKey(), Arrays.asList(values.split("\\s*,\\s*")));
                }
                System.out.printf(fragrances.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Eroare la preluarea parfumurilor din baza de dare", error.toException());
            }
        });

        yesButton.setOnClickListener(view -> {
            if(questions != null){
                if(currentQuestionIndex < questions.size()) { // pus size()
                    kbs.addFact(questionMap.get(questions.get(currentQuestionIndex)));
                    currentQuestionIndex++;
                    if(currentQuestionIndex < questions.size()) {
                        textView.setText(questions.get(currentQuestionIndex)+currentQuestionIndex);
                    } else {
                        // face ce ar face endButton
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Nu mai sunt intrebari disponibile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        noButton.setOnClickListener(view -> {
            if(currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                textView.setText(questions.get(currentQuestionIndex)+currentQuestionIndex);
            }
            else {
                // face ce face end Button
                Toast.makeText(getApplicationContext(), "Nu mai sunt intrebari disponibile", Toast.LENGTH_SHORT).show();
            }
        });

        endButton.setOnClickListener(view -> {
            if(currentQuestionIndex < 1) {
                Toast.makeText(getApplicationContext(), "Prea putine informatii", Toast.LENGTH_SHORT).show();
            } else {

                kbs.infer();// trebuie sa returneze o lista cu parfumuri
                Intent intent = new Intent(MainActivity.this, ResultPage.class);
                System.out.println("In main avem urm parfumuri:" + fragrances.keySet().toString());
                intent.putStringArrayListExtra("fragrances",new ArrayList<>(fragrances.keySet()));
                startActivity(intent);
            }
        });

    }
}