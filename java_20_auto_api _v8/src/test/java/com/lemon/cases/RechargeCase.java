package com.lemon.cases;

import com.alibaba.fastjson.JSONPath;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 充值接口测试类型
 */

public class RechargeCase extends BaseCase{

    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) throws Exception {
//  * 1、参数化替换
        paramsReplace(caseInfo);
//  * 2、数据库前置查询结果（数据断言必须在接口执行前后都查询）
        BigDecimal beforeSQLResult = (BigDecimal) SQLUtils.getSingleResult(caseInfo.getSql());

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
        BigDecimal afterSQLResult = (BigDecimal)SQLUtils.getSingleResult(caseInfo.getSql());
//  * 7、数据库断言
        //afterSQLResult - beforeSQLResult = 参数中的amount
        //用jsonPath取出参数中的amount   46:38 实战9
        boolean sqlAssertFlag= sqlAssert(caseInfo, beforeSQLResult, afterSQLResult);
//  * 8、添加断言回写内容
        String assertResult=responseAssertFlag ? Constans.ASSERT_SUCCESS:Constans.ASSERT_FAILED;
        addWriteBackData(sheetIndex,caseInfo.getId(),Constans.ASSERT_CELL_NUM,assertResult);
//  * 9、添加日志
//  * 10、报表断言
        Assert.assertEquals(assertResult,Constans.ASSERT_SUCCESS);
    }


    /**
     * 数据库断言
     * @param caseInfo
     * @param beforeSQLResult
     * @param afterSQLResult
     */
    public boolean sqlAssert(CaseInfo caseInfo, BigDecimal beforeSQLResult, BigDecimal afterSQLResult) {
        boolean flag=false;
        if (StringUtils.isNotBlank(caseInfo.getSql())) {
            String amountStr = JSONPath.read(caseInfo.getParams(), "$.amount").toString();
            //String--->BigDecimal,用构造函数解决，大的小数类型，小数位多的时候不会有数据的损失
            //把字符串类型的amountStr转成BigDecimal,BigDecimal同类型之间进行运算
            BigDecimal amount = new BigDecimal(amountStr);
            //afterSQLResult-beforeSQLResult
            BigDecimal subtractResult = afterSQLResult.subtract(beforeSQLResult);
            //subtractResult.compareTo(amount)==0 说明subtractResult==amount
            System.out.println("beforeSQLResult：" + beforeSQLResult);
            System.out.println("afterSQLResult：" + afterSQLResult);
            System.out.println("subtractResult：" + subtractResult);
            System.out.println("amount：" + amount);
            //equal不能处理1.00==1，所以用compareTo:相等等于0，<等于-1，>等于1
            if (subtractResult.compareTo(amount) == 0) {    //
                System.out.println("数据库断言成功");
                flag=true;
            } else {
                System.out.println("数据库断言失败");
            }
        }
        return flag;
    }


    @DataProvider
        public Object[] datas() throws Exception {  //改成一维数组
            List<CaseInfo> list = ExcelUtils_finally.read(sheetIndex, 1, CaseInfo.class);
            return list.toArray();//集合转数组 就用。toArray
        }
}
