package com.example.estudenti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class dbConnect extends SQLiteOpenHelper {

    // Database name and version
    private static final String dbName = "eStudeniDb";
    private static final int dbVersion = 3; // Increment version for schema change

    // Users table
    private static final String dbTableUsers = "users";
    private static final String ID = "id";
    private static final String fullname = "fullname";
    private static final String username = "username";
    private static final String password = "password";
    private static final String dob = "dob";
    private static final String phoneNumber = "phonenumber";

    // Marks table
    private static final String dbTableMarks = "marks";
    private static final String subjectId = "subject_id";
    private static final String mark = "mark";

    // Subjects table
    private static final String dbTableSubjects = "subjects";
    private static final String subjectName = "subject_name";

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create the users table
        String queryUsers = "CREATE TABLE " + dbTableUsers + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + fullname + " TEXT, " + username + " TEXT, " + password + " TEXT, "
                + dob + " TEXT, " + phoneNumber + " TEXT)";

        // Create the marks table
        String queryMarks = "CREATE TABLE " + dbTableMarks + " (" + subjectId + " INTEGER, "
                + mark + " INTEGER)";

        // Create the subjects table
        String querySubjects = "CREATE TABLE " + dbTableSubjects + " (" + subjectId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + subjectName + " TEXT)";

        // Execute the SQL statements to create the tables
        sqLiteDatabase.execSQL(queryUsers);
        sqLiteDatabase.execSQL(queryMarks);
        sqLiteDatabase.execSQL(querySubjects);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTableUsers);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTableMarks);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbTableSubjects);
        onCreate(sqLiteDatabase);
    }

    // Method to add a user to the users table
    public void addUser(Users users) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(fullname, users.getFullname());
        values.put(username, users.getUsername());
        values.put(password, users.getPassword());
        values.put(dob, users.getDOB());
        values.put(phoneNumber, users.getPhoneNumber());
        db.insert(dbTableUsers, null, values);
    }

    // Method to add a mark to the marks table
    public void addMark(int subjectId, int mark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject_id", subjectId);
        values.put("mark", mark);
        db.insert(dbTableMarks, null, values);
    }

    // Getter for dbTableSubjects
    public String getDbTableSubjects() {
        return dbTableSubjects;
    }

    // Getter for dbTableMarks
    public String getDbTableMarks() {
        return dbTableMarks;
    }

    // Method to get subject ID based on subject name
    public int getSubjectIdByName(String subjectName) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Use the correct column name in the query
        Cursor cursor = db.rawQuery("SELECT " + subjectId + " FROM " + dbTableSubjects + " WHERE " + this.subjectName + " = ?", new String[]{subjectName});

        // Log the query to debug
        Log.d("dbConnect", "Querying subject ID for: " + subjectName);

        int id = -1; // Initialize to -1 to indicate not found

        if (cursor.moveToFirst()) {
            // Get the column index
            int columnIndex = cursor.getColumnIndex(subjectId);
            if (columnIndex >= 0) {
                id = cursor.getInt(columnIndex);
            } else {
                Log.e("dbConnect", "Column index for subject_id is -1. Column not found.");
            }
        }
        cursor.close(); // Make sure to close the cursor
        return id; // Return -1 if not found or the id if found
    }

    // New method to get a list of subject names
    public List<String> getSubjectNames() {
        List<String> subjectNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + subjectName + " FROM " + dbTableSubjects, null);

        if (cursor.moveToFirst()) {
            do {
                subjectNames.add(cursor.getString(cursor.getColumnIndex(subjectName)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return subjectNames;
    }

    // New method to get marks for a specific subject
    public int getMarkForSubject(String subjectName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT mark FROM " + dbTableMarks + " WHERE subject_id = (SELECT " + subjectId + " FROM " + dbTableSubjects + " WHERE " + subjectName + " = ?)", new String[]{subjectName});

        if (cursor.moveToFirst()) {
            int markValue = cursor.getInt(cursor.getColumnIndex(mark));
            cursor.close();
            return markValue;
        } else {
            cursor.close();
            return 0; // Return 0 if no mark found
        }
    }

    // Method to update a mark for a subject
    public void updateMark(int subjectId, int newMark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mark, newMark);

        // Update the mark for the specific subject ID
        db.update(dbTableMarks, values, subjectId + " = ?", new String[]{String.valueOf(subjectId)});
    }

}