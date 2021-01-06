package com.example.languide.ui.student.testActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ReadingTest;
import com.example.languide.tests.VocabularyTest;
import com.example.languide.tests.WritingTest;
import com.example.languide.ui.student.StudentMainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WritingTestActivity extends AppCompatActivity {

    private TextView titleExercise;
    private TextView exerciseContent;
    private TextView instructionsExercise;

    private String test;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_test);

        titleExercise = findViewById(R.id.idTitleExercise);
        instructionsExercise = findViewById(R.id.test_instructions);
        exerciseContent = findViewById(R.id.exercise_text);

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
                titleExercise.setText(writingTest.getTitle());
                instructionsExercise.setText(writingTest.getInstructions());
                //Manage exerciseContent and clickable options
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