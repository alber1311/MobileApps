package com.example.languide.ui.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.languide.R;
import com.example.languide.database.DatabaseAccess;
import com.example.languide.ui.login.LoginActivity;

public class ProfileActivity  extends AppCompatActivity {

    TextView textName;
    TextView  textEmail;

    private String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        name = intent.getStringExtra("profile_name");
        email = intent.getStringExtra("profile_email");

        Button buttonTest = findViewById(R.id.buttonTest);
        Button buttonLogout = findViewById(R.id.idLogout);
        textName = findViewById(R.id.idNameProfile);
        textEmail = findViewById(R.id.idEmailProfile);

        textName.setText(name);
        textEmail.setText(email);

        buttonTest.setOnClickListener(v -> openStudentMain());
        buttonLogout.setOnClickListener(v -> logout());
    }

    public void openStudentMain(){
        Intent intent = new Intent(this,StudentMainActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("email",email);
        startActivity(intent);
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("EXIT");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
