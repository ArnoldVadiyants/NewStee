package com.newstee.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Author {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author_name")
    @Expose
    private String authorName;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("quantity_subs")
    @Expose
    private String quantitySubs;

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
     * The authorName
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     *
     * @param authorName
     * The author_name
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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
     * The quantitySubs
     */
    public String getQuantitySubs() {
        return quantitySubs;
    }

    /**
     *
     * @param quantitySubs
     * The quantity_subs
     */
    public void setQuantitySubs(String quantitySubs) {
        this.quantitySubs = quantitySubs;
    }

}