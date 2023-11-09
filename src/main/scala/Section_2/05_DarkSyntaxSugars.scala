/*
package Lectures.Section_2

import scala.util.Try

object DarkSyntaxSugars
{
    def singleArgMethod(arg: Int): String =
    {
        s"$arg little ducks..."
    }

    val description = singleArgMethod
    {
        42
    }

    val aTryInstance = Try
    {
        throw new RuntimeException
    }

    List(1,2,3).map{x => x + 1}

    trait Action
    {
        def act(x: Int): Int
    }

    val anInstance: Action = new Action:
        override def act(x: Int): Int = x + 1

    val aFunkyInstance: Action = (x: Int) => x + 1

    val aThread = new Runnable(new Runnable):
        override def run(): Unit = println("hello, scala")

    val aSweeterThread = new Thread(() => println("sweet, scala"))

    abstract class AnAbstractType
    {
        def implemented: Int = 23
        def f(a: Int): Unit
    }

    val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")

    val test = 1 :: List(1,2,3)

    class TestingMethodeDeclarations
    {
        def -->(): Unit =
        {
            println("test")
        }
    }

    val testingMethodeDeclarations = new TestingMethodeDeclarations

    testingMethodeDeclarations.-->
}
*/
