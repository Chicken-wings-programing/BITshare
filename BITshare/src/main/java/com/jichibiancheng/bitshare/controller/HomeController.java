package com.jichibiancheng.bitshare.controller;

import com.jichibiancheng.bitshare.api.FileResult;
import com.jichibiancheng.bitshare.api.UserResult;
import com.jichibiancheng.bitshare.server.DbController;
import com.jichibiancheng.bitshare.documents.MongoUser;
import com.jichibiancheng.bitshare.documents.UploadFile;
import com.jichibiancheng.bitshare.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class HomeController {

    //获取所有文件
    @GetMapping("/home/file")
    public ArrayList<FileResult> getAllFile(@RequestParam("userId") String userId) {

        log.info("post ：/home/file/upload  -- 前端传入 userId: {}", userId);

        DbController dbController = SpringUtil.getObject(DbController.class);
        ArrayList resList = new ArrayList<FileResult>();

        //找到数据库中的所有文件
        List<UploadFile> allFile = dbController.findAllFile();
        //依次遍历所有文件
        for (UploadFile uploadFile : allFile) {

            //查询该用户是否给该文件点赞了
            Boolean isThumbsUp = dbController.isThumbsUp(userId, uploadFile.getFileId());
            //封装FileResult
            FileResult fileResult = new FileResult(
                    // uploadFile中的multipartFile不会装入数据库
                    uploadFile.getFileName(),
                    uploadFile.getFileSizeInByte() / 1024,
                    uploadFile.getUploadUserId(),
                    uploadFile.getUploadTime(),
                    uploadFile.getDownloadTimes(),
                    uploadFile.getThumbsUpTimes(),
                    isThumbsUp   //
            );

            resList.add(fileResult);
        }

        return resList;
    }

    //获取当前用户
    @GetMapping("/home/user")
    public UserResult getUserInfo(@RequestParam("userId") String uid) {

        UserResult res;

        DbController dbController = SpringUtil.getObject(DbController.class);
        MongoUser user = dbController.getUserWithPhoto(uid);

        if (user != null) {
            res = new UserResult(
                    user.getId(),
                    user.getMailAddress(),
                    dbController.getProfilePhoto(uid),
                    user.getUpLoadFileNum(),
                    user.getNumberOfThumbsUp()
            );
        } else {
            res = null;
        }

        return res;
    }
}
