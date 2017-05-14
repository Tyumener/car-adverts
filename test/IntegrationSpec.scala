import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {

  "Application" should {

    "work from within a browser" in new WithBrowser {

      browser.goTo(s"http://localhost:${port}/adverts")

      browser.pageSource must contain("title")
    }

    // Here comes a set of tests I'd also implement if I had more time

    "should return BAD_REQUEST when provided invalid json" in new WithBrowser {
    }

    "should return BAD_REQUEST and meaningfull error if provided invalid value for fuel type" in new WithBrowser {
    }

    "should return BAD_REQUEST and meaningfull error if provided negative price" in new WithBrowser {
    }

    "should return BAD_REQUEST if provided mileage and/or registration date for a new car" in new WithBrowser {
    }

    "should return CREATED if no mileage and/or registration provided for a new car" in new WithApplication {
    }
  }
}
