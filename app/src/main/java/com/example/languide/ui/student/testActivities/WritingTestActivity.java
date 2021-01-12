package com.example.languide.ui.student.testActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ReadingTest;
import com.example.languide.tests.VocabularyTest;
import com.example.languide.tests.WritingTest;
import com.example.languide.ui.student.StudentMainActivity;
import com.example.languide.ui.student.TestResultActivity;
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

public class WritingTestActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_writing_test);

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

        Call<WritingTest> call = service.loadWritingTest();
        call.enqueue(new Callback<WritingTest>() {
            @Override
            public void onResponse(Call<WritingTest> call, Response<WritingTest> response) {
                WritingTest writingTest = response.body();
                titleExercise.setText(writingTest.getData().getExercise().getTitle());
                instructionsExercise.setText(writingTest.getData().getExercise().getInstructions());
                //Manage exerciseContent and clickable options


                finishTest.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("ReadingTests").document(userID);

                    Map<String, Object> test = new HashMap<>();
                    test.put("title", writingTest.getData().getExercise().getTitle());
                    test.put("grade", (grade*10.0)/WritingTestActivity.position);

                    documentReference.set(test);

                    Intent intent = new Intent(WritingTestActivity.this, TestResultActivity.class);
                    intent.putExtra("grade", (grade*10.0)/WritingTestActivity.position);
                    intent.putExtra("exercise", writingTest.toString());
                    WritingTestActivity.position = 0;
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(Call<WritingTest> call, Throwable t) {
                Toast.makeText(WritingTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Intent intentFail = new Intent(WritingTestActivity.this, StudentMainActivity.class);
                startActivity(intentFail);
            }
        });
    }
}