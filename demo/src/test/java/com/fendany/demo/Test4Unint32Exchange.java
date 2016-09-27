package com.fendany.demo;

/**
 * Created by zuoer on 16-9-18.
 */
public class Test4Unint32Exchange {

    private void test00() {

        byte[] tmpByte = {88, 127, 89, 1};

        int s = 0;
        for (int i = 0; i < tmpByte.length; i++) {
            s <<= 8;
            s |= (tmpByte[i] & 0x000000ff);
            System.out.println(s);
        }
    }

}
