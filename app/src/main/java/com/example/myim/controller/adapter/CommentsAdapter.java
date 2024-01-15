package com.example.myim.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myim.R;
import com.example.myim.model.bean.CommentInfo;

import java.util.ArrayList;

public class CommentsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CommentInfo> commentList;

    public CommentsAdapter(Context context, ArrayList<CommentInfo> commentList) {
        this.context = context;
        this.commentList = commentList;
    }
    public void setData(ArrayList<CommentInfo> comments){
        this.commentList=comments;
    }
    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_item_layout, null);
        }

        // 获取当前评论信息
        CommentInfo comment = commentList.get(position);

        // 在这里设置评论内容到视图元素
        TextView userIdTextView = convertView.findViewById(R.id.userIdTextView);
        TextView contextTextView = convertView.findViewById(R.id.contextTextView);

        // 设置评论内容
        userIdTextView.setText("User ID: " + comment.getUserId());
        contextTextView.setText(comment.getContext());

        return convertView;
    }
}
