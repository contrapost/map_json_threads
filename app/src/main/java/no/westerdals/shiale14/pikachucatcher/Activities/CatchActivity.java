package no.westerdals.shiale14.pikachucatcher.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import no.westerdals.shiale14.pikachucatcher.DB.Location;
import no.westerdals.shiale14.pikachucatcher.DB.LocationDataSource;
import no.westerdals.shiale14.pikachucatcher.DB.Pikachu;
import no.westerdals.shiale14.pikachucatcher.DB.PikachuDataSource;
import no.westerdals.shiale14.pikachucatcher.JSON.PikachuJSON;
import no.westerdals.shiale14.pikachucatcher.Net.Response;
import no.westerdals.shiale14.pikachucatcher.R;

public class CatchActivity extends AppCompatActivity {

    private Button btnCatch;
    private EditText pikachuIdInput;
    private String url, token;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        context = this;

        url = "https://locations.lehmann.tech/pokemon/";
        //noinspection SpellCheckingInspection
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.InNoaWFsZTE0Ig.wn-j-S2hPYcNLudRCsMjxlLgzzSvwinLjqQu4iSOAa4\n";

        initWidgets();
        initListeners();

        String action = getIntent().getAction();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

            MifareUltralight ultralight = MifareUltralight.get(tag);

            try {
                ultralight.connect();
                byte[] payload = ultralight.readPages(8);
                checkId(new String(payload));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong with NFC connection", Toast.LENGTH_SHORT).show();
                try {
                    ultralight.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Toast.makeText(this, "Couldn't close NFC Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        pikachuIdInput.setText("");
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

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pikachuIdInput.getWindowToken(), 0);
            }
        });
    }

    private void checkId(String id) {
        final String urlWithId = url + id.trim();

        new AsyncTask<Void, Void, Response>() {

            private ProgressDialog progressDialog = new ProgressDialog(CatchActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Checking the id");
                progressDialog.show();
            }

            @Override
            protected Response doInBackground(final Void... params) {

                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) new URL(urlWithId).openConnection();
                    connection.setRequestProperty("X-Token", token);

                    Scanner scanner = new Scanner(connection.getInputStream());

                    final StringBuilder builder = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        builder.append(scanner.nextLine());
                    }

                    return new Response(connection.getResponseCode(), builder.toString());

                } catch (FileNotFoundException e) {

                    assert connection != null;
                    Scanner scanner = new Scanner(connection.getErrorStream());

                    final StringBuilder builder = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        builder.append(scanner.nextLine());
                    }

                    try {
                        return new Response(connection.getResponseCode(), builder.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @SuppressWarnings("ConstantConditions")
            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(final Response response) {
                super.onPostExecute(response);

                if (response == null) {
                    Toast.makeText(context, "Cannot check the ID. No connection!", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    return;
                }

                if (response == null) {
                    Toast.makeText(context, "Wasn't able to check the ID", Toast.LENGTH_LONG).show();
                } else {
                    if(response.getStatusCode() > 400) {
                        Toast.makeText(context, response.getStatusCode() + " " + response.getBody(), Toast.LENGTH_LONG).show();
                    }

                    boolean isCaught = false;
                    if(response.getStatusCode() == 201) {
                        savePikachuInDB(response, isCaught);
                    }
                    if(response.getStatusCode() == 200) {
                        isCaught = true;
                        Gson gson = new Gson();
                        PikachuJSON pikachuJSON = gson.fromJson(response.getBody(), PikachuJSON.class);

                        Toast.makeText(context, "Already caught " + pikachuJSON.getName() + "!", Toast.LENGTH_SHORT).show();

                        PikachuDataSource pikachuDataSource = new PikachuDataSource(context);
                        pikachuDataSource.open();

                        if(pikachuDataSource.getPikachus().isEmpty()){
                            savePikachuInDB(response, isCaught);
                        } else {
                            String pikachuId = pikachuJSON.get_id();

                            List<Pikachu> pikachusFromDB = pikachuDataSource.getPikachus();
                            HashSet<String> pikachuIds = new HashSet<>(pikachusFromDB.size());

                            for(Pikachu p : pikachusFromDB) {
                                pikachuIds.add(p.get_id());
                            }

                            if(!pikachuIds.contains(pikachuId)){
                                savePikachuInDB(response, isCaught);
                            }
                        }

                    }
                }
                progressDialog.cancel();
            }

        }.execute();
    }

    private void savePikachuInDB(Response response, boolean isCaught) {
        Gson gson = new Gson();
        PikachuJSON pikachuJSON = gson.fromJson(response.getBody(), PikachuJSON.class);

        PikachuDataSource pikachuDataSource = new PikachuDataSource(context);
        pikachuDataSource.open();

        updateLocations(pikachuJSON);

        Pikachu pikachu = new Pikachu();
        pikachu.set_id(pikachuJSON.get_id());
        pikachu.setPikachuId(pikachuJSON.getId());
        pikachu.setName(pikachuJSON.getName());
        pikachu.setImageUrl(pikachuJSON.getImageUrl());
        pikachuDataSource.savePikachu(pikachu);

        pikachuDataSource.close();

        startActivity(new Intent(context, ResultActivity.class));
        if(!isCaught) {
            Toast.makeText(context, "You've caught " + pikachuJSON.getName() + "!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateLocations(PikachuJSON pikachuJSON) {
        LocationDataSource locationDataSource = new LocationDataSource(context);
        locationDataSource.open();
        String pikachuName = pikachuJSON.getName();
        List<Location> locations = locationDataSource.getLocations();
        assert locations != null;
        for (Location l : locations) {
            if(l.getName().equals(pikachuName)){
                locationDataSource.updateCaughtStatus(l.getId());
            }
        }
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
