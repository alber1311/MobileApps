package com.example.languide.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.languide.R;
import com.example.languide.ui.student.StudentMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button register;
    Spinner role;


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.idName);
        email = findViewById(R.id.idEmail);
        password = findViewById(R.id.idPass);
        confirmPassword = findViewById(R.id.idConfPass);
        register = findViewById(R.id.register);
        role = findViewById(R.id.idRole);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(v -> {
            if(checkData()){
                registerUser(email.getText().toString(), password.getText().toString());
            }
        });
    }

    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        String nameString = name.getText().toString();
                        String roleString = role.getSelectedItem().toString();
                        String userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();

                        user.put("Email", email);
                        user.put("Name", nameString);
                        user.put("Role", roleString);

                        documentReference.set(user);

                        startActivity(new Intent(RegisterActivity.this, StudentMainActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Check if the data input is correct
    public boolean checkData(){
        if (isEmpty(name)) {
            Toast t = Toast.makeText(this, "You must enter first name to register!", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        if (!isEmail(email)) {
            email.setError("Enter a valid email!");
            return false;
        }
        if (!isPasswordValid(password.getText().toString())) {
            password.setError("Password must have at least 6 characters");
            return false;
        }
        if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Passwords must be the same!");
            return false;
        }
        return true;
    }

    //Checks if an EditText is empty
    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    //Checks if email is valid
    private boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}