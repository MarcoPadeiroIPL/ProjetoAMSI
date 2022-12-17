package com.projeto.airbender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.projeto.airbender.R;
import com.projeto.airbender.models.SingletonAirbender;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView teste1, teste2, teste3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        teste1 = findViewById(R.id.teste1);
        teste2 = findViewById(R.id.teste2);
        teste3 = findViewById(R.id.teste3);
        Bundle extras = getIntent().getExtras();

        teste1.setText(extras.getInt("id") + "");
        teste2.setText(extras.getString("token"));
        teste3.setText(extras.getString("role"));
    }
}