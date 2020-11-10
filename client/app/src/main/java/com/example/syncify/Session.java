package com.example.syncify;

import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Session {
    public static String key;
    public static boolean isGuest;
    public static String accessToken;
    public static String refreshToken;
    public static int expiresIn;
    public static DatabaseReference user;

    public static String CLIENT_ID;
    public static String CLIENT_SECRET;
    public static String REDIRECT_URI;
    private static final ScheduledExecutorService eService =
            new ScheduledThreadPoolExecutor(1);

    public static void autoUpdateToken() {
        eService.scheduleAtFixedRate(new TokenRefresher(), 55, 55, TimeUnit.MINUTES);
    }
}
