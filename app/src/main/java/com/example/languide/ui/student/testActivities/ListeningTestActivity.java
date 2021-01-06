package com.example.languide.ui.student.testActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ListeningTest;
import com.example.languide.ui.student.StudentMainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListeningTestActivity extends AppCompatActivity {

    private TextView titleExercise;
    private TextView instructionsExercise;
    private TextView exerciseContent;

    private String exerciseAudio;
    private String test;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_test);

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

        Call<ListeningTest> call = service.loadListeningTest();
        call.enqueue(new Callback<ListeningTest>() {
            @Override
            public void onResponse(@NotNull Call<ListeningTest> call, @NotNull Response<ListeningTest> response) {
                ListeningTest listeningTest = response.body();
                titleExercise.setText(listeningTest.getTitle());
                instructionsExercise.setText(listeningTest.getInstructions());
                exerciseAudio = listeningTest.getAudio();
                //Manage the audio
            }

            @Override
            public void onFailure(@NotNull Call<ListeningTest> call, @NotNull Throwable t) {
                Toast.makeText(ListeningTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Intent intentFail = new Intent(ListeningTestActivity.this, StudentMainActivity.class);
                startActivity(intentFail);
            }
        });
    }
}