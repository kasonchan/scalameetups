// Note that we can use java libraries easily by just importing them.
import java.io.File
import java.nio.file.{Files, Paths}

// This library is for creating process to execute some commands below.
import sys.process._

import scala.util.{Failure, Success, Try}

/**
  * @author kasonchan
  * @since Jul-2017
  */
object GenerateSBTProject {
  def main(args: Array[String]): Unit = {
    // Control flow of if {...} else {...} in Scala is the same syntax and semantics as Java
    if (args.length == 4) {
      // Synatic-sugar for matching the values to the variable
      val Array(sbtVersion, projectName, projectVersion, scalaVersion) = args

      // Try {...} catches error or exceptions execute in the block
      // match is a pattern matching, result of Try can either be Success or Failure
      Try {
        // Create the basic directories
        val path = Paths.get(
          s"$projectName/src/main/resources/../scala/../../test/resources/../scala")
        Files.createDirectories(path)

        // Create the project directory
        val project: File = new File(s"$projectName/project")
        project.mkdir

        // Create build.properties
        import java.io.PrintWriter
        val buildPropPW =
          new PrintWriter(new File(s"$projectName/project/build.properties"))
        buildPropPW.write(s"sbt.version=$sbtVersion")
        buildPropPW.close()

        // Create build.sbt
        val buildPW = new PrintWriter(new File(s"$projectName/build.sbt"))
        buildPW.write(s"""name := \"$projectName\"\n\n""")
        buildPW.write(s"""version := \"$projectVersion\"\n\n""")
        buildPW.write(s"""scalaVersion := \"$scalaVersion\"\n""")
        buildPW.close()
      } match {
        case Success(message) =>
          // Console.BLUE changees the text color to blue
          // Console.RESET resets the setting back to normal
          println(
            Console.BLUE + s"Created sbt project ${projectName} successfully." + Console.RESET)
          println(s"tree $projectName".!)
        case Failure(error) =>
          // error is a Throwable
          // .getMessage gets the message of the error
          Console.err.println(Console.RED + error.getMessage + Console.RESET)
      }
    } else {
      Console.err.println(
        Console.RED + "Invalid number of arguments." + Console.RESET)
      Console.err.println(
        Console.RED + "Usage: scala GenerateSBTStructure.scala [sbt version] [project name] [project version] [scala version]." + Console.RESET)
      Console.err.println(
        Console.RED + "You must specify the sbt version, project name, project version, and scala version." + Console.RESET)
      System.exit(1)
    }
  }
}
