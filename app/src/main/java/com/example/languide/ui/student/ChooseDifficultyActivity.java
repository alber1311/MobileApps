package com.example.languide.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(ChooseDifficultyActivity.this, TestActivity.class);
                //intent.putExtra("test_Name", testName);
                //intent.putExtra("test_Difficulty", difficulty.get(position));
                //startActivity(intent);
            }
        });

    }
}