package com.tdtu.starrail.classes;

public class Category {
    private int cateId;
    private String catename;

    public Category(int cateId, String catename) {
        this.cateId = cateId;
        this.catename = catename;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }
}
