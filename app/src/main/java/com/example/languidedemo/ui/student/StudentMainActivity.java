package com.example.languidedemo.ui.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.languidedemo.R;

public class StudentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonPrevResults = findViewById(R.id.buttonPrevResults);
        Button buttonProfile = findViewById(R.id.buttonProfile);

        buttonStart.setOnClickListener(v -> openStart());
        buttonPrevResults.setOnClickListener(v -> openPrevResults());
        buttonProfile.setOnClickListener(v -> openProfile());
    }
    public void openStart(){
        Intent intent = new Intent(this,ChooseTestActivity.class);
        startActivity(intent);
    }

    public void openPrevResults(){
        //Intent intent = new Intent(this,ShowRatings.class);
        //startActivity(intent);
    }

    public void openProfile(){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
}