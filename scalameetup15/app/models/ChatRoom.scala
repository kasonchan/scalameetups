package models

import java.util.Calendar

import actors.SystemActor
import akka.actor.Actor
import play.api.libs.iteratee.{Concurrent, Enumerator, Iteratee}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author kasonchan
  * @since Nov-2017
  */
case class Join(user: String)

case class Leave(user: String)

case class Broadcast(message: String)

class ChatRoom extends Actor {

  // A set of users' list
  var users = Set[String]()

  // Enumerator channel
  val (enumerator, channel) = Concurrent.broadcast[String]

  def receive = {

    case Join(user: String) => {
      // Log the join user
      SystemActor.log ! ("%s: %s" format (self.path, Join(user)))

      if (!users.contains(user)) {
        val iteratee = Iteratee
          .foreach[String] { msg =>
            self ! Broadcast("%s: %s" format (user, msg))
          }
          .mapM { _ =>
            Future {
              self ! Leave(user)
            }
          }

        // Add new user to the users list
        users += user

        // Send the message to channel
        channel.push(
          "%s %s has joined the room, now the chatroom has %s users"
            format (Calendar.getInstance.getTime, user, users.size))

        sender() ! (iteratee, enumerator)
      } else {
        val enumerator = Enumerator(
          "%s %s has already joined the room" format (Calendar.getInstance.getTime, user))

        // Ignore user messages
        val iteratee = Iteratee.ignore

        // Return the iteratee and enumerator to action
        sender() ! (iteratee, enumerator)
      }
    }
    case Leave(user) => {
      // Log the leave user
      SystemActor.log ! ("%s %s: %s" format (Calendar.getInstance.getTime, self.path, Leave(
        user)))

      // Remove user from the users list
      users -= user

      // Add the msg to the channel
      channel.push(
        "%s has left the room, %s users left"
          format (user, users.size))
    }
    case Broadcast(msg: String) => {
      // Log the msg
      SystemActor.log ! ("%s %s: %s" format (Calendar.getInstance.getTime, self.path, Broadcast(
        msg)))

      // Add the msg to channel
      channel.push(s"${Calendar.getInstance.getTime} $msg")
    }
    case x => {
      // Log error
      SystemActor.log ! ("%s %s: %s" format (Calendar.getInstance.getTime, self.path, x))
    }
  }

}
