package com.example.user.tvfood.Model;

/**
 * Created by USER on 17/11/2017.
 */

public class SpinnerLanguage {
    private String text = "";
    private int image;

    public SpinnerLanguage() {
    }

    public SpinnerLanguage(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
