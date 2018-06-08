package com.github.yyu.slackcont.cont.impl

import com.github.yyu.slackcont.cont.SlackCont
import com.github.yyu.slackcont.cont.SlackCont.SlackCont
import slack.models.Message
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

/**
  * "x + y"の式を評価し、その足し算結果を継続へ渡す
  */
object AddCont {
  private val addRegex: Regex = """(\d+) ?\+ ?(\d+)""".r

  def addCont(message: Message): SlackCont[Int] = SlackCont[Int] { env =>
    implicit val ec: ExecutionContext = env.ec

    k =>
      message.text match {
        case addRegex(l, r) =>
          Try(l.toInt + r.toInt) match {
            case Success(n) => k(n)
            case Failure(e) => Future.failed(e)
          }

        case _ =>
          Future.successful(())
      }
  }
}
