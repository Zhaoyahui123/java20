package com.lemon.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import sun.awt.windows.WPrinterJob;

/**
 * @author Katy
 * CaseId	Name	Url	Type
 */
public class API {
    @Excel(name="CaseId")
    private int id;
    @Excel(name="Name")
    private String name;
    @Excel(name="Url")
    private String url;
    @Excel(name="Type")
    private  String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public API() {
    }

    @Override
    public String toString() {
        return "API{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
