package com.shops;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.google.maps.model.LatLng;
import com.shops.data.MemoryStore;
import com.shops.data.Store;
import com.shops.geocode.GeocodeService;
import com.shops.model.Shop;

@SpringBootApplication
public class ShopsNearYouApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopsNearYouApplication.class, args);
	}

	@Bean(name = "geocodeService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConfigurationProperties(prefix = "config")
	public GeocodeService getGeocodeService() {
		GeocodeService service = new GeocodeService();
		return service;
	}

	@Bean(name = "memoryStore")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Store<Shop, LatLng> getMemoryStore() {
		return new MemoryStore();
	}
}
