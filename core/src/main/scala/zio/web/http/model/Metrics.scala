package zio.web.http.model

/*

    To properly implement this I need:
    - Insert metrics sorted by time
    
    Or emit metrics aggregated by clock intervals.

*/

final case class Metrics(
  histogram: Map[HttpStatusCode, Int]
) {
  def processHttpResponse(status: HttpStatusCode): Metrics =
    this.copy(histogram = histogram + (status -> histogram.getOrElse(status, 0)))
}

object Metrics {
  def apply(): Metrics = Metrics(histogram = Map())
}

final case class HttpServerStats(
  total: Long,
  minutely: Metrics,
  hourly: Metrics,
  daily: Metrics
) {

  def processHttpResponse(status: HttpStatusCode): HttpServerStats =
    this.copy(
      total = this.total + 1,
      minutely = this.minutely.processHttpResponse(status),
      hourly = this.hourly.processHttpResponse(status),
      daily = this.daily.processHttpResponse(status)
    )
}

object HttpServerStats {
  def apply(): HttpServerStats = HttpServerStats(0, minutely = Metrics(), hourly = Metrics(), daily = Metrics())
}
