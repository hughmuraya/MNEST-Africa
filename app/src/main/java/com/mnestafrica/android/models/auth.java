package com.mnestafrica.android.models;

public class auth {

    private String auth_token;
    private String pass;

    public auth(String auth_token,String pass) {
        this.auth_token = auth_token;
        this.pass = pass;

    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


}