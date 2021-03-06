package com.example.syncify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class UserAdapter extends ArrayAdapter<User> {

    boolean canListen;

    public UserAdapter(Context context, User[] users, boolean canListen) {
        super(context, 0, users);
        this.canListen = canListen;
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

        TextView username = convertView.findViewById(R.id.user_name);
        ImageView profilePic = convertView.findViewById(R.id.avatar_pic);
        ImageView hosting = convertView.findViewById(R.id.user_isActive);

        username.setText(user.name);

        if (user.isHosting) {
            hosting.setImageResource(R.drawable.syncify_green);
            profilePic.setImageResource((R.drawable.syncify_green));
        } else {
            hosting.setImageResource(R.drawable.syncify_grey);
            profilePic.setImageResource((R.drawable.syncify_grey));
        }

        convertView.setOnClickListener(view -> {
            if (Session.isGuest) {
                Toast toast = Toast.makeText(getContext(), "Can't join a room as a guest", Toast.LENGTH_SHORT);
                toast.show();
            } else if (!user.isHosting) {
                Toast toast = Toast.makeText(getContext(), user.name + " is not hosting", Toast.LENGTH_SHORT);
                toast.show();
            } else if (!canListen) {
                Toast toast = Toast.makeText(getContext(), "Can't join a room as a free user", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Intent listenerIntent = new Intent(getContext(), TransitionActivity.class);
                listenerIntent.putExtra("Host?", false);
                listenerIntent.putExtra("HostKey", user.key);
                listenerIntent.putExtra("HostName", user.name);
                getContext().startActivity(listenerIntent);
            }
        });

        return convertView;
    }
}
