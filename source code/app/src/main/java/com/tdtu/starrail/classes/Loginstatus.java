package com.tdtu.starrail.classes;

public class Loginstatus {
    private static Loginstatus instance;
    private boolean loggedIn = false;

    private Loginstatus(){}

    public static synchronized Loginstatus getInstance(){
        if(instance == null){
            instance = new Loginstatus();
        }
        return instance;
    }

    public void setLoggedIn(boolean loggedIn){
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }
}
