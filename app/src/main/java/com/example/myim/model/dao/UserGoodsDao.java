package com.example.myim.model.dao;

import com.example.myim.controller.activity.SplashActivity;
import com.example.myim.model.bean.GoodsInfo;
import com.example.myim.model.bean.UserInfo;
import com.example.myim.model.db.UserDbHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserGoodsDao extends UserDbHelper {
    public static void updateUserGoods(int goodsId){
        try{
            while (conn==null) {
                getConnection();
            }
            String sql="select * from usergoods where user_id =? and goods_id =? ";
            pStmt=conn.prepareStatement(sql);
            pStmt.setInt(1, SplashActivity.currentUser.getId());
            pStmt.setInt(2,goodsId);
            rs=pStmt.executeQuery();
            if(rs.next()){//已经收藏过了
                sql="delete from usergoods where user_id =? and goods_id=?";
                pStmt=conn.prepareStatement(sql);
                pStmt.setInt(1, SplashActivity.currentUser.getId());
                pStmt.setInt(2,goodsId);
                pStmt.executeUpdate();
            }
            else{
                sql="insert into usergoods(user_id, goods_id) values(?,?)";
                pStmt=conn.prepareStatement(sql);
                pStmt.setInt(1, SplashActivity.currentUser.getId());
                pStmt.setInt(2,goodsId);
                pStmt.executeUpdate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            closeALl();
        }

    }
    public static boolean getIfWanted(GoodsInfo goods){
        UserInfo user=SplashActivity.currentUser;

        try{
            getConnection();
            String sql="select * from usergoods where user_id=? and goods_id=?";
            pStmt=conn.prepareStatement(sql);
            pStmt.setInt(1,user.getId());
            pStmt.setInt(2,goods.getId());
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
        }
        closeALl();
        return false;

    }
    public static ArrayList<GoodsInfo> getAllLoves(){
        UserInfo user=SplashActivity.currentUser;
        ArrayList<GoodsInfo> arr=new ArrayList<>();
        try{
            getConnection();
            String sql = "SELECT g.* FROM goodsinfo g " +
                    "JOIN usergoods ug ON g.id = ug.goods_id " +
                    "WHERE ug.user_id = ?";
            pStmt=conn.prepareStatement(sql);
            pStmt.setInt(1,user.getId());
            rs=pStmt.executeQuery();
            while(rs.next()){
                GoodsInfo goods=new GoodsInfo();
                goods.setGoods(rs.getString("goods"));
                goods.setContext(rs.getString("context"));
                goods.setUserId(rs.getInt("user_id"));
                goods.setPhoto(rs.getString("photo"));
                arr.add(goods);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            closeALl();
        }
        return arr;
    }
}
