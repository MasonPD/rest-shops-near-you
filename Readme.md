# Shops Near You API
Give you the facility to add a shop with address. Stores the longitude and latitude of your shop using Google Geocoding API. You can request for a shop mentioning you current latitude and longitude. The API would return you the shop nearest to you.

## The distance comparison logic
The [Great-circle distance](https://en.wikipedia.org/wiki/Great-circle_distance) or orthodromic distance is the shortest distance between two points on the surface of a sphere, measured along the surface of the sphere (as opposed to a straight line through the sphere's interior). [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula) is used to calculate the Great-circle distance between two points on a sphere from their longitudes and latitudes. Refer this [article](http://www.movable-type.co.uk/scripts/latlong.html) for the formula.
