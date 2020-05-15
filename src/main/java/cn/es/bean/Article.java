package cn.es.bean;

import lombok.Data;
import lombok.ToString;

/**
 * @Auther jxy
 * @Date 2020-05-13
 */
@Data
@ToString
public class Article {
    private String id;
    private String title;
    private String from;
    private String times;
    private String readCounts;
    private String content;
}
