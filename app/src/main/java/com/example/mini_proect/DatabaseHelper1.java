package com.example.mini_proect;




import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper1 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "credentials1.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_CREDENTIALS = "credentials1";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Create table query
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_CREDENTIALS + " (" +
            COLUMN_EMAIL + " TEXT PRIMARY KEY," +
            COLUMN_PASSWORD + " TEXT)";

    // Predefined credentials
    private static final String[] PREDEFINED_EMAILS = {"priya@gmail.com", "suku@gmail.com"};
    private static final String[] PREDEFINED_PASSWORDS = {"priya123", "suku123"};

    public DatabaseHelper1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        // Insert predefined credentials into the table
        for (int i = 0; i < PREDEFINED_EMAILS.length; i++) {
            String email = PREDEFINED_EMAILS[i];
            String password = PREDEFINED_PASSWORDS[i];
            db.execSQL("INSERT INTO " + TABLE_CREDENTIALS + " (" + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ") VALUES (?, ?)",
                    new String[]{email, password});
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table if exists and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIALS);
        onCreate(db);
    }
}



