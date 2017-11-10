import org.specs2.Specification

class Specs2GivenWhenThen extends Specification {

  def is = s2"""
 Given a first number         $g1
 When I double it             $w1
 Then I get twice that number $t1
"""

  var number = 0

  def g1 = step {
    number = 1
  }

  def w1 = step {
    // do an action
    number += number
  }

  def t1 = number must_== 2

}
