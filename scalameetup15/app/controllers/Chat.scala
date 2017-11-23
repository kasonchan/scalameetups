package controllers

import javax.inject._

import actors.SystemActor
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import models.Join
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.mvc.{WebSocket, _}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

/**
  * @author kasonchan
  * @since Nov-2017
  */
@Singleton
class Chat @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(
    implicit exec: ExecutionContext)
    extends AbstractController(cc) {

  implicit val timeout = Timeout(1.seconds)

  /**
    * Chat function
    * @param user String: the name of the user
    * @return the future of chatroom screen
    */
  def chat(user: String) = Action.async { implicit request =>
    // Log the user
    SystemActor.log ! user

    // Show the chatroom screen
    Future.successful(Ok(views.html.chatBody(user)(request)))
  }

  /**
    * Chat socket
    * @param user String: the name of the user
    * @return the channel of iteratee and enumerator
    */
  def chatSocket(user: String) =
    WebSocket.acceptOrResult[String, String] { implicit request =>
      // Send the received msg to the chatroom
      val channelsFuture: Future[(Iteratee[String, _], Enumerator[String])] =
        ask(SystemActor.chatRoom, Join(user))
          .mapTo[(Iteratee[String, _], Enumerator[String])]

      channelsFuture.map { cf =>
        Right(cf)
      }

      Future.successful {
        Left(Forbidden("forbidden"))
      }
    }

  /**
    * User function
    * @return the redirection to chatroom
    */
  def user = Action.async { implicit request =>
    val username: String = request.body.asFormUrlEncoded.get("username")(0)

    SystemActor.log ! username

    // Redirect to the chatBody function
    Future.successful(Redirect("/chatBody/" + username))
  }

}
