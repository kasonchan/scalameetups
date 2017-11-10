import org.specs2.Specification

/**
  * @author kasonchan
  * @since Nov-2017
  */
class Specs2TestSuite extends Specification {

  val list = 2 :: 3 :: 4 :: Nil

  val testFrameworks = <frameworks>
    <framework name="ScalaTest"/>
    <framework name="Specs2"/>
    <framework name="ScalaMock"/>
    <framework name="ScalaCheck"/>
  </frameworks>

  def is =
    s2"""

 This is my first specification
   it is working                 $ok
   really working!               $ok
 
 This is simple specification
   list size must be 3           $listSpecs


 This is a specification for the 'Hello world' string
   The 'Hello world' string should
     contain 11 characters                             $e1
     start with 'Hello'                                $e2
     end with 'world'                                  $e3
     
 This is a specification for XML
   testFrameworks contains all correct data $testFrameworksSpecs
      """

  def listSpecs = {
    list.size mustEqual (3)
  }

  def e1 = "Hello world" must haveSize(11)

  def e2 = "Hello world" must startWith("Hello")

  def e3 = "Hello world" must endWith("world")

  def testFrameworksSpecs = {
    testFrameworks must be_==(<frameworks>
      <framework name="ScalaTest"/>
      <framework name="Specs2"/>
      <framework name="ScalaMock"/>
      <framework name="ScalaCheck"/>
    </frameworks>).ignoreSpace
  }

}
