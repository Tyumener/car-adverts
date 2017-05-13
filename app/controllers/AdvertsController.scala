package controllers

import models.Advert
import play.api.mvc._
import play.api.libs.json._

class AdvertsController extends Controller {

 def get = Action {
    Ok(???)
  }

  def get(id: Int) = Action {
    Ok(???)
  }

  def add(advert: Advert) = Action(parse.json) { implicit request =>
    request.body.validate[Advert].fold(
      error => BadRequest(Json.obj("err" -> "Json was not correct")),
      advert => Created(???)
    )
  }

  def edit(id: Int, advert: Advert) = Action(parse.json) { implicit request =>
    request.body.validate[Advert].fold(
      error => BadRequest(Json.obj("err" -> "Json was not correct")),
      advert => NotImplemented //NoContent
    )
  }

  def delete(id: Int) = Action {
    NotImplemented //NoContent
  }
}
