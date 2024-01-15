package com.example.myim.model.dao;

import com.example.myim.model.bean.GoodsInfo;
import com.example.myim.model.bean.postInfo;
import com.example.myim.model.db.UserDbHelper;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class GoodsInfoDao extends UserDbHelper {
    public static ArrayList<GoodsInfo> getAllGoods() {
        ArrayList<GoodsInfo> arr = new ArrayList<>();
        try {
            getConnection();
            String sql = "select * from goodsinfo";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                GoodsInfo good = new GoodsInfo();
                good.setGoods(rs.getString("goods"));
                good.setId(rs.getInt("id"));
                good.setContext(rs.getString("context"));
                good.setPrice(rs.getDouble("price"));
                good.setPhoto(rs.getString("photo"));
                good.setUserId(rs.getInt("user_id"));
                arr.add(good);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeALl();
        }
        return arr;
    }
    public static GoodsInfo getGoodsById(int id){
        GoodsInfo good = null;
        try {
            while(conn==null) {
                getConnection();
                Thread.sleep(100);
            }
            String sql = "select * from goodsinfo where id=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            rs = pStmt.executeQuery();
            if(rs.next()) {
                good = new GoodsInfo();
                good.setId(id);
                good.setPrice(rs.getDouble("price"));
                good.setGoods(rs.getString("goods"));
                good.setContext(rs.getString("context"));
                good.setPhoto(rs.getString("photo"));
                good.setUserId(rs.getInt("user_id"));
            }

        } catch (Exception e) {
            closeALl();
            e.printStackTrace();
        } finally {
            closeALl();
        }
        return good;
    }
    public static void addGoods(GoodsInfo goods){
        try{
            getConnection();
            if(goods.getPhoto()==null||goods.getPhoto().isEmpty()) {//不插入图片
                String sql = "insert into goodsinfo(price,goods,context,user_id) values(?,?,?,?)";
                pStmt=conn.prepareStatement(sql);
                pStmt.setDouble(1,goods.getPrice());
                pStmt.setString(2,goods.getGoods());
                pStmt.setString(3,goods.getContext());
                pStmt.setInt(4,goods.getUserId());
                pStmt.executeUpdate();
            }
            else{
                String sql = "insert into goodsinfo(price,goods,context,user_id,photo) values(?,?,?,?,?)";
                pStmt=conn.prepareStatement(sql);
                pStmt.setDouble(1,goods.getPrice());
                pStmt.setString(2,goods.getGoods());
                pStmt.setString(3,goods.getContext());
                pStmt.setInt(4,goods.getUserId());
                pStmt.setString(5,goods.getPhoto());
                pStmt.executeUpdate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeALl();
        }
    }
}
