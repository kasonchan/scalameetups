import org.specs2._
import org.specs2.specification.script.StandardDelimitedStepParsers

class Specs2GivenWhenThenFullSupport
    extends Specification
    with specification.dsl.GWT
    with StandardDelimitedStepParsers {
  def is = s2"""
 Given a first number {2}     $g1
 When multiply it by {3}      $w1
 Then I get {6}               $t1
"""
  var number = 0
  def g1 = step(anInt) { i =>
    number = i
  }

  def w1 = step(anInt) { j =>
    number = number * j
  }

  def t1 = example(anInt) { n =>
    number must_== n
  }
}
