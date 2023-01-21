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

        replaceFragment(new LoginFragment(), false);

        SingletonAirbender.getInstance(getApplicationContext()).setLoginListener(this);
    }
    public void checkIfLoggedIn() {
        // save on shared prefenrences
        SharedPreferences sharedInfoUser = getSharedPreferences("user_data", MODE_PRIVATE);

        if (sharedInfoUser.contains("TOKEN")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void replaceFragment(Fragment newFragment, boolean keepBack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, newFragment);

        if (keepBack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void attemptLogin(String username, String password) {
        SingletonAirbender.getInstance(getApplicationContext()).loginAPI(getApplicationContext(), username, password);
    }

    @Override
    public void onLogin(Map<String, String> map) {
        // save on shared prefenrences
        SharedPreferences sharedInfoUser = getSharedPreferences("user_data", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedInfoUser.edit();
        editor.putString("ROLE", map.get("role"));
        editor.putString("TOKEN", map.get("token"));
        editor.putString("FNAME", map.get("fName"));
        editor.putString("SURNAME", map.get("surname"));
        editor.putString("PHONE", map.get("phone"));
        editor.putString("NIF", map.get("nif"));
        editor.putFloat("BALANCE", Float.parseFloat(map.get("balance")));
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

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
