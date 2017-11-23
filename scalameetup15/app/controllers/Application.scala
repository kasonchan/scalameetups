package controllers

import javax.inject.{Singleton, _}

import actors.SystemActor
import akka.actor.{ActorSystem, Props}
import models.ChatRoom
import play.i18n.{Lang, Messages}
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.mvc.{AbstractController, Action, AnyContent, _}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * @author kasonchan
  * @since Nov-2017
  */
@Singleton
class Application @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(
    implicit exec: ExecutionContext)
    extends AbstractController(cc) {

  val concatIteratee = Iteratee.fold("") { (cons: String, chunk: String) =>
    cons.concat(chunk)
  }

  def index: Action[AnyContent] = Action.async { implicit request =>
    actorSystem.log.info("Index")

    // Create enumerator of Hello World
    val enumerator: Enumerator[String] =
      Enumerator("Hello", " World!").andThen(Enumerator.eof)

    // Create iteratee future iterate enumerator
    val iterateeFuture: Future[Iteratee[String, String]] =
      enumerator(concatIteratee)

    // Iterate the enumerator
    val resultFuture: Future[String] = enumerator run concatIteratee

    // Wait for the result for 3 seconds
    val result = Await.result(resultFuture, 3.seconds)

    // Print the result to the screen
    Future.successful(Ok(views.html.chatHome(Some(result))))
  }

}
