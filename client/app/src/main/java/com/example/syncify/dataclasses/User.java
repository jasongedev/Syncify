package com.example.syncify.dataclasses;

public class User {
    public String name;
    public String accessToken;
    public boolean isPlaying;
    public long timestamp;
    public String playingState; // resume or pause
    public String songName;
    public String songArtist;
    public String songUri;
    public boolean getPlaylists;
    public boolean getUsers;
    public String userQuery;
    public String[] listeners;
    public Playlist[] playlists;
    public User[] searchedUsers;

    public User() {

    }
}