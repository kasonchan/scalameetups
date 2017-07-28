# Into to Scala and SBT

## Scala

> Scalable Language (Scala).
> https://www.scala-lang.org/download/

Install Scala on your mac using terminal:
```
$ brew install scala
```
Start Scala interpreter's interactive mode, the REPL (pronounced as ripple) which is stands for read-evaluate-print loop.
```scala
$ scala
Welcome to Scala 2.12.3 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_65).
Type in expressions for evaluation. Or try :help.

scala>
```
To get help from the REPL:
```scala
scala> :help
All commands can be abbreviated, e.g., :he instead of :help.
:edit <id>|<line>        edit history
:help [command]          print this summary or command-specific help
:history [num]           show the history (optional num is commands to show)
:h? <string>             search the history
:imports [name name ...] show import history, identifying sources of names
:implicits [-v]          show the implicits in scope
:javap <path|class>      disassemble a file or class name
:line <id>|<line>        place line(s) at the end of history
:load <path>             interpret lines in a file
:paste [-raw] [path]     enter paste mode or paste a file
:power                   enable power user mode
:quit                    exit the interpreter
:replay [options]        reset the repl and replay all previous commands
:require <path>          add a jar to the classpath
:reset [options]         reset the repl to its initial state, forgetting all session entries
:save <path>             save replayable session to a file
:sh <command line>       run a shell command (result is implicitly => List[String])
:settings <options>      update compiler options, if possible; see reset
:silent                  disable/enable automatic printing of results
:type [-v] <expr>        display the type of an expression without evaluating it
:kind [-v] <type>        display the kind of a type. see also :help kind
:warnings                show the suppressed warnings from the most recent line which had any
```
We can create our hello world scala program by create a file called `HelloWorld.scala`:
```scala
$ vi HelloWorld.scala
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
```
We can execute the hello world program by:
```bash
$ scala HelloWorld.scala Scala
Hello World!
Hello Scala!
What is your favourite programming language?
Scala
My favourite programming language is Scala!
```
More information about String Interpolation: http://docs.scala-lang.org/overviews/core/string-interpolation.html

## Simple Build Tool (SBT)

> Interactive build tool (SBT) for Scala, Java and more. 
> http://www.scala-sbt.org/download.html

Install sbt on your mac using terminal:
```bash
$ brew install sbt
```
To get help from sbt interactive mode:
```bash
$ sbt
> help

  help                                    Displays this help message or prints detailed help on requested commands (run 'help <command>').
  completions                             Displays a list of completions for the given argument string (run 'completions <string>').
  about                                   Displays basic information about sbt and the build.
  tasks                                   Lists the tasks defined for the current project.
  settings                                Lists the settings defined for the current project.
  reload                                  (Re)loads the current project or changes to plugins project or returns from it.
  projects                                Lists the names of available projects or temporarily adds/removes extra builds to the session.
  project                                 Displays the current project or changes to the provided `project`.
  set [every] <setting>                   Evaluates a Setting and applies it to the current project.
  session                                 Manipulates session settings.  For details, run 'help session'.
  inspect [uses|tree|definitions] <key>   Prints the value for 'key', the defining scope, delegates, related definitions, and dependencies.
  <log-level>                             Sets the logging level to 'log-level'.  Valid levels: debug, info, warn, error
  plugins                                 Lists currently available plugins.
  ; <command> (; <command>)*              Runs the provided semicolon-separated commands.
  ~ <command>                             Executes the specified command whenever source files change.
  last                                    Displays output from a previous command or the output from a specific task.
  last-grep                               Shows lines from the last output for 'key' that match 'pattern'.
  export <tasks>+                         Executes tasks and displays the equivalent command lines.
  exit                                    Terminates the build.
  --<command>                             Schedules a command to run before other commands on startup.
  show <key>                              Displays the result of evaluating the setting or task associated with 'key'.
  all <task>+                             Executes all of the specified tasks concurrently.

More command help available using 'help <command>' for:
  !, +, ++, <, alias, append, apply, eval, iflast, onFailure, reboot, shell

```

## Intellij IDEA

> Scala Ecosystem survey: https://www.jetbrains.com/research/devecosystem-2017/scala/

I would encourage you to download the Intellij IDEA Community for developing Scala. There are some cool plugin support for developing Scala and from the communities.
For the future sessions, I will probably going to use Intellij IDEA for the whole time.
But if you don't really wanna to use Intellij, here is a bash script to generate a SBT Scala project structure.

```bash
vi generateSBTProject.sh
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

For Intellij IDEA user, create a new project by select Scala > SBT and click Next:
![SelectSBT](https://github.com/kasonchan/scalameetups/blob/scalameetup1/scalameetup1/images/SelectSBT.png)

Enter the project name scalameetup1 and click OK and you will have a project like this.
![scalameetup1](https://github.com/kasonchan/scalameetups/blob/scalameetup1/scalameetup1/images/scalameetup1.png)

We will have similar structure:
```bash
scalameetup1
├── build.sbt
├── project
│   └── build.properties # This file is mainly used to specify the version of sbt
├── src # This directory is where we code our main java/scala code
│   ├── main
│   │   ├── resources
│   │   └── scala
│   └── test # This directory is where we put our tests
│       ├── resources
│       └── scala
└── target # This directory is for the built generated java bytecodes and classes
```
Other files or directories are generated by IntelliJ and SBT.

Create a resources directory on your root directory of your project.

Create a file called `tags.txt` in this resources directory. We will need it for this session. Data will be read from this file.
tags.txt

```text
cc1,Cost Center,Group,Cost Center,123,Kason,Company,7/21
er1,Expense Report,Group,Expense Report,123,Kason,Company,3/21
e1,Expense 1,Group,Expense,123,Kason,Company,6/21
e2,Expense 2,Group,Expense,123,Kason,Company,7/21
e3,Expense 3,Group,Expense,123,Kason,Company,7/21
cc1,Cost Center,Group,Cost Center,123,Alice,Company,8/20
er1,Expense Report,Group,Expense Report,123,Alice,Company,5/21
e1,Expense 1,Group,Expense,123,Alice,Company,6/21
e2,Expense 2,Group,Expense,123,Alice,Company,8/20
e3,Expense 3,Group,Expense,123,Alice,Company,8/20
```

Create a `TagsDemo.scala` file in the `src/main/scala` directory.
Main.scala

```scala
// case class let you define a class with built in default constructor of the parameters and getter method. hashcode, equals, toString are come for free and they are serializable by default. You do not need the new keyword to instantiate the class as in line 19 Tag(...).
case class Tag(id: String, name: String, group: String, description: String, context: Context)
 
case class Context(user: User, system: String, timestamp: String)
 
case class User(id: String, name: String)
 
 
object TagsDemo {
 
  def main(args: Array[String]): Unit = {
    // Get the source from the file "resources/tags.txt" and save as a immutable value src
    val src = scala.io.Source.fromFile("resources/tags.txt")
    // .getLines read line by line from the source, we get an collection of strings
    // .map on each element we apply the the function line => line.split(",")
    // line => line.split(",") we parse the line and split each line using "," as delimiter to collection of strings
    // .toList convert the type to List because src.getLines is of type of Iterator
    val lines = src.getLines.map(line => line.split(",")).toList
 
    // .collect function build a new collection by applying the body partial function to each elements in this case, we want to transform data to Tags
    val tags = lines.collect {
      // If it match Array type here, I map the strings to Tag, I the strings in the Array differently to distinguish them easily
      case Array(tagId, name, group, description, userId, userName, system, timestamp) =>
        // Some is one option of the Option container
        Some(Tag(tagId, name, group, description, Context(User(userId, userName), system, timestamp)))
      // case _ => matches everything else from above and produce a None, None is one option of the Option container/monand
      case _ => None
    // .flatten extract filter out the None type and produce a pure collection of Tag
    }.flatten
 
    println("All tags")
    // Print out each tag on a line
    for (tag <- tags) println(tag)
    println // Print an empty line
 
    // tags is a collection of tags
    // tag => tag.context.user.name == "Kason" is a anonymous function that returns true if user name match "Kason" otherwise returns false
    val kasonTags = tags.filter(tag => tag.context.user.name == "Kason")
    println("All tags with user name Kason")
    // Print out each kasonTag on a line
    for (kasonTag <- kasonTags) println(kasonTag)
  }
}
```

Run the program
```bash
$ sbt run
All tags
Some(Tag(cc1,Cost Center,Group,Cost Center,Context(User(123,Kason),Company,7/21)))
Some(Tag(er1,Expense Report,Group,Expense Report,Context(User(123,Kason),Company,3/21)))
Some(Tag(e1,Expense 1,Group,Expense,Context(User(123,Kason),Company,6/21)))
Some(Tag(e2,Expense 2,Group,Expense,Context(User(123,Kason),Company,7/21)))
Some(Tag(e3,Expense 3,Group,Expense,Context(User(123,Kason),Company,7/21)))
Some(Tag(cc1,Cost Center,Group,Cost Center,Context(User(123,Alice),Company,8/20)))
Some(Tag(er1,Expense Report,Group,Expense Report,Context(User(123,Alice),Company,5/21)))
Some(Tag(e1,Expense 1,Group,Expense,Context(User(123,Alice),Company,6/21)))
Some(Tag(e2,Expense 2,Group,Expense,Context(User(123,Alice),Company,8/20)))
Some(Tag(e3,Expense 3,Group,Expense,Context(User(123,Alice),Company,8/20)))
 
All tags with user name Kason
Some(Tag(cc1,Cost Center,Group,Cost Center,Context(User(123,Kason),Company,7/21)))
Some(Tag(er1,Expense Report,Group,Expense Report,Context(User(123,Kason),Company,3/21)))
Some(Tag(e1,Expense 1,Group,Expense,Context(User(123,Kason),Company,6/21)))
Some(Tag(e2,Expense 2,Group,Expense,Context(User(123,Kason),Company,7/21)))
Some(Tag(e3,Expense 3,Group,Expense,Context(User(123,Kason),Company,7/21)))
```

Incremental compilation by adding `~` in front of run `sbt ~run`. This allows auto compilation and execute of your code change every time you edit and save the project.

## Comprehensive references to Scala libraries

- https://scala.zeef.com/ivano.pagano
- Scaladex: https://index.scala-lang.org/
