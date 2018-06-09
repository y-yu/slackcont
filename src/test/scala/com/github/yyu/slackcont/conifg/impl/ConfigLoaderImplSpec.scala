package com.github.yyu.slackcont.conifg.impl

import com.typesafe.config.ConfigException
import org.scalatest.{MustMatchers, WordSpec}
import scala.util.Success

class ConfigLoaderImplSpec extends WordSpec {
  import MustMatchers._

  "loadString" should {
    "load config successfully" in {
      val sut = new ConfigLoaderImpl("default_ok.conf")

      sut.loadString("slack.token") must be(Success("Token"))
    }

    "return Failure if the config is invalid" in {
      val sut = new ConfigLoaderImpl("default_ng.conf")

      intercept[ConfigException.Missing]{
        sut.loadString("slack.token").get
      }
    }
  }
}
