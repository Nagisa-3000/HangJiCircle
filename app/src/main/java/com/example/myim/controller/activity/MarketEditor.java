package com.example.myim.controller.activity;

import static com.example.myim.model.dao.GoodsInfoDao.addGoods;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myim.R;
import com.example.myim.controller.fragment.MarketFragment;
import com.example.myim.model.bean.GoodsInfo;
import com.example.myim.model.bean.UserInfo;
import com.example.myim.utils.Transform;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MarketEditor extends AppCompatActivity {
    private EditText text_price;
    private EditText text_goods;
    private EditText text_context;
    private Button btn_submit;
    private Button btn_back_to_main;
    private Button btn_select_photo;
    private ImageView iv_image;
    private String str="";

    //部件
    //需要
    private double price;
    private String name;
    private String context;
    private String photo;
    private GoodsInfo goodsInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_editor);
        goodsInfo=new GoodsInfo();
        initView();
        initListener();
    }

    private void initListener() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();//初始化goodsInfo
                goodsInfo.setPhoto(str);
                try {
                    new Thread(new Runnable() {
                        @Override

                        public void run() {

                            addGoods(goodsInfo);
                        }
                    }).start();
                    //显示成功
                    Toast.makeText(MarketEditor.this, "商品发布成功！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MarketEditor.this, MainActivity.class);

                    //传递数据，让MainActivity直接调用MarketFragment
                    intent.putExtra("key","editor_to_market");
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        btn_back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MarketEditor.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btn_select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
    }

    private void initData() {
        price=Double.parseDouble(text_price.getText().toString());
        name=text_goods.getText().toString();
        context=text_context.getText().toString();
        goodsInfo=new GoodsInfo();
        goodsInfo.setPrice(price);
        goodsInfo.setGoods(name);
        goodsInfo.setContext(context);
        UserInfo userInfo=SplashActivity.currentUser;
        goodsInfo.setUserId(userInfo.getId());

    }

    private void initView() {
        text_price= (EditText) findViewById(R.id.market_price);
        text_context= (EditText) findViewById(R.id.market_context);
        text_goods= (EditText) findViewById(R.id.market_name);
        btn_submit= (Button) findViewById(R.id.btn_market_submit);
        btn_back_to_main= (Button) findViewById(R.id.btn_back_to_main_market);
        btn_select_photo= (Button) findViewById(R.id.btn_market_picture);
        iv_image= (ImageView) findViewById(R.id.iv_image);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            Log.e(this.getClass().getName(), "Result:" + data.toString());
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                iv_image.setImageURI(uri);

                Log.e(this.getClass().getName(), "Uri:" + String.valueOf(uri));

                //
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    str = Transform.bitmapToString(bitmap);

                    System.out.println(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}