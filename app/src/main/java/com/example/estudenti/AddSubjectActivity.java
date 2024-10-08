package com.example.estudenti;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddSubjectActivity extends AppCompatActivity {

    private EditText etSubjectName;
    private Button btnSave;
    private dbConnect dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        etSubjectName = findViewById(R.id.et_subject_name);
        btnSave = findViewById(R.id.btn_save);
        dbHelper = new dbConnect(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = etSubjectName.getText().toString().trim();
                if (!subjectName.isEmpty()) {
                    saveSubject(subjectName);
                } else {
                    Toast.makeText(AddSubjectActivity.this, "Please enter a subject name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveSubject(String subjectName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject_name", subjectName); // Make sure to match your database schema
        db.insert("subjects", null, values); // Adjust the table name if necessary
        Toast.makeText(this, "Subject added", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity after saving
    }
}
