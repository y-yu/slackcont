package com.github.yyu.slackcont.main

import com.github.yyu.slackcont.cont.impl.{AddCont, HelloWorldCont, SayCont}
import com.github.yyu.slackcont.di.DefaultModule
import com.github.yyu.slackcont.infra.SlackRunner
import com.google.inject.Guice

object Main {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new DefaultModule)

    val slackRunner = injector.getInstance(classOf[SlackRunner])

    slackRunner.onMessage(msg =>
      for {
        world <- HelloWorldCont.helloWorldCont(msg)
        _ <- SayCont.sayCont(msg.channel, world)
      } yield ()
    )

    slackRunner.onMessage(msg =>
      for {
        num <- AddCont.addCont(msg)
        _ <- SayCont.sayCont(msg.channel, num.toString)
      } yield ()
    )
  }
}
