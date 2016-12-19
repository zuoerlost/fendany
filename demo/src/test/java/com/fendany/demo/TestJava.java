package com.fendany.demo;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuoer on 16-9-27.
 */
public class TestJava {

    @Test
    public void test00() {
        String temp = ",,,,,,,,,a,b,c,,,,,,";
        System.out.println(ArrayUtils.toString(temp.split(",")));
    }


    @Test
    public void test01() {

        List<String> list1 = new ArrayList<>();
        list1.add("a");
        list1.add("b");
        List<String> list2 = new ArrayList<>();
        list2.add("a");
        list2.add("b");
        list2.add("c");

        for (String temp : list2) {
            if (!list1.contains(temp)) {
                System.out.println(temp);
            }
        }


        List<emp> list1_ = new ArrayList<>();
        list1_.add(new emp("a"));
        list1_.add(new emp("b"));
        List<emp> list2_ = new ArrayList<>();
        list2_.add(new emp("a"));
        list2_.add(new emp("b"));
        list2_.add(new emp("c"));
        List<String> list3 = new ArrayList<>();
        for (emp e : list1_) {
            list3.add(e.getX());
        }

        List<emp> result = new ArrayList<>();
        for (emp e : list2_) {
            String temp = e.getX();
            if (temp != null && !list3.contains(temp) ) {
                result.add(e);
            }
        }

        System.out.println(result);
    }


    private class emp {

        private String x;

        public emp(String name) {
            x = name;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        @Override
        public String toString() {
            return "emp{" +
                    "x='" + x + '\'' +
                    '}';
        }
    }
}
