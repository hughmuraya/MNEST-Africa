package com.mnestafrica.android.models;

public class OpenUnit {

    private int id;
    private String uuid;
    private String unit_name;
    private String value;
    private String service_charge;
    private String floor;
    private String size;
    private String security_deposit;



    public OpenUnit(int id,  String uuid, String unit_name, String value, String service_charge,String floor, String size, String security_deposit) {

        this.id = id;
        this.uuid = uuid;
        this.unit_name = unit_name;
        this.value = value;
        this.service_charge = service_charge;
        this.floor = floor;
        this.size = size;
        this.security_deposit = security_deposit;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSecurity_deposit() {
        return security_deposit;
    }

    public void setSecurity_deposit(String security_deposit) {
        this.security_deposit = security_deposit;
    }


}

