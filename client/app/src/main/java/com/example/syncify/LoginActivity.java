package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //String clientId = getResources().getString(R.string.clientSecret);
    }

    public void loginWithSpotify(View view) {

    }

    public void loginAsGuest(View view) {

    }

    private boolean authenticate() {

        return false;
    }
}
