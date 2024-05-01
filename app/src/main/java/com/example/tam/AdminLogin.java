package com.example.tam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity {

    EditText username, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        username = findViewById(R.id.editTextText6);
        password = findViewById(R.id.editTextText7);
        login = findViewById(R.id.button11);

        login.setOnClickListener(v -> {
            String usern = username.getText().toString();
            String passw = password.getText().toString();

            if (usern.equals("ADMIN") && passw.equals("Futacpe1")) {
                startActivity(new Intent(AdminLogin.this, AdminHome.class));
            } else {
                Toast.makeText(AdminLogin.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
