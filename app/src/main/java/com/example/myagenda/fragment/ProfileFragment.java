package com.example.myagenda.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myagenda.database.Operations.UserOperations.GetUserOperation;
import com.example.myagenda.R;
import com.example.myagenda.database.Operations.UserOperations.UpdateUserOperation;
import com.example.myagenda.database.User;
import com.example.myagenda.database.Operations.UserOperations.UserOperationsInterface;
import com.example.myagenda.activity.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment implements UserOperationsInterface {

    SharedPreferences sharedPreferences;
    Button changePasswordButton;
    Button saveChangesButton;
    TextInputLayout  email;
    TextInputLayout phone;
    TextInputLayout oldPassword;
    TextInputLayout newPassword;
    TextInputLayout confirmNewPassword;

    Boolean changePassword = false;
    User loggedUser;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String oldPasswordText = oldPassword.getEditText().getText().toString();
            String newPasswordText = newPassword.getEditText().getText().toString();
            String confirmPasswordText = confirmNewPassword.getEditText().getText().toString();
            String phoneText = phone.getEditText().getText().toString();

            Boolean pass = false;

            if (!oldPasswordText.isEmpty()) {
                if (!oldPasswordText.equals(loggedUser.getPassword())) {
                    oldPassword.setError("This is not your password");
                    pass = false;
                } else {
                    oldPassword.setHelperText("Ok");
                }
            } else {
                oldPassword.setHelperText("*Required");
            }
            if (!confirmPasswordText.isEmpty()) {
                if (notTheSame(newPasswordText, confirmPasswordText)){
                    confirmNewPassword.setError("Not the same password");
                    pass = false;
                } else {
                    confirmNewPassword.setHelperText("Ok");
                }
            } else {
                confirmNewPassword.setHelperText("*Required");
            }

            if (!newPasswordText.isEmpty()) {
                if (!isValidPassword(newPasswordText)) {
                    newPassword.setError("Must contain lowercase characters, uppercase characters, numbers, and special characters");
                    pass = false;
                } else {
                    newPassword.setHelperText("Ok");
                }
            } else {
                newPassword.setHelperText("*Required");
            }

            if (!phoneText.isEmpty()) {
                if (phoneText.length() != 10) {
                    phone.setError("Must contain 10 numbers");
                    pass = false;
                } else {
                    phone.setHelperText("Ok");
                    pass = true;
                }
            } else {
                phone.setHelperText("*Required");
            }

            saveChangesButton.setEnabled(pass);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        new GetUserOperation(this).execute(sharedPreferences.getString("email", ""));

        changePasswordButton = view.findViewById(R.id.changePassword);
        saveChangesButton = view.findViewById(R.id.saveChangesButton);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        oldPassword = view.findViewById(R.id.oldPassword);
        newPassword = view.findViewById(R.id.newPassword);
        confirmNewPassword = view.findViewById(R.id.confirmPassword);

        oldPassword.setVisibility(View.INVISIBLE);
        newPassword.setVisibility(View.INVISIBLE);
        confirmNewPassword.setVisibility(View.INVISIBLE);

        email.getEditText().addTextChangedListener(textWatcher);
        phone.getEditText().addTextChangedListener(textWatcher);
        oldPassword.getEditText().addTextChangedListener(textWatcher);
        newPassword.getEditText().addTextChangedListener(textWatcher);
        confirmNewPassword.getEditText().addTextChangedListener(textWatcher);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordButton.setVisibility(View.INVISIBLE);
                oldPassword.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.VISIBLE);
                confirmNewPassword.setVisibility(View.VISIBLE);
                changePassword = true;
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedUser.setPhone(phone.getEditText().getText().toString());

                if (changePassword) {
                    loggedUser.setPassword(newPassword.getEditText().getText().toString());
                }

                new UpdateUserOperation(ProfileFragment.this).execute(loggedUser);
            }
        });
        return view;
    }

    public boolean notTheSame(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }

    public boolean isValidPassword(String password) {
        String passwordRegex = "^[a-zA-Z0-9@#$%^&!\\\\+=()*_/].{8,20}$";
        return password.matches(passwordRegex);
    }

    @Override
    public void insertUserOperationFinished(String response) {

    }

    @Override
    public void updateUserOperationFinished(String response) {
        if (response.equals("success")) {
            Toast.makeText(getActivity(), "User updated successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(getActivity(), "User update failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void existsUserOperationFinished(Integer response) {

    }

    @Override
    public void getUserOperationFinished(User user) {
        if (user != null) {
            loggedUser = user;

            email.getEditText().setText(loggedUser.getEmail());
            phone.getEditText().setText(loggedUser.getPhone());

        } else {
            email.getEditText().setText(sharedPreferences.getString("email", ""));
        }
    }
}
