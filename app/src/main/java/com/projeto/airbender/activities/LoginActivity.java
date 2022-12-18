package com.projeto.airbender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.projeto.airbender.R;
import com.projeto.airbender.listeners.LoginListener;
import com.projeto.airbender.models.SingletonAirbender;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfLoggedIn();
        setContentView(R.layout.activity_login);


        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        SingletonAirbender.getInstance(getApplicationContext()).setLoginListener(this);
    }
    public void checkIfLoggedIn() {
        // save on shared prefenrences
        SharedPreferences sharedInfoUser = getSharedPreferences("user_data", MODE_PRIVATE);

        if (sharedInfoUser.contains("TOKEN")) 
            redirectToMain();

        return;
    }

    public void onClickLogin(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(username.isEmpty()){
            etUsername.setError("Invalid username");
            return;
        }
            if(password.isEmpty() || password.length() < 8){
            etPassword.setError("Invalid password");
            return;
        }
        // chamar o LoginAPI do singleton
        SingletonAirbender.getInstance(getApplicationContext()).loginAPI(getApplicationContext(), username, password);

    }

    public void redirectToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onLogin(Map<String, String> map) {
        int id = map.get("id") != null ? Integer.parseInt(map.get("id")) : null;
        String role = map.get("role");
        String token = map.get("token");


        // save on shared prefenrences
        SharedPreferences sharedInfoUser = getSharedPreferences("user_data", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedInfoUser.edit();
        editor.putInt("ID", id);
        editor.putString("ROLE", role);
        editor.putString("TOKEN", token);
        editor.apply();


        redirectToMain();


    }
}
