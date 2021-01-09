package com.example.languide.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import  com.example.languide.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ChooseTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_test);

        ListView listView = findViewById(R.id.idListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChooseTestActivity.this, ChooseDifficultyActivity.class);
                intent.putExtra("test_name", parent.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });

    }
}