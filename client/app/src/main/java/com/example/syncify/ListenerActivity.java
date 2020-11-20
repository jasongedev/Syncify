package com.example.syncify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Empty;
import com.spotify.protocol.types.PlayerState;

public class ListenerActivity extends  MusicPlayerActivity {

    private SpotifyAppRemote mSpotifyAppRemote;
    private PlayerApi playerApi;
    private Subscription<PlayerState> mSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listener);
        connectAppRemote();
        startSoundBarAnim();

        String key = getIntent().getStringExtra("HostKey");
        Session.user.child("listeningTo").setValue(key);

        // Check if there is a valid person to listen to
        Session.user.child("listeningTo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class) == null){
                    exitRoom(new View(getApplicationContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Connect to API
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

    // Find the song and take appropriate actions
    public void listenToURI(){
        Session.user.child("songUri").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null){
                    return;
                }
                playerApi.play(snapshot.getValue(String.class)).setResultCallback(empty -> {
                    setTimestamp();
                    setPlayingListener();
                });

                playerApi.pause();

                playerApi.subscribeToPlayerState().setEventCallback(playerState -> {
                    songInfoView = findViewById(R.id.songText);
                    String trackInfo = playerState.track.name + " by " + playerState.track.artist.name;
                    songInfoView.setText(trackInfo);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Check if the current song is being played
    public void setPlayingListener(){
        Session.user.child("isPlaying").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class)){
                    playerApi.resume();
                    toggleSoundBarAnim(true);
                }
                else {
                    playerApi.pause();
                    toggleSoundBarAnim(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setTimestamp() {
        Session.user.child("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long temp = snapshot.getValue(Long.class);
                playerApi.seekTo(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void exitRoom(View v){
        playerApi.pause();
        mSub.cancel();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        super.onBackPressed();
    }

    @Override
    public void onBackPressed(){
        exitRoom(new View(this));
    }

}