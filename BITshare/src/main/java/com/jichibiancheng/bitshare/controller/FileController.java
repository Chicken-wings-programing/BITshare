package com.jichibiancheng.bitshare.controller;

import com.jichibiancheng.bitshare.api.DownloadResult;
import com.jichibiancheng.bitshare.api.CommonResult;
import com.jichibiancheng.bitshare.server.DbController;
import com.jichibiancheng.bitshare.documents.UploadFile;
import com.jichibiancheng.bitshare.utils.MyFileUtil;
import com.jichibiancheng.bitshare.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;


import org.apache.commons.io.FileUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*文件类,操纵文件*/
@Slf4j
@RestController
public class FileController {

    // 上传文件
    @PostMapping("/home/file/upload")
    public CommonResult upload(@RequestParam("userId") String userId,
                               @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
        log.info("post ：/home/file/upload  -- 前端传入 userId: {}, file: {}",
                userId, multipartFile);

        DbController dbController = SpringUtil.getObject(DbController.class);
        CommonResult res = new CommonResult();

        if (dbController.upload(userId, multipartFile)) {
            res.setCode(1);
            res.setInfo("上传文件成功");
        } else {
            res.setCode(0);
            res.setInfo("上传文件失败,用户不存在！");
        }

        log.info("post ：/home/file/upload  -- 后端响应 {}", res);
        return res;
    }

    // 下载文件: url不能出现'\'所以还是传入用户id和文件名
    @GetMapping("/home/file/download")
    public DownloadResult download(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("hostId") String hostId,
            @RequestParam("fileName") String fileName) throws Exception {

        log.info("post ：/home/file/download  -- 前端传入 hostId:{}, fileName: {}",
                hostId, fileName);

        DbController dbController = SpringUtil.getObject(DbController.class);
        DownloadResult res = new DownloadResult();

        //生成文件路径
        String fileId = MyFileUtil.getFileId(hostId, fileName);
        //获取文件对象: 从本地获取
        File downFile = dbController.download(fileId);

        if (downFile != null) {
            //返回信息
            res.setCode(1);
            res.setInfo("下载文件成功");
            res.setFile(downFile);
            res.setFileSizeInKB(downFile.length() / 1024);
            //前端下载文件
            MyFileUtil.downFile(request, response, downFile);
        } else {
            res.setCode(0);
            res.setInfo("下载文件失败, 用户不存在！");
            res.setFile(null);
        }

        log.info("post ：/home/file/download  -- 后端响应 {}", res);
        return res;
    }

    //让浏览器跳出方框，下载文件...
    private void downFile(HttpServletRequest request, HttpServletResponse response, File downFile) {
        try {
            String range = null;
            // 特殊头处理
            if (null != request.getHeader("RANGE")) {// 断点续传的头
                range = request.getHeader("RANGE");
            }
            if (null != request.getHeader("Range")) {
                range = request.getHeader("Range");
            }
            response.setContentType("application/x-msdownload");
            int fileLength = Integer.parseInt(Long.toString(downFile.length()));
            response.setContentLength(fileLength);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(downFile.getName().getBytes("gb2312"), "ISO8859-1"));// 处理默认文件名的中文问题
            int startPos = 0;
            if (null != range) {// 断点续传
                startPos = Integer.parseInt(range.replaceAll("bytes=", "").replaceAll("-$|-\\d+$", ""));
            }
            if (startPos == 0) {
                FileCopyUtils.copy(new FileInputStream(downFile), response.getOutputStream());
            } else {// 断点续传
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                if (startPos != 0) {
                    /** 设置Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小] **/
                    StringBuffer sb = new StringBuffer("bytes ");
                    sb.append(Long.toString(startPos));
                    sb.append("-");
                    sb.append(Long.toString(fileLength - 1));
                    sb.append("/");
                    sb.append(Long.toString(fileLength));
                    response.setHeader("Content-Range", sb.toString());
                }
                if (startPos < fileLength) {
                    fileLength = fileLength - startPos;
                    response.getOutputStream().write(FileUtils.readFileToByteArray(downFile), (int) startPos, (int) fileLength);
                }
            }
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
