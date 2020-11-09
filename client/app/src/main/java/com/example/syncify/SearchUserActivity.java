package com.example.syncify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.lang.String;
import com.google.firebase.database.*;

public class SearchUserActivity extends AppCompatActivity {
    String searchInput;
    String[] searchResults;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mref = ref.child("searchInput");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
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

    public void generateList(User[] user_array)
    {

    }
}