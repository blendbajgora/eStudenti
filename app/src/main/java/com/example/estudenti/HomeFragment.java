package com.example.estudenti;

import android.app.AlertDialog;
import android.app.Activity; // Import Activity for the result
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ListView subjectsListView;
    private Button addSubjectButton;
    private SubjectAdapter subjectAdapter; // Change to SubjectAdapter
    private ArrayList<Subject> subjectsList; // Change to ArrayList<Subject>
    private dbConnect dbHelper;
    private static final int ADD_MARK_REQUEST = 1; // Define a request code

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        subjectsListView = view.findViewById(R.id.subjectsListView);
        addSubjectButton = view.findViewById(R.id.addSubjectButton);
        subjectsList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(getContext(), subjectsList); // Initialize SubjectAdapter
        subjectsListView.setAdapter(subjectAdapter);

        dbHelper = new dbConnect(getContext());
        loadSubjects();

        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a dialog to enter the subject name
                showAddSubjectDialog();
            }
        });

        subjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubjectName = subjectsList.get(position).getName(); // Get the subject name
                // Show an options dialog
                showSubjectOptionsDialog(selectedSubjectName);
            }
        });

        return view;
    }

    private void loadSubjects() {
        // Clear the current list to avoid duplication
        subjectsList.clear();

        // Get a readable database instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query the subjects table to get average marks
        Cursor cursor = db.rawQuery("SELECT s.subject_name, AVG(m.mark) as avg_mark FROM subjects s LEFT JOIN marks m ON s.subject_id = m.subject_id GROUP BY s.subject_id", null);

        double totalMarks = 0; // Variable to accumulate total marks
        int totalSubjects = 0; // Variable to count subjects with marks

        if (cursor.moveToFirst()) {
            int subjectNameIndex = cursor.getColumnIndex("subject_name");
            int avgMarkIndex = cursor.getColumnIndex("avg_mark");

            if (subjectNameIndex != -1 && avgMarkIndex != -1) { // Ensure both columns exist
                do {
                    String subjectName = cursor.getString(subjectNameIndex);
                    double avgMark = cursor.isNull(avgMarkIndex) ? 0 : cursor.getDouble(avgMarkIndex);
                    subjectsList.add(new Subject(subjectName, (int) avgMark)); // Store subject name and mark

                    // Accumulate total marks and count subjects
                    totalMarks += avgMark;
                    totalSubjects++;
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(getContext(), "Column 'subject_name' or 'avg_mark' does not exist.", Toast.LENGTH_SHORT).show();
            }
        }

        // Calculate overall average
        double overallAverage = totalSubjects > 0 ? totalMarks / totalSubjects : 0;

        // Add overall average mark to the list
        subjectsList.add(new Subject("Average Mark", (int) overallAverage)); // Create a new Subject for the average

        // Close the cursor
        cursor.close();

        // Notify the adapter of the updated data
        subjectAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MARK_REQUEST && resultCode == Activity.RESULT_OK) {
            // Reload the subjects to refresh the ListView
            loadSubjects();
        }
    }

    private void showAddSubjectDialog() {
        // Create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Subject");

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setHint("Enter subject name");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String subjectName = input.getText().toString().trim();

                if (!subjectName.isEmpty()) {
                    // Add the subject to the database
                    addSubjectToDatabase(subjectName);

                    // Refresh the subjects list
                    loadSubjects();
                } else {
                    Toast.makeText(getContext(), "Subject name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addSubjectToDatabase(String subjectName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject_name", subjectName); // Make sure the column name is correct
        db.insert(dbHelper.getDbTableSubjects(), null, values);
        Toast.makeText(getContext(), "Subject added", Toast.LENGTH_SHORT).show();
    }

    private void showSubjectOptionsDialog(final String subjectName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select an option");
        String[] options = {"Edit Mark", "Delete Subject"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Save Mark
                        // Start AddMarkActivity
                        Intent intent = new Intent(getContext(), AddMarkActivity.class);
                        intent.putExtra("subject_name", subjectName);
                        startActivityForResult(intent, ADD_MARK_REQUEST);
                        break;
                    case 1: // Delete Subject
                        deleteSubject(subjectName);
                        break;
                }
            }
        });

        builder.show();
    }

    private void deleteSubject(String subjectName) {
        // Get the subject ID based on the subject name
        int subjectId = dbHelper.getSubjectIdByName(subjectName);

        if (subjectId != -1) { // Subject found
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dbHelper.getDbTableSubjects(), "subject_id = ?", new String[]{String.valueOf(subjectId)});
            // Also delete marks associated with the subject
            db.delete(dbHelper.getDbTableMarks(), "subject_id = ?", new String[]{String.valueOf(subjectId)});
            Toast.makeText(getContext(), "Subject deleted", Toast.LENGTH_SHORT).show();

            // Refresh the subjects list
            loadSubjects();
        } else {
            Toast.makeText(getContext(), "Subject not found", Toast.LENGTH_SHORT).show();
        }
    }






}
