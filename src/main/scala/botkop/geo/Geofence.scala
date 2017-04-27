package botkop.geo

import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.{Json, OFormat}

import scala.io.Source
import scala.language.postfixOps

case class Geofence(name: String, polygon: List[LatLng])

case object Geofence extends LazyLogging {
  implicit val f: OFormat[Geofence] = Json.format[Geofence]

  /**
    * Read geofence file in json format and return GeoFenceList object
    */
  def read(fileName: String): List[Geofence] =
    try {
      Json
        .parse(Source fromFile fileName mkString)
        .validate[List[Geofence]] get
    } catch {
      case e: Throwable =>
        logger.error("exception caught while reading geofence file", e)
        List[Geofence]()
    }

}
