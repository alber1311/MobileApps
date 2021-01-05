package com.example.languide.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import  com.example.languide.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ChooseTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_test);

        ListView listView;
        ArrayList<String> tests = new ArrayList<>();
        tests.add("Listening");
        tests.add("Vocabulary");
        tests.add("Writing");
        tests.add("Speaking");
        tests.add("Reading");

        listView = findViewById(R.id.idListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_list_item, tests);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChooseTestActivity.this, ChooseDifficultyActivity.class);
                intent.putExtra("test_name", tests.get(position));
                startActivity(intent);
            }
        });
    }
}