package com.lemon.utils;

import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;

/**
 * @author Katy
 * @date 2020/8/27-14:22
 * 阿甘坚持住学习 加油涨薪！！！！
 */
public class Constans {
    //定义静态常量快捷键 psfi;  final修饰变量，变量成为常量，常量只能赋值一次
   //响应数据回写列号
    public static final int RESPONSE_CELL_NUM=8;
    //断言回写列号
    public static final int ASSERT_CELL_NUM=10;
    //用例文件地址
    public static final String EXCEL_PATH="src/test/resources/cases_v3.xlsx";
    //断言成功
    public static final String ASSERT_SUCCESS="PASSED";
    //断言失败
    public static final String ASSERT_FAILED="FAILED";

    //定义数据库连接
    //jdbc:oracle:thin:@127.0.0.1:1521:XE
    //jdbc:sqlserver://localhost:1433;DatabaseName=Java
    public static final String JDBC_URL ="jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    public static final String JDBC_USERNAME ="future";
    public static final String JDBC_PASSWORD ="123456";

    //

}
