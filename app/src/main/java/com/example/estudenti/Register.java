package com.example.estudenti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class Register extends AppCompatActivity {

    EditText edtFullName, edtUsername, edtPassword, edtDOB, edtPhoneNumber;
    Button btnLoginReg, btnRegisterReg;
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new dbConnect(this);

        edtFullName = findViewById(R.id.edtFullNameReg);
        edtUsername = findViewById(R.id.edtUsernameReg);
        edtPassword = findViewById(R.id.edtPasswordReg);
        edtDOB = findViewById(R.id.edtDOBReg);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumberReg);

        btnLoginReg = findViewById(R.id.btnLoginReg);
        btnRegisterReg = findViewById(R.id.btnRegisterReg);

        btnLoginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnRegisterReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strFullName = edtFullName.getText().toString();
                String strUsername = edtUsername.getText().toString();
                String strPassword = edtPassword.getText().toString();
                String strDOB = edtDOB.getText().toString();
                String strPhonenumber = edtPhoneNumber.getText().toString();

                if(strFullName.isEmpty() || strUsername.isEmpty() || strPassword.isEmpty() || strDOB.isEmpty() || strPhonenumber.isEmpty()){
                    Toast.makeText(Register.this, "All fields required!", Toast.LENGTH_SHORT).show();
                } else {
                    Users user = new Users(0, strFullName, strUsername, strPassword, strDOB, strPhonenumber);
                    db.addUser(user);
                    Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Register.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
