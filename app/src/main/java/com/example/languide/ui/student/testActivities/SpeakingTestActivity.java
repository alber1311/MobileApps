package com.example.languide.ui.student.testActivities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ReadingTest;
import com.example.languide.tests.SpeakingTest;
import com.example.languide.tests.VocabularyTest;
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

public class SpeakingTestActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private TextView titleExercise;
    private TextView exerciseContent;
    private TextView instructionsExercise;
    private Button finishTest;
    private Button record;
    private Button stop;
    private Button play;
    private Button next;

    private TextView answer;

    private MediaRecorder recorder;
    private MediaPlayer player;

    private int grade = 0;
    private static int position = 0;
    private String test;
    private String difficulty;

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
        //stop = findViewById(R.id.stop);
        //play = findViewById(R.id.play);
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
            /*if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SpeakingTestActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
            } else {
                Toast.makeText(SpeakingTestActivity.this, "You have permissions", Toast.LENGTH_SHORT).show();
            }


            recorder = new MediaRecorder();
            recorder.reset();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            file = new File(Environment.getExternalStorageDirectory(),"Recording.3gp");
            if(!file.exists()){ // Si no existe, crea el archivo.
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            recorder.setOutputFile(file);
            try {
                recorder.prepare();
                recorder.start();
                Toast.makeText(SpeakingTestActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.forLanguageTag("en"));
            try {
                startActivityForResult(intent, 100);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });

        /*stop.setOnClickListener(v -> {
            if (recorder != null) {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
                Toast.makeText(SpeakingTestActivity.this, "Recording succeed", Toast.LENGTH_SHORT).show();
            }

        });
        play.setOnClickListener(v -> {
            player = new MediaPlayer();

            try {
                player.setDataSource(String.valueOf(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.setOnPreparedListener(mp -> {
                player.start();
                Toast.makeText(SpeakingTestActivity.this, "Playing recording", Toast.LENGTH_SHORT).show();
            });

        });*/

        next.setOnClickListener(v -> {
            if (SpeakingTestActivity.position < speakingTest.getItems().size() - 1){
                SpeakingTestActivity.position++;
                exerciseContent.setText(speakingTest.getItems().get(SpeakingTestActivity.position).getText1());
            }
        });

        finishTest.setOnClickListener(v -> {
            //Calcula la nota a través del array de answers comparando en un for con las answers del json parseado en speakingTest
            Intent intent = new Intent(SpeakingTestActivity.this, TestResultActivity.class);
            intent.putExtra("exercise", "finalResolvedTest");
            intent.putExtra("grade", (grade*10.0)/SpeakingTestActivity.position);
            SpeakingTestActivity.position = 0;
            startActivity(intent);
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

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}