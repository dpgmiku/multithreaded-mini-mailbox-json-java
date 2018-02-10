package com.mycompany.app.server;

/**
 * Created by admin on 29.06.17.
 */
public class User {

    private String name;


    public User(String name){
        this.name = name;


    }

    public String getName(){
        return name;


    }

    @Override
    public boolean equals(Object o){
      if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
User user = (User) o;

if (name != null ? ! name.equals(user.name) : user.name != null) return false;
return true;

    }
}
