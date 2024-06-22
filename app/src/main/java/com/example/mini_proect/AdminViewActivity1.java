package com.example.mini_proect;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminViewActivity1 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminViewAdapter adapter;
    private List<AdminViewItem> adminViewItemList;
    private DatabaseHelper2 dbHelper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminview1);

        // Initialize DatabaseHelper
        dbHelper2 = new DatabaseHelper2(this);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize data list and adapter
        adminViewItemList = new ArrayList<>();
        adapter = new AdminViewAdapter(this, adminViewItemList);
        recyclerView.setAdapter(adapter);

        // Fetch and display stored data
        displayStoredData();
    }

    private void displayStoredData() {
        // Open database for reading
        SQLiteDatabase db = dbHelper2.getReadableDatabase();

        // Define columns to be retrieved
        String[] projection = {DatabaseHelper2.COLUMN_IMAGE, DatabaseHelper2.COLUMN_PREDICTED_CLASS};

        // Query the database, ordering by ID in descending order to get the latest entry first
        Cursor cursor = db.query(DatabaseHelper2.TABLE_NAME, projection, null, null, null, null, DatabaseHelper2.COLUMN_ID + " DESC");

        // Check if cursor is valid and contains data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve image byte array and predicted class from the cursor
                @SuppressLint("Range") byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper2.COLUMN_IMAGE));
                @SuppressLint("Range") String predictedClass = cursor.getString(cursor.getColumnIndex(DatabaseHelper2.COLUMN_PREDICTED_CLASS));

                // Add data to the list
                adminViewItemList.add(new AdminViewItem(BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length), predictedClass));
            } while (cursor.moveToNext());

            // Notify adapter of data change
            adapter.notifyDataSetChanged();

            // Close the cursor and database
            cursor.close();
            db.close();
        } else {
            // Handle the case when no data is available
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }
}
