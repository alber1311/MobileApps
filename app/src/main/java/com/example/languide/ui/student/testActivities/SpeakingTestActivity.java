package com.example.languide.ui.student.testActivities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ReadingTest;
import com.example.languide.tests.SpeakingTest;
import com.example.languide.tests.VocabularyTest;
import com.example.languide.ui.login.LoginActivity;
import com.example.languide.ui.student.ProfileActivity;
import com.example.languide.ui.student.StudentMainActivity;
import com.example.languide.ui.student.TestResultActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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
    private Button record;
    private Button next;

    private TextView answer;

    private int grade = 0;
    private static int position = 0;
    private static int testNumber = 0;

    private String st;
    private String path;
    private  File file;

    private ArrayList<String> answers = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_test);

        titleExercise = findViewById(R.id.idTitleExercise);
        instructionsExercise = findViewById(R.id.test_instructions);
        exerciseContent = findViewById(R.id.exercise_text);
        finishTest = findViewById(R.id.finishTest);
        record = findViewById(R.id.record);
        next = findViewById(R.id.next);
        answer = findViewById(R.id.answer);

        //loadTest();
        loadLocalTest();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){
            if (resultCode==RESULT_OK && null!=data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                answers.add(result.get(0));
                answer.setText(result.get(0));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadLocalTest() {
        Gson gson=new Gson();
        InputStream in = getResources().openRawResource(R.raw.speakingtest);
        String exercise = readTextFile(in);
        SpeakingTest speakingTest = gson.fromJson(exercise, SpeakingTest.class);

        st = speakingTest.toString();

        titleExercise.setText(speakingTest.getTitle());
        instructionsExercise.setText(speakingTest.getInstructions());
        exerciseContent.setText(speakingTest.getItems().get(SpeakingTestActivity.position).getText1());
        exerciseContent.setMovementMethod(new ScrollingMovementMethod());

        record.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.forLanguageTag("en"));
            try {
                startActivityForResult(intent, 100);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });

        next.setOnClickListener(v -> {
            if (SpeakingTestActivity.position < speakingTest.getItems().size() - 1){
                SpeakingTestActivity.position++;
                exerciseContent.setText(speakingTest.getItems().get(SpeakingTestActivity.position).getText1());
            }
        });

        finishTest.setOnClickListener(v -> {
            if (answers.size() != speakingTest.getItems().size()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("EXIT");
                builder.setMessage("Are you sure you want to exit the test without finishing?");
                builder.setPositiveButton("YES", (dialog, which) -> {
                    Toast.makeText(SpeakingTestActivity.this, "Test wasn´t saved, finish the test to see the results!", Toast.LENGTH_LONG).show();
                    Intent intento = new Intent(SpeakingTestActivity.this, TestResultActivity.class);
                    intento.putExtra("exercise", st);
                    intento.putExtra("grade", 0);
                    startActivity(intento);
                });
                builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                //Calcula la nota a través del array de answers comparando en un for con las answers del json parseado en speakingTest
                for (int i = 0; i < speakingTest.getItems().size(); i++) {
                    if(answers.get(i).equals(speakingTest.getItems().get(i).getAnswer())) {
                        Log.println(Log.INFO, "ANSWER", answers.get(i));
                        grade++;
                    }
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference documentReference = db.collection("SpeakingTests").document(userID);

                documentReference.addSnapshotListener((value, error) -> {
                    if(value != null && value.exists()){
                        Map<String, Object> size = value.getData();
                        SpeakingTestActivity.testNumber = size.size() + 1;
                    }else{
                        SpeakingTestActivity.testNumber = 1;
                    }

                });

                Map<String, Object> test = new HashMap<>();
                test.put("title", speakingTest.getTitle());
                if(SpeakingTestActivity.position != 0){
                    test.put("grade", (grade*10.0)/speakingTest.getItems().size());
                } else {
                    test.put("grade", 0);
                }


                Map<String, Object> testCollection = new HashMap<>();
                testCollection.put(String.valueOf(SpeakingTestActivity.testNumber), test);

                documentReference.update(testCollection).addOnFailureListener(e -> documentReference.set(testCollection));

                Intent intent = new Intent(SpeakingTestActivity.this, TestResultActivity.class);
                intent.putExtra("exercise", st);
                intent.putExtra("grade", (grade*10.0)/SpeakingTestActivity.position);
                SpeakingTestActivity.position = 0;
                startActivity(intent);
            }
        });
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException ignored) {

        }
        return outputStream.toString();
    }

    public void loadTest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://languide-app.herokuapp.com/repository/api/exercise/").addConverterFactory(GsonConverterFactory.create()).build();
        TestService service = retrofit.create(TestService.class);

        Call<SpeakingTest> call = service.loadSpeakingTest();
        call.enqueue(new Callback<SpeakingTest>() {
            @Override
            public void onResponse(Call<SpeakingTest> call, Response<SpeakingTest> response) {
                SpeakingTest speakingTest = response.body();
                titleExercise.setText(speakingTest.getTitle());
                instructionsExercise.setText(speakingTest.getInstructions());
                //Manage exerciseContent and clickable options



                finishTest.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("ReadingTests").document(userID);

                    Map<String, Object> test = new HashMap<>();
                    test.put("title", speakingTest.getTitle());
                    test.put("grade", (grade*10.0)/SpeakingTestActivity.position);

                    documentReference.set(test);

                    Intent intent = new Intent(SpeakingTestActivity.this, TestResultActivity.class);
                    intent.putExtra("grade", (grade*10.0)/SpeakingTestActivity.position);
                    intent.putExtra("exercise", speakingTest.toString());
                    SpeakingTestActivity.position = 0;
                    startActivity(intent);
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