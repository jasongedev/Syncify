package com.example.syncify;


import org.junit.Test;

import static org.junit.Assert.*;

public class SelectPlaylistActivityTest {

    @Test
    public void getNonNullArrayOfPlaylists() {
        SelectPlaylistActivity frag = new SelectPlaylistActivity();
        assertNotNull(frag.getPlaylists());
    }

    @Test
    public void missingPlaylistData() {
        SelectPlaylistActivity frag = new SelectPlaylistActivity();
        String[] playlists = frag.getPlaylists();
        for (String s : playlists) {
            assertNotNull(s);
        }
    }
}