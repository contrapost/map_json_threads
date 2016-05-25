package no.westerdals.shiale14.pikachucatcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
}
