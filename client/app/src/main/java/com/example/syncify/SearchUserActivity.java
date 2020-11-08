package com.example.syncify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.lang.String;

public class SearchUserActivity extends AppCompatActivity {
    String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
    }
}