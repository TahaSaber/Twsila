package com.example.tahasaber.twsila.DataClasses;

/**
 * Created by anaconda on 6/17/17.
 */

public class UserDataClass {
    public String id , name, email;
    public UserDataClass(String id , String name , String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }
    public UserDataClass(){

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
