package com.example.mini_proect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view);

        // Receive the data from previous activity
        Intent intent = getIntent();
        if (intent != null) {
            String predictedClass = intent.getStringExtra("predictedClass");
            byte[] byteArray = intent.getByteArrayExtra("image");
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            // Do whatever you want with the received data
            updatePredictedClass(predictedClass);
            displayImage(imageBitmap);
        }
    }

    private void updatePredictedClass(String result) {
        TextView predictedClassTextView = findViewById(R.id.predictedClassTextView);
        predictedClassTextView.setText("Predicted class: " + result);
    }

    private void displayImage(Bitmap imageBitmap) {
        ImageView imageView = findViewById(R.id.imageView);
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
        } else {
            // Handle the case when the Bitmap is null
            // For example, display a placeholder image or show an error message
            imageView.setImageResource(R.drawable.img);
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }
}
