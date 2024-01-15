package com.example.myim.model.db;

import static com.example.myim.model.dao.mysqlTable.password;
import static com.example.myim.model.dao.mysqlTable.url;
import static com.example.myim.model.dao.mysqlTable.user;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myim.model.bean.UserInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDbHelper {
    private static final String ip = "rm-bp1j0nu90w3y6x4ayzo.mysql.rds.aliyuncs.com";
    private static final int port = 3306;
    private static final String dbName = "chenyujia";
    private static final String url = "jdbc:mysql://" + ip + ":" + port
            + "/" + dbName;
    // 构建连接mysql的字符串
    private static String user = "lapta";
    private static String password = "Cyj14151617";

    private static final int VERSION = 1;
    public static Connection conn;
    public static Statement stmt;
    public static PreparedStatement pStmt;
    public static ResultSet rs;

    public static void getConnection() {
        try {
            while(conn==null) {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, password);
            }
            System.out.println(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void closeALl(){
        try{
            if (rs != null) {

                rs.close();
                rs=null;
            }
            if (stmt != null) {

                stmt.close();
                stmt=null;
            }
            if (pStmt != null) {

                pStmt.close();
                pStmt=null;
            }
            if (conn != null) {

                conn.close();
                conn=null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


