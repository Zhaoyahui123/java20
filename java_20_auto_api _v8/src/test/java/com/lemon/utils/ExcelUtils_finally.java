package com.lemon.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.pojo.API;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 将ExcelUtils类 简化下 将sheetIndex,sheetNum,API或者CaseInfo类抽取成Class clazz
 */

    public class ExcelUtils_finally {
    //批量回写，存储到一个List集合
    public static List<WriteBackData> wbdList = new ArrayList<>();


    public static void main(String[] args)  throws Exception{
        List<CaseInfo> list = read(0, 1, CaseInfo.class);
        List<API> list2 = read(1, 1, API.class);
        for (CaseInfo caseInfo : list) {
            System.out.println(caseInfo);
        }
        System.out.println("=========================");
        for (API api : list2) {
            System.out.println(api);
        }
    }

    /**
     * 读取excel数据并封装到指定对象中
     * @param sheetIndex   开始sheet索引
     * @param sheetNum      sheet个数
     * @param clazz         excel映射类字节对象
     * @return
     */
    public static List read(int sheetIndex,int sheetNum,Class clazz) throws Exception {
        //easypoi
        //1、excel文件流
        FileInputStream fis=new FileInputStream(Constans.EXCEL_PATH);
        //2、easypoi的导入参数
        ImportParams params=new ImportParams();//该ImportParams方法可以直接跳过表头，默认只读一个 默认读第一个sheet
        params.setStartSheetIndex(sheetIndex);//设置从第几个sheet开始读
        params.setSheetNum(sheetNum);//设置读几个sheet
        //3、导入 importExcel(第二中3个参数 execel文件流、映射关系字节码对象、导入参数)
        List apiList = ExcelImportUtil.importExcel(fis, clazz, params);//List<API> apiList 泛型去掉
        //4、关流(也可以不关)
        fis.close();
        return apiList;
    }

    public static void batchWrite() throws Exception {
        //回写的逻辑：变量wbdList集合，取出sheetIndex,rowNum,cellNum,content
        FileInputStream fis=new FileInputStream(Constans.EXCEL_PATH);
        //获取所有的sheet
        Workbook sheets = WorkbookFactory.create(fis);
        //循环wbdList集合
        for (WriteBackData wbd : wbdList) {
            int sheetIndex=wbd.getSheetIndex();
            int rowNum=wbd.getRowNum();
            int cellNum=wbd.getCellNum();
            String content=wbd.getContent();
            //获取对应的sheet对象
            Sheet sheet=sheets.getSheetAt(sheetIndex);
            //获取对象的Row对象
            Row row =sheet.getRow(rowNum);
            //获取对象的cell对象
            Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            //回写内容
            cell.setCellValue(content);
        }
        //回写到excel文件中
        FileOutputStream fos=new FileOutputStream(Constans.EXCEL_PATH);
        sheets.write(fos);
        fis.close();
        fos.close();
    }
}

