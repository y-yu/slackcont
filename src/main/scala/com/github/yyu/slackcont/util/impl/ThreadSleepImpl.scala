package com.github.yyu.slackcont.util.impl

import com.github.yyu.slackcont.util.ThreadSleep

class ThreadSleepImpl extends ThreadSleep {
  def sleep(msec: Long): Unit =
    Thread.sleep(msec)
}
