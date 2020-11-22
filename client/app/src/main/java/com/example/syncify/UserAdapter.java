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
import java.net.URL;

public class UserAdapter extends ArrayAdapter<User> {

    Bitmap[] mBitmaps;

    public UserAdapter(Context context, User[] users) {
        super(context, 0, users);
        mBitmaps = new Bitmap[users.length];
        for (int i = 0; i < users.length; i++) {
            final int idx = i;
            Thread thread = new Thread(() -> convertBitmap(users[idx].profilePic, mBitmaps, idx));
            thread.start();
        }
    }

    @Override
    @NotNull
    public View getView(int position, View convertView, @NotNull final ViewGroup parent) {
        final User user = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_user, parent, false);
        }

        // TODO: uncomment and fix once layout files are ready
        TextView username = convertView.findViewById(R.id.search_user_name);
        ImageView profilePic = convertView.findViewById(R.id.user_profile);
        ImageView hosting = convertView.findViewById(R.id.user_isActive);

        username.setText(user.name);

        if (user.isHosting) {
            hosting.setBackgroundResource(R.drawable.syncify_green);
        } else {
            hosting.setBackgroundResource(R.drawable.syncify_grey);
        }


        convertView.setOnClickListener(view -> {
            Intent listenerIntent = new Intent(getContext(), ListenerActivity.class);
            listenerIntent.putExtra("HostKey", user.key);
            listenerIntent.putExtra("HostName", user.name);
            getContext().startActivity(listenerIntent);
        });

        return convertView;
    }

    private void convertBitmap(String src, Bitmap[] bitmaps, int index) {
        Thread thread = new Thread(() -> {
            try {
                URL imageUrl = new URL(src);
                bitmaps[index] = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            } catch(IOException e) {
                // TODO: change this to default profile picture
                bitmaps[index] = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.syncify_grey);
            }
        });

        thread.start();
    }
}
