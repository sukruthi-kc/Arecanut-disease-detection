package com.example.mini_proect;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ModelActivity extends AppCompatActivity {

    private Interpreter interpreter;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_PICK_IMAGE = 2;
    private final int INPUT_IMAGE_SIZE_WIDTH = 224;
    private final int INPUT_IMAGE_SIZE_HEIGHT = 224;
    private final int NUM_CLASSES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model);

        // Load the TensorFlow Lite model
        try {
            interpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get references to UI components
        ImageView imageView = findViewById(R.id.imageView);
        Button scanButton = findViewById(R.id.scanButton);
        Button pickButton = findViewById(R.id.pickButton);

        // Set up onClick listener for scan button
        scanButton.setOnClickListener(v -> dispatchTakePictureIntent());

        // Set up onClick listener for pick button
        pickButton.setOnClickListener(v -> dispatchPickImageIntent());
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getResources().openRawResourceFd(R.raw.combined_model);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    imageBitmap = (Bitmap) extras.get("data");
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                if (data != null && data.getData() != null) {
                    try {
                        imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (imageBitmap != null) {
                // Perform inference on the selected image
                String result = classifyImage(imageBitmap);

                // Store image, predicted class, and phone number in database
                storeDataInDatabase(imageBitmap, result);

                // Show image
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(imageBitmap);

                // Update predicted class TextView
                TextView predictedClassTextView = findViewById(R.id.predictedClassTextView);
                predictedClassTextView.setText("Predicted Class: " + result);
            }
        }
    }

    private String classifyImage(Bitmap bitmap) {
        // Preprocess the input image (if needed)
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_IMAGE_SIZE_WIDTH, INPUT_IMAGE_SIZE_HEIGHT, true);

        // Perform inference
        ByteBuffer inputBuffer = convertBitmapToByteBuffer(resizedBitmap);
        ByteBuffer outputBuffer = ByteBuffer.allocateDirect(NUM_CLASSES * Float.SIZE / Byte.SIZE);

        if (interpreter != null) {
            interpreter.run(inputBuffer, outputBuffer);
        } else {
            // TensorFlow Lite interpreter is not initialized
            return "Error: TensorFlow Lite interpreter is not initialized";
        }

        // Postprocess the output
        // Extract the class probabilities from the output tensor
        float[] outputArray = new float[NUM_CLASSES];
        outputBuffer.rewind();
        outputBuffer.asFloatBuffer().get(outputArray);

        // Determine the class with the highest probability
        int predictedClassIndex = argmax(outputArray);
        String predictedClassLabel;
        switch (predictedClassIndex) {
            case 0:
                predictedClassLabel = "Not Arecanut";
                break;
            case 1:
                predictedClassLabel = "Healthy Leaf";
                break;
            case 2:
                predictedClassLabel = "Yellow Leaf Disease";
                break;
            default:
                predictedClassLabel = "Unknown";
                break;
        }

        return predictedClassLabel;
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        int bufferSize = 4 * bitmap.getHeight() * bitmap.getWidth() * 3; // 4 bytes per pixel for RGBA
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferSize);
        byteBuffer.order(java.nio.ByteOrder.nativeOrder());
        int[] intValues = new int[INPUT_IMAGE_SIZE_WIDTH * INPUT_IMAGE_SIZE_HEIGHT];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < INPUT_IMAGE_SIZE_WIDTH; ++i) {
            for (int j = 0; j < INPUT_IMAGE_SIZE_HEIGHT; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat(((val >> 16) & 0xFF) / 255.0f); // Red component
                byteBuffer.putFloat(((val >> 8) & 0xFF) / 255.0f);  // Green component
                byteBuffer.putFloat((val & 0xFF) / 255.0f);   // Blue component
            }
        }
        return byteBuffer;
    }

    private int argmax(float[] array) {
        int best = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[best]) {
                best = i;
            }
        }
        return best;
    }

    private void storeDataInDatabase(Bitmap imageBitmap, String predictedClass) {
        // Convert Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Store image and predicted class in the database
        DatabaseHelper2 dbHelper = new DatabaseHelper2(this);
        long rowId = dbHelper.insertData(byteArray, predictedClass);
        if (rowId != -1) {
            // Data inserted successfully
            // Handle success as needed
        } else {
            // Failed to insert data
            // Handle failure as needed
        }
    }
}
