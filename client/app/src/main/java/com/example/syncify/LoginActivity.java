package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class LoginActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1234;
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String REDIRECT_URI;
    private String[] scopes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CLIENT_ID = getResources().getString(R.string.clientId);
        CLIENT_SECRET = getResources().getString(R.string.clientSecret);
        REDIRECT_URI = getResources().getString(R.string.redirectUri);
        scopes = new String[]{"app-remote-control", "user-read-private", "playlist-read-private"};
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            Toast toast;
            switch (response.getType()) {
                // Response was successful and contains authorization code
                case CODE:
                    String authCode = response.getCode();
                    Log.d("Response", "Code: " + authCode);
                    break;
                // Auth flow returned an error
                case ERROR:
                    Log.e("Response", response.getError());
                    toast = Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
                    toast = Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT);
                    toast.show();
            }
        }
    }

    public void loginWithSpotify(View view) {
        AuthenticationRequest request;
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.CODE,
                        REDIRECT_URI);

        builder.setScopes(scopes);
        request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    public void loginAsGuest(View view) {

    }

    private boolean authenticate() {

        return false;
    }
}
