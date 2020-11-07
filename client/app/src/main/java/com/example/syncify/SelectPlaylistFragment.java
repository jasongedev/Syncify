package com.example.syncify;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SelectPlaylistFragment extends Fragment {

    String[] playlists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_select_playlist, container, false);

        // find listViewPlaylists by ID
        //generateList

        return fragment;
    }

    public String[] getPlaylists() {

        return null;
    }

    void selectPlaylists() {

    }
}
