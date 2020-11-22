package com.example.syncify;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SelectPlaylistActivity extends AppCompatActivity {

    ListView mListView;
    List<Playlist> list;
    Playlist[] playlists;
    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        mListView = findViewById(R.id.list_view);
        Session.user.child("getPlaylists").setValue(true);
        listenToPlaylistField();

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
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<Playlist>> t = new GenericTypeIndicator<List<Playlist>>() {};
                list = snapshot.getValue(t);
                createPlaylistArray();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Session.user.child("playlists").addValueEventListener(listener);
    }

    public void createPlaylistArray() {
        Session.user.child("playlists").removeEventListener(listener);
        playlists = convertListToArray(list);
        runOnUiThread(() -> populateListView(playlists));
    }

    private Playlist[] convertListToArray(List<Playlist> pList) {
        Playlist[] arr = new Playlist[pList.size()];

        for (int i = 0; i < pList.size(); i++) {
            arr[i] = pList.get(i);
        }

        return arr;
    }

    private void  populateListView(Playlist[] playlists) {
        PlaylistAdapter adapter = new PlaylistAdapter(this, playlists);
        mListView.setAdapter(adapter);
    }
}
