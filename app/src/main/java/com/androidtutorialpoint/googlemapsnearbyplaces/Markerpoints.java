package com.androidtutorialpoint.googlemapsnearbyplaces;

/**
 * Created by Saranya GS on 28/12/2017.
 */

public class Markerpoints {
    Double lat,lng;String rating,photo,open;


    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getPhoto() {

        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    String place,viccnity;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Double getLat() {

        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getViccnity() {
        return viccnity;
    }

    public void setViccnity(String viccnity) {
        this.viccnity = viccnity;
    }
}
