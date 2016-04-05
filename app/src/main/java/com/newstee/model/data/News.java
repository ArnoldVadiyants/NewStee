package com.newstee.model.data;

/**
 * Created by Arnold on 06.04.2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class News {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idauthor")
    @Expose
    private String idauthor;
    @SerializedName("idtags")
    @Expose
    private String idtags;
    @SerializedName("linksong")
    @Expose
    private String linksong;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("addition_time")
    @Expose
    private String additionTime;
    @SerializedName("picture_news")
    @Expose
    private String pictureNews;
    @SerializedName("title")
    @Expose
    private String title;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The idauthor
     */
    public String getIdauthor() {
        return idauthor;
    }

    /**
     *
     * @param idauthor
     * The idauthor
     */
    public void setIdauthor(String idauthor) {
        this.idauthor = idauthor;
    }

    /**
     *
     * @return
     * The idtags
     */
    public String getIdtags() {
        return idtags;
    }

    /**
     *
     * @param idtags
     * The idtags
     */
    public void setIdtags(String idtags) {
        this.idtags = idtags;
    }

    /**
     *
     * @return
     * The linksong
     */
    public String getLinksong() {
        return linksong;
    }

    /**
     *
     * @param linksong
     * The linksong
     */
    public void setLinksong(String linksong) {
        this.linksong = linksong;
    }

    /**
     *
     * @return
     * The likes
     */
    public String getLikes() {
        return likes;
    }

    /**
     *
     * @param likes
     * The likes
     */
    public void setLikes(String likes) {
        this.likes = likes;
    }

    /**
     *
     * @return
     * The additionTime
     */
    public String getAdditionTime() {
        return additionTime;
    }

    /**
     *
     * @param additionTime
     * The addition_time
     */
    public void setAdditionTime(String additionTime) {
        this.additionTime = additionTime;
    }

    /**
     *
     * @return
     * The pictureNews
     */
    public String getPictureNews() {
        return pictureNews;
    }

    /**
     *
     * @param pictureNews
     * The picture_news
     */
    public void setPictureNews(String pictureNews) {
        this.pictureNews = pictureNews;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

}