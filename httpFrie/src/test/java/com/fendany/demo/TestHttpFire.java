package com.fendany.demo;

import com.fendany.httpfire.HttpFire;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Lost on 2016/11/2.
 */
public class TestHttpFire {

    @Test
    public void test00() throws IOException {
        HttpFire httpFire = new HttpFire();
        httpFire.invoke();
    }

}
