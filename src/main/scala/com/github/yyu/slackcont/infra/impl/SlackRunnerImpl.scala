package com.github.yyu.slackcont.infra.impl

import com.github.yyu.slackcont.cont.SlackCont.{SlackCont, SlackEnv}
import com.github.yyu.slackcont.infra.provider.{ExecutionContextProvider, SlackRtmClientProvider}
import com.github.yyu.slackcont.infra.{SlackClient, SlackRunner}
import com.google.inject.Inject
import slack.rtm.SlackRtmClient
import slack.models.{Message, SlackEvent}
import scala.concurrent.{ExecutionContext, Future}

class SlackRunnerImpl @Inject() (
  slackRtmClientProvider: SlackRtmClientProvider,
  slackClient: SlackClient,
  executionContextProvider: ExecutionContextProvider
) extends SlackRunner {
  private val ec: ExecutionContext = executionContextProvider.get()

  private val client: SlackRtmClient = slackRtmClientProvider.get()

  def onMessage(f: Message => SlackCont[Unit]): Unit = {
    val env = SlackEnv(slackClient, ec)

    client.onMessage { msg =>
      f(msg)(env).run(Future.successful)
    }
  }

  override def onEvent(f: SlackEvent => SlackCont[Unit]): Unit = {
    val env = SlackEnv(slackClient, ec)

    client.onEvent { event =>
      f(event)(env).run(Future.successful)
    }
  }
}
