package controllers

import javax.inject.Inject

import models.Advert
import play.api.mvc._
import play.api.libs.json._
import services.DataService

class AdvertsController @Inject() (dateService: DataService[Advert]) extends Controller {

  def getAll = Action {
    var adverts = dateService.get
    val json = Json.toJson(adverts.map{a => Json.toJson(a)})
    Ok(json)
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
}
