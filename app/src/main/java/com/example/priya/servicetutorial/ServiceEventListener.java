package com.example.priya.servicetutorial;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;

import java.util.Collection;

/**
 * Created by priya on 1/15/2017.
 */

public interface ServiceEventListener {
    public void onMessageReceived(Message message);
    public void onRosterLoaded(Collection<String> addresses);
}
