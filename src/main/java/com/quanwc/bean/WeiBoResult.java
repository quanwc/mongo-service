package com.quanwc.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "weibo_result")
public class WeiBoResult {

    @Id
    private Long id;                            //微博id
    private Integer info_type=1;                 //数据来源 1：微博
    private String state;                       // 微博正负向：n是负向，p是正向
    private Integer profit_lm;                  //利多利空   0:利空 1：利多 2：中性
    private Integer negative_num;               //负向评论数量
    private Integer positive_num;               //正向评论数量
    private Integer sentiment_index;            //情绪指数
    private Integer positive_index;             //正向情绪指数
    private Integer negative_index;             //负向情绪指数
    private Integer importance_index;           //重要性指数
    private JSONObject match_labels;            //匹配的公司以及关键词标签
    private List<String> match_classifies;      //资讯所属分类
    private LocalDateTime create_time;          //创建时间
    private LocalDateTime update_time;          //更新时间
    private JSONObject data;                    //分时及历史的评论数据  eg：2019-1-16当天有人评论了这条微博，当天情绪指数value是1；9点有一条评论数据，是p/n正/负向的，重要性是1

}
