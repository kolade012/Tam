package com.example.tam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminLogin extends AppCompatActivity {
    EditText username, password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = findViewById(R.id.editTextText6);
        password = findViewById(R.id.editTextText7);
        login = findViewById(R.id.button11);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usern = username.getText().toString();
                String passw = password.getText().toString();

                if (usern.equals("ADMIN") && passw.equals("FUTA")){
                    startActivity(new Intent(AdminLogin.this,AdminHome.class));
                } else {
                    Toast.makeText(AdminLogin.this, "Wrong Username and password", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminLogin.this,AdminLogin.class));
                }
            }
        });
    }
}