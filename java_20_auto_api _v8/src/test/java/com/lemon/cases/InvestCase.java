package com.lemon.cases;

import com.lemon.pojo.CaseInfo;
import com.lemon.utils.Constans;
import com.lemon.utils.ExcelUtils_finally;
import com.lemon.utils.HttpUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;


public class InvestCase extends BaseCase{

    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) throws Exception {
//  * 1、参数化替换
        paramsReplace(caseInfo);
//  * 2、数据库前置查询结果（数据断言必须在接口执行前后都查询）

//  * 3、调用接口
        //获取鉴权头+默认头
        HashMap<String, String> authorizationHeader = getAuthorizationHeader();
        String responseBody = HttpUtils.call(caseInfo, authorizationHeader);
//  * 4、断言响应结果
        boolean responseAssertFlag= responseAssert(caseInfo.getExpectedResult(), responseBody);
//  * 5、添加接口响应回写内容
        //添加响应回写内容
        addWriteBackData(sheetIndex,caseInfo.getId(), Constans.RESPONSE_CELL_NUM,responseBody);
//  * 6、数据库后置查询结果
//  * 7、数据库断言

//  * 8、添加断言回写内容
        String assertResult=responseAssertFlag ? Constans.ASSERT_SUCCESS:Constans.ASSERT_FAILED;
        addWriteBackData(sheetIndex,caseInfo.getId(),Constans.ASSERT_CELL_NUM,assertResult);
//  * 9、添加日志

//  * 10、报表断言
        Assert.assertEquals(assertResult,Constans.ASSERT_SUCCESS);
    }



    @DataProvider
        public Object[] datas() throws Exception {  //改成一维数组
            List<CaseInfo> list = ExcelUtils_finally.read(sheetIndex, 1, CaseInfo.class);
            return list.toArray();//集合转数组 就用。toArray
        }
}
