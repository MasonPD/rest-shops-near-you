package com.shops.geocode;

import java.util.Objects;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.shops.model.Address;
import com.shops.model.Shop;

/**
 * Service to locate the latitude and longitude of a shop. Uses Google's
 * Geocoding service.
 * 
 * @author ranjan
 *
 */

public class GeocodeService {

	/**
	 * the context
	 */
	private GeoApiContext context;

	/**
	 * the api key
	 */
	private String key;

	public GeocodeService() {
		// TODO : set the proxy and the credentials
		context = new GeoApiContext();
	}

	/**
	 * return the geocode of the shop
	 * 
	 * @param shop
	 * @return
	 */
	public LatLng getGeocode(Shop shop) {
		context.setApiKey(key);
		GeocodingResult[] results = null;
		LatLng geocode = null;
		try {
			results = GeocodingApi.geocode(context, getFormattedAddress(shop)).await();
			geocode = results[0].geometry.location;
		} catch (Exception e) {
			// TODO : add logging
			e.printStackTrace();
		}
		return geocode;
	}

	/**
	 * comma separated string formated address
	 * 
	 * @param shop
	 * @return
	 */
	private String getFormattedAddress(Shop shop) {
		Address address = shop.getShopAddress();
		StringBuilder formattedAddress = new StringBuilder();
		if (Objects.nonNull(shop.getShopName())) {
			formattedAddress.append(shop.getShopName()).append(",");
		}
		if (Objects.nonNull(address.getNumber())) {
			formattedAddress.append(address.getNumber()).append(",");
		}
		if (Objects.nonNull(address.getAddressLine1())) {
			formattedAddress.append(address.getAddressLine1()).append(",");
		}
		if (Objects.nonNull(address.getAddressLine2())) {
			formattedAddress.append(address.getAddressLine2()).append(",");
		}
		if (Objects.nonNull(address.getPostCode())) {
			formattedAddress.append(address.getPostCode());
		}
		System.out.println("Evaluating geocode for the address :" + formattedAddress.toString());
		return formattedAddress.toString();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
