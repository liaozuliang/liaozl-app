package com.liaozl.scala.test

/**
  *
  * @author liaozuliang
  * @date 2017-04-10
  */
object WhileTest {

  def main(args: Array[String]): Unit = {
    testWhile();
  }

  def testWhile(): Unit = {
    var a = 10;

    while (a > 0) {
      println("a=" + a);
      a -= 1;
    }
  }

}
