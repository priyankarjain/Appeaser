package com.example.priya.servicetutorial;

import java.io.Serializable;

/**
 * Created by priya on 1/24/2017.
 */

public class ChatModel implements Serializable{
    private long id;
    private String source;
    private String type;
    private String message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "[Source:"+source+", Message: "+message+"]";
    }
}
