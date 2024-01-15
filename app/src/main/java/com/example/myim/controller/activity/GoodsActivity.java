package com.example.myim.controller.activity;



import static com.example.myim.model.dao.GoodsInfoDao.getGoodsById;
import static com.example.myim.model.dao.PostInfoDao.updateLikes;
import static com.example.myim.model.dao.UserGoodsDao.getIfWanted;
import static com.example.myim.model.dao.UserGoodsDao.updateUserGoods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myim.R;
import com.example.myim.controller.adapter.CommentsAdapter;
import com.example.myim.model.bean.CommentInfo;
import com.example.myim.model.bean.GoodsInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.dao.CommentsInfoDao;
import com.example.myim.model.dao.GoodsCommentInfoDao;
import com.example.myim.model.dao.PostInfoDao;
import com.example.myim.utils.Transform;

import java.util.ArrayList;

public class GoodsActivity extends AppCompatActivity {
    private CommentsInfoDao commentsInfoDao;
    private PostInfoDao GoodsInfoDao;
    private TextView product_title;
    private TextView product_price;
    private TextView product_context;
    private Button btn_to_main;
    private ArrayList<CommentInfo> comments;
    private Button btn_comment_submit;
    private boolean isLiked = false;//这里修改成调用数据库。
    private ImageView btn_want;
    private TextView cnt_want;
    private EditText text_comment;
    private int want;
    private int goodsId;
    private GoodsInfo goods;
    private CommentsAdapter commentsAdapter;
    private LinearLayout product_picture;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GoodsCommentInfoDao goodsCommentInfoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        goodsCommentInfoDao=new GoodsCommentInfoDao();
        Intent intent=getIntent();
        if(intent!=null){
            goodsId=intent.getIntExtra("goodsId",0);
        }


        initView();
        initListener();
        try {
            initData();
            commentsAdapter=new CommentsAdapter(this,comments);
            listView.setAdapter(commentsAdapter);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isLiked=getIfWanted(goods);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            btn_want.setImageResource(isLiked?  R.drawable.baseline_shopping_cart_24:R.drawable.baseline_add_shopping_cart_24);
                        }
                    });
                }
            }).start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




        product_picture.setGravity(Gravity.CENTER);
        String str = goods.getPhoto();
        if(!str.isEmpty()){
            Bitmap bitmap= Transform.stringToBitmap(goods.getPhoto());
            ImageButton imageButton = new ImageButton(this);
            int imageWidth = bitmap.getWidth();
            int imageHeight = bitmap.getHeight();
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;
            int desiredWidth = screenWidth / 5*4;
            float scale = (float) desiredWidth / imageWidth;
            int finalWidth = (int) (imageWidth * scale);
            int finalHeight = (int) (imageHeight * scale);
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, imageWidth, imageHeight, matrix, false);
            imageButton.setImageBitmap(scaledBitmap);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(finalWidth, finalHeight);
            imageButton.setLayoutParams(params);
            product_picture.addView(imageButton);

        }

        //adapter 初始评论区
        commentsAdapter=new CommentsAdapter(this,comments);
        listView.setAdapter(commentsAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void initData() throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取该帖子的信息
                while(goods==null) {
                    goods = getGoodsById(goodsId);
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
//                获取该帖子的评论信息
                comments = GoodsCommentInfoDao.getCommentsByGoodsId(goodsId);
            }
        });
        thread1.start();
        thread2.start();
//         等待线程执行完成
        thread1.join();
        thread2.join();
        product_title.setText(goods.getGoods());
        product_price.setText("￥"+String.valueOf(goods.getPrice()));
        product_context.setText(goods.getContext());

    }

    private void initListener() {
        btn_to_main.setOnClickListener(new View.OnClickListener() {//退出
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GoodsActivity.this, MainActivity.class);
                startActivity(intent);
                //更新点赞数
            }
        });
        btn_want.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLiked=!isLiked;
                btn_want.setImageResource(isLiked?  R.drawable.baseline_shopping_cart_24:R.drawable.baseline_add_shopping_cart_24);
                //更新界面
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            updateUserGoods(goodsId);
                        }
                    }).start();

                    if(isLiked) {
                        Toast.makeText(GoodsActivity.this, "已添加至购物车！", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(GoodsActivity.this, "已从购物车中删除！", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(GoodsActivity.this, "添加/删除失败"+e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        comments=GoodsCommentInfoDao.getCommentsByGoodsId(goodsId);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 更新 UI，创建适配器并设置给 ListView
                                commentsAdapter=new CommentsAdapter(GoodsActivity.this,comments);
                                listView.setAdapter(commentsAdapter);

                                // 执行其他 UI 相关操作
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();


            }
        });
        btn_comment_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = text_comment.getText().toString();//获取评论内容
                //判断是否为空
                if (comment.isEmpty() || comment == null) {
                    Toast.makeText(GoodsActivity.this, "输入的评论不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            goodsCommentInfoDao.addCommentToGoods(goodsId, comment);
                            text_comment.setText("");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 这里可以调用方法更新评论区
                                    updateComments();
                                }
                            });
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(GoodsActivity.this, "发布评论成功！", Toast.LENGTH_SHORT).show();
                //更新评论区
            }
        });
    }

    private void updateComments() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 在这里调用加载评论区的方法，例如 getCommentsByPostId
                comments = GoodsCommentInfoDao.getCommentsByGoodsId(goodsId);

                // 更新 UI，可以使用 Handler 或者 runOnUiThread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在这里更新评论区的 UI，例如显示新的评论列表
                        commentsAdapter.setData(comments);

                        listView.setAdapter(commentsAdapter);
                        commentsAdapter.notifyDataSetChanged();
                        // 你可以使用适配器更新 RecyclerView 或者直接更新 TextView 等
                    }
                });
            }
        }).start();
    }

    private void initView() {
        btn_to_main = (Button) findViewById(R.id.back_button);
        product_title = (TextView) findViewById(R.id.product_name);
        product_price = (TextView) findViewById(R.id.product_price);
        product_context = (TextView) findViewById(R.id.product_description);
        listView = (ListView) findViewById(R.id.comments_list_view);
        btn_comment_submit = (Button) findViewById(R.id.send_comment_button);
        product_picture = (LinearLayout) findViewById(R.id.product_image);
        btn_want= (ImageView) findViewById(R.id.want_button);
        cnt_want= (TextView) findViewById(R.id.want_cnt);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_goods);
        text_comment= (EditText) findViewById(R.id.comment_input);
    }

}