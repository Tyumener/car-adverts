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
    val existingAdvert = adverts.filter(_.id == id).head
    val indexOfExistingAdvert = adverts.indexOf(existingAdvert)
    adverts(indexOfExistingAdvert) = advert
  }

  def delete(id: Int) = {
    val existingAdvert = adverts.filter(_.id == id).head
    val indexOfExistingAdvert = adverts.indexOf(existingAdvert)
    adverts.remove(indexOfExistingAdvert)
  }

  def get(): List[Advert] = {
    adverts.toList
  }

  def get(id: Int): Advert = {
    val existingAdvert = adverts.filter(_.id == id).head
    existingAdvert
  }
}
