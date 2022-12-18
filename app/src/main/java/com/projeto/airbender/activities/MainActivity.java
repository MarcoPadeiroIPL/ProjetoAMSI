package com.projeto.airbender.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.projeto.airbender.R;
import com.projeto.airbender.fragments.FlightFragment;
import com.projeto.airbender.fragments.HomeFragment;
import com.projeto.airbender.fragments.ProfileFragment;
import com.projeto.airbender.fragments.TicketFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFragment(new HomeFragment());
        bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navHome:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navFlights:
                    replaceFragment(new FlightFragment());
                    break;
                case R.id.navQRCode:
                    replaceFragment(new TicketFragment());
                    break;
                case R.id.navProfile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
        SharedPreferences sharedInfoUser = getSharedPreferences("user_data", MODE_PRIVATE);
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, newFragment);
        fragmentTransaction.commit();
    }

    public void onClickLogout(View view) {
        SharedPreferences sharedInfoUser = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedInfoUser.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
