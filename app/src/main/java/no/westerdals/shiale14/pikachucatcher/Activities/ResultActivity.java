package no.westerdals.shiale14.pikachucatcher.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import no.westerdals.shiale14.pikachucatcher.DB.Location;
import no.westerdals.shiale14.pikachucatcher.DB.LocationDataSource;
import no.westerdals.shiale14.pikachucatcher.R;

public class ResultActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        LocationDataSource lds = new LocationDataSource(this);
        lds.open();
        List<Location> locs = lds.getLocations();
        ListView listView = (ListView) findViewById(R.id.listViewResults);
        ArrayAdapter<Location> adapter = new ArrayAdapter<>(this,  android.R.layout.simple_list_item_1, locs);
        assert listView != null;
        listView.setAdapter(adapter);



        lds.close();
    }
}
