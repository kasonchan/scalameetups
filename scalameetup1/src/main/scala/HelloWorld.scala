/**
  * @author kasonchan
  * @since Jul-2017
  */
// object keyword defines a singleton, HelloWorld is the name of this singleton.
object HelloWorld {
  // def is the keyword to define a function, main is the main entry of this program. args is array of string like java, you can pass in some string to this program.
  // :Unit in Scala is like void in Java, meaning returns any
  def main(args: Array[String]): Unit = {
    // Print "Hello World!" to the console.
    println("Hello World!")
    // String interoperation: s"$arg": For each arg of the array, prepend Hello to it and append ! at the end and print it out.
    for (arg <- args) println(s"Hello $arg!")

    println("What is your favourite programming language?")
    // Read a line from console
    val favourite = scala.io.StdIn.readLine()
    // String interoperation: s"$favourite": variable favourite is evaluated first and then put back to the string
    println(s"My favourite programming language is ${favourite}!")
  }
}
