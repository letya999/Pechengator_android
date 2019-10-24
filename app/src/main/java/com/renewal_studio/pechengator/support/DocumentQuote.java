package com.renewal_studio.pechengator.support;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.firestore.GeoPoint;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class DocumentQuote implements Parcelable {
    @Nullable
    private String name;
    @Nullable
    private String note;
    @Nullable
    private String devoted;
    @Nullable
    private String visual_description;
    @Nullable
    private List<String> photo = new ArrayList<String>();
    @Nullable
    private GeoPoint geopoint;
    @Nullable
    private double lat;
    @Nullable
    private double lng;

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

    public void clearPhotoList() {
        photo.clear();
    }

    public void addPhotoUrl(String url) {
        photo.add(url);
    }

    protected DocumentQuote(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.note = ((String) in.readValue((String.class.getClassLoader())));
        this.devoted = ((String) in.readValue((String.class.getClassLoader())));
        this.visual_description = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.photo, (java.lang.String.class.getClassLoader()));
        lat = ((double) in.readValue((double.class.getClassLoader())));
        lng = ((double) in.readValue((double.class.getClassLoader())));
        this.geopoint = new GeoPoint(lat,lng);
    }

    public DocumentQuote() {}

    public DocumentQuote(String name, String note, String devoted,
                         String visual_description, List<String> photos, double lat, double lng) {
        super();
        this.name = name;
        this.note = note;
        this.devoted = devoted;
        this.visual_description = visual_description;
        this.photo = photos;
        this.lat = lat;
        this.lng = lng;
    }

    public DocumentQuote(String name, String note, String devoted,
                         String visualDiscription, List<String> photos, GeoPoint geopoint) {
        super();
        this.name = name;
        this.note = note;
        this.devoted = devoted;
        this.visual_description = visualDiscription;
        this.photo = photos;
        this.geopoint = geopoint;
        this.lat = geopoint.getLatitude();
        this.lng = geopoint.getLongitude();
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

    public String getVisual_description() {
        return visual_description;
    }

    public void setVisual_description(String visual_description) {
        this.visual_description = visual_description;
    }

    public List<String> getPhoto() {
        return photo;
    }

    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }

    public GeoPoint getGeopoint() {
        return geopoint;
    }

    public void setGeopoint(GeoPoint geopoint) {
        this.geopoint = geopoint;
        this.lat = geopoint.getLatitude();
        this.lng = geopoint.getLongitude();
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(name).append(" "+note).append(" "+devoted).
                append(" "+visual_description).append(" "+photo.toString()).toString();
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
        dest.writeValue(visual_description);
        dest.writeList(photo);
        dest.writeValue(geopoint.getLatitude());
        dest.writeValue(geopoint.getLongitude());
    }

    public int describeContents() {
        return 0;
    }
}
