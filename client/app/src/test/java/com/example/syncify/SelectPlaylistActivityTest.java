package com.example.syncify;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

public class SelectPlaylistActivityTest {

    @Test
    public void convertTest() {
        SelectPlaylistActivity act = new SelectPlaylistActivity();
        List<Playlist> list = new ArrayList<>();

        for (int  i = 0; i < 100; i++) {
            list.add(new Playlist());
        }

        try {
            Method convert = SelectPlaylistActivity.class.getDeclaredMethod("convertListToArray", List.class);
            convert.setAccessible(true);
            Playlist[] arr = (Playlist[]) convert.invoke(act, list);
            assertNotNull(arr);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}