package com.github.yyu.slackcont.infra.impl

import akka.actor.ActorSystem
import com.github.yyu.slackcont.conifg.ConfigLoader
import com.google.inject.{Provider, Singleton}
import javax.inject.Inject
import slack.rtm.SlackRtmClient
import scala.concurrent.duration._

@Singleton
class SlackRtmClientProvider @Inject()(
  configLoader: ConfigLoader
)(
  implicit actorSystem: ActorSystem
) extends Provider[SlackRtmClient] {

  val token: String = configLoader.loadString("slack.token").get
  val duration: FiniteDuration = configLoader.loadString("slack.duration").map(d => FiniteDuration.apply(d.toLong, MINUTES)).getOrElse(5 minutes)

  val client = new SlackRtmClient(token, duration)

  override def get(): SlackRtmClient = client
}
