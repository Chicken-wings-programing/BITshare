package com.jichibiancheng.bitshare.api;

import java.util.Date;

public class FileResult {
    private String filename;
    private long fileSizeInKB;
    private String uploadUserId;
    private Date uploadTime;
    private Integer downloadTimes;
    private Integer thumbsUpTimes;
    private Boolean isThumbsUp = false;   //针对当前用户判断：该文件是否被点赞

    public FileResult() {
    }

    public FileResult(String filename, long fileSizeInKB, String uploadUserId, Date uploadTime, Integer downloadTimes, Integer thumbsUpTimes, Boolean isThumbsUp) {
        this.filename = filename;
        this.fileSizeInKB = fileSizeInKB;
        this.uploadUserId = uploadUserId;
        this.uploadTime = uploadTime;
        this.downloadTimes = downloadTimes;
        this.thumbsUpTimes = thumbsUpTimes;
        this.isThumbsUp = isThumbsUp;
    }

    public long getFileSizeInKB() {
        return fileSizeInKB;
    }

    public void setFileSizeInKB(long fileSizeInKB) {
        this.fileSizeInKB = fileSizeInKB;
    }

    public Integer getDownloadTimes() {
        return downloadTimes;
    }

    public void setDownloadTimes(Integer downloadTimes) {
        this.downloadTimes = downloadTimes;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getThumbsUpTimes() {
        return thumbsUpTimes;
    }

    public void setThumbsUpTimes(Integer thumbsUpTimes) {
        this.thumbsUpTimes = thumbsUpTimes;
    }

    public Boolean getThumbsUp() {
        return isThumbsUp;
    }

    public void setThumbsUp(Boolean thumbsUp) {
        isThumbsUp = thumbsUp;
    }
}
