package com.example.user.tvfood.Model;

/**
 * Created by USER on 05/08/2017.
 */

public class detailDTO {
    private String urlImage;
    private String Name;

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public detailDTO() {
    }

    public detailDTO(String urlImage, String name) {
        this.urlImage = urlImage;
        Name = name;
    }
}
