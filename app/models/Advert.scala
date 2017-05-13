package models

import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._

case class Advert(id: Int,
                  title: String,
                  fuel: String,
                  price: Int,
                  isNew: Boolean,
                  mileage: Option[Int] = None,
                  firstRegistration: Option[DateTime] = None)

object Advert {
  implicit val advertWrites: Writes[Advert] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "title").write[String] and
      (JsPath \ "fuel").write[String] and
      (JsPath \ "price").write[Int] and
      (JsPath \ "isNew").write[Boolean] and
      (JsPath \ "mileage").writeNullable[Int] and
      (JsPath \ "firstRegistration").writeNullable[DateTime]
    ) (unlift(Advert.unapply))

  implicit val advertReads: Reads[Advert] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "title").read[String] and
    (JsPath \ "fuel").read[String] and
    (JsPath \ "price").read[Int] and
    (JsPath \ "isNew").read[Boolean] and
    (JsPath \ "mileage").readNullable[Int] and
    (JsPath \ "firstRegistration").readNullable[DateTime]
  )(Advert.apply _)
}
