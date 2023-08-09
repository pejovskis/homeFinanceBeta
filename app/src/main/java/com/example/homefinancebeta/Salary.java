package com.example.homefinancebeta;

public class Salary {

    String Employer;
    String Category;
    String Date;
    String amount;

    public Salary() {}

    public Salary(String employer, String category, String date, String amount) {
        this.Employer = employer;
        this.Category = category;
        this.Date = date;
        this.amount = amount;
    }

    public String getEmployer() {
        return Employer;
    }

    public void setEmployer(String employer) {
        Employer = employer;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
