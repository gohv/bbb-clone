
package gohv.github.com.babyoffers.controller;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gohv.github.com.babyoffers.model.Offer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Downloader {
    private final String OFFERS_URL = "http://139.162.162.175:12345/offers";
    private OkHttpClient client;
    private Request request;
    private Response response;


    private InputStream getStream(String urlString) throws IOException {

        client = new OkHttpClient();
        request = new Request.Builder()
                .url(urlString)
                .build();
        response = client.newCall(request).execute();
        return response.body().byteStream();
    }

    public Result getOffers(int start, int end) {
        try {
            String url = String.format("%s?s=%s&e=%s", OFFERS_URL, start, end);
            InputStream inputStream = getStream(url);
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            String json = "";
            String line = "";

            while (true) {
                line = bf.readLine();

                if (line == null) break;

                json += line;
            }

            return new Gson().fromJson(json, Result.class);

        } catch (IOException e) {
            Log.d("Connection", "Connection Error");
            e.printStackTrace();
        }

        return null;
    }

    public List<Offer> applySearch(int shopIdentifier,List<Offer> offerList) {
        if(shopIdentifier > 6){
            try {
                throw new Exception("nema takyw shop");
            } catch (Exception e) {
                Log.d("SHOP EXCEPTION","NEMA TAKYW SHOP");
            }
        }

        ArrayList<Offer> searchShop = new ArrayList<>();
        int[] shops = {1,2,3,4,5,6};

        for (int i = 0; i < offerList.size(); i++) {
            if(offerList.get(i).getShopIdentifier() == shops[shopIdentifier]) {
                searchShop.add(offerList.get(i));
            }
        }
        return searchShop;
    }

    public class Result {
        public List<Offer> offers;
        public int size;

    }

}
