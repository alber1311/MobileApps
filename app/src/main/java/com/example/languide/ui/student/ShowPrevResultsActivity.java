package com.example.languide.ui.student;

import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class ShowPrevResultsActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private String userID;
    private ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prev_results);

        listView = findViewById(R.id.idPastTests);

        db = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("ReadingTests").document(userID);
        documentReference.addSnapshotListener((value, error) -> {
            ArrayList<String> tests = new ArrayList<>();
            assert value != null;
            tests.add("Title:\t" + value.get("title") + ":\t" + value.get("grade") + "/10");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowPrevResultsActivity.this, android.R.layout.simple_list_item_1, tests);
            listView.setAdapter(adapter);
        });
    }
}