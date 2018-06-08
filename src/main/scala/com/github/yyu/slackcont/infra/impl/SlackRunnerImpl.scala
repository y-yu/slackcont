package com.github.yyu.slackcont.infra.impl

import com.github.yyu.slackcont.cont.SlackCont.{SlackCont, SlackEnv}
import com.github.yyu.slackcont.infra.{SlackClient, SlackRunner}
import com.google.inject.Inject
import play.api.libs.concurrent.ExecutionContextProvider
import slack.rtm.SlackRtmClient
import slack.models.{Message, SlackEvent}
import scala.concurrent.{ExecutionContext, Future}

class SlackRunnerImpl @Inject() (
  slackRtmClientProvider: SlackRtmClientProvider,
  slackClient: SlackClient,
  executionContextProvider: ExecutionContextProvider
) extends SlackRunner {

  implicit val ec: ExecutionContext = executionContextProvider.get()

  val client: SlackRtmClient = slackRtmClientProvider.get()

  def onMessage(f: Message => SlackCont[Unit]): Future[Unit] = {
    val env = SlackEnv(slackClient, ec)

    client.onMessage { msg =>
      f(msg)(env).run(Future.successful)
    }
    Future.successful()
  }

  override def onEvent(f: SlackEvent => SlackCont[Unit]): Future[Unit] = {
    val env = SlackEnv(slackClient, ec)

    client.onEvent { event =>
      f(event)(env).run(Future.successful)
    }
    Future.successful()
  }
}
