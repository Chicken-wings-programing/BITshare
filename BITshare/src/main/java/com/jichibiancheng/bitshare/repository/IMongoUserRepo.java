package com.jichibiancheng.bitshare.repository;

import com.jichibiancheng.bitshare.documents.MongoUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// you can do filter here,
@Repository
public interface IMongoUserRepo extends MongoRepository<MongoUser,String> {

}
