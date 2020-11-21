import zio.web.http.model._

import zio.test._
import zio.test.Assertion._

object MetricsSpec extends DefaultRunnableSpec {

  override def spec = suite("Metrics")(
    suite("server stats")(
      test("instantiated with zero requests count") {
        val serverStats = HttpServerStats()
        assert(serverStats.total)(equalTo(0L))
      },
      test("one request contributes to the total count") {
        val serverStats = HttpServerStats()
        val updatedStats = serverStats.processHttpResponse(
          HttpStatusCode(200)
        )
        assert(updatedStats.total)(equalTo(1L))
      }
    )
  )

}
