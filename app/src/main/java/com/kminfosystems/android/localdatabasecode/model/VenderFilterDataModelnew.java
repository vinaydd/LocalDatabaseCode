package com.kminfosystems.android.localdatabasecode.model;

import java.io.Serializable;

/**
 * Created by root on 21/4/16.
 */
public class VenderFilterDataModelnew implements Serializable {
    private String vendor_id;
    private String vendor_name;
    private String vendor_image;
    private String food_categories;
    private String vendor_logo;
    private String followers_count;
    private String user_following;
    private String vendor_rating;

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getVendor_image() {
        return vendor_image;
    }

    public void setVendor_image(String vendor_image) {
        this.vendor_image = vendor_image;
    }

    public String getFood_categories() {
        return food_categories;
    }

    public void setFood_categories(String food_categories) {
        this.food_categories = food_categories;
    }

    public String getVendor_logo() {
        return vendor_logo;
    }

    public void setVendor_logo(String vendor_logo) {
        this.vendor_logo = vendor_logo;
    }

    public String getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(String followers_count) {
        this.followers_count = followers_count;
    }

    public String getUser_following() {
        return user_following;
    }

    public void setUser_following(String user_following) {
        this.user_following = user_following;
    }

    public String getVendor_rating() {
        return vendor_rating;
    }

    public void setVendor_rating(String vendor_rating) {
        this.vendor_rating = vendor_rating;
    }


}
