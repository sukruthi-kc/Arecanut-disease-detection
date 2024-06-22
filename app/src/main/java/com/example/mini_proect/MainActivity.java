package com.example.mini_proect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText loginPhoneNumber, loginPassword;
    Button loginButton;
    TextView signUpTextView; // TextView for "Don't have an account?"
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginPhoneNumber = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.dont_have_account_textview); // Initialize signUpTextView
        db = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = loginPhoneNumber.getText().toString();
                String password = loginPassword.getText().toString();

                // Validate fields
                if (phoneNumber.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Attempt login
                    if (db.checkUser(phoneNumber, password)) {
                        // Navigate to next activity upon successful login
                        Intent intent = new Intent(MainActivity.this, ModelActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Set onClickListener for signUpTextView
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignUpActivity();
            }
        });
    }

    // Method to navigate to SignUpActivity
    private void navigateToSignUpActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
