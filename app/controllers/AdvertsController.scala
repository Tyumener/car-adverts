package controllers

import models.Advert
import models.dao.AdvertsDAO
import play.api.mvc._
import play.api.libs.json._

class AdvertsController extends Controller {

  def getAll = Action {
    var adverts = AdvertsDAO.get
    val json = Json.toJson(adverts.map{a => Json.toJson(a)})
    Ok(json)
  }

  def get(id: Int) = Action {
    AdvertsDAO.get(id) match {
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
        AdvertsDAO.add(advert) match {
          case None =>
            Created.withHeaders(LOCATION -> routes.AdvertsController.get(advert.id).absoluteURL)
          case Some(errorMessage) =>
            BadRequest(errorMessage)
        }
      }
    )
  }

  def edit(id: Int) = Action(parse.json) { implicit request =>
    request.body.validate[Advert].fold(
      error => BadRequest(Json.obj("err" -> "Json was not correct")),
      advert => {
        val advert = request.body.as[Advert]
        AdvertsDAO.edit(id, advert) match {
          case None =>
            NotFound
          case Some(_) =>
            NoContent
        }
      }
    )
  }

  def delete(id: Int) = Action {
    AdvertsDAO.delete(id)
    NoContent
  }
}
