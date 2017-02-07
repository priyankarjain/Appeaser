package com.example.priya.servicetutorial;

/**
 * Created by priya on 1/17/2017.
 */

public class Contact {
    String name;
    boolean available;
    String status;

    public Contact(){
        name = null;
        available = false;
    }

    public Contact(String name){
        this.name = name;
        available = false;
    }

    public Contact(String name,boolean available){
        this.available = available;
        this.name = name;
    }

    public void setAvailable(boolean available){
        this.available = available;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
