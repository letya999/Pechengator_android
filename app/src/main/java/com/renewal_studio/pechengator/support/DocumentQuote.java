package com.renewal_studio.pechengator.support;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class DocumentQuote implements Parcelable {

    private String name;
    private String note;
    private String devoted;
    private String visualDiscription;
    private List<String> photos = new ArrayList<String>();
    private LatLng geopoint;

    public final static Parcelable.Creator<DocumentQuote> CREATOR = new Creator<DocumentQuote>() {
        @SuppressWarnings({
                "unchecked"
        })
        public DocumentQuote createFromParcel(Parcel in) {
            return new DocumentQuote(in);
        }

        public DocumentQuote[] newArray(int size) {
            return (new DocumentQuote[size]);
        }

    };

    protected DocumentQuote(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.note = ((String) in.readValue((String.class.getClassLoader())));
        this.devoted = ((String) in.readValue((String.class.getClassLoader())));
        this.visualDiscription = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.photos, (java.lang.String.class.getClassLoader()));
        double lat = ((double) in.readValue((double.class.getClassLoader())));
        double lng = ((double) in.readValue((double.class.getClassLoader())));
        this.geopoint = new LatLng(lat,lng);
    }

    public DocumentQuote() {}

    public DocumentQuote(String name, String note, String devoted,
                         String visualDiscription, List<String> photos, LatLng geopoint) {
        super();
        this.name = name;
        this.note = note;
        this.devoted = devoted;
        this.visualDiscription = visualDiscription;
        this.photos = photos;
        this.geopoint = geopoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDevoted() {
        return devoted;
    }

    public void setDevoted(String devoted) {
        this.devoted = devoted;
    }

    public String getVisualDiscription() {
        return visualDiscription;
    }

    public void setVisualDiscription(String visualDiscription) {
        this.visualDiscription = visualDiscription;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public LatLng getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(LatLng geopoint) {
        this.geopoint = geopoint;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(name).append(" "+note).append(" "+devoted).
                append(" "+visualDiscription).append(" "+photos).append(" "+geopoint.toString()).toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if ((other instanceof DocumentQuote) == false)
            return false;
        DocumentQuote rhs = ((DocumentQuote) other);
        return rhs.equals(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(note);
        dest.writeValue(devoted);
        dest.writeValue(visualDiscription);
        dest.writeList(photos);
        dest.writeValue(geopoint.latitude);
        dest.writeValue(geopoint.longitude);
    }

    public int describeContents() {
        return 0;
    }
}
