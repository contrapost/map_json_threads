package no.westerdals.shiale14.pikachucatcher.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import no.westerdals.shiale14.pikachucatcher.Net.Response;
import no.westerdals.shiale14.pikachucatcher.R;

public class CatchActivity extends AppCompatActivity {

    private Button btnCatch;
    private Context context;
    private EditText pikachuIdInput;
    private String url, token;

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

                try {
                    HttpURLConnection connection =
                            (HttpURLConnection) new URL(urlWithId).openConnection();
                } catch (IOException e) {
                    throw new RuntimeException("Encountered a problem while fetching website", e);
                } /* catch (FileNotFoundException e) {

                } */

                return new Response(1, "1");
            }

            @Override
            protected void onPostExecute(final Response response) {
                super.onPostExecute(response);


                progressDialog.cancel();
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
