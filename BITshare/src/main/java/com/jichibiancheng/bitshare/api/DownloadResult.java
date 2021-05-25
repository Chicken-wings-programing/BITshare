package com.jichibiancheng.bitshare.api;

import java.io.File;

public class DownloadResult {
    private int code;
    private String info;
    private File file;
    private long fileSizeInKB;

    public long getFileSizeInKB() {
        return fileSizeInKB;
    }

    public void setFileSizeInKB(long fileSizeInKB) {
        this.fileSizeInKB = fileSizeInKB;
    }

    public DownloadResult(int code, String info, File file) {
        this.code = code;
        this.info = info;
        this.file = file;
    }


    public DownloadResult() {
    }

    @Override
    public String toString() {
        return "DownloadResult{" +
                "code=" + code +
                ", info='" + info + '\'' +
                ", file=" + file +
                ", fileSizeInKB=" + fileSizeInKB +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
