package com.liaozl.scala.test

/**
  *
  * @author liaozuliang
  * @date 2017-04-10
  */
object DowhileTest {

  def main(args: Array[String]): Unit = {
    testDowhile
  }

  def testDowhile: Any = {
    var a = 1;
    do {
      println("do while test");
    } while (a > 2);

    do {
      println("a=" + a)
      a += 1;
    } while (a < 10);
  }

}
