package no.westerdals.shiale14.pikachucatcher.Activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import no.westerdals.shiale14.pikachucatcher.R;

/**
 * Created by Alexander on 25.05.2016.
 */
public class DescriptionDialog extends Dialog {

    public DescriptionDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_description);
        Button btnClose = (Button) findViewById(R.id.btnDescriptionClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
