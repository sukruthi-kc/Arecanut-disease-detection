// DatabaseHelper2.java
package com.example.mini_proect;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper2 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "image_predictions4.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "predictions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_PREDICTED_CLASS = "predicted_class";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_PREDICTED_CLASS + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertData(byte[] image, String predictedClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_PREDICTED_CLASS, predictedClass);
        long rowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return rowId;
    }
}
