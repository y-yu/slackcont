package com.github.yyu.slackcont.cont.impl

import com.github.yyu.slackcont.TestUtil.{await, smartMock, takeout}
import com.github.yyu.slackcont.cont.SlackCont.SlackEnv
import com.github.yyu.slackcont.infra.SlackClient
import org.scalatest.{MustMatchers, WordSpec}
import slack.models.Message
import scala.concurrent.ExecutionContext

class AddContSpec extends WordSpec {
  import MustMatchers._

  trait SetUp {
    val mockSlackClient = smartMock[SlackClient]
    implicit val dummyExecutionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

    val fakeEnv = SlackEnv(
      mockSlackClient,
      dummyExecutionContext
    )
  }

  "apply" should {
    "pass a sum of x + y if the message.text matches the regex" in new SetUp {
      val message = Message(
        ts = "ts",
        channel = "channel",
        user = "user",
        text = "1 + 1",
        is_starred = None,
        thread_ts = None
      )

      val actual = AddCont(message)

      await(takeout(actual, fakeEnv)) must be(Some(2))
    }

    "return failure if the number of message.text is not integer" in new SetUp {
      val message = Message(
        ts = "ts",
        channel = "channel",
        user = "user",
        text = s"11111111111111111111111111111 + 1",
        is_starred = None,
        thread_ts = None
      )

      val actual = AddCont(message)

      intercept[NumberFormatException]{
        await(takeout(actual, fakeEnv))
      }
    }

    "not pass any number if the message.text does not match the regex" in new SetUp {
      val message = Message(
        ts = "ts",
        channel = "channel",
        user = "user",
        text = s"1 +++++ 1",
        is_starred = None,
        thread_ts = None
      )

      val actual = AddCont(message)

      await(takeout(actual, fakeEnv)) must be(None)
    }
  }
}
