package com.quanwc.service;

import com.mongodb.client.result.UpdateResult;
import com.quanwc.bean.WeiBoResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * @author quanwenchao
 * @date 2019/4/25 10:31:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void findOne() {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(4328738784850359L));

        WeiBoResult one = mongoTemplate.findOne(query, WeiBoResult.class);
        System.out.println("one: " + one);
    }

    @Test
    public void updateFirst() {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(43287387848503590L));

        Update update = Update.update("update_time", LocalDateTime.now())
                .inc("attitudes_count", 100) // todo 微博的正、向的数量都加1
                .inc("comments_count", 100)
                .inc("reposts_count", 100);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, WeiBoResult.class);
        System.out.println("updateResult: " + updateResult.getModifiedCount());


    }
}