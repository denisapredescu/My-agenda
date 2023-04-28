package com.example.myagenda.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myagenda.database.Operations.UserOperations.ExistsUserOperation;
import com.example.myagenda.R;
import com.example.myagenda.database.User;
import com.example.myagenda.database.Operations.UserOperations.UserOperationsInterface;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements UserOperationsInterface {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private final int REQUEST_CODE = 1000;
    ImageView googleButton;
    SharedPreferences sharedPreferences;
    EditText email;
    EditText password;
    Button submitButton;
    TextView goToRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleButton = findViewById(R.id.google_button);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submitButton = findViewById(R.id.login_button);
        goToRegister = findViewById(R.id.goToRegister);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean okPass = true;

                if (email.getText().toString().length() == 0) {
                    email.setError("Insert an e-mail address.");
                    okPass = false;
                }

                if (password.getText().toString().length() == 0) {
                    password.setError("Insert a password.");
                    okPass = false;
                }

                if (okPass) {
                    new ExistsUserOperation(LoginActivity.this).execute(email.getText().toString(), password.getText().toString());
                }
            }
        });

        clickOnGoToRegister();

    }

    protected void signInWithGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
               task.getResult(ApiException.class);
               GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount( this);
               if(acct != null){
                   String personEmail = acct.getEmail();
                   sharedPreferences.edit().putBoolean("logged", true).apply();
                   sharedPreferences.edit().putString("email", personEmail).apply();
                   MainActivity.loginModifications(personEmail);
               }
               finish();
               goToHomePage();
           } catch (Exception e) {
               Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
           }
        }
    }

    protected void goToHomePage() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    protected void clickOnGoToRegister() {
        SpannableString spannableString = new SpannableString(goToRegister.getText().toString());

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        };
        spannableString.setSpan(clickableSpan, 0, goToRegister.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        goToRegister.setText(spannableString);
        goToRegister.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void insertUserOperationFinished(String response) {

    }

    @Override
    public void updateUserOperationFinished(String response) {

    }

    @Override
    public void existsUserOperationFinished(Integer response) {

        if (response == 1) {
            sharedPreferences.edit().putBoolean("logged", true).apply();
            sharedPreferences.edit().putString("email", email.getText().toString()).apply();
            MainActivity.loginModifications(email.getText().toString());

            goToHomePage();
        } else {
            Toast.makeText(this, "User not found :( try again", Toast.LENGTH_LONG).show();
        }
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
