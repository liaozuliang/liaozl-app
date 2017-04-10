package com.liaozl.scala.test

/**
  *
  * @author liaozuliang
  * @date 2017-04-10
  */
object IfelseTest {

  def main(args: Array[String]): Unit = {
    testIfelse
  }

  def testIfelse: Unit = {
    var a = 1;

    if (a > 0) {
      println("a>0: " + (a > 0))
    }

    if (a <= 1) {
      println("a<=1")
    } else {
      println("a>1")
    }

    if (a == 0) {
      println("a==0: " + (a == 0))
    } else if (a == 1) {
      println("a==1: " + (a == 1))
    } else if (a == 3) {
      println("a==3: " + (a == 3))
    } else {
      println("other")
    }
  }
}
