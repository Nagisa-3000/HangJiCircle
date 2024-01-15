package com.example.myim.controller.activity;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myim.R;
import com.example.myim.model.bean.UserInfo;
import com.example.myim.model.dao.UserInfoDao;
import com.example.myim.model.db.UserDbHelper;

import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    private EditText name_hint;
    private EditText password_hint;
    private Button bt_login_regist;
    private Button bt_login_login;
    private UserDbHelper userDbHelper;
    private UserInfoDao dao;
    private Handler mainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initView();
        initListener();
    }

    private void initView() {
        name_hint= (EditText) findViewById(R.id.name_text);
        password_hint=(EditText) findViewById(R.id.password_text);
        bt_login_login= (Button) findViewById(R.id.login_button);
        bt_login_regist= (Button) findViewById(R.id.register_button);
        dao=new UserInfoDao();
    }

    private void initListener() {
        bt_login_regist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                regist();
            }
        });
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    loginAct();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void loginAct() throws SQLException {
        final String account=name_hint.getText().toString();
        final String pwd=password_hint.getText().toString();

        if(TextUtils.isEmpty(account)){
            Toast.makeText(LoginActivity.this,"请输入账号",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(pwd)){
            Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UserInfo item=dao.getUserByAccountAndPass(account,pwd);
                            if(item==null){
                                Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                SplashActivity.currentUser=item;
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                //在sharedpreferences里加入登陆过的信息：之后在由splash跳转时进行判断。

                                // 启动主页面 MainActivity
                                startActivity(intent);
                                SharedPreferences sharedPreferences=getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();

                                editor.putString("account",account);
                                editor.putString("password",pwd);
                                editor.apply();
                                // 关闭当前的 LoginActivity
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

    }

    private void upgradeSharedPreference(UserInfo userInfo) {
        SharedPreferences.Editor editor=SplashActivity.getSp().edit();
        editor.putString("account",userInfo.getAccount());
        editor.commit();
    }

    private void regist() {
        Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);

        startActivity(intent);

        finish();
    }
}