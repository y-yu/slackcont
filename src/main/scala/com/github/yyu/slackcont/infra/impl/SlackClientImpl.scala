package com.github.yyu.slackcont.infra.impl

import akka.actor.ActorSystem
import com.github.yyu.slackcont.infra.SlackClient
import com.github.yyu.slackcont.infra.SlackClient.SlackSendMessage
import com.google.inject.Inject
import slack.rtm.SlackRtmClient
import sun.misc.ObjectInputFilter.Config

import scala.concurrent.Future

class SlackClientImpl @Inject() (
  slackRtmClientProvider: SlackRtmClientProvider
) extends SlackClient {
  val client: SlackRtmClient = slackRtmClientProvider.get()

  def sendMessage(message: SlackSendMessage): Future[Long] = {
    client.sendMessage(message.channelId, message.text, message.threadTs)
  }
}
