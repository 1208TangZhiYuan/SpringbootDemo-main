package com.zhulang.xfxhsimple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component



public class Chat {

    public void startChat(String username, Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎 " + username + " 进入聊天系统！");
        while (true) {
            System.out.print("请输入消息 ('q' 退出): ");
            String message = scanner.nextLine();
            if (message.equals("q")) {
                break;
            }
            // 插入聊天记录
            insertChatLogIntoDatabase(connection, username, message);
            System.out.println("消息已发送！");
            // 调用后端AI接口获取回复
            String aiResponse = getAIResponse(message);
            System.out.println("AI回复: " + aiResponse);
        }
    }

    private void insertChatLogIntoDatabase(Connection connection, String username, String message) {
        try {
            String insertChatLogQuery = "INSERT INTO chat_logs (username, message, timestamp) VALUES (?, ?, ?)";
            PreparedStatement insertChatLogStatement = connection.prepareStatement(insertChatLogQuery);
            insertChatLogStatement.setString(1, username);
            insertChatLogStatement.setString(2, message);
            insertChatLogStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            insertChatLogStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getAIResponse(String input) {
        try {
            String apiUrl = "http://localhost:8080/test/sendQuestion?question=hello" + URLEncoder.encode(input, "UTF-8");
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 获取API响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); // 设置字符编码为UTF-8
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "无法获取AI回复";
        }
    }

}
