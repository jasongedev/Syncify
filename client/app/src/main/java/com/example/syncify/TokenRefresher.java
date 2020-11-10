package com.example.syncify;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class TokenRefresher implements Runnable {

    @Override
    public void run() {
        // TODO: use refresh token to generate a new access token
        String credentials = Session.CLIENT_ID + ":" + Session.CLIENT_SECRET;

        try {
            URL url = new URL("https://accounts.spotify.com/api/token?grant_type=refresh_token&refresh_token=" + Session.refreshToken);
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
                Log.d("AccessResponse", "Getting...");
                Thread.sleep(500);
            }

            // TODO: make sure this is good
            if (response.equals("OK")) {
                JsonParser jParser = new JsonParser();
                JsonObject jObject = jParser.parse(br).getAsJsonObject();
                Session.accessToken = jObject.get("access_token").getAsString();
                Session.user.child("accessToken").setValue(Session.accessToken);
                Log.d("AccessResponse", "token: " + Session.accessToken);

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
