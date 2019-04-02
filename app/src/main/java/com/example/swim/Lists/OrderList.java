package com.example.swim.Lists;

public class OrderList  {
    String OrderId,ProductName,ProductPrice,ProductQty,Address,Status;

    public OrderList(){

    }

    public OrderList(String orderId, String productName,String productPrice, String productQty, String address, String status) {
        OrderId = orderId;
        ProductName = productName;
        ProductPrice = productPrice;
        ProductQty = productQty;
        Address = address;
        Status = status;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductQty() {
        return ProductQty;
    }

    public void setProductQty(String productQty) {
        ProductQty = productQty;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
