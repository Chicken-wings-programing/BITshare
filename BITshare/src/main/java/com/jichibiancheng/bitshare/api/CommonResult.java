package com.jichibiancheng.bitshare.api;

public class CommonResult {
    private int code;
    private String info;

    public CommonResult() { }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "code=" + code +
                ", info='" + info + '\'' +
                '}';
    }
}
