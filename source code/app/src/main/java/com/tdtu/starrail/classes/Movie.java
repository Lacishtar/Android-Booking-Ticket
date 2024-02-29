package com.tdtu.starrail.classes;

public class Movie {
    private int id;
    private int categoryId;
    private String name;
    private String author;
    private String description;
    private String trailer;
    private int price;
    private String image1;
    private String image2;
    private String image3;
    private Integer time_slot1;
    private Integer time_slot2;
    private Integer time_slot3;
    private Integer time_slot4;
    private Integer time_slot5;


    public Movie() {
    }

    public Movie(int id, int categoryId, String name, int price, String author, String description, String trailer,
                 String image1, String image2, String image3, int time_slot1, int time_slot2, int time_slot3,
                 int time_slot4, int time_slot5) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.author = author;
        this.description = description;
        this.trailer = trailer;
        this.price = price;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.time_slot1 = time_slot1;
        this.time_slot2 = time_slot2;
        this.time_slot3 = time_slot3;
        this.time_slot4 = time_slot4;
        this.time_slot5 = time_slot5;
    }

    public boolean isTime_slot1()
    {
        return time_slot1 == 1;
    }

    public boolean isTime_slot2()
    {
        return time_slot2 == 1;
    }

    public boolean isTime_slot3()
    {
        return time_slot3 == 1;
    }

    public boolean isTime_slot4()
    {
        return time_slot4 == 1;
    }

    public boolean isTime_slot5()
    {
        return time_slot5 == 1;
    }

    public Integer getTime_slot1() {
        return time_slot1;
    }

    public void setTime_slot1(Integer time_slot1) {
        this.time_slot1 = time_slot1;
    }

    public Integer getTime_slot2() {
        return time_slot2;
    }

    public void setTime_slot2(Integer time_slot2) {
        this.time_slot2 = time_slot2;
    }

    public Integer getTime_slot3() {
        return time_slot3;
    }

    public void setTime_slot3(Integer time_slot3) {
        this.time_slot3 = time_slot3;
    }

    public Integer getTime_slot4() {
        return time_slot4;
    }

    public void setTime_slot4(Integer time_slot4) {
        this.time_slot4 = time_slot4;
    }

    public Integer getTime_slot5() {
        return time_slot5;
    }

    public void setTime_slot5(Integer time_slot5) {
        this.time_slot5 = time_slot5;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
