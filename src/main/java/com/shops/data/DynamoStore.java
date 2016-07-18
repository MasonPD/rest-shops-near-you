package com.shops.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.ServiceAbbreviations;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;
import com.shops.model.Shop;

/**
 * Store too use with AWS DynamoDB
 * 
 * @author ranjan
 *
 */
public class DynamoStore implements Store<Shop, LatLng> {

	private static final Logger LOG = Logger.getLogger(DynamoStore.class.getName());

	/*
	 * table details
	 */
	private static final String TABLE_NAME = "shops";

	private static final String ID_ATTRIBUTE = "id";

	private static final String NAME_ATTRIBUTE = "shopName";

	/*
	 * dynamo configs
	 */
	private boolean local;

	private String localurl;

	/*
	 * variables
	 */
	private boolean initialized;

	private AmazonDynamoDBClient client = null;

	private DynamoDB db = null;

	private Table shopsTable = null;

	public void init() {
		if (!initialized) {
			if (local) {
				client = new AmazonDynamoDBClient().withEndpoint(localurl);
			} else if (Region.getRegion(Regions.AP_SOUTHEAST_1).isServiceSupported(ServiceAbbreviations.Dynamodb)) {
				client = new AmazonDynamoDBClient().withRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
			} else {
				LOG.log(Level.SEVERE, "Could not configure dynamodb");
			}

			db = new DynamoDB(client);
			shopsTable = db.getTable(DynamoStore.TABLE_NAME);
			if (null == shopsTable || null == shopsTable.getDescription()) {
				shopsTable = createShopsTable();
			}
			initialized = true;
		}
	}

	private Table createShopsTable() {
		List<KeySchemaElement> keySchema = Arrays.asList(new KeySchemaElement(DynamoStore.ID_ATTRIBUTE, KeyType.HASH));
		List<AttributeDefinition> attributeDefinitions = Arrays
				.asList(new AttributeDefinition(DynamoStore.ID_ATTRIBUTE, ScalarAttributeType.N));
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(10L, 10L);
		Table shopsTable = null;
		try {
			shopsTable = db.createTable(DynamoStore.TABLE_NAME, keySchema, attributeDefinitions, provisionedThroughput);
			shopsTable.waitForActive();
		} catch (Exception e) {
			shopsTable = db.getTable(DynamoStore.TABLE_NAME);
			LOG.log(Level.SEVERE, "Table already exist.");
		}
		return shopsTable;
	}

	@Override
	public Shop get(LatLng geocode) {
		init();
		// customer latitude and longitude
		double lat1 = geocode.lat;
		double lon1 = geocode.lng;
		// hold the nearest distance found till now
		double nearestDist = -1;
		// hold the reference to the nearest shop id found till now
		long nearestShopId = 0L;
		Shop nearestShop = null;

		try {
			ScanSpec scanspec = new ScanSpec().withProjectionExpression("id, shopLatitude, shopLongitude");
			ItemCollection<ScanOutcome> items = shopsTable.scan(scanspec);
			Iterator<Item> iter = items.iterator();
			while (iter.hasNext()) {
				Item item = iter.next();
				// latitude and longitude of the shop to compare
				double lat2 = Double.parseDouble(item.getString("shopLatitude"));
				double lon2 = Double.parseDouble(item.getString("shopLongitude"));
				// distance to the shop in comparison
				double dist = Util.haversine(lat1, lon1, lat2, lon2);
				// if the shop in comparison is nearer than the previous shop or
				// if it is the first shop
				if (dist < nearestDist || nearestDist == -1) {
					nearestShopId = item.getLong(DynamoStore.ID_ATTRIBUTE);
					nearestDist = dist;
				}
			}
			// use the shop id to fetch the shop details
			GetItemSpec getspec = new GetItemSpec().withPrimaryKey(DynamoStore.ID_ATTRIBUTE, nearestShopId);
			Item nearestShopItem = shopsTable.getItem(getspec);

			ObjectMapper mapper = new ObjectMapper();
			nearestShop = mapper.readValue(nearestShopItem.toJSON(), Shop.class);

		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Unable to scan the table:", e.getMessage());
			e.printStackTrace();
		}
		return nearestShop;
	}

	@Override
	public Shop add(Shop item) {
		init();
		Random rand = new Random();
		final Map<String, String> address = new HashMap<>();
		address.put("number", item.getShopAddress().getNumber());
		address.put("addressLine1", item.getShopAddress().getAddressLine1());
		address.put("addressLine2", item.getShopAddress().getAddressLine2());
		address.put("postCode", item.getShopAddress().getPostCode());
		shopsTable.putItem(new Item().withPrimaryKey(DynamoStore.ID_ATTRIBUTE, rand.nextLong())
				.with(DynamoStore.NAME_ATTRIBUTE, item.getShopName()).with("shopLatitude", item.getShopLatitude())
				.with("shopLongitude", item.getShopLongitude()).withMap("shopAddress", address));
		return item;
	}

	public void shutdown() {
		if (null != db) {
			db.shutdown();
		}
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public void setLocalurl(String localurl) {
		this.localurl = localurl;
	}

}
