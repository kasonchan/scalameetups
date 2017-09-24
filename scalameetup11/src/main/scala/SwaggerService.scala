import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import io.swagger.models.auth.BasicAuthDefinition

/**
  * @author kasonchan
  * @since Sep-2017
  */
object SwaggerService extends SwaggerHttpService {
  override val apiClasses = Set(classOf[HarryPotterService])
  override val host = "localhost:9000"
  override val info = Info(version = "1.0")
  override val securitySchemeDefinitions = Map(
    "basicAuth" -> new BasicAuthDefinition())
}
