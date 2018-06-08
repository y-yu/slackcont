package com.github.yyu.slackcont.main

import com.github.yyu.slackcont.cont.impl.{AddCont, HelloWorldCont, SayCont}
import com.github.yyu.slackcont.di.DefaultModule
import com.github.yyu.slackcont.infra.SlackRunner
import play.api.inject.guice.GuiceInjectorBuilder

object Main {
  def main(args: Array[String]): Unit = {
    val injector = new GuiceInjectorBuilder()
      .bindings(new DefaultModule)
      .build()

    val slackRunner = injector.instanceOf(classOf[SlackRunner])

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
