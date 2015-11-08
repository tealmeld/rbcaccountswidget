package controllers

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import scaldi.Module
import scaldi.play.ScaldiApplicationBuilder
import services.SimpleUUIDGenerator

/**
 * You can mock out a whole application including requests, plugins etc.

 */
class ApplicationIT extends Specification {

  class TestModule extends Module {
    binding to new SimpleUUIDGenerator
  }

  val application = new ScaldiApplicationBuilder().prependModule(new TestModule).build()

  "Application" should {

    /* --FIXME
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }
    */

    "render the index page" in {
      running(application) {
        val home = route(FakeRequest(GET, "/")).get
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
      }
    }

  }
}