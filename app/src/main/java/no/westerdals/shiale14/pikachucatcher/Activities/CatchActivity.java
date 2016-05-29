package no.westerdals.shiale14.pikachucatcher.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class CatchActivity extends AppCompatActivity {

    private Button btnCatch;
    private Context context;
    private EditText pikachuIdInput;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        context = this;

        url = "https://locations.lehmann.tech/pokemon/";

        initWidgets();
        initListeners();
    }

    private void initWidgets() {
        btnCatch = (Button) findViewById(R.id.btnCatchWithID);
        pikachuIdInput = (EditText) findViewById(R.id.pikachuIdEditText);
    }

    private void initListeners() {
        btnCatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkId(pikachuIdInput.getText().toString());
            }
        });
    }

    private void checkId(String id) {
        String urlWithId = url + id.trim();

        new AsyncTask<Void, Void, List<LocationJSON>>() {

            private ProgressDialog progressDialog = new ProgressDialog(CatchActivity.this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.catch_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeMenuItem:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.mapsMenuItem:
                startActivity(new Intent(this, MapsActivity.class));
                return true;
            case R.id.resultMenuItem:
                startActivity(new Intent(this, ResultActivity.class));
                return true;
            case R.id.scoresMenuItem:
                startActivity(new Intent(this, ScoresActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
