package com.mnestafrica.android.models;

public class Property {

    private int id;
    private String uuid;
    private String property_name;
    private String property_type;
    private String building_type;
    private String electricity;
    private String water;


    public Property(int id,  String uuid, String property_name, String property_type, String building_type, String electricity,String water) {

        this.id = id;
        this.uuid = uuid;
        this.property_name = property_name;
        this.property_type = property_type;
        this.building_type = building_type;
        this.electricity = electricity;
        this.water = water;

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

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getBuilding_type() {
        return building_type;
    }

    public void setBuilding_type(String building_type) {
        this.building_type = building_type;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

}

