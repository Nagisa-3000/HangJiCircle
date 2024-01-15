package com.example.myim.model.dao;

import com.example.myim.controller.activity.SplashActivity;
import com.example.myim.model.bean.CommentInfo;
import com.example.myim.model.bean.UserInfo;
import com.example.myim.model.db.UserDbHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class GoodsCommentInfoDao extends UserDbHelper {
    public static ArrayList<CommentInfo> getCommentsByGoodsId(int id) {
        ArrayList<CommentInfo> arr = new ArrayList<>();
        try {
            getConnection();  // 假设这个方法安全地处理数据库连接

            String sql = "SELECT * FROM goodscommentinfo WHERE goods_id="+id;
            pStmt=conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setId(rs.getInt("id"));
                commentInfo.setUserId(rs.getInt("user_id"));
                commentInfo.setContext(rs.getString("context"));// 正确的列名
                // 在这里获取图片和标签（如果需要）
                arr.add(commentInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeALl();
        }
        return arr;
    }

    public void addCommentToGoods(int goodsId, String comment){
        try{
            while(conn==null) {
                getConnection();
            }
            String sql="insert into goodscommentinfo (context,user_id,goods_id) values (?,?,?)";
            pStmt=conn.prepareStatement(sql);
            UserInfo user= SplashActivity.currentUser;
            pStmt.setString(1,comment);
            pStmt.setInt(2,user.getId());
            pStmt.setInt(3, goodsId);

            pStmt.executeUpdate();//
        } catch (SQLException e) {
            closeALl();
            throw new RuntimeException(e);
        } finally{
            closeALl();
        }
    }
}
