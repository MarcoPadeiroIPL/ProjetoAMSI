package com.projeto.airbender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.projeto.airbender.R;
import com.projeto.airbender.listeners.LoginListener;
import com.projeto.airbender.models.SingletonAirbender;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        SingletonAirbender.getInstance(getApplicationContext()).setLoginListener(this);
    }

    /*public boolean isUsernameValid() {
        // TODO: Validar username com API
    }*/
    /*public boolean isPasswordValid() {
        // TODO: Validar password com API
    }*/

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

    @Override
    public void onAttemptLogin(String token, int id, String role) {
        // obs: Guardar no shared o token de login
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("token",token);
        intent.putExtra("id",id);
        intent.putExtra("role",role);
        startActivity(intent);
        finish();

    }
}
