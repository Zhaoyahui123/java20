package com.lemon.utils;

import com.lemon.pojo.Member;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import javax.management.Query;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Katy
 * @date 2020/8/19-0:21
 * 阿甘坚持住学习 加油涨薪！！！！
 */
public class SQLUtils {
    public static void main(String[] args) throws Exception {

    }

    /**
     * sql查询方法
     * @param sql   sql语句
     * @return
     */

    public static Object getSingleResult(String sql) {
        //处理sql为空的情况，StringUtils来自com.lang3
        if (StringUtils.isBlank(sql)){
            return null;
        }
       //1、定义返回值
        Object result = null;
        try {
            //2、创建DBUtils sql语句操作类
            QueryRunner runner = new QueryRunner();
            //3、获取数据库连接
            Connection conn = JDBCUtils.getConnection();
            //4、创建ScalarHandler,针对单行单列的数据
            ScalarHandler handler=new ScalarHandler<>();
            //5、执行sql语句
            result = runner.query(conn, sql,handler);
            //6、关闭数据库连接
            JDBCUtils.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void scalarHandler() throws SQLException {
        //查询结果数
//        QueryRunner runner = new QueryRunner();
//        String sql = "select count(*) from member limit 5;";
//        Connection conn = JDBCUtils.getConnection();
//        ScalarHandler<Long> handler=new ScalarHandler<>();
//        Long count = runner.query(conn, sql, handler);//ScalarHandler查询结果1不是int类型是Long
//        System.out.println(count);
//        System.out.println(count.getClass());//java中查看变量类型
//        JDBCUtils.close(conn);
        //查询余额
        QueryRunner runner = new QueryRunner();
        String sql = "select leave_amount from member WHERE id=2065275;";
        Connection conn = JDBCUtils.getConnection();
        ScalarHandler<BigDecimal> handler=new ScalarHandler<>();
        BigDecimal amount = runner.query(conn, sql, handler);//ScalarHandler查询结果1不是int类型是Long
        System.out.println(amount);
        System.out.println(amount.getClass());//java中查看变量类型
        JDBCUtils.close(conn);
    }


    private static void beanHandler() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from member WHERE id=2065275;";
        Connection conn = JDBCUtils.getConnection();
        BeanHandler<Member> handler=new BeanHandler<>(Member.class);
        Member member = runner.query(conn, sql, handler);//查询结果是数组，索引取值麻烦，所以用到resultsetHandler结果集处理类
        System.out.println(member);
        System.out.println(member.getId());
        System.out.println(member.getLeave_amount());
        System.out.println(member.getMobile_phone());
        JDBCUtils.close(conn);
    }

    private static void mapHandler() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from member WHERE id=2065275;";
        Connection conn = JDBCUtils.getConnection();
        MapHandler handler = new MapHandler();
        Map<String, Object> query = runner.query(conn, sql, handler);////查询结果是数组，索引取值麻烦，所以用到resultsetHandler结果集处理类
        System.out.println(query);
        JDBCUtils.close(conn);
    }

    private static void insert() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "INSERT into member VALUES(NULL,\"阿甘3\",123456,18841732383,1,1000,NOW());";
        Connection conn = JDBCUtils.getConnection();
        int count = runner.update(conn, sql);
        System.out.println(count);
        JDBCUtils.close(conn);
    }

    private static void update() throws Exception {
        QueryRunner runner=new QueryRunner();
        String sql="update member set leave_amount=100 where id=2065275;";
        Connection conn = JDBCUtils.getConnection();
        int count = runner.update(conn, sql);
        System.out.println(count);
        JDBCUtils.close(conn);
    }
}
