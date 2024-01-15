package com.example.myim.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.myim.R;
import com.example.myim.controller.fragment.CommunityFragment;
import com.example.myim.controller.fragment.MarketFragment;
import com.example.myim.controller.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {

    private SettingFragment settingFragment;
    private CommunityFragment communityFragment;
    private MarketFragment marketFragment;
    private RadioGroup rg_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
        initListener();
    }

    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                Fragment fragment = null;
                if (checkId == R.id.rb_main_market) {
                    fragment = marketFragment;
                } else if (checkId == R.id.rb_main_communicate) {
                    fragment = communityFragment;
                } else if (checkId == R.id.rb_main_setting) {
                    fragment = settingFragment;
                }
                switchFragment(fragment);
            }
        });

    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main, fragment).commit();//提交
    }

    private void initData() {
        marketFragment = new MarketFragment();
        settingFragment = new SettingFragment();
        communityFragment = new CommunityFragment();
    }

    private void initView() {
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

    }
}
