package com.tdtu.starrail.classes;

public class OrderDetail {
    private int orderdetailid;
    private int orderId;
    private int movieId;
    private int quantity;

    public OrderDetail(int orderdetailid, int orderId, int movieId, int quantity) {
        this.orderdetailid = orderdetailid;
        this.orderId = orderId;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    public int getOrderdetailid() {
        return orderdetailid;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setOrderdetailid(int orderdetailid) {
        this.orderdetailid = orderdetailid;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
