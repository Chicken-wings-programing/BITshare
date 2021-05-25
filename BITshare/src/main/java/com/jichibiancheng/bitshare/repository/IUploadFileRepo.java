package com.jichibiancheng.bitshare.repository;

import com.jichibiancheng.bitshare.documents.UploadFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// you can do filter here.
@Repository
public interface IUploadFileRepo extends MongoRepository<UploadFile,String> {
}
