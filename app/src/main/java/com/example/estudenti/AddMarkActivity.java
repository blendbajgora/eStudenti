package com.example.estudenti;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMarkActivity extends AppCompatActivity {

    private EditText etMark;
    private Button btnSave;
    private dbConnect dbHelper;
    private String subjectName;
    private int subjectId; // Add field for subject ID
    private Integer existingMark; // Field for existing mark, if editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mark);

        etMark = findViewById(R.id.et_mark);
        btnSave = findViewById(R.id.btn_save);
        dbHelper = new dbConnect(this);

        // Get the subject name and mark from the intent
        subjectName = getIntent().getStringExtra("subject_name"); // Get the subject name
        subjectId = dbHelper.getSubjectIdByName(subjectName); // Get the subject ID

        // Check if an existing mark is being passed for editing
        existingMark = getIntent().getIntExtra("existing_mark", -1); // Get the existing mark, default is -1

        // Populate the EditText with the existing mark if editing
        if (existingMark != -1) {
            etMark.setText(String.valueOf(existingMark));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String markString = etMark.getText().toString().trim();
                if (!markString.isEmpty()) {
                    int mark = Integer.parseInt(markString);
                    // Check if we are editing an existing mark
                    if (existingMark != -1) {
                        updateMark(subjectId, mark);
                    } else {
                        saveMark(subjectId, mark);
                    }
                } else {
                    Toast.makeText(AddMarkActivity.this, "Please enter a mark", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveMark(int subjectId, int mark) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject_id", subjectId); // Use the integer subject ID
        values.put("mark", mark);
        db.insert("marks", null, values); // Insert into marks table
        Toast.makeText(this, "Mark added", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK); // Set the result to notify HomeFragment
        finish(); // Close the activity after saving
    }

    private void updateMark(int subjectId, int mark) {
        // Update the mark in the database
        dbHelper.updateMark(subjectId, mark); // Ensure this method exists in dbConnect
        Toast.makeText(this, "Mark updated", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK); // Set the result to notify HomeFragment
        finish(); // Close the activity after updating
    }
}
