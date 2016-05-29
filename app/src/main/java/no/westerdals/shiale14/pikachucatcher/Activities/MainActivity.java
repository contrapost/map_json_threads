package no.westerdals.shiale14.pikachucatcher.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import no.westerdals.shiale14.pikachucatcher.DB.Location;
import no.westerdals.shiale14.pikachucatcher.DB.LocationDataSource;
import no.westerdals.shiale14.pikachucatcher.JSON.LocationJSON;
import no.westerdals.shiale14.pikachucatcher.R;

public class MainActivity extends AppCompatActivity {

    private Button btnMap, btnCatch, btnResults;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        initWidgets();
        initListeners();

        downloadLocations();
    }


    private void initWidgets() {
        btnMap = (Button) findViewById(R.id.btnMap);
        btnCatch = (Button) findViewById(R.id.btnCatch);
        btnResults = (Button) findViewById(R.id.btnResult);
    }

    private void initListeners() {
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MapsActivity.class));
            }
        });

        btnCatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CatchActivity.class));
            }
        });

        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ResultActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.resultMenuItem:
                startActivity(new Intent(context, ResultActivity.class));
                return true;
            case R.id.descriptionMenuItem:
                DescriptionDialog descriptionDialog = new DescriptionDialog(MainActivity.this);
                descriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                descriptionDialog.show();
                return true;
            case R.id.scoresMenuItem:
                startActivity(new Intent(context, ScoresActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void downloadLocations() {
        new AsyncTask<Void, Void, List<LocationJSON>>() {

            private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Searching for Pikachu");
                progressDialog.show();
            }

            @Override
            protected List<LocationJSON> doInBackground(final Void... params) {

                try {
                    HttpURLConnection connection =
                            (HttpURLConnection) new URL("https://locations.lehmann.tech/locations").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    final StringBuilder builder = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        builder.append(scanner.nextLine());
                    }

                    String json = builder.toString();

                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<List<LocationJSON>>() {
                    }.getType();

                    return gson.fromJson(json, collectionType);
                } catch (IOException e) {
                    throw new RuntimeException("Encountered a problem while fetching website", e);
                }
            }

            @Override
            protected void onPostExecute(final List<LocationJSON> locationsFromJson) {
                super.onPostExecute(locationsFromJson);

                LocationDataSource locationDataSource = new LocationDataSource(context);
                locationDataSource.open();

                //TODO: more efficient way to save/update location data could be introduced here afterwords

                if (locationDataSource.getLocations().isEmpty()) {
                    for (LocationJSON l : locationsFromJson) {
                        saveLocation(l, locationDataSource);
                    }
                } else {
                    List<Location> locationsFromDB = locationDataSource.getLocations();
                    HashSet<String> locationIds = new HashSet<>(locationsFromDB.size());
                    for (Location l : locationsFromDB) {
                        locationIds.add(l.getLocationId());
                    }
                    for (LocationJSON l : locationsFromJson) {
                            if (locationIds.contains(l.get_id())
                                    || l.get_id() == null) { // prevents from saving locations without _id
                                break;
                            }
                            saveLocation(l, locationDataSource);
                    }
                }

                locationDataSource.close();

                progressDialog.cancel();
            }

            private void saveLocation(LocationJSON location, LocationDataSource locationDataSource) {
                Location tempLocation = new Location();
                tempLocation.setLocationId(location.get_id());
                tempLocation.setName(location.getName());
                tempLocation.setLat(location.getLat());
                tempLocation.setLng(location.getLng());
                locationDataSource.saveLocation(tempLocation);
            }

        }.execute();
    }
}
