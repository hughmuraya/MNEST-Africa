package com.mnestafrica.android.models;

public class LeaseAgreement {

    private String unit;
    private String link;

    public LeaseAgreement(String unit, String link) {

        this.unit = unit;
        this.link = link;

    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
