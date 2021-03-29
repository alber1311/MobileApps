package com.example.languide.ui.student.testActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ListeningTest;
import com.example.languide.tests.ReadingTest;
import com.example.languide.ui.student.StudentMainActivity;
import com.example.languide.ui.student.TestResultActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListeningTestActivity extends AppCompatActivity {

    private TextView titleExercise;
    private TextView instructionsExercise;
    private TextView exerciseContent;
    private EditText answerListening;
    private Button finishTest;
    private Button playAudio;
    private  Button nextQuestion;


    private int grade = 0;
    private static int position = 0;
    private static int testNumber = 0;

    private ArrayList<String> answers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_test);

        answerListening = findViewById(R.id.answerListening);
        titleExercise = findViewById(R.id.idTitleExercise);
        instructionsExercise = findViewById(R.id.test_instructions);
        exerciseContent = findViewById(R.id.exercise_text);
        finishTest = findViewById(R.id.finishTest);
        playAudio = findViewById(R.id.playAudio);
        nextQuestion = findViewById(R.id.nextQuestion);
        //loadTest();

        loadLocalTest();
    }

    public void loadLocalTest() {
        Gson gson=new Gson();
        InputStream in = getResources().openRawResource(R.raw.listeningtest);
        String exercise = readTextFile(in);
        ListeningTest listeningTest = gson.fromJson(exercise, ListeningTest.class);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.listeningaudio);

        titleExercise.setText(listeningTest.getTitle());
        instructionsExercise.setText(listeningTest.getInstructions());
        exerciseContent.setText(listeningTest.getItems().get(ListeningTestActivity.position).getText());
        nextQuestion.setOnClickListener(v -> {
            try {
                String answer = answerListening.getText().toString();
                if(answer.isEmpty()){
                    Toast.makeText(ListeningTestActivity.this, "Enter your answer", Toast.LENGTH_SHORT).show();
                } else {
                    answers.add(answer);
                    if(answer.equals(listeningTest.getItems().get(ListeningTestActivity.position).getGap())) {
                        grade++;
                    }
                    ListeningTestActivity.position++;
                    if(listeningTest.getItems().size() != ListeningTestActivity.position){
                        exerciseContent.setText(listeningTest.getItems().get(ListeningTestActivity.position).getText());
                    } else {
                        exerciseContent.setText("");
                    }
                    answerListening.setText("");
                }
            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(ListeningTestActivity.this, "Enter your answer", Toast.LENGTH_SHORT).show();
            }
        });

        //When everything is loaded set a ButtonClickListener to start playing the audio file.
        playAudio.setOnClickListener(v -> {
            if (playAudio.getText().toString().equals("PLAY!")){
                mediaPlayer.start();
                playAudio.setText("STOP!");
            } else {
                mediaPlayer.pause();
                playAudio.setText("PLAY!");
            }
        });

        finishTest.setOnClickListener(v -> {
            String finalResolvedTest = loadResolvedTest(listeningTest);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("ListeningTests").document(userID);

            documentReference.addSnapshotListener((value, error) -> {
                if(value != null && value.exists()){
                    Map<String, Object> size = value.getData();
                    ListeningTestActivity.testNumber = size.size() + 1;
                }else{
                    ListeningTestActivity.testNumber = 1;
                }

            });

            Map<String, Object> test = new HashMap<>();
            test.put("title", listeningTest.getTitle());
            if(ListeningTestActivity.position != 0){
                test.put("grade", (grade*10.0)/ListeningTestActivity.position);
            } else {
                test.put("grade", 0);
            }


            Map<String, Object> testCollection = new HashMap<>();
            testCollection.put(String.valueOf(ListeningTestActivity.testNumber), test);

            documentReference.update(testCollection).addOnFailureListener(e -> documentReference.set(testCollection));

            //documentReference.set(testCollection);
            Intent intent = new Intent(ListeningTestActivity.this, TestResultActivity.class);
            intent.putExtra("exercise", finalResolvedTest);
            intent.putExtra("grade", (grade*10.0)/ListeningTestActivity.position);
            ListeningTestActivity.position = 0;
            startActivity(intent);
        });
    }

    public String loadResolvedTest(ListeningTest test){
        return test.toString();
    }

    public void loadTest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://languide-app.herokuapp.com/repository/api/exercise/").addConverterFactory(GsonConverterFactory.create()).build();
        TestService service = retrofit.create(TestService.class);

        Call<ListeningTest> call = service.loadListeningTest();
        call.enqueue(new Callback<ListeningTest>() {
            @Override
            public void onResponse(@NotNull Call<ListeningTest> call, @NotNull Response<ListeningTest> response) {
                ListeningTest listeningTest = response.body();
                titleExercise.setText(listeningTest.getTitle());
                instructionsExercise.setText(listeningTest.getInstructions());
                //exerciseAudio = listeningTest.getAudio();
                //Manage the audio

                finishTest.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("ListeningTests").document(userID);

                    Map<String, Object> test = new HashMap<>();
                    test.put("title", listeningTest.getTitle());
                    test.put("grade", (grade*10.0)/ListeningTestActivity.position);

                    documentReference.set(test);

                    Intent intent = new Intent(ListeningTestActivity.this, TestResultActivity.class);
                    intent.putExtra("grade", (grade*10.0)/ListeningTestActivity.position);
                    intent.putExtra("exercise", listeningTest.toString());
                    ListeningTestActivity.position = 0;
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(@NotNull Call<ListeningTest> call, @NotNull Throwable t) {
                Toast.makeText(ListeningTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Intent intentFail = new Intent(ListeningTestActivity.this, StudentMainActivity.class);
                startActivity(intentFail);
            }
        });
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}