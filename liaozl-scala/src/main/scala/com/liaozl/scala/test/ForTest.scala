package com.liaozl.scala.test

/**
  *
  * @author liaozuliang
  * @date 2017-04-10
  */
object ForTest {

  def main(args: Array[String]): Unit = {
    testFor();
  }

  def testFor(): Unit = {
    var a = 0;
    for (a <- 0 to 10) {
      println("test1: a=" + a);
    }

    var b = 0;
    for (b <- 0 until 10) {
      println("test2: b=" + b);
    }

    a = 0;
    b = 0;
    for (a <- 0 to 5; b <- 0 until 10) {
      println("test3: a=" + a + ", b=" + b);
    }

    a = 0;
    for (a <- 0 to 10; if a != 4; if a < 8) {
      println("test4: a=" + a);
    }

    var x = 0;
    var list = List(1, 2, 3, 4, 5);
    for (x <- list) {
      println("test5: x=" + x)
    }
  }
}
