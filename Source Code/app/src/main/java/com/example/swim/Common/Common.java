package com.example.swim.Common;

public class Common {

    String result = "";
    String qtyC = "";

    public Common() {

    }

    public Common(String result, String qtyC) {
        this.result = result;
        this.qtyC = qtyC;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getQtyC() {
        return qtyC;
    }

    public void setQtyC(String qtyC) {
        this.qtyC = qtyC;
    }
}
