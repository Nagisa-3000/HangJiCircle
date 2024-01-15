package com.example.myim.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myim.R;
import com.example.myim.controller.fragment.SettingFragment;
import com.example.myim.model.bean.UserInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.dao.PostInfoDao;

import com.example.myim.model.dao.UserInfoDao;
import com.example.myim.model.db.UserDbHelper;
import com.example.myim.model.db.UserDbHelper.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Modification extends AppCompatActivity {


    private EditText text_context;
    private Button btn_submit;
    private Button btn_exit;
    private Button person_sign;
    private String final_str;
    private String target;

    private UserDbHelper udh;

    private UserInfoDao us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modification_activity);
        Intent intent1 = getIntent();
        target = intent1.getStringExtra("target");


        initView();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //更改
                initData();
                Intent intent = new Intent(Modification.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //退回到settingfragment
                Intent intent = new Intent(Modification.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }



    private void initData() {
        final_str = text_context.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    int id = us.getUserIdByAccount(SplashActivity.currentUser.getAccount());
                    String Id = Integer.toString(id);
                    while(udh.conn==null){
                        udh.getConnection();
                    }
                    if(target.equals("nickname")){
                        String sql = "update userinfo set username = ? where id = ?";
                        udh.pStmt = udh.conn.prepareStatement(sql);
                        udh.pStmt.setString(1, final_str);
                        udh.pStmt.setInt(2, id);
                        udh.pStmt.executeUpdate();


                    }else if(target.equals("signature")){
                        String sql = "update userinfo set signature = ? where id = ?";
                        udh.pStmt = udh.conn.prepareStatement(sql);
                        udh.pStmt.setString(1, final_str);
                        udh.pStmt.setInt(2, id);
                        udh.pStmt.executeUpdate();

                    }
                    udh.conn.close();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }





            }
        }).start();

    }


    private void initView() {
        text_context = (EditText) findViewById(R.id.modi_input);
        btn_submit = (Button) findViewById(R.id.modi_submit);
        btn_exit = (Button) findViewById(R.id.back_to_setting);
        udh = new UserDbHelper();
        us = new UserInfoDao();
    }



}
