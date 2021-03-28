package com.example.languide.ui.student;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.languide.R;
import com.example.languide.ui.student.showPrevTests.ShowReadingTestsActivity;
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

    private ListView listView;

    private double sum = 0;
    private int number = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prev_results);

        listView = findViewById(R.id.idPastTests);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selection = parent.getItemAtPosition(position).toString();
            if (selection.equals("Reading")){
                Intent intent = new Intent(ShowPrevResultsActivity.this, ShowReadingTestsActivity.class);
                startActivity(intent);
            /*} else if (selection.equals("Reading")){
                Intent intent = new Intent(ShowPrevResultsActivity.this, ShowListeningTestsActivity.class);
                startActivity(intent);
            } else if (selection.equals("Reading")){
                Intent intent = new Intent(ShowPrevResultsActivity.this, ShowVocabularyTestsActivity.class);
                startActivity(intent);
            } else if (selection.equals("Reading")){
                Intent intent = new Intent(ShowPrevResultsActivity.this, ShowWritingTestsActivity.class);
                startActivity(intent);
            } else if (selection.equals("Reading")){
                Intent intent = new Intent(ShowPrevResultsActivity.this, ShowSpeakingTestsActivity.class);
                startActivity(intent);*/
            } else {
                Toast.makeText(ShowPrevResultsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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