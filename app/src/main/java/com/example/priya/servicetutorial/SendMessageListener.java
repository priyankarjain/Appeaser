package com.example.priya.servicetutorial;

/**
 * Created by priya on 1/17/2017.
 */

public interface SendMessageListener {
    public void onCreateChat(String source);
    public void onSendMessage(String source,String Message);
}
