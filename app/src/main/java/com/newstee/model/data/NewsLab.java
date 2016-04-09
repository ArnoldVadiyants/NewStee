package com.newstee.model.data;

/**
 * Created by Arnold on 09.04.2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class NewsLab {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<News> data = new ArrayList<News>();
    @SerializedName("message")
    @Expose
    private String message;

    /**
     *
     * @return
     * The result
     */
    public String getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     *
     * @return
     * The data
     */
    public List<News> getNews() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setNews(List<News> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}