package com.github.yyu.slackcont.conifg.impl

import com.github.yyu.slackcont.conifg.ConfigLoader
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

class ConfigLoaderImpl extends ConfigLoader {
  val config: Config = ConfigFactory.load("default.conf")

  def loadString(key: String): Try[String] = {
    Try {
      config.getString(key)
    }
  }
}
