package com.fendany.demo;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zuoer on 16-8-19.
 * JDK8 新特性
 */
public class NewWayJDK8 {

    /**
     * 接口可以支持扩展，方便为之前的代码进行更新
     */
    private interface Formula {

        double calculate(int a);

        default double sqrt(int a) {
            return Math.sqrt(a);
        }
    }

    /**
     * default 不需要重新实现，可直接调用
     */
    @Test
    public void test00() {

        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };
        formula.calculate(100);     // 100.0
        formula.sqrt(16);           // 4.0
    }

    /**
     * 好吧 这个简直不要太嚣张
     */
    @Test
    public void test01() {
        // 之前版本的写法
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });

        // lambda 表达式 -- 常规
        // 多行写法
        Collections.sort(names, (String a, String b) -> {
            System.out.println("000000");
            return b.compareTo(a);
        });

        // lambda 表达式 -- 简单
        Collections.sort(names, (String a, String b) -> b.compareTo(a));

        // lambda 表达式 -- 极简
        Collections.sort(names, (a, b) -> b.compareTo(a));
    }

    /**
     * 超过一个 方法报错
     */
    @FunctionalInterface
    private interface Converter<F, T> {

        T convert(F from);

//        T convert1(F from);

    }

    @Test
    public void test02() {
        Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
        Integer converted = converter.convert("123");
        System.out.println(converted);    // 123
    }

    /**
     * 方法与构造函数引用
     * 代码还可以通过静态方法引用来表示：
     * Java 8 允许你使用 :: 关键字来传递方法或者构造函数引用，上面的代码展示了如何引用一个静态方法，我们也可以引用一个对象的方法：
     */
    @Test
    public void test03() {
        Converter<String, Integer> converter = Integer::valueOf;
        Integer converted = converter.convert("123");
        System.out.println(converted);   // 123
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    static class Person {
        String firstName;
        String lastName;
        String fullName;

        Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }

    /**
     * 只能有一个方法
     * <p>
     * 参数不匹配报错
     */
    interface PersonFactory<P extends Person> {

        P work(String firstName, String lastName);

//        P create(String fullName,String oooo,String ppppp);
    }

    /**
     * 感觉并不会有什么实际的意义
     * 首先在工厂模式中，参数的改变也是有必要的
     */
    @Test
    public void test04() {
        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.work("Peter", "Parker");
        System.out.println(person);
    }

    /**
     * 建议还是都添加final吧，其实是一样的
     */
    @Test
    public void test05() {
        final int num = 1;
        int sum = 2;
        Converter<Integer, String> stringConverter =
                (from) -> String.valueOf(from + num + sum);
        stringConverter.convert(2);     // 3

        //不过这里的num必须不可被后面的代码修改（即隐性的具有final的语义），例如下面的就无法编译：
//        Converter<Integer, String> stringConverter1 =
//                (from) -> String.valueOf(from + num);
//        sum = 3;
    }

}
