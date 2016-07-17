package com.shops.data;

import java.util.List;

import com.google.maps.model.LatLng;
import com.shops.model.Shop;

public class DynamoStore implements Store<Shop, LatLng> {

	@Override
	public Shop get(LatLng geocode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Shop add(Shop item) {
		// TODO Auto-generated method stub
		return item;
	}

	@Override
	public List<Shop> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
