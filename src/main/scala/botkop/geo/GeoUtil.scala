package botkop.geo

import java.lang.Math._

/**
 * Translated from Java into Scala from:
 * https://github.com/googlemaps/android-maps-utils/tree/master/library/src/com/google/maps/android
 * classes MathUtil.java and PoyUtil.java
 */
object GeoUtil {

    /**
     * Computes whether the given point lies inside the specified polygon.
     * The polygon is always considered closed, regardless of whether the last point equals
     * the first or not.
     * Inside is defined as not containing the South Pole -- the South Pole is always outside.
     * The polygon is formed of great circle segments if geodesic is true, and of rhumb
     * (loxodromic) segments otherwise.
     */
    def containsLocation(point: LatLng, polygon: List[LatLng], geodesic: Boolean = true): Boolean = {
        val size = polygon.size
        if (size == 0) {
           return false
        }

        val lat3 = toRadians(point.lat)
        val lng3 = toRadians(point.lng)
        val prev = polygon(size - 1)
        var lat1 = toRadians(prev.lat)
        var lng1 = toRadians(prev.lng)
        var nIntersect = 0

        for (point2 <- polygon) {
            val dLng3 = wrap(lng3 - lng1, -PI, PI)
            // Special case: point equal to vertex is inside.
            if (lat3 == lat1 && dLng3 == 0) {
                return true
            }
            val lat2 = toRadians(point2.lat)
            val lng2 = toRadians(point2.lng)
            // Offset lngs by -lng1.
            if (intersects(lat1, lat2, wrap(lng2 - lng1, -PI, PI), lat3, dLng3, geodesic)) {
                nIntersect += 1
            }
            lat1 = lat2
            lng1 = lng2
        }
        (nIntersect & 1) != 0
    }

    /**
     * Computes whether the vertical segment (lat3, lng3) to South Pole intersects the segment
     * (lat1, lng1) to (lat2, lng2).
     * Longitudes are offset by -lng1. The implicit lng1 becomes 0.
     */
    def intersects(lat1: Double, lat2: Double, lng2: Double, lat3: Double, lng3:Double, geodesic: Boolean): Boolean ={
        // Both ends on the same side of lng3.
        if ((lng3 >= 0 && lng3 >= lng2) || (lng3 < 0 && lng3 < lng2)) {
            return false
        }
        // Point is South Pole.
        if (lat3 <= -PI/2) {
            return false
        }
        // Any segment end is a pole.
        if (lat1 <= -PI/2 || lat2 <= -PI/2 || lat1 >= PI/2 || lat2 >= PI/2) {
            return false
        }
        if (lng2 <= -PI) {
            return false
        }
        val linearLat = (lat1 * (lng2 - lng3) + lat2 * lng3) / lng2
        // Northern hemisphere and point under lat-lng line.
        if (lat1 >= 0 && lat2 >= 0 && lat3 < linearLat) {
            return false
        }
        // Southern hemisphere and point above lat-lng line.
        if (lat1 <= 0 && lat2 <= 0 && lat3 >= linearLat) {
            return true
        }
        // North Pole.
        if (lat3 >= PI/2) {
            return true
        }
        // Compare lat3 with lat on the GC/Rhumb segment corresponding to lng3.
        // Compare through a strictly-increasing function (tan() or mercator()) as convenient.
        if(geodesic)
            tan(lat3) >= tanLatGC(lat1, lat2, lng2, lng3)
        else
            mercator(lat3) >= mercatorLatRhumb(lat1, lat2, lng2, lng3)
    }

    /**
     * Returns tan(lat-at-lng3) on the great circle (lat1, lng1) to (lat2, lng2). lng1==0.
     * See http://williams.best.vwh.net/avform.htm .
     */
    def tanLatGC(lat1: Double, lat2: Double, lng2: Double, lng3: Double) =
        (tan(lat1) * sin(lng2 - lng3) + tan(lat2) * sin(lng3)) / sin(lng2)

    /**
     * Returns mercator(lat-at-lng3) on the Rhumb line (lat1, lng1) to (lat2, lng2). lng1==0.
     */
    def mercatorLatRhumb(lat1: Double, lat2: Double, lng2: Double, lng3: Double) =
        (mercator(lat1) * (lng2 - lng3) + mercator(lat2) * lng3) / lng2

    /**
     * Returns mercator Y corresponding to lat.
     * See http://en.wikipedia.org/wiki/Mercator_projection .
     */
    def mercator(lat: Double) =
        log(tan(lat * 0.5 + PI/4))

    /**
     * Returns lat from mercator Y.
     */
    def inverseMercator(y: Double) =
        2 * atan(exp(y)) - PI / 2

    /**
     * Wraps the given value into the inclusive-exclusive interval between min and max.
     */
    def wrap(n: Double, min: Double, max: Double) =
        if (n >= min && n < max) n else mod(n - min, max - min) + min

    /**
     * Returns the non-negative remainder of x / m.
     */
    def mod(x: Double, m: Double) =
        ((x % m) + m) % m

}
