package com.lemon.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.ExcelUtils_finally;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.UserData;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Katy
 * @date 2020/8/15-14:57
 * 阿甘坚持住学习 加油涨薪！！！！
 */
public class BaseCase {

    private static Logger logger=Logger.getLogger(BaseCase.class);

    //获取testng.xml中sheetIndex
    public int sheetIndex;

    @BeforeClass
    @Parameters({"sheetIndex"})
    public void beforeClass(int sheetIndex){
        this.sheetIndex=sheetIndex;
    }

    @AfterSuite
    public void finish() throws Exception {
        logger.info("============finish========");
        ExcelUtils_finally.batchWrite();
        //删除垃圾数据：通过UserDate中VARS获取的数据去删除

    }

    /**
     * 从 responseBody 通过jsonpath取出对应的参数，存入到UserData.Vars中
     * @param responseBody          接口响应json字符串
     * @param jsonPathExpression    jsonpath表达式
     * @param userDataKey           VARS中的key
     */
    public void getParamsInUserData(String responseBody,String jsonPathExpression,String userDataKey) {
        Object userDataValue = JSONPath.read(responseBody, jsonPathExpression);
        System.out.println("userDataKey:"+userDataKey);
        System.out.println("userDataValue:"+userDataValue);
        //存储到 VARS中
        if (userDataValue!=null){
            UserData.VARS.put(userDataKey,userDataValue);
        }
    }

    /**
     * 获取鉴权头，并且加入默认请求头，并且返回
     * @return
     */
    public HashMap<String, String> getAuthorizationHeader() {
        //从用户变量中取出登录接口存入的token值
        Object token = UserData.VARS.get("${token}");
        //Authorization Bearer  ${token_value}
        //新定义一个请求头：鉴权头+默认头
        HashMap<String,String> headers=new HashMap<>();
        //添加鉴权头
        headers.put("Authorization","Bearer "+ token );
        //添加默认头
        headers.putAll(UserData.DEFAULT_HEADERS);
        return headers;
    }

    /**
     * 接口响应断言
     * @param expectedResult        断言期望值
     * @param responseBody          接口响应内容
     * @return                      接口响应断言结果
     */
    public boolean responseAssert(String expectedResult, String responseBody) {
        //expectedResult转成Map,因为期望结果可以有多个key值
        Map<String,Object> map = JSONObject.parseObject(expectedResult, Map.class);//第二个类型为object就可以不管value是什么类型啦
        Set<String> keySet = map.keySet();
        boolean responseAssertFlag=true;
        for (String actualValueExpression : keySet) {
            //获取了期望值
            Object expectesValue = map.get(actualValueExpression);
            //通过表达式从响应体获取实际值
            Object actualValue = JSONPath.read(responseBody, actualValueExpression);
            //断言，只要失败一次，整个断言就失败
            if (!expectesValue.equals(actualValue)){
                //断言失败
                responseAssertFlag=false;
                break;
            }
        }
        logger.info("接口响应断言结果:"+responseAssertFlag);
        return responseAssertFlag;
    }

    /**
     * 添加回写对象到回写集合中
     * @param sheetIndex  sheetIndex
     * @param rowNum         行号
     * @param cellNum        列号
     * @param content   回写内容
     */
    public void addWriteBackData(int sheetIndex,int rowNum,int cellNum,String content) {
        //响应回写
        WriteBackData wdb =new WriteBackData(sheetIndex,rowNum,cellNum,content);
        //批量回写，存储到一个List集合
        ExcelUtils_finally.wbdList.add(wdb);
    }

    /**
     * 参数化替换
     * @param caseInfo
     */
    @Step("参数化")
    public void paramsReplace(CaseInfo caseInfo) {
        //sql:select count(*) from member a where a.mobile_phone = '${register_pwd}';
        //params:{"mobile_phone":"${register_mb}","pwd":"${register_pwd}"}
        //1、取出需要替换的字段
        String params = caseInfo.getParams();
        String sql = caseInfo.getSql();
        String expectedResult = caseInfo.getExpectedResult();
        String url = caseInfo.getUrl();
        //2、获取所有的占位符和实际值
        Set<String> keySet = UserData.VARS.keySet();
        //3、遍历所有的占位符
        for (String placeHolder : keySet) {
            //4、取出实际值
            String value = UserData.VARS.get(placeHolder).toString();
            if (StringUtils.isNotBlank(params)) {
                //实际值value替换占位符placeHolder
                params = params.replace(placeHolder,value);
            }
            if (StringUtils.isNotBlank(sql)) {
                //实际值value替换占位符placeHolder
                sql = sql.replace(placeHolder,value);
            }
            if (StringUtils.isNotBlank(expectedResult)) {
                //实际值value替换占位符placeHolder
                expectedResult = expectedResult.replace(placeHolder,value);
            }
            if (StringUtils.isNotBlank(url)) {
                //实际值value替换占位符placeHolder
                url = url.replace(placeHolder,value);
            }
        }
        //替换后重新写回表格
        caseInfo.setParams(params);
        caseInfo.setSql(sql);
        caseInfo.setExpectedResult(expectedResult);
        caseInfo.setUrl(url);
//        logger.info(caseInfo);
    }



}
