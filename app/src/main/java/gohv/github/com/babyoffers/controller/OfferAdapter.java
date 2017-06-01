package gohv.github.com.babyoffers.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

import gohv.github.com.babyoffers.R;
import gohv.github.com.babyoffers.model.Offer;


public class OfferAdapter extends BaseAdapter {
    private Context context;
    public List<Offer> offers;

    public OfferAdapter(Context context, List<Offer> offers) {
        this.context = context;
        this.offers = offers;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView;

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.offer,parent,false);
        } else {
            gridView = convertView;
        }

        Offer offer = offers.get(position);

        TextView nameTextView = (TextView) gridView.findViewById(R.id.nameTextView);
        nameTextView.setText(offer.getProductName());

        TextView oldPriceTextView = (TextView) gridView.findViewById(R.id.oldPriceTextView);
        oldPriceTextView.setText(context.getString(R.string.old_price, offer.getOldPrice()));

        TextView newPriceTextView = (TextView) gridView.findViewById(R.id.newPriceTextView);
        newPriceTextView.setText(context.getString(R.string.discount_price, offer.getNewPrice()));

        TextView discountTextVIew = (TextView) gridView.findViewById(R.id.discountTextView);
        String discount = "-" + offer.getDiscount() + "%";
        discountTextVIew.setText(discount);
        discountTextVIew.setVisibility(View.INVISIBLE);
        discountTextVIew.setBackgroundColor(Color.parseColor(offer.getDiscountColor(offer.getDiscount())));



        new ImageDownloader(gridView).execute(offer);

        return gridView;
    }

    @Override
    public int getCount() {
        return offers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ImageDownloader extends AsyncTask<Offer, Void, Offer> {
        ImageView image;
        ProgressBar progressBar;

        public ImageDownloader(View container) {
            this.image = (ImageView) container.findViewById(R.id.productImageView);
            this.progressBar = (ProgressBar) container.findViewById(R.id.progressBar1);
        }

        protected Offer doInBackground(Offer... urls) {
            Offer offer = urls[0];
            return offer;
        }

        protected void onPostExecute(Offer result) {
            Picasso.with(context).load(result.getProductPhoto()).into(image);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}

