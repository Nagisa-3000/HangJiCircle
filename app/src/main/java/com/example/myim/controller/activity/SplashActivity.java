package com.example.myim.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myim.R;
import com.example.myim.model.bean.UserInfo;
import com.example.myim.model.dao.UserInfoDao;
import com.example.myim.model.db.UserDbHelper;

import java.sql.SQLException;

public class SplashActivity extends AppCompatActivity {
    private static SharedPreferences sp=null;

    public static SharedPreferences getSp() {
        return sp;
    }
    public static UserInfo currentUser;
    private static UserInfoDao dao=new UserInfoDao();

    public static void setSp(SharedPreferences sp) {
        SplashActivity.sp = sp;
    }

    TextView appname;
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appname = findViewById(R.id.appname);
        lottie = findViewById(R.id.lottie);

        appname.animate().translationY(-900).setDuration(2700).setStartDelay(0);
        lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sp=getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                String ac=sp.getString("account","");
                String pwd=sp.getString("password","");
                toMainOrLogin(ac,pwd);
                finish();
            }
        },5000);
    }

    private void toMainOrLogin(String ac, String pwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(ac.isEmpty()||ac==null||pwd.isEmpty()||pwd==null){
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    //获取当前用户
                    try {
                        currentUser=dao.getUserByAccountAndPass(ac,pwd);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    startActivity(intent);

                }
            }
        }).start();
    }
}