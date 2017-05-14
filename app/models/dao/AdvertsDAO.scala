package models.dao

import models.Advert
import org.joda.time.DateTime

import scala.collection.mutable

object AdvertsDAO {
  // Temporary in-memory repository
  var adverts = mutable.Buffer[Advert] (
    new Advert(1, "Audi A4", "gasoline", 10000, false, Option(100000), Option(DateTime.parse("2010"))),
    new Advert(2, "BMW 5", "gasoline", 15000, false, Option(120000), Option(DateTime.parse("2009"))),
    new Advert(3, "Volkswagen Touran", "diesel", 4500, false, Option(140000), Option(DateTime.parse("2004"))),
    new Advert(4, "Opel Astra", "gasoline", 20000, true)
  )

  def add(advert: Advert) = {
    adverts = adverts :+ advert
  }

  def edit(id: Int, advert: Advert) = {
    adverts.filter(_.id == id).lift(0) match {
      case Some(advert) =>
        val indexOfExistingAdvert = adverts.indexOf(advert)
        adverts(indexOfExistingAdvert) = advert
      case _ =>
    }
  }

  def delete(id: Int) = {
    adverts.filter(_.id == id).lift(0) match {
      case Some(advert) =>
        val indexOfExistingAdvert = adverts.indexOf(advert)
        adverts.remove(indexOfExistingAdvert)
      case _ =>
    }
  }

  def get(): List[Advert] = {
    adverts.toList
  }

  def get(id: Int): Option[Advert] = {
    val existingAdvert = adverts.filter(_.id == id).lift(0)
    existingAdvert
  }
}
