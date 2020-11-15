package com.example.syncify;

import java.util.ArrayList;

public class User {
    public String key;
    public boolean isGuest;
    public String name;
    public String accessToken;
    public boolean isHosting;
    public boolean isPlaying; // resume or pause
    public long timestamp;
    public String songName;
    public String songArtist;
    public String songUri;
    public boolean getPlaylists;
    public boolean getUsers;
    public String userQuery;
    public ArrayList<String> listeners;
    public ArrayList<Playlist> playlists;
    public ArrayList<String> searchedUsers;

    public User() {
        isGuest = true;
    }

    public User(String accessToken) {
        this.accessToken = accessToken;
        isGuest = false;
    }
}