package com.jichibiancheng.bitshare.server;

import com.jichibiancheng.bitshare.documents.MongoUser;
import com.jichibiancheng.bitshare.documents.UploadFile;
import com.jichibiancheng.bitshare.repository.IMongoUserRepo;
import com.jichibiancheng.bitshare.repository.IUploadFileRepo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class DbController {
    private final com.jichibiancheng.bitshare.repository.IMongoUserRepo IMongoUserRepo;
    private final com.jichibiancheng.bitshare.repository.IUploadFileRepo IUploadFileRepo;
    private final MailController mailController;
    private final String userFileDir = "c:\\userFile";
    private final String profilePhotoName = "profile_photo.png";

    public enum LoginStates {
        userNotExist, passWordNotMatch, LogInSuccess
    }

    public final int codeLength = 4; // 验证码长度

    private CommonsMultipartFile fileToCommonsMultipartFile(File file) {
        return null;
    }

    @Autowired
    public DbController(IMongoUserRepo IMongoUserRepo, IUploadFileRepo IUploadFileRepo, MailController mailController) {
        this.IMongoUserRepo = IMongoUserRepo;
        this.IUploadFileRepo = IUploadFileRepo;
        this.mailController = mailController;
        File dir = new File(userFileDir);
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    /* 基础功能 */
    public MongoUser getUser(String uid) {
        return this.IMongoUserRepo.findById(uid).orElse(null);
    }

    // 获取加载头像的用户
    public MongoUser getUserWithPhoto(String uid) {
        MongoUser mongoUser = getUser(uid);
        mongoUser.setProfilePhoto(fileToCommonsMultipartFile(new File(userFileDir + "\\" + mongoUser.getProfilePhotoId())));
        return mongoUser;
    }

    // 获取用户头像
    public File getProfilePhoto(String uid) {
        MongoUser mongoUser = getUser(uid);
        return new File(userFileDir + "\\" + mongoUser.getProfilePhotoId());
    }

    public UploadFile getFile(String id) {
        return this.IUploadFileRepo.findById(id).orElse(null);
    }

    public void updateUser(MongoUser mongoUser) {
        this.IMongoUserRepo.save(mongoUser);
    }
    public void updateFile(UploadFile uploadFile) {
        this.IUploadFileRepo.save(uploadFile);
    }


    public void insertFile(UploadFile uploadFile) {
        this.IUploadFileRepo.insert(uploadFile);
    }

    public void deleteFile(UploadFile uploadFile) {
        this.IUploadFileRepo.delete(uploadFile);
    }

    public List<MongoUser> getAllUser() {
        return this.IMongoUserRepo.findAll();
    }

    // 在本地创建用户文件夹
    private void createUserDir(String uid) {
        // 找到用户所在的文件夹路径
        File userDir = new File(userFileDir + "\\" + uid);
        if (!userDir.exists()) { // 没有用户文件夹创建用户文件夹
            userDir.mkdir();
        }
    }

    private void loadFileIntoUserFile(MultipartFile multipartFile, String uid, String fileName) throws IOException {
        boolean res = false;
        // 这里一定要使file为绝对路径,否则transferto默认使用Temp目录,会出问题
        File file = new File(new File(userFileDir + "\\" + uid).getAbsolutePath() + "\\" + fileName);
        if (!file.exists())
            res = file.createNewFile();
        multipartFile.transferTo(file);
    }

    // 在本地删除文件
    private Boolean deleteFileInUserFile(String fileId) {
        // 删除本地文件
        File file = new File(userFileDir + "\\" + fileId);
        return file.delete();
    }

    /* 要求的方法 */
    // 1. 登录
    public LoginStates isUserValid(String id, String password) {
        MongoUser mongoUser = getUser(id);
        if (mongoUser != null) // 用户存在
            return mongoUser.getPassword().equals(password) ? LoginStates.LogInSuccess : LoginStates.passWordNotMatch;
        return LoginStates.userNotExist;
    }

    // 2. 注册
    public Boolean insertUser(String uid, String password, String mailAddress, MultipartFile profilePhoto) throws IOException {
        if (getUser(uid) == null) { // 新用户
            MongoUser newUser = new MongoUser(uid, password, mailAddress);
            createUserDir(uid);
            if (profilePhoto != null) { // 头像不为空
                loadFileIntoUserFile(profilePhoto, uid, profilePhotoName);
                newUser.setProfilePhotoId(uid + "\\" + profilePhotoName);
            }
            this.IMongoUserRepo.insert(newUser);
            return true;
        }
        return false;
    }

    // 注册时动态显示：检测用户名是否存在
    public Boolean hasUser(String id) {
        return getUser(id) != null;
    }

    // 2.1 删除用户
    public Boolean deleteUser(String id) {
        MongoUser mongoUser = getUser(id);
        if (mongoUser != null) { // 用户存在
            FileSystemUtils.deleteRecursively(new File(userFileDir + "\\" + id));
        }
        return false; // 用户已不存在
    }

    // 2.2 设置头像
    public Boolean setProfilePhoto(String uid, MultipartFile profilePhoto) throws IOException {
        MongoUser mongoUser = getUser(uid);
        if (mongoUser != null && profilePhoto != null) {
            if (mongoUser.getProfilePhoto() != null) { // 该用户原来有头像,却有设置新头像,则应删掉旧头像
                deleteFileInUserFile(uid + "\\" + profilePhotoName);
            }
            mongoUser.setProfilePhotoId(uid + "\\" + profilePhotoName);
            updateUser(mongoUser);
            loadFileIntoUserFile(profilePhoto, uid, profilePhotoName);
            return true;
        }
        return false;
    }


    // 3. 找回丢失密码.
    public Boolean sendEmail(String id) { // 发送带有验证码的邮件
        MongoUser mongoUser = getUser(id);
        if (mongoUser != null) {
            mongoUser.setIdentificationCode(codeLength);  //为用户生成找回验证码
            //发送验证码给用户邮箱
            if (!mailController.sendIdentificationCode(
                    id,
                    mongoUser.getMailAddress(),
                    mongoUser.getIdentificationCode())) {
                mongoUser.setIdentificationCode(0); // 邮箱不合法,使验证码为空
                return false;
            }
            // 邮箱合法,更新验证码
            updateUser(mongoUser);
            return true;
        }
        //用户不存在或用户邮箱不合法
        return false;
    }

    public Boolean checkCode(String id, String code) {
        MongoUser mongoUser = getUser(id);
        if (mongoUser != null) {
            String correctCode = mongoUser.getIdentificationCode();
            if (correctCode != null && correctCode.equals(code)) {
                mongoUser.setIdentificationCode(0); //code is correct, clear it.
                updateUser(mongoUser); // update change into database.
                return true;
            }
        }
        return false;
    }

    public Boolean updateUserPassword(String id, String password) {
        MongoUser mongoUser = getUser(id);
        if (mongoUser != null) {
            if (password.equals(mongoUser.getPassword()))
                return false; // new password cannot be the same as old one.
            mongoUser.setPassword(password);
            updateUser(mongoUser);
            return true;
        }
        return false;
    }

    // 4. 公共服务
    public List<String> findAllFileId() {
        List<String> fileIds = new ArrayList<>();
        for (MongoUser mongoUser : getAllUser()) {
            fileIds.addAll(mongoUser.getFileIdGroup());
        }
        return fileIds;
    }

    public List<UploadFile> findAllFile() {
        List<UploadFile> files = new LinkedList<>();
        List<String> allFileId = findAllFileId();
        for (String fileId : allFileId) {
            files.add(getFile(fileId));
        }
        return files;
    }

    // 4.1 根据用户id和文件id判断该用户是否给该文件点赞了
    public Boolean isThumbsUp(String uid, String fileId) {
        MongoUser mongoUser = getUser(uid);
        return null != mongoUser && mongoUser.getThumbsUpFileIdGroup().contains(fileId);
    }

    // 5.下载: 返回文件对象
    public File download(String fileId) {
        UploadFile uploadFile = getFile(fileId);
        if (null == uploadFile) {
            log.info("没找到id为{}的文件", fileId);
            return null;
        } else {
            log.info("已找到id为{}的文件", fileId);
            uploadFile.addDownloadTimes();  //下载次数 + 1
            // 更新入数据库
            updateFile(uploadFile);
            // 根据文件id寻找文件再变为CommonsMultiPartFile最后设置进类中
//            uploadFile.setMultipartFile(fileToCommonsMultipartFile(new File(userFileDir + "\\" + uploadFile.getFileId())));
            //直接取出本地文件
            return new File(userFileDir + "\\" + uploadFile.getFileId());
        }
    }

    // 6.上传.
    public Boolean upload(String uid, @NonNull MultipartFile multipartFile) throws IOException {
        MongoUser user = getUser(uid);
        if (user != null) {

            /* 处理文件名 */
            // 若文件名有空格则替换掉空格.
            String fileId = uid + "\\" + Objects.requireNonNull(
                    multipartFile.getOriginalFilename()).replaceAll(" ", "");

            String former = fileId.substring(
                    0,
                    fileId.lastIndexOf('.')),
                    latter = fileId.substring(fileId.lastIndexOf('.'));

            int version = 2;
            //如果同名，在文件名后加合适的版本号
            while (!user.addFileId(fileId)) {
                fileId = former + "-" + version + latter;  //生成新的文件名
                version++;
            }

            /* 存储文件信息 */
            // 向数据库写入文件路径
            UploadFile uploadFile = new UploadFile(fileId, multipartFile, uid);
            insertFile(uploadFile);

            // 向本地写入文件
            loadFileIntoUserFile(multipartFile, uid, uploadFile.getFileName());

            // 更新用户
            updateUser(user);

            log.info("用户{} 上传文件id{}成功.", user, fileId);
            return true;
        }
        log.info("上传文件失败:没有用户");
        return false;
    }

    // 7.删除上传文件
    public Boolean deleteUploadedFile(@NonNull String fileId) {
        String uid = fileId.substring(0, fileId.indexOf('\\'));
        MongoUser user = getUser(uid);
        if (user != null) {
            UploadFile delFile = getFile(fileId);
            if (delFile != null) {
                // 删除文件组记录
                user.delFileId(fileId);
                updateUser(user);
                // 删除文件记录
                deleteFile(delFile);
                // 删除本地文件
                deleteFileInUserFile(fileId);
                return true;
            }
            log.info("删除文件失败:没有文件id{}", fileId);
        }
        log.info("删除文件失败:没有用户");
        return false;
    }

    // 8.点赞: 需获取被点赞者的uid所以需要文件id
    public Boolean thumbsUp(String uid, String fileId) {
        MongoUser liker = getUser(uid); // 点赞者
        String hostId = fileId.substring(0, fileId.indexOf("\\"));
        if(null!=liker && !liker.getThumbsUpFileIdGroup().contains(fileId) && !hostId.equals(uid)){
            UploadFile uploadFile = getFile(fileId); // 点赞文件
            uploadFile.addThumbsUpTimes(); // 文件点赞数目
            liker.addThumbsUpFileIdGroup(fileId); // 点赞了该文件

            MongoUser host = getUser(hostId); // 被点赞者
            host.addNumberOfThumbsUpTimes();

            // 更新数据库
            updateUser(liker);
            updateUser(host);
            updateFile(uploadFile);

            log.info("用户{}给文件{}点赞成功",uid,fileId);
            return true;
        }
        return false;
    }

    // 撤销点赞
    public Boolean cancelThumbsUp(String uid, String fileId){
        MongoUser hater = getUser(uid); // 取消点赞者
        String hostId = fileId.substring(0, fileId.indexOf("\\"));
        if(null!=hater && hater.getThumbsUpFileIdGroup().contains(fileId) && !hostId.equals(uid)){
            UploadFile uploadFile = getFile(fileId); // 点赞文件
            uploadFile.decThumbsUpTimes(); // 文件点赞数目
            hater.delThumbsUpFileIdGroup(fileId); // 取消点赞了该文件

            MongoUser host = getUser(hostId); // 被取消点赞者
            host.decNumberOfThumbsUpTimes();

            // 更新数据库
            updateUser(hater);
            updateUser(host);
            updateFile(uploadFile);

            log.info("用户{}给文件{}取消点赞成功",uid,fileId);
            return true;
        }
        return false;
    }
}
