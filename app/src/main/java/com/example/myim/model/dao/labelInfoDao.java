package com.example.myim.model.dao;

import com.example.myim.model.bean.LabelInfo;
import com.example.myim.model.db.UserDbHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class labelInfoDao extends UserDbHelper {
    public static ArrayList<LabelInfo> getAllLabels(){
        ArrayList<LabelInfo> labels=new ArrayList<>();
        try{
            getConnection();
            String sql = "select * from labels";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while(rs.next()){
                String labelKind = rs.getString("LabelKind");
                if(labelKind!=null&&!labelKind.trim().isEmpty()){
                    labels.add(new LabelInfo(labelKind));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            closeALl();
        }
        return labels;
    }
}
