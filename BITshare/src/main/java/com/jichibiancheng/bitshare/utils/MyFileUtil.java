package com.jichibiancheng.bitshare.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class MyFileUtil {

    //根据上传者和上传文件名，得到文件在服务器上的相对路径
    //此相对路劲即为 fileid
    public static String getFileId(String userId, String filename) {
        return userId + "\\" + filename;
    }

    //文件格式转换
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downFile(HttpServletRequest request, HttpServletResponse response, File downFile) {
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
