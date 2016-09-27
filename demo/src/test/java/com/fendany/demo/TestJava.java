package com.fendany.demo;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

/**
 * Created by zuoer on 16-9-27.
 */
public class TestJava {

    @Test
    public void test00(){
        String temp = ",,,,,,,,,a,b,c,,,,,,";
        System.out.println(ArrayUtils.toString(temp.split(",")));
    }
}
