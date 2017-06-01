package gohv.github.com.babyoffers.controller;

import java.util.List;

import gohv.github.com.babyoffers.model.Offer;


interface Searchable {

    List<Offer> applySearchByShop(int shopIdentifier, List<Offer> offerList);
    List<Offer> applySearchByShop(int shopIdentifier,int secondShop, List<Offer> offerList);
    List<Offer> applySearchForToys(String[] keyword,List<Offer> offerList);
    List<Offer> applySearchByType(String[] keyword, List<Offer> offerList );

}
