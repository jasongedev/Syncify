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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SelectPlaylistActivity extends AppCompatActivity {

    ListView mListView;
    List<Playlist> list = new ArrayList<>();
    Playlist[] playlists;
    ChildEventListener listener;
    final long waitTime = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        Session.user.child("getPlaylists").setValue(true);
        listenToPlaylistField();
        createPlaylistArray();

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
    }

    public void listenToPlaylistField() {
        listener = new ChildEventListener() {
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
    }

    public void createPlaylistArray() {
        TimerTask removeListener = new TimerTask() {
            @Override
            public void run() {
                Session.user.child("playlists").removeEventListener(listener);
                playlists = convertListToArray(list);
            }
        };

        Timer timer = new Timer();
        timer.schedule(removeListener, waitTime);
    }

    private Playlist[] convertListToArray(List<Playlist> pList) {
        Playlist[] arr = new Playlist[pList.size()];

        for (int i = 0; i < pList.size(); i++) {
            arr[i] = pList.get(i);
        }

        return arr;
    }

    void generateList(Playlist[] playlists) {

    }
}
