package com.example.languide.ui.student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.languide.R;
import com.example.languide.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity  extends AppCompatActivity {

    TextView  textEmail;
    TextView textName;
    TextView textRole;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button buttonTest = findViewById(R.id.buttonTest);
        Button buttonLogout = findViewById(R.id.idLogout);
        textEmail = findViewById(R.id.idEmailProfile);
        textName = findViewById(R.id.idNameProfile);
        textRole = findViewById(R.id.idRoleProfile);

        userID = mAuth.getCurrentUser().getUid();

        buttonTest.setOnClickListener(v -> openStudentMain());
        buttonLogout.setOnClickListener(v -> logout());

        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String email = "Email:   " + value.getString("Email");
                String name = "Name:   " + value.getString("Name");
                String role = "Role:   " + value.getString("Role");
                textEmail.setText(email);
                textName.setText(name);
                textRole.setText(role);
            }
        });
    }

    public void openStudentMain(){
        Intent intent = new Intent(this,StudentMainActivity.class);
        startActivity(intent);
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("EXIT");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            db.terminate();
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        });
        builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
