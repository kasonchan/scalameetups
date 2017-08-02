/**
  * @author kasonchan
  * @since Jul-2017
  */
object EvalDemo {
  def main(args: Array[String]): Unit = {
    val strictVarEval = StrictVarEval
    println(strictVarEval.x)
    strictVarEval.x = "StrictVarEval 2nd done."
    println(strictVarEval.x)

    val strictValEval = StrictValEval
    println(strictValEval.x)

    val lazyValEval = LazyValEval
    println(lazyValEval.x)

    var defEval = DefEval
    println(defEval.x)
  }
}

object StrictVarEval {
  var x = {
    println("Initializing StrictVarEval x")
    "StrictVarEval Done."
  }
}

object StrictValEval {
  val x = {
    println("Initializing StrictValEval x")
    "StrictValEval Done."
  }
}

object LazyValEval {
  lazy val x = {
    println("Initializing LazyValEval x")
    "LazyValEval Done."
  }
}

object DefEval {
  def x = {
    println("Initializing DefEval x")
    "DefEval Done."
  }
}
