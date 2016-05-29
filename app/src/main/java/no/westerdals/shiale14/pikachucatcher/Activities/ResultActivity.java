package no.westerdals.shiale14.pikachucatcher.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import no.westerdals.shiale14.pikachucatcher.DB.Pikachu;
import no.westerdals.shiale14.pikachucatcher.DB.PikachuDataSource;
import no.westerdals.shiale14.pikachucatcher.R;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ListView listView = (ListView) findViewById(R.id.listViewResults);

        PikachuDataSource pds = new PikachuDataSource(this);
        pds.open();

        if (!pds.isEmpty()) {
            List<Pikachu> pikachus = pds.getPikachus();
            ArrayAdapter<Pikachu> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pikachus);
            assert listView != null;
            listView.setAdapter(adapter);
        } else {
            ArrayList<String> dummyList = new ArrayList<>();
            dummyList.add("You haven't caught one yet!");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_expandable_list_item_1,
                    dummyList);
            assert listView != null;
            listView.setAdapter(adapter);
        }

        pds.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.results, menu);
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
            case R.id.scoresMenuItem:
                startActivity(new Intent(this, ScoresActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
