package com.example.priya.servicetutorial;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SendMessageListener{
    private MessagingService MyService;
    private boolean bounded;
    private ContactsData contactsData;
    private ChatDataSource dataSource;

    private final android.os.Handler messageHandler= new android.os.Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
        Bundle data = msg.getData();
        String type = data.getString("Type");
        Log.v("StringTye:",type);

        if(type.equals("message")){
            String from = data.getString("From");
            String body = data.getString("Body");
            //Toast.makeText(MainActivity.this,from+" "+body,Toast.LENGTH_SHORT).show();
            Log.v("frag",from.split("/")[0]);
            ChatFragment chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentByTag("chat:"+from.split("/")[0]);
            Log.v("BackStack",getSupportFragmentManager().getBackStackEntryAt(0)+"");
            if(chatFragment!=null){
                Log.v("Hello","Yes");
                chatFragment.receiveMessages(body);
            }
        }else if(type.equals("rosterAdd")){
            ArrayList<String> addresses = data.getStringArrayList("addresses");
            for (String address: addresses ) {
                if(!contactsData.contacts.contains(address)){
                    contactsData.contacts.add(address);
                    contactsData.presences.put(address,false);
                }
            }
            RosterFragment fragment =
                    (RosterFragment) getSupportFragmentManager().findFragmentByTag("Roster");
            if(fragment!=null){
                fragment.addContacts(addresses);
            }
        }else if(type.equals("presenceChange")) {
            contactsData.presences.put(data.getString("From"),data.getBoolean("Available"));
            RosterFragment fragment = (RosterFragment) getSupportFragmentManager().findFragmentByTag("Roster");
            if (fragment != null) {
                Log.v("presence", "registered");
                String from = data.getString("From");
                boolean availability = data.getBoolean("Available");
                fragment.availabilityChanged(from, availability);
            }
        }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v("MAINACTIVITY","SERVICE CONNECTED");
            bounded = true;

            MyService =((MessagingService.MyBinder) service).getService();
            MyService.setMessageHander(messageHandler);
            boolean isloggedin = MyService.isLoggedIn();

            if(isloggedin){
                contactsData.contacts = MyService.loadRosters();
                contactsData.presences = MyService.loadPresences();
                Log.v("contacts", String.valueOf(contactsData.contacts));
                Log.v("presences",String.valueOf(contactsData.presences));
                RosterFragment fragment = (RosterFragment) getSupportFragmentManager().findFragmentByTag("Roster");
                if (fragment != null) {
                    fragment.notifyAboutData();
                }
            }else{
                MyService.start();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            MyService = null;
            bounded = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this,MessagingService.class);
        bindService(i,serviceConnection,BIND_AUTO_CREATE);
        Log.v("MAINACTIVITY","SERVICE BIND CALL");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactsData = new ContactsData();
        dataSource = new ChatDataSource(this);
        dataSource.open();

        RosterFragment fragment = new RosterFragment();
        fragment.setContactsData(contactsData);
        fragment.setDataSource(dataSource);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main,fragment,"Roster").commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bounded){
            unbindService(serviceConnection);
            Log.v("MAINACTIVITY","SERVICE UNBIND CALL");
            bounded = false;
        }
    }

    @Override
    public void onCreateChat(String source) {
        if(MyService!=null){
            MyService.createUserChat(source);
        }
    }

    @Override
    public void onSendMessage(String source, String message) {
        if(MyService!=null){
            MyService.sendUserMessage(source,message);
        }
    }
}
