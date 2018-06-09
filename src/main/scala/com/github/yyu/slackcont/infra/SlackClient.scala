package com.github.yyu.slackcont.infra

import com.github.yyu.slackcont.infra.SlackClient.{SlackEditMessage, SlackSendMessage}
import scala.concurrent.{ExecutionContext, Future}

trait SlackClient {
  /**
    * メッセージを投稿する
    *
    * @param message メッセージ
    * @param ec ExecutionContext
    * @return Future.successful(Long) メッセージのTS
    *         Future.failed(SlackApiException) 投稿に失敗した
    */
  def sendMessage(message: SlackSendMessage)(implicit ec: ExecutionContext): Future[Long]

  /**
    * メッセージを編集する
    *
    * @param message メッセージ
    * @param ec ExecutionContext
    * @return Future.successful(()) 成功
    *         Future.failed(SlackApiException) 失敗
    */
  def editMessage(message: SlackEditMessage)(implicit ec: ExecutionContext): Future[Unit]

  /**
    * タイピングを表示する
    *
    * @param channelId 表示するチャンネルID
    * @param ec ExecutionContext
    * @return Future.successful(()) 成功
    *         Future.failed(SlackApiException) 失敗
    */
  def indicateTyping(channelId: String)(implicit ec: ExecutionContext): Future[Unit]
}

object SlackClient {
  case class SlackSendMessage(
    channelId: String,
    text: String,
    threadTs: Option[String] = None
  )

  case class SlackEditMessage(
    channelId: String,
    ts: String,
    text: String
  )

  case class SlackApiException(message: String = null, cause: Throwable = null) extends Exception(message, cause)
}
