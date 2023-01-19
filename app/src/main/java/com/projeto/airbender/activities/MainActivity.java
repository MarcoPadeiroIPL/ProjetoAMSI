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
import com.projeto.airbender.fragments.BalanceReqFragment;
import com.projeto.airbender.fragments.HomeFragment;
import com.projeto.airbender.fragments.ProfileFragment;
import com.projeto.airbender.fragments.TicketFragment;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            mqtt();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        replaceFragment(new HomeFragment());
        bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navHome:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navTickets:
                    replaceFragment(new TicketFragment());
                    break;
                case R.id.navBalanceReq:
                    replaceFragment(new BalanceReqFragment());
                    break;
                case R.id.navProfile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
        SharedPreferences sharedInfoUser = getSharedPreferences("user_data", MODE_PRIVATE);
    }

    public void mqtt() throws MqttException {
        String clientId = MqttClient.generateClientId();
        MqttClient client = new MqttClient("tcp://10.0.2.2:1883", clientId, null);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String messageBody = new String(message.getPayload());
                System.out.println("Message arrived: " + messageBody);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Delivery complete");
            }
        });

        client.connect();

        client.subscribe("config", 1);
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
