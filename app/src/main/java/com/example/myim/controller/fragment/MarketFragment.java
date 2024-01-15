package com.example.myim.controller.fragment;

import static com.example.myim.model.dao.GoodsInfoDao.getAllGoods;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myim.R;
import com.example.myim.controller.activity.GoodsActivity;
import com.example.myim.controller.activity.MarketEditor;
import com.example.myim.controller.activity.PostActivity;
import com.example.myim.controller.adapter.MarketAdapter;
import com.example.myim.controller.adapter.postAdapter;
import com.example.myim.model.bean.GoodsInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.dao.GoodsInfoDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment {

    public MarketFragment() {
        // Required empty public constructor
    }
    private List<GoodsInfo> goodsList;
    private FloatingActionButton btn_to_market_editor;
    private MarketAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        // 获取 ListView 实例
        ListView listView = view.findViewById(R.id.commodity_list_view);

        //初始化button
        btn_to_market_editor=view.findViewById(R.id.fab_add_product);
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout_market);
        //button监听
        btn_to_market_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), MarketEditor.class);
                startActivity(intent);
            }
        });

        //设置listView的监听时间
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击的项的数据（MarketAdapter的getItem方法返回Post对象）
                GoodsInfo clickedPost =(GoodsInfo) adapter.getItem(position);

                // 执行跳转到新页面的操作，这里使用Intent启动一个新的Activity
                Intent intent = new Intent(getActivity(), GoodsActivity.class);
                // 通过Intent传递数据，如果需要的话
                intent.putExtra("goodsId", clickedPost.getId());
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        goodsList=getAllGoods();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 更新 UI，创建适配器并设置给 ListView
                                adapter = new MarketAdapter(requireContext(), goodsList);
                                listView.setAdapter(adapter);

                                // 执行其他 UI 相关操作
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();


            }
        });
        // 创建并设置 MarketAdapter，暂时使用空的商品列表

        // 在新线程中进行数据库查询，获取商品数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                goodsList= GoodsInfoDao.getAllGoods();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        adapter=new MarketAdapter(requireContext(), goodsList);
                        listView.setAdapter(adapter);
                    }
                });
            }
        }).start();

        return view;
    }
}