package com.example.mini_proect;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "UserDB";
        private static final String TABLE_USERS = "users";
        private static final String KEY_ID = "id";
        private static final String KEY_PHONE_NUMBER = "phone_number";
        private static final String KEY_PASSWORD = "password";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_PHONE_NUMBER + " TEXT,"
                    + KEY_PASSWORD + " TEXT" + ")";
            db.execSQL(CREATE_USERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }

        public void addUser(String phoneNumber, String password) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_PHONE_NUMBER, phoneNumber);
            values.put(KEY_PASSWORD, password);
            db.insert(TABLE_USERS, null, values);
            db.close();
        }

        public boolean checkUser(String phoneNumber, String password) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                    KEY_PHONE_NUMBER + " = ? AND " + KEY_PASSWORD + " = ?", new String[]{phoneNumber, password});
            boolean result = cursor.getCount() > 0;
            cursor.close();
            db.close();
            return result;
        }


}
