package com.example.studentswapzone;

public class Product {
    private String id;
    private String name;
    private String description;
    private int yearsUsed;
    private double sellingPrice;
    private String category;

    private  String uname;
    private String imageResource;

    // Default constructor (required for Firebase)
    public Product() {
    }

    public Product(String id, String name, String description, int yearsUsed, double sellingPrice, String category,String uname) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.yearsUsed = yearsUsed;
        this.sellingPrice = sellingPrice;
        this.category = category;
        this.uname=uname;
        this.imageResource=id;
    }

    // Getters and setters for the product attributes

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYearsUsed() {
        return yearsUsed;
    }

    public void setYearsUsed(int yearsUsed) {
        this.yearsUsed = yearsUsed;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }


    public String getImageResource() {
        return imageResource;
    }

    // Setter method for the image resource
    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}

