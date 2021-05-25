package com.jichibiancheng.bitshare.controller;


import com.jichibiancheng.bitshare.api.CommonResult;
import com.jichibiancheng.bitshare.server.DbController;
import com.jichibiancheng.bitshare.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class LikeController {

    //根据上传者和上传文件名，得到文件在服务器上的相对路径
    //此相对路劲即为 fileid
    private String getFileId(String userId, String filename) {
        return userId + "\\" + filename;
    }

    // 点赞文件
    @PostMapping("/home/file/like")
    public CommonResult thumbsUp(@RequestParam("likerId") String likerId, // 点赞者
                                 @RequestParam("hostId") String hostId, // 被点赞者
                                 @RequestParam("fileName") String fileName) throws IOException {

        log.info("post ：/home/file/download  -- 前端传入 likerId:{}, hostId:{}, fileName: {}",
                likerId, hostId, fileName);

        DbController dbController = SpringUtil.getObject(DbController.class);
        CommonResult res = new CommonResult();

        String fileId = getFileId(hostId, fileName);
        if (dbController.thumbsUp(likerId, fileId)) {
            res.setCode(1);
            res.setInfo("点赞文件成功");
        } else {
            res.setCode(0);
            res.setInfo("点赞文件失败, 用户不存在 或 已点赞过该文件！");
        }
        log.info("post ：/home/file/like  -- 后端响应 {}", res);
        return res;
    }

    // 取消点赞文件
    @PostMapping("/home/file/cancel_like")
    public CommonResult cancelThumbsUp(@RequestParam("haterId") String haterId, // 取消点赞者
                                       @RequestParam("hostId") String hostId, // 被取消点赞者
                                       @RequestParam("fileName") String fileName) throws IOException {

        log.info("post ：/home/file/download  -- 前端传入 likerId:{}, hostId:{}, fileName: {}",
                haterId, hostId, fileName);

        DbController dbController = SpringUtil.getObject(DbController.class);
        CommonResult res = new CommonResult();

        String fileId = getFileId(hostId, fileName);

        if (dbController.cancelThumbsUp(haterId, fileId)) {
            res.setCode(1);
            res.setInfo("取消点赞文件成功");
        } else {
            res.setCode(0);
            res.setInfo("取消点赞文件失败, 用户不存在 或 没有点赞该文件！");
        }
        log.info("post ：/home/file/like  -- 后端响应 {}", res);
        return res;
    }

}
