package botkop.geo

import play.api.libs.json.Json

import scala.io.Source
import scala.language.postfixOps

case class Geofence(name: String, polygon: List[LatLng])

case object Geofence {
    implicit val f = Json.format[Geofence]

    /**
      * Read geofence file in json format and return GeoFenceList object
      */
    def read(fileName: String) =
        try {
            Json.parse(Source fromFile fileName mkString).validate[List[Geofence]] get
        } catch {
            case e: Throwable =>

                //TODO: println with log statement that can be used in both scala 2.10 and 2.11
                println("exception caught while reading geofence file: {}", e)
                List[Geofence]()
        }

}

