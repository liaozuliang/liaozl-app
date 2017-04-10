package com.liaozl.scala.test

import scala.util.control.Breaks

/**
  *
  * @author liaozuliang
  * @date 2017-04-10
  */
object BreakTest {

  def main(args: Array[String]): Unit = {
    testBreak();
  }

  def testBreak(): Unit = {
    var a = List(1, 2, 3, 4, 5, 6);
    var b = List(11, 12, 13);

    val innerLoop = new Breaks;
    val outerLoop = new Breaks;

    outerLoop.breakable {
      var i = 0;
      for (i <- a) {
        println("i=" + i)
        if (i > 4) {
          outerLoop.break;
        }

        innerLoop.breakable {
          var j = 0;
          for (j <- b) {
            println("j=" + j)
            if (j == 12) {
              innerLoop.break;
            }
          }
        }

      }

    }
  }
}
