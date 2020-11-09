package com.example.syncify;

import java.util.ArrayList;

public class User {
    public String key;
    public String name = "none";
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
    public ArrayList<User> searchedUsers;

    public User(String accessToken) {
        this.accessToken = accessToken;
    }
}