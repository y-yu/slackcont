package com.github.yyu.slackcont.cont.impl

import com.github.yyu.slackcont.TestUtil._
import com.github.yyu.slackcont.cont.SlackCont.SlackEnv
import com.github.yyu.slackcont.infra.SlackClient
import com.github.yyu.slackcont.infra.SlackClient.SlackApiException
import com.github.yyu.slackcont.util.ThreadSleep
import org.mockito.Mockito
import org.scalatest.{MustMatchers, WordSpec}
import scala.concurrent.{ExecutionContext, Future}

class TypingContSpec extends WordSpec {
  import MustMatchers._

  trait SetUp {
    val msec = 2000L
    val channel = "channel"

    val mockThreadSleep = smartMock[ThreadSleep]
    val mockSlackClient = smartMock[SlackClient]
    implicit val dummyExecutionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

    val fakeEnv = SlackEnv(
      mockSlackClient,
      dummyExecutionContext
    )

    Mockito.doNothing().when(mockThreadSleep).sleep(msec)

    val sut = new TypingCont(mockThreadSleep)
  }

  "apply" should {
    "indicate typing using Slack Client successfully" in new SetUp {
      Mockito.when(mockSlackClient.indicateTyping(channel)).thenReturn(Future.successful(()))

      val actual = sut(channel, msec)

      await(takeout(actual, fakeEnv)) must be(Some(()))
      Mockito.verify(mockThreadSleep, Mockito.times(1)).sleep(msec)
    }

    "execute a continuation even if fail to indicate typing" in new SetUp {
      Mockito.when(mockSlackClient.indicateTyping(channel)).thenReturn(Future.failed(SlackApiException()))

      val actual = sut(channel, msec)

      await(takeout(actual, fakeEnv)) must be(Some(()))
      Mockito.verify(mockThreadSleep, Mockito.times(1)).sleep(msec)
    }
  }
}
