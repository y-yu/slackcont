package com.github.yyu.slackcont.infra.impl

import com.github.yyu.slackcont.TestUtil._
import com.github.yyu.slackcont.cont.SlackCont
import com.github.yyu.slackcont.infra.SlackClient
import com.github.yyu.slackcont.infra.provider.{ExecutionContextProvider, SlackRtmClientProvider}
import org.mockito.Mockito
import org.scalatest.{MustMatchers, WordSpec}
import slack.rtm.SlackRtmClient
import scala.concurrent.{ExecutionContext, Future}

class SlackRunnerImplSpec extends WordSpec {
  import MustMatchers._

  trait SetUp {
    val mockSlackRtmClient = smartMock[SlackRtmClient]
    val mockSlackRtmClientProvider = smartMock[SlackRtmClientProvider]
    val mockSlackClient = smartMock[SlackClient]
    val dummyExecutionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    val mockExecutionContextProvider = smartMock[ExecutionContextProvider]

    Mockito.when(mockSlackRtmClientProvider.get()).thenReturn(mockSlackRtmClient)
    Mockito.when(mockExecutionContextProvider.get()).thenReturn(dummyExecutionContext)

    val sut = new SlackRunnerImpl(
      mockSlackRtmClientProvider,
      mockSlackClient,
      mockExecutionContextProvider
    )
  }

  "onMessage" should {
    "run a SlackCont" in new SetUp {
      val slackCont = SlackCont[Unit] { env =>
        k =>
          k(())
      }

      sut.onMessage(_ => slackCont) must be(())
    }

    "return a unit value if SlackCont returns failure" in new SetUp {
      val slackCont = SlackCont[Unit] { env =>
        k =>
          Future.failed(new RuntimeException)
      }

      sut.onMessage(_ => slackCont) must be(())
    }
  }

  "onEvent" should {
    "run a SlackCont" in new SetUp {
      val slackCont = SlackCont[Unit] { env =>
        k =>
          k(())
      }

      sut.onEvent(_ => slackCont) must be(())
    }

    "return a unit value if SlackCont returns failure" in new SetUp {
      val slackCont = SlackCont[Unit] { env =>
        k =>
          Future.failed(new RuntimeException)
      }

      sut.onEvent(_ => slackCont) must be(())
    }
  }

}
