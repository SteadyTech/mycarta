package me.muhammadfaisal.mycarta.v1.register.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String uid;
    private String image;
    private String email;
    private String name;

    public UserModel(String uid,String image, String email, String name) {
        this.uid = uid;
        this.image = image;
        this.email = email;
        this.name = name;
    }

    public UserModel() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
