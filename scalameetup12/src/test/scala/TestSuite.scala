import org.scalatest.{FunSpec, GivenWhenThen, Matchers}

import scala.collection.mutable

/**
  * @author kasonchan
  * @since Oct-2017
  */
class TestSuite extends FunSpec with Matchers with GivenWhenThen {

  describe("Mutable Set") {
    it("should allow an element to be added") {
      Given("an empty mutable Set")
      val testingFrameworks = mutable.Set.empty[String]

      When("an element is added")
      testingFrameworks += "ScalaTest"

      Then("the Set should have size 1")
      testingFrameworks.isEmpty shouldBe false
      testingFrameworks.size should equal(1)
      testingFrameworks should contain("ScalaTest")

      info("That's all for this mutable Set")
    }
  }

}
