package com.quanwc.service;

import com.quanwc.bean.WeiBoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author quanwenchao
 * @date 2019/4/25 09:44:11
 */
@Service
public class ConsumerService{

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 测试mongoTemplate.updateFirst()
     */
    public void findOne() {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(4328738784850359L));

        WeiBoResult one = mongoTemplate.findOne(query, WeiBoResult.class);
        System.out.println("one: " + one);

    }

}