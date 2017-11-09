import org.specs2.Specification

/**
  * @author kasonchan
  * @since Nov-2017
  */
class Specs2TestSuite1 extends Specification {
  def is = s2"""

 This is my first specification
   it is working                 $ok
   really working!               $ok
                                 """
}
