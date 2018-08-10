package com.development.lakshya.basicshit.POJO;

public class Line {

    private double latitude1, latitude2, longitude1, longitude2;

    public Line(double latitude1, double latitude2, double longitude1, double longitude2) {
        this.latitude1 = latitude1;
        this.latitude2 = latitude2;
        this.longitude1 = longitude1;
        this.longitude2 = longitude2;
    }

    public double getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(double latitude1) {
        this.latitude1 = latitude1;
    }

    public double getLatitude2() {
        return latitude2;
    }

    public void setLatitude2(double latitude2) {
        this.latitude2 = latitude2;
    }

    public double getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(double longitude1) {
        this.longitude1 = longitude1;
    }

    public double getLongitude2() {
        return longitude2;
    }

    public void setLongitude2(double longitude2) {
        this.longitude2 = longitude2;
    }
}
