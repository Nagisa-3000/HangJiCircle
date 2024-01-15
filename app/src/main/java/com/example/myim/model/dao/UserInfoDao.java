package com.example.myim.model.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myim.controller.activity.SplashActivity;
import com.example.myim.model.bean.UserInfo;
import com.example.myim.model.db.UserDbHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoDao extends UserDbHelper{
    public int register(String username, String password, int type , String account) throws SQLException {
        int row = 0;
        try {
            while(conn==null) {
                getConnection();
                Thread.sleep(100);
            }
            String sql = "insert into userinfo(username,password,account) values(?,?,?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1,username);
            pStmt.setString(2,password);
            pStmt.setString(3,account);
            row = pStmt.executeUpdate();
        } catch (SQLException e) {
            closeALl();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeALl();
        }
        return row;
    }
    @SuppressLint("Range")
    public UserInfo getUserByAccountAndPass(String account,String pass) throws SQLException {
        UserInfo item=null;
        try {
            while(conn==null) {
                getConnection();
                Thread.sleep(100);
            }
            String sql = "select * from userinfo where account=? and password=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1,account);
            pStmt.setString(2,pass);
            rs=pStmt.executeQuery();
            if(rs.next()){
                item=new UserInfo();
                item.setName(rs.getString("username"));
                item.setId(rs.getInt("id"));
                item.setAccount(account);
                item.setPassword(pass);
            }
        } catch (SQLException e) {
            closeALl();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeALl();
        }
        return item;
    }

    public int getUserIdByAccount(String account) throws SQLException {
        int user_Id=0;
        try {
            while(conn==null) {
                getConnection();
                Thread.sleep(100);
            }
            String sql = "select * from userinfo where account=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1,account);
            rs=pStmt.executeQuery();
            if(rs.next()){
                user_Id=(rs.getInt("id"));
                return user_Id;
            }
        } catch (SQLException e) {
            closeALl();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeALl();
        }
        return 0;
    }
    public String getUserNickNameByAccount(String account) throws SQLException {
        String user_NickName="";
        try {
            while(conn==null) {
                getConnection();
                Thread.sleep(100);
            }
            String sql = "select * from userinfo where account=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1,account);
            rs=pStmt.executeQuery();
            if(rs.next()){
                user_NickName=(rs.getString("username"));
                return user_NickName;
            }
        } catch (SQLException e) {
            closeALl();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeALl();
        }
        return user_NickName;
    }
    public String getUserSignatureByAccount(String account) throws SQLException {
        String user_Signature="";
        try {
            while(conn==null) {
                getConnection();
                Thread.sleep(100);
            }
            String sql = "select * from userinfo where account=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1,account);
            rs=pStmt.executeQuery();
            if(rs.next()){
                user_Signature=(rs.getString("signature"));
                return user_Signature;
            }
        } catch (SQLException e) {
            closeALl();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            closeALl();
        }
        return user_Signature;
    }
}

