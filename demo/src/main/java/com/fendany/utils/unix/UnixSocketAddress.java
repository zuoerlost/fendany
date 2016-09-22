package com.fendany.utils.unix;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ZHANGBAISONG083 on 16-9-22.
 * SOCK 结构体
 */
public class UnixSocketAddress extends Structure {

    public final static byte[] ZERO_BYTE = new byte[]{0};
    public final static int SUN_PATH_SIZE = 108;
    public final static String FAMILY = "family";
    public final static String PATH = "path";

    /**
     * 结构体 类型参数
     */
    public short family = 1;

    /**
     * 结构体 SOCK 文件地址
     */
    public byte[] path = new byte[SUN_PATH_SIZE];

    public void setFamily(short family) {
        this.family = family;
    }

    public void setPath(String sunPath) {
        System.arraycopy(sunPath.getBytes(), 0, this.path, 0, sunPath.length());
        System.arraycopy(ZERO_BYTE, 0, this.path, sunPath.length(), 1);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[]{FAMILY, PATH});
    }

}
