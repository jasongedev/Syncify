package com.example.syncify;

import java.util.ArrayList;

public class User {
    public String key;
    public boolean isGuest;
    public String name = "";
    public String accessToken;
    public boolean isPlaying;
    public long timestamp;
    public String playingState = "none"; // resume or pause
    public String songName = "none";
    public String songArtist = "none";
    public String songUri = "none";
    public boolean getPlaylists;
    public boolean getUsers;
    public String userQuery = "none";
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