package com.github.yyu.slackcont.cont.impl

import com.github.yyu.slackcont.cont.SlackCont
import com.github.yyu.slackcont.cont.SlackCont.SlackCont
import com.github.yyu.slackcont.util.ThreadSleep
import com.google.inject.Inject
import scala.concurrent.ExecutionContext

/**
  * 継続前にSlackのメッセージ入力中（"typing"）として与えられたミリ秒だけ待機する
  */
class TypingCont @Inject()(
  threadSleep: ThreadSleep
) {
  def apply(channelId: String, msec: Long): SlackCont[Unit] = SlackCont { env =>
    implicit val ec: ExecutionContext = env.ec

    k =>
      env.client.indicateTyping(channelId) // 失敗を無視する
      threadSleep.sleep(msec)
      k(())
  }
}
