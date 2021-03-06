package models

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.libs.json.Reads._
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

  val jodaDateReads = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString)
    )
  )

  val dateFormat = "yyyy-MM-dd"
  val jodaDateWrites: Writes[DateTime] = new Writes[DateTime] {
    def writes(d: DateTime): JsValue = JsString(d.toString(dateFormat))
  }

  implicit val advertWrites: Writes[Advert] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "title").write[String] and
      (JsPath \ "fuel").write[String] and
      (JsPath \ "price").write[Int] and
      (JsPath \ "isNew").write[Boolean] and
      (JsPath \ "mileage").writeNullable[Int] and
      (JsPath \ "firstRegistration").writeNullable[DateTime](jodaDateWrites)
    ) (unlift(Advert.unapply))

  val onlyAllowedFuelType: Reads[String] = {
    val allowedFuelTypes = Seq("gasoline", "diesel")
    Reads.StringReads.filter(ValidationError("Invalid fuel type"))(s => allowedFuelTypes.contains(s))
  }

  implicit val advertReads: Reads[Advert] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "title").read[String] and
    (JsPath \ "fuel").read[String](onlyAllowedFuelType) and
    (JsPath \ "price").read[Int](min[Int](0)) and
    (JsPath \ "isNew").read[Boolean] and
    (JsPath \ "mileage").readNullable[Int] and
    (JsPath \ "firstRegistration").readNullable[DateTime](jodaDateReads)
  )(Advert.apply _)
    .filterNot(ValidationError("New car can't have mileage and/or first registration defined"))(f =>
    (f.isNew && f.mileage.isDefined) || (f.isNew && f.firstRegistration.isDefined))

}
