package com.example.tam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity {

    private EditText fullname, email, pass1, pass2;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        // Initialize views
        fullname = findViewById(R.id.editTextText);
        email = findViewById(R.id.editTextText2);
        pass1 = findViewById(R.id.editTextText3);
        pass2 = findViewById(R.id.editTextText4);

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize buttons
        Button registerButton = findViewById(R.id.button7);
        Button loginButton = findViewById(R.id.button8);

        // Set click listener for register button
        registerButton.setOnClickListener(v -> registerUser());

        // Set click listener for login button
        loginButton.setOnClickListener(v -> startActivity(new Intent(UserSignUp.this, UserLogin.class)));
    }

    private void registerUser() {
        // Retrieve user inputs
        String fullNameText = fullname.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passwordText1 = pass1.getText().toString().trim();
        String passwordText2 = pass2.getText().toString().trim();

        // Check if passwords match
        if (!passwordText1.equals(passwordText2)) {
            Toast.makeText(UserSignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password complexity
        if (!isValidPassword(passwordText1)) {
            Toast.makeText(UserSignUp.this, "Password must be at least 8 characters with at least one uppercase letter and one digit", Toast.LENGTH_LONG).show();
            return;
        }

        // Perform user registration with Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText1)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Registration success
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            User user = new User(fullNameText, emailText);
                            databaseReference.child(userId).setValue(user);

                            // Finish the registration activity
                            finish();

                            // Start the login activity after registration
                            startActivity(new Intent(UserSignUp.this, UserLogin.class));
                        } else {
                            // Registration failed
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(UserSignUp.this, "Invalid email or password format", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserSignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private boolean isValidPassword(String password) {
        // Password must be at least 8 characters with at least one uppercase letter and one digit
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(passwordPattern);
    }
}
