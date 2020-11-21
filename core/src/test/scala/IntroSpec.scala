// import zio.duration._
import zio.{Promise, UIO, ZIO, test => _}
import zio.test._
import zio.test.Assertion._
import zio.test.TestAspect._

object IntroSpec extends DefaultRunnableSpec {

  // type Assertion[A] = A => Boolean

  val myEffect: UIO[Int] = ZIO.succeed(1 + 1)

  val myEffect2: UIO[Int] =
    for {
      promise <- Promise.make[Nothing, Int]
      _       <- promise.succeed(2).fork
      value   <- promise.await
    } yield value

  val effect = ZIO.fail("fail")

  override def spec =
    suite("IntroSpec")(
      test("addition works") {
        assert(1 + 1)(equalTo(2))
      },
      testM("ZIO.succeeds with specified value") {
        assertM(myEffect2)(equalTo(2))
      },
      testM("testing an effect using a for complrehension") {
        for {
          x <- ZIO.succeed(1)
          y <- ZIO.succeed(2)
        } yield assert(x + y)(equalTo(3))
      } @@ ignore,
      testM("and") {
        for {
          x <- ZIO.succeed(1)
          y <- ZIO.succeed(2)
        } yield assert(x)(equalTo(1)) && assert(y)(equalTo(2))
      },
      testM("ZIO.fail fails with specified value") {
        assertM(effect.run)(fails(equalTo("fail")))
      }
    )
}
