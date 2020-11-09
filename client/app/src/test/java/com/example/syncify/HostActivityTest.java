package com.example.syncify;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HostActivityTest {

    @Test
    public void upDateField(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://syncify-bf9e2.firebaseio.com/").getReference();
        rootRef.child("users").child("-MLa9CT8ixbBZcCZFqFa").child("name").setValue("masatoisHere");

        DatabaseReference users = rootRef.child("users");
        DatabaseReference mattSong = users.child("-MLa9CT8ixbBZcCZFqFa");
        mattSong.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Assert.assertEquals(snapshot.child("name").getValue(String.class),"masatoisHere");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mattSong.removeValue();

    }
    public void testBroadcastPlay() {
        /*HostActivity host = new HostActivity();
        Method broadCastPlay = HostActivity.getMethod("broadCastPlay", String.class, String.class, String.class);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        User testUser = new User();
        testUser.songName = "blarg";
        testUser.songArtist = "Matthew";
        testUser.songURI = "random";
        testUser.playingState = "resume";
        testUser.timeStamp = 1;
        DatabaseReference users = rootRef.child("users");
        users.child("mattSong").setValue(testUser);

        try {
            broadCastPlay.invoke(host,"testName", "testArtist", "testURI");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        DatabaseReference mattSong = users.child("mattSong");
        mattSong.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Assert.assertEquals(snapshot.child("songName"),"testName");
                Assert.assertEquals(snapshot.child("songArtist"),"testArtist");
                Assert.assertEquals(snapshot.child("songURI"),"testURI");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mattSong.removeValue();*/

    }

    public void testBroadCastPause() {
        /*HostActivity host = new HostActivity();
        Method broadCastPause = HostActivity.getMethod("broadCastPause");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        User testUser = new User();
        testUser.songName = "blarg";
        testUser.songArtist = "Matthew";
        testUser.songURI = "random";
        testUser.playingState = "resume";
        testUser.timeStamp = 1;
        DatabaseReference users = rootRef.child("users");
        users.child("mattSong").setValue(testUser);

        try {
            broadCastPause.invoke(host);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        DatabaseReference mattSong = users.child("mattSong");
        mattSong.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Assert.assertEquals(snapshot.child("playingState"),"pause");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mattSong.removeValue();*/
    }

    public void testBroadCastResume() {
        /*HostActivity host = new HostActivity();
        Method broadCastResume = HostActivity.getMethod("broadCastResume");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        User testUser = new User();
        testUser.songName = "blarg";
        testUser.songArtist = "Matthew";
        testUser.songURI = "random";
        testUser.playingState = "pause";
        testUser.timeStamp = 1;
        DatabaseReference users = rootRef.child("users");
        users.child("mattSong").setValue(testUser);

        try {
            broadCastResume.invoke(host);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        DatabaseReference mattSong = users.child("mattSong");
        mattSong.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Assert.assertEquals(snapshot.child("playingState"),"resume");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mattSong.removeValue();*/
    }
}