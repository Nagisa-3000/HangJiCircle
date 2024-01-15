package com.example.myim.controller.activity;

import static android.widget.Toast.LENGTH_SHORT;

import static com.example.myim.model.dao.labelInfoDao.getAllLabels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myim.R;
import com.example.myim.model.bean.LabelInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.dao.PostInfoDao;
import com.example.myim.utils.Transform;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity {
    private EditText text_title;
    private EditText text_context;
    private Button btn_submit;
    private Button btn_exit;
    private String title;
    private String context;
    private PostInfoDao ps;
    private postInfo post;
    private Button btn_send_picture;
    private ImageView iv_image;
    private String str= "";
    private ArrayList<LabelInfo> labels;
    private Spinner label_selector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initView();
        //初始化label数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                labels=getAllLabels();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<LabelInfo> adapter = new ArrayAdapter<>(EditorActivity.this,android.R.layout.simple_spinner_item,labels);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        label_selector.setAdapter(adapter);
                    }
                });
            }
        }).start();


        initListener();
    }

    private void initData() {
        ps = new PostInfoDao();
        post = new postInfo();
        title = text_title.getText().toString();
        context = text_context.getText().toString();
        post.setTitle(title);
        post.setContext(context);
        post.setPhoto(str);
    }

    private void initListener() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
                if (title.isEmpty() || title == null) {
                    Toast.makeText(EditorActivity.this, "请输入标题", LENGTH_SHORT).show();
                    return;
                }
                if (context.isEmpty() || context == null) {
                    Toast.makeText(EditorActivity.this, "请输入正文", LENGTH_SHORT).show();
                    return;
                }
                try {
                    addPostToDb();
                    //显示已经添加成功
                    Toast.makeText(EditorActivity.this, "添加到数据库成功！",LENGTH_SHORT).show();
                    //跳转到主界面
                    Intent intent=new Intent(EditorActivity.this, MainActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    Toast.makeText(EditorActivity.this, "添加到数据库错误！"+e.toString(),LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                //
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_send_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
    }

    private void addPostToDb() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ps.addPost(post);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        text_title = (EditText) findViewById(R.id.post_title);
        text_context = (EditText) findViewById(R.id.post_context);
        btn_submit = (Button) findViewById(R.id.btn_post_submit);
        btn_exit = (Button) findViewById(R.id.btn_back_to_main);
        btn_send_picture = findViewById(R.id.btn_send_picture);
        iv_image = findViewById(R.id.iv_image);
        label_selector= (Spinner) findViewById(R.id.labelSpinner);
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
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}