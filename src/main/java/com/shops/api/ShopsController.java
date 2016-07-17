package com.shops.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.model.LatLng;
import com.shops.data.Store;
import com.shops.geocode.GeocodeService;
import com.shops.model.Shop;

@RestController
public class ShopsController {

	@Autowired
	@Qualifier("geocodeService")
	private GeocodeService geocodeService;

	@Autowired
	@Qualifier("memoryStore")
	private Store<Shop, LatLng> store;

	@RequestMapping(path = "/shop", method = RequestMethod.POST)
	public Shop updateShop(@RequestBody Shop shop) {
		LatLng geocode = geocodeService.getGeocode(shop);
		if (null != geocode) {
			shop.setShopLatitude(geocode.lat);
			shop.setShopLongitude(geocode.lng);
		}
		store.add(shop);
		return shop;
	}

	@RequestMapping(path = "/shop/{latitude}/{longitude}", method = RequestMethod.GET)
	public Shop getShop(@PathVariable double latitude, @PathVariable double longitude) {
		LatLng geocode = new LatLng(latitude, longitude);
		Shop shop = store.get(geocode);
		return shop;
	}

	@RequestMapping(path = "/shop", method = RequestMethod.GET)
	public List<Shop> getShops() {
		return store.getAll();
	}

}
