package com.shops.geocode;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.shops.model.Address;
import com.shops.model.Shop;

//TODO : add comments
public class GeocodeService {

	private GeoApiContext context;

	public GeocodeService() {
		//TODO : set the proxy and the credentials
		context = new GeoApiContext();
		//TODO : get this key from an yml file
		context.setApiKey("AIzaSyATiU-bQDwz2xzXNKbqF3qsyuXv7T6VeAU");
	}

	public LatLng getGeocode(Shop shop) {
		GeocodingResult[] results = null;
		GeocodingResult result = null;
		LatLng geocode = null;
		try {
			results = GeocodingApi.geocode(context, getFormattedAddress(shop)).await();
			result = results[0];
			geocode = result.geometry.location;
		} catch (Exception e) {
			//TODO : add logging
			e.printStackTrace();
		}
		return geocode;
	}

	private String getFormattedAddress(Shop shop) {
		Address address = shop.getShopAddress();
		StringBuilder formattedAddress = new StringBuilder();
		//TODO have null and blank check before appending
		formattedAddress.append(shop.getShopName()).append(",").append(address.getNumber()).append(",")
				.append(address.getAddressLine1()).append(",").append(address.getAddressLine2()).append(",")
				.append(address.getPostCode());
		System.out.println("Evaluating geocode for the address :" + formattedAddress.toString());
		return formattedAddress.toString();
	}

}
