import org.specs2.Specification

/**
  * @author kason.chan
  * @since Nov-2017
  */
class Specs2ChainingTestSuite extends Specification {
  def is = s2"""

 This is my first specification
   it is working                 $ok
   really working!               $ok
                                 """
}
