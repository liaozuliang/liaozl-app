package com.liaozl.scala.test

/**
  *
  * @author liaozuliang
  * @date 2017-04-20
  */
object MatchTest {

  def main(args: Array[String]): Unit = {
    testMatch()
  }

  def testMatch(): Unit = {
    var i = 3;

    var str = i match {
      case 1 => "i=1"
      case 2 => "i=2"
      case 3 => "i=3"
      case _ => "i!=1, i!=2, i!=3"
    }

    println(str)
  }
}
