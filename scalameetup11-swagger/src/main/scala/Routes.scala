import java.util.Calendar
import javax.ws.rs.Path

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import io.swagger.annotations._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.ExecutionContext

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

@Api(value = "/v1", produces = "application/json")
@Path("/v1")
class Routes(implicit executionContext: ExecutionContext) extends Directives {

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

  final def routes: Route = cors()(v1)

  def v1: Route = pathPrefix("v1") {
    pathEnd {
      println(Message(StatusCodes.OK.intValue, Seq("Version 1")))
      complete(Message(StatusCodes.OK.intValue, Seq("Version 1")))
    } ~ products
  }

  @Path("/products")
  @ApiOperation(
    value = "Get all products",
    notes = "Returns all products",
    httpMethod = "GET",
    produces = "application/json",
    response = classOf[TimedProducts]
  )
  @ApiResponses(
    Array(
      new ApiResponse(code = 500, message = "Internal server error")
    ))
  def products: Route = pathPrefix("products") {
    pathEnd {
      println(s"GET /products ${Data.products}")
      complete(TimedProducts(Data.products))
    } ~ getProductById ~ createProduct
  }

  @Path("/products/product/{id}")
  @ApiOperation(
    value = "Get product by product's id",
    notes = "Return a product with timestamp",
    httpMethod = "GET",
    produces = "application/json",
    response = classOf[TimedProduct]
  )
  @ApiImplicitParams(
    Array(
      new ApiImplicitParam(name = "id",
                           value = "Id of the product",
                           required = true,
                           dataType = "string",
                           paramType = "path")
    ))
  @ApiResponses(
    Array(
      new ApiResponse(code = 200,
                      message = "Return product",
                      response = classOf[TimedProduct]),
      new ApiResponse(code = 500, message = "Internal server error")
    ))
  def getProductById: Route = path("product" / Segment) { id =>
    get {
      println(s"GET /product/$id ${Data.products.filter(_.id == id)}")
      complete(TimedProducts(Data.products.filter(_.id == id)))
    }
  }

  @Path("/products/product")
  @ApiOperation(
    code = 201,
    value = "Create a product",
    notes = "Return a product with timestamp",
    httpMethod = "POST",
    consumes = "application/json",
    produces = "application/json",
    response = classOf[TimedProduct]
  )
  @ApiImplicitParams(
    Array(
      new ApiImplicitParam(name = "body",
                           value = "product",
                           required = true,
                           dataTypeClass = classOf[Product],
                           paramType = "body")
    ))
  @ApiResponses(
    Array(
      new ApiResponse(code = 500, message = "Internal server error")
    ))
  def createProduct: Route = path("product") {
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
        println(s"POST /product $timedProduct")
        complete(timedProduct)
      }
    }
  }

}
