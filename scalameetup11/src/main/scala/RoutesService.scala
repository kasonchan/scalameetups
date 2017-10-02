import javax.ws.rs._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.swagger.annotations.Api
import spray.json.DefaultJsonProtocol.{jsonFormat2, _}
import spray.json.RootJsonFormat

/**
  * @author kasonchan
  * @since Sep-2017
  */
case class Message(statusCode: Int, messages: Seq[String])

case class Tag(name: String)

class RoutesService {

  implicit val messageFormat: RootJsonFormat[Message] = jsonFormat2(Message)
  implicit val TagFormat: RootJsonFormat[Tag] = jsonFormat1(Tag)

  final def routes: Route =
    pathPrefix("v1") {
      pathEnd {
        println(Message(StatusCodes.OK.intValue, Seq("Version 1")))
        complete(Message(StatusCodes.OK.intValue, Seq("Version 1")))
      } ~
        pathPrefix("tags") {
          pathEnd {
            println(Message(StatusCodes.OK.intValue, Seq("tags")))
            complete(Message(StatusCodes.OK.intValue, Seq("tags")))
          } ~
            path("tag") {
              get {
                complete("tag")
              } ~
                post {
                  entity(as[Tag]) { tag =>
                    complete(tag.name)
                  }
                }
            }
        } ~
        pathPrefix("taggings") {
          pathEnd {
            println(Message(StatusCodes.OK.intValue, Seq("taggings")))
            complete(Message(StatusCodes.OK.intValue, Seq("taggings")))
          }
        }
    }

}
