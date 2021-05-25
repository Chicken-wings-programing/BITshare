package com.jichibiancheng.bitshare.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

// 上传文件
@Document(collection = "UploadFiles")
public class UploadFile {
    @Id
    private String fileId; //主键: 亦为文件路径

    private Integer downloadTimes;
    private Integer thumbsUpTimes; // 文件点赞数
    private long fileSize; // 单位: Byte
    private String uploadUserId;
    private Date uploadTime;
    private MultipartFile multipartFile; // 不会存入数据库


    public UploadFile() {
        this.fileId = null;
        this.downloadTimes = 0;
        this.thumbsUpTimes = 0;
        this.fileSize = 0;
        this.uploadUserId = null;
        this.uploadTime = null;
    }

    public UploadFile(String fileId, MultipartFile file, String uploadUserId) {
        this.fileId = fileId;
        this.downloadTimes = 0;
        this.thumbsUpTimes = 0;
        this.fileSize = file.getSize();
        this.uploadUserId = uploadUserId;
        this.uploadTime = new Date();
    }

    //新增下载
    public void addDownloadTimes() {
        this.setDownloadTimes(downloadTimes + 1);
    }

    //新增点赞
    public void addThumbsUpTimes() {
        this.setThumbsUpTimes(thumbsUpTimes + 1);
    }

    //减少点赞
    public void decThumbsUpTimes() {
        this.setThumbsUpTimes(thumbsUpTimes - 1);
    }

    //getter and setter
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public Integer getDownloadTimes() {
        return downloadTimes;
    }

    public void setDownloadTimes(Integer downloadTimes) {
        this.downloadTimes = downloadTimes;
    }

    public Integer getThumbsUpTimes() {
        return thumbsUpTimes;
    }

    public void setThumbsUpTimes(Integer thumbsUpTimes) {
        this.thumbsUpTimes = thumbsUpTimes;
    }

    public long getFileSizeInByte() {
        return fileSize;
    }

    public void setFileSizeInByte(long fileSize) {
        this.fileSize = fileSize;
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

    @Override
    public String toString() {
        return "UploadFile{" +
                "fileId='" + fileId + '\'' +
                ", downloadTimes=" + downloadTimes +
                ", thumbsUpTimes=" + thumbsUpTimes +
                ", fileSizeInKB=" + fileSize +
                ", uploadUserId='" + uploadUserId + '\'' +
                ", uploadTime=" + uploadTime +
                '}';
    }

    public String getFileName() {
        return fileId.substring(fileId.lastIndexOf("\\")+1);
    }
}
