package com.quanwc.service;

import com.alibaba.fastjson.JSONObject;
import com.quanwc.bean.Student;
import com.quanwc.bean.User;
import lombok.val;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author quanwenchao
 * @date 2019/4/2 20:19:35
 */
@Service
public class MongoService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void save() {
        Student student = new Student();
        student.setName("zhangsan111");
        student.setAge(1313);
//        Student save = mongoTemplate.save(student);


        User user = new User();
        user.set_id(999);
        user.setStu(student);
        user.setPhone("12345678911");
        mongoTemplate.save(user);

    }

    public void save2() {
        JSONObject weiboDate = getWeiboDateById(4328738613182938L);
        mongoTemplate.insert(weiboDate.getJSONObject("user"), "subject_user");
    }

    public JSONObject getWeiboDateById(Long id) {
        JSONObject data = mongoTemplate.findById(id, JSONObject.class, "weibo_data");
        return data;
    }

    public void save3() {

//        try {
//            String nowHourStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00"));
//            Date nowHour = new SimpleDateFormat("yyyy-MM-dd HH:00:00").parse(nowHourStr);
//            Document doc = new Document("id", 100).append("created_at", nowHour).append("weibo", "123");
//            mongoTemplate.insert(doc, "test");
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        JSONObject test = mongoTemplate.findById("5cde8d5f268ff590f00bddd1", JSONObject.class, "test");
        long value = test.getDate("created_at").getTime();
        LocalDateTime localDateTime = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
        String format = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime);
        System.out.println(format);


        System.out.println(test.toString());

    }
}
