package com.github.yyu.slackcont.cont.impl

import com.github.yyu.slackcont.TestUtil._
import com.github.yyu.slackcont.cont.SlackCont.SlackEnv
import com.github.yyu.slackcont.infra.SlackClient
import org.scalatest.{MustMatchers, WordSpec}
import slack.models.Message
import scala.concurrent.ExecutionContext

class HelloWorldContSpec extends WordSpec {
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
    "pass a word 'World' to a continuation if the message.text is 'Hello' successfully" in new SetUp {
      val message = Message(
        ts = "ts",
        channel = "channel",
        user = "user",
        text = "Hello",
        is_starred = None,
        thread_ts = None
      )

      val actual = HelloWorldCont(message)

      await(takeout(actual, fakeEnv)) must be(Some("World"))
    }

    "not pass any words to a continuation if the message.text is not 'Hello' successfully" in new SetUp {
      val message = Message(
        ts = "ts",
        channel = "channel",
        user = "user",
        text = "Not Hello",
        is_starred = None,
        thread_ts = None
      )

      val actual = HelloWorldCont(message)

      await(takeout(actual, fakeEnv)) must be(None)
    }
  }
}
