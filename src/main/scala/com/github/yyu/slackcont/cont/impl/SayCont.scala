package com.github.yyu.slackcont.cont.impl

import com.github.yyu.slackcont.cont.SlackCont
import com.github.yyu.slackcont.cont.SlackCont.SlackCont
import com.github.yyu.slackcont.infra.SlackClient.SlackSendMessage
import scala.concurrent.ExecutionContext

/**
  * 引数のチャンネルIDへメッセージを投稿する
  */
object SayCont {
  def apply(sendChannelId: String, sendMessage: String): SlackCont[Long] = SlackCont[Long] { env =>
    implicit val ec: ExecutionContext = env.ec

    k =>
      for {
        n <- env.client.sendMessage(SlackSendMessage(sendChannelId, sendMessage))
        r <- k(n)
      } yield r
  }
}
