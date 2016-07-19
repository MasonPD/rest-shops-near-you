package com.shops.api;

import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.shops.ShopsNearYouApplication;
import com.shops.model.Address;
import com.shops.model.Shop;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShopsNearYouApplication.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class ShopsControllerTest {

	@Value("${local.server.port}")
	private int port;

	private URL base;
	private RestTemplate template;

	@Before
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:" + port + "/shop");
		template = new TestRestTemplate();
	}

	@Test
	public void updateShop() throws Exception {
		Shop shop = new Shop();
		shop.setShopName("Grocery Store");
		Address address = new Address();
		address.setNumber("1600");
		address.setAddressLine1("Amphitheatre Parkway");
		address.setAddressLine2("Mountain View, CA");
		address.setPostCode("94043");
		shop.setShopAddress(address);

		ResponseEntity response = template.postForEntity(base.toString(), shop, null);

		Assert.assertEquals("The Post request should  be successful.", HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void getShop() throws Exception {
		this.base = new URL("http://localhost:" + port + "/shop/{latitude}/{longitude}");
		Object[] urlVariables = { "37.422364", "-122.084364" };
		ResponseEntity<Shop> response = template.getForEntity(base.toString(), Shop.class, urlVariables);

		Assert.assertEquals("The Get request should  be successful.", HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull("Should return a Shop with name.", response.getBody().getShopName());
		Assert.assertNotNull("Should return a Shop with address.",
				response.getBody().getShopAddress().getAddressLine1());
	}
}
