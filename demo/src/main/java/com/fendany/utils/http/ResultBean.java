package com.fendany.utils.http;

import java.io.Serializable;

/**
 * Created by Lost on 2016/11/2.
 */
public class ResultBean implements Serializable {

    private boolean success;

    private long begin;

    private long end;

    private long use;

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setUse(long use) {
        this.use = use;
    }

    public long getUse() {
        return use;
    }

    public void setUse() {
        this.use = end - begin;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
        setUse();
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "success=" + success +
                ", begin=" + begin +
                ", end=" + end +
                ", use=" + use +
                "ms , errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
