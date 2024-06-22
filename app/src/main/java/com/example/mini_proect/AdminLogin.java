package com.example.mini_proect;




import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signupButton;

    private DatabaseHelper1 dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        dbHelper = new DatabaseHelper1(this);

        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        confirmPasswordEditText = findViewById(R.id.signup_confirm);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(AdminLogin.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(AdminLogin.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    if (isValidCredentials(email, password)) {
                        Intent intent = new Intent(AdminLogin.this, AdminViewActivity1.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AdminLogin.this, "Only admin can login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isValidCredentials(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DatabaseHelper1.COLUMN_EMAIL};
        String selection = DatabaseHelper1.COLUMN_EMAIL + " = ? AND " + DatabaseHelper1.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(DatabaseHelper1.TABLE_CREDENTIALS, projection, selection, selectionArgs, null, null, null);
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;

    }
}

