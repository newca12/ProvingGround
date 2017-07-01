package provingground

import provingground.{FiniteDistribution => FD, ProbabilityDistribution => PD}

import learning._

// import breeze.linalg.{Vector => _, _}
// import breeze.stats.distributions._
//
// import breeze.plot._
//
import scala.concurrent._
//
// import scala.util.Try

import scala.concurrent.ExecutionContext.Implicits.global

import HoTT._

// import FineDeducer._

case class NextSample(p: FD[Term],
                      ded: FineDeducer = FineDeducer(),
                      vars: Vector[Term] = Vector(),
                      size: Int = 1000,
                      derTotalSize: Int = 1000,
                      epsilon: Double = 0.2,
                      inertia: Double = 0.3) {
  @volatile lazy val init = ded.evolve(p)

  import Sampler._

  @volatile lazy val nextSamp = sample(init, size)

  @volatile lazy val nextFD = toFD(nextSamp) * (1.0 - inertia) ++ (p * inertia)

  @volatile lazy val nextTypSamp = sample(ded.evolveTyp(p), size)

  @volatile lazy val nextTypFD = toFD(nextTypSamp).map {
    case tp: Typ[u] => tp: Typ[Term]
  }

  @volatile lazy val thmFeedback = TheoremFeedback(nextFD, nextTypFD, vars)

  def derivativePD(tang: FD[Term]): PD[Term] = ded.Devolve(nextFD, tang)

  /**
    * Sample sizes for tangents at atomic vectors
    */
  @volatile lazy val derSamplesSizes = sample(nextFD, derTotalSize)

  /**
    * Finite distributions as derivatives at nextFD of the atomic tangent vectors with chosen sample sizes.
    */
  @volatile lazy val derFDs =
    derSamplesSizes map {
      case (x, n) =>
        val tang = FD.unif(x) //tangent vecror, atom at `x`
        val dPD =
          ded.Devolve(nextFD, tang) //recursive distribution based on derivative for sampling
        val samp = sample(dPD, n)
        val DdPd = ded.DevolveTyp(nextFD, tang)
        val Dsamp = sample(DdPd, n).map {
          case (tp: Typ[u], n) => (tp: Typ[Term], n)
        }
        (x, (toFD(samp), toFD(Dsamp)))
    }

  @volatile lazy val feedBacks =
    derFDs map {
      case (x, (tfd, tpfd)) =>
        x -> thmFeedback.feedbackTermDist(tfd, tpfd)
    }

  def derivativeFD(p: FD[Term], n: Int) = toFD(sample(derivativePD(p), n))

  // def vecFlow(vec: FD[Term], n: Int) =
  //   thmFeedback.feedbackTermDist(derivativeFD(p, n))
  //
  // def termFlow(x: Term, n: Int) = vecFlow(FD.unif(x), n)
  //
  // def totalFlow(totalSize: Int): Map[Term, Double] =
  //   (sample(nextFD, totalSize) map {
  //         case (x, n) =>
  //           val flow = termFlow(x, n)
  //           x -> flow
  //       }).toMap

  def shiftedFD(epsilon: Double) = {
    val tf    = feedBacks // totalFlow(totalSize)
    val shift = (x: Term) => tf.getOrElse(x, 0.0)

    val pmf =
      nextFD.pmf map {
        case Weighted(x, p) =>
          Weighted(x, p * math.exp(shift(x) * epsilon))
      }

    FD(pmf).flatten.normalized()
  }

  @volatile lazy val succFD = shiftedFD(epsilon)

  @volatile lazy val succ = this.copy(p = succFD)

  def iter = Iterator.iterate(this)(_.succ)

  def buf = BufferedRun(iter)
}

case class BufferedRun[A](iter: Iterator[A]) {
  var live: Boolean = true
  def stop() = {
    println(s"Sending halt signal to $this")
    live = false
  }
  val it = iter.takeWhile((_) => {
    if (!live) println(s"Halted $this")
    live
  })
  val timeseries = scala.collection.mutable.ArrayBuffer[A]()
  Future {
    it.foreach((ns) => timeseries.append(ns))
  }
}
