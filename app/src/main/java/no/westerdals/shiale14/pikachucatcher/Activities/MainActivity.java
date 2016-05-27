package no.westerdals.shiale14.pikachucatcher.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;
import java.util.Scanner;

import no.westerdals.shiale14.pikachucatcher.DB.Location;
import no.westerdals.shiale14.pikachucatcher.DB.LocationDataSource;
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
                Intent intent = new Intent(context, ResultActivity.class);
                startActivity(intent);
                return true;
            case R.id.descriptionMenuItem:
                DescriptionDialog descriptionDialog = new DescriptionDialog(MainActivity.this);
                descriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                descriptionDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void downloadLocations() {
        new AsyncTask<Void, Void, List<Location>>() {

            private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Searching for Pikachu");
                progressDialog.show();
            }

            @Override
            protected List<Location> doInBackground(final Void... params) {

                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://locations.lehmann.tech/locations").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    final StringBuilder builder = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        builder.append(scanner.nextLine());
                    }

                    String json = builder.toString();

                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<List<Location>>() {
                    }.getType();

                    return gson.fromJson(json, collectionType);
                } catch (IOException e) {
                    throw new RuntimeException("Encountered a problem while fetching website", e);
                }
            }

            @Override
            protected void onPostExecute(final List<Location> locationsFromJson) {
                super.onPostExecute(locationsFromJson);

                LocationDataSource locationDataSource = new LocationDataSource(context);

                locationDataSource.open();

                List<Location> loc = locationDataSource.getLocations();

//                if (locationDataSource.getLocations().size() == 0) {
//                    for (Location l : locationsFromJson) {
//                        Location location = new Location();
//                        location.setLocationId(l.getId()+"");
//                        location.setName(l.getName());
//                        location.setLat(l.getLat());
//                        location.setLng(l.getLng());
//                        locationDataSource.saveLocation(location);
//                    }
//                } else {
//                    List<Location> locationsFromDB = locationDataSource.getLocations();
//                    for (Location l : locationsFromJson) {
//                        String id = l.getLocationId();
//                        for (Location location : locationsFromDB) {
//                            if (location.getLocationId().equals(id)) {
//                                break;
//                            }
//                            Location tempLocation = new Location();
//                            tempLocation.setLocationId(l.getLocationId());
//                            tempLocation.setName(l.getName());
//                            tempLocation.setLat(l.getLat());
//                            tempLocation.setLng(l.getLng());
//                            locationDataSource.saveLocation(tempLocation);
//                        }
//                    }
//                }
                locationDataSource.close();
                progressDialog.cancel();
            }
        }.execute();
    }
}
