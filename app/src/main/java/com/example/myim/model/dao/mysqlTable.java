package com.example.myim.model.dao;

public class mysqlTable {
    public static final String ip = "10.193.18.25";
    public static final int port = 3306;
    public static final String dbName = "userInfo";
    public static final String user = "root";
    public static final String password = "root";

    public static final String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName+"?useUnicode=true&characterEncoding=utf-8&useSSL=false";
}
