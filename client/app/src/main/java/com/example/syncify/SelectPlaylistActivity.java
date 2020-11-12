package com.example.syncify;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SelectPlaylistActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Playlist> list = new ArrayList<>();
    Playlist[] playlists;
    boolean initialEvent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        Session.user.child("getPlaylists").setValue(true);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Playlist playlist = snapshot.getValue(Playlist.class);
                if (playlist != null) {
                    list.add(playlist);
                } else {
                    Log.d("Here", "NULL");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Session.user.child("playlists").addChildEventListener(listener);

        /*TimerTask task = new TimerTask() {
            @Override
            public void run() {
                List<Playlist> lists = new ArrayList<>();
                Playlist play1 = new Playlist();
                play1.name = "my name";
                play1.imageUrl = "my url";
                play1.uri = "my uri";

                Playlist play2 = new Playlist();
                play2.name = "the name";
                play2.imageUrl = "the url";
                play2.uri = "the uri";

                lists.add(play1);
                lists.add(play2);

                Session.user.child("playlists").setValue(lists);
            }
        };

        Timer myTimer = new Timer();
        myTimer.schedule(task, 100);*/


        TimerTask removeListener = new TimerTask() {
            @Override
            public void run() {
                Session.user.child("playlists").removeEventListener(listener);
                playlists = new Playlist[list.size()];

                for (int i = 0; i < list.size(); i++) {
                    playlists[i] = list.get(i);
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(removeListener, 3500);


    }

    public Playlist[] getPlaylists() {

        return null;
    }

    void generateList(Playlist[] playlists) {

    }
}
