package com.jichibiancheng.bitshare.api;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class UserResult {
    String userId;
    String mailbox;
    File profilePhoto;
    int numberOfUpLoad;
    int numberOfThumbsUp;


    public UserResult(String userId, String mailbox, File profilePhoto, int numberOfUpLoad, int numberOfThumbsUp) {
        this.userId = userId;
        this.mailbox = mailbox;
        this.profilePhoto = profilePhoto;
        this.numberOfUpLoad = numberOfUpLoad;
        this.numberOfThumbsUp = numberOfThumbsUp;
    }


    @Override
    public String toString() {
        return "UserResult{" +
                "userId='" + userId + '\'' +
                ", mailbox='" + mailbox + '\'' +
                ", profilePhoto=" + profilePhoto +
                ", numberOfUpLoad=" + numberOfUpLoad +
                ", numberOfThumbsUp=" + numberOfThumbsUp +
                '}';
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public void setProfilePhoto(File profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setNumberOfUpLoad(int numberOfUpLoad) {
        this.numberOfUpLoad = numberOfUpLoad;
    }

    public void setNumberOfThumbsUp(int numberOfThumbsUp) {
        this.numberOfThumbsUp = numberOfThumbsUp;
    }

    public String getUserId() {
        return userId;
    }

    public String getMailbox() {
        return mailbox;
    }

    public File getProfilePhoto() {
        return profilePhoto;
    }

    public int getNumberOfUpLoad() {
        return numberOfUpLoad;
    }

    public int getNumberOfThumbsUp() {
        return numberOfThumbsUp;
    }
}
