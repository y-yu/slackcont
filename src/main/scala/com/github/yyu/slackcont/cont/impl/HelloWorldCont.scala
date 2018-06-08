package com.github.yyu.slackcont.cont.impl

import com.github.yyu.slackcont.cont.SlackCont
import com.github.yyu.slackcont.cont.SlackCont.SlackCont
import slack.models.Message
import scala.concurrent.{ExecutionContext, Future}

/**
  * Helloというメッセージならば継続に"World"を渡す
  */
object HelloWorldCont {
  def helloWorldCont(message: Message): SlackCont[String] = SlackCont[String] { env =>
    implicit val ec: ExecutionContext = env.ec

    k =>
      for {
        _ <- if (message.text == "Hello") {
          k("World")
        } else {
          Future.successful(())
        }
      } yield ()
  }
}
