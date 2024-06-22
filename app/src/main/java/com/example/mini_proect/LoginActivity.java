package com.example.mini_proect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText signUpPhoneNumber, signUpPassword;
    Button signUpButton;
    TextView loginTextView; // TextView for "Already have an account?"
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        signUpPhoneNumber = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.signup_button);
        loginTextView = findViewById(R.id.already_have_account_textview); // Initialize loginTextView
        db = new DatabaseHelper(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = signUpPhoneNumber.getText().toString();
                String password = signUpPassword.getText().toString();

                // Validate fields
                if (phoneNumber.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!isNumeric(phoneNumber) || phoneNumber.length() != 10) {
                    Toast.makeText(LoginActivity.this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
                } else {
                    // Attempt registration
                    db.addUser(phoneNumber, password);
                    Toast.makeText(LoginActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        // Set onClickListener for loginTextView
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLoginActivity();
            }
        });
    }

    // Method to check if a string consists only of numeric characters
    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    // Method to navigate to LoginActivity
    private void navigateToLoginActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
