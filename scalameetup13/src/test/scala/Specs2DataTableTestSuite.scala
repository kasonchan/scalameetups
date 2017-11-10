import org.specs2.Specification
import org.specs2.specification.Tables

/**
  * @author kason.chan
  * @since Nov-2017
  */
class Specs2DataTableTestSuite extends Specification with Tables {
  def is =
    s2"""

 adding integers should just work in scala ${// the header of the table, with `|` separated strings (`>` executes the table)
    "a" | "b" | "c" |>
      2 ! 2 ! 4 | // an example row
      1 ! 1 ! 2 | // another example row
      { (a, b, c) =>
        a + b must_== c
      } // the expectation to check on each row
    }
"""

}
