package com.fendany.demo;

import org.junit.Test;

/**
 * Created by zuoer on 16-9-18.
 * <p>
 * Unint32 测试类
 */
public class Test4Unint32 {

    public void test(Uint32 num) {
        System.out.println(num == null ? "null" : num.getValue());
    }

    /**
     * 大端模式
     */
    @Test
    public void test00() {
        byte[] header = new byte[4];
        int length = 99999;
        header[0] = (byte) ((length >>> 24) & 0xff);
        header[1] = (byte) ((length >>> 16) & 0xff);
        header[2] = (byte) ((length >>> 8) & 0xff);
        header[3] = (byte) (length & 0xff);
    }

    @Test
    public void test01() {
        String temp = "111111111asdasd";
        System.out.println(temp.indexOf("a"));
    }

    @Test
    public void test02() {
        Uint32 min = Uint32.MIN_VALUE;
        Uint32 max = Uint32.MAX_VALUE;
        Uint32 a = new Uint32(4294967295L);
        Uint32 b = new Uint32(0);
        Uint32 c = new Uint32(1);

        System.out.println(min.getValue());
        System.out.println(max.getValue());
        System.out.println(a.getValue());
        System.out.println(b.getValue());
        System.out.println(c.getValue());

        test(max);

        System.out.println(a.equals(max));
    }

}
