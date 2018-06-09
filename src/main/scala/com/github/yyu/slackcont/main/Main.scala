package com.github.yyu.slackcont.main

import com.github.yyu.slackcont.cont.impl.{AddCont, HelloWorldCont, SayCont, TypingCont}
import com.github.yyu.slackcont.di.DefaultModule
import com.github.yyu.slackcont.infra.SlackRunner
import com.google.inject.Guice

object Main {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new DefaultModule)

    val slackRunner = injector.getInstance(classOf[SlackRunner])

    val typingCont = injector.getInstance(classOf[TypingCont])

    slackRunner.onMessage(msg =>
      for {
        world <- HelloWorldCont(msg)
        _ <- typingCont(msg.channel, 2000)
        _ <- SayCont(msg.channel, world)
      } yield ()
    )

    slackRunner.onMessage(msg =>
      for {
        num <- AddCont(msg)
        _ <- SayCont(msg.channel, num.toString)
      } yield ()
    )
  }
}
