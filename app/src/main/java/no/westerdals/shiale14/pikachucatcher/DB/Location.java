package no.westerdals.shiale14.pikachucatcher.DB;

/**
 * Created by Alexander on 26.05.2016.
 *
 */
public class Location {

    private int id;
    private String locationId, name;
    private float lat, lng;
    private boolean isCaught;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public void setCaught(boolean caught) {
        isCaught = caught;
    }

    public boolean isCaught() {
        return isCaught;
    }

    @Override
    public String toString() {
        return "{name: " + name + ", locationId:" + locationId + ", lat:" + lat +
                ", lng: " + lng + ", has been caught: " + isCaught + "}";
    }
}
