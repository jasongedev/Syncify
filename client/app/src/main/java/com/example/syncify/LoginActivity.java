package com.example.syncify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class LoginActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1234;
    private String[] scopes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Session.CLIENT_ID = getResources().getString(R.string.clientId);
        Session.CLIENT_SECRET = getResources().getString(R.string.clientSecret);
        Session.REDIRECT_URI = getResources().getString(R.string.redirectUri);
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
                    Log.d("AuthResponse", "Code: " + authCode);

                    claimTokens(authCode);
                    break;
                // Auth flow returned an error
                case ERROR:
                    Log.e("AuthResponse", response.getError());
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
        if (!SpotifyAppRemote.isSpotifyInstalled(this)) {
            Toast toast = Toast.makeText(this, "Please Install Spotify", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        AuthenticationRequest request;
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(Session.CLIENT_ID, AuthenticationResponse.Type.CODE,
                        Session.REDIRECT_URI);

        builder.setScopes(scopes);
        request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    public void loginAsGuest(View view) {
        makeUserDBObject(true);
        Intent intent = new Intent(this, StarterPageActivity.class);
        startActivity(intent);
    }

    private void claimTokens(String authorizationCode) {
        Thread thread = new Thread(() -> {
            String credentials = Session.CLIENT_ID + ":" + Session.CLIENT_SECRET;

            try {
                URL url = new URL("https://accounts.spotify.com/api/token?grant_type=authorization_code&code=" + authorizationCode + "&redirect_uri=" + Session.REDIRECT_URI);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes())));
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                BufferedReader br;
                urlConnection.connect();

                if (100 <= urlConnection.getResponseCode() && urlConnection.getResponseCode() <= 399) {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                }

                String response = null;

                while (response == null) {
                    response = urlConnection.getResponseMessage();
                    Log.d("AccessResponse", "Waiting...");
                    Thread.sleep(500);
                }

                if (response.equals("OK")) {
                    JsonParser jParser = new JsonParser();
                    JsonObject jObject = jParser.parse(br).getAsJsonObject();
                    Session.accessToken = jObject.get("access_token").getAsString();
                    Session.refreshToken = jObject.get("refresh_token").getAsString();
                    Session.expiresIn = jObject.get("expires_in").getAsInt();
                    Session.autoUpdateToken();

                    makeUserDBObject(false);
                    Intent intent = new Intent(this, StarterPageActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    private void makeUserDBObject(boolean isGuest) {
        Session.isGuest = isGuest;
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = root.child("users");

        User thisUser;
        if (isGuest) {
            thisUser = new User();
        } else {
            thisUser = new User(Session.accessToken);
        }

        Session.user = users.push();
        Session.user.setValue(thisUser);
        Session.key = Session.user.getKey();
        Session.user.child("key").setValue(Session.user.getKey());
        Log.d("Key", Session.key);
    }
}
