import org.scalatest.{FunSpec, GivenWhenThen, Matchers, Tag}

import scala.collection.mutable

/**
  * @author kasonchan
  * @since Oct-2017
  */
case class Person(first: String, last: String, number: Int)

class FunSpecShouldTestSuite extends FunSpec with Matchers with GivenWhenThen {

  describe("Mutable Set") {
    it("should allow an element to be added") {
      Given("an empty mutable Set")
      val testingFrameworks = mutable.Set.empty[String]

      When("an element is added")
      testingFrameworks += "ScalaTest"

      Then("the Set should have size 1")
      testingFrameworks.isEmpty shouldBe false
      testingFrameworks.size should equal(1)
      testingFrameworks.size should be(1)
      testingFrameworks should contain("ScalaTest")

      info("That's all for this mutable Set")

      And("list should have size 3")
      val list = 2 :: 3 :: 4 :: Nil
      list.size should be(3)
      list should have size (3)
      list should have length (3)
      list should contain(4)
      list should not contain (5)

      list should ((contain(3)) and not(contain(1)))
      list should ((have length (3)) or have length (2))
      list should (not be (null) and contain(2))

      And("list is empty")
      List() should be('empty)
      //      List() should not be ('empty)

      val string =
        """This is a scala meetup about Testing in Scala Basics Tour."""
      string should startWith("This is")
      string should endWith("Basics Tour.")
      string should include("about Testing")

      string should startWith regex ("This.is+")
      string should endWith regex ("T.{2}r.")
      string should not include regex("flames?")

      string should fullyMatch regex ("""This(.|\n|\S)*Tour.""")

      And("number should be 7")
      val number = 7
      number should be(7)
      number should equal(7)
      number should not equal (12)
      number should be > (3)
      number should be < (14)

      (0.9 - 0.8) should be(0.1 +- .01)

      And("Person test passed")
      val me = Person("kason", "chan", 12)
      val you = me

      me should be theSameInstanceAs (you)

      val he = Person("tomas", "higgens", 1)
      me should not be theSameInstanceAs(he)

      me should have(
        'first ("kason"),
        'last ("chan"),
        'number (12)
      )

      val maps = Map(1 -> "One", 2 -> "Two", 3 -> "Three")
      maps should contain key (1)
      maps should contain value ("Two")
      maps should not contain key(7)

      And("9 / 0 should throw an java.lang.ArithmeticException")
      intercept[java.lang.ArithmeticException] {
        9 / 0
      }
    }

    it("can be tagged with Tags", Tag("tagged")) {}
  }

  describe("Pending tests") {
    it("This test is pending") { pending }
  }

  describe("Ignore tests") {
    ignore("This test is ignored") {
      // Ignored tests
    }
  }

  describe("Tagged Tests") {
    it("can be tagged with Tags", Tag("tagged")) {}
  }

}
