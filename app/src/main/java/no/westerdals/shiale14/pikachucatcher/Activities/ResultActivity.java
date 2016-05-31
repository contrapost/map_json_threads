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
import no.westerdals.shiale14.pikachucatcher.Util.ListAdapterWithImage;

public class ResultActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initWidgets();
        loadPikachus();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadPikachus();
    }

    private void initWidgets() {
        listView = (ListView) findViewById(R.id.listViewResults);
    }

    private void loadPikachus() {
        PikachuDataSource pds = new PikachuDataSource(this);
        pds.open();

        if (!pds.getPikachus().isEmpty()) {
            List<Pikachu> pikachus = pds.getPikachus();
            String[] pikachuNames = new String[pikachus.size()];
            String[] imageUrls = new String[pikachus.size()];

            for(int i = 0; i < pikachus.size(); i++) {
                imageUrls[i] = pikachus.get(i).getImageUrl();
                pikachuNames[i] = pikachus.get(i).getName();
            }

            ListAdapterWithImage listAdapterWithImage = new ListAdapterWithImage(this, pikachuNames, imageUrls);
            listView.setAdapter(listAdapterWithImage);
        } else {
            ArrayList<String> dummyList = new ArrayList<>();
            dummyList.add("You haven't caught one yet!");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_expandable_list_item_1,
                    dummyList);
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
