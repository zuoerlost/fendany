package com.fendany.demo;

import com.fendany.utils.xlsx.ReadExcel;
import org.junit.Test;

/**
 * Created by moilions on 2016/11/11.
 */
public class Testxlsx {

    @Test
    public void test00(){
        ReadExcel readExcel = new ReadExcel();
        readExcel.loadXlsx("/Users/moilions/doc/社商结算平台数据指标管理表_产险客户V1.0.0-20161024.xlsx");
    }

}
