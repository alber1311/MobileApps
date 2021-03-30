package com.example.languide.ui.student.testActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ReadingTest;
import com.example.languide.tests.VocabularyTest;
import com.example.languide.ui.student.StudentMainActivity;
import com.example.languide.ui.student.TestResultActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VocabularyTestActivity extends AppCompatActivity {

    private TextView titleExercise;
    private TextView exerciseContent;
    private TextView instructionsExercise;
    private Button finishTest;

    private ListView listView;

    private String test;
    private String difficulty;

    private int grade = 0;
    private static int position = 0;
    private static int testNumber = 0;

    private String resolvedTest;

    private  String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_test);

        titleExercise = findViewById(R.id.idTitleExercise);
        instructionsExercise = findViewById(R.id.test_instructions);
        exerciseContent = findViewById(R.id.exercise_text);
        finishTest = findViewById(R.id.finishTest);
        listView = findViewById(R.id.choices);

        Intent intent = getIntent();
        test = intent.getStringExtra("test_Name");
        difficulty = intent.getStringExtra("test_Difficulty");

        //loadTest();
        loadLocalTest();
    }

    public void loadLocalTest() {
        Gson gson=new Gson();
        InputStream in = getResources().openRawResource(R.raw.vocabularytest);
        String exercise = readTextFile(in);
        VocabularyTest vocabularyTest = gson.fromJson(exercise, VocabularyTest.class);

        st = vocabularyTest.toString();

        titleExercise.setText(vocabularyTest.getTitle());
        instructionsExercise.setText(vocabularyTest.getInstructions());
        exerciseContent.setText(vocabularyTest.toString());
        exerciseContent.setMovementMethod(new ScrollingMovementMethod());
        resolvedTest = vocabularyTest.toString();
        //Manage exerciseContent and clickable options
        VocabularyTestActivity.position = 0;
        final int[] j = new int[1];
        ArrayList<String> choices = new ArrayList<>();
        ArrayList<Boolean> choicesResult = new ArrayList<>();
        for(j[0] = 0 ; j[0] <  vocabularyTest.getItems().get(VocabularyTestActivity.position).getChoices().size() ; j[0]++) {
            choices.add(vocabularyTest.getItems().get(VocabularyTestActivity.position).getChoices().get(j[0]).getText());
            choicesResult.add(vocabularyTest.getItems().get(VocabularyTestActivity.position).getChoices().get(j[0]).getCorrect());
        }
        AtomicReference<ArrayAdapter<String>> adapter = new AtomicReference<>(new ArrayAdapter<>(VocabularyTestActivity.this, android.R.layout.simple_list_item_1, choices));
        listView.setAdapter(adapter.get());
        listView.setOnItemClickListener((parent, view, position, id) -> {

            st = st.replaceFirst("_____", " " + choices.get(position) + " ");
            exerciseContent.setText(st);

            if (choicesResult.get(position).equals(true)){
                grade++;
            }
            VocabularyTestActivity.position++;
            if( VocabularyTestActivity.position >= vocabularyTest.getItems().size()) {
                listView.setOnItemClickListener(null);
                listView.setAdapter(null);
            } else {
                choices.clear();
                choicesResult.clear();
                for(j[0] = 0 ; j[0] <  vocabularyTest.getItems().get(VocabularyTestActivity.position).getChoices().size() ; j[0]++) {
                    choices.add(vocabularyTest.getItems().get(VocabularyTestActivity.position).getChoices().get(j[0]).getText());
                    choicesResult.add(vocabularyTest.getItems().get(VocabularyTestActivity.position).getChoices().get(j[0]).getCorrect());
                }
                adapter.set(new ArrayAdapter<String>(VocabularyTestActivity.this, android.R.layout.simple_list_item_1, choices));
                listView.setAdapter(adapter.get());
            }
        });
        String finalResolvedTest = loadResolvedTest(vocabularyTest);
        finishTest.setOnClickListener(v -> {
            if (VocabularyTestActivity.position != vocabularyTest.getItems().size()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("EXIT");
                builder.setMessage("Are you sure you want to exit the test without finishing?");
                builder.setPositiveButton("YES", (dialog, which) -> {
                    Toast.makeText(VocabularyTestActivity.this, "Test wasn´t saved, finish the test to see the results!", Toast.LENGTH_LONG).show();
                    Intent intento = new Intent(VocabularyTestActivity.this, TestResultActivity.class);
                    intento.putExtra("exercise", st);
                    intento.putExtra("grade", 0);
                    startActivity(intento);
                });
                builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference documentReference = db.collection("VocabularyTests").document(userID);

                documentReference.addSnapshotListener((value, error) -> {
                    if (value != null && value.exists()) {
                        Map<String, Object> size = value.getData();
                        VocabularyTestActivity.testNumber = size.size() + 1;
                    } else {
                        VocabularyTestActivity.testNumber = 1;
                    }
                });
                Map<String, Object> test = new HashMap<>();
                test.put("title", vocabularyTest.getTitle());
                test.put("grade", (grade*10.0)/vocabularyTest.getItems().size());

                Map<String, Object> testCollection = new HashMap<>();
                testCollection.put(String.valueOf(VocabularyTestActivity.testNumber), test);

                documentReference.update(testCollection).addOnFailureListener(e -> documentReference.set(testCollection));

                //documentReference.set(testCollection);
                Intent intent = new Intent(VocabularyTestActivity.this, TestResultActivity.class);
                intent.putExtra("exercise", finalResolvedTest);
                intent.putExtra("grade", (grade*10.0)/VocabularyTestActivity.position);
                VocabularyTestActivity.position = 0;
                startActivity(intent);
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

    public String loadResolvedTest(VocabularyTest test){
        String str = test.toString();
        for (int i = 0; i < test.getItems().size(); i++) {
            for(int j = 0 ; j <  test.getItems().get(i).getChoices().size() ; j++) {
                if (test.getItems().get(i).getChoices().get(j).getCorrect()) {
                    str = str.replaceFirst("_____", " " + test.getItems().get(i).getChoices().get(j).getText() + " ");
                }
            }
        }
        return str;
    }

    public void loadTest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        TestService service = retrofit.create(TestService.class);

        Call<VocabularyTest> call = service.loadVocabularyTest();
        call.enqueue(new Callback<VocabularyTest>() {
            @Override
            public void onResponse(Call<VocabularyTest> call, Response<VocabularyTest> response) {
                VocabularyTest vocabularyTest = response.body();
                titleExercise.setText(vocabularyTest.getTitle());
                instructionsExercise.setText(vocabularyTest.getInstructions());
                //Manage exerciseContent and clickable options



                finishTest.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("VocabularyTests").document(userID);

                    Map<String, Object> test = new HashMap<>();
                    test.put("title", vocabularyTest.getTitle());
                    test.put("grade", (grade*10.0)/VocabularyTestActivity.position);

                    documentReference.set(test);

                    Intent intent = new Intent(VocabularyTestActivity.this, TestResultActivity.class);
                    intent.putExtra("grade", (grade*10.0)/VocabularyTestActivity.position);
                    intent.putExtra("exercise", vocabularyTest.toString());
                    VocabularyTestActivity.position = 0;
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(Call<VocabularyTest> call, Throwable t) {
                Toast.makeText(VocabularyTestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Intent intentFail = new Intent(VocabularyTestActivity.this, StudentMainActivity.class);
                startActivity(intentFail);
            }
        });
    }
}