package com.example.myagenda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myagenda.database.Operations.UserOperations.InsertUserOperation;
import com.example.myagenda.R;
import com.example.myagenda.database.User;
import com.example.myagenda.database.Operations.UserOperations.UserOperationsInterface;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity implements UserOperationsInterface {
    TextInputLayout email;
    TextInputLayout password;
    TextInputLayout confirmPassword;
    TextInputLayout phone;
    Button register_button;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String emailText = email.getEditText().getText().toString();
            String passwordText = password.getEditText().getText().toString();
            String confirmPasswordText = confirmPassword.getEditText().getText().toString();
            String phoneText = phone.getEditText().getText().toString();

            Boolean pass = true;
            if (!confirmPasswordText.isEmpty()) {
                if (notTheSame(passwordText, confirmPasswordText)){
                    confirmPassword.setError("Not the same password");
                    pass = false;
                } else {
                    confirmPassword.setHelperText("Ok");
                }
            } else {
                confirmPassword.setHelperText("*Required");
            }
            if (!emailText.isEmpty()) {
                if (!isValidEmail(emailText)) {
                    email.setError("Invalid format for email");
                    pass = false;
                } else {
                    email.setHelperText("Ok");
                }
            } else {
                email.setHelperText("*Required");
            }

            if (!passwordText.isEmpty()) {
                if (!isValidPassword(passwordText)) {
                    password.setError("Must contain lowercase characters, uppercase characters, numbers, and special characters");
                    pass = false;
                } else {
                    password.setHelperText("Ok");
                }
            } else {
                password.setHelperText("*Required");
            }

            if (!phoneText.isEmpty()) {
                if (phoneText.length() != 10) {
                    phone.setError("Must contain 10 numbers");
                    pass = false;
                } else {
                    phone.setHelperText("Ok");
                }
            } else {
                phone.setHelperText("*Required");
            }

            register_button.setEnabled(pass);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        phone = findViewById(R.id.phone);
        register_button = findViewById(R.id.register);

        email.getEditText().addTextChangedListener(textWatcher);
        password.getEditText().addTextChangedListener(textWatcher);
        confirmPassword.getEditText().addTextChangedListener(textWatcher);
        phone.getEditText().addTextChangedListener(textWatcher);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(
                  email.getEditText().getText().toString(),
                  password.getEditText().getText().toString(),
                  phone.getEditText().getText().toString()
                );

                new InsertUserOperation(RegisterActivity.this).execute(user);
            }
        });
    }

    public boolean notTheSame(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }

    public boolean isValidPassword(String password) {
        String passwordRegex = "^[a-zA-Z0-9@#$%^&!\\\\+=()*_/].{8,20}$";
        return password.matches(passwordRegex);
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }


    @Override
    public void insertUserOperationFinished(String response) {
        if (response.equals("success")) {
            Toast.makeText(this, "User inserted successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "User insert failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateUserOperationFinished(String response) {

    }

    @Override
    public void existsUserOperationFinished(Integer response) {

    }
    @Override
    public void getUserOperationFinished(User user) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
