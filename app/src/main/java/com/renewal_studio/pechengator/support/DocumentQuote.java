package com.renewal_studio.pechengator.support;

import com.google.android.gms.maps.model.LatLng;

public class DocumentQuote {

    private String devoted;
    private LatLng geopoint;
    private String name;
    private String note;
    private String[] photos;
    private String visual_discription;

    public DocumentQuote() {};

    public String getDevoted(){
        return devoted;
    }

    public LatLng getGeopoint(){
        return geopoint;
    }

    public String getName(){
        return name;
    }

    public String getNote(){
        return note;
    }

    public String[] getPhoto(){
        return photos;
    }

    public String getVisual_discription(){
        return visual_discription;
    }

}
