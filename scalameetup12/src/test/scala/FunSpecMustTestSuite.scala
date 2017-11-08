import org.scalatest.{FunSpec, GivenWhenThen, MustMatchers}

import scala.collection.mutable

/**
  * @author kasonchan
  * @since Oct-2017
  */
class FunSpecMustTestSuite
    extends FunSpec
    with MustMatchers
    with GivenWhenThen {

  describe("Mutable Set") {
    it("should allow an element to be added") {
      Given("an empty mutable Set")
      val testingFrameworks = mutable.Set.empty[String]

      When("an element is added")
      testingFrameworks += "ScalaTest"

      Then("the Set should have size 1")
      testingFrameworks.isEmpty must be(false)
      testingFrameworks.size must equal(1)
      testingFrameworks.size must be(1)
      testingFrameworks must contain("ScalaTest")

      info("That's all for this mutable Set")

      And("list size must be 3")
      val list = 2 :: 3 :: 4 :: Nil
      list.size must be(3)
      list.size must equal(3)
      //      list.size must not be (3)

      And("list must be empty")
      List() must be('empty)

      And("number must be 7")
      val number = 7
      number must equal(7)
    }
  }

}
