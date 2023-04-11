package com.example.myagenda.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.myagenda.R;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myagenda.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    AppBarConfiguration mAppBarConfiguration;
    ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my_books, R.id.nav_finished, R.id.nav_find, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (sharedPreferences.getString("email", "").equals("")) {  // user not logged
            logoutModifications();
        } else {
            loginModifications(sharedPreferences.getString("email", ""));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static void loginModifications(String email) {
        View headerView = navigationView.getHeaderView(0);
        TextView emailNavbar = headerView.findViewById(R.id.emailNavbar);
        emailNavbar.setText(email);
        navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_finished).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_my_books).setVisible(true);
    }

    public static void logoutModifications() {
        View headerView = navigationView.getHeaderView(0);
        TextView emailNavbar = headerView.findViewById(R.id.emailNavbar);
        emailNavbar.setText("");
        navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_finished).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_my_books).setVisible(false);
    }
}