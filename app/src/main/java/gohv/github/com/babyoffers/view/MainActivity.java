package gohv.github.com.babyoffers.view;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gohv.github.com.babyoffers.AlertDialogs.AlertDialogNoNetwork;
import gohv.github.com.babyoffers.AlertDialogs.AlertDialogNoServer;
import gohv.github.com.babyoffers.R;
import gohv.github.com.babyoffers.controller.Downloader;
import gohv.github.com.babyoffers.model.Offer;
import gohv.github.com.babyoffers.controller.OfferAdapter;


public class MainActivity extends AppCompatActivity {


    private GridView gridView;
    private OfferAdapter adapter;
    private ListView drawerList;
    private ArrayAdapter<String> listAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private String activityTitle;
    private ViewGroup titlePanel;
    private Downloader.Result result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Start BIND*/
            drawerList = (ListView) findViewById(R.id.drawerList);
            gridView = (GridView) findViewById(R.id.gridview);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            activityTitle = getTitle().toString();

        /*END BIND*/
        /*Connection Check*/
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

            initializeGridView();
        /*Drawer*/
            addDrawerItems();
            setupDrawer();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        /*END Drawer*/




            new DownloadTask().execute(new Range(0, 10));

        } else {
            AlertDialogNoNetwork noNetwork = new AlertDialogNoNetwork();
            noNetwork.show(getFragmentManager(), "get_message");
        }
        /*End Connection Check*/
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.category_string);
                invalidateOptionsMenu();
            }


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void addDrawerItems() {
        final String[] goods = {"Всички","Дрехи", "Играчки", "Консумативи", "Колички"};

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goods);
        drawerList.setAdapter(listAdapter);


        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position) {
                    case 0:// всички
                        initializeGridView();
                        new DownloadTask().execute(new Range(0, 10));
                        break;
                    case 1://дрех
                        initializeSpecificGridView(4,result.offers);


                        break;
                    case 2://играчки
                        initializeSpecificGridView(3,result.offers);

                        break;
                    case 3://консумативи
                        initializeSpecificGridView(2,result.offers);

                        break;
                    case 4://колички
                        initializeSpecificGridView(0,result.offers);

                        break;
                    default:
                }
            }
        });

    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class Range {

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int start;
        public int end;
    }

    private class DownloadTask extends AsyncTask<Range, List<Offer>, List<Offer>> {
        protected List<Offer> doInBackground(Range... ranges) {
            int pageSize = 30;
            int currentIndexStart = 0;
            int currentIndexEnd = pageSize;
            int lastIndex = 0;
            try {
                while (true) {


                    result = new Downloader().getOffers(currentIndexStart, currentIndexEnd);
                    publishProgress(result.offers);
                    lastIndex = result.size - 1;
                    currentIndexStart += pageSize;
                    currentIndexEnd += pageSize;

                    if (currentIndexEnd > lastIndex) break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                displayMessage();
            }

            return null;

        }

        protected void onProgressUpdate(List<Offer>... progress) {

            adapter.offers.addAll(progress[0]);
            adapter.notifyDataSetChanged();
        }

        protected void onPostExecute(List<Offer> offers) {

        }
    }

    private class SpecificDownloadTask extends AsyncTask<Range, List<Offer>, List<Offer>>{

        @Override
        protected List<Offer> doInBackground(Range... ranges) {
            int pageSize = 30;
            int currentIndexStart = 0;
            int currentIndexEnd = pageSize;
            int lastIndex = 0;

            try {
                while (true) {

                    result = new Downloader().getOffers(currentIndexStart, currentIndexEnd);
                         publishProgress(result.offers);


                    lastIndex = result.size - 1;
                    currentIndexStart += pageSize;
                    currentIndexEnd += pageSize;

                    if (currentIndexEnd > lastIndex) break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                displayMessage();
            }

            return null;
        }

        protected void onPostExecute(List<Offer> offers) {

        }
    }

    private void displayMessage() {
        AlertDialogNoServer dialog = new AlertDialogNoServer();
        dialog.show(getFragmentManager(), "display_message");


    }

    private void updateGridview(final List<Offer> offers) {
    }

    private void initializeGridView() {
        adapter = new OfferAdapter(this, new ArrayList<Offer>());
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                showDetails(adapter.offers.get(position));
            }
        });
    }


    private void initializeSpecificGridView(int shopIdentifier, List<Offer> offerList){
        offerList.addAll(adapter.offers);
        adapter = new OfferAdapter(this, new Downloader().applySearch(shopIdentifier,offerList));
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                showDetails(adapter.offers.get(position));
            }
        });

    }

    private void showDetails(Offer offer) {
        View offerView = View.inflate(this, R.layout.offer_details, null);
        ImageView productImageView = (ImageView) offerView.findViewById(R.id.productImageView);
        TextView productNameTextView = (TextView) offerView.findViewById(R.id.nameTextView);
        TextView oldPriceTextView = (TextView) offerView.findViewById(R.id.oldPriceTextView);
        TextView newPriceTextView = (TextView) offerView.findViewById(R.id.newPriceTextView);
        TextView discountTextView = (TextView) offerView.findViewById(R.id.discountTextView);
        TextView shopNameTextView = (TextView) offerView.findViewById(R.id.storeNameTextView);
        TextView linkToItemTextView = (TextView) offerView.findViewById(R.id.linkToItemTextView);

        oldPriceTextView.setText(String.valueOf(offer.getOldPrice()));
        newPriceTextView.setText(String.valueOf(offer.getNewPrice()));
        discountTextView.setText(String.valueOf("-" + offer.getDiscount()) + "%");
        discountTextView.setBackgroundColor(Color.parseColor(offer.getDiscountColor(offer.getDiscount())));
        shopNameTextView.setText(String.valueOf("Store Name: " + offer.getShopName()));
        //productImageView.setImageBitmap(offer.getProductImage());
        Picasso.with(this).load(offer.getProductPhoto()).into(productImageView);
        productNameTextView.setText(offer.getProductName());
        linkToItemTextView.setText(offer.getLinkToItem());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Offer Details:")
                .setView(offerView)
                .setCancelable(true).show();


    }





}
