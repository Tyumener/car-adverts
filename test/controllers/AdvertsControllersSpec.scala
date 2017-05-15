package controllers

import javax.inject.Inject

import models.Advert
import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.{JsArray, Json}
import play.api.test.Helpers._
import play.api.test._
import services.DataService

@RunWith(classOf[JUnitRunner])
class AdvertsControllersSpec @Inject()() extends Specification with Mockito {

  "AdvertsController" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beSome.which (status(_) == NOT_FOUND)
    }

    "should return all adverts when asked to" in new WithApplication{
      val mockDataService = mock[DataService[Advert]]
      val fakeAdverts = List[Advert] (
        new Advert(1, "Audi A4", "gasoline", 10000, false, Option(100000), Option(DateTime.parse("2010"))),
        new Advert(2, "BMW 5", "gasoline", 15000, false, Option(120000), Option(DateTime.parse("2009"))),
        new Advert(3, "Volkswagen Touran", "diesel", 4500, false, Option(140000), Option(DateTime.parse("2004"))),
        new Advert(4, "Opel Astra", "gasoline", 20000, true)
      )
      mockDataService.getAllSortInt(a => a.id) returns fakeAdverts
      val advertsController = new AdvertsController(mockDataService)

      Seq("/adverts", "/adverts/") foreach { page =>
        val request = FakeRequest(GET, page)

        val result = advertsController.getAll()(request)

        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "application/json")
        val json = Json.parse(contentAsString(result))
        var count = json.as[JsArray].value.size
        count must equalTo(fakeAdverts.size)
      }
    }

    "should return an advert if a provided id is valid" in new WithApplication {
      val mockDataService = mock[DataService[Advert]]
      mockDataService.get(anyInt) returns Some(new Advert(
        3, "Volkswagen Touran", "diesel", 4500, false, Option(140000), Option(DateTime.parse("2004"))))
      val advertsController = new AdvertsController(mockDataService)
      val request = FakeRequest(GET, "/adverts/3")

      val result = advertsController.get(3)(request)

      status(result) must equalTo(OK)
      contentType(result) must beSome.which(_ == "application/json")
      contentAsString(result) must contain("Volkswagen")
    }

    "should return NotFound if a provided id is invalid" in new WithApplication {
      val mockDataService = mock[DataService[Advert]]
      mockDataService.get(anyInt) returns None
      val advertsController = new AdvertsController(mockDataService)
      val request = FakeRequest(GET, "/adverts/3")

      var result = advertsController.get(3)(request)

      status(result) must equalTo(NOT_FOUND)
    }

    "should call add method on DataService and return CREATED" in new WithApplication {
      val mockDataService = mock[DataService[Advert]]
      mockDataService.add(any[Advert]) returns None
      val advertsController = new AdvertsController(mockDataService)
      val jsonString = """{"id":15,"title":"Volkswagen Touran","fuel":"gasoline","price":4,"isNew":false,"mileage":140000,"firstRegistration":"2014-01-01"}"""
      val request = FakeRequest(POST, "/adverts/", null, Json.parse(jsonString))

      var result = advertsController.add()(request)

      there was one (mockDataService).add(any[Advert])
      status(result) must equalTo(CREATED)
    }

    // Here comes a set of tests I'd also implement if I had more time

    "should return BAD_REQUEST when trying to add an advert multiple times" in new WithApplication {
    }

    "should return NOT_FOUND when trying to update an unexisting advert" in new WithApplication {
    }

    "should return NO_CONTENT when updating an existing advert" in new WithApplication {
    }

    "should return NO_CONTENT when deleting an existing advert" in new WithApplication {
    }

    "should return NO_CONTENT when trying to delete an unexisting item" in new WithApplication {
    }


  }
}
