package model;

import java.io.Serializable;

public class User implements HasId<String>,Serializable{
    private String userId;
    private String parola;
    private String aspect;

    public User(String userId, String parola, String aspect) {
        this.userId = userId;
        this.parola = parola;
        this.aspect = aspect;
    }



    @Override
    public String getId() {
        return userId;
    }

    @Override
    public void setId(String s) {
        this.userId=s;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "model.model.User{" +
                "userId='" + userId + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

}