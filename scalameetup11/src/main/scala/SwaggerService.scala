import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import io.swagger.models.auth.BasicAuthDefinition

/**
  * @author kasonchan
  * @since Sep-2017
  */
object SwaggerService extends SwaggerHttpService {
<<<<<<< HEAD
  override val apiClasses = Set(classOf[RoutesService])
=======
  override val apiClasses = Set(classOf[HarryPotterService])
>>>>>>> 1e0d535a7897921c6847aff88579d8bc4f66a82f
  override val host = "localhost:9000"
  override val info = Info(version = "1.0")
  override val securitySchemeDefinitions = Map(
    "basicAuth" -> new BasicAuthDefinition())
}
