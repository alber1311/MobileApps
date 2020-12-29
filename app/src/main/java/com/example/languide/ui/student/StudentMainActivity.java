package com.example.languide.ui.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.languide.R;

public class StudentMainActivity extends AppCompatActivity {

    private String name;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

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
        Intent intent = new Intent(this,ShowPrevResultsActivity.class);
        startActivity(intent);
    }

    public void openProfile(){
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.putExtra("profile_name", name);
        intent.putExtra("profile_email", email);
        startActivity(intent);
    }
}