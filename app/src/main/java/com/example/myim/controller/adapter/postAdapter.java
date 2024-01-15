package com.example.myim.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myim.R;
import com.example.myim.model.bean.postInfo;

import java.util.ArrayList;
import java.util.List;

public class postAdapter extends ArrayAdapter<postInfo> {

    private List<postInfo> posts;
    public postAdapter(Context context, List<postInfo> posts) {
        super(context, 0, posts);
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_item_layout, parent, false);
        }

        postInfo post = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView contentTextView = convertView.findViewById(R.id.contentTextView);
        TextView authorTextView= convertView.findViewById(R.id.post_author);
        titleTextView.setText(post.getTitle());
        contentTextView.setText(post.getContext());
        authorTextView.setText(post.getUserName());
        return convertView;
    }
}
