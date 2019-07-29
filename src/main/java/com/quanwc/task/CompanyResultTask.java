package com.quanwc.task;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 监控company_result、subject_result表的数据是否正常
 * 
 * @author quanwenchao
 * @date 2019/7/28 13:26:32
 */
@Component
public class CompanyResultTask {

    @Autowired
    private MongoTemplate mongoTemplate;
//    @Autowired
//    private DingTalkConfig dingTalkConfig;

    /**
     * 监控company_result、subject_result表的分时数据是否中断 每小时的10分，触发一次
     */
    @Scheduled(cron = "0 10 * * * ?")
    public void checkHour() {
        checkCompanyHour();
        checkSubjectHour();
    }

    public void checkCompanyHour() {
        Query query = new Query();
        List<JSONObject> companyResultList = mongoTemplate.find(query, JSONObject.class, "company_result");
        JSONObject jsonObject = companyResultList.get(companyResultList.size() - 1);
        String hour = jsonObject.getJSONArray("hour").getJSONObject(0).getString("date");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(hour, formatter);

        if (dateTime.isBefore(LocalDateTime.now())) {
            notifyDingTalk("company_result hour发生中断");
        }
    }

    /**
     * 检查话题的分时数据
     */
    private void checkSubjectHour() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        List<JSONObject> companyResultList = mongoTemplate.find(query, JSONObject.class, "subject_result");
        JSONObject jsonObject = companyResultList.get(0);
        String hour = jsonObject.getJSONArray("hour").getJSONObject(0).getString("date");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(hour, formatter);

        if (dateTime.isBefore(LocalDateTime.now())) {
            notifyDingTalk("subject_result hour发生中断");
        }
    }

    /**
     * 唤醒钉钉通知
     *
     * @param errorMessage
     */
    private void notifyDingTalk(String errorMessage) {
//        TextMessage textMessage = new TextMessage();
//        textMessage.setMsgtype("text");
//
//        Text text = new Text();
//        text.setContent(errorMessage);
//        textMessage.setText(text);
//
//        At at = new At();
//        at.setAtMobiles(dingTalkConfig.getAtMobiles());
//        at.setAtAll(true);
//        textMessage.setAt(at);
//
//        Response response =
//            DingTalkUtil.sendTextMessage(dingTalkConfig.getWebhookUrl(), dingTalkConfig.getAccessToken(), textMessage);
    }
}
