package me.muhammadfaisal.mycarta.v2.model.api;

public class Article {
    public static final int IMAGE_TYPE = 1;
    public String title;
    public String subtitle;
    public String image;
    public int type;


    public Article ( int type, String title, String subtitle, String image){
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
