package com.fendany.doc;

/**
 * Created by zuoer on 16-10-12.
 */
public class DocCommandResult {

    public String out;

    public String httpCode;

    public String httpMessage;

    public String httpMessageType;

    public boolean fix;

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(String httpCode) {
        this.httpCode = httpCode;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(String httpMessage) {
        this.httpMessage = httpMessage;
    }

    public String getHttpMessageType() {
        return httpMessageType;
    }

    private void setHttpMessageType(String httpMessageType) {
        this.httpMessageType = httpMessageType;
    }

    public void setTypeFixLength(){
        setHttpMessageType("FixLength");
    }

    public void setTypeEndFile(){
        setHttpMessageType("EndFile");
    }

    public boolean isFix() {
        return fix;
    }

    public void setFix(boolean fix) {
        this.fix = fix;
    }

    @Override
    public String toString() {
        return "DocCommandResult{" +
                "out='" + out + '\'' +
                ", httpCode='" + httpCode + '\'' +
                ", httpMessage='" + httpMessage + '\'' +
                ", httpMessageType='" + httpMessageType + '\'' +
                '}';
    }
}
