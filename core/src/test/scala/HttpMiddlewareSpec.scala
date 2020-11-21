import zio._
import zio.test._
import zio.test.Assertion._
import zio.web.http.HttpMiddleware

object HttpMiddlewareSpec extends DefaultRunnableSpec {

    def spec = suite("HTTP Middleware")(
        testM("basic") {

            val middleWare = HttpMiddleware.basicAuth((maybeCreds: Option[(String, String)]) => ZIO.unit)
            
        }
    )
    
}