package com.lemon.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 以后用到，需要改的就是url、user、password这三个值~~
 * @param <piblic>
 */
public class JDBCUtils<piblic> {
    public static Connection getConnection(){
        //定义数据库连接
        //jdbc:oracle:thin:@127.0.0.1:1521:XE
        //jdbc:sqlserver://localhost:1433;DatabaseName=Java
        String url=Constans.JDBC_URL;
        String user=Constans.JDBC_USERNAME;
        String password=Constans.JDBC_PASSWORD;
        //定义数据库连接对象
        Connection conn=null;
        try{
            //你导入的数据库驱动包，mysql
            conn= DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;

    }

    public static void close(Connection conn){
        if (conn !=null){
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
