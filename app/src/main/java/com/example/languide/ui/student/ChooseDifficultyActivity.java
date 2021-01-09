package com.example.languide.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.ui.student.testActivities.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ChooseDifficultyActivity extends AppCompatActivity {

    private String testName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);

        Intent intent = getIntent();
        testName = intent.getStringExtra("test_name");

        ListView listView;
        listView = findViewById(R.id.idListView2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(testName.equals("Listening") || testName.equals("Escucha") || testName.equals("Lyssnande")) {
                        Intent intent1 = new Intent(ChooseDifficultyActivity.this, ListeningTestActivity.class);
                        intent1.putExtra("test_Name", testName);
                        intent1.putExtra("test_Difficulty", parent.getItemAtPosition(position).toString());
                        startActivity(intent1);
                    }
                    else if(testName.equals("Vocabulary") || testName.equals("Vocabulario") || testName.equals("Ordförråd")) {
                        Intent intent2 = new Intent(ChooseDifficultyActivity.this, VocabularyTestActivity.class);
                        intent2.putExtra("test_Name", testName);
                        intent2.putExtra("test_Difficulty", parent.getItemAtPosition(position).toString());
                        startActivity(intent2);
                    }
                    else if(testName.equals("Writing") || testName.equals("Escritura") || testName.equals("Skrift")) {
                        Intent intent3 = new Intent(ChooseDifficultyActivity.this, WritingTestActivity.class);
                        intent3.putExtra("test_Name", testName);
                        intent3.putExtra("test_Difficulty", parent.getItemAtPosition(position).toString());
                        startActivity(intent3);
                    }
                    else if(testName.equals("Speaking") || testName.equals("Oral") || testName.equals("Tala")) {
                        Intent intent4 = new Intent(ChooseDifficultyActivity.this, SpeakingTestActivity.class);
                        intent4.putExtra("test_Name", testName);
                        intent4.putExtra("test_Difficulty", parent.getItemAtPosition(position).toString());
                        startActivity(intent4);
                    }
                    else if(testName.equals("Reading") || testName.equals("Lectura") || testName.equals("Läsning")) {
                        Intent intent5 = new Intent(ChooseDifficultyActivity.this, ReadingTestActivity.class);
                        intent5.putExtra("test_Name", testName);
                        intent5.putExtra("test_Difficulty", parent.getItemAtPosition(position).toString());
                        startActivity(intent5);
                    }
            }
        });

    }
}