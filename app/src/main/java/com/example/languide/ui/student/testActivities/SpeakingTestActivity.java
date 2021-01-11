package com.example.languide.ui.student.testActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ReadingTest;
import com.example.languide.tests.SpeakingTest;
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

public class SpeakingTestActivity extends AppCompatActivity {

    private TextView titleExercise;
    private TextView exerciseContent;
    private TextView instructionsExercise;
    private Button finishTest;

    private int grade = 0;
    private static int position = 0;
    private String test;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_test);

        titleExercise = findViewById(R.id.idTitleExercise);
        instructionsExercise = findViewById(R.id.test_instructions);
        exerciseContent = findViewById(R.id.exercise_text);
        finishTest = findViewById(R.id.finishTest);

        //loadTest();
    }

    public void loadTest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://languide-app.herokuapp.com/repository/api/exercise/").addConverterFactory(GsonConverterFactory.create()).build();
        TestService service = retrofit.create(TestService.class);

        Call<SpeakingTest> call = service.loadSpeakingTest();
        call.enqueue(new Callback<SpeakingTest>() {
            @Override
            public void onResponse(Call<SpeakingTest> call, Response<SpeakingTest> response) {
                SpeakingTest speakingTest = response.body();
                titleExercise.setText(speakingTest.getData().getExercise().getTitle());
                instructionsExercise.setText(speakingTest.getData().getExercise().getInstructions());
                //Manage exerciseContent and clickable options



                finishTest.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("ReadingTests").document(userID);

                    Map<String, Object> test = new HashMap<>();
                    test.put("title", speakingTest.getData().getExercise().getTitle());
                    test.put("grade", (grade*10.0)/SpeakingTestActivity.position);

                    documentReference.set(test);

                    Toast.makeText(SpeakingTestActivity.this, "Your grade is:\t" + (grade*10.0)/SpeakingTestActivity.position, Toast.LENGTH_LONG).show();
                    SpeakingTestActivity.position = 0;
                    startActivity(new Intent(SpeakingTestActivity.this, StudentMainActivity.class));
                });
            }

            @Override
            public void onFailure(Call<SpeakingTest> call, Throwable t) {
                Toast.makeText(SpeakingTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Intent intentFail = new Intent(SpeakingTestActivity.this, StudentMainActivity.class);
                startActivity(intentFail);
            }
        });
    }
}