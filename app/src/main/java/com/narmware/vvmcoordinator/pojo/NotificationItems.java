package com.narmware.vvmcoordinator.pojo;

import io.realm.RealmObject;

public class NotificationItems extends RealmObject {

   private String not_id,not_type,not_title,not_msg,not_img,not_link,not_date,not_cat_img;
   private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getNot_id() {
        return not_id;
    }

    public void setNot_id(String not_id) {
        this.not_id = not_id;
    }

    public String getNot_type() {
        return not_type;
    }

    public void setNot_type(String not_type) {
        this.not_type = not_type;
    }

    public String getNot_title() {
        return not_title;
    }

    public void setNot_title(String not_title) {
        this.not_title = not_title;
    }

    public String getNot_msg() {
        return not_msg;
    }

    public void setNot_msg(String not_msg) {
        this.not_msg = not_msg;
    }

    public String getNot_img() {
        return not_img;
    }

    public void setNot_img(String not_img) {
        this.not_img = not_img;
    }

    public String getNot_link() {
        return not_link;
    }

    public void setNot_link(String not_link) {
        this.not_link = not_link;
    }

    public String getNot_date() {
        return not_date;
    }

    public void setNot_date(String not_date) {
        this.not_date = not_date;
    }

    public String getNot_cat_img() {
        return not_cat_img;
    }

    public void setNot_cat_img(String not_cat_img) {
        this.not_cat_img = not_cat_img;
    }
}
