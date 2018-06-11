package com.github.yyu.slackcont.infra.impl

import com.github.yyu.slackcont.infra.SlackClient
import com.github.yyu.slackcont.infra.SlackClient.{SlackApiException, SlackEditMessage, SlackSendMessage}
import com.google.inject.{Inject, Provider}
import slack.rtm.SlackRtmClient
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class SlackClientImpl @Inject() (
  slackRtmClientProvider: Provider[SlackRtmClient],
) extends SlackClient {
  private val client: SlackRtmClient = slackRtmClientProvider.get()

  def sendMessage(message: SlackSendMessage)(implicit ec: ExecutionContext): Future[Long] =
    client
      .sendMessage(message.channelId, message.text, message.threadTs)
      .recoverWith {
        case NonFatal(e) =>
          Future.failed(SlackApiException("Fail to send a message", e))
      }

  def editMessage(message: SlackEditMessage)(implicit ec: ExecutionContext): Future[Unit] =
    Future(client.editMessage(message.channelId, message.ts, message.text))
      .recoverWith {
        case NonFatal(e) =>
          Future.failed(SlackApiException("Fail to edit a message", e))
      }

  def indicateTyping(channelId: String)(implicit ec: ExecutionContext): Future[Unit] =
    Future(client.indicateTyping(channelId))
      .recoverWith {
        case NonFatal(e) =>
          Future.failed(SlackApiException("Fail to indicate typing", e))
      }
}
