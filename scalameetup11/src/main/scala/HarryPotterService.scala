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

@Api(value = "/harrypotter", produces = "application/json")
@Path("/harrypotter")
class HarryPotterService {

  implicit val messageFormat: RootJsonFormat[Message] = jsonFormat2(Message)

  final def routes: Route =
    pathPrefix("harrypotter") {
      pathEnd {
        get {
          println(Message(StatusCodes.OK.intValue, Seq("Harry Potter Series!")))
          complete(
            Message(StatusCodes.OK.intValue, Seq("Harry Potter Series!")))
        }
      }
    }

}
