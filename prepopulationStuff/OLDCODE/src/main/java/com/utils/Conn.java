package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class Conn {
    private final String url = "jdbc:postgresql://testdb-1.c9k0s64g0471.eu-north-1.rds.amazonaws.com:5432/postgres";
    private final String user = "postgres";
    private final String password = "jhyu1982.1";
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            if(conn!=null){
                System.out.println("Connection Established");
            }
            else{
                System.out.println("Connection Failed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
