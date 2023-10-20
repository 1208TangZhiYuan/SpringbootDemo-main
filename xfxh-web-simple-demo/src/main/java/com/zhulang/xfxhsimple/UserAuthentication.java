package com.zhulang.xfxhsimple;

import java.sql.*;
import java.util.Scanner;

public class UserAuthentication {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        ResultSet userResult = null;
        try {
            String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/ai?useUnicode=true&characterEncoding=UTF-8"; // 设置数据库连接的字符编码为UTF-8
            String username = "root";
            String password = "nc7515331";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            while (true) {
                System.out.println("1. 注册");
                System.out.println("2. 登录");
                System.out.print("请选择操作 (1/2): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 清除换行符

                if (choice == 1) {
                    // 注册部分，根据你的需求实现
                    System.out.println("用户注册部分...");
                } else if (choice == 2) {
                    System.out.print("请输入用户名: ");
                    username = scanner.nextLine();
                    System.out.print("请输入密码: ");
                    password = scanner.nextLine();
                    // 查询用户是否存在
                    String selectUserQuery = "SELECT id, chat_count, full_name, gender, id_card FROM users WHERE username = ? AND password = ?";
                    PreparedStatement selectUserStatement = connection.prepareStatement(selectUserQuery);
                    selectUserStatement.setString(1, username);
                    selectUserStatement.setString(2, password);
                    userResult = selectUserStatement.executeQuery();
                    if (userResult.next()) {
                        int userId = userResult.getInt("id");
                        String fullName = userResult.getString("full_name");
                        String gender = userResult.getString("gender");
                        String idCard = userResult.getString("id_card");
                        System.out.println("欢迎 " + fullName + "（性别: " + gender + ", 身份证: " + idCard + "）!");
                        Chat chat = new Chat();
                        chat.startChat(username, connection);
                    } else {
                        System.out.println("登录失败，请检查用户名和密码。");
                    }
                } else {
                    System.out.println("请选择有效的操作 (1/2).");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (userResult != null) {
                try {
                    userResult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
