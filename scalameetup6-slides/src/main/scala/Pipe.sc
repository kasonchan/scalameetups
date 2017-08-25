implicit class Pipe[T](x: T) {
  def |>[U](f: T => U): U = f(x)
}

val piped = 3 |> addOne |> sum

def sum(x: String): Int = 10

def addOne(x: Int): String = s"< $x >"

val composed = sum _ compose addOne

val andThened = addOne _ andThen sum

composed(3)

andThened(3)
