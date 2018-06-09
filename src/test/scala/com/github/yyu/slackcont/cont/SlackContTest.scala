package com.github.yyu.slackcont.cont

import java.util.concurrent.Executor
import com.github.yyu.slackcont.TestUtil._
import com.github.yyu.slackcont.cont.SlackCont.{SlackCont, SlackEnv}
import org.mockito.Mockito
import scalaprops._
import scalaz._
import scalaz.std.anyVal._
import scalaprops.Scalaprops
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try
import scala.util.Success
import scala.util.Failure

object SlackContTest extends Scalaprops {
  private implicit val dummyExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(new BlockingExecutor)

  class BlockingExecutor extends Executor {
    def execute(command: Runnable): Unit = {
      command.run()
    }
  }

  case class Error(value: Int) extends RuntimeException

  implicit def genError(implicit I: Gen[Int]): Gen[Error] =
    I.map(x => Error(x))

  implicit def equalError(implicit I: Equal[Int]): Equal[Error] =
    I.contramap(_.value)

  implicit def equalFuture[A](implicit A: Equal[A], E: Equal[Error]): Equal[Future[A]] =
    Equal.equal { (a, b) =>
      (Try(Await.result(a, 5 seconds)), Try(Await.result(b, 5 seconds))) match {
        case (Success(va), Success(vb)) => A.equal(va, vb)
        case (Failure(ea@Error(_)), Failure(eb@Error(_))) => E.equal(ea, eb)
        case _ => false
      }
    }

  implicit def genFuture[A](implicit A: Gen[A], E: Gen[Error]): Gen[Future[A]] =
    Gen.frequency(
      1 -> E.map(Future.failed),
      10 -> A.map(Future.successful)
    )

  val mockSlackEnv = smartMock[SlackEnv]
  Mockito.when(mockSlackEnv.ec).thenReturn(dummyExecutionContext)

  implicit def equalSlackCont[A](implicit F: Equal[Future[Unit]]): Equal[SlackCont[A]] =
    F.contramap(x => x(mockSlackEnv).run(x => Future.successful(())))

  implicit def genSlackCont[A](implicit F: Gen[Future[A]]): Gen[SlackCont[A]] =
    F.map(fa => SlackCont.fromFuture(fa))

  val slackContMonadLawsTest = Properties.list(
    scalazlaws.monad.all[SlackCont]
  )
}
