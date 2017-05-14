import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class AdvertsControllersSpec extends Specification {

  "AdvertsController" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beSome.which (status(_) == NOT_FOUND)
    }

    "send 200 on a request to the endpoint to get all the adverts" in new WithApplication{
      Seq("/adverts", "/adverts/") foreach { page =>
        val getAllEndpoint = route(FakeRequest(GET, page)).get

        status(getAllEndpoint) must equalTo(OK)
        contentType(getAllEndpoint) must beSome.which(_ == "application/json")
        contentAsString(getAllEndpoint) must contain ("title")
      }
    }
  }
}
