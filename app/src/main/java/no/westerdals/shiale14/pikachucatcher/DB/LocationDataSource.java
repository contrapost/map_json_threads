package no.westerdals.shiale14.pikachucatcher.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 26.05.2016.
 *
 */

public class LocationDataSource {
    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public LocationDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void saveLocation(Location location) {
        ContentValues contentValues = new ContentValues();
        // table -> value
        contentValues.put("locationId", location.getLocationId());
        contentValues.put("name", location.getName());
        contentValues.put("lat", location.getLat());
        contentValues.put("lng", location.getLng());
        contentValues.put("isCought", location.getCought());

        db.insert("location", null, contentValues);
    }

    public List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();

        String[] columns = { "id", "locationId", "name", "lat", "lng", "isCought" };

        Cursor cursor = db.query("location", columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            locations.add(locationFromCursor(cursor));
            cursor.moveToNext();
        }

        return locations;
    }

    private Location locationFromCursor(final Cursor cursor) {
        Location location = new Location();

        location.setId(cursor.getInt(0));
        location.setLocationId(cursor.getString(1));
        location.setName(cursor.getString(2));
        location.setLat(cursor.getFloat(3));
        location.setLng(cursor.getFloat(4));
        location.setCought(cursor.getInt(5) > 0);

        return location;
    }

}
