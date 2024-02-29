package com.tdtu.starrail.classes;

public class HistoryOrder {
    private int orderid;
    private int userId;
    private int totalPrice;
    private String orderDate;
    private String receiverName;
    private String receiverPhone;
    private String paymentMethod;
    public HistoryOrder(int orderid, int userId, int totalPrice, String orderDate, String receiverName,
                        String receiverPhone, String paymentMethod) {
        this.orderid = orderid;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.paymentMethod = paymentMethod;
    }

    public int getOrderid() {
        return orderid;
    }

    public int getUserId() {
        return userId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
