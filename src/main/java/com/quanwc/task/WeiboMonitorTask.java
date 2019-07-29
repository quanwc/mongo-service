package com.quanwc.task;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import com.quanwc.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 监控微博数据、评论数据的订阅逻辑是否发生中断，5分钟检查一次，如果中断则报警
 *
 * @author quanwenchao
 * @date 2019/6/3 19:36:46
 */
@Slf4j
@Component
public class WeiboMonitorTask {

    private String weiboCollection = "";
    private String commentCollection = "";

    private long dailyWeiboMaxCount = 0L;
    private long dailyCommentMaxCount = 0L;

    @Autowired
    private MongoTemplate mongoTemplate;
//    @Autowired
//    private DingTalkConfig dingTalkConfig;

    /**
     * 监控微博数据、评论数据的订阅状态
     */
    @Scheduled(cron = " 0 */5 * * * *") // 每5分钟执行一次
    public void checkMonitor() {
        // 检查微博
        checkWeiboData();
        // 检查评论
        checkCommentData();
    }


    @Scheduled(cron = " 0 0 0 * * ?") // 每天0点0分0秒，将weiboMaxCount赋值为0
    public void updateWeiboMaxCount() {
        log.info("updateDailyWeiboMaxCount: " + LocalDateTime.now());
        dailyWeiboMaxCount = 0L;
    }

    @Scheduled(cron = " 0 0 0 * * *") // 每天0点0分0秒，将commentMaxCount赋值为0
    public void updateCommentMaxCount() {
        log.info("updateDailyCommentMaxCount: " + LocalDateTime.now());
        dailyCommentMaxCount = 0L;
    }


    public void checkWeiboData() {
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        if (0 == hour && minute < 5) {
            // 每天[00:00] - [00:05]之间不检查，避免临界点的钉钉报错通知
            return;
        }

        weiboCollection = "weibo-" + DateUtil.getDateTimeAsString(LocalDateTime.now(), "yyyyMMdd");

        long count = mongoTemplate.count(new Query(), weiboCollection);
        if (count <= dailyWeiboMaxCount) {
            notifyDingTalk("微博数据发生中断，count没有递增");
        }
        dailyWeiboMaxCount = count;

        log.info("dailyWeiboMaxCount: " + dailyWeiboMaxCount);
    }

    /**
     * 监控评论数据是否发生中断
     */
    public void checkCommentData() {
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        if (0 == hour && minute < 5) {
            // 每天[00:00] - [00:05]之间不检查，避免临界点的钉钉报错通知
            return;
        }

        commentCollection = "comment-" + DateUtil.getDateTimeAsString(LocalDateTime.now(), "yyyyMMdd");

        long count = mongoTemplate.count(new Query(), commentCollection);
        if (count <= dailyCommentMaxCount) {
            notifyDingTalk("评论数据发生中断，count没有递增");
        }
        dailyCommentMaxCount = count;

        log.info("dailyCommentMaxCount: " + dailyCommentMaxCount);
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
//        Response response = DingTalkUtil
//            .sendTextMessage(dingTalkConfig.getWebhookUrl(), dingTalkConfig.getAccessToken(), textMessage);
//        log.error(response.getBody());
    }

    /**
     * 检查当前日期是否在[00:00]至[00:40]之间
     * @return
     */
    private boolean checkCurrentDate4Weibo() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        String date = formatter.format(new Date()); // 获取当前时间的小时分钟：19:54 -> 1954
        int time = Integer.parseInt(date);
        if (time >= 00 && time <= 40) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查当前日期是否在[00:00]至[00:05]之间
     * @return
     */
    private boolean checkCurrentDate4Comment() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        String date = formatter.format(new Date()); // 获取当前时间的小时分钟：19:54 -> 1954
        int time = Integer.parseInt(date);
        if (time >= 0 && time <= 5) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        int hour = LocalDateTime.now().plusHours(9).getHour();
        int minute = LocalDateTime.now().getMinute();
        System.out.println(hour + ", " + minute);
        if (0 == hour && minute<5) {
            return;
        }

    }

}
