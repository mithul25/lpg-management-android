package com.example.lpgdemo;

public class Order {

    private String Date;

    private String Time;
    private String CylinderSize;
    private String AmountPayable;
    private String oid;
    private String quan;
    private String status;
    private String bdate;

    public Order() {
    }

    public Order(String date, String time, String cylinderSize, String amountPayable, String oid, String quan, String status, String bdate) {
        Date = date;
        Time = time;
        CylinderSize = cylinderSize;
        AmountPayable = amountPayable;
        this.oid = oid;
        this.quan = quan;
        this.status = status;
        this.bdate = bdate;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getCylinderSize() {
        return CylinderSize;
    }

    public void setCylinderSize(String cylinderSize) {
        CylinderSize = cylinderSize;
    }

    public String getAmountPayable() {
        return AmountPayable;
    }

    public void setAmountPayable(String amountPayable) {
        AmountPayable = amountPayable;
    }


}
