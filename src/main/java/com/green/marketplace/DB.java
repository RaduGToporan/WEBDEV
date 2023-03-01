package com.green.marketplace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.security.crypto.scrypt.*;

public class DB {
    public static void main(String[] args) {
        String SQL_SELECT = "Select * from users";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/marketplace", "webdev", "ai_marketplace")) {

            if (conn != null) {
                System.out.println("Connected to the database!");
                PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);

                ResultSet resultSet = preparedStatement.executeQuery();

                Scanner sc=new Scanner(System.in);  

                System.out.print("Username: ");
                String u = sc.nextLine();
                
                System.out.print("Password: ");
                String p = sc.nextLine();

                sc.close();

                boolean loggedIn = false;
                boolean userFound = false;

                SCryptPasswordEncoder s = new SCryptPasswordEncoder(64, 8, 1, 32, 16);

                while(resultSet.next()){
                    int uid = resultSet.getInt("uid");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    String dob = resultSet.getString("dob");
                    
                    if (u.equals(username)) {
                        userFound = true;
                        if (s.matches(p, password)) {
                            System.out.println("Successfully logged in as " + username);
                            System.out.println();
                            loggedIn = true;
                            System.out.println("ID: " + uid);
                            System.out.println("Username: " + username);
                            System.out.println("E-Mail: " + email);
                            System.out.println("Phone No.: " + phone);
                            System.out.println("DOB: " + dob);
                            System.out.println();
                        } else {
                            System.out.println("Incorrect Password");
                        }
                    }
                }

                if (!userFound) {
                    System.out.println("User not found!");
                }

                if (!loggedIn) {
                    System.out.println("Login attempt failed");
                }

            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
