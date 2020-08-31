package com.lemon.utils;

import com.alibaba.fastjson.JSONObject;
import com.lemon.pojo.CaseInfo;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *发送一个get请求
// * @param url 携带参数的url 2065275
 *            例如：http:/api.lemonban.com/futureloan/member/2065275/info
 *
 */
public class HttpUtils {
    //Logger一定要用log4j的 一定一定不要用错啦！！哪个类里面用日志，就传哪个类
    //把syso换成logger.info()就可以，那个类就复制到哪
    private static Logger logger=Logger.getLogger(HttpUtils.class);


    /**
     * http请求方法
     * @param caseInfo  请求参数
     * @param headers   请求头
     * @return
     * @throws Exception
     */
    public static String call(CaseInfo caseInfo,HashMap<String,String> headers) throws Exception {
       String responseBody="";
        String params=caseInfo.getParams();
        String url=caseInfo.getUrl();
        String method=caseInfo.getMethod();
        //2、判断请求方式，如果是post
        if("post".equalsIgnoreCase(method)){
            String contentType = caseInfo.getContentType();
            //2.1 判断参数，如果是json,
            if ("json".equalsIgnoreCase(contentType)){

                //2.2 判断参数，如果是form,
            }else if("form".equalsIgnoreCase(contentType)){
            //json参数转成key=value参数
                params=jsonStr2KeyValueStr(params);
                //覆盖默认请求头中Content-Type
                headers.put("Content-Type","application/x-www-form-urlencoded");
            }
            responseBody= HttpUtils.post(url, params,headers);

            //3、判断请求方式，如果是get
        }else if("get".equalsIgnoreCase(method)){
            responseBody= HttpUtils.get(url,headers);//get请求头直接加请求后面
            //4、判断请求方式，如果是patch
        }else if("patch".equalsIgnoreCase(method)){
            responseBody= HttpUtils.patch(url, params,headers);
        }
        return responseBody;
    }

    /**
     * json字符串转成key=value
     * 例如：{"mobile_phone":"13877788811","pwd":"12345678"}==》mobile_phone=13877788811&pwd=12345678
//   * @param json字符串
     * @return
     */

    public static String jsonStr2KeyValueStr(String json){
        //json参数转成key=value参数
        //json==>map==>字符串 :json转map 需要在pom.xml文件中导入fastjson 右键generate-dependency--
        Map<String,String> map = JSONObject.parseObject(json, Map.class);
        Set<String> keySet = map.keySet();
        String formParams="";
        for (String key : keySet) {
            //key=value&key=value&
            formParams += key +"="+map.get(key)+"&";
        }
        return formParams.substring(0,formParams.length()-1);
    }


   //工具类的方法一般是静态的，静态的方法的好处就是不需要创建对象
    public static String get(String url,Map<String,String> headers) throws Exception {
        //1、创建请求
        HttpGet get=new HttpGet(url);
        //2、 添加请求头
//        get.setHeader("X-Lemonban-Media-Type","lemonban.v1");
//        get.setHeader("Content-Type","json");
//        get.setHeader("X-Lemonban-Media-Type","lemonban.v1");
        setHeaders(headers, get);
        //3、 创建客户端
        HttpClient client = HttpClients.createDefault();
        //4、发送请求，获取响应结果
        HttpResponse response = client.execute(get);//抛出异常；方
        return printResponse(response);

    }



    /**
     *发送一个post请求
     * @param url               接口地址
     * @param params            接口参数
     * @throws Exception
     */

    public static String post(String url, String params, Map<String,String> headers) throws Exception {
        //1创建请求
        HttpPost post=new HttpPost(url);
        //2 添加请求头
//        post.setHeader("X-Lemonban-Media-Type","lemonban.v1");
//        post.setHeader("Content-Type","application/json");
//        post.setHeader("Content-Type","application/x-www-form-urlencode");//Form的Content-Type:application/x-www-form-urlencod
        //contenttype如果有多种方式时，后面的会覆盖前面的 ；将header作为map传参 相当于把请求方式都put到header里面：
//        headers.put("X-Lemonban-Media-Type","lemonban.v1");
//        headers.put("Content-Type","application/json");
//        。。。。
        setHeaders(headers, post);

        //3 添加请求体（参数）
        StringEntity body=new StringEntity(params,"UTF-8");
        post.setEntity(body);
        //4、 创建客户端
        HttpClient client = HttpClients.createDefault();
        //5、发送请求，获取响应结果
        HttpResponse response = client.execute(post);//抛出异常；方
        //6、格式化响应对象response=响应状态 + 响应头 +响应体
        //6.1响应状态码  链式编程
        return printResponse(response);

    }


    /**
     * 发送一个patch请求
     * @param url
     * @param params
     * @throws Exception
     */
    public static String patch(String url,String params,Map<String,String> headers) throws Exception {
        //1创建请求
        HttpPatch patch=new HttpPatch(url);
        //2 添加请求头
//        patch.setHeader("X-Lemonban-Media-Type","lemonban.v1");
//        patch.setHeader("Content-Type","application/json");
        setHeaders(headers,patch);

        //3 添加请求体（参数）
        StringEntity body=new StringEntity(params,"UTF-8");
        patch.setEntity(body);
        //4、 创建客户端
        HttpClient client = HttpClients.createDefault();
        //5、发送请求，获取响应结果
        HttpResponse response = client.execute(patch);//抛出异常；方
        //6、格式化响应对象response=响应状态 + 响应头 +响应体
        //6.1响应状态码  链式编程
       return  printResponse(response);

    }



    private static String printResponse(HttpResponse response) throws Exception {
        //5、格式化响应对象response=响应状态 + 响应头 +响应体
        //5.1响应状态码  链式编程
        int statusCode = response.getStatusLine().getStatusCode();
//        System.out.println(statusCode);
        logger.info(statusCode);
        //5.2响应头
        Header[] allHeaders = response.getAllHeaders();
        logger.info(Arrays.toString(allHeaders));
        //5.3响应体
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        logger.info(body);
        logger.info("========================");
        return body;
    }

    /**
     * 设置请求头
     * @param headers  包含了请求头的Map集合
     * @param request      请求对象post--->request
     *        HttpPost的父接口叫HttpRequest ,这样不管post还是get都可以使用setHeaders接口啦
     *        ctrl+H 查看类结构
     *        将各种请求方式代码中的第2步//2 添加请求头都改成setHeaders(headers,（对应的请求方式）);
     *        在请求参数上都加上Map<String, String> headers
     *
     *
     */
    public static void setHeaders(Map<String, String> headers, HttpRequest request) {
        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            String value=headers.get(key);
            request.setHeader(key,value);
        }
    }



}
