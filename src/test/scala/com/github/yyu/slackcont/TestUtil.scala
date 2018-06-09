package com.github.yyu.slackcont

import com.github.yyu.slackcont.cont.SlackCont.{SlackCont, SlackEnv}
import org.mockito.Mockito
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.runtime.BoxedUnit

object TestUtil {
  def smartMock[T: ClassTag](): T =
    Mockito.mock(implicitly[ClassTag[T]].runtimeClass, Mockito.RETURNS_SMART_NULLS).asInstanceOf[T]

  def await[A](fa: Future[A], timeout: Duration = Duration.Inf): A =
    Await.result(fa, timeout)

  def takeout[A](s: SlackCont[A], env: SlackEnv)(implicit ec: ExecutionContext): Future[Option[A]] = {
    var box: Option[A] = None

    s(env).run { x =>
      box = Some(x)
      Future.successful(())
    }.map(_ => box)
  }
}
