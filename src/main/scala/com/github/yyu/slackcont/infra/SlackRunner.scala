package com.github.yyu.slackcont.infra

import com.github.yyu.slackcont.cont.SlackCont.SlackCont
import slack.models.{Message, SlackEvent}

// これSlackのデータ構造そのまま使っているので、
// 作りなおすときはv2つくるしかない……
trait SlackRunner {
  def onMessage(f: Message => SlackCont[Unit]): Unit

  def onEvent(f: SlackEvent => SlackCont[Unit]): Unit
}
