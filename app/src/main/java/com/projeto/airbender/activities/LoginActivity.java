package com.projeto.airbender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.projeto.airbender.MainActivity;
import com.projeto.airbender.R;
import com.projeto.airbender.listeners.LoginListener;
import com.projeto.airbender.models.SingletonAirbender;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
    }

    /*public boolean isUsernameValid() {
        // TODO: Validar username com API
    }*/
    /*public boolean isPasswordValid() {
        // TODO: Validar password com API
    }*/

    public void onClickLogin(View view) {
        /*if(!isUsernameValid()){
            username.setError("Invalid username");
            return;
        }
        if(!isPasswordValid()){
            password.setError("Invalid password");
            return;
        }*/
        // chamar o LoginAPI do singleton
        SingletonAirbender.getInstance(getApplicationContext()).loginAPI(getApplicationContext(), username.getText().toString(), password.getText().toString());

    }

    @Override
    public void onAttemptLogin(String token) {
        // obs: Guardar no shared o token de login
       // Intent intent = new Intent(this, MainActivity.class);
       // startActivity(intent);
       // finish();
        //Toast.makeText(this, token, Toast.LENGTH_LONG).show();


    }
}
