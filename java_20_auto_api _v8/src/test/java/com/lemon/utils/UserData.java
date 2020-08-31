package com.lemon.utils;

import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Katy
 * @date 2020/8/13-21:29
 * 阿甘坚持住学习 加油涨薪！！！！
 */
public class UserData {
    //存储接口响应变量
    public static Map<String,Object> VARS=new HashMap<>();
    //存储默认请求头
    public static HashMap<String,String> DEFAULT_HEADERS=new HashMap<>();

    static {
        //静态代码块:类在加载时自动加载一次本代码
        //静态方法需要手动调用，静态代码块类加载自动执行一次
        DEFAULT_HEADERS.put("Content-Type","application/json");
        DEFAULT_HEADERS.put("X-Lemonban-Media-Type","lemonban.v2");

        //把需要参数化的数据存储到VARS,建立占位符和实际值的关系
        //随机手机号码
        VARS.put("${register_mb}",ChineseMobileNumberGenerator.getInstance().generate());
        VARS.put("${register_pwd}","12345678");
        VARS.put("${amount}","5000");
        //VARS.put("${login_mb}","从数据库查一个知道密码的账号");
        //VARS.put("${login_mb}","从数据库查一个知道密码的账号");
        //${member_id}、是在代码运行中，token自动放到VARS里面的
    }


}
