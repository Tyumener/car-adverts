package services

import javax.inject.Inject
import models.Advert
import repositories.Repository

class AdvertsDataService @Inject()(repository: Repository[Advert]) extends DataService[Advert] {

  def add(advert: Advert) : Option[String] = {
    repository.add(advert) match {
      case Some(errorMessage) =>
        Some(errorMessage)
      case None =>
        None
    }
  }

  def edit(id: Int, advert: Advert): Option[Advert] = {
    repository.edit(id, advert) match {
      case Some(modifiedAdvert) =>
        Some(advert)
      case None =>
        None
    }
  }

  def delete(id: Int) = {
    repository.delete(id)
  }

  def getAll() : List[Advert] = {
    getAllSortInt(a => a.id)
  }

  def getAllSortInt(f: Advert => Int): List[Advert] = {
    repository.get.sortBy(f)
  }

  def getAllSortString(f: Advert => String): List[Advert] = {
    repository.get.sortBy(f)
  }

  def get(id: Int): Option[Advert] = {
    repository.get(id) match {
      case Some(advert) =>
        Some(advert)
      case None =>
        None
    }
  }
}
