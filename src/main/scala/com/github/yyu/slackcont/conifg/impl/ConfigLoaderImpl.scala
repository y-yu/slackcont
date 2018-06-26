package com.github.yyu.slackcont.conifg.impl

import com.github.yyu.slackcont.conifg.ConfigLoader
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

class ConfigLoaderImpl(defaultConfigName: String = "default.conf") extends ConfigLoader {
  val configName: String = System.getProperty("com.github.yyu.slackcont.config", defaultConfigName)

  val config: Config = ConfigFactory.load(configName)

  def loadString(key: String): Try[String] = {
    Try {
      config.getString(key)
    }
  }
}
