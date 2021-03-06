package com.example.syncify;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HostActivity extends MusicPlayerActivity {
    private SpotifyAppRemote mSpotifyAppRemote;
    private PlayerApi playerApi;
    private Subscription<PlayerState> mSub;
    private boolean isPaused;
    private ValueEventListener trackListener;
    private ScheduledExecutorService timeStampUpdater;
    private final DatabaseReference listeners = Session.user.child("listeners");
    private ImageButton playPauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        songInfoView = findViewById(R.id.songText);
        listenerCount = findViewById(R.id.listenerCount);
        trackStart = findViewById(R.id.trackStart);
        trackEnd = findViewById(R.id.trackEnd);
        progressBar = findViewById(R.id.trackProgress);
        progressBar.setMin(0);
        playPauseButton = findViewById(R.id.pauseButton);

        Session.user.child("isHosting").setValue(true);
        connectAppRemote();
        trackListenerNum();
        startSoundBarAnim();
    }

    void play(){
        String playlistURI = getIntent().getStringExtra("PlaylistUri");
        playerApi.play(playlistURI).setResultCallback(empty -> {
            isPaused = false;
            playerApi.seekTo(0);
            playerApi.toggleRepeat();
            broadCastPlay();});

        playPauseButton.setImageResource(R.drawable.pause_icon);
        timeStampUpdater = new ScheduledThreadPoolExecutor(1);
        timeStampUpdater.scheduleAtFixedRate(new TimeStampUpdate(), 1, 1, TimeUnit.SECONDS);
    }
    public void togglePause(View v){
        if (isPaused) {
            playerApi.resume();
            isPaused = false;
            playPauseButton.setImageResource(R.drawable.pause_icon);
            toggleSoundBarAnim(true);
        } else {
            playerApi.pause();
            isPaused = true;
            toggleSoundBarAnim(false);
            playPauseButton.setImageResource(R.drawable.play_button);
        }
    }
    public void skipNext(View v){
        playerApi.skipNext();
        toggleSoundBarAnim(true);
        playPauseButton.setImageResource(R.drawable.pause_icon);
    }
    public void skipPrev(View v){
        mSpotifyAppRemote.getUserApi().getCapabilities().setResultCallback(capabilities -> {
            if (capabilities.canPlayOnDemand) {
                playerApi.skipPrevious();
                toggleSoundBarAnim(true);
                playPauseButton.setImageResource(R.drawable.pause_icon);
                playerApi.getPlayerState().setResultCallback(playerState -> updateSongInfo(playerState.track));
            }
        });
    }
    public void closeRoom(View v){
        Session.user.child("isHosting").setValue(false);
        timeStampUpdater.shutdownNow();
        listeners.removeEventListener(trackListener);
        playerApi.pause();
        mSub.cancel();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        clearSongInfo();
        super.onBackPressed();
    }
    void broadCastPlay(){
        playerApi.getPlayerState()
                .setResultCallback(playerState -> updateSongInfo(playerState.track))
                .setErrorCallback(throwable -> Log.e("HostActivity", throwable.getMessage(), throwable));
        mSub = playerApi.subscribeToPlayerState().setEventCallback(playerState -> {
            if(playerState.track != null){
                updateSongInfo(playerState.track);
                trackEnd.setText(milliToTime(playerState.track.duration));
                progressBar.setMax((int) playerState.track.duration);
                progressBar.setProgress((int) playerState.playbackPosition, true);
            }
            Session.user.child("isPlaying").setValue(!playerState.isPaused);
        });
    }

    void updateSongInfo(Track track){
        String songInfo;
        if (track.artist.name == null) {
            songInfo = "Advertisement";
        } else {
            songInfo = track.name + " by " + track.artist.name;
        }
        songInfoView.setText(songInfo);
        Session.user.child("songName").setValue(track.name);
        Session.user.child("songArtist").setValue(track.artist.name);
        Session.user.child("songUri").setValue(track.uri);
    }

    void clearSongInfo(){
        Session.user.child("songName").setValue(null);
        Session.user.child("songArtist").setValue(null);
        Session.user.child("songUri").setValue(null);
        Session.user.child("isPlaying").setValue(false);
        Session.user.child("timestamp").setValue(0);
    }

    private void trackListenerNum() {
        trackListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) return;

                GenericTypeIndicator<Map<String, Boolean>> t = new GenericTypeIndicator<Map<String, Boolean>>() {};
                Map<String, Boolean> keyList = snapshot.getValue(t);
                if (keyList != null) {
                    listenerCount.setText(String.valueOf(keyList.size() - 1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        listeners.addValueEventListener(trackListener);
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
                        play();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Something went wrong when attempting to connect! Handle errors here
                        Log.e("HostActivity", throwable.getMessage(), throwable);
                        Toast toast = Toast.makeText(getApplicationContext(), "Could not connect", Toast.LENGTH_SHORT);
                        toast.show();
                        Session.user.child("isHosting").setValue(false);
                        HostActivity.super.onBackPressed();
                    }
                });
    }

    String milliToTime(long milli) {
        long mins = milli / 60000;
        long secs = (milli % 60000) / 1000;
        return String.format(Locale.US, "%2d:%02d", mins, secs);
    }

    private class TimeStampUpdate implements Runnable{
        @Override
        public void run() {
            playerApi.getPlayerState()
                    .setResultCallback(playerState -> {
                        progressBar.setProgress((int) playerState.playbackPosition);
                        trackStart.setText(milliToTime(playerState.playbackPosition));
                        Session.user.child("timestamp").setValue(playerState.playbackPosition);
                    })
                    .setErrorCallback(throwable -> Log.e("HostActivity", throwable.getMessage(), throwable));
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        closeRoom(new View(this));
    }
}