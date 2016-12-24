package com.fendany.web.vo;

/**
 * Created by moilions on 2016/12/24.
 */
public class SecurityVo {

    private String key;

    private String decode;

    private String encode;

    private String info = "KEY使用BASE64编码，长度为【24】，3DES 模式为【DESede】，编码后的密文，使用BASE64编码，以上。";

    private int code;

    private String message;

    public SecurityVo(){

    }

    public SecurityVo(int code , String message){
        this.code = code;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDecode() {
        return decode;
    }

    public void setDecode(String decode) {
        this.decode = decode;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
