package com.example.myim.controller.activity;

import static com.example.myim.model.dao.UserGoodsDao.getAllLoves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.myim.R;
import com.example.myim.controller.adapter.MyLoveAdapter;
import com.example.myim.controller.adapter.postAdapter;
import com.example.myim.model.bean.GoodsInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.dao.UserGoodsDao;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyLoveActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<GoodsInfo> myloves;
    private Button btn_back_to_main;
    private MyLoveAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_love);

        initView();

        initListener();

        new Thread(new Runnable() {
            @Override
            public void run() {
                myloves=  getAllLoves();
                if(myloves==null){
                    myloves=new ArrayList<>();
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // 更新 UI，创建适配器并设置给 ListView
                        adapter=new MyLoveAdapter(MyLoveActivity.this,myloves);
                        listView.setAdapter(adapter);

                        // 执行其他 UI 相关操作
                    }
                });

            }
        }).start();


    }

    private void initListener() {
        btn_back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyLoveActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击的项的数据（PostAdapter的getItem方法返回Post对象）
                postInfo clickedPost =(postInfo) adapter.getItem(position);

                // 执行跳转到新页面的操作，这里使用Intent启动一个新的Activity
                Intent intent = new Intent(MyLoveActivity.this, GoodsActivity.class);
                // 通过Intent传递数据，如果需要的话
                intent.putExtra("goodsId", clickedPost.getId());
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView() {
        btn_back_to_main= (Button) findViewById(R.id.btn_back_to_main);
        listView= (ListView) findViewById(R.id.mylove_listview);

    }

}