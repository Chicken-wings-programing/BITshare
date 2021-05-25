package com.jichibiancheng.bitshare;

import com.jichibiancheng.bitshare.server.DbController;
import com.jichibiancheng.bitshare.documents.UploadFile;
import com.jichibiancheng.bitshare.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
@Slf4j
public class FileTest {

    @Test
    void upLoad() throws IOException {
        DbController dbController = SpringUtil.getObject(DbController.class);

        /* 文件上传测试 */

        File file = new File("d:\\README.md");
        FileInputStream in_file = new FileInputStream(file);
        // 转 MultipartFile
        MockMultipartFile multipartFile
                = new MockMultipartFile("file",
                file.getName(),
                "text/plain", in_file);

        //上传
        dbController.upload("nihaoya", multipartFile);
        log.info("upload success");
    }

    @Test
    void downLoad() throws IOException {
        DbController dbController = SpringUtil.getObject(DbController.class);

        /* 文件下载测试 */
//        UploadFile uploadFile = dbController.download("nihaoya\\README.md");
//        if(uploadFile!=null)
//            log.info("download success：" + uploadFile.toString());
        File file = dbController.download("nihaoya\\README.md");
        if (file != null)
            log.info("download success：" + file.toString());
    }

    @Test
    void commonService() throws IOException {
        DbController dbController = SpringUtil.getObject(DbController.class);

        /* 公共服务测试 */
        System.out.println(dbController.findAllFileId());
        var fileList = dbController.findAllFile();
        for (UploadFile file : fileList) {
            System.out.println(file.getUploadUserId());
        }
    }

    @Test
    void deleteUploadedFile() throws IOException {
        DbController dbController = SpringUtil.getObject(DbController.class);

        /*删除已上传文件测试*/
        if (dbController.deleteUploadedFile("nihaoya\\README.md"))
            log.info("delete file success");
        else
            log.info("delete file failed");
    }

}
