package com.example.languide.ui.student.testActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ReadingTest;
import com.example.languide.tests.VocabularyTest;
import com.example.languide.ui.student.StudentMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VocabularyTestActivity extends AppCompatActivity {

    private TextView titleExercise;
    private TextView exerciseContent;
    private TextView instructionsExercise;
    private Button finishTest;

    private String test;
    private String difficulty;

    private int grade = 0;
    private static int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);

        titleExercise = findViewById(R.id.idTitleExercise);
        instructionsExercise = findViewById(R.id.test_instructions);
        exerciseContent = findViewById(R.id.exercise_text);
        finishTest = findViewById(R.id.finishTest);

        Intent intent = getIntent();
        test = intent.getStringExtra("test_Name");
        difficulty = intent.getStringExtra("test_Difficulty");

        //loadTest();
    }

    public void loadTest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        TestService service = retrofit.create(TestService.class);

        Call<VocabularyTest> call = service.loadVocabularyTest();
        call.enqueue(new Callback<VocabularyTest>() {
            @Override
            public void onResponse(Call<VocabularyTest> call, Response<VocabularyTest> response) {
                VocabularyTest vocabularyTest = response.body();
                titleExercise.setText(vocabularyTest.getData().getExercise().getTitle());
                instructionsExercise.setText(vocabularyTest.getData().getExercise().getInstructions());
                //Manage exerciseContent and clickable options



                finishTest.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("VocabularyTests").document(userID);

                    Map<String, Object> test = new HashMap<>();
                    test.put("title", vocabularyTest.getData().getExercise().getTitle());
                    test.put("grade", (grade*10.0)/VocabularyTestActivity.position);

                    documentReference.set(test);

                    Toast.makeText(VocabularyTestActivity.this, "Your grade is:\t" + (grade*10.0)/VocabularyTestActivity.position, Toast.LENGTH_LONG).show();
                    VocabularyTestActivity.position = 0;
                    startActivity(new Intent(VocabularyTestActivity.this, StudentMainActivity.class));
                });
            }

            @Override
            public void onFailure(Call<VocabularyTest> call, Throwable t) {
                Toast.makeText(VocabularyTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Intent intentFail = new Intent(VocabularyTestActivity.this, StudentMainActivity.class);
                startActivity(intentFail);
            }
        });
    }
}