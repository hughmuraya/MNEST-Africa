package com.mnestafrica.android.models;

public class RentInvoice {

    private String invoice_no;
    private String property_name;
    private int amount;
    private String uuid;
    private String unit_name;
    private String username;
    private String created_at;
    private int paid;
    private String status;
    private int penalties;


    public RentInvoice(String invoice_no,String property_name,int amount, String uuid, String unit_name, String username, String created_at, int paid, String status, int penalties) {

        this.invoice_no = invoice_no;
        this.property_name = property_name;
        this.amount = amount;
        this.uuid = uuid;
        this.unit_name = unit_name;
        this.username = username;
        this.created_at = created_at;
        this.paid = paid;
        this.status = status;
        this.penalties = penalties;

    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPenalties() {
        return penalties;
    }

    public void setPenalties(int penalties) {
        this.penalties = penalties;
    }
}

