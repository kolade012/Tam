package com.example.tam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UserLogin extends AppCompatActivity {

    private Button login, forgot, reg;
    private FirebaseAuth firebaseAuth;
    private EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        login = findViewById(R.id.button10);
        forgot = findViewById(R.id.button9);
        reg = findViewById(R.id.button11);
        user = findViewById(R.id.editTextText5);
        pass = findViewById(R.id.editTextTextPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> loginUser());

        forgot.setOnClickListener(v -> startActivity(new Intent(UserLogin.this, ForgotPassword.class)));

        reg.setOnClickListener(v -> startActivity(new Intent(UserLogin.this, UserSignUp.class)));
    }

    private void loginUser() {
        String username = user.getText().toString();
        String password = pass.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful, navigate to the main activity
                        Intent intent = new Intent(UserLogin.this, UserHome.class);
                        intent.putExtra("USER_NAME", username);
                        startActivity(intent);
                    } else {
                        // Handle login failure
                        Toast.makeText(UserLogin.this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle other failure scenarios (e.g., network issues)
                    Toast.makeText(UserLogin.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}


