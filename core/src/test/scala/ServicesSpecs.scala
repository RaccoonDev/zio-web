import zio._
import zio.duration._
import zio.console._
import zio.test._
import zio.test.Assertion._
import zio.test.environment.TestConsole
import zio.test.TestAspect._
import zio.clock.Clock
import zio.test.environment.TestClock

object ServicesSpec extends DefaultRunnableSpec {

  val greet: ZIO[Console, Nothing, Unit] =
    for {
      name <- getStrLn.orDie
      _    <- putStrLn(s"Hello, $name!")
    } yield ()

  val delayedGreet: ZIO[Console with Clock, Nothing, Unit] =
    putStr(s"Hello!").delay(1.hour)

  def spec = suite("ServicesSpec")(
    testM("great says hello to the user") {
      for {
        _     <- TestConsole.feedLines("Jane")
        _     <- greet
        value <- TestConsole.output
      } yield assert(value)(equalTo(Vector("Hello, Jane!\n")))
    } @@ silent,
    testM("delayedGreet delays for one hour") {
      for {
        _     <- delayedGreet.fork
        _     <- TestClock.adjust(1.hour)
        value <- TestConsole.output
      } yield assert(value)(equalTo(Vector("Hello!")))
    } @@ silent,
    testM("test would be repeated to ensure that test is stable") {
      for {
        ref   <- Ref.make(0)
        _     <- ZIO.foreachPar(1 to 10000)(_ => ref.update(_+ 1))
        value <- ref.get
      } yield assert(value)(equalTo(10000))
    } @@ nonFlaky
  )

}
