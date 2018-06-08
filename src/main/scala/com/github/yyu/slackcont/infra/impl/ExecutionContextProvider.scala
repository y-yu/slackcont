package com.github.yyu.slackcont.infra.impl

import akka.actor.ActorSystem
import com.google.inject.{Inject, Provider, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ExecutionContextProvider @Inject()(
  actorSystem: ActorSystem
) extends Provider[ExecutionContext] {
  val ec: ExecutionContext = actorSystem.dispatcher

  def get(): ExecutionContext = ec
}
