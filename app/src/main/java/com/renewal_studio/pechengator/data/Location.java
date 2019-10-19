package com.renewal_studio.pechengator.data;

import com.google.android.gms.maps.model.LatLng;

public class Location {

    private String name;
    private String discription;
    private String path;// for offline.
    private LatLng location;

    public void Location(String name, String discription, LatLng location){

        this.name = name;
        this.discription = discription;
        this.path = null;
        this.location = location;

    }

    public void Location(){

        this.name = null;
        this.discription = null;
        this.path = null;
        this.location = null;

    }

    public void Location(String name, String discription, String path, LatLng location){

        this.name = name;
        this.discription = discription;
        this.path = path;
        this.location = location;

    }

    public String getLocationName(){

        return this.name;

    }

    public String getLocationDiscription(){

        return this.discription;

    }

    public String getLocationPath(){

        return this.path;

    }

    public LatLng getLocation(){

        return this.location;

    }

    public void setLocationName(String name){

        this.name = name;

    }

    public void setLocationDiscription(String discription){

        this.discription = discription;

    }

    public void setLocationPath(String path){

        this.path = path;

    }

    public void setLocation(LatLng location){

        this.location = location;

    }
}
