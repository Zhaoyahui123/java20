package com.lemon.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.Constans;
import com.lemon.utils.ExcelUtils_finally;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.UserData;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Katy
 * @date 2020/8/11-17:20
 * 多接口时，比如之前只有注册接口，现在要登录接口，直接复制注册的代码，
         * 1、改下sheet是第几个
         * 2、改下ExcelUtil中的用例文件路径
 * 怎么把注册和登录用例一起执行呢？？用testng.xml
 */
public class LoginCase extends BaseCase{


    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) throws Exception {
//  * 1、参数化替换:真实的手机号码等替换占位符
        paramsReplace(caseInfo);
//  * 2、数据库前置查询结果（数据断言必须在接口执行前后都查询）
//  * 3、调用接口
        //执行登录接口
        String responseBody=HttpUtils.call(caseInfo,UserData.DEFAULT_HEADERS);
        //jsonpath 从响应体中取出token和memberId
        getParamsInUserData(responseBody,"$.data.token_info.token","${token}");
        getParamsInUserData(responseBody,"$.data.id","${member_id}");
//  * 4、断言响应结果
        boolean responseAssertFlag = responseAssert(caseInfo.getExpectedResult(), responseBody);
//  * 5、添加接口响应回写内容
        //添加响应回写内容
        addWriteBackData(sheetIndex,caseInfo.getId(), Constans.RESPONSE_CELL_NUM,responseBody);
//  * 6、数据库后置查询结果
//  * 7、数据库断言
//  * 8、添加断言回写内容
        String assertResult=responseAssertFlag ? Constans.ASSERT_SUCCESS:Constans.ASSERT_FAILED;
        addWriteBackData(sheetIndex,caseInfo.getId(),Constans.ASSERT_CELL_NUM,assertResult);
//  * 9、添加日志

//  * 10、报表断言  断言失败应该在报表中体现
        //testng手机信息--》传给allure--》展示
//        Assert.assertEquals(assertResult,Constans.ASSERT_SUCCESS);
//      注释

    }


    @DataProvider
    public Object[] datas() throws Exception {  //改成一维数组
        List list = ExcelUtils_finally.read(sheetIndex, 1, CaseInfo.class);
        return list.toArray();//集合转数组 就用。toArray
    }

}
