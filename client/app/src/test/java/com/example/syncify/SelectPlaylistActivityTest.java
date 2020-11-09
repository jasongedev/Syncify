package com.example.syncify;


import org.junit.Test;

import static org.junit.Assert.*;

public class SelectPlaylistActivityTest {

    @Test
    public void getNonNullArrayOfPlaylists() {
        SelectPlaylistActivity act = new SelectPlaylistActivity();
        assertNotNull(act.getPlaylists());
    }

    @Test
    public void missingPlaylistData() {
        SelectPlaylistActivity act = new SelectPlaylistActivity();
        Playlist[] playlists = act.getPlaylists();
        for (Playlist s : playlists) {
            assertNotNull(s);
        }
    }
}