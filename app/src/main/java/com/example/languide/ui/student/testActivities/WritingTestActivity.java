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
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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

public class WritingTestActivity extends AppCompatActivity {

    private TextView titleExercise;
    private TextView exerciseContent;
    private TextView instructionsExercise;
    private TextView keyWord;
    private EditText answer;
    private Button nextQuestion;
    private Button finishTest;

    private String test;
    private String difficulty;
    private ArrayList<String> answers = new ArrayList<>();

    private int grade = 0;
    private static int position = 0;
    private static int testNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_test);

        titleExercise = findViewById(R.id.idTitleExercise);
        instructionsExercise = findViewById(R.id.test_instructions);
        exerciseContent = findViewById(R.id.exercise_text);
        answer = findViewById(R.id.answerWriting);
        nextQuestion = findViewById(R.id.nextWriting);
        finishTest = findViewById(R.id.finishTest);
        keyWord = findViewById(R.id.keyword);

        Intent intent = getIntent();
        test = intent.getStringExtra("test_Name");
        difficulty = intent.getStringExtra("test_Difficulty");

        //loadTest();
        loadLocalTest();
    }

    public void loadLocalTest() {
        Gson gson=new Gson();
        InputStream in = getResources().openRawResource(R.raw.writingtest);
        String exercise = readTextFile(in);
        WritingTest writingTest = gson.fromJson(exercise, WritingTest.class);

        //Missing
        titleExercise.setText(writingTest.getTitle());
        instructionsExercise.setText(writingTest.getInstructions());
        exerciseContent.setText(writingTest.getItems().get(WritingTestActivity.position).toString());
        keyWord.setText(writingTest.getItems().get(WritingTestActivity.position).getWord());

        nextQuestion.setOnClickListener(v -> {
            if (writingTest.getItems().size() - 1 != WritingTestActivity.position) {
                try{
                    String ans = answer.getText().toString();
                    if(!ans.isEmpty()){
                        answers.add(ans);
                        if(answers.get(WritingTestActivity.position).equals(writingTest.getItems().get(WritingTestActivity.position).getAnswer())) {
                            grade ++;
                        }
                        WritingTestActivity.position++;
                        exerciseContent.setText(writingTest.getItems().get(WritingTestActivity.position).toString());
                        keyWord.setText(writingTest.getItems().get(WritingTestActivity.position).getWord());
                        answer.setText("");
                    } else {
                        Toast.makeText(WritingTestActivity.this, "Fill your answer!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(WritingTestActivity.this, "Fill your answer!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(WritingTestActivity.this, "You finished the test!", Toast.LENGTH_SHORT).show();
                answer.setText("");
            }
        });

        finishTest.setOnClickListener(v -> {
            String resolvedTest = loadResolvedTest(writingTest);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("WritingTests").document(userID);

            documentReference.addSnapshotListener((value, error) -> {
                if(value != null && value.exists()){
                    Map<String, Object> size = value.getData();
                    WritingTestActivity.testNumber = size.size() + 1;
                }else{
                    WritingTestActivity.testNumber = 1;
                }
            });

            Map<String, Object> test = new HashMap<>();
            test.put("title", writingTest.getTitle());
            test.put("grade", (grade*10.0)/WritingTestActivity.position);

            Map<String, Object> testCollection = new HashMap<>();
            testCollection.put(String.valueOf(WritingTestActivity.testNumber), test);

            documentReference.update(testCollection).addOnFailureListener(e -> documentReference.set(testCollection));

            Intent intent = new Intent(WritingTestActivity.this, TestResultActivity.class);
            intent.putExtra("exercise", resolvedTest);
            intent.putExtra("grade", (grade*10.0)/WritingTestActivity.position);
            WritingTestActivity.position = 0;
            startActivity(intent);
        });
    }

    private String loadResolvedTest(WritingTest writingTest) {
        String st = "";
        for (int i = 0; i < writingTest.getItems().size(); i++){
            st += writingTest.getItems().get(i).getAnswer() + " / ";
        }
        return st;
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


                finishTest.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("ReadingTests").document(userID);

                    Map<String, Object> test = new HashMap<>();
                    test.put("title", writingTest.getTitle());
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