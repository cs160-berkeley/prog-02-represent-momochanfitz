package com.example.maurafitzgerald.prog02;

import java.io.Serializable;

/**
 * Created by maurafitzgerald on 3/8/16.
 */
public class MyLocation implements Serializable {
    public String county;
    public String state;
    public double lat;
    public double lng;
    public String zip;

    public MyLocation(String county, String state, double lat, double lng, String zip) {
        this.county = county;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
        this.zip = zip;
    }
}
