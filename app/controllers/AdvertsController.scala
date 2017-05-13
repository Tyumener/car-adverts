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
    val json = Json.toJson(AdvertsDAO.get(id))
    Ok(json)
  }

  def add = Action(parse.json) { implicit request =>
    request.body.validate[Advert].fold(
      error => BadRequest(Json.obj("err" -> "Json was not correct")),
      advert => {
        val advert = request.body.as[Advert]
        AdvertsDAO.add(advert)
        Created
      }
    )
  }

  def edit(id: Int) = Action(parse.json) { implicit request =>
    request.body.validate[Advert].fold(
      error => BadRequest(Json.obj("err" -> "Json was not correct")),
      advert => {
        val advert = request.body.as[Advert]
        AdvertsDAO.edit(id, advert)
        NoContent
      }
    )
  }

  def delete(id: Int) = Action {
    AdvertsDAO.delete(id)
    NoContent
  }
}
