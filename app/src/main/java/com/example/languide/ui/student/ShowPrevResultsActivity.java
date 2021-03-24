package com.example.languide.ui.student;

import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.languide.R;
import com.example.languide.ui.student.testActivities.ReadingTestActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ShowPrevResultsActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private ListView listView;
    private TextView average;

    private double sum = 0;
    private int number = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prev_results);

        listView = findViewById(R.id.idPastTests);
        average = findViewById(R.id.average);

        db = FirebaseFirestore.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ArrayList<String> tests = new ArrayList<>();

        DocumentReference documentReference = db.collection("ReadingTests").document(userID);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowPrevResultsActivity.this, android.R.layout.simple_list_item_1, entry);
                listView.setAdapter(adapter);
                double totalResult = sum/number;
                String totalAverage = "Average:\t" + String.format("%.2f", totalResult);
                average.setText(totalAverage);
            } catch (Exception e) {
                average.setText("No tests done yet!");
            }
        });

        /*DocumentReference documentReference2 = db.collection("ListeningTests").document(userID);
        documentReference2.addSnapshotListener((value, error) -> {
            assert value != null;
            tests.add("Listening Tests:\nTitle:\t" + value.get("title") + ":\t" + value.get("grade") + "/10\n");
        });

        DocumentReference documentReference3 = db.collection("WritingTests").document(userID);
        documentReference3.addSnapshotListener((value, error) -> {
            assert value != null;
            tests.add("Writing Tests:\nTitle:\t" + value.get("title") + ":\t" + value.get("grade") + "/10\n");
        });

        DocumentReference documentReference4 = db.collection("SpeakingTests").document(userID);
        documentReference4.addSnapshotListener((value, error) -> {
            assert value != null;
            tests.add("Speaking Tests:\nTitle:\t" + value.get("title") + ":\t" + value.get("grade") + "/10\n");
        });

        DocumentReference documentReference5 = db.collection("VocabularyTests").document(userID);
        documentReference5.addSnapshotListener((value, error) -> {
            assert value != null;
            tests.add("Vocabulary Tests:\nTitle:\t" + value.get("title") + ":\t" + value.get("grade") + "/10");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowPrevResultsActivity.this, android.R.layout.simple_list_item_1, tests);
            listView.setAdapter(adapter);
        });*/
    }
}