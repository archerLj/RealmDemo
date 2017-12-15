package com.example.realmdemo;

import io.realm.RealmObject;

/**
 * Created by archerlj on 2017/12/12.
 */

public class User extends RealmObject {
//    @PrimaryKey
//    private int id;

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
