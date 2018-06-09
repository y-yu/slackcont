package com.github.yyu.slackcont.cont.impl

import com.github.yyu.slackcont.TestUtil.{await, smartMock, takeout}
import com.github.yyu.slackcont.cont.SlackCont.SlackEnv
import com.github.yyu.slackcont.infra.SlackClient
import com.github.yyu.slackcont.infra.SlackClient.{SlackApiException, SlackSendMessage}
import org.mockito.Mockito
import org.scalatest.{MustMatchers, WordSpec}
import scala.concurrent.{ExecutionContext, Future}

class SayContSpec extends WordSpec {
  import MustMatchers._

  trait SetUp {
    val sendMessage = SlackSendMessage(
      channelId = "channel",
      text = "text",
      threadTs = None
    )

    val mockSlackClient = smartMock[SlackClient]
    implicit val dummyExecutionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

    val fakeEnv = SlackEnv(
      mockSlackClient,
      dummyExecutionContext
    )
  }

  "apply" should {
    "send a message using Slack Client successfully" in new SetUp {
      Mockito.when(mockSlackClient.sendMessage(sendMessage)).thenReturn(Future.successful(1L))

      val actual = SayCont(sendMessage.channelId, sendMessage.text)

      await(takeout(actual, fakeEnv)) must be(Some(1L))
      Mockito.verify(mockSlackClient, Mockito.times(1)).sendMessage(sendMessage)
    }

    "not execute a continuation if fail to send a message" in new SetUp {
      Mockito.when(mockSlackClient.sendMessage(sendMessage)).thenReturn(Future.failed(SlackApiException()))

      val actual = SayCont(sendMessage.channelId, sendMessage.text)

      intercept[SlackApiException]{
        await(takeout(actual, fakeEnv))
      }
    }
  }
}
