package com.example.mini_proect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class userlogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login_tab);

        // Find the "USER LOGIN" button and set OnClickListener
        Button userLoginButton = findViewById(R.id.user_login_button);
        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userlogin.this, MainActivity.class); // Change MainActivity to Login
                startActivity(intent);
            }
        });

        // Find the "ADMIN LOGIN" button and set OnClickListener
    }
}
