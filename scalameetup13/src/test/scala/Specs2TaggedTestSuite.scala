import org.specs2.Specification

/**
  * @author kason.chan
  * @since Nov-2017
  */
class Specs2TaggedTestSuite extends Specification { def is = s2"""
 this is some introductory text
  and the first group of examples
  example 1 $success                         ${tag("tag")}
  example 2 $pending                         ${tag("integration")}

  and the second group of examples           ${section("checkin")}
  example 3 $success
  example 4 $failure                         ${section("checkin")}
                                             """ }
