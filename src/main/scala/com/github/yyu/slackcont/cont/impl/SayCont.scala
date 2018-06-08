package com.github.yyu.slackcont.cont.impl

import com.github.yyu.slackcont.cont.SlackCont
import com.github.yyu.slackcont.cont.SlackCont.SlackCont
import com.github.yyu.slackcont.infra.SlackClient.SlackSendMessage

/**
  * 引数のチャンネルIDへメッセージを投稿する
  */
object SayCont {
  def sayCont(sendChannelId: String, sendMessage: String): SlackCont[Unit] = SlackCont[Unit] { env =>
    k =>
      env.client.sendMessage(SlackSendMessage(sendChannelId, sendMessage))
      k(())
  }
}
