package com.example.syncify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.String;
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

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        //editText = findViewById(R.id); // TODO: find the view
        adapter = new UserAdapter(this, users);
        listView = new ListView(this); // TODO: replace this with findviewbyid
        listView.setAdapter(adapter);

        setListener();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                userQuery.setValue(query);
                getUsers.setValue(true);
            }
        });

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
        }, 5, 3, TimeUnit.SECONDS);*/


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
}