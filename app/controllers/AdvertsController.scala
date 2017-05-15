package controllers

import javax.inject.Inject

import models.Advert
import play.api.mvc._
import play.api.libs.json._
import services.DataService

class AdvertsController @Inject() (dateService: DataService[Advert]) extends Controller {

  def add = Action(parse.json) { implicit request =>
    request.body.validate[Advert].fold(
      error => BadRequest(Json.obj("Error" -> JsError.toJson(error))),
      advert => {
        val advert = request.body.as[Advert]
        dateService.add(advert) match {
          case None =>
            try{
              val url = routes.AdvertsController.get(advert.id).absoluteURL
              Created.withHeaders(LOCATION -> url)
            }
            catch {
              case _ => Created
            }
          case Some(errorMessage) =>
            BadRequest(errorMessage)
        }
      }
    )
  }

  def edit(id: Int) = Action(parse.json) { implicit request =>
    request.body.validate[Advert].fold(
      error => BadRequest(Json.obj("Error" -> "Json was not correct")),
      advert => {
        val advert = request.body.as[Advert]
        dateService.edit(id, advert) match {
          case None =>
            NotFound
          case Some(_) =>
            NoContent
        }
      }
    )
  }

  def delete(id: Int) = Action {
    dateService.delete(id)
    NoContent
  }

  def getAll(sortBy: Option[String] = Some("id")) = Action {
    // This code is supper smelly and needs to be rewritten
    sortBy match {
      case Some("id") =>
        val adverts = dateService.getAllSortInt(a => a.id)
        val json = Json.toJson(adverts.map{a => Json.toJson(a)})
        Ok(json)
      case Some("price") =>
        val adverts = dateService.getAllSortInt(a => a.price)
        val json = Json.toJson(adverts.map{a => Json.toJson(a)})
        Ok(json)
      case Some("mileage") =>
        val adverts = dateService.getAllSortString(a => a.mileage.toString)
        val json = Json.toJson(adverts.map{a => Json.toJson(a)})
        Ok(json)
      case Some("title") =>
        val adverts = dateService.getAllSortString(a => a.title)
        val json = Json.toJson(adverts.map{a => Json.toJson(a)})
        Ok(json)
      case Some("fuel") =>
        val adverts = dateService.getAllSortString(a => a.fuel)
        val json = Json.toJson(adverts.map{a => Json.toJson(a)})
        Ok(json)
      case Some("firstRegistration") =>
        val adverts = dateService.getAllSortString(a => a.firstRegistration.toString)
        val json = Json.toJson(adverts.map{a => Json.toJson(a)})
        Ok(json)
      case Some("isNew") =>
        var adverts = dateService.getAllSortString(a => a.isNew.toString)
        val json = Json.toJson(adverts.map{a => Json.toJson(a)})
        Ok(json)
      case _ =>
        var adverts = dateService.getAllSortInt(a => a.id)
        val json = Json.toJson(adverts.map{a => Json.toJson(a)})
        Ok(json)
    }
  }

  def get(id: Int) = Action {
    dateService.get(id) match {
      case Some(advert) =>
        val json = Json.toJson(advert)
        Ok(json)
      case None =>
        val json = Json.obj("Error" -> "Not found")
        NotFound(json)
    }
  }
}
