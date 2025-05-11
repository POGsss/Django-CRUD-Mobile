package com.example.djangocrud;

public class UserModel {
    // Properties
    int id;
    String profile_picture;
    String name;
    String gender;
    String age;
    String hobby;
    String profession;

    // Constructor
    public UserModel(int id, String profile_picture, String name, String gender, String age, String hobby, String profession) {
        this.id = id;
        this.profile_picture = profile_picture;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.hobby = hobby;
        this.profession = profession;
    }

    // Getters And Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

}