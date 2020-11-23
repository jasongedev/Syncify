package com.example.syncify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SearchUserActivity extends AppCompatActivity {
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

    User[] users = new User[0];
    ArrayList<User> usersList = new ArrayList<>();

    ValueEventListener valueListener;
    DatabaseReference searchQuery = Session.user.child("searchQuery");
    DatabaseReference isGetUsers = Session.user.child("isGetUsers");
    DatabaseReference searchedUsers = Session.user.child("searchedUsers");

    UserAdapter adapter;
    ListView listView;

    EditText editText;
    boolean prepSearch;
    long searchDelay = 1;
    ScheduledExecutorService searchExec = new ScheduledThreadPoolExecutor(1);
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        editText = findViewById(R.id.search_user_text);
        adapter = new UserAdapter(this, users);
        listView = findViewById(R.id.list_view);

        setListener();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!prepSearch) {
                    prepSearch = true;
                    searchExec.schedule(() -> {
                        String query = editable.toString();
                        searchQuery.setValue(query);
                        isGetUsers.setValue(true);
                        prepSearch = false;
                    }, searchDelay, TimeUnit.SECONDS);
                }
            }
        });
    }

    private void setListener() {
        valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) return;

                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                List<String> keyList = snapshot.getValue(t);

                for (int i = 0; i < keyList.size(); i++) {
                    usersRef.child(keyList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            usersList.add(snapshot.getValue(User.class));

                            adapter = new UserAdapter(mContext, usersList.toArray(users));
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
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