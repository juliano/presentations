import br.com.lambda3.spa._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle with DatabaseInit {
  
  override def init(context: ServletContext) {
    configureDb()
    context.mount(new SpaServlet, "/*")
  }
  
  override def destroy(context:ServletContext) {
    closeDbConnection()
  }
}
