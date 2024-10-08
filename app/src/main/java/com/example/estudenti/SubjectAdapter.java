// SubjectAdapter.java
package com.example.estudenti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SubjectAdapter extends ArrayAdapter<Subject> {
    public SubjectAdapter(Context context, List<Subject> subjects) {
        super(context, 0, subjects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Subject subject = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_item, parent, false);
        }

        TextView subjectNameTextView = convertView.findViewById(R.id.subjectName);
        TextView subjectMarkTextView = convertView.findViewById(R.id.subjectMark);

        subjectNameTextView.setText(subject.getName());
        subjectMarkTextView.setText(String.valueOf(subject.getMark()));

        return convertView;
    }
}
