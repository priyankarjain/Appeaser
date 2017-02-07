package com.example.priya.servicetutorial;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by priya on 1/15/2017.
 */

public class MessagingService extends Service {
    private String username = "testuser1";
    private String password = "150101088";
    private MyXMPP XMPPHandle;
    private boolean activityForeground;
    private Handler messageHander;
    private ChatDataSource dataSource;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        activityForeground = true;
        Log.v("Service Binded","true");
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        activityForeground = false;
        removeMessageHandler();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.v("Service","Service Stopped");
        super.onDestroy();
    }

    public void start() {
        createDataSource();
        if(XMPPHandle == null || !XMPPHandle.connected){

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    XMPPHandle = null;
                    XMPPHandle = new MyXMPP(username,password,MessagingService.this);
                    XMPPHandle.init();
                }
            });
            t.start();
        }
    }

    public ChatDataSource getDataSource(){
        return dataSource;
    }

    public void createDataSource(){
        dataSource = new ChatDataSource(this);
        dataSource.open();
    }

    public boolean isActivityForeground(){
        return this.activityForeground;
    }

    public void setMessageHander(Handler handler){
        this.messageHander = handler;
    }

    public boolean isLoggedIn(){
        return XMPPHandle!=null && XMPPHandle.logedin;
    }

    public void removeMessageHandler(){
        this.messageHander = null;
    }

    public Handler getMessageHandler(){
        return messageHander;
    }

    public void createUserChat(String username){
        if(XMPPHandle!=null){
            XMPPHandle.createChatThread(username);
        }
    }

    public void sendUserMessage(String username,String message){
        if(XMPPHandle!=null){
            XMPPHandle.sendMessage(username,message);
        }
    }

    public ArrayList<String> loadRosters(){
        ArrayList<RosterEntry> rosterEntries =  new ArrayList<>(XMPPHandle.getContacts());
        ArrayList<String> entries = new ArrayList<>();
        for (RosterEntry rosterEntry:
             rosterEntries) {
            entries.add(rosterEntry.getUser());
        }
        return entries;
    }

    public HashMap<String,Boolean> loadPresences(){
        HashMap<String,Presence> presences = XMPPHandle.getPresence();
        HashMap<String,Boolean> loadedpresences = new HashMap<>();
        Set<String> userids = presences.keySet();

        for (String s :
                userids) {
            Presence p = presences.get(s);
            loadedpresences.put(s,p.isAvailable());
        }
        return loadedpresences;
    }

    class MyBinder extends Binder{
        public MessagingService getService(){
            return MessagingService.this;
        }
    }
}
