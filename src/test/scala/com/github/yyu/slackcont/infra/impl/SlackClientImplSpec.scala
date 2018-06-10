package com.github.yyu.slackcont.infra.impl

import com.github.yyu.slackcont.TestUtil._
import com.github.yyu.slackcont.infra.SlackClient.{SlackApiException, SlackEditMessage, SlackSendMessage}
import com.github.yyu.slackcont.infra.provider.SlackRtmClientProvider
import org.mockito.Mockito
import org.scalatest.{MustMatchers, WordSpec}
import slack.rtm.SlackRtmClient
import scala.concurrent.{ExecutionContext, Future}

class SlackClientImplSpec extends WordSpec {
  import MustMatchers._

  trait SetUp {
    val sendMessage = SlackSendMessage(
      channelId = "channel",
      text = "text",
      threadTs = None
    )

    val editMessage = SlackEditMessage(
      channelId = "channel",
      ts = "ts",
      text = "text"
    )

    val channelId = "channel"

    val mockSlackRtmClient = smartMock[SlackRtmClient]
    val mockSlackRtmClientProvider = smartMock[SlackRtmClientProvider]
    implicit val dummyExecutionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

    Mockito.when(mockSlackRtmClientProvider.get()).thenReturn(mockSlackRtmClient)

    val sut = new SlackClientImpl(
      mockSlackRtmClientProvider,
    )
  }

  "sendMessage" should {
    "send a message successfully" in new SetUp {
      Mockito.when(mockSlackRtmClient.sendMessage(sendMessage.channelId, sendMessage.text, sendMessage.threadTs))
        .thenReturn(Future.successful(1L))

      val actual = sut.sendMessage(sendMessage)

      await(actual) must be(1L)
    }

    "return SlackApiException if the client returns any errors" in new SetUp {
      Mockito.when(mockSlackRtmClient.sendMessage(sendMessage.channelId, sendMessage.text, sendMessage.threadTs))
        .thenReturn(Future.failed(new RuntimeException))

      val actual = sut.sendMessage(sendMessage)

      intercept[SlackApiException] {
        await(actual)
      }
    }
  }

  "editMessage" should {
    "edit a message successfully" in new SetUp {
      Mockito.doNothing().when(mockSlackRtmClient).editMessage(editMessage.channelId, editMessage.ts, editMessage.text)

      val actual = sut.editMessage(editMessage)

      await(actual) must be(())
    }

    "return SlackApiException if the client returns any errors" in new SetUp {
      Mockito.when(mockSlackRtmClient.editMessage(editMessage.channelId, editMessage.ts, editMessage.text))
        .thenThrow(new RuntimeException)

      val actual = sut.editMessage(editMessage)

      intercept[SlackApiException]{
        await(actual)
      }
    }
  }

  "indicateTyping" should {
    "indicate typing successfully" in new SetUp {
      Mockito.doNothing().when(mockSlackRtmClient).indicateTyping(channelId)

      val actual = sut.indicateTyping(channelId)

      await(actual) must be(())
    }

    "return SlackApiException if the client returns any errors" in new SetUp {
      Mockito.when(mockSlackRtmClient.indicateTyping(channelId))
        .thenThrow(new RuntimeException)

      val actual = sut.indicateTyping(channelId)

      intercept[SlackApiException]{
        await(actual)
      }
    }
  }
}
