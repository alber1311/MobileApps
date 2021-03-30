package com.example.languide.ui.student.testActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

    private int grade = 0;
    private static int position = 0;
    private static int testNumber = 1;

    private ReadingTest readingTest;
    private String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reding_test);

        titleExercise = findViewById(R.id.idTitleExercise);
        instructionsExercise = findViewById(R.id.test_instructions);
        exerciseContent = findViewById(R.id.exercise_text);
        finishTest = findViewById(R.id.finishTest);
        listView = findViewById(R.id.choices);

        try {
            loadLocalTest();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(ReadingTestActivity.this, "Something went wronk when retrieving the test :(", Toast.LENGTH_LONG).show();
        }
    }

    public void loadTest() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://languide-app.herokuapp.com/repository/").addConverterFactory(GsonConverterFactory.create()).build();
        TestService service = retrofit.create(TestService.class);

        Call<ReadingTest> call = service.loadReadingTest();
        call.enqueue(new Callback<ReadingTest>() {
            @Override
            public void onResponse(Call<ReadingTest> call, Response<ReadingTest> response) {
                readingTest = response.body();
                titleExercise.setText(readingTest.getTitle());
                instructionsExercise.setText(readingTest.getInstructions());
                exerciseContent.setText(readingTest.toString());
                exerciseContent.setMovementMethod(new ScrollingMovementMethod());
                resolvedTest = readingTest.toString();
                //Manage exerciseContent and clickable options
                ReadingTestActivity.position = 0;
                final int[] j = new int[1];
                ArrayList<String> choices = new ArrayList<>();
                ArrayList<Boolean> choicesResult = new ArrayList<>();
                for(j[0] = 0 ; j[0] <  readingTest.getItems().get(ReadingTestActivity.position).getChoices().size() ; j[0]++) {
                    choices.add(j[0] + 1 + "\t" + readingTest.getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getText());
                    choicesResult.add(readingTest.getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getCorrect());
                }
                AtomicReference<ArrayAdapter<String>> adapter = new AtomicReference<>(new ArrayAdapter<>(ReadingTestActivity.this, android.R.layout.simple_list_item_1, choices));
                listView.setAdapter(adapter.get());
                listView.setOnItemClickListener((parent, view, position, id) -> {

                    if (choicesResult.get(position).equals(true)){
                        grade++;
                    }
                    ReadingTestActivity.position++;
                    if( ReadingTestActivity.position >= readingTest.getItems().size()) {
                        listView.setOnItemClickListener(null);
                        listView.setAdapter(null);
                    } else {
                        choices.clear();
                        choicesResult.clear();
                        for(j[0] = 0 ; j[0] <  readingTest.getItems().get(ReadingTestActivity.position).getChoices().size() ; j[0]++) {
                            choices.add(j[0] + 1 + "\t" + readingTest.getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getText());
                            choicesResult.add(readingTest.getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getCorrect());
                        }
                        adapter.set(new ArrayAdapter<String>(ReadingTestActivity.this, android.R.layout.simple_list_item_1, choices));
                        listView.setAdapter(adapter.get());
                    }
                });
                String finalResolvedTest = loadResolvedTest(readingTest);
                finishTest.setOnClickListener(v -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("ReadingTests").document(userID);

                    documentReference.addSnapshotListener((value, error) -> {
                        Map<String, Object> size = value.getData();
                        ReadingTestActivity.testNumber = size.size();
                    });

                    Map<String, Object> test = new HashMap<>();
                    test.put("title", readingTest.getTitle());
                    test.put("grade", (grade*10.0)/ReadingTestActivity.position);

                    Map<String, Object> testCollection = new HashMap<>();
                    testCollection.put(String.valueOf(ReadingTestActivity.testNumber), test);

                    documentReference.update(testCollection).addOnFailureListener(e -> documentReference.set(testCollection));

                    //documentReference.set(testCollection);
                    Intent intent = new Intent(ReadingTestActivity.this, TestResultActivity.class);
                    intent.putExtra("exercise", finalResolvedTest);
                    intent.putExtra("grade", (grade*10.0)/ReadingTestActivity.position);
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

    public void loadLocalTest() throws FileNotFoundException {
        Gson gson=new Gson();
        InputStream in = getResources().openRawResource(R.raw.readingtest);
        String exercise = readTextFile(in);
        ReadingTest readingTest = gson.fromJson(exercise, ReadingTest.class);

        st = readingTest.toString();

        titleExercise.setText(readingTest.getTitle());
        instructionsExercise.setText(readingTest.getInstructions());
        exerciseContent.setText(readingTest.toString());
        exerciseContent.setMovementMethod(new ScrollingMovementMethod());
        resolvedTest = readingTest.toString();
        //Manage exerciseContent and clickable options
        ReadingTestActivity.position = 0;
        final int[] j = new int[1];
        ArrayList<String> choices = new ArrayList<>();
        ArrayList<Boolean> choicesResult = new ArrayList<>();
        for(j[0] = 0 ; j[0] <  readingTest.getItems().get(ReadingTestActivity.position).getChoices().size() ; j[0]++) {
            choices.add(readingTest.getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getText());
            choicesResult.add(readingTest.getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getCorrect());
        }
        AtomicReference<ArrayAdapter<String>> adapter = new AtomicReference<>(new ArrayAdapter<>(ReadingTestActivity.this, android.R.layout.simple_list_item_1, choices));
        listView.setAdapter(adapter.get());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selection = choices.get(position);
            st = st.replaceFirst("_____", " [ " + selection + " ] ");
            exerciseContent.setText(st);

            if (choicesResult.get(position).equals(true)){
                grade++;
            }
            ReadingTestActivity.position++;
            if( ReadingTestActivity.position >= readingTest.getItems().size()) {
                listView.setOnItemClickListener(null);
                listView.setAdapter(null);
            } else {
                choices.clear();
                choicesResult.clear();
                for(j[0] = 0 ; j[0] <  readingTest.getItems().get(ReadingTestActivity.position).getChoices().size() ; j[0]++) {
                    choices.add(readingTest.getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getText());
                    choicesResult.add(readingTest.getItems().get(ReadingTestActivity.position).getChoices().get(j[0]).getCorrect());
                }
                adapter.set(new ArrayAdapter<String>(ReadingTestActivity.this, android.R.layout.simple_list_item_1, choices));
                listView.setAdapter(adapter.get());
            }
        });
        String finalResolvedTest = loadResolvedTest(readingTest);
        finishTest.setOnClickListener(v -> {
            if (ReadingTestActivity.position != readingTest.getItems().size()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("EXIT");
                builder.setMessage("Are you sure you want to exit the test without finishing?");
                builder.setPositiveButton("YES", (dialog, which) -> {
                    Toast.makeText(ReadingTestActivity.this, "Test wasn´t saved, finish the test to see the results!", Toast.LENGTH_LONG).show();
                    Intent intento = new Intent(ReadingTestActivity.this, TestResultActivity.class);
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
                DocumentReference documentReference = db.collection("ReadingTests").document(userID);

                documentReference.addSnapshotListener((value, error) -> {
                    if(value != null && value.exists()){
                        Map<String, Object> size = value.getData();
                        ReadingTestActivity.testNumber = size.size() + 1;
                    }else{
                        ReadingTestActivity.testNumber = 1;
                    }
                });

                Map<String, Object> test = new HashMap<>();
                test.put("title", readingTest.getTitle());
                test.put("grade", (grade*10.0)/ReadingTestActivity.position);

                Map<String, Object> testCollection = new HashMap<>();
                testCollection.put(String.valueOf(ReadingTestActivity.testNumber), test);

                documentReference.update(testCollection).addOnFailureListener(e -> documentReference.set(testCollection));

                //documentReference.set(testCollection);
                Intent intent = new Intent(ReadingTestActivity.this, TestResultActivity.class);
                intent.putExtra("exercise", finalResolvedTest);
                intent.putExtra("grade", (grade*10.0)/ReadingTestActivity.position);
                ReadingTestActivity.position = 0;
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

    public String loadResolvedTest(ReadingTest test){
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
}