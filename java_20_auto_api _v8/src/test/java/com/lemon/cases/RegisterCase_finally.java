package com.lemon.cases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import com.alibaba.fastjson.JSONObject;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * easypoi 实现单接口批量执行
 * 注册接口测试类型
 */

/**
 * 1、用文字写出代码流程，从哪个类开始，中间调用哪些类，到哪个类结束。
 *     1、RegisterCase测试类--@Test调用请求类HttpUtils.post,@DataProvider 调用ExcelUtils_finally.read
 *     2、ExcelUtils_finally类 用到映射对象 CaseInfo，读取excel数据并封装到指定对象中，然后封装成read方法
 */
public class RegisterCase_finally extends BaseCase{
    @Test(dataProvider = "datas")

    public void test(CaseInfo caseInfo) throws Exception {
//  * 1、参数化替换
        paramsReplace(caseInfo);
//  * 2、数据库前置查询结果（数据断言必须在接口执行前后都查询）
        Long beforeSQLResult = (Long)SQLUtils.getSingleResult(caseInfo.getSql());

//  * 3、调用接口
        String responseBody = HttpUtils.call(caseInfo, UserData.DEFAULT_HEADERS);
//  * 4、断言响应结果
        boolean responseAssertFlag= responseAssert(caseInfo.getExpectedResult(), responseBody);
//  * 5、添加接口响应回写内容
        //添加响应回写内容
        addWriteBackData(sheetIndex,caseInfo.getId(), Constans.RESPONSE_CELL_NUM,responseBody);
//  * 6、数据库后置查询结果
        Long afterSQLResult = (Long)SQLUtils.getSingleResult(caseInfo.getSql());
//  * 7、数据库断言
        boolean sqlAssertFlag=sqlAssert(caseInfo.getSql(), beforeSQLResult, afterSQLResult);
//  * 8、添加断言回写内容
        String assertResult=responseAssertFlag ? Constans.ASSERT_SUCCESS:Constans.ASSERT_FAILED;
        addWriteBackData(sheetIndex,caseInfo.getId(),Constans.ASSERT_CELL_NUM,assertResult);
//  * 9、添加日志
//  * 10、报表断言
        Assert.assertEquals(assertResult,Constans.ASSERT_SUCCESS);
    }

    /**
     * 数据库断言，因为每个接口的业务逻辑不一样，所以数据库断言方法不能抽取到父类中供其他子类使用
     * @param sql
     * @param beforeSQLResult
     * @param afterSQLResult
     */
    public boolean sqlAssert(String sql, Long beforeSQLResult, Long afterSQLResult) {
        boolean flag=false;
        if (StringUtils.isNotBlank(sql)){
            System.out.println("beforeSQLResult:"+ beforeSQLResult);
            System.out.println("afterSQLResult:"+ afterSQLResult);
            if (beforeSQLResult ==0 && afterSQLResult ==1) {
                System.out.println("数据库断言成功");
                flag=true;
            }else{
                System.out.println("数据库断言失败");
            }
        }
        return flag;
    }

    @DataProvider
        public Object[] datas() throws Exception {  //改成一维数组
            List list = ExcelUtils_finally.read(sheetIndex, 1, CaseInfo.class);
            return list.toArray();//集合转数组 就用。toArray
        }


}
