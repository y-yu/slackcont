package com.github.yyu.slackcont.cont

import com.github.yyu.slackcont.infra.SlackClient
import scalaz.{ContT, Monad}
import scala.concurrent.{ExecutionContext, Future}

object SlackCont {
  case class SlackEnv(client: SlackClient, ec: ExecutionContext)

  type SlackCont[A] = SlackEnv => ContT[Future, Unit, A]

  def apply[A](f: SlackEnv => (A => Future[Unit]) => Future[Unit]): SlackCont[A] =
    env => ContT(f(env))

  def fromFuture[A](future: => Future[A]): SlackCont[A] = { env =>
    implicit val ec: ExecutionContext = env.ec

    ContT(k => future.flatMap(k))
  }

  def successful[A](a: A): SlackCont[A] =
    fromFuture(Future.successful(a))

  def failed(throwable: Throwable): SlackCont[Nothing] =
    fromFuture(Future.failed(throwable))

  def result[A](a: => A): SlackCont[A] = { env =>
    implicit val ec: ExecutionContext = env.ec

    ContT(_ => Future.successful(a))
  }

  implicit class MapFlatMap[A](slackCont: SlackCont[A])(implicit M: Monad[SlackCont]) {
    def map[B](f: A => B): SlackCont[B] = M.map(slackCont)(f)

    def flatMap[B](f: A => SlackCont[B]): SlackCont[B] = M.bind(slackCont)(f)

    def recover(slackCont: SlackCont[A])(pf: PartialFunction[Throwable, Future[Unit]]): SlackCont[A] = { env =>
      implicit val ec: ExecutionContext = env.ec

      ContT(k => slackCont(env).run(k).recoverWith(pf))
    }
  }

  implicit val slackContMonadInstance: Monad[SlackCont] = new Monad[SlackCont] {
    override def point[A](a: => A): SlackCont[A] =
      successful(a)

    override def bind[A, B](fa: SlackCont[A])(f: A => SlackCont[B]): SlackCont[B] = { env =>
      implicit val ec: ExecutionContext = env.ec

      for {
        a <- fa(env)
        b <- f(a)(env)
      } yield b
    }
  }
}
