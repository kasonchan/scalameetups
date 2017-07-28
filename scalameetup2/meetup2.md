# Scala Scripting, ScalaStyle and Scalafmt

We can run/write Scala code in many different ways:
As we mentioned in [Intro to Scala &#65120; SBT](https://github.com/kasonchan/scalameetups/blob/master/scalameetup1/meetup1.md), we can use Scala REPL.
You can also try using some online playground to run Scala code directly on your browser such as https://scalafiddle.io/ and https://scastie.scala-lang.org/.

For the following part of the notes, we will need `Scala` installed on your machine:
To install Scala on your mac using terminal:

```bash
$ brew install scala
```

Fundamental building blocks of Scala programs are expressions, types and values.
Types exist at compile time, values at run time.
```scala
$ scala
Welcome to Scala 2.12.2 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_45).
Type in expressions for evaluation. Or try :help.
scala> 1 + 2 + 3
res0: Int = 6
scala> res0 * 2
res1: Int = 12
scala> :type res0
Int
scala> println("Hello World!")
Hello World!
scala> :type println("Hello World!")
Unit
```
- res0 and res1 are identifier
- Int is a type
- 6 and 12 are value

Two distinct stages that a Scala program goes through - first it is compiled if successful then it can be evaluated. First stage is `compile time` and evaluation at `run time`.
Compile time check if the program makes sense or not: syntactically correct and type check. However, types do not necessarily contain all possible information about the values that conform to the type; therefore type system will not prevent us from dividing an Int by zero which causes a run time error. Use the type system smartly can much reduce run time errors.

Below is a bash script to generate a basic SBT structure:
```bash
# generateSBTProject.sh
#!/bin/sh
NAME=$1
PROJECT_VERSION=$2
SCALA_VERSION=$3
 
if [ ! $# == 3 ];then
    echo "Usage: generateSBTProject [project_name] [project_version] [scala_version]"
    echo "You must specify the sbt project name, project version and scala version of a file/directory to be removed."
    echo ""
    exit 1
fi
 
mkdir ${NAME}
cd ${NAME}
mkdir -p src/{main,test}/{java,resources,scala}
mkdir project target
echo 'sbt.version=0.13.15' > project/build.properties
 
# create an initial build.sbt file
echo "name := "${NAME}"
version := "${PROJECT_VERSION}"
scalaVersion := "${SCALA_VERSION}"" > build.sbt
```

In today's session, we are going to write a Scala script to generate the same basic structure.

Let's create a file called `generateSBTProject.scala`.

Note that in Scala, the class name does not need to match the filename as in Java. You can have multiple classes or objects in a `.scala` file.
```scala
$ vi GenerateSBTProject.scala
// Note that we can use java libraries easily by just importing them.
import java.io.File
import java.nio.file.{Files, Paths}
 
// This library is for creating process to execute some commands below.
import sys.process._
 
import scala.util.{Failure, Success, Try}
 
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
        val path = Paths.get(s"$projectName/src/main/resources/../scala/../../test/resources/../scala")
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
          println(Console.BLUE + s"Created sbt project ${projectName} successfully." + Console.RESET)
          println(s"tree $projectName".!)
        case Failure(error) =>
          // error is a Throwable
          // .getMessage gets the message of the error
          Console.err.println(Console.RED + error.getMessage + Console.RESET)
      }
    } else {
      Console.err.println(Console.RED + "Invalid number of arguments." + Console.RESET)
      Console.err.println(Console.RED + "Usage: scala GenerateSBTStructure.scala [sbt version] [project name] [project version] [scala version]." + Console.RESET)
      Console.err.println(Console.RED + "You must specify the sbt version, project name, project version, and scala version." + Console.RESET)
      System.exit(1)
    }
  }
}
```
We can execute the above script by:
```bash
$ scala GenerateSBTProject.scala
$ scala GenerateSBTProject.scala 0.13.15 scalameetup3 0.0.1 2.12.2
Created sbt project scalameetup2 successfully. # This print in blue.
scalameetup3
├── build.sbt
├── project
│   └── build.properties
└── src
    ├── main
    │   ├── resources
    │   └── scala
    └── test
        ├── resources
        └── scala
 
8 directories, 2 files
0
$ scala GenerateSBTProject.scala
# These print in red.
Invalid number of arguments.
Usage: scala GenerateSBTStructure.scala [sbt version] [project name] [project version] [scala version].
You must specify the sbt version, project name, project version, and scala version.
If you are interested in Scala Scripting and some more related tools, checkout http://www.lihaoyi.com/post/ScalaScriptingGettingto10.html.
```

## ScalaStyle
In the following section, we are going to import SBT plugin for static check your Scala code at compile time.

I highly recommend you to add this at the beginning of your new project. This enforce you to write Scala code in a set format and everyone need to follow; otherwise, the code won't compile if you set the rules to error.
We are going to use ScalaStyle here by create a file called `plugins.sbt` in your `project` directory.

```scala
resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
```

We can then generate a default `scalastyle-config.xml` file by using the following command, and of course, you can also manually create your own style sheet file.
```bash
$ sbt scalastyleGenerateConfig
```
I am attaching my adjusted style sheet here for your references.
scalastyle-config.xml
```
<scalastyle>
  <name>Scalastyle standard configuration</name>
  <check level="error" class="org.scalastyle.file.FileTabChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.file.FileLengthChecker" enabled="true">
    <parameters>
      <parameter name="maxFileLength"><![CDATA[800]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.file.HeaderMatchesChecker" enabled="false"></check>
  <check level="error" class="org.scalastyle.scalariform.SpacesAfterPlusChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.file.WhitespaceEndOfLineChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.SpacesBeforePlusChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.ProcedureDeclarationChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.file.FileLineLengthChecker" enabled="true">
    <parameters>
      <parameter name="maxLineLength"><![CDATA[160]]></parameter>
      <parameter name="tabSize"><![CDATA[2]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.ClassNamesChecker" enabled="true">
    <parameters>
      <parameter name="regex"><![CDATA[[A-Z][A-Za-z]*]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.ObjectNamesChecker" enabled="true">
    <parameters>
      <parameter name="regex"><![CDATA[[A-Z][A-Za-z]*]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.PackageObjectNamesChecker" enabled="true">
    <parameters>
      <parameter name="regex"><![CDATA[^[a-z][A-Za-z]*$]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.EqualsHashCodeChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.IllegalImportsChecker" enabled="true">
    <parameters>
      <parameter name="illegalImports"><![CDATA[sun._,java.awt._]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.ParameterNumberChecker" enabled="true">
    <parameters>
      <parameter name="maxParameters"><![CDATA[8]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.MagicNumberChecker" enabled="true">
    <parameters>
      <parameter name="ignore"><![CDATA[-1,0,1,2,3]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.NoWhitespaceBeforeLeftBracketChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.NoWhitespaceAfterLeftBracketChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.ReturnChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.NullChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.NoCloneChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.NoFinalizeChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.CovariantEqualsChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.StructuralTypeChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.file.RegexChecker" enabled="true">
    <parameters>
      <parameter name="regex"><![CDATA[println]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.NumberOfTypesChecker" enabled="true">
    <parameters>
      <parameter name="maxTypes"><![CDATA[30]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.CyclomaticComplexityChecker" enabled="true">
    <parameters>
      <parameter name="maximum"><![CDATA[10]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.UppercaseLChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.SimplifyBooleanExpressionChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.scalariform.IfBraceChecker" enabled="true">
    <parameters>
      <parameter name="singleLineAllowed"><![CDATA[true]]></parameter>
      <parameter name="doubleLineAllowed"><![CDATA[false]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.MethodLengthChecker" enabled="true">
    <parameters>
      <parameter name="maxLength"><![CDATA[50]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.MethodNamesChecker" enabled="true">
    <parameters>
      <parameter name="regex"><![CDATA[^[a-z][A-Za-z0-9]*$]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.NumberOfMethodsInTypeChecker" enabled="true">
    <parameters>
      <parameter name="maxMethods"><![CDATA[30]]></parameter>
    </parameters>
  </check>
  <check level="error" class="org.scalastyle.scalariform.PublicMethodsHaveTypeChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.file.NewLineAtEofChecker" enabled="true"></check>
  <check level="error" class="org.scalastyle.file.NoNewLineAtEofChecker" enabled="false"></check>
</scalastyle>
```
More information of ScalaStyle can be found on http://www.scalastyle.org.

## Scalafmt

> Code formatter for Scala http://scalameta.org/scalafmt/

Scala community starts to migrating to this tool for code formatting. Scalafmt turns the mess on the left into the (hopefully) readable, idiomatic and consistently formatted Scala code on the right.

We can use Scalafmt by installing plugin at IntelliJ.

![Scalafmt](https://github.com/kasonchan/scalameetups/blob/scalameetup2/scalameetup2/images/plugin.png)

We can default set to use Scalafmt by checking the Scalafmt format on save checkbox.
![Save](https://github.com/kasonchan/scalameetups/blob/scalameetup2/scalameetup2/images/save.png)

More information of Scalafmt can be found on http://scalameta.org/scalafmt/.
