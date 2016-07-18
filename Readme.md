# Shops Near You API
Gives you the facility to add a shop with address. Stores the longitude and latitude of your shop using Google [Geocoding API](https://developers.google.com/maps/documentation/geocoding/intro). You can request for a shop mentioning your current latitude and longitude. The API would return you the shop nearest to you.

## How to run

## If you are behind a proxy server
If you are running it behind a proxy server, set the proxy setting in `application.properties`. e.g.
```
config.proxy=true
config.proxyaddress=127.0.0.1
config.proxyport=80
config.proxyuser=user
config.proxypassword=password
```

Else keep `config.proxy=false` in that file.

## Google map's Geocoding API
The Google Maps [Geocoding API](https://developers.google.com/maps/documentation/geocoding/start) is a service that provides you the latitude and longitude of an addresses which is called  geocoding. It also supports reverse geocoding i.e. gives you the address from the latitude and longitude provided. To use this Geocoding API you need to first [register](https://developers.google.com/maps/documentation/geocoding/get-api-key) your application with Google using your Google ID. After registration Google gives you an API key which you need to update in the `application.properties` file as
```
config.apikey=<your key goes here>
```

## The distance comparison logic
The [Great-circle distance](https://en.wikipedia.org/wiki/Great-circle_distance) or orthodromic distance is the shortest distance between two points on the surface of a sphere, measured along the surface of the sphere (as opposed to a straight line through the sphere's interior). [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula) is used to calculate the Great-circle distance between two points on a sphere from their longitudes and latitudes. Refer this [article](http://www.movable-type.co.uk/scripts/latlong.html) for the formula.

## TO DO List
- [x] Add comments and JavaDoc
- [ ] Add logging
- [x] Move the API key to a property file
- [ ] Add proxy support with authenticator
