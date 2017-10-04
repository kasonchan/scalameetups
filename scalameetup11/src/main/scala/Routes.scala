import java.util.Calendar

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json.DefaultJsonProtocol.{jsonFormat2, _}
import spray.json.RootJsonFormat

/**
  * @author kasonchan
  * @since Sep-2017
  */
case class Message(statusCode: Int, messages: Seq[String])

case class User(name: String, id: String)

case class Context(createdBy: User)

case class Product(name: String,
                   id: String,
                   description: String,
                   context: Context)

case class TimedContext(createdBy: User,
                        createdOn: Long,
                        updatedBy: User,
                        updatedOn: Long)

case class TimedProduct(name: String,
                        id: String,
                        description: String,
                        context: TimedContext)

case class TimedProducts(products: Seq[TimedProduct])

case class Tag(name: String, product: Product)

class Routes {

  implicit val messageFormat: RootJsonFormat[Message] = jsonFormat2(Message)

  implicit val UserFormat: RootJsonFormat[User] = jsonFormat2(User)
  implicit val ContextFormat: RootJsonFormat[Context] = jsonFormat1(Context)

  implicit val ProductFormat: RootJsonFormat[Product] = jsonFormat4(Product)

  implicit val TimedContextFormat: RootJsonFormat[TimedContext] = jsonFormat4(
    TimedContext)
  implicit val TimedProductFormat: RootJsonFormat[TimedProduct] = jsonFormat4(
    TimedProduct)

  implicit val ProductsFormat: RootJsonFormat[TimedProducts] = jsonFormat1(
    TimedProducts)

  implicit val TagFormat: RootJsonFormat[Tag] = jsonFormat2(Tag)

  final def routes: Route =
    pathPrefix("v1") {
      pathEnd {
        println(Message(StatusCodes.OK.intValue, Seq("Version 1")))
        complete(Message(StatusCodes.OK.intValue, Seq("Version 1")))
      } ~
        pathPrefix("products") {
          pathEnd {
            println(Data.products)
            complete(TimedProducts(Data.products))
          } ~
            path("product") {
              parameters('id) { (id) =>
                println(Data.products.filter(_.id == id))
                complete(TimedProducts(Data.products.filter(_.id == id)))
              } ~
                post {
                  entity(as[Product]) { product =>
                    val timedProduct = TimedProduct(
                      name = product.name,
                      id = product.id,
                      description = product.description,
                      context = TimedContext(
                        createdBy = product.context.createdBy,
                        createdOn = Calendar.getInstance().getTime.getTime,
                        updatedBy = product.context.createdBy,
                        updatedOn = Calendar.getInstance().getTime.getTime
                      )
                    )
                    Data.products = Data.products :+ timedProduct
                    println(timedProduct)
                    complete(timedProduct)
                  }
                }
            }
        }
    }

}
