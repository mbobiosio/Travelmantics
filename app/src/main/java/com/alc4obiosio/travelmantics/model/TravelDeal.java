package com.alc4obiosio.travelmantics.model;

import java.io.Serializable;

/**
 * Created by Mbuodile Obiosio on Aug 03,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class TravelDeal implements Serializable {

    private String id;
    private String title;
    private String description;
    private String price;
    private String imageUrl;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    private String imageName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public TravelDeal(){}

    public TravelDeal(String title, String description, String price, String imageUrl, String imageName)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }
}
