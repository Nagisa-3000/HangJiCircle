package com.example.myim.controller.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myim.R;
import com.example.myim.model.bean.GoodsInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.utils.Transform;

import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends ArrayAdapter<GoodsInfo> {

    private List<GoodsInfo> goods;
    public MarketAdapter(Context context, List<GoodsInfo> goods) {
        super(context, 0,goods);
        this.goods = goods;
    }
    @Override
    public int getCount() {
        return goods.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.market_item_layout, parent, false);
        }

        GoodsInfo goods = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.commodity_title);
        TextView contentTextView = convertView.findViewById(R.id.commodity_context);
        TextView authorTextView= convertView.findViewById(R.id.commodity_author);
        ImageView photoImageView=convertView.findViewById(R.id.commodity_photo);

        try {
            titleTextView.setText(goods.getGoods());
            authorTextView.setText(String.valueOf(goods.getUserId()));
            Bitmap bitmap = Transform.stringToBitmap(goods.getPhoto());
            photoImageView.setImageBitmap(bitmap);


        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    public void setGoodsList(List<GoodsInfo> goodsList) {
        this.goods=goodsList;
    }
}
