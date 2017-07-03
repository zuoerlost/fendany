package com.fendany.demo;

/**
 * Created by moilions on 2017/3/30.
 *
 * 1. 泛型
 * 2. 内部类
 * 3. 继承外部类
 * 4. 抽象方法
 * 5. 实现抽象方法
 * 6. 调用外部类属性
 *
 */
public abstract class TesTLDD<T> {

    protected String name;

    public abstract T test00();

    private class TestIn extends TesTLDD<String> {

        @Override
        public String test00() {
            return name;
        }
    }

}
