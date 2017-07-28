/**
  * @author kasonchan
  * @since Jul-2017
  */
// case class let you define a class with built in default constructor of the parameters and getter method. hashcode,
// equals, toString are come for free and they are serializable by default. You do not need the new keyword to
// instantiate the class as in line 19 Tag(...).
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

    // .collect function build a new collection by applying the body partial function to each elements in this case,
    // we want to transform data to Tags
    val tags = lines.collect {
      // If it match Array type here, I map the strings to Tag, I the strings in the Array differently to distinguish
      // them easily
      case Array(tagId, name, group, description, userId, userName, system, timestamp) =>
        // Some is one option of the Option container
        Some(Tag(tagId, name, group, description, Context(User(userId, userName), system, timestamp)))
      // case _ => matches everything else from above and produce a None, None is one option of the Option
      // container/monand
      case _ => None
      // .flatten extract filter out the None type and produce a pure collection of Tag
    }.flatten

    println("All tags")
    // Print out each tag on a line
    for (tag <- tags) println(tag)
    println // Print an empty line

    // tags is a collection of tags
    // tag => tag.context.user.name == "Kason" is a anonymous function that returns true if user name match "Kason"
    // otherwise returns false
    val kasonTags = tags.filter(tag => tag.context.user.name == "Kason")
    println("All tags with user name Kason")
    // Print out each kasonTag on a line
    for (kasonTag <- kasonTags) println(kasonTag)
  }
}
