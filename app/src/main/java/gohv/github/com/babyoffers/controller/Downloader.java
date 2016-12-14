
package gohv.github.com.babyoffers.controller;


import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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




    public class Result {
        public List<Offer> offers;
        public int size;

    }

}
