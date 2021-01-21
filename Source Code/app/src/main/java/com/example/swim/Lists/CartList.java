package com.example.swim.Lists;

public class CartList {
    String ProductName,ProductPrice,ProductQty,Address,Status;

    public CartList(){

    }

    public CartList(String productName,String productPrice, String productQty, String address, String status) {
        ProductName = productName;
        ProductQty = productQty;
        ProductPrice = productPrice;
        Address = address;
        Status = status;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
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
