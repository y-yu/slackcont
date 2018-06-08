package com.github.yyu.slackcont.conifg

import scala.util.Try

trait ConfigLoader {
  def loadString(key: String): Try[String]
}
