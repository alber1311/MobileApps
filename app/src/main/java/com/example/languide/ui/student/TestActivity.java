package com.example.languide.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import com.example.languide.*;
import com.example.languide.api.TestService;
import com.example.languide.tests.ListeningTest;
import com.example.languide.tests.ReadingTest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends AppCompatActivity {

    private String test;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        test = intent.getStringExtra("test_Name");
        difficulty = intent.getStringExtra("test_Difficulty");
    }

    public void loadTest(View view) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        TestService service = retrofit.create(TestService.class);

        switch(test) {
            case "Reading":
                Call<ReadingTest> call = service.loadReadingTest();
                call.enqueue(new Callback<ReadingTest>() {
                    @Override
                    public void onResponse(Call<ReadingTest> call, Response<ReadingTest> response) {
                        ReadingTest readingExchange = response.body();
                    }

                    @Override
                    public void onFailure(Call<ReadingTest> call, Throwable t) {
                        Toast.makeText(TestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "Writing":
                break;
            case "Listening":
                Call<ListeningTest> call3 = service.loadListeningTest();
                call3.enqueue(new Callback<ListeningTest>() {
                    @Override
                    public void onResponse(Call<ListeningTest> call3, Response<ListeningTest> response3) {
                        ListeningTest listeningExchange = response3.body();
                    }

                    @Override
                    public void onFailure(Call<ListeningTest> call3, Throwable t3) {
                        Toast.makeText(TestActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "Vocabulary":
                break;
            case "Speaking":
                break;
        }
    }
}