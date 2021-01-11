package com.example.languide.ui.student.testActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ReadingTest;
import com.example.languide.ui.student.StudentMainActivity;
import com.example.languide.ui.student.TestResultActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReadingTestActivity extends AppCompatActivity {

    private TextView titleExercise;
    private TextView exerciseContent;
    private TextView instructionsExercise;
    private Button finishTest;
    private ListView listView;

    private String resolvedTest;
    private String resolvedTest1;

    private int grade = 0;
    private static int position = 0;

    private ReadingTest readingTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reding_test);

        titleExercise = findViewById(R.id.idTitleExercise);
        instructionsExercise = findViewById(R.id.test_instructions);
        exerciseContent = findViewById(R.id.exercise_text);
        finishTest = findViewById(R.id.finishTest);
        listView = findViewById(R.id.choices);

        loadTest();
    }

    public void loadTest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://languide-app.herokuapp.com/repository/").addConverterFactory(GsonConverterFactory.create()).build();
        TestService service = retrofit.create(TestService.class);

        Call<ReadingTest> call = service.loadReadingTest();
        call.enqueue(new Callback<ReadingTest>() {
            @Override
            public void onResponse(Call<ReadingTest> call, Response<ReadingTest> response) {
                readingTest = response.body();
                titleExercise.setText(readingTest.getData().getExercise().getTitle());
                instructionsExercise.setText(readingTest.getData().getExercise().getInstructions());
                exerciseContent.setText(readingTest.toString());
                exerciseContent.setMovementMethod(new ScrollingMovementMethod());
                resolvedTest = readingTest.toString();
                //Manage exerciseContent and clickable options
                position = 0;
                final int[] j = new int[1];
                ArrayList<String> choices = new ArrayList<>();
                ArrayList<Boolean> choicesResult = new ArrayList<>();
                for(j[0] = 0 ; j[0] <  readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().size() ; j[0]++) {
                    choices.add(readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getText());
                    choicesResult.add(readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getCorrect());
                    if (readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getCorrect()){
                        resolvedTest1 = resolvedTest.replaceFirst("_____", readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getText());
                    }
                }
                AtomicReference<ArrayAdapter<String>> adapter = new AtomicReference<>(new ArrayAdapter<>(ReadingTestActivity.this, android.R.layout.simple_list_item_1, choices));
                listView.setAdapter(adapter.get());
                listView.setOnItemClickListener((parent, view, position, id) -> {

                    if (choicesResult.get(position).equals(true)){
                        grade++;
                    }
                    ReadingTestActivity.position++;
                    if( ReadingTestActivity.position >= readingTest.getData().getExercise().getItems().size()) {
                        listView.setOnItemClickListener(null);
                        listView.setAdapter(null);
                    } else {
                        choices.clear();
                        choicesResult.clear();
                        for(j[0] = 0 ; j[0] <  readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().size() ; j[0]++) {
                            choices.add(readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getText());
                            choicesResult.add(readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getCorrect());
                            if (readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getCorrect()){
                                resolvedTest1 = resolvedTest1.replaceFirst("_____", readingTest.getData().getExercise().getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getText());
                            }
                        }
                        adapter.set(new ArrayAdapter<String>(ReadingTestActivity.this, android.R.layout.simple_list_item_1, choices));
                        listView.setAdapter(adapter.get());
                    }
                });

                String finalResolvedTest = resolvedTest1;
                finishTest.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("ReadingTests").document(userID);

                    Map<String, Object> test = new HashMap<>();
                    test.put("title", readingTest.getData().getExercise().getTitle());
                    test.put("grade", (grade*10.0)/ReadingTestActivity.position);

                    documentReference.set(test);
                    Intent intent = new Intent(ReadingTestActivity.this, TestResultActivity.class);
                    intent.putExtra("exercise", finalResolvedTest);
                    intent.putExtra("grade", (grade*10.0)/ReadingTestActivity.position);
                    Toast.makeText(ReadingTestActivity.this, "Your grade is:\t" + (grade*10.0)/ReadingTestActivity.position, Toast.LENGTH_LONG).show();
                    ReadingTestActivity.position = 0;
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(Call<ReadingTest> call, Throwable t) {
                Toast.makeText(ReadingTestActivity.this, "Something went wrong\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intentFail = new Intent(ReadingTestActivity.this, StudentMainActivity.class);
                startActivity(intentFail);
            }
        });
    }
}