package com.github.yyu.slackcont.di

import akka.actor.ActorSystem
import com.github.yyu.slackcont.conifg.ConfigLoader
import com.github.yyu.slackcont.conifg.impl.ConfigLoaderImpl
import com.github.yyu.slackcont.infra.{SlackClient, SlackRunner}
import com.github.yyu.slackcont.infra.impl.{SlackClientImpl, SlackRtmClientProvider, SlackRunnerImpl}
import com.google.inject.AbstractModule
import slack.rtm.SlackRtmClient

class DefaultModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ActorSystem]).toInstance(ActorSystem("slack"))

    bind(classOf[ConfigLoader]).to(classOf[ConfigLoaderImpl])

    bind(classOf[SlackRtmClient]).toProvider(classOf[SlackRtmClientProvider])

    bind(classOf[SlackClient]).to(classOf[SlackClientImpl])

    bind(classOf[SlackRunner]).to(classOf[SlackRunnerImpl])
  }
}
