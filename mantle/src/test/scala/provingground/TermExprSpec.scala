package provingground

import HoTT._
import translation._

import org.scalatest.FlatSpec

@deprecated("Use expression spec", "April 2016")
class TermExprSpec extends FlatSpec {
  import HoTTSpec.{g, f, b, B, a, mp, A, Bs}

  val terms: List[Term] = List(g, f, b, B, a, mp, A)

  val Bf = lmbda(a)(Bs(a))

  val typs: List[Typ[Term]] =
    List(A, B, A ->: B, SigmaTyp(Bf), PiDefn(Bf), Type)

  implicit val lp: provingground.translation.LiteralParser.Empty.type = LiteralParser.Empty

  "A term without literals" should "give itself on mapping by TermExpr.simple and then _.asTerm" in {
    terms foreach ((x) => assert(TermExpr.simple(x).asTerm == x))
    typs foreach ((x) => assert(TermExpr.simple(x).asTerm == x))
  }

  "A type without literals" should "give itself on mapping by TermExpr.simple and then _.asTyp" in {
    typs foreach ((x) => assert(TermExpr.simple(x).asTyp == x))
  }
}
