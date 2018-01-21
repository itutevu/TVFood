package com.example.user.tvfood.Local_Address;

/**
 * Created by USER on 11/08/2017.
 */

public class TinhThanhPhoDTO {
    private int id;
    private String Name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public TinhThanhPhoDTO(int id, String name) {
        this.id = id;
        Name = name;
    }

    public void createTinhThanhDTO(TinhThanhPhoDTO tinhThanhPhoDTO) {
        this.id = tinhThanhPhoDTO.getId();
        this.Name = tinhThanhPhoDTO.getName();
    }

    public TinhThanhPhoDTO() {
    }
}
