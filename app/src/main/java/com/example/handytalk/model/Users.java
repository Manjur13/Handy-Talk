package com.example.handytalk.model;
public class Users {

    public String username;
    public String email;
    public String password;
    public String uid;
    public Integer marks;
    public String image;


    public Users(String username, String email, String password,String uid,int marks,String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.marks=marks;
        this.image=image;


    }


}