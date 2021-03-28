package com.example.languide.ui.student;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.languide.R;
import com.example.languide.ui.student.showPrevTests.*;

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
            } else if (selection.equals("Listening")){
                Intent intent = new Intent(ShowPrevResultsActivity.this, ShowListeningTestActivity.class);
                startActivity(intent);
            } else if (selection.equals("Vocabulary")){
                Intent intent = new Intent(ShowPrevResultsActivity.this, ShowVocabularyTestActivity.class);
                startActivity(intent);
            /*} else if (selection.equals("Reading")){
                Intent intent = new Intent(ShowPrevResultsActivity.this, ShowWritingTestsActivity.class);
                startActivity(intent);
            } else if (selection.equals("Reading")){
                Intent intent = new Intent(ShowPrevResultsActivity.this, ShowSpeakingTestsActivity.class);
                startActivity(intent);*/
            } else {
                Toast.makeText(ShowPrevResultsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        });
    }
}