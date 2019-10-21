package provingground.learning

import provingground._
import HoTT._
import monix.eval.Task

import scala.concurrent._
import duration._

import TermRandomVars._

import GeneratorVariables._, Expression._

import EntropyAtomWeight._

import Utils.bestTask

import monix.tail.Iterant

import collection.mutable.ArrayBuffer
import _root_.shapeless.Succ

object StrategicProvers {
  type Successes = Vector[(HoTT.Typ[HoTT.Term], Double, FiniteDistribution[HoTT.Term])]

  type SeekResult = (
      Successes,
      Set[EquationNode]
  )

  def formal(sc: Successes) : Set[EquationNode] = {
    val termsGroups = sc.toSet
    val terms = termsGroups.map(_._3).flatMap(_.support)
    terms.flatMap(t => DE.formalEquations(t))
  }

  var currentGoal: Option[Typ[Term]] = None

  def seekGoal(
      lp: LocalProver,
      typ: Typ[Term],
      scale: Double = 2,
      maxSteps: Int = 100
  ): Task[SeekResult] = {
    val base = lp.addGoals(typ -> 0.5, negate(typ) -> 0.5)
    currentGoal = Option(typ)
    val tasks = (1 until (maxSteps)).toVector.map { n =>
      val prover = lp.sharpen(math.pow(scale, n))
      val pair = for {
        sc  <- prover.successes
        eqs <- prover.equationNodes
      } yield (sc, eqs union formal(sc) )
      pair
    }

    bestTask[SeekResult](tasks, p => p._1.nonEmpty)
  }.map(_.getOrElse((Vector(), Set())))

  val successes : ArrayBuffer[Successes] = ArrayBuffer()

  def goalChomper(
      lp: LocalProver,
      typs: Vector[Typ[Term]],
      accumSucc: Vector[Successes] = Vector(),
      accumEqs: Set[EquationNode] = Set(),
      scale: Double = 2,
      maxSteps: Int = 100
  ): Task[(Vector[Successes], Set[EquationNode], Vector[Typ[Term]])] =
    typs match {
      case Vector() => Task.now((accumSucc, accumEqs, Vector()))
      case typ +: ys =>
        seekGoal(lp, typ, scale, maxSteps).flatMap { case (ss, eqs) =>
          if (ss.isEmpty) Task.now((accumSucc :+ ss, accumEqs union eqs, typ +: ys))
          else
            {   
                 successes.append(ss)
                 goalChomper(lp, ys, accumSucc :+ ss, accumEqs union eqs, scale, maxSteps)
            }
        }
    }
}
