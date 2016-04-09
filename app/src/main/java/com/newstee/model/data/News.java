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
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("tag_ids")
    @Expose
    private String tagIds;
    @SerializedName("audio_ids")
    @Expose
    private String audioIds;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("like_count")
    @Expose
    private String likeCount;
    @SerializedName("author_id")
    @Expose
    private String authorId;

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

    /**
     *
     * @return
     * The content
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     * The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return
     * The tagIds
     */
    public String getTagIds() {
        return tagIds;
    }

    /**
     *
     * @param tagIds
     * The tag_ids
     */
    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    /**
     *
     * @return
     * The audioIds
     */
    public String getAudioIds() {
        return audioIds;
    }

    /**
     *
     * @param audioIds
     * The audio_ids
     */
    public void setAudioIds(String audioIds) {
        this.audioIds = audioIds;
    }

    /**
     *
     * @return
     * The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     *
     * @param avatar
     * The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     *
     * @return
     * The likeCount
     */
    public String getLikeCount() {
        return likeCount;
    }

    /**
     *
     * @param likeCount
     * The like_count
     */
    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    /**
     *
     * @return
     * The authorId
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     *
     * @param authorId
     * The author_id
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

}
