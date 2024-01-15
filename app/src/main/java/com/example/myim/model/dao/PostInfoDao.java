package com.example.myim.model.dao;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.myim.controller.activity.EditorActivity;
import com.example.myim.controller.activity.SplashActivity;
import com.example.myim.model.bean.UserInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.db.UserDbHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PostInfoDao extends UserDbHelper {
    public ArrayList<postInfo> getAllPosts() {
        ArrayList<postInfo> items = null;
        try {
            getConnection();
            items = new ArrayList<>();
            postInfo info ;
            String sql = "SELECT p.id, p.title, p.context, p.user_id,p.createtime, u.username " +
                    "FROM postinfo p " +
                    "INNER JOIN userinfo u ON p.user_id = u.id";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                info = new postInfo();
                info.setTitle(rs.getString("title"));
                info.setContext(rs.getString("context"));
                info.setUserId(rs.getInt("user_id"));
                info.setTimestamp(rs.getTimestamp("createtime"));
                info.setId(rs.getInt("id"));
                info.setUserName(rs.getString("username"));
                items.add(info);
            }

        } catch (Exception e) {
            closeALl();
            e.printStackTrace();
        }finally{
            closeALl();
        }

        //排序
        Collections.sort(items, new Comparator<postInfo>() {
            @Override
            public int compare(postInfo post1, postInfo post2) {
                return post2.getTimestamp().compareTo(post1.getTimestamp());
            }
        });
        return items;
    }
    public void addPost(postInfo post) {
        System.out.println(1);
        try{
            while(conn==null) {
                getConnection();
                Thread.sleep(100);
            }
            String sql="insert into postinfo(title,context,user_id,photo) values(?,?,?,?)";//label
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1,post.getTitle());
            pStmt.setString(2,post.getContext());

            pStmt.setInt(3, SplashActivity.currentUser.getId());
            pStmt.setString(4,post.getPhoto());
            pStmt.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            closeALl();
            throw new RuntimeException(e);
        } finally{
            closeALl();
        }
    }

    public postInfo getPostById(int id) {
        postInfo post = null;
        try {
            while(conn==null) {
                getConnection();
                Thread.sleep(100);
            }
            String sql = "select * from postinfo where id=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            rs = pStmt.executeQuery();
            if(rs.next()) {
                post = new postInfo();
                post.setId(id);
                post.setLikes(rs.getInt("likes"));
                post.setTitle(rs.getString("title"));
                post.setContext(rs.getString("context"));
                post.setPhoto(rs.getString("photo"));
            }

        } catch (Exception e) {
            closeALl();
            e.printStackTrace();
        } finally {
            closeALl();
        }
        return post;
    }

    public static void updateLikes(boolean ifLike, postInfo post){
        UserInfo user=SplashActivity.currentUser;
        //获取当前用户
        try{
            getConnection();
            String sql="select * from user_likedpost where user_id=? and post_id=?";
            pStmt=conn.prepareStatement(sql);
            pStmt.setInt(1,user.getId());
            pStmt.setInt(2,post.getId());
            rs=pStmt.executeQuery();
            if(!rs.next()){//如果无
                sql="update postinfo set likes=likes+1 where id="+post.getId();//点赞数+1
                if(conn==null){
                    getConnection();
                }
                pStmt.executeUpdate(sql);
                sql="insert into user_likedpost (user_id, post_id) values(?,?)";
                if(conn==null){
                    getConnection();
                }
                pStmt=conn.prepareStatement(sql);
                pStmt.setInt(1,user.getId());
                pStmt.setInt(2,post.getId());
                pStmt.executeUpdate();
            }
            else{
                sql="delete from user_likedpost where user_id =? and post_id=?";
                if(conn==null){
                    getConnection();
                }
                pStmt=conn.prepareStatement(sql);
                pStmt.setInt(1,user.getId());
                pStmt.setInt(2,post.getId());
                pStmt.executeUpdate();
                sql="update postinfo set likes=likes-1 where id="+post.getId();//点赞数+1
                if(conn==null){
                    getConnection();
                }
                pStmt.executeUpdate(sql);
            }
            // 在userlikepost中更新：如果有user-post，则删除，同时post的点赞数-1；
            //如果无，则增加，同时post点赞数+1;
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public static boolean getIfLiked(postInfo post){
        UserInfo user=SplashActivity.currentUser;
        try{
            getConnection();
            String sql="select * from user_likedpost where user_id=? and post_id =?";
            pStmt=conn.prepareStatement(sql);
            pStmt.setInt(1,user.getId());
            pStmt.setInt(2,post.getId());
            rs=pStmt.executeQuery();
            boolean tmp=rs.next();
            if(tmp){
                closeALl();
                return true;
            }
            else{
                closeALl();
                return false;

            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeALl();
        }
        return false;
    }
}
