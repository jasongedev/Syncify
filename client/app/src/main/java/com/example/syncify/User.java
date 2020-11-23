package com.example.syncify;

import java.util.ArrayList;
import java.util.Map;

public class User {
    public String key;
    public boolean isGuest;
    public String name;
    public String profilePic;
    public String accessToken;
    public boolean isHosting;
    public boolean isPlaying; // resume or pause
    public long timestamp;
    public String songName;
    public String songArtist;
    public String songUri;
    public boolean getPlaylists;
    public boolean isGetUsers;
    public String searchQuery;
    public Map<String, Boolean> listeners;
    public ArrayList<Playlist> playlists;
    public ArrayList<String> searchedUsers;
    public String prevListeningTo;
    public String listeningTo;
    public Long numOfListeners;

    public User() {
        isGuest = true;
    }

    public User(String accessToken) {
        this.accessToken = accessToken;
        isGuest = false;
    }
}