package com.example.syncify;


import org.junit.Test;

import java.lang.reflect.*;

import static org.junit.Assert.*;

public class SelectPlaylistFragmentTest {

    @Test
    public void getNonNullArrayOfPlaylists() {
        SelectPlaylistFragment frag = new SelectPlaylistFragment();
        assertNotNull(frag.getPlaylists());
    }

    @Test
    public void missingPlaylistData() {
        SelectPlaylistFragment frag = new SelectPlaylistFragment();
        String[] playlists = frag.getPlaylists();
        for (String s : playlists) {
            assertNotNull(s);
        }
    }
}