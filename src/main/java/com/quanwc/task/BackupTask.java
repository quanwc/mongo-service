package com.quanwc.task;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * company_result、subject_result表备份
 *
 * @author quanwenchao
 * @date 2019/7/29 14:53:42
 */
@Slf4j
@Component
public class BackupTask {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 备份company_result、subject_result表
     * 每小时的15分，触发一次
     */
    @Scheduled(cron = "0 15 * * * ?")
    public void dealBackup() {
        dealSubjectResultBackup();
        dealCompanyResultBackup();
    }

    /**
     * 处理company_result表的备份数据
     * 按小时新增备份新表、删除上一个小时的备份老表
     */
    private void dealCompanyResultBackup() {
        log.info("backup company_result begin");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        String date = LocalDateTime.now().format(formatter);

        // step1：新增备份新表
        List<JSONObject> companyList = mongoTemplate.findAll(JSONObject.class, "company_result");
        mongoTemplate.insert(companyList, "z_backup_company_result_" + date);

        List<JSONObject> companyNewsList = mongoTemplate.findAll(JSONObject.class, "company_result_news");
        mongoTemplate.insert(companyNewsList, "z_backup_company_result_news_" + date);

        List<JSONObject> companyXueqiuList = mongoTemplate.findAll(JSONObject.class, "company_result_xueqiu");
        mongoTemplate.insert(companyXueqiuList, "z_backup_company_result_xueqiu_" + date);

        // step2：删除备份老表
        String date2 = LocalDateTime.now().minusHours(1).format(formatter);
        mongoTemplate.dropCollection("z_backup_company_result_" + date2);
        mongoTemplate.dropCollection("z_backup_company_result_news_" + date2);
        mongoTemplate.dropCollection("z_backup_company_result_xueqiu_" + date2);

        log.info("backup company_result end");
    }

    /**
     * 处理subject_result表的备份数据
     * 按小时新增备份新表、删除上一个小时的备份老表
     */
    private void dealSubjectResultBackup() {
        log.info("backup subject_result begin");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        String date = LocalDateTime.now().format(formatter);

        // 新增备份新表
        List<JSONObject> companyList = mongoTemplate.findAll(JSONObject.class, "subject_result");
        mongoTemplate.insert(companyList, "z_backup_subject_result_" + date);

        List<JSONObject> companyNewsList = mongoTemplate.findAll(JSONObject.class, "subject_result_news");
        mongoTemplate.insert(companyNewsList, "z_backup_subject_result_news_" + date);

        List<JSONObject> companyXueqiuList = mongoTemplate.findAll(JSONObject.class, "subject_result_xueqiu");
        mongoTemplate.insert(companyXueqiuList, "z_backup_subject_result_xueqiu_" + date);

        // 删除备份老表
        String date2 = LocalDateTime.now().minusHours(1).format(formatter);
        mongoTemplate.dropCollection("z_backup_subject_result_" + date2);
        mongoTemplate.dropCollection("z_backup_subject_result_news_" + date2);
        mongoTemplate.dropCollection("z_backup_subject_result_xueqiu_" + date2);

        log.info("backup subject_result end");
    }

}
