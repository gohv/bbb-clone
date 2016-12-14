package gohv.github.com.babyoffers.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gohv.github.com.babyoffers.model.Offer;

public class Search implements Searchable {

    @Override
    public List<Offer> applySearchByShop(int shopIdentifier, List<Offer> offerList) {
        if(shopIdentifier > 6){

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

    @Override
    public List<Offer> applySearchByShop(int shopIdentifier, int secondShop, List<Offer> offerList) {

        ArrayList<Offer> searchShop = new ArrayList<>();
        int[] shops = {1,2,3,4,5,6};

        for (int i = 0; i < offerList.size(); i++) {
            if(offerList.get(i).getShopIdentifier() == shops[shopIdentifier]
                    || offerList.get(i).getShopIdentifier() == shops[secondShop] ) {
                searchShop.add(offerList.get(i));
            }
        }
        return searchShop;
    }

    @Override
    public List<Offer> applySearchForToys(int shopIdentifier, int secondShop,String[] keyword,
                                          List<Offer> offerList) {

        ArrayList<Offer> searchShop = new ArrayList<>();
        int[] shops = {1,2,3,4,5,6};

        for (int i = 0; i < offerList.size(); i++) {
            if(offerList.get(i).getShopIdentifier() == shops[shopIdentifier]
                    || offerList.get(i).getShopIdentifier() == shops[secondShop] ) {
                searchShop.add(offerList.get(i));

                for(Offer o : offerList){
                    for(String s : keyword){
                        if(o.getProductName().contains(s) == true){
                            searchShop.remove(o);
                        }
                    }
                }
            }
        }
        return searchShop;
    }


    @Override
    public List<Offer> applySearchByType(String[] keyword,List<Offer> offerList) {

        ArrayList<Offer> searchShop = new ArrayList<>();
        for (Offer o : offerList) {
            for (String s : keyword) {
                if (o.getProductName().contains(s) == true) {
                    searchShop.add(o);
                }
            }
        }

        return searchShop;
    }
}
