package com.example.myim.controller.activity;

import static com.example.myim.model.dao.GoodsInfoDao.getAllGoods;
import static com.example.myim.model.dao.PostInfoDao.getIfLiked;
import static com.example.myim.model.dao.PostInfoDao.updateLikes;
import static com.example.myim.model.dao.UserGoodsDao.getIfWanted;
import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myim.R;
import com.example.myim.controller.adapter.CommentsAdapter;
import com.example.myim.controller.adapter.MarketAdapter;
import com.example.myim.model.bean.CommentInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.dao.CommentsInfoDao;
import com.example.myim.model.dao.PostInfoDao;
import com.example.myim.utils.Transform;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private CommentsInfoDao commentsInfoDao;
    private PostInfoDao postInfoDao;
    private TextView post_title;
    private TextView post_context;
    private Button btn_to_main;
    private ArrayList<CommentInfo> comments;
    private Button btn_comment_submit;
    private boolean isLiked=false;
    private ImageView btn_like;
    private TextView cnt_like;
    private EditText text_comment;
    private int like;
    private int postId;
    private postInfo post;
    private CommentsAdapter commentsAdapter;
    private ImageView post_picture;
    private ListView comments_list;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent=getIntent();
        if (intent != null) {
            postId=intent.getIntExtra("postId",0);
        }
        commentsInfoDao=new CommentsInfoDao();
        postInfoDao=new PostInfoDao();
        initView();
        initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                isLiked=getIfLiked(post);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        btn_like.setImageResource(isLiked?  R.drawable.baseline_thumb_up_alt_24:R.drawable.baseline_thumb_up_off_alt_24);
                    }
                });
            }
        }).start();
        //初始化图片
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.post_image_layout);//该
        linearLayout.setGravity(Gravity.CENTER);
        String str =post.getPhoto();
        if(!str.isEmpty()){
            Bitmap bitmap=Transform.stringToBitmap(post.getPhoto());
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
            linearLayout.addView(imageButton);
        }


        //adapter 初始评论区
        commentsAdapter=new CommentsAdapter(this,comments);
        comments_list.setAdapter(commentsAdapter);
        initListener();
    }

    private void initData() {
        try {

            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 获取该帖子的信息
                    while(post==null) {
                        post = postInfoDao.getPostById(postId);
                    }
                }
            });

            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                     //获取该帖子的评论信息
                    comments = commentsInfoDao.getCommentsByPostId(postId);
                }
            });

            // 启动线程
            thread1.start();
            thread2.start();

            // 等待线程执行完成
            thread1.join();
            thread2.join();
            like = post.getLikes();
            post_title.setText(post.getTitle());
            post_context.setText(post.getContext());
            cnt_like.setText(Integer.toString(like));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initListener() {
        btn_to_main.setOnClickListener(new View.OnClickListener() {//退出
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PostActivity.this, MainActivity.class);
                startActivity(intent);
                //更新点赞数
            }
        });
        btn_like.setOnClickListener(new View.OnClickListener() {//点赞
            @Override
            public void onClick(View view) {
                isLiked=!isLiked;
                like+=isLiked?1:-1;
                cnt_like.setText(String.valueOf(like));
                btn_like.setImageResource(isLiked? R.drawable.baseline_thumb_up_alt_24 : R.drawable.baseline_thumb_up_off_alt_24);
                //更新界面
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updateLikes(isLiked, post);
                        }
                    }).start();

                    if(isLiked) {
                        Toast.makeText(PostActivity.this, "点赞成功！", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(PostActivity.this, "已取消点赞！", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(PostActivity.this, "点赞/点踩失败"+e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        btn_comment_submit.setOnClickListener(new View.OnClickListener() {//提交评论
            @Override
            public void onClick(View view) {
                    String comment = text_comment.getText().toString();//获取评论内容
                    //判断是否为空
                    if (comment.isEmpty() || comment == null) {
                        Toast.makeText(PostActivity.this, "输入的评论不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                commentsInfoDao.addCommentToPost(postId, comment);
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
                Toast.makeText(PostActivity.this, "发布评论成功！", Toast.LENGTH_SHORT).show();
                    //更新评论区
                }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        comments=CommentsInfoDao.getCommentsByPostId(postId);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 更新 UI，创建适配器并设置给 ListView
                                commentsAdapter = new CommentsAdapter(PostActivity.this, comments);
                                comments_list.setAdapter(commentsAdapter);

                                // 执行其他 UI 相关操作
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();


            }
        });
    }

    private void updateComments() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 在这里调用加载评论区的方法，例如 getCommentsByPostId
                comments = commentsInfoDao.getCommentsByPostId(postId);

                // 更新 UI，可以使用 Handler 或者 runOnUiThread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在这里更新评论区的 UI，例如显示新的评论列表
                        commentsAdapter.setData(comments);

                        comments_list.setAdapter(commentsAdapter);
                        commentsAdapter.notifyDataSetChanged();
                        // 你可以使用适配器更新 RecyclerView 或者直接更新 TextView 等
                    }
                });
            }
        }).start();
    }

    private void initView() {
        post_title= (TextView) findViewById(R.id.post_title);
        post_context= (TextView) findViewById(R.id.post_context);
        btn_to_main= (Button) findViewById(R.id.btn_back_to_main);
        btn_like= (ImageView) findViewById(R.id.like_button);
        text_comment= (EditText) findViewById(R.id.comment_input);
        btn_comment_submit= (Button) findViewById(R.id.btn_send_comment);
        cnt_like= (TextView) findViewById(R.id.like_count);
        comments_list= (ListView) findViewById(R.id.comments_list_view);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_post);
    }
}