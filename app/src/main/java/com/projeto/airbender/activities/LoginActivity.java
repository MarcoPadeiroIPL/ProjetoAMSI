package com.projeto.airbender.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.projeto.airbender.R;
import com.projeto.airbender.fragments.HomeFragment;
import com.projeto.airbender.fragments.LoginFragment;
import com.projeto.airbender.fragments.SettingsFragment;
import com.projeto.airbender.listeners.LoginListener;
import com.projeto.airbender.models.SingletonAirbender;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private EditText etUsername, etPassword;
    private FloatingActionButton fabSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkIfLoggedIn();
        defineServer();

        setContentView(R.layout.activity_login);

        replaceFragment(new LoginFragment());

        SingletonAirbender.getInstance(getApplicationContext()).setLoginListener(this);
    }
    public void checkIfLoggedIn() {
        // save on shared prefenrences
        SharedPreferences sharedInfoUser = getSharedPreferences("user_data", MODE_PRIVATE);

        if (sharedInfoUser.contains("TOKEN")) 
            redirectToMain();

        return;
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, newFragment);
        fragmentTransaction.commit();
    }


    public void redirectToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onLogin(Map<String, String> map) {
        String role = map.get("role");
        String token = map.get("token");


        // save on shared prefenrences
        SharedPreferences sharedInfoUser = getSharedPreferences("user_data", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedInfoUser.edit();
        editor.putString("ROLE", role);
        editor.putString("TOKEN", token);
        editor.apply();


        redirectToMain();


    }
    public void defineServer(){
        SharedPreferences sharedInfoUser = getSharedPreferences("settings", MODE_PRIVATE);
        if (!sharedInfoUser.contains("SERVER")) {
            SharedPreferences.Editor editor = sharedInfoUser.edit();
            // set default server to local server
            editor.putString("SERVER", "10.0.2.2");
            editor.apply();
        }
    }
}
