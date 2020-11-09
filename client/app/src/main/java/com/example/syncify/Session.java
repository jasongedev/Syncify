package com.example.syncify;

import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Session {
    public static boolean isGuest;
    public static String accessToken;
    public static String refreshToken;
    public static int expiresIn;
    public static DatabaseReference user;
    private static final ScheduledExecutorService eService =
            new ScheduledThreadPoolExecutor(1);

    public static void autoUpdateToken() {
        // eService starts TokenRefresher thread to acquire a new access token every 55 minutes.
    }
}
