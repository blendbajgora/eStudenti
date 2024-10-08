package com.example.estudenti;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

    private SwitchCompat switchCompat;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Find the SwitchCompat using the inflated view
        switchCompat = view.findViewById(R.id.switchCompat);
        Button btnLogout = view.findViewById(R.id.btnLogout); // Find the logout button


        // Get the saved night mode preference
        boolean isNightMode = sharedPreferences.getBoolean("nightMode", false);
        Toast.makeText(getActivity(), "Night Mode: " + isNightMode, Toast.LENGTH_SHORT).show();

        switchCompat.setChecked(isNightMode);
        AppCompatDelegate.setDefaultNightMode(isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Handle switch state changes
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("nightMode", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("nightMode", false);
            }
            editor.apply();
        });

        // Handle logout button click
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent); // Start the MainActivity
            getActivity().finish(); // Optionally finish the current activity
        });

        return view; // Return the inflated view
    }
}
