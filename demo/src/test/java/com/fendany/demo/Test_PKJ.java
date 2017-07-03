package com.fendany.demo;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by moilions on 2017/6/16.
 */
public abstract class Test_PKJ<T> {

    @Autowired
    private T t;

    @Resource
    private String name;

    public abstract boolean setValue(T t);

    class innerClass extends Test_PKJ<Integer> {
        @Override
        public boolean setValue(Integer o) {
            Integer.parseInt(o.toString());

            // map
            Map<?,?> map = new HashMap<>();

            // list
            List<?> list = new CopyOnWriteArrayList<>();

            // set
            Set set = new HashSet();

            return false;
        }
    }

}
