package com.ldx.mygraduationproject.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by freeFreAme on 2019/1/22.
 */
public class Article extends LitePalSupport {

    /**
     * id : 41
     * createDate : 2018-08-31 01:02:29
     * title : 冬季易上火，“灭火”之道你知道几个呢？
     * content : 前几天，黄先生和朋友小聚，喝了一些白酒。第二天，他感觉眼睛干涩，照镜子一看，眼睛里还有一些血丝，看上去白眼仁发红。那么冬季上火吃什么好呢? 对此，著名中医养生专家梅晓芳认为，...
     * classes : 1
     * articleDate : null
     * articleUrl : https://www.jianshu.com/p/9ed1b0b950fa
     * author : 993
     * img : upload-images.jianshu.io/upload_images/9128118-efd7b3d784837389.gif
     * status : 1
     */

    @Column(unique = true)
    private int id;
    private String createDate;
    private String title;
    private String content;
    private String classes;
    @Column(ignore = true)
    private Object articleDate;
    private String articleUrl;
    private String author;
    private String img;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public Object getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(Object articleDate) {
        this.articleDate = articleDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", createDate='" + createDate + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", classes='" + classes + '\'' +
                ", articleDate=" + articleDate +
                ", articleUrl='" + articleUrl + '\'' +
                ", author='" + author + '\'' +
                ", img='" + img + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
