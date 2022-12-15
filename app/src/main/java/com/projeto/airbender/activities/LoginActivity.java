package com.projeto.airbender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.projeto.airbender.MainActivity;
import com.projeto.airbender.R;

public class LoginActivity extends AppCompatActivity {

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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
