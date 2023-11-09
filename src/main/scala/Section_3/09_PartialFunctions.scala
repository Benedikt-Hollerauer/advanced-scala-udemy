package Section_3

object PartialFunctions extends App
{
    val aNonPartialFunction = (x: Int) => x.match
    {
        case 1 => 12
        case 2 => 13
        case 4 => 14
    }

    val aPartialFunction: PartialFunction[Int, Float] =
    {
        case 1 => 12.0
        case 2 => 13.0
        case 4 => 14.0
    }

    println(aNonPartialFunction(2))
    println(aPartialFunction(4))

    // Exercise
    //
    // 1 - construct a PF instance yourself ( anonymous class )
    // 2 - dumb chatbot as a PF

    // scala.io.Source.stdin.getLines().foreach(lines => println(s"you said $lines"))

    // 1:

    // val aPfInstance = new PartialFunction[Int, Int]
    // {
    //     case 1 => 1
    // }
    //
    // println(aPfInstance(1))

    // 2:

    // val chatBot: PartialFunction[String, String] =
    // {
    //     for
    //     {
    //         input   <-  scala.io.Source.stdin.getLines()
    //     } yield(input)
    //
    //     case "hello" => foreach(input => println(s"you said $input"))
    // }

    // Result

    // 1:

    val aPfInstanceResult = new PartialFunction[Int, Int]
    {
        override def apply(v1: Int): Int = v1.match
        {
            case 1 => 1
        }

        override def isDefinedAt(x: Int): Boolean =
            x == 1 | x == 2 | x== 3
    }

    // 2:

    val chatbotResult: PartialFunction[String, String] =
    {
        case "hello"    => "bye"
        case _          => "else"   // partitial functions can have a "all" operator but can´t have another type then the type of the partial function
    }

    scala.io.Source.stdin.getLines().map(chatbotResult).foreach(println)
    class -->[A, B](a: A, b: B)
    val test: (Int, String) = ???
    val asdfjkoasdjk: Int --> String = new -->(2, "moin")
}
