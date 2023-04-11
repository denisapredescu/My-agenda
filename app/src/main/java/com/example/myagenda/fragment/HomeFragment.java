package com.example.myagenda.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myagenda.activity.LoginActivity;
import com.example.myagenda.R;
import com.example.myagenda.activity.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class HomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    Button loginButton;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), gso);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        loginButton = view.findViewById(R.id.login_button);

        if(sharedPreferences.getBoolean("logged", false)) { // is logged
            loginButton.setText("LOGOUT");

        } else {
            loginButton.setText("LOGIN");
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (loginButton.getText().toString().equals("LOGIN")) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    loginButton.setText("LOGIN");
                    sharedPreferences.edit().putBoolean("logged", false).apply();
                    sharedPreferences.edit().putString("email", "").apply();

                    gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                        }
                    });

                    Intent intent = new Intent(getActivity(), MainActivity.class);   // need to go back to MainActivity (by default navigate to HomeFragment - this) to reset the email to none on nav_header_main
                    startActivity(intent);
                    getActivity().overridePendingTransition(0, 0);  // remove the animation to mime the fact that there is no transaction

                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}