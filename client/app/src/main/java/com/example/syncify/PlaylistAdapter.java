package com.example.syncify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    Bitmap[] mBitmaps;
    Playlist[] mPlaylists;
    String uriTag = "spotify:playlist:";

    public PlaylistAdapter(Context context, Playlist[] playlists) {
        super(context, 0, playlists);
        mBitmaps = new Bitmap[playlists.length];
        mPlaylists = playlists;
    }

    @Override
    @NotNull
    public View getView(int position, View convertView, @NotNull final ViewGroup parent) {
        final Playlist playlist = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_playlist, parent, false);
        }

        TextView name = convertView.findViewById(R.id.Playlist_name);
        ImageView image = convertView.findViewById(R.id.playlist_cover);
        ImageView background = convertView.findViewById(R.id.playlist_list_view);

        name.setText(playlist.name);
        image.setImageBitmap(mBitmaps[position]);
        background.setImageResource(R.drawable.base);

        convertView.setOnClickListener(view -> {
            Intent hostIntent = new Intent(getContext(), TransitionActivity.class);
            hostIntent.putExtra("Host?", true);
            hostIntent.putExtra("PlaylistUri", uriTag + playlist.id);
            hostIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(hostIntent);
        });

        return convertView;
    }

    public void createBitmaps() {
        for (int i = 0; i < mPlaylists.length; i++) {
            final int idx = i;
            getAndStoreBitmap(mPlaylists[idx].imageUrl, mBitmaps, idx);
//            Thread thread = new Thread(() -> {
//                getAndStoreBitmap(mPlaylists[idx].imageUrl, mBitmaps, idx);
//            });
//            thread.start();
        }
    }

    private void getAndStoreBitmap(String src, Bitmap[] bitmaps, int index) {
        try {
            URL url = new URL(src);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            bitmaps[index] = image;

           /*HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}