package models

import org.joda.time.DateTime
import play.api.libs.json._

case class Advert(id: Int,
                  title: String,
                  fuel: String,
                  price: Int,
                  isNew: Boolean,
                  mileage: Option[Int],
                  firstRegistration: Option[DateTime])

object Advert {
  implicit val advertWrites: Writes[Advert] = (
    //TODO: Json serialization
  )
}
