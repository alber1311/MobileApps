package com.example.languide.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestResultActivity extends AppCompatActivity {

    private TextView textGrade;
    private TextView exerciseText;
    private Button backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        textGrade = findViewById(R.id.testGrade);
        exerciseText = findViewById(R.id.exercise_text);
        backToMain = findViewById(R.id.next);

        Intent intent = getIntent();
        double grade = intent.getExtras().getDouble("grade");
        String exercise = intent.getStringExtra("exercise");
        String text_grade = "Grade :\t" + grade;
        textGrade.setText(text_grade);
        exerciseText.setText(exercise);
        exerciseText.setMovementMethod(new ScrollingMovementMethod());

        backToMain.setOnClickListener(v -> {
            startActivity(new Intent(TestResultActivity.this, StudentMainActivity.class));
        });


    }
}