package com.example.homefinancebeta;

import android.text.format.DateFormat;

import java.util.Date;

public class Expense {

    //fields
    private String id;
    private String where;
    private String category;
    private String essentials;
    private String date;
    private String price;

    public Expense(){}

    public Expense(String id, String where, String category, String essentials, String date, String price) {
        this.id = id;
        this.where = where;
        this.category = category;
        this.essentials = essentials;
        this.date = date;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEssentials() {
        return essentials;
    }

    public void setEssentials(String essentials) {
        this.essentials = essentials;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
