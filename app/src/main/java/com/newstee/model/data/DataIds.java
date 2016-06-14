package com.newstee.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

/**
 * Created by Arnold on 18.04.2016.
 */
@Generated("org.jsonschema2pojo")
public class DataIds {

        @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("data")
    @Expose
    private List<Integer> data = new ArrayList<Integer>();
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
    public List<Integer> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Integer> data) {
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
