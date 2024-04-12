package com.example.tam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity {
    private EditText fullname, email, pass1, pass2;
    Button llo;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button register = findViewById(R.id.button7);
        Button login1 = findViewById(R.id.button8);
        fullname = findViewById(R.id.editTextText);

        email = findViewById(R.id.editTextText2);

        pass1 = findViewById(R.id.editTextText3);
        pass2 = findViewById(R.id.editTextText4);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSignUp.this, Login.class));
            }
        });
    }

    private void registerUser() {
        final String emailText = email.getText().toString().trim();
        final String name = fullname.getText().toString().trim();
        String passwordText1 = pass1.getText().toString().trim();
        String passwordText2 = pass2.getText().toString().trim();
        if (!passwordText1.equals(passwordText2)) {
            Toast.makeText(UserSignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success
                            // Save user details to Firebase Realtime Database
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            User user = new User(name, emailText);
                            databaseReference.child(userId).setValue(user);

                            // For simplicity, let's just finish the registration activity
                            finish();

                            // Start the login activity after registration
                            Intent intent = new Intent(UserSignUp.this, UserLogin.class);
                            startActivity(intent);
                        } else {
                            // Registration failed
                            Toast.makeText(UserSignUp.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}