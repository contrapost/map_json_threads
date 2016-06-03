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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import no.westerdals.shiale14.pikachucatcher.JSON.ScoreJSON;
import no.westerdals.shiale14.pikachucatcher.R;

public class ScoresActivity extends AppCompatActivity {

    private ListView listView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        context = this;

        initWidgets();
        downloadScores();
    }

    private void initWidgets() {
        listView = (ListView) findViewById(R.id.scoresListView);
    }

    private void downloadScores() {


        new AsyncTask<Void, Void, List<ScoreJSON>>() {

            private ProgressDialog progressDialog = new ProgressDialog(ScoresActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog.setMessage("Getting scores");
                progressDialog.show();
            }

            @Override
            protected List<ScoreJSON> doInBackground(final Void... params) {

                try {
                    HttpURLConnection connection =
                            (HttpURLConnection) new URL("https://locations.lehmann.tech/scores").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    final StringBuilder builder = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        builder.append(scanner.nextLine());
                    }

                    String json = builder.toString();

                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<List<ScoreJSON>>() {
                    }.getType();

                    return gson.fromJson(json, collectionType);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final List<ScoreJSON> scoreJSONs) {
                super.onPostExecute(scoreJSONs);

                if (scoreJSONs == null) {
                    Toast.makeText(context, "Cannot download scores. No connection!", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    return;
                }

                showScores(scoreJSONs);

                progressDialog.cancel();
            }

        }.execute();
    }

    private void showScores(List<ScoreJSON> scoreJSONs) {
        ArrayAdapter<ScoreJSON> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoreJSONs);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scores, menu);
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
            case R.id.catchMenuItem:
                startActivity(new Intent(this, CatchActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
