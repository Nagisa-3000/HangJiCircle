package com.example.myim.controller.activity;

import static com.example.myim.model.dao.labelInfoDao.getAllLabels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myim.R;
import com.example.myim.controller.adapter.postAdapter;
import com.example.myim.controller.fragment.CommunityFragment;
import com.example.myim.model.bean.LabelInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.dao.PostInfoDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;

import com.example.myim.model.dao.PostRecommendation;

import com.example.myim.model.dao.PostInfoDao;
import com.example.myim.model.dao.UserInfoDao;
public class InterestPost extends AppCompatActivity {


    private ListView listView;

    private List<postInfo> posts;

    private postInfo singlepost;

    private postAdapter adapter;
    private PostInfoDao ps;

    private UserInfoDao us;

    private PostRecommendation postRecommendation;

    private ArrayList<Integer> Wait;

    private Button return_button;

    private List<String> recommendPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        posts = new ArrayList<>();
        initView();
        String account = SplashActivity.currentUser.getAccount();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(us.getUserIdByAccount(account)!=0){
                        int user_id = us.getUserIdByAccount(account);
                        String User_id = Integer.toString(user_id);
                        recommendPosts = PostRecommendation.getRecommendationList(User_id);
                        //进行展示操作 recommendPosts中存post的id
                        for (String post: recommendPosts){
                            int post_id = Integer.parseInt(post);
                            singlepost = ps.getPostById(post_id);
                            posts.add(singlepost);
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                            public void run() {
                            // 更新 UI，创建适配器并设置给 ListView
                                adapter = new postAdapter(InterestPost.this, posts);
                                listView.setAdapter(adapter);

                            // 执行其他 UI 相关操作
                            }
                        });
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        initListener();
    }

    private void initView() {
        listView = findViewById(R.id.post_interest_list);
        ps = new PostInfoDao();
        us = new UserInfoDao();
        postRecommendation = new PostRecommendation();
        return_button = (Button) findViewById(R.id.left_interest_button);

    }



    private void initListener() {
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 获取点击的项的数据（PostAdapter的getItem方法返回Post对象）
                    postInfo clickedPost =(postInfo) adapter.getItem(position);

                    // 执行跳转到新页面的操作，这里使用Intent启动一个新的Activity
                    Intent intent = new Intent(InterestPost.this, PostActivity.class);
                    // 通过Intent传递数据，如果需要的话
                    intent.putExtra("postId", clickedPost.getId());
                    startActivity(intent);
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestPost.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
