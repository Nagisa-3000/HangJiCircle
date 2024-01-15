package com.example.myim.model.dao;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.myim.model.db.UserDbHelper;
import com.example.myim.model.db.UserDbHelper.*;
public class PostRecommendation {

    static class User {
        String userId;
        Set<String> likedPosts;

        public User(String userId, Set<String> likedPosts) {
            this.userId = userId;
            this.likedPosts = likedPosts;
        }
    }

    public static List<String> getRecommendationList(String targetUserId){
        // 创建一个 FutureTask 用来在子线程中获取数据
        FutureTask<List<String>> futureTask = new FutureTask<>(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                UserDbHelper.getConnection();

                if (UserDbHelper.conn != null) {
                    List<User> users = getUsersAndLikedPosts();
                    List<String> recommendations = getRecommendations(users, targetUserId);

                    UserDbHelper.closeALl();

                    return recommendations;
                }

                return new ArrayList<>(); // 返回空列表作为默认值
            }
        });

        new Thread(futureTask).start();

        try {
            return futureTask.get(); // 在数据获取完成后返回结果
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // 返回空列表作为默认值
        }

    }


    static List<User> getUsersAndLikedPosts() {
        List<User> users = new ArrayList<>();
        Map<String, Set<String>> userPostsMap = new HashMap<>();

        // SQL 查询，根据您的数据库结构可能需要修改
        String sql = "SELECT user_id, post_id FROM user_likedpost";
        try {
            // 通过 UserDbHelper 获取 Statement
            UserDbHelper.stmt = UserDbHelper.conn.createStatement();
            UserDbHelper.rs = UserDbHelper.stmt.executeQuery(sql);

            while (UserDbHelper.rs.next()) {
                String userId = UserDbHelper.rs.getString("user_id");
                String postId = UserDbHelper.rs.getString("post_id");

                userPostsMap.computeIfAbsent(userId, k -> new HashSet<>()).add(postId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 一旦完成操作，关闭 ResultSet 和 Statement
            try {
                if (UserDbHelper.rs != null) {
                    UserDbHelper.rs.close();
                }
                if (UserDbHelper.stmt != null) {
                    UserDbHelper.stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 构建 User 对象列表
        for (Map.Entry<String, Set<String>> entry : userPostsMap.entrySet()) {
            users.add(new User(entry.getKey(), entry.getValue()));
        }

        return users;
    }

    public static List<String> getRecommendations(List<User> users, String targetUserId) {
        //目标用户和其他用户相似度
        Map<String, Double> similarityScores = new HashMap<>();
        //目标用户赞过的帖子，是包含加密后的图片还是纯文本？
        Set<String> targetUserPosts = new HashSet<>();

        // 找到目标用户
        for (User user : users) {
            if (user.userId.equals(targetUserId)) {
                targetUserPosts = user.likedPosts;
                break;
            }
        }

        // 计算目标用户与所有其他用户的相似度
        for (User user : users) {
            if (!user.userId.equals(targetUserId)) {
                double similarity = calculateSimilarity(targetUserPosts, user.likedPosts);
                similarityScores.put(user.userId, similarity);
            }
        }

        // 找到最相似的用户
        PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>((a, b) -> b.getValue().compareTo(a.getValue()));
        pq.addAll(similarityScores.entrySet());

        // 基于最相似的用户的喜好生成推荐
        Set<String> recommendedPosts = new HashSet<>();
        while (!pq.isEmpty() && recommendedPosts.size() <5 ) {
            Map.Entry<String, Double> entry = pq.poll();
            String similarUserId = entry.getKey();
            for (User user : users) {
                if (user.userId.equals(similarUserId)) {
                    recommendedPosts.addAll(user.likedPosts);
                    break;
                }
            }
        }

        // 移除已经被目标用户喜欢的帖子
        //recommendedPosts.removeAll(targetUserPosts);
        return new ArrayList<>(recommendedPosts);
    }

    private static double calculateSimilarity(Set<String> posts1, Set<String> posts2) {
        // 这里使用的是Jaccard相似度计算，就是交集/并集
        Set<String> intersection = new HashSet<>(posts1);
        intersection.retainAll(posts2);
        Set<String> union = new HashSet<>(posts1);
        union.addAll(posts2);
        return intersection.size() / (double) union.size();
    }
}

