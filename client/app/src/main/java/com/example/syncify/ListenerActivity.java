package com.example.syncify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class ListenerActivity extends  MusicPlayerActivity {

    private SpotifyAppRemote mSpotifyAppRemote;
    private PlayerApi playerApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listener);
        connectAppRemote();
    }

    void connectAppRemote() {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(Session.CLIENT_ID)
                        .setRedirectUri(Session.REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        playerApi = mSpotifyAppRemote.getPlayerApi();
                        listenToURI();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Something went wrong when attempting to connect! Handle errors here
                        Log.e("HostActivity", throwable.getMessage(), throwable);
                        Toast toast = Toast.makeText(getApplicationContext(), "Could not connect", Toast.LENGTH_SHORT);
                        toast.show();
                        Session.user.child("isHosting").setValue(false);
                        ListenerActivity.super.onBackPressed();
                    }
                });
    }

    public void listenToURI(){
        Session.user.child("songUri").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null){
                    return;
                }
                playerApi.play(snapshot.getValue(String.class));
                playerApi.pause();
                setPlayingListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setPlayingListener(){
        Session.user.child("isPlaying").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class)){
                    playerApi.resume();
                }
                else {
                    playerApi.pause();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void exitRoom(){

    }

}