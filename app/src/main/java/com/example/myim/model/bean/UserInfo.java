package com.example.myim.model.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String name; // 用户名称
    private int id;
    private String account;

//    private String photo; // 头像
    private int type;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserInfo() {
    }

    public UserInfo(int _id,String name,String password,int type){
        this.id=_id;
        this.name=name;
        this.password=password;
        this.type=type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
//    public String getPhoto() {
//        return photo;
//    }
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }
}
