package com.example.myim.controller.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myim.R;
import com.example.myim.model.bean.UserInfo;
import com.example.myim.model.dao.UserInfoDao;
import com.example.myim.model.db.UserDbHelper;

import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity {
    private EditText name_text;
    private EditText phone_text;
    private EditText password_text;
    private EditText repassword_text;
    private EditText test_code;
    private Button register_button;
    private Button cancel_register_button;
    private Button get_test_code;

    private UserInfoDao dao;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dao = new UserInfoDao();
        initView();

        initListener();
    }

    private void initView() {
        name_text = (EditText) findViewById(R.id.name_text);
        phone_text = (EditText) findViewById(R.id.phone_text);
        password_text = (EditText) findViewById(R.id.password_text);
        repassword_text = (EditText) findViewById(R.id.repassword_text);
        ;
        test_code = (EditText) findViewById(R.id.test_code);
        cancel_register_button = (Button) findViewById(R.id.cancel_register_button);
        ;
        register_button = (Button) findViewById(R.id.register_button);
        ;
        get_test_code = (Button) findViewById(R.id.get_test_code);
        ;
    }

    private void initListener() {
        cancel_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToLogin();
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    regist();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        get_test_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "haha", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void returnToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    private void regist() throws SQLException {
        //获取
        String registName = name_text.getText().toString();
        String registPhone = phone_text.getText().toString();
        String registPwd = password_text.getText().toString();
        String repassword = repassword_text.getText().toString();
        String testCode = test_code.getText().toString();
        //校验（后期加入其他校验）
//        if(TextUtils.isEmpty(registName)){
//            Toast.makeText(RegisterActivity.this,"the name can't be empty", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(registPhone)){
//            Toast.makeText(RegisterActivity.this,"the phone can't be empty", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(registPwd)){
//            Toast.makeText(RegisterActivity.this,"the password can't be empty", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(repassword)){
//            Toast.makeText(RegisterActivity.this,"请再次输入密码", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(testCode)){
//            Toast.makeText(RegisterActivity.this,"请输入验证码！", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(!RegistrationValidator.checkRegistrationAccount(registName)){
//            Toast.makeText(RegisterActivity.this,"Illegal account!",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(!RegistrationValidator.checkRegistrationPassword(registPwd)){
//            Toast.makeText(RegisterActivity.this,"Illegal password:密码应由字母、数字、特殊字符，任意2种组成",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(!RegistrationValidator.checkRegistrationRepassword(repassword,registPwd)){
//            Toast.makeText(RegisterActivity.this,"两次输入密码不同！",Toast.LENGTH_SHORT).show();
//            return;
//        }
        //之后插入对验证码的验证。（或许吧）
        UserInfo item = new UserInfo();
        item.setAccount(registPhone);
        item.setType(0);
        item.setName(registName);
        item.setPassword(registPwd);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final int row = dao.register(registName, registPwd, 0, registPhone);
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
}
