package com.github.yyu.slackcont.infra

import com.github.yyu.slackcont.infra.SlackClient.SlackSendMessage
import scala.concurrent.Future

trait SlackClient {
  // TODO: 返り値の型が適当
  def sendMessage(message: SlackSendMessage): Future[Long]
}

object SlackClient {
  case class SlackSendMessage(
    channelId: String,
    text: String,
    threadTs: Option[String] = None
  )
}
