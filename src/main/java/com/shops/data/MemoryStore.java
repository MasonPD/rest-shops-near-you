package com.shops.data;

import java.util.ArrayList;
import java.util.List;

import com.google.maps.model.LatLng;
import com.shops.model.Shop;

public class MemoryStore implements Store<Shop, LatLng> {

	private final List<Shop> data = new ArrayList<>();

	@Override
	public Shop get(LatLng geocode) {
		Shop nearestShop = findNearest(geocode);
		return nearestShop;
	}

	@Override
	public List<Shop> getAll() {
		return data;
	}

	@Override
	public Shop add(Shop item) {
		data.add(item);
		return item;
	}

	public Shop findNearest(LatLng geocode) {
		// customer latitude and longitude
		double lat1 = geocode.lat;
		double lon1 = geocode.lng;
		// hold the distance to the previous shop
		double tempDist = -1;
		// hold the reference to the nearest shop found till now
		Shop nearestShop = null;
		for (Shop shop : data) {
			// latitude and longitude of the shop to compare
			double lat2 = shop.getShopLatitude();
			double lon2 = shop.getShopLongitude();
			// distance to the shop in comparison
			double dist = Util.haversine(lat1, lon1, lat2, lon2);
			// if the shop in comparison is nearer than the previous shop or if
			// it is the first shop
			if (dist < tempDist || tempDist == -1) {
				nearestShop = shop;
			}
			tempDist = dist;
		}
		return nearestShop;
	}

}
