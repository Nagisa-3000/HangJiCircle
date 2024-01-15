package com.example.myim.controller.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myim.R;
import com.example.myim.model.bean.GoodsInfo;
import com.example.myim.utils.Transform;

import java.util.ArrayList;

public class MyLoveAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GoodsInfo> goodsList;

    public MyLoveAdapter(Context context, ArrayList<GoodsInfo> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.mylove_item_layout, null);
            holder = new ViewHolder();
            holder.textViewName = view.findViewById(R.id.commodity_title);
            holder.textViewDescription = view.findViewById(R.id.commodity_context);
            holder.textViewAuthor=view.findViewById(R.id.commodity_author);
            holder.imagePhoto=view.findViewById(R.id.commodity_photo);
            // Add more TextViews or other UI elements as needed
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        GoodsInfo goodsInfo = goodsList.get(position);
        holder.textViewName.setText(goodsInfo.getGoods());
        holder.textViewDescription.setText(goodsInfo.getContext());
        try {
            Bitmap bitmap = Transform.stringToBitmap(goodsInfo.getPhoto());
            holder.imagePhoto.setImageBitmap(bitmap);


        }catch (Exception e){
            e.printStackTrace();
        }
        // Set other data to corresponding UI elements

        return view;
    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        TextView textViewAuthor;
        ImageView imagePhoto;
        // Add references to other UI elements as needed
    }
}
