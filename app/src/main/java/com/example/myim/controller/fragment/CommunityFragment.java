package com.example.myim.controller.fragment;

import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myim.R;
import com.example.myim.controller.activity.EditorActivity;
import com.example.myim.controller.activity.InterestPost;
import com.example.myim.controller.activity.MainActivity;
import com.example.myim.controller.activity.PostActivity;
import com.example.myim.controller.adapter.postAdapter;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.dao.PostInfoDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class CommunityFragment extends Fragment {
    private ListView listView;
    private List<postInfo> posts;
    private FloatingActionButton btn_to_editor;

    private postAdapter adapter;
    private PostInfoDao ps;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button shuaixuan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ps = new PostInfoDao();
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_community, container,false);
            listView = view.findViewById(R.id.post_list);
            shuaixuan = view.findViewById(R.id.shuaixuan_right_button);
            btn_to_editor=view. findViewById(R.id.btn_to_editor);
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 在新线程中执行耗时操作，例如获取数据
                    posts = ps.getAllPosts();

                    // 使用 Handler 在主线程中更新 UI
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // 更新 UI，创建适配器并设置给 ListView
                            adapter = new postAdapter(requireContext(), posts);
                            listView.setAdapter(adapter);

                            // 执行其他 UI 相关操作
                        }
                    });
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        initListener();

        return view;
    }



    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击的项的数据（PostAdapter的getItem方法返回Post对象）
                postInfo clickedPost =(postInfo) adapter.getItem(position);

                // 执行跳转到新页面的操作，这里使用Intent启动一个新的Activity
                Intent intent = new Intent(getActivity(), PostActivity.class);
                // 通过Intent传递数据，如果需要的话
                intent.putExtra("postId", clickedPost.getId());
                startActivity(intent);
                getActivity().finish();
            }
        });

        btn_to_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), EditorActivity.class);
                startActivity(intent);
            }
        });
        shuaixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getActivity(), InterestPost.class);
                    startActivity(intent);
                    getActivity().finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        posts=ps.getAllPosts();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 更新 UI，创建适配器并设置给 ListView
                                adapter = new postAdapter(requireContext(), posts);
                                listView.setAdapter(adapter);

                                // 执行其他 UI 相关操作
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();


            }
        });




    }
}