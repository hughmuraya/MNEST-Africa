package com.mnestafrica.android.models;

public class OpenUnit {

    private int id;
    private String uuid;
    private String unit_name;
    private String value;
    private String floor;
    private String size;


    public OpenUnit(int id,  String uuid, String unit_name, String value, String floor, String size) {

        this.id = id;
        this.uuid = uuid;
        this.unit_name = unit_name;
        this.value = value;
        this.floor = floor;
        this.size = size;

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

}

