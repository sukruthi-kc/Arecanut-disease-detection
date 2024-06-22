package com.example.mini_proect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup_tab);

        // Find the "ADMIN LOGIN" button and set OnClickListener
        Button adminLoginButton = findViewById(R.id.admin_login_button);
        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AdminActivity when the button is clicked
                Intent intent = new Intent(admin.this, AdminLogin.class);
                startActivity(intent);
            }
        });
    }
}
