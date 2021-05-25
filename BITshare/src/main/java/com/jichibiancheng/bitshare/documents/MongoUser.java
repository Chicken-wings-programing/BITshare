package com.jichibiancheng.bitshare.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// 登录用户
@Document(collection = "MongoUsers")
public class MongoUser {
    @Id
    private String id; // 主键

    private String password;
    private String mailAddress;
    private String profilePhotoId;   // 头像文件路径
    private MultipartFile profilePhoto;  // 除非调用特定函数,否则不加载
    private Set<String> fileIdGroup;  // 用户已上传文件id集, 大小是用户上传过的文件数
    private int numberOfThumbsUp;   //用户被点赞数: 是所有用户上传文件的点赞数之和
    private Set<String> thumbsUpFileIdGroup; // 该用户点赞过的文件id集合
    private String identificationCode;  // 空值证明不能被验证


    public MongoUser() {
        this.id = null;
        this.password = null;
        this.mailAddress = null;
        this.profilePhotoId = null;
        this.fileIdGroup = new HashSet<>();
        this.thumbsUpFileIdGroup = new HashSet<>();
        this.identificationCode = null;
    }

    public MongoUser(String id, String password, String mailAddress) {
        this.id = id;
        this.password = password;
        this.mailAddress = mailAddress;
        this.profilePhotoId = null;
        this.numberOfThumbsUp = 0;
        this.fileIdGroup = new HashSet<>();
        this.thumbsUpFileIdGroup = new HashSet<>();
        this.identificationCode = null;
    }

    @Override
    public String toString() {
        return "MongoUser{" +
                "id='" + id + '\'' + '}';
    }

    //新增点赞
    public void addNumberOfThumbsUpTimes() {
        this.setNumberOfThumbsUp(numberOfThumbsUp + 1);
    }

    //减少点赞
    public void decNumberOfThumbsUpTimes() {
        this.setNumberOfThumbsUp(numberOfThumbsUp - 1);
    }

    // 添加被点赞文件
    public void addThumbsUpFileIdGroup(String fileId) {
        this.thumbsUpFileIdGroup.add(fileId);
    }
    // getters and setters.

    public Set<String> getThumbsUpFileIdGroup() {
        return thumbsUpFileIdGroup;
    }

    public void setThumbsUpFileIdGroup(Set<String> thumbsUpFileIdGroup) {
        this.thumbsUpFileIdGroup = thumbsUpFileIdGroup;
    }

    public String getId() {
        return id;
    }

    public int getNumberOfThumbsUp() {
        return numberOfThumbsUp;
    }

    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }

    public void setNumberOfThumbsUp(int numberOfThumbsUp) {
        this.numberOfThumbsUp = numberOfThumbsUp;
    }

    public MultipartFile getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(MultipartFile profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhotoId() {
        return profilePhotoId;
    }

    public void setProfilePhotoId(String profilePhotoId) {
        this.profilePhotoId = profilePhotoId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public Set<String> getFileIdGroup() {
        return fileIdGroup;
    }

    public void setFileIdGroup(Set<String> fileIdGroup) {
        this.fileIdGroup = fileIdGroup;
    }

    public String getIdentificationCode() {
        return identificationCode;
    }

    // generating random string with given length
    public void setIdentificationCode(int length) {
        if (length > 0) {
            String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length; i++) {
                int number = random.nextInt(62);
                sb.append(str.charAt(number));
            }
            this.identificationCode = sb.toString();
        } else
            this.identificationCode = null;
    }

    // 获取用户上传的文件数
    public Integer getUpLoadFileNum() {
        return fileIdGroup.size();
    }

    // 插入新的用户文件id
    public Boolean addFileId(String fileId) {
        return this.fileIdGroup.add(fileId);
    }

    // 删除用户文件id
    public void delFileId(String fileId) {
        this.fileIdGroup.remove(fileId);
    }

    public void delThumbsUpFileIdGroup(String fileId) {
        this.thumbsUpFileIdGroup.remove(fileId);
    }
}
