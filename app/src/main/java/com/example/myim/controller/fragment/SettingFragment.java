package com.example.myim.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myim.R;
import com.example.myim.controller.activity.LoginActivity;
import com.example.myim.controller.activity.Modification;
import com.example.myim.controller.activity.MyLoveActivity;
import com.example.myim.controller.activity.SplashActivity;
import com.example.myim.model.dao.UserInfoDao;

import java.sql.SQLException;

public class SettingFragment extends Fragment {

    Button bt_setting_out;
    Button personal_nickname;

    Button personal_signature;

    Button personal_post;

    Button shopping_cart;

    private UserInfoDao Uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            View view = View.inflate(getActivity(), R.layout.fragment_setting, null);
            initView(view);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String account = SplashActivity.currentUser.getAccount();



                        personal_nickname.setText(Uid.getUserNickNameByAccount(account));
                        personal_signature.setText(Uid.getUserSignatureByAccount(account));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


                }
            }).start();


            initListener();


            //调用数据库更新名字和签名
            //


            return view;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void initData() {
    }

    private void initView(View view) {
        bt_setting_out = (Button) view.findViewById(R.id.left_button);
        personal_nickname =(Button)  view.findViewById(R.id.personal_nickname);
        personal_signature =(Button)  view.findViewById(R.id.personal_signature);
        personal_post =(Button)  view.findViewById(R.id.personal_post);
        shopping_cart =(Button)  view.findViewById(R.id.shopping_cart);
        Uid = new UserInfoDao();
    }

    private void initListener() {
        bt_setting_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //删除序列化文件
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE);

                        // 获取 SharedPreferences.Editor 对象
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        // 清除用户信息
                        editor.remove("username");
                        editor.remove("password");

// 提交更改
                        editor.apply();
                    }
                }).start();
                //跳转到login
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                getActivity().finish();
            }
        });
        personal_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Modification.class);
                intent.putExtra("target","nickname" );
                startActivity(intent);
                getActivity().finish();

            }
        });



        personal_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Modification.class);
                intent.putExtra("target","signature" );
                startActivity(intent);
                getActivity().finish();

            }
        });

        personal_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MyLoveActivity.class);
                startActivity(intent);
            }
        });

    }
}


