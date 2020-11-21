import zio.test._
import zio.test.Assertion._
import zio.random._

object GeneratorSpec extends DefaultRunnableSpec {
  final case class User(name: String, age: Int)

  val intGen: Gen[Random, Int] = Gen.anyInt

  lazy val genName: Gen[Random with Sized, String] =
    Gen.anyASCIIString

  lazy val genAge: Gen[Random, Int] =
    Gen.int(18, 120)

  lazy val genUser: Gen[Random with Sized, User] =
    for {
        name <- genName
        age <- genAge
    } yield User(name, age)

  def spec = suite("GeneratorsSepc")(
    testM("integer addition is associative") {
      check(intGen, intGen, intGen) { (x, y, z) =>
        val left  = (x + y) + z
        val right = x + (y + z)
        assert(left)(equalTo(right))
      }
    },
    testM("random user assertion") {
        check(genUser) { (user) =>
            assert(user.age)(isGreaterThanEqualTo(18))
        }
    }
  )
}
