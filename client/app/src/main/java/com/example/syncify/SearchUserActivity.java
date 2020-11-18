package com.example.syncify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SearchUserActivity extends AppCompatActivity {
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

    User[] users = new User[0];

    ValueEventListener valueListener;
    DatabaseReference userQuery = Session.user.child("userQuery");
    DatabaseReference getUsers = Session.user.child("getUsers");
    DatabaseReference searchedUsers = Session.user.child("searchedUsers");

    Semaphore sem = new Semaphore(1);

    UserAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        adapter = new UserAdapter(this, users);
        listView = new ListView(this); // TODO: replace this with findviewbyid
        listView.setAdapter(adapter);

        setListener();


        /*ScheduledExecutorService exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(() -> {
            ArrayList<String> thekeys = new ArrayList<>();
            thekeys.add("-MME78KwRd5Leh67MVt7");
            thekeys.add("-MME7BwopQeMB38s7zs9");

            Session.user.child("searchedUsers").setValue(thekeys);
        }, 3, TimeUnit.SECONDS);

        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.scheduleAtFixedRate(() -> {
            User obj = (User) listView.getItemAtPosition(0);
            Log.d("Hello", "Item is: " + obj.name);
        }, 5, 3, TimeUnit.SECONDS);
        //String query = "hello there";*/


    }

    private void setListener() {
        valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) return;

                try {
                    sem.acquire();
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                    List<String> keyList = snapshot.getValue(t);
                    users = new User[keyList.size()];

                    // insert Users into User[] users
                    for (int i = 0; i < users.length; i++) {
                        final int idx = i;
                        usersRef.child(keyList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                users[idx] = snapshot.getValue(User.class);
                                Log.d("Hello", String.valueOf(users[idx].name));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    }

                    // update adapter and list view
                    adapter = new UserAdapter(getBaseContext(), users);
                    listView.setAdapter(adapter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    sem.release();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        searchedUsers.addValueEventListener(valueListener);
    }

    private void removeListener() {
        searchedUsers.removeEventListener(valueListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeListener();
    }

//    public String[] getResults(String searchID) // want to show users that are hosting a room at the current moment
//    {
//        searchInput = searchID;
//        mref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snapshot_each : snapshot.getChildren()){
//                    String name = snapshot_each.getValue(String.class);
//                    searchResults.insert(name);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return searchResults;
//    }

    /*public void generateList(User[] user_array)
    {

    }*/
}