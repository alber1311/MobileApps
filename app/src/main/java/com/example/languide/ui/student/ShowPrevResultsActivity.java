package com.example.languide.ui.student;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.languide.R;
import com.example.languide.api.TestService;
import com.example.languide.tests.ShowTests;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowPrevResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prev_results);

        /*Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        TestService service = retrofit.create(TestService.class);

        Call<ShowTests> call = service.loadPrevResults();
        call.enqueue(new Callback<ShowTests>() {
            @Override
            public void onResponse(@NotNull Call<ShowTests> call, @NotNull Response<ShowTests> response) {

            }

            @Override
            public void onFailure(@NotNull Call<ShowTests> call, @NotNull Throwable t) {

            }
        });*/
    }
}