package com.example.user.tvfood.Model;

/**
 * Created by USER on 25/12/2017.
 */

public class CategoryModel {
    private long id;
    private String name;

    public CategoryModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void createCategoryModel(CategoryModel categoryModel) {
        this.id = categoryModel.getId();
        this.name = categoryModel.getName();
    }
}
