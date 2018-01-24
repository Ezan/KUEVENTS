package com.example.eforezan.kuevents;

/**
 * Created by ezan on 1/14/18.
 */

public class Event {
    private String title, desc, image, date;

    public Event(){

    }

    public Event(String title, String desc, String image, String date) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
