package com.example.languide.ui.student.showPrevTests;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.languide.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ShowWritingTestActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private ListView listView;
    private TextView average;

    private double sum = 0;
    private int number = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_writing_test);

        listView = findViewById(R.id.idWritingPastTests);
        average = findViewById(R.id.averageWriting);

        db = FirebaseFirestore.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DocumentReference documentReference = db.collection("WritingTests").document(userID);
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;

            try {
                Set<String> keys = value.getData().keySet();

                ArrayList<String> entry = new ArrayList<>();
                for (String key : keys) {
                    Map<String, Object> m = (Map<String, Object>) value.getData().get(key);
                    entry.add("Title:\t" + m.get("title") + "\nGrade: " + m.get("grade"));
                    sum += Double.parseDouble(m.get("grade").toString());
                    number++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowWritingTestActivity.this, android.R.layout.simple_list_item_1, entry);
                listView.setAdapter(adapter);
                double totalResult = sum/number;
                String totalAverage = "Average:\t" + String.format("%.2f", totalResult);
                average.setText(totalAverage);
            } catch (Exception e) {
                String exc = "No tests done yet";
                average.setText(exc);
            }
        });
    }
}