package no.westerdals.shiale14.pikachucatcher.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import no.westerdals.shiale14.pikachucatcher.R;

/**
 * Created by Alexander on 31.05.2016.
 *
 */
public class ListAdapterWithImage extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemName;
    private final String[] imageUrl;

    public ListAdapterWithImage(Activity context, String[] itemName, String[] imageUrl) {
        super(context, R.layout.list_view_with_img, itemName);

        this.context = context;
        this.itemName = itemName;
        this.imageUrl = imageUrl;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"})
        View rowView = inflater.inflate(R.layout.list_view_with_img, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.pikachuItem);

        txtTitle.setText(itemName[position]);
        new DownloadImageTask((ImageView) rowView.findViewById(R.id.pikachuImg))
                .execute(imageUrl[position]);
        return rowView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
