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
        ArrayList<String> difficulty = new ArrayList<>();
        difficulty.add("Easy");
        difficulty.add("Medium");
        difficulty.add("Hard");

        listView = findViewById(R.id.idListView2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_list_item, difficulty);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (testName) {
                case "Listening":
                    Intent intent1 = new Intent(ChooseDifficultyActivity.this, ListeningTestActivity.class);
                    intent1.putExtra("test_Name", testName);
                    intent1.putExtra("test_Difficulty", difficulty.get(position));
                    startActivity(intent1);
                    break;
                case "Vocabulary":
                    Intent intent2 = new Intent(ChooseDifficultyActivity.this, VocabularyTestActivity.class);
                    intent2.putExtra("test_Name", testName);
                    intent2.putExtra("test_Difficulty", difficulty.get(position));
                    startActivity(intent2);
                    break;
                case "Writing":
                    Intent intent3 = new Intent(ChooseDifficultyActivity.this, WritingTestActivity.class);
                    intent3.putExtra("test_Name", testName);
                    intent3.putExtra("test_Difficulty", difficulty.get(position));
                    startActivity(intent3);
                    break;
                case "Speaking":
                    Intent intent4 = new Intent(ChooseDifficultyActivity.this, SpeakingTestActivity.class);
                    intent4.putExtra("test_Name", testName);
                    intent4.putExtra("test_Difficulty", difficulty.get(position));
                    startActivity(intent4);
                    break;
                case "Reading":
                    Intent intent5 = new Intent(ChooseDifficultyActivity.this, ReadingTestActivity.class);
                    intent5.putExtra("test_Name", testName);
                    intent5.putExtra("test_Difficulty", difficulty.get(position));
                    startActivity(intent5);
                    break;
            }
        });

    }
}